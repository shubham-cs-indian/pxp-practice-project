package com.cs.core.config.interactor.model.marketingbundle;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassesAndTaxonomiesModel extends IModel {
  
  public static final String KLASS_IDS    = "klassIds";
  public static final String TAXONOMY_IDS = "taxonomyIds";
  public static final String USER_ID      = "userId";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public List<String> getTaxonomyIds();
  
  public void setTaxonomyIds(List<String> taxonomyIds);
  
  public String getUserId();
  
  public void setUserId(String userId);
}
