package com.cs.core.config.interactor.model;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetCloneWizardForRequestStrategyModel extends IModel {
  
  public String TYPEIDS_TO_CLONE       = "typesIdsToClone";
  public String TAXONOMY_IDS_TO_CLONE  = "taxonomyIdsToClone";
  public String TYPE_IDS               = "typeIds";
  public String TAXONOMY_IDS           = "taxonomyIds";
  public String IS_FOR_LINKED_VARIANT  = "isForLinkedVariant";
  public String PARENT_NATURE_KLASS_ID = "parentNatureKlassId";
  
  public List<String> getTypesIdsToClone();
  
  public void setTypesIdsToClone(List<String> typesIdsClone);
  
  public List<String> getTaxonomyIdsToClone();
  
  public void setTaxonomyIdsToClone(List<String> taxonomyIdsClone);
  
  public List<String> getTypesIds();
  
  public void setTypesIds(List<String> typeIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public Boolean getIsForLinkedVariant();
  
  public void setIsForLinkedVariant(Boolean isForLinkedVariant);
  
  public String getParentNatureKlassId();
  
  public void setParentNatureKlassId(String parentNatureKlasId);
}
