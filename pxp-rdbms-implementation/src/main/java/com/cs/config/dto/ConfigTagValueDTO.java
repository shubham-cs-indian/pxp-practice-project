package com.cs.config.dto;

import com.cs.config.idto.IConfigTagValueDTO;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.TagValueDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

/**
 * @author tauseef
 **/
public class ConfigTagValueDTO extends AbstractConfigJSONDTO implements IConfigTagValueDTO {
  
  /**
   * Tag keys and its corresponding PXON key map
   */
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>                IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Definition of the tag and its PXON key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.propertyIID, IPXON.PXONTag.propertyiid.toReadOnlyTag());

    // Updatable Properties
    KEY_MAP.put(ConfigTag.color, IPXON.PXONTag.color.toTag());
    KEY_MAP.put(ConfigTag.label, IPXON.PXONTag.label.toTag());
    KEY_MAP.put(ConfigTag.linkedMasterTagId, IPXON.PXONTag.linkedmastertag.toTag());
    KEY_MAP.put(ConfigTag.imageResolution, IPXON.PXONTag.imageresolution.toTag());
    KEY_MAP.put(ConfigTag.imageExtension, IPXON.PXONTag.imageextension.toTag());
    KEY_MAP.put(ConfigTag.icon, IPXON.PXONTag.icon.toTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
    IGNORED_PXON_TAGS.add(ConfigTag.propertyIID);
  }
  
  /**
   * TagValue DTO
   */
  private final TagValueDTO tagValueDTO = new TagValueDTO();
  
  /**
   * Initializing the static key map
   */
  public ConfigTagValueDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }

  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    tagValueDTO.setCode( getString(ConfigTag.code));
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    tagValueDTO.fromPXON(json);
    setString(ConfigTag.code, tagValueDTO.getCode());
  }
    
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    return tagValueDTO.toCSExpressID();
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }

  @Override
  public String getLabel()
  {
    return getString(ConfigTag.label);
  }
  
  @Override
  public String getCode()
  {
    return getString(ConfigTag.code);
  }

  public long getPropertyIID () {
    return tagValueDTO.getPropertyIID();
  }

  public void setPropertyIID (long iid) {
    setLong(ConfigTag.propertyIID, iid);
    tagValueDTO.setProperty(iid);
  }

  @Override
  public void setLabel(String label)
  {
    setString(ConfigTag.label, label);
  }

  @Override
  public String getColor()
  {
    return getString(ConfigTag.color);
  }

  @Override
  public void setColor(String color)
  {
    setString(ConfigTag.color, color);
  }

  @Override
  public String getLinkedMasterTag()
  {
    return getString(ConfigTag.linkedMasterTagId);
  }

  @Override
  public void setLinkedMasterTag(String matserTag)
  {
    setString(ConfigTag.linkedMasterTagId, matserTag);
  }

  @Override
  public void setTagValueDTO(String tagValueCode)
  {
    tagValueDTO.setCode(tagValueCode);
  }
  
  @Override
  public Integer getImageResolution()
  {
    return getInt(ConfigTag.imageResolution);
  }

  @Override
  public void setImageResolution(Integer imageResolution)
  {
    setInt(ConfigTag.imageResolution, imageResolution);
  }

  @Override
  public String getImageExtension()
  {
    return getString(ConfigTag.imageExtension);
  }

  @Override
  public void setImageExtension(String imageExtension)
  {
    setString(ConfigTag.imageExtension, imageExtension);
  }
  
  @Override
  public String getIcon()
  {
    return getString(ConfigTag.icon);
  }

  @Override
  public void setIcon(String icon)
  {
    setString(ConfigTag.icon, icon);
  }

}
