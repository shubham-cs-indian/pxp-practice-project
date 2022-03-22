package com.cs.core.runtime.interactor.entity.contentidentifier;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IContextStructureInfo extends IEntity {
  
  public static final String STRUCTURE_ID      = "structureId";
  public static final String ROOT_STRUCTURE_ID = "rootStructureId";
  
  public String getStructureId();
  
  public void setStructureId(String structureId);
  
  public String getRootStructureId();
  
  public void setRootStructureId(String rootStructureId);
}
