package poc.sa.ms.helper;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordHelper {
    /**
     * Verifies a login attempt against a password from the data store
     *
     * @param attemptedPassword The unencrypted password used by the user when logging into the service
     * @param encryptedPassword The encrypted password from the data store
     * @param salt              The salt for the encrypted password from the data store
     * @return True if the passwords match, false otherwise.
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean authenticate(String attemptedPassword, byte[] encryptedPassword, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        // Encrypt the clear-text password using the same salt that was used to
        // encrypt the original password
        byte[] encryptedAttemptedPassword = getEncryptedPassword(attemptedPassword, salt);

        // Authentication succeeds if encrypted password that the user entered
        // is equal to the stored hash
        return Arrays.equals(encryptedPassword, encryptedAttemptedPassword);
    }

    /**
     * Encrypts the given password with a salt
     *
     * @param password The password string to be encrypted
     * @param salt     A randomly generated salt for the password encryption
     * @return The byte[] containing the encrypted password
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static byte[] getEncryptedPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String algorithm = "PBKDF2WithHmacSHA1";
        int derivedKeyLength = 160;
        // The NIST recommends at least 1,000 iterations:
        // http://csrc.nist.gov/publications/nistpubs/800-132/nist-sp800-132.pdf
        int iterations = 20000;

        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, derivedKeyLength);

        SecretKeyFactory f = SecretKeyFactory.getInstance(algorithm);

        return f.generateSecret(spec).getEncoded();
    }

    /**
     * Generates a random 8 byte salt using the SecureRandom with the SHA1PRNG
     *
     * @return The generated salt
     * @throws NoSuchAlgorithmException
     */
    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

        byte[] salt = new byte[8];
        random.nextBytes(salt);

        return salt;
    }
}

