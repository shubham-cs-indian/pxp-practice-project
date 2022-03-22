package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IEntityRelationshipInfoDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class EntityRelationshipInfoDTO extends SimpleDTO implements IEntityRelationshipInfoDTO {

  /**
   * 
   */
  private static final long  serialVersionUID       = 1L;
  public static final String ADDED_ELEMENT          = "addedElemet";
  public static final String REMOVED_ELEMENT        = "removedElemet";
  public static final String NATURE_RELATIONSHIP_ID = "natureRelationshipId";
  public static final String RELATIONSHIP_ID        = "relationshipId";
  public static final String SIDE_ID                = "sideId";
  
  protected List<Long>       addedElements          = new ArrayList<Long>();
  protected List<Long>       removedElements        = new ArrayList<Long>();
  private String             natureRelationshipId;
  private String             relationshipId;
  private String             sideId;
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(
        JSONBuilder.newJSONLongArray(ADDED_ELEMENT, addedElements),
        JSONBuilder.newJSONLongArray(REMOVED_ELEMENT, removedElements),JSONBuilder.newJSONField(SIDE_ID, sideId),
        JSONBuilder.newJSONField(RELATIONSHIP_ID, relationshipId),
        JSONBuilder.newJSONField(NATURE_RELATIONSHIP_ID, natureRelationshipId));  
   
  }

  @Override
  public List<Long> getAddedElements()
  {
    return addedElements;
  }

  @Override
  public void setAddedElements(List<Long> addedElements)
  {
    this.addedElements = addedElements;
  }

  @Override
  public List<Long> getRemovedElements()
  {
    return removedElements;
  }

  @Override
  public void setRemovedElements(List<Long> removedElements)
  { 
    this.removedElements = removedElements;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    addedElements.clear();
    JSONArray addedEntitiesArray = json.getJSONArray(ADDED_ELEMENT);
    for (Object jsonV : addedEntitiesArray) {
      addedElements.add((Long)jsonV);
    }
    removedElements.clear();
    JSONArray removedEntitiesArray = json.getJSONArray(REMOVED_ELEMENT);
    for (Object jsonV : removedEntitiesArray) {
      removedElements.add((Long)jsonV);
    }
    natureRelationshipId = json.getString(NATURE_RELATIONSHIP_ID);
    sideId =json.getString(SIDE_ID);
    relationshipId = json.getString(RELATIONSHIP_ID);
  }

  @Override
  public String getNatureRelationshipId()
  {
    return natureRelationshipId;
  }

  @Override
  public void setNatureRelationshipId(String natureRelationshipId)
  {
    this.natureRelationshipId = natureRelationshipId;
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
