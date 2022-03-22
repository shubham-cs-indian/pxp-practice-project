package com.cs.config.strategy.plugin.usecase.asset;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfiguration;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class GetOrCreateAsset extends AbstractOrientPlugin {
  
  public static List<String> propertiesToExclude = Arrays.asList(IAssetModel.REFERENCED_CONTEXTS,
      IAssetModel.PARENT, IAssetModel.CHILDREN, IAssetModel.PERMISSIONS,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      IAssetModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE);
  
  public GetOrCreateAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    List<HashMap<String, Object>> assetMapList = new ArrayList<HashMap<String, Object>>();
    boolean inheritenceFlag = true;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    assetMapList = (List<HashMap<String, Object>>) map.get("list");
    inheritenceFlag = map.get("shouldInherit") != null ? (boolean) map.get("shouldInherit") : true;
    
    OrientGraph graph = UtilClass.getGraph();
    for (int i = 0; i < assetMapList.size(); i++) {
      HashMap<String, Object> assetMap = (HashMap<String, Object>) assetMapList.get(i);
      String assetId = (String) assetMap.get("code");
      Iterable<Vertex> existAsset = graph.getVertices(VertexLabelConstants.ENTITY_TYPE_ASSET,
          new String[] { CommonConstants.CODE_PROPERTY }, new String[] { assetId });
      
      if (!existAsset.iterator()
          .hasNext()) {
        if (ValidationUtils.validateKlassInfo(assetMap)) {
          List<Map<String, Object>> assetExtensionConfiguration = (List<Map<String, Object>>) assetMap
              .remove(IAssetModel.EXTENSION_CONFIGURATION);
          Vertex klassNode = createAssetNode(assetMap);
          klassNode.removeProperty(IAssetModel.REFERENCED_CONTEXTS);
          klassNode.removeProperty(IAssetModel.DATA_RULES_OF_KLASS);
          klassNode.removeProperty("parent");
          klassNode.removeProperty("children");
          klassNode.removeProperty("permission");
          klassNode.removeProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
          
          CreateKlassUtils.inheritTreeTypeOption(assetMap, klassNode,
              VertexLabelConstants.ENTITY_TYPE_ASSET);
          String parentId = "-1";
          HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) assetMap.get("parent");
          if (parentKlassMap != null) {
            parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
                .toString();
          }
          
          KlassUtils.createSectionNodes(assetMap, klassNode,
              VertexLabelConstants.ENTITY_TYPE_ASSET);
          KlassUtils.createParentChildLink(klassNode, VertexLabelConstants.ENTITY_TYPE_ASSET,
              assetMap, inheritenceFlag);
          // KlassUtils.manageGlobalPermissionForRoles(klassNode);
          
          List<Vertex> klassAndChildNodes = new ArrayList<Vertex>();
          klassAndChildNodes.add(klassNode);
          graph.commit();
          
          Map<String, Object> klassMap = AssetUtils.getAssetEntityMap(klassNode, null);
          returnMap.put("klass", klassMap);
          KlassUtils.addGlobalPermission(klassNode, returnMap);
          
          linkDefaultExtensions(klassNode, assetExtensionConfiguration);
        }
      }
    }
    
    return returnMap;
  }
  
  private void linkDefaultExtensions(Vertex klassNode,
      List<Map<String, Object>> assetExtensionConfiguration) throws Exception
  {
    for (Map<String, Object> assetExtension : assetExtensionConfiguration) {
      AssetUtils.createAndLinkExtension(klassNode,
          (String) assetExtension.get(IAssetExtensionConfiguration.EXTENSION),
          (Boolean) assetExtension.get(IAssetExtensionConfiguration.EXTRACT_METADATA),
          (Boolean) assetExtension.get(IAssetExtensionConfiguration.EXTRACT_RENDITION));
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateAsset/*" };
  }
  
  private Vertex createAssetNode(Map<String, Object> assetMap) throws Exception
  {
    Vertex assetNode = null;
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ASSET, CommonConstants.CODE_PROPERTY);
    assetMap.remove(IAssetModel.REFERENCED_TAGS);
    if (assetMap.get(CommonConstants.ID_PROPERTY) != null) {
      String assetId = assetMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      assetNode = UtilClass.createNode(assetMap, vertexType, propertiesToExclude);
      assetNode.setProperty(CommonConstants.CODE_PROPERTY, assetId);
    }
    else {
      assetNode = UtilClass.createNode(assetMap, vertexType, propertiesToExclude);
    }
    return assetNode;
  }
}
