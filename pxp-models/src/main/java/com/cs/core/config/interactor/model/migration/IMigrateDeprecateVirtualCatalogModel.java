package com.cs.core.config.interactor.model.migration;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;

public interface IMigrateDeprecateVirtualCatalogModel extends IConfigResponseWithAuditLogModel {
  
  public static String KLASS_IIDS = "klassIIDs";
  public static String KLASS_IDS  = "klassIds";
  
  public List<Long> getKlassIIDs();
  
  public void setKlassIIDs(List<Long> klassIIDs);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
}
