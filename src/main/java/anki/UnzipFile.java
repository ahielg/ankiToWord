package anki;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipFile {

    public static File unzip(String fileZip) throws IOException {
        File newFile = null;

        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
        ZipEntry zipEntry = zis.getNextEntry();
        while (zipEntry != null) {

            if (zipEntry.getName().endsWith(".anki21")
                    || (zipEntry.getName().endsWith(".anki2") && (newFile == null))) {
                newFile = writeFile(buffer, zis);
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
        //System.out.println(newFile.getAbsoluteFile().getName());
        return newFile;
    }

    private static File writeFile(byte[] buffer, ZipInputStream zis) throws IOException {
        File newFile;
        newFile = File.createTempFile("anki", ".sqlite");

        // write file content
        FileOutputStream fos = new FileOutputStream(newFile);
        int len;
        while ((len = zis.read(buffer)) > 0) {
            fos.write(buffer, 0, len);
        }
        fos.close();
        return newFile;
    }

}