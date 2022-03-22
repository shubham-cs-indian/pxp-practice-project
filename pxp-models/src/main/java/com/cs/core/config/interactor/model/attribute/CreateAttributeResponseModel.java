package com.cs.core.config.interactor.model.attribute;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.AbstractAttributeModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class CreateAttributeResponseModel extends ConfigResponseWithAuditLogModel
    implements ICreateAttributeResponseModel {
  
  private static final long       serialVersionUID = 1L;
  protected List<IAttributeModel> attribute;
  
  @Override
  public List<IAttributeModel> getAttribute()
  {
    return attribute;
  }
  
  @Override
  @JsonTypeInfo(use = Id.NONE)
  @JsonDeserialize(contentAs = AbstractAttributeModel.class)
  public void setAttribute(List<IAttributeModel> attribute)
  {
    this.attribute = attribute;
  }
  
}