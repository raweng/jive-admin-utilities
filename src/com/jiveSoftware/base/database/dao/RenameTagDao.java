/*This module is licenced under the BSD license.

Copyright (C) 2011 by raw engineering <ninad.hatkar (at) raweng (dot) com, mayank (at) raweng (dot) com>.

All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

    * Redistributions of source code must retain the above copyright
    notice, this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions and the following disclaimer in the
    documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/
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

