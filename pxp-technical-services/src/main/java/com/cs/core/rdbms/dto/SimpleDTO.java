package com.cs.core.rdbms.dto;

import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

/**
 * Root implementation of ISimpleDTO
 *
 * @author vallee
 */
public abstract class SimpleDTO implements ISimpleDTO {

  @Override
  public void fromJSON(String json) throws CSFormatException {
    json = json.trim();
    json = json.isEmpty() ? "{}" : json;
    JSONContentParser parser = new JSONContentParser(json);
    fromJSON(parser);
  }

  public abstract void fromJSON(JSONContentParser json) throws CSFormatException;
}
