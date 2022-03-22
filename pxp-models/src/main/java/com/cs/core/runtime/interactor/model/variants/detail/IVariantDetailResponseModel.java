package com.cs.core.runtime.interactor.model.variants.detail;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IVariantDetailResponseModel extends IModel {
  
  public static final String KLASS_INSTANCE_CACHE = "klassinstancecache";
  
  public List<Map<String, Object>> getKlassinstancecache();
  
  public void setKlassinstancecache(List<Map<String, Object>> klassinstancecache);
}
