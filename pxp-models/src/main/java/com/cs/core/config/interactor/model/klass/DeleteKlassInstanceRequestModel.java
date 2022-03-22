package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceRequestModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DeleteKlassInstanceRequestModel implements IDeleteKlassInstanceRequestModel {
  
  private static final long                 serialVersionUID = 1L;
  protected List<IDeleteKlassInstanceModel> deleteRequest;
  // TODO : MUST REMOVE : ROHITH
  protected Boolean                         hasDeletePermission;
  protected Boolean                         isDeleteFromDICatalog;
  protected Boolean                         isDeleteFromArchivalPortal;
  protected Boolean                         isGoldenRecordSourceDelete;
  
  @Override
  public List<IDeleteKlassInstanceModel> getDeleteRequest()
  {
    if (deleteRequest == null) {
      deleteRequest = new ArrayList<>();
    }
    return deleteRequest;
  }
  
  @JsonDeserialize(contentAs = DeleteKlassInstanceModel.class)
  @Override
  public void setDeleteRequest(List<IDeleteKlassInstanceModel> deleteRequest)
  {
    this.deleteRequest = deleteRequest;
  }
  
  @Override
  public Boolean getHasDeletePermission()
  {
    return hasDeletePermission;
  }
  
  @Override
  public void setHasDeletePermission(Boolean hasDeletePermission)
  {
    this.hasDeletePermission = hasDeletePermission;
  }
  
  @Override
  public Boolean getIsDeleteFromDICatalog()
  {
    if (isDeleteFromDICatalog == null) {
      isDeleteFromDICatalog = false;
    }
    return isDeleteFromDICatalog;
  }
  
  @Override
  public void setIsDeleteFromDICatalog(Boolean isDeleteFromDICatalog)
  {
    this.isDeleteFromDICatalog = isDeleteFromDICatalog;
  }
  
  @Override
  public Boolean getIsDeleteFromArchivalPortal()
  {
    if (isDeleteFromArchivalPortal == null) {
      isDeleteFromArchivalPortal = false;
    }
    return isDeleteFromArchivalPortal;
  }
  
  @Override
  public void setIsDeleteFromArchivalPortal(Boolean isDeleteFromArchivalPortal)
  {
    this.isDeleteFromArchivalPortal = isDeleteFromArchivalPortal;
  }
  
  @Override
  public Boolean getIsGoldenRecordSourceDelete()
  {
    if (isGoldenRecordSourceDelete == null) {
      isGoldenRecordSourceDelete = false;
    }
    return isGoldenRecordSourceDelete;
  }
  
  @Override
  public void setIsGoldenRecordSourceDelete(Boolean isGoldenRecordSourceDelete)
  {
    this.isGoldenRecordSourceDelete = isGoldenRecordSourceDelete;
  }
}
