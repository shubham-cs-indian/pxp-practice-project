package com.cs.core.runtime.interactor.model.transfer;


import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IDiExportDataModel extends IModel {
  
  public static final String PHYSICAL_CATALOG_ID = "physicalCatalogId";
  public static final String KLASS_INSTANCE_ID   = "klassInstanceId";
  public static final String DATA_INSTANCE       = "dataInstance";
  public static final String USE_CASE            = "useCase";
  
  public String getPhysicalCatalogId();
  
  public void setPhysicalCatalogId(String physicalCatalogId);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public IModel getDataInstance();
  
  public void setDataInstance(IModel dataInstance);
  
  /*public  WorkflowUtils.UseCases getUseCase();
  
  public void setUseCase(String WorkflowUtils.UseCases useCase);*/
}
