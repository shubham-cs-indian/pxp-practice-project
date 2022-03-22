package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IIIDsListModel extends IAdditionalPropertiesModel {
  
  public static String IIDS = "iids";

  public List<Long> getIids();
  public void setIids(List<Long> iids);
}
