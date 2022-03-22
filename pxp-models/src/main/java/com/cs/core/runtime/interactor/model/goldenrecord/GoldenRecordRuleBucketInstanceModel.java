package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.goldenrecord.IGoldenRecordRuleBucketInstance;
import com.cs.core.runtime.interactor.entity.masterarticle.GoldenRecordRuleBucketInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;

import java.util.List;

public class GoldenRecordRuleBucketInstanceModel implements IGoldenRecordRuleBucketInstanceModel {
  
  private static final long                 serialVersionUID = 1L;
  protected Long                            klassInstanceCount;
  protected Long                            iid;
  protected IGoldenRecordRuleBucketInstance entity;
  
  public GoldenRecordRuleBucketInstanceModel()
  {
    entity = new GoldenRecordRuleBucketInstance();
  }
  
  @Override
  public String getRuleId()
  {
    return entity.getRuleId();
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    entity.setRuleId(ruleId);
  }
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    return entity.getKlassInstanceIds();
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    entity.setKlassInstanceIds(klassInstanceIds);
  }
  
  @Override
  public List<ITagInstance> getTags()
  {
    return entity.getTags();
  }
  
  @Override
  public void setTags(List<ITagInstance> tags)
  {
    entity.setTags(tags);
  }
  
  @Override
  public List<IAttributeInstance> getAttributes()
  {
    return entity.getAttributes();
  }
  
  @Override
  public void setAttributes(List<IAttributeInstance> attributes)
  {
    entity.setAttributes(attributes);
  }
  
  @Override
  public String getOrganizationId()
  {
    return entity.getOrganizationId();
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    entity.setOrganizationId(organizationId);
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return entity.getPhysicalCatalogId();
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    entity.setPhysicalCatalogId(physicalCatalogId);
  }
  
  @Override
  public String getEndpointId()
  {
    return entity.getEndpointId();
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    entity.setEndpointId(endpointId);
  }
  
  @Override
  public String getCreatedBy()
  {
    return entity.getCreatedBy();
  }
  
  @Override
  public void setCreatedBy(String createdBy)
  {
    entity.setCreatedBy(createdBy);
  }
  
  @Override
  public Long getCreatedOn()
  {
    return entity.getCreatedOn();
  }
  
  @Override
  public void setCreatedOn(Long createdOn)
  {
    entity.setCreatedOn(createdOn);
  }
  
  /*@Override
  public String getOwner()
  {
    // TODO Auto-generated method stub
    return null;
  }*/
  
  /*@Override
  public void setOwner(String owner)
  {
    // TODO Auto-generated method stub
  
  }*/
  
  @Override
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
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
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
  
  @Override
  public Long getklassInstanceCount()
  {
    // TODO Auto-generated method stub
    return klassInstanceCount;
  }
  
  @Override
  public void setKlassInstanceCount(Long klassInstanceCount)
  {
    this.klassInstanceCount = klassInstanceCount;
  }
  
  @Override
  public Boolean getIsSearchable()
  {
    return entity.getIsSearchable();
  }
  
  @Override
  public void setIsSearchable(Boolean isSearchable)
  {
    entity.setIsSearchable(isSearchable);
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
