package com.cs.config.strategy.plugin.usecase.target;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.target.ITargetModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateTarget extends AbstractOrientPlugin {
  
  public CreateTarget(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> targetMap = new HashMap<String, Object>();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    boolean inheritenceFlag = true;
    targetMap = (Map<String, Object>) requestMap.get("target");
    inheritenceFlag = requestMap.get("shouldInherit") != null
        ? (boolean) requestMap.get("shouldInherit")
        : true;
    
    Vertex targetNode = createTargetNode(targetMap);
    CreateKlassUtils.inheritTreeTypeOption(targetMap, targetNode,
        VertexLabelConstants.ENTITY_TYPE_TARGET);
    KlassUtils.createSectionNodes(targetMap, targetNode, VertexLabelConstants.ENTITY_TYPE_TARGET);
    KlassUtils.createParentChildLink(targetNode, VertexLabelConstants.ENTITY_TYPE_TARGET, targetMap,
        inheritenceFlag);
    VariantContextUtils.manageContextKlasses(targetMap, targetNode);
    
    UtilClass.getGraph()
        .commit();
    AuditLogUtils.fillAuditLoginfo(returnMap, targetNode, Entities.CLASSES, Elements.MARKET);
    
    Map<String, Object> klassMap = TargetUtils.getTargetEntityMap(targetNode, false);
    returnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, klassMap);
    KlassGetUtils.fillReferencedConfigDetails(returnMap, targetNode);
    return returnMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateTarget/*" };
  }
  
  private Vertex createTargetNode(Map<String, Object> targetMap) throws Exception
  {
    
    Vertex targetNode = null;
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_TARGET, CommonConstants.CODE_PROPERTY);
    if (targetMap.get(CommonConstants.ID_PROPERTY) != null) {
      String targetId = targetMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      targetMap.remove(ITargetModel.REFERENCED_TAGS);
      targetNode = UtilClass.createNode(targetMap, vertexType, propertiesToExclude);
      targetNode.setProperty(CommonConstants.CODE_PROPERTY, targetId);
    }
    else {
      targetNode = UtilClass.createNode(targetMap, vertexType, propertiesToExclude);
    }
    
    return targetNode;
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(IAssetModel.REFERENCED_CONTEXTS,
      "parent", "children", "permission", CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      IAssetModel.EMBEDDED_KLASS_IDS);
}
