package com.cs.core.transactionend.handlers.dto;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

@SuppressWarnings("serial")
public class PostProcessingOnCouplingUpdateDTO extends InitializeBGProcessDTO implements IPostProcessingOnCouplingUpdateDTO {

  public static final String CHANGES = "changes";
  DependencyChangeDTO changes = new DependencyChangeDTO();
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    //changes = (IDependencyChangeDTO) json.getJSONContent(CHANGES);
    changes.fromJSON(json.getJSONParser(CHANGES));
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONField(CHANGES, changes.toJSONBuffer()));
  }
  
  @Override
  public IDependencyChangeDTO getChanges()
  {
    return changes;
  }

  @Override
  public void setChanges(IDependencyChangeDTO changes)
  {
    this.changes = (DependencyChangeDTO)changes;
  }
  
}
