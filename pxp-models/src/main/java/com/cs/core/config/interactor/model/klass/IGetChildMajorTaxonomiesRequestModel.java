package com.cs.core.config.interactor.model.klass;

import java.util.List;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

public interface IGetChildMajorTaxonomiesRequestModel extends IConfigGetAllRequestModel {
  
  public static final String TAXONOMY_ID      = "taxonomyId";
  public static final String TAXONOMY_TYPES   = "taxonomyTypes";
  
  public String getTaxonomyId();  
  public void setTaxonomyId(String taxonomyId);
  
  public List<String> getTaxonomyTypes();  
  public void setTaxonomyTypes(List<String> taxonomyTypes);
}
