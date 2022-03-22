package com.cs.runtime.strategy.plugin.usecase.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.task.util.TasksUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.datarule.IAttributeConflictingValuesModel;
import com.cs.core.config.interactor.entity.datarule.IElementConflictingValuesModel;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.propertycollection.IPropertyCollection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.relationship.IReferencedRelationshipProperty;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.entity.template.IHeaderPermission;
import com.cs.core.config.interactor.entity.template.IPropertyCollectionPermission;
import com.cs.core.config.interactor.entity.template.IPropertyPermission;
import com.cs.core.config.interactor.entity.template.IRelationshipPermission;
import com.cs.core.config.interactor.entity.template.ITabPermission;
import com.cs.core.config.interactor.entity.template.ITemplate;
import com.cs.core.config.interactor.entity.template.ITemplateHeaderVisibility;
import com.cs.core.config.interactor.entity.template.ITemplateTab;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.config.interactor.model.klass.IReferencedNatureRelationshipModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionRelationshipModel;
import com.cs.core.config.interactor.model.permission.IFunctionPermissionModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.MultipleVertexFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetails extends AbstractConfigDetails {
  
  public static final List<String> PROPERTY_CONTEXT_FIELDS_TO_FETCH = Arrays.asList(
      CommonConstants.CODE_PROPERTY, IVariantContext.CODE, IVariantContext.LABEL,
      IVariantContext.TYPE, IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED,
      IVariantContext.IS_TIME_ENABLED);
  
  public AbstractGetConfigDetails(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected void fillTaxonomyDetails(Map<String, Object> mapToReturn, List<String> taxonomyIds,
      Map<String, Object> referencedDataRuleMap,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    Set<Vertex> taxonomyVertices = new HashSet<>();
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyVertex = null;
      try {
        taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
            VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        taxonomyVertices.add(taxonomyVertex);
      }
      catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE),
          taxonomyVertex);
      
      // Fetching property collections link to klass taxonomy
      Map<String, Object> klassTaxonomyMap = TaxonomyUtil.getKlassTaxonomy(taxonomyVertex);
      if (!taxonomyId.equals("-1")) {
        KlassUtils.addSectionsToKlassEntityMap(taxonomyVertex, klassTaxonomyMap);
        klassTaxonomyMap.remove(IKlass.PERMISSIONS);
      }
      
      List<Map<String, Object>> sections = (List<Map<String, Object>>) klassTaxonomyMap
          .get(IKlass.SECTIONS);
      
      List<Map<String, Object>> propertyCollection = new ArrayList<>();
      for (Map<String, Object> section : sections) {
        Map<String, Object> temporaryMap = new HashMap<>();
        temporaryMap.put(IIdParameterModel.ID, section.get(ISection.PROPERTY_COLLECTION_ID));
        propertyCollection.add(temporaryMap);
      }
      taxonomyMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS, propertyCollection);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
      
      fillReferencedTasks(taxonomyVertex, mapToReturn);
      fillDataRulesOfKlass(taxonomyVertex, referencedDataRuleMap, helperModel,
          RelationshipLabelConstants.HAS_TAXONOMY_RULE_LINK);
    }
    helperModel.setTaxonomyVertices(taxonomyVertices);
  }
  
  protected void consolidateTaxonomyHierarchyIds(List<String> taxonomyIdsToGetDetailsFor,
      Map<String, Object> mapToReturn) throws Exception, KlassTaxonomyNotFoundException
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Collection<String>> taxonomyHierarchies = (Map<String, Collection<String>>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.TAXONOMY_HIERARCHIES);
    if (taxonomyIdsToGetDetailsFor != null) {
      for (String taxonomyId : taxonomyIdsToGetDetailsFor) {
        Vertex taxonomyVertex = null;
        try {
          taxonomyVertex = UtilClass.getVertexByIndexedId(taxonomyId,
              VertexLabelConstants.ROOT_KLASS_TAXONOMY);
        }
        catch (NotFoundException e) {
          throw new KlassTaxonomyNotFoundException();
        }
        String query = "select from(traverse out('"
            + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from "
            + taxonomyVertex.getId() + " strategy BREADTH_FIRST)";
        Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
            .execute();
        List<String> parentTaxonomyIds = new ArrayList<>();
        taxonomyHierarchies.put(taxonomyId, parentTaxonomyIds);
        for (Vertex taxonomyNode : resultIterable) {
          if (TagUtils.isTagNode(taxonomyNode)) {
            continue;
          }
          String taxnomyId = taxonomyNode.getProperty(CommonConstants.CODE_PROPERTY);
          parentTaxonomyIds.add(taxnomyId);
        }
      }
    }
  }
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap)
  {
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedKlassMap = new HashMap<>();
    Map<String, Object> referencedSectionElementMap = new HashMap<>();
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Map<String, Collection<String>> taxonomyHierarchies = new HashMap<String, Collection<String>>();
    List<String> referencedLifeCycleStatusTags = new ArrayList<>();
    List<Map<String, Object>> referencedTemplates = new ArrayList<Map<String, Object>>();
    Map<String, Object> referencedTasks = new HashMap<>();
    Map<String, Object> referencedPermissions = new HashMap<String, Object>();
    Set<String> taskIdsHavingReadPermissions = new HashSet<String>();
    Map<String, Object> taskIdsForRolesHavingReadPermission = new HashMap<>();
    referencedPermissions.put(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.EXPANDABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.CAN_ADD_RELATIONSHIP_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.CAN_DELETE_RELATIONSHIP_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.CAN_EDIT_CONTEXT_RELATIONSHIP_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_RELATIONSHIP_IDS,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_TAB_TYPES,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.TASK_IDS_HAVING_READ_PERMISSION,
        taskIdsHavingReadPermissions);
    referencedPermissions.put(
        IReferencedTemplatePermissionModel.TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION,
        taskIdsForRolesHavingReadPermission);
    referencedPermissions.put(IReferencedTemplatePermissionModel.ENTITIES_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.KLASS_IDS_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.ALL_TAXONOMY_IDS_HAVING_RP,
        new HashSet<String>());
    
    referencedPermissions.put(IReferencedTemplatePermissionModel.TAXONOMY_IDS_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.EXPANDABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS,
        new HashSet<>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.CAN_ADD_RELATIONSHIP_IDS,
        new HashSet<>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.CAN_DELETE_RELATIONSHIP_IDS,
        new HashSet<>());
    referencedPermissions.put(IReferencedTemplatePermissionModel.VISIBLE_RELATIONSHIP_IDS,
        new HashSet<>());
    
    Map<String, Object> referencedVariantContextsMap = new HashMap<String, Object>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        embeddedVariantContexts);
    
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_KLASSES, referencedKlassMap);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_ELEMENTS, referencedSectionElementMap);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_ATTRIBUTES, referencedAttributeMap);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TAGS, referencedTagMap);
    mapToReturn.put(IGetConfigDetailsModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, 0);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    mapToReturn.put(IGetConfigDetailsModel.TAXONOMY_HIERARCHIES, taxonomyHierarchies);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_LIFECYCLE_STATUS_TAGS,
        referencedLifeCycleStatusTags);
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_TEMPLATES, referencedTemplates);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_TASKS, referencedTasks);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_DATA_RULES,
        referencedDataRuleMap.values());
    
    mapToReturn.put(IGetConfigDetailsModel.LANGAUGE_CONTEXT_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsModel.VARIANT_CONTEXT_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsModel.PROMOTION_VERSION_CONTEXT_IDS, new ArrayList<>());
    
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_PERMISSIONS, referencedPermissions);
    mapToReturn.put(IGetConfigDetailsModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE,
        new ArrayList<>());
    
    mapToReturn.put(IGetConfigDetailsModel.PERSONAL_TASK_IDS, new HashSet<String>());
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    
    mapToReturn.put(IGetConfigDetailsModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS, new HashMap<>());
    mapToReturn.put(IGetConfigDetailsModel.REFERENCED_LANGUAGES, new LinkedHashMap<>());
    mapToReturn.put(IGetConfigDetailsModel.NUMBER_OF_VERSIONS_TO_MAINTAIN_FOR_PARENT, 0);
    return mapToReturn;
  }
  
  /**
   * fill referencedStatusTags in referencedContext with status tag linked to
   * context Lokesh
   *
   * @param contextNode
   * @param referencedTagMap
   * @param variantContextMap
   * @throws Exception
   */
  protected void fillReferencedTagsAndStatusTags(Vertex contextNode,
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
  
  /**
   * @author Aayush fills languageContextIds, variantContextIds and
   *         promotionVersionContextIds from the klassIds
   * @param klassIds
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillReferencedContextIds(List<String> klassIds, Map<String, Object> mapToReturn)
      throws Exception
  {
    for (String klassId : klassIds) {
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      
      List<String> languageContextIds = (List<String>) mapToReturn
          .get(IGetConfigDetailsModel.LANGAUGE_CONTEXT_IDS);
      List<String> variantContextIds = (List<String>) mapToReturn
          .get(IGetConfigDetailsModel.VARIANT_CONTEXT_IDS);
      List<String> promotionVersionContextIds = (List<String>) mapToReturn
          .get(IGetConfigDetailsModel.PROMOTION_VERSION_CONTEXT_IDS);
      List<Map<String, Object>> contextsWithAutoCreate = (List<Map<String, Object>>) mapToReturn
          .get(IGetConfigDetailsModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE);
      
      Iterable<Vertex> variantContextsIterable = klassNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      for (Vertex variantContextNode : variantContextsIterable) {
        
        String contextId = UtilClass.getCodeNew(variantContextNode);
        Map<String, Object> variantContextMap = UtilClass.getMapFromVertex(
            Arrays.asList(CommonConstants.CODE_PROPERTY, IVariantContext.TYPE,
                IVariantContext.IS_AUTO_CREATE, IVariantContext.DEFAULT_TIME_RANGE),
            variantContextNode);
        
        switch ((String) variantContextMap.get(IVariantContext.TYPE)) {
          case "imageVariant":
          case CommonConstants.CONTEXTUAL_VARIANT:
            Map<String, Object> variantContextMapWithAutoCreate = KlassGetUtils
                .getContextMapWithAutoCreateEnabled(variantContextNode);
            if (variantContextMapWithAutoCreate != null) {
              contextsWithAutoCreate.add(variantContextMapWithAutoCreate);
            }
            if (!variantContextIds.contains(contextId))
              variantContextIds.add(contextId);
            break;
          case CommonConstants.LANGUAGE_VARIANT:
            if (!languageContextIds.contains(contextId))
              languageContextIds.add(contextId);
            break;
          case CommonConstants.PROMOTIONAL_VERSION:
            if (!promotionVersionContextIds.contains(contextId))
              promotionVersionContextIds.add(contextId);
            break;
        }
      }
    }
  }
  
  /**
   * @author Lokesh
   * @param userId
   * @param responseMap
   * @throws Exception
   */
  @Deprecated
  protected void fillReferencedPermission(Map<String, Object> responseMap, String tabType,
      Vertex userInRole, IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    
    Set<Vertex> roles = new HashSet<Vertex>();
    // Get all roles & pass to GlobalPermissionUtil to get task class Ids that
    OrientGraph graphDB = UtilClass.getGraph();
    Iterable<Vertex> roleVertices = graphDB
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      roles.add(roleVertex);
    }
    fillRoleIdsAndTaskIdsHavingReadPermission(responseMap, userInRole, roles);
    
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Map<String, String> templateIdVsKlassTaxonomyId = helperModel.getTemplateIdVsKlassTaxonomyId();
    
    String roleId = UtilClass.getCodeNew(userInRole);
    
    Set<String> entitiesHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.ENTITIES_HAVING_RP);
    entitiesHavingReadPermission.addAll(userInRole.getProperty(IRole.ENTITIES));
    if (entitiesHavingReadPermission.isEmpty()) {
      entitiesHavingReadPermission.addAll(CommonConstants.MODULE_ENTITIES);
    }
    fillGlobalPermissionDetails(roleId, responseMap, helperModel);
    
    Map<String, Object> referenceTemplate = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsModel.REFERENCED_TEMPLATE);
    String templateId = (String) referenceTemplate.get(ITemplate.ID);
    List<Map<String, Object>> templatesForPermission = new ArrayList<>();
    if (templateId == "all") {
      templatesForPermission = (List<Map<String, Object>>) responseMap
          .get(IGetConfigDetailsModel.REFERENCED_TEMPLATES);
    }
    else {
      templatesForPermission
          .add((Map<String, Object>) responseMap.get(IGetConfigDetailsModel.REFERENCED_TEMPLATE));
    }
    
    for (Map<String, Object> templateForPermission : templatesForPermission) {
      String referencedTemplateId = (String) templateForPermission.get(ITemplate.ID);
      String tempateType = (String) templateForPermission.get(ITemplate.TYPE);
      String templateForPermissionId = (String) templateForPermission.get(ITemplate.ID);
      String entityId = null;
      
      /*Iterable<Vertex> tabs = TemplateUtils.getTabFromTemplate(referencedTemplateId);
      
      fillTabPermission(roleId, referencedTemplateId, tabs, referencedPermissions, tabType);
      
      fillHeaderPermission(referencedPermissions, roleId, referencedTemplateId);
      
      if (tabType.equals(CommonConstants.TEMPLATE_CONTEXTUAL_TAB_BASETYPE)
          || tabType.equals(CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE)) {
        continue;
      }*/
      
      fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
          referencedPermissions, referencedTemplateId, roleId);
    }
  }
  
  @Deprecated
  private void fillPropertyCollectionPermissionAndEntitiesPermission(
      Map<String, Object> responseMap, IGetConfigDetailsHelperModel helperModel,
      Map<String, Object> referencedPermissions, String referencedTemplateId, String roleId)
      throws Exception
  {
    Map<String, Set<String>> templateIdVsAssociatedPropertyCollectionIds = helperModel
        .getTemplateIdVsAssociatedPropertyCollectionIds();
    Set<String> referencedPropertyCollection = templateIdVsAssociatedPropertyCollectionIds
        .get(referencedTemplateId);
    if (referencedPropertyCollection != null) {
      GlobalPermissionUtils.fillPropertyCollectionPermission(roleId, referencedTemplateId,
          referencedPropertyCollection, referencedPermissions);
    }
    
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    referencedTaxonomies = (referencedTaxonomies != null) ? referencedTaxonomies : new HashMap<>();
    Set<String> taxonomyIds = new HashSet<>(referencedTaxonomies.keySet());
    Set<String> propertyIds = helperModel.getTemplateIdVsAssociatedPropertyIds()
        .get(referencedTemplateId);
    if (propertyIds != null) {
      Set<String> tempPropertyIds = new HashSet<>(propertyIds);
      taxonomyIds.retainAll(propertyIds);
      tempPropertyIds.removeAll(taxonomyIds);
      fillPropertyPermission(roleId, referencedTemplateId, taxonomyIds, referencedPermissions);
      GlobalPermissionUtils.fillPropertyPermission(roleId, referencedTemplateId, propertyIds,
          referencedPermissions);
    }
    /* Map<String, String> referencedRelationshipMapping = (Map<String, String>) responseMap.get(IGetConfigDetailsForRelationshipTabModel.REFERENCED_RELATIONSHIPS_MAPPING);
    referencedRelationshipMapping = (referencedRelationshipMapping != null) ? referencedRelationshipMapping : new HashMap<>();
    Set<String> relationshipIds = new HashSet<>(referencedRelationshipMapping.keySet());
    /*
    Set<String> relevantRelationshipIds = TemplateUtils.getAllNatureRelationshipsAndRelationshipIdsAssociatedWithTemplate(referencedTemplateId);
    relationshipIds.retainAll(relevantRelationshipIds);
    */
    // GlobalPermissionUtils.fillRelationshipPermission(roleId,
    // referencedTemplateId,
    // relationshipIds, referencedPermissions);
  }
  
  private void fillHeaderPermission(Map<String, Object> referencedPermissions, String roleId,
      String templateId) throws Exception, NotFoundException, MultipleVertexFoundException
  {
    Vertex templateNode = UtilClass.getVertexById(templateId, VertexLabelConstants.TEMPLATE);
    Iterator<Vertex> headerIterator = templateNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE_HEADER)
        .iterator();
    Vertex headerNode = headerIterator.next();
    String headerId = UtilClass.getCodeNew(headerNode);
    Map<String, Object> headerPermissionToMerge = (Map<String, Object>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.HEADER_PERMISSION);
    if (headerPermissionToMerge == null) {
      headerPermissionToMerge = GlobalPermissionUtils.fillDefaultHeaderPermission();
    }
    Map<String, Object> headerPermission = GlobalPermissionUtils.getHeaderPermission(headerId,
        roleId, templateId);
    mergeHeaderPermissions(headerPermissionToMerge, headerPermission);
    referencedPermissions.put(IReferencedTemplatePermissionModel.HEADER_PERMISSION,
        headerPermissionToMerge);
  }
  
  /**
   * fill KlasIds Having Read Permissions from parametered natureKlassIds
   *
   * @author Lokesh
   * @param roleId
   * @param referencedPermissions
   * @param natureKlassIds
   * @throws Exception
   */
  @Deprecated
  protected void fillNatureKlassIdsHavingReadPermission(String roleId,
      Map<String, Object> referencedPermissions, List<String> natureKlassIds) throws Exception
  {
    Set<String> klassIdsHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.KLASS_IDS_HAVING_RP);
    
    for (String klassId : natureKlassIds) {
      if (klassIdsHavingReadPermission.contains(klassId)) {
        continue;
      }
      
      Map<String, Object> permission = GlobalPermissionUtils.getKlassAndTaxonomyPermission(klassId,
          roleId);
      if ((Boolean) permission.get(IGlobalPermission.CAN_READ)) {
        klassIdsHavingReadPermission.add(klassId);
      }
    }
  }
  
  /**
   * @param roleId
   * @param templateId
   * @param tabList
   * @param referencedPermissions
   * @throws Exception
   */
  protected void fillTabPermission(String roleId, String templateId, Iterable<Vertex> tabs,
      Map<String, Object> referencedPermissions, String tabType) throws Exception
  {
    Set<String> visibleTabTypes = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_TAB_TYPES);
    for (Vertex tab : tabs) {
      
      String baseType = (String) tab.getProperty(ITemplateTab.BASE_TYPE);
      String tabId = (String) tab.getProperty(CommonConstants.CODE_PROPERTY);
      Map<String, Object> permission = GlobalPermissionUtils.getTabPermission(tabId, roleId,
          templateId);
      if ((Boolean) permission.get(ITabPermission.IS_VISIBLE)) {
        visibleTabTypes.add(baseType);
      }
      if (tabType.equals(baseType)) {
        referencedPermissions.put(IReferencedTemplatePermissionModel.TAB_PERMISSION, permission);
      }
    }
  }
  
  /**
   * @param roleId
   * @param templateId
   * @param propertyCollectionIds
   * @param referencedPermissions
   * @throws Exception
   */
  protected void fillPropertyCollectionPermission(String roleId, String templateId,
      Set<String> propertyCollectionIds, Map<String, Object> referencedPermissions,
      Boolean isEditableKlassCollection) throws Exception
  {
    Set<String> editablePCodes = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS);
    Set<String> expandablePCodes = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EXPANDABLE_PROPERTY_COLLECTION_IDS);
    Set<String> visiblePCodes = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS);
    
    for (String pCode : propertyCollectionIds) {
      Map<String, Object> permission = GlobalPermissionUtils.getPropertyCollectionPermission(pCode,
          roleId, templateId);
      if ((Boolean) permission.get(IPropertyCollectionPermission.CAN_EDIT)
          && isEditableKlassCollection) {
        editablePCodes.add(pCode);
      }
      if ((Boolean) permission.get(IPropertyCollectionPermission.IS_EXPANDED)) {
        expandablePCodes.add(pCode);
      }
      if ((Boolean) permission.get(IPropertyCollectionPermission.IS_VISIBLE)) {
        visiblePCodes.add(pCode);
      }
    }
  }
  
  /**
   * @param roleId
   * @param templateId
   * @param entityIds
   * @param referencedPermissions
   * @throws Exception
   */
  protected void fillPropertyPermission(String roleId, String templateId, Set<String> entityIds,
      Map<String, Object> referencedPermissions, Boolean isKlassEditable) throws Exception
  {
    
    Set<String> editableEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    Set<String> visibleEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
    
    for (String entityId : entityIds) {
      Map<String, Object> permission = GlobalPermissionUtils.getPropertyPermission(entityId, roleId,
          templateId);
      if ((Boolean) permission.get(IPropertyPermission.CAN_EDIT) && isKlassEditable) {
        editableEntityIds.add(entityId);
      }
      if ((Boolean) permission.get(IPropertyPermission.IS_VISIBLE)) {
        visibleEntityIds.add(entityId);
      }
    }
  }
  
  /**
   * @param roleId
   * @param templateId
   * @param entityIds
   * @param referencedPermissions
   * @throws Exception
   */
  /*  protected void fillTaxonomyPropertyPermission(String roleId, String templateId,
      Set<String> entityIds, Map<String, Object> referencedPermissions) throws Exception
  {
  
    Set<String> editableEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    Set<String> visibleEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
  
    for (String entityId : entityIds) {
      Map<String, Object> permission = GlobalPermissionUtils.getPropertyPermission(entityId, roleId, templateId);
      if ((Boolean) permission.get(IPropertyPermission.CAN_EDIT)) {
        editableEntityIds.add(entityId);
      }
      if ((Boolean) permission.get(IPropertyPermission.IS_VISIBLE)) {
        visibleEntityIds.add(entityId);
      }
    }
  }*/
  
  protected void fillRelationshipPermission(String roleId, String templateId,
      Set<String> relationshipIds, Map<String, Object> referencedPermissions,
      Boolean isContextOrKlassEditable) throws Exception
  {
    Set<String> canAddRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.CAN_ADD_RELATIONSHIP_IDS);
    Set<String> canDeleteRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.CAN_DELETE_RELATIONSHIP_IDS);
    Set<String> visibleRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_RELATIONSHIP_IDS);
    
    for (String relationshipId : relationshipIds) {
      Map<String, Object> permission = GlobalPermissionUtils
          .getRelationshipPermission(relationshipId, roleId, templateId);
      if ((Boolean) permission.get(IRelationshipPermission.CAN_ADD) && isContextOrKlassEditable) {
        canAddRelationhipIds.add(relationshipId);
      }
      if ((Boolean) permission.get(IRelationshipPermission.CAN_DELETE)
          && isContextOrKlassEditable) {
        canDeleteRelationhipIds.add(relationshipId);
      }
      if ((Boolean) permission.get(IRelationshipPermission.IS_VISIBLE)) {
        visibleRelationhipIds.add(relationshipId);
      }
    }
  }
  
  /**
   * @author Lokesh
   * @param userId
   * @param roleId
   * @param configDetails
   * @param taxonomyIds
   * @throws Exception
   */
  protected void fillGlobalPermissionDetails(String roleId, Map<String, Object> configDetails,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedPermission = (Map<String, Object>) configDetails
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    
    Map<String, Object> globalPermissionMap = (Map<String, Object>) referencedPermission
        .get(IReferencedTemplatePermissionModel.GLOBAL_PERMISSION);
    if (globalPermissionMap == null) {
      globalPermissionMap = GlobalPermissionUtils.getDefaultGlobalPermission();
      referencedPermission.put(IReferencedTemplatePermissionModel.GLOBAL_PERMISSION,
          globalPermissionMap);
    }
    
    for (String klassId : helperModel.getInstanceKlassIds()) {
      Vertex klassVertext = UtilClass.getVertexByIndexedId(klassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Map<String, Object> klass = UtilClass.getMapFromVertex(Arrays.asList(IKlass.IS_NATURE),
          klassVertext);
      Boolean isNature = (Boolean) klass.get(IKlass.IS_NATURE);
      if (isNature == null || !isNature) {
        continue;
      }
      Map<String, Object> permissionMap = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(klassId, roleId);
      
      Set<String> klassIdsHavingReadPermission = (Set<String>) referencedPermission
          .get(IReferencedTemplatePermissionModel.KLASS_IDS_HAVING_RP);
      if (klassIdsHavingReadPermission.contains(klassId)
          || klassIdsHavingReadPermission.isEmpty()) {
        permissionMap.put(IGlobalPermission.CAN_READ, true);
      }
      else {
        permissionMap.put(IGlobalPermission.CAN_READ, false);
      }
      GlobalPermissionUtils.mergeGlobalPermissons(globalPermissionMap, permissionMap);
    }
    boolean isKlassReadPermission = (boolean) globalPermissionMap.get(IGlobalPermission.CAN_READ);
    
    Map<String, Object> globalReadPermissionForTaxonomyMap = new HashMap<>();
    globalReadPermissionForTaxonomyMap.put(IGlobalPermission.CAN_READ, false);
    for (Vertex taxonomyVertex : helperModel.getTaxonomyVertices()) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
      Map<String, Object> permissionMap = GlobalPermissionUtils
          .getKlassAndTaxonomyPermission(taxonomyId, roleId);
      Set<String> taxonomyIdsHavingRP = (Set<String>) referencedPermission
          .get(IReferencedTemplatePermissionModel.TAXONOMY_IDS_HAVING_RP);
      Set<String> allTaxonomyIdsHavingRP = (Set<String>) referencedPermission
          .get(IReferencedTemplatePermissionModel.ALL_TAXONOMY_IDS_HAVING_RP);
      if (allTaxonomyIdsHavingRP.contains(taxonomyId) || taxonomyIdsHavingRP.isEmpty()) {
        permissionMap.put(IGlobalPermission.CAN_READ, true);
      }
      else {
        permissionMap.put(IGlobalPermission.CAN_READ, false);
      }
      Boolean canRead = (Boolean) permissionMap.get(IGlobalPermission.CAN_READ)
          || (Boolean) globalReadPermissionForTaxonomyMap.get(IGlobalPermission.CAN_READ);
      globalReadPermissionForTaxonomyMap.put(IGlobalPermission.CAN_READ, canRead);
    }
    
    boolean isTaxonomyReadPermission = (boolean) globalReadPermissionForTaxonomyMap
        .get(IGlobalPermission.CAN_READ) || helperModel.getTaxonomyVertices()
            .isEmpty();
    if (isKlassReadPermission == true && isTaxonomyReadPermission == true) {
      globalPermissionMap.put(IGlobalPermission.CAN_READ, true);
    }
    else {
      globalPermissionMap.put(IGlobalPermission.CAN_READ, false);
    }
    
    if ((Boolean) globalPermissionMap.get(IGlobalPermission.CAN_CREATE)
        && (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_READ)
        && (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_EDIT)
        && (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_DELETE)) {
      return;
    }
  }
  
  /**
   * fills referencedTasks for the associated klassIds
   *
   * @author Arshad
   * @param klassIds
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillReferencedTasks(List<String> klassIds, Map<String, Object> mapToReturn)
      throws Exception
  {
    Map<String, Object> referencedTasks = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TASKS);
    for (String klassId : klassIds) {
      Vertex klassVertex = null;
      try {
        klassVertex = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      
      Iterable<Vertex> taskVertices = klassVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TASK);
      for (Vertex taskVertex : taskVertices) {
        String taskId = UtilClass.getCodeNew(taskVertex);
        if (referencedTasks.get(taskId) != null) {
          continue;
        }
        
        Map<String, Object> taskMap = TasksUtil.getTaskMapFromNode(taskVertex);
        referencedTasks.put(taskId, taskMap);
      }
    }
  }
  
  /**
   * fills referencedTasks for the associated klassIds
   *
   * @author Arshad
   * @param klassIds
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillReferencedTasks(Vertex klassVertex, Map<String, Object> mapToReturn)
      throws Exception
  {
    Map<String, Object> referencedTasks = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TASKS);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    
    Iterable<Vertex> taskVertices = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TASK);
    for (Vertex taskVertex : taskVertices) {
      String taskId = UtilClass.getCodeNew(taskVertex);
      if (referencedTasks.get(taskId) != null) {
        continue;
      }
      
      Map<String, Object> taskMap = TasksUtil.getTaskMapFromNode(taskVertex);
      referencedTasks.put(taskId, taskMap);
      String statusTagId = (String) taskMap.get(ITask.STATUS_TAG);
      if (statusTagId != null && !referencedTags.containsKey(statusTagId)) {
        Vertex statusTag = UtilClass.getVertexById(statusTagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(statusTag, true);
        referencedTags.put(statusTagId, referencedTag);
      }
      String priorityTagId = (String) taskMap.get(ITask.PRIORITY_TAG);
      if (priorityTagId != null && !referencedTags.containsKey(priorityTagId)) {
        Vertex priorityTag = UtilClass.getVertexById(priorityTagId,
            VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> referencedTag = TagUtils.getTagMap(priorityTag, true);
        referencedTags.put(priorityTagId, referencedTag);
      }
    }
  }
  
  /**
   * Description : For all klassIds passed, get all relationship nodes
   * connected, fetch respective side KR nodes and fill
   * referencedRelationshipProperties map\ with attribute/tags info, klassIds
   * that are connected to kR Nodes and targetTypes.
   *
   * @author Ajit
   * @param klassIds
   * @param mapToReturn
   * @throws Exception
   */
  protected void fillRelationshipsProperties(List<String> klassIds, Map<String, Object> mapToReturn)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    
    String query = "select from (select expand(in('has_property')) from "
        + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP
        + ") where in('has_klass_property') contains (code in " + EntityUtil.quoteIt(klassIds)
        + ")";
    Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
        .execute();
    for (Vertex kRNode : iterable) {
      Iterator<Vertex> relationshipIterator = kRNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      Vertex relationshipNode = relationshipIterator.next();
      String relationshipId = UtilClass.getCodeNew(relationshipNode);
      String label = (String) UtilClass.getValueByLanguage(relationshipNode,
          CommonConstants.LABEL_PROPERTY);
      Map<String, Object> relationshipPropertiesMap = new HashMap<>();
      relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.ID, relationshipId);
      relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
      relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.CODE,
          (String) relationshipNode.getProperty(IReferencedRelationshipProperty.CODE));
      RelationshipUtils.populatePropetiesInfoNew(relationshipNode, relationshipPropertiesMap);
      referencedRelationshipProperties.put(relationshipId, relationshipPropertiesMap);
    }
  }
  
  protected void fillNatureRelationshipsProperties(List<String> klassIds,
      Map<String, Object> mapToReturn) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Map<String, Object> referencedRelationshipProperties;
    referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    
    for (String klassId : klassIds) {
      
      String query = "select from (select expand(in('has_property')) from "
          + VertexLabelConstants.NATURE_RELATIONSHIP
          + ") where in('has_klass_property') contains (code='" + klassId + "')";
      
      Iterable<Vertex> iterable = graph.command(new OCommandSQL(query))
          .execute();
      for (Vertex kRNode : iterable) {
        Iterator<Vertex> relationshipIterator = kRNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
            .iterator();
        if (!relationshipIterator.hasNext()) {
          relationshipIterator = kRNode
              .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
              .iterator();
        }
        Vertex relationshipNode = relationshipIterator.next();
        String relationshipId = UtilClass.getCodeNew(relationshipNode);
        String label = (String) UtilClass.getValueByLanguage(relationshipNode,
            CommonConstants.LABEL_PROPERTY);
        Map<String, Object> relationshipPropertiesMap = new HashMap<>();
        relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.ID, relationshipId);
        relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
        relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.CODE,
            (String) relationshipNode.getProperty(IReferencedRelationshipProperty.CODE));
        RelationshipUtils.populatePropetiesInfoNew(relationshipNode, relationshipPropertiesMap);
        referencedRelationshipProperties.put(relationshipId, relationshipPropertiesMap);
      }
    }
  }
  
  /**
   * fills taskIds having read permission for the current user (configured for
   * all roles of current user) fill all roleIds having read permission
   *
   * @param mapToReturn
   * @param rolesContainingLoginUser
   * @param roles
   *          : all roleIds
   * @throws Exception
   */
  protected void fillRoleIdsAndTaskIdsHavingReadPermission(Map<String, Object> mapToReturn,
      Vertex userInRole, Set<Vertex> roles) throws Exception
  {
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Map<String, Object> taskIdsForRoleHavingReadPermission = (Map<String, Object>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.TASK_IDS_FOR_ROLES_HAVING_READ_PERMISSION);
    Map<String, Object> referencedTasks = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_TASKS);
    Set<String> taskIdsHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.TASK_IDS_HAVING_READ_PERMISSION);
    Set<String> taskIds = referencedTasks.keySet();
    
    for (String taskId : taskIds) {
      for (Vertex role : roles) {
        String roleId = UtilClass.getCodeNew(role);
        Map<String, Object> globalPermissions = GlobalPermissionUtils
            .getKlassAndTaxonomyPermission(taskId, roleId);
        List<String> taskIdForRoleHavingReadPermission = (List<String>) taskIdsForRoleHavingReadPermission
            .get(roleId);
        if (taskIdForRoleHavingReadPermission == null) {
          taskIdForRoleHavingReadPermission = new ArrayList<>();
          taskIdsForRoleHavingReadPermission.put(roleId, taskIdForRoleHavingReadPermission);
        }
        if ((Boolean) globalPermissions.get(IGlobalPermission.CAN_READ)) {
          taskIdForRoleHavingReadPermission.add(taskId);
          if (userInRole.equals(role)) {
            taskIdsHavingReadPermission.add(taskId);
          }
        }
      }
    }
  }
  
  /**
   * fill referenced variant Context and also add asociated PC in referencedPC
   * and fill respective entities in entityIds list And return selected
   * contextId if not provided by UI..
   *
   * @param klassIds
   * @param nodeLabel
   * @param mapToReturn
   * @param requestContextId
   * @param entityIds
   * @param tabType
   * @param isCampare
   * @throws Exception
   */
  @Deprecated
  protected void fillVariantContextDetailsAndReturnSelectedContextId(List<String> klassIds,
      String nodeLabel, Map<String, Object> mapToReturn, String requestContextId, String tabType,
      Boolean isCampare, Boolean isGetSelfTabContext) throws Exception
  {
    for (String klassId : klassIds) {
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(klassId, nodeLabel);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      Map<String, Object> referencedVariantContextsMap = (Map<String, Object>) mapToReturn
          .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
      
      Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContextsMap
          .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
      Map<String, Object> languageVariantContexts = (Map<String, Object>) referencedVariantContextsMap
          .get(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS);
      
      Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
          .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS);
      
      List<String> languageContextIds = (List<String>) mapToReturn
          .get(IGetConfigDetailsModel.LANGAUGE_CONTEXT_IDS);
      List<String> variantContextIds = (List<String>) mapToReturn
          .get(IGetConfigDetailsModel.VARIANT_CONTEXT_IDS);
      List<String> promotionVersionContextIds = (List<String>) mapToReturn
          .get(IGetConfigDetailsModel.PROMOTION_VERSION_CONTEXT_IDS);
      
      Iterable<Vertex> variantContextsIterable = klassNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.VARIANT_CONTEXT_OF);
      for (Vertex variantContextNode : variantContextsIterable) {
        String contextType = (String) variantContextNode.getProperty(IVariantContext.TYPE);
        if (!isGetSelfTabContext) {
          /*  if(tabType.equals(CommonConstants.TEMPLATE_CONTEXTUAL_TAB_BASETYPE) && (contextType.equals(CommonConstants.CONTEXTUAL_VARIANT) || contextType.equals(CommonConstants.IMAGE_VARIANT))){
            continue;
          } else*/ if (tabType.equals(CommonConstants.TEMPLATE_LANGUAGE_TAB_BASETYPE)
              && contextType.equals(CommonConstants.LANGUAGE_VARIANT)) {
            continue;
          }
        }
        String contextId = UtilClass.getCodeNew(variantContextNode);
        Map<String, Object> variantContextMap = VariantContextUtils
            .getReferencedContexts(variantContextNode);
        
        if (contextId.equals(requestContextId)) {
          List<String> propertyCollectionIds = getPropertyCollectionIds(variantContextNode);
          
          variantContextMap.put(IReferencedVariantContextModel.PROPERTY_COLLECTIONS,
              propertyCollectionIds);
        }
        /*else if(isCampare){
          //if it is a campare call & contextId is not equal to request contextId then continue
          continue;
        }*/
        
        if (requestContextId == null) {
          /* if(tabType.equals(CommonConstants.TEMPLATE_CONTEXTUAL_TAB_BASETYPE) && (contextType.equals(CommonConstants.CONTEXTUAL_VARIANT))){
              requestContextId = contextId;
            } else if(tabType.equals(CommonConstants.TEMPLATE_LANGUAGE_TAB_BASETYPE) && contextType.equals(CommonConstants.LANGUAGE_VARIANT)){
              requestContextId = contextId;
          }*/
        }
        
        fillContextDetails(embeddedVariantContexts, languageVariantContexts, referencedTagsMap,
            languageContextIds, variantContextIds, promotionVersionContextIds, variantContextNode,
            variantContextMap, true);
        fillSubsetChildrenInVariantMap(variantContextNode, variantContextMap);
      }
    }
  }
  
  /**
   * get PropertyCollection Ids List for contextNode
   *
   * @author Lokesh
   * @param parentContextNode
   * @return
   * @throws Exception
   */
  protected List<String> getPropertyCollectionIds(Vertex parentContextNode) throws Exception
  {
    /*Vertex templateNode = TemplateUtils.getTemplateFromKlassIfExist(parentContextNode);
    if(templateNode == null) {
      return new ArrayList<>();
    }
    Vertex contextSequenceNode = TemplateUtils.getSequenceFromTemplate(templateNode,
        RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE);
    List<String> propertyCollectionIds = contextSequenceNode
        .getProperty(CommonConstants.SEQUENCE_LIST);
    return propertyCollectionIds;*/
    return null;
  }
  
  /**
   * fill context details in respective parametered ArrayList
   *
   * @param embeddedVariantContexts
   * @param languageVariantContexts
   * @param referencedTagsMap
   * @param languageContextIds
   * @param variantContextIds
   * @param promotionVersionContextIds
   * @param variantContextNode
   * @param variantContextMap
   * @throws Exception
   */
  protected void fillContextDetails(Map<String, Object> embeddedVariantContexts,
      Map<String, Object> languageVariantContexts, Map<String, Object> referencedTagsMap,
      List<String> languageContextIds, List<String> variantContextIds,
      List<String> promotionVersionContextIds, Vertex variantContextNode,
      Map<String, Object> variantContextMap, Boolean isAddInContextIds) throws Exception
  {
    fillReferencedTagsAndStatusTags(variantContextNode, referencedTagsMap, variantContextMap);
    
    for (Map<String, Object> contextTags : (List<Map<String, Object>>) variantContextMap
        .get(IReferencedVariantContextModel.TAGS)) {
      String entityId = (String) contextTags.get(IReferencedVariantContextTagsModel.TAG_ID);
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
    
    String variantContextId = (String) variantContextMap.get(IReferencedVariantContextModel.ID);
    switch ((String) variantContextMap.get(IVariantContext.TYPE)) {
      case "imageVariant":
      case CommonConstants.CONTEXTUAL_VARIANT:
        if (!embeddedVariantContexts.containsKey(variantContextId)) {
          embeddedVariantContexts.put(variantContextId, variantContextMap);
          if (isAddInContextIds) {
            variantContextIds.add(variantContextId);
          }
        }
        break;
      case CommonConstants.LANGUAGE_VARIANT:
        if (!languageVariantContexts.containsKey(variantContextId)) {
          languageVariantContexts.put(variantContextId, variantContextMap);
          if (isAddInContextIds) {
            languageContextIds.add(variantContextId);
          }
        }
        break;
      case CommonConstants.PROMOTIONAL_VERSION:
        if (!promotionVersionContextIds.contains(variantContextId) && isAddInContextIds)
          promotionVersionContextIds.add(variantContextId);
        break;
    }
  }
  
  /**
   * @param variantContextNode
   * @param variantContextMap
   */
  protected void fillSubsetChildrenInVariantMap(Vertex variantContextNode,
      Map<String, Object> variantContextMap)
  {
    List<String> childrenList = new ArrayList<String>();
    Iterable<Vertex> iterable = variantContextNode.getVertices(Direction.IN,
        RelationshipLabelConstants.SUB_CONTEXT_OF);
    for (Vertex variantNode : iterable) {
      String variantContextId = UtilClass.getCodeNew(variantNode);
      childrenList.add(variantContextId);
    }
    variantContextMap.put(IReferencedVariantContextModel.CHILDREN, childrenList);
  }
  
  /**
   * fill Image Context having autoCreate = true(enabled)
   *
   * @author Lokesh
   * @param mapToReturn
   * @param variantContextMap
   * @param variantContextNode
   * @throws NotFoundException
   */
  protected void fillTechnicalImageVariantWithAutoCreateEnabled(Map<String, Object> mapToReturn,
      Map<String, Object> variantContextMap, Vertex variantContextNode) throws NotFoundException
  {
    
    Boolean isAutoCreate = (Boolean) variantContextMap.get(IVariantContext.IS_AUTO_CREATE);
    if (isAutoCreate != null && !isAutoCreate) {
      return;
    }
    List<Map<String, Object>> technicalImageVariantWithAutoCreateEnabled = (List<Map<String, Object>>) mapToReturn
        .get(IGetConfigDetailsModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE);
    
    List<Map<String, Object>> contextTagsList = VariantContextUtils
        .getContextTags(variantContextNode);
    Map<String, Object> imageVariantMap = new HashMap<String, Object>();
    imageVariantMap.put(ITechnicalImageVariantWithAutoCreateEnableModel.ID,
        variantContextMap.get(IVariantContext.ID));
    imageVariantMap.put(ITechnicalImageVariantWithAutoCreateEnableModel.CONTEXTUAL_TAGS,
        contextTagsList);
    
    List<String> attributeIds = new ArrayList<String>();
    imageVariantMap.put(ITechnicalImageVariantWithAutoCreateEnableModel.ATTRIBUTE_IDS,
        attributeIds);
    Iterable<Vertex> vertices = variantContextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    for (Vertex klassPropertyNode : vertices) {
      String type = klassPropertyNode.getProperty(CommonConstants.TYPE);
      if (!type.equals(CommonConstants.ATTRIBUTE)) {
        continue;
      }
      Iterator<Vertex> iterator = klassPropertyNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator();
      if (!iterator.hasNext()) {
        throw new NotFoundException();
      }
      Vertex attributeNode = iterator.next();
      attributeIds.add(UtilClass.getCodeNew(attributeNode));
    }
    technicalImageVariantWithAutoCreateEnabled.add(imageVariantMap);
  }
  
  /**
   * fill the allowed templates for all the roles containing login user returns
   * the defaultTemplateId of first role for which default template is defined
   * if default template is not defined for any role it returns default template
   * node of natureKlassNode
   *
   * @author Aayush and Arshad
   * @param mapToReturn
   * @param rolesForUser
   * @param natureKlassNode
   * @return
   * @throws Exception
   */
  protected String getDefaultTemplateIdAndFillAllowedTemplates(Map<String, Object> mapToReturn,
      Vertex userInRole, Vertex natureKlassNode, IGetConfigDetailsHelperModel helperModel)
      throws Exception
  {
    String defaultTemplateId = null;
    Set<Vertex> allowedTemplates = new HashSet<>();
    String natureKlassId = UtilClass.getCodeNew(natureKlassNode);
    Map<String, List<String>> klassIdVsAllowedTemplate = new HashMap<>();
    Map<String, Object> referencedPermission = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Set<String> klassIdsHavingReadPermission = (Set<String>) referencedPermission
        .get(IReferencedTemplatePermissionModel.KLASS_IDS_HAVING_RP);
    Set<String> allTaxonomyIdsHavingReadPermission = (Set<String>) referencedPermission
        .get(IReferencedTemplatePermissionModel.ALL_TAXONOMY_IDS_HAVING_RP);
    Map<String, String> templateIdVsKlassTaxonomyId = helperModel.getTemplateIdVsKlassTaxonomyId();
    
    String query = "select from " + VertexLabelConstants.KLASS_TAXONOMY_GLOBAL_PERMISSIONS + " "
        + "where in('" + RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_GLOBAL_PERMISSION
        + "').code contains \"" + natureKlassId + "\" and in('"
        + RelationshipLabelConstants.HAS_KLASS_TAXONOMY_GLOBAL_PERMISSIONS + "').code contains \""
        + UtilClass.getCodeNew(userInRole) + "\"";
    
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> globalPermissionIterator = vertices.iterator();
    if (globalPermissionIterator.hasNext()) {
      Vertex globalPermissionNode = globalPermissionIterator.next();
      
      Iterable<Vertex> allowedTemplatesIterable = globalPermissionNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_ALLOWED_TEMPLATE);
      for (Vertex allowedTemplate : allowedTemplatesIterable) {
        allowedTemplates.add(allowedTemplate);
      }
      
      if (defaultTemplateId == null) {
        Iterator<Vertex> iterator = globalPermissionNode
            .getVertices(Direction.OUT,
                RelationshipLabelConstants.RELATIONSHIPLABEL_HAS_DEFAULT_TEMPLATE)
            .iterator();
        if (iterator.hasNext()) {
          Vertex defaultTemplateNode = iterator.next();
          defaultTemplateId = UtilClass.getCodeNew(defaultTemplateNode);
          allowedTemplates.add(defaultTemplateNode);
        }
      }
    }
    /*try {
      Vertex templateNode = TemplateUtils.getTemplateFromKlass(natureKlassNode);
      allowedTemplates.add(templateNode);
      templateIdVsKlassTaxonomyId.put(UtilClass.getCode(templateNode), natureKlassId);
    }
    catch (NotFoundException e) {
      throw new TemplateNotFoundException();
    }*/
    klassIdVsAllowedTemplate.put(natureKlassId,
        UtilClass.getCodes(new ArrayList<>(allowedTemplates)));
    
    // Fill all templates for nonNatureNode
    Set<Vertex> nonNatureKlassNodes = helperModel.getNonNatureKlassNodes();
    
    for (Vertex nonNatureKlassNode : nonNatureKlassNodes) {
      if (!klassIdsHavingReadPermission.isEmpty()
          && !klassIdsHavingReadPermission.contains(UtilClass.getCodeNew(nonNatureKlassNode))) {
        continue;
      }
      
      /*Vertex templateNode = TemplateUtils.getTemplateFromKlass(nonNatureKlassNode);
            String templateId =  UtilClass.getCode(templateNode);
            String nonNatureKlassId = UtilClass.getCode(nonNatureKlassNode);
            allowedTemplates.add(templateNode);
            List<String> templateIdsAssociated = new ArrayList<>();
            templateIdsAssociated.add(templateId);
            klassIdVsAllowedTemplate.put(nonNatureKlassId, templateIdsAssociated);
            templateIdVsKlassTaxonomyId.put(templateId, nonNatureKlassId);
      */
    }
    
    Set<Vertex> taxonomyVertices = helperModel.getTaxonomyVertices();
    
    /*for (Vertex taxonomyNode : taxonomyVertices) {
      if (!allTaxonomyIdsHavingReadPermission.contains(UtilClass.getCode(taxonomyNode))) {
        continue;
      }
      try{
        //Vertex templateNode = TemplateUtils.getTemplateFromKlass(taxonomyNode);
        //String templateId =  UtilClass.getCode(templateNode);
        String taxonomyId = UtilClass.getCode(taxonomyNode);
        List<String> templateIdsAssociated = new ArrayList<>();
        templateIdsAssociated.add(templateId);
        klassIdVsAllowedTemplate.put(taxonomyId, templateIdsAssociated);
        templateIdVsKlassTaxonomyId.put(templateId, taxonomyId);
        if (allowedTemplates.contains(templateNode)) {
          continue;
        }
        allowedTemplates.add(templateNode);
      }
      catch(TemplateNotFoundException e) {
        continue;
      }
    }*/
    
    List<Map<String, Object>> templates = (List<Map<String, Object>>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_TEMPLATES);
    for (Vertex templateNode : allowedTemplates) {
      Map<String, Object> templateMap = UtilClass.getMapFromNode(templateNode);
      templates.add(templateMap);
    }
    
    return defaultTemplateId;
  }
  
  private Boolean checkIfNatureRelationshipIsPresentInTemplate(String templateId,
      String relationshipId)
  {
    String query = "select from " + RelationshipLabelConstants.HAS_TEMPLATE_NATURE_RELATIONSHIP
        + " where in.code = '" + relationshipId + "' and out.code = '" + templateId + "'";
    
    Iterable<Edge> iterable = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Edge> iterator = iterable.iterator();
    if (iterator.hasNext()) {
      return true;
    }
    return false;
  }
  
  protected void fillReferencedRACIVSRoles(Map<String, Object> mapToReturn) throws Exception
  {
    List<String> RACIVSRoles = new ArrayList<>();
    RACIVSRoles.add(SystemLevelIds.RESPONSIBLE);
    RACIVSRoles.add(SystemLevelIds.ACCOUNTABLE);
    RACIVSRoles.add(SystemLevelIds.CONSULTED);
    RACIVSRoles.add(SystemLevelIds.INFORMED);
    RACIVSRoles.add(SystemLevelIds.VERIFY);
    RACIVSRoles.add(SystemLevelIds.SIGN_OFF);
    
    Map<String, Object> referencedRoles = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForTasksTabModel.REFERENCED_ROLES);
    Iterable<Vertex> roleVertices = UtilClass.getVerticesByIds(RACIVSRoles,
        VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      referencedRoles.put(roleVertex.getProperty(CommonConstants.CODE_PROPERTY),
          RoleUtils.getRoleEntityMap(roleVertex));
    }
  }
  
  /*  private void mergeHeaderPermissions(Map<String, Object> headerPermissionToMerge, Map<String, Object> headerPermission)
  {
    Boolean viewName = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_NAME)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_NAME);
    Boolean canEditName = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_EDIT_NAME)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_EDIT_NAME);
    Boolean viewIcon = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_ICON)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_ICON);
    Boolean canChangeIcon = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_CHANGE_ICON)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_CHANGE_ICON);
    Boolean viewPrimaryType = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_PRIMARY_TYPE)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_PRIMARY_TYPE);
    Boolean canEditPrimaryType = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE);
    Boolean viewAdditionalClasses = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_ADDITIONAL_CLASSES)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_ADDITIONAL_CLASSES);
    Boolean canAddClasses = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_ADD_CLASSES)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_ADD_CLASSES);
    Boolean canDeleteClasses = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_DELETE_CLASSES)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_DELETE_CLASSES);
    Boolean viewTaxonomies = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_TAXONOMIES)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_TAXONOMIES);
    Boolean canAddTaxonomy = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_ADD_TAXONOMY)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_ADD_TAXONOMY);
    Boolean canDeleteTaxonomy = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_DELETE_TAXONOMY)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_DELETE_TAXONOMY);
    Boolean viewStatusTag = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_STATUS_TAGS)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_STATUS_TAGS);
    Boolean canEditStatusTag = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_EDIT_STATUS_TAG)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_EDIT_STATUS_TAG);
    Boolean viewCreatedOn = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_CREATED_ON)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_CREATED_ON);
    Boolean viewLastModifiedBy = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_LAST_MODIFIED_BY)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_LAST_MODIFIED_BY);
  
    headerPermissionToMerge.put(IHeaderPermission.VIEW_NAME, viewName);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_NAME, canEditName);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_ICON, viewIcon);
    headerPermissionToMerge.put(IHeaderPermission.CAN_CHANGE_ICON, canChangeIcon);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_PRIMARY_TYPE, viewPrimaryType);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE, canEditPrimaryType);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_ADDITIONAL_CLASSES, viewAdditionalClasses);
    headerPermissionToMerge.put(IHeaderPermission.CAN_ADD_CLASSES, canAddClasses);
    headerPermissionToMerge.put(IHeaderPermission.CAN_DELETE_CLASSES, canDeleteClasses);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_TAXONOMIES, viewTaxonomies);
    headerPermissionToMerge.put(IHeaderPermission.CAN_ADD_TAXONOMY, canAddTaxonomy);
    headerPermissionToMerge.put(IHeaderPermission.CAN_DELETE_TAXONOMY, canDeleteTaxonomy);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_STATUS_TAGS, viewStatusTag);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_STATUS_TAG, canEditStatusTag);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_CREATED_ON, viewCreatedOn);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_LAST_MODIFIED_BY, viewLastModifiedBy);
  }*/
  
  protected void mergeHeaderVisibility(Map<String, Object> templateMapToReturn,
      Map<String, Object> templateMap)
  {
    Map<String, Object> mergedHeaderVisibility = (Map<String, Object>) templateMapToReturn
        .get(ITemplate.HEADER_VISIBILITY);
    Map<String, Object> headerVisibility = (Map<String, Object>) templateMap
        .get(ITemplate.HEADER_VISIBILITY);
    if (mergedHeaderVisibility == null) {
      templateMapToReturn.put(ITemplate.HEADER_VISIBILITY, headerVisibility);
    }
    else {
      Boolean viewName = (Boolean) headerVisibility.get(ITemplateHeaderVisibility.VIEW_NAME)
          || (Boolean) mergedHeaderVisibility.get(ITemplateHeaderVisibility.VIEW_NAME);
      Boolean viewIcon = (Boolean) headerVisibility.get(ITemplateHeaderVisibility.VIEW_ICON)
          || (Boolean) mergedHeaderVisibility.get(ITemplateHeaderVisibility.VIEW_ICON);
      Boolean viewPrimaryType = (Boolean) headerVisibility
          .get(ITemplateHeaderVisibility.VIEW_PRIMARY_TYPE)
          || (Boolean) mergedHeaderVisibility.get(ITemplateHeaderVisibility.VIEW_PRIMARY_TYPE);
      Boolean viewAdditionKlasses = (Boolean) headerVisibility
          .get(ITemplateHeaderVisibility.VIEW_ADDITIONAL_KLASSES)
          || (Boolean) mergedHeaderVisibility
              .get(ITemplateHeaderVisibility.VIEW_ADDITIONAL_KLASSES);
      Boolean viewTaxonomies = (Boolean) headerVisibility
          .get(ITemplateHeaderVisibility.VIEW_TAXONOMIES)
          || (Boolean) mergedHeaderVisibility.get(ITemplateHeaderVisibility.VIEW_TAXONOMIES);
      Boolean viewStatusTags = (Boolean) headerVisibility
          .get(ITemplateHeaderVisibility.VIEW_STATUS_TAGS)
          || (Boolean) mergedHeaderVisibility.get(ITemplateHeaderVisibility.VIEW_STATUS_TAGS);
      Boolean viewCreateOn = (Boolean) headerVisibility
          .get(ITemplateHeaderVisibility.VIEW_CREATED_ON)
          || (Boolean) mergedHeaderVisibility.get(ITemplateHeaderVisibility.VIEW_CREATED_ON);
      Boolean viewLastModifiedBy = (Boolean) headerVisibility
          .get(ITemplateHeaderVisibility.VIEW_LASTMODIFIED_BY)
          || (Boolean) mergedHeaderVisibility.get(ITemplateHeaderVisibility.VIEW_LASTMODIFIED_BY);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_NAME, viewName);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_ICON, viewIcon);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_PRIMARY_TYPE, viewPrimaryType);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_ADDITIONAL_KLASSES,
          viewAdditionKlasses);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_TAXONOMIES, viewTaxonomies);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_STATUS_TAGS, viewStatusTags);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_CREATED_ON, viewCreateOn);
      mergedHeaderVisibility.put(ITemplateHeaderVisibility.VIEW_LASTMODIFIED_BY,
          viewLastModifiedBy);
    }
  }
  
  protected void fillPersonalTaskIds(Map<String, Object> maptoReturn)
  {
    Set<String> personalTaskIds = (Set<String>) maptoReturn
        .get(IGetConfigDetailsModel.PERSONAL_TASK_IDS);
    OrientGraph graphDB = UtilClass.getGraph();
    Iterable<Vertex> taskVertices = graphDB
        .getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_TASK);
    for (Vertex taskVertex : taskVertices) {
      if ((taskVertex.getProperty(ITask.TYPE)).equals(CommonConstants.TASK_TYPE_PERSONAL)) {
        personalTaskIds.add(UtilClass.getCodeNew(taskVertex));
      }
    }
  }
  
  /**
   * This function fill referencedEmbeddedContext with its directly attached
   * context if present. This is for variantInstance
   *
   * @author Lokesh
   * @param mapToReturn
   * @param templateNode
   * @throws Exception
   */
  protected void fillReferencedEmbeddedContext(Map<String, Object> mapToReturn, Vertex templateNode)
      throws Exception
  {
    /*
     * TODO change strategy
    String templateType = templateNode.getProperty(ITemplate.TYPE);
    if(CommonConstants.CUSTOM_TEMPLATE.equals(templateType))
    {
      return;
    }
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForRelationshipTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedContexts = (Map<String, Object>) referencedVariantContexts.get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn.get(IGetConfigDetailsForRelationshipTabModel.REFERENCED_TAGS);
    List<Map<String, Object>> contextsWithAutoCreateEnabled = (List<Map<String, Object>>) mapToReturn
        .get(IGetConfigDetailsForRelationshipTabModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE);
    Vertex klassFromTemplate = TemplateUtils.getKlassFromTemplate(templateNode);
    
    fillReferencedContextLinkedToKlass(klassFromTemplate, embeddedContexts, referencedTags, contextsWithAutoCreateEnabled);
    */
  }
  
  protected void fillReferencedContextLinkedToKlass(Vertex klassNode,
      Map<String, Object> embeddedContexts, Map<String, Object> referencedTags,
      List<Map<String, Object>> contextsWithAutoCreateEnabled) throws Exception
  {
    Iterator<Vertex> iterator = klassNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (iterator.hasNext()) {
      Vertex variantContextNode = iterator.next();
      String contextId = UtilClass.getCodeNew(variantContextNode);
      String variantType = (String) variantContextNode.getProperty(IVariantContext.TYPE);
      switch (variantType) {
        case CommonConstants.CONTEXTUAL_VARIANT:
        case CommonConstants.LANGUAGE_VARIANT:
        case CommonConstants.GTIN_VARIANT:
        case CommonConstants.IMAGE_VARIANT:
          Map<String, Object> variantContextMap = VariantContextUtils
              .getReferencedContexts(variantContextNode);
          String contextKlassId = UtilClass.getCodeNew(klassNode);
          variantContextMap.put(IReferencedVariantContextModel.CONTEXT_KLASS_ID, contextKlassId);
          List<String> entityIds = new ArrayList<>();
          entityIds.add(contextKlassId);
          variantContextMap.put(IReferencedVariantContextModel.ENTITY_IDS, entityIds);
          
          Map<String, Object> contextWithAutoCreateEnable = KlassGetUtils
              .getContextMapWithAutoCreateEnabled(variantContextNode);
          if (contextWithAutoCreateEnable != null) {
            contextsWithAutoCreateEnabled.add(contextWithAutoCreateEnable);
          }
          
          embeddedContexts.put(contextId, variantContextMap);
          
          for (Map<String, Object> contextTag : (List<Map<String, Object>>) variantContextMap
              .get(IReferencedVariantContextModel.TAGS)) {
            String entityId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
            Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
            Map<String, Object> entity = TagUtils.getTagMap(entityNode, false);
            List<String> tagValuesIds = (List<String>) contextTag
                .get(IReferencedVariantContextTagsModel.TAG_VALUE_IDS);
            filterChildrenTagsInKlass(entity, tagValuesIds,
                (Map<String, Object>) referencedTags.get(entityId));
            referencedTags.put(entityId, entity);
          }
          break;
      }
    }
  }
  
  /**
   * This funation create map of rootTaxonomyId vs list its childrens using
   * referenced Taxonomies. Then check permission for parent and
   * fillTaxonomyIdsHavingReadPermissions
   *
   * @author Lokesh
   * @param roleId
   * @param referencedPermissions
   * @param referencedTaxonomies
   * @throws Exception
   */
  protected void fillTaxonomyIdsHavingReadPermissions(Vertex roleNode,
      Map<String, Object> referencedPermissions, Map<String, Object> referencedTaxonomies)
      throws Exception
  {
    Set<String> taxonomiesHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.TAXONOMY_IDS_HAVING_RP);
    taxonomiesHavingReadPermission
        .addAll(GlobalPermissionUtils.getTaxonomyIdsHavingReadPermission(roleNode));
  }
  
  protected void fillReferencedElements(Map<String, Object> mapToReturn, Vertex klassVertex,
      String vertexLabel, String contextId, IGetConfigDetailsHelperModel helperModel,
      List<String> propertiesToFetch) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> elementsConflictingValues = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.ELEMENTS_CONFLICTING_VALUES);
    List<String> versionableAttributes = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.VERSIONABLE_ATTRIBUTES);
    List<String> versionableTags = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.VERSIONABLE_TAGS);
    Map<String, Object> typeIdIdentifierAttributeIds = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.TYPEID_IDENTIFIER_ATTRIBUTEIDS);
    List<String> mandatoryAttributeIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.MANDATORY_ATTRIBUTE_IDS);
    List<String> mandatoryTagIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.MANDATORY_TAG_IDS);
    List<String> shouldAttributeIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.SHOULD_ATTRIBUTE_IDS);
    List<String> shouldTagIds = (List<String>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.SHOULD_TAG_IDS);
    
    List<String> identifierAttributesForKlass = new ArrayList<>();
    String klassId = UtilClass.getCodeNew(klassVertex);
    String conflictingSourceType = EntityUtil.getEntityTypeByOrientClassType(vertexLabel);
    
    typeIdIdentifierAttributeIds.put(klassId, identifierAttributesForKlass);
    Map<String, Set<String>> typeIdVsAssociatedPropertyIds = helperModel
        .getTypeIdVsAssociatedPropertyIds();
    Set<String> propertyIds = new HashSet<>();
    typeIdVsAssociatedPropertyIds.put(klassId, propertyIds);
    Iterable<Vertex> kPNodesIterable = getKlassPropertyNodes(klassVertex, propertiesToFetch,
        helperModel);
    for (Vertex klassPropertyNode : kPNodesIterable) {
      String entityId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
      propertyIds.add(entityId);
      String klassPropertyContextId = null;
      Iterator<Vertex> attributeContextsIterator = klassPropertyNode
          .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
          .iterator();
      
      if (contextId != null && !attributeContextsIterator.hasNext()) {
        continue;
      }
      
      if (attributeContextsIterator.hasNext()) {
        Vertex attributeContextVertex = attributeContextsIterator.next();
        klassPropertyContextId = UtilClass.getCodeNew(attributeContextVertex);
        if (contextId != null && !contextId.equals(klassPropertyContextId)) {
          continue;
        }
        Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
            .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
        Map<String, Object> embeddedContexts = (Map<String, Object>) referencedVariantContexts
            .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
        helperModel.getAssociatedAttributeContextIds()
            .add(klassPropertyContextId);
        if (embeddedContexts.containsKey(klassPropertyContextId)) {
          Map<String, Object> propertyContextMap = (Map<String, Object>) embeddedContexts
              .get(klassPropertyContextId);
          Set<String> entityIds = (Set<String>) propertyContextMap
              .get(IReferencedVariantContextModel.ENTITY_IDS);
          entityIds.add(entityId);
        }
        else {
          Map<String, Object> propertyContextMap = VariantContextUtils
              .getReferencedContexts(attributeContextVertex);
          Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
              .get(IGetConfigDetailsModel.REFERENCED_TAGS);
          Set<String> entityIds = new HashSet<>();
          entityIds.add(entityId);
          propertyContextMap.put(IReferencedVariantContextModel.ENTITY_IDS, entityIds);
          embeddedContexts.put(klassPropertyContextId, propertyContextMap);
          
          for (Map<String, Object> contextTag : (List<Map<String, Object>>) propertyContextMap
              .get(IReferencedVariantContextModel.TAGS)) {
            String tagId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
            if (referencedTags.containsKey(entityId)) {
              continue;
            }
            Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
            Map<String, Object> entity = TagUtils.getTagMap(tagNode, false);
            referencedTags.put(tagId, entity);
          }
        }
      }
      
      String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
      
      Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
      referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
      referencedElementMap.put(CommonConstants.CODE_PROPERTY, entityId);
      if (klassPropertyContextId != null) {
        referencedElementMap.put(ISectionAttribute.ATTRIBUTE_VARIANT_CONTEXT,
            klassPropertyContextId);
      }
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAG)) {
        List<Map<String, Object>> defaultTagValues = KlassUtils
            .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
        referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
        List<String> selectedTagValues = KlassUtils
            .getSelectedTagValuesListOfKlassPropertyNode(klassPropertyNode);
        referencedElementMap.put(CommonConstants.SELECTED_TAG_VALUES_LIST, selectedTagValues);
        helperModel.getTagIds()
            .add(entityId);
        
        Vertex entityVertex = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> entity = UtilClass.getMapFromVertex(new ArrayList<>(), entityVertex);
        
        Boolean isVersionable = (Boolean) referencedElementMap.get(ISectionTag.IS_VERSIONABLE);
        if (isVersionable == null) {
          referencedElementMap.put(ISectionTag.IS_VERSIONABLE, entity.get(ITag.IS_VERSIONABLE));
        }
        
        if (!versionableTags.contains(entityId)
            && (Boolean) referencedElementMap.get(ITag.IS_VERSIONABLE)) {
          versionableTags.add(entityId);
        }
        fillMandatoryShouldPropertyIds(mandatoryTagIds, shouldTagIds, referencedElementMap);
      }
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_TAXONOMY)) {
        fillMinorTaxonomyInReferencedTaxonomy(mapToReturn, entityId, referencedElementMap);
        continue;
      }
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE)) {
        Vertex entityVertex = UtilClass.getVertexById(entityId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        Map<String, Object> entity = UtilClass.getMapFromVertex(new ArrayList<>(), entityVertex);
        String defaultValue = (String) referencedElementMap.get(ISectionAttribute.DEFAULT_VALUE);
        if (defaultValue == null || defaultValue.equals("")) {
          referencedElementMap.put(ISectionAttribute.DEFAULT_VALUE,
              entity.get(IAttribute.DEFAULT_VALUE));
        }
        Boolean isVersionable = (Boolean) referencedElementMap
            .get(ISectionAttribute.IS_VERSIONABLE);
        if (isVersionable == null) {
          referencedElementMap.put(ISectionAttribute.IS_VERSIONABLE,
              entity.get(IAttribute.IS_VERSIONABLE));
        }
        
        String valueAsHtml = (String) referencedElementMap.get(ISectionAttribute.VALUE_AS_HTML);
        if (valueAsHtml == null || valueAsHtml.equals("")) {
          referencedElementMap.put(ISectionAttribute.VALUE_AS_HTML,
              entity.get(IAttribute.VALUE_AS_HTML));
        }
        
        helperModel.getAttributeIds()
            .add(entityId);
        Boolean isIdentifierAttribute = (Boolean) referencedElementMap
            .get(ISectionAttribute.IS_IDENTIFIER);
        if (isIdentifierAttribute != null && isIdentifierAttribute) {
          identifierAttributesForKlass.add(entityId);
        }
        
        if (!versionableAttributes.contains(entityId)
            && (Boolean) referencedElementMap.get(IAttribute.IS_VERSIONABLE)) {
          versionableAttributes.add(entityId);
        }
        fillAutoGeneratedAttributeInfo(klassVertex, klassPropertyNode, mapToReturn);
        fillMandatoryShouldPropertyIds(mandatoryAttributeIds, shouldAttributeIds,
            referencedElementMap);
      }
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_ROLE)) {
        helperModel.getRoleIds()
            .add(entityId);
      }
      
      if (entityType.equals(SystemLevelIds.PROPERTY_TYPE_RELATIONSHIP)) {
        Boolean isNature = klassPropertyNode.getProperty(ISectionRelationship.IS_NATURE);
        if (isNature) {
          continue;
        }
        referencedElementMap.put(CommonConstants.ID_PROPERTY,
            UtilClass.getCodeNew(klassPropertyNode));
        referencedElementMap.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
        fillRelationships(klassVertex, mapToReturn, klassPropertyNode, referencedElementMap,
            helperModel);
        continue;
      }
      
      // This function fill conflicting source in referencedElementMap. It must
      // be outside if
      // because during merge it get both conflicting values
      fillConflictionSourcesInReferencedElementMap(klassId, conflictingSourceType,
          referencedElementMap);
      
      if (referencedElements.containsKey(entityId)) {
        Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements
            .get(entityId);
        mergeReferencedElement(referencedElementMap, existingReferencedElement, klassId, entityType,
            true);
        
        String existingCouplingType = (String) existingReferencedElement
            .get(IReferencedSectionElementModel.COUPLING_TYPE);
        updateElementConflictingValues(elementsConflictingValues, klassId, conflictingSourceType,
            referencedElementMap, existingCouplingType);
        
      }
      else {
        referencedElements.put(entityId, referencedElementMap);
        createOrUpdateElementConflictingValues(klassId, conflictingSourceType, referencedElementMap,
            elementsConflictingValues, false);
      }
    }
  }
  
  protected Iterable<Vertex> getKlassPropertyNodes(Vertex klassVertex,
      List<String> propertiesToFetch, IGetConfigDetailsHelperModel helperModel)
  {
    Iterable<Vertex> kPNodesIterable;
    if (propertiesToFetch == null || propertiesToFetch.isEmpty()) {
      kPNodesIterable = klassVertex.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_KLASS_PROPERTY);
    }
    else {
      String query = "SELECT FROM (SELECT EXPAND (OUT('"
          + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "')) FROM " + klassVertex.getId()
          + " ) WHERE " + ISectionElement.PROPERTY_ID + " IN "
          + EntityUtil.quoteIt(propertiesToFetch);
      kPNodesIterable = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
    }
    return kPNodesIterable;
  }
  
  protected void fillAutoGeneratedAttributeInfo(Vertex natureKlass, Vertex klassPropertyNode,
      Map<String, Object> mapToReturn) throws Exception
  {
  }
  
  protected void fillConflictionSourcesInReferencedElementMap(String klassId,
      String conflictingSourceType, Map<String, Object> referencedElementMap)
  {
    List<Map<String, Object>> confictingSources = new ArrayList<>();
    referencedElementMap.put(IReferencedSectionElementModel.CONFLICTING_SOURCES, confictingSources);
    
    String couplingType = (String) referencedElementMap
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    if (couplingType == null || couplingType.isEmpty()) {
      return;
    }
    Map<String, Object> confictingSourceMap = new HashMap<String, Object>();
    confictingSourceMap.put(IIdAndType.ID, klassId);
    confictingSourceMap.put(IElementConflictingValuesModel.ID, klassId);
    confictingSourceMap.put(IElementConflictingValuesModel.SOURCE_TYPE, conflictingSourceType);
    confictingSourceMap.put(IAttributeConflictingValuesModel.VALUE,
        referencedElementMap.get(IReferencedSectionAttributeModel.DEFAULT_VALUE));
    String referenceType = (String) referencedElementMap.get(IReferencedSectionElementModel.TYPE);
    confictingSourceMap.put(IElementConflictingValuesModel.TYPE, referenceType);
    confictingSourceMap.put(IAttributeConflictingValuesModel.COUPLING_TYPE,
        referencedElementMap.get(IReferencedSectionElementModel.COUPLING_TYPE));
    if (referenceType.equals(CommonConstants.ATTRIBUTE)) {
      confictingSourceMap.put(IAttributeConflictingValuesModel.VALUE_AS_HTML,
          referencedElementMap.get(IReferencedSectionAttributeModel.VALUE_AS_HTML));
    }
    
    confictingSources.add(confictingSourceMap);
  }
  
  protected void updateElementConflictingValues(Map<String, Object> elementsConflictingValues,
      String klassId, String conflictingSourceType, Map<String, Object> referencedElementMap,
      String existingCouplingType)
  {
    String newCouplingType = (String) referencedElementMap
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    if (existingCouplingType == null || newCouplingType == null) {
      return;
    }
    
    if (newCouplingType.equals(CommonConstants.DYNAMIC_COUPLED)
        && existingCouplingType.equals(CommonConstants.TIGHTLY_COUPLED)) {
      createOrUpdateElementConflictingValues(klassId, conflictingSourceType, referencedElementMap,
          elementsConflictingValues, true);
    }
    else {
      createOrUpdateElementConflictingValues(klassId, conflictingSourceType, referencedElementMap,
          elementsConflictingValues, false);
    }
  }
  
  protected void createOrUpdateElementConflictingValues(String klassId,
      String conflictingSourceType, Map<String, Object> referencedElementMap,
      Map<String, Object> elementsConflictingValues, Boolean isClear)
  {
    String couplingType = (String) referencedElementMap
        .get(IReferencedSectionElementModel.COUPLING_TYPE);
    if (elementsConflictingValues == null || couplingType == null || couplingType.isEmpty()) {
      return;
    }
    String entityId = (String) referencedElementMap.get(IReferencedSectionElementModel.ID);
    List<Map<String, Object>> elementConflictingValueList = (List<Map<String, Object>>) elementsConflictingValues
        .get(entityId);
    if (elementConflictingValueList == null || isClear) {
      elementConflictingValueList = new ArrayList<>();
      elementsConflictingValues.put(entityId, elementConflictingValueList);
    }
    Map<String, Object> conflictingValue = new HashMap<String, Object>();
    conflictingValue.put(IElementConflictingValuesModel.ID, klassId);
    conflictingValue.put(IElementConflictingValuesModel.SOURCE_TYPE, conflictingSourceType);
    conflictingValue.put(IElementConflictingValuesModel.TYPE,
        referencedElementMap.get(IReferencedSectionElementModel.TYPE));
    conflictingValue.put(IAttributeConflictingValuesModel.VALUE,
        referencedElementMap.get(IReferencedSectionAttributeModel.DEFAULT_VALUE));
    elementConflictingValueList.add(conflictingValue);
  }
  
  protected void fillMinorTaxonomyInReferencedTaxonomy(Map<String, Object> mapToReturn,
      String entityId, Map<String, Object> referencedElementMap) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    Vertex entityNode = UtilClass.getVertexByIndexedId(entityId,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
            IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.CODE,
            IReferencedArticleTaxonomyModel.TAXONOMY_TYPE),
        entityNode);
    Iterable<Vertex> sectionVertices = entityNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    List<String> propertyCollection = new ArrayList<>();
    for (Vertex sectionVertex : sectionVertices) {
      Vertex propertyCollectionVertex = KlassUtils
          .getPropertyCollectionNodeFromKlassSectionNode(sectionVertex);
      propertyCollection.add(UtilClass.getCodeNew(propertyCollectionVertex));
    }
    taxonomyMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS, propertyCollection);
    fillTaxonomiesChildrenAndParentData(taxonomyMap, entityNode);
    referencedTaxonomyMap.put(entityId, taxonomyMap);
    referencedElements.put(entityId, referencedElementMap);
  }
  
  protected void fillRelationships(Vertex klassVertex, Map<String, Object> mapToReturn,
      Vertex klassRelationshipNode, Map<String, Object> referencedElementMap,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedRelationshipMap = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS);
    Map<String, String> referencedRelationshipsMapping = (Map<String, String>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_RELATIONSHIPS_MAPPING);
    Map<String, Object> referencedRelationshipProperties = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Set<String> entitiesHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.ENTITIES_HAVING_RP);
    
    Iterator<Vertex> relationshipIterator = klassRelationshipNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    Vertex relationshipNode = relationshipIterator.next();
    String typeId = UtilClass.getCodeNew(klassVertex);
    
    String relationshipId = UtilClass.getCodeNew(relationshipNode);
    helperModel.getRelationshipIds()
        .add(relationshipId);
    
    Map<String, Object> side = (Map<String, Object>) referencedElementMap
        .get(IReferencedSectionRelationshipModel.RELATIONSHIP_SIDE);
    String targetKlassType = (String) side.get(IKlassRelationshipSide.TARGET_TYPE);
    String moduleEntity = EntityUtil.getModuleEntityFromKlassType(targetKlassType);
    if (!entitiesHavingReadPermission.contains(moduleEntity) && moduleEntity != null) {
      return;
    }
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipsVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    if (typeIdVsAssociatedRelationshipsVertices.containsKey(typeId)) {
      typeIdVsAssociatedRelationshipsVertices.get(typeId)
          .add(relationshipNode);
    }
    else {
      Set<Vertex> associatedRelationshipVertices = new HashSet<>();
      associatedRelationshipVertices.add(relationshipNode);
      typeIdVsAssociatedRelationshipsVertices.put(typeId, associatedRelationshipVertices);
    }
    
    String klassRelationshipId = UtilClass.getCodeNew(klassRelationshipNode);
    helperModel.getKlassRelationshipIds()
        .add(klassRelationshipId);
    referencedElements.put(klassRelationshipId, referencedElementMap);
    
    Map<String, Object> relationshipMap = RelationshipUtils
        .getRelationshipMapWithContext(relationshipNode);
    relationshipMap.remove(IReferencedNatureRelationshipModel.RELATIONSHIP_TYPE);
    relationshipMap.remove(IReferencedNatureRelationshipModel.AUTO_CREATE_SETTINGS);
    relationshipMap.remove(IReferencedNatureRelationshipModel.MAX_NO_OF_ITEMS);
    relationshipMap.remove(IReferencedNatureRelationshipModel.TAXONOMY_INHERITANCE_SETTING);
    if (side != null) {
      side.put(CommonConstants.RELATIONSHIP_MAPPING_ID_PROPERTY, klassRelationshipId);
      String contextId = RelationshipUtils.getContextId(klassRelationshipNode);
      side.put(IRelationshipSide.CONTEXT_ID, contextId);
    }
    fillReferenceRelationshipVariantContext(relationshipMap, mapToReturn,
        helperModel.getContextTagIds());
    referencedRelationshipMap.put(relationshipId, relationshipMap);
    referencedRelationshipsMapping.put(relationshipId, typeId);
    String label = (String) UtilClass.getValueByLanguage(relationshipNode,
        CommonConstants.LABEL_PROPERTY);
    Map<String, Object> relationshipPropertiesMap = new HashMap<>();
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.LABEL, label);
    relationshipPropertiesMap.put(IReferencedRelationshipPropertiesModel.CODE,
        relationshipNode.getProperty(CommonConstants.CODE_PROPERTY));
    RelationshipUtils.populatePropetiesInfoNew(relationshipNode, relationshipPropertiesMap);
    referencedRelationshipProperties.put(relationshipId, relationshipPropertiesMap);
  }
  
  protected void fillReferenceRelationshipVariantContext(Map<String, Object> relationshipMap,
      Map<String, Object> mapToReturn, Set<String> contextTagIds) throws Exception
  {
    Set<String> contextIds = new HashSet<String>();
    
    Map<String, Object> side1Map = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE1);
    Map<String, Object> side2Map = (Map<String, Object>) relationshipMap.get(IRelationship.SIDE2);
    String side1ContextId = (String) side1Map.get(IRelationshipSide.CONTEXT_ID);
    String side2ContextId = (String) side2Map.get(IRelationshipSide.CONTEXT_ID);
    if (side1ContextId != null) {
      contextIds.add(side1ContextId);
    }
    if (side2ContextId != null) {
      contextIds.add(side2ContextId);
    }
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> relationshipVariantContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    for (String contextId : contextIds) {
      Vertex contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
      Map<String, Object> contextMap = VariantContextUtils.getReferencedContexts(contextNode);
      relationshipVariantContexts.put(contextId, contextMap);
      List<Map<String, Object>> contextTags = (List<Map<String, Object>>) contextMap
          .get(IReferencedVariantContextModel.TAGS);
      
      for (Map<String, Object> tag : contextTags) {
        String tagId = (String) tag.get(IReferencedVariantContextTagsModel.TAG_ID);
        if (referencedTagsMap.containsKey(tagId)) {
          continue;
        }
        Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
        Map<String, Object> entity = TagUtils.getTagMap(tagNode, false);
        referencedTagsMap.put(tagId, entity);
        contextTagIds.add(tagId);
      }
    }
  }
  
  protected void fillReferencedPropertyCollections(IGetConfigDetailsHelperModel helperModel,
      Vertex klassVertex, Map<String, Object> mapToReturn, List<String> propertiesToFetch)
      throws Exception
  {
    String typeId = UtilClass.getCodeNew(klassVertex);
    Set<Vertex> associatedPropertyCollectionVertices = new HashSet<>();
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    if (typeIdVsAssociatedPropertyCollectionVertices.containsKey(typeId)) {
      associatedPropertyCollectionVertices = typeIdVsAssociatedPropertyCollectionVertices
          .get(typeId);
    }
    else {
      typeIdVsAssociatedPropertyCollectionVertices.put(typeId,
          associatedPropertyCollectionVertices);
    }
    
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    Iterable<Vertex> sectionVertices = klassVertex.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_SECTION_OF);
    for (Vertex sectionVertex : sectionVertices) {
      Vertex propertyCollectionVertex = KlassUtils
          .getPropertyCollectionNodeFromKlassSectionNode(sectionVertex);
      associatedPropertyCollectionVertices.add(propertyCollectionVertex);
      String propertyCollectionId = UtilClass.getCodeNew(propertyCollectionVertex);
      if (referencedPropertyCollections.containsKey(propertyCollectionId)) {
        continue;
      }
      
      Map<String, Object> referencedPropertyCollection = UtilClass
          .getMapFromVertex(new ArrayList<>(), propertyCollectionVertex);
      if (propertiesToFetch != null && !propertiesToFetch.isEmpty()) {
        List<String> tempAttributeIds = new ArrayList<>(propertiesToFetch);
        List<String> tempTagIds = new ArrayList<>(propertiesToFetch);
        tempAttributeIds.retainAll(
            (List<String>) referencedPropertyCollection.get(IPropertyCollection.ATTRIBUTE_IDS));
        tempTagIds.retainAll(
            (List<String>) referencedPropertyCollection.get(IPropertyCollection.TAG_IDS));
        if (tempAttributeIds.isEmpty() && tempTagIds.isEmpty()) {
          continue;
        }
      }
      
      referencedPropertyCollections.put(propertyCollectionId, referencedPropertyCollection);
      List<Map<String, Object>> elementsList = new ArrayList<Map<String, Object>>();
      Iterable<Edge> entityToRelationships = propertyCollectionVertex.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
      Set<String> entityIds = new HashSet<>();
      for (Edge entityTo : entityToRelationships) {
        Vertex entityVertex = entityTo.getVertex(Direction.OUT);
        String entityId = UtilClass.getCodeNew(entityVertex);
        Map<String, Object> propertyCollectionElement = new HashMap<String, Object>();
        propertyCollectionElement.put(IReferencedPropertyCollectionElementModel.ID, entityId);
        elementsList.add(propertyCollectionElement);
        entityIds.add(entityId);
      }
      
      List<String> propertySequenceIds = propertyCollectionVertex.getProperty(CommonConstants.PROPERTY_SEQUENCE_IDS);
      PropertyCollectionUtils.sortPropertyCollectionList(elementsList, propertySequenceIds,CommonConstants.ID_PROPERTY);
      
      referencedPropertyCollection.put(IReferencedPropertyCollectionModel.ELEMENTS, elementsList);
    }
  }
  
  protected void fillNatureKlassPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Vertex natureKlassVertex = helperModel.getNatureNode();
    fillHeaderPermission(referencedPermissions, roleId, natureKlassVertex);
    fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
        referencedPermissions, natureKlassVertex, roleId);
  }
  
  protected void fillNonNatureKlassPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Set<Vertex> nonNatureKlassVertices = helperModel.getNonNatureKlassNodes();
    for (Vertex nonNatureKlassVertex : nonNatureKlassVertices) {
      fillHeaderPermission(referencedPermissions, roleId, nonNatureKlassVertex);
      fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
          referencedPermissions, nonNatureKlassVertex, roleId);
    }
  }
  
  protected void fillTaxonomyPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Set<Vertex> taxonomyVertices = helperModel.getTaxonomyVertices();
    for (Vertex taxonomyVertex : taxonomyVertices) {
      fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
          referencedPermissions, taxonomyVertex, roleId);
    }
  }
  
  private void fillHeaderPermission(Map<String, Object> referencedPermissions, String roleId,
      Vertex typeVertex) throws Exception
  {
    Map<String, Object> headerPermissionToMerge = (Map<String, Object>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.HEADER_PERMISSION);
    if (headerPermissionToMerge == null) {
      headerPermissionToMerge = GlobalPermissionUtils.fillDefaultHeaderPermission();
    }
    Map<String, Object> headerPermission = GlobalPermissionUtils.getHeaderPermission(typeVertex,
        roleId);
    mergeHeaderPermissions(headerPermissionToMerge, headerPermission);
    referencedPermissions.put(IReferencedTemplatePermissionModel.HEADER_PERMISSION,
        headerPermissionToMerge);
  }
  
  private void mergeHeaderPermissions(Map<String, Object> headerPermissionToMerge,
      Map<String, Object> headerPermission)
  {
    Boolean viewName = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_NAME)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_NAME);
    Boolean canEditName = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_EDIT_NAME)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_EDIT_NAME);
    Boolean viewIcon = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_ICON)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_ICON);
    Boolean canChangeIcon = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_CHANGE_ICON)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_CHANGE_ICON);
    Boolean viewPrimaryType = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.VIEW_PRIMARY_TYPE)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_PRIMARY_TYPE);
    Boolean canEditPrimaryType = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE);
    Boolean viewAdditionalClasses = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.VIEW_ADDITIONAL_CLASSES)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_ADDITIONAL_CLASSES);
    Boolean canAddClasses = (Boolean) headerPermissionToMerge.get(IHeaderPermission.CAN_ADD_CLASSES)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_ADD_CLASSES);
    Boolean canDeleteClasses = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.CAN_DELETE_CLASSES)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_DELETE_CLASSES);
    Boolean viewTaxonomies = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.VIEW_TAXONOMIES)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_TAXONOMIES);
    Boolean canAddTaxonomy = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.CAN_ADD_TAXONOMY)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_ADD_TAXONOMY);
    Boolean canDeleteTaxonomy = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.CAN_DELETE_TAXONOMY)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_DELETE_TAXONOMY);
    Boolean viewStatusTag = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.VIEW_STATUS_TAGS)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_STATUS_TAGS);
    Boolean canEditStatusTag = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.CAN_EDIT_STATUS_TAG)
        || (Boolean) headerPermission.get(IHeaderPermission.CAN_EDIT_STATUS_TAG);
    Boolean viewCreatedOn = (Boolean) headerPermissionToMerge.get(IHeaderPermission.VIEW_CREATED_ON)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_CREATED_ON);
    Boolean viewLastModifiedBy = (Boolean) headerPermissionToMerge
        .get(IHeaderPermission.VIEW_LAST_MODIFIED_BY)
        || (Boolean) headerPermission.get(IHeaderPermission.VIEW_LAST_MODIFIED_BY);
    
    headerPermissionToMerge.put(IHeaderPermission.VIEW_NAME, viewName);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_NAME, canEditName);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_ICON, viewIcon);
    headerPermissionToMerge.put(IHeaderPermission.CAN_CHANGE_ICON, canChangeIcon);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_PRIMARY_TYPE, viewPrimaryType);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_PRIMARY_TYPE, canEditPrimaryType);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_ADDITIONAL_CLASSES, viewAdditionalClasses);
    headerPermissionToMerge.put(IHeaderPermission.CAN_ADD_CLASSES, canAddClasses);
    headerPermissionToMerge.put(IHeaderPermission.CAN_DELETE_CLASSES, canDeleteClasses);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_TAXONOMIES, viewTaxonomies);
    headerPermissionToMerge.put(IHeaderPermission.CAN_ADD_TAXONOMY, canAddTaxonomy);
    headerPermissionToMerge.put(IHeaderPermission.CAN_DELETE_TAXONOMY, canDeleteTaxonomy);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_STATUS_TAGS, viewStatusTag);
    headerPermissionToMerge.put(IHeaderPermission.CAN_EDIT_STATUS_TAG, canEditStatusTag);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_CREATED_ON, viewCreatedOn);
    headerPermissionToMerge.put(IHeaderPermission.VIEW_LAST_MODIFIED_BY, viewLastModifiedBy);
  }
  
  protected void fillPropertyCollectionPermissionAndEntitiesPermission(
      Map<String, Object> responseMap, IGetConfigDetailsHelperModel helperModel,
      Map<String, Object> referencedPermissions, Vertex typeVertex, String roleId) throws Exception
  {
    String typeId = UtilClass.getCodeNew(typeVertex);
    Map<String, Set<Vertex>> typeIdVsAssociatedPropertyCollectionVertices = helperModel
        .getTypeIdVsAssociatedPropertyCollectionVertices();
    Set<Vertex> referencedPropertyCollection = typeIdVsAssociatedPropertyCollectionVertices
        .get(typeId);
    if (referencedPropertyCollection != null) {
      fillPropertyCollectionPermission(roleId, typeId, referencedPropertyCollection,
          referencedPermissions);
    }
    
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    referencedTaxonomies = (referencedTaxonomies != null) ? referencedTaxonomies : new HashMap<>();
    Set<String> taxonomyIds = new HashSet<>(referencedTaxonomies.keySet());
    Set<String> propertyIds = helperModel.getTypeIdVsAssociatedPropertyIds()
        .get(typeId);
    if (propertyIds != null) {
      Set<String> tempPropertyIds = new HashSet<>(propertyIds);
      taxonomyIds.retainAll(propertyIds);
      tempPropertyIds.removeAll(taxonomyIds);
      fillPropertyPermission(roleId, typeId, taxonomyIds, referencedPermissions);
      fillPropertyPermission(roleId, typeId, propertyIds, referencedPermissions);
    }
    Map<String, Set<Vertex>> typeIdVsAssociatedRelationshipsVertices = helperModel
        .getTypeIdVsAssociatedRelationshipsVertices();
    
    Set<Vertex> relationshipVerticesAssociatedWithType = typeIdVsAssociatedRelationshipsVertices
        .get(typeId);
    if (relationshipVerticesAssociatedWithType != null
        && !relationshipVerticesAssociatedWithType.isEmpty()) {
      Set<String> relevantRelationshipIds = new HashSet<>(
          UtilClass.getCodes(relationshipVerticesAssociatedWithType));
      fillRelationshipPermission(roleId, typeId, relevantRelationshipIds, referencedPermissions);
    }
    
  }
  
  protected void fillPropertyCollectionPermission(String roleId, String typeId,
      Set<Vertex> propertyCollectionVertices, Map<String, Object> referencedPermissions)
      throws Exception
  {
    Set<String> editablePCIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS);
    Set<String> visiblePCIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS);
    Set<String> visiblePropertyCollectionIdsForCurrentType = new HashSet<>();
    Set<String> editablePropertyCollectionIdsForCurrentType = new HashSet<>();
    for (Vertex propertyCollectionVertex : propertyCollectionVertices) {
      visiblePropertyCollectionIdsForCurrentType
          .add(UtilClass.getCodeNew(propertyCollectionVertex));
      editablePropertyCollectionIdsForCurrentType
          .add(UtilClass.getCodeNew(propertyCollectionVertex));
    }
    
    Set<String> nonVisiblePropertyCollectionIdsForCurrentType = new HashSet<>();
    Set<Vertex> canReadPermissionVertices = GlobalPermissionUtils
        .getPropertyCollectionPermissionVertexIfExist(roleId, typeId,
            VertexLabelConstants.PROPERTY_COLLECTION_CAN_READ_PERMISSION);
    for (Vertex canReadPermissionVertex : canReadPermissionVertices) {
      String nonVisiblePropertyCollectionId = canReadPermissionVertex
          .getProperty(IPropertyCollectionPermission.PROPERTY_COLLECTION_ID);
      nonVisiblePropertyCollectionIdsForCurrentType.add(nonVisiblePropertyCollectionId);
    }
    visiblePropertyCollectionIdsForCurrentType
        .removeAll(nonVisiblePropertyCollectionIdsForCurrentType);
    visiblePCIds.addAll(visiblePropertyCollectionIdsForCurrentType);
    
    Set<String> nonEditablePropertyCollectionIdsForCurrentType = new HashSet<>();
    Set<Vertex> canEditPermissionVertices = GlobalPermissionUtils
        .getPropertyCollectionPermissionVertexIfExist(roleId, typeId,
            VertexLabelConstants.PROPERTY_COLLECTION_CAN_EDIT_PERMISSION);
    for (Vertex canEditPermissionVertex : canEditPermissionVertices) {
      String nonEditablePropertyCollectionId = canEditPermissionVertex
          .getProperty(IPropertyCollectionPermission.PROPERTY_COLLECTION_ID);
      nonEditablePropertyCollectionIdsForCurrentType.add(nonEditablePropertyCollectionId);
    }
    editablePropertyCollectionIdsForCurrentType
        .removeAll(nonEditablePropertyCollectionIdsForCurrentType);
    editablePCIds.addAll(editablePropertyCollectionIdsForCurrentType);
  }
  
  public static void fillRelationshipPermission(String roleId, String typeId,
      Set<String> relationshipIds, Map<String, Object> referencedPermissions) throws Exception
  {
    Set<String> canAddRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.CAN_ADD_RELATIONSHIP_IDS);
    Set<String> canDeleteRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.CAN_DELETE_RELATIONSHIP_IDS);
    Set<String> canEditContextRelationshipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.CAN_EDIT_CONTEXT_RELATIONSHIP_IDS);
    Set<String> visibleRelationhipIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_RELATIONSHIP_IDS);
    Set<String> canAddRelationshipIdsForCurrentType = new HashSet<>(relationshipIds);
    Set<String> canDeleteRelationshipIdsForCurrentType = new HashSet<>(relationshipIds);
    Set<String> visibleRelationshipIdsForCurrentType = new HashSet<>(relationshipIds);
    
    Set<String> canNotAddRelationshipIdsForCurrentType = new HashSet<>();
    Set<Vertex> canAddPermissions = GlobalPermissionUtils.getRelationshipPermissionVertexIfExist(
        roleId, typeId, VertexLabelConstants.RELATIONSHIP_CAN_ADD_PERMISSION);
    for (Vertex canAddPermission : canAddPermissions) {
      String relationshipId = canAddPermission.getProperty(IRelationshipPermission.RELATIONSHIP_ID);
      canNotAddRelationshipIdsForCurrentType.add(relationshipId);
    }
    canAddRelationshipIdsForCurrentType.removeAll(canNotAddRelationshipIdsForCurrentType);
    canAddRelationhipIds.addAll(canAddRelationshipIdsForCurrentType);
    
    Set<String> canNotDeleteRelationshipIdsForCurrentType = new HashSet<>();
    Set<Vertex> canDeletePermissions = GlobalPermissionUtils.getRelationshipPermissionVertexIfExist(
        roleId, typeId, VertexLabelConstants.RELATIONSHIP_CAN_DELETE_PERMISSION);
    for (Vertex canDeletePermission : canDeletePermissions) {
      String relationshipId = canDeletePermission
          .getProperty(IRelationshipPermission.RELATIONSHIP_ID);
      canNotDeleteRelationshipIdsForCurrentType.add(relationshipId);
    }
    canDeleteRelationshipIdsForCurrentType.removeAll(canNotDeleteRelationshipIdsForCurrentType);
    canDeleteRelationhipIds.addAll(canDeleteRelationshipIdsForCurrentType);
    

    canEditContextRelationshipIds.addAll(relationshipIds);
    Set<Vertex> canEditContextPermissions = GlobalPermissionUtils.getRelationshipPermissionVertexIfExist(
        roleId, typeId, VertexLabelConstants.RELATIONSHIP_CONTEXT_CAN_EDIT_PERMISSION);
    
    canEditContextPermissions.forEach(node ->
        canEditContextRelationshipIds.remove(node.getProperty(IRelationshipPermission.RELATIONSHIP_ID)));
    
    
    Set<String> canNotReadRelationshipIdsForCurrentType = new HashSet<>();
    Set<Vertex> canReadPermissions = GlobalPermissionUtils.getRelationshipPermissionVertexIfExist(
        roleId, typeId, VertexLabelConstants.RELATIONSHIP_CAN_READ_PERMISSION);
    for (Vertex canReadPermission : canReadPermissions) {
      String relationshipId = canReadPermission
          .getProperty(IRelationshipPermission.RELATIONSHIP_ID);
      canNotReadRelationshipIdsForCurrentType.add(relationshipId);
    }
    visibleRelationshipIdsForCurrentType.removeAll(canNotReadRelationshipIdsForCurrentType);
    visibleRelationhipIds.addAll(visibleRelationshipIdsForCurrentType);
  }

  protected void fillPropertyPermission(String roleId, String typeId, Set<String> entityIds,
      Map<String, Object> referencedPermissions) throws Exception
  {
    Set<String> editableEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    Set<String> visibleEntityIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
    Set<String> visiblePropertyIdsForCurrentType = new HashSet<>(entityIds);
    Set<String> editablePropertyIdsForCurrentType = new HashSet<>(entityIds);
    
    Set<String> nonVisiblePropertyIdsForCurrentType = new HashSet<>();
    Set<Vertex> canReadPermissionVertices = GlobalPermissionUtils
        .getPropertyPermissionVertexIfExist(roleId, typeId,
            VertexLabelConstants.PROPERTY_CAN_READ_PERMISSION);
    for (Vertex canReadPermissionVertex : canReadPermissionVertices) {
      String nonVisiblePropertyId = canReadPermissionVertex
          .getProperty(IPropertyPermission.PROPERTY_ID);
      nonVisiblePropertyIdsForCurrentType.add(nonVisiblePropertyId);
    }
    visiblePropertyIdsForCurrentType.removeAll(nonVisiblePropertyIdsForCurrentType);
    visibleEntityIds.addAll(visiblePropertyIdsForCurrentType);
    
    Set<String> nonEditablePropertyIdsForCurrentType = new HashSet<>();
    Set<Vertex> canEditPermissionVertices = GlobalPermissionUtils
        .getPropertyCollectionPermissionVertexIfExist(roleId, typeId,
            VertexLabelConstants.PROPERTY_CAN_EDIT_PERMISSION);
    for (Vertex canEditPermissionVertex : canEditPermissionVertices) {
      String nonEditablePropertyId = canEditPermissionVertex
          .getProperty(IPropertyPermission.PROPERTY_ID);
      nonEditablePropertyIdsForCurrentType.add(nonEditablePropertyId);
    }
    editablePropertyIdsForCurrentType.removeAll(nonEditablePropertyIdsForCurrentType);
    editableEntityIds.addAll(editablePropertyIdsForCurrentType);
  }
  
  protected void fillReferencedElementInRespectiveMap(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    Map<String, Object> referencedRoles = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES);
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Map<String, Object> referencedPCs = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    
    Set<String> visiblePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
    Set<String> editablePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    
    Set<String> allPropertyIds = new HashSet<>(visiblePropertyIds);
    allPropertyIds.addAll(editablePropertyIds);
    Set<String> attributeIds = helperModel.getAttributeIds();
    Set<String> roleIds = helperModel.getRoleIds();
    Set<String> tagIds = helperModel.getTagIds();
    
    Set<String> referencedElementIds = referencedElements.keySet();
    
    List<String> propertyToRetain = fetchPropertyIds(referencedPCs, attributeIds, tagIds,
        referencedElementIds);
    
    referencedElementIds.retainAll(propertyToRetain);
    allPropertyIds.retainAll(referencedElementIds);
    for (String entityId : allPropertyIds) {
      Map<String, Object> entity = new HashMap<>();
      if (attributeIds.contains(entityId)) {
        Vertex entityNode = UtilClass.getVertexByIndexedId(entityId,
            VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
        entity = AttributeUtils.getAttributeMap(entityNode);
        if (entity.get(IAttribute.TYPE)
            .equals(Constants.CALCULATED_ATTRIBUTE_TYPE)
            || entity.get(IAttribute.TYPE)
                .equals(Constants.CONCATENATED_ATTRIBUTE_TYPE)) {
          AttributeUtils.fillDependentAttributesForCalculatedOrConcatenatedAttributes(
              referencedAttributes, referencedTags, entity);
        }
        if (!referencedAttributes.containsKey(entityId)) {
          referencedAttributes.put(entityId, entity);
        }
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements
            .get(entityId);
        String defaultValue = (String) referencedElementMap.get(ISectionAttribute.DEFAULT_VALUE);
        if (defaultValue == null || defaultValue.equals("")) {
          referencedElementMap.put(ISectionAttribute.DEFAULT_VALUE,
              entity.get(IAttribute.DEFAULT_VALUE));
        }
      }
      
      if (tagIds.contains(entityId)) {
        Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElements
            .get(entityId);
        List<String> selectedTagValuesList = (List<String>) referencedElementMap
            .remove(CommonConstants.SELECTED_TAG_VALUES_LIST);
        
        entity = (Map<String, Object>) referencedTags.get(entityId);
        
        if (helperModel.getContextTagIds()
            .contains(entityId)) {
          // entity must not be null as it must be get filled on time of filling
          // referencedContext
          String tagType = (String) referencedElementMap.get(CommonConstants.TAG_TYPE_PROPERTY);
          if (tagType != null && !tagType.equals("")) {
            entity.put(ITag.TAG_TYPE, tagType);
          }
          else {
            referencedElementMap.put(ISectionTag.TAG_TYPE, entity.get(ITag.TAG_TYPE));
          }
          
          Boolean isMultiselect = (Boolean) referencedElementMap.get(ISectionTag.IS_MULTI_SELECT);
          if (isMultiselect != null) {
            entity.put(ITag.IS_MULTI_SELECT, isMultiselect);
          }
          else {
            referencedElementMap.put(ISectionTag.IS_MULTI_SELECT, entity.get(ITag.IS_MULTI_SELECT));
          }
          continue;
        }
        
        // Only filter tag values for types mentioned in list below
        List<String> tagTypes = Arrays.asList(SystemLevelIds.YES_NEUTRAL_NO_TAG_TYPE_ID,
            SystemLevelIds.YES_NEUTRAL_TAG_TYPE_ID, SystemLevelIds.RANGE_TAG_TYPE_ID);
        Vertex entityNode = UtilClass.getVertexByIndexedId(entityId,
            VertexLabelConstants.ENTITY_TAG);
        String couplingType = (String) referencedElementMap
            .get(IReferencedSectionElementModel.COUPLING_TYPE);
        if (helperModel.getShouldUseTagIdTagValueIdsMap()
            && tagTypes.contains(entityNode.getProperty(ITag.TAG_TYPE))
            && !couplingType.equals(CommonConstants.READ_ONLY_COUPLED)) {
          Map<String, List<String>> tagIdTagValueIdsMap = helperModel.getTagIdTagValueIdsMap();
          List<String> tagValueIds = tagIdTagValueIdsMap.get(entityId);
          entity = TagUtils.getTagMapWithSelectTagValues(entityNode, tagValueIds);
        }
        else {
          entity = TagUtils.getTagMap(entityNode, true);
        }
        
        filterChildrenTagsInKlass(entity, selectedTagValuesList,
            (Map<String, Object>) referencedTags.get(entityId));
        
        referencedTags.put(entityId, entity);
        String tagType = (String) referencedElementMap.get(CommonConstants.TAG_TYPE_PROPERTY);
        if (tagType != null && !tagType.equals("")) {
          entity.put(ITag.TAG_TYPE, tagType);
        }
        else {
          referencedElementMap.put(ISectionTag.TAG_TYPE, entity.get(ITag.TAG_TYPE));
        }
        
        Boolean isMultiselect = (Boolean) referencedElementMap.get(ISectionTag.IS_MULTI_SELECT);
        if (isMultiselect != null) {
          entity.put(ITag.IS_MULTI_SELECT, isMultiselect);
        }
        else {
          referencedElementMap.put(ISectionTag.IS_MULTI_SELECT, entity.get(ITag.IS_MULTI_SELECT));
        }
      }
      
      if (roleIds.contains(entityId)) {
        Vertex entityNode = UtilClass.getVertexByIndexedId(entityId,
            VertexLabelConstants.ENTITY_TYPE_ROLE);
        entity = (Map<String, Object>) referencedRoles.get(entityId);
        entity = RoleUtils.getRoleEntityMap(entityNode);
        referencedRoles.put(entityId, entity);
      }
    }
  }
  
  public Vertex fillPriceContextDetails(Vertex attributeVertex,
      Map<String, Object> referencedAttribute, Map<String, Object> mapToReturn) throws Exception
  {
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    
    Vertex priceContext = attributeVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator()
        .next();
    
    String priceContextId = UtilClass.getCodeNew(priceContext);
    
    referencedAttribute.put(CommonConstants.CONTEXT_ID, priceContextId);
    
    if (embeddedVariantContexts.containsKey(priceContextId)) {
      return priceContext;
    }
    
    Map<String, Object> priceContextMap = VariantContextUtils.getReferencedContexts(priceContext);
    embeddedVariantContexts.put(priceContextId, priceContextMap);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_TAGS);
    for (Map<String, Object> contextTag : (List<Map<String, Object>>) priceContextMap
        .get(IReferencedVariantContextModel.TAGS)) {
      String tagId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
      if (referencedTags.containsKey(tagId)) {
        continue;
      }
      Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> entity = TagUtils.getTagMap(tagNode, false);
      referencedTags.put(tagId, entity);
    }
    return priceContext;
  }
  
  protected List<String> fetchPropertyIds(Map<String, Object> referencedPCs,
      Set<String> attributeIds, Set<String> tagIds, Set<String> referencedElementIds)
  {
    List<String> notAttributeOrTagIds = referencedElementIds.stream()
        .filter(elementId -> !(tagIds.contains(elementId) || attributeIds.contains(elementId)))
        .collect(Collectors.toList());
    
    List<String> propertyToRetain = referencedPCs.values()
        .stream()
        .map(referencePC -> getElementId(referencePC))
        .flatMap(x -> x.stream())
        .collect(Collectors.toList());
    
    propertyToRetain.addAll(notAttributeOrTagIds);
    return propertyToRetain;
  }
  
  private List<String> getElementId(Object referencePC)
  {
    
    List<Map<String, Object>> object = (List<Map<String, Object>>) ((Map<String, Object>) referencePC)
        .get(IReferencedPropertyCollectionModel.ELEMENTS);
    List<String> collect = object.stream()
        .map(element -> (String) element.get(IReferencedPropertyCollectionElementModel.ID))
        .collect(Collectors.toList());
    return collect;
  }
  
  protected void filterOutReferencedElements(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel)
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Set<String> visiblePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
    Set<String> editablePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    Set<String> allPropertyIds = new HashSet<>(visiblePropertyIds);
    allPropertyIds.addAll(editablePropertyIds);
    
    allPropertyIds.addAll(helperModel.getKlassRelationshipIds());
    allPropertyIds.addAll(helperModel.getKlassNatureRelationshipIds());
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    if (referencedTaxonomies != null) {
      allPropertyIds.addAll(referencedTaxonomies.keySet());
    }
    referencedElements.keySet()
        .retainAll(allPropertyIds);
  }
  
  protected void fillReferencedPropertyCollectionsForCustomTemplate(
      Iterable<Vertex> propertyCollectionVertices, Map<String, Object> mapToReturn)
  {
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    for (Vertex propertyCollectionVertex : propertyCollectionVertices) {
      String propertyCollectionId = UtilClass.getCodeNew(propertyCollectionVertex);
      if (referencedPropertyCollections.containsKey(propertyCollectionId)) {
        continue;
      }
      Map<String, Object> referencedPropertyCollection = UtilClass
          .getMapFromVertex(new ArrayList<>(), propertyCollectionVertex);
      referencedPropertyCollections.put(propertyCollectionId, referencedPropertyCollection);
      List<Map<String, Object>> elementsList = new ArrayList<Map<String, Object>>();
      Iterable<Edge> entityToRelationships = propertyCollectionVertex.getEdges(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_ENTITY_TO);
      Set<String> entityIds = new HashSet<>();
      for (Edge entityTo : entityToRelationships) {
        Vertex entityVertex = entityTo.getVertex(Direction.OUT);
        String entityId = UtilClass.getCodeNew(entityVertex);
        Map<String, Object> propertyCollectionElement = new HashMap<String, Object>();
        propertyCollectionElement.put(IReferencedPropertyCollectionElementModel.ID, entityId);
        elementsList.add(propertyCollectionElement);
        entityIds.add(entityId);
      }
      
      List<String> propertySequenceIds = propertyCollectionVertex.getProperty(CommonConstants.PROPERTY_SEQUENCE_IDS);
      PropertyCollectionUtils.sortPropertyCollectionList(elementsList, propertySequenceIds,CommonConstants.ID_PROPERTY);
      
      referencedPropertyCollection.put(IReferencedPropertyCollectionModel.ELEMENTS, elementsList);
    }
  }
  
  protected void fillEntityIdsHavingReadPermission(Vertex userInRole,
      Map<String, Object> mapToReturn)
  {
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Set<String> entitiesHavingReadPermission = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.ENTITIES_HAVING_RP);
    entitiesHavingReadPermission.addAll(userInRole.getProperty(IRole.ENTITIES));
    if (entitiesHavingReadPermission.isEmpty()) {
      entitiesHavingReadPermission.addAll(CommonConstants.MODULE_ENTITIES);
    }
  }
  
  protected void fillReferencedTemplates(List<Map<String, Object>> referencedTemplates,
      String roleId, String klassId, Map<String, Object> referencedPermissions) throws Exception
  {
    List<String> compositeKey = Arrays.asList(klassId, roleId);
    String query = "SELECT FROM (SELECT EXPAND (rid) FROM INDEX:"
        + UtilClass.getIndexFromVertexType(VertexLabelConstants.TEMPLATE_PERMISSION)
        + " WHERE key = " + EntityUtil.quoteIt(compositeKey) + ")";
    Iterable<Vertex> x = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    if (x == null || !x.iterator()
        .hasNext()) {
      return;
    }
    
    Set<String> templateIds = new HashSet<>();
    for (Vertex templatePermission : x) {
      Iterable<Vertex> templates = templatePermission.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_ALLOWED_TEMPLATE);
      Vertex templateNode = null;
      Iterator<Vertex> iterator = templates.iterator();
      while (iterator.hasNext()) {
        templateNode = iterator.next();
        referencedTemplates.add(UtilClass.getMapFromNode(templateNode));
        templateIds.add(UtilClass.getCodeNew(templateNode));
      }
    }
    
    Map<String, Set<String>> klassIdVsTemplateIdsMap = (Map<String, Set<String>>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.KLASS_ID_VS_TEMPLATE_IDS_MAP);
    if (klassIdVsTemplateIdsMap == null) {
      klassIdVsTemplateIdsMap = new HashMap<>();
      referencedPermissions.put(IReferencedTemplatePermissionModel.KLASS_ID_VS_TEMPLATE_IDS_MAP,
          klassIdVsTemplateIdsMap);
    }
    klassIdVsTemplateIdsMap.put(klassId, templateIds);
  }
  
  protected void filterContextAccordingToKlassPermission(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Vertex userInRole)
  {
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Set<String> klassIdsHavingRP = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.KLASS_IDS_HAVING_RP);
    Map<String, String> klassIdWithRPVsContextId = helperModel.getKlassIdVsContextId();
    if (!klassIdsHavingRP.isEmpty()) {
      klassIdWithRPVsContextId.keySet()
          .retainAll(klassIdsHavingRP);
    }
    List<String> contextIdsHavingRP = new ArrayList<>(klassIdWithRPVsContextId.values());
    Set<String> associatedAttributeContextIds = helperModel.getAssociatedAttributeContextIds();
    contextIdsHavingRP.addAll(associatedAttributeContextIds);
    embeddedVariantContexts.keySet()
        .retainAll(contextIdsHavingRP);
  }
  
  protected void fillCollectionPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Set<Vertex> collectionVertices = helperModel.getCollectionVertices();
    for (Vertex collectionVertex : collectionVertices) {
      fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
          referencedPermissions, collectionVertex, roleId);
    }
  }
  
  protected void fillVariantContextsOfKlass(Vertex klassNode,
      Map<String, Object> referencedVariantContextsMap, Map<String, Object> mapToReturn,
      Set<String> contextTagIds, String linkedVariantKey) throws Exception
  {
    Map<String, Object> linkedVariantContexts = (Map<String, Object>) referencedVariantContextsMap
        .get(linkedVariantKey);
    
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS);
    
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
            contextTagIds.add(entityId);
          }
          catch (NotFoundException e) {
            // Do nothing;
          }
        }
      }
      
      if (!linkedVariantContexts.containsKey(variantContextId)) {
        linkedVariantContexts.put(variantContextId, variantContextMap);
      }
    }
  }
  
  private void prepareReadOnlyAttributesFromRelationships(Vertex kR,
      List<String> readOnlyAttributeIds, List<String> readOnlyTagIds,
      Set<String> readOnlyAttributeIdsToExclude, Set<String> readOnlyTagIdsToExclude)
  {
    Vertex relationshipNode = kR.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator()
        .next();
    Iterator<Vertex> kRNodes = relationshipNode
        .getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY)
        .iterator();
    Vertex otherSideKRNode = kRNodes.next();
    if (kR.equals(otherSideKRNode)) {
      if (!kRNodes.hasNext()) {
        return; // As it is a self relationship
      }
      otherSideKRNode = kRNodes.next();
    }
    
    Iterable<Edge> hasRelationshipAttributeEdges = otherSideKRNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_ATTRIBUTE);
    for (Edge hasRelationshipAttributeEdge : hasRelationshipAttributeEdges) {
      Vertex attribute = hasRelationshipAttributeEdge.getVertex(Direction.IN);
      String couplingType = hasRelationshipAttributeEdge.getProperty(ISectionElement.COUPLING_TYPE);
      String attributeId = UtilClass.getCodeNew(attribute);
      if (couplingType.equals(CommonConstants.READ_ONLY_COUPLED)) {
        readOnlyAttributeIds.add(attributeId);
      }
      if (couplingType.equals(CommonConstants.TIGHTLY_COUPLED)
          || couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
        readOnlyAttributeIdsToExclude.add(attributeId);
      }
    }
    Iterable<Edge> hasRelationshipTagEdges = otherSideKRNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_TAG);
    for (Edge hasRelationshipTagEdge : hasRelationshipTagEdges) {
      Vertex tag = hasRelationshipTagEdge.getVertex(Direction.IN);
      String couplingType = hasRelationshipTagEdge.getProperty(ISectionElement.COUPLING_TYPE);
      String tagId = UtilClass.getCodeNew(tag);
      if (couplingType.equals(CommonConstants.READ_ONLY_COUPLED)) {
        readOnlyTagIds.add(tagId);
      }
      if (couplingType.equals(CommonConstants.TIGHTLY_COUPLED)
          || couplingType.equals(CommonConstants.DYNAMIC_COUPLED)) {
        readOnlyTagIdsToExclude.add(tagId);
      }
    }
  }
  
  /**
   * @author yash.waghmare
   * @param roleId
   * @param configDetails
   * @throws Exception
   */
  protected void fillFunctionPermissionDetails(Vertex userInRole, Map<String, Object> configDetails) throws Exception
  {
    Map<String, Object> referencedPermission = (Map<String, Object>) configDetails
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    Map<String, Boolean> functionPermissionMap = GlobalPermissionUtils
        .getFunctionPermission(userInRole);
    referencedPermission.put(IReferencedTemplatePermissionModel.FUNCTION_PERMISSION,
        functionPermissionMap);
  }
}
