package com.cs.config.strategy.plugin.usecase.klass.supplier;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.supplier.ISupplierModel;
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

@SuppressWarnings("unchecked")
public class GetOrCreateSupplier extends AbstractOrientPlugin {
  
  public static List<String> propertiesToExclude = Arrays.asList(ISupplierModel.PARENT,
      ISupplierModel.CHILDREN, ISupplierModel.PERMISSIONS,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      ISupplierModel.TECHNICAL_IMAGEVARIANT_CONTEXT_WITH_AUTOCREATEENABLE);
  
  public GetOrCreateSupplier(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> supplierMap = new HashMap<String, Object>();
    List<HashMap<String, Object>> targetMapList = new ArrayList<HashMap<String, Object>>();
    boolean inheritenceFlag = true;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    targetMapList = (List<HashMap<String, Object>>) map.get("list");
    inheritenceFlag = map.get("shouldInherit") != null ? (boolean) map.get("shouldInherit") : true;
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setSectionPermissionIdMap(new HashMap<>());
    
    for (int i = 0; i < targetMapList.size(); i++) {
      supplierMap = targetMapList.get(i);
      String targetId = (String) supplierMap.get("id");
      try {
        UtilClass.getVertexByIndexedId(targetId, VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
      }
      catch (NotFoundException e) {
        Vertex supplierNode = createSupplierNode(graph, supplierMap);
        CreateKlassUtils.inheritTreeTypeOption(supplierMap, supplierNode,
            VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
        HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) supplierMap
            .get("parent");
        String parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
            .toString();
        
        // CreateKlassUtils.createDefaultTemplateNode(supplierMap,supplierNode,parentId,CommonConstants.KLASS_TEMPLATE);
        KlassUtils.createSectionNodes(supplierMap, supplierNode,
            VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
        KlassUtils.createParentChildLink(supplierNode, VertexLabelConstants.ENTITY_TYPE_SUPPLIER,
            supplierMap, inheritenceFlag);
        
        List<Vertex> klassAndChildNodes = new ArrayList<Vertex>();
        klassAndChildNodes.add(supplierNode);
        
        CreateKlassUtils.manageSectionPermissions(parentId,
            supplierNode.getProperty(CommonConstants.CODE_PROPERTY));
        
        String klassId = (String) supplierNode.getProperty(CommonConstants.CODE_PROPERTY);
        List<String> utilizingIdsList = new ArrayList<>();
        utilizingIdsList.add(klassId);
        returnMap = TargetUtils.getTargetEntityMap(supplierNode, null);
      }
    }
    graph.commit();
    
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetOrCreateSupplier/*" };
  }
  
  private Vertex createSupplierNode(OrientGraph graph, HashMap<String, Object> klassMap) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_SUPPLIER, CommonConstants.CODE_PROPERTY);
    Vertex klassNode = null;
    klassMap.remove(ISupplierModel.REFERENCED_TAGS);
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
