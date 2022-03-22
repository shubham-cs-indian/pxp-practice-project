package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.model.klass.GetKlassModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getKlassWithReferencedKlassesStrategy")
public class OrientDBGetKlassWithReferencedKlassesStrategy extends OrientDBBaseStrategy
    implements IGetKlassWithReferencedKlassesStrategy {
  
  public static final String      useCase = "GetKlassWithLinkedKlasses";
  @Autowired
  protected TransactionThreadData controllerThread;
  
  @Override
  public IGetKlassModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    
    TransactionData transactionData = controllerThread.getTransactionData();
    requestMap.put(IMulticlassificationRequestModel.ENDPOINT_ID, transactionData.getEndpointId());
    requestMap.put(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID,
        transactionData.getPhysicalCatalogId());
    requestMap.put(IMulticlassificationRequestModel.ORAGANIZATION_ID,
        transactionData.getOrganizationId());
    requestMap.put(IMulticlassificationRequestModel.PROCESS_INSTANCE_ID,
        model.getAdditionalProperty("processInstanceId"));
    requestMap.put("id", model.getId());
    
    return super.execute(useCase, requestMap, GetKlassModel.class);
  }
}
