package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigRelationshipPermissionDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

public class ConfigRelationshipPermissionDTO extends AbstractConfigJSONDTO
    implements IConfigRelationshipPermissionDTO {
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.relationshipId, IPXON.PXONTag.relationshipId.toReadOnlyTag());
    
    // update only Properties
    
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entityid.toTag());
    KEY_MAP.put(ConfigTag.isVisible, IPXON.PXONTag.isvisible.toTag());
    KEY_MAP.put(ConfigTag.canEdit, IPXON.PXONTag.canEdit.toTag());
    KEY_MAP.put(ConfigTag.canAdd, IPXON.PXONTag.canAdd.toTag());
    KEY_MAP.put(ConfigTag.canDelete, IPXON.PXONTag.canDelete.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigRelationshipPermissionDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.RelationshipPermission);
    String code = getString(ConfigTag.relationshipId);
    cse.setCode(code);
    return cse;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID(parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }
  
  @Override
  public String getEntityId()
  {
    return getString(ConfigTag.entityId);
  }
  
  @Override
  public void setEntityId(String entityId)
  {
    setString(ConfigTag.entityId, entityId);
    
  }
  
  @Override
  public String getRelationshipId()
  {
    return getString(ConfigTag.relationshipId);
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    setString(ConfigTag.relationshipId, relationshipId);
    
  }
  
  @Override
  public Boolean getIsVisible()
  {
    return getBoolean(ConfigTag.isVisible);
  }
  
  @Override
  public void setIsVisible(Boolean isVisible)
  {
    setBoolean(ConfigTag.isVisible, isVisible);
  }
  
  @Override
  public Boolean getCanAdd()
  {
    return getBoolean(ConfigTag.canAdd);
  }
  
  @Override
  public void setCanAdd(Boolean canAdd)
  {
    setBoolean(ConfigTag.canAdd, canAdd);
  }
  
  @Override
  public Boolean getCanDelete()
  {
    return getBoolean(ConfigTag.canDelete);
  }
  
  @Override
  public void setCanDelete(Boolean canDelete)
  {
    setBoolean(ConfigTag.canDelete, canDelete);
    
  }
  
  @Override
  public Boolean getCanEdit()
  {
    return getBoolean(ConfigTag.canEdit);
  }
  
  @Override
  public void setCanEdit(Boolean canEdit)
  {
    setBoolean(ConfigTag.canEdit, canEdit);
    
  }
}
