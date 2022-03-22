package com.cs.core.config.interactor.model.sso;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.user.IGetUserValidateModel;

public class GetIDPConfigurationResponseModel implements IGetIDPConfigurationResponseModel {
  
  private static final long                  serialVersionUID = 1L;
  protected List<IGetUserValidateModel> idpConfiguration = new ArrayList<>();
  
  public GetIDPConfigurationResponseModel(List<IGetUserValidateModel> idpConfiguration)
  {
    this.idpConfiguration = idpConfiguration;
  }
  
  @Override
  public List<IGetUserValidateModel> getIdpConfiguration()
  {
    return idpConfiguration;
  }
  
  @Override
  public void setIdpConfiguration(List<IGetUserValidateModel> idpConfiguration)
  {
    this.idpConfiguration = idpConfiguration;
  }
}
