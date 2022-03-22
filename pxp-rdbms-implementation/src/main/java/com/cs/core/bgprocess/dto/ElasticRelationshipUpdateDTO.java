package com.cs.core.bgprocess.dto;

import com.cs.core.bgprocess.idto.IElasticRelationshipUpdateDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;

import java.util.HashSet;
import java.util.Set;

public class ElasticRelationshipUpdateDTO extends ElasticBulkUpdateDTO implements IElasticRelationshipUpdateDTO {


  private static final String ENTITIES_TO_UPDATE = "entitiesToUpdate";

  public Set<Long> entitiesToUpdate = new HashSet<>();

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(), JSONBuilder.newJSONLongArray(ENTITIES_TO_UPDATE, entitiesToUpdate));
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    entitiesToUpdate.addAll(json.getJSONArray(ENTITIES_TO_UPDATE));
  }

  @Override
  public Set<Long> getEntitiesToUpdate()
  {
    return entitiesToUpdate;
  }

  @Override
  public void setEntitiesToUpdate(Set<Long> entitiesToUpdate)
  {
    this.entitiesToUpdate = entitiesToUpdate;
  }
}
