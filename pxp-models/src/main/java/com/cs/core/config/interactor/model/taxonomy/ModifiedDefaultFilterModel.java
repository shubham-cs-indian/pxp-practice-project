package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.taxonomy.FilterTagValue;
import com.cs.core.config.interactor.entity.taxonomy.IFilterTagValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ModifiedDefaultFilterModel implements IModifiedDefaultFilterModel {
  
  private static final long       serialVersionUID = 1L;
  
  protected List<IFilterTagValue> addedTagValues;
  protected List<IFilterTagValue> modifiedTagValues;
  protected List<String>          deletedTagValues;
  protected String                id;
  
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
  
  @Override
  public List<IFilterTagValue> getAddedTagValues()
  {
    if (addedTagValues == null) {
      addedTagValues = new ArrayList<>();
    }
    return addedTagValues;
  }
  
  @JsonDeserialize(contentAs = FilterTagValue.class)
  @Override
  public void setAddedTagValues(List<IFilterTagValue> tagValues)
  {
    this.addedTagValues = tagValues;
  }
  
  @Override
  public List<IFilterTagValue> getModifiedTagValues()
  {
    if (modifiedTagValues == null) {
      modifiedTagValues = new ArrayList<>();
    }
    return modifiedTagValues;
  }
  
  @JsonDeserialize(contentAs = FilterTagValue.class)
  @Override
  public void setModifiedTagValues(List<IFilterTagValue> tagValues)
  {
    this.modifiedTagValues = tagValues;
  }
  
  @Override
  public List<String> getDeletedTagValues()
  {
    if (deletedTagValues == null) {
      deletedTagValues = new ArrayList<>();
    }
    return deletedTagValues;
  }
  
  @Override
  public void setDeletedTagValues(List<String> tagValues)
  {
    this.deletedTagValues = tagValues;
  }
  
  @JsonIgnore
  @Override
  public List<IFilterTagValue> getTagValues()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTagValues(List<IFilterTagValue> tagValues)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getTagId()
  {
    return null;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
