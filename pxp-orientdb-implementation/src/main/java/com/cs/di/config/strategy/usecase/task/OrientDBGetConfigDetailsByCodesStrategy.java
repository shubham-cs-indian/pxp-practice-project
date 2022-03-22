package com.cs.di.config.strategy.usecase.task;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.marketingbundle.IKlassesAndTaxonomiesModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.di.config.interactor.model.task.GetConfigDetailsByCodesResponseModel;
import com.cs.di.config.interactor.model.task.IGetConfigDetailsByCodesResponseModel;

@Component("getConfigDetailsByCodesStrategy")
public class OrientDBGetConfigDetailsByCodesStrategy extends OrientDBBaseStrategy
    implements IGetConfigDetailsByCodesStrategy {
  
  public static final String useCase = "GetConfigDetailsByCodes";
  
  @Override
  public IGetConfigDetailsByCodesResponseModel execute(IKlassesAndTaxonomiesModel model)
      throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("requestModel", model);
    return execute(useCase, requestMap, GetConfigDetailsByCodesResponseModel.class);
  }
  
}
