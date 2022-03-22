package com.cs.core.config.interactor.model.configdetails;


import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetCloneWizardRequestModel extends IModel {
  
  public String ID                     = "id";
  public String SELECTED_TYPES_IDS     = "selectedTypesIds";
  public String IS_FOR_LINKED_VARIANT  = "isForLinkedVariant";
  public String PARENT_NATURE_KLASS_ID = "parentNatureKlassId";
  public String SELECTED_TAXONOMY_IDS  = "selectedTaxonomyIds";
  
  public String getId();
  public void setId(String id);
  
  public List<String> getSelectedTypesIds();
  public void setSelectedTypesIds(List<String> types);
  
  public List<String> getSelectedTaxonomyIds();
  public void setSelectedTaxonomyIds(List<String> types);
  
  public Boolean getIsForLinkedVariant();
  public void setIsForLinkedVariant(Boolean isForLinkedVariant);
  
  public String getParentNatureKlassId();
  public void setParentNatureKlassId(String parentNatureKlasId);
  
}