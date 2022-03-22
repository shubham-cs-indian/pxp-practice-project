package com.cs.runtime.strategy.plugin.usecase.pricing;

import com.cs.core.config.interactor.exception.context.ContextNotFoundException;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.runtime.strategy.plugin.usecase.base.variant.AbstractGetConfigDetailsForGetPropertiesVariantInstancesInTableView;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForGetPricingVariantTableViewInstances
    extends AbstractGetConfigDetailsForGetPropertiesVariantInstancesInTableView {
  
  public GetConfigDetailsForGetPricingVariantTableViewInstances(
      final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetPricingVariantTableViewInstances/*" };
  }
  
  @Override
  protected void fillFilterInfo(Map<String, Object> mapToReturn, String contextId)
      throws ContextNotFoundException
  {
    // no need to fill filter Info.
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = (Map<String, Object>) super.execute(requestMap);
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    mergeCouplingTypeFromOfReferencedElementsFromRelationship(responseMap, klassIds);
    
    return responseMap;
  }
}
