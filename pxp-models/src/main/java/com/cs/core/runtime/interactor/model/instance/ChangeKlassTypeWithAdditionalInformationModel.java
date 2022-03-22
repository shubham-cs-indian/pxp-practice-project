package com.cs.core.runtime.interactor.model.instance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeKlassTypeWithAdditionalInformationModel
    implements IChangeKlassTypeWithAdditionalInformationModel {
  
  protected List<String>        ids;
  protected Map<String, String> defaultKlass;
  protected Map<String, Object> additionalProperties = new HashMap<>();
  
  @Override
  public Map<String, Object> getAdditionalProperty()
  {
    return this.additionalProperties;
  }
  
  @Override
  public void setAdditionalProperty(Map<String, Object> additionalProperties)
  {
    this.additionalProperties = additionalProperties;
  }
  
  @Override
  public List<String> getIds()
  {
    if (ids == null) {
      ids = new ArrayList<>();
    }
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public Map<String, String> getDefaultKlass()
  {
    if (defaultKlass == null) {
      defaultKlass = new HashMap<>();
    }
    return defaultKlass;
  }
  
  @Override
  public void setDefaultKlass(Map<String, String> defaultKlass)
  {
    this.defaultKlass = defaultKlass;
  }
}
