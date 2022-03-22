package com.cs.config.strategy.plugin.usecase.asset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CreateAsset extends AbstractOrientPlugin {
  
  public CreateAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    HashMap<String, Object> assetMap = new HashMap<String, Object>();
    boolean inheritenceFlag = true;
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    
    assetMap = (HashMap<String, Object>) map.get("asset");
    inheritenceFlag = map.get("shouldInherit") != null ? (boolean) map.get("shouldInherit") : true;
    
    OrientGraph graph = UtilClass.getGraph();
    
    Vertex assetNode = createAssetNode(assetMap);
    CreateKlassUtils.inheritTreeTypeOption(assetMap, assetNode,
        VertexLabelConstants.ENTITY_TYPE_ASSET);
    String parentId = "-1";
    HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) assetMap.get("parent");
    if (parentKlassMap != null) {
      parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
          .toString();
    }
    
    KlassUtils.createSectionNodes(assetMap, assetNode, VertexLabelConstants.ENTITY_TYPE_ASSET);
    KlassUtils.createParentChildLink(assetNode, VertexLabelConstants.ENTITY_TYPE_ASSET, assetMap,
        inheritenceFlag);
    VariantContextUtils.manageContextKlasses(assetMap, assetNode);
    String natureType = (String) assetMap.get(IProjectKlass.NATURE_TYPE);
    if (natureType.equals(CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE)) {
      CreateKlassUtils.createDefaultContextNodeForTechnicalImageType(assetNode, assetMap);
    }
    
    if (parentId.equals("-1")) {
      CreateKlassUtils.inheritKlassRelationshipNodesInChildKlasses(assetNode, parentId);
    }
    AuditLogUtils.fillAuditLoginfo(returnMap, assetNode, Entities.CLASSES, Elements.ASSET);
    
    graph.commit();
    
    Map<String, Object> klassMap = AssetUtils.getAssetEntityMap(assetNode, false);
    returnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, klassMap);
    KlassGetUtils.fillReferencedConfigDetails(returnMap, assetNode);
    
    return returnMap;
  }
  
  
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateAsset/*" };
  }
  
  private Vertex createAssetNode(Map<String, Object> assetMap) throws Exception
  {
    
    Vertex assetNode = null;
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_ASSET, CommonConstants.CODE_PROPERTY);
    if (assetMap.get(CommonConstants.ID_PROPERTY) != null) {
      String assetId = assetMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      assetMap.remove(IAssetModel.REFERENCED_TAGS);
      assetNode = UtilClass.createNode(assetMap, vertexType, propertiesToExclude);
      assetNode.setProperty(CommonConstants.CODE_PROPERTY, assetId);
    }
    else {
      assetNode = UtilClass.createNode(assetMap, vertexType, propertiesToExclude);
    }
    return assetNode;
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(IAssetModel.REFERENCED_CONTEXTS,
      "parent", "children", "permission", CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      IAssetModel.EMBEDDED_KLASS_IDS);
}
