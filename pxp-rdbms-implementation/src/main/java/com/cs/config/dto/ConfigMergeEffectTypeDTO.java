package com.cs.config.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

import com.cs.config.idto.IConfigMergeEffectTypeDTO;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

public class ConfigMergeEffectTypeDTO extends AbstractConfigJSONDTO implements IConfigMergeEffectTypeDTO {
  
  private static final long                       serialVersionUID  = 1L;
  /**
   * Tag keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /**
   * Initializing the static key map
   */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    
    // update only Properties
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toTag());
    KEY_MAP.put(ConfigTag.entityId, IPXON.PXONTag.entityid.toTag());
    KEY_MAP.put(ConfigTag.entityType, IPXON.PXONTag.entitytype.toTag());
    KEY_MAP.put(ConfigTag.supplierIds, IPXON.PXONTag.supplierids.toJSONArrayTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigMergeEffectTypeDTO() throws CSFormatException
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    return null;
  }
  
  @Override
  public String getType()
  {
    return getString(ConfigTag.type);
  }
  
  @Override
  public void setType(String type)
  {
    setString(ConfigTag.type, type);
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
  public String getEntityType()
  {
    return getString(ConfigTag.entityType);
  }
  
  @Override
  public void setEntityType(String type)
  {
    setString(ConfigTag.entityType, type);
  }
  
  @Override
  public List<String> getSupplierIds()
  {
    return getJSONArray(ConfigTag.supplierIds);
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
 // No CSE Element
  }

  @Override
  public void setSupplierIds(List<String> supplierIds)
  {
    configData.setStringArrayField(ConfigTag.supplierIds.name(), supplierIds);
  }
  
}
