package com.blogen.repositories;

import com.blogen.domain.Avatar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * This is an example implementation of the AvatarRepository using jdbcTemplate. It is not used in the project, but
 * kept for reference purposes
 *
 * Author: Cliff
 */
@Repository
@Slf4j
@Profile("jdbc")
public class AvatarRepositoryImpl implements AvatarRepository {

    private final String FETCH_SQL_BY_ID = "SELECT id,file_name FROM avatar WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AvatarRepositoryImpl( JdbcTemplate jdbcTemplate ) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Avatar save( Avatar avatar ) {
        return null;
    }

    @Override
    public Optional<Avatar> findById( Long id ) {
        Avatar avatar;
        List<Avatar> avatars = jdbcTemplate.query( FETCH_SQL_BY_ID, new AvatarMapper(), id );
        avatar = avatars.get( 0 );
        return Optional.ofNullable( avatar );
    }

    @Override
    public Optional<Avatar> findByFileName( String fileName ) {
        return Optional.empty();
    }

    @Override
    public List<String> findAllAvatarFileNames() {
        return null;
    }


    /**
     * maps an Avatar table row into a Avatar domain object
     */
    class AvatarMapper implements RowMapper<Avatar> {

        @Override
        public Avatar mapRow( ResultSet rs, int rowNum ) throws SQLException {
            return Avatar.builder()
                    .id( rs.getLong( "id" ) )
                    .fileName( rs.getString( "file_name" ) )
                    .build();
        }
    }
}
