package com.cs.core.config.interactor.model.klass;


import com.fasterxml.jackson.annotation.JsonIgnore;

public class ReferencedPropertyCollectionElementModel
    implements IReferencedPropertyCollectionElementModel {
  
  private static final long serialVersionUID = 1L;
  protected String          elementId;
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
  public String getId()
  {
    return elementId;
  }
  
  @Override
  public void setId(String id)
  {
    this.elementId = id;
  }
  
  /**
   * ********************* ignored properties ****************
   */
  @Override
  @JsonIgnore
  public String getLabel()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLabel(String label)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public String getType()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setType(String type)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
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
  
  @Override
  @JsonIgnore
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
  
  @Override
  @JsonIgnore
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

  @Override
  @JsonIgnore
  public Integer getIndex()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setIndex(Integer index)
  {
    // TODO Auto-generated method stub
    
  }
}
