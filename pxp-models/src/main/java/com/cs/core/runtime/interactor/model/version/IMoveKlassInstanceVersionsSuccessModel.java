package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IMoveKlassInstanceVersionsSuccessModel extends IModel {
  
  public static final String VERSION_NUMBERS = "versionNumbers";
  
  public List<Integer> getVersionNumbers();
  
  public void setVersionNumbers(List<Integer> versionNumbers);
}
