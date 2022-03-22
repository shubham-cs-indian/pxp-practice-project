package com.cs.dam.config.strategy.usecase.assetinstance;

import com.cs.core.config.interactor.model.asset.AssetModel;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.config.strategy.usecase.asset.IGetAssetStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAssetStrategy")
public class OrientDBGetAssetStrategy extends OrientDBBaseStrategy implements IGetAssetStrategy {
  
  @Autowired
  protected TransactionThreadData controllerThread;
  
  @Override
  public IAssetModel execute(IIdParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    
    TransactionData transactionData = controllerThread.getTransactionData();
    requestMap.put(IMulticlassificationRequestModel.ENDPOINT_ID, transactionData.getEndpointId());
    requestMap.put(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID,
        transactionData.getPhysicalCatalogId());
    requestMap.put(IMulticlassificationRequestModel.ORAGANIZATION_ID,
        transactionData.getOrganizationId());
    return super.execute(OrientDBBaseStrategy.GET_ASSET, requestMap, AssetModel.class);
  }
}
