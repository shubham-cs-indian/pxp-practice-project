package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdAndNameModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdAndNameModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GetConflictSourcesInfoRuntimeModel implements IGetConflictSourcesInfoRuntimeModel {
  
  private static final long              serialVersionUID = 1L;
  protected Map<String, IIdAndNameModel> instances;
  
  @Override
  public Map<String, IIdAndNameModel> getContents()
  {
    if (instances == null) {
      instances = new HashMap<>();
    }
    return instances;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdAndNameModel.class)
  public void setContents(Map<String, IIdAndNameModel> instances)
  {
    this.instances = instances;
  }
}
