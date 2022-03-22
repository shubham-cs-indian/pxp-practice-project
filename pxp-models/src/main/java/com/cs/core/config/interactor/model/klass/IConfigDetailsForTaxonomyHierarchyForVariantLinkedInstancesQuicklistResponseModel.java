package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;

import java.util.Set;

public interface IConfigDetailsForTaxonomyHierarchyForVariantLinkedInstancesQuicklistResponseModel
    extends IModel {
  
  public static final String CATEGORY_INFO          = "categoryInfo";
  public static final String ENTITIES               = "entities";
  public static final String KLASS_IDS_HAVING_RP    = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP = "taxonomyIdsHavingRP";
  
  public ICategoryInformationModel getCategoryInfo();
  
  public void setCategoryInfo(ICategoryInformationModel categoryInfo);
  
  public Set<String> getKlassIdsHavingRP();
  
  public void setKlassIdsHavingRP(Set<String> klassIdsHavingRP);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
}
