package com.blogen.services.security;

/**
 * Service for Encrypting and checking passwords
 *
 * @author Cliff
 */
public interface EncryptionService {

    String encrypt( String input );

    boolean checkPassword( String plainPassword, String encryptedPassword );
}
