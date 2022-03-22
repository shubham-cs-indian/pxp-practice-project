package com.cs.core.config.strategy.usecase.rulelist;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetKlassModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrientDBGetReferencedDataRulesStrategy extends OrientDBBaseStrategy
    implements IGetReferencedDataRulesStrategy {
  
  public static final String      useCase = "GetReferencedDataRules";
  @Autowired
  protected TransactionThreadData controllerThread;
  
  @Override
  public IListModel<IGetKlassModel> execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("listId", model.getId());
    
    TransactionData transactionData = controllerThread.getTransactionData();
    requestMap.put(IMulticlassificationRequestModel.ENDPOINT_ID, transactionData.getEndpointId());
    requestMap.put(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID,
        transactionData.getPhysicalCatalogId());
    requestMap.put(IMulticlassificationRequestModel.ORAGANIZATION_ID,
        transactionData.getOrganizationId());
    
    return execute(useCase, requestMap, new TypeReference<ListModel<GetKlassModel>>()
    {
      
    });
  }
}
