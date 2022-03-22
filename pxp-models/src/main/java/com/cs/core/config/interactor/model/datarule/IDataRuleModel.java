package com.cs.core.config.interactor.model.datarule;

import java.util.List;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.runtime.interactor.model.configuration.IAdditionalPropertiesModel;

public interface IDataRuleModel extends IConfigModel, IDataRule, IAdditionalPropertiesModel, IConfigResponseWithAuditLogModel {
  
  public static final String KLASS_IDS                    = "klassIds";
  public static final String USER_ID                      = "userId";
  public static final String CONFIG_DETAILS               = "configDetails";
  public static final String IS_PHYSICAL_CATALOGS_CHANGED = "isPhysicalCatalogsChanged";
  public static final String PHYSICAL_CATALOG_LIST        = "physicalCatalogList";
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
  
  public String getUserId();
  
  public void setUserId(String userId);
  
  public IConfigDetailsForDataRuleModel getConfigDetails();
  
  public void setConfigDetails(IConfigDetailsForDataRuleModel configDetails);
  
  public Boolean getIsPhysicalCatalogsChanged();
  
  public void setIsPhysicalCatalogsChanged(Boolean isPhysicalCatalogsChanged);
  
  public List<String> getPhysicalCatalogList();
  
  public void setPhysicalCatalogList(List<String> physicalCatalogList);
}
