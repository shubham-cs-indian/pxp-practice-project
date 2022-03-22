package com.cs.core.runtime.interactor.model.configuration;

import com.cs.core.runtime.interactor.entity.contentidentifier.ContentIdentifier;
import com.cs.core.runtime.interactor.entity.contentidentifier.IContentIdentifier;

public class ContentIdentifierModel implements IContentIdentifierModel {
  
  private static final long    serialVersionUID = 1L;
  protected IContentIdentifier entity;
  
  public ContentIdentifierModel(String organizationId, String physicalCatalogId, String portalId,
      String logicalCatalogId, String systemId, String endpointId)
  {
    entity = new ContentIdentifier();
    entity.setOrganizationId(organizationId);
    entity.setPhysicalCatalogId(physicalCatalogId);
    entity.setPortalId(portalId);
    entity.setLogicalCatalogId(logicalCatalogId);
    entity.setSystemId(systemId);
    entity.setEndpointId(endpointId);
  }
  
  public ContentIdentifierModel()
  {
    entity = new ContentIdentifier();
  }
  
  public String getOrganizationId()
  {
    return entity.getOrganizationId();
  }
  
  public void setOrganizationId(String organizationId)
  {
    entity.setOrganizationId(organizationId);
  }
  
  public String getSystemId()
  {
    return entity.getSystemId();
  }
  
  public void setSystemId(String systemId)
  {
    entity.setSystemId(systemId);
  }
  
  public String getPhysicalCatalogId()
  {
    return entity.getPhysicalCatalogId();
  }
  
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    entity.setPhysicalCatalogId(physicalCatalogId);
  }
  
  public String getPortalId()
  {
    return entity.getPortalId();
  }
  
  public void setPortalId(String portalId)
  {
    entity.setPortalId(portalId);
  }
  
  public String getLogicalCatalogId()
  {
    return entity.getLogicalCatalogId();
  }
  
  public void setLogicalCatalogId(String logicalCatalogId)
  {
    entity.setLogicalCatalogId(logicalCatalogId);
  }
  
  public String getEndpointId()
  {
    return entity.getEndpointId();
  }
  
  public void setEndpointId(String endpointId)
  {
    entity.setEndpointId(endpointId);
  }
  
  @Override
  public String getId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setId(String id)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
}
