package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.datarule.IConflictingValueSource;

public interface IKlassConflictingValueSource extends IConflictingValueSource {
  
  public static final String KLASS_VERSION_ID = "klassVersionId";
  
  public String getKlassVersionId();
  
  public void setKlassVersionId(String klassVersionId);
}
