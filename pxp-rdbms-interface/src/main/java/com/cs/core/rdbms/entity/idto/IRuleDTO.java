package com.cs.core.rdbms.entity.idto;

public interface IRuleDTO extends IChangedPropertiesDTO {
  
  public static final String IS_RULE_CAUSE_SATISFIED = "isRuleCauseSatisfied";
  
  public Boolean getIsRuleCauseSatisfied();
  public void setIsRuleCauseSatisfied(Boolean isRuleCauseSatisfied);
  
}
