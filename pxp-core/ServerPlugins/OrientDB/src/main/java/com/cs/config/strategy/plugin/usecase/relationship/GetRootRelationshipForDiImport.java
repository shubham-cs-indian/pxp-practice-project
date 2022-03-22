package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.relationship.util.GetRelationshipUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IEntity;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.dataintegration.IRelationshipsInfoModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetRootRelationshipForDiImport extends AbstractOrientPlugin {
  
  public GetRootRelationshipForDiImport(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    HashMap<String, Object> idVsRelationshipInfoMap = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    try {
      List<String> ids = (List<String>) requestMap.get(IIdsListParameterModel.IDS);
      String query = "Select from " + VertexLabelConstants.ROOT_RELATIONSHIP + " where code in "
          + EntityUtil.quoteIt(ids);
      Iterable<Vertex> resultIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      
      for (Vertex relationshipNode : resultIterable) {
        if (relationshipNode != null) {
          Iterable<Vertex> hasPropertyIterator = relationshipNode.getVertices(Direction.IN,
              RelationshipLabelConstants.HAS_PROPERTY);
          for (Vertex krNode : hasPropertyIterator) {
            prepareReturnMap(idVsRelationshipInfoMap, krNode, relationshipNode);
          }
        }
      }
    }
    catch (Exception e) {
      throw new RelationshipNotFoundException();
    }
    
    returnMap.put(IRelationshipsInfoModel.RELATIONSHIP_INFO, idVsRelationshipInfoMap);
    return returnMap;
  }
  
  private void prepareReturnMap(HashMap<String, Object> idVsRelationshipInfoMap, Vertex krNode,
      Vertex relationshipNode)
  {
    String id = UtilClass.getCodeNew(relationshipNode);
    HashMap<String, Object> sideMap = (HashMap<String, Object>) idVsRelationshipInfoMap.get(id);
    if (sideMap == null || sideMap.isEmpty()) {
      sideMap = new HashMap<String, Object>();
    }
    String side = krNode.getProperty(CommonConstants.SIDE_PROPERTY);
    if (side != null && !side.equals("")) {
      Map<String, Object> relationshipSide = krNode.getProperty(CommonConstants.RELATIONSHIP_SIDE);
      GetRelationshipUtils.prepareSideMapTranslation(relationshipSide);
      relationshipSide.put(IEntity.ID, krNode.getProperty(CommonConstants.CODE_PROPERTY)
          .toString());
      sideMap.put(side, relationshipSide);
    }
    idVsRelationshipInfoMap.put(id, sideMap);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetRootRelationshipsByIdsForDiImport/*" };
  }
}
