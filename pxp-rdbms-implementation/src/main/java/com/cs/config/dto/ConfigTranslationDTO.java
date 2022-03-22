package com.cs.config.dto;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.json.simple.JSONObject;

import com.cs.config.idto.IConfigTranslationDTO;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.ijosn.IJSONContent;

public class ConfigTranslationDTO extends AbstractConfigJSONDTO implements IConfigTranslationDTO {
  
  private static final long                       serialVersionUID  = 1L;
  private static final BidiMap<ConfigTag, String> KEY_MAP           = new DualHashBidiMap<>();
  private static final Set<ConfigTag>             IGNORED_PXON_TAGS = new HashSet<>();
  
  /** Initializing the static key map */
  static {
    // Read only Properties
    KEY_MAP.put(ConfigTag.code, IPXON.PXONTag.code.toReadOnlyTag());
    KEY_MAP.put(ConfigTag.type, IPXON.PXONTag.type.toReadOnlyTag());
    
    KEY_MAP.put(ConfigTag.languageTranslation, IPXON.PXONTag.languagetranslation.toJSONContentTag());
    
    IGNORED_PXON_TAGS.add(ConfigTag.code);
  }
  
  public ConfigTranslationDTO()
  {
    super(KEY_MAP, IGNORED_PXON_TAGS);
  }
  
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    CSEObject cse = new CSEObject(CSEObjectType.Translation);
    String code = getString(ConfigTag.code);
    cse.setCode(code);
    return cse;
  }
  
  @Override
  void fromCSExpression(JSONContentParser parser) throws CSFormatException
  {
    fromCSExpressID(parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }
  
  @Override
  public void setType(EntityType entityType)
  {
    setString(ConfigTag.type, entityType.getEntityType());
  }
  
  @Override
  public void setCode(String entityCode)
  {
    setString(ConfigTag.code, entityCode);
  }

  @Override
  public IJSONContent getTranslations()
  {
    return getJSONContent(ConfigTag.languageTranslation);
  }

  @Override
  public void setTranslations(IJSONContent translations)
  {
    setJSONContent(ConfigTag.languageTranslation, translations);
  }

  @Override
  public EntityType getEntityType()
  {
    String type = getString(ConfigTag.type);
    return IConfigTranslationDTO.EntityType.valueOfEntityType(type);
  }
  
  @Override
  public void fromPXON(String json) throws CSFormatException
  {
    super.fromPXON(json);
    JSONObject pxonInput = JSONContent.StringToJSON(json);
    String type = (String) pxonInput.get(IPXON.PXONTag.type.toReadOnlyTag());
    setString(ConfigTag.type, type);
  }
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
  }
  
}
