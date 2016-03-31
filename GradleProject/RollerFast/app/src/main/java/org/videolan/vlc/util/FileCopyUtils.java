/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.videolan.vlc.util;

import java.io.*;

//CHECKSTYLE:OFF

/**
 * Simple utility methods for file and stream copying. All copy methods use a
 * block size of 4096 bytes, and close all affected streams when done.
 * 
 * <p>
 * Mainly for use within the framework, but also useful for application code.
 * 
 * @author Juergen Hoeller
 * @since 1.0
 */
public abstract class FileCopyUtils {

    private static final int BUFFER_SIZE = 4096;

    // ---------------------------------------------------------------------
    // Copy methods for java.io.File
    // ---------------------------------------------------------------------

    /**
     * Copy the contents of the given input File to the given output File.
     * 
     * @param in
     *            the file to copy from
     * @param out
     *            the file to copy to
     * @return the number of bytes copied
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static int copy(File in, File out) throws IOException { // NO_UCD
                                                                   // (unused
                                                                   // code)
        return copy(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(
                new FileOutputStream(out)));
    }

    /**
     * Copy the contents of the given byte array to the given output File.
     *
     * @param in
     *            the byte array to copy from
     * @param out
     *            the file to copy to
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static void copy(byte[] in, File out) throws IOException {
        ByteArrayInputStream inStream = new ByteArrayInputStream(in);
        OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
        copy(inStream, outStream);
    }

    /**
     * Copy the contents of the given input File into a new byte array.
     *
     * @param in
     *            the file to copy from
     * @return the new byte array that has been copied to
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static byte[] copyToByteArray(File in) throws IOException { // NO_UCD
                                                                       // (unused
                                                                       // code)
        return copyToByteArray(new BufferedInputStream(new FileInputStream(in)));
    }

    // ---------------------------------------------------------------------
    // Copy methods for java.io.InputStream / java.io.OutputStream
    // ---------------------------------------------------------------------

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * Closes both streams when done.
     *
     * @param in
     *            the stream to copy from
     * @param out
     *            the stream to copy to
     * @return the number of bytes copied
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        try {
            int byteCount = 0;
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Copy the contents of the given byte array to the given OutputStream.
     * Closes the stream when done.
     *
     * @param in
     *            the byte array to copy from
     * @param out
     *            the OutputStream to copy to
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static void copy(byte[] in, OutputStream out) throws IOException { // NO_UCD
                                                                              // (unused
                                                                              // code)
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Copy the contents of the given InputStream into a new byte array. Closes
     * the stream when done.
     *
     * @param in
     *            the stream to copy from
     * @return the new byte array that has been copied to
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static byte[] copyToByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
        copy(in, out);
        return out.toByteArray();
    }

    // ---------------------------------------------------------------------
    // Copy methods for java.io.Reader / java.io.Writer
    // ---------------------------------------------------------------------

    /**
     * Copy the contents of the given Reader to the given Writer. Closes both
     * when done.
     *
     * @param in
     *            the Reader to copy from
     * @param out
     *            the Writer to copy to
     * @return the number of characters copied
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static int copy(Reader in, Writer out) throws IOException { // NO_UCD
                                                                       // (use
                                                                       // private)
        try {
            int byteCount = 0;
            char[] buffer = new char[BUFFER_SIZE];
            int bytesRead = -1;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
            return byteCount;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Copy the contents of the given String to the given output Writer. Closes
     * the write when done.
     *
     * @param in
     *            the String to copy from
     * @param out
     *            the Writer to copy to
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static void copy(String in, Writer out) throws IOException { // NO_UCD
                                                                        // (unused
                                                                        // code)
        try {
            out.write(in);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
            }
        }
    }

    /**
     * Copy the contents of the given Reader into a String. Closes the reader
     * when done.
     *
     * @param in
     *            the reader to copy from
     * @return the String that has been copied to
     * @throws java.io.IOException
     *             in case of I/O errors
     */
    public static String copyToString(Reader in) throws IOException {
        StringWriter out = new StringWriter();
        copy(in, out);
        return out.toString();
    }

    /**
     * Copy data from a source stream to destFile. Return true if succeed,
     * return false if failed.
     * 
     * @param inputStream
     *            source file inputstream
     * @param destFile
     *            destFile
     * 
     * @return success return true
     */
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Copy data from a source string to destFile. Return true if succeed,
     * return false if failed.
     * 
     * @param in
     *            source file inputstream
     * @param destFile
     *            destFile
     * 
     * @return success return true
     */
    public static boolean copyToFile(String in, File destFile) {
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = in.getBytes();
                out.write(buffer);
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.close();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } 
    //CHECKSTYLE:ON
}
