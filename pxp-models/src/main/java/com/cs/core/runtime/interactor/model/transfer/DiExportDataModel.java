package com.cs.core.runtime.interactor.model.transfer;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public class DiExportDataModel implements IDiExportDataModel {
  
  private static final long serialVersionUID = 1L;
  private String            physicalCatalogId;
  private String            klassInstanceId;
  private IModel            dataInstance;
  private String /*WorkflowUtils.UseCases*/ useCase;
  
  @Override
  public String getPhysicalCatalogId()
  {
    return physicalCatalogId;
  }
  
  @Override
  public void setPhysicalCatalogId(String physicalCatalogId)
  {
    this.physicalCatalogId = physicalCatalogId;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public IModel getDataInstance()
  {
    return dataInstance;
  }
  
  @Override
  public void setDataInstance(IModel dataInstance)
  {
    this.dataInstance = dataInstance;
  }
  
  /*@Override
  public WorkflowUtils.UseCases getUseCase()
  {
    return useCase;
  }
  
  @Override
  public void setUseCase(String WorkflowUtils.UseCases useCase)
  {
    this.useCase = useCase;
  }*/
}
