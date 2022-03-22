package com.cs.core.config.user;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IBulkSaveUsersResponseModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.strategy.usecase.user.ISaveUsersStrategy;
import com.cs.core.runtime.strategy.utils.AuthUtils;

@SuppressWarnings("unchecked")
@Service
public class SaveUsersService
    extends AbstractSaveConfigService<IListModel<IUserModel>, IBulkSaveUsersResponseModel>
    implements ISaveUsersService {
  
  @Autowired
  ISaveUsersStrategy saveUserStrategy;
  
  @Override
  public IBulkSaveUsersResponseModel executeInternal(IListModel<IUserModel> userModels) throws Exception
  {
    List<IUserModel> userList = (List<IUserModel>) userModels.getList();
    
    for (IUserModel userModel : userList) {
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
    }
    
    return saveUserStrategy.execute(userModels);
  }
}
