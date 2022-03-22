package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetConfigDetailsForSaveRelationshipInstancesRequestModel extends IModel {
  
  public static final String KLASS_IDS               = "klassIds";
  public static final String TAXONOMY_IDS            = "taxonomyIds";
  public static final String RELATIONSHIP_IDS        = "relationshipIds";
  public static final String NATURE_RELATIONSHIP_IDS = "natureRelationshipIds";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public List<String> getRelationshipIds();
  
  public void setRelationshipIds(List<String> relationshipIds);
  
  public List<String> getNatureRelationshipIds();
  
  public void setNatureRelationshipIds(List<String> natureRelationshipIds);
}
