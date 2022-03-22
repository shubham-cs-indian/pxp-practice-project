package com.cs.core.config.interactor.usecase.user.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.exception.user.UserNotFoundException;
import com.cs.core.config.interactor.model.user.GetUserValidateModel;
import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.config.interactor.model.user.ISAMLValidationModel;
import com.cs.core.config.interactor.model.user.IValidateUserRequestModel;
import com.cs.core.config.interactor.model.user.LDAPValidationModel;
import com.cs.core.config.strategy.usecase.user.validation.IGetUserForValidationStrategy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class GetUserForValidation
    extends AbstractGetConfigInteractor<IValidateUserRequestModel, IGetUserValidateModel>
    implements IGetUserForValidation {
  
  @Autowired
  protected IGetUserForValidationStrategy getUserForValidationStrategy;
  
  @Override
  public IGetUserValidateModel executeInternal(IValidateUserRequestModel dataModel) throws Exception
  {
    IGetUserValidateModel model = null;
    Map<String, String> validationMap = dataModel.getValidationMap();
    String userName = validationMap.get(IUser.USER_NAME);
    if (userName != null && userName.equals(CommonConstants.ADMIN_USER_ID)) {
      model = new LDAPValidationModel();
      model.setUserName(userName);
      model.setUserId(CommonConstants.ADMIN_USER_ID);
      model.setIsValidUser(true);
    }
    else {
      model = getUserForValidationStrategy.execute(dataModel);
      if (model.getIsValidUser()) {
        if (model.getIdp() != null) {
          model = readConfiguration(model);
        }
      }
      else {
        throw new UserNotFoundException();
      }
    }
    return model;
    
  }
  
  private IGetUserValidateModel readConfiguration(IGetUserValidateModel idpConfiguration)
      throws JsonParseException, JsonMappingException, IOException
  {
    InputStream stream = GetUserForValidation.class.getClassLoader()
        .getResourceAsStream(Constants.SSO_CONFIGURATION_MAPPING_JSON_FILE_NAME);
    
    String idp = idpConfiguration.getIdp();
    
    Map<String, List<IGetUserValidateModel>> idpConfigurationMap = ObjectMapperUtil.readValue(stream,
        new TypeReference<Map<String, List<GetUserValidateModel>>>()
        {
        });
    List<IGetUserValidateModel> idpDetails = idpConfigurationMap.get("mappings");
    IGetUserValidateModel desiredConfiguration = idpDetails.stream().filter(x -> x.getId().equals(idp)).findFirst().get();
    desiredConfiguration.setUserId(idpConfiguration.getUserId());
    desiredConfiguration.setUserName(idpConfiguration.getUserName());
    desiredConfiguration.setIdp(idpConfiguration.getIdp());
    desiredConfiguration.setIsValidUser(idpConfiguration.getIsValidUser());
    String type = idpConfiguration.getType();
    if(type.equals("SAML")) {
      ((ISAMLValidationModel)desiredConfiguration).setSsoDomain(((ISAMLValidationModel)idpConfiguration).getSsoDomain());
    }
    
    stream.close();
    return desiredConfiguration;
  }

}
