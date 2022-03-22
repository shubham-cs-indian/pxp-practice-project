package com.cs.di.runtime.interactor.model.transfer;

import java.util.ArrayList;
import java.util.List;

public class TransferEntityRequestModel implements ITransferEntityRequestModel {

  private static final long serialVersionUID = 1L;
  
  protected List<String>                       ids                  = new ArrayList<>();
  protected String                             destinationCatalogId;
  protected String                             sourceCatalogId;
  protected String                             destinationOrganizationId;
  protected String                             moduleId;
  protected String                             isTransferBetweenStages;
  protected String                             authorizationMappingId;
  protected String                             destinationEndpointId;
  protected Boolean                            isRevisionableTransfer = false;
  
  @Override
  public List<String> getIds()
  {
    return ids;
  }
  
  @Override
  public void setIds(List<String> ids)
  {
    this.ids = ids;
  }
  
  @Override
  public String getDestinationCatalogId()
  {
    return destinationCatalogId;
  }
  
  @Override
  public void setDestinationCatalogId(String destinationCatalogId)
  {
    this.destinationCatalogId = destinationCatalogId;
  }
  
  @Override
  public String getSourceCatalogId()
  {
    return sourceCatalogId;
  }
  
  @Override
  public void setSourceCatalogId(String sourceCatalogId)
  {
    this.sourceCatalogId = sourceCatalogId;
  }

  @Override
  public String getDestinationOrganizationId()
  {
    return destinationOrganizationId;
  }

  @Override
  public void setDestinationOrganizationId(String destinationOrganizationId)
  {
    this.destinationOrganizationId = destinationOrganizationId;
  }

  @Override
  public String getModuleId()
  {
    return moduleId;
  }

  @Override
  public void setModuleId(String moduleId)
  {
    this.moduleId = moduleId;
  }

  @Override
  public String getIsTransferBetweenStages()
  {
    return isTransferBetweenStages;
  }

  @Override
  public void setIsTransferBetweenStages(String isTransferBetweenStages)
  {
    this.isTransferBetweenStages = isTransferBetweenStages;
  }

  @Override
  public String getAuthorizationMappingId()
  {
    return authorizationMappingId;
  }

  @Override
  public void setAuthorizationMappingId(String authorizationMappingId)
  {
    this.authorizationMappingId = authorizationMappingId;
  }

  @Override
  public String getDestinationEndpointId()
  {
    return destinationEndpointId;
  }

  @Override
  public void setDestinationEndpointId(String destinationEndpointId)
  {
    this.destinationEndpointId = destinationEndpointId;
  }

  @Override
  public Boolean getIsRevisionableTransfer()
  {
    if(isRevisionableTransfer == null) {
      return false;
    }
    return isRevisionableTransfer;
  }

  @Override
  public void setIsRevisionableTransfer(Boolean isRevisionableTransfer)
  {
    this.isRevisionableTransfer = isRevisionableTransfer;
  }

  
}
