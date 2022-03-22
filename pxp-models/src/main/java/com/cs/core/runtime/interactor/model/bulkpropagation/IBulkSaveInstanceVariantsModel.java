package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.variants.IGetVariantInstanceInTableViewRequestModel;

import java.util.List;

public interface IBulkSaveInstanceVariantsModel extends IModel {
  
  public static final String INSTANCES_TO_SAVE  = "instancesToSave";
  public static final String TABLE_VIEW_REQUEST = "tableViewRequest";
  
  public List<IKlassInstanceSaveModel> getInstancesToSave();
  
  public void setInstancesToSave(List<IKlassInstanceSaveModel> instancesToSave);
  
  public IGetVariantInstanceInTableViewRequestModel getTableViewRequest();
  
  public void setTableViewRequest(IGetVariantInstanceInTableViewRequestModel tableViewRequest);
}
