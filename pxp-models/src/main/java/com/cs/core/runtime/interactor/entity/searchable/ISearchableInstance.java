package com.cs.core.runtime.interactor.entity.searchable;

import com.cs.core.runtime.interactor.entity.configuration.base.IRuntimeEntity;
import com.cs.core.runtime.interactor.entity.message.IMessageInformation;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.entity.tag.ISearchableTagInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import java.util.List;

public interface ISearchableInstance extends IRuntimeEntity {
  
  public static final String BASETYPE              = "baseType";
  public static final String TAXONOMY_IDS          = "taxonomyIds";
  public static final String SELECTED_TAXONOMY_IDS = "selectedTaxonomyIds";
  public static final String TYPES                 = "types";
  public static final String TAGS                  = "tags";
  public static final String ATTRIBUTES            = "attributes";
  public static final String ORGANIZATION_ID       = "organizationId";
  public static final String ENDPOINT_ID           = "endpointId";
  public static final String PHYSICAL_CATALOG_ID   = "physicalCatalogId";
  public static final String LOGICAL_CATALOG_ID    = "logicalCatalogId";
  public static final String SYSTEM_ID             = "systemId";
  public static final String DATA_GOVERNANCE       = "dataGovernance";
  public static final String KLASS_INSTANCE_ID     = "klassInstanceId";
  public static final String LANGUAGE_CODES        = "languageCodes";
  public static final String PARENT_ID             = "parentId";
  public static final String IS_EMBEDDED           = "isEmbedded";
  public static final String CONTEXT               = "context";
  public static final String MESSAGES              = "messages";
  
  public List<ISearchableTagInstance> getTags();
  
  public void setTags(List<ISearchableTagInstance> tags);
  
  public List<ISearchableAttributeInstance> getAttributes();
  
  public void setAttributes(List<ISearchableAttributeInstance> attributeInstances);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getTypes();
  
  public void setTypes(List<String> types);
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public List<String> getSelectedTaxonomyIds();
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public String getSystemId();
  
  public void setSystemId(String systemId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getLogicalCatalogId();
  
  public void setLogicalCatalogId(String logicalCatalogId);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public List<IStatistics> getDataGovernance();
  
  public void setDataGovernance(List<IStatistics> dataGovernance);
  
  public IMessageInformation getMessages();
  
  public void setMessages(IMessageInformation messages);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String masterContentId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getParentId();
  
  public void setParentId(String parentId);
  
  public Boolean getIsEmbedded();
  
  public void setIsEmbedded(Boolean isEmbedded);
  
  public IContextInstance getContext();
  
  public void setContext(IContextInstance context);
}
