package com.cs.config.strategy.plugin.usecase.klass.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.globalpermissions.IKlassTaxonomyPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyCollectionPermissions;
import com.cs.core.config.interactor.entity.globalpermissions.IPropertyPermissions;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.attribute.AttributeNotFoundException;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.klass.IConflictingValueModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedPermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedRolePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionTagModel;
import com.cs.core.config.interactor.model.klass.IValueIdModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class MultiClassificationUtils {
  
  protected static final List<String> DEFAULT_PROPERTY_IDS_FOR_INSTANCES = Arrays.asList(
      IStandardConfig.StandardProperty.nameattribute.toString(),
      IStandardConfig.StandardProperty.createdbyattribute.toString(),
      IStandardConfig.StandardProperty.createdonattribute.toString(),
      IStandardConfig.StandardProperty.lastmodifiedattribute.toString(),
      IStandardConfig.StandardProperty.lastmodifiedbyattribute.toString());
  
  public static Map<String, Object> getMultiCLassificationKlassDetailsMap(List<String> klassIds,
      String nodeLabel, String taxonomyNodeLabel, Set<String> allowedEntities,
      List<String> taxonomyIds, String loginUserId, Boolean isUnlinkedRelationships,
      List<String> taxonomyIdsToGetDetailsFor) throws Exception
  {
    if (taxonomyIds == null) {
      taxonomyIds = new ArrayList<>();
    }
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedKlassMap = new HashMap<>();
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    Map<String, Object> rolePermissionMap = new HashMap<>();
    Map<String, Object> referencedSectionElementMap = new HashMap<>();
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedRoleMap = new HashMap<>();
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Set<String>> referencedNotificationSettings = new HashMap<>();
    Map<String, Object> referencedRelationships = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    Map<String, Object> languageVariantContexts = new HashMap<>();
    Map<String, Object> productVariantContexts = new HashMap<>();
    Map<String, Object> promotionalVariantContexts = new HashMap<>();
    List<String> referencedLifeCycleStatusTags = new ArrayList<>();
    Map<String, String> referencedRelationshipsMapping = new HashMap<>();
    
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFRENCED_KLASSES, referencedKlassMap);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_PROPERTY_COLLECTIONS,
        referencedPropertyCollectionMap);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_PERMISSION,
        rolePermissionMap);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_ELEMENTS,
        referencedSectionElementMap);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_ATTRIBUTES,
        referencedAttributeMap);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS, referencedTagMap);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_ROLES, referencedRoleMap);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_DATA_RULES,
        referencedDataRuleMap.values());
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, 1);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.NOTIFICATION_SETTINGS,
        referencedNotificationSettings);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS,
        referencedRelationships);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_LIFECYCLE_STATUS_TAGS,
        referencedLifeCycleStatusTags);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS_MAPPING,
        referencedRelationshipsMapping);
    
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        embeddedVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS,
        languageVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS,
        productVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.PROMOTIONAL_VERSION_CONTEXTS,
        promotionalVariantContexts);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAXONOMIES,
        referencedTaxonomies);
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.REFERENCED_NATURE_RELATIONSHIPS,
        referencedNatureRelationships);
    
    /*Iterable<Vertex> i = UtilClass.getGraph().command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TYPE_ROLE + " order by " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();
    Set<String> roleIds = new HashSet<>();
    for (Vertex roleNode : i) {
      roleIds.add(UtilClass.getCode(roleNode));
    }*/
    
    // get all roleIds for current logged in user..
    String roleId = RoleUtils.getRoleIdFromUser(loginUserId);
    
    // get all klass properties..
    for (String klassId : klassIds) {
      Vertex klassVertex = null;
      try {
        klassVertex = UtilClass.getVertexById(klassId, nodeLabel);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.NATURE_TYPE, IKlass.IS_NATURE);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      referencedKlassMap.put(klassId, klassMap);
      
      Integer numberOfVersionsToMaintain = (Integer) klassMap
          .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
          .get(IGetMultiClassificationKlassDetailsModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
        mapToReturn.put(IGetMultiClassificationKlassDetailsModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
            numberOfVersionsToMaintain);
      }
      
      fillPropertyCollectionsData(klassVertex, klassId, klassMap, referencedPropertyCollectionMap,
          mapToReturn, allowedEntities, roleId, rolePermissionMap);
      
      fillReferencedTagsAndLifeCycleStatusTags(klassVertex, referencedTagMap,
          referencedLifeCycleStatusTags);
      
      fillDataRulesOfKlass(klassVertex, referencedDataRuleMap);
      fillVariantContextsOfKlass(klassVertex, referencedVariantContextsMap, mapToReturn, klassId,
          allowedEntities, roleId, rolePermissionMap);
      
      Iterator<Vertex> klassNatures = klassVertex
          .getVertices(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF)
          .iterator();
      while (klassNatures.hasNext()) {
        Vertex natureNode = klassNatures.next();
        if (natureNode.getProperty(IKlassNatureRelationship.RELATIONSHIP_TYPE)
            .equals(CommonConstants.PRODUCT_VARIANT_RELATIONSHIP)) {
          fillVariantContextsOfKlass(natureNode, referencedVariantContextsMap, mapToReturn, klassId,
              allowedEntities, roleId, rolePermissionMap);
        }
      }
    }
    
    // add relationship of klass having no section
    // if(isUnlinkedRelationships){
    Map<String, Object> relationshipsMap = new HashMap<>();
    Map<String, Object> referencedElementMap = new HashMap<>();
    Map<String, String> referencedUnlinkedRelationshipMapping = new HashMap<>();
    
    RelationshipUtils.getRelationshipIdsNotLinkedWithKlass(klassIds, relationshipsMap,
        referencedElementMap, referencedUnlinkedRelationshipMapping);
    
    for (String key : relationshipsMap.keySet()) {
      Map<String, Object> map = (Map<String, Object>) relationshipsMap.get(key);
      if (map.containsKey(IKlassNatureRelationship.RELATIONSHIP_TYPE)) {
        referencedNatureRelationships.put(key, map);
      }
      else {
        referencedRelationships.put(key, map);
      }
      Map<String, Object> referencedElement = (Map<String, Object>) referencedElementMap.get(key);
      referencedElement.put(IReferencedSectionRelationshipModel.IS_LINKED, false);
    }
    referencedSectionElementMap.putAll(referencedElementMap);
    referencedRelationshipsMapping.putAll(referencedUnlinkedRelationshipMapping);
    // get all taxonomy properties...
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexById(taxonomyId, taxonomyNodeLabel);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
          ITaxonomy.ICON);
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(fieldsToFetch, taxonomyVertex);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      referencedTaxonomies.put(taxonomyId, taxonomyMap);
      
      fillPropertyCollectionsData(taxonomyVertex, taxonomyId, taxonomyMap,
          referencedPropertyCollectionMap, mapToReturn, allowedEntities, roleId, rolePermissionMap);
    }
    
    List<String> entityIds = new ArrayList<>();
    entityIds.addAll(klassIds);
    entityIds.addAll(taxonomyIds);
    
    rolePermissionMap.put(IReferencedRolePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS,
        new HashSet<>());
    rolePermissionMap.put(IReferencedRolePermissionModel.OPENED_PROPERTY_COLLECTION_IDS,
        new HashSet<>());
    rolePermissionMap.put(IReferencedRolePermissionModel.ENABLED_SECTION_ELEMENT_IDS,
        new HashSet<>());
    rolePermissionMap.put(IReferencedRolePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<>());
    rolePermissionMap.put(IReferencedRolePermissionModel.EDITABLE_KLASS_AND_TAXONOMY_IDS,
        new HashSet<>());
    
    Map<String, Object> globalPermission = new HashMap<>();
    if (loginUserId.equals(IStandardConfig.StandardUser.admin.toString())) {
      // if (true) {
      globalPermission.put(IKlassTaxonomyPermissions.CAN_CREATE, true);
      globalPermission.put(IKlassTaxonomyPermissions.CAN_READ, true);
      globalPermission.put(IKlassTaxonomyPermissions.CAN_EDIT, true);
      globalPermission.put(IKlassTaxonomyPermissions.CAN_DELETE, true);
    }
    else {
      globalPermission = GlobalPermissionUtils.getGlobalPermission(roleId, entityIds);
      managePermissionDetails(roleId, mapToReturn);
    }
    mapToReturn.put(IGetMultiClassificationKlassDetailsModel.GLOBAL_PERMISSION, globalPermission);
    
    return mapToReturn;
  }
  
  private static void fillReferencedTagsAndLifeCycleStatusTags(Vertex klassVertex,
      Map<String, Object> referencedTagMap, List<String> referencedLifeCycleStatusTags)
      throws Exception
  {
    Iterable<Vertex> linkedLifeCycleStatusTags = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);
    for (Vertex linkedLifeCycleStatusTag : linkedLifeCycleStatusTags) {
      String id = linkedLifeCycleStatusTag.getProperty(CommonConstants.CODE_PROPERTY);
      if (!referencedLifeCycleStatusTags.contains(id)) {
        referencedLifeCycleStatusTags.add(id);
      }
      Vertex linkedTagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
      String tagId = (String) referencedTag.get(ITag.ID);
      referencedTagMap.put(tagId, referencedTag);
    }
  }
  
  private static void fillReferencedTagsAndStatusTags(Vertex contextNode,
      Map<String, Object> referencedTagMap, Map<String, Object> variantContextMap) throws Exception
  {
    Iterable<Vertex> linkedStatusTags = contextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.STATUS_TAG_TYPE_LINK);
    List<String> referencedStatusTagList = new ArrayList<>();
    for (Vertex linkedStatusTag : linkedStatusTags) {
      String id = linkedStatusTag.getProperty(CommonConstants.CODE_PROPERTY);
      referencedStatusTagList.add(id);
      Vertex linkedTagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
      String tagId = (String) referencedTag.get(ITag.ID);
      referencedTagMap.put(tagId, referencedTag);
    }
    variantContextMap.put(IReferencedVariantContextModel.REFERENCED_STATUS_TAGS,
        referencedStatusTagList);
  }
  
  private static void fillTaxonomiesChildrenAndParentData(Map<String, Object> taxonomyMap,
      Vertex taxonomyVertex) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = taxonomyVertex.getId()
        .toString();
    List<Map<String, Object>> taxonomiesChildrenList = new ArrayList<>();
    Map<String, Object> taxonomiesParentMap = new HashMap<>();
    
    List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL,
        ITaxonomy.ICON);
    String query = "select from(traverse in('Child_Of') from " + rid
        + " strategy BREADTH_FIRST) where @rid <> " + taxonomyVertex.getId();
    
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex childNode : resultIterable) {
      taxonomiesChildrenList.add(UtilClass.getMapFromVertex(fieldsToFetch, childNode));
    }
    
    taxonomyMap.put(IReferencedArticleTaxonomyModel.CHILDREN, taxonomiesChildrenList);
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PARENT, taxonomiesParentMap);
    fillParentsData(fieldsToFetch, taxonomiesParentMap, taxonomyVertex);
  }
  
  public static String fillParentsData(List<String> fieldsToFetch,
      Map<String, Object> taxonomiesParentMap, Vertex taxonomyVertex) throws Exception
  {
    Vertex parentNode = AttributionTaxonomyUtil.getParentTaxonomy(taxonomyVertex);
    if (parentNode == null) {
      taxonomiesParentMap.put(IReferencedTaxonomyParentModel.ID, "-1");
      return UtilClass.getCodeNew(taxonomyVertex);
    }
    Map<String, Object> parentMap = new HashMap<>();
    taxonomiesParentMap.putAll(UtilClass.getMapFromVertex(fieldsToFetch, parentNode));
    taxonomiesParentMap.put(IReferencedTaxonomyParentModel.PARENT, parentMap);
    return fillParentsData(fieldsToFetch, parentMap, parentNode);
  }
  
  private static void fillPropertyCollectionsData(Vertex klassVertex, String klassId,
      Map<String, Object> klassMap, Map<String, Object> referencedPropertyCollectionMap,
      Map<String, Object> mapToReturn, Set<String> allowedEntities, String roleId,
      Map<String, Object> rolePermissionMap) throws Exception
  {
    List<Map<String, Object>> sections = new ArrayList<>();
    Iterator<Vertex> sectionNodes = klassVertex
        .getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF)
        .iterator();
    
    while (sectionNodes.hasNext()) {
      Vertex klassSectionNode = sectionNodes.next();
      Edge sectionOf = KlassUtils.getSectionOfEdgeFromKlassAndSection(klassVertex,
          klassSectionNode);
      Vertex propertyCollectionNode = KlassUtils
          .getPropertyCollectionNodeFromKlassSectionNode(klassSectionNode);
      String propertyCollectionId = UtilClass.getCodeNew(propertyCollectionNode);
      
      Map<String, Object> referencedPropertyCollection = UtilClass
          .getMapFromVertex(new ArrayList<>(), propertyCollectionNode);
      Boolean isSkipped = sectionOf.getProperty(ISection.IS_SKIPPED);
      
      if (isSkipped == null || !isSkipped) {
        Map<String, Object> sectionMap = new HashMap<>();
        sectionMap.put(CommonConstants.ID_PROPERTY, propertyCollectionId);
        referencedPropertyCollection.put(IReferencedPropertyCollectionModel.ID,
            propertyCollectionId);
        sections.add(sectionMap);
        
        if (referencedPropertyCollectionMap.get(propertyCollectionId) == null) {
          referencedPropertyCollectionMap.put(propertyCollectionId, referencedPropertyCollection);
        }
        else {
          // TODO: merge
        }
        
        // TODO: fillPropertyCollectionPermission(klassVertex,
        // propertyCollectionId,
        // klassSectionNode, rolePermissionMap, roleIds);
        fillReferencedElements(mapToReturn, klassVertex, propertyCollectionNode, roleId,
            allowedEntities);
      }
      Iterator<Edge> previousSectionOfEdges = klassSectionNode
          .getEdges(Direction.IN, RelationshipLabelConstants.PREVIOUS_SECTION)
          .iterator();
      klassSectionNode = null;
      while (previousSectionOfEdges.hasNext()) {
        Edge previousSectionOf = previousSectionOfEdges.next();
        List<String> utilizingKlassIds = previousSectionOf
            .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
        if (utilizingKlassIds.contains(klassId)) {
          klassSectionNode = previousSectionOf.getVertex(Direction.OUT);
          break;
        }
      }
    }
    
    klassMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS, sections);
    
    Map<String, Object> klassNature = KlassUtils.getKlassNature(klassVertex, null);
    
    List<Map<String, Object>> natureRelationships = (List<Map<String, Object>>) klassNature
        .get(IKlass.RELATIONSHIPS);
    Map<String, Object> returnNatureRelationship = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_NATURE_RELATIONSHIPS);
    if (natureRelationships != null && natureRelationships.size() > 0) {
      for (Map<String, Object> natureRelationship : natureRelationships) {
        // Map<String, Object> natureRelationship = natureRelationships.get(0);
        Map<String, Object> propertyCollection = (Map<String, Object>) natureRelationship
            .remove(IKlassNatureRelationship.PROPERTY_COLLECTION);
        if (propertyCollection != null) {
          String propertyCollectionId = (String) propertyCollection.get(IPropertyCollection.ID);
          Vertex propertyCollectionNode = UtilClass.getVertexById(propertyCollectionId,
              VertexLabelConstants.PROPERTY_COLLECTION);
          natureRelationship.put(IGetReferencedNatureRelationshipModel.PROPERTY_COLLECTION_ID,
              propertyCollectionId);
          Map<String, Object> referencedPropertyCollections = (Map<String, Object>) mapToReturn
              .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_PROPERTY_COLLECTIONS);
          Map<String, Object> propertyCollectionMap = (Map<String, Object>) referencedPropertyCollections
              .get(UtilClass.getCodeNew(propertyCollectionNode));
          if (propertyCollectionMap == null) {
            referencedPropertyCollections.put(UtilClass.getCodeNew(propertyCollectionNode),
                UtilClass.getMapFromVertex(new ArrayList<>(), propertyCollectionNode));
            fillNatureReferencedElements(mapToReturn, klassVertex, propertyCollectionNode, roleId,
                allowedEntities);
          }
        }
        
        natureRelationship.put(IReferencedNatureRelationshipModel.NATURE_TYPE,
            klassNature.get(IKlass.NATURE_TYPE));
        String entityId = (String) natureRelationship.get(IRelationship.ID);
        returnNatureRelationship.put(entityId, natureRelationship);
        
        Map<String, Object> referencedElementsMap = (Map<String, Object>) mapToReturn
            .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ELEMENTS);
        
        Map<String, Object> referencedRelationshipsMapping = (Map<String, Object>) mapToReturn
            .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS_MAPPING);
        
        referencedRelationshipsMapping.put(entityId, UtilClass.getCodeNew(klassVertex));
        
        Vertex klassPropertyNode = KlassUtils.getKlassNatureRelationshipPropertyNode(klassVertex,
            entityId);
        Map<String, Object> referencedElement = UtilClass.getMapFromVertex(new ArrayList<>(),
            klassPropertyNode);
        referencedElement.remove("tagGroups"); // remove tag groups from klass
        // property node
        referencedElement.put(IReferencedSectionElementModel.ID, entityId);
        
        Map<String, Object> side = (Map<String, Object>) referencedElement
            .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
        side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY,
            UtilClass.getCodeNew(klassPropertyNode));
        
        referencedElement.put("type", "relationship");
        referencedElement.put(IReferencedSectionRelationshipModel.IS_LINKED, true);
        referencedElementsMap.put(entityId, referencedElement);
      }
    }
  }
  
  private static void managePermissionDetails(String roleId,
      Map<String, Object> multiClassificationDetails) throws Exception
  {
    Map<String, Object> rolePermissionMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_PERMISSION);
    Set<String> visiblePropertyCollectionIds = new HashSet<>();
    Set<String> openedPropertyCollectionIds = new HashSet<>();
    Set<String> editablePropertyCollectionIds = new HashSet<>();
    Set<String> enabledSectionElementIds = new HashSet<>();
    Set<String> ediatbleKlassAndTaxonomyIds = new HashSet<>();
    
    Map<String, Object> referencedKlasses = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFRENCED_KLASSES);
    for (Map.Entry<String, Object> entry : referencedKlasses.entrySet()) {
      String klassId = entry.getKey();
      Map<String, Object> klass = (Map<String, Object>) entry.getValue();
      Boolean isNature = (Boolean) klass.remove(IKlass.IS_NATURE);
      if (isNature != null && isNature) {
        fillKlassAndTaxonomyPermissionDetails(roleId, klassId, ediatbleKlassAndTaxonomyIds,
            multiClassificationDetails, true);
      }
      else {
        fillKlassAndTaxonomyPermissionDetails(roleId, klassId, ediatbleKlassAndTaxonomyIds,
            multiClassificationDetails, false);
      }
    }
    
    Map<String, Object> referencedTaxonmies = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAXONOMIES);
    for (String taxonomyId : referencedTaxonmies.keySet()) {
      fillKlassAndTaxonomyPermissionDetails(roleId, taxonomyId, ediatbleKlassAndTaxonomyIds,
          multiClassificationDetails, false);
    }
    
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_PROPERTY_COLLECTIONS);
    for (String propertyCollectionId : referencedPropertyCollections.keySet()) {
      fillPropertyCollectionPermissionDetails(roleId, propertyCollectionId,
          visiblePropertyCollectionIds, openedPropertyCollectionIds, editablePropertyCollectionIds);
    }
    
    Map<String, Object> referencedElements = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ELEMENTS);
    Set<String> propertyIds = new HashSet<>(referencedElements.keySet());
    propertyIds.addAll(DEFAULT_PROPERTY_IDS_FOR_INSTANCES);
    
    for (String elementId : propertyIds) {
      fillPropertyPermissionDetails(roleId, enabledSectionElementIds, elementId);
    }
    
    ((Set<String>) rolePermissionMap
        .get(IReferencedRolePermissionModel.ENABLED_SECTION_ELEMENT_IDS))
            .addAll(enabledSectionElementIds);
    ((Set<String>) rolePermissionMap
        .get(IReferencedRolePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS))
            .addAll(editablePropertyCollectionIds);
    ((Set<String>) rolePermissionMap
        .get(IReferencedRolePermissionModel.OPENED_PROPERTY_COLLECTION_IDS))
            .addAll(openedPropertyCollectionIds);
    ((Set<String>) rolePermissionMap
        .get(IReferencedRolePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS))
            .addAll(visiblePropertyCollectionIds);
    ((Set<String>) rolePermissionMap
        .get(IReferencedRolePermissionModel.EDITABLE_KLASS_AND_TAXONOMY_IDS))
            .addAll(ediatbleKlassAndTaxonomyIds);
  }
  
  private static void fillKlassAndTaxonomyPermissionDetails(String roleId, String klassId,
      Set<String> ediatbleKlassIds, Map<String, Object> multiClassificationDetails,
      boolean isNature) throws NotFoundException, MultipleVertexFoundException
  {
    Map<String, Object> klassAndTaxonomyPermission = GlobalPermissionUtils
        .getKlassAndTaxonomyPermission(klassId, roleId);
    Boolean isEditable = (Boolean) klassAndTaxonomyPermission
        .get(IKlassTaxonomyPermissions.CAN_EDIT);
    if (isNature) {
      multiClassificationDetails.put(IGetMultiClassificationKlassDetailsModel.CAN_CREATE_NATURE,
          klassAndTaxonomyPermission.get(IGlobalPermission.CAN_CREATE));
    }
    if (isEditable) {
      ediatbleKlassIds.add(klassId);
      return;
    }
  }
  
  private static void fillPropertyCollectionPermissionDetails(String roleId,
      String propertyCollectionId, Set<String> visiblePropertyCollectionIds,
      Set<String> openedPropertyCollectionIds, Set<String> editablePropertyCollectionIds)
      throws Exception
  {
    Map<String, Object> propertyCollectionPermission = GlobalPermissionUtils
        .getPropertyCollectionPermission(propertyCollectionId, roleId);
    Boolean isHidden = (Boolean) propertyCollectionPermission
        .get(IPropertyCollectionPermissions.IS_HIDDEN);
    Boolean isCollapsed = (Boolean) propertyCollectionPermission
        .get(IPropertyCollectionPermissions.IS_COLLAPSED);
    Boolean canEdit = (Boolean) propertyCollectionPermission
        .get(IPropertyCollectionPermissions.CAN_EDIT);
    if (!isHidden) {
      visiblePropertyCollectionIds.add(propertyCollectionId);
    }
    
    if (!isCollapsed) {
      openedPropertyCollectionIds.add(propertyCollectionId);
    }
    
    if (canEdit) {
      editablePropertyCollectionIds.add(propertyCollectionId);
    }
  }
  
  private static void fillPropertyPermissionDetails(String roleId,
      Set<String> enabledSectionElementIds, String elementId)
      throws NotFoundException, MultipleVertexFoundException
  {
    Map<String, Object> propertyPermission = GlobalPermissionUtils
        .getPropertyPermissionDeprecated(elementId, roleId);
    Boolean isDisabled = (Boolean) propertyPermission.get(IPropertyPermissions.IS_DISABLED);
    if (!isDisabled) {
      enabledSectionElementIds.add(elementId);
      return;
    }
  }
  
  /*  private static void fillPropertyCollectionPermission(Set<String> roleIds, List<String> entityIds,
      String type, Map<String, Object> rolePermissionMap, Map<String, Object> globalPermission) throws Exception
  {
    Set<String> visiblePropertyCollectionIds = new HashSet<>();
    Set<String> openedPropertyCollectionIds = new HashSet<>();
    Set<String> editablePropertyCollectionIds = new HashSet<>();
    Set<String> enabledSectionElementIds = new HashSet<>();
  
    for (String roleId : roleIds) {
      Map<String, Object> propertyCollectionPermissionsMap = GlobalPermissionUtils
          .getPropertyCollectionsPermissions(roleId, entityIds, type);
      Map<String, Object> propertyCollectionPermissionsForRoleMap = (Map<String, Object>) propertyCollectionPermissionsMap
          .get(IGlobalPermissionsForRole.PROPERTY_COLLECTION_PERMISSIONS);
      for (String propertyCollectionId : propertyCollectionPermissionsForRoleMap.keySet()) {
        Map<String, Object> propertyCollectionPermissionMap = (Map<String, Object>) propertyCollectionPermissionsForRoleMap
            .get(propertyCollectionId);
        if (!(Boolean) propertyCollectionPermissionMap
            .get(IPropertyCollectionPermissions.IS_COLLAPSED)) {
          openedPropertyCollectionIds.add(propertyCollectionId);
        }
        if ((Boolean) propertyCollectionPermissionMap.get(IPropertyCollectionPermissions.CAN_EDIT)
            && (Boolean) globalPermission.get(IKlassTaxonomyPermissions.CAN_EDIT)) {
          editablePropertyCollectionIds.add(propertyCollectionId);
        }
        if (!(Boolean) propertyCollectionPermissionMap
            .get(IPropertyCollectionPermissions.IS_HIDDEN)) {
          visiblePropertyCollectionIds.add(propertyCollectionId);
        }
        if ((Boolean) propertyCollectionPermissionMap.get(IPropertyCollectionPermissions.CAN_EDIT)
            && (Boolean) globalPermission.get(IKlassTaxonomyPermissions.CAN_EDIT)) {
          Map<String, Object> propertiesPermissionsMap = GlobalPermissionUtils
              .getPropertiesPermissions(roleId, propertyCollectionId);
          Map<String, Object> propertiesPermissionsForRoleMap = (Map<String, Object>) propertiesPermissionsMap
              .get(IGlobalPermissionsForRole.PROPERTY_PERMISSIONS);
          for (String propertyId : propertiesPermissionsForRoleMap.keySet()) {
            Map<String, Object> propertyPermissionMap = (Map<String, Object>) propertiesPermissionsForRoleMap
                .get(propertyId);
            if (!(Boolean) propertyPermissionMap.get(IPropertyPermissions.IS_DISABLED)) {
              enabledSectionElementIds.add(propertyId);
            }
          }
        }
      }
    }
    ((Set<String>) rolePermissionMap.get(IReferencedRolePermissionModel.ENABLED_SECTION_ELEMENT_IDS)).addAll(enabledSectionElementIds);
    ((Set<String>) rolePermissionMap.get(IReferencedRolePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS)).addAll(editablePropertyCollectionIds);
    ((Set<String>) rolePermissionMap.get(IReferencedRolePermissionModel.OPENED_PROPERTY_COLLECTION_IDS)).addAll(openedPropertyCollectionIds);
    ((Set<String>) rolePermissionMap.get(IReferencedRolePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS)).addAll(visiblePropertyCollectionIds);
  }*/
  
  /*private static void fillReferencedElementPermission(Map<String, Object> rolePermissionMap,
      String klassId, String entityId, Vertex klassPropertyNode, Set<String> roleIds) throws Exception
  {
    //TODO: use query to fetch all permission Edges where utilising class id's contains klassId
    Iterator<Edge> permissionForIterator = klassPropertyNode.getEdges(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_PERMISSION_FOR).iterator();
    Set<String> rolesForDefaultPermission = new HashSet<>(roleIds);
    while (permissionForIterator.hasNext()) {
      Edge permissionFor = permissionForIterator.next();
      List<String> utilizingKlassIds = permissionFor.getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if (utilizingKlassIds.contains(klassId)) {
        String roleId = permissionFor.getProperty(CommonConstants.ROLE_ID_PROPERY);
        rolesForDefaultPermission.remove(roleId);
        Map<String, Object> rolePermission = (Map<String, Object>) rolePermissionMap.get(roleId);
  
        if (rolePermission == null) {
          rolePermission = new HashMap<String, Object>();
          rolePermission.put(IReferencedRolePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS, new HashSet<>());
          rolePermission.put(IReferencedRolePermissionModel.OPENED_PROPERTY_COLLECTION_IDS, new HashSet<>());
          rolePermission.put(IReferencedRolePermissionModel.ENABLED_SECTION_ELEMENT_IDS, new HashSet<>());
          rolePermissionMap.put(roleId, rolePermission);
        }
  
        Vertex sectionElementPermissionNode = permissionFor.getVertex(Direction.OUT);
        Boolean isDisabled = sectionElementPermissionNode.getProperty(CommonConstants.IS_DISABLED_PROPERTY);
        if (!isDisabled) {
          Set<String> enabledSectionElementIds = (Set<String>)rolePermission.get(IReferencedRolePermissionModel.ENABLED_SECTION_ELEMENT_IDS);
          enabledSectionElementIds.add(entityId);
        }
      }
    }
  
    for (String roleForDefaultPermission : rolesForDefaultPermission) {
      Map<String,Object> rolePermission = (Map<String, Object>) rolePermissionMap.get(roleForDefaultPermission);
      if(rolePermission == null) {
        rolePermission = new HashMap<String, Object>();
        rolePermission.put(IReferencedRolePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS, new HashSet<>());
        rolePermission.put(IReferencedRolePermissionModel.OPENED_PROPERTY_COLLECTION_IDS, new HashSet<>());
        rolePermission.put(IReferencedRolePermissionModel.ENABLED_SECTION_ELEMENT_IDS, new HashSet<>());
        rolePermissionMap.put(roleForDefaultPermission, rolePermission);
      }
    }
  
  }*/
  
  public static void fillReferencedElements(Map<String, Object> multiClassificationDetails,
      Vertex klassNode, Vertex propertyCollectionNode, String roleId, Set<String> allowedEntities)
      throws Exception
  {
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_PROPERTY_COLLECTIONS);
    Map<String, Object> referencedPropertyCollectionMap = (Map<String, Object>) referencedPropertyCollections
        .get(UtilClass.getCodeNew(propertyCollectionNode));
    Map<String, Object> referencedElementsMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedAttributesMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS);
    Map<String, Object> referencedRolesMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ROLES);
    Map<String, Object> referencedRelationshipsMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS);
    Map<String, Set<String>> referencedNotificationSettings = (Map<String, Set<String>>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.NOTIFICATION_SETTINGS);
    Map<String, Object> referencedPermissions = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_PERMISSION);
    referencedPermissions.get(IReferencedPermissionModel.ROLE_PERMISSION);
    Map<String, Object> referencedRelationshipsMappingMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS_MAPPING);
    
    List<Map<String, Object>> elementsList = (List<Map<String, Object>>) referencedPropertyCollectionMap
        .get(IReferencedPropertyCollectionModel.ELEMENTS);
    if (elementsList == null) {
      elementsList = new ArrayList<>();
    }
    referencedPropertyCollectionMap.put(IReferencedPropertyCollectionModel.ELEMENTS, elementsList);
    
    Boolean isRelationshipSectionSkipped = false;
    
    Iterable<Edge> entityToRelationships = propertyCollectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
    for (Edge entityTo : entityToRelationships) {
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      String entityId = UtilClass.getCodeNew(entityNode);
      String entityType = entityTo.getProperty(IPropertyCollectionElement.TYPE);
      
      Vertex klassPropertyNode = KlassUtils.getRespectiveKlassPropertyNode(klassNode, entityNode);
      if (klassPropertyNode == null) {
        continue;
      }
      
      Map<String, Object> propertyCollectionElement = isPropertyCollectionElementExist(elementsList,
          entityId);
      if (propertyCollectionElement == null) {
        propertyCollectionElement = new HashMap<>();
        propertyCollectionElement.put(IReferencedPropertyCollectionElementModel.ID, entityId);
        elementsList.add(propertyCollectionElement);
      }
      String klassId = UtilClass.getCodeNew(klassNode);
      
      Map<String, Object> referencedElement = UtilClass.getMapFromVertex(new ArrayList<>(),
          klassPropertyNode);
      referencedElement.remove("tagGroups"); // remove tag groups from klass
                                             // property node
      referencedElement.put(IReferencedSectionElementModel.ID, entityId);
      Map<String, Object> entity;
      switch (entityType) {
        case SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE:
          entity = (Map<String, Object>) referencedAttributesMap.get(entityId);
          if (entity == null) {
            entity = AttributeUtils.getAttributeMap(entityNode);
            referencedAttributesMap.put(entityId, entity);
          }
          
          String defaultValue = (String) referencedElement.get(ISectionAttribute.DEFAULT_VALUE);
          if (defaultValue == null || defaultValue.equals("")) {
            referencedElement.put(ISectionAttribute.DEFAULT_VALUE,
                entity.get(IAttribute.DEFAULT_VALUE));
          }
          
          break;
        case SystemLevelIds.PROPERTY_TYPE_TAG:
          List<Map<String, Object>> defaultTagValues = KlassUtils
              .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
          referencedElement.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
          
          entity = (Map<String, Object>) referencedTagsMap.get(entityId);
          if (entity == null) {
            entity = TagUtils.getTagMap(entityNode, false);
            referencedTagsMap.put(entityId, entity);
          }
          
          String tagType = (String) referencedElement.get(CommonConstants.TAG_TYPE_PROPERTY);
          if (tagType != null && !tagType.equals("")) {
            entity.put(ITag.TAG_TYPE, tagType);
          }
          else {
            referencedElement.put(ISectionTag.TAG_TYPE, entity.get(ITag.TAG_TYPE));
          }
          
          Boolean isMultiselect = (Boolean) referencedElement.get(ISectionTag.IS_MULTI_SELECT);
          if (isMultiselect != null) {
            entity.put(ITag.IS_MULTI_SELECT, isMultiselect);
          }
          else {
            referencedElement.put(ISectionTag.IS_MULTI_SELECT, entity.get(ITag.IS_MULTI_SELECT));
          }
          break;
        
        case SystemLevelIds.PROPERTY_TYPE_ROLE:
          entity = (Map<String, Object>) referencedRolesMap.get(entityId);
          if (entity == null) {
            entity = RoleUtils.getRoleEntityMap(entityNode);
            referencedRolesMap.put(entityId, entity);
          }
          
          break;
        
        case SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP:
          entity = (Map<String, Object>) referencedRelationshipsMap.get(entityId);
          if (entity == null) {
            entity = RelationshipUtils.getRelationshipMapWithContext(entityNode);
            Map<String, Object> side = (Map<String, Object>) referencedElement
                .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
            if (side == null) {
              continue;
            }
            String targetKlassType = (String) side.get(IKlassRelationshipSide.TARGET_TYPE);
            String moduleEntity = EntityUtil.getModuleEntityFromKlassType(targetKlassType);
            if (!allowedEntities.contains(moduleEntity)) {
              isRelationshipSectionSkipped = true;
              elementsList.remove(propertyCollectionElement);
              continue;
            }
            side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY,
                UtilClass.getCodeNew(klassPropertyNode));
            referencedRelationshipsMap.put(entityId, entity);
            referencedRelationshipsMappingMap.put(entityId, klassId);
          }
          // This Relationship is linked with klass
          referencedElement.put(IReferencedSectionRelationshipModel.IS_LINKED, true);
          break;
        
        default:
          break;
      }
      
      referencedElement.put("type", entityType);
      
      // TODO: merge referencedElement
      Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElementsMap
          .get(entityId);
      if (existingReferencedElement != null) {
        mergeReferencedElement(referencedElement, existingReferencedElement, klassId, entityType);
      }
      else {
        referencedElementsMap.put(entityId, referencedElement);
      }
      
      // TODO: fillReferencedElementPermission(rolePermissionMap, klassId,
      // entityId,
      // klassPropertyNode, roleIds);
      fillNotificationSettings(referencedNotificationSettings, klassId, entityId, klassPropertyNode,
          roleId);
    }
    
    Map<String, Object> propertyCollection = (Map<String, Object>) referencedPropertyCollections
        .get(UtilClass.getCodeNew(propertyCollectionNode));
    List<Object> elementList = (List<Object>) propertyCollection
        .get(IReferencedPropertyCollectionModel.ELEMENTS);
    if (isRelationshipSectionSkipped && elementList.size() == 0) {
      referencedPropertyCollections.remove(UtilClass.getCodeNew(propertyCollectionNode));
    }
  }
  
  private static Map<String, Object> isPropertyCollectionElementExist(
      List<Map<String, Object>> elementsList, String entityId)
  {
    for (Map<String, Object> element : elementsList) {
      if (entityId.equals(element.get(IReferencedPropertyCollectionElementModel.ID))) {
        return element;
      }
    }
    
    return null;
  }
  
  public static void putInitialConflictingEntry(String klassId, String entityId,
      Map<String, Object> referencedElement, Map<String, Object> conflictingValues)
  {
    String type = (String) referencedElement.get(IReferencedSectionElementModel.TYPE);
    String couplingType = (String) referencedElement
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    Map<String, Object> attributeMap = new HashMap<>();
    List<Map<String, Object>> valueList = new ArrayList<>();
    Map<String, Object> valueMap = new HashMap<>();
    valueMap.put(IValueIdModel.ID, klassId);
    valueMap.put(IValueIdModel.TYPE, type);
    valueMap.put(IValueIdModel.VALUE,
        referencedElement.get(IReferencedSectionAttributeModel.DEFAULT_VALUE));
    
    valueList.add(valueMap);
    attributeMap.put(IConflictingValueModel.COUPLING_TYPE, couplingType);
    // attributeMap.put(IConflictingValueModel.TYPE, type);
    attributeMap.put(IConflictingValueModel.VALUES, valueList);
    attributeMap.put(CommonConstants.COUNT, 1);
    conflictingValues.put(entityId, attributeMap);
  }
  
  private static void mergeReferencedElement(Map<String, Object> referencedElement,
      Map<String, Object> existingReferencedElement, String klassId, String entityType)
  {
    Boolean isVariantAllowed = (Boolean) referencedElement
        .get(IReferencedSectionElementModel.IS_VARIANT_ALLOWED);
    if (isVariantAllowed != null && isVariantAllowed) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_VARIANT_ALLOWED,
          isVariantAllowed);
    }
    
    Integer existingNumberOfVersionsAllowed = (Integer) existingReferencedElement
        .get(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED);
    Integer numberOfVersionsAllowed = (Integer) referencedElement
        .get(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED);
    if (numberOfVersionsAllowed != null && existingNumberOfVersionsAllowed != null
        && numberOfVersionsAllowed > existingNumberOfVersionsAllowed) {
      existingReferencedElement.put(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED,
          numberOfVersionsAllowed);
    }
    
    mergeCouplingTypesAndManageConflicts(existingReferencedElement, referencedElement, klassId);
    
    Boolean existingIsSkipped = (Boolean) existingReferencedElement
        .get(IReferencedSectionElementModel.IS_SKIPPED);
    Boolean newIsSkipped = (Boolean) referencedElement
        .get(IReferencedSectionElementModel.IS_SKIPPED);
    if (newIsSkipped != null && existingIsSkipped != null
        && (!existingIsSkipped || !newIsSkipped)) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SKIPPED, false);
    }
    else {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SKIPPED, true);
    }
    
    if (entityType.equals(CommonConstants.TAG_PROPERTY)) {
      Boolean existingIsMultiselect = (Boolean) existingReferencedElement
          .get(IReferencedSectionTagModel.IS_MULTI_SELECT);
      Boolean newIsMultiselect = (Boolean) referencedElement
          .get(IReferencedSectionTagModel.IS_MULTI_SELECT);
      if (existingIsMultiselect || newIsMultiselect) {
        existingReferencedElement.put(IReferencedSectionTagModel.IS_MULTI_SELECT, true);
      }
      else {
        existingReferencedElement.put(IReferencedSectionTagModel.IS_MULTI_SELECT, false);
      }
    }
    
    Integer existingNumberOfItemsAllowed = (Integer) existingReferencedElement
        .get(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED);
    Integer numberOfItemsAllowed = (Integer) referencedElement
        .get(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED);
    if (numberOfItemsAllowed != null && existingNumberOfItemsAllowed != null
        && numberOfItemsAllowed > existingNumberOfItemsAllowed) {
      existingReferencedElement.put(IReferencedSectionAttributeModel.NUMBER_OF_ITEMS_ALLOWED,
          numberOfItemsAllowed);
    }
  }
  
  public static void mergeCouplingTypesAndManageConflicts(
      Map<String, Object> existingReferencedElement, Map<String, Object> referencedElement,
      String klassId)
  {
    String existingCouplingType = (String) existingReferencedElement
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    String newCouplingType = (String) referencedElement
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    
    Boolean existingIsMandatory = (Boolean) existingReferencedElement
        .get(IReferencedSectionElementModel.IS_MANDATORY);
    Boolean newIsMmandatory = (Boolean) referencedElement
        .get(IReferencedSectionElementModel.IS_MANDATORY);
    if (newIsMmandatory != null && existingIsMandatory != null
        && (existingIsMandatory || newIsMmandatory)) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_MANDATORY, true);
    }
    else {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_MANDATORY, false);
    }
    
    Boolean existingIsShould = (Boolean) existingReferencedElement
        .get(IReferencedSectionElementModel.IS_SHOULD);
    Boolean newIsShould = (Boolean) referencedElement.get(IReferencedSectionElementModel.IS_SHOULD);
    if (newIsShould != null && existingIsShould != null && (existingIsShould || newIsShould)) {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SHOULD, true);
    }
    else {
      existingReferencedElement.put(IReferencedSectionElementModel.IS_SHOULD, false);
    }
    
    if (existingCouplingType != null && newCouplingType != null) {
      if (existingCouplingType.equals(newCouplingType)) {
        // do nothing
      }
      else if ((newCouplingType.equals(CommonConstants.DYNAMIC_COUPLED))
          || (newCouplingType.equals(CommonConstants.TIGHTLY_COUPLED)
              && !existingCouplingType.equals(CommonConstants.DYNAMIC_COUPLED))) {
        
        existingReferencedElement.put(IReferencedSectionElementModel.COUPLING_TYPE,
            newCouplingType);
        existingReferencedElement.put(IReferencedSectionAttributeModel.DEFAULT_VALUE,
            referencedElement.get(IReferencedSectionAttributeModel.DEFAULT_VALUE));
      }
    }
  }
  
  public static void fillDataRulesOfKlass(Vertex klassNode,
      Map<String, Object> referencedDataRuleMap) throws Exception
  {
    Iterable<Edge> dataRuleEdges = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.DATA_RULES);
    for (Edge dataRuleEdge : dataRuleEdges) {
      Vertex dataRuleNode = dataRuleEdge.getVertex(Direction.IN);
      String dataRuleId = UtilClass.getCodeNew(dataRuleNode);
      if (referencedDataRuleMap.get(dataRuleId) != null) {
        continue;
      }
      
      Map<String, Object> dataRuleMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleNode, true);
      referencedDataRuleMap.put(dataRuleId, dataRuleMap);
    }
  }
  
  private static void fillNotificationSettings(Map<String, Set<String>> notificationSettings,
      String klassId, String entityId, Vertex klassPropertyNode,
      String rolesIdForDefaultNotificationSettings) throws Exception
  {
    // TODO: use query to fetch all permission Edges where utilising class id's
    // contains klassId
    Iterator<Edge> notificationSettingsForIterator = klassPropertyNode
        .getEdges(Direction.IN,
            RelationshipLabelConstants.RELATIONSHIPLABEL_NOTIFICATION_SETTING_FOR)
        .iterator();
    Set<String> rolesForDefaultNotificationSettings = new HashSet<>(
        Arrays.asList(rolesIdForDefaultNotificationSettings));
    while (notificationSettingsForIterator.hasNext()) {
      Edge notificationSettingsFor = notificationSettingsForIterator.next();
      List<String> utilizingKlassIds = notificationSettingsFor
          .getProperty(CommonConstants.UTILIZING_KLASS_IDS_PROPERTY);
      if (utilizingKlassIds.contains(klassId)) {
        String roleId = notificationSettingsFor.getProperty(CommonConstants.ROLE_ID_PROPERY);
        rolesForDefaultNotificationSettings.remove(roleId);
        Set<String> entitiesToNotify = notificationSettings.get(roleId);
        
        if (entitiesToNotify == null) {
          entitiesToNotify = new HashSet<>();
          notificationSettings.put(roleId, entitiesToNotify);
        }
        
        Vertex notificationSettingNode = notificationSettingsFor.getVertex(Direction.OUT);
        Boolean isnotificationEnabled = notificationSettingNode
            .getProperty(CommonConstants.IS_NOTIFICATION_ENABLED);
        if (isnotificationEnabled) {
          entitiesToNotify.add(entityId);
        }
      }
    }
    
    for (String roleForDefaultPermission : rolesForDefaultNotificationSettings) {
      Set<String> entitiesToNotify = notificationSettings.get(roleForDefaultPermission);
      if (entitiesToNotify == null) {
        entitiesToNotify = new HashSet<>();
        notificationSettings.put(roleForDefaultPermission, entitiesToNotify);
      }
    }
  }
  
  public static void fillVariantContextsOfKlass(Vertex klassNode,
      Map<String, Object> referencedVariantContextsMap, Map<String, Object> mapToReturn,
      String klassId, Set<String> allowedEntities, String roleId,
      Map<String, Object> rolePermissionMap) throws Exception
  {
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContextsMap
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> languageVariantContexts = (Map<String, Object>) referencedVariantContextsMap
        .get(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS);
    Map<String, Object> productVariantContexts = (Map<String, Object>) referencedVariantContextsMap
        .get(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS);
    Map<String, Object> promotionalVariantContexts = (Map<String, Object>) referencedVariantContextsMap
        .get(IReferencedContextModel.PROMOTIONAL_VERSION_CONTEXTS);
    
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS);
    Map<String, Object> referencedPropertyCollectionMap = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_PROPERTY_COLLECTIONS);
    
    Iterable<Edge> variantContextEdges = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.VARIANT_CONTEXT_OF);
    for (Edge variantContextEdge : variantContextEdges) {
      Vertex variantContextNode = variantContextEdge.getVertex(Direction.IN);
      
      Map<String, Object> variantContextMap = VariantContextUtils
          .getReferencedContexts(variantContextNode);
      
      fillReferencedTagsAndStatusTags(variantContextNode, (Map<String, Object>) mapToReturn
          .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS), variantContextMap);
      
      String variantContextId = (String) variantContextMap.get(IReferencedVariantContextModel.ID);
      for (Map<String, Object> variantContext : (List<Map<String, Object>>) variantContextMap
          .get(IReferencedVariantContextModel.TAGS)) {
        String entityId = (String) variantContext.get(IReferencedVariantContextTagsModel.TAG_ID);
        Map<String, Object> entity = (Map<String, Object>) referencedTagsMap.get(entityId);
        if (entity == null) {
          try {
            Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
            entity = TagUtils.getTagMap(entityNode, false);
            referencedTagsMap.put(entityId, entity);
          }
          catch (NotFoundException e) {
            // Do nothing;
          }
        }
      }
      
      fillPropertyCollectionsData(variantContextNode, variantContextId, variantContextMap,
          referencedPropertyCollectionMap, mapToReturn, allowedEntities, roleId, rolePermissionMap);
      
      // VariantContextUtils.addEditablePropertiesToContextMap(variantContextMap,
      // variantContextNode);
      switch ((String) variantContextMap.get(IVariantContext.TYPE)) {
        case CommonConstants.CONTEXTUAL_VARIANT:
          if (!embeddedVariantContexts.containsKey(variantContextId)) {
            embeddedVariantContexts.put(variantContextId, variantContextMap);
          }
          break;
        case CommonConstants.PRODUCT_VARIANT:
          if (!productVariantContexts.containsKey(variantContextId)) {
            productVariantContexts.put(variantContextId, variantContextMap);
          }
          break;
        case CommonConstants.LANGUAGE_VARIANT:
          if (!languageVariantContexts.containsKey(variantContextId)) {
            languageVariantContexts.put(variantContextId, variantContextMap);
          }
          break;
        case CommonConstants.PROMOTIONAL_VERSION:
          if (!promotionalVariantContexts.containsKey(variantContextId)) {
            promotionalVariantContexts.put(variantContextId, variantContextMap);
          }
          break;
      }
    }
  }
  
  public static void fillNatureReferencedElements(Map<String, Object> multiClassificationDetails,
      Vertex klassNode, Vertex propertyCollectionNode, String roleId, Set<String> allowedEntities)
      throws Exception
  {
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_PROPERTY_COLLECTIONS);
    Map<String, Object> referencedPropertyCollectionMap = (Map<String, Object>) referencedPropertyCollections
        .get(UtilClass.getCodeNew(propertyCollectionNode));
    Map<String, Object> referencedElementsMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedAttributesMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) multiClassificationDetails
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS);
    
    List<Map<String, Object>> elementsList = new ArrayList<>();
    referencedPropertyCollectionMap.put(IReferencedPropertyCollectionModel.ELEMENTS, elementsList);
    
    Iterable<Edge> entityToRelationships = propertyCollectionNode.getEdges(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
    for (Edge entityTo : entityToRelationships) {
      Vertex entityNode = entityTo.getVertex(Direction.OUT);
      String entityId = UtilClass.getCodeNew(entityNode);
      String entityType = entityTo.getProperty(IPropertyCollectionElement.TYPE);
      
      Map<String, Object> propertyCollectionElement = new HashMap<>();
      propertyCollectionElement.put(IReferencedPropertyCollectionElementModel.ID, entityId);
      elementsList.add(propertyCollectionElement);
      
      Map<String, Object> referencedElement = new HashMap<>();
      referencedElement.put(IReferencedSectionElementModel.ID, entityId);
      referencedElement.put(IReferencedSectionElementModel.TYPE, entityType);
      referencedElement.put(IReferencedSectionElementModel.IS_SKIPPED, false);
      referencedElement.put(IReferencedSectionElementModel.IS_DISABLED, false);
      referencedElement.put(IReferencedSectionElementModel.TOOLTIP, "");
      referencedElement.put(IReferencedSectionElementModel.COUPLING_TYPE,
          CommonConstants.LOOSELY_COUPLED);
      referencedElement.put(IReferencedSectionElementModel.LABEL, "");
      referencedElement.put(IReferencedSectionElementModel.IS_VARIANT_ALLOWED, false);
      referencedElement.put(IReferencedSectionElementModel.IS_CUT_OFF, false);
      referencedElement.put(IReferencedSectionElementModel.IS_MANDATORY, false);
      referencedElement.put(IReferencedSectionElementModel.IS_SHOULD, false);
      referencedElement.put(IReferencedSectionElementModel.NUMBER_OF_VERSIONS_ALLOWED, 0);
      
      Map<String, Object> entity;
      switch (entityType) {
        case SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE:
          entity = (Map<String, Object>) referencedAttributesMap.get(entityId);
          if (entity == null) {
            entity = AttributeUtils.getAttributeMap(entityNode);
            referencedAttributesMap.put(entityId, entity);
          }
          
          String defaultValue = (String) referencedElement.get(ISectionAttribute.DEFAULT_VALUE);
          if (defaultValue == null || defaultValue.equals("")) {
            referencedElement.put(ISectionAttribute.DEFAULT_VALUE,
                entity.get(IAttribute.DEFAULT_VALUE));
          }
          
          break;
        case SystemLevelIds.PROPERTY_TYPE_TAG:
          entity = (Map<String, Object>) referencedTagsMap.get(entityId);
          if (entity == null) {
            entity = TagUtils.getTagMap(entityNode, false);
            referencedTagsMap.put(entityId, entity);
          }
          
          String tagType = (String) referencedElement.get(CommonConstants.TAG_TYPE_PROPERTY);
          if (tagType != null && !tagType.equals("")) {
            entity.put(ITag.TAG_TYPE, tagType);
          }
          else {
            referencedElement.put(ISectionTag.TAG_TYPE, entity.get(ITag.TAG_TYPE));
          }
          
          Boolean isMultiselect = (Boolean) referencedElement.get(ISectionTag.IS_MULTI_SELECT);
          if (isMultiselect != null) {
            entity.put(ITag.IS_MULTI_SELECT, isMultiselect);
          }
          else {
            referencedElement.put(ISectionTag.IS_MULTI_SELECT, entity.get(ITag.IS_MULTI_SELECT));
          }
          break;
        
        default:
          break;
      }
      
      // TODO: merge referencedElement
      Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElementsMap
          .get(entityId);
      if (existingReferencedElement != null) {
        /*mergeReferencedElement(referencedElement, existingReferencedElement, conflictingValues,
        klassId, entityType);*/
      }
      else {
        referencedElementsMap.put(entityId, referencedElement);
      }
    }
  }
  
  public static Map<String, Object> getXRayConfigDetails(List<String> xRayAttributes,
      List<String> xRayTags) throws Exception, AttributeNotFoundException, TagNotFoundException
  {
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedAttributes = new HashMap<>();
    for (String attributeId : xRayAttributes) {
      Vertex attributeNode = null;
      try {
        attributeNode = UtilClass.getVertexById(attributeId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
      }
      catch (NotFoundException e) {
        throw new AttributeNotFoundException();
      }
      Map<String, Object> referencedAttribute = AttributeUtils.getAttributeMap(attributeNode);
      if (referencedAttribute.get(IAttribute.TYPE).equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
        AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(referencedAttributes, referencedTags,
            referencedAttribute);
      }
      referencedAttributes.put(attributeId, referencedAttribute);
    }
    
  
    for (String tagId : xRayTags) {
      Vertex tagNode;
      try {
        tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
      Map<String, Object> referencedTag = TagUtils.getTagMap(tagNode, true);
      referencedTags.put(tagId, referencedTag);
    }
    
    Map<String, Object> xRayConfigDetails = new HashMap<>();
    xRayConfigDetails.put(IXRayConfigDetailsModel.REFERENCED_ATTRIBUTES, referencedAttributes);
    xRayConfigDetails.put(IXRayConfigDetailsModel.REFERENCED_TAGS, referencedTags);
    return xRayConfigDetails;
  }
  
}