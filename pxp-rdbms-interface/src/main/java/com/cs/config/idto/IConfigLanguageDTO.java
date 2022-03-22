package com.cs.config.idto;

/**
 * Language DTO from the configuration realm
 *
 * @author abhishek
 */

public interface IConfigLanguageDTO extends IConfigJSONDTO {
  
  public String getLabel();

  public void setLabel(String label);

  public String getIcon();

  public void setIcon(String icon);
  
  public String getAbbreviation();
  
  public void setAbbreviation(String abbreviation);
  
  public String getLocaleId();
  
  public void setLocaleId(String localeId);
  
  public String getParentCode();
  
  public void setParentCode(String parentCode);
  
  public Boolean isDataLanguage();
  
  public void setIsDataLanguage(Boolean isDataLanguage);
  
  public Boolean isDefaultLanguage();
  
  public void setIsDefaultLanguage(Boolean isDefaultLanguage);
  
  public Boolean isUserInterfaceLanguage();
  
  public void setIsUserInterfaceLanguage(Boolean isUserInterfaceLanguage);
  
  public String getDateFormat();
  
  public void setDateFormat(String dateFormat);
  
  public String getNumberFormat();
  
  public void setNumberFormat(String numberFormat);
  
  public Boolean isStandard();
  
  public void setIsStandard(Boolean isStandard);
}
