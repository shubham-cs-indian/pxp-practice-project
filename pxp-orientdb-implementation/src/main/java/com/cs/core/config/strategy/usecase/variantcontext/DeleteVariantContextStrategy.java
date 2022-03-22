package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.variantcontext.BulkDeleteVariantContextReturnModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteVariantContextReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("deleteVariantContextStrategy")
public class DeleteVariantContextStrategy extends OrientDBBaseStrategy
    implements IDeleteVariantContextStrategy {
  
  @Override
  public IBulkDeleteVariantContextReturnModel execute(IIdsListParameterModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("ids", model.getIds());
    return execute(DELETE_VARIANT_CONTEXT, requestMap, BulkDeleteVariantContextReturnModel.class);
  }
}
