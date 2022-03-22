package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigPropertyPermissionDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;

public class ConfigPropertyPermissionDTO extends AbstractConfigJSONDTO
    implements IConfigPropertyPermissionDTO {
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.propertyId, IPXON.PXONTag.propertyId.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.propertyType, IPXON.PXONTag.propertytype.toTag());
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entity.toTag());
    KEY_MAP.put(ConfigTag.isVisible, IPXON.PXONTag.isvisible.toTag());
    KEY_MAP.put(ConfigTag.canEdit, IPXON.PXONTag.canEdit.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigPropertyPermissionDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.PropertyPermission);
    String code = getString(ConfigTag.propertyId);
    cse.setCode(code);
    return cse;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID(parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }
  
  @Override
  public Boolean getIsVisible()
  {
    return getBoolean(ConfigTag.isVisible);
  }
  
  @Override
  public Boolean getCanEdit()
  {
    return getBoolean(ConfigTag.canEdit);
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
  public String getPropertyId()
  {
    return getString(ConfigTag.propertyId);
  }
  
  @Override
  public void setPropertyId(String propertyId)
  {
    setString(ConfigTag.propertyId, propertyId);
    
  }
  
  @Override
  public void setIsVisible(Boolean isVisible)
  {
    setBoolean(ConfigTag.isVisible, isVisible);
    
  }
  
  @Override
  public void setCanEdit(Boolean canEdit)
  {
    setBoolean(ConfigTag.canEdit, canEdit);
    
  }
  
  @Override
  public String getPropertyType()
  {
    return getString(ConfigTag.propertyType);
  }
  
  @Override
  public void setPropertyType(String propertytype)
  {
    setString(ConfigTag.propertyType, propertytype);
    
  }
  
}
