package com.cs.core.initialize;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.store.strategy.base.user.IGetOrCreateUserStrategy;
import com.cs.core.interactor.usecase.initialize.utils.ValidationUtils;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.AuthUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.rdbms.idto.IUserDTO;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeUsersService implements IInitializeUsersService {
  
  @Autowired
  protected IGetOrCreateUserStrategy getOrCreateUserStrategy;
  
  @Override
  public void execute() throws Exception
  {
    initializeUsers();
  }
  
  private void initializeUsers() throws Exception
  {
    InputStream stream = this.getClass()
        .getClassLoader()
        .getResourceAsStream(InitializeDataConstants.USERS_JSON);
    List<IUserModel> users = ObjectMapperUtil.readValue(stream, new TypeReference<List<UserModel>>()
    {
    });
    stream.close();
    
    IConfigurationDAO configurationDAO = RDBMSUtils.newConfigurationDAO();
    for (IUserModel user : users) {
      ValidationUtils.validateUser(user);
      user.setPassword(AuthUtils.getSaltedHash(user.getPassword()));
      IUserDTO userDTO = configurationDAO.createStandardUser(user.getUserIID(), user.getUserName());
      getOrCreateUserStrategy.execute(user);
    }
  }
}
