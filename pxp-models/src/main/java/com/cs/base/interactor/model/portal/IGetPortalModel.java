package com.cs.base.interactor.model.portal;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetPortalModel extends IModel {
  
  public static final String PORTAL_MODELS = "portalModels";
  
  public List<IPortalModel> getPortals();
  
  public void setPortals(List<IPortalModel> portalModels);
}
