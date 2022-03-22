package com.cs.core.config.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.user.IUserLanguageModel;
import com.cs.core.config.strategy.usecase.user.IGetLanguageForUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetLanguageForUserService extends AbstractGetConfigService<IIdParameterModel, IUserLanguageModel>
    implements IGetLanguageForUserService {
  
  @Autowired
  IGetLanguageForUserStrategy getLanguageForUserStrategy;
  
  @Override
  public IUserLanguageModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getLanguageForUserStrategy.execute(dataModel);
  }
}
