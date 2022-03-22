package com.cs.core.runtime.interactor.model.instancetree;

import com.cs.core.config.interactor.model.instancetree.IGetInstanceTreeRequestModel;

public interface IGetBookmarkRequestModel extends IGetInstanceTreeRequestModel {
  
  String CLICKED_TAXONOMY_ID = "clickedTaxonomyId";
  
  String getClickedTaxonomyId();
  
  void setClickedTaxonomyId(String clickedTaxonomyId);
}