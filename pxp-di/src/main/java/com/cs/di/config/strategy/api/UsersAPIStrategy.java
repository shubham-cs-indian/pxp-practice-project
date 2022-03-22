package com.cs.di.config.strategy.api;

import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.user.IUserModel;
import com.cs.core.config.interactor.model.user.UserModel;
import com.cs.core.config.interactor.usecase.user.ICreateUser;
import com.cs.core.config.interactor.usecase.user.IGetUser;
import com.cs.core.config.interactor.usecase.user.IGetUserByUserName;
import com.cs.core.config.interactor.usecase.user.ISaveUsers;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.runtime.utils.DiValidationUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("usersAPIStrategy")
public class UsersAPIStrategy extends AbstractConfigurationAPIStrategy implements IConfigurationAPIStrategy {
  
  @Autowired
  IGetUser           getUser;
  
  @Autowired
  IGetUserByUserName validateUserName;
  
  @Autowired
  ICreateUser        createUser;
  
  @Autowired
  ISaveUsers         saveUsers;
  
  private static final ObjectMapper objectMapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel model = new IdParameterModel();
    model.setId(code);
    return getUser.execute(model);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception
  {
    IUserModel createusermodel = objectMapper.convertValue(inputData, UserModel.class);
    String email = createusermodel.getEmail();
    //Validating email id
    if (!DiValidationUtil.emailValidator(email)) {
      throw new Exception("Email is invalid");
    }
    createusermodel.setType(CommonConstants.USER_TYPE);
    String encodedPasswordToken = convertToEncryptedPassword(createusermodel);
    createusermodel.setPassword(encodedPasswordToken);
    //To check for duplicate userName
    IUserModel checkvalidUserName = validateUserName.execute(createusermodel);
    return createUser.execute(checkvalidUserName);
   
  }
  
  // Encryption of password
  private String convertToEncryptedPassword(IUserModel createusermodel)
  {
    String inputPassword = createusermodel.getPassword();
    String encodedPasswordToken = "";
    if (inputPassword != null && !inputPassword.isEmpty()) {
      String password = createusermodel.getUserName() + "::" + inputPassword;
      encodedPasswordToken = Base64.getEncoder().encodeToString(password.getBytes());
    }
    return encodedPasswordToken;
  }
  
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    IUserModel user = objectMapper.convertValue(inputData, UserModel.class);
    user.setId((String) getData.get(IConfigMasterEntity.CODE));
    user.setUserName((String) getData.get(IUserModel.USER_NAME));
    user.setRoleId((String) getData.get(IUserModel.ROLE_ID));
    String encodedPasswordToken_new = convertToEncryptedPassword(user); 
    user.setPassword(encodedPasswordToken_new);
    user.setType((String) getData.get(IConfigMasterEntity.TYPE));
    user.setOrganizationName((String) getData.get(IUserModel.ORGANIZATION_NAME));
    user.setOrganizationType((String) getData.get(IUserModel.ORGANIZATION_TYPE));
    user.setIsBackgroundUser((Boolean) getData.get(IUserModel.IS_BACKGROUND_USER));
    user.setIsStandard((Boolean) getData.get(IUserModel.IS_STANDARD));   
    IListModel<IUserModel> usersListSaveModel = new ListModel<>();
    usersListSaveModel.setList(Arrays.asList(user));
    return saveUsers.execute(usersListSaveModel);
  }
  
}
