package com.cs.core.config.interactor.model.translations;

import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SaveRelationshipTranslationsResponseModel extends ConfigResponseWithAuditLogModel
    implements ISaveRelationshipTranslationsResponseModel {
  
  private static final long              serialVersionUID = 1L;
  IExceptionModel                        failure;
  List<IGetRelationshipTranslationModel> success;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @Override
  @JsonDeserialize(as = ExceptionModel.class)
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public Object getSuccess()
  {
    return success;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetRelationshipTranslationModel.class)
  public void setSuccess(List<IGetRelationshipTranslationModel> success)
  {
    this.success = success;
  }
}
