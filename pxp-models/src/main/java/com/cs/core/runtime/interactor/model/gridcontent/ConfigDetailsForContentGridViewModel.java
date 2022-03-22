package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.config.interactor.entity.attribute.AbstractAttribute;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class ConfigDetailsForContentGridViewModel implements IConfigDetailsForContentGridViewModel {
  
  private static final long                              serialVersionUID = 1L;
  protected Map<String, IAttribute>                      referencedAttributes;
  protected Map<String, ITag>                            referencedTags;
  protected List<String>                                 allowedPropertyIds;
  protected Map<String, IGridInstanceConfigDetailsModel> instanceConfigDetails;
  protected List<String>                                 gridPropertySequenceList;
  
  @Override
  public Map<String, IGridInstanceConfigDetailsModel> getInstanceConfigDetails()
  {
    return instanceConfigDetails;
  }
  
  @Override
  @JsonDeserialize(contentAs = GridInstanceConfigDetailsModel.class)
  public void setInstanceConfigDetails(
      Map<String, IGridInstanceConfigDetailsModel> instanceConfigDetails)
  {
    this.instanceConfigDetails = instanceConfigDetails;
  }
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractAttribute.class)
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
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
  
  @Override
  public List<String> getAllowedPropertyIds()
  {
    return allowedPropertyIds;
  }
  
  @Override
  public void setAllowedPropertyIds(List<String> allowedPropertyIds)
  {
    this.allowedPropertyIds = allowedPropertyIds;
  }
  
  @Override
  public List<String> getGridPropertySequenceList()
  {
    return gridPropertySequenceList;
  }
  
  @Override
  public void setGridPropertySequenceList(List<String> gridPropertySequenceList)
  {
    this.gridPropertySequenceList = gridPropertySequenceList;
  }
}
