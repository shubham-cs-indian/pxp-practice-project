package com.cs.core.rdbms.entity.dto;

import com.cs.core.rdbms.entity.idto.IRuleViolationAggregationDTO;

public class RuleViolationAggregationDTO extends BaseAggregationDTO implements IRuleViolationAggregationDTO {
  
  private String localeId;
  
  public RuleViolationAggregationDTO(String localeId)
  {
    this.localeId = localeId;
    this.setAggregationType(AggregationType.byRuleViolation);
  }

  @Override
  public String getLocale()
  {
    return this.localeId;
  }
  
}
