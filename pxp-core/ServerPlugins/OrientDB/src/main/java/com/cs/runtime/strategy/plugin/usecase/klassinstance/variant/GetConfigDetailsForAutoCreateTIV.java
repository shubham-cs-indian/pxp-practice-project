package com.cs.runtime.strategy.plugin.usecase.klassinstance.variant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVRequestModel;
import com.cs.core.config.interactor.model.variantcontext.IGetConfigDetailsForAutoCreateTIVResponseModel;
import com.cs.runtime.strategy.plugin.usecase.base.variant.AbstractGetConfigDetailsForAutoCreateVariantInstance;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;


public class GetConfigDetailsForAutoCreateTIV
    extends AbstractGetConfigDetailsForAutoCreateVariantInstance {
  
  public GetConfigDetailsForAutoCreateTIV(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> responseMap = new HashMap<>();
    List<String> contextIds = (List<String>) requestMap.get(IGetConfigDetailsForAutoCreateTIVRequestModel.CONTEXT_IDS);
    for(String contextId : contextIds)
    {
      Map<String, Object> configDetails = getConfigDetails(requestMap, contextId);
      returnMap.put(contextId, configDetails);
    }
    responseMap.put(IGetConfigDetailsForAutoCreateTIVResponseModel.CONFIG_DETAILS_MAP, returnMap);
    return responseMap;
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForAutoCreateTIV/*" };
  }
  
}
