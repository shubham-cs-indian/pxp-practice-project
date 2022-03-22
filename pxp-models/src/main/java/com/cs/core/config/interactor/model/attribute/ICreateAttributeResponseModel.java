package com.cs.core.config.interactor.model.attribute;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;

public interface ICreateAttributeResponseModel extends IConfigResponseWithAuditLogModel {
  
  public static final String ATTRIBUTE      = "attribute";
  
  public List<IAttributeModel> getAttribute();
  public void setAttribute(List<IAttributeModel> attribute);
   
}
