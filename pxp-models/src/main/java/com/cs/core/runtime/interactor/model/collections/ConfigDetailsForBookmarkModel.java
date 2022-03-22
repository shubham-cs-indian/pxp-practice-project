package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTreeInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTreeInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsForBookmarkModel implements IConfigDetailsForBookmarkModel {
  
  private static final long              serialVersionUID = 1L;
  
  protected IIdLabelTreeInformationModel taxonomyTree;
  
  @Override
  public IIdLabelTreeInformationModel getTaxonomyTree()
  {
    return taxonomyTree;
  }
  
  @JsonDeserialize(as = IdLabelTreeInformationModel.class)
  @Override
  public void setTaxonomyTree(IIdLabelTreeInformationModel taxonomyTree)
  {
    this.taxonomyTree = taxonomyTree;
  }
}
