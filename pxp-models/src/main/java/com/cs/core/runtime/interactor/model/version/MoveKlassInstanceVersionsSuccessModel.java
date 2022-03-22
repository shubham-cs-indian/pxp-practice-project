package com.cs.core.runtime.interactor.model.version;

import java.util.ArrayList;
import java.util.List;

public class MoveKlassInstanceVersionsSuccessModel
    implements IMoveKlassInstanceVersionsSuccessModel {
  
  private static final long serialVersionUID = 1L;
  protected List<Integer>    versionNumbers   = new ArrayList<>();
  
  @Override
  public List<Integer> getVersionNumbers()
  {
    
    return versionNumbers;
  }
  
  @Override
  public void setVersionNumbers(List<Integer> versionNumbers)
  {
    this.versionNumbers = versionNumbers;
  }
}
