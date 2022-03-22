package com.cs.core.config.interactor.entity.languagecontext;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

import java.util.List;

public interface ILanguageContext extends IEntity {
  
  public static final String NAME                   = "name";
  public static final String LANGUAGE_ID            = "languageId";
  public static final String IS_DEFAULT             = "isDefault";
  public static final String ATTRIBUTE_INSTANCE_IDS = "attributeInstanceIds";
  
  public String getName();
  
  public void setName(String name);
  
  public String getLanguageId();
  
  public void setLanguageId(String languageId);
  
  public Boolean getIsDefault();
  
  public void setIsDefault(Boolean isDefault);
  
  public List<String> getAttributeInstanceIds();
  
  public void setAttributeInstanceIds(List<String> attributeInstanceIds);
}
