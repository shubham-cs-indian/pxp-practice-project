package com.cs.core.rdbms.goldenrecordbucket.dao;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.rdbms.goldenrecordbucket.idao.IGoldenRecordRuleAndBaseEntityIIDsDTO;

public class GoldenRecordRuleAndBaseEntityIIDsDTO implements IGoldenRecordRuleAndBaseEntityIIDsDTO {
  
  private String     ruleId;
  private List<Long> baseEntityIIDs;
  
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
  public List<Long> getBaseEntityIIDs()
  {
    if (baseEntityIIDs == null) {
      baseEntityIIDs = new ArrayList<>();
    }
    return baseEntityIIDs;
  }
  
  @Override
  public void setBaseEntityIIDs(List<Long> baseEntityIIDs)
  {
    this.baseEntityIIDs = baseEntityIIDs;
  }
}
