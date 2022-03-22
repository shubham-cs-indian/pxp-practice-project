package com.cs.di.config.mappings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoRequestModel;
import com.cs.core.config.interactor.model.mapping.IGetPropertyGroupInfoResponseModel;
import com.cs.core.config.strategy.usecase.mapping.IGetPropertyGroupInfoStrategy;

@Service
public class GetPropertyGroupInfoService extends
    AbstractGetConfigService<IGetPropertyGroupInfoRequestModel, IGetPropertyGroupInfoResponseModel>
    implements IGetPropertyGroupInfoService {
  
  @Autowired
  protected IGetPropertyGroupInfoStrategy getPropertyGroupInfoStrategy;
  
  @Override
  public IGetPropertyGroupInfoResponseModel executeInternal(
      IGetPropertyGroupInfoRequestModel dataModel) throws Exception
  {
    return getPropertyGroupInfoStrategy.execute(dataModel);
  }
}
