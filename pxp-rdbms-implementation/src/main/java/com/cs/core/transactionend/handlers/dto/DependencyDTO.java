package com.cs.core.transactionend.handlers.dto;

import com.cs.core.bgprocess.dto.InitializeBGProcessDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContent;
import com.cs.core.json.JSONContentParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

import java.util.HashMap;
import java.util.Map;

public class DependencyDTO extends InitializeBGProcessDTO implements IDependencyDTO {

  public static final String DEPENDENCIES = "dependencies";

  private Map<Long, IDependencyChangeDTO> dependencies = new HashMap<>();

  @Override
  public Map<Long, IDependencyChangeDTO> getDependencies()
  {
    return dependencies;
  }

  @Override
  public void setDependencies(Map<Long, IDependencyChangeDTO> dependencies)
  {
    this.dependencies = dependencies;
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    super.fromJSON(json);
    dependencies.clear();
    Map<String, Object> dependency = (Map<String, Object>) json.toJSONObject().get(DEPENDENCIES);
    if (dependency != null) {
      for (Map.Entry<String, Object> dependent : dependency.entrySet()) {
        IDependencyChangeDTO dependencyChangeDTO = new DependencyChangeDTO();
        dependencyChangeDTO.fromJSON(dependent.getValue().toString());
        dependencies.put(Long.valueOf(dependent.getKey()), dependencyChangeDTO);
      }
    }

  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    IJSONContent jsonContent = new JSONContent();
    for(Map.Entry<Long, IDependencyChangeDTO> dependent : dependencies.entrySet()){
      jsonContent.setField(String.valueOf(dependent.getKey()), dependent.getValue().toJSON());
    }
    return JSONBuilder.assembleJSONBuffer(super.toJSONBuffer(),
        JSONBuilder.newJSONField(DEPENDENCIES, jsonContent));
  }
}
