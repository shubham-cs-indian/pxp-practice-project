package com.cs.config.dto;

import com.cs.config.idto.IConfigNatureRelationshipDTO;
import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;

public class ConfigNatureRelationshipDTO extends ConfigRelationshipDTO implements IConfigNatureRelationshipDTO{
  
  static {
    KEY_MAP.put(ConfigTag.taxonomyInheritanceSetting, IPXON.PXONTag.taxonomyInheritanceSetting.toTag());
  }
  
  /**
   * Initializing the static key map
   */
  public ConfigNatureRelationshipDTO()
  {
    super();
  }
  
  @Override
  public String getTaxonomyInheritanceSetting()
  {
    return getString(ConfigTag.taxonomyInheritanceSetting);
  }

  @Override
  public void setTaxonomyInheritanceSetting(String taxonomyInheritanceSetting)
  {
    setString(ConfigTag.taxonomyInheritanceSetting, taxonomyInheritanceSetting);
  }
  
  protected void initPropertyDTO() throws CSFormatException {
    super.initPropertyDTO();
  }

  @Override
  public void fromPXON(String json) throws CSFormatException {
    super.fromPXON(json);
  }

  @Override
  protected JSONContent toConfigPXONContent() throws CSFormatException {
    JSONContent pxonOutput = super.toConfigPXONContent();
    return pxonOutput;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    return super.toJSONBuffer();
  }
}
