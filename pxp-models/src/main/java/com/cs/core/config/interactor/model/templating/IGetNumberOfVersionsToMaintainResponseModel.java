package com.cs.core.config.interactor.model.templating;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetNumberOfVersionsToMaintainResponseModel extends IModel {
  
  String NUMBER_OF_VERSIONS_TO_MAINTAIN = "numberOfVersionsToMaintain";

  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
}
