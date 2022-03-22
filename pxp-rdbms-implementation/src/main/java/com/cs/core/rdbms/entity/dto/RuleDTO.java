package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IRuleDTO;

public class RuleDTO extends ChangedPropertiesDTO implements IRuleDTO {
  
  private Boolean isRuleCauseSatisfied;
  
  @Override
  public Boolean getIsRuleCauseSatisfied()
  {
    if (isRuleCauseSatisfied == null) {
      isRuleCauseSatisfied = false;
    }
    return isRuleCauseSatisfied;
  }
  
  @Override
  public void setIsRuleCauseSatisfied(Boolean isRuleCauseSatisfied)
  {
    this.isRuleCauseSatisfied = isRuleCauseSatisfied;
  }
  
}
