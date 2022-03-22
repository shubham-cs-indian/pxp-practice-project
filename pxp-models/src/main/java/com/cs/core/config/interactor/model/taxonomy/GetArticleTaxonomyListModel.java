package com.cs.core.config.interactor.model.taxonomy;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class GetArticleTaxonomyListModel implements IGetArticleTaxonomyListModel {
  
  protected List<IConfigEntityInformationModel> taxonomyList;
  
  @Override
  public List<IConfigEntityInformationModel> getTaxonomyList()
  {
    
    return taxonomyList;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setTaxonomyList(List<IConfigEntityInformationModel> taxonomyList)
  {
    this.taxonomyList = taxonomyList;
  }
}
