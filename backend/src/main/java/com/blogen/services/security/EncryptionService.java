package com.blogen.services.security;

/**
 * Service for Encrypting and checking passwords
 *
 * @author Cliff
 */
public interface EncryptionService {

    String encrypt( String input );

    /**
     *
     * @param plainPassword
     * @param encryptedPassword
     * @return - true if plainPassword matches encrypted password, otherwise returns false
     */
    boolean checkPassword( String plainPassword, String encryptedPassword );
}
