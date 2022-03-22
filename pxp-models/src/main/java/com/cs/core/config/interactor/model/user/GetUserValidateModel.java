package com.cs.core.config.interactor.model.user;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type",
    visible = true)
@JsonSubTypes({ @Type(value = SAMLValidationModel.class, name = "SAML"),
    @Type(value = LDAPValidationModel.class, name = "LDAP"),
    @Type(value = PXPValidationModel.class, name = "nonSSOUser") })
public class GetUserValidateModel implements IGetUserValidateModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          id;
  protected String          userId;
  protected String          label;
  protected String          userName;
  protected String          idp;
  protected Boolean         isValidUser;
  protected String          type;
  protected String          url;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getIdp()
  {
    return idp;
  }
  
  @Override
  public void setIdp(String idp)
  {
    this.idp = idp;
  }
  
  @Override
  public String getLabel()
  {
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public Boolean getIsValidUser()
  {
    return isValidUser;
  }
  
  @Override
  public void setIsValidUser(Boolean isValidateUser)
  {
    this.isValidUser = isValidateUser;
  }
  
  @Override
  public String getUserId()
  {
    return userId;
  }

  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  @Override
  public String getUserName()
  {
    return userName;
  }

  @Override
  public void setUserName(String userName)
  {
    this.userName = userName;   
  }
  
  @Override
  public String getType()
  {
    return type;
  }

  @Override
  public void setType(String type)
  {
    this.type = type;   
  }
  
  @Override
  public String getUrl()
  {
    return url;
  }
  
  @Override
  public void setUrl(String url)
  {
    this.url = url;
  }
}
