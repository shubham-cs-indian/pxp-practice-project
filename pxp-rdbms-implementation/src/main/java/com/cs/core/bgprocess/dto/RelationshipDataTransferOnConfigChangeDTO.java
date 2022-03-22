package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.IRelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.dto.ModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.technical.exception.CSFormatException;

public class RelationshipDataTransferOnConfigChangeDTO extends InitializeBGProcessDTO
    implements IRelationshipDataTransferOnConfigChangeDTO {
  
  /**
   * 
   */
  private static final long serialVersionUID       = 1L;
  private Long              relationshipIID;
  private Boolean           isNature               = false;
  private List<Long>        deletedPropertyIIDs    = new ArrayList<>();
  private List<Long>        side1AddedPropertyIIDs = new ArrayList<Long>();
  private List<Long>        side2AddedPropertyIIDs = new ArrayList<Long>();
  private List<IModifiedPropertiesCouplingDTO> modifiedProperties = new ArrayList<>();
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), JSONBuilder.newJSONField(RELATIONSHIP_IID, relationshipIID),
        JSONBuilder.newJSONField(IS_NATURE, isNature),
        JSONBuilder.newJSONLongArray(DELETED_PROPERTY_IIDS, deletedPropertyIIDs),
        JSONBuilder.newJSONLongArray(SIDE1_ADDED_PROPERTY_IIDS, side1AddedPropertyIIDs),
        JSONBuilder.newJSONLongArray(SIDE2_ADDED_PROPERTY_IIDS, side2AddedPropertyIIDs),
        JSONBuilder.newJSONArray(MODIFIED_PROPERIES, modifiedProperties));
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    relationshipIID = json.getLong(RELATIONSHIP_IID);
    isNature = json.getBoolean(IS_NATURE);
    deletedPropertyIIDs.clear();
    json.getJSONArray(IRelationshipDataTransferOnConfigChangeDTO.DELETED_PROPERTY_IIDS).forEach((iid) -> {
      deletedPropertyIIDs.add((Long) iid);
    });
    
    side1AddedPropertyIIDs.clear();
    json.getJSONArray(IRelationshipDataTransferOnConfigChangeDTO.SIDE1_ADDED_PROPERTY_IIDS).forEach((iid) -> {
      side1AddedPropertyIIDs.add((Long) iid);
    });
    
    side2AddedPropertyIIDs.clear();
    json.getJSONArray(IRelationshipDataTransferOnConfigChangeDTO.SIDE2_ADDED_PROPERTY_IIDS).forEach((iid) -> {
      side2AddedPropertyIIDs.add((Long) iid);
    });
    
    modifiedProperties.clear();
    json.getJSONArray(MODIFIED_PROPERIES).forEach((iid) -> {
      IModifiedPropertiesCouplingDTO modifiedPropertiesDTO = new ModifiedPropertiesCouplingDTO();
      try {
        modifiedPropertiesDTO.fromJSON(iid.toString());
        modifiedProperties.add(modifiedPropertiesDTO);
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
    
  }
  
  @Override
  public Long getRelationshipIID()
  {
    return relationshipIID;
  }
  
  @Override
  public void setRelationshipIID(Long relationshipIID)
  {
    this.relationshipIID = relationshipIID;
  }
  
  @Override
  public List<Long> getDeletedPropertyIIDs()
  {
    return deletedPropertyIIDs;
  }
  
  @Override
  public void setDeletedPropertyIIDs(List<Long> deletedPropertyIIDs)
  {
    this.deletedPropertyIIDs = deletedPropertyIIDs;
  }
  
  @Override
  public List<Long> getSide1AddedPropertyIIDs()
  {
    return side1AddedPropertyIIDs;
  }
  
  @Override
  public void setSide1AddedPropertyIIDs(List<Long> side1AddedPropertyIIDs)
  {
    this.side1AddedPropertyIIDs = side1AddedPropertyIIDs;
  }
  
  @Override
  public List<Long> getSide2AddedPropertyIIDs()
  {
    return side2AddedPropertyIIDs;
  }
  
  @Override
  public void setSide2AddedPropertyIIDs(List<Long> side2AddedPropertyIIDs)
  {
    this.side2AddedPropertyIIDs = side2AddedPropertyIIDs;
  }

  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }

  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }

  @Override
  public List<IModifiedPropertiesCouplingDTO> getModifiedProperties()
  {
    return modifiedProperties;
  }

  @Override
  public void setModifiedProperties(List<IModifiedPropertiesCouplingDTO> modifiedProperties)
  {
    this.modifiedProperties = modifiedProperties;
  }
  
}
