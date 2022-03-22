package com.cs.core.bgprocess.dto;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class InitializeBGProcessDTO extends SimpleDTO implements IInitializeBGProcessDTO {
  
  private String       localeID          = "";
  private String       catalogCode       = "";
  private String       organizationCode  = IStandardConfig.STANDARD_ORGANIZATION_CODE;
  private String       userId            = "";
  private String       userName            = "";
  
  private final String LOCALE_ID         = "localeID";
  private final String CATALOG_CODE      = "catalogCode";
  private final String ORGANIZATION_CODE = "organizationCode";
  private final String USER_ID           = "userId";
  private final String USER_NAME         = "userName";
  
  @Override
  public String getLocaleID()
  {
    return localeID;
  }
  
  @Override
  public void setLocaleID(String localeID)
  {
    this.localeID = localeID;
  }
  
  @Override
  public String getCatalogCode()
  {
    return catalogCode;
  }
  
  @Override
  public void setCatalogCode(String catalogCode)
  {
    this.catalogCode = catalogCode;
  }
  
  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }
  
  @Override
  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
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
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONField(LOCALE_ID, localeID),
        JSONBuilder.newJSONField(CATALOG_CODE, catalogCode),
        JSONBuilder.newJSONField(ORGANIZATION_CODE, organizationCode),
        JSONBuilder.newJSONField(USER_ID, userId),
        JSONBuilder.newJSONField(USER_NAME, userName));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    localeID = json.getString(LOCALE_ID);
    catalogCode = json.getString(CATALOG_CODE);
    organizationCode = json.getString(ORGANIZATION_CODE);
    userId = json.getString(USER_ID);
    userName = json.getString(USER_NAME);
  }
  
}
