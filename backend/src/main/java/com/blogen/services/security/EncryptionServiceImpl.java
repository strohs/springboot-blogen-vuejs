package com.blogen.services.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service for encrypting and checking passwords
 *
 * @author Cliff
 */
@Service
public class EncryptionServiceImpl implements EncryptionService {


    private PasswordEncoder passwordEncoder;

    @Autowired
    public EncryptionServiceImpl( PasswordEncoder passwordEncoder ) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public String encrypt( String input ) {
        return passwordEncoder.encode( input );
    }

    @Override
    public boolean checkPassword( String plainPassword, String encryptedPassword ) {
        return passwordEncoder.matches( plainPassword,  encryptedPassword );
    }
}
