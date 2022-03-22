package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.variantcontext.GetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ICreateVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("createVariantContextStrategy")
public class CreateVariantContextStrategy extends OrientDBBaseStrategy
    implements ICreateVariantContextStrategy {
  
  public static final String useCase = "CreateVariantContext";
  
  @Override
  public IGetVariantContextModel execute(ICreateVariantContextModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("variantContext", model);
    return execute(useCase, requestMap, GetVariantContextModel.class);
  }
}
