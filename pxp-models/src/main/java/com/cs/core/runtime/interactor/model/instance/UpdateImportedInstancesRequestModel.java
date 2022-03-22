package com.cs.core.runtime.interactor.model.instance;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.klassinstance.IUpdateImportedInstancesRequestModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import javax.swing.text.html.HTML.Tag;
import java.util.Map;

public class UpdateImportedInstancesRequestModel implements IUpdateImportedInstancesRequestModel {
  
  private static final long   serialVersionUID = 1L;
  protected String            contentId;
  protected Map<String, ITag> referencedTags;
  
  @Override
  public String getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
}
