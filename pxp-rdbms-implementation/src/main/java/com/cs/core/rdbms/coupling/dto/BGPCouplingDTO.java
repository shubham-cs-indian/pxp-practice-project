package com.cs.core.rdbms.coupling.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IBGPCouplingDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class BGPCouplingDTO extends SimpleDTO implements IBGPCouplingDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID     = 1L;
  
  public List<Long>         sourceBaseEntityIIDs = new ArrayList<>();
  public List<Long>         addedEntityIIDs      = new ArrayList<>();
  public List<Long>         deletedEntityIIDs    = new ArrayList<>();
  public String             relationshipId;
  public String             natureRelationshipId;
  public String             sideId;
  
  @Override
  public void setSourceBaseEntityIIDs(List<Long> sourceBaseEntityIIDs)
  {
    this.sourceBaseEntityIIDs = sourceBaseEntityIIDs;
  }
  
  @Override
  public List<Long> getSourceBaseEntityIIDs()
  {
    return sourceBaseEntityIIDs;
  }
  
  @Override
  public void setAddedEntityIIDs(List<Long> addedEntityIIDs)
  {
    this.addedEntityIIDs = addedEntityIIDs;
  }
  
  @Override
  public List<Long> getAddedEntityIIDs()
  {
    return addedEntityIIDs;
  }
  
  @Override
  public void setDeletedEntityIIDs(List<Long> deletedEntityIIDs)
  {
    this.deletedEntityIIDs = deletedEntityIIDs;
  }
  
  @Override
  public List<Long> getDeletedEntityIIDs()
  {
    return deletedEntityIIDs;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    parser.getJSONArray(SOURCE_BASE_ENTITY_IIDS).forEach((iid) -> {
      sourceBaseEntityIIDs.add((Long) iid);
    });
    
    parser.getJSONArray(ADDED_ENTITY_IIDS).forEach((iid) -> {
      addedEntityIIDs.add((Long) iid);
    });
    
    parser.getJSONArray(DELETED_ENTITY_IIDS).forEach((iid) -> {
      deletedEntityIIDs.add((Long) iid);
    });
    
    relationshipId = parser.getString(RELATIONSHIP_ID);
    natureRelationshipId = parser.getString(NATURE_RELATIONSHIP_ID);
    sideId = parser.getString(SIDE_ID);
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONLongArray(SOURCE_BASE_ENTITY_IIDS, sourceBaseEntityIIDs),
        JSONBuilder.newJSONLongArray(ADDED_ENTITY_IIDS, addedEntityIIDs),
        JSONBuilder.newJSONLongArray(DELETED_ENTITY_IIDS, deletedEntityIIDs),
        relationshipId != null ? JSONBuilder.newJSONField(RELATIONSHIP_ID, relationshipId) : JSONBuilder.VOID_FIELD,
//        JSONBuilder.newJSONField(RELATIONSHIP_ID, relationshipId),
        natureRelationshipId != null ? JSONBuilder.newJSONField(NATURE_RELATIONSHIP_ID, natureRelationshipId) : JSONBuilder.VOID_FIELD, 
        //JSONBuilder.newJSONField(NATURE_RELATIONSHIP_ID, natureRelationshipId),
        sideId != null ? JSONBuilder.newJSONField(SIDE_ID, sideId) : JSONBuilder.VOID_FIELD);
  }
  
  @Override
  public void setNatureRelationshipId(String natureRelationshipId)
  {
    this.natureRelationshipId = natureRelationshipId;
  }
  
  @Override
  public String getNatureRelationishipId()
  {
    return natureRelationshipId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public String getSideId()
  {
    return sideId;
  }
  
}
