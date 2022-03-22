package com.cs.core.bgprocess.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.cs.core.bgprocess.idto.IBaseEntityPlanForBulkCloneDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class BaseEntityPlanForBulkCloneDTO extends BaseEntityPlanDTO implements IBaseEntityPlanForBulkCloneDTO {
  
  public static final String NATURE_RELATIONSHIP_IDS = "natureRelationshipIds";
  private final Set<String> natureRelationshipIds    = new HashSet<>();
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    natureRelationshipIds.clear();
    json.getJSONArray(NATURE_RELATIONSHIP_IDS)
        .forEach((iid) -> {
          natureRelationshipIds.add((String) iid);
        });
    super.fromJSON(json);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONStringArray(NATURE_RELATIONSHIP_IDS, natureRelationshipIds));
  }

  @Override
  public Set<String> getNatureRelationshipIds()
  {
  
    return natureRelationshipIds;
  }

  @Override
  public void setNatureRelationshipIds(Collection<String> natureRelationshipIds)
  {
    this.natureRelationshipIds.clear();
    this.natureRelationshipIds.addAll(natureRelationshipIds); 
  }
  
 
}
