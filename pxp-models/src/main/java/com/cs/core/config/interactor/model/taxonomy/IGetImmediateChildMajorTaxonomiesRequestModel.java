package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;

import java.util.List;

public interface IGetImmediateChildMajorTaxonomiesRequestModel extends IConfigGetAllRequestModel {
  
  public static final String TAXONOMY_ID    = "taxonomyId";
  public static final String TAXONOMY_TYPES = "taxonomyTypes";
  
  public String getTaxonomyId();
  
  public void setTaxonomyId(String taxonomyId);
  
  public List<String> getTaxonomyTypes();
  
  public void setTaxonomyTypes(List<String> taxonomyTypes);
}
