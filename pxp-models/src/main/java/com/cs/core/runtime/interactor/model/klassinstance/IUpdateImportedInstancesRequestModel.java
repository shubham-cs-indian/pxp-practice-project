package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IUpdateImportedInstancesRequestModel extends IModel {
  
  public static final String CONTENT_ID      = "contentId";
  public static final String REFERENCED_TAGS = "referencedTags";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
}
