package com.cs.core.config.interactor.model.configdetails;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.datarule.ICategoryTreeInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CategoryTreeInformationModel implements ICategoryTreeInformationModel {
  
  protected List<IConfigEntityTreeInformationModel> categoryInfo;
  protected List<String>                            klassesIds;
  
  @Override
  public List<IConfigEntityTreeInformationModel> getCategoryInfo()
  {
    if (categoryInfo == null) {
      categoryInfo = new ArrayList<>();
    }
    return categoryInfo;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityTreeInformationModel.class)
  @Override
  public void setCategoryInfo(List<IConfigEntityTreeInformationModel> categoryInfo)
  {
    this.categoryInfo = categoryInfo;
  }
  
  @Override
  public List<String> getKlassesIds()
  {
    if (klassesIds == null) {
      klassesIds = new ArrayList<>();
    }
    return klassesIds;
  }
  
  @Override
  public void setKlassesIds(List<String> klassesIds)
  {
    this.klassesIds = klassesIds;
  }
}
