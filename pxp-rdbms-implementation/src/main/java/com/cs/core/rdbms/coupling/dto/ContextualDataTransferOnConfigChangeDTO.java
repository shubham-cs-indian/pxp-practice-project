package com.cs.core.rdbms.coupling.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.idto.IRelationshipDataTransferOnConfigChangeDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferOnConfigChangeDTO;
import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class ContextualDataTransferOnConfigChangeDTO extends SimpleDTO implements IContextualDataTransferOnConfigChangeDTO {
  
  /**
   * 
   */
  private static final long                   serialVersionUID = 1L;
  public Long                                 contextualIID;
  public List<IModifiedPropertiesCouplingDTO> addedProperties = new ArrayList<>();
  public List<IModifiedPropertiesCouplingDTO> modifiedProperties = new ArrayList<>();
  public List<Long>                           deletedPropertyIIDs =  new ArrayList<>();
  
  
  @Override
  public void setContextualIID(Long contextualIID)
  {
    this.contextualIID = contextualIID;
  }
  
  @Override
  public Long getContextualIID()
  {
    return contextualIID;
  }
  
  @Override
  public void setAddedProperties(List<IModifiedPropertiesCouplingDTO> addedProperties)
  {
    this.addedProperties = addedProperties;
  }
  
  @Override
  public List<IModifiedPropertiesCouplingDTO> getAddedProperties()
  {
    return addedProperties;
  }
  
  @Override
  public void setModifiedProperties(List<IModifiedPropertiesCouplingDTO> modifiedProperties)
  {
    this.modifiedProperties = modifiedProperties;
  }
  
  @Override
  public List<IModifiedPropertiesCouplingDTO> getModifiedProperties()
  {
    return modifiedProperties;
  }
  
  @Override
  public void setDeletedPropertyIIDs(List<Long> deletedPropertyIIDs)
  {
    this.deletedPropertyIIDs = deletedPropertyIIDs;
  }
  
  @Override
  public List<Long> getDeletedPropertyIIDs()
  {
    return deletedPropertyIIDs;
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    contextualIID = json.getLong(CONTEXTUAL_IID);
    
    deletedPropertyIIDs.clear();
    json.getJSONArray(IRelationshipDataTransferOnConfigChangeDTO.DELETED_PROPERTY_IIDS).forEach((iid) -> {
      deletedPropertyIIDs.add((Long) iid);
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
    
    
    addedProperties.clear();
    json.getJSONArray(ADDED_PROPERTIES).forEach((iid) -> {
      IModifiedPropertiesCouplingDTO modifiedPropertiesDTO = new ModifiedPropertiesCouplingDTO();
      try {
        modifiedPropertiesDTO.fromJSON(iid.toString());
        addedProperties.add(modifiedPropertiesDTO);
      }
      catch (CSFormatException e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    });
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer( 
        JSONBuilder.newJSONField(CONTEXTUAL_IID, contextualIID),
        JSONBuilder.newJSONLongArray(DELETED_PROPERTY_IIDS, deletedPropertyIIDs),
        JSONBuilder.newJSONArray(MODIFIED_PROPERIES, modifiedProperties),
        JSONBuilder.newJSONArray(ADDED_PROPERTIES, addedProperties));
  }
  
}
