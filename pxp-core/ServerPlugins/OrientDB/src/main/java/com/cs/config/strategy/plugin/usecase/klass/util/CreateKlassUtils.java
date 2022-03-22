package com.cs.config.strategy.plugin.usecase.klass.util;

import org.apache.commons.lang.mutable.MutableBoolean;

import com.cs.config.strategy.plugin.usecase.references.utils.ReferenceUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.klass.IAddedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.apache.commons.lang.mutable.MutableBoolean;

import java.util.*;

@SuppressWarnings("unchecked")
public class CreateKlassUtils {
  
  public static void inheritTreeTypeOption(Map<String, Object> klassMap, Vertex klassNode,
      String entityLabel) throws Exception
  {
    Map<String, Object> parentKlassMap = (Map<String, Object>) klassMap
        .get(CommonConstants.PARENT_PROPERTY);
    
    if (parentKlassMap != null) {
      String parentId = (String) parentKlassMap.get(CommonConstants.ID_PROPERTY);
      if (parentId == null || parentId.equals("") || parentId.equals("-1")) {
        linkKlassToNewTreeTypeVertex(klassMap, klassNode);
      }
      else {
        Vertex parentKlassNode = UtilClass.getVertexById(parentId, entityLabel);
        Iterable<Vertex> vertices = parentKlassNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.TREE_TYPE_OPTION_LINK);
        Iterator<Vertex> iterator = vertices.iterator();
        if (iterator.hasNext()) {
          Vertex treeTypeNode = iterator.next();
          String treeTypeOption = treeTypeNode
              .getProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY);
          Edge treeTypeOptionLink = klassNode
              .addEdge(RelationshipLabelConstants.TREE_TYPE_OPTION_LINK, treeTypeNode);
          treeTypeOptionLink.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
          klassMap.put(CommonConstants.TREE_TYPE_OPTION_PROPERTY, treeTypeOption);
        }
      }
    }
    else {
      linkKlassToNewTreeTypeVertex(klassMap, klassNode);
    }
  }
  
  private static void linkKlassToNewTreeTypeVertex(Map<String, Object> klassMap, Vertex klassNode)
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.TREE_TYPE,
        CommonConstants.CODE_PROPERTY);
    Map<String, Object> treeTypeMap = new HashMap<String, Object>();
    treeTypeMap.put(CommonConstants.TREE_TYPE_OPTION_PROPERTY,
        klassMap.get(CommonConstants.TREE_TYPE_OPTION_PROPERTY));
    
    Vertex treeTypeNode = UtilClass.createNode(treeTypeMap, vertexType);
    Edge treeTypeOptionLink = klassNode.addEdge(RelationshipLabelConstants.TREE_TYPE_OPTION_LINK,
        treeTypeNode);
    treeTypeOptionLink.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
  }
  
  public static void manageSectionPermissions(String parentId, String klassId) throws Exception
  {
    if (parentId.equals("-1")) {
      return;
    }
    Vertex parentKlassNode = null;
    try {
      parentKlassNode = UtilClass.getVertexById(parentId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    
    Iterable<Vertex> klassPropetyNodes = parentKlassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassPropetyNode : klassPropetyNodes) {
      Iterable<Edge> permissionEdges = klassPropetyNode.getEdges(Direction.IN,
          RelationshipLabelConstants.PERMISSION_FOR);
      for (Edge permissionEdge : permissionEdges) {
        List<String> utilizingKlassIds = permissionEdge
            .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        if (utilizingKlassIds.contains(parentId)) {
          utilizingKlassIds.add(klassId);
          permissionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
              utilizingKlassIds);
        }
      }
    }
  }
  
  public static void manageNotificationSetting(String parentId, String klassId) throws Exception
  {
    if (parentId.equals("-1")) {
      return;
    }
    Vertex parentKlassNode = null;
    try {
      parentKlassNode = UtilClass.getVertexById(parentId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    }
    catch (NotFoundException e) {
      throw new KlassNotFoundException();
    }
    
    Iterable<Vertex> klassPropetyNodes = parentKlassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassPropetyNode : klassPropetyNodes) {
      Iterable<Edge> notificationEdges = klassPropetyNode.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_NOTIFICATION_SETTING_FOR);
      for (Edge notificationEdge : notificationEdges) {
        List<String> utilizingKlassIds = notificationEdge
            .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        if (utilizingKlassIds.contains(parentId)) {
          utilizingKlassIds.add(klassId);
          notificationEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
              utilizingKlassIds);
        }
      }
    }
  }
  
  public static Map<String, Object> createKlassData(Map<String, Object> klassMap, Vertex klassNode,
      String entityType) throws Exception
  {
    inheritTreeTypeOption(klassMap, klassNode, entityType);
    if (entityType.equals(VertexLabelConstants.ENTITY_TYPE_KLASS)) {
      KlassUtils.createTaxonomy(klassMap, klassNode, entityType);
    }
    KlassUtils.createSectionNodes(klassMap, klassNode, entityType);
    
    HashMap<String, Object> parentKlassMap = (HashMap<String, Object>) klassMap.get("parent");
    
    String parentId = parentKlassMap.get(CommonConstants.ID_PROPERTY)
        .toString();
    boolean isParentStandardKlass = KlassUtils.rootLevelIds.contains(parentId.toString());
    boolean isInheriting = !parentId.equals("-1");
    
    KlassUtils.createParentChildLink(klassNode, entityType, klassMap, isInheriting);
    
    List<Vertex> klassAndChildNodes = new ArrayList<Vertex>();
    klassAndChildNodes.add(klassNode);
    
    manageSectionPermissions(parentId, klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    manageNotificationSetting(parentId, klassNode.getProperty(CommonConstants.CODE_PROPERTY));
    
    String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    List<String> klassAndChildIds = new ArrayList<>();
    klassAndChildIds.add(klassId);
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> returnKlassMap = new HashMap<>();

    Map<String, Object> klassmap = KlassUtils.getKlassEntityMap(klassNode);
    returnKlassMap.put("klass", klassmap);
    KlassUtils.addGlobalPermission(klassNode, returnKlassMap);
    
    return klassmap;
  }
  
  public static void manageKlassNatureNode(String parentId, Vertex klassNode,
      Map<String, Object> klassmap) throws Exception
  {
    Vertex parentNode = UtilClass.getVertexById(parentId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    Map<String, Object> parentNodeMap = KlassUtils.getKlassMap(parentNode, null);
    /*  if (!(Boolean) parentNodeMap.get(IKlass.IS_NATURE)) {
      klassNode.setProperty(IKlass.NATURE_TYPE, CommonConstants.SINGLE_ARTICLE);
    }*/
    Map<String, Object> klassADM = new HashMap<>();
    List<String> addedProductVariantContexts = new ArrayList<String>();
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) parentNodeMap
        .get(IKlass.RELATIONSHIPS);
    // Map<String, List<String>> contexts = (Map<String, List<String>>)
    // parentNodeMap.get(IKlass.CONTEXTS);
    
    // Dont inherit any nature relationships.. because if parent klass is
    // pid+sku and it has pidsku
    // relationship and child klass
    // is single/embed/gtin, then no need of that nature relationship..
    List<Map<String, Object>> relationshipsToExclude = new ArrayList<>();
    for (Map<String, Object> relationship : relationships) {
      String relationshipType = (String) relationship
          .get(IKlassNatureRelationship.RELATIONSHIP_TYPE);
      if (CommonConstants.NATURE_RELATIONSHIP_TYPES.contains(relationshipType)) {
        relationshipsToExclude.add(relationship);
      }
    }
    relationships.removeAll(relationshipsToExclude);
    
    List<Map<String, Object>> defaultRelationshipsToAdd = (List<Map<String, Object>>) klassmap
        .get(IKlass.RELATIONSHIPS);
    relationships.addAll(defaultRelationshipsToAdd);
    // addedProductVariantContexts.addAll(contexts.get(IKlassContextModel.PRODUCT_VARIANT_CONTEXTS));
    List<String> deletedProductVariantContexts = new ArrayList<>();
    klassADM.put(IKlass.NATURE_TYPE, klassNode.getProperty(IKlass.NATURE_TYPE));
    klassADM.put(IKlassSaveModel.ADDED_RELATIONSHIPS,
        updateRelationshipMaps(relationships, klassNode));
    SaveKlassUtil.manageKlassNatureInKlass(klassNode, klassADM, addedProductVariantContexts,
        deletedProductVariantContexts, new HashMap<>(), new HashMap<>(), new MutableBoolean());
  }
  
  public static List<HashMap<String, Object>> updateRelationshipMaps(
      List<Map<String, Object>> relationships, Vertex klassNode) throws Exception
  {
    String klassId = UtilClass.getCodeNew(klassNode);
    String natureType = klassNode.getProperty(IKlass.NATURE_TYPE);
    List<HashMap<String, Object>> updatedRelationshipMaps = new ArrayList<>();
    
    for (Map<String, Object> relationship : relationships) {
      
      // relationship.put(IKlassNatureRelationship.ID,
      // UtilClass.getUniqueSequenceId(relationshipVertexType));
      relationship.put(IKlassNatureRelationship.ID,
          relationship.get(CommonConstants.CODE_PROPERTY));
      relationship.put(IKlassNatureRelationship.CODE,
          relationship.get(IKlassNatureRelationship.CODE));
      
      Map<String, Object> side1 = (Map<String, Object>) relationship
          .get(IKlassNatureRelationship.SIDE1);
      side1.put(IRelationshipSide.ID, UtilClass.getUniqueSequenceId(null));
      side1.put(IRelationshipSide.KLASS_ID, klassId);
      side1.put(IRelationshipSide.LABEL,
          UtilClass.getValueByLanguage(klassNode, IKlass.LABEL));
      
      Map<String, Object> side2 = (Map<String, Object>) relationship
          .get(IKlassNatureRelationship.SIDE2);
      side2.put(IRelationshipSide.ID, UtilClass.getUniqueSequenceId(null));
      String side2KlassId = (String) side2.get(IRelationshipSide.KLASS_ID);
      String labelKey = EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
      Vertex sideKlassVertex = null;
      try {
        sideKlassVertex = UtilClass.getVertexById(side2KlassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      String side2Label = sideKlassVertex.getProperty(labelKey);
      side2.put(IRelationshipSide.LABEL, side2Label);
      relationship.put(IKlassNatureRelationship.LABEL, natureType + " - " + side2Label);
      Map<String, Object> propertyCollection = (Map<String, Object>) relationship
          .get(IKlassNatureRelationship.PROPERTY_COLLECTION);
      if (propertyCollection != null) {
        relationship.put(IAddedNatureRelationshipModel.ADDED_PROPERTY_COLLECTION,
            propertyCollection.get(IPropertyCollection.ID));
      }
      updatedRelationshipMaps.add((HashMap<String, Object>) relationship);
    }
    
    return updatedRelationshipMaps;
  }
  
  public static void inheritKlassRelationshipNodesInChildKlasses(Vertex klassNode, String parentId)
      throws Exception
  {
    List<Vertex> klassRelationshipNodes = getKlassRelationships(parentId);
    Iterable<Vertex> childNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : childNodes) {
      for (Vertex klassPropertyNode : klassRelationshipNodes) {
        Edge klassPropertyEdge = childNode.addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY,
            klassPropertyNode);
        klassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
        klassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY, new ArrayList<String>());
      }
    }
  }

  public static List<Vertex> getKlassRelationships(String code) throws Exception
  {
    Vertex parentNode = UtilClass.getVertexById(code,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    return getKlassRelationships(parentNode);
  }

  public static List<Vertex> getKlassRelationships(Vertex parentNode)
  {
    Iterable<Vertex> hasKlassProperties = parentNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    List<Vertex> klassRelationshipNodes = new ArrayList<>();
    for (Vertex hasKlassProperty : hasKlassProperties) {
      if (hasKlassProperty.getProperty(ISectionElement.TYPE)
          .equals(CommonConstants.RELATIONSHIP)) {
        klassRelationshipNodes.add(hasKlassProperty);
      }
    }
    return klassRelationshipNodes;
  }

  /**
   * Description creates default template. called by klass/taxonomy/context
   * usecases..
   *
   * @author Ajit
   * @param klassMap
   * @param klassNode
   * @param parentId
   * @param type
   * @return
   */
  /*
  public static Vertex createDefaultTemplateNode(Map<String, Object> klassMap, Vertex klassNode,
      String parentId, String type)
  {
    return createDefaultTemplateNode(klassMap, klassNode, parentId, type, false);
  }
  
  public static Vertex createDefaultTemplateNode(Map<String, Object> klassMap, Vertex klassNode,
      String parentId, String type, Boolean isInherited)
  {
    Map<String, Object> templateMap = new HashMap<String, Object>();
    String klassLabel = (String) klassMap.get(IKlass.LABEL);
    templateMap.put(ITemplate.LABEL, klassLabel);
    templateMap.put(ITemplate.TYPE, type);
  
    Boolean shouldCreateSequence = false;
    if(parentId.equals("-1")){
      shouldCreateSequence = true;
    }
    Vertex templateNode = TemplateUtils.createTemplateNode(templateMap, shouldCreateSequence);
    Edge hasTemplateEdge = klassNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE, templateNode);
    hasTemplateEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, isInherited);
    return templateNode;
  }*/
  
  public static void createDefaultContextNodeForTechnicalImageType(Vertex assetNode, Map<String, Object> assetMap) throws Exception
  {
    Vertex contextNode = KlassUtils.createContextNodeForKlass(assetNode,
        CommonConstants.IMAGE_VARIANT, (String) assetMap.get(IVariantContext.CODE));
    List<String> tagValuesIds = Arrays.asList(SystemLevelIds.RESOLUTION_150P,
        SystemLevelIds.RESOLUTION_300P, SystemLevelIds.RESOLUTION_72P);
    List<String> tagValuesForImageExtensionIds = Arrays.asList(
        SystemLevelIds.IMAGE_EXTENSION_TAG_FORMAT_JPG,
        SystemLevelIds.IMAGE_EXTENSION_TAG_FORMAT_PNG,
        SystemLevelIds.IMAGE_EXTENSION_TAG_FORMAT_ORIGINAL);
    Map<String, Object> addedTagsMap = new HashMap<>();
    addedTagsMap.put(SystemLevelIds.RESOLUTION_TAG, tagValuesIds);
    addedTagsMap.put(SystemLevelIds.IMAGE_EXTENSION_TAG, tagValuesForImageExtensionIds);
    VariantContextUtils.handleAddedTags(contextNode, addedTagsMap);
  }
}
