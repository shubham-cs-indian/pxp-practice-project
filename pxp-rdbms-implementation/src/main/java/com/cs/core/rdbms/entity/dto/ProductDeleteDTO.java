package com.cs.core.rdbms.entity.dto;
 
import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.entity.idto.IProductDeleteDTO;
import com.cs.core.technical.exception.CSFormatException;

public class ProductDeleteDTO extends InitializeBGProcessDTO implements IProductDeleteDTO {
  
  private static final long serialVersionUID = 1L;
  public List<Long>         baseEntityIIDs   = new ArrayList<>();
  public List<Long>         sourceEntityIIDs = new ArrayList<>();
  public Long               classifierIID;
  
  @Override
  public List<Long> getBaseEntityIIDs()
  {
    return baseEntityIIDs;
  }

  @Override
  public void setBaseEntityIIDs(List<Long> baseEntityIIDs)
  {
    this.baseEntityIIDs = baseEntityIIDs;
  }

  @Override
  public List<Long> getSourceEntityIIDs()
  {
    return sourceEntityIIDs;
  }

  @Override
  public void setSourceEntityIIDs(List<Long> sourceEntityIIDs)
  {
    this.sourceEntityIIDs = sourceEntityIIDs;
  }
  
  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), 
        JSONBuilder.newJSONLongArray(BASE_ENTITY_IIDs, baseEntityIIDs),
        JSONBuilder.newJSONLongArray(SOURCE_ENTITY_IIDs, sourceEntityIIDs));
  }

  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser parser) throws CSFormatException
  {
    super.fromJSON(parser);
    parser.getJSONArray(BASE_ENTITY_IIDs).forEach((iid) -> {
      baseEntityIIDs.add((Long)iid);
    });  
    parser.getJSONArray(SOURCE_ENTITY_IIDs).forEach((sourceIId) -> {
      sourceEntityIIDs.add((Long) sourceIId);
    });
  }
  
}
