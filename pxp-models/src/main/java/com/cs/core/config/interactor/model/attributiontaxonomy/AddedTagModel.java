package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class AddedTagModel implements IAddedTagModel {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected List<String>    tagValueIds;
  protected Boolean         isNewlyCreated   = false;
  protected String          label;
  protected String          code;
  protected long            propertyIID;
  
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
  public List<String> getTagValueIds()
  {
    return tagValueIds;
  }
  
  @Override
  public void setTagValueIds(List<String> tagValueIds)
  {
    this.tagValueIds = tagValueIds;
  }
  
  @Override
  public Boolean getIsNewlyCreated()
  {
    return isNewlyCreated;
  }
  
  @Override
  public void setIsNewlyCreated(Boolean isNewlyCreated)
  {
    this.isNewlyCreated = isNewlyCreated;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
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
  
  /**
   * ********************** Ignored ***************************
   */
  @Override
  @JsonIgnore
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public long getPropertyIID()
  {
    return this.propertyIID;
  }
  
  @Override
  public void setPropertyIID(long iid)
  {
    this.propertyIID = iid;
  }
}
