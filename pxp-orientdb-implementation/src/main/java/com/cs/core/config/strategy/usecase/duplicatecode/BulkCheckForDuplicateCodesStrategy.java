package com.cs.core.config.strategy.usecase.duplicatecode;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component
public class BulkCheckForDuplicateCodesStrategy extends OrientDBBaseStrategy
    implements IBulkCheckForDuplicateCodesStrategy {
  
  @Override
  public IBulkCheckForDuplicateCodesReturnModel execute(
      IListModel<IBulkCheckForDuplicateCodesModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put(IListModel.LIST, model);
    return execute(BULK_CHECK_FOR_DUPLICATE_CODES, requestMap, BulkCheckForDuplicateCodesReturnModel.class);
  }
}
