package com.cs.core.config.interactor.model.attribute;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IBooleanReturnModel extends IModel {
  
  boolean getDuplicateStatus();
  
  void setDuplicateStatus(boolean duplicateStatus);
  
}
