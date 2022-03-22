package com.cs.base.interactor.model.portal;

import java.util.List;

public class GetProtalModel implements IGetPortalModel {
  
  protected List<IPortalModel> portals;
  
  @Override
  public List<IPortalModel> getPortals()
  {
    return portals;
  }
  
  @Override
  public void setPortals(List<IPortalModel> portals)
  {
    this.portals = portals;
  }
}
