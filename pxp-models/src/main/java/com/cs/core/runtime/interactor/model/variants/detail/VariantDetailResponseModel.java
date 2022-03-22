package com.cs.core.runtime.interactor.model.variants.detail;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VariantDetailResponseModel implements IVariantDetailResponseModel {
  
  private static final long           serialVersionUID = 1L;
  protected List<Map<String, Object>> klassinstancecache;
  
  @Override
  public List<Map<String, Object>> getKlassinstancecache()
  {
    return klassinstancecache;
  }
  
  @Override
  public void setKlassinstancecache(List<Map<String, Object>> klassinstancecache)
  {
    if (klassinstancecache == null) {
      klassinstancecache = new ArrayList<>();
    }
    this.klassinstancecache = klassinstancecache;
  }
}
