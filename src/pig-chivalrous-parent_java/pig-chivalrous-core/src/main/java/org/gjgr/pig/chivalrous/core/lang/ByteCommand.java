package org.gjgr.pig.chivalrous.core.lang;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author gwd
 * @Time 10-29-2018 Monday
 * @Description: org.gjgr.pig.chivalrous.core:
 * @Target:
 * @More:
 */
public final class ByteCommand {
    private ByteCommand() {
    }

    public static short readShort(byte[] bytes, int offset) {
        return (short) ((bytes[offset] << 8) | (bytes[offset + 1] & 0xff));
    }

    public static int read2ByteInt(byte[] bytes, int offset) {
        int i = 0;
        i |= bytes[offset] & 0xFF;
        i <<= 8;
        i |= bytes[offset + 1] & 0xFF;
        return i;

    }

    public static int readInt(byte[] bytes, int offset) {
        return (((bytes[offset + 0] & 0xff) << 24) | ((bytes[offset + 1] & 0xff) << 16)
                | ((bytes[offset + 2] & 0xff) << 8) | (bytes[offset + 3] & 0xff));
    }

    public static long readUnsignedInt(byte[] bytes, int offset) {
        return (((bytes[offset + 0] & 0xffL) << 24) | ((bytes[offset + 1] & 0xffL) << 16)
                | ((bytes[offset + 2] & 0xffL) << 8) | (bytes[offset + 3] & 0xffL));
    }

    public static long readLong(byte[] bytes, int offset) {
        return (((long) (bytes[offset + 0] & 0xff) << 56) | ((long) (bytes[offset + 1] & 0xff) << 48)
                | ((long) (bytes[offset + 2] & 0xff) << 40) | ((long) (bytes[offset + 3] & 0xff) << 32)
                | ((long) (bytes[offset + 4] & 0xff) << 24) | ((long) (bytes[offset + 5] & 0xff) << 16)
                | ((long) (bytes[offset + 6] & 0xff) << 8) | ((long) bytes[offset + 7] & 0xff));
    }

    public static long readBytes(byte[] bytes, int offset, int numBytes) {
        int shift = 0;
        long value = 0;
        for (int i = offset + numBytes - 1; i >= offset; i--) {
            value |= (bytes[i] & 0xFFL) << shift;
            shift += 8;
        }
        return value;
    }

    public static void writeShort(byte[] bytes, short value, int offset) {
        bytes[offset] = (byte) (0xFF & (value >> 8));
        bytes[offset + 1] = (byte) (0xFF & value);
    }

    public static void writeInt(byte[] bytes, int value, int offset) {
        bytes[offset] = (byte) (0xFF & (value >> 24));
        bytes[offset + 1] = (byte) (0xFF & (value >> 16));
        bytes[offset + 2] = (byte) (0xFF & (value >> 8));
        bytes[offset + 3] = (byte) (0xFF & value);
    }

    public static void writeLong(byte[] bytes, long value, int offset) {
        bytes[offset] = (byte) (0xFF & (value >> 56));
        bytes[offset + 1] = (byte) (0xFF & (value >> 48));
        bytes[offset + 2] = (byte) (0xFF & (value >> 40));
        bytes[offset + 3] = (byte) (0xFF & (value >> 32));
        bytes[offset + 4] = (byte) (0xFF & (value >> 24));
        bytes[offset + 5] = (byte) (0xFF & (value >> 16));
        bytes[offset + 6] = (byte) (0xFF & (value >> 8));
        bytes[offset + 7] = (byte) (0xFF & value);
    }

    public static void writeBytes(byte[] bytes, long value, int offset, int numBytes) {
        int shift = 0;
        for (int i = offset + numBytes - 1; i >= offset; i--) {
            bytes[i] = (byte) (0xFF & (value >> shift));
            shift += 8;
        }
    }

    public static byte[] byteArrayConcat(byte[]...args) {
        int fulllength = 0;
        for (byte[] arrItem : args) {
            fulllength += arrItem.length;
        }

        byte[] retArray = new byte[fulllength];
        int start = 0;
        for (byte[] arrItem : args) {
            System.arraycopy(arrItem, 0, retArray, start, arrItem.length);
            start += arrItem.length;
        }
        return retArray;
    }

    public static byte[] intToByteArray(int number) {
        int temp = number;
        byte[] b = new byte[4];
        for (int i = b.length - 1; i > -1; i--) {
            b[i] = Integer.valueOf(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return b;
    }

    /**
     * Concatenate the given {@code byte} arrays into one, with overlapping array elements included twice.
     * <p />
     * The order of elements in the original arrays is preserved.
     *
     * @param array1 the first array.
     * @param array2 the second array.
     * @return the new array.
     */
    public static byte[] concat(byte[] array1, byte[] array2) {

        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);

        return result;
    }

    /**
     * Concatenate the given {@code byte} arrays into one, with overlapping array elements included twice. Returns a
     * new, empty array if {@code arrays} was empty and returns the first array if {@code arrays} contains only a single
     * array.
     * <p />
     * The order of elements in the original arrays is preserved.
     *
     * @param arrays the arrays.
     * @return the new array.
     */
    public static byte[] concatAll(byte[]...arrays) {

        if (arrays.length == 0) {
            return new byte[] {};
        }
        if (arrays.length == 1) {
            return arrays[0];
        }

        byte[] cur = concat(arrays[0], arrays[1]);
        for (int i = 2; i < arrays.length; i++) {
            cur = concat(cur, arrays[i]);
        }
        return cur;
    }

    /**
     * Split {@code source} into partitioned arrays using delimiter {@code c}.
     *
     * @param source the source array.
     * @param c delimiter.
     * @return the partitioned arrays.
     */
    public static byte[][] split(byte[] source, int c) {

        if (ObjectCommand.isEmpty(source)) {
            return new byte[][] {};
        }

        List<byte[]> bytes = new ArrayList<>();
        int offset = 0;
        for (int i = 0; i <= source.length; i++) {

            if (i == source.length) {

                bytes.add(Arrays.copyOfRange(source, offset, i));
                break;
            }

            if (source[i] == c) {
                bytes.add(Arrays.copyOfRange(source, offset, i));
                offset = i + 1;
            }
        }
        return bytes.toArray(new byte[bytes.size()][]);
    }

    /**
     * Merge multiple {@code byte} arrays into one array
     *
     * @param firstArray must not be {@literal null}
     * @param additionalArrays must not be {@literal null}
     * @return
     */
    public static byte[][] mergeArrays(byte[] firstArray, byte[]...additionalArrays) {

        AssertCommand.notNull(firstArray, "first array must not be null");
        AssertCommand.notNull(additionalArrays, "additional arrays must not be null");

        byte[][] result = new byte[additionalArrays.length + 1][];
        result[0] = firstArray;
        System.arraycopy(additionalArrays, 0, result, 1, additionalArrays.length);

        return result;
    }

    /**
     * Extract a byte array from {@link ByteBuffer} without consuming it.
     *
     * @param byteBuffer must not be {@literal null}.
     * @return
     * @since 2.0
     */
    public static byte[] getBytes(ByteBuffer byteBuffer) {

        AssertCommand.notNull(byteBuffer, "ByteBuffer must not be null!");

        ByteBuffer duplicate = byteBuffer.duplicate();
        byte[] bytes = new byte[duplicate.remaining()];
        duplicate.get(bytes);
        return bytes;
    }

    /**
     * Tests if the {@code haystack} starts with the given {@code prefix}.
     *
     * @param haystack the source to scan.
     * @param prefix the prefix to find.
     * @return {@literal true} if {@code haystack} at position {@code offset} starts with {@code prefix}.
     * @see #startsWith(byte[], byte[], int)
     * @since 1.8.10
     */
    public static boolean startsWith(byte[] haystack, byte[] prefix) {
        return startsWith(haystack, prefix, 0);
    }

    /**
     * Tests if the {@code haystack} beginning at the specified {@code offset} starts with the given {@code prefix}.
     *
     * @param haystack the source to scan.
     * @param prefix the prefix to find.
     * @param offset the offset to start at.
     * @return {@literal true} if {@code haystack} at position {@code offset} starts with {@code prefix}.
     * @since 1.8.10
     */
    public static boolean startsWith(byte[] haystack, byte[] prefix, int offset) {

        int to = offset;
        int prefixOffset = 0;
        int prefixLength = prefix.length;

        if ((offset < 0) || (offset > haystack.length - prefixLength)) {
            return false;
        }

        while (--prefixLength >= 0) {
            if (haystack[to++] != prefix[prefixOffset++]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Searches the specified array of bytes for the specified value. Returns the index of the first matching value in
     * the {@code haystack}s natural order or {@code -1} of {@code needle} could not be found.
     *
     * @param haystack the source to scan.
     * @param needle the value to scan for.
     * @return index of first appearance, or -1 if not found.
     * @since 1.8.10
     */
    public static int indexOf(byte[] haystack, byte needle) {

        for (int i = 0; i < haystack.length; i++) {
            if (haystack[i] == needle) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Convert a {@link String} into a {@link ByteBuffer} using {@link java.nio.charset.StandardCharsets#UTF_8}.
     *
     * @param theString must not be {@literal null}.
     * @return
     * @since 2.1
     */
    public static ByteBuffer getByteBuffer(String theString) {
        return getByteBuffer(theString, StandardCharsets.UTF_8);
    }

    /**
     * Convert a {@link String} into a {@link ByteBuffer} using the given {@link Charset}.
     *
     * @param theString must not be {@literal null}.
     * @param charset must not be {@literal null}.
     * @return
     * @since 2.1
     */
    public static ByteBuffer getByteBuffer(String theString, Charset charset) {

        AssertCommand.notNull(theString, "The String must not be null!");
        AssertCommand.notNull(charset, "The String must not be null!");

        return charset.encode(theString);
    }

    /**
     * Extract/Transfer bytes from the given {@link ByteBuffer} into an array by duplicating the buffer and fetching its
     * content.
     *
     * @param buffer must not be {@literal null}.
     * @return the extracted bytes.
     * @since 2.1
     */
    public static byte[] extractBytes(ByteBuffer buffer) {

        ByteBuffer duplicate = buffer.duplicate();
        byte[] bytes = new byte[duplicate.remaining()];
        duplicate.get(bytes);

        return bytes;
    }
}
