package com.cs.core.runtime.interactor.model.version;

import com.cs.core.runtime.interactor.model.templating.AbstractGetKlassInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetKlassInstanceVersionForTimeLineModel extends AbstractGetKlassInstanceModel
    implements IGetKlassInstanceVersionsForTimeLineModel {
  
  private static final long                  serialVersionUID = 1L;
  protected List<IKlassInstanceVersionModel> versions;
  
  @Override
  public List<IKlassInstanceVersionModel> getVersions()
  {
    if (versions == null)
      versions = new ArrayList<IKlassInstanceVersionModel>();
    return versions;
  }
  
  @Override
  @JsonDeserialize(contentAs = KlassInstanceVersionModel.class)
  public void setVersions(List<IKlassInstanceVersionModel> versions)
  {
    this.versions = versions;
  }
}
