package com.cs.core.bgprocess.idto;

import java.util.Set;

public interface ISaveDataRuleDTO extends IInitializeBGProcessDTO {
  
  public void setRuleCode(String ruleCode);
  
  public String getRuleCode(); 
  
  public void setRuleExpressionId(Long ruleExpressionId);
  
  public Long getRuleExpressionId(); 
  
  public void setChangedClassifierCodes(Set<String> classifierCodes);
  
  public Set<String> getChangedClassifierCodes();
  
  public void setChangedCatalogIds(Set<String> catalogIds);
  
  public Set<String> getChangedCatalogIds();
  
  public void setChangedOrganizationIds(Set<String> organizationIds);
  
  public Set<String> getChangedOrganizationIds();

}
