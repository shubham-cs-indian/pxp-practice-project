package com.cs.config.strategy.plugin.usecase.textasset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.textasset.util.TextAssetUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

public class CreateTextAsset extends AbstractOrientPlugin {
  
  public CreateTextAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> textAssetMap = (Map<String, Object>) requestMap.get("textasset");
    boolean inheritenceFlag = true;
    Map<String, Object> returnKlassMap = new HashMap<>();
    
    inheritenceFlag = requestMap.get("shouldInherit") != null
        ? (boolean) requestMap.get("shouldInherit")
        : true;
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET, CommonConstants.CODE_PROPERTY);
    Vertex textAssetNode = createTextAssetNode(textAssetMap, vertexType);
    CreateKlassUtils.inheritTreeTypeOption(textAssetMap, textAssetNode,
        VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
    KlassUtils.createSectionNodes(textAssetMap, textAssetNode,
        VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
    KlassUtils.createParentChildLink(textAssetNode, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET,
        textAssetMap, inheritenceFlag);
    VariantContextUtils.manageContextKlasses(textAssetMap, textAssetNode);
    
    AuditLogUtils.fillAuditLoginfo(returnKlassMap, textAssetNode, Entities.CLASSES, Elements.TEXT_ASSET);

    UtilClass.getGraph()
        .commit();
    Map<String, Object> klassMap = TextAssetUtils.getTextAssetEntityMap(textAssetNode, false);
    returnKlassMap.put(IGetKlassEntityWithoutKPModel.ENTITY, klassMap);
    KlassGetUtils.fillReferencedConfigDetails(returnKlassMap, textAssetNode);
    return returnKlassMap;
  }
  
  private Vertex createTextAssetNode(Map<String, Object> textAssetMap, OrientVertexType vertexType) throws Exception
  {
    Vertex textAssetNode = null;
    if (textAssetMap.get(CommonConstants.ID_PROPERTY) != null) {
      String textAssetId = textAssetMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      textAssetMap.remove(ITextAssetModel.REFERENCED_TAGS);
      textAssetNode = UtilClass.createNode(textAssetMap, vertexType, propertiesToExclude);
      textAssetNode.setProperty(CommonConstants.CODE_PROPERTY, textAssetId);
    }
    else {
      textAssetNode = UtilClass.createNode(textAssetMap, vertexType, propertiesToExclude);
    }
    return textAssetNode;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateTextAsset/*" };
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(IAssetModel.REFERENCED_CONTEXTS,
      "parent", "children", "permission", CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      IAssetModel.EMBEDDED_KLASS_IDS);
}
