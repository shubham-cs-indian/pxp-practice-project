package com.cs.core.config.sso;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.entity.sso.ISSOConfiguration;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.sso.ICreateSSOConfigurationModel;
import com.cs.core.exception.IDPMustNotBeEmptyException;
import com.cs.core.exception.InvalidSSOIDPTypeException;
import com.cs.core.exception.LabelMustNotBeEmptyException;
import com.cs.core.exception.SSODomainMustNotBeEmptyException;

@Component
public class SSOConfigurationValidations extends Validations {
  
  public void validate(ICreateSSOConfigurationModel model) throws Exception
  {
    try {
      validate(model.getCode(), model.getDomain());
    }
    catch (LabelMustNotBeEmptyException exception) {
      throw new SSODomainMustNotBeEmptyException(ISSOConfiguration.DOMAIN + " can't be empty");
    }
    validateIDP(model.getIdp(), model.getType());
  }
  
  public void validate(IListModel<ICreateSSOConfigurationModel> ssoConfigurationListModel)
      throws Exception
  {
    for (ICreateSSOConfigurationModel model : ssoConfigurationListModel.getList()) {
      validate(model);
    }
  }
  
  protected void validateIDP(String idp, String type)
      throws IDPMustNotBeEmptyException, InvalidSSOIDPTypeException
  {
    if (isEmpty(idp)) {
      throw new IDPMustNotBeEmptyException(ISSOConfiguration.IDP + " can't be empty");
    }
    
    if (!isValidIDPType(type)) {
      throw new InvalidSSOIDPTypeException(ISSOConfiguration.IDP + " is not valid IDP");
    }
  }
  
  private boolean isValidIDPType(String type)
  {
    return type.equals("SAML") || type.equals("LDAP");
  }
  
}
