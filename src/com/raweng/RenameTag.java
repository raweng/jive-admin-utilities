package com.raweng;

/**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: May 25, 2011
 * Time: 3:27:51 PM
 * To change this template use File | Settings | File Templates.
 */

import com.jiveSoftware.base.database.dao.ContentTagContainer;
import com.jiveSoftware.base.database.dao.RenameTagDao;
import com.jivesoftware.community.ContentTag;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.TagObjectNotFoundException;
import com.jivesoftware.community.action.admin.AdminActionSupport;

import com.jivesoftware.community.lifecycle.JiveApplication;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.lang.Exception;
import java.lang.String;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RenameTag extends AdminActionSupport {

    private static final Logger log = LogManager.getLogger(RenameTag.class);


    private String newTagName;
    private String oldTagName;

    private RenameTagDao renameTagDao = null ;
    private ClearCache clearCache = null;
    private boolean successParameter = false;
    private boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSuccessParameter() {
        return successParameter;
    }

    public void setSuccessParameter(boolean successParameter) {
        this.successParameter = successParameter;
    }

    public ClearCache getClearCache() {
        return clearCache;
    }

    public void setClearCache(ClearCache clearCache) {
        this.clearCache = clearCache;
    }

    public String getNewTagName() {
        return newTagName;
    }

    public void setNewTagName(String newTagName) {
        this.newTagName = newTagName;
    }

    public String getOldTagName() {
        return oldTagName;
    }

    public void setOldTagName(String oldTagName) {
        this.oldTagName = oldTagName;
    }

    public RenameTagDao getRenameTagDao() {
        return renameTagDao;
    }

    public void setRenameTagDao(RenameTagDao renameTagDao) {
        this.renameTagDao = renameTagDao;
    }

    public String execute() {
        boolean update = request.getParameter("update")!=null;
        if(update && newTagName != null && oldTagName != null && !oldTagName.equals("") && !newTagName.equals("")) {
            try {
                //get ContentTags to be replaced
                ContentTag newContentTag = getTag(newTagName.trim());
                ContentTag oldContentTag = getTag(oldTagName.trim());


                ContentTagContainer contentTagContainer;

                Iterator<ContentTagContainer> contentTagContainerIterator;

                List<ContentTagContainer> oldContentTagContainers;

                if(oldContentTag != null && newTagName.trim().indexOf(" ")==-1) {
                    oldContentTagContainers = renameTagDao.getTagContainer(oldContentTag.getID());
                    contentTagContainerIterator = oldContentTagContainers.iterator();
                    if(newContentTag == null) {
                        newContentTag = tagManager.createTag(newTagName);
                    }
                    if(newContentTag!=null) {
                        if(!newContentTag.equals(oldContentTag)) {
                            while(contentTagContainerIterator.hasNext()) {
                                contentTagContainer = contentTagContainerIterator.next();
                                try{
                                    JiveObject oldJiveObject = JiveApplication.getContext().getObjectLoader().getJiveObject(contentTagContainer.getContentType(),contentTagContainer.getContainerID());
                                    List<ContentTag> oldContentTagsList = getJiveObjectTagsList(tagManager.getTags(oldJiveObject));
                                    if(oldJiveObject != null && oldContentTagsList != null) {
                                        tagManager.removeTag(oldContentTag,oldJiveObject);
                                        if(!oldContentTagsList.contains(newContentTag)) {
                                            tagManager.addTag(newContentTag,oldJiveObject);
                                        }
                                    }
                                } catch (Exception e){
                                    log.error( e + " Something get wrong with container " + contentTagContainer.getContainerID() + " with type " + contentTagContainer.getContentType() );
                                }
                            }
                        } else {
                            addFieldError("EQUAL_TAG_NAME","Old tag name and New tag name cannot be same");
                            return INPUT;
                        }
                        log.info("Replace old tag " + oldContentTag.getName() + ", with new tag " + newContentTag.getName());
                    } else {
                        renameTagDao.replaceTagName(newTagName,oldContentTag);
                    }

                    clearCache.clearTagCache(oldContentTag);
                    contentTagContainerIterator =  oldContentTagContainers.iterator();
                    while(contentTagContainerIterator.hasNext()) {
                        contentTagContainer = contentTagContainerIterator.next();
                        try {                                   
                            JiveObject jiveObject = JiveApplication.getContext().getObjectLoader().getJiveObject(contentTagContainer.getContentType(),contentTagContainer.getContainerID());
                            if(jiveObject!=null){
                                clearCache.clearTagContentCache(jiveObject);
                            }
                        } catch (Exception e){
                            log.error( " Cache Something get wrong with container " + contentTagContainer.getContainerID() + " with type " + contentTagContainer.getContentType() );
                            e.printStackTrace();
                        }

                    }
                    successParameter = true;

                } else {
                    if(newTagName.trim().indexOf(" ")!=-1)
                    addFieldError("SPACE_IN_TAG", "Tag name cannot have space");

                    if(oldContentTag == null)
                    addFieldError("CONTENT_TAG_NOT_FOUND","Tag not found with name " + oldTagName);
                    
                }
            }  catch (Exception e){
                error = true;
                e.printStackTrace();
                return INPUT;
            }
        } else {
            if(update){
                if(newTagName == null || newTagName.equals("")){
                    addFieldError("NEW_TAG","Please enter tag name");
                }
                if((oldTagName == null || oldTagName.equals("")) && (!getFieldErrors().containsKey("CONTENT_TAG_NOT_FOUND"))){
                    addFieldError("OLD_TAG", "Please enter tag name");
                }
            }
            return INPUT;
        }

        return SUCCESS;
    }

    public ContentTag getTag(String s) {
        try {
            return tagManager.getTag(s.trim());
        } catch (TagObjectNotFoundException e) {
            return null;
        }
    }

    public ArrayList<ContentTag> getJiveObjectTagsList( Iterator<ContentTag> contentTagIterator){
        ArrayList<ContentTag> contentTagList = new ArrayList<ContentTag>();
        while (contentTagIterator.hasNext()){
            contentTagList.add(contentTagIterator.next());
        }
        if(!contentTagList.isEmpty()){
            return contentTagList;
        }
        return null;
    }
}
