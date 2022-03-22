package com.cs.core.config.strategy.usecase.klass;

import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassBasic;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetKlassForModuleStrategy extends OrientDBBaseStrategy
    implements IGetKlassForModuleStrategy {
  
  public static final String      useCase = "GetKlassForModule";
  @Autowired
  protected TransactionThreadData controllerThread;
  
  @Override
  public IListModel<IKlassInformationModel> execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    
    TransactionData transactionData = controllerThread.getTransactionData();
    requestMap.put(IMulticlassificationRequestModel.ENDPOINT_ID, transactionData.getEndpointId());
    requestMap.put(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID,
        transactionData.getPhysicalCatalogId());
    requestMap.put(IMulticlassificationRequestModel.ORAGANIZATION_ID,
        transactionData.getOrganizationId());
    
    ListModel<IKlass> klassesList = execute(useCase, requestMap,
        new TypeReference<ListModel<IKlass>>()
        {
          
        });
    
    List<IKlassInformationModel> klassModelsList = new ArrayList<>();
    for (IKlass klass : klassesList.getList()) {
      klassModelsList.add(new KlassInformationModel((IKlassBasic) klass));
    }
    IListModel<IKlassInformationModel> returnModel = new ListModel<>();
    returnModel.setList(klassModelsList);
    return returnModel;
  }
}
