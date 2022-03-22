package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

public class BulkAssetInstanceInformationModel implements IBulkAssetInstanceInformationModel {
  
  private static final long                      serialVersionUID = 1L;
  
  protected List<IKlassInstanceInformationModel> success;
  protected IExceptionModel                      failure;
  
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
  public List<IKlassInstanceInformationModel> getSuccess()
  {
    return success;
  }
  
  @JsonDeserialize(contentAs = KlassInstanceInformationModel.class)
  @Override
  public void setSuccess(List<IKlassInstanceInformationModel> success)
  {
    this.success = success;
  }
}
