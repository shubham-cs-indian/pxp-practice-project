package com.cs.core.rdbms.coupling.idto;

import java.util.List;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IContextualDataTransferOnConfigChangeDTO extends ISimpleDTO {
  
  public static final String CONTEXTUAL_IID        = "contextualIID";
  public static final String DELETED_PROPERTY_IIDS = "deletedPropertyIIDs";
  public static final String MODIFIED_PROPERIES    = "modifiedProperties";
  public static final String ADDED_PROPERTIES      = "addedProperties";
  
  public void setContextualIID(Long contextualIID);
  public Long getContextualIID();
  
  public void setAddedProperties(List<IModifiedPropertiesCouplingDTO> addedProperties);
  public List<IModifiedPropertiesCouplingDTO> getAddedProperties();
  
  public void setModifiedProperties(List<IModifiedPropertiesCouplingDTO> modifiedProperties);
  public List<IModifiedPropertiesCouplingDTO> getModifiedProperties();
  
  public void setDeletedPropertyIIDs(List<Long> deletedPropertyIIDs);
  public List<Long> getDeletedPropertyIIDs();
  
}
