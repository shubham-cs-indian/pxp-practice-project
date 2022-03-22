package com.cs.runtime.strategy.plugin.usecase.klassinstance;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IConfigDetailsForDataTransferModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetIdentifierAttributesForTypes extends AbstractConfigDetails {
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetIdentifierAttributesForTypes/*" };
  }
  
  public GetIdentifierAttributesForTypes(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    List<String> klassIds = (List<String>) request.get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) request
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    String endpointId = (String) request.get(IMulticlassificationRequestModel.ENDPOINT_ID);
    String organizationId = (String) request.get(IMulticlassificationRequestModel.ORAGANIZATION_ID);
    String physicalCatalogId = (String) request
        .get(IMulticlassificationRequestModel.PHYSICAL_CATALOG_ID);
    
    Map<String, Object> returnMap = new HashMap<>();
    returnMap.put(IConfigDetailsForDataTransferModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS,
        new HashMap<>());
    returnMap.put(IConfigDetailsForDataTransferModel.KLASS_DATA_RULES, new HashMap<>());
    returnMap.put(IConfigDetailsForDataTransferModel.REFERENCED_DATA_RULES, new HashMap<>());
    fillKlassDetails(returnMap, klassIds, endpointId, organizationId, physicalCatalogId);
    fillTaxonomiesDetails(returnMap, taxonomyIds, endpointId, organizationId, physicalCatalogId);
    
    return returnMap;
  }
}
