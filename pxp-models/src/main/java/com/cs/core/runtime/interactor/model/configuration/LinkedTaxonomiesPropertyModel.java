package com.cs.core.runtime.interactor.model.configuration;

import java.util.HashSet;
import java.util.Set;

import com.cs.core.runtime.interactor.entity.propertyinstance.PropertyInstance;

public class LinkedTaxonomiesPropertyModel extends PropertyInstance
    implements ILinkedTaxonomiesPropertyModel {
  
  private static final long serialVersionUID = 1L;

  protected Set<String> selectedTaxonomies = new HashSet<>();
  
  @Override
  public Set<String> getSelectedTaxonomies()
  {
    return this.selectedTaxonomies;
  }
  
  @Override
  public void setSelectedTaxonomies(Set<String> selectedTaxonomies)
  {
    this.selectedTaxonomies = selectedTaxonomies;
  }
  
}
