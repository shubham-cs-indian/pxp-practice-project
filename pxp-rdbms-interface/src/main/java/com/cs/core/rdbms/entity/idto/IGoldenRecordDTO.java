package com.cs.core.rdbms.entity.idto;

import java.util.List;

import com.cs.core.bgprocess.idto.IInitializeBGProcessDTO;

public interface IGoldenRecordDTO extends IInitializeBGProcessDTO {
  
  public static final String RULE_ID             = "ruleId";
  public static final String LINKED_BASE_ENTITES = "linkedBaseEntites";
  
  public String getRuleId();
  public void setRuleId(String ruleId);
  
  public List<Long> getLinkedBaseEntities();
  public void setLinkedBaseEntities(List<Long> linkedBaseEntites);
  
}
