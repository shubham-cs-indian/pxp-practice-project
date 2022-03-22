package com.cs.core.config.user;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.ICreateUserStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.AuthUtils;
import com.cs.core.technical.rdbms.idto.IUserDTO;

@Service
public class CreateUserService extends AbstractCreateConfigService<IUserModel, IUserModel>
    implements ICreateUserService {
  
  @Autowired
  ICreateUserStrategy  createUserStrategy;
  
  @Override
  public IUserModel executeInternal(IUserModel userModel) throws Exception
  {
    UserValidation.validateUser(userModel);
    String uiEncodedPasswordToken = userModel.getPassword();
    String uiDecodedString = new String(Base64.getDecoder()
        .decode(uiEncodedPasswordToken));
    String[] array = uiDecodedString.split("::");
    String password = "";
    if (array.length > 1) {
      password = uiDecodedString.split("::")[1];
    }
    String encodedPassword = "";
    if (!password.isEmpty()) {
      encodedPassword = AuthUtils.getSaltedHash(password);
    }
    userModel.setPassword(encodedPassword);
    String userId = userModel.getId();
    if (userId == null || userId.equals("")) {
      userId = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.USER.getPrefix());
      userModel.setId(userId);
    }

    //Create User into RDBMS and set UserIID
    IUserDTO createdUser = RDBMSUtils.getOrCreateUser(userModel.getUserName());
    userModel.setUserIID(createdUser.getUserIID());

    return createUserStrategy.execute(userModel);
  }
}
