package com.cs.core.runtime.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetTaxonomyTreeLeafIdsStrategyModel extends IModel {
  
  public static final String PARENT_TAXONOMY_ID = "parentTaxonomyId";
  public static final String LEAF_IDS           = "leafIds";
  
  public String getParentTaxonomyId();
  
  public void setParentTaxonomyId(String parentTaxonomyId);
  
  public List<String> getLeafIds();
  
  public void setLeafIds(List<String> leafIds);
}
