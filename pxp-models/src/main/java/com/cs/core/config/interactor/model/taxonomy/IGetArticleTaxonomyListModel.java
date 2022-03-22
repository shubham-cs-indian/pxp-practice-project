package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetArticleTaxonomyListModel extends IModel {
  
  public static final String TAXONOMY_LIST = "taxonomyList";
  
  public List<IConfigEntityInformationModel> getTaxonomyList();
  
  public void setTaxonomyList(List<IConfigEntityInformationModel> taxonomyList);
}
