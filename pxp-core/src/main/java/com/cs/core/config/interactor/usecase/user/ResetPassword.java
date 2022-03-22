package com.cs.core.config.interactor.usecase.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.user.IResetPasswordService;

@Service
public class ResetPassword extends AbstractSaveConfigInteractor<IUserModel, IUserModel>
    implements IResetPassword {
  
  @Autowired
  IResetPasswordService resetPasswordService;
  
  @Override
  public IUserModel executeInternal(IUserModel userModel) throws Exception
  {
    
    return resetPasswordService.execute(userModel);
  }
}
