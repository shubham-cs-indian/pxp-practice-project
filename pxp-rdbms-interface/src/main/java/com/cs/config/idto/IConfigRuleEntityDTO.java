package com.cs.config.idto;

import java.util.List;

/**
 * Rule Entity DTO from the configuration realm
 *
 * @author janak
 */

public interface IConfigRuleEntityDTO extends IConfigJSONDTO {
  
  public String getCode();
  
  public void setCode(String code);
  
  public String getType();
  
  public void setType(String type);
  
  public String getTo();
  
  public void setTo(String to);
  
  public String getFrom();
  
  public void setFrom(String from);
  
  public List<String> getValues();
  
  public void setValues(List<String> values);
  
  public String getRuleListLinkId();
  
  public void setRuleListLinkId(String ruleListLinkId);
  
  public String getAttributeLinkId();
  
  public void setAttributeLinkId(String attributeLinkId);
  
  public String getEntityType();
  
  public void setEntityType(String entityType);
  
  public String getEntityAttributeType();
  
  public void setEntityAttributeType(String entityAttributeType);
  
  public List<String> getKlassLinkIds();
  
  public void setKlassLinkIds(List<String> klassLinkdIds);
  
  public Boolean getShouldCompareWithSystemDate();
  
  public void setShouldCompareWithSystemDate(Boolean shouldCompareWithSystemDate);
}
