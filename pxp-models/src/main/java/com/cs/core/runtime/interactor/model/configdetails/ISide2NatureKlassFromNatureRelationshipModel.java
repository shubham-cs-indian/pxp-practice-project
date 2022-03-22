package com.cs.core.runtime.interactor.model.configdetails;

public interface ISide2NatureKlassFromNatureRelationshipModel extends IConfigEntityInformationModel {

  public static final String SELECT_VARIANT  = "selectVariant";
  
  public void setSelectVariant(Boolean selectVariant);
  public Boolean getSelectVariant();
  
}
