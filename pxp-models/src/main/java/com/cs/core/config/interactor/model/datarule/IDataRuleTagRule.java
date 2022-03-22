package com.cs.core.config.interactor.model.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.entity.datarule.IDataRuleTagValues;

import java.util.List;

public interface IDataRuleTagRule extends IEntity {
  
  public static final String CODE = "code";
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
  
  public String getColor();
  
  public void setColor(String color);
  
  @Override
  public String getId();
  
  @Override
  public void setId(String id);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public List<IDataRuleTagValues> getTagValues();
  
  public void setTagValues(List<IDataRuleTagValues> tagValues);
}
