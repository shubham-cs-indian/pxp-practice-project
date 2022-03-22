package com.cs.config.strategy.plugin.usecase.base.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.MultiClassificationUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestForRelationshipsModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractGetMultiClassificationKlassDetails extends AbstractOrientPlugin {
  
  public AbstractGetMultiClassificationKlassDetails(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected abstract String getKlassNodeLabel();
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("userId");
    Boolean isUnlinkedRelationships = (Boolean) requestMap.get("isUnlinkedRelationships");
    Vertex userVertex = UtilClass.getVertexById(userId, VertexLabelConstants.ENTITY_TYPE_USER);
    Set<String> allowedEntities = RoleUtils.getAllowedEntities(userVertex);
    if (allowedEntities.isEmpty()) {
      allowedEntities.addAll(CommonConstants.MODULE_ENTITIES);
    }
    List<String> ids = (List<String>) requestMap.get(IMulticlassificationRequestModel.KLASS_IDS);
    List<String> taxonomyIds = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    List<String> taxonomyIdsToGetDetailsFor = (List<String>) requestMap
        .get(IMulticlassificationRequestModel.TAXONOMY_IDS_FOR_DETAILS);
    
    Map<String, Object> mapToReturn = MultiClassificationUtils
        .getMultiCLassificationKlassDetailsMap(ids, getKlassNodeLabel(), VertexLabelConstants.ROOT_KLASS_TAXONOMY,
            allowedEntities, taxonomyIds, userId, isUnlinkedRelationships, taxonomyIdsToGetDetailsFor);
    
    Map<String, Object> xRayConfigDetails = MultiClassificationUtils.getXRayConfigDetails(
        (List<String>) requestMap
            .get(IMulticlassificationRequestForRelationshipsModel.X_RAY_ATTRIBUTES),
        (List<String>) requestMap.get(IMulticlassificationRequestForRelationshipsModel.X_RAY_TAGS));
    
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.X_RAY_CONFIG_DETAILS,
        xRayConfigDetails);
    
    return mapToReturn;
  }
}
