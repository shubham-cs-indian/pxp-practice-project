package com.cs.core.config.interactor.entity.smartdocument.preset;

import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;

import java.util.List;

public interface ISmartDocumentPresetEntityRule extends IConfigEntity {
  
  public static final String TYPE                            = "type";
  public static final String TO                              = "to";
  public static final String FROM                            = "from";
  public static final String VALUES                          = "values";
  public static final String SHOULD_COMPARE_WITH_SYSTEM_DATE = "shouldCompareWithSystemDate";
  
  public String getType();
  
  public void setType(String type);
  
  public String getTo();
  
  public void setTo(String to);
  
  public String getFrom();
  
  public void setFrom(String from);
  
  public List<String> getValues();
  
  public void setValues(List<String> values);
  
  public Boolean getShouldCompareWithSystemDate();
  
  public void getShouldCompareWithSystemDate(Boolean shouldCompareWithSystemDate);
}
