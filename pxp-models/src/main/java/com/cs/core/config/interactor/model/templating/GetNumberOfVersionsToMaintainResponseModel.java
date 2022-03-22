package com.cs.core.config.interactor.model.templating;

public class GetNumberOfVersionsToMaintainResponseModel
    implements IGetNumberOfVersionsToMaintainResponseModel {
  
  private static final long serialVersionUID = 1L;
  protected Integer         numberOfVersionsToMaintain;
  
  @Override
  public Integer getNumberOfVersionsToMaintain()
  {
    return numberOfVersionsToMaintain;
  }
  
  @Override
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain)
  {
    this.numberOfVersionsToMaintain = numberOfVersionsToMaintain;
  }
  
}
