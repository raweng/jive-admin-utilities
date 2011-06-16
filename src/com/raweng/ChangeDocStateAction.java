package com.raweng;
/**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: Jan 24, 2011
 * Time: 8:11:19 PM
 * To change this template use File | Settings | File Templates.
 */
import com.jiveSoftware.base.database.dao.UtilitiesDao;
import com.jivesoftware.community.*;
import com.jivesoftware.community.impl.DbDocumentManager;
import com.jivesoftware.community.action.admin.AdminActionSupport;
import com.jivesoftware.community.lifecycle.JiveApplication;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;

import java.util.Calendar;

public class ChangeDocStateAction extends AdminActionSupport {

    private long expirationDate = 218345270400000L;
    private String documentID;
    private String documentState;
    private Document document;
    private UtilitiesDao utilitiesDao = null;
    private DocumentManager documentManager;
    private VersionManager versionManager;
    private DocumentVersion newestDocVersion;
    private String[] arrayDocumentID;
    private boolean successParameter=false;
    private String alreadyInRequestedState;
    private boolean error=false;
    private String successfulDocIDString;

    public String getAlreadyInRequestedState() {
        return alreadyInRequestedState;
    }

    public void setAlreadyInRequestedState(String alreadyInRequestedState) {
        this.alreadyInRequestedState = alreadyInRequestedState;
    }

    public String getSuccessfulDocIDString() {
        return successfulDocIDString;
    }

    public void setSuccessfulDocIDString(String successfulDocIDString) {
        this.successfulDocIDString = successfulDocIDString;
    }

    private static final Logger log = Logger.getLogger(ChangeDocStateAction.class);

    public VersionManager getVersionManager() {
        return versionManager;
    }

    public void setVersionManager(VersionManager versionManager) {
        this.versionManager = versionManager;
    }

    public UtilitiesDao getUtilitiesDao() {
        return utilitiesDao;

    }

    public void setUtilitiesDao(UtilitiesDao utilitiesDao) {
        this.utilitiesDao = utilitiesDao;

    }

    public DocumentVersion getNewestDocVersion() {
        return newestDocVersion;
    }

    public void setNewestDocVersion(DocumentVersion newestDocVersion) {
        this.newestDocVersion = newestDocVersion;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getDocumentState() {
        return documentState;
    }

    public void setDocumentState(String documentState) {
        this.documentState = documentState;
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

    public String[] getArrayDocumentID() {
        return arrayDocumentID;
    }

    public void setArrayDocumentID(String[] arrayDocumentID) {
        this.arrayDocumentID = arrayDocumentID;
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

    public String input() {
        System.out.println("input");
        return "input";
    }

    public String execute() {
        StringBuilder errorInState = new StringBuilder();
        StringBuilder nonExistingDocID = new StringBuilder();
        StringBuilder successFulDocID = new StringBuilder();
        StringBuilder alreadyInState = new StringBuilder();

        DbDocumentManager dbDocumentManager = (DbDocumentManager) JiveApplication.getContext().getDocumentManager();
        boolean update = request.getParameter("update")!=null;
        Calendar modificationDate = Calendar.getInstance();
        if(update && documentID!=null && !documentID.trim().equals("")) {
        arrayDocumentID = commaSeparatedStringToLongArray(documentID);

            for(String docID : arrayDocumentID) {

                try {
                    document = documentManager.getDocument(Long.valueOf(docID.trim()));
                    newestDocVersion = document.getVersionManager().getNewestDocumentVersion();
                    if(documentState.equalsIgnoreCase(document.getDocumentState().toString())){
                        if(alreadyInState.length() > 0) {
                            alreadyInState.append(", ");
                        }
                        alreadyInState.append(document.getID());
                    }else if(documentState.equalsIgnoreCase(DocumentState.PUBLISHED.toString())) {
                        //Change document state to PUBLISHED
                        try {

                           if (document.getDocumentState().equals(DocumentState.EXPIRED)) {
                               utilitiesDao.publishExpiredDocument(expirationDate,modificationDate.getTimeInMillis(),document);
                            } else {
                                if(errorInState.length() > 0) {
                                    errorInState.append(", ");
                                }
                                errorInState.append(document.getID());
                            }
                            log.info("Document state changed to PUBLISHED");

                        } catch (DataAccessException dae) {
                                log.error(dae);
                        }

                    } else if(documentState.equalsIgnoreCase(DocumentState.EXPIRED.toString())) {
                        //Change document state to EXPIRED
                        try{
                            Calendar expDate = Calendar.getInstance();
                            expDate.add(Calendar.DATE,-1);
                            if(document.getDocumentState().equals(DocumentState.PUBLISHED)) {
                                utilitiesDao.expiredPublishedDocument(expDate.getTimeInMillis(),document);
                                log.info("Document state changed EXPIRED");

                            } else {
                                if(errorInState.length() > 0) {
                                    errorInState.append(", ");
                                }
                                errorInState.append(document.getID());

                            }
                            log.info("Document state changed to EXPIRED");

                        } catch (DataAccessException dae) {
                                log.error(dae);
                        }

                    }
                    //If there is no errorInState change then successFulDocID stringBuilder get updated
                    if(errorInState.indexOf(String.valueOf(document.getID())) == -1 && alreadyInState.indexOf(String.valueOf(document.getID())) == -1){
                        if(successFulDocID.length() > 0) {
                            successFulDocID.append(", ");
                        }
                        successFulDocID.append(document.getID());
                        dbDocumentManager.clearCaches(document);
                    }

                } catch (DocumentObjectNotFoundException e) {
                    if(docID != null || !docID.trim().equals("")) {
                        if(nonExistingDocID.length() > 0) {
                            nonExistingDocID.append(", ");
                        }
                        nonExistingDocID.append(docID.trim());

                    }
                    log.info(e);

                } catch(Exception e) {
                    error = true;
                    log.info(e);
                    e.printStackTrace();
                    return input();

                }
            }

            if( successFulDocID.length() != 0 ) {
                successParameter = true;
                successfulDocIDString = successFulDocID.toString();
            }

            if(nonExistingDocID.length() != 0 ) {
               addFieldError("ERROR_DOC_NOT_EXIST" , "Document " + nonExistingDocID.toString() + " Dose not exist");
            }

            if( alreadyInState.length() != 0 ) {
                addFieldError("ALREADY_IN_STATE","Document ID " + alreadyInState.toString() + " already in " + documentState + " state");
            }

            if(errorInState.length() != 0 ) {
                if (documentState.trim().equalsIgnoreCase(DocumentState.EXPIRED.toString()))
                    addFieldError("ERROR_STATE","Document " + errorInState.toString() + " is not in PUBLISHED state");
                else if(documentState.trim().equalsIgnoreCase(DocumentState.PUBLISHED.toString()))
                    addFieldError("ERROR_STATE","Document/s " + errorInState.toString() + " is not in EXPIRED");
            }
            return "success";

        } else {
            if(update)
            {
                addFieldError("EMPTY","Please Enter Document ID/s");
            }
            return input();
        }
    }


    private String[] commaSeparatedStringToLongArray(String aString) {
        String[] SSplitArray=null;
        if (aString != null || !aString.equalsIgnoreCase("")) {
            SSplitArray = aString.split(",");
            log.info(aString +" "+SSplitArray.toString());
        }
        return SSplitArray;
    }
}
