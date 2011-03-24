package com.baleksan.util.compression;

import java.util.Arrays;

/**
 * @author <a href="mailto:baleksan@yammer-inc.com" boris/>
 */
public class ByteArrayWrapper {
    private byte[] byteArray;

    public ByteArrayWrapper(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public byte[] getBytes() {
        return byteArray;
    }

    public int length() {
        return byteArray.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ByteArrayWrapper that = (ByteArrayWrapper) o;

        return Arrays.equals(byteArray, that.byteArray);

    }

    @Override
    public int hashCode() {
        return byteArray != null ? Arrays.hashCode(byteArray) : 0;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(byteArray.length);
        builder.append(" bytes");

        return builder.toString();
    }
}
    
