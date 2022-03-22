package com.cs.core.config.interactor.model.translations;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetPropertyTranslationsModel implements IGetPropertyTranslationsModel {
  
  private static final long                        serialVersionUID = 1L;
  protected String                                 id;
  protected String                                 label;
  protected String                                 description;
  protected String                                 tooltip;
  protected String                                 placeholder;
  protected Map<String, IStandardTranslationModel> translations;
  protected String                                 code;
  protected String                                 type;
  
  @Override
  public String getId()
  {
    return this.id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getLabel()
  {
    return this.label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getPlaceholder()
  {
    return this.placeholder;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    this.placeholder = placeholder;
  }
  
  @Override
  public String getDescription()
  {
    return this.description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public String getTooltip()
  {
    return this.tooltip;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    this.tooltip = tooltip;
  }
  
  @Override
  @JsonDeserialize(contentAs = StandardTranslationModel.class)
  public Map<String, IStandardTranslationModel> getTranslations()
  {
    return translations;
  }
  
  @Override
  public void setTranslations(Map<String, IStandardTranslationModel> translations)
  {
    this.translations = translations;
  }
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
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
}
