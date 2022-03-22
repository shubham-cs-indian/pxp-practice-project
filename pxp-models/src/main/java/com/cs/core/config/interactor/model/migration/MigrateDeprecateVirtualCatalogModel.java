package com.cs.core.config.interactor.model.migration;

import java.util.ArrayList;
import java.util.List;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;

public class MigrateDeprecateVirtualCatalogModel extends ConfigResponseWithAuditLogModel implements IMigrateDeprecateVirtualCatalogModel {
  
  private static final long serialVersionUID = 1L;
  
  List<Long>                klassIIDs        = new ArrayList<Long>();
  List<String>              klassIds         = new ArrayList<String>();
  
  @Override
  public List<Long> getKlassIIDs()
  {
    return klassIIDs;
  }
  
  @Override
  public void setKlassIIDs(List<Long> klassIIDs)
  {
    this.klassIIDs = klassIIDs;
  }
  
  @Override
  public List<String> getKlassIds()
  {
    return klassIds;
  }
  
  @Override
  public void setKlassIds(List<String> klassIds)
  {
    this.klassIds = klassIds;
  }
}
