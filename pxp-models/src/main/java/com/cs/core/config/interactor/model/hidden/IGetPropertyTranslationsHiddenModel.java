package com.cs.core.config.interactor.model.hidden;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetPropertyTranslationsHiddenModel extends IModel {
  
  public static final String ID           = "id";
  public static final String CODE         = "code";
  public static final String SCREENS      = "screens";
  public static final String VERSION_ID   = "versionId";
  public static final String LABEL__EN_US = "label__en_US";
  public static final String LABEL__FR_FR = "label__fr_FR";
  public static final String LABEL__DE_DE = "label__de_DE";
  public static final String LABEL__EN_UK = "label__en_UK";
  public static final String LABEL__ES_ES = "label__es_ES";
  
  public String getId();
  
  public void setId(String id);
  
  public String getCode();
  
  public void setCode(String code);
  
  public Integer getVersionId();
  
  public void setVersionId(Integer versionId);
  
  public String getLabel__en_US();
  
  public void setLabel__en_US(String label__en_US);
  
  public String getLabel__fr_FR();
  
  public void setLabel__fr_FR(String label__fr_FR);
  
  public String getLabel__de_DE();
  
  public void setLabel__de_DE(String label__de_DE);
  
  public String getLabel__en_UK();
  
  public void setLabel__en_UK(String label__en_UK);
  
  public String getLabel__es_ES();
  
  public void setLabel__es_ES(String label__es_ES);
  
  public List<String> getScreens();
  
  public void setScreens(List<String> screens);
}
