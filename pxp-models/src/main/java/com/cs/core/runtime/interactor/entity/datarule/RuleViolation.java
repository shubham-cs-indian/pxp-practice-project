package com.cs.core.runtime.interactor.entity.datarule;

public class RuleViolation implements IRuleViolation {
  
  private static final long serialVersionUID = 1L;
  protected String          id;
  protected String          entityId;
  protected String          color;
  protected String          description;
  protected String          type;
  protected String          ruleId;
  protected String          ruleLabel;
  protected String          calculatedAttributeUnit;
  protected String          calculatedAttributeUnitAsHTML;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getEntityId()
  {
    return entityId;
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    this.entityId = entityId;
  }
  
  @Override
  public String getColor()
  {
    return color;
  }
  
  @Override
  public void setColor(String color)
  {
    this.color = color;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
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
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
  
  @Override
  public String getRuleLabel()
  {
    return ruleLabel;
  }
  
  @Override
  public void setRuleLabel(String ruleLabel)
  {
    this.ruleLabel = ruleLabel;
  }
  
  @Override
  public String getCalculatedAttributeUnit()
  {
    return calculatedAttributeUnit;
  }
  
  @Override
  public void setCalculatedAttributeUnit(String calculatedAttributeUnit)
  {
    this.calculatedAttributeUnit = calculatedAttributeUnit;
  }
  
  @Override
  public String getCalculatedAttributeUnitAsHTML()
  {
    return calculatedAttributeUnitAsHTML;
  }
  
  @Override
  public void setCalculatedAttributeUnitAsHTML(String calculatedAttributeUnitAsHTML)
  {
    this.calculatedAttributeUnitAsHTML = calculatedAttributeUnitAsHTML;
  }
}
