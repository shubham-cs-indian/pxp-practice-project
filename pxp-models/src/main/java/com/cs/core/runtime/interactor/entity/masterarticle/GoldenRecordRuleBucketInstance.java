package com.cs.core.runtime.interactor.entity.masterarticle;

import com.cs.core.runtime.interactor.entity.configuration.base.AbstractRuntimeEntity;
import com.cs.core.runtime.interactor.entity.goldenrecord.IGoldenRecordRuleBucketInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.AttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.ArrayList;
import java.util.List;

public class GoldenRecordRuleBucketInstance extends AbstractRuntimeEntity
    implements IGoldenRecordRuleBucketInstance {
  
  private static final long          serialVersionUID = 1L;
  protected List<IAttributeInstance> attributes;
  protected List<ITagInstance>       tags;
  protected String                   ruleId;
  protected List<String>             klassInstanceIds;
  protected String                   organizationId;
  protected String                   endpointId;
  protected String                   physicalCatalogId;
  protected Boolean                  isSearchable;
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
  
  @Override
  public List<ITagInstance> getTags()
  {
    if (tags == null) {
      tags = new ArrayList<>();
    }
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentAs = TagInstance.class)
  public void setTags(List<ITagInstance> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<IAttributeInstance> getAttributes()
  {
    if (attributes == null) {
      attributes = new ArrayList<>();
    }
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentAs = AttributeInstance.class)
  public void setAttributes(List<IAttributeInstance> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public String getOrganizationId()
  {
    return organizationId;
  }
  
  @Override
  public void setOrganizationId(String organizationId)
  {
    this.organizationId = organizationId;
  }
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getEndpointId()
  {
    return endpointId;
  }
  
  @Override
  public void setEndpointId(String endpointId)
  {
    this.endpointId = endpointId;
  }
  
  @Override
  public List<String> getKlassInstanceIds()
  {
    if (klassInstanceIds == null) {
      klassInstanceIds = new ArrayList<>();
    }
    return klassInstanceIds;
  }
  
  @Override
  public void setKlassInstanceIds(List<String> klassInstanceIds)
  {
    this.klassInstanceIds = klassInstanceIds;
  }
  
  @Override
  public Boolean getIsSearchable()
  {
    return isSearchable;
  }
  
  @Override
  public void setIsSearchable(Boolean isSearchable)
  {
    this.isSearchable = isSearchable;
  }
}
