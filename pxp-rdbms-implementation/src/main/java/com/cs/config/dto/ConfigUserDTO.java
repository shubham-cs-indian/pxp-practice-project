package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONObject;

import com.cs.config.idto.IConfigUserDTO;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigUserDTO extends AbstractConfigJSONDTO implements IConfigUserDTO {
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isStandard, IPXON.PXONTag.isstandard.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.userName, IPXON.PXONTag.username.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.userIID, IPXON.PXONTag.useriid.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.isBackgroundUser, IPXON.PXONTag.isbackgrounduser.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.firstName, IPXON.PXONTag.firstname.toTag());
    KEY_MAP.put(ConfigTag.lastName, IPXON.PXONTag.lastname.toTag());
    KEY_MAP.put(ConfigTag.email, IPXON.PXONTag.email.toTag());
    KEY_MAP.put(ConfigTag.gender, IPXON.PXONTag.gender.toTag());
    KEY_MAP.put(ConfigTag.contact, IPXON.PXONTag.contact.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    KEY_MAP.put(ConfigTag.password, IPXON.PXONTag.password.toTag());
    KEY_MAP.put(ConfigTag.isEmailLog, IPXON.PXONTag.isemaillog.toTag());
    KEY_MAP.put(ConfigTag.preferredDataLanguage, IPXON.PXONTag.preferreddatalanguage.toTag());
    KEY_MAP.put(ConfigTag.preferredUILanguage, IPXON.PXONTag.preferreduilanguage.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.userIID);
    IGNORED_PXON_TAGS.add(ConfigTag.password);
  }
  
  private final UserDTO userDTO = new UserDTO();
  
  public ConfigUserDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  @Override
  public String getFirstName()
  {
    return getString(ConfigTag.firstName);
  }

  @Override
  public String getLastName()
  {
    return getString(ConfigTag.lastName);
  }

  @Override
  public String getGender()
  {
    return getString(ConfigTag.gender);
  }

  @Override
  public String getEmail()
  {
    return getString(ConfigTag.email);
  }

  @Override
  public String getContact()
  {
    return getString(ConfigTag.contact);
  }

  @Override
  public String getBirthDate()
  {
    return getString(ConfigTag.birthDate);
  }

  @Override
  public String getIcon()
  {
    return getString(ConfigTag.icon);
  }

  @Override
  public String getUserName()
  {
    return getString(ConfigTag.userName);
  }

  @Override
  public String getPassword()
  {
    return getString(ConfigTag.password);
  }

  @Override
  public Boolean getIsStandard()
  {
    return getBoolean(ConfigTag.isStandard);
  }
 
  @Override
  public Boolean getIsBackgroundUser()
  {
    return getBoolean(ConfigTag.isBackgroundUser);
  }
  
  @Override
  public Boolean getIsEmailLog()
  {
    return getBoolean(ConfigTag.isEmailLog);
  }
  
  @Override
  public long getUserIID() {
    
    return getLong(ConfigTag.userIID);
  }

  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    initUserDTO();
  }
  
  private void initUserDTO()
  {
    userDTO.setIID(getLong(ConfigTag.userIID));
    userDTO.setUserName(getString(ConfigTag.userName));
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    return userDTO.toCSExpressID();
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    userDTO.fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }

  @Override
  public void fromPXON(String json) throws CSFormatException {
    super.fromPXON(json);
    JSONObject pxonInput = JSONContent.StringToJSON(json);
    String codeTag = KEY_MAP.get(ConfigTag.code);
    String code = (String) pxonInput.get(codeTag);
    setString(ConfigTag.code, code == null ? "" : code);
  }

  @Override
  public void setUserIID(long userIID)
  {
    setLong(ConfigTag.userIID, userIID);
  }

  @Override
  public void setFirstName(String firstName)
  {
    setString(ConfigTag.firstName, firstName);
  }

  @Override
  public void setLastName(String lastName)
  {
    setString(ConfigTag.lastName, lastName);
  }

  @Override
  public void setGender(String gender)
  {
    setString(ConfigTag.gender, gender);

  }

  @Override
  public void setEmail(String email)
  {
    setString(ConfigTag.email, email);
  }

  @Override
  public void setContact(String contact)
  {
    setString(ConfigTag.contact, contact);
  }

  @Override
  public void setUserName(String userName)
  {
    userDTO.setUserName(userName);
    setString(ConfigTag.userName, userName);
  }

  @Override
  public void setPassword(String password)
  {
    setString(ConfigTag.password, password);
  }

  @Override
  public void setIsStandard(boolean isStandard)
  {
    setBoolean(ConfigTag.isStandard, isStandard);
  }

  @Override
  public void setIsBackgroundUser(boolean isBackgroundUser)
  {
    setBoolean(ConfigTag.isBackgroundUser, isBackgroundUser);
  }

  @Override
  public void setIsEmailLog(boolean isEmailLog)
  {
    setBoolean(ConfigTag.isEmailLog, isEmailLog);
  }
  
  @Override
  public void setCode(String code) {
    setString(ConfigTag.code, code);
  }
  
  @Override
  public String getCode()
  {
    return getString(ConfigTag.code);
  }

  @Override
  public void setIcon(String icon)
  {
    setString(ConfigTag.icon, icon);
  }


  @Override
  public String getPreferredDataLanguage()
  {
    return getString(ConfigTag.preferredDataLanguage);
  }

  @Override
  public void setPreferredDataLanguage(String preferredDataLanguage)
  {
    setString(ConfigTag.preferredDataLanguage, preferredDataLanguage);
  }

  @Override
  public String getPreferredUILanguage()
  {
    return getString(ConfigTag.preferredUILanguage);
  }

  @Override
  public void setPreferredUILanguage(String preferredUILanguage)
  {
    setString(ConfigTag.preferredUILanguage, preferredUILanguage);
  }
}
