package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = Id.NONE)
public class GetTagGridResponseModel implements IGetTagGridResponseModel {
  
  private static final long                            serialVersionUID = 1L;
  protected List<ITag>                                 tagsList;
  protected Integer                                    count;
  protected Map<String, IConfigEntityInformationModel> referencedTags;
  
  @Override
  public Integer getCount()
  {
    return count;
  }
  
  @Override
  public void setCount(Integer count)
  {
    this.count = count;
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
  
  @Override
  public List<ITag> getTagsList()
  {
    return tagsList;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setTagsList(List<ITag> tagsList)
  {
    this.tagsList = tagsList;
  }
}
