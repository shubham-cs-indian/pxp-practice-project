package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetReferencedRelationshipProperties extends AbstractGetConfigDetails {
  
  public GetReferencedRelationshipProperties(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetReferencedRelationshipProperties/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    return getConfigDetails(requestMap, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
  }
  
  protected Map<String, Object> getConfigDetails(Map<String, Object> requestMap, String nodeLabel)
      throws Exception
  {
    List<String> klassIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.KLASS_IDS);
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedRelationshipProperties = new HashMap<>();
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES,
        referencedRelationshipProperties);
    fillRelationshipsProperties(klassIds, mapToReturn);
    fillNatureRelationshipsProperties(klassIds, mapToReturn);
    return mapToReturn;
  }
}
