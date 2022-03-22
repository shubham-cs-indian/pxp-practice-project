package com.cs.core.runtime.interactor.model.goldenrecord;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValue;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface ICreateGoldenRecordKlassInstanceModel extends IModel {
  
  public static final String ATTRIBUTES                = "attributes";
  public static final String DEPENDENT_ATTRIBUTES      = "dependentAttributes";
  public static final String TAGS                      = "tags";
  public static final String ORGANIZATION_ID           = "organizationId";
  public static final String LOGICAL_CATALOG_ID        = "logicalCatalogId";
  public static final String PHYSICAL_CATALOG_ID       = "physicalCatalogId";
  public static final String ENDPOINT_ID               = "endpointId";
  public static final String SYSTEM_ID                 = "systemId";
  public static final String DEFAULT_ASSET_INSTANCE_ID = "defaultAssetInstanceId";
  public static final String BASETYPE                  = "baseType";
  public static final String LANGUAGE_CODES            = "languageCodes";
  public static final String CREATION_LANGUAGE         = "creationLanguage";
  
  // key : attributeId
  public Map<String, List<IConflictingValue>> getAttributes();
  
  public void setAttributes(Map<String, List<IConflictingValue>> attributes);
  
  // key:languageCode
  public Map<String, Map<String, List<IConflictingValue>>> getDependentAttributes();
  
  public void setDependentAttributes(
      Map<String, Map<String, List<IConflictingValue>>> dependentAttributes);
  
  // Key : tagId
  public Map<String, List<IConflictingValue>> getTags();
  
  public void setTags(Map<String, List<IConflictingValue>> tags);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getLogicalCatalogId();
  
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getDefaultAssetInstanceId();
  
  public void setDefaultAssetInstanceId(String defaultAssetInstanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getCreationLanguage();
  
  public void setCreationLanguage(String creationLanguage);
}
