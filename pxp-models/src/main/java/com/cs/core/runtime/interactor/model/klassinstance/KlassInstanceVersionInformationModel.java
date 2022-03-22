package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KlassInstanceVersionInformationModel implements IKlassInstanceVersionInformationModel {
  
  private static final long                            serialVersionUID = 1L;
  
  protected IListModel<IKlassInstanceInformationModel> minorVersions;
  
  protected String                                     id;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public IListModel<IKlassInstanceInformationModel> getMinorVersions()
  {
    return minorVersions;
  }
  
  @Override
  public void setMinorVersions(IListModel<IKlassInstanceInformationModel> minorVersions)
  {
    this.minorVersions = minorVersions;
  }
}
