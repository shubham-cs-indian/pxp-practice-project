package com.cs.core.config.interactor.usecase.configdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetConfigEntityIdsCodeNamesResponseModel;
import com.cs.core.config.strategy.usecase.configdata.IGetConfigEntityIdsCodeNamesStrategy;

@Service
public class GetConfigEntityIdsCodeNamesInteractor extends
    AbstractGetConfigInteractor<IGetConfigEntityIdsCodeNamesRequestModel, IGetConfigEntityIdsCodeNamesResponseModel>
    implements IGetConfigEntityIdsCodeNamesInteractor {
  
  @Autowired
  IGetConfigEntityIdsCodeNamesStrategy getConfigEntityIdsCodeNamesStrategy;
  
  @Override
  public IGetConfigEntityIdsCodeNamesResponseModel executeInternal(
      IGetConfigEntityIdsCodeNamesRequestModel dataModel) throws Exception
  {
    return getConfigEntityIdsCodeNamesStrategy.execute(dataModel);
  }
}
