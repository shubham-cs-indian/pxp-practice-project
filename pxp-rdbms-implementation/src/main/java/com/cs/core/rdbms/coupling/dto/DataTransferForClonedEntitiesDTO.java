package com.cs.core.rdbms.coupling.dto;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.coupling.idto.IDataTransferForClonedEntitiesDTO;
import com.cs.core.technical.exception.CSFormatException;

public class DataTransferForClonedEntitiesDTO extends InitializeBGProcessDTO implements IDataTransferForClonedEntitiesDTO {
  
  private static final long   serialVersionUID       = 1L;
  private List<Long> clonedBaseEntityIIDs   = new ArrayList<>();

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), !clonedBaseEntityIIDs.isEmpty() ? 
        JSONBuilder.newJSONLongArray(CLONED_BASE_ENTITY_IIDS, clonedBaseEntityIIDs) : JSONBuilder.VOID_FIELD);
    
  }

  @SuppressWarnings("unchecked")
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    clonedBaseEntityIIDs.clear();
    json.getJSONArray(CLONED_BASE_ENTITY_IIDS)
        .forEach((iid) -> {
          clonedBaseEntityIIDs.add((Long) iid);
        });
  }
  
  @Override
  public void setClonedBaseEntityIIDs(List<Long> clonedBaseEntityIIDs)
  {
    this.clonedBaseEntityIIDs = clonedBaseEntityIIDs;
  }
  
  @Override
  public List<Long> getClonedBaseEntityIIDs()
  {
    return clonedBaseEntityIIDs;
  }

}