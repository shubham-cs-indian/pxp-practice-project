package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;

public interface ITaxonomyInformationModel
    extends ICategoryInformationModel, ITaxonomyConfigEntityInformationModel {
  public static final String IS_LAST_NODE = "isLastNode";
  
  public Boolean getIsLastNode();
  
  public void setIsLastNode(Boolean isLastNode);
}
