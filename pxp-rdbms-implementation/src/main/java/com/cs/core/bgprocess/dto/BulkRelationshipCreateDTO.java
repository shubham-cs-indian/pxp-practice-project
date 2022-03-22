package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.IBulkRelationshipCreateDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

public class BulkRelationshipCreateDTO extends InitializeBGProcessDTO implements IBulkRelationshipCreateDTO {
  
  //TODO : Make these DTO constants private.
  public static final String SUCCESS_INSTANCE_IIDS  = "successInstanceIIds";
  public static final String SIDE_1_INSTANCE_ID     = "side1InstanceId";
  public static final String SIDE_1_BASE_TYPE       = "side1baseType";
  public static final String MODIFIED_RELATIONSHIPS = "modifiedRelationship";
  public static final String TAB_ID                 = "tabId";
  public static final String TYPE_ID                = "typeId";
  public static final String TAB_TYPE               = "tabType";
  public static final String RELATIONSHIP_ID        = "relationshipId";
  public static final String RELATIONSHIP_ENTITY_ID = "relationshipEntityId";
  public static final String SIDE_ID                = "sideId";
  
  private List<Long>          successInstanceIIds    = new ArrayList<>();
  private String              side1InstanceId;
  private String              side1baseType;
  private String              tabType;
  private String              tabId;
  private String              relationshipId;
  private String              relationshipEntityId;
  private String              sideId;
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONLongArray(SUCCESS_INSTANCE_IIDS, successInstanceIIds),
        JSONBuilder.newJSONField(SIDE_1_INSTANCE_ID, side1InstanceId),
        JSONBuilder.newJSONField(TAB_ID, tabId),
        JSONBuilder.newJSONField(TAB_TYPE, tabType),
        JSONBuilder.newJSONField(SIDE_1_BASE_TYPE, side1baseType),
        JSONBuilder.newJSONField(RELATIONSHIP_ID, relationshipId),
        JSONBuilder.newJSONField(RELATIONSHIP_ENTITY_ID, relationshipEntityId),
        JSONBuilder.newJSONField(SIDE_ID, sideId)
        );
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    successInstanceIIds.clear();
    successInstanceIIds = json.getJSONArray(SUCCESS_INSTANCE_IIDS);
    side1InstanceId = json.getString(SIDE_1_INSTANCE_ID);
    side1baseType = json.getString(SIDE_1_BASE_TYPE);
    tabId = json.getString(TAB_ID);
    tabType = json.getString(TAB_TYPE);
    relationshipId = json.getString(RELATIONSHIP_ID);
    relationshipEntityId = json.getString(RELATIONSHIP_ENTITY_ID);
    sideId = json.getString(SIDE_ID);
  }
  
  @Override
  public List<Long> getSuccessInstanceIIds()
  {
    return successInstanceIIds;
  }

  @Override
  public void setSuccessInstanceIIds(List<Long> successInstanceIIds)
  {
    this.successInstanceIIds = successInstanceIIds;
  }

  @Override
  public void setSide1InstanceId(String id)
  {
    this.side1InstanceId = id;
  }
  
  @Override
  public String getSide1InstanceId()
  {
    return side1InstanceId;  
  }
  
  @Override
  public String getSide1BaseType()
  {
    return side1baseType;
  }
  
  @Override
  public void setSide1BaseType(String baseType)
  {
    this.side1baseType = baseType;
  }
  
  @Override
  public String getTabId()
  {
    return tabId;
  }
  
  
  @Override
  public void setTabId(String tabId)
  {
    this.tabId = tabId;
  }
  
  @Override
  public String getTabType()
  {
    return tabType;
  }
  
  @Override
  public void setTabType(String tabType)
  {
    this.tabType = tabType;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }

  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }

  @Override
  public String getRelationshipEntityId()
  {
    return relationshipEntityId;
  }

  @Override
  public void setRelationshipEntityId(String relationshipEntityId)
  {
    this.relationshipEntityId = relationshipEntityId;
  }

  @Override
  public String getSideId()
  {
    return sideId;
  }

  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
}
