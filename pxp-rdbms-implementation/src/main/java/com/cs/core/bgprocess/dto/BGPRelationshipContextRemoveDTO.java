package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IBGPRelationshipContextRemoveDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class BGPRelationshipContextRemoveDTO extends InitializeBGProcessDTO implements IBGPRelationshipContextRemoveDTO {
  
  private static final long  serialVersionUID        = 1L;
  public static final String RELATIONSHIP_PROPERTYID = "relationshipPropertyId";
  public static final String REMOVED_SIDE1_CONTEXTID = "removedSide1ContextId";
  public static final String REMOVED_SIDE2_CONTEXTID = "removedSide2ContextId";
  
  private Long               relationshipPropertyId;
  private String             removedSide1ContextId;
  private String             removedSide2ContextId;
  
  @Override
  public Long getRelationshipPropertyId()
  {
    return relationshipPropertyId;
  }
  
  @Override
  public void setRelationshipPropertyId(Long relationshipPropertyId)
  {
    this.relationshipPropertyId = relationshipPropertyId;
  }
  
  @Override
  public String getRemovedSide1ContextId()
  {
    return removedSide1ContextId;
  }
  
  @Override
  public void setRemovedSide1ContextId(String removedSide1ContextId)
  {
    this.removedSide1ContextId = removedSide1ContextId;
  }
  
  @Override
  public String getRemovedSide2ContextId()
  {
    return removedSide2ContextId;
  }
  
  @Override
  public void setRemovedSide2ContextId(String removedSide2ContextId)
  {
    this.removedSide2ContextId = removedSide2ContextId;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), JSONBuilder.newJSONField(RELATIONSHIP_PROPERTYID, relationshipPropertyId),
        JSONBuilder.newJSONField(REMOVED_SIDE1_CONTEXTID, removedSide1ContextId),
        JSONBuilder.newJSONField(REMOVED_SIDE2_CONTEXTID, removedSide2ContextId));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    relationshipPropertyId = json.getLong(RELATIONSHIP_PROPERTYID);
    removedSide1ContextId = json.getString(REMOVED_SIDE1_CONTEXTID);
    removedSide2ContextId = json.getString(REMOVED_SIDE2_CONTEXTID);
  }
}
