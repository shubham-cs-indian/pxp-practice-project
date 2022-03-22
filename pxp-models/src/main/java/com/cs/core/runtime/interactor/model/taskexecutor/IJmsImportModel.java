package com.cs.core.runtime.interactor.model.taskexecutor;

import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
import com.cs.core.config.interactor.model.transaction.ITransactionDataModel;
import com.cs.core.runtime.interactor.model.component.klassinstance.IDiKlassInstanceImportMessageModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IJmsImportModel extends IModel {
  
  public static final String COMPONENT_MODEL  = "componentModel";
  public static final String TRANSACTION_DATA = "transactionData";
  public static final String DATA             = "data";
  
  public IComponentModel getComponentModel();
  
  public void setComponentModel(IComponentModel componentModel);
  
  public ITransactionDataModel getTransactionDataModel();
  
  public void setTransactionDataModel(ITransactionDataModel transactionData);
  
  public IDiKlassInstanceImportMessageModel getData();
  
  public void setData(IDiKlassInstanceImportMessageModel data);
}
