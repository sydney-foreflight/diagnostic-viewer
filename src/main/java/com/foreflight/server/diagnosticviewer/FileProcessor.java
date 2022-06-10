/* FileProcessor.java unzips a file and stores the file in the user-given directory.
   Author: Sydney Thompson
   Date: 06/09/22
 */

package com.foreflight.server.diagnosticviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileProcessor {

    private final String filePath;
    private final String destName;
    private ArrayList<File> filesIncluded;
    private ArrayList<Directory> directoriesIncluded;

    public FileProcessor(String filePath, String destName) throws IOException {
        this.filePath = filePath;
        this.destName = destName;
        filesIncluded = new ArrayList<>();
        directoriesIncluded = new ArrayList<>();
        unZip();
    }

    public String getAllFiles() {
        String output = "files: \n";
        for (int i = 0; i < filesIncluded.size(); i++) {
            output += "\t" + filesIncluded.get(i).getName() + "\n";
        }
        for (int i = 0; i < directoriesIncluded.size(); i++) {
            Directory current = directoriesIncluded.get(i);
            output += "\t" + current.name;
            for (int j = 0; j < current.files.size(); j++) {
                output += "\n\t\t" + current.files.get(j).getName();
            }
            output += "\n";
        }
        return output;
    }

    public ArrayList<File> getFilesIncluded() {
        return filesIncluded;
    }

    public ArrayList<Directory> getDirectoriesIncluded() {
        return directoriesIncluded;
    }

    /* Start of methods from https://www.baeldung.com/java-compress-and-uncompress + some integrated data structures
     * Unzipping a compressed file and saving the files to destName.
     */
    private void unZip() throws IOException {
        String fileZip = filePath;
        File destDir = new File(destName);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
                if (!directoriesIncluded.contains(newFile)) {
                    directoriesIncluded.add(new Directory(newFile.getName()));
                }
            } else {
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to add child because parent directory not created - " + parent);
                }
                FileOutputStream fos = new FileOutputStream(newFile);
                if (parent.isDirectory() && !parent.getName().equals(destDir.getName())) {
                    getDirectory(parent.getName()).addFile(newFile);
                } else {
                    filesIncluded.add(newFile);
                }
                int length;
                while ((length = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destDir, zipEntry.getName());
        String destDirPath = destDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return destFile;
    }
    /* End of methods from https://www.baeldung.com/java-compress-and-uncompress */

    private Directory getDirectory(String name) {
        for (int i = 0; i < directoriesIncluded.size(); i++) {
            if (directoriesIncluded.get(i).name.equals(name)) {
                return directoriesIncluded.get(i);
            }
        }
        Directory newDirectory = new Directory(name);
        return newDirectory;
    }

    class Directory {
        private String name;
        private ArrayList<File> files;

        private Directory(String name) {
            this.name = name;
            files = new ArrayList<>();
        }

        private void addFile(File newFile) {
            files.add(newFile);
        }

        public ArrayList<File> getFiles() {
            return files;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Directory directory = (Directory) o;
            return name.equals(directory.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }

}

