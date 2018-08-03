package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.UserPrefsDTO;
import com.blogen.domain.UserPrefs;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mappers for mapping between {@link com.blogen.domain.UserPrefs} and {@link com.blogen.api.v1.model.UserPrefsDTO}
 *
 * Author: Cliff
 */
@Mapper
public interface UserPrefsMapper {

    UserPrefsMapper INSTANCE = Mappers.getMapper( UserPrefsMapper.class );

    @Mapping( target = "avatarImage", source = "avatar.fileName")
    UserPrefsDTO userPrefsToUserPrefsDto( UserPrefs userPrefs );

    @Mapping( target = "avatar.fileName", source = "avatarImage")
    UserPrefs userPrefsDtoToUserPrefs( UserPrefsDTO userPrefsDTO );

}
