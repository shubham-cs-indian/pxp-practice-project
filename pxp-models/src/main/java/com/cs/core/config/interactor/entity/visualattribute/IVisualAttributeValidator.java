package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.structure.IStructureValidator;

public interface IVisualAttributeValidator extends IStructureValidator {
  
  public Boolean getShouldVersion();
  
  public void setShouldVersion(Boolean shouldVersion);
}
