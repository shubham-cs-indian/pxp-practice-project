package com.cs.core.runtime.interactor.model.tag;

import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.SectionTag;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TagDiffModel implements ITagDiffModel {
  
  protected String                   id;
  protected String                   tagId;
  // protected String mappingId;
  protected ISectionElement          sectionElement;
  protected List<ITagDiffValueModel> oldValues;
  protected List<ITagDiffValueModel> addedValues;
  protected List<ITagDiffValueModel> deletedValues;
  protected List<ITagDiffValueModel> modifiedValues;
  protected Boolean                  isAdded = false;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  /*  @JsonProperty("tagMappingId")
  @Override
  public String getMappingId()
  {
    return mappingId;
  }
  
  @JsonProperty("tagMappingId")
  @Override
  public void setMappingId(String mappingId)
  {
    this.mappingId = mappingId;
  }*/
  
  @Override
  public ISectionElement getSectionElement()
  {
    return sectionElement;
  }
  
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(as = SectionTag.class)
  @Override
  public void setSectionElement(ISectionElement tag)
  {
    this.sectionElement = tag;
  }
  
  @JsonDeserialize(contentAs = TagDiffValueModel.class)
  @Override
  public List<ITagDiffValueModel> getOldValues()
  {
    if (oldValues == null) {
      oldValues = new ArrayList<ITagDiffValueModel>();
    }
    return oldValues;
  }
  
  @JsonDeserialize(contentAs = TagDiffValueModel.class)
  @Override
  public void setOldValues(List<ITagDiffValueModel> oldValues)
  {
    this.oldValues = oldValues;
  }
  
  @Override
  public List<ITagDiffValueModel> getAddedValues()
  {
    if (addedValues == null) {
      addedValues = new ArrayList<ITagDiffValueModel>();
    }
    return addedValues;
  }
  
  @JsonDeserialize(contentAs = TagDiffValueModel.class)
  @Override
  public void setAddedValues(List<ITagDiffValueModel> newValues)
  {
    this.addedValues = newValues;
  }
  
  @Override
  public List<ITagDiffValueModel> getDeletedValues()
  {
    if (deletedValues == null) {
      deletedValues = new ArrayList<ITagDiffValueModel>();
    }
    return deletedValues;
  }
  
  @JsonDeserialize(contentAs = TagDiffValueModel.class)
  @Override
  public void setDeletedValues(List<ITagDiffValueModel> newValues)
  {
    this.deletedValues = newValues;
  }
  
  @Override
  public List<ITagDiffValueModel> getModifiedValues()
  {
    if (modifiedValues == null) {
      modifiedValues = new ArrayList<ITagDiffValueModel>();
    }
    return modifiedValues;
  }
  
  @JsonDeserialize(contentAs = TagDiffValueModel.class)
  @Override
  public void setModifiedValues(List<ITagDiffValueModel> newValues)
  {
    this.modifiedValues = newValues;
  }
  
  @Override
  public Boolean getIsAdded()
  {
    return isAdded;
  }
  
  @Override
  public void setIsAdded(Boolean isAdded)
  {
    this.isAdded = isAdded;
  }
  
  @Override
  public String getTagId()
  {
    return tagId;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
}
