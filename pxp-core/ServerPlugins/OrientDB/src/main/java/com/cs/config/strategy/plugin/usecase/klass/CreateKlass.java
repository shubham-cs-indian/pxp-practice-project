package com.cs.config.strategy.plugin.usecase.klass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.CreateKlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.exception.validationontype.InvalidNatureTypeKlassException;
import com.cs.core.exception.InvalidTypeException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class CreateKlass extends AbstractOrientPlugin {
  
  public CreateKlass(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> klassmap = new HashMap<String, Object>();
    boolean inheritenceFlag = true;
    Map<String, Object> returnKlassMap = new HashMap<>();
    
    klassmap = (HashMap<String, Object>) requestMap.get("klass");
    try {
      UtilClass.validateOnType(Constants.NATURE_TYPES_LIST_FOR_KLASS,
          (String) klassmap.get(IKlass.NATURE_TYPE), true);
    }
    catch (InvalidTypeException e) {
      throw new InvalidNatureTypeKlassException(e);
    }
    
    inheritenceFlag = requestMap.get("shouldInherit") != null
        ? (boolean) requestMap.get("shouldInherit")
        : true;
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.ENTITY_KLASS,
        CommonConstants.CODE_PROPERTY);
    Vertex klassNode = KlassUtils.createKlassNode(klassmap, vertexType, propertiesToExclude);
    CreateKlassUtils.inheritTreeTypeOption(klassmap, klassNode,
        VertexLabelConstants.ENTITY_TYPE_KLASS);
    
    HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) klassmap.get("parent");
    String parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    
    KlassUtils.createTaxonomy(klassmap, klassNode, CommonConstants.ENTITY_KLASS);
    KlassUtils.createParentChildLink(klassNode, CommonConstants.ENTITY_KLASS, klassmap,
        inheritenceFlag);
    
    manageLinkedContextKlasses(klassmap, klassNode);
    VariantContextUtils.manageContextKlasses(klassmap, klassNode);
    KlassUtils.createSectionNodes(klassmap, klassNode, CommonConstants.ENTITY_KLASS);
    
    List<String> statusTagIds = new ArrayList<>();
    statusTagIds.add(SystemLevelIds.LIFE_STATUS_TAG_ID);
    statusTagIds.add(SystemLevelIds.LISTING_STATUS_TAG_ID);
    SaveKlassUtil.addLifeCycleStatusTag(statusTagIds, klassNode);
    
    List<Vertex> klassAndChildNodes = new ArrayList<Vertex>();
    klassAndChildNodes.add(klassNode);
    if (!parentId.equals("-1")) {
      CreateKlassUtils.inheritKlassRelationshipNodesInChildKlasses(klassNode, parentId);
    }
    
    String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    List<String> utilizingIdsList = new ArrayList<>();
    utilizingIdsList.add(klassId);
    
    if (!parentId.equals("-1") && (Boolean) klassNode.getProperty(IKlass.IS_NATURE)) {
      CreateKlassUtils.manageKlassNatureNode(parentId, klassNode, klassmap);
    }
    
    UtilClass.getGraph()
        .commit();
    
    returnKlassMap = KlassGetUtils.getKlassEntityReferencesMap(klassNode, false);
    KlassGetUtils.fillReferencedConfigDetails(returnKlassMap, klassNode);
    AuditLogUtils.fillAuditLoginfo(returnKlassMap, klassNode , Entities.CLASSES, Elements.ARTICLE);
    
    return returnKlassMap;
  }
  
  private void manageLinkedContextKlasses(Map<String, Object> klassADM, Vertex klassNode)
      throws Exception
  {
    List<String> addedContextKlasses = (List<String>) klassADM
        .get(IProjectKlass.EMBEDDED_KLASS_IDS);
    String gTINKlassId = (String) klassADM.get(IProjectKlass.GTIN_KLASS_ID);
    if (gTINKlassId != null) {
      addedContextKlasses.add(gTINKlassId);
    }
    for (String addedContextKlassId : addedContextKlasses) {
      Vertex unitKlassNode = UtilClass.getVertexById(addedContextKlassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      klassNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_KLASS, unitKlassNode);
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreateKlass/*" };
  }
  
  public static List<String> propertiesToExclude = Arrays.asList("referencedClassIds", "validator",
      "referencedAttributeIds", "permission", "deletedStructures", "notificationSettings",
      "structureChildren", "children", "klassViewSetting", "modifiedStructures", "addedStructures",
      IProjectKlass.IS_ENFORCED_TAXONOMY, IProjectKlass.PARENT, "referencedTags",
      CommonConstants.TREE_TYPE_OPTION_PROPERTY, IProjectKlass.EMBEDDED_KLASS_IDS,
      IProjectKlass.GTIN_KLASS_ID);
}
