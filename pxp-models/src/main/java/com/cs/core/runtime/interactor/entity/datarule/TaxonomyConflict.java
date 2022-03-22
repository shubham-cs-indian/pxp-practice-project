package com.cs.core.runtime.interactor.entity.datarule;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TaxonomyConflict implements ITaxonomyConflict{
  
  private static final long                  serialVersionUID           = 1L;
  protected List<ITaxonomyConflictingSource> taxonomyConflictingSource;
  protected Boolean                          isResolved                 = false;
  protected Setting                          taxonomyInheritanceSetting;
  
  @Override
  public List<ITaxonomyConflictingSource> getConflicts()
  {
    if (taxonomyConflictingSource == null) {
      taxonomyConflictingSource = new ArrayList<>();
    }
    return taxonomyConflictingSource;
  }
  
  @Override
  @JsonDeserialize(contentAs = TaxonomyConflictingSource.class)
  public void setConflicts(List<ITaxonomyConflictingSource> relationshipConflictingSource)
  {
    this.taxonomyConflictingSource = relationshipConflictingSource;
  }
  
  @Override
  public Boolean getIsResolved()
  {
    return isResolved;
  }
  
  @Override
  public void setIsResolved(Boolean isResolved)
  {
    this.isResolved = isResolved;
  }
  
  @Override
  public Setting getTaxonomyInheritanceSetting()
  {
    if(taxonomyInheritanceSetting == null) {
      taxonomyInheritanceSetting = Setting.off;
    }
    return taxonomyInheritanceSetting;
  }
  
  @Override
  public void setTaxonomyInheritanceSetting(Setting taxonomyInheritanceSetting)
  {
    this.taxonomyInheritanceSetting = taxonomyInheritanceSetting;
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

  @Override
  @JsonIgnore
  public Long getLastModified()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setLastModified(Long lastModified)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
  public String getJobId()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  @JsonIgnore
  public void setJobId(String jobId)
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  @JsonIgnore
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
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setIid(Long iid)
  {
    // TODO Auto-generated method stub
    
  }

}

