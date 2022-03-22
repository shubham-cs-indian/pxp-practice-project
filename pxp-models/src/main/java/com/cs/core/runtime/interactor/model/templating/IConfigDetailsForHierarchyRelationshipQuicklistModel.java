package com.cs.core.runtime.interactor.model.templating;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Set;

public interface IConfigDetailsForHierarchyRelationshipQuicklistModel extends IModel {
  
  public static final String KLASS_TYPE             = "klassType";
  public static final String ENTITIES               = "entities";
  public static final String TAXONOMY_IDS_HAVING_RP = "taxonomyIdsHavingRP";
  public static final String CATEGORY_INFO          = "categoryInfo";
  public static final String KLASSES_IDS            = "klassesIds";
  public static final String ALLOWED_TYPES          = "allowedTypes";
  
  public String getKlassType();
  
  public void setKlassType(String klassType);
  
  public Set<String> getEntities();
  
  public void setEntities(Set<String> entities);
  
  public Set<String> getTaxonomyIdsHavingRP();
  
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);
  
  public List<IConfigEntityTreeInformationModel> getCategoryInfo();
  
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo);
  
  public List<String> getKlassesIds();
  
  public void setKlassesIds(List<String> klassesIds);
  
  public Set<String> getAllowedTypes();
  
  public void setAllowedTypes(Set<String> allowedTypes);
}
