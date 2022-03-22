package com.cs.core.config.interactor.model.translations;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetPropertyTranslationsModel extends IModel {
  
  public static final String ID          = "id";
  public static final String LABEL       = "label";
  public static final String PLACEHOLDER = "placeholder";
  public static final String DESCRIPTION = "description";
  public static final String TOOLTIP     = "tooltip";
  public static final String TRANSLATION = "translations";
  public static final String CODE        = "code";
  public static final String TYPE        = "type";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public String getPlaceholder();
  
  public void setPlaceholder(String placeholder);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getTooltip();
  
  public void setTooltip(String tooltip);
  
  public Map<String, IStandardTranslationModel> getTranslations();
  
  public void setTranslations(Map<String, IStandardTranslationModel> translations);
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
}
