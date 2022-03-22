package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;
import java.util.Set;

import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;

public interface IConfigDetailsKlassTaxonomyTreeResponseModel extends IGetKlassTaxonomyTreeResponseModel {
  
  public static final String KLASS_IDS_HAVING_RP        = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP     = "taxonomyIdsHavingRP";
  public static final String ALLOWED_ENTITIES           = "allowedEntities";
  public static final String SEARCHABLE_ATTRIBUTE_IDS   = "searchableAttributeIds";
  public static final String TRANSLATABLE_ATTRIBUTE_IDS = "translatableAttributeIds";
  public static final String MAJOR_TAXONOMY_IDS         = "majorTaxonomyIds";
  public static final String TOTAL_CHILDREN_COUNT       = "totalChildrenCount";

  public Set<String> getTaxonomyIdsHavingRP();
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public Set<String> getKlassIdsHavingRP();
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public List<String> getAllowedEntities();
  public void setAllowedEntities(List<String> allowedEntities);
  
  public List<String> getSearchableAttributeIds();
  public void setSearchableAttributeIds(List<String> searchableAttributes);
  
  public List<String> getTranslatableAttributeIds();
  public void setTranslatableAttributeIds(List<String> translatableAttributes);
  
  public List<String> getMajorTaxonomyIds();
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds);
  
  public Long getTotalChildrenCount();
  public void setTotalChildrenCount(Long count);
}
