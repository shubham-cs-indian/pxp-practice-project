package com.cs.core.runtime.interactor.entity.klassinstance;

import java.util.List;

public interface IKlassInstanceSet extends IContentInstance {
  
  public static final String KLASS_INSTANCE_IDS = "klassInstanceIds";
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
}
