package com.cs.core.runtime.interactor.model.klassinstance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IKlassInstanceVersionInformationModel extends IModel {
  
  public static final String ID            = "id";
  public static final String MINOR_VERSION = "minorVersion";
  
  public String getId();
  
  public void setId(String id);
  
  public IListModel<IKlassInstanceInformationModel> getMinorVersions();
  
  public void setMinorVersions(IListModel<IKlassInstanceInformationModel> minorVersion);
}
