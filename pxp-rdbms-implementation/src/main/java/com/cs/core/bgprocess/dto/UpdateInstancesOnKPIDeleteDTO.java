package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.IUpdateInstancesOnKPIDeleteDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class UpdateInstancesOnKPIDeleteDTO extends SimpleDTO implements IUpdateInstancesOnKPIDeleteDTO {

  private static final long serialVersionUID = 1L;
  
  public static final String DELETED_RULE_CODES = "deletedRuleCodes";
  
  private List<String>       ruleCodes          = new ArrayList<>();

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return  JSONBuilder.newJSONStringArray(DELETED_RULE_CODES, ruleCodes);
  }
  
  

  @Override
  public void setRuleCodes(List<String> ruleCodes)
  {
    this.ruleCodes = ruleCodes;
  }

  @Override
  public List<String> getRuleCodes()
  {
    return ruleCodes;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    ruleCodes.clear();
    json.getJSONArray(DELETED_RULE_CODES)
    .forEach((ruleCode) -> {
      ruleCodes.add(ruleCode.toString());
    });
    
  }
  
}
