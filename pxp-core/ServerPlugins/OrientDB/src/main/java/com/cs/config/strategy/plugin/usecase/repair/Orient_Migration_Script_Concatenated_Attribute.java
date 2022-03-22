package com.cs.config.strategy.plugin.usecase.repair;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedAttributeOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedOperator;
import com.cs.core.config.interactor.entity.attribute.IConcatenatedTagOperator;
import com.cs.core.config.interactor.entity.datarule.IAttributeOperator;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orient_Migration_Script_Concatenated_Attribute extends AbstractOrientMigration {
  
  public Orient_Migration_Script_Concatenated_Attribute(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_Concatenated_Attribute/*" };
  }
  
  @Override
  protected Object executeMigration(Map<String, Object> requestMap) throws Exception
  {
    String type = CommonConstants.CONCATENATED_ATTRIBUTE_TYPE;
    String query = "select from attribute where type in " + EntityUtil.quoteIt(type);
    Iterable<Vertex> concatenatedAttributes = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex vertex : concatenatedAttributes) {
      List<String> nodeIdsToBeDeleted = new ArrayList<>();
      Iterable<Vertex> concatenatedNodes = vertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK);
      List<Map<String, Object>> attributeConcatenatedList = new ArrayList<>();
      for (Vertex node : concatenatedNodes) {
        String attributeId = null;
        nodeIdsToBeDeleted.add(node.getProperty(CommonConstants.CODE_PROPERTY));
        HashMap<String, Object> concatenatedNodeMap = UtilClass.getMapFromNode(node);
        Iterable<Vertex> attributeVertices = node.getVertices(Direction.OUT,
            RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK);
        for (Vertex attributeVertex : attributeVertices) {
          attributeId = attributeVertex.getProperty(CommonConstants.CODE_PROPERTY);
        }
        concatenatedNodeMap.put(IAttributeOperator.ATTRIBUTE_ID, attributeId);
        attributeConcatenatedList.add(concatenatedNodeMap);
      }
      UtilClass.deleteNode(nodeIdsToBeDeleted, VertexLabelConstants.CONCATENATED_NODE_ATTRIBUTE);
      createConcatenatedNodes(vertex, attributeConcatenatedList);
    }
    UtilClass.getGraph()
        .commit();
    return null;
  }
  
  protected void createConcatenatedNodes(Vertex vertex,
      List<Map<String, Object>> attributeConcatenatedList) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.CONCATENATED_NODE, CommonConstants.CODE_PROPERTY);
    for (Map<String, Object> operator : attributeConcatenatedList) {
      String type = (String) operator.get(IConcatenatedOperator.TYPE);
      if (type.equals(CommonConstants.ATTRIBUTE)) {
        Vertex concatenatedVertex = UtilClass.createNode(operator, vertexType,
            new ArrayList<>(Arrays.asList(IConcatenatedAttributeOperator.ATTRIBUTE_ID,
                IConcatenatedTagOperator.TAG_ID, IAttributeOperator.VALUE)));
        // attributeId is the id of the operator attribute
        vertex.addEdge(RelationshipLabelConstants.ATTRIBUTE_CONCATENATED_NODE_LINK,
            concatenatedVertex);
        String attributeId = (String) operator.get(IConcatenatedAttributeOperator.ATTRIBUTE_ID);
        if (attributeId != null) {
          Vertex attributeVertex = UtilClass.getVertexById(attributeId,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          concatenatedVertex.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_ATTRIBUTE_LINK,
              attributeVertex);
        }
      }
      /*   else if(type.equals(CommonConstants.TAG)) {
        vertex.addEdge(RelationshipLabelConstants.TAG_CONCATENATED_NODE_LINK, concatenatedVertex);
        String tagId = (String) operator.get(IConcatenatedTagOperator.TAG_ID);
        if (tagId != null) {
          Vertex tagVertex = UtilClass.getVertexByIndexedId(tagId,
              VertexLabelConstants.ENTITY_TAG);
          concatenatedVertex.addEdge(RelationshipLabelConstants.CONCATENATED_NODE_TAG_LINK, tagVertex);
        }
      }*/
      else {
        Vertex concatenatedVertex = UtilClass.createNode(operator, vertexType,
            new ArrayList<>(Arrays.asList(IConcatenatedAttributeOperator.ATTRIBUTE_ID,
                IConcatenatedTagOperator.TAG_ID)));
        vertex.addEdge(RelationshipLabelConstants.HTML_CONCATENATED_NODE_LINK, concatenatedVertex);
      }
    }
  }
}
