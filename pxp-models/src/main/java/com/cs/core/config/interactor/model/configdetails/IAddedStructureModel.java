package com.cs.core.config.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.structure.IStructure;

public interface IAddedStructureModel extends IStructure, IConfigModel {
  
  public static final String PARENT_ID = "parentId";
  
  public String getParentId();
  
  public void setParentId(String parentId);
}
