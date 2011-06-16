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

import com.jivesoftware.cache.Cache;
import com.jivesoftware.community.ContentTag;
import com.jivesoftware.community.JiveObject;
import com.jivesoftware.community.impl.TagCloudCacheKey;
import com.jivesoftware.community.tags.impl.TagContentCacheBean;
import com.jivesoftware.community.util.concurrent.LockUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.Integer;import java.lang.Long;import java.lang.String;import java.util.Map;


/**
 * Created by IntelliJ IDEA.
 * User: Ninad
 * Date: May 25, 2011
 * Time: 3:32:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClearCache {

     private static final Logger log = LogManager.getLogger(ClearCache.class);

    /**
    * A cache that maps tag names to ID's.
    */
    protected Cache<String, Long> tagIDCache;

    /**
     * A cache for tag objects.
     */
    protected Cache<Long, ContentTag> tagCache;

    /**
     * A cache for content object to tagID list
     */
    protected Cache<String, TagContentCacheBean> tagContentCache;

    // This cache expires fairly quickly (15 minutes by default).
    protected Cache<TagCloudCacheKey, Map<String, Integer>> tagCloudCache;


    public Cache<String, Long> getTagIDCache() {
        return tagIDCache;
    }

    public void setTagIDCache(Cache<String, Long> tagIDCache) {
        this.tagIDCache = tagIDCache;
    }

    public Cache<Long, ContentTag> getTagCache() {
        return tagCache;
    }

    public void setTagCache(Cache<Long, ContentTag> tagCache) {
        this.tagCache = tagCache;
    }

    public Cache<String, TagContentCacheBean> getTagContentCache() {
        return tagContentCache;
    }

    public void setTagContentCache(Cache<String, TagContentCacheBean> tagContentCache) {
        this.tagContentCache = tagContentCache;
    }

    public Cache<TagCloudCacheKey, Map<String, Integer>> getTagCloudCache() {
        return tagCloudCache;
    }

    public void setTagCloudCache(Cache<TagCloudCacheKey, Map<String, Integer>> tagCloudCache) {
        this.tagCloudCache = tagCloudCache;
    }

    public void clearTagCache(ContentTag contentTag) {
        ContentTag cacheContentTag = tagCache.get(contentTag.getID());

        if (cacheContentTag != null) {
            tagCache.remove(contentTag.getID());
            tagIDCache.remove(cacheContentTag.getName());
            tagCloudCache.clear();
            tagContentCache.remove(cacheContentTag.getName());
        }
    }

    public void clearTagContentCache(JiveObject jiveObject){
        tagContentCache.remove(getCacheKey(jiveObject));
    }

    /**
     * Returns a tag content cache key.
     *
     * @param jiveObject the jiveObject.
     * @return a cache key.
     */
    protected String getCacheKey(JiveObject jiveObject) {
        return LockUtil.intern(String.format("t-%d-%d", jiveObject.getObjectType(), jiveObject.getID()));
    }


}
