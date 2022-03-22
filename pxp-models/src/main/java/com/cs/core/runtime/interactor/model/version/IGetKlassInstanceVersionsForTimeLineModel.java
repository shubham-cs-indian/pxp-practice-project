package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;

import java.util.List;

public interface IGetKlassInstanceVersionsForTimeLineModel extends IGetKlassInstanceModel {
  
  public static final String VERSIONS = "versions";
  
  public List<IKlassInstanceVersionModel> getVersions();
  
  public void setVersions(List<IKlassInstanceVersionModel> versions);
}
