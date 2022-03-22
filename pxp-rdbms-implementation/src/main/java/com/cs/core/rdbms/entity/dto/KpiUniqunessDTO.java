package com.cs.core.rdbms.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.entity.idto.IKpiUniqunessDTO;
import com.cs.core.technical.exception.CSFormatException;

public class KpiUniqunessDTO extends SimpleDTO implements IKpiUniqunessDTO {
  
  private static final long serialVersionUID = 1L;
  public double             result;
  public Long               ruleExpressionIID = 0L;
  public List<Long>         baseEntityIID    = new ArrayList<>();
  public Long               sourceIID = 0L;
  
  @Override
  public double getResult()
  {
    return result;
  }
  
  @Override
  public void setResult(double result)
  {
    this.result = result;
  }
  
  @Override
  public Long getRuleExpressionIID()
  {
    return ruleExpressionIID;
  }
  
  @Override
  public void setRuleExpressionIID(Long ruleExpressionIID)
  {
    this.ruleExpressionIID = ruleExpressionIID;
  }
  
  @Override
  public List<Long> getBaseEntityIID()
  {
    return baseEntityIID;
  }
  
  @Override
  public void setBaseEntityIID(List<Long> baseEntityIID)
  {
    this.baseEntityIID = baseEntityIID;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    result = parser.getDouble(RESULT);
    ruleExpressionIID = parser.getLong(RULE_EXPRESSION_IID);
    
    parser.getJSONArray(BASE_ENTITY_IID).forEach((iid) -> {
      baseEntityIID.add((Long)iid);
    });
    sourceIID = parser.getLong(SOURCE_IID);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(RESULT, result),
        JSONBuilder.newJSONField(RULE_EXPRESSION_IID, ruleExpressionIID), 
        JSONBuilder.newJSONLongArray(BASE_ENTITY_IID, baseEntityIID),
        JSONBuilder.newJSONField(SOURCE_IID, sourceIID));
  }

  @Override
  public Long getSourceIID()
  {
    return sourceIID;
  }

  @Override
  public void setSourceIID(long sourceIID)
  {
    this.sourceIID = sourceIID;
  }
}
