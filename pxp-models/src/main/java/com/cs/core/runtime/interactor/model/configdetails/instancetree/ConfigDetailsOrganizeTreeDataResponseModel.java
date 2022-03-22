package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ConfigDetailsOrganizeTreeDataResponseModel
    extends ConfigDetailsKlassTaxonomyTreeResponseModel
    implements IConfigDetailsOrganizeTreeDataResponseModel {
  
  private static final long                 serialVersionUID = 1L;
  
  protected List<ICategoryInformationModel> taxonomyInfo;
  
  @Override
  public List<ICategoryInformationModel> getKlassTaxonomyInfo()
  {
    if (taxonomyInfo == null) {
      taxonomyInfo = new ArrayList<>();
    }
    return taxonomyInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = CategoryInformationModel.class)
  public void setKlassTaxonomyInfo(List<ICategoryInformationModel> taxonomyInfo)
  {
    this.taxonomyInfo = taxonomyInfo;
  }
  
}
