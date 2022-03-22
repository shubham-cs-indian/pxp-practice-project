package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.ConfigDetailsUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.model.klass.IConfigDetailsForRelationshipPaginationModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestForRelationshipsModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetConfigDetailsForRelationshipPagination extends AbstractOrientPlugin {
  
  public GetConfigDetailsForRelationshipPagination(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForRelationshipPagination/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("userId");
    Map<String, Object> mapToReturn = new HashMap<String, Object>();
    Map<String, Object> xRayConfigDetails = MultiClassificationUtils.getXRayConfigDetails(
        (List<String>) requestMap
            .get(IMulticlassificationRequestForRelationshipsModel.X_RAY_ATTRIBUTES),
        (List<String>) requestMap.get(IMulticlassificationRequestForRelationshipsModel.X_RAY_TAGS));
    
    Map<String, Object> referencedElements = new HashMap<>();
    String sideId = (String) requestMap
        .get(IMulticlassificationRequestForRelationshipsModel.SIDE_ID);
    mapToReturn.put(IConfigDetailsForRelationshipPaginationModel.X_RAY_CONFIG_DETAILS,
        xRayConfigDetails);
    ConfigDetailsUtils.fillSide2LinkedVariantKrIds(mapToReturn);
    Vertex klassRelationshipNode = UtilClass.getVertexById(sideId,
        VertexLabelConstants.KLASS_RELATIONSHIP);
    
    Map<String, Object> referencedElement = UtilClass.getMapFromVertex(new ArrayList<>(),
        klassRelationshipNode);
    referencedElement.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
    referencedElements.put(sideId, referencedElement);
    mapToReturn.put(IConfigDetailsForRelationshipPaginationModel.RFERENCED_ELEMENTS,
        referencedElements);
    
    GlobalPermissionUtils.fillEntitiesAndKlassIdsAndTaxonomyIdsHavingReadPermission(userId,
        mapToReturn);
    return mapToReturn;
  }
}
