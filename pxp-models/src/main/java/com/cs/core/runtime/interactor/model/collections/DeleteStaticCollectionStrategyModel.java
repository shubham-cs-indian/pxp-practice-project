package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.model.klass.ContentTypeIdsInfoModel;
import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class DeleteStaticCollectionStrategyModel implements IDeleteStaticCollectionStrategyModel {
  
  private static final long      serialVersionUID = 1L;
  IExceptionModel                failure;
  List<String>                   success;
  List<IContentTypeIdsInfoModel> contentTypeInfo;
  
  @Override
  public IExceptionModel getFailure()
  {
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public List<String> getSuccess()
  {
    return success;
  }
  
  @Override
  public void setSuccess(List<String> success)
  {
    this.success = success;
  }
  
  @Override
  public List<IContentTypeIdsInfoModel> getContentTypeInfo()
  {
    return contentTypeInfo;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentTypeIdsInfoModel.class)
  public void setContentTypeInfo(List<IContentTypeIdsInfoModel> contentTypeInfo)
  {
    this.contentTypeInfo = contentTypeInfo;
  }
}
