package com.cs.core.rdbms.goldenrecordbucket.idao;

import java.util.List;

public interface IGoldenRecordRuleAndBaseEntityIIDsDTO {
  
  public static final String RULE_ID          = "ruleId";
  public static final String BASE_ENTITY_IIDS = "baseEntityIIDs";
  
  public String getRuleId();
  public void setRuleId(String ruleId);
  
  public List<Long> getBaseEntityIIDs();
  public void setBaseEntityIIDs(List<Long> goldenRecordRules);
}
