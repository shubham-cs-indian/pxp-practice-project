package com.cs.core.runtime.interactor.model.configuration;

import java.util.Set;

import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;


public interface ILinkedTaxonomiesPropertyModel extends IPropertyInstance, IModel {
  
  String SELECTED_TAXONOMIES = "selectedTaxonomies";
  
  Set<String> getSelectedTaxonomies();
  void setSelectedTaxonomies(Set<String> selectedTaxonomies);
}
