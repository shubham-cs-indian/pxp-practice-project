package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;

public class GridEditVariantContextInformationModel extends ConfigEntityInformationModel
    implements IGridEditVariantContextInformationModel {
  
  private static final long serialVersionUID = 1L;
  
  protected Boolean         isStandard;
  protected Boolean         isCloneContext   = false;
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
}
