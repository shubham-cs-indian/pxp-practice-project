package com.cs.config.idto;

import java.util.List;

import com.cs.core.technical.ijosn.IJSONContent;

public interface IConfigDataRuleTagRuleDTO extends IConfigJSONDTO {
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
  
  public String getColor();
  
  public void setColor(String color);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public List<IJSONContent> getTagValues();
  
  public void setTagValues(List<IJSONContent> tagValues);
  
}
