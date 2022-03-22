package com.cs.config.strategy.plugin.usecase.textasset;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.exception.NotFoundException;
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

public class GetOrCreateTextAsset extends AbstractOrientPlugin {
  
  public static List<String> propertiesToExclude = Arrays.asList(ISupplierModel.PARENT,
      ISupplierModel.CHILDREN, ISupplierModel.PERMISSIONS,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      ISupplierModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE);
  
  public GetOrCreateTextAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> textAssetMap = new HashMap<String, Object>();
    List<HashMap<String, Object>> targetMapList = (List<HashMap<String, Object>>) requestMap
        .get("list");
    boolean inheritenceFlag = true;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    inheritenceFlag = requestMap.get("shouldInherit") != null
        ? (boolean) requestMap.get("shouldInherit")
        : true;
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setSectionPermissionIdMap(new HashMap<>());
    
    for (int i = 0; i < targetMapList.size(); i++) {
      textAssetMap = targetMapList.get(i);
      String targetId = (String) textAssetMap.get("id");
      try {
        UtilClass.getVertexByIndexedId(targetId, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
      }
      catch (NotFoundException e) {
        Vertex textAssetNode = createTextAssetNode(UtilClass.getGraph(), textAssetMap);
        CreateKlassUtils.inheritTreeTypeOption(textAssetMap, textAssetNode,
            VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
        
        HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) textAssetMap
            .get("parent");
        
        String parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
            .toString();
        // CreateKlassUtils.createDefaultTemplateNode(textAssetMap,
        // textAssetNode, parentId, CommonConstants.KLASS_TEMPLATE);
        KlassUtils.createSectionNodes(textAssetMap, textAssetNode,
            VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
        
        KlassUtils.createParentChildLink(textAssetNode, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET,
            textAssetMap, inheritenceFlag);
        
        List<Vertex> klassAndChildNodes = new ArrayList<Vertex>();
        klassAndChildNodes.add(textAssetNode);
        
        CreateKlassUtils.manageSectionPermissions(parentId,
            textAssetNode.getProperty(CommonConstants.CODE_PROPERTY));
        
        String klassId = (String) textAssetNode.getProperty(CommonConstants.CODE_PROPERTY);
        
        List<String> utilizingIdsList = new ArrayList<>();
        utilizingIdsList.add(klassId);
        
        returnMap = TargetUtils.getTargetEntityMap(textAssetNode, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateTextAsset/*" };
  }
  
  private Vertex createTextAssetNode(OrientGraph graph, Map<String, Object> klassMap) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET, CommonConstants.CODE_PROPERTY);
    Vertex klassNode = null;
    klassMap.remove(ITextAssetModel.REFERENCED_TAGS);
    if (klassMap.get(CommonConstants.ID_PROPERTY) != null) {
      String klassId = klassMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      klassNode = UtilClass.createNode(klassMap, vertexType, propertiesToExclude);
      klassNode.setProperty(CommonConstants.CODE_PROPERTY, klassId);
    }
    else {
      klassNode = UtilClass.createNode(klassMap, vertexType, propertiesToExclude);
    }
    return klassNode;
  }
}
