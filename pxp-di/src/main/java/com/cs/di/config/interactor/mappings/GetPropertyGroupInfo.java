package com.cs.di.config.interactor.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoResponseModel;
import com.cs.di.config.mappings.IGetPropertyGroupInfoService;

@Service("getPropertyGroupInfo")
public class GetPropertyGroupInfo extends
    AbstractGetConfigInteractor<IGetPropertyGroupInfoRequestModel, IGetPropertyGroupInfoResponseModel>
    implements IGetPropertyGroupInfo {
  
  @Autowired
  protected IGetPropertyGroupInfoService getPropertyGroupInfoService;
  
  @Override
  public IGetPropertyGroupInfoResponseModel executeInternal(
      IGetPropertyGroupInfoRequestModel dataModel) throws Exception
  {
    return getPropertyGroupInfoService.execute(dataModel);
  }
}
