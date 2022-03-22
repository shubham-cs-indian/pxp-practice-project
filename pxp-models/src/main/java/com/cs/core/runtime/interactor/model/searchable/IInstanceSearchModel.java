package com.cs.core.runtime.interactor.model.searchable;

import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.filter.IFilterValueModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;

import java.util.List;

public interface IInstanceSearchModel extends IModel {
  
  public static final String ID                    = "id";
  public static final String FROM                  = "from";
  public static final String SIZE                  = "size";
  public static final String ATTRIBUTES            = "attributes";
  public static final String TAGS                  = "tags";
  public static final String ROLES                 = "roles";
  public static final String ALL_SEARCH            = "allSearch";
  public static final String SORT_OPTIONS          = "sortOptions";
  public static final String SELECTED_ROLES        = "selectedRoles";
  public static final String SELECTED_TYPES        = "selectedTypes";
  public static final String SELECTED_TAXONOMY_IDS = "selectedTaxonomyIds";
  public static final String PARENT_TAXONOMY_ID    = "parentTaxonomyId";
  public static final String MODULE_ID             = "moduleId";
  public static final String IS_RED                = "isRed";
  public static final String IS_ORANGE             = "isOrange";
  public static final String IS_YELLOW             = "isYellow";
  public static final String IS_GREEN              = "isGreen";
  public static final String ORGANIZATION_ID       = "organizationId";
  public static final String ENDPOINT_ID           = "endpointId";
  public static final String PHYSICAL_CATALOG_ID   = "physicalCatalogId";
  public static final String PORTAL_ID             = "portalId";
  public static final String LOGICAL_CATALOG_ID    = "logicalCatalogId";
  public static final String SYSTEM_ID             = "systemId";
  public static final String X_RAY_ATTRIBUTES      = "xrayAttributes";
  public static final String X_RAY_TAGS            = "xrayTags";
  public static final String EXPIRY_STATUS         = "expiryStatus";
  
  public String getId();
  
  public void setId(String id);
  
  public Integer getFrom();
  
  public void setFrom(Integer from);
  
  public Integer getSize();
  
  public void setSize(Integer size);
  
  public List<IRoleModel> getRoles();
  
  public void setRoles(List<IRoleModel> roles);
  
  public List<? extends IPropertyInstanceFilterModel> getAttributes();
  
  public void setAttributes(List<? extends IPropertyInstanceFilterModel> attributes);
  
  public List<? extends IPropertyInstanceFilterModel> getTags();
  
  public void setTags(List<? extends IPropertyInstanceFilterModel> tags);
  
  public String getAllSearch();
  
  public void setAllSearch(String allSearch);
  
  public List<? extends IFilterValueModel> getSelectedRoles();
  
  public void setSelectedRoles(List<? extends IFilterValueModel> selectedRoles);
  
  public List<String> getSelectedTypes();
  
  public void setSelectedTypes(List<String> selectedTypes);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public String getParentTaxonomyId();
  
  public void setParentTaxonomyId(String parentTaxonomyId);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
  
  public Boolean getIsRed();
  
  public void setIsRed(Boolean isRed);
  
  public Boolean getIsOrange();
  
  public void setIsOrange(Boolean isOrange);
  
  public Boolean getIsYellow();
  
  public void setIsYellow(Boolean isYellow);
  
  public Boolean getIsGreen();
  
  public void setIsGreen(Boolean isGreen);
  
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
  
  public List<String> getXRayAttributes();
  
  public void setXRayAttributes(List<String> xRayAttributes);
  
  public List<String> getXRayTags();
  
  public void setXRayTags(List<String> xRayTags);
  
  public List<ISortModel> getSortOptions();
  
  public void setSortOptions(List<ISortModel> sortOptions);
  
  public Integer getExpiryStatus();
  
  public void setExpiryStatus(Integer expiryStatus);
}
