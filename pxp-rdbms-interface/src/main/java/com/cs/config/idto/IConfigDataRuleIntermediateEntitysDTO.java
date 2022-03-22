package com.cs.config.idto;

import java.util.List;

public interface IConfigDataRuleIntermediateEntitysDTO extends IConfigJSONDTO {
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public String getEntityAttributeType();
  
  public void setEntityAttributeType(String entityAttributeType);
  
  public List<IConfigRuleEntityDTO> getRules();
  
}
