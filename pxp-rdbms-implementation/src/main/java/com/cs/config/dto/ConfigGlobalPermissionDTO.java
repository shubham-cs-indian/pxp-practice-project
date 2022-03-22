package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigGlobalPermissionDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigGlobalPermissionDTO extends AbstractConfigJSONDTO implements IConfigGlobalPermissionDTO {
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entityid.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.canRead, IPXON.PXONTag.canRead.toTag());
    KEY_MAP.put(ConfigTag.canEdit, IPXON.PXONTag.canEdit.toTag());
    KEY_MAP.put(ConfigTag.canDelete, IPXON.PXONTag.canDelete.toTag());
    KEY_MAP.put(ConfigTag.canCreate, IPXON.PXONTag.canCreate.toTag());
    KEY_MAP.put(ConfigTag.canDownload, IPXON.PXONTag.canDownload.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigGlobalPermissionDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    // No CSE element
  }
  
  @Override
  public Boolean getCanRead()
  {
    return getBoolean(ConfigTag.canRead);
  }
  
  @Override
  public void setCanRead(Boolean canRead)
  {
    setBoolean(ConfigTag.canRead, canRead);
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
  public Boolean getCanCreate()
  {
    return getBoolean(ConfigTag.canCreate);
  }
  
  @Override
  public void setCanCreate(Boolean canCreate)
  {
    setBoolean(ConfigTag.canCreate, canCreate);
  }
  
  @Override
  public Boolean getCanDownload()
  {
    return getBoolean(ConfigTag.canDownload);
  }
  
  @Override
  public void setCanDownload(Boolean canDownload)
  {
    setBoolean(ConfigTag.canDownload, canDownload);
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
  
}
