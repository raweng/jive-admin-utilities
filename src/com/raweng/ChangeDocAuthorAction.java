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

