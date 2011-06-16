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
 * Date: Feb 2, 2011
 * Time: 6:13:36 PM
 * To change this template use File | Settings | File Templates.
 */
import com.jivesoftware.base.User;
import com.jivesoftware.base.database.dao.JiveJdbcDaoSupport;
import com.jivesoftware.community.Document;
import com.jivesoftware.community.UserContainer;
import org.apache.log4j.Logger;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UtilitiesDao extends JiveJdbcDaoSupport {

    private static final Logger log = Logger.getLogger(UtilitiesDao.class);

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
     public void changeDocumentAuthor(User oldUser, User newUser, Document document , UserContainer newUserContainer){
       //Container type 2020 is for users container
        if(document.getContainerType() == 2020) {
            getSimpleJdbcTemplate().update("UPDATE jiveDocument SET userID = ?, containerID = ? WHERE userID = ? AND internalDocID = ? ",newUser.getID(), newUserContainer.getID(), oldUser.getID(), document.getID());

        } else {
            getSimpleJdbcTemplate().update("UPDATE jiveDocCollab SET userID = ? WHERE userID = ? AND internalDocID = ?", newUser.getID(), oldUser.getID(), document.getID());
            getSimpleJdbcTemplate().update("UPDATE jiveDocument SET userID = ? WHERE userID = ? AND internalDocID = ?", newUser.getID(), oldUser.getID(), document.getID());
        }    
        getSimpleJdbcTemplate().update("UPDATE jiveDocVersion SET userID = ? WHERE userID = ? AND internalDocID = ?", newUser.getID(),oldUser.getID(), document.getID());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void publishExpiredDocument(long expirationDate, long modificationDate, Document document){
        getSimpleJdbcTemplate().update("UPDATE jiveDocument SET expirationDate = ? WHERE internalDocID = ?", expirationDate, document.getID());
        getSimpleJdbcTemplate().update("UPDATE jiveDocVersion SET state = 'published' , status=2, modificationDate = ? "+
                " WHERE internalDocID = ? AND versionID = ?", modificationDate, document.getID(), document.getVersionID());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
     public void expiredPublishedDocument(long expirationDate, Document document){
        getSimpleJdbcTemplate().update("UPDATE jiveDocument SET expirationDate = " + expirationDate + " WHERE internalDocID = " + document.getID());
        getSimpleJdbcTemplate().update("UPDATE jiveDocVersion SET state = 'expired',status = 9 WHERE internalDocID = " + document.getID() + " AND versionID = "+ document.getVersionID());
    }
    
}
