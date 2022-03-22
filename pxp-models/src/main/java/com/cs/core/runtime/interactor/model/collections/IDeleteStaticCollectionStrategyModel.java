package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.model.klass.IContentTypeIdsInfoModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;

import java.util.List;

public interface IDeleteStaticCollectionStrategyModel extends IBulkResponseModel {
  
  public static final String CONTENT_TYPE_INFO = "contentTypeInfo";
  
  public List<IContentTypeIdsInfoModel> getContentTypeInfo();
  
  public void setContentTypeInfo(List<IContentTypeIdsInfoModel> contentTypeInfo);
  
  public List<String> getSuccess();
  
  public void setSuccess(List<String> success);
}
