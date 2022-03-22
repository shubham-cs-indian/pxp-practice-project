package com.cs.core.config.interactor.usecase.configdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.configdata.IGetConfigDataService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;

@Service
public class GetConfigData extends AbstractGetConfigInteractor<IGetConfigDataRequestModel, IGetConfigDataResponseModel>
    implements IGetConfigData {
  
  @Autowired
  protected IGetConfigDataService getConfigDataService;
  
  @Override
  public IGetConfigDataResponseModel executeInternal(IGetConfigDataRequestModel dataModel) throws Exception
  {
    return getConfigDataService.execute(dataModel);
  }
}
