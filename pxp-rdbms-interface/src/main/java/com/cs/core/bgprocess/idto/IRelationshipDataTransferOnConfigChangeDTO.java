package com.cs.core.bgprocess.idto;

import java.util.List;

import com.cs.core.rdbms.coupling.idto.IModifiedPropertiesCouplingDTO;

public interface IRelationshipDataTransferOnConfigChangeDTO extends IInitializeBGProcessDTO {
  
  public static final String RELATIONSHIP_IID          = "relationshipIID";
  public static final String IS_NATURE                 = "isNature";
  public static final String DELETED_PROPERTY_IIDS     = "deletedPropertyIIDs";
  public static final String SIDE1_ADDED_PROPERTY_IIDS = "side1AddedPropertyIIDs";
  public static final String SIDE2_ADDED_PROPERTY_IIDS = "side2AddedPropertyIIDs";
  public static final String MODIFIED_PROPERIES        = "modifiedProperties";

  
  public Long getRelationshipIID();
  
  public void setRelationshipIID(Long relationshipIID);
  
  public Boolean getIsNature();
  
  public void setIsNature(Boolean isNature);

  public List<Long> getDeletedPropertyIIDs();
  
  public void setDeletedPropertyIIDs(List<Long> deletedPropertyIIDs);
  
  public List<Long> getSide1AddedPropertyIIDs();
  
  public void setSide1AddedPropertyIIDs(List<Long> side1AddedPropertyIIDs);
  
  public List<Long> getSide2AddedPropertyIIDs();
  
  public void setSide2AddedPropertyIIDs(List<Long> side2AddedPropertyIIDs);
  
  public List<IModifiedPropertiesCouplingDTO> getModifiedProperties();
  
  public void setModifiedProperties(List<IModifiedPropertiesCouplingDTO> modifiedProperties);

}
