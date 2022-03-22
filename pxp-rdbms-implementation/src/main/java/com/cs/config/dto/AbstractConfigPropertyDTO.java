package com.cs.config.dto;

import com.cs.core.dataintegration.idto.IPXON;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import java.util.Set;
import org.apache.commons.collections4.BidiMap;

/**
 * @author vallee
 */
public abstract class AbstractConfigPropertyDTO extends AbstractConfigJSONDTO {

  /**
   * Property DTO
   */
  protected final PropertyDTO propertyDTO = new PropertyDTO();
  
  /**
   * Initializing the static key map
   */
  protected AbstractConfigPropertyDTO(BidiMap<ConfigTag, String> jsonkeymap, Set<ConfigTag> ignoredPXONTags)
  {
    super(jsonkeymap, ignoredPXONTags);
  }
  
  /**
   * Initialize propertyDTO using configData
   *
   * @throws CSFormatException
   */
  protected abstract void initPropertyDTO() throws CSFormatException;
  
  @Override
  public void fromJSON(String json) throws CSFormatException
  {
    super.fromJSON(json);
    initPropertyDTO();
  }
    
  @Override
  public ICSEElement toCSExpressID() throws CSFormatException {
    return propertyDTO.toCSExpressID();
  }

  @Override
  void fromCSExpression (JSONContentParser parser) throws CSFormatException {
    fromCSExpressID( parser.getCSEElement(IPXON.PXONTag.csid.toTag()));
  }

  public void setPropertyIID (long iid) {
    setLong(ConfigTag.propertyIID, iid);
    propertyDTO.setIID(iid);
  }
}
