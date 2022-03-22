package com.cs.config.strategy.plugin.usecase.repair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class Orient_Migration_Script_FOR_RENDITION_TAB extends AbstractOrientPlugin {
  
  public Orient_Migration_Script_FOR_RENDITION_TAB(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|Orient_Migration_Script_FOR_RENDITION_TAB/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    /*OrientGraph graph = UtilClass.getGraph();
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ASSET  + " where natureType =" + CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE;
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
        .execute();
    */
    Iterator<Vertex> iterator = UtilClass.getGraph()
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ASSET)
        .iterator();
    Vertex tabNode = UtilClass.getVertexById(SystemLevelIds.RENDITION_TAB,
        VertexLabelConstants.TAB);
    while (iterator.hasNext()) {
      Vertex asset = iterator.next();
      Boolean isNature = asset.getProperty("isNature");
      Object natureType = asset.getProperty("natureType");
      if (isNature) {
        if (natureType.equals(CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE)) {
          Iterator<Vertex> contextIterator = asset
              .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
              .iterator();
          while (contextIterator.hasNext()) {
            Vertex context = contextIterator.next();
            Boolean isRenditionTab = false;
            
            for (Edge edge : context.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TAB)) {
              Vertex tab = edge.getVertex(Direction.IN);
              Object cid = tab.getProperty(CommonConstants.CODE_PROPERTY);
              List<String> tabEntityList = tab.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
              if (!cid.equals(SystemLevelIds.RENDITION_TAB)) {
                edge.remove();
                tabEntityList.remove(context.getProperty(CommonConstants.CODE_PROPERTY));
              }
              else {
                isRenditionTab = true;
              }
            }
            if (!isRenditionTab) {
              List<String> entityList = tabNode.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
              entityList.add(context.getProperty(CommonConstants.CODE_PROPERTY));
              tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, entityList);
              context.addEdge(RelationshipLabelConstants.HAS_TAB, tabNode);
            }
            
            List<String> tagValuesForImageExtensionIds = Arrays.asList(
                SystemLevelIds.IMAGE_EXTENSION_TAG_FORMAT_JPG,
                SystemLevelIds.IMAGE_EXTENSION_TAG_FORMAT_PNG,
                SystemLevelIds.IMAGE_EXTENSION_TAG_FORMAT_ORIGINAL);
            Map<String, Object> addedTagsMap = new HashMap<>();
            addedTagsMap.put(SystemLevelIds.IMAGE_EXTENSION_TAG, tagValuesForImageExtensionIds);
            VariantContextUtils.handleAddedTags(context, addedTagsMap);
            
            Vertex tagNode = UtilClass.getVertexById(SystemLevelIds.IMAGE_EXTENSION_TAG,
                VertexLabelConstants.ENTITY_TAG);
            Vertex tagValueNode = UtilClass.getVertexById(
                SystemLevelIds.IMAGE_EXTENSION_TAG_FORMAT_ORIGINAL,
                VertexLabelConstants.ENTITY_TAG);
            Iterable<Vertex> uniqueSelectorVertices = context.getVertices(Direction.OUT,
                RelationshipLabelConstants.HAS_UNIQUE_SELECTOR);
            for (Vertex uniqueSelectorVertex : uniqueSelectorVertices) {
              OrientVertexType uniqueTagPropertyVertexType = UtilClass.getOrCreateVertexType(
                  VertexLabelConstants.UNIQUE_TAG_PROPERTY, CommonConstants.CODE_PROPERTY);
              Vertex uniqueTagPropertyVertex = UtilClass.createNode(new HashMap<>(),
                  uniqueTagPropertyVertexType, new ArrayList<>());
              uniqueSelectorVertex.addEdge(RelationshipLabelConstants.HAS_UNIQUE_TAG_PROPERTY,
                  uniqueTagPropertyVertex);
              uniqueTagPropertyVertex.addEdge(RelationshipLabelConstants.HAS_UNIQUE_TAG, tagNode);
              uniqueTagPropertyVertex.addEdge(RelationshipLabelConstants.HAS_UNIQUE_TAG_VALUE,
                  tagValueNode);
            }
          }
        }
      }
    }
    return null;
  }
}
