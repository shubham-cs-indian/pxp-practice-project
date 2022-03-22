package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserLanguageModel;
import com.cs.core.config.user.IGetLanguageForUserService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetLanguageForUser extends AbstractGetConfigInteractor<IIdParameterModel, IUserLanguageModel>
    implements IGetLanguageForUser {
  
  @Autowired
  IGetLanguageForUserService getLanguageForUserService;
  
  @Override
  public IUserLanguageModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getLanguageForUserService.execute(dataModel);
  }
}
