package com.raweng; /**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: Jan 24, 2011
 * Time: 12:34:21 PM
 * To change this template use File | Settings | File Templates.
 */
import com.jiveSoftware.base.database.dao.UtilitiesDao;
import com.jivesoftware.community.*;
import com.jivesoftware.community.action.admin.AdminActionSupport;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class UpdateDocExpirationAction extends AdminActionSupport {

    private Date expirationDate;
    private Date oldExpirationDate;
    private String documentID;
    private DocumentManager documentManager;
    private Document document;
    private UtilitiesDao utilitiesDao = null;
    private static final Logger log = Logger.getLogger(ClearCachesAction.class);
    private boolean error = false;
    private boolean successParameter = false;
    private String nonExistingDocID;

    public UtilitiesDao getUtilitiesDao() {
        return utilitiesDao;
    }

    public void setUtilitiesDao(UtilitiesDao utilitiesDao) {
        this.utilitiesDao = utilitiesDao;
    }

    public Date getOldExpirationDate() {
        return oldExpirationDate;
    }

    public void setOldExpirationDate(Date oldExpirationDate) {
        this.oldExpirationDate = oldExpirationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getNonExistingDocID() {
        return nonExistingDocID;
    }

    public void setNonExistingDocID(String nonExistingDocID) {
        this.nonExistingDocID = nonExistingDocID;
    }

    public boolean getError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }   

    public boolean getSuccessParameter() {
        return successParameter;
    }

    public void setSuccessParameter(boolean successParameter) {
        this.successParameter = successParameter;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentManager getDocumentManager() {
        return documentManager;
    }

    public void setDocumentManager(DocumentManager documentManager) {
        this.documentManager = documentManager;
    }

    public String getDocumentID() {
           return documentID;
       }

   public void setDocumentID(String documentID) {
       this.documentID = documentID;
   }


    public String input() {
        return INPUT;
    }

    public String execute() {
        boolean update = request.getParameter("update")!=null;
        Calendar expirationDate = Calendar.getInstance();
        if(update) {
            if(update && documentID != null && !documentID.trim().equals("")) {
                try {

                    document = documentManager.getDocument(Long.valueOf(documentID.trim()));
                    oldExpirationDate = document.getExpirationDate();

                    if(this.expirationDate != null) {
                        if(document.getExpirationDate().equals(this.expirationDate))
                        {
                            addFieldError("EQUAL_EXPIRATION_DATE","Please Enter another date.");
                            return INPUT;
                        }
                        document.setExpirationDate(this.expirationDate);
                        document.save();
                    }
                    log.info("Expiration Date Changed to : " + document.getExpirationDate());
                    successParameter = true;
                } catch (DocumentObjectNotFoundException e) {
                    addFieldError("ERROR_DOC_NOT_EXIST","Document " + documentID + " dose not exist");
                    e.printStackTrace();
                    input();
                } catch (Exception e) {
                    error  = true;
                    e.printStackTrace();
                    input();
                }
                return SUCCESS;
            } else {
                if(update) {
                    addFieldError("EMPTY","Please Enter DocumentID");
                }
                return input();
            }
        }
        return SUCCESS;
    }

}
