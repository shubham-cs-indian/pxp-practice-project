package com.cs.core.config.interactor.model.language;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

public interface IGetLanguagesInfoModel extends IConfigEntityInformationModel {
  
  public static final String NUMBER_FORMAT = "numberFormat";
  public static final String DATE_FORMAT   = "dateFormat";
  public static final String LOCALE_ID     = "localeId";
  public static final String ABBREVIATION  = "abbreviation";
  
  public String getNumberFormat();
  
  public void setNumberFormat(String numberFormat);
  
  public String getDateFormat();
  
  public void setDateFormat(String dateFormat);
  
  public String getLocaleId();
  
  public void setLocaleId(String localeId);
  
  public String getAbbreviation();
  
  public void setAbbreviation(String abbreviation);
}
