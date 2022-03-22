package com.cs.core.config.strategy.usecase.variantcontext;

import com.cs.core.config.interactor.model.variantcontext.GetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("saveVariantContextStrategy")
public class SaveVariantContextStrategy extends OrientDBBaseStrategy
    implements ISaveVariantContextStrategy {
  
  public static final String useCase = "SaveVariantContext";
  
  @Override
  public IGetVariantContextModel execute(ISaveVariantContextModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("variantContext", model);
    return execute(useCase, requestMap, GetVariantContextModel.class);
  }
}
