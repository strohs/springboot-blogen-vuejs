package com.blogen.services.security;

/**
 * Service for Encrypting and checking passwords
 *
 * @author Cliff
 */
public interface PasswordEncryptionService {

    String encrypt(String input);

    /**
     * @param plainPassword - the plain text password
     * @param encryptedPassword - the encrypted version of the plainPassword
     * @return - true if plainPassword matches encrypted password, otherwise returns false
     */
    boolean checkPassword(String plainPassword, String encryptedPassword);
}
