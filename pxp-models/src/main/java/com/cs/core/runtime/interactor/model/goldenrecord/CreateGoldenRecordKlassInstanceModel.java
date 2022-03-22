package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.model.attribute.AttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.attribute.DependentAttributesConflictMapCustomDeserializer;
import com.cs.core.runtime.interactor.model.tag.TagConflictMapCustomDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGoldenRecordKlassInstanceModel implements ICreateGoldenRecordKlassInstanceModel {
  
  private static final long                                   serialVersionUID = 1L;
  protected Map<String, List<IConflictingValue>>              attributes;
  protected Map<String, List<IConflictingValue>>              tags;
  protected Map<String, Map<String, List<IConflictingValue>>> dependentAttributes;
  protected String                                            organizationId;
  protected String                                            logicalCatalogId;
  protected String                                            physicalCatalogId;
  protected String                                            endpointId;
  protected String                                            systemId;
  protected String                                            defaultAssetInstanceId;
  protected String                                            baseType;
  protected List<String>                                      languageCodes;
  protected String                                            creationLanguage;
  
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
  public String getLogicalCatalogId()
  {
    return logicalCatalogId;
  }
  
  @Override
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    this.logicalCatalogId = logicalCatalogId;
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
  public String getSystemId()
  {
    return systemId;
  }
  
  @Override
  public void setSystemId(String systemId)
  {
    this.systemId = systemId;
  }
  
  @Override
  public String getDefaultAssetInstanceId()
  {
    return defaultAssetInstanceId;
  }
  
  @Override
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId)
  {
    this.defaultAssetInstanceId = defaultAssetInstanceId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public Map<String, List<IConflictingValue>> getAttributes()
  {
    if (attributes == null) {
      attributes = new HashMap<>();
    }
    return attributes;
  }
  
  @Override
  @JsonDeserialize(contentUsing = AttributesConflictMapCustomDeserializer.class)
  public void setAttributes(Map<String, List<IConflictingValue>> attributes)
  {
    this.attributes = attributes;
  }
  
  @Override
  public Map<String, Map<String, List<IConflictingValue>>> getDependentAttributes()
  {
    return dependentAttributes;
  }
  
  @Override
  @JsonDeserialize(contentUsing = DependentAttributesConflictMapCustomDeserializer.class)
  public void setDependentAttributes(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributes)
  {
    this.dependentAttributes = dependentAttributes;
  }
  
  @Override
  public Map<String, List<IConflictingValue>> getTags()
  {
    return tags;
  }
  
  @Override
  @JsonDeserialize(contentUsing = TagConflictMapCustomDeserializer.class)
  public void setTags(Map<String, List<IConflictingValue>> tags)
  {
    this.tags = tags;
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  @Override
  public String getCreationLanguage()
  {
    return creationLanguage;
  }
  
  @Override
  public void setCreationLanguage(String creationLanguage)
  {
    this.creationLanguage = creationLanguage;
  }
}
