package com.cs.core.rdbms.dto;

import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.rdbms.idto.IRootDTO;

/**
 * Root implementation of IRootDTO
 *
 * @author vallee
 */
public abstract class RootDTO implements IRootDTO {

  // Standard field name for the CS Express identity part
  protected static final String CSE_ID = PXONTag.csid.toTag();
  protected boolean exportIID = true; // TODO: move default to false in future
  private boolean isChanged = false;

  /**
   * @param dtos multiple DTO to be included in a CSExpression list
   * @return a list of CSExpress elements
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public static CSEList toCSExpress(IRootDTO... dtos) throws CSFormatException {
    CSEList list = new CSEList();
    for (int i = 0; i < dtos.length; i++) {
      ICSEElement item = dtos[i].toCSExpressID();
      if (!item.isEmpty()) {
        list.getSubElements().add(item);
      }
    }
    return list;
  }

  @Override
  public boolean isChanged() {
    return isChanged;
  }

  @Override
  public void setChanged(boolean status) {
    isChanged = status;
  }

  @Override
  public void setExportOfIID( boolean status) {
    exportIID = true;
  }

  @Override
  public void fromPXON(String json) throws CSFormatException {
    json = json.trim();
    json = json.isEmpty() ? "{}" : json;
    JSONContentParser parser = new JSONContentParser(json);
    fromPXON(parser);
  }

  /**
   * Overwrite DTO properties from an incoming JSON content
   *
   * @param json the DTO pxon content
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public void fromPXON(JSONContentParser json) throws CSFormatException {
    if (!json.isEmpty()) {
      this.fromCSExpressID(json.getCSEElement(CSE_ID));
    } else {
      this.fromCSExpressID(new CSEObject(CSEObjectType.Unknown)); // empty
    }
  }

  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException {
    ICSEElement id = toCSExpressID();
    return JSONBuilder.newJSONField(CSE_ID, id);
  }
}
