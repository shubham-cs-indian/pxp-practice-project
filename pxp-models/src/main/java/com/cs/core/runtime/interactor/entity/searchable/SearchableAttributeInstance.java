package com.cs.core.runtime.interactor.entity.searchable;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchableAttributeInstance implements ISearchableAttributeInstance {
  
  private static final long serialVersionUID = 1L;
  
  protected String          value;
  protected Double          valueAsNumber;
  protected Boolean         isMandatoryViolated;
  protected Boolean         isShouldViolated;
  protected String          klassInstanceId;
  protected String          attributeId;
  protected String          code;
  protected Integer         isUnique         = -1;
  protected String          variantInstanceId;
  protected Long            iid;
  
  @Override
  public String getValue()
  {
    return value;
  }
  
  @Override
  public void setValue(String value)
  {
    this.value = value;
  }
  
  @Override
  public Double getValueAsNumber()
  {
    return valueAsNumber;
  }
  
  @Override
  public void setValueAsNumber(Double valueAsNumber)
  {
    this.valueAsNumber = valueAsNumber;
  }
  
  @Override
  public Boolean getIsMandatoryViolated()
  {
    return isMandatoryViolated;
  }
  
  @Override
  public void setIsMandatoryViolated(Boolean isMandatoryViolated)
  {
    this.isMandatoryViolated = isMandatoryViolated;
  }
  
  @Override
  public Boolean getIsShouldViolated()
  {
    return isShouldViolated;
  }
  
  @Override
  public void setIsShouldViolated(Boolean isShouldViolated)
  {
    this.isShouldViolated = isShouldViolated;
  }
  
  @Override
  public String getAttributeId()
  {
    return attributeId;
  }
  
  @Override
  public void setAttributeId(String attributeId)
  {
    this.attributeId = attributeId;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
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
  
  @Override
  public Integer getIsUnique()
  {
    return isUnique;
  }
  
  @Override
  public void setIsUnique(Integer isUnique)
  {
    this.isUnique = isUnique;
  }
  
  @Override
  public String getVariantInstanceId()
  {
    return variantInstanceId;
  }
  
  @Override
  public void setVariantInstanceId(String variantInstanceId)
  {
    this.variantInstanceId = variantInstanceId;
  }
  
  @Override
  @JsonIgnore
  public String getCreatedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setCreatedBy(String createdBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  @JsonIgnore
  public Long getCreatedOn()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setCreatedOn(Long createdOn)
  {
    // TODO Auto-generated method stub
    
  }
  
  /*@Override
  @JsonIgnore
  public String getOwner()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@Override
  @JsonIgnore
  public void setOwner(String owner)
  {
    // TODO Auto-generated method stub
  
  }*/
  
  @Override
  @JsonIgnore
  public Long getLastModified()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModified(Long lastModified)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getJobId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setJobId(String jobId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  @JsonIgnore
  public void setId(String id)
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
  public void setLastModifiedBy(String lastModifiedBy)
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
  public Long getIid()
  {
    return iid;
  }
  
  @Override
  public void setIid(Long iid)
  {
    this.iid = iid;
  }
}
