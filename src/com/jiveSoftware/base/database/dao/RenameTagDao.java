package com.jiveSoftware.base.database.dao;

/**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: May 25, 2011
 * Time: 3:31:43 PM
 * To change this template use File | Settings | File Templates.
 */

import com.jivesoftware.base.database.dao.JiveJdbcDaoSupport;
import com.jivesoftware.community.ContentTag;
import org.apache.log4j.Logger;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.transaction.annotation.Propagation;

import java.lang.Object;
import java.lang.String;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class RenameTagDao extends JiveJdbcDaoSupport {

    private static final Logger log = Logger.getLogger(RenameTagDao.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void replaceTagName(String newTagName , ContentTag oldContentTag){
        getSimpleJdbcTemplate().update("UPDATE jiveTag SET tagName = ? WHERE tagID = ?",newTagName.trim(),oldContentTag.getID());
    }

    private final ParameterizedRowMapper<ContentTagContainer> tagContainerMapper = new ParameterizedRowMapper(){
        public ContentTagContainer mapRow(ResultSet rs, int rowNum) throws SQLException {

            DbContentTagContainer dbContentTagContainer = new DbContentTagContainer();
            dbContentTagContainer.setContentType(rs.getInt("objectType"));
            dbContentTagContainer.setContainerID(rs.getLong("objectID"));
            return dbContentTagContainer;
        }
    };

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<ContentTagContainer> getTagContainer(long ID)  {
        try {
            return getSimpleJdbcTemplate().query("SELECT objectType, objectID FROM jiveObjectTag WHERE tagID = "+ID ,tagContainerMapper,new Object[0]);
        } catch (EmptyResultDataAccessException e) {
            log.debug("No tag found for id '" + ID + "'");
            return null;
        }
    }


}

