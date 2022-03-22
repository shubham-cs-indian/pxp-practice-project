package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.instance.ISaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.instance.SaveStrategyInstanceResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkSaveKlassInstanceVariantsStrategyModel
    implements IBulkSaveKlassInstanceVariantsStrategyModel {
  
  private static final long                          serialVersionUID = 1L;
  protected IExceptionModel                          failure;
  protected List<String>                             successIds       = new ArrayList<>();
  protected List<ISaveStrategyInstanceResponseModel> instancesSaveResponse;
  
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
    return successIds;
  }
  
  @Override
  public void setSuccess(List<String> successIds)
  {
    this.successIds = successIds;
  }
  
  @Override
  public List<ISaveStrategyInstanceResponseModel> getInstancesSaveResponse()
  {
    return instancesSaveResponse;
  }
  
  @Override
  @JsonDeserialize(contentAs = SaveStrategyInstanceResponseModel.class)
  public void setInstancesSaveResponse(
      List<ISaveStrategyInstanceResponseModel> instancesSaveResponse)
  {
    this.instancesSaveResponse = instancesSaveResponse;
  }
}
