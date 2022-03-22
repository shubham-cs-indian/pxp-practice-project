package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.BulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IBulkSaveVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGridEditVariantContextInformationModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BulkSaveVariantContextsStrategy extends OrientDBBaseStrategy
    implements IBulkSaveVariantContextsStrategy {
  
  public static final String useCase = "BulkSaveVariantContexts";
  
  @Override
  public IBulkSaveVariantContextsResponseModel execute(
      IListModel<IGridEditVariantContextInformationModel> model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("list", model);
    return execute(useCase, requestMap, BulkSaveVariantContextsResponseModel.class);
  }
}
