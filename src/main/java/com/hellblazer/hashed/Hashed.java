package com.hellblazer.hashed;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Set;
import java.util.Stack;

/**
 * @author hal.hildebrand
 **/
public class Hashed {

    private static final Path REPOSITORY_ROOT = Path.of(".", "mvn-artifact");

    // Run through the repo, generating MD5 files if they do not exist where they should
    public static void main(String[] argv) throws Exception {
        Stack<Path> directories = new Stack<>();
        directories.addAll(process(REPOSITORY_ROOT));
        while (!directories.isEmpty()) {
            var next = directories.pop();
            directories.addAll(process(next));
        }
    }

    public static Set<Path> process(Path dir) throws Exception {
        System.out.println("Processing directory: " + REPOSITORY_ROOT.relativize(dir).getFileName());
        var fileSet = new HashSet<Path>();
        var directories = new HashSet<Path>();
        try (var stream = Files.newDirectoryStream(dir)) {
            for (Path path : stream) {
                if (Files.isDirectory(path)) {
                    directories.add(path);
                } else {
                    fileSet.add(path);
                }
            }
        }
        for (var file : fileSet) {
            var fn = file.getFileName().toString();
            if (fn.startsWith(".") || fn.endsWith(".md5") || fn.endsWith(".sha1") || fn.endsWith(".lastUpdated")
            || fn.endsWith(".repositories")) {
                continue;
            }
            writeMd5(file);
            writeSha1(file);
        }
        return directories;
    }

    public static void writeMd5(final Path baseFile) throws NoSuchAlgorithmException, IOException {
        System.out.println("MD5: " + REPOSITORY_ROOT.relativize(baseFile).toString());
        final FileInputStream fis = new FileInputStream(baseFile.toFile());

        final MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.reset();
        final byte[] buffer = new byte[1024];
        int size = fis.read(buffer, 0, 1024);
        while (size >= 0) {
            messageDigest.update(buffer, 0, size);
            size = fis.read(buffer, 0, 1024);
        }

        final String result = HexFormat.of().formatHex(messageDigest.digest());

        fis.close();

        final Path md5File = baseFile.getFileSystem().getPath(baseFile.toString() + ".md5");

        try (var fos = new FileOutputStream(md5File.toFile()); var bais = new ByteArrayInputStream(result.getBytes())) {
            var buff = new byte[1024];
            for (int read = bais.read(buff); read >= 0; read = bais.read(buff)) {
                fos.write(buff, 0, read);
            }
        }
    }

    public static void writeSha1(final Path baseFile) throws NoSuchAlgorithmException, IOException {
        System.out.println("SHA1: " + REPOSITORY_ROOT.relativize(baseFile).toString());
        final FileInputStream fis = new FileInputStream(baseFile.toFile());

        final MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.reset();
        final byte[] buffer = new byte[1024];
        int size = fis.read(buffer, 0, 1024);
        while (size >= 0) {
            messageDigest.update(buffer, 0, size);
            size = fis.read(buffer, 0, 1024);
        }

        final String result = HexFormat.of().formatHex(messageDigest.digest());

        fis.close();

        final Path md5File = baseFile.getFileSystem().getPath(baseFile.toString() + ".sha1");

        try (var fos = new FileOutputStream(md5File.toFile()); var bais = new ByteArrayInputStream(result.getBytes())) {
            var buff = new byte[1024];
            for (int read = bais.read(buff); read >= 0; read = bais.read(buff)) {
                fos.write(buff, 0, read);
            }
        }
    }
}
