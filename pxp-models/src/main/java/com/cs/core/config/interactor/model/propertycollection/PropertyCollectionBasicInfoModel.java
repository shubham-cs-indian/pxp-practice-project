package com.cs.core.config.interactor.model.propertycollection;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class PropertyCollectionBasicInfoModel extends ConfigEntityInformationModel
    implements IPropertyCollectionBasicInfoModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isForXRay;
  
  @Override
  public Boolean getIsForXRay()
  {
    return isForXRay;
  }
  
  @Override
  public void setIsForXRay(Boolean isForXRay)
  {
    this.isForXRay = isForXRay;
  }
}
