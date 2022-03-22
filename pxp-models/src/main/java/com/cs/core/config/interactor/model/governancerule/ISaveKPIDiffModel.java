package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveKPIDiffModel extends IModel {
  
  public static final String PHYSICAL_CATALOG_IDS              = "physicalCatalogIds";
  public static final String ENDPOINT_IDS                      = "endpointIds";
  public static final String PARTNER_IDS                       = "partnerIds";
  public static final String CATALOG_CHANGED_STATUS            = "catalogChangedStatus";
  public static final String IS_PROPERTY_EXIST                 = "isPropertyExist";
  public static final String IS_PROPERTY_EXIST_IN_DELETED_RULE = "isPropertyExistInDeletedRule";
  
  public List<String> getPhysicalCatalogIds();
  
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds);
  
  public List<String> getEndpointIds();
  
  public void setEndpointIds(List<String> endpointIds);
  
  public List<String> getPartnerIds();
  
  public void setPartnerIds(List<String> partnerIds);
  
  public Boolean getCatalogChangedStatus();
  
  public void setCatalogChangedStatus(Boolean catalogChangedStatus);
  
  public Boolean getIsPropertyExist();
  
  public void setIsPropertyExist(Boolean isPropertyExist);
  
  public Boolean getIsPropertyExistInDeletedRule();
  
  public void setIsPropertyExistInDeletedRule(Boolean isPropertyExistInDeletedRule);
}
