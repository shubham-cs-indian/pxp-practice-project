package com.cs.config.strategy.plugin.usecase.offboardingnaturerelationship;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.exception.relationship.RelationshipNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.IKlassNatureRelationshipModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// @SuppressWarnings("unchecked")
public class GetAllSideKlassIdsForNatureRelationship extends AbstractOrientPlugin {
  
  public GetAllSideKlassIdsForNatureRelationship(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Iterator<Vertex> verticesOfClass = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.NATURE_RELATIONSHIP)
        .iterator();
    List<HashMap<String, Object>> returnList = new ArrayList<>();
    Vertex relationshipNode = null;
    if (!verticesOfClass.hasNext()) {
      throw new RelationshipNotFoundException();
    }
    while (verticesOfClass.hasNext()) {
      HashMap<String, Object> returnMap = new HashMap<>();
      HashMap<String, Object> relationshipMap = new HashMap<>();
      relationshipNode = verticesOfClass.next();
      relationshipMap = UtilClass.getMapFromNode(relationshipNode);
      returnMap.put(IRelationship.ID, relationshipMap.get(IRelationship.ID));
      returnMap.put(IRelationship.SIDE1, relationshipMap.get(IRelationship.SIDE1));
      Iterator<Vertex> knrNodeIterator = relationshipNode
          .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      while (knrNodeIterator.hasNext()) {
        Vertex kRNode = knrNodeIterator.next();
        Boolean isNature = kRNode.getProperty(ISectionRelationship.IS_NATURE);
        if (isNature) {
          String targetRelationshipMappingId = UtilClass.getCodeNew(kRNode);
          returnMap.put(IKlassNatureRelationshipModel.TARGET_RELATIONSHIP_MAPPING_ID,
              targetRelationshipMappingId);
        }
      }
      
      returnList.add(returnMap);
    }
    Map<String, Object> listModel = new HashMap<>();
    listModel.put(IListModel.LIST, returnList);
    
    return listModel;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllSideKlassIdsForNatureRelationship/*" };
  }
}
