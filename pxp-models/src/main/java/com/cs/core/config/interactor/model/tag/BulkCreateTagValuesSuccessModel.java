package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class BulkCreateTagValuesSuccessModel implements IBulkCreateTagValuesSuccessModel {
  
  private static final long                            serialVersionUID = 1L;
  protected List<ITagModel>                            tags;
  protected Map<String, IConfigEntityInformationModel> referencedTags;
  
  @Override
  public List<ITagModel> getTags()
  {
    return tags;
  }
  
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(contentAs = TagModel.class)
  @Override
  public void setTags(List<ITagModel> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedTags(Map<String, IConfigEntityInformationModel> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
}
