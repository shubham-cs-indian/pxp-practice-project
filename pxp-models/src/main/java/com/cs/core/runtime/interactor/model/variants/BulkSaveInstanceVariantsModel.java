package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkSaveInstanceVariantsModel;
import com.cs.core.runtime.interactor.model.klassinstance.AbstractKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkSaveInstanceVariantsModel implements IBulkSaveInstanceVariantsModel {
  
  private static final long                            serialVersionUID = 1L;
  
  protected List<IKlassInstanceSaveModel>              instancesToSave  = new ArrayList<>();
  protected IGetVariantInstanceInTableViewRequestModel tableViewRequest;
  
  @Override
  public List<IKlassInstanceSaveModel> getInstancesToSave()
  {
    return instancesToSave;
  }
  
  @JsonDeserialize(contentAs = AbstractKlassInstanceSaveModel.class)
  @Override
  public void setInstancesToSave(List<IKlassInstanceSaveModel> instancesToSave)
  {
    this.instancesToSave = instancesToSave;
  }
  
  @Override
  public IGetVariantInstanceInTableViewRequestModel getTableViewRequest()
  {
    return tableViewRequest;
  }
  
  @JsonDeserialize(as = GetVariantInstancesInTableViewRequestModel.class)
  @Override
  public void setTableViewRequest(IGetVariantInstanceInTableViewRequestModel tableViewRequest)
  {
    this.tableViewRequest = tableViewRequest;
  }
}
