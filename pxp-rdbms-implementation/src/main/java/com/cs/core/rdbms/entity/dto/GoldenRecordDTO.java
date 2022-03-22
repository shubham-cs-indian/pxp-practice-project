package com.cs.core.rdbms.entity.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.entity.idto.IGoldenRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;

public class GoldenRecordDTO extends InitializeBGProcessDTO implements IGoldenRecordDTO {
  
  private static final long serialVersionUID  = 1L;
  
  private String            ruleId            = null;
  private List<Long>        linkedBaseEntites = new ArrayList<Long>();
  
  public GoldenRecordDTO()
  {
  }
  
  protected GoldenRecordDTO(String ruleId, List<Long> linkedBaseEntites)
  {
    this.ruleId = ruleId;
    this.linkedBaseEntites = linkedBaseEntites;
  }
  
  protected GoldenRecordDTO(IResultSetParser result) throws Exception
  {
    this.ruleId = result.getString(GoldenRecordDTO.RULE_ID);
  }
  
  @Override
  public String getRuleId()
  {
    return ruleId;
  }
  
  @Override
  public List<Long> getLinkedBaseEntities()
  {
    return linkedBaseEntites;
  }
  
  @Override
  public void setLinkedBaseEntities(List<Long> linkedBaseEntites)
  {
    this.linkedBaseEntites = linkedBaseEntites;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONLongArray(GoldenRecordDTO.LINKED_BASE_ENTITES, linkedBaseEntites), JSONBuilder.newJSONField(RULE_ID, ruleId));
  }
  
  @Override
  public void setRuleId(String ruleId)
  {
    this.ruleId = ruleId;
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    ruleId = json.getString(RULE_ID);
    linkedBaseEntites.clear();
    json.getJSONArray(LINKED_BASE_ENTITES).forEach((iid) -> {
      linkedBaseEntites.add((Long) iid);
    });
  }
}
