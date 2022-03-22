package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.variantcontext.GetAllVariantContextsResponseModel;
import com.cs.core.config.interactor.model.variantcontext.IGetAllVariantContextsResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

@Component("getAllVariantContextStrategy")
public class GetAllVariantContextStrategy extends OrientDBBaseStrategy
    implements IGetAllVariantContextStrategy {
  
  @Override
  public IGetAllVariantContextsResponseModel execute(IConfigGetAllRequestModel model)
      throws Exception
  {
    return execute(GET_ALL_VARIANT_CONTEXT, model, GetAllVariantContextsResponseModel.class);
  }
}
