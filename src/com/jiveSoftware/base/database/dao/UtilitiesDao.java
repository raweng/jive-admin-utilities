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
