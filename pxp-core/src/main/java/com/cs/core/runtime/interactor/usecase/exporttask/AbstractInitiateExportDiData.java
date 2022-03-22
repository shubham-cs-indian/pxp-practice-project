package com.cs.core.runtime.interactor.usecase.exporttask;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dataintegration.IAcknowledgementModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.transfer.IDiExportDataModel;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractInitiateExportDiData
    implements IDiDataInteractor<IDiExportDataModel, IModel> {
  
  @Autowired
  protected TransactionThreadData controllerThread;
  
  public void setTransactionData(TransactionData transactionData)
  {
    controllerThread.setTransactionData(transactionData);
  }
  
  protected HashMap<String, Object> prepareDataMap(IModel klassInstanceModel)
  {
    HashMap<String, Object> DataMap = new HashMap<>();
    DataMap.put(IAcknowledgementModel.HEADERS, null);
    DataMap.put(IAcknowledgementModel.PROPERTIES, null);
    DataMap.put(IAcknowledgementModel.BODY, klassInstanceModel);
    
    return DataMap;
  }
}
