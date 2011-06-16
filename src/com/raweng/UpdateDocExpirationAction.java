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
