package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IRelationshipInheritanceModifiedRelationshipDTO;
import com.cs.core.bgprocess.idto.IRelationshipInheritanceOnConfigChangeDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class RelationshipInheritanceOnConfigChangeDTO extends SimpleDTO implements IRelationshipInheritanceOnConfigChangeDTO {
  
  private static final long                                     serialVersionUID               = 1L;
  
  public static final String                                    RELATIONSHIP_ID                = "relationshipId";
  public static final String                                    SIDE_ID                        = "sideId";
  public static final String                                    DELETED_RELATIONSHIPS          = "deletedRelationships";
  public static final String                                    MODIFIED_RELATIONSHIPS         = "modifiedRelationships";
  public static final String                                    DELETED_NATURE_RELATIONSHIP_IDS = "deletedNatureRelationshipIds";
  
  private String                                                relationshipId;
  private String                                                sideId;
  private List<String>                                          deletedRelationships           = new ArrayList<>();
  private List<IRelationshipInheritanceModifiedRelationshipDTO> modifiedRelationships          = new ArrayList<>();
  private List<String>                                          deletedNatureRelationshipIds   = new ArrayList<>();
  
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
  public String getSideId()
  {
    return sideId;
  }
  
  @Override
  public void setSideId(String sideId)
  {
    this.sideId = sideId;
  }
  
  @Override
  public List<String> getDeletedRelationships()
  {
    return deletedRelationships;
  }
  
  @Override
  public void setDeletedRelationships(List<String> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  @Override
  public List<IRelationshipInheritanceModifiedRelationshipDTO> getModifiedRelationships()
  {
    return modifiedRelationships;
  }
  
  @Override
  public void setModifiedRelationships(List<IRelationshipInheritanceModifiedRelationshipDTO> modifiedRelationships)
  {
    this.modifiedRelationships = modifiedRelationships;
  }
  
  @Override
  public List<String> getDeletedNatureRelationshipIds()
  {
    return deletedNatureRelationshipIds;
  }

  @Override
  public void setDeletedNatureRelationshipIds(List<String> deletedNatureRelationshipIds)
  {
    this.deletedNatureRelationshipIds = deletedNatureRelationshipIds;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONField(RELATIONSHIP_ID, relationshipId),
        JSONBuilder.newJSONField(SIDE_ID, sideId), JSONBuilder.newJSONStringArray(DELETED_RELATIONSHIPS, deletedRelationships),
        JSONBuilder.newJSONArray(MODIFIED_RELATIONSHIPS, modifiedRelationships),
        JSONBuilder.newJSONStringArray(DELETED_NATURE_RELATIONSHIP_IDS, deletedNatureRelationshipIds));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    deletedRelationships.clear();
    JSONArray deletedRelationshipsArray = json.getJSONArray(DELETED_RELATIONSHIPS);
    for (Object jsonV : deletedRelationshipsArray) {
      deletedRelationships.add((String) jsonV);
    }
    modifiedRelationships.clear();
    JSONArray modifiedRelationshipsArray = json.getJSONArray(MODIFIED_RELATIONSHIPS);
    for (Object jsonV : modifiedRelationshipsArray) {
      RelationshipInheritanceModifiedRelationshipDTO modifiedRelationDTO = new RelationshipInheritanceModifiedRelationshipDTO();
      modifiedRelationDTO.fromJSON(jsonV.toString());
      modifiedRelationships.add(modifiedRelationDTO);
    }
    deletedNatureRelationshipIds.clear();
    JSONArray deletedNatureRelationshipArray = json.getJSONArray(DELETED_NATURE_RELATIONSHIP_IDS);
    for (Object jsonV : deletedNatureRelationshipArray) {
      deletedNatureRelationshipIds.add((String) jsonV);
    }
    
    relationshipId = json.getString(RELATIONSHIP_ID);
    sideId = json.getString(SIDE_ID);
  }

}
