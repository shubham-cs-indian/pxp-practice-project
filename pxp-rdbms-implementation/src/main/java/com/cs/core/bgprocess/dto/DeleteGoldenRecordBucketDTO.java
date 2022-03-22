package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IDeleteGoldenRecordBucketDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class DeleteGoldenRecordBucketDTO extends SimpleDTO implements IDeleteGoldenRecordBucketDTO {
  
  public static final String RULE_ID          = "ruleId";
  
  private static final long  serialVersionUID = 1L;
  private String             ruleId;
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    ruleId = json.getString(RULE_ID);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(RULE_ID, ruleId));
  }
}
