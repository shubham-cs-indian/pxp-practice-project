package com.cs.core.config.interactor.model.klass;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetChildKlassIdsInHierarchyModel extends IModel {
  
  public static final String TYPE_ID_VS_CHILD_KLASS_IDS = "typeIdVsChildKlassIds";
  public static final String KLASS_TYPES     = "klassTypes";

  public Map<String, List<Long>> getTypeIdVsChildKlassIds();
  
  public void setTypeIdVsChildKlassIds(Map<String, List<Long>> typeIdVsChildKlassIds);
  
  public Set<String> getKlassTypes();
  
  public void setKlassTypes(Set<String> klassTypes);
}
