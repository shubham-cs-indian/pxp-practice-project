package com.cs.core.config.interactor.model.sso;

import java.util.List;

import com.cs.core.config.interactor.model.user.IGetUserValidateModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetIDPConfigurationResponseModel extends IModel {
  
  public static final String SSO_SETTINGS_MAPPING = "idpConfiguration";
  
  public List<IGetUserValidateModel> getIdpConfiguration();
  public void setIdpConfiguration(List<IGetUserValidateModel> idpConfiguration);
}
