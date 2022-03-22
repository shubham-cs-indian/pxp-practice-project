package com.cs.config.idto;

import java.util.List;
/**
 * 
 * @author Janak.Gurme
 *
 */
public interface IConfigDataRuleTagsDTO extends IConfigJSONDTO {
  
  public void setCode(String code);
  
  public String getCode();
  
  public String getEntityId();
  
  public void setEntityId(String entityId);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public String getEntityAttributeType();
  
  public void setEntityAttributeType(String entityAttributeType);
  
  public List<IConfigDataRuleTagRuleDTO> getRules();
  
}
