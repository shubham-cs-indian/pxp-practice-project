package com.cs.core.runtime.interactor.entity.klassinstance;

import com.cs.core.runtime.interactor.entity.datarule.AbstractConflictingValueSource;

public class KlassConflictingValueSource extends AbstractConflictingValueSource
    implements IKlassConflictingValueSource {
  
  private static final long serialVersionUID = 1L;
  
  protected String          klassVersionId;
  
  @Override
  public String getKlassVersionId()
  {
    return klassVersionId;
  }
  
  @Override
  public void setKlassVersionId(String klassVersionId)
  {
    this.klassVersionId = klassVersionId;
  }
}
