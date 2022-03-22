package com.cs.core.config.strategy.usecase.variant;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantWithNumberModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.variants.VariantWithNumberModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("getAllVariantsStrategy")
public class OrientDBGetAllVariantsStrategy extends OrientDBBaseStrategy
    implements IGetAllVariantsStrategy {
  
  public static final String useCase = "GetAllVariantsByArticle";
  
  @Override
  public IListModel<IVariantWithNumberModel> execute(IIdParameterModel model) throws Exception
  {
    
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("id", model.getId());
    return execute(useCase, requestMap, new TypeReference<ListModel<VariantWithNumberModel>>()
    {
      
    });
  }
}
