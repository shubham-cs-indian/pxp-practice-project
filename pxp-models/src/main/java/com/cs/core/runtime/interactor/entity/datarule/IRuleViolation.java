package com.cs.core.runtime.interactor.entity.datarule;

import com.cs.core.config.interactor.entity.configuration.base.IEntity;

public interface IRuleViolation extends IEntity {
  
  public static final String ENTITY_ID                         = "entityId";
  public static final String COLOR                             = "color";
  public static final String DESCRIPTION                       = "description";
  public static final String TYPE                              = "type";
  public static final String RULE_ID                           = "ruleId";
  public static final String RULE_LABEL                        = "ruleLabel";
  public static final String CALCULATED_ATTRIBUTE_UNIT         = "calculatedAttributeUnit";
  public static final String CALCULATED_ATTRIBUTE_UNIT_AS_HTML = "calculatedAttributeUnitAsHTML";
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getColor();
  
  public void setColor(String color);
  
  public String getDescription();
  
  public void setDescription(String description);
  
  public String getType();
  
  public void setType(String type);
  
  public String getRuleId();
  
  public void setRuleId(String ruleId);
  
  public String getRuleLabel();
  
  public void setRuleLabel(String ruleLabel);
  
  public String getCalculatedAttributeUnit();
  
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit);
  
  public String getCalculatedAttributeUnitAsHTML();
  
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML);
}
