package com.cs.core.config.interactor.entity.visualattribute;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterPropertyEntity;
import com.cs.core.config.interactor.entity.structure.IStructure;

public interface IVisualAttributeFrameStructure extends IStructure, IConfigMasterPropertyEntity {
  
  public String getReferenceId();
  
  public void setReferenceId(String referernceId);
  
  public String getVariantOf();
  
  public void setVariantOf(String variantOf);
}
