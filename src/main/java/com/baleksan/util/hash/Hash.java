package com.baleksan.util.hash;

import com.baleksan.util.conf.ConfigurationWrapper;

/**
 * This class represents a common API for hashing functions. Lifted with
 * modifications from the hadoop/commons project.
 */

public abstract class Hash {
    protected static final int SHIFT_1B = 8;
    protected static final int SHIFT_2B = 8 * 2;
    protected static final int SHIFT_3B = 8 * 3;
    protected static final int SHIFT_4B = 8 * 4;
    protected static final int SHIFT_5B = 8 * 5;
    protected static final int SHIFT_6B = 8 * 6;
    protected static final int SHIFT_7B = 8 * 7;

    /**
     * Constant to denote invalid hash type.
     */
    public static final int INVALID_HASH = -1;
    /**
     * Constant to denote {@link JenkinsHash}.
     */
    public static final int JENKINS_HASH = 0;
    /**
     * Constant to denote {@link MurmurHash}.
     */
    public static final int MURMUR_HASH = 1;

    /**
     * This utility method converts the name of the configured hash type to a
     * symbolic constant.
     *
     * @param conf configuration
     * @return one of the predefined constants
     */
    public static int getHashType(final ConfigurationWrapper conf) {
        String name = conf.getProperty("util.hash");
        return parseHashType(name);
    }

    /**
     * Get a singleton instance of hash function of a given type.
     *
     * @param type predefined hash type
     * @return hash function instance, or null if type is invalid
     */
    public static Hash getInstance(final int type) {
        switch (type) {
            case JENKINS_HASH:
                return JenkinsHash.getInstance();
            case MURMUR_HASH:
                return MurmurHash.getInstance();
            default:
                return null;
        }
    }

    /**
     * Get a singleton instance of hash function of a type defined in the
     * configuration.
     *
     * @param conf current configuration
     * @return defined hash type, or null if type is invalid
     */
    public static Hash getInstance(final ConfigurationWrapper conf) {
        int type = getHashType(conf);
        return getInstance(type);
    }

    /**
     * This utility method converts String representation of hash function name
     * to a symbolic constant. Currently two function types are supported,
     * "jenkins" and "murmur".
     *
     * @param name hash function name
     * @return one of the predefined constants
     */
    public static int parseHashType(final String name) {
        if ("jenkins".equalsIgnoreCase(name)) {
            return JENKINS_HASH;
        } else if ("murmur".equalsIgnoreCase(name)) {
            return MURMUR_HASH;
        } else {
            return INVALID_HASH;
        }
    }

    /**
     * Calculate a hash using all bytes from the input argument, and a seed of
     * -1.
     *
     * @param bytes input bytes
     * @return hash value
     */
    public int hash(final byte[] bytes) {
        return hash(bytes, bytes.length, -1);
    }

    /**
     * Calculate a hash using all bytes from the input argument, and a provided
     * seed value.
     *
     * @param bytes   input bytes
     * @param initval seed value
     * @return hash value
     */
    public int hash(final byte[] bytes, final int initval) {
        return hash(bytes, bytes.length, initval);
    }

    /**
     * Calculate a hash using bytes from 0 to <code>length</code>, and the
     * provided seed value.
     *
     * @param bytes   input bytes
     * @param length  length of the valid bytes to consider
     * @param initval seed value
     * @return hash value
     */
    public abstract int hash(byte[] bytes, int length, int initval);

    /**
     * Calculate hash given a long value as an input. Used to randomize
     * sequential numbers.
     *
     * @param value input
     * @return hash value
     */
    public int hash(final long value) {
        byte[] bytes =
                new byte[]{
                        (byte) (value >>> SHIFT_7B),
                        (byte) (value >>> SHIFT_6B),
                        (byte) (value >>> SHIFT_5B),
                        (byte) (value >>> SHIFT_4B),
                        (byte) (value >>> SHIFT_3B),
                        (byte) (value >>> SHIFT_2B),
                        (byte) (value >>> SHIFT_1B), (byte) (value)
                };

        return hash(bytes);
    }
}
