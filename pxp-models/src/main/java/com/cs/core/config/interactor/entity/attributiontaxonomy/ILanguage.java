package com.cs.core.config.interactor.entity.attributiontaxonomy;

import com.cs.core.config.interactor.entity.configuration.base.IConfigMasterEntity;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;

public interface ILanguage extends ITreeEntity, IConfigMasterEntity {
  
  public static final String ABBREVIATION               = "abbreviation";
  public static final String LOCALE_ID                  = "localeId";
  public static final String IS_DATA_LANGUAGE           = "isDataLanguage";
  public static final String IS_DEFAULT_LANGUAGE        = "isDefaultLanguage";
  public static final String IS_USER_INTERFACE_LANGUAGE = "isUserInterfaceLanguage";
  public static final String DATE_FORMAT                = "dateFormat";
  public static final String NUMBER_FORMAT              = "numberFormat";
  public static final String IS_STANDARD                = "isStandard";
  
  public String getAbbreviation();
  
  public void setAbbreviation(String abbreviation);
  
  public String getLocaleId();
  
  public void setLocaleId(String localeId);
  
  public Boolean getIsDataLanguage();
  
  public void setIsDataLanguage(Boolean isDataLanguage);
  
  public Boolean getIsDefaultLanguage();
  
  public void setIsDefaultLanguage(Boolean isDefaultLanguage);
  
  public Boolean getIsUserInterfaceLanguage();
  
  public void setIsUserInterfaceLanguage(Boolean isUserInterfaceLanguage);
  
  public String getDateFormat();
  
  public void setDateFormat(String dateFormat);
  
  public String getNumberFormat();
  
  public void setNumberFormat(String numberFormat);
  
  public Boolean getIsStandard();
  
  public void setIsStandard(Boolean isStandard);
}
