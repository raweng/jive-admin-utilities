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

package com.raweng;
/**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: Jan 29, 2011
 * Time: 4:50:41 PM
 * To change this template use File | Settings | File Templates.
 */


import com.jiveSoftware.base.database.dao.UtilitiesDao;
import com.jivesoftware.base.User;
import com.jivesoftware.base.UserNotFoundException;
import com.jivesoftware.community.*;
import com.jivesoftware.community.action.admin.AdminActionSupport;
import com.jivesoftware.community.impl.DbDocumentManager;
import com.jivesoftware.community.lifecycle.JiveApplication;
import org.apache.log4j.Logger;

public class  ChangeDocAuthorAction extends AdminActionSupport {

    private Document document;
    private String documentID;
    private String userID;
    private User user;
    private DocumentManager documentManager;
    private WatchManager watchManager;
    private Watch watch;
    private boolean documentDoesNotExist = false;
    private boolean userDoesNotExist = false;
    private UtilitiesDao utilitiesDao = null ;
    private boolean successParameter = false;
    private boolean error = false;


    private static final Logger log = Logger.getLogger(ChangeDocAuthorAction.class);

    public UtilitiesDao getUtilitiesDao() {
        return utilitiesDao;
    }

    public void setUtilitiesDao(UtilitiesDao utilitiesDao) {
        this.utilitiesDao = utilitiesDao;
    }

    public String input() {
        return  INPUT;
    }

    public String execute() {

        boolean update = request.getParameter("update")!=null;
        if(update) {
            if((documentID!=null && userID!=null) && (!documentID.trim().equals("") && !userID.trim().equals(""))) {

                if(!documentID.trim().equals("") && !userID.trim().equals(""))
                {
                    try {
                        DbDocumentManager dbDocumentManager = (DbDocumentManager) JiveApplication.getContext().getDocumentManager();
                        try {
                            document = documentManager.getDocument(Long.parseLong(documentID.trim()));
                        } catch (DocumentObjectNotFoundException e) {
                            documentDoesNotExist = true;
                            addFieldError("DOCUMENT_NOT_EXIST","Document ID " + documentID + " dose not exist");
                            log.info(e);
                        }

                        try{
                            user = userManager.getUser(Long.parseLong(userID.trim()));
                        }  catch (UserNotFoundException e) {
                            userDoesNotExist = true;
                            addFieldError("USER_NOT_EXIST","User ID " + userID + " dose not exist");
                            log.info(e);
                        }

                        if (userDoesNotExist || documentDoesNotExist) {
                            return input();
                        }

                        if(document.getUser().equals(user)){
                            addFieldError("ALREADY_AUTHOR","User " + user.getID() + " is already a author of document " + document.getID());
                            return input();
                        }

                        UserContainerManager userContainerManager = JiveApplication.getContext().getUserContainerManager();

                        UserContainer userContainer =  userContainerManager.getUserContainer(user);

                        User oldUser = document.getUser();

                        utilitiesDao.changeDocumentAuthor(oldUser, user, document, userContainer);

                        watchManager = JiveApplication.getContext().getWatchManager();
                        watch = watchManager.getWatch(oldUser ,document);
                        if(watch != null){
                            watchManager.deleteWatch(watch);
                        }

                        Watch newWatch = watchManager.createWatch(user , document);
                        
                        if(newWatch != null){
                            watchManager.update(newWatch);
                        }

                        document.save();

                        dbDocumentManager.clearCaches(document);
                        successParameter = true;
                        log.info("Change Author to : " + user.getName() + " of Document : " + document.getID() + " old User : " + oldUser.getID());

                    }catch(Exception e) {
                        error = true;
                        e.printStackTrace();
                        return input();
                    }
                    return SUCCESS;

                }
            } else {
               if(update) {
                  if(userID == null || userID.trim().equals("")) {
                     addFieldError("USER","Please enter User ID");
                  }
                  if(documentID == null || documentID.trim().equals("")) {
                     addFieldError("DOCUMENT","Please enter Document ID");
                  }
               }
               return input();
            }
        }
        return SUCCESS;
    }


    public boolean getSuccessParameter() {
        return successParameter;
    }

    public void setSuccessParameter(boolean successParameter) {
        this.successParameter = successParameter;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Document getDocument()  {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

}

