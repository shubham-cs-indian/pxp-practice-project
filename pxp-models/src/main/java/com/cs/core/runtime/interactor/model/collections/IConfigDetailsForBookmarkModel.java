package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTreeInformationModel;

public interface IConfigDetailsForBookmarkModel extends IModel {
  
  public static final String TAXONOMY_TREE = "taxonomyTree";
  
  public IIdLabelTreeInformationModel getTaxonomyTree();
  
  public void setTaxonomyTree(IIdLabelTreeInformationModel taxonomyTree);
}
