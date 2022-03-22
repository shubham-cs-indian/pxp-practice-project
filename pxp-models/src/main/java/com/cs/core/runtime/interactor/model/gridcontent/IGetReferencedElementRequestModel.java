package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetReferencedElementRequestModel extends IModel {
  
  public static final String KLASS_IDS    = "klassIds";
  public static final String TAXONOMY_IDS = "taxonomyIds";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
}
