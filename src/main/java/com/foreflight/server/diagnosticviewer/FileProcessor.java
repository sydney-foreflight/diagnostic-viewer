package com.foreflight.server.diagnosticviewer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileProcessor {

    private static String filePath;
    private static String destName;

    public FileProcessor(String filePath, String destName) {
        this.filePath = filePath;
        this.destName = destName;
    }





    /* Start of methods from https://www.baeldung.com/java-compress-and-uncompress
     * Unzipping a compressed file and saving the files to destName.
     */
    private static void unZip() throws IOException {
        String fileZip = filePath;
        File destDir = new File(destName);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while(zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if(zipEntry.isDirectory()) {
                if(!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                File parent = newFile.getParentFile();
                if(!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to add child because parent directory not created - " + parent);
                } FileOutputStream fos = new FileOutputStream(newFile);
                int length;
                while((length = zis.read(buffer)) > 0) { fos.write(buffer, 0, length); }
                fos.close();
            } zipEntry = zis.getNextEntry();
        } zis.closeEntry();
        zis.close();
    }

    private static File newFile(File destDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destDir, zipEntry.getName());
        String destDirPath = destDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();
        if(!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        } return destFile;
    }
    /* End of methods from https://www.baeldung.com/java-compress-and-uncompress */
}

