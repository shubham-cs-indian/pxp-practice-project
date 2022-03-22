package com.cs.core.config.interactor.model.governancerule;

import java.util.ArrayList;
import java.util.List;

public class SaveKPIDiffModel implements ISaveKPIDiffModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    physicalCatalogIds;
  protected List<String>    endpointIds;
  protected List<String>    partnerIds;
  protected Boolean         catalogChangedStatus;
  protected Boolean         isPropertyExist;
  protected Boolean         isPropertyExistInDeletedRule;
  
  @Override
  public List<String> getPhysicalCatalogIds()
  {
    if (physicalCatalogIds == null) {
      physicalCatalogIds = new ArrayList<>();
    }
    return physicalCatalogIds;
  }
  
  @Override
  public void setPhysicalCatalogIds(List<String> physicalCatalogIds)
  {
    this.physicalCatalogIds = physicalCatalogIds;
  }
  
  @Override
  public List<String> getEndpointIds()
  {
    if (endpointIds == null) {
      endpointIds = new ArrayList<>();
    }
    return endpointIds;
  }
  
  @Override
  public void setEndpointIds(List<String> endpointIds)
  {
    this.endpointIds = endpointIds;
  }
  
  @Override
  public List<String> getPartnerIds()
  {
    if (partnerIds == null) {
      partnerIds = new ArrayList<>();
    }
    return partnerIds;
  }
  
  @Override
  public void setPartnerIds(List<String> partnerIds)
  {
    this.partnerIds = partnerIds;
  }
  
  @Override
  public Boolean getCatalogChangedStatus()
  {
    
    return catalogChangedStatus;
  }
  
  @Override
  public void setCatalogChangedStatus(Boolean catalogChangedStatus)
  {
    
    this.catalogChangedStatus = catalogChangedStatus;
  }
  
  @Override
  public Boolean getIsPropertyExist()
  {
    return isPropertyExist;
  }
  
  @Override
  public void setIsPropertyExist(Boolean isPropertyExist)
  {
    this.isPropertyExist = isPropertyExist;
  }
  
  @Override
  public Boolean getIsPropertyExistInDeletedRule()
  {
    return isPropertyExistInDeletedRule;
  }
  
  @Override
  public void setIsPropertyExistInDeletedRule(Boolean isPropertyExistInDeletedRule)
  {
    this.isPropertyExistInDeletedRule = isPropertyExistInDeletedRule;
  }
}
