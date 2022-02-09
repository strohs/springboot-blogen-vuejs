package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.UserPrefsDTO;
import com.blogen.domain.Avatar;
import com.blogen.domain.UserPrefs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit Test MapStruct UserPrefsMapper between UserPrefs and UserPrefsDTO
 * <p>
 * Author: Cliff
 */
public class UserPrefsMapperTest {

    private static final Long ID = 22L;
    private static final String AVATAR_IMAGE = "avatar1.jpg";

    private UserPrefsMapper userPrefsMapper = UserPrefsMapper.INSTANCE;

    @BeforeEach
    public void setUp() throws Exception {
    }

    @Test
    public void userPrefsToUserPrefsDto() {
        //given
        Avatar avatar = Avatar.builder().id(1L).fileName(AVATAR_IMAGE).build();
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setId(ID);
        userPrefs.setAvatar(avatar);

        //when
        UserPrefsDTO userPrefsDTO = userPrefsMapper.userPrefsToUserPrefsDto(userPrefs);

        assertNotNull(userPrefsDTO);
        assertThat(userPrefsDTO.getId(), is(ID));
        assertThat(userPrefsDTO.getAvatarImage(), is(AVATAR_IMAGE));
    }

    @Test
    public void userPrefsDtoToUserPrefs() {
        //given
        Avatar avatar = Avatar.builder().id(1L).fileName(AVATAR_IMAGE).build();
        UserPrefsDTO userPrefsDTO = new UserPrefsDTO(ID, AVATAR_IMAGE);

        //when
        UserPrefs userPrefs = userPrefsMapper.userPrefsDtoToUserPrefs(userPrefsDTO);

        assertNotNull(userPrefs);
        assertThat(userPrefs.getId(), is(ID));
        assertThat(userPrefs.getAvatar().getFileName(), is(AVATAR_IMAGE));
    }
}