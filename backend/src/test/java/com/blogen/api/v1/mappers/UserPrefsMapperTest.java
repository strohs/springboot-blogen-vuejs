package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.UserPrefsDTO;
import com.blogen.domain.UserPrefs;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Unit Test MapStruct UserPrefsMapper between UserPrefs and UserPrefsDTO

 * Author: Cliff
 */
public class UserPrefsMapperTest {

    private static final Long ID = 22L;
    private static final String AVATAR_IMAGE = "avatar1.jpg";

    private UserPrefsMapper userPrefsMapper = UserPrefsMapper.INSTANCE;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void userPrefsToUserPrefsDto() {
        //given
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setId( ID );
        userPrefs.setAvatarImage( AVATAR_IMAGE );

        //when
        UserPrefsDTO userPrefsDTO = userPrefsMapper.userPrefsToUserPrefsDto( userPrefs );

        assertNotNull( userPrefsDTO );
        assertThat( userPrefsDTO.getId(), is(ID) );
        assertThat( userPrefsDTO.getAvatarImage(), is( AVATAR_IMAGE) );
    }

    @Test
    public void userPrefsDtoToUserPrefs() {
        //given
        UserPrefsDTO userPrefsDTO = new UserPrefsDTO( ID, AVATAR_IMAGE );

        //when
        UserPrefs userPrefs = userPrefsMapper.userPrefsDtoToUserPrefs( userPrefsDTO );

        assertNotNull( userPrefs );
        assertThat( userPrefs.getId(), is(ID) );
        assertThat( userPrefs.getAvatarImage(), is( AVATAR_IMAGE ) );
    }
}