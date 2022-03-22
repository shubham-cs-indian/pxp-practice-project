package com.cs.core.config.interactor.model.attributiontaxonomy;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBulkCreateTaxonomyListResponseModel extends IModel {
  
  public static final String LIST                  = "list";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  
  public List<IGetAttributionTaxonomyModel> getList();
  
  public void setList(List<IGetAttributionTaxonomyModel> list);
  
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies);
  
}
