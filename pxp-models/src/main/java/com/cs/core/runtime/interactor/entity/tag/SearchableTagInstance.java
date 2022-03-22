package com.cs.core.runtime.interactor.entity.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class SearchableTagInstance implements ISearchableTagInstance {
  
  private static final long           serialVersionUID = 1L;
  
  protected Boolean                   isMandatoryViolated;
  protected Boolean                   isShouldViolated;
  protected String                    klassInstanceId;
  protected String                    code;
  protected String                    variantInstanceId;
  protected String                    tagId;
  protected List<ISearchableTagValue> tagValues        = new ArrayList<>();
  protected String                    contextInstanceId;
  protected Long                      iid;
  
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
  public List<ISearchableTagValue> getTagValues()
  {
    return this.tagValues;
  }
  
  @JsonDeserialize(contentAs = SearchableTagValue.class)
  @Override
  public void setTagValues(List<ISearchableTagValue> tagValues)
  {
    this.tagValues = tagValues;
  }
  
  @Override
  public void setTagId(String tagId)
  {
    this.tagId = tagId;
  }
  
  @Override
  public String getTagId()
  {
    return this.tagId;
  }
  
  @Override
  public String getContextInstanceId()
  {
    return contextInstanceId;
  }
  
  @Override
  public void setContextInstanceId(String contextInstanceId)
  {
    this.contextInstanceId = contextInstanceId;
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
  
  /*@JsonIgnore
  @Override
  public String getOwner()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  /*@JsonIgnore
  @Override
  public void setOwner(String owner)
  {
    // TODO Auto-generated method stub
  
  }*/
  @JsonIgnore
  @Override
  public Long getLastModified()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModified(Long lastModified)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
  @Override
  public String getJobId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setJobId(String jobId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @JsonIgnore
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
