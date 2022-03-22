package com.cs.config.strategy.plugin.usecase.asset.util;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetUtils {
  
  public static HashMap<String, Object> getAssetEntityMap(Vertex klassNode, Boolean shouldGetKP)
      throws Exception
  {
    HashMap<String, Object> klassEntityMap = new HashMap<>();
    klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
    KlassUtils.addChildrenInfoToKlassEntityMap(klassNode, klassEntityMap,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    KlassUtils.addParentInfoToKlassEntityMap(klassNode, klassEntityMap);
    KlassUtils.addSectionsToKlassEntityMap(klassNode, klassEntityMap, shouldGetKP);
    KlassUtils.addDataRules(klassNode, klassEntityMap);
    KlassUtils.addLifeCycleStatusTags(klassNode, klassEntityMap);
    KlassUtils.addKlassTasks(klassNode, klassEntityMap);
    KlassUtils.addVariantContexts(klassNode, klassEntityMap);
    KlassUtils.addKlassNature(klassNode, klassEntityMap);
    addAssetExtensionConfigurationDetails(klassNode, klassEntityMap,
        IAsset.EXTENSION_CONFIGURATION);
    return klassEntityMap;
  }
  
  public static void createAndLinkExtension(Vertex klassNode, String extension,
      Boolean extractMetadata, Boolean extractRendition) throws Exception
  {
    OrientVertexType extensionVertexType = UtilClass
        .getOrCreateVertexType(VertexLabelConstants.ASSET_EXTENSION);
    Map<String, Object> extensionMap = new HashMap<>();
    extensionMap.put(IAssetExtensionConfiguration.ID,
        UtilClass.getUniqueSequenceId(extensionVertexType));
    extensionMap.put(IAssetExtensionConfiguration.EXTENSION, extension);
    extensionMap.put(IAssetExtensionConfiguration.EXTRACT_METADATA, extractMetadata);
    extensionMap.put(IAssetExtensionConfiguration.EXTRACT_RENDITION, extractRendition);
    Vertex extensionVertex = UtilClass.createNode(extensionMap, extensionVertexType,
        new ArrayList<>());
    klassNode.addEdge(RelationshipLabelConstants.ASSET_EXTENSION_LINK, extensionVertex);
  }
  
  public static void getExtensionsByType(Vertex assetNode, Map<String, List<String>> assetEntityMap,
      String key)
  {
    List<String> extensions = new ArrayList<String>();
    Iterable<Vertex> extensionIterator = assetNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.ASSET_EXTENSION_LINK);
    for (Vertex extensionNode : extensionIterator) {
      HashMap<String, Object> mapFromNode = UtilClass.getMapFromNode(extensionNode);
      extensions.add("." + (String) mapFromNode.get(IAssetExtensionConfiguration.EXTENSION));
    }
    assetEntityMap.put(key, extensions);
  }
  
  public static void addAssetExtensionConfigurationDetails(Vertex assetNode,
      Map<String, Object> assetEntityMap, String key)
  {
    List<Map<String, Object>> extensions = new ArrayList<>();
    Iterable<Vertex> extensionIterator = assetNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.ASSET_EXTENSION_LINK);
    for (Vertex extensionNode : extensionIterator) {
      extensions.add(UtilClass.getMapFromNode(extensionNode));
    }
    assetEntityMap.put(key, extensions);
  }
  
  public static void getAllAssetExtensionsDetail(Map<String, Object> assetEntityMap)
  {
    List<Map<String, Object>> extensions = new ArrayList<>();
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> iterable = graph.getVerticesOfClass(VertexLabelConstants.ASSET_EXTENSION);
    for (Vertex extensionVertex : iterable) {
      extensions.add(UtilClass.getMapFromNode(extensionVertex));
    }
    assetEntityMap.put(IAsset.EXTENSION_CONFIGURATION, extensions);
  }
}
