package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.IGetUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetUserService extends AbstractGetConfigService<IIdParameterModel, IUserModel>
    implements IGetUserService {
  
  @Autowired
  IGetUserStrategy getUserStrategy;
  
  @Override
  public IUserModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getUserStrategy.execute(dataModel);
  }
}
