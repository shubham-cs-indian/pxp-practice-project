package com.cs.core.runtime.interactor.model.instancetree;

import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;

@SuppressWarnings("rawtypes")
public interface INewInstanceTreeRequestModel extends INewInstanceTreeParentRequestModel {
  
  public static final String KLASS_IDS_HAVING_RP         = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP      = "taxonomyIdsHavingRP";
  public static final String ATTRIBUTES                  = "attributes";
  public static final String TAGS                        = "tags";
  public static final String TRANSLATABLE_ATTRIBUTE_IDS  = "translatableAttributeIds";
  public static final String SPECIAL_USECASE_FILTERS     = "specialUsecaseFilters";
  public static final String KPI_ID                      = "kpiId";
  public static final String IDS_TO_EXCLUDE              = "idsToExclude";
  public static final String MAJOR_TAXONOMY_IDS          = "majorTaxonomyIds";
  public static final String XRAY_ENABLED                = "xrayEnabled";
  

  public Set<String> getKlassIdsHavingRP();
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public Set<String> getTaxonomyIdsHavingRP();
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public List<IPropertyInstanceFilterModel> getAttributes();
  public void setAttributes(List<IPropertyInstanceFilterModel> attributes);

  public List<IPropertyInstanceFilterModel> getTags();
  public void setTags(List<IPropertyInstanceFilterModel> tags);
  
  public List<String> getTranslatableAttributeIds();
  public void setTranslatableAttributeIds(List<String> translatableAttributesIds);
  
  //All filters other than attributes and tags will come in special filters. like ruleVoilation, assetExpiry
  public List<ISpecialUsecaseFiltersModel> getSpecialUsecaseFilters();
  public void setSpecialUsecaseFilters(List<ISpecialUsecaseFiltersModel> specialUsecaseFilters);
  
  
  public String getKpiId();
  public void setKpiId(String kpiId);
  
  public List<String> getIdsToExclude();
  public void setIdsToExclude(List<String> idsToExclude);
  
  public List<String> getMajorTaxonomyIds();
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds);
  
  Boolean getXrayEnabled();
  void setXrayEnabled(Boolean xrayEnabled);

}
