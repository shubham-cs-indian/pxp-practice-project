package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IGridEditVariantContextInformationModel extends IConfigEntityInformationModel {
  
  public static final String IS_STANDARD = "isStandard";
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
}
