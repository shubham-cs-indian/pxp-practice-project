package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IKlassInstanceTypeModel extends IModel {
  
  public static final String TYPES                     = "types";
  public static final String TAXONOMY_IDS              = "taxonomyIds";
  public static final String SELECTED_TAXONOMY_IDS     = "selectedTaxonomyIds";
  public static final String TAG_ID_TAG_VALUE_IDS_MAP  = "tagIdTagValueIdsMap";
  public static final String ORAGANIZATION_ID          = "organizationId";
  public static final String ENDPOINT_ID               = "endpointId";
  public static final String PHYSICAL_CATALOG_ID       = "physicalCatalogId";
  public static final String LANGUAGE_CODES            = "languageCodes";
  public static final String BASETYPE                  = "baseType";
  public static final String NAME                      = "name";
  public static final String PARENT_KLASS_IDS          = "parentKlassIds";
  public static final String PARENT_TAXONOMY_IDS       = "parentTaxonomyIds";
  
  public Collection<String> getTypes();
  
  public void setTypes(Collection<String> types);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getSelectedTaxonomyIds();
  
  public void setSelectedTaxonomyIds(List<String> selectedTaxonomyIds);
  
  public Map<String, List<String>> getTagIdTagValueIdsMap();
  
  public void setTagIdTagValueIdsMap(Map<String, List<String>> tagIdTagValueIdsMap);
  
  public String getEndpointId();
  
  public void setEndpointId(String endpointId);
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getOrganizationId();
  
  public void setOrganizationId(String organizationId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public String getName();
  
  public void setName(String name);
  
  public List<String> getParentKlassIds();
  
  public void setParentKlassIds(List<String> parentKlassIds);
  
  public List<String> getParentTaxonomyIds();
  
  public void setParentTaxonomyIds(List<String> parentTaxonomyIds);
}
