package com.cs.config.strategy.plugin.usecase.klass.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassContext;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassPermission;
import com.cs.core.config.interactor.entity.klass.IKlassTagValues;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionPermission;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTaxonomy;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.role.IRolePermission;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.entity.variantcontext.IAttributeContext;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.propertycollection.PropertyCollectionNotFoundException;
import com.cs.core.config.interactor.exception.template.SequenceNotFoundException;
import com.cs.core.config.interactor.model.datarule.IDataRule;
import com.cs.core.config.interactor.model.klass.IAttributeDefaultValueCouplingTypeModel;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityConfigDetailsModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassWithGlobalPermissionModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IPropagableContextKlassInformationModel;
import com.cs.core.config.interactor.model.klass.ISaveRelationshipToExportModel;
import com.cs.core.config.interactor.model.klass.ITagDefaultValueCouplingTypeModel;
import com.cs.core.config.interactor.model.tabs.ITabModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModel;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class KlassUtils {
  
  public static final List<String> TAXONOMY_PROPERTIES_TO_FETCH = Arrays.asList(
      CommonConstants.CODE_PROPERTY, IMasterTaxonomy.LABEL, IMasterTaxonomy.TYPE,
      IMasterTaxonomy.ICON, ITaxonomy.CODE, IMasterTaxonomy.TAXONOMY_TYPE, ITaxonomy.BASE_TYPE);
  
  public static final List<String> rootLevelIds                 = new ArrayList<>(Arrays.asList(
      SystemLevelIds.ARTICLE_KLASS, SystemLevelIds.COLLECTION_KLASS, SystemLevelIds.SET_KLASS));
  
  public static Map<String, Object> getKlassEntityMap(Vertex klassNode) throws Exception
  {
    return getKlassMap(klassNode, true);
  }
  
  public static Map<String, Object> getKlassMap(Vertex klassNode) throws Exception
  {
    return getKlassMap(klassNode, true);
  }
  
  public static Map<String, Object> getKlassMap(Vertex klassNode, Boolean shouldGetKP)
      throws Exception
  {
    Map<String, Object> klassEntityMap = null;
    if (klassNode != null) {
      klassEntityMap = new HashMap<>();
      klassEntityMap.putAll(UtilClass.getMapFromNode(klassNode));
      OrientVertexType vertexType = ((OrientVertex) klassNode).getType();
      
      addChildrenInfoToKlassEntityMap(klassNode, klassEntityMap, vertexType.getName());
      addSectionsToKlassEntityMap(klassNode, klassEntityMap, shouldGetKP);
      addParentInfoToKlassEntityMap(klassNode, klassEntityMap);
      
      // structures
      // addStructuresToKlassEntityMap(klassNode, klassEntityMap,
      // linkedKlassNodes);
      // addKlassViewSettingToEntityMap(klassNode, klassEntityMap);
      addTaxonomySettingToEntityMap(klassNode, klassEntityMap);
      addAllowedTypes(klassNode, klassEntityMap);
      addDataRules(klassNode, klassEntityMap);
      addVariantContexts(klassNode, klassEntityMap);
      addKlassNature(klassNode, klassEntityMap);
      addLifeCycleStatusTags(klassNode, klassEntityMap);
      addKlassTasks(klassNode, klassEntityMap);
    }
    return klassEntityMap;
  }
  
  /**
   * @author Ajit.Bhandari
   * @param klassNode
   * @param klassEntityMap
   * @throws MultipleVertexFoundException
   */
  public static void addParentInfoToKlassEntityMap(Vertex klassNode,
      Map<String, Object> klassEntityMap) throws MultipleVertexFoundException
  {
    Iterable<Vertex> parentKlassVertices = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    Iterator<Vertex> parentKlassesIterator = parentKlassVertices.iterator();
    if (!parentKlassesIterator.hasNext()) {
      Map<String, Object> parentKlassMap = new HashMap<>();
      parentKlassMap.put(IKlass.ID, "-1");
      parentKlassMap.put(IKlass.TYPE, klassNode.getProperty(IKlass.TYPE));
      klassEntityMap.put(IKlass.PARENT, parentKlassMap);
      return;
    }
    Vertex parentKlassVertex = parentKlassesIterator.next();
    if (parentKlassesIterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    Map<String, Object> parentKlassMap = UtilClass
        .getMapFromVertex(Arrays.asList(IKlass.ID, IKlass.TYPE), parentKlassVertex);
    klassEntityMap.put(IKlass.PARENT, parentKlassMap);
  }
  
  public static void addLifeCycleStatusTags(Vertex klassNode, Map<String, Object> klassEntityMap)
  {
    List<String> lifeCycleStatusTags = new ArrayList<>();
    Iterable<Vertex> linkedLifeCycleStatusTags = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);
    for (Vertex linkedLifeCycleStatusTag : linkedLifeCycleStatusTags) {
      String id = linkedLifeCycleStatusTag.getProperty(CommonConstants.CODE_PROPERTY);
      lifeCycleStatusTags.add(id);
    }
    klassEntityMap.put(IKlass.LIFE_CYCLE_STATUS_TAGS, lifeCycleStatusTags);
  }
  
  public static void addKlassTasks(Vertex klassNode, Map<String, Object> mapToReturn)
  {
    List<String> tasks = new ArrayList<>();
    mapToReturn.put(IKlass.TASKS, tasks);
    Iterable<Vertex> iterable = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TASK);
    for (Vertex vertex : iterable) {
      tasks.add(vertex.getProperty(CommonConstants.CODE_PROPERTY));
    }
  }
  
  public static void setTreeTypeOption(Vertex klassNode, HashMap<String, Object> klassEntityMap)
      throws Exception
  {
    Iterable<Vertex> vertices = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.TREE_TYPE_OPTION_LINK);
    Iterator<Vertex> iterator = vertices.iterator();
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    
    Vertex vertex = iterator.next();
    klassEntityMap.put(CommonConstants.TREE_TYPE_OPTION_PROPERTY,
        vertex.getProperty(CommonConstants.TREE_TYPE_OPTION_PROPERTY));
  }
  
  public static void addTaxonomySettingToEntityMap(Vertex klassNode,
      Map<String, Object> klassEntityMap)
  {
    Iterator<Edge> iterator = klassNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF)
        .iterator();
    Edge taxonomySettingOf = null;
    while (iterator.hasNext()) {
      taxonomySettingOf = iterator.next();
      Vertex taxonomyNode = taxonomySettingOf.getVertex(Direction.OUT);
      boolean isEnforcedTaxonomy = taxonomyNode
          .getProperty(CommonConstants.IS_ENFORCED_TAXONOMY_PROPERTY);
      klassEntityMap.put(CommonConstants.IS_ENFORCED_TAXONOMY_PROPERTY, isEnforcedTaxonomy);
    }
  }
  
  public static Map<String, Object> getKlassEntityReferencesMap(Vertex klassNode,
      boolean shouldGetReferencedKlasses) throws Exception
  {
    Set<Vertex> linkedKlassNodes = null;
    if (shouldGetReferencedKlasses) {
      linkedKlassNodes = new HashSet<>();
    }
    HashMap<String, Object> klassReturnMap = new HashMap<>();
    Map<String, Object> klassEntityMap = getKlassMap(klassNode);
    
    fillReferencedAttributesAndTags(klassReturnMap, klassEntityMap);
    
    klassReturnMap.put("klass", klassEntityMap);
    
    if (klassEntityMap != null && shouldGetReferencedKlasses) {
      List<Map<String, Object>> referencedKlasses = new ArrayList<>();
      for (Vertex referencedKlassNode : linkedKlassNodes) {
        Map<String, Object> linkedKlassMap = new HashMap<>(
            UtilClass.getMapFromNode(referencedKlassNode));
        linkedKlassMap.put(CommonConstants.NOTIFICATION_SETTINGS,
            new HashMap<String, Map<String, Boolean>>());
        addSectionsToKlassEntityMap(referencedKlassNode, linkedKlassMap);
        referencedKlasses.add(linkedKlassMap);
      }
      klassReturnMap.put("referencedKlasses", referencedKlasses);
    }
    
    return klassReturnMap;
  }
  
  @Deprecated
  public static void fillReferencedAttributesAndTags(Map<String, Object> klassReturnMap,
      Map<String, Object> klassEntityMap) throws Exception
  {
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedRelationships = new HashMap<>();
    Map<String, Object> referencedklasses = new HashMap<>();
    List<String> lifeCycleStatusTags = (List<String>) klassEntityMap
        .get(IKlass.LIFE_CYCLE_STATUS_TAGS);
    if (lifeCycleStatusTags != null) {
      for (String id : lifeCycleStatusTags) {
        Vertex linkedTagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
        referencedTags.put(id, referencedTag);
      }
    }
    klassReturnMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_TAGS, referencedTags);
    klassReturnMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_ATTRIBUTES,
        referencedAttributes);
    klassReturnMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_RELATIONSHIPS,
        referencedRelationships);
    fillReferencedTagsAttributesAndRelationshipsFromNatureRelationships(klassEntityMap,
        referencedAttributes, referencedTags, referencedRelationships, referencedklasses);
    
    klassReturnMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_KLASSES, referencedklasses);
  }
  
  public static void fillReferencedAttributesAndTagsDetails(Map<String, Object> configDetails,
      Map<String, Object> klassEntityMap) throws Exception
  {
    Map<String, Object> referencedTags = new HashMap<>();
    List<String> lifeCycleStatusTags = (List<String>) klassEntityMap
        .get(IKlass.LIFE_CYCLE_STATUS_TAGS);
    if (lifeCycleStatusTags != null) {
      for (String id : lifeCycleStatusTags) {
        Vertex linkedTagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
        referencedTags.put(id, referencedTag);
      }
    }
    Map<String, Object> referencedklasses = new HashMap<>();
    Map<String, Object> referencedAttributes = new HashMap<>();
    Map<String, Object> referencedRelationships = new HashMap<>();
    
    configDetails.put(IGetKlassWithGlobalPermissionModel.REFERENCED_TAGS, referencedTags);
    configDetails.put(IGetKlassWithGlobalPermissionModel.REFERENCED_ATTRIBUTES,
        referencedAttributes);
    configDetails.put(IGetKlassWithGlobalPermissionModel.REFERENCED_KLASSES, referencedklasses);
    configDetails.put(IGetKlassWithGlobalPermissionModel.REFERENCED_RELATIONSHIPS,
        referencedRelationships);
    
    fillReferencedTagsAttributesAndRelationshipsFromNatureRelationships(klassEntityMap,
        referencedAttributes, referencedTags, referencedRelationships, referencedklasses);
  }
  
  private static void fillReferencedTagsAttributesAndRelationshipsFromNatureRelationships(
      Map<String, Object> klassEntityMap, Map<String, Object> referencedAttributes,
      Map<String, Object> referencedTags, Map<String, Object> referencedRelationships,
      Map<String, Object> referencedklasses) throws Exception
  {
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) klassEntityMap
        .get(IKlass.RELATIONSHIPS);
    if (relationships != null) {
      for (Map<String, Object> relationship : relationships) {
        List<String> contextTags = (List<String>) relationship
            .get(IKlassNatureRelationship.CONTEXT_TAGS);
        for (String tagId : contextTags) {
          if (!referencedTags.containsKey(tagId)) {
            Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
            referencedTags.put(tagId, TagUtils.getTagMap(tagNode, true));
          }
        }
        
        Map<String, Object> side1Map = (Map<String, Object>) relationship
            .get(IKlassNatureRelationship.SIDE1);
        Map<String, Object> side2Map = (Map<String, Object>) relationship
            .get(IKlassNatureRelationship.SIDE2);
        fillConfigDetailsForSide(referencedAttributes, referencedTags, referencedRelationships,
            referencedklasses, new HashMap<>(), side1Map);
        fillConfigDetailsForSide(referencedAttributes, referencedTags, referencedRelationships,
            referencedklasses, new HashMap<>(), side2Map);
      }
    }
  }
  
  public static List<Map<String, Object>> getDataRulesOfKlass(Vertex klassNode, String endpointId,
      String organisationId, String physicalCatalogId) throws Exception
  {
    String query = getDataRulesQuery(klassNode, organisationId, physicalCatalogId, endpointId,
        RelationshipLabelConstants.HAS_KLASS_RULE_LINK);
    Iterable<Vertex> dataRuleVertices = executeQuery(query);
    
    List<Map<String, Object>> dataRules = new ArrayList<Map<String, Object>>();
    for (Vertex dataRuleVertex : dataRuleVertices) {
      Map<String, Object> dataRuleMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleVertex, true);
      dataRules.add(dataRuleMap);
    }
    return dataRules;
  }
  
  public static void addChildrenInfoToKlassEntityMap(Vertex klassNode,
      Map<String, Object> klassEntityMap, String label)
  {
    OrientGraph graph = UtilClass.getGraph();
    
    List<Map<String, Object>> childrenList = new ArrayList<>();
    klassEntityMap.put("children", childrenList);
    
    String parentId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "SELECT EXPAND( IN('CHILD_OF')) FROM " + label + " WHERE code=\"" + parentId
        + "\" order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + " asc";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    
    for (Vertex childklassNode : resultIterable) {
      Map<String, Object> childMap = new HashMap<>();
      childMap.putAll(UtilClass.getMapFromNode(childklassNode));
      childrenList.add(childMap);
    }
  }
  
  public static void removeLinkbetweenSectionElementAndNotificationSetting(
      Vertex sectionElementNode)
  {
    Iterable<Edge> relationshipsToRemove = sectionElementNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_NOTIFICATION_SETTING_FOR);
    
    for (Edge relationship : relationshipsToRemove) {
      Vertex notificationSettingNode = relationship.getVertex(Direction.OUT);
      notificationSettingNode.remove();
    }
  }
  
  public static Vertex getRespectiveRelationshipKlassPropertyNode(Vertex klassNode,
      Vertex entityNode, String sectionId)
  {
    OrientGraph graph = UtilClass.getGraph();
    String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "select from (select expand(in('has_property')) from " + entityNode.getId()
        + ") where in('has_klass_property') contains (code='" + klassId + "')";
    Iterable<Vertex> iterator = graph.command(new OCommandSQL(query))
        .execute();
    Vertex klassPropertyNode = null;
    for (Vertex vertex : iterator) {
      Iterable<Edge> edges = vertex.getEdges(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      for (Edge edge : edges) {
        List<String> list = (List<String>) edge
            .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
        if (list.contains(sectionId)) {
          klassPropertyNode = vertex;
        }
      }
    }
    
    return klassPropertyNode;
  }
  
  protected static void addNotificationSetting(Map<String, Object> klassMap,
      String sectionElementId, String roleId, Boolean isNotificationEnabled)
  {
    Map<String, Map<String, Boolean>> notificationSettings = (Map<String, Map<String, Boolean>>) klassMap
        .get(CommonConstants.NOTIFICATION_SETTINGS);
    if (notificationSettings == null) {
      notificationSettings = new HashMap<String, Map<String, Boolean>>();
      klassMap.put(CommonConstants.NOTIFICATION_SETTINGS, notificationSettings);
    }
    Map<String, Boolean> roleNotificationSetting = notificationSettings.get(roleId);
    if (roleNotificationSetting != null) {
      roleNotificationSetting.put(sectionElementId, isNotificationEnabled);
    }
    else {
      Map<String, Boolean> sectionElementNotificationMap = new HashMap<>();
      sectionElementNotificationMap.put(sectionElementId, isNotificationEnabled);
      notificationSettings.put(roleId, sectionElementNotificationMap);
    }
  }
  
  public static void updateSectionPermissionMapForAddedRoles(Vertex klassNode,
      Map<String, Object> klassADMMap, List<String> addedRoleIds)
  {
    List<String> sectionIds = new ArrayList<String>();
    Iterable<Edge> sectionOfRelationships = klassNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    for (Edge sectionOfRelationship : sectionOfRelationships) {
      Vertex sectionNode = sectionOfRelationship.getVertex(Direction.OUT);
      sectionIds.add((String) sectionNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    for (String roleId : addedRoleIds) {
      Map<String, Object> rolePermission = new HashMap<String, Object>();
      Map<String, Object> sectionPermissionMap = new HashMap<String, Object>();
      for (String sectionId : sectionIds) {
        Map<String, Object> sectionPermission = getDefaultSectionPermission();
        sectionPermissionMap.put(sectionId, sectionPermission);
      }
      rolePermission.put(CommonConstants.SECTION_PERMISSION_PROPERTY, sectionPermissionMap);
      
      Map<String, Object> permissions = (Map<String, Object>) klassADMMap
          .get(CommonConstants.PERMISSION_PROPERTY);
      Map<String, Object> rolePermissionMap = (Map<String, Object>) permissions
          .get(CommonConstants.ROLE_PERMISSION_PROPERTY);
      rolePermissionMap.put(roleId, rolePermission);
    }
  }
  
  public static Map<String, Object> getDefaultSectionPermission()
  {
    Map<String, Object> sectionPermission = new HashMap<String, Object>();
    sectionPermission.put(CommonConstants.IS_COLLAPSED_PROPERTY, false);
    sectionPermission.put(CommonConstants.IS_HIDDEN_PROPERTY, false);
    sectionPermission.put(CommonConstants.DISABLED_ELEMENTS_PROPERTY, new ArrayList<String>());
    return sectionPermission;
  }
  
  public static void createAndLinkElementsToKlassSection(Vertex sectionNode,
      List<HashMap<String, Object>> sectionElements, String klassId, List<String> klassAndChildIds,
      List<String> addedRoleIds) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    // now loop over to each section elements
    for (HashMap<String, Object> sectionElementMap : sectionElements) {
      
      sectionElementMap.remove(CommonConstants.IS_CUTOFF_PROPERTY);
      sectionElementMap.remove(CommonConstants.IS_INHERITED_PROPERTY);
      
      HashMap<String, Object> startPosition = (HashMap<String, Object>) sectionElementMap
          .get(CommonConstants.START_POSITION_PROPERTY);
      
      if (sectionElementMap.get(CommonConstants.TYPE_PROPERTY)
          .equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
        Map<String, Object> attributeMap = (Map<String, Object>) sectionElementMap
            .get(CommonConstants.ATTRIBUTE_PROPERTY);
        Boolean isDisabled = (Boolean) attributeMap.get(CommonConstants.IS_DISABLED_PROPERTY);
        if (isDisabled == null) {
          isDisabled = false;
        }
        sectionElementMap.put(CommonConstants.IS_DISABLED_PROPERTY, isDisabled);
      }
      
      Vertex positionNode = getSectionPositionNode(sectionNode, startPosition);
      List<String> utilizingKlassesOnExistingPosition = new ArrayList<>();
      Iterable<Edge> elementOfRelationships = positionNode.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF);
      
      // loop over elements of that particular position
      for (Edge elementOfRelation : elementOfRelationships) {
        String ownerId = (String) elementOfRelation
            .getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
        
        List<String> utilizingKlassIds = (List<String>) elementOfRelation
            .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        
        if (klassAndChildIds.contains(ownerId)) {
          utilizingKlassesOnExistingPosition.addAll(utilizingKlassIds);
        }
        else {
          List<String> newUtilizingKlassIds = utilizingKlassIds;
          newUtilizingKlassIds.removeAll(klassAndChildIds);
          
          elementOfRelation.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
              newUtilizingKlassIds);
        }
      }
      
      OrientVertexType vtype = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_TYPE_SECTION_ELEMENT, CommonConstants.CODE_PROPERTY);
      Vertex sectionElementNode = UtilClass.createNode(sectionElementMap, vtype);
      
      sectionElementNode.removeProperty(CommonConstants.START_POSITION_PROPERTY);
      sectionElementNode.removeProperty(CommonConstants.END_POSITION_PROPERTY);
      sectionElementNode.removeProperty(CommonConstants.ATTRIBUTE_PROPERTY);
      
      String secionElementIdFromUI = (String) sectionElementMap.get(CommonConstants.ID_PROPERTY);
      String sectionElementNodeId = sectionElementNode.getProperty(CommonConstants.CODE_PROPERTY)
          .toString();
      if (secionElementIdFromUI != null && !secionElementIdFromUI.equals(sectionElementNodeId)) {
        Map<String, String> sectionElementIdMap = UtilClass.getSectionElementIdMap();
        sectionElementIdMap.put(secionElementIdFromUI, sectionElementNodeId);
      }
      
      Edge sectionElementRelationship = sectionElementNode
          .addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF, positionNode);
      
      List<String> utilizingKlassIds = new ArrayList<>();
      utilizingKlassIds.addAll(klassAndChildIds);
      utilizingKlassIds.removeAll(utilizingKlassesOnExistingPosition);
      
      sectionElementRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
          utilizingKlassIds);
      sectionElementRelationship.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, klassId);
      Map<String, Object> sectionElementPermissionMap = new HashMap<>();
      sectionElementPermissionMap.put(CommonConstants.IS_DISABLED_PROPERTY, false);
      
      String sectionElementType = (String) sectionElementMap.get("type");
      String entityId = null;
      String entityLabel = null;
      
      switch (sectionElementType) {
        case SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE:
          Map<String, Object> attributeMap = (Map<String, Object>) sectionElementMap
              .get("attribute");
          entityId = (String) attributeMap.get(CommonConstants.ID_PROPERTY);
          entityLabel = VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE;
          
          if (attributeMap.get("type")
              .equals(CommonConstants.ASSIGNEE_ATTRIBUTE_TYPE)
              || attributeMap.get("type")
                  .equals(CommonConstants.OWNER_ATTRIBUTE_TYPE)) {
            addedRoleIds.add(entityId);
          }
          break;
        case SystemLevelIds.PROPERTY_TYPE_TAG:
          Map<String, Object> tagMap = (Map<String, Object>) sectionElementMap
              .get(CommonConstants.TAG_PROPERTY);
          entityId = (String) tagMap.get(CommonConstants.ID_PROPERTY);
          entityLabel = VertexLabelConstants.ENTITY_TAG;
          break;
        case SystemLevelIds.PROPERTY_TYPE_ROLE:
          Map<String, Object> roleMap = (Map<String, Object>) sectionElementMap
              .get(CommonConstants.ROLE_PROPERTY);
          entityId = (String) roleMap.get(CommonConstants.ID_PROPERTY);
          entityLabel = VertexLabelConstants.ENTITY_TYPE_ROLE;
          addedRoleIds.add(entityId);
          
          // major change to remove "role" property from section_element
          sectionElementNode.removeProperty(CommonConstants.ROLE_PROPERTY);
          
          break;
        case SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP:
          // throw new RuntimeException("Relationship not implemented");
          Map<String, Object> relationshipMap = (Map<String, Object>) sectionElementMap
              .get("relationship");
          entityId = (String) relationshipMap.get("id");
          entityLabel = VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP;
          sectionElementNode.removeProperty(CommonConstants.RELATIONSHIP_PROPERTY);
          break;
        default:
          throw new NotFoundException();
      }
      
      Vertex elementEntity = null;
      Vertex v = UtilClass.getVertexByIndexedId(entityId, entityLabel);
      elementEntity = v;
      
      if (elementEntity == null) {
        throw new NotFoundException();
      }
      
      elementEntity.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO,
          sectionElementNode);
      String elementLabel = (String) UtilClass.getValueByLanguage(elementEntity,
          CommonConstants.LABEL_PROPERTY);
      String sectionElementLabel = (String) UtilClass.getValueByLanguage(sectionElementNode,
          CommonConstants.LABEL_PROPERTY);
      if (elementLabel.equals(sectionElementLabel)) {
        sectionElementNode.setProperty(CommonConstants.LABEL_PROPERTY, "");
      }
    }
    sectionNode.removeProperty(CommonConstants.ELEMENTS_PROPERTY);
  }
  
  public static Vertex getSectionPositionNode(Vertex sectionNode, HashMap<String, Object> position)
  {
    Vertex foundPositionNode = null;
    Iterable<Edge> positionOfRelationships = sectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_POSITION_OF);
    Integer xPosition = (Integer) position.get("x");
    Integer yPosition = (Integer) position.get("y");
    
    for (Edge positionRelationship : positionOfRelationships) {
      
      Vertex positionNode = positionRelationship.getVertex(Direction.OUT);
      
      Integer nodeXPosition = (Integer) positionNode.getProperty("x");
      Integer nodeYPosition = (Integer) positionNode.getProperty("y");
      
      if (xPosition.equals(nodeXPosition) && yPosition.equals(nodeYPosition)) {
        foundPositionNode = positionNode;
        break;
      }
    }
    
    if (foundPositionNode == null) {
      OrientVertexType vtype = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.ENTITY_TYPE_SECTION_ELEMENT_POSITION, CommonConstants.CODE_PROPERTY);
      
      Vertex newPositionNode = UtilClass.createNode(position, vtype);
      newPositionNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_POSITION_OF,
          sectionNode);
      foundPositionNode = newPositionNode;
    }
    return foundPositionNode;
  }
  
  public static Vertex cutOffSectionElement(String klassId, List<String> klassAndChildrenIds,
      Vertex existingSectionElementNode, Edge elementOfRelationship)
  {
    Vertex elementNodeToUpdate;
    List<String> utilizingKlassIds = (List<String>) elementOfRelationship
        .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
    
    Vertex positionNode = elementOfRelationship.getVertex(Direction.IN);
    
    Vertex duplicatedSectionElement = KlassUtils.createDuplicateSectionElementAndLinkToPositionNode(
        klassAndChildrenIds, klassId, positionNode, elementOfRelationship,
        existingSectionElementNode);
    
    List<String> utilizingKlassIdsList = utilizingKlassIds;
    utilizingKlassIdsList.removeAll(klassAndChildrenIds);
    elementOfRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
        utilizingKlassIdsList);
    
    elementNodeToUpdate = duplicatedSectionElement;
    return elementNodeToUpdate;
  }
  
  protected static Vertex createDuplicateSectionElementAndLinkToPositionNode(List<String> klassIds,
      String owningKlassId, Vertex parentPositionNode, Edge sectionElementOfRelationship,
      Vertex sectionElementNode)
  {
    Vertex duplicatedSectionElementNode = UtilClass.createDuplicateNode(sectionElementNode);
    
    // new code to remove old utilizing when section element cutoff..
    List<String> oldUtilizingIds = (List<String>) sectionElementOfRelationship
        .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
    oldUtilizingIds.removeAll(klassIds);
    sectionElementOfRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
        oldUtilizingIds);
    
    // new code to put new section element ids to threadlocal..
    if (duplicatedSectionElementNode.getProperty(CommonConstants.CODE_PROPERTY) != null) {
      Map<String, String> sectionElementIdMap = UtilClass.getSectionElementIdMap();
      sectionElementIdMap.put(
          (String) sectionElementNode.getProperty(CommonConstants.CODE_PROPERTY),
          (String) duplicatedSectionElementNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Iterator<Edge> iterator = sectionElementNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO)
        .iterator();
    Edge elementEntityRelationship = null;
    while (iterator.hasNext()) {
      elementEntityRelationship = iterator.next();
    }
    
    Vertex elementEntityNode = elementEntityRelationship.getVertex(Direction.OUT);
    UtilClass.duplicateRelationshipBetweenToNodes(duplicatedSectionElementNode, elementEntityNode,
        elementEntityRelationship);
    Edge duplicatedSectionElementOfRelationship = UtilClass.duplicateRelationshipBetweenToNodes(
        parentPositionNode, duplicatedSectionElementNode, sectionElementOfRelationship);
    duplicatedSectionElementOfRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
        klassIds);
    duplicatedSectionElementOfRelationship.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY,
        owningKlassId);
    
    Iterable<Edge> permissionForRelationships = sectionElementNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
    for (Edge permissionRelationship : permissionForRelationships) {
      Vertex permissionNode = permissionRelationship.getVertex(Direction.OUT);
      iterator = permissionNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF)
          .iterator();
      Edge edge = null;
      while (iterator.hasNext()) {
        edge = iterator.next();
      }
      Vertex roleNode = edge.getVertex(Direction.OUT);
      Vertex duplicatedPermissionNode = UtilClass.createDuplicateNode(permissionNode);
      roleNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF,
          duplicatedPermissionNode);
      Edge permissionOfRelationship = duplicatedPermissionNode.addEdge(
          RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR,
          duplicatedSectionElementNode);
      permissionOfRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY, klassIds);
      permissionOfRelationship.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, owningKlassId);
    }
    
    // TODO :: notification nodes copy.. whole new bunch of code...
    Iterable<Edge> notificationForRelationships = sectionElementNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_NOTIFICATION_SETTING_FOR);
    for (Edge notificationForRelationship : notificationForRelationships) {
      Vertex notificationNode = notificationForRelationship.getVertex(Direction.OUT);
      iterator = notificationNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF)
          .iterator();
      Edge edge = null;
      while (iterator.hasNext()) {
        edge = iterator.next();
      }
      Vertex roleNode = edge.getVertex(Direction.OUT);
      Vertex duplicatedNotificationNode = UtilClass.createDuplicateNode(notificationNode);
      roleNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF,
          duplicatedNotificationNode);
      Edge duplicatedNotificationOfRelationship = duplicatedNotificationNode.addEdge(
          RelationshipLabelConstants.RELATIONSHIPLABEL_NOTIFICATION_SETTING_FOR,
          duplicatedSectionElementNode);
      duplicatedNotificationOfRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
          klassIds);
      duplicatedNotificationOfRelationship.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY,
          owningKlassId);
      // now remove utilizing from old notification nodes.. TODO::
      List<String> utilizingIds = notificationForRelationship
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      utilizingIds.removeAll(klassIds);
      notificationForRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
          utilizingIds);
    }
    
    Iterable<Edge> allowedTagRelationships = sectionElementNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ALLOWED_TAGS);
    for (Edge allowedTagRelationship : allowedTagRelationships) {
      Vertex allowedTagNode = allowedTagRelationship.getVertex(Direction.IN);
      duplicatedSectionElementNode
          .addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ALLOWED_TAGS, allowedTagNode);
    }
    
    duplicatedSectionElementNode.setProperty(CommonConstants.IS_CUTOFF_PROPERTY, true);
    
    // remove if nobody uses section element...
    oldUtilizingIds = sectionElementOfRelationship
        .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
    if (oldUtilizingIds.isEmpty()) {
      KlassUtils.removeLinkbetweenSectionElementAndNotificationSetting(sectionElementNode);
      KlassUtils.removeLinkbetweenSectionElementAndPermissions(sectionElementNode);
      sectionElementNode.remove();
    }
    
    return duplicatedSectionElementNode;
  }
  
  public static void deleteSectionFromKlass(Edge sectionOfRelationship, Vertex sectionNode,
      List<String> klassAndChildIds, String klassId, Set<Vertex> nodesToDelete,
      Set<Edge> relationshipsToDelete, boolean isKlassDeleted, String vertexLabel,
      Map<String, List<String>> deletedPropertyMap) throws Exception
  {
    Boolean isInheritedSection = (Boolean) sectionOfRelationship
        .getProperty(CommonConstants.IS_INHERITED_PROPERTY);
    
    if (!isInheritedSection) {
      KlassUtils.deleteSectionTree(sectionNode, klassId, klassAndChildIds, nodesToDelete,
          relationshipsToDelete, true, isKlassDeleted, deletedPropertyMap, vertexLabel);
    }
    else {
      KlassUtils.deleteSectionTree(sectionNode, klassId, klassAndChildIds, nodesToDelete,
          relationshipsToDelete, false, isKlassDeleted, deletedPropertyMap, vertexLabel);
    }
    
    Iterable<Edge> sectionOfRelationships = sectionNode.getEdges(Direction.OUT);
    for (Edge klassSectionRelationship : sectionOfRelationships) {
      Vertex klassNode = klassSectionRelationship.getVertex(Direction.IN);
      if (klassAndChildIds.contains(klassNode.getProperty(CommonConstants.CODE_PROPERTY))) {
        relationshipsToDelete.add(klassSectionRelationship);
      }
    }
  }
  
  public static void deleteSectionTree(Vertex sectionNode, String klassId,
      List<String> selfAndChildKlassIds, Set<Vertex> nodesToDelete, Set<Edge> relationshipsToDelete,
      boolean forceDelete, boolean isKlassDeleted, Map<String, List<String>> deletedPropertyMap,
      String vertexLabel) throws Exception
  {
    // updateSequenceOfSectionNodes(sectionNode,klassId,selfAndChildKlassIds,
    // relationshipsToDelete);
    // delete sections
    if (forceDelete) {
      nodesToDelete.add(sectionNode);
      // delete permissionNodes
      Vertex pCNode = KlassUtils.getPropertyCollectionNodeFromKlassSectionNode(sectionNode);
      GlobalPermissionUtils.deletePropertyCollectionPermissionNode(klassId,
          UtilClass.getCodeNew(pCNode), vertexLabel);
    }
    
    String sectionId = UtilClass.getCodeNew(sectionNode);
    Set<Edge> hasKPEdgesUtilzingSections = getHasKPEdgeUtilizingSection(sectionId,
        selfAndChildKlassIds);
    updateKPAndHasKPOnSectionRemoval(sectionId, hasKPEdgesUtilzingSections, isKlassDeleted,
        nodesToDelete, relationshipsToDelete, deletedPropertyMap, klassId, vertexLabel);
  }
  
  public static void deleteKlassPropertyNode(Set<Vertex> nodesToDelete, boolean isKlassDeleted,
      Vertex klassPropertyNode, Map<String, List<String>> deletedPropertyMap) throws Exception
  {
    if (!klassPropertyNode.getProperty(CommonConstants.TYPE_PROPERTY)
        .equals(CommonConstants.RELATIONSHIP)) {
      nodesToDelete.add(klassPropertyNode);
      Iterable<Vertex> iterableNodes = klassPropertyNode.getVertices(Direction.IN,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
      List<String> nodesIdsAssociatedWithKlassPropertyNode = new ArrayList<>();
      for (Vertex node : iterableNodes) {
        nodesIdsAssociatedWithKlassPropertyNode.add(UtilClass.getCodeNew(node));
      }
      Iterator<Vertex> propertyNodes = klassPropertyNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex propertyNode = propertyNodes.next();
      String entityId = UtilClass.getCodeNew(propertyNode);
      deletedPropertyMap.put(entityId, nodesIdsAssociatedWithKlassPropertyNode);
    }
    
    // check if klass does have any taxanomy associated with it
    TaxonomyUtil.deleteTaxonomyNodesAttachedToSectionElement(
        klassPropertyNode.getProperty(CommonConstants.CODE_PROPERTY), isKlassDeleted);
  }
  
  public static void inheritParentKlassData(Vertex childKlassNode, Vertex parentKlassNode)
      throws Exception
  {
    inheritSectionsOfParentKlass(childKlassNode, parentKlassNode);
    // inheritStructuresOfParentKlass(childKlassNode, parentKlassNode);
  }
  
  protected static void inheritSectionsOfParentKlass(Vertex childKlassNode, Vertex parentKlassNode)
      throws Exception
  {
    Iterable<Edge> parentKlassSectionRelationships = parentKlassNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    String parentKlassId = (String) parentKlassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String klassId = (String) childKlassNode.getProperty(CommonConstants.CODE_PROPERTY);
    
    for (Edge parentKlassSectionRelationship : parentKlassSectionRelationships) {
      
      Vertex sectionNode = parentKlassSectionRelationship.getVertex(Direction.OUT);
      Edge inheritedSectionRelationship = sectionNode
          .addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF, childKlassNode);
      inheritedSectionRelationship.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      
      Iterable<Edge> previousSectionEdges = sectionNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.PREVIOUS_SECTION);
      for (Edge previousSectionEdge : previousSectionEdges) {
        String owningKlassId = previousSectionEdge
            .getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
        List<String> utilizingKlassIds = previousSectionEdge
            .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        if (utilizingKlassIds.contains(parentKlassId)) {
          utilizingKlassIds.add(klassId);
          previousSectionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
              utilizingKlassIds);
        }
      }
    }
    Iterable<Edge> hasKlassAttributes = parentKlassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Edge hasKlassAttribute : hasKlassAttributes) {
      List<String> utilizingSectionIds = hasKlassAttribute
          .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
      Vertex klassAttribute = hasKlassAttribute.getVertex(Direction.IN);
      String classProperty = klassAttribute.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY);
      if (classProperty.equals(VertexLabelConstants.KLASS_RELATIONSHIP)) {
        Boolean isNature = klassAttribute.getProperty(ISectionRelationship.IS_NATURE);
        if (isNature != null && isNature) {
          continue;
        }
      }
      Edge DuplicateHasKlassAttribute = childKlassNode
          .addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY, klassAttribute);
      DuplicateHasKlassAttribute.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      DuplicateHasKlassAttribute.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
          utilizingSectionIds);
    }

    /*
     * TODO inherit PC and sequence
    Vertex parentTemplateNode = TemplateUtils.getTemplateFromKlassIfExist(parentKlassNode);
    Vertex childTemplateNode = TemplateUtils.getTemplateFromKlassIfExist(childKlassNode);
    String type = ((OrientVertex)childKlassNode).getType().toString();
    if(!type.equals(VertexLabelConstants.KLASS_TAXONOMY)
        && !type.equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)
        && !type.equals(VertexLabelConstants.COLLECTION)) {
      inheritParentPCInTemplate(childTemplateNode,parentTemplateNode);
      inheritParentRelationshipInTemplate(childTemplateNode,parentTemplateNode);
      inheritSequenceFromParent(childTemplateNode, parentTemplateNode, RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE);
      inheritSequenceFromParent(childTemplateNode, parentTemplateNode, RelationshipLabelConstants.HAS_RELATIONSHIP_SEQUENCE);
    }*/
  }
  
  public static Vertex cutOffSectionFromKlass(Vertex klassNode, Edge sectionOfRelationship,
      Vertex sectionNode, List<String> klassAndChildIds, String klassId)
  {
    Vertex sectionNodeToModify;
    Vertex duplicatedKlassSectionNode = KlassUtils.createDuplicateKlassSectionNode(sectionNode,
        klassAndChildIds, klassId);
    
    // new code to set new section id craeted in threadlocal..
    if (duplicatedKlassSectionNode.getProperty(CommonConstants.CODE_PROPERTY) != null) {
      Map<String, String> sectionIdMap = UtilClass.getSectionIdMap();
      sectionIdMap.put(sectionNode.getProperty(CommonConstants.CODE_PROPERTY)
          .toString(),
          duplicatedKlassSectionNode.getProperty(CommonConstants.CODE_PROPERTY)
              .toString());
    }
    
    Edge duplicatedSectionOfRelationship = UtilClass.duplicateRelationshipBetweenToNodes(klassNode,
        duplicatedKlassSectionNode, sectionOfRelationship);
    
    duplicatedSectionOfRelationship.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
    duplicatedSectionOfRelationship.setProperty(CommonConstants.IS_CUTOFF_PROPERTY, true);
    sectionNodeToModify = duplicatedKlassSectionNode;
    sectionOfRelationship.remove();
    
    Iterable<Edge> sectionOfRelationships = sectionNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    for (Edge sectionRelationship : sectionOfRelationships) {
      Vertex oppositeKlassNode = sectionRelationship.getVertex(Direction.IN);
      String oppositeKlassId = (String) oppositeKlassNode
          .getProperty(CommonConstants.CODE_PROPERTY);
      
      if (klassAndChildIds.contains(oppositeKlassId)) {
        Edge duplicatedChildKlassSectionRelationship = UtilClass
            .duplicateRelationshipBetweenToNodes(oppositeKlassNode, duplicatedKlassSectionNode,
                sectionRelationship);
        duplicatedChildKlassSectionRelationship.setProperty(CommonConstants.IS_INHERITED_PROPERTY,
            true);
        sectionRelationship.remove();
      }
    }
    return sectionNodeToModify;
  }
  
  @Deprecated
  public static Vertex createDuplicateKlassSectionNode(Vertex sectionNode, List<String> klassIds,
      String owningKlassId)
  {
    Vertex duplicatedSectionNode = UtilClass.createDuplicateNode(sectionNode);
    
    Iterable<Edge> positionOfRelationships = sectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_POSITION_OF);
    
    for (Edge sectionPositionRelationship : positionOfRelationships) {
      Vertex sectionPositionNode = sectionPositionRelationship.getVertex(Direction.OUT);
      
      Iterable<Edge> elementOfRelationships = sectionPositionNode.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF);
      for (Edge sectionElementOfRelationship : elementOfRelationships) {
        List<String> sectionElementOfUtilizingIds = (List<String>) sectionElementOfRelationship
            .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        if (sectionElementOfUtilizingIds.contains(owningKlassId)) {
          Vertex duplicateSectionPositionNode = UtilClass.createDuplicateNode(sectionPositionNode);
          UtilClass.duplicateRelationshipBetweenToNodes(duplicatedSectionNode,
              duplicateSectionPositionNode, sectionPositionRelationship);
          Vertex sectionElementNode = sectionElementOfRelationship.getVertex(Direction.OUT);
          createDuplicateSectionElementAndLinkToPositionNode(klassIds, owningKlassId,
              duplicateSectionPositionNode, sectionElementOfRelationship, sectionElementNode);
        }
      }
    }
    
    // section permissions..
    Iterable<Edge> permissionForRelationships = sectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
    for (Edge permissionRelationship : permissionForRelationships) {
      
      Vertex permissionNode = permissionRelationship.getVertex(Direction.OUT);
      Iterator<Edge> iterator = permissionNode
          .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF)
          .iterator();
      Vertex roleNode = null;
      Edge roleOf = null;
      while (iterator.hasNext()) {
        roleOf = iterator.next();
        roleNode = roleOf.getVertex(Direction.OUT);
      }
      
      Vertex duplicatedPermissionNode = UtilClass.createDuplicateNode(permissionNode);
      
      if (duplicatedPermissionNode.getProperty(CommonConstants.CODE_PROPERTY) != null) {
        Map<String, String> sectionPermissionIdMap = UtilClass.getSectionPermissionIdMap();
        sectionPermissionIdMap.put(permissionNode.getProperty(CommonConstants.CODE_PROPERTY)
            .toString(),
            duplicatedPermissionNode.getProperty(CommonConstants.CODE_PROPERTY)
                .toString());
      }
      
      roleNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_ROLE_OF,
          duplicatedPermissionNode);
      Edge duplicatedPermissionOfRelationship = duplicatedPermissionNode.addEdge(
          RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR, duplicatedSectionNode);
      
      duplicatedPermissionOfRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
          klassIds);
      duplicatedPermissionOfRelationship.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY,
          owningKlassId);
    }
    return duplicatedSectionNode;
  }
  
  public static void manageADMElementsOfModifiedSection(String klassId, Vertex existingSectionNode,
      List<HashMap<String, Object>> modifiedElements, List<String> deletedElementIds,
      List<HashMap<String, Object>> addedElements, List<String> klassAndChildrenIds,
      List<String> addedRoleIds) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Edge> sectionElementRelationships = existingSectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF);
    for (HashMap<String, Object> modifiedElement : modifiedElements) {
      
      String modifiedElementId = (String) modifiedElement.get(CommonConstants.ID_PROPERTY);
      
      String modifiedElementType = (String) modifiedElement.get(CommonConstants.TYPE_PROPERTY);
      if (modifiedElementType.equals(CommonConstants.TAG_PROPERTY)) {
        Boolean isFilterable = (Boolean) modifiedElement
            .get(CommonConstants.IS_FILTERABLE_PROPERTY);
        if (isFilterable.equals(false)) {
          TaxonomyUtil.deleteTaxonomyNodesAttachedToSectionElement(modifiedElementId, false);
        }
      }
      else if (modifiedElementType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
        Boolean isSortable = (Boolean) modifiedElement.get(CommonConstants.IS_SORTABLE_PROPERTY);
        if (isSortable.equals(false)) {
          TaxonomyUtil.deleteTaxonomyNodesAttachedToSectionElement(modifiedElementId, false);
        }
      }
      
      Vertex existingSectionElementNode = UtilClass.getVertexByIndexedId(modifiedElementId,
          VertexLabelConstants.ENTITY_TYPE_SECTION_ELEMENT);
      Iterator<Edge> iterator = existingSectionElementNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF)
          .iterator();
      Edge elementOfRelationship = null;
      while (iterator.hasNext()) {
        elementOfRelationship = iterator.next();
      }
      
      String owningKlassId = (String) elementOfRelationship
          .getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      Vertex elementNodeToUpdate = existingSectionElementNode;
      
      modifiedElement.remove(CommonConstants.IS_CUTOFF_PROPERTY);
      modifiedElement.remove(CommonConstants.IS_INHERITED_PROPERTY);
      if (!owningKlassId.equals(klassId)) {
        
        elementNodeToUpdate = KlassUtils.cutOffSectionElement(klassId, klassAndChildrenIds,
            existingSectionElementNode, elementOfRelationship);
      }
      
      UtilClass.saveNode(modifiedElement, elementNodeToUpdate);
      
      // new code fix
      HashMap<String, Object> startPosition = (HashMap<String, Object>) modifiedElement
          .get(CommonConstants.START_POSITION_PROPERTY);
      Vertex newPositionNode = KlassUtils.getSectionPositionNode(existingSectionNode,
          startPosition);
      iterator = elementNodeToUpdate
          .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF)
          .iterator();
      Edge existingElementOfRelationship = null;
      while (iterator.hasNext()) {
        existingElementOfRelationship = iterator.next();
      }
      
      Vertex existingPositionNode = existingElementOfRelationship.getVertex(Direction.IN);
      if (!newPositionNode.equals(existingPositionNode)) {
        UtilClass.duplicateRelationshipBetweenToNodes(newPositionNode, elementNodeToUpdate,
            existingElementOfRelationship);
        existingElementOfRelationship.remove();
      }
    }
    
    // TODO : Deleted Elements
    for (String deletedElementId : deletedElementIds) {
      
      TaxonomyUtil.deleteTaxonomyNodesAttachedToSectionElement(deletedElementId, false);
      
      Vertex existingSectionElementNode = UtilClass.getVertexByIndexedId(deletedElementId,
          VertexLabelConstants.ENTITY_TYPE_SECTION_ELEMENT);
      Iterator<Edge> iterator = existingSectionElementNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_ELEMENT_OF)
          .iterator();
      Edge elementOfRelationship = null;
      while (iterator.hasNext()) {
        elementOfRelationship = iterator.next();
      }
      
      String owningKlassId = (String) elementOfRelationship
          .getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      List<String> utilizingKlassIds = (List<String>) elementOfRelationship
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      
      if (owningKlassId.equals(klassId)) {
        
        KlassUtils
            .removeLinkbetweenSectionElementAndNotificationSetting(existingSectionElementNode);
        KlassUtils.removeLinkbetweenSectionElementAndPermissions(existingSectionElementNode);
        
        Iterable<Edge> allSectionElementRelationships = existingSectionElementNode
            .getEdges(Direction.BOTH);
        
        for (Edge sectionElementRelationship : allSectionElementRelationships) {
          sectionElementRelationship.remove();
        }
        existingSectionElementNode.remove();
      }
      else {
        List<String> utilizingKlassIdsList = utilizingKlassIds;
        utilizingKlassIdsList.removeAll(klassAndChildrenIds);
        elementOfRelationship.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
            utilizingKlassIdsList);
      }
    }
    
    // Added Elements
    KlassUtils.createAndLinkElementsToKlassSection(existingSectionNode, addedElements, klassId,
        klassAndChildrenIds, addedRoleIds);
  }
  
  protected static void removeLinkbetweenSectionElementAndPermissions(
      Vertex existingSectionElementNode)
  {
    Iterable<Edge> relationshipsToRemove = existingSectionElementNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
    for (Edge relationship : relationshipsToRemove) {
      Vertex permissionSettingNode = relationship.getVertex(Direction.OUT);
      permissionSettingNode.remove();
    }
  }
  
  public static void manageDataRules(Map klassADM, Vertex klassNode, String nodelLabel, String type)
      throws Exception
  {
    String entityRuleLink = null;
    if (type.equals("klass")) {
      entityRuleLink = RelationshipLabelConstants.HAS_KLASS_RULE_LINK;
    }
    else {
      entityRuleLink = RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK;
    }
    manageAddedDataRules(klassADM, klassNode, nodelLabel, entityRuleLink);
    manageDeletedDataRules(klassADM, klassNode, nodelLabel, entityRuleLink);
  }
  
  public static void manageAddedDataRules(Map<String, Object> klassADM, Vertex klassNode,
      String nodelLabel, String entityRuleLink) throws Exception
  {
    OrientGraph graphDb = UtilClass.getGraph();
    List<String> addedDataRules = (List<String>) klassADM.remove("addedDataRules");
    for (String dataRule : addedDataRules) {
      Boolean relationshipFound = false;
      try {
        Vertex allowedChildNode = UtilClass.getVertexByIndexedId(dataRule, nodelLabel);
        Iterable<Edge> allowedTypesRelationships = klassNode.getEdges(Direction.OUT,
            entityRuleLink);
        for (Edge allowedTypeRelationship : allowedTypesRelationships) {
          Vertex otherNode = allowedTypeRelationship.getVertex(Direction.IN);
          if (otherNode.getProperty(CommonConstants.CODE_PROPERTY)
              .equals(dataRule)) {
            relationshipFound = true;
            break;
          }
        }
        if (!relationshipFound) {
          klassNode.addEdge(entityRuleLink, allowedChildNode);
        }
      }
      catch (NotFoundException e) {
        throw new NotFoundException();
      }
    }
  }
  
  public static void manageDeletedDataRules(Map klassADM, Vertex klassNode, String nodelLabel,
      String entityRuleLink) throws Exception
  {
    List<String> deletedAllowedTypes = (List<String>) klassADM.remove("deletedDataRules");
    for (String allowedType : deletedAllowedTypes) {
      Iterable<Edge> allowedTypesRelationships = klassNode.getEdges(Direction.OUT, entityRuleLink);
      for (Edge allowedTypeRelationship : allowedTypesRelationships) {
        Vertex otherNode = allowedTypeRelationship.getVertex(Direction.IN);
        if (otherNode.getProperty(CommonConstants.CODE_PROPERTY)
            .equals(allowedType)) {
          allowedTypeRelationship.remove();
          break;
        }
      }
    }
  }
  
  public static void deleteAllowedTypeRelationships(Vertex klassNode)
  {
    Iterable<Edge> allowedTypeRelationships = klassNode.getEdges(Direction.BOTH,
        RelationshipLabelConstants.ALLOWED_TYPES);
    for (Edge relationship : allowedTypeRelationships) {
      relationship.remove();
    }
  }
  
  public static void addAllowedTypes(Vertex klassNode, Map<String, Object> klassEntityMap)
  {
    List<String> allowedTypes = getAllowedTypes(klassNode);
    klassEntityMap.put(CommonConstants.ALLOWED_TYPES_PROPERTY, allowedTypes);
  }
  
  public static void addDataRules(Vertex klassNode, Map<String, Object> klassEntityMap)
  {
    List<String> allowedTypes = getDataRules(klassNode);
    klassEntityMap.put(CommonConstants.DATA_RULES, allowedTypes);
  }
  
  public static List<String> getAllowedTypes(Vertex klassNode)
  {
    List<String> allowedTypes = new ArrayList<String>();
    Iterable<Edge> allowedTypesrelationships = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.ALLOWED_TYPES);
    for (Edge allowedTypesrelationship : allowedTypesrelationships) {
      Vertex otherNode = allowedTypesrelationship.getVertex(Direction.IN);
      allowedTypes.add((String) otherNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return allowedTypes;
  }
  
  public static List<String> getDataRules(Vertex klassNode)
  {
    List<String> dataRules = new ArrayList<String>();
    Iterable<Edge> dataRulesrelationships = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.DATA_RULES);
    for (Edge allowedTypesrelationship : dataRulesrelationships) {
      Vertex otherNode = allowedTypesrelationship.getVertex(Direction.IN);
      dataRules.add((String) otherNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return dataRules;
  }
  
  public static Vertex createKlassNode(Map<String, Object> klassMap, OrientVertexType vertexType,
      List<String> propertiesToExclude) throws Exception
  {
    Vertex klassNode = null;
    if (klassMap.get(CommonConstants.ID_PROPERTY) != null) {
      String klassId = klassMap.get(CommonConstants.ID_PROPERTY)
          .toString();
      klassNode = UtilClass.createNode(klassMap, vertexType, propertiesToExclude);
      klassNode.setProperty(CommonConstants.CODE_PROPERTY, klassId);
    }
    else {
      klassNode = UtilClass.createNode(klassMap, vertexType, propertiesToExclude);
    }
    
    HashMap<String, Object> validatorMap = (HashMap<String, Object>) klassMap
        .get(CommonConstants.VALIDATOR_PROPERTY);
    OrientVertexType vType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_KLASS_VALIDATOR, CommonConstants.CODE_PROPERTY);
    Vertex validatorNode = UtilClass.createNode(validatorMap, vType, new ArrayList<>());
    validatorNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_VALIDATOR_OF, klassNode);
    return klassNode;
  }
  
  public static void createParentChildLink(Vertex klassNode, String vertexType,
      Map<String, Object> klassMap, boolean shouldInherit) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> parentKlassMap = (Map<String, Object>) klassMap
        .get(CommonConstants.PARENT_PROPERTY);
    if ((parentKlassMap != null)) {
      String parentId = (String) parentKlassMap.get(CommonConstants.ID_PROPERTY);
      if (!parentId.equals("-1")) {
        try {
          Vertex parentNode = UtilClass.getVertexByIndexedId(parentId, vertexType);
          klassNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF, parentNode);
          if (shouldInherit) {
            KlassUtils.inheritParentKlassData(klassNode, parentNode);
          }
        }
        catch (NotFoundException e) {
          throw new NotFoundException();
        }
      }
    }
  }
  
  public static void createTaxonomy(Map<String, Object> klassmap, Vertex klassNode,
      String klassVertexType) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    Map<String, Object> parentKlassMap = (Map<String, Object>) klassmap.get("parent");
    
    if ((parentKlassMap != null)) {
      String parentId = (String) parentKlassMap.get(CommonConstants.ID_PROPERTY);
      
      if (parentId.equals("-1")) {
        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.ENTITY_TAXONOMY_SETTING, CommonConstants.CODE_PROPERTY);
        Vertex taxonomyNode = UtilClass.createNode(new HashMap<String, Object>(), vertexType);
        boolean isEnforcedTaxonomy = (boolean) klassmap
            .get(CommonConstants.IS_ENFORCED_TAXONOMY_PROPERTY);
        taxonomyNode.setProperty(CommonConstants.IS_ENFORCED_TAXONOMY_PROPERTY, isEnforcedTaxonomy);
        taxonomyNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF,
            klassNode);
      }
      else if (!parentId.equals("-1")) {
        try {
          Vertex parentNode = UtilClass.getVertexByIndexedId(parentId, klassVertexType);
          Iterator<Edge> edgeIterator = parentNode
              .getEdges(Direction.IN,
                  RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF)
              .iterator();
          Edge taxonomySettingOf = null;
          while (edgeIterator.hasNext()) {
            taxonomySettingOf = edgeIterator.next();
            Vertex taxonomyNode = taxonomySettingOf.getVertex(Direction.OUT);
            taxonomyNode.addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_TAXONOMY_SETTING_OF,
                klassNode);
          }
        }
        catch (NotFoundException e) {
          throw new NotFoundException();
        }
      }
    }
  }
  
  public static void createSectionNodes(Map<String, Object> klassMap, Vertex klassNode,
      String vertexLabel) throws Exception
  {
    List<HashMap<String, Object>> sections = (List<HashMap<String, Object>>) klassMap
        .get(CommonConstants.SECTIONS_PROPERTY);
    if (sections != null) {
      List<Vertex> klassAndChildNodes = new ArrayList<>();
      klassAndChildNodes.add(klassNode);
      createAndLinkSectionsToKlass(klassNode, sections, klassAndChildNodes, new ArrayList<String>(),
          new ArrayList<>(), vertexLabel, new ArrayList<String>());
    }
  }
  
  public static void addGlobalPermission(Vertex klassNode, Map<String, Object> klassMap)
      throws Exception
  {
    klassMap.put(CommonConstants.GLOBAL_PERMISSION_PROPERTY, new HashMap<String, Object>());
  }
  
  // forRoleNode: get default permission for forRoleNode
  public static Map<String, Object> getSectionPermissionForKlass(Vertex forKlassNode,
      Vertex forRoleNode) throws Exception
  {
    String forRoleNodeId = forRoleNode.getProperty(CommonConstants.CODE_PROPERTY);
    Map<String, Object> klassPermission = new HashMap<>();
    Map<String, Object> rolePermissionMap = new HashMap<String, Object>();
    klassPermission.put(CommonConstants.ROLE_PERMISSION_PROPERTY, rolePermissionMap);
    Iterable<Vertex> klassSectionNodes = forKlassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Vertex klassSectionNode : klassSectionNodes) {
      String sectionId = klassSectionNode.getProperty(CommonConstants.CODE_PROPERTY);
      Iterable<Vertex> sectionPermissionNodes = klassSectionNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
      
      Boolean isSectionPermissionFound = false;
      for (Vertex sectionPermissionNode : sectionPermissionNodes) {
        Vertex roleNode = UtilClass.getRoleFromSectionPermissionNode(sectionPermissionNode);
        if (roleNode.getProperty(CommonConstants.CODE_PROPERTY)
            .equals(forRoleNodeId)) {
          isSectionPermissionFound = true;
        }
        Map<String, Object> sectionPermission = UtilClass.getMapFromNode(sectionPermissionNode);
        sectionPermission.put(CommonConstants.DISABLED_ELEMENTS_PROPERTY, new ArrayList<String>());
        Map<String, Object> rolePermission = (Map<String, Object>) rolePermissionMap
            .get(roleNode.getProperty(CommonConstants.CODE_PROPERTY));
        if (rolePermission == null) {
          rolePermission = new HashMap<String, Object>();
          Map<String, Object> sectionPermissionMap = new HashMap<String, Object>();
          sectionPermissionMap.put(sectionId, sectionPermission);
          rolePermission.put(CommonConstants.SECTION_PERMISSION_PROPERTY, sectionPermissionMap);
          rolePermissionMap.put(roleNode.getProperty(CommonConstants.CODE_PROPERTY),
              rolePermission);
        }
        else {
          Map<String, Object> sectionPermissionMap = (Map<String, Object>) rolePermission
              .get(CommonConstants.SECTION_PERMISSION_PROPERTY);
          sectionPermissionMap.put(sectionId, sectionPermission);
        }
      }
      
      if (!isSectionPermissionFound) {
        Map<String, Object> rolePermission = (Map<String, Object>) rolePermissionMap
            .get(forRoleNodeId);
        Map<String, Object> sectionPermission;
        Map<String, Object> sectionPermissionMap;
        if (rolePermission == null) {
          rolePermission = new HashMap<>();
          sectionPermissionMap = new HashMap<String, Object>();
          sectionPermission = new HashMap<>();
          sectionPermissionMap.put(sectionId, sectionPermission);
          rolePermission.put(CommonConstants.SECTION_PERMISSION_PROPERTY, sectionPermissionMap);
          rolePermissionMap.put(forRoleNodeId, rolePermission);
        }
        sectionPermissionMap = (Map<String, Object>) rolePermission
            .get(CommonConstants.SECTION_PERMISSION_PROPERTY);
        sectionPermission = (Map<String, Object>) sectionPermissionMap.get(sectionId);
        if (sectionPermission == null) {
          sectionPermission = new HashMap<>();
          sectionPermissionMap.put(sectionId, sectionPermission);
        }
        sectionPermission.put(CommonConstants.IS_COLLAPSED_PROPERTY, false);
        sectionPermission.put(CommonConstants.IS_HIDDEN_PROPERTY, false);
        sectionPermission.put(CommonConstants.DISABLED_ELEMENTS_PROPERTY, new ArrayList<String>());
      }
      
      Iterable<Vertex> sectionElementPositionNodes = klassSectionNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_POSITION_OF);
      for (Vertex sectionElementPositionNode : sectionElementPositionNodes) {
        Vertex sectionElementNode = UtilClass.getSectionElement(sectionElementPositionNode,
            forKlassNode);
        if (sectionElementNode == null) {
          continue;
        }
        
        Boolean isSectionElementPermissionFound = false;
        Iterable<Vertex> sectionElementPermissionNodes = sectionElementNode
            .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR);
        Iterator<Vertex> iterator = sectionElementPermissionNodes.iterator();
        
        while (iterator.hasNext()) {
          Vertex sectionElementPermissionNode = iterator.next();
          Boolean isDisabled = (Boolean) sectionElementPermissionNode
              .getProperty(CommonConstants.IS_DISABLED_PROPERTY);
          if (!isDisabled) {
            continue;
          }
          
          Vertex roleNode = UtilClass
              .getRoleFromSectionElementPermissionNode(sectionElementPermissionNode);
          if (roleNode.getProperty(CommonConstants.CODE_PROPERTY)
              .equals(forRoleNodeId)) {
            isSectionElementPermissionFound = true;
          }
          
          Map<String, Object> klassRolePermissionMap = (Map<String, Object>) klassPermission
              .get(CommonConstants.ROLE_PERMISSION_PROPERTY);
          Map<String, Object> rolePermission = (Map<String, Object>) klassRolePermissionMap
              .get(roleNode.getProperty(CommonConstants.CODE_PROPERTY));
          Map<String, Object> sectionPermissionMap = (Map<String, Object>) rolePermission
              .get(CommonConstants.SECTION_PERMISSION_PROPERTY);
          Map<String, Object> sectionPermission = (Map<String, Object>) sectionPermissionMap
              .get(sectionId);
          List<String> disabledElements = (List<String>) sectionPermission
              .get(CommonConstants.DISABLED_ELEMENTS_PROPERTY);
          disabledElements.add(sectionElementNode.getProperty(CommonConstants.CODE_PROPERTY));
        }
        
        if (!isSectionElementPermissionFound) {
          Map<String, Object> klassRolePermissionMap = (Map<String, Object>) klassPermission
              .get(CommonConstants.ROLE_PERMISSION_PROPERTY);
          Map<String, Object> rolePermission = (Map<String, Object>) klassRolePermissionMap
              .get(forRoleNodeId);
          Map<String, Object> sectionPermissionMap = (Map<String, Object>) rolePermission
              .get(CommonConstants.SECTION_PERMISSION_PROPERTY);
          Map<String, Object> sectionPermission = (Map<String, Object>) sectionPermissionMap
              .get(sectionId);
          List<String> disabledElements = (List<String>) sectionPermission
              .get(CommonConstants.DISABLED_ELEMENTS_PROPERTY);
          disabledElements.add(sectionElementNode.getProperty(CommonConstants.CODE_PROPERTY));
        }
      }
    }
    return klassPermission;
  }
  
  public static Edge getHasKlassPropertyEdgeFromKlassPropertyAndKLass(Vertex modifiedSectionElement,
      String klassId) throws Exception
  {
    Iterable<Edge> edges = modifiedSectionElement.getEdges(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    Iterator<Edge> iterator = edges.iterator();
    while (iterator.hasNext()) {
      Edge edge = iterator.next();
      Vertex otherKlassNode = edge.getVertex(Direction.OUT);
      if (otherKlassNode.getProperty(CommonConstants.CODE_PROPERTY)
          .equals(klassId)) {
        return edge;
      }
    }
    return null;
  }
  
  public static Vertex getPropertyNodeFromKlassProperty(Vertex modifiedSectionElement)
      throws Exception
  {
    Iterator<Vertex> properties = modifiedSectionElement
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    Vertex propertyNode = null;
    if (!properties.hasNext()) {
      throw new NotFoundException();
    }
    propertyNode = properties.next();
    
    if (properties.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    return propertyNode;
  }
  
  public static Edge getSectionOfEdgeFromKlassAndSection(Vertex klassNode, Vertex sectionNode)
      throws Exception
  {
    String query = "SELECT FROM " + RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF
        + " where in = " + klassNode.getId() + " and out = " + sectionNode.getId();
    
    Iterable<Edge> edges = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Edge> iterator = edges.iterator();
    Edge sectionOfEdge = null;
    
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    
    sectionOfEdge = iterator.next();
    if (iterator.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    
    return sectionOfEdge;
  }
  
  public static Edge getPermissionForEdgeFromKlassPropertyRoleIdAndKlassId(
      Vertex modifiedSectionElement, String klassId, String roleId) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    // Commit needed for newly created edge during concurrent save of section
    // Element
    // and Section Element Permission
    // graph.commit();
    String query = "SELECT FROM Permission_For WHERE in = " + modifiedSectionElement.getId()
        + " and roleId = " + "\"" + roleId + "\"" + " and utilizingKlassIds contains " + "\""
        + klassId + "\"";
    
    Iterable<Edge> edges = graph.command(new OCommandSQL(query))
        .execute();
    Iterator<Edge> iterator = edges.iterator();
    Edge permissionFor = null;
    if (iterator.hasNext()) {
      permissionFor = iterator.next();
    }
    
    if (iterator.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    
    return permissionFor;
  }
  
  public static Edge getPermissionForEdgeFromSectionRoleIdAndKlassId(Vertex section, String klassId,
      String roleId) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String query = "SELECT FROM Permission_For WHERE in = " + section.getId() + " and roleId = "
        + "\"" + roleId + "\"" + " and utilizingKlassIds contains " + "\"" + klassId + "\"";
    
    Iterable<Edge> edges = graph.command(new OCommandSQL(query))
        .execute();
    Iterator<Edge> iterator = edges.iterator();
    Edge permissionFor = null;
    if (iterator.hasNext()) {
      permissionFor = iterator.next();
    }
    
    if (iterator.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    
    return permissionFor;
  }
  
  /*  public static void updateSequenceOfSectionNodes(Vertex sectionNode, String klassId,
      List<String> selfAndChildKlassIds, Set<Edge> edgesToDelete) throws Exception
  {
    Iterator<Edge> previousNodesEdges = sectionNode.getEdges(Direction.OUT, RelationshipLabelConstants.PREVIOUS_SECTION).iterator();
    Iterator<Edge> nextNodesEdges = sectionNode.getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION).iterator();
    List<Edge> previousSectionEdges = new ArrayList<Edge>();
    List<Edge> nextSectionEdges = new ArrayList<Edge>();
    while(previousNodesEdges.hasNext()){
      Edge previousNodeEdge = previousNodesEdges.next();
      List<String> utilizingKlassIds = previousNodeEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      for (String ids : selfAndChildKlassIds) {
        if(utilizingKlassIds.contains(ids)) {
          previousSectionEdges.add(previousNodeEdge);
          break;
        }
       }
    }
    while(nextNodesEdges.hasNext()){
      Edge nextNodeEdge = nextNodesEdges.next();
      List<String> utilizingKlassIds = nextNodeEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      for (String ids : selfAndChildKlassIds) {
       if(utilizingKlassIds.contains(ids)) {
         nextSectionEdges.add(nextNodeEdge);
         break;
       }
      }
    }
    if(nextSectionEdges.size() ==0 || previousSectionEdges.size() == 0){
      for(Edge previousSectionEdge :previousSectionEdges){
        List<String> previousUtilizingKlassIds = previousSectionEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        List<String> copyOfPreviousUtilizingKlassIds = new ArrayList<>(previousUtilizingKlassIds);
        copyOfPreviousUtilizingKlassIds.removeAll(selfAndChildKlassIds);
        if (copyOfPreviousUtilizingKlassIds.size() == 0) {
          previousSectionEdge.remove();
        }
        else {
          previousSectionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
              copyOfPreviousUtilizingKlassIds);
        }
      }
      for (Edge nextSectionEdge : nextSectionEdges) {
        List<String> nextUtilizingKlassIds = nextSectionEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
          nextUtilizingKlassIds.removeAll(selfAndChildKlassIds);
        if (nextUtilizingKlassIds.size() == 0) {
          nextSectionEdge.remove();
        }
        else {
          nextSectionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
              nextUtilizingKlassIds);
        }
      }
  
      Iterable<Edge> sectionOfEdges = sectionNode.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
      for (Edge sectionOfEdge : sectionOfEdges) {
        Vertex klassNode = sectionOfEdge.getVertex(Direction.IN);
        String usableKlassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
        if(selfAndChildKlassIds.contains(usableKlassId)) {
          edgesToDelete.add(sectionOfEdge);
          //sectionOfEdge.remove();
          break;
        }
      }
      return;
    }
  
    Set<Edge> relationshipToDelete = new HashSet<>();
    for (Edge nextSectionEdge : nextSectionEdges) {
      List<String> utilizingKlassIdsNextEdge = nextSectionEdge
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      List<String> copyOfUtilizingKlassIdsNextEdge = new ArrayList<String>();
      copyOfUtilizingKlassIdsNextEdge.addAll(utilizingKlassIdsNextEdge);
      utilizingKlassIdsNextEdge.retainAll(selfAndChildKlassIds);
      Vertex nextNode = nextSectionEdge.getVertex(Direction.OUT);
      for (Edge previousSectionEdge : previousSectionEdges) {
        List<String> utilizingKlassIdsPreviousEdge = previousSectionEdge
            .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        List<String> copyOfUtilizingKlassIdsPreviousEdge = new ArrayList<String>();
        List<String> copyOfUtilizingKlassIdsOfPreviousEdge = new ArrayList<String>(utilizingKlassIdsPreviousEdge);
        copyOfUtilizingKlassIdsPreviousEdge.addAll(utilizingKlassIdsPreviousEdge);
        Vertex previousNode = previousSectionEdge.getVertex(Direction.IN);
        copyOfUtilizingKlassIdsOfPreviousEdge.retainAll(utilizingKlassIdsNextEdge);
  
  
        // update previousSectionOf edge utilizing klass ids
  
        copyOfUtilizingKlassIdsPreviousEdge.removeAll(selfAndChildKlassIds);
        if(copyOfUtilizingKlassIdsPreviousEdge.size()!=0){
          previousSectionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
              copyOfUtilizingKlassIdsPreviousEdge);
        }
        else{
          relationshipToDelete.add(previousSectionEdge);
        }
        String owningKlassId = getOwningKlassId(previousNode, nextNode,
            copyOfUtilizingKlassIdsOfPreviousEdge, klassId);
        if (owningKlassId == null) {
          continue;
        }
        Edge previousSectionOf = nextNode.addEdge(RelationshipLabelConstants.PREVIOUS_SECTION,
            previousNode);
        previousSectionOf.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
            copyOfUtilizingKlassIdsOfPreviousEdge);
        previousSectionOf.setProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY, owningKlassId);
        Iterable<Edge> sectionOfEdges = sectionNode.getEdges(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
        for (Edge sectionOf : sectionOfEdges) {
          Vertex klassNode = sectionOf.getVertex(Direction.IN);
          String otherKlassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
          if(copyOfUtilizingKlassIdsOfPreviousEdge.contains(otherKlassId)){
            //sectionOf.remove();
            edgesToDelete.add(sectionOf);
          }
        }
      }
      copyOfUtilizingKlassIdsNextEdge.removeAll(selfAndChildKlassIds);
      if(copyOfUtilizingKlassIdsNextEdge.size()!=0){
        nextSectionEdge.setProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY,
            copyOfUtilizingKlassIdsNextEdge);
      }
      else{
        relationshipToDelete.add(nextSectionEdge);
      }
    }
    for (Edge edge : relationshipToDelete) {
      edge.remove();
    }
  }*/
  
  private static String getOwningKlassId(Vertex previousNode, Vertex nextNode,
      List<String> utilizingKlassIds, String removingKlassId)
  {
    if (utilizingKlassIds.size() == 0) {
      return null;
    }
    Iterable<Edge> iterable = previousNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Edge edge : iterable) {
      Vertex vertex = edge.getVertex(Direction.IN);
      Boolean isInheriting = edge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      String klassId = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      if (utilizingKlassIds.contains(klassId) && !isInheriting) {
        return klassId;
      }
    }
    iterable = nextNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Edge edge : iterable) {
      Vertex vertex = edge.getVertex(Direction.IN);
      Boolean isInheriting = edge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      String klassId = vertex.getProperty(CommonConstants.CODE_PROPERTY);
      if (utilizingKlassIds.contains(klassId) && !isInheriting) {
        return klassId;
      }
    }
    return removingKlassId;
  }
  
  public static void manageVariantContext(Map<String, Object> klassADM, Vertex klassNode,
      String nodelLabel) throws Exception
  {
    manageAddedVariantContexts(klassADM, klassNode, nodelLabel);
    manageDeletedVariantContexts(klassADM, klassNode, nodelLabel);
  }
  
  public static void manageAddedVariantContexts(Map klassADM, Vertex klassNode, String nodelLabel)
      throws Exception
  {
    Map<String, Object> addedContexts = (Map<String, Object>) klassADM
        .remove(IKlassSaveModel.ADDED_CONTEXTS);
    if (addedContexts != null) {
      for (String variantType : addedContexts.keySet()) {
        for (String contextId : (List<String>) addedContexts.get(variantType)) {
          addedVariantContexts(klassNode, nodelLabel, contextId);
        }
      }
    }
  }
  
  public static void addedVariantContexts(Vertex klassNode, String nodelLabel, String contextId)
      throws Exception
  {
    Boolean relationshipFound = false;
    try {
      Vertex allowedContextNode = UtilClass.getVertexByIndexedId(contextId, nodelLabel);
      Iterable<Edge> variantContextEdges = klassNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      for (Edge variantContextEdge : variantContextEdges) {
        Vertex variantContextNode = variantContextEdge.getVertex(Direction.IN);
        if (variantContextNode.getProperty(CommonConstants.CODE_PROPERTY)
            .equals(contextId)) {
          relationshipFound = true;
          break;
        }
      }
      
      if (!relationshipFound) {
        klassNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, allowedContextNode);
      }
    }
    catch (NotFoundException e) {
      throw new NotFoundException();
    }
  }
  
  @SuppressWarnings("rawtypes")
  public static void manageDeletedVariantContexts(Map klassADM, Vertex klassNode, String nodelLabel)
      throws Exception
  {
    Map<String, Object> deletedVariantContexts = (Map<String, Object>) klassADM
        .remove(IKlassSaveModel.DELETED_CONTEXTS);
    if (deletedVariantContexts != null) {
      for (String variantType : deletedVariantContexts.keySet()) {
        for (String deletedVariantContextId : (List<String>) deletedVariantContexts
            .get(variantType)) {
          deletedVariantContexts(klassNode, deletedVariantContextId);
        }
      }
    }
  }
  
  public static void deletedVariantContexts(Vertex klassNode, String deletedVariantContextId)
  {
    Iterable<Edge> variantContextEdges = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.VARIANT_CONTEXT_OF);
    for (Edge variantContextEdge : variantContextEdges) {
      Vertex otherNode = variantContextEdge.getVertex(Direction.IN);
      if (otherNode.getProperty(CommonConstants.CODE_PROPERTY)
          .equals(deletedVariantContextId)) {
        variantContextEdge.remove();
        break;
      }
    }
  }
  
  public static void addVariantContexts(Vertex klassNode, Map<String, Object> klassEntityMap)
  {
    Map<String, Object> contexts = getVariantContexts(klassNode);
    klassEntityMap.put(IKlass.CONTEXTS, contexts);
  }
  
  private static Map<String, Object> getVariantContexts(Vertex klassNode)
  {
    List<String> contextualvariants = new ArrayList<String>();
    List<String> productvariants = new ArrayList<String>();
    List<String> languagevariants = new ArrayList<String>();
    List<String> promotionalvariants = new ArrayList<String>();
    
    Iterable<Edge> dataRulesrelationships = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.VARIANT_CONTEXT_OF);
    for (Edge allowedTypesrelationship : dataRulesrelationships) {
      Vertex otherNode = allowedTypesrelationship.getVertex(Direction.IN);
      switch ((String) otherNode.getProperty(IVariantContext.TYPE)) {
        case CommonConstants.IMAGE_VARIANT:
        case CommonConstants.CONTEXTUAL_VARIANT:
        case CommonConstants.PROMOTION_CONTEXT:
          contextualvariants.add((String) otherNode.getProperty(CommonConstants.CODE_PROPERTY));
          break;
        /*case CommonConstants.PRODUCT_VARIANT:
        productvariants.add((String) otherNode.getProperty(CommonConstants.CODE_PROPERTY));
        break;*/
        case CommonConstants.LANGUAGE_VARIANT:
          languagevariants.add((String) otherNode.getProperty(CommonConstants.CODE_PROPERTY));
          break;
        case CommonConstants.PROMOTIONAL_VERSION:
          promotionalvariants.add((String) otherNode.getProperty(CommonConstants.CODE_PROPERTY));
          break;
      }
    }
    Map<String, Object> variantContexts = new HashMap<>();
    variantContexts.put(IKlassContext.EMBEDDED_VARIANT_CONTEXTS, contextualvariants);
    variantContexts.put(IKlassContext.LANGUAGE_VARIANT_CONTEXTS, languagevariants);
    variantContexts.put(IKlassContext.PRODUCT_VARIANT_CONTEXTS, productvariants);
    variantContexts.put(IKlassContext.PROMOTIONAL_VERSION_CONTEXTS, promotionalvariants);
    return variantContexts;
  }
  
  public static void addSectionsToKlassEntityMap(Vertex klassNode,
      Map<String, Object> klassEntityMap) throws Exception
  {
    addSectionsToKlassEntityMap(klassNode, klassEntityMap, true);
  }
  
  public static void addSectionsToKlassEntityMap(Vertex klassNode,
      Map<String, Object> klassEntityMap, Boolean shouldGetKP) throws Exception
  {
    
    Iterator<Vertex> sectionNodes = klassNode
        .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
        .iterator();
    
    addSectionsToKlassMap(klassNode, klassEntityMap, sectionNodes, shouldGetKP);
  }
  
  public static void addSectionsToKlassMap(Vertex klassNode, Map<String, Object> klassEntityMap,
      Iterator<Vertex> sectionNodes, Boolean shouldGetKP) throws Exception
  {
    List<Map<String, Object>> sectionsList = new ArrayList<>();
    klassEntityMap.put(IKlass.SECTIONS, sectionsList);
    
    String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    int sequence = 0;
    while (sectionNodes.hasNext()) {
      Vertex klassSectionNode = sectionNodes.next();
      Vertex propertyCollectionNode = getPropertyCollectionNodeFromKlassSectionNode(
          klassSectionNode);
      String sectionId = (String) klassSectionNode.getProperty(CommonConstants.CODE_PROPERTY);
      
      Edge sectionOf = SaveKlassUtil.getConnectingSectionOfEdge(klassNode, klassSectionNode);
      Boolean isSkipped = sectionOf.getProperty(ISection.IS_SKIPPED);
      Boolean isInherited = sectionOf.getProperty(ISection.IS_INHERITED);
      
      Map<String, Object> sectionMap = UtilClass.getMapFromVertex(new ArrayList<>(),
          propertyCollectionNode);
      sectionMap.put(ISection.ID, sectionId);
      sectionMap.put(ISection.IS_SKIPPED, isSkipped);
      sectionMap.put(ISection.IS_INHERITED, isInherited);
      sectionMap.put(ISection.PROPERTY_COLLECTION_ID,
          propertyCollectionNode.getProperty(CommonConstants.CODE_PROPERTY));
      sectionMap.put(ISection.ELEMENTS, new ArrayList<>());
      
      if (shouldGetKP == null || shouldGetKP) {
        addSectionElementsToSectionMap(klassNode, sectionMap, propertyCollectionNode, true);
      }
      sectionMap.put(ISection.SEQUENCE, sequence);
      sectionsList.add(sectionMap);
      sequence++;
    }
    Collections.sort(sectionsList, new SectionsSequenceComparator());
  }
  
  public static void addSectionsToKlassEntityMapForExport(Vertex klassNode,
      Map<String, Object> klassEntityMap) throws Exception
  {
    
    Iterator<Vertex> sectionNodes = klassNode
        .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
        .iterator();
    
    Map<String, Object> sectionElementMap = new HashMap<>();
    klassEntityMap.put(IKlass.SECTIONS, sectionElementMap);
    
    while (sectionNodes.hasNext()) {
      Vertex klassSectionNode = sectionNodes.next();
      Vertex propertyCollectionNode = getPropertyCollectionNodeFromKlassSectionNode(
          klassSectionNode);
      
      Map<String, Object> sectionElements = new HashMap<>();
      String pcID = klassSectionNode.getProperty(CommonConstants.CODE_PROPERTY);
      sectionElements.put(CommonConstants.ID_PROPERTY, pcID);
      addSectionElementsToSectionMap(klassNode, sectionElements, propertyCollectionNode, true);
      List<Map<String,Object>> elements = (List<Map<String, Object>>) sectionElements.get("elements");
      for(Map<String,Object> element : elements) {
        String type = (String) element.get(CommonConstants.TYPE_PROPERTY);
        Map<String,Object> property = new HashMap<>();
        if(type.equals(ISectionAttribute.ATTRIBUTE)) {
           property = (Map<String, Object>) element.get(ISectionAttribute.ATTRIBUTE);
           String attributeVariantContext = (String) element.get(ISectionAttribute.ATTRIBUTE_VARIANT_CONTEXT);
           element.put(ConfigTag.attributeVariantContextCode.name(), attributeVariantContext);
        }else if(type.equals(ISectionTag.TAG)) {
          property = (Map<String, Object>) element.get(ISectionTag.TAG);
          List<Map<String, Object>> defaultTagValue = (List<Map<String, Object>>) element.get(ISectionTag.DEFAULT_VALUE);
          if(defaultTagValue != null) {
            StringBuilder defaultValue = new StringBuilder();
            defaultTagValue.forEach(v -> defaultValue.append(v.get(IIdRelevance.TAGID)).append(","));
            if(defaultValue.length() >1)
              defaultValue.deleteCharAt(defaultValue.length()-1);
            element.put(ISectionTag.DEFAULT_VALUE, defaultValue.toString());
          }
          
          List<Map<String,Object>> selectedTagValues = (List<Map<String, Object>>) element.get(ISectionTag.SELECTED_TAG_VALUES);
          List<String> selectedTagValue = new ArrayList<>();
          if(selectedTagValues != null) {
            selectedTagValues.forEach(v -> selectedTagValue.add((String) v.get(IIdRelevance.TAGID)));
          }
          element.put(ISectionTag.SELECTED_TAG_VALUES, selectedTagValue);
        }
      
        String code = (String) element.get(ISectionAttribute.PROPERTY_ID);
        element.put(CommonConstants.CODE_PROPERTY, code);
       
      }
      sectionElementMap.put(propertyCollectionNode.getProperty(CommonConstants.CODE_PROPERTY),
          elements);
    }
  }

  public static void removeParentChildLink(Vertex klassNode, Map<String, Edge> parentEdges)
  {
    if (parentEdges.isEmpty()) {
      return;
    }
    for (Map.Entry<String, Edge> parentEdge : parentEdges.entrySet()) {
      parentEdge.getValue().remove();
    }
    Iterable<Edge> edges = klassNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Edge edge : edges) {
      boolean isInherited = edge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if (isInherited) {
        edge.remove();
      }
    }

    Iterable<Edge> propertyCollections = klassNode.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Edge propertyCollection : propertyCollections) {
      Boolean isInherited = propertyCollection.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if (isInherited) {
        propertyCollection.remove();
      }
    }
  }

  static class SectionsSequenceComparator implements Comparator<Map<String, Object>> {
    
    @Override
    public int compare(Map<String, Object> section1, Map<String, Object> section2)
    {
      Integer section1Sequence = (Integer) section1.get(CommonConstants.SEQUENCE);
      Integer section2Sequence = (Integer) section2.get(CommonConstants.SEQUENCE);
      return section1Sequence - section2Sequence;
    }
  }
  
  public static void addSectionElementsToSectionMap(Vertex klassNode,
      Map<String, Object> sectionMap, Vertex propertyCollectionNode, Boolean isRefNedded)
      throws Exception
  {
    List<Map<String, Object>> elementsList = new ArrayList<>();
    sectionMap.put("elements", elementsList);

    Iterable<Edge> entityToRelationships = propertyCollectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
    for (Edge entityTo : entityToRelationships) {
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      String entityType = entityTo.getProperty(CommonConstants.TYPE);
      Map<String, Object> sectionElementMap = new HashMap<>();
      
      Vertex klassPropertyNode = null;
      /*      if (entityType.equals(CommonConstants.RELATIONSHIP)) {
      klassPropertyNode = getRespectiveRelationshipKlassPropertyNode(klassNode, entityNode,
          sectionMap.get(CommonConstants.ID_PROPERTY)
              .toString());
      sectionMap.put(CommonConstants.TYPE_PROPERTY, CommonConstants.RELATIONSHIP_SECTION_TYPE);
          }
          else {*/
      klassPropertyNode = getRespectiveKlassPropertyNode(klassNode, entityNode);
      sectionMap.put(ISection.TYPE, CommonConstants.SECTION_TYPE);
      // }
      Boolean isInherited = false;
      Boolean found = false;
      if (klassPropertyNode != null) {
        String sectionId = (String) sectionMap.get(ISection.ID);
        Iterator<Edge> hasKlassPropertyEdges = klassPropertyNode
            .getEdges(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY)
            .iterator();
        Edge hasKlassProperty = null;
        while (hasKlassPropertyEdges.hasNext()) {
          hasKlassProperty = hasKlassPropertyEdges.next();
          Vertex klassVertex = hasKlassProperty.getVertex(Direction.OUT);
          if (klassVertex.equals(klassNode)) {
            List<String> utilizingSectionIds = hasKlassProperty
                .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
            if (utilizingSectionIds.contains(sectionId)) {
              sectionElementMap
                  .putAll(UtilClass.getMapFromVertex(new ArrayList<>(), klassPropertyNode));
              sectionElementMap.put("isDeleted", false);
              sectionElementMap.remove("tagGroups"); // remove tag groups from
              
              // klass property node
              Edge klassPropertyEdge = PropertyCollectionUtils
                  .getLinkBetweenKlassAndKlassProperty(klassNode, klassPropertyNode);
              isInherited = klassPropertyEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
              found = true;
            }
            break;
          }
        }
      }
      if (!found) {
        fillDefaultSectionElementMap(sectionElementMap);
        sectionElementMap.put("isDeleted", true);
      }
      if (entityType.equals(CommonConstants.RELATIONSHIP)
          && sectionElementMap.get(CommonConstants.RELATIONSHIP_SIDE_PROPERTY) == null) {
        continue;
      }
      
      if (found && entityType.equals(CommonConstants.TAG_PROPERTY)) {
        List<Map<String, Object>> defaultTagValues = getDefaultTagValuesOfKlassPropertyNode(
            klassPropertyNode);
        sectionElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
        List<Map<String, Object>> selectedTagValues = getSelectedTagValuesOfKlassPropertyNode(
            klassPropertyNode);
        sectionElementMap.put(ISectionTag.SELECTED_TAG_VALUES, selectedTagValues);
      }
      
      if (found && entityType.equals(CommonConstants.ATTRIBUTE)) {
        Map<String, Object> context = getAttributeContextMap(klassPropertyNode);
        sectionElementMap.put(ISectionAttribute.CONTEXT, context);
        String attributeVariantContextId = getAttributeVariantContextId(klassPropertyNode);
        sectionElementMap.put(ISectionAttribute.ATTRIBUTE_VARIANT_CONTEXT,
            attributeVariantContextId);
        Boolean isIdentifier = klassPropertyNode.getProperty(ISectionAttribute.IS_IDENTIFIER);
        sectionElementMap.put(ISectionAttribute.IS_IDENTIFIER, isIdentifier);
      }
      
      sectionElementMap.put(CommonConstants.TYPE, entityType);
      //sectionElementMap.put(ISectionElement.CODE, entityNode.getProperty(ISectionElement.CODE));
      if (isRefNedded) {
        addEntityDetailsToSectionElementMap(sectionElementMap, entityNode, entityType);
      }


      sectionElementMap.put(ISectionElement.IS_INHERITED, isInherited);
      if (isInherited) {
        sectionElementMap.put(ISectionElement.IS_CUT_OFF, false);
      }
      
      elementsList.add(sectionElementMap);
    }
    
    List<String> propertySequenceIds = propertyCollectionNode.getProperty(CommonConstants.PROPERTY_SEQUENCE_IDS);
    PropertyCollectionUtils.sortPropertyCollectionList(elementsList, propertySequenceIds, CommonConstants.PROPERTY_ID);
  }
  
  private static String getAttributeVariantContextId(Vertex klassPropertyNode) throws Exception
  {
    String attributeVariantContextId = null;
    Iterator<Vertex> attributeVariantContextIterator = klassPropertyNode
        .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (attributeVariantContextIterator.hasNext()) {
      Vertex attributeVariantContextNode = attributeVariantContextIterator.next();
      attributeVariantContextId = UtilClass.getCodeNew(attributeVariantContextNode);
    }
    if (attributeVariantContextIterator.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    return attributeVariantContextId;
  }
  
  public static List<Map<String, Object>> getDefaultTagValuesOfKlassPropertyNode(
      Vertex klassPropertyNode)
  {
    Iterable<Edge> hasDefaultTagValues = klassPropertyNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE);
    List<Map<String, Object>> defaultTagValuesList = new ArrayList<>();
    for (Edge edge : hasDefaultTagValues) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      Integer relevance = edge.getProperty(CommonConstants.SORT_FIELD_RELEVANCE);
      String TagId = UtilClass.getCodeNew(tagValueNode);
      Map<String, Object> defaultTagValue = new HashMap<>();
      defaultTagValue.put(IIdRelevance.ID, UtilClass.getUniqueSequenceId(null));
      defaultTagValue.put(IIdRelevance.TAGID, TagId);
      defaultTagValue.put(IIdRelevance.RELEVANCE, relevance);
      defaultTagValuesList.add(defaultTagValue);
    }
    return defaultTagValuesList;
  }
  
  protected static void addEntityDetailsToSectionElementMap(Map<String, Object> sectionElementMap,
      Vertex elementEntityNode, String sectionElementType) throws Exception
  {
    Map<String, Object> elementEntityMap = null;
    switch (sectionElementType) {
      case SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE:
        elementEntityMap = AttributeUtils.getAttributeMap(elementEntityNode);
        
        fillSectionAttributeMapFromAttribute(sectionElementMap, elementEntityMap);
        
        break;
      case SystemLevelIds.PROPERTY_TYPE_TAG:
        elementEntityMap = TagUtils.getTagMap(elementEntityNode, false);
        String tagType = (String) sectionElementMap.get(ISectionTag.TAG_TYPE);
        Boolean isMultiselect = (Boolean) sectionElementMap.get(ISectionTag.IS_MULTI_SELECT);
        Boolean isVersionable = (Boolean) sectionElementMap.get(ISectionTag.IS_VERSIONABLE);
        
        if (tagType != null && !tagType.equals("")) {
          elementEntityMap.put(ITag.TAG_TYPE, tagType);
        }
        else {
          sectionElementMap.put(ISectionTag.TAG_TYPE, elementEntityMap.get(ITag.TAG_TYPE));
        }
        
        if (isMultiselect != null) {
          elementEntityMap.put(ITag.IS_MULTI_SELECT, isMultiselect);
        }
        else {
          sectionElementMap.put(ISectionTag.IS_MULTI_SELECT,
              elementEntityMap.get(ITag.IS_MULTI_SELECT));
        }
        
        if (isVersionable != null) {
          elementEntityMap.put(ITag.IS_VERSIONABLE, isVersionable);
        }
        else {
          sectionElementMap.put(ISectionTag.IS_VERSIONABLE,
              elementEntityMap.get(ITag.IS_VERSIONABLE));
        }
        
        break;
      case SystemLevelIds.PROPERTY_TYPE_ROLE:
        elementEntityMap = new HashMap<String, Object>();
        elementEntityMap.putAll(UtilClass.getMapFromNode(elementEntityNode));
        break;
      case SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP:
        elementEntityMap = RelationshipUtils.getRelationshipMapWithContext(elementEntityNode);
        break;
      case SystemLevelIds.PROPERTY_TYPE_TAXONOMY:
        elementEntityMap = new HashMap<String, Object>();
        elementEntityMap
            .putAll(UtilClass.getMapFromVertex(TAXONOMY_PROPERTIES_TO_FETCH, elementEntityNode));
        break;
      default:
        throw new NotFoundException();
    }
    
    sectionElementMap.put("type", sectionElementType);
    sectionElementMap.put(sectionElementType, elementEntityMap);
  }
  
  public static void fillSectionAttributeMapFromAttribute(Map<String, Object> sectionElementMap,
      Map<String, Object> elementEntityMap)
  {
    String defaultValue = (String) sectionElementMap.get(ISectionAttribute.DEFAULT_VALUE);
    if (defaultValue == null || defaultValue.equals("")) {
      sectionElementMap.put(ISectionAttribute.DEFAULT_VALUE, elementEntityMap.get("defaultValue"));
    }
    
    String valueAsHtml = (String) sectionElementMap.get(ISectionAttribute.VALUE_AS_HTML);
    if (valueAsHtml == null || valueAsHtml.equals("")) {
      sectionElementMap.put(ISectionAttribute.VALUE_AS_HTML, elementEntityMap.get("valueAsHtml"));
    }
    
    String defaultUnit = (String) sectionElementMap.get(ISectionAttribute.DEFAULT_UNIT);
    if (defaultUnit == null || defaultUnit.equals("")) {
      sectionElementMap.put(ISectionAttribute.DEFAULT_UNIT, elementEntityMap.get("defaultUnit"));
    }
    
    Integer precision = (Integer) sectionElementMap.get(ISectionAttribute.PRECISION);
    if (precision == null) {
      sectionElementMap.put(ISectionAttribute.PRECISION, elementEntityMap.get("precision"));
    }
    
    Boolean isVersionable = (Boolean) sectionElementMap.get(ISectionAttribute.IS_VERSIONABLE);
    if (isVersionable == null) {
      sectionElementMap.put(ISectionAttribute.IS_VERSIONABLE,
          elementEntityMap.get("isVersionable"));
    }
  }
  
  private static void fillDefaultSectionElementMap(Map<String, Object> sectionElementMap)
  {
    sectionElementMap.put(ISectionElement.TOOLTIP, "");
    sectionElementMap.put(ISectionElement.COUPLING_TYPE, CommonConstants.LOOSELY_COUPLED);
    sectionElementMap.put(ISectionElement.LABEL, "");
    sectionElementMap.put(ISectionElement.IS_VARIANT_ALLOWED, false);
    sectionElementMap.put(ISectionElement.IS_CUT_OFF, false);
    sectionElementMap.put(ISectionElement.IS_DISABLED, false);
    sectionElementMap.put(ISectionElement.IS_MANDATORY, false);
    sectionElementMap.put(ISectionElement.IS_SHOULD, false);
    sectionElementMap.put(ISectionElement.NUMBER_OF_VERSIONS_ALLOWED, 0);
    sectionElementMap.put(ISectionElement.ID, null);
  }
  
  public static Vertex getRespectiveKlassPropertyNode(Vertex klassNode, Vertex entityNode)
  {
    OrientGraph graph = UtilClass.getGraph();
    String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "select from (select expand(in('" + RelationshipLabelConstants.HAS_PROPERTY + "')) from " + entityNode.getId()
        + ") where in('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "') contains (code='" + klassId + "')";
    Iterable<Vertex> iterator = graph.command(new OCommandSQL(query))
        .execute();
    Vertex klassPropertyNode = null;
    for (Vertex vertex : iterator) {
      klassPropertyNode = vertex;
    }
    return klassPropertyNode;
  }
  
  public static void fillSectionElementPermissionAndNotification(Vertex klassNode,
      Map<String, Object> klassEntityMap)
  {
    
    Map<String, Object> klassPermission = (Map<String, Object>) klassEntityMap
        .get(IKlass.PERMISSIONS);
    Map<String, Object> rolePermissionMap = (Map<String, Object>) klassPermission
        .get(IKlassPermission.ROLE_PERMISSION);
    Map<String, Object> notificationSettingMap = (Map<String, Object>) klassEntityMap
        .get(IKlass.NOTIFICATION_SETTINGS);
    String klassId = (String) klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    Iterator<Edge> iterator = klassNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_KLASS_PROPERTY)
        .iterator();
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> i = graph
        .command(
            new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ROLE + " order by "
                + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    List<String> rolesIds = new ArrayList<>();
    for (Vertex roleNode : i) {
      rolesIds.add(roleNode.getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    while (iterator.hasNext()) {
      Edge hasKlassProperty = iterator.next();
      Vertex klassElementNode = hasKlassProperty.getVertex(Direction.IN);
      List<String> utilizingSectionIds = hasKlassProperty
          .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
      fillSectionElementPermission(rolePermissionMap, klassId, klassElementNode,
          utilizingSectionIds, rolesIds);
      fillSectionElementNotification(notificationSettingMap, klassId, klassElementNode, rolesIds);
    }
  }
  
  private static void fillSectionElementPermission(Map<String, Object> rolePermissionMap,
      String klassId, Vertex klassElementNode, List<String> utilizingSectionIds,
      List<String> roleIds)
  {
    Iterator<Edge> permissionForIterator = klassElementNode
        .getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR)
        .iterator();
    List<String> roleIdsForDefaultPermission = new ArrayList<>();
    roleIdsForDefaultPermission.addAll(roleIds);
    while (permissionForIterator.hasNext()) {
      Edge permissionFor = permissionForIterator.next();
      List<String> utilizingKlassIds = permissionFor
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if (utilizingKlassIds.contains(klassId)) {
        String roleId = permissionFor.getProperty(CommonConstants.ROLE_ID_PROPERY);
        roleIdsForDefaultPermission.remove(roleId);
        Map<String, Object> rolePermission = (Map<String, Object>) rolePermissionMap.get(roleId);
        
        if (rolePermission == null) {
          rolePermission = new HashMap<String, Object>();
          rolePermission.put(IRolePermission.SECTION_PERMISSION, new HashMap<String, Object>());
          rolePermissionMap.put(roleId, rolePermission);
        }
        
        Map<String, Object> sectionPermissionMap = (Map<String, Object>) rolePermission
            .get(IRolePermission.SECTION_PERMISSION);
        for (String sectionId : utilizingSectionIds) {
          Map<String, Object> sectionPermission = (Map<String, Object>) sectionPermissionMap
              .get(sectionId);
          if (sectionPermission == null) {
            sectionPermission = new HashMap<String, Object>();
            sectionPermission.put(ISectionPermission.DISABLED_ELEMENTS, new ArrayList<String>());
            sectionPermission.put(ISectionPermission.IS_COLLAPSED, false);
            sectionPermission.put(ISectionPermission.IS_HIDDEN, false);
            sectionPermissionMap.put(sectionId, sectionPermission);
          }
          
          Vertex sectionElementPermissionNode = permissionFor.getVertex(Direction.OUT);
          Boolean isDisabled = sectionElementPermissionNode
              .getProperty(CommonConstants.IS_DISABLED_PROPERTY);
          if (isDisabled) {
            List<String> disableElements = (List<String>) sectionPermission
                .get(ISectionPermission.DISABLED_ELEMENTS);
            disableElements.add(klassElementNode.getProperty(CommonConstants.CODE_PROPERTY));
          }
        }
      }
    }
    
    fillDefaultPermissionForSectionElement(rolePermissionMap, klassElementNode, utilizingSectionIds,
        roleIdsForDefaultPermission);
  }
  
  private static void fillDefaultPermissionForSectionElement(Map<String, Object> rolePermissionMap,
      Vertex klassElementNode, List<String> utilizingSectionIds, List<String> roleIds)
  {
    for (String roleId : roleIds) {
      Map<String, Object> rolePermission = (Map<String, Object>) rolePermissionMap.get(roleId);
      if (rolePermission == null) {
        rolePermission = new HashMap<String, Object>();
        rolePermission.put(IRolePermission.SECTION_PERMISSION, new HashMap<String, Object>());
        rolePermissionMap.put(roleId, rolePermission);
      }
      
      Map<String, Object> sectionPermissionMap = (Map<String, Object>) rolePermission
          .get(IRolePermission.SECTION_PERMISSION);
      for (String sectionId : utilizingSectionIds) {
        Map<String, Object> sectionPermission = (Map<String, Object>) sectionPermissionMap
            .get(sectionId);
        if (sectionPermission == null) {
          sectionPermission = new HashMap<String, Object>();
          sectionPermission.put(ISectionPermission.DISABLED_ELEMENTS, new ArrayList<String>());
          sectionPermission.put(ISectionPermission.IS_COLLAPSED, false);
          sectionPermission.put(ISectionPermission.IS_HIDDEN, false);
          sectionPermissionMap.put(sectionId, sectionPermission);
        }
        List<String> disableElements = (List<String>) sectionPermission
            .get(ISectionPermission.DISABLED_ELEMENTS);
        if (!disableElements
            .contains(klassElementNode.getProperty(CommonConstants.CODE_PROPERTY))) {
          disableElements.add(klassElementNode.getProperty(CommonConstants.CODE_PROPERTY));
        }
      }
    }
  }
  
  private static void fillSectionElementNotification(Map<String, Object> notificationSettingsMap,
      String klassId, Vertex klassElementNode, List<String> roleIds)
  {
    Iterator<Edge> iterator = klassElementNode
        .getEdges(Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_NOTIFICATION_SETTING_FOR)
        .iterator();
    List<String> roleIdsForDefaultNotificationSetting = new ArrayList<>();
    roleIdsForDefaultNotificationSetting.addAll(roleIds);
    boolean found = false;
    while (iterator.hasNext()) {
      Edge notificationSettingFor = iterator.next();
      List<String> utilizingKlassIds = notificationSettingFor
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if (utilizingKlassIds.contains(klassId)) {
        found = true;
        String roleId = notificationSettingFor.getProperty(CommonConstants.ROLE_ID_PROPERY);
        roleIdsForDefaultNotificationSetting.remove(roleId);
        Vertex notificationSettingNode = notificationSettingFor.getVertex(Direction.OUT);
        Boolean isNotificationEnabled = notificationSettingNode
            .getProperty(CommonConstants.IS_NOTIFICATION_ENABLED);
        Map<String, Boolean> roleNotifictionMap = (Map<String, Boolean>) notificationSettingsMap
            .get(roleId);
        if (roleNotifictionMap == null) {
          roleNotifictionMap = new HashMap<String, Boolean>();
          notificationSettingsMap.put(roleId, roleNotifictionMap);
        }
        roleNotifictionMap.put(klassElementNode.getProperty(CommonConstants.CODE_PROPERTY),
            isNotificationEnabled);
      }
    }
    if (!found) {
      fillDefaultNotificationSetting(notificationSettingsMap, klassElementNode,
          roleIdsForDefaultNotificationSetting);
    }
  }
  
  private static void fillDefaultNotificationSetting(Map<String, Object> notificationSettingsMap,
      Vertex klassElementNode, List<String> roleIds)
  {
    for (String roleId : roleIds) {
      Map<String, Boolean> roleNotifictionMap = (Map<String, Boolean>) notificationSettingsMap
          .get(roleId);
      if (roleNotifictionMap == null) {
        roleNotifictionMap = new HashMap<String, Boolean>();
        notificationSettingsMap.put(roleId, roleNotifictionMap);
      }
      roleNotifictionMap.put(klassElementNode.getProperty(CommonConstants.CODE_PROPERTY), false);
    }
  }
  
  public static Vertex getPropertyCollectionNodeFromKlassSectionNode(Vertex klassSectionNode)
      throws Exception
  {
    Iterator<Vertex> iterator = klassSectionNode
        .getVertices(Direction.IN, RelationshipLabelConstants.PROPERTY_COLLECTION_OF)
        .iterator();
    Vertex propertyCollectionNode = null;
    if (!iterator.hasNext()) {
      throw new NotFoundException();
    }
    propertyCollectionNode = iterator.next();
    
    if (iterator.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    return propertyCollectionNode;
  }
  
  public static void createAndLinkSectionsToKlass(Vertex klassNode,
      List<HashMap<String, Object>> sections, List<Vertex> klassAndChildNodes,
      List<String> addedSectionIds, List<Map<String, Object>> addedPropertiesInSource,
      String vertexLabel, List<String> addedCalculatedAttributeIds) throws Exception
  {
    List<String> klassAndChildIds = new ArrayList<>();
    
    if (sections.isEmpty()) {
      return;
    }
    
    for (Vertex node : klassAndChildNodes) {
      klassAndChildIds.add((String) node.getProperty(CommonConstants.CODE_PROPERTY));
    }

    String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.ENTITY_TYPE_KLASS_SECTION, CommonConstants.CODE_PROPERTY);

    for (HashMap<String, Object> sectionMap : sections) {
      String propertyCollectionId = (String) sectionMap.get(ISection.PROPERTY_COLLECTION_ID);
      if (propertyCollectionId == null) {
        continue;
      }
      Vertex propertyCollectionNode = null;
      try {
        propertyCollectionNode = UtilClass.getVertexByIndexedId(propertyCollectionId,
            VertexLabelConstants.PROPERTY_COLLECTION);
      }
      catch (NotFoundException e) {
        throw new PropertyCollectionNotFoundException(e);
      }
      
      HashMap<String, Object> newSectionMap = new HashMap<String, Object>();
      generateMapWithId(newSectionMap, klassId, propertyCollectionId);
      Vertex sectionNode = UtilClass.createNode(newSectionMap, vertexType, new ArrayList<>());
      String sectionId = sectionNode.getProperty(CommonConstants.CODE_PROPERTY);
      
      Edge sectionRelationship = sectionNode
          .addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF, klassNode);
      sectionRelationship.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
      propertyCollectionNode.addEdge(RelationshipLabelConstants.PROPERTY_COLLECTION_OF,
          sectionNode);
      // TODO inherit sequence
      inheritSectionsInChildKlasses(klassNode, sectionNode, sectionRelationship,
          propertyCollectionNode.getId());
      
      Iterable<Edge> entityToRelationships = propertyCollectionNode.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
      
      for (Edge entityTo : entityToRelationships) {
        Map<String, Object> addedPropertyMap = new HashMap<>();
        List<String> klassAndChildIdsForProperty = new ArrayList<>();
        klassAndChildIdsForProperty.add(klassId);
        String entityType = entityTo.getProperty(CommonConstants.TYPE_PROPERTY);
        Vertex elementNode = entityTo.getVertex(Direction.OUT);
        
        // This if statement means that element is already added as part of
        // other section...
        Vertex klassPropertyNode = getRespectiveKlassPropertyNode(klassNode, elementNode);
        if (klassPropertyNode != null) {
          Iterator<Edge> hasKlassPropertyIterator = klassPropertyNode
              .getEdges(Direction.IN, RelationshipLabelConstants.HAS_KLASS_PROPERTY)
              .iterator();
          while (hasKlassPropertyIterator.hasNext()) {
            Edge hasKlassProperty = hasKlassPropertyIterator.next();
            if (klassNode.equals(hasKlassProperty.getVertex(Direction.OUT))) {
              List<String> utilizingSectionIds = hasKlassProperty
                  .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
              if (utilizingSectionIds == null) {
                utilizingSectionIds = new ArrayList<>();
              }
              utilizingSectionIds.add(sectionId);
              hasKlassProperty.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
                  utilizingSectionIds);
              break;
            }
          }
          if (entityType.equals(CommonConstants.RELATIONSHIP)) {
            inheritKlassRelationshipNodesInChildKlasses(klassPropertyNode, sectionId,
                klassAndChildNodes);
          }
          else {
            inheritKlassPropertyNodeInChildKlasses(klassNode, sectionId, elementNode,
                klassPropertyNode, klassAndChildIdsForProperty);
          }
        }
        else {
          Map<String, Object> klassPropertyMap = new HashMap<>();
          if (addedSectionIds.size() > 0) {
            klassPropertyMap.put(CommonConstants.ID_PROPERTY, addedSectionIds.get(0));
          }
          Map<String, Object> rSide = (Map<String, Object>) sectionMap
              .get(CommonConstants.RELATIONSHIP_SIDE_PROPERTY);
          if (entityType.equals(CommonConstants.RELATIONSHIP) && rSide == null) {
            updateUtilizingSectionIdsForHasKPOfChildNodes(elementNode, sectionNode,
                klassAndChildNodes);
          }
          else {
            klassPropertyNode = createNewKlassPropertyNode(entityType, klassNode, elementNode,
                sectionId, rSide, klassPropertyMap, klassAndChildIdsForProperty);
            
            fillAddedPropertyMap(addedPropertyMap, klassPropertyNode, vertexLabel);
            
            if (entityType.equals(CommonConstants.ATTRIBUTE)
                && elementNode.getProperty(CommonConstants.TYPE_PROPERTY).equals(Constants.CALCULATED_ATTRIBUTE_TYPE)) {
              addedCalculatedAttributeIds.add(elementNode.getProperty(CommonConstants.CODE_PROPERTY));
            }
          }
        }
        if (!addedPropertyMap.isEmpty()) {
          addedPropertyMap.put(IDefaultValueChangeModel.KLASS_AND_CHILDRENIDS,
              klassAndChildIdsForProperty);
          addedPropertiesInSource.add(addedPropertyMap);
        }
      }
      
      List<Vertex> childNodes = getChildKlassNodesConnectedToSection(sectionNode, klassNode,
          klassAndChildNodes);
      // TODO update sequence
      // linkPropertyCollectiontoTemplate(template, propertyCollectionNode,
      // childNodes);
      // updateTemplateSequenceNode(template, propertyCollectionId, childNodes,
      // -1,
      // RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE);
    }
    klassNode.removeProperty(CommonConstants.SECTIONS_PROPERTY);
  }
  
  public static void fillAddedPropertyMap(Map<String, Object> addedPropertyMap,
      Vertex klassPropertyNode, String vertexLabel)
  {
    String entityType = klassPropertyNode.getProperty(ISectionElement.TYPE);
    if (!entityType.equals(CommonConstants.ATTRIBUTE) && !entityType.equals(CommonConstants.TAG)) {
      return;
    }
    Iterator<Vertex> entityNodeIterator = klassPropertyNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    Vertex entityNode = entityNodeIterator.next();
    if (entityType.equals(CommonConstants.ATTRIBUTE)) {
      String defaultValue = klassPropertyNode.getProperty(ISectionAttribute.DEFAULT_VALUE);
      String defaultValueAsHtml = klassPropertyNode.getProperty(ISectionAttribute.VALUE_AS_HTML);
      /* if (defaultValue == null || defaultValue.isEmpty()) {
              String defaultValueOfEntity = entityNode.getProperty(IAttribute.DEFAULT_VALUE);
              String defaultValueAsHtmlOfEntity = entityNode.getProperty(IAttribute.VALUE_AS_HTML);
              String attrType = entityNode.getProperty(IAttribute.TYPE);
              if ((defaultValueOfEntity == null || defaultValueOfEntity.isEmpty()) && !attrType.equals(CommonConstants.CALCULATED_ATTRIBUTE_TYPE)) {
                return;
              }
              else {
                addedPropertyMap.put(IAttributeDefaultValueCouplingTypeModel.VALUE, defaultValueOfEntity);
                addedPropertyMap.put(IAttributeDefaultValueCouplingTypeModel.VALUE_AS_HTML, defaultValueAsHtmlOfEntity);
      //        }
            }
            else {*/
      addedPropertyMap.put(IAttributeDefaultValueCouplingTypeModel.VALUE, defaultValue);
      addedPropertyMap.put(IAttributeDefaultValueCouplingTypeModel.VALUE_AS_HTML,
          defaultValueAsHtml);
      addedPropertyMap.put(IAttributeDefaultValueCouplingTypeModel.IS_DEPENDENT,
          entityNode.getProperty(IAttribute.IS_TRANSLATABLE));
      // }
    }
    else if (entityType.equals(CommonConstants.TAG)) {
      Iterator<Edge> defaultTagEdgesIterator = klassPropertyNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE)
          .iterator();
      List<Map<String, Object>> defaultValueListOfTag = new ArrayList<>();
      if (!defaultTagEdgesIterator.hasNext()) {
        Iterator<Vertex> defaultTagNodesOfEntityIterator = entityNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF)
            .iterator();
        /*if (!defaultTagNodesOfEntityIterator.hasNext()) {
          //return;
        }*/
        
        while (defaultTagNodesOfEntityIterator.hasNext()) {
          Vertex defaultTagNodeOfEntity = defaultTagNodesOfEntityIterator.next();
          Map<String, Object> defaultValueMap = new HashMap<>();
          defaultValueMap.put(IIdRelevance.RELEVANCE, 100);
          defaultValueMap.put(IIdRelevance.TAGID, UtilClass.getCodeNew(defaultTagNodeOfEntity));
          defaultValueListOfTag.add(defaultValueMap);
        }
      }
      
      while (defaultTagEdgesIterator.hasNext()) {
        Edge defaultTagEdge = defaultTagEdgesIterator.next();
        Map<String, Object> defaultValueMap = new HashMap<>();
        Vertex defaultTagNode = defaultTagEdge.getVertex(Direction.IN);
        Integer relevance = defaultTagEdge.getProperty(IIdRelevance.RELEVANCE);
        if (relevance == null) {
          relevance = 100;
        }
        defaultValueMap.put(IIdRelevance.RELEVANCE, relevance);
        defaultValueMap.put(IIdRelevance.TAGID, UtilClass.getCodeNew(defaultTagNode));
        defaultValueListOfTag.add(defaultValueMap);
      }
      addedPropertyMap.put(ITagDefaultValueCouplingTypeModel.VALUE, defaultValueListOfTag);
    }
    addedPropertyMap.put(IDefaultValueChangeModel.ENTITY_ID, UtilClass.getCodeNew(entityNode));
    Long propertyiid = entityNode.getProperty(IAttribute.PROPERTY_IID);
    addedPropertyMap.put(IDefaultValueChangeModel.PROPERTY_IID, propertyiid);
    addedPropertyMap.put(IDefaultValueChangeModel.TYPE, entityType);
    addedPropertyMap.put(IDefaultValueChangeModel.COUPLING_TYPE,
        klassPropertyNode.getProperty(ISectionElement.COUPLING_TYPE));
    addedPropertyMap.put(IDefaultValueChangeModel.IS_MANDATORY,
        klassPropertyNode.getProperty(ISectionElement.IS_MANDATORY));
    addedPropertyMap.put(IDefaultValueChangeModel.IS_SHOULD,
        klassPropertyNode.getProperty(ISectionElement.IS_SHOULD));
    addedPropertyMap.put(IDefaultValueChangeModel.IS_SKIPPED,
        klassPropertyNode.getProperty(ISectionElement.IS_SKIPPED));
    String sourceType = getSourceTypeBasedUponNodeLabel(vertexLabel);
    addedPropertyMap.put(IDefaultValueChangeModel.SOURCE_TYPE, sourceType);
  }
  
  /**
   * add hasTemplatePC link with templateNode with isInherited = false and also
   * with templateNode of all parametered childNodes with isInherited = true
   *
   * @author Lokesh
   * @param templateNode
   * @param propertyCollectionNode
   * @param childNodes
   * @throws Exception
   */
  @Deprecated
  private static void linkPropertyCollectiontoTemplate(Vertex templateNode,
      Vertex propertyCollectionNode, List<Vertex> childNodes) throws Exception
  {
    Edge hasTemplatePC;
    hasTemplatePC = templateNode.addEdge(
        RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION, propertyCollectionNode);
    hasTemplatePC.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
    for (Vertex childNode : childNodes) {
      Vertex childTemplateNode = null; // TemplateUtils.getTemplateFromKlass(childNode);
      hasTemplatePC = childTemplateNode.addEdge(
          RelationshipLabelConstants.HAS_TEMPLATE_PROPERTY_COLLECTION, propertyCollectionNode);
      hasTemplatePC.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
    }
  }
  
  /**
   * Update existing sequence Node or create new sequence if isInherited = true
   * also inherit it in child
   *
   * @author Lokesh
   * @param templateNode
   * @param entityId
   *          (PropertyCollectionId or relationshipId)
   * @param childKlassNodes
   * @param newPosition
   *          (-1 for last position, -2 to remove propertyCollection)
   * @param SequenceEdgeLabel
   * @throws Exception
   */
  /*public static void updateTemplateSequenceNode(Vertex templateNode, String entityId,
        List<Vertex> childKlassNodes, Integer newPosition, String sequenceEdgeLabel) throws Exception
    {
      Vertex originalSequenceNode = null;
      Vertex sequenceNodeToUpdate = null;
      Iterator<Edge> edgesIterator = templateNode.getEdges(Direction.OUT,
          sequenceEdgeLabel).iterator();
      if(edgesIterator.hasNext()){
        Edge hasSequenceEdge = edgesIterator.next();
        originalSequenceNode = hasSequenceEdge.getVertex(Direction.IN);
        Boolean isInherited = hasSequenceEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
        List<String> sequenceList = getUpdatedSequenceList(
            entityId, newPosition, originalSequenceNode);
        if(!isInherited){
          sequenceNodeToUpdate = originalSequenceNode;
          originalSequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, sequenceList);
        }
        else{
          hasSequenceEdge.remove();
          sequenceNodeToUpdate = UtilClass.createNode(originalSequenceNode.getProperty("@class"));
          //As sequence node don't had cid or id property don't use UtilClass.createDuplicateNode method
          Map<String,Object> sequenceMap = new HashMap<String, Object>();
          sequenceMap.put(CommonConstants.SEQUENCE_LIST, sequenceList);
          UtilClass.saveNode(sequenceMap, sequenceNodeToUpdate, new ArrayList<String>());
  
          Edge hasSequence = templateNode.addEdge(sequenceEdgeLabel, sequenceNodeToUpdate);
          hasSequence.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
        }
      }
      updateSequenceInChildNodes(entityId, childKlassNodes, sequenceNodeToUpdate, originalSequenceNode, sequenceEdgeLabel, newPosition);
  
    }
  */
  /**
   * get sequenceList from parametered sequenceNode update it according to
   * position and entityId and return it.
   *
   * @author Lokesh
   * @param entityId
   *          (PropertyCollectionId or relationshipId)
   * @param position
   *          (-1 for last position, -2 to remove propertyCollection)
   * @param sequenceNode
   * @return
   */
  public static List<String> getUpdatedSequenceList(String entityId, Integer position,
      Vertex sequenceNode)
  {
    List<String> sequenceList = sequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    List<String> updatedSequenceList = new ArrayList<String>();
    updatedSequenceList.addAll(sequenceList);
    if (position == -1) {
      if (!updatedSequenceList.contains(entityId)) {
        updatedSequenceList.add(entityId);
      }
    }
    else if ((position == -2)) {
      updatedSequenceList.remove(entityId);
    }
    else {
      updatedSequenceList.remove(entityId);
      updatedSequenceList.add(position, entityId);
    }
    return updatedSequenceList;
  }
  
  /**
   * Return child nodes from klassAndChildNodes linked with parametered
   * sectionNode Note : it return only child Nodes which doesn't contains
   * klassNode itself from klassAndChildNodes
   *
   * @author Lokesh
   * @param sectionNode
   * @param originalKlassNode
   * @param klassAndChildNodes
   * @return
   */
  public static List<Vertex> getChildKlassNodesConnectedToSection(Vertex sectionNode,
      Vertex originalKlassNode, List<Vertex> klassAndChildNodes)
  {
    List<Vertex> childNodes = new ArrayList<Vertex>();
    Iterable<Vertex> klassIterator = sectionNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    
    for (Vertex klassNode : klassIterator) {
      if (klassAndChildNodes.contains(klassNode) && !klassNode.equals(originalKlassNode)) {
        childNodes.add(klassNode);
      }
    }
    return childNodes;
  }
  
  /**
   * update sequence for childNodes
   *
   * @author Lokesh
   * @param entityId
   * @param childNodes
   * @param newSequenceNode
   * @param originalSequenceNode
   * @param sequenceEdgeLabel
   * @param newPosition
   * @throws Exception
   */
  @Deprecated
  private static void updateSequenceInChildNodes(String entityId, List<Vertex> childNodes,
      Vertex newSequenceNode, Vertex originalSequenceNode, String sequenceEdgeLabel,
      Integer newPosition) throws Exception
  {
    for (Vertex klassNode : childNodes) {
      Vertex templateNode = null; // TemplateUtils.getTemplateFromKlass(klassNode);
      Edge hasSequence = null;
      Iterator<Edge> edgesIterator = templateNode.getEdges(Direction.OUT, sequenceEdgeLabel)
          .iterator();
      if (!edgesIterator.hasNext()) {
        throw new SequenceNotFoundException();
      }
      hasSequence = edgesIterator.next();
      
      Vertex sequenceNode = hasSequence.getVertex(Direction.IN);
      
      Boolean isInInherited = hasSequence.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      // if isInInherited =true & new sequence node is created & klassNode from
      // childNodes using
      // originalSequenceNode
      if (isInInherited && !newSequenceNode.equals(originalSequenceNode)
          && sequenceNode.equals(originalSequenceNode)) {
        hasSequence.remove();
        Edge hasSequenceNew = templateNode.addEdge(sequenceEdgeLabel, newSequenceNode);
        hasSequenceNew.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      }
      // don't do anythink for sequence update if isInInherited = false
      else if (!isInInherited && newPosition < 0) {
        List<String> sequenceList = sequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
        sequenceList = getUpdatedSequenceList(entityId, newPosition, sequenceNode);
        sequenceNode.setProperty(CommonConstants.SEQUENCE_LIST, sequenceList);
      }
    }
  }
  
  /**
   * Update UtilizingSectionIds For klassAndChildNodes List if that elementNode
   * is linked with it via KP
   *
   * @param elementNode
   * @param sectionNode
   * @param klassAndChildNodes
   * @throws Exception
   */
  private static void updateUtilizingSectionIdsForHasKPOfChildNodes(Vertex elementNode,
      Vertex sectionNode, List<Vertex> klassAndChildNodes) throws Exception
  {
    for (Vertex childNode : klassAndChildNodes) {
      Iterator<Vertex> childSectionNodes = childNode
          .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
          .iterator();
      while (childSectionNodes.hasNext()) {
        Vertex tempSectionNode = childSectionNodes.next();
        if (tempSectionNode.equals(sectionNode)) {
          Vertex childKlassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(childNode,
              elementNode);
          if (childKlassPropertyNode != null) {
            Edge hasKlassPropertyOfChild = KlassUtils
                .getHasKlassPropertyEdgeFromKlassPropertyAndKLass(childKlassPropertyNode,
                    UtilClass.getCodeNew(childNode));
            List<String> utilizingSectionIds = hasKlassPropertyOfChild
                .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
            if (utilizingSectionIds == null) {
              utilizingSectionIds = new ArrayList<String>();
            }
            if (!utilizingSectionIds.contains(UtilClass.getCodeNew(sectionNode))) {
              utilizingSectionIds.add(sectionNode.getProperty(CommonConstants.CODE_PROPERTY));
            }
            hasKlassPropertyOfChild.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
                utilizingSectionIds);
          }
        }
      }
    }
  }
  
  /**
   * iterates over hasKP and adds sectionId to the utilizingSectionIds
   *
   * @param klassPropertyNode
   * @param sectionId
   * @param klassAndChildNodes
   */
  public static void inheritKlassRelationshipNodesInChildKlasses(Vertex klassPropertyNode,
      String sectionId, List<Vertex> klassAndChildNodes)
  {
    Iterable<Edge> hasKlassPropertyEdges = klassPropertyNode.getEdges(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Edge hasKlassPropertyEdge : hasKlassPropertyEdges) {
      Vertex klassNode = hasKlassPropertyEdge.getVertex(Direction.OUT);
      if (!klassAndChildNodes.contains(klassNode)) {
        continue;
      }
      
      if (!checkIfSectionIsLinkedToTheKlass(sectionId, klassNode)) {
        continue;
      }
      
      List<String> utilizingSectionIds = hasKlassPropertyEdge
          .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
      if (!utilizingSectionIds.contains(sectionId)) {
        utilizingSectionIds.add(sectionId);
        hasKlassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
            utilizingSectionIds);
      }
    }
  }
  
  public static void generateMapWithId(Map<String, Object> map, String... ids)
  {
    StringBuilder sb = new StringBuilder();
    for (String id : ids) {
      CRC32 crc = new CRC32();
      crc.update(id.getBytes());
      String str = Long.toHexString(crc.getValue());
      sb.append(str);
    }
    map.put(CommonConstants.ID_PROPERTY, sb.toString());
    map.put(CommonConstants.CODE_PROPERTY, sb.toString());
  }
  
  /*  public static Vertex getPreviousSectionNode(int sequence, Vertex previousNode,
      Vertex firstSectionNode, String klassId)
  {
    if (sequence > 0) {
      if (sequence == 1) {
        sequence = sequence - 1;
        if (previousNode == null) {
          previousNode = firstSectionNode;
        }
        previousNode = getPreviousSectionNode(sequence, previousNode, firstSectionNode, klassId);
      }
      else {
        Iterable<Edge> previousSectionEdges = firstSectionNode.getEdges(Direction.IN,
            RelationshipLabelConstants.PREVIOUS_SECTION);
        for (Edge previousSectionEdge : previousSectionEdges) {
          String owningKlassId = previousSectionEdge
              .getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
          List<String> utilizingKlassIds = previousSectionEdge
              .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
          if (utilizingKlassIds.contains(klassId)) {
            previousNode = previousSectionEdge.getVertex(Direction.OUT);
          }
        }
        sequence = sequence - 1;
        if (previousNode == null) {
          previousNode = firstSectionNode;
        }
        previousNode = getPreviousSectionNode(sequence, previousNode, previousNode, klassId);
      }
    }
    return previousNode;
  }*/
  
  /*  public static Vertex getNextSectionNode(String klassId, Vertex previousNode, Vertex nextNode)
  {
    Iterable<Edge> nextNodeEdges = previousNode.getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION);
    for (Edge nextNodeEdge : nextNodeEdges) {
      String owningId = nextNodeEdge.getProperty(CommonConstants.OWNING_KLASS_ID_PROPERTY);
      List<String> utilizingKlassId = nextNodeEdge.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if(utilizingKlassId.contains(klassId)){
        nextNode = nextNodeEdge.getVertex(Direction.OUT);
      }
    }
    return nextNode;
  }*/
  
  public static boolean checkIfSectionNodeExist(String klassId, Object propertyCollectionId)
  {
    String query = "select from (select expand(out('"
        + RelationshipLabelConstants.PROPERTY_COLLECTION_OF + "')) from " + propertyCollectionId
        + ") where out('" + RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF
        + "') contains (code='" + klassId + "')";
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (iterator.hasNext()) {
      return true;
    }
    
    return false;
  }
  
  private static void inheritSectionsInChildKlasses(Vertex klassNode, Vertex sectionNode,
      Edge sectionOfRelkation, Object propertyCollectionId) throws Exception
  {
    Iterable<Vertex> childNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childNode : childNodes) {
      if (checkIfSectionNodeExist(childNode.getProperty(CommonConstants.CODE_PROPERTY),
          propertyCollectionId)) {
        continue;
      }
      
      Edge sectionChildRelationship = sectionNode
          .addEdge(RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF, childNode);
      sectionChildRelationship.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
      
      UtilClass.addNodesForVersionIncrement(childNode);
      inheritSectionsInChildKlasses(childNode, sectionNode, sectionOfRelkation,
          propertyCollectionId);
    }
  }
  
  public static Vertex createNewKlassPropertyNode(String entityType, Vertex klassNode,
      Vertex elementNode, String sectionId, Map<String, Object> relationshipSide,
      Map<String, Object> klassPropertyMap, List<String> klassAndChildIdsForProperty)
      throws Exception
  {
    Vertex klassPropertyNode = null;
    String klassPropertyVertexType = null;
    String tagType = null;
    switch (entityType) {
      case CommonConstants.ATTRIBUTE_PROPERTY:
        klassPropertyVertexType = VertexLabelConstants.KLASS_ATTRIBUTE;
        break;
      case CommonConstants.TAG_PROPERTY:
        klassPropertyVertexType = VertexLabelConstants.KLASS_TAG;
        klassPropertyMap.put(ISectionTag.IS_MULTI_SELECT,
            (Boolean) elementNode.getProperty(ISectionTag.IS_MULTI_SELECT));
        break;
      case CommonConstants.ROLE_PROPERTY:
        klassPropertyVertexType = VertexLabelConstants.KLASS_ROLE;
        break;
      case CommonConstants.RELATIONSHIP:
        klassPropertyVertexType = VertexLabelConstants.KLASS_RELATIONSHIP;
        TranslationsUtils.updateRelationshipSideLabel(relationshipSide);
        klassPropertyMap.put(CommonConstants.RELATIONSHIP_SIDE_PROPERTY, relationshipSide);
        break;
      case CommonConstants.TAXONOMY_PROPERTY:
        klassPropertyVertexType = VertexLabelConstants.KLASS_TAXONOMY_ENTITY;
        klassPropertyMap.put(ISectionTaxonomy.IS_MULTI_SELECT, true);
        break;
    }
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(klassPropertyVertexType,
        CommonConstants.CODE_PROPERTY);
    String klassPropertyId = (String) klassPropertyMap.get(CommonConstants.ID_PROPERTY);
    if (klassPropertyId == null || klassPropertyId.equals("")) {
      String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
      String elementId = elementNode.getProperty(CommonConstants.CODE_PROPERTY);
      generateMapWithId(klassPropertyMap, klassId, elementId);
    }
    klassPropertyNode = UtilClass.createNode(klassPropertyMap, vertexType, new ArrayList<>());
    klassPropertyNode.setProperty(CommonConstants.TYPE_PROPERTY, entityType);
    setDefaultPropertiesForKlassPropertyNode(klassPropertyNode, tagType, entityType);
    
    klassPropertyNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY, elementNode);
    Edge hasKlassProperty = klassNode.addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY,
        klassPropertyNode);
    hasKlassProperty.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
    Iterable<Vertex> defaultTagValueNodes = elementNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_DEFAULT_TAG_OF);
    for (Vertex defaultTagValueNode : defaultTagValueNodes) {
      Edge edge = klassPropertyNode.addEdge(RelationshipLabelConstants.HAS_DEFAULT_TAG_VALUE,
          defaultTagValueNode);
      edge.setProperty(CommonConstants.SORT_FIELD_RELEVANCE, 100);
    }
    List<String> utilizingSectionIds = new ArrayList<>();
    if (sectionId != null) { // while creating relationship section id is null
                             // since it is not added in any PC
      utilizingSectionIds.add(sectionId);
    }
    hasKlassProperty.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
        utilizingSectionIds);
    if (!entityType.equals(CommonConstants.RELATIONSHIP)) {
      inheritKlassPropertyNodeInChildKlasses(klassNode, sectionId, elementNode, klassPropertyNode,
          klassAndChildIdsForProperty);
    }
    // add propertyId to KP
    klassPropertyNode.setProperty(CommonConstants.PROPERTY_ID, UtilClass.getCodeNew(elementNode));
    
    return klassPropertyNode;
  }
  
  public static void inheritKlassPropertyNodeInChildKlasses(Vertex klassNode, String sectionId,
      Vertex elementNode, Vertex klassPropertyNode, List<String> klassAndChildIdsForProperty)
      throws Exception
  {
    Iterable<Vertex> childNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    // Iterate over child nodes
    for (Vertex child : childNodes) {
      if (!checkIfSectionIsLinkedToTheKlass(sectionId, child)) {
        continue;
      }
      klassAndChildIdsForProperty.add(UtilClass.getCodeNew(child));
      // get klass Property Node for child
      Vertex childklassPropertyNode = getRespectiveKlassPropertyNode(child, elementNode);
      // If no appropriate klassPropertyNode found in child klass
      if (childklassPropertyNode == null) {
        // Create edge with parentKlassProperty node and set utilizing section
        // ids
        Edge hasKlassPropertyEdge = getHasKlassPropertyEdgeFromKlassPropertyAndKLass(
            klassPropertyNode, klassId);
        List<String> utilizingSectionIds = hasKlassPropertyEdge
            .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
        Edge duplicatedHasKlassPropertyEdge = child
            .addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY, klassPropertyNode);
        duplicatedHasKlassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
        duplicatedHasKlassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
            utilizingSectionIds);
      }
      // if klassProperty Node is found in child klass
      else {
        Edge childHasKlassPropertyEdge = PropertyCollectionUtils
            .getLinkBetweenKlassAndKlassProperty(child, childklassPropertyNode);
        List<String> existingUtilizingSectionIds = childHasKlassPropertyEdge
            .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
        Set<String> newUtilizingSectionIds = new HashSet<>();
        if (existingUtilizingSectionIds == null) {
          existingUtilizingSectionIds = new ArrayList<>();
        }
        newUtilizingSectionIds.addAll(existingUtilizingSectionIds);
        newUtilizingSectionIds.add(sectionId);
        childHasKlassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
            new ArrayList<>(newUtilizingSectionIds));
      }
      inheritKlassPropertyNodeInChildKlasses(child, sectionId, elementNode, klassPropertyNode,
          klassAndChildIdsForProperty);
    }
  }
  
  public static Boolean checkIfSectionIsLinkedToTheKlass(String sectionId, Vertex child)
  {
    // get all sectionNodesAssociatedWithChildKlass
    Iterator<Vertex> sectionNodesAssociatedWithChildKlass = child
        .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
        .iterator();
    List<String> sectionIdsAssociatedWithChildKlass = new ArrayList<String>();
    while (sectionNodesAssociatedWithChildKlass.hasNext()) {
      sectionIdsAssociatedWithChildKlass.add(sectionNodesAssociatedWithChildKlass.next()
          .getProperty(CommonConstants.CODE_PROPERTY));
    }
    // if the section is not inherited in child since the same PC was already
    // present in it
    if (sectionIdsAssociatedWithChildKlass.contains(sectionId)) {
      return true;
    }
    
    return false;
  }
  
  public static void setDefaultPropertiesForKlassPropertyNode(Vertex klassPropertyNode,
      String tagType, String entityType)
  {
    if (entityType.equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
      klassPropertyNode.setProperty(ISectionAttribute.DEFAULT_VALUE, "");
      klassPropertyNode.setProperty(ISectionAttribute.IS_IDENTIFIER, false);
    }
    else if (entityType.equals(CommonConstants.TAG_PROPERTY)) {
      klassPropertyNode.setProperty(ISectionAttribute.DEFAULT_VALUE, new ArrayList<>());
    }
    
    klassPropertyNode.setProperty(ISectionElement.TOOLTIP, "");
    
    klassPropertyNode.setProperty(ISectionElement.IS_CUT_OFF, false);
    klassPropertyNode.setProperty(ISectionElement.IS_INHERITED, false);
    klassPropertyNode.setProperty(ISectionElement.IS_DISABLED, false);
    klassPropertyNode.setProperty(ISectionElement.IS_MANDATORY, false); // TODO
    // ::
    // isMandatory
    // always
    // false
    // ??
    klassPropertyNode.setProperty(ISectionElement.IS_SHOULD, false);
    klassPropertyNode.setProperty(ISectionElement.IS_VARIANT_ALLOWED, false);
    klassPropertyNode.setProperty(ISectionElement.LABEL, "");
    klassPropertyNode.setProperty(ISectionElement.NUMBER_OF_VERSIONS_ALLOWED, 0);
    klassPropertyNode.setProperty(ISectionElement.IS_SKIPPED, false);
    klassPropertyNode.setProperty(ISectionElement.COUPLING_TYPE, CommonConstants.LOOSELY_COUPLED);
    
    if (tagType != null && !tagType.equals("")) {
      klassPropertyNode.setProperty(ISectionTag.TAG_TYPE, tagType);
    }
  }
  
  @Deprecated
  public static void linkAllInheritingChildNode(Vertex klassNode, Vertex klassPropertyNode,
      Vertex entityNode, String sectionId)
  {
    List<String> utilizingSectionIds = new ArrayList<>();
    utilizingSectionIds.add(sectionId);
    Iterable<Vertex> childNodes = klassNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    // get all child nodes
    for (Vertex childNode : childNodes) {
      Iterable<Vertex> sectionNodes = childNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
      for (Vertex sectionNode : sectionNodes) {
        String childSectionId = sectionNode.getProperty(CommonConstants.CODE_PROPERTY);
        // check if child has that section
        // also check if child has already a KP
        if (childSectionId.equals(sectionId)
            && getRespectiveKlassPropertyNode(childNode, entityNode) == null) {
          Edge hasKlassProperty = childNode.addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY,
              klassPropertyNode);
          hasKlassProperty.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
          hasKlassProperty.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
              utilizingSectionIds);
          // update child of child
          linkAllInheritingChildNode(childNode, klassPropertyNode, entityNode, sectionId);
        }
      }
    }
  }
  
  public static Vertex createKlassNatureNode(Vertex klassNode, String relationshipType,
      String natureType) throws Exception
  {
    Map<String, Object> klassNatureRelationship = new HashMap<>();
    klassNatureRelationship.put(IKlassNatureRelationship.RELATIONSHIP_TYPE, relationshipType);
    klassNatureRelationship.put(IKlass.NATURE_TYPE, natureType);
    klassNatureRelationship.put(ISectionRelationship.IS_NATURE, true);
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.KLASS_RELATIONSHIP, CommonConstants.CODE_PROPERTY);
    Vertex vertex = UtilClass.createNode(klassNatureRelationship, vertexType,
        new ArrayList<String>());
    klassNode.addEdge(RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF, vertex);
    
    return vertex;
  }
  
  public static void addKlassNature(Vertex klassNode, Map<String, Object> klassEntityMap)
      throws Exception
  {
    Map<String, Object> klassNature = getKlassNature(klassNode, klassEntityMap);
    klassEntityMap.putAll(klassNature);
  }
  
  public static Map<String, Object> getKlassNature(Vertex klassNode,
      Map<String, Object> klassEntityMap) throws Exception
  {
    Map<String, Object> klassNature = new HashMap<>();
    Iterator<Vertex> klassNatures = klassNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF)
        .iterator();
    List<Map<String, Object>> natureRelationships = new ArrayList<>();
    List<String> productvariants = new ArrayList<String>();
    while (klassNatures.hasNext()) {
      Vertex natureNode = klassNatures.next();
      klassNature.put(IKlass.NATURE_TYPE, natureNode.getProperty(IKlass.NATURE_TYPE));
      Iterator<Vertex> relationships = natureNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      while (relationships.hasNext()) {
        Vertex relationshipNode = relationships.next();
        Map<String, Object> relationshipMap = new HashMap<>();
        relationshipMap = UtilClass.getMapFromVertex(new ArrayList<>(), relationshipNode);
        Iterator<Edge> propertyCollections = natureNode
            .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY_COLLECTION)
            .iterator();
        while (propertyCollections.hasNext()) {
          Edge propertyCollectionEdge = propertyCollections.next();
          relationshipMap.put(IKlassNatureRelationship.PROPERTY_COLLECTION, PropertyCollectionUtils
              .getPropertyCollection(propertyCollectionEdge.getVertex(Direction.IN)));
        }
        
        Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(relationshipNode,
            Arrays.asList(CommonConstants.CODE_PROPERTY));
        relationshipMap.put(IRelationship.TAB_ID, referencedTab.get(ITabModel.ID));
        
        Iterable<Vertex> tagGroupsVertices = relationshipNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_RELATIONSHIP_CONTEXT_TAG);
        List<String> contextTags = new ArrayList<>();
        for (Vertex tagGroupVertex : tagGroupsVertices) {
          String tagGroupId = tagGroupVertex.getProperty(CommonConstants.CODE_PROPERTY);
          contextTags.add(tagGroupId);
        }
        relationshipMap.put(IKlassNatureRelationship.CONTEXT_TAGS, contextTags);
        RelationshipUtils.populatePropetiesInfo(relationshipNode, relationshipMap);
        natureRelationships.add(relationshipMap);
      }
      String relationshipType = natureNode.getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE);
      if (relationshipType.equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)
          && klassEntityMap != null) {
        
        Iterable<Edge> dataRulesrelationships = natureNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.VARIANT_CONTEXT_OF);
        for (Edge allowedTypesrelationship : dataRulesrelationships) {
          Vertex otherNode = allowedTypesrelationship.getVertex(Direction.IN);
          switch ((String) otherNode.getProperty(IVariantContext.TYPE)) {
            case CommonConstants.PRODUCT_VARIANT:
              productvariants.add((String) otherNode.getProperty(CommonConstants.CODE_PROPERTY));
              break;
          }
        }
      }
    }
    if (klassEntityMap != null) {
      Map<String, List<String>> variantContexts = (Map<String, List<String>>) klassEntityMap
          .get(IKlass.CONTEXTS);
      variantContexts.put(IKlassContext.PRODUCT_VARIANT_CONTEXTS, productvariants);
    }
    klassNature.put(IKlass.RELATIONSHIPS, natureRelationships);

    return klassNature;
  }

  public static void updateKlassNaturePropertyNode(Vertex klassNode, Vertex elementNode,
      Map<String, Object> relationshipSide, Vertex natureNode, String natureType, String knrId)
  {
    natureNode.setProperty(CommonConstants.RELATIONSHIP_SIDE_PROPERTY, relationshipSide);
    natureNode.setProperty(CommonConstants.TYPE_PROPERTY, CommonConstants.RELATIONSHIP);
    natureNode.setProperty(IKlass.NATURE_TYPE, natureType);
    natureNode.setProperty(CommonConstants.SIDE_PROPERTY, CommonConstants.RELATIONSHIP_SIDE_1);
    natureNode.setProperty(CommonConstants.PROPERTY_ID, UtilClass.getCodeNew(elementNode));
    natureNode.setProperty(CommonConstants.CODE_PROPERTY, knrId);
    natureNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY, elementNode);
  }
  
  public static Vertex getKlassNatureRelationshipPropertyNode(Vertex klassNode,
      String relationshipId) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Vertex vertex = null;
    Vertex entityNode = UtilClass.getVertexById(relationshipId,
        VertexLabelConstants.ROOT_RELATIONSHIP);
    String klassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "select from (select expand(in("
        + EntityUtil.quoteIt(RelationshipLabelConstants.HAS_PROPERTY) + ")) from "
        + entityNode.getId() + ") where in('klass_nature_relationship_of') contains (code='"
        + klassId + "')";
    Iterable<Vertex> iterator = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNatureVertex : iterator) {
      vertex = klassNatureVertex;
    }
    
    return vertex;
  }
  
  /**
   * get hasKPEdgesUtilzingSection whos utilizingSectionIds contains parametered
   * sectionId
   *
   * @param sectionId
   * @param selfAndChildKlassIds
   * @return hasKPEdgesUtilzingSection
   * @throws Exception
   */
  public static Set<Edge> getHasKPEdgeUtilizingSection(String sectionId,
      List<String> selfAndChildKlassIds)
  {
    
    OrientGraph graph = UtilClass.getGraph();
    Set<Edge> hasKPEdgesUtilzingSection = new HashSet<Edge>();
    // Query to get all hasKlassProperty Edges in which utilizingSectionIds
    // contains sectionId
    String query = "select from " + RelationshipLabelConstants.HAS_KLASS_PROPERTY + " where "
        + CommonConstants.UTILIZING_SECTION_IDS_PROPERTY + " contains '" + sectionId + "'";
    Iterable<Edge> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Edge hasKlassProperty : resultIterable) {
      Vertex klassNode = hasKlassProperty.getVertex(Direction.OUT);
      String klassId = UtilClass.getCodeNew(klassNode);
      if (!selfAndChildKlassIds.contains(klassId)) {
        continue;
      }
      hasKPEdgesUtilzingSection.add(hasKlassProperty);
    }
    return hasKPEdgesUtilzingSection;
  }
  
  /**
   * remove sectionId from UtilzingSectionIds of HasKlassProperty Edges in
   * hasKPEdgeutilzingSections If UtilzingSectionIds became empty then add edge
   * to relationshipsToDelete. Also if klass of this edge is owner of KP then
   * make new duplicate KP for other KlassNodes and add that KP node to
   * nodesToDelete
   *
   * @param sectionId
   * @param hasKPEdgesUtilzingSection
   * @param isKlassDeleted
   * @param nodesToDelete
   * @param relationshipsToDelete
   * @param klassId
   * @throws Exception
   */
  public static void updateKPAndHasKPOnSectionRemoval(String sectionId,
      Set<Edge> hasKPEdgesUtilzingSection, Boolean isKlassDeleted, Set<Vertex> nodesToDelete,
      Set<Edge> relationshipsToDelete, Map<String, List<String>> deletedPropertyMap, String klassId,
      String vertexLabel) throws Exception
  {
    for (Edge hasKlassProperty : hasKPEdgesUtilzingSection) {
      List<String> utilizingSectionIds = hasKlassProperty
          .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
      Boolean isInherited = hasKlassProperty.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      utilizingSectionIds.remove(sectionId);
      Vertex klassPropertyNode = hasKlassProperty.getVertex(Direction.IN);
      // if utilizing sectionIds is no empty or Entity is relationship then
      // simply update it.
      if (!utilizingSectionIds.isEmpty()
          || klassPropertyNode.getProperty(CommonConstants.TYPE_PROPERTY)
              .equals(CommonConstants.RELATIONSHIP)) {
        hasKlassProperty.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
            utilizingSectionIds);
      }
      // if utilizingSectionIds is empty and isInherited = true then delete only
      // hasKlassPropertyEdge
      else if (isInherited) {
        relationshipsToDelete.add(hasKlassProperty);
      }
      // if utilizingSectionIds is empty and isInherited = false then delete KP
      // and
      // create new duplicate KP for all other Klasses using that deleted KP
      else {
        String propertyId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
        createDuplicateKPForAllKlassesUsingParameterKP(sectionId, klassPropertyNode,
            relationshipsToDelete);
        deleteKlassPropertyNode(nodesToDelete, isKlassDeleted, klassPropertyNode,
            deletedPropertyMap);
        GlobalPermissionUtils.deletePropertyPermissionNode(klassId, propertyId, vertexLabel);
      }
    }
  }
  
  /**
   * create Duplicate KlassPropertyNode for all Klasses connected to existing
   * parametered klass Property Node where utilizingSectionIds is not empty
   * after removing sectionId
   *
   * @param sectionId
   * @param klassPropertyNode
   */
  public static void createDuplicateKPForAllKlassesUsingParameterKP(String sectionId,
      Vertex klassPropertyNode, Set<Edge> relationshipsToDelete) throws Exception
  {
    Iterable<Edge> iterable = klassPropertyNode.getEdges(Direction.IN,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Edge hasKlassProperty : iterable) {
      List<String> utilizingSectionIds = hasKlassProperty
          .getProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY);
      utilizingSectionIds.remove(sectionId);
      // If utilizingSectionIds is not empty after excluding deleting sectionId
      // then only creatre
      // new KP
      if (!utilizingSectionIds.isEmpty()) {
        // delete existing hasKlassProperty Edge
        relationshipsToDelete.add(hasKlassProperty);
        
        // Create duplicate KP and hasKlassProperty edge with new KP.
        Vertex klassNode = hasKlassProperty.getVertex(Direction.OUT);
        Vertex duplicateKlassPropertyNode = UtilClass.createDuplicateNode(klassPropertyNode);
        Vertex entityNode = KlassUtils.getPropertyNodeFromKlassProperty(klassPropertyNode);
        String childKlassId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
        String entityId = entityNode.getProperty(CommonConstants.CODE_PROPERTY);
        Map<String, Object> klassPropertyMap = UtilClass.getMapFromVertex(new ArrayList<String>(),
            duplicateKlassPropertyNode);
        generateMapWithId(klassPropertyMap, childKlassId, entityId);
        duplicateKlassPropertyNode.setProperty(CommonConstants.CODE_PROPERTY,
            klassPropertyMap.get(CommonConstants.ID_PROPERTY));
        Edge newHasKlassPropertyEdge = klassNode
            .addEdge(RelationshipLabelConstants.HAS_KLASS_PROPERTY, duplicateKlassPropertyNode);
        newHasKlassPropertyEdge.setProperty(CommonConstants.UTILIZING_SECTION_IDS_PROPERTY,
            utilizingSectionIds);
        newHasKlassPropertyEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);
        duplicateKlassPropertyNode.addEdge(RelationshipLabelConstants.HAS_PROPERTY, entityNode);
      }
    }
  }
  
  public static String getSourceTypeBasedUponNodeLabel(String nodeLabel)
  {
    String sourceType = null;
    switch (nodeLabel) {
      case VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS:
      case VertexLabelConstants.ENTITY_TYPE_KLASS:
      case VertexLabelConstants.ENTITY_TYPE_TARGET:
      case VertexLabelConstants.ENTITY_TYPE_ASSET:
      case VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET:
      case VertexLabelConstants.ENTITY_TYPE_SUPPLIER:
        sourceType = CommonConstants.KLASS_CONFLICTING_SOURCE_TYPE;
        break;
      case VertexLabelConstants.ATTRIBUTION_TAXONOMY:
        sourceType = CommonConstants.TAXONOMY_CONFLICTING_SOURCE_TYPE;
        break;
      case VertexLabelConstants.COLLECTION:
        sourceType = CommonConstants.COLLECTION_CONFLICTING_SOURCE_TYPE;
        break;
    }
    return sourceType;
  }
  
  /**
   * @Lokesh
   *
   * @return
   */
  public static List<String> getAllNatureKlassNodeIds(String baseType)
  {
    List<String> klassIds = new ArrayList<String>();
    
    
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where "
        + IKlass.IS_NATURE + " is not null and " + IKlass.IS_NATURE + " = true";
    if (baseType != null) {
      query = query + " and type = \"" + baseType + "\"";
    }
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klassIds.add(UtilClass.getCodeNew(klassNode));
    }
    return klassIds;
  }
  
  public static Vertex getAttributeVariant(Vertex klassPropertyNode)
  {
    Iterator<Vertex> vertices = klassPropertyNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (vertices.hasNext()) {
      return vertices.next();
    }
    return null;
  }
  
  /**
   * @author Lokesh
   * @param klassPropertyNode
   * @return
   */
  private static Map<String, Object> getAttributeContextMap(Vertex klassPropertyNode)
  {
    Vertex attributeContext = getAttributeVariant(klassPropertyNode);
    if (attributeContext == null) {
      return null;
    }
    Map<String, Object> attributeContextMap = UtilClass.getMapFromVertex(new ArrayList<String>(),
        attributeContext);
    
    List<Map<String, Object>> tags = new ArrayList<>();
    
    Iterable<Vertex> vertices = attributeContext.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    
    attributeContextMap.put(IAttributeContext.TAGS, tags);
    return attributeContextMap;
  }
  
  /**
   * @author Lokesh
   * @param returnKlassMap
   * @param klassNode
   */
  @Deprecated
  public static void fillContextKlassesDetails(Map<String, Object> returnKlassMap,
      Map<String, Object> klassMap, Vertex klassNode) throws Exception
  {
    Map<String, Object> referencedKlasses = (Map<String, Object>) returnKlassMap
        .get(IGetKlassWithGlobalPermissionModel.REFERENCED_KLASSES);
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<String, Object>();
      returnKlassMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_KLASSES, referencedKlasses);
    }
    fillReferencedContextKlassDetails(klassMap, klassNode, referencedKlasses, new HashMap<>());
  }
  
  public static void fillContextKlassDetails(Map<String, Object> returnKlassMap, Vertex klassNode)
      throws Exception
  {
    Map<String, Object> klassMap = (Map<String, Object>) returnKlassMap
        .get(IGetKlassEntityWithoutKPModel.ENTITY);
    Map<String, Object> configDetails = (Map<String, Object>) returnKlassMap
        .get(IGetKlassEntityWithoutKPModel.CONFIG_DETAILS);
    Map<String, Object> referencedKlasses = (Map<String, Object>) configDetails
        .get(IGetKlassEntityConfigDetailsModel.REFERENCED_KLASSES);
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<String, Object>();
      configDetails.put(IGetKlassEntityConfigDetailsModel.REFERENCED_KLASSES, referencedKlasses);
    }
    fillReferencedContextKlassDetails(klassMap, klassNode, referencedKlasses, configDetails);
  }
  
  public static void fillReferencedContextKlassDetails(Map<String, Object> klassMap,
      Vertex klassNode, Map<String, Object> referencedKlasses, Map<String, Object> configDetails)
      throws Exception
  {
    Iterable<Vertex> contextKlasses = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    List<String> embeddedKlassIds = (List<String>) klassMap.get(IProjectKlass.EMBEDDED_KLASS_IDS);
    if (embeddedKlassIds == null) {
      embeddedKlassIds = new ArrayList<String>();
      klassMap.put(IProjectKlass.EMBEDDED_KLASS_IDS, embeddedKlassIds);
    }
    List<String> contextKlassIds = new ArrayList<>();
    for (Vertex contextKlassNode : contextKlasses) {
      String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
      contextKlassIds.add(contextKlassId);
      String natureType = contextKlassNode.getProperty(IKlass.NATURE_TYPE);
      switch (natureType) {
        case CommonConstants.LANGUAGE_KLASS_TYPE:
          klassMap.put(IKlass.LANGUAGE_KLASS_ID, contextKlassId);
          break;
        case CommonConstants.GTIN_KLASS_TYPE:
          klassMap.put(IProjectKlass.GTIN_KLASS_ID, contextKlassId);
          break;
        case CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE:
        case CommonConstants.EMBEDDED_KLASS_TYPE:
          embeddedKlassIds.add(contextKlassId);
          break;
      }
      Map<String, Object> referencedKlassMap = UtilClass
          .getMapFromVertex(Arrays.asList(IIdLabelTypeModel.LABEL, IIdLabelTypeModel.TYPE,
              IKlass.NATURE_TYPE, CommonConstants.CODE_PROPERTY), contextKlassNode);
      referencedKlasses.put(contextKlassId, referencedKlassMap);
    }
    
    fillReferencedPropagableContextKlasses(klassNode, referencedKlasses, configDetails,
        contextKlassIds);
  }
  
  public static void fillReferencedPropagableContextKlasses(Vertex klassNode,
      Map<String, Object> referencedKlasses, Map<String, Object> configDetails,
      List<String> contextKlassIds) throws Exception
  {
    Iterable<Vertex> contextualNodes = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK);
    for (Vertex contextualNode : contextualNodes) {
      String contextKlassIdByIntermediateNode = (String) contextualNode
          .getProperty(IContextKlassModel.CONTEXT_KLASS_ID);
      if (contextKlassIds.contains(contextKlassIdByIntermediateNode)) {
        Map<String, Object> referencedKlassMap = (Map<String, Object>) referencedKlasses
            .get(contextKlassIdByIntermediateNode);
        
        Map<String, Object> propagableTags = new HashMap<>();
        Map<String, Object> propagableAttributes = new HashMap<>();
        
        referencedKlassMap.put(IPropagableContextKlassInformationModel.PROPAGABLE_TAGS,
            propagableTags);
        referencedKlassMap.put(IPropagableContextKlassInformationModel.PROPAGABLE_ATTRIBUTES,
            propagableAttributes);
        
        Iterable<Edge> propertyLinks = contextualNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
        
        Map<String, Object> referencedTags = (Map<String, Object>) configDetails
            .get(IGetKlassWithGlobalPermissionModel.REFERENCED_TAGS);
        Map<String, Object> referencedAttributes = (Map<String, Object>) configDetails
            .get(IGetKlassWithGlobalPermissionModel.REFERENCED_ATTRIBUTES);
        
        for (Edge propertyLink : propertyLinks) {
          Map<String, Object> propagableEntityMap = UtilClass.getMapFromEdge(propertyLink);
          Vertex propertyVertex = propertyLink.getVertex(Direction.IN);
          String propagableEntityId = UtilClass.getCodeNew(propertyVertex);
          propagableEntityMap.put(IIdAndCouplingTypeModel.ID, propagableEntityId);
          if (propertyVertex.getProperty(CommonConstants.TYPE_PROPERTY)
              .equals(CommonConstants.TAG_TYPE)) {
            propagableTags.put(propagableEntityId, propagableEntityMap);
            if (referencedTags.containsKey(propagableEntityId)) {
              continue;
            }
            Map<String, Object> propertyMap = TagUtils.getTagMap(propertyVertex, true);
            referencedTags.put(propagableEntityId, propertyMap);
          }
          else {
            Map<String, Object> propertyMap = AttributeUtils.getAttributeMap(propertyVertex);
            propagableAttributes.put(propagableEntityId, propagableEntityMap);
            if (referencedAttributes.containsKey(propagableEntityId)) {
              continue;
            }
            referencedAttributes.put(propagableEntityId, propertyMap);
          }
        }
      }
    }
  }
  
  public static void fillPropagableContextualData(Map<String, Object> klassMap, Vertex klassNode)
  {
    
    Map<String, Object> referencedKlasses = new HashMap<String, Object>();
    Iterable<Vertex> contextKlasses = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    List<String> embeddedKlassIds = (List<String>) klassMap.get(IProjectKlass.EMBEDDED_KLASS_IDS);
    if (embeddedKlassIds == null) {
      embeddedKlassIds = new ArrayList<String>();
      klassMap.put(IProjectKlass.EMBEDDED_KLASS_IDS, embeddedKlassIds);
    }
    List<String> contextKlassIds = new ArrayList<>();
    for (Vertex contextKlassNode : contextKlasses) {
      String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
      contextKlassIds.add(contextKlassId);
      String natureType = contextKlassNode.getProperty(IKlass.NATURE_TYPE);
      switch (natureType) {
        case CommonConstants.LANGUAGE_KLASS_TYPE:
          klassMap.put(IKlass.LANGUAGE_KLASS_ID, contextKlassId);
          break;
        case CommonConstants.GTIN_KLASS_TYPE:
          klassMap.put(IProjectKlass.GTIN_KLASS_ID, contextKlassId);
          break;
        case CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE:
        case CommonConstants.EMBEDDED_KLASS_TYPE:
          embeddedKlassIds.add(contextKlassId);
          break;
      }
      Map<String, Object> referencedKlassMap = UtilClass
          .getMapFromVertex(Arrays.asList(IIdLabelTypeModel.LABEL, IIdLabelTypeModel.TYPE,
              IKlass.NATURE_TYPE, CommonConstants.CODE_PROPERTY), contextKlassNode);
      referencedKlasses.put(contextKlassId, referencedKlassMap);
    }
    Iterable<Vertex> contextualNodes = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK);
    for (Vertex contextualNode : contextualNodes) {
      String contextKlassIdByIntermediateNode = (String) contextualNode
          .getProperty(IContextKlassModel.CONTEXT_KLASS_ID);
      if (contextKlassIds.contains(contextKlassIdByIntermediateNode)) {
        Map<String, Object> referencedKlassMap = (Map<String, Object>) referencedKlasses
            .get(contextKlassIdByIntermediateNode);
        
        List<Map<String, Object>> tags = new ArrayList<>();
        List<Map<String, Object>> attributes = new ArrayList<>();
        
        referencedKlassMap.put(CommonConstants.TAGS, tags);
        referencedKlassMap.put(CommonConstants.ATTRIBUTES, attributes);
        
        Iterable<Edge> propertyLinks = contextualNode.getEdges(Direction.OUT,
            RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
        
        for (Edge propertyLink : propertyLinks) {
          Map<String, Object> propagableEntityMap = UtilClass.getMapFromEdge(propertyLink);
          Vertex propertyVertex = propertyLink.getVertex(Direction.IN);
          String propagableEntityId = UtilClass.getCodeNew(propertyVertex);
          propagableEntityMap.put(IIdAndCouplingTypeModel.ID, propagableEntityId);
          propagableEntityMap.put(CommonConstants.CODE_PROPERTY, propagableEntityId);
          if (propertyVertex.getProperty(CommonConstants.TYPE_PROPERTY)
              .equals(CommonConstants.TAG_TYPE)) {
            tags.add(propagableEntityMap);
            propagableEntityMap.put(CommonConstants.TYPE, CommonConstants.TAG);
          }
          else {
            attributes.add(propagableEntityMap);
            propagableEntityMap.put(CommonConstants.TYPE, CommonConstants.ATTRIBUTE);
          }
        }
        List<Map<String, Object>> couplingFromSide = RelationshipUtils
            .getCouplingFromSide(referencedKlassMap);
        referencedKlassMap.put("couplings", couplingFromSide);
      }
      klassMap.put("embeddedClasses", referencedKlasses);
    }
  }
  
  public static List<String> getAllNatureKlassNodeIds()
  {
    return getAllNatureKlassNodeIds(null);
  }
  
  public static List<String> getAllNonNatureKlassNodeIds(String baseType)
  {
    List<String> klassIds = new ArrayList<String>();
    
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where "
        + IKlass.IS_NATURE + " is null or " + IKlass.IS_NATURE + " = false";
    if (baseType != null) {
      query = query + " and type = \"" + baseType + "\"";
    }
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klassIds.add(UtilClass.getCodeNew(klassNode));
    }
    return klassIds;
  }
  
  public static List<String> getAllNonNatureKlassNodeIds()
  {
    return getAllNatureKlassNodeIds(null);
  }
  
  @Deprecated
  public static void fillReferencedContextDetails(Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> klassEntityMap = (Map<String, Object>) returnMap
        .get(IGetKlassWithGlobalPermissionModel.KLASS);
    Map<String, Object> referencedContextDetails = getReferencedContextDetails(klassEntityMap);
    returnMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_CONTEXTS, referencedContextDetails);
  }
  
  public static void fillReferencedContextDetails(Map<String, Object> returnMap,
      Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> klassEntityMap = (Map<String, Object>) returnMap
        .get(IGetKlassEntityWithoutKPModel.ENTITY);
    Map<String, Object> referencedContextDetails = getReferencedContextDetails(klassEntityMap);
    configDetails.put(IGetKlassEntityConfigDetailsModel.REFERENCED_CONTEXTS,
        referencedContextDetails);
  }
  
  private static Map<String, Object> getReferencedContextDetails(Map<String, Object> klassEntityMap)
      throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.CODE,
        IConfigEntityInformationModel.LABEL, IConfigEntityInformationModel.TYPE);
    
    Map<String, Object> contextMap = (Map<String, Object>) klassEntityMap.get(IKlass.CONTEXTS);
    List<String> embeddedVariantContexts = (List<String>) contextMap
        .get(IKlassContext.EMBEDDED_VARIANT_CONTEXTS);
    List<String> productVariantContexts = (List<String>) contextMap
        .get(IKlassContext.PRODUCT_VARIANT_CONTEXTS);
    List<String> languageVariantContexts = (List<String>) contextMap
        .get(IKlassContext.LANGUAGE_VARIANT_CONTEXTS);
    Map<String, Object> referencedContextDetails = new HashMap<>();
    
    for (String embeddedVariantContext : embeddedVariantContexts) {
      Vertex embeddedVariantContextNode = UtilClass.getVertexById(embeddedVariantContext,
          VertexLabelConstants.VARIANT_CONTEXT);
      referencedContextDetails.put(embeddedVariantContext,
          UtilClass.getMapFromVertex(fieldsToFetch, embeddedVariantContextNode));
    }
    
    for (String productVariantContext : productVariantContexts) {
      Vertex productVariantContextNode = UtilClass.getVertexById(productVariantContext,
          VertexLabelConstants.VARIANT_CONTEXT);
      referencedContextDetails.put(productVariantContext,
          UtilClass.getMapFromVertex(fieldsToFetch, productVariantContextNode));
    }
    
    for (String languageVariantContext : languageVariantContexts) {
      Vertex languageVariantContextNode = UtilClass.getVertexById(languageVariantContext,
          VertexLabelConstants.VARIANT_CONTEXT);
      referencedContextDetails.put(languageVariantContext,
          UtilClass.getMapFromVertex(fieldsToFetch, languageVariantContextNode));
    }
    
    List<Map<String, Object>> klassSections = (List<Map<String, Object>>) klassEntityMap
        .get(IKlass.SECTIONS);
    for (Map<String, Object> section : klassSections) {
      List<Map<String, Object>> elements = (List<Map<String, Object>>) section
          .get(ISection.ELEMENTS);
      for (Map<String, Object> element : elements) {
        if (!element.get(ISectionElement.TYPE)
            .equals(CommonConstants.ATTRIBUTE)) {
          continue;
        }
        String attributeVariantContextId = (String) element
            .get(ISectionElement.ATTRIBUTE_VARIANT_CONTEXT);
        if (attributeVariantContextId == null
            || referencedContextDetails.containsKey(attributeVariantContextId)) {
          continue;
        }
        Vertex attributeVariantContextNode = UtilClass.getVertexById(attributeVariantContextId,
            VertexLabelConstants.VARIANT_CONTEXT);
        referencedContextDetails.put(attributeVariantContextId,
            UtilClass.getMapFromVertex(fieldsToFetch, attributeVariantContextNode));
      }
    }
    return referencedContextDetails;
  }
  
  public static Vertex createContextNodeForKlass(Vertex klassNode, String contextType)
      throws Exception
  {
    String klasLabel = (String) UtilClass.getValueByLanguage(klassNode, IKlass.LABEL);
    Vertex contextNode = VariantContextUtils.createDefaultContextNode(contextType);
    contextNode.setProperty(EntityUtil.getLanguageConvertedField(IVariantContext.LABEL), klasLabel);
    klassNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, contextNode);
    TabUtils.linkAddedOrDefaultTab(contextNode, null, CommonConstants.CONTEXT);
    return contextNode;
  }
  
  public static Vertex createContextNodeForKlass(Vertex klassNode, String contextType,
      String contextId, String contextCode) throws Exception
  {
    String klasLabel = (String) UtilClass.getValueByLanguage(klassNode, IKlass.LABEL);
    Vertex contextNode = VariantContextUtils.createDefaultContextNode(contextType, contextId,
        contextCode);
    contextNode.setProperty(EntityUtil.getLanguageConvertedField(IVariantContext.LABEL), klasLabel);
    klassNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, contextNode);
    TabUtils.linkAddedOrDefaultTab(contextNode, null, CommonConstants.CONTEXT);
    return contextNode;
  }
  
  public static Vertex createContextNodeForKlass(Vertex klassNode, String contextType,
      String contextCode) throws Exception
  {
    String klasLabel = (String) UtilClass.getValueByLanguage(klassNode, IKlass.LABEL);
    Vertex contextNode = VariantContextUtils.createDefaultContextNode(contextType, "", contextCode);
    contextNode.setProperty(EntityUtil.getLanguageConvertedField(IVariantContext.LABEL), klasLabel);
    klassNode.addEdge(RelationshipLabelConstants.VARIANT_CONTEXT_OF, contextNode);
    TabUtils.linkAddedOrDefaultTab(contextNode, null, CommonConstants.CONTEXT);
    return contextNode;
  }
  
  /**
   * @author Aayush
   * @param klassVertex
   * @return context for the Embedded, GTIN, TechnicalImageVariant klasses
   * @throws MultipleVertexFoundException
   */
  public static Vertex getContextForKlass(Vertex klassVertex) throws MultipleVertexFoundException
  {
    Iterator<Vertex> iterator = klassVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    
    Vertex context = null;
    if (!iterator.hasNext()) {
      return context;
    }
    
    context = iterator.next();
    
    if (iterator.hasNext()) {
      throw new MultipleVertexFoundException();
    }
    
    return context;
  }
  
  public static List<Map<String, Object>> getSelectedTagValuesOfKlassPropertyNode(
      Vertex klassPropertyNode)
  {
    Iterable<Edge> hasSelectedTagValues = klassPropertyNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_TAG_VALUE);
    List<Map<String, Object>> selectedTagValuesList = new ArrayList<>();
    for (Edge edge : hasSelectedTagValues) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      String tagId = UtilClass.getCodeNew(tagValueNode);
      Map<String, Object> selectedTagValue = new HashMap<>();
      selectedTagValue.put(IKlassTagValues.TAG_ID, tagId);
      selectedTagValue.put(IKlassTagValues.LABEL, tagValueNode.getProperty(ITag.LABEL));
      selectedTagValue.put(IKlassTagValues.COLOR, tagValueNode.getProperty(ITag.COLOR));
      selectedTagValue.put(IKlassTagValues.ICON, tagValueNode.getProperty(ITag.ICON));
      selectedTagValuesList.add(selectedTagValue);
    }
    
    return selectedTagValuesList;
  }
  
  public static List<String> getSelectedTagValuesListOfKlassPropertyNode(Vertex klassPropertyNode)
  {
    Iterable<Edge> hasSelectedTagValues = klassPropertyNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_TAG_VALUE);
    List<String> selectedTagValuesList = new ArrayList<>();
    for (Edge edge : hasSelectedTagValues) {
      Vertex tagValueNode = edge.getVertex(Direction.IN);
      String tagId = UtilClass.getCodeNew(tagValueNode);
      selectedTagValuesList.add(tagId);
    }
    
    return selectedTagValuesList;
  }
  
  /**
   * This function returns all nature klasses which are not abstract in sorted
   * order according label
   *
   * @author Lokesh
   * @param baseType
   * @return
   */
  public static List<Vertex> getAllNatureKlassNodes(String baseType)
  {
    List<Vertex> klasses = new ArrayList<>();
    
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where " + "("
        + IKlass.IS_NATURE + " is not null and " + IKlass.IS_NATURE + " = true) AND " + "("
        + IKlass.IS_ABSTRACT + " is null or " + IKlass.IS_ABSTRACT + " = false)";
    if (baseType != null) {
      query = query + " and type = \"" + baseType + "\"";
    }
    query += " order by " + EntityUtil.getLanguageConvertedField(IKlass.LABEL) + " asc";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klasses.add(klassNode);
    }
    return klasses;
  }
  
  public static List<Vertex> getAllNatureKlassNodesForPagination(String baseType, int from, int size, String searhText)
  {
    List<Vertex> klasses = new ArrayList<>();
    String labelColumn = EntityUtil.getLanguageConvertedField(IKlass.LABEL);
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where " + "("
         + IKlass.IS_NATURE + " = true) AND " + "(" + IKlass.IS_ABSTRACT + " = false)";
    if (baseType != null) {
      query = query + " and type = \"" + baseType + "\"";
    }
    if(!searhText.isEmpty()) {
      query = query + " AND " + labelColumn +  " like '%" + searhText + "%' "; 
    }
    query += " order by " + labelColumn + " asc " + "SKIP " + from + " LIMIT " + size;
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klasses.add(klassNode);
    }
    return klasses;
  }
  
  public static long getTotalCountOfKlasses(String baseType, String searhText, boolean isNature){
    
    String labelColumn = EntityUtil.getLanguageConvertedField(IKlass.LABEL);
    String countQuery = "SELECT COUNT(*) as count FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where " + "("
         + IKlass.IS_NATURE + " = " + isNature + ") AND " + "(" + IKlass.IS_ABSTRACT + " = " + false + ")" + " AND code not in 'golden_article_klass'";
    if (baseType != null) {
      countQuery = countQuery + " and type = \"" + baseType + "\"";
    }
    
    if(!searhText.isEmpty()) {
      countQuery = countQuery + " AND " + labelColumn +  " like '%" + searhText + "%' "; 
    }

    return EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
  }

 public static long getTotalCountOfMajorTaxonomies(String clickedTaxonomyId){
    
   if(clickedTaxonomyId == null) {
     String countQuery = "SELECT COUNT(*) as count FROM " + VertexLabelConstants.ROOT_KLASS_TAXONOMY 
         + " where outE('Child_Of').size() = 0 and " + ITaxonomy.TAXONOMY_TYPE + " = '"
             + CommonConstants.MAJOR_TAXONOMY + "'";
     return EntityUtil.executeCountQueryToGetTotalCount(countQuery);
   }
   else {
     String query = "SELECT childCount FROM Root_Klass_Taxonomy where code = " + "'" + clickedTaxonomyId + "'";
     Long count = 0L;
     OrientGraph graph = UtilClass.getGraph();
     Iterable<Vertex> countResult = graph.command(new OCommandSQL(query)).execute();
     Iterator<Vertex> iterator = countResult.iterator();
     if(iterator.hasNext()) {
       Vertex next = iterator.next();
         Integer childCount = next.getProperty("childCount");
         count = childCount.longValue();
         
       }
     return count;
     }
   }
    
  public static List<Vertex> getAllNonNatureKlassNodesForPagination(String baseType, int from, int size, String searhText)
  {
    List<Vertex> klasses = new ArrayList<>();
    String labelColumn = EntityUtil.getLanguageConvertedField(IKlass.LABEL);

    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where ("
        + IKlass.IS_NATURE + " = false AND "
        + IKlass.IS_ABSTRACT + " = \"false\" " + " ) AND "
        + CommonConstants.ORIENTDB_CLASS_PROPERTY + " not in "
        + EntityUtil.quoteIt(VertexLabelConstants.ATTRIBUTION_TAXONOMY) + " AND "
        + CommonConstants.CODE_PROPERTY + " NOT IN "
        + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN);
    
    if (baseType != null) {
      query = query + " and type = \"" + baseType + "\"";
    }
    if(!searhText.isEmpty()) {
      query = query + " AND " + labelColumn +  " like '%" + searhText + "%' "; 
    }
    query += " order by " + labelColumn + " asc " + "SKIP " + from + " LIMIT " + size;
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klasses.add(klassNode);
    }
    return klasses;
  }
  
  /**
   * This function returns all nature klasses which are not abstract in sorted
   * order according label except some cids in list
   *
   * @author meetali
   * @param baseType
   * @return
   */
  public static List<Vertex> getAllNatureKlassNodes(String baseType, String cidToExclude)
  {
    List<Vertex> klasses = new ArrayList<>();
    
    // TODO change label in query by language
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where " + "("
        + IKlass.IS_NATURE + " is not null and " + IKlass.IS_NATURE + " = true) AND " + "("
        + IKlass.IS_ABSTRACT + " is null or " + IKlass.IS_ABSTRACT + " = false)"
        + " AND  code not in " + cidToExclude;
    if (baseType != null) {
      query = query + " and type = \"" + baseType + "\"";
    }
    query += " order by " + EntityUtil.getLanguageConvertedField(IKlass.LABEL) + " asc";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klasses.add(klassNode);
    }
    return klasses;
  }
  
  /**
   * This function returns all non nature klasses which are not abstract in
   * sorted order according label except of those cid list
   *
   * @author Meetali
   * @return
   */
  public static List<Vertex> getAllNonNatureKlassNodes(String baseTypes, String idsToExclude)
  {
    List<Vertex> klasses = new ArrayList<>();
    
    // TODO change label in query by language
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where ("
        + IKlass.IS_NATURE + " is null or " + IKlass.IS_NATURE + " = false AND "
        + IKlass.IS_ABSTRACT + " = \"false\" or " + IKlass.IS_ABSTRACT + " is null) AND "
        + CommonConstants.ORIENTDB_CLASS_PROPERTY + " not in "
        + EntityUtil.quoteIt(VertexLabelConstants.ATTRIBUTION_TAXONOMY) + " AND "
        + CommonConstants.CODE_PROPERTY + " NOT IN "
        + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN)
        + " And code not in " + idsToExclude;
    
    if (baseTypes != null && !baseTypes.isEmpty()) {
      query = query + " and type in " + baseTypes;
    }
    query += " order by " + EntityUtil.getLanguageConvertedField(IKlass.LABEL) + " asc";
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klasses.add(klassNode);
    }
    return klasses;
  }
  
  /**
   * This function returns all non nature klasses which are not abstract in
   * sorted order according label
   *
   * @author Lokesh
   * @param baseType
   * @return
   */
  public static List<Vertex> getAllNonNatureKlassNodes(String baseType)
  {
    List<Vertex> klasses = new ArrayList<>();
    
    // TODO change label in query by language
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where ("
        + IKlass.IS_NATURE + " is null or " + IKlass.IS_NATURE + " = false AND "
        + IKlass.IS_ABSTRACT + " = \"false\" or " + IKlass.IS_ABSTRACT + " is null) AND "
        + CommonConstants.ORIENTDB_CLASS_PROPERTY + " not in "
        + EntityUtil.quoteIt(VertexLabelConstants.ATTRIBUTION_TAXONOMY) + " AND "
        + CommonConstants.CODE_PROPERTY + " NOT IN "
        + EntityUtil.quoteIt(SystemLevelIds.KLASSES_TO_EXCLUDE_FROM_CONFIG_SCREEN);
    
    if (baseType != null) {
      query = query + " and type = \"" + baseType + "\"";
    }
    query += " order by " + EntityUtil.getLanguageConvertedField(IKlass.LABEL) + " asc";
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    for (Vertex klassNode : vertices) {
      klasses.add(klassNode);
    }
    return klasses;
  }
  
  @Deprecated
  public static void fillReferencedTaskDetails(Map<String, Object> getKlassMap) throws Exception
  {
    Map<String, Object> klassEntityMap = (Map<String, Object>) getKlassMap
        .get(IGetKlassWithGlobalPermissionModel.KLASS);
    Map<String, Object> referencedTaskDetails = getReferencedTaskDetails(klassEntityMap);
    getKlassMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_TASKS, referencedTaskDetails);
  }
  
  public static void fillReferencedTaskDetails(Map<String, Object> getKlassMap,
      Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> klassEntityMap = (Map<String, Object>) getKlassMap
        .get(IGetKlassEntityWithoutKPModel.ENTITY);
    Map<String, Object> referencedTaskDetails = getReferencedTaskDetails(klassEntityMap);
    configDetails.put(IGetKlassEntityConfigDetailsModel.REFERENCED_TASKS, referencedTaskDetails);
  }
  
  private static Map<String, Object> getReferencedTaskDetails(Map<String, Object> klassEntityMap)
      throws Exception
  {
    List<String> fieldsToFetch = Arrays.asList(IConfigEntityInformationModel.CODE,
        IConfigEntityInformationModel.LABEL, IConfigEntityInformationModel.TYPE);
    
    List<String> taskIds = (List<String>) klassEntityMap.get(IKlass.TASKS);
    Map<String, Object> referencedTaskDetails = new HashMap<>();
    for (String taskId : taskIds) {
      Vertex taskNode = UtilClass.getVertexById(taskId, VertexLabelConstants.ENTITY_TYPE_TASK);
      Map<String, Object> taskMap = UtilClass.getMapFromVertex(fieldsToFetch, taskNode);
      referencedTaskDetails.put(taskId, taskMap);
    }
    return referencedTaskDetails;
  }
  
  @Deprecated
  public static void fillReferencedDataRuleDetails(Map<String, Object> getKlassMap) throws Exception
  {
    Map<String, Object> klassEntityMap = (Map<String, Object>) getKlassMap
        .get(IGetKlassWithGlobalPermissionModel.KLASS);
    Map<String, String> referencedDataRuleDetails = getReferencedDataRulesDetails(klassEntityMap);
    getKlassMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_DATARULES,
        referencedDataRuleDetails);
  }
  
  public static void fillReferencedDataRuleDetails(Map<String, Object> getKlassMap,
      Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> klassEntityMap = (Map<String, Object>) getKlassMap
        .get(IGetKlassEntityWithoutKPModel.ENTITY);
    Map<String, String> referencedDataRuleDetails = getReferencedDataRulesDetails(klassEntityMap);
    configDetails.put(IGetKlassEntityConfigDetailsModel.REFERENCED_DATARULES,
        referencedDataRuleDetails);
  }
  
  private static Map<String, String> getReferencedDataRulesDetails(
      Map<String, Object> klassEntityMap) throws Exception
  {
    List<String> dataRuleIds = (List<String>) klassEntityMap.get(IKlass.DATA_RULES);
    Map<String, String> referencedDataRuleDetails = new HashMap<>();
    for (String dataRuleId : dataRuleIds) {
      Vertex eventNode = UtilClass.getVertexById(dataRuleId, VertexLabelConstants.DATA_RULE);
      referencedDataRuleDetails.put(dataRuleId,
          (String) UtilClass.getValueByLanguage(eventNode, CommonConstants.LABEL_PROPERTY));
    }
    return referencedDataRuleDetails;
  }
  
  @Deprecated
  public static void fillTabDetailsAssociatedWithNatureRelationship(
      Map<String, Object> returnKlassMap) throws Exception
  {
    Map<String, Object> referencedTabs = new HashMap<>();
    getReferencedTabsDetails(returnKlassMap, referencedTabs);
    returnKlassMap.put(IGetKlassWithGlobalPermissionModel.REFERENCED_TABS, referencedTabs);
  }
  
  @Deprecated
  private static void getReferencedTabsDetails(Map<String, Object> returnKlassMap,
      Map<String, Object> referencedTabs) throws Exception
  {
    Map<String, Object> klassDetails = (Map<String, Object>) returnKlassMap
        .get(IGetKlassWithGlobalPermissionModel.KLASS);
    List<Map<String, Object>> natureRelationships = (List<Map<String, Object>>) klassDetails
        .get(IKlass.RELATIONSHIPS);
    for (Map<String, Object> natureRelationship : natureRelationships) {
      String natureRelationshipId = (String) natureRelationship.get(IKlassNatureRelationship.ID);
      Vertex natureRelationshipVertex = UtilClass.getVertexById(natureRelationshipId,
          VertexLabelConstants.NATURE_RELATIONSHIP);
      Iterable<Vertex> tabVertices = natureRelationshipVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAB);
      for (Vertex tab : tabVertices) {
        Map<String, Object> tabMap = new HashMap<>();
        tabMap.put(IIdLabelModel.ID, UtilClass.getCodeNew(tab));
        tabMap.put(IIdLabelModel.LABEL,
            UtilClass.getValueByLanguage(tab, CommonConstants.LABEL_PROPERTY));
        referencedTabs.put(natureRelationshipId, tabMap);
      }
    }
  }
  
  public static void fillTabDetailsAssociatedWithNatureRelationship(
      Map<String, Object> returnKlassMap, Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> referencedTabs = new HashMap<>();
    getReferencedTabDetails(returnKlassMap, referencedTabs);
    configDetails.put(IGetKlassEntityConfigDetailsModel.REFERENCED_TABS, referencedTabs);
  }
  
  private static void getReferencedTabDetails(Map<String, Object> returnKlassMap,
      Map<String, Object> referencedTabs) throws Exception
  {
    Map<String, Object> klassDetails = (Map<String, Object>) returnKlassMap
        .get(IGetKlassEntityWithoutKPModel.ENTITY);
    List<Map<String, Object>> natureRelationships = (List<Map<String, Object>>) klassDetails
        .get(IKlass.RELATIONSHIPS);
    for (Map<String, Object> natureRelationship : natureRelationships) {
      String natureRelationshipId = (String) natureRelationship.get(IKlassNatureRelationship.ID);
      Vertex natureRelationshipVertex = UtilClass.getVertexById(natureRelationshipId,
          VertexLabelConstants.NATURE_RELATIONSHIP);
      Iterable<Vertex> tabVertices = natureRelationshipVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAB);
      for (Vertex tab : tabVertices) {
        Map<String, Object> tabMap = UtilClass.getMapFromVertex(
            Arrays.asList(IConfigEntityInformationModel.ID, IConfigEntityInformationModel.LABEL,
                IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.ICON),
            tab);
        referencedTabs.put((String) tabMap.get(ITab.ID), tabMap);
      }
    }
  }
  
  public static List<Vertex> getKlassAndChildNodes(Vertex klassNode)
  {
    OrientGraph graph = UtilClass.getGraph();
    List<Vertex> klassAndChildNodes = new ArrayList<>();
    String rid = klassNode.getId()
        .toString();
    Iterable<Vertex> i = graph
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
        .execute();
    for (Vertex node : i) {
      klassAndChildNodes.add(node);
    }
    return klassAndChildNodes;
  }
  
  public static String getDataRulesQuery(Vertex klass, String organisationId,
      String physicalCatalogId, String endpointId, String hasEntityRuleLink)
  {
    String query = "SELECT FROM (SELECT EXPAND(OUT('" + hasEntityRuleLink + "')) FROM "
        + klass.getId() + " )";
    
    // get rule if
    // 1. rule is connected with the provided organization
    // 2. rule is not link with any organization(i.e it is link with all the
    // organization)
    query = query + " WHERE (( OUT('" + RelationshipLabelConstants.ORGANISATION_RULE_LINK
        + "').code CONTAINS '" + organisationId + "' ) OR OUT('"
        + RelationshipLabelConstants.ORGANISATION_RULE_LINK + "').size() = 0 ) AND ";
    
    // get rule if
    // 1. physicalCatalogIds contains physicalCatalogId
    // 2. physicalCatalogIdy is empty(i.e applicable for all physicalCatalogs)
    query = query + " ( " + IDataRule.PHYSICAL_CATALOG_IDS + " CONTAINS '" + physicalCatalogId
        + "' OR " + IDataRule.PHYSICAL_CATALOG_IDS + ".size() = 0 ) ";
    
    if (physicalCatalogId.equals(Constants.DATA_INTEGRATION_CATALOG_IDS)) {
      // get rule if
      // 1. rule is connected with the provided endpoint
      // 2. rule is not link with any endpoint(i.e it is link with all the
      // endpoint)
      query = query + " AND (( OUT('" + RelationshipLabelConstants.RULE_ENDPOINT_LINK
          + "').code CONTAINS '" + endpointId + "' ) OR OUT('"
          + RelationshipLabelConstants.RULE_ENDPOINT_LINK + "').size() = 0)";
    }
    
    return query;
  }
  
  protected static Iterable<Vertex> executeQuery(String query)
  {
    Iterable<Vertex> dataRuleVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    return dataRuleVertices;
  }
  
  public static void fillConfigDetailsForSide(Map<String, Object> referencedAttributes,
      Map<String, Object> referencedTags, Map<String, Object> referencedRelationships,
      Map<String, Object> referencedKlasses, Map<String, Object> referencedContexts,
      Map<String, Object> sideMap) throws Exception
  {
    List<Map<String, Object>> attributes = (List<Map<String, Object>>) sideMap
        .get(IRelationshipSide.ATTRIBUTES);
    List<Map<String, Object>> allAttributes = new ArrayList<>();
    allAttributes.addAll(attributes);
    for (Map<String, Object> attribute : attributes) {
      String attributeId = (String) attribute.get(IReferencedRelationshipProperty.ID);
      if (!referencedAttributes.containsKey(attributeId)) {
        Vertex attributeVertex = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        referencedAttributes.put(attributeId, AttributeUtils.getAttributeMap(attributeVertex));
      }
    }
    List<Map<String, Object>> tags = (List<Map<String, Object>>) sideMap
        .get(IRelationshipSide.TAGS);
    List<Map<String, Object>> allTags = new ArrayList<>();
    allTags.addAll(tags);
    for (Map<String, Object> tag : tags) {
      String tagId = (String) tag.get(IReferencedRelationshipProperty.ID);
      if (!referencedTags.containsKey(tagId)) {
        Vertex tagNode = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
        referencedTags.put(tagId, TagUtils.getTagMap(tagNode, true));
      }
    }
    
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) sideMap
        .get(IRelationshipSide.RELATIONSHIPS);
    for (Map<String, Object> relationship : relationships) {
      String relationshipId = (String) relationship.get(IReferencedRelationshipProperty.ID);
      if (!referencedRelationships.containsKey(relationshipId)) {
        Vertex relationshipNode = UtilClass.getVertexByIndexedId(relationshipId,
            VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
        referencedRelationships
            .put(relationshipId,
                UtilClass.getMapFromVertex(
                    Arrays.asList(IConfigEntityInformationModel.ID,
                        IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.LABEL),
                    relationshipNode));
      }
    }
    
    String klassId = (String) sideMap.get(IRelationshipSide.KLASS_ID);
    Map<String, Object> klassMap = new HashMap<>();
    Vertex klassVertex = UtilClass.getVertexByIndexedId(klassId,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    klassMap.put(IConfigEntityInformationModel.ID, klassId);
    klassMap.put(IConfigEntityInformationModel.LABEL,
        (String) UtilClass.getValueByLanguage(klassVertex, IKlassModel.LABEL));
    klassMap.put(IConfigEntityInformationModel.TYPE, klassVertex.getProperty(IKlassModel.TYPE));
    klassMap.put(IConfigEntityInformationModel.ICON, klassVertex.getProperty(IKlassModel.ICON));
    klassMap.put(IConfigEntityInformationModel.CODE, klassVertex.getProperty(IKlass.CODE));
    referencedKlasses.put(klassId, klassMap);
    
    String contextId = (String) sideMap.get(IRelationshipSide.CONTEXT_ID);
    if (contextId != null) {
      Map<String, Object> contextMap = new HashMap<>();
      Vertex contextVertex = UtilClass.getVertexById(contextId,
          VertexLabelConstants.VARIANT_CONTEXT);
      
      contextMap = UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
          IVariantContextModel.LABEL, IVariantContextModel.TYPE, IVariantContextModel.ICON),
          contextVertex);
      referencedContexts.put(contextId, contextMap);
    }
  }
  
  public static void getParentInfoToKlassEntityMap(Vertex klassNode,
      Map<String, Object> klassEntity)
  {
    Iterator<Vertex> parentKlasses = klassNode.getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF).iterator();
    boolean hasParent = parentKlasses.hasNext();
    String parentCode = hasParent ? parentKlasses.next().getProperty(IConfigEntity.CODE) : CommonConstants.STANDARD_PARENT_CODE;
    klassEntity.put(ConfigTag.parentCode.toString(), parentCode);
  }
  
  public static void manageADMForPropertiesToExportForRelationship(Vertex klassNode, Map<String, Object> relationshipExport)
      throws Exception
  {
    //Handling for relationships
    addEntityForRelationshipExport(klassNode, (List<String>) relationshipExport
        .get(ISaveRelationshipToExportModel.ADDED_RELATIONSHIP_TO_EXPORT), VertexLabelConstants.ROOT_RELATIONSHIP, RelationshipLabelConstants.HAS_RELATIONSHIP_TO_EXPORT);
    deleteEntityForRelationshipExport(klassNode, (List<String>) relationshipExport
        .get(ISaveRelationshipToExportModel.DELETED_RELATIONSHIP_TO_EXPORT), RelationshipLabelConstants.HAS_RELATIONSHIP_TO_EXPORT);
    
    //Handling for attributes
    addEntityForRelationshipExport(klassNode,  (List<String>) relationshipExport
        .get(ISaveRelationshipToExportModel.ADDED_ATTRIBUTES_TO_EXPORT), VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE, RelationshipLabelConstants.HAS_ATTRIBUTE_TO_EXPORT);
    deleteEntityForRelationshipExport(klassNode, (List<String>) relationshipExport
        .get(ISaveRelationshipToExportModel.DELETED_ATTRIBUTES_TO_EXPORT), RelationshipLabelConstants.HAS_ATTRIBUTE_TO_EXPORT);
    
    //Handling for tags
    addEntityForRelationshipExport(klassNode,  (List<String>) relationshipExport
        .get(ISaveRelationshipToExportModel.ADDED_TAGS_TO_EXPORT), VertexLabelConstants.ENTITY_TAG, RelationshipLabelConstants.HAS_TAG_TO_EXPORT);
    deleteEntityForRelationshipExport(klassNode, (List<String>) relationshipExport
        .get(ISaveRelationshipToExportModel.DELETED_TAGS_TO_EXPORT), RelationshipLabelConstants.HAS_TAG_TO_EXPORT);
  }
  
  public static void addEntityForRelationshipExport(Vertex klassNode, List<String> addedEntities,
      String entityType, String relationshipLabel) throws Exception
  {
    for (String addedEntity : addedEntities) {
      Vertex attributeNode = UtilClass.getVertexById(addedEntity, entityType);
      klassNode.addEdge(relationshipLabel, attributeNode);
    }
  }
  
  public static void deleteEntityForRelationshipExport(Vertex klassNode,
      List<String> deletedEntities,String relationshipLabel) throws Exception
  {
    Iterable<Edge> entityEdges = klassNode.getEdges(Direction.OUT, relationshipLabel);
    for (Edge entityEdge : entityEdges) {
      Vertex entityNode = entityEdge.getVertex(Direction.IN);
      if (deletedEntities.contains(UtilClass.getCodeNew(entityNode))) {
        entityEdge.remove();
      }
   }
  }
  
  public static void inheritParentEmbeddedKlasses(Vertex childKlassNode, Vertex parentKlassNode) throws Exception
  {
    List<Map<String, Object>> inheritedEmbeddedKlasses = new ArrayList<Map<String, Object>>();
    Iterable<Vertex> embeddedKlassNodes = parentKlassNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex embeddedKlassNode : embeddedKlassNodes) {
      if (!UtilClass.fetchEdgeBetweenTwoNodes(childKlassNode, embeddedKlassNode, RelationshipLabelConstants.HAS_CONTEXT_KLASS)) {
        Map<String, Object> inheritedEmbeddedMap = new HashMap<String, Object>();
        inheritedEmbeddedMap.put(IContextKlassModel.CONTEXT_KLASS_ID, UtilClass.getCId(embeddedKlassNode));
        inheritedEmbeddedMap.put(IContextKlassModel.ATTRIBUTES, new ArrayList<>());
        inheritedEmbeddedMap.put(IContextKlassModel.TAGS, new ArrayList<>());
        inheritedEmbeddedMap.put(IContextKlassModel.CONTEXT_ID, null);
        inheritedEmbeddedKlasses.add(inheritedEmbeddedMap);
      }
    }
    VariantContextUtils.manageAddedContextKlasses(childKlassNode, inheritedEmbeddedKlasses, new HashMap<String, Map<String, Object>>());
  }

  /**
   * Get All Nature Relationship of Particular Class
   * @param klassNode
   * @return relationship Code Vs Relationship Map
   * @throws Exception
   */
  public static Map<String, Object> getNatureRelationships(Vertex klassNode) throws Exception
  {
    Map<String, Object> natureRelationships = new HashMap<>();
    Iterator<Vertex> natureRelationKPs = klassNode.getVertices(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF)
        .iterator();
    while (natureRelationKPs.hasNext()) {
      Vertex natureRelationKP = natureRelationKPs.next();
      Iterator<Vertex> relationships = natureRelationKP.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY).iterator();
      while (relationships.hasNext()) {
        Vertex relationshipNode = relationships.next();
        Map<String, Object> relationshipMap = UtilClass.getMapFromVertex(new ArrayList<>(), relationshipNode);

        Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(relationshipNode,
            Arrays.asList(CommonConstants.CODE_PROPERTY));
        relationshipMap.put(IRelationship.TAB_ID, referencedTab.get(ITabModel.ID));

        RelationshipUtils.populatePropetiesInfo(relationshipNode, relationshipMap);
        String code = relationshipNode.getProperty(CommonConstants.CODE_PROPERTY);
        natureRelationships.put(code, relationshipMap);
      }
    }
    return natureRelationships;
  }

}
