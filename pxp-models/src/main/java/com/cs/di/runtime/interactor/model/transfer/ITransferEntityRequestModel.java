package com.cs.di.runtime.interactor.model.transfer;

import java.util.List;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ITransferEntityRequestModel extends IModel{
  
  
  public static final String IDS                         = "ids";
  public static final String DESTINATION_CATALOG_ID      = "destinationCatalogId";
  public static final String SOURCE_CATALOG_ID           = "sourceCatalogId";
  public static final String DESTINATION_ORGANIZATION_ID = "destinationOrganizationId";
  public static final String MODULE_ID                   = "moduleId";
  public static final String IS_TRANSFERBETWEEN_STAGES   = "isTransferBetweenStages";
  public static final String AUTHORIZATION_MAPPING_ID    = "authorizationMappingId";
  public static final String DESTINATION_ENDPOINT_ID     = "destinationEndpointId";
  public static final String IS_REVISIONABLE_TRANSFER    = "isRevisionableTransfer";

  
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public String getDestinationOrganizationId();
  
  public void setDestinationOrganizationId(String destinationOrganizationId);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
  
  public String getIsTransferBetweenStages();
  
  public void setIsTransferBetweenStages(String isTransferBetweenStages);
  
  public String getAuthorizationMappingId();
  
  public void setAuthorizationMappingId(String authorizationMappingId);
  
  public String getDestinationCatalogId();
  
  public void setDestinationCatalogId(String destinationCatalogId);
  
  public String getSourceCatalogId();
  
  public void setSourceCatalogId(String sourceCatalogId);
  
  public String getDestinationEndpointId();
  
  public void setDestinationEndpointId(String destinationEndpointId);

  public Boolean getIsRevisionableTransfer();  
  public void setIsRevisionableTransfer(Boolean isRevisionableTransfer);
}
