package com.blogen.api.v1.controllers;

import com.blogen.api.v1.model.AvatarResponse;
import com.blogen.services.AvatarService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for retrieving user preferences
 *
 * Author: Cliff
 */
@Api
@Slf4j
@RestController
@RequestMapping( UserPrefsController.BASE_URL )
public class UserPrefsController {

    public static final String BASE_URL = "/api/v1/userPrefs";

    private AvatarService avatarService;

    @Autowired
    public UserPrefsController( AvatarService avatarService ) {
        this.avatarService = avatarService;
    }

    @GetMapping( "/avatars" )
    public AvatarResponse avatarImageFileNames() {
        return AvatarResponse.builder()
                .avatars( avatarService.getAllAvatarImageNames() )
                .build();

    }
}
