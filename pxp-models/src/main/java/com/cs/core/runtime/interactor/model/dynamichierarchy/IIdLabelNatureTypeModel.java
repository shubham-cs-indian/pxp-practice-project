package com.cs.core.runtime.interactor.model.dynamichierarchy;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IIdLabelNatureTypeModel extends IConfigEntityInformationModel  {
  
  public static final String IS_NATURE = "isNature";
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);
}
