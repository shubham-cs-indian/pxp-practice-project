package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface ITypesGetForInstancesRequestModel extends IModel {
  
  String CONTENT_INFO                   = "contentInfo";
  String IS_DELETE_FROM_ARCHIVAL_PORTAL = "isDeleteFromArchivalPortal";
  
  public List<IIdAndTypeModel> getContentInfo();
  
  public void setContentInfo(List<IIdAndTypeModel> contentInfo);
  
  Boolean getIsDeleteFromArchivalPortal();
  
  void setIsDeleteFromArchivalPortal(Boolean isDeleteFromArchivalPortal);
}
