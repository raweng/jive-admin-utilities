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
