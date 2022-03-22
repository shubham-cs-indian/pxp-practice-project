package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeleteKlassInstanceRequestModel extends IModel {
  
  String DELETE_REQUEST                 = "deleteRequest";
  String IS_DELETE_FROM_DI_CATALOG      = "isDeleteFromDICatalog";
  String IS_DELETE_FROM_ARCHIVAL_PORTAL = "isDeleteFromArchivalPortal";
  String IS_GOLDEN_RECORD_SOURCE_DELETE = "isGoldenRecordSourceDelete";
  
  public List<IDeleteKlassInstanceModel> getDeleteRequest();
  
  public void setDeleteRequest(List<IDeleteKlassInstanceModel> deleteRequest);
  
  Boolean getHasDeletePermission();
  
  void setHasDeletePermission(Boolean hasDeletePermission);
  
  Boolean getIsDeleteFromDICatalog();
  
  void setIsDeleteFromDICatalog(Boolean isDeleteFromDICatalog);
  
  Boolean getIsDeleteFromArchivalPortal();
  
  void setIsDeleteFromArchivalPortal(Boolean isDeleteFromArchivalPortal);
  
  public Boolean getIsGoldenRecordSourceDelete();
  
  public void setIsGoldenRecordSourceDelete(Boolean isGoldenRecordSourceDelete);
}
