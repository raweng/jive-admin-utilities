package com.raweng;

/**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: Jan 22, 2011
 * Time: 10:51:20 AM
 * To change this template use File | Settings | File Templates.
 */

import com.jivesoftware.community.*;
import com.jivesoftware.community.impl.DbDocumentManager;
import com.jivesoftware.community.action.admin.AdminActionSupport;
import com.jivesoftware.community.lifecycle.JiveApplication;
import org.apache.log4j.Logger;

public class ClearCachesAction extends AdminActionSupport{
    private Document document;
    private boolean successParameter = false;
    private boolean error = false;
    private String[] arrayDocumentID;
    private String documentID;
    private String successfulDocIDString;
    private static final Logger log = Logger.getLogger(ClearCachesAction.class);

    public String getSuccessfulDocIDString() {
        return successfulDocIDString;
    }

    public void setSuccessfulDocIDString(String successfulDocIDString) {
        this.successfulDocIDString = successfulDocIDString;
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
    public String[] getArrayDocumentID() {
        return arrayDocumentID;
    }

    public void setArrayDocumentID(String[] arrayDocumentID) {
        this.arrayDocumentID = arrayDocumentID;
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

        DbDocumentManager documentManager = (DbDocumentManager) JiveApplication.getContext().getDocumentManager();
        StringBuilder nonExistingDocID = new StringBuilder();
        StringBuilder successFulDocID = new StringBuilder();
        boolean clear = request.getParameter("clear") != null;

        if( clear && documentID!=null && !documentID.equals("")) {
            arrayDocumentID = commaSeparatedStringToLongArray(documentID);
                for(String doc: arrayDocumentID)
                {
                    try {
                        document = documentManager.getDocument(Long.valueOf(doc.trim()));
                        log.info(document.getDocumentID());
                        documentManager.clearCaches(document);

                        if(successFulDocID.length() > 0) {
                            successFulDocID.append(", ");
                        }
                        successFulDocID.append(document.getID());                        
                    } catch (DocumentObjectNotFoundException e) {

                        if(doc != null) {
                            if(nonExistingDocID.length() > 0) {
                                nonExistingDocID.append(", ");
                            }
                            nonExistingDocID.append(doc.trim());
                        }
                        log.info(e);

                    } catch (Exception e) {
                        error = true;
                        log.info(e);
                    }                    
                }

            } else {
                if(clear) {
                    addFieldError("EMPTY","Please Enter DocumentID/s");
                }
                return input();
            }

        successfulDocIDString = successFulDocID.toString();

        if(successFulDocID.length() != 0){
            successParameter = true;
        }

        if(nonExistingDocID.length() != 0) {
            addFieldError("ERROR_DOC_NOT_EXIST" , "Document " + nonExistingDocID.toString() + " Dose not exist");
        }
        return SUCCESS;
    }

    private String[] commaSeparatedStringToLongArray(String aString){
        String[] SSplitArray=null;
        if (aString != null || !aString.equalsIgnoreCase("")){
            SSplitArray = aString.split(",");
            log.info(aString +" "+SSplitArray.toString());
        }
        return SSplitArray;
    }
}
