/*
 */
package model;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Manage salting and hashing of passwords.
 *
 * @author Michael Schneider <michael.schneider@bbbaden.ch>
 */
public class Password {

    /**
     * Number of iterations the password is hashed.
     */
    public static final int ITERATIONS = 25000;

    /**
     * Length of key: 512 Bits == 64 Bytes
     */
    public static final int KEY_LENGTH = 512;

    private final static Logger LOGGER = Logger.getLogger(Password.class.getName());

    /**
     * Returns random bits usable as salt.
     *
     * @param length Number of bytes in the salt.
     * @return The array of bytes usable as salt.
     */
    public static byte[] getRandomSalt(int length) {
        final byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Hashes the password multiple times including the salt.
     *
     * @param password The password in clear-text.
     * @param salt The salt to be used.
     * @return The salted and hashed password.
     */
    public static byte[] hashPassword(final String password, final byte[] salt) {

        try {
            final SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            final PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            final SecretKey key = skf.generateSecret(spec);
            byte[] res = key.getEncoded();
            return res;

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.log(Level.SEVERE, "BBB InsecureApp password hashing problem, giving up.", e);
            throw new RuntimeException(e);
        }
    }
}
