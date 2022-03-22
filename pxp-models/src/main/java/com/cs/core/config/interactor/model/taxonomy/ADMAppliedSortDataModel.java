package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

public class ADMAppliedSortDataModel implements IADMAppliedSortDataModel {
  
  private static final long serialVersionUID = 1L;
  
  protected List<String>    addedAttributes;
  protected List<String>    deletedAttributes;
  protected String          code;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  @JsonIgnore
  public List<String> getAttributes()
  {
    return null;
  }
  
  @Override
  public void setAttributes(List<String> attributes)
  {
  }
  
  @Override
  @JsonIgnore
  public String getId()
  {
    return null;
  }
  
  @Override
  public void setId(String id)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  @JsonIgnore
  public IEntity getEntity()
  {
    return null;
  }
  
  @Override
  public List<String> getAddedAttributes()
  {
    if (addedAttributes == null) {
      addedAttributes = new ArrayList<>();
    }
    return addedAttributes;
  }
  
  @Override
  public void setAddedAttributes(List<String> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public List<String> getDeletedAttributes()
  {
    if (deletedAttributes == null) {
      deletedAttributes = new ArrayList<>();
    }
    return deletedAttributes;
  }
  
  @Override
  public void setDeletedAttributes(List<String> deletedAttributes)
  {
    this.deletedAttributes = deletedAttributes;
  }
}
