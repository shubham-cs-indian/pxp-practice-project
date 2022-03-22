package com.cs.config.strategy.plugin.usecase.dataintegration;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IEntityLabelCodeMapModel;
import com.cs.core.runtime.interactor.model.dataintegration.ITaxonomyCodeLevelIdMapModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public class GetLevelIdsByTaxonomyCodes extends AbstractOrientPlugin {
  
  public GetLevelIdsByTaxonomyCodes(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetLevelIdsByTaxonomyCodes/*" };
  }
  
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> taxonomyMap = (Map<String, Object>) requestMap
        .get(IEntityLabelCodeMapModel.ENTITYlABELCODEMAP);
    Set<String> keySet = taxonomyMap.keySet();
    String taxonomyType = null;
    for (String key : keySet) {
      taxonomyType = key;
    }
    List<String> taxonomyCodes = (List<String>) taxonomyMap.get(taxonomyType);
    Map<String, Object> taxonomyCodeLevelIdMap = new HashMap<>();
    
    Vertex taxonomyVertex = null;
    
    switch (taxonomyType) {
      //TODO: PXPFDEV-21215 : Deprecate - Taxonomy Hierarchies
      /*case ProcessConstants.HIERARCHY_TAXONOMY:
      -      {
      -        for (String code : taxonomyCodes) {
      -          try {
      -            taxonomyVertex = UtilClass.getVertexByCode(code,
      -                VertexLabelConstants.HIERARCHY_TAXONOMY);
      -          }
      -          catch (NotFoundException e) {
      -            continue;
      -          }
      -          Iterable<Vertex> vertices = taxonomyVertex.getVertices(Direction.OUT,
      -              RelationshipLabelConstants.HAS_MASTER_TAG);
      -          Vertex attributionTaxonomyVertex = vertices.iterator()
      -              .next();
      -          Vertex tagVertex = attributionTaxonomyVertex
      -              .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
      -              .iterator()
      -              .next();
      -          taxonomyCodeLevelIdMap.put(code,
      -              tagVertex.getProperty(CommonConstants.CODE_PROPERTY));
      -        }
      -        
      -        break;
      -      }*/
      
      case ProcessConstants.MASTER_TAXONOMY:
      {
        for (String code : taxonomyCodes) {
          try {
            Vertex tagVertex = UtilClass
                .getVertexByCode(code, VertexLabelConstants.ATTRIBUTION_TAXONOMY)
                .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF)
                .iterator()
                .next();
            taxonomyCodeLevelIdMap.put(code,
                tagVertex.getProperty(CommonConstants.CODE_PROPERTY));
          }
          catch (NotFoundException e) {
            continue;
          }
        }
      }
      default:
        break;
    }
    
    Map<String, Object> mapToReturn = new HashMap<>();
    mapToReturn.put(ITaxonomyCodeLevelIdMapModel.TAXONOMY_CODE_LEVEL_ID_MAP,
        taxonomyCodeLevelIdMap);
    return mapToReturn;
  }
}
