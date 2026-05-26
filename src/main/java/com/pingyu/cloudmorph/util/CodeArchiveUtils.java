package com.pingyu.cloudmorph.util;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 代码包打包工具。
 */
public final class CodeArchiveUtils {

    private CodeArchiveUtils() {
    }

    public static File zipDirectory(File sourceDir, File targetZipFile) {
        if (sourceDir == null || !sourceDir.exists() || !sourceDir.isDirectory()) {
            throw new IllegalArgumentException("sourceDir invalid");
        }
        FileUtil.touch(targetZipFile);
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(targetZipFile)))) {
            zipDirectoryRecursive(sourceDir, sourceDir.getName(), zipOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("打包失败: " + e.getMessage(), e);
        }
        return targetZipFile;
    }

    private static void zipDirectoryRecursive(File file, String entryName, ZipOutputStream zipOutputStream) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null || files.length == 0) {
                zipOutputStream.putNextEntry(new ZipEntry(entryName + "/"));
                zipOutputStream.closeEntry();
                return;
            }
            for (File child : files) {
                zipDirectoryRecursive(child, entryName + "/" + child.getName(), zipOutputStream);
            }
            return;
        }
        zipOutputStream.putNextEntry(new ZipEntry(entryName));
        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(file))) {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, len);
            }
        }
        zipOutputStream.closeEntry();
    }
}
