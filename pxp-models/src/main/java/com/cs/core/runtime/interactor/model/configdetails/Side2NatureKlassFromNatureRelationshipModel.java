package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class Side2NatureKlassFromNatureRelationshipModel extends  ConfigEntityInformationModel implements ISide2NatureKlassFromNatureRelationshipModel {
  
  private static final long serialVersionUID = 1L;
  
  private Boolean selectVariant;
  
  @Override
  public Boolean getSelectVariant()
  {
    return selectVariant;
  }

  @Override
  public void setSelectVariant(Boolean selectVariant)
  {
    this.selectVariant = selectVariant;
  }

 
}
