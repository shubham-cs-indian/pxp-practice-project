package com.cs.core.config.interactor.model.tag;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;

public interface ICreateTagResponseModel extends IConfigResponseWithAuditLogModel {
  
  public static final String CREATE_TAG_RESPONSE = "tagResponseModel";
  
  public List<ITagModel> getTagResponseModel();
  public void setTagResponseModel(List<ITagModel> tagResponseModel);
  
}