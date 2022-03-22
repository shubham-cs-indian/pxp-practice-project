package com.cs.core.runtime.interactor.entity.goldenrecord;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import com.cs.core.runtime.interactor.entity.propertyinstance.IAttributeInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import java.util.List;

public interface IGoldenRecordRuleBucketInstance extends IRuntimeEntity {
  
  public static final String RULE_ID             = "ruleId";
  public static final String KLASS_INSTANCE_IDS  = "klassInstanceIds";
  public static final String ATTRIBUTES          = "attributes";
  public static final String TAGS                = "tags";
  public static final String ORGANIZATION_ID     = "organizationId";
  public static final String ENDPOINT_ID         = "endpointId";
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String IS_SEARCHABLE       = "isSearchable";
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public List<ITagInstance> getTags();
  
  public void setTags(List<ITagInstance> tags);
  
  public List<IAttributeInstance> getAttributes();
  
  public void setAttributes(List<IAttributeInstance> attributes);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public Boolean getIsSearchable();
  
  public void setIsSearchable(Boolean isSearchable);
}
