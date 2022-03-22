package com.cs.config.strategy.plugin.usecase.target;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.target.ITargetModel;
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

public class GetOrCreateTarget extends AbstractOrientPlugin {
  
  public static List<String> propertiesToExclude = Arrays.asList(ITargetModel.PARENT,
      ITargetModel.CHILDREN, ITargetModel.PERMISSIONS, CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      ITargetModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE);
  
  public GetOrCreateTarget(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> targetMap = new HashMap<String, Object>();
    List<Map<String, Object>> targetMapList = (List<Map<String, Object>>) requestMap.get("list");
    boolean inheritenceFlag = true;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    inheritenceFlag = requestMap.get("shouldInherit") != null
        ? (boolean) requestMap.get("shouldInherit")
        : true;
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setSectionPermissionIdMap(new HashMap<>());
    
    for (int i = 0; i < targetMapList.size(); i++) {
      targetMap = targetMapList.get(i);
      String targetId = (String) targetMap.get("id");
      try {
        UtilClass.getVertexByIndexedId(targetId, VertexLabelConstants.ENTITY_TYPE_TARGET);
      }
      catch (NotFoundException e) {
        Vertex targetNode = createTargetNode(UtilClass.getGraph(), targetMap);
        CreateKlassUtils.inheritTreeTypeOption(targetMap, targetNode,
            VertexLabelConstants.ENTITY_TYPE_TARGET);
        
        HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) targetMap.get("parent");
        String parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
            .toString();
        KlassUtils.createSectionNodes(targetMap, targetNode,
            VertexLabelConstants.ENTITY_TYPE_TARGET);
        
        KlassUtils.createParentChildLink(targetNode, VertexLabelConstants.ENTITY_TYPE_TARGET,
            targetMap, inheritenceFlag);
        
        List<Vertex> klassAndChildNodes = new ArrayList<Vertex>();
        klassAndChildNodes.add(targetNode);
        
        CreateKlassUtils.manageSectionPermissions(parentId,
            targetNode.getProperty(CommonConstants.CODE_PROPERTY));
        returnMap = TargetUtils.getTargetEntityMap(targetNode, null);
      }
    }
    UtilClass.getGraph()
        .commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateTarget/*" };
  }
  
  private Vertex createTargetNode(OrientGraph graph, Map<String, Object> targetMap) throws Exception
  {
    Vertex targetNode = null;
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_TARGET, CommonConstants.CODE_PROPERTY);
    targetMap.remove(ITargetModel.REFERENCED_TAGS);
    if (targetMap.get(CommonConstants.ID_PROPERTY) != null) {
      String targetId = targetMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      targetNode = UtilClass.createNode(targetMap, vertexType, propertiesToExclude);
      targetNode.setProperty(CommonConstants.CODE_PROPERTY, targetId);
    }
    else {
      targetNode = UtilClass.createNode(targetMap, vertexType, propertiesToExclude);
    }
    return targetNode;
  }
}
