package com.cs.core.config.user;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.IResetPasswordStrategy;
import com.cs.core.runtime.strategy.utils.AuthUtils;

@Service
public class ResetPasswordService extends AbstractSaveConfigService<IUserModel, IUserModel>
    implements IResetPasswordService {
  
  @Autowired
  IResetPasswordStrategy resetPasswordStrategy;
  
  @Override
  public IUserModel executeInternal(IUserModel userModel) throws Exception
  {
    String uiEncodedPasswordToken = userModel.getPassword();
    if (uiEncodedPasswordToken == null) {
      userModel.setPassword("");
    }
    else if (!uiEncodedPasswordToken.isEmpty()) {
      String uiDecodedString = new String(Base64.getDecoder()
          .decode(uiEncodedPasswordToken));
      String password = uiDecodedString.split("::")[1];
      String encodedPassword = AuthUtils.getSaltedHash(password);
      userModel.setPassword(encodedPassword);
    }
    
    return resetPasswordStrategy.execute(userModel);
  }
}
