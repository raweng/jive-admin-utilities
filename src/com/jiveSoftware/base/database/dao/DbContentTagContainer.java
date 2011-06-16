package com.jiveSoftware.base.database.dao;

/**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: May 25, 2011
 * Time: 3:33:43 PM
 * To change this template use File | Settings | File Templates.
 */

import java.io.Serializable;

public class DbContentTagContainer implements ContentTagContainer,Serializable{
    private static final long serialVersionUID = 0x2L;
	private long containerID;
	private int contentType;

    public long getContainerID() {
        return containerID;
    }

    public void setContainerID(long containerID) {
        this.containerID = containerID;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

}
