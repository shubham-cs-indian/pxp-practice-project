package com.cs.runtime.strategy.plugin.usecase.base.variant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.ListUtils;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.attribute.util.AttributeUtils;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variant.util.VariantUtils;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.INumberAttribute;
import com.cs.core.config.interactor.entity.attribute.IUnitAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISectionAttribute;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;
import com.cs.core.config.interactor.entity.propertycollection.ISectionTag;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.ISortDataModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.ITemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewRequestModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractGetConfigDetails;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForGetVariantInstancesInTableView
    extends AbstractGetConfigDetails {
  
  public AbstractGetConfigDetailsForGetVariantInstancesInTableView(
      OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA = Arrays.asList(
      IAttribute.ID, IAttribute.LABEL, IAttribute.TYPE, IUnitAttribute.DEFAULT_UNIT,
      IAttribute.CODE, INumberAttribute.PRECISION);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA       = Arrays
      .asList(ITag.ID, ITag.LABEL, ITag.TAG_TYPE, ITag.CODE);
  
  protected static final List<String> FIELDS_TO_FETCH_FOR_SORT_DATA             = Arrays
      .asList(IAttribute.ID, IAttribute.TYPE, IAttribute.LABEL, IAttribute.CODE);
  
  protected List<String>              MANDATORY_SORTABLE_ATTRIBUTE_IDS          = Arrays.asList(
      IStandardConfig.StandardProperty.nameattribute.toString(),
      IStandardConfig.StandardProperty.createdonattribute.toString(),
      IStandardConfig.StandardProperty.lastmodifiedattribute.toString());
  
  private final String TEMP_REFERENCED_ELEMENTS = "tempReferencedElements";
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewRequestModel.USER_ID);
    String contextId = (String) requestMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewRequestModel.CONTEXT_ID);
    List<String> propertyIdsToGet = (List<String>) requestMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewRequestModel.PROPERTY_IDS);
    String moduleId = (String) requestMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewRequestModel.MODULE_ID);
    String systemLevelId = moduleId != null ? EntityUtil.getSystemLevelIdByModuleId(moduleId)
        : null;
    
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    
    Map<String, Object> mapToReturn = getMapToReturn();
    
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.getEntityIds()
        .add(IStandardConfig.StandardProperty.nameattribute.toString());
    
    String baseType = (String) requestMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewRequestModel.BASE_TYPE);
    
    Map<String, Map<String, List<String>>> instanceIdVsOtherClassifiers = (Map<String, Map<String, List<String>>>) requestMap.get(
        IConfigDetailsForGetVariantInstancesInTableViewRequestModel.INSTANCE_ID_VS_OTHER_CLASSIFIERS);

    
    Vertex contextKlassNode = fillVariantContextDetails(mapToReturn, contextId, helperModel, propertyIdsToGet, baseType);
    
    fillInstanceIdVsReferencedElements(mapToReturn, contextKlassNode, helperModel, propertyIdsToGet, instanceIdVsOtherClassifiers);
    
    fillReferencedPermission(mapToReturn, userInRole, helperModel);
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    fillFilterInfo(mapToReturn, contextId, systemLevelId);
    List<String> parentKlassIds = (List<String>) requestMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewRequestModel.PARENT_KLASS_IDS);
    List<String> parentTaxonomyIds = (List<String>) requestMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewRequestModel.PARENT_TAXONOMY_IDS);
    mergeCouplingTypeOfReferencedElementsFromContext(mapToReturn, helperModel.getNatureNode(),
        parentKlassIds, parentTaxonomyIds);
    return mapToReturn;
  }
  
  private void fillInstanceIdVsReferencedElements(Map<String, Object> mapToReturn, Vertex contextKlassNode,
      IGetConfigDetailsHelperModel helperModel, List<String> propertyIdsToGet, Map<String, Map<String, List<String>>> instanceIdVsOtherClassifiers) throws Exception
  {
    Map<String, Object> contextReferencedElements = new HashMap<>();
    mapToReturn.put(TEMP_REFERENCED_ELEMENTS , contextReferencedElements);
    String contextId = UtilClass.getCodeNew(contextKlassNode);
    
    fillReferencedElements(mapToReturn, contextKlassNode,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, null , helperModel, propertyIdsToGet);
    
    // FIXME : remove this code as soon as core fix relationship coupling
    mergeCouplingTypeFromOfReferencedElementsFromRelationship(
        Arrays.asList(contextId), contextReferencedElements, null);  
    
    Map<String, Map<String, Object>> instanceIdVsReferencedElements = (Map<String, Map<String, Object>>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.INSTANCE_ID_VS_REFERENCED_ELEMENTS);
    
    for(String instanceId : instanceIdVsOtherClassifiers.keySet()) {
      Map<String, List<String>> otherClassifiers = instanceIdVsOtherClassifiers.get(instanceId);
      List<String> klasses = otherClassifiers.get("klasses");
      List<String> taxonomies = otherClassifiers.get("taxonomies");
      
      Map<String, Object> referencedElements = new HashMap<>();
      instanceIdVsReferencedElements.put(instanceId, referencedElements);
      
      referencedElements.putAll(contextReferencedElements);
      mapToReturn.put(TEMP_REFERENCED_ELEMENTS, referencedElements);
      
      for(String klassId : klasses) {
        
        Vertex klassVertex = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        helperModel.getNonNatureKlassNodes().add(klassVertex);
        
        fillReferencedElements(mapToReturn, klassVertex, null , null, helperModel, propertyIdsToGet);
        fillReferencedPropertyCollections(helperModel, klassVertex, mapToReturn, propertyIdsToGet);
      }
      
      for(String taxonomyId : taxonomies) {
        
        Vertex taxonomyVertex = UtilClass.getVertexById(taxonomyId, VertexLabelConstants.ATTRIBUTION_TAXONOMY);
        helperModel.getTaxonomyVertices().add(taxonomyVertex);
        
        fillReferencedElements(mapToReturn, taxonomyVertex, null , null, helperModel, propertyIdsToGet);
        fillReferencedPropertyCollections(helperModel, taxonomyVertex, mapToReturn, propertyIdsToGet);
        fillReferencedTaxonomy(mapToReturn, taxonomyVertex, helperModel);
      }
      
      // FIXME : remove this code as soon as core fix relationship coupling
      mergeCouplingTypeFromOfReferencedElementsFromRelationship(
          ListUtils.union(klasses, taxonomies), referencedElements, null);
    }
    mapToReturn.remove(TEMP_REFERENCED_ELEMENTS);
  }
  
  protected void fillReferencedTaxonomy(Map<String, Object> mapToReturn, Vertex taxonomyVertex,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedTaxonomyMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAXONOMIES);
    
      String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
      
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, IReferencedArticleTaxonomyModel.LABEL,
              IReferencedArticleTaxonomyModel.ICON, IReferencedArticleTaxonomyModel.TAXONOMY_TYPE,
              IReferencedArticleTaxonomyModel.CODE, IReferencedArticleTaxonomyModel.BASETYPE),
          taxonomyVertex);
      
      fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomyVertex);
      taxonomyMap.put(IReferencedArticleTaxonomyModel.PROPERTY_COLLECTIONS,
          UtilClass.getCodes(
              new ArrayList<>(helperModel.getTypeIdVsAssociatedPropertyCollectionVertices()
                  .get(taxonomyId))));
      referencedTaxonomyMap.put(taxonomyId, taxonomyMap);
  }
  
  @Override
  protected void fillReferencedElementInRespectiveMap(Map<String, Object> mapToReturn,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ATTRIBUTES);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_TAGS);
    Map<String, Object> referencedRoles = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES);
    Map<String, Object> referencedPermissions = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PERMISSIONS);
    Map<String, Object> referencedPCs = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    Map<String, Map<String, Object>> instanceIdVsReferencedElements = (Map<String, Map<String, Object>>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.INSTANCE_ID_VS_REFERENCED_ELEMENTS);
    
    Set<String> visiblePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
    Set<String> editablePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    
    Set<String> allPropertyIds = new HashSet<>(visiblePropertyIds);
    allPropertyIds.addAll(editablePropertyIds);
    Set<String> attributeIds = helperModel.getAttributeIds();
    Set<String> roleIds = helperModel.getRoleIds();
    Set<String> tagIds = helperModel.getTagIds();
    
    Set<String> referencedElementIds = getReferencedElementIds(instanceIdVsReferencedElements);
    
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
        
        for(Map<String, Object> referencedElementMap : instanceIdVsReferencedElements.values()) {
          Map<String, Object> elementMap = (Map<String, Object>) referencedElementMap
              .get(entityId);
         if(elementMap != null) {
          String defaultValue = (String) elementMap.get(ISectionAttribute.DEFAULT_VALUE);
          if (defaultValue == null || defaultValue.equals("")) {
            elementMap.put(ISectionAttribute.DEFAULT_VALUE,
                entity.get(IAttribute.DEFAULT_VALUE));
            }
          }
        }
      }
      
      if (tagIds.contains(entityId)) {
        for(Map<String, Object> referencedElement : instanceIdVsReferencedElements.values()) { 
       
          Map<String, Object> referencedElementMap = (Map<String, Object>) referencedElement
            .get(entityId);
         if(referencedElementMap != null) {
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
  
  private Set<String> getReferencedElementIds(
      Map<String, Map<String, Object>> instanceIdVsReferencedElements)
  {
    Set<String> keys = new HashSet<>();
    instanceIdVsReferencedElements.values().forEach(referencedElement -> keys.addAll(referencedElement.keySet()));
    return keys;
  }

  protected void fillReferencedPermission(Map<String, Object> responseMap, Vertex userInRole,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    fillGlobalPermissionDetails(UtilClass.getCodeNew(userInRole), responseMap, helperModel);
    fillKlassIdsAndTaxonomyIdsHavingReadPermission(userInRole, responseMap);
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    String roleId = UtilClass.getCodeNew(userInRole);
    
    // context tag always have visible and editable permission
    Set<String> contextTagIds = helperModel.getContextTagIds();
    Set<String> editablePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.EDITABLE_PROPERTY_IDS);
    Set<String> visiblePropertyIds = (Set<String>) referencedPermissions
        .get(IReferencedTemplatePermissionModel.VISIBLE_PROPERTY_IDS);
    visiblePropertyIds.addAll(contextTagIds);
    editablePropertyIds.addAll(contextTagIds);
    
    fillNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
    fillNonNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
    fillTaxonomyPermissions(responseMap, helperModel, referencedPermissions, roleId);
  }
  
  @Override
  protected void fillNonNatureKlassPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Set<Vertex> nonNatureKlassVertices = helperModel.getNonNatureKlassNodes();
    for (Vertex nonNatureKlassVertex : nonNatureKlassVertices) {
      fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
          referencedPermissions, nonNatureKlassVertex, roleId);
    }
  }
  
  @Override
  protected void fillNatureKlassPermissions(Map<String, Object> responseMap,
      IGetConfigDetailsHelperModel helperModel, Map<String, Object> referencedPermissions,
      String roleId) throws Exception
  {
    Vertex natureKlassVertex = helperModel.getNatureNode();
    fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
        referencedPermissions, natureKlassVertex, roleId);
  }
  
  /**
   * fill referenced VariantContext and referenced PC using its template
   *
   * @author Lokesh
   * @param mapToReturn
   * @param contextId
   * @param helperModel
   * @param propertyIdsToGet
   * @throws Exception
   */
  protected Vertex fillVariantContextDetails(Map<String, Object> mapToReturn, String contextId,
      IGetConfigDetailsHelperModel helperModel, List<String> propertyIdsToGet, String baseType)
      throws Exception
  {
    Vertex contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    
    Iterator<Vertex> contextKlassIterator = contextNode
        .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    Vertex contextKlassNode = VariantContextUtils.getKlassNodeFromContextNode(baseType,
        contextKlassIterator);
    Set<String> contextTagIds = helperModel.getContextTagIds();
    
    helperModel.setNatureNode(contextKlassNode);
    fillReferencedPropertyCollections(helperModel, contextKlassNode, mapToReturn, propertyIdsToGet);
    
    Map<String, Object> referencedPropertyCollections = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS);
    
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContexts
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_TAGS);
    
    Map<String, Object> variantContextMap = VariantContextUtils.getReferencedContexts(contextNode);
    
    Set<String> contextPC = new HashSet<>(referencedPropertyCollections.keySet());
    variantContextMap.put(IReferencedVariantContextModel.PROPERTY_COLLECTIONS,
        contextPC);
    
    variantContextMap.put(IReferencedVariantContextModel.CONTEXT_KLASS_ID,
        UtilClass.getCodeNew(contextKlassNode));
    
    for (Map<String, Object> contextTag : (List<Map<String, Object>>) variantContextMap
        .get(IReferencedVariantContextModel.TAGS)) {
      String entityId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
      if (referencedTagsMap.containsKey(entityId)) {
        continue;
      }
      Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> entity = TagUtils.getTagMap(entityNode, false);
      referencedTagsMap.put(entityId, entity);
      contextTagIds.add(entityId);
    }
    
    String contextType = contextNode.getProperty(IVariantContext.TYPE);
    switch (contextType) {
      case CommonConstants.IMAGE_VARIANT:
      case CommonConstants.CONTEXTUAL_VARIANT:
      case CommonConstants.GTIN_VARIANT:
      case CommonConstants.LANGUAGE_VARIANT:
        embeddedVariantContexts.put(contextId, variantContextMap);
        break;
    }
    return contextKlassNode;
  }
  
  /**
   * fill searchData , sort Data and filterData using referencedAttributes,
   * referencedTags and referencedElements
   *
   * @author Lokesh
   * @param mapToReturn
   * @param contextId
   * @throws Exception
   */
  private void fillFilterInfo(Map<String, Object> mapToReturn, String contextId,
      String systemLevelId) throws Exception
  {
    
    Map<String, Object> referencedAttributes = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_ATTRIBUTES);
    Map<String, Map<String, Object>> instanceIdVsReferencedElements = (Map<String, Map<String, Object>>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.INSTANCE_ID_VS_REFERENCED_ELEMENTS);
    Map<String, Object> referencedTags = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_TAGS);
    
    List<String> searchableAttributes = new ArrayList<>();
    List<Map<String, Object>> sortableAttributes = new ArrayList<>();
    List<Map<String, Object>> filterData = new ArrayList<>();
    List<String> translatableAttributeIds = new ArrayList<>();
    
    Map<String, Object> referencedVariantContext = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> embeddedVariantContexts = (Map<String, Object>) referencedVariantContext
        .get(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS);
    Map<String, Object> contextMap = (Map<String, Object>) embeddedVariantContexts.get(contextId);
    List<String> contextTagIds = VariantUtils.getContextTagIds(contextMap);
    
    for (String attributeId : referencedAttributes.keySet()) {
      Map<String, Object> referencedAttribute = (Map<String, Object>) referencedAttributes
          .get(attributeId);
      /**
       * In case of calculated & concatenated, their dependent attributes may
       * not be available in referenced elements
       */
      if (!elementMapExist(attributeId, instanceIdVsReferencedElements)) {
        continue;
      }
      
      List<String> availability = (List<String>) referencedAttribute.get(IAttribute.AVAILABILITY);
      if (availability != null && availability.size() != 0) {
        if (!availability.contains(systemLevelId)) {
          continue;
        }
      }
      
      Boolean isSearchable = (Boolean) referencedAttribute.get(IAttribute.IS_SEARCHABLE);
      if (isSearchable != null && isSearchable) {
        searchableAttributes.add(attributeId);
      }
      Boolean isSortable = (Boolean) referencedAttribute.get(IAttribute.IS_SORTABLE);
      if (isSortable != null && isSortable) {
        Map<String, Object> sortableMap = prepareMapByFieldsToPut(referencedAttribute,
            FIELDS_TO_FETCH_FOR_SORT_DATA);
        sortableAttributes.add(sortableMap);
      }
      Boolean isFilterable = (Boolean) referencedAttribute.get(IAttribute.IS_FILTERABLE);
      if (isFilterable != null && isFilterable) {
        Map<String, Object> filterMap = prepareMapByFieldsToPut(referencedAttribute,
            FIELDS_TO_FETCH_FOR_FILTER_ATTRIBUTE_DATA);
        filterData.add(filterMap);
      }
      Boolean isTranslatable = (Boolean) referencedAttribute.get(IAttribute.IS_TRANSLATABLE);
      if (isTranslatable != null && isTranslatable) {
        translatableAttributeIds.add(attributeId);
      }
    }
    
    for (String tagId : referencedTags.keySet()) {
      Map<String, Object> referencedTag = (Map<String, Object>) referencedTags.get(tagId);
      Boolean isFilterable = (Boolean) referencedTag.get(ITag.IS_FILTERABLE);
      if (isFilterable || contextTagIds.contains(tagId)) {
        Map<String, Object> filterMap = prepareMapByFieldsToPut(referencedTag,
            FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA);
        List<Map<String, Object>> children = new ArrayList<>();
        String tagType = (String) filterMap.remove(ITag.TAG_TYPE);
        filterMap.put(IApplicableFilterModel.TYPE, tagType);
        filterMap.put(IApplicableFilterModel.CHILDREN, children);
        List<Map<String, Object>> tagValues = (List<Map<String, Object>>) referencedTag
            .get(ITag.CHILDREN);
        List<String> tagValuesSequence = (List<String>) referencedTag.get(ITag.TAG_VALUES_SEQUENCE);
        final List<String> tagValuesSequenceCopy = tagValuesSequence;
        for (Map<String, Object> tagValue : tagValues) {
          Map<String, Object> tagValueMap = prepareMapByFieldsToPut(tagValue,
              FIELDS_TO_FETCH_FOR_FILTER_TAG_DATA);
          tagValueMap.remove(ITag.TAG_TYPE);
          children.add(tagValueMap);
        }
        children.sort((Map<String, Object> obj1,
            Map<String, Object> obj2) -> ((Integer) tagValuesSequenceCopy
                .indexOf(obj1.get(CommonConstants.ID_PROPERTY))).compareTo(
                    tagValuesSequenceCopy.indexOf(obj2.get(CommonConstants.ID_PROPERTY))));
        filterData.add(filterMap);
      }
    }
    
    if (!referencedAttributes.keySet()
        .containsAll(MANDATORY_SORTABLE_ATTRIBUTE_IDS)) {
      List<String> mandatoryAttributeIds = new ArrayList<>(MANDATORY_SORTABLE_ATTRIBUTE_IDS);
      mandatoryAttributeIds.removeAll(referencedAttributes.keySet());
      Map<String, Object> sortData = TaxonomyUtil.getSortData(mandatoryAttributeIds);
      sortableAttributes
          .addAll((List<Map<String, Object>>) sortData.get(ISortDataModel.ATTRIBUTES));
    }
    
    Map<String, Object> sortData = new HashMap<String, Object>();
    sortData.put(ISortDataModel.ATTRIBUTES, sortableAttributes);
    /*Map<String, Object> filterData = new HashMap<String, Object>();
    filterData.put(IFilterDataModel.ATTRIBUTES, filterableAttributes);
    filterData.put(IFilterDataModel.TAGS, filterableTags);*/
    
    Map<String, Object> filterInfo = new HashMap<>();
    filterInfo.put(IGetFilterInfoModel.SEARCHABLE_ATTRIBUTES, searchableAttributes);
    filterInfo.put(IGetFilterInfoModel.SORT_DATA, sortData);
    filterInfo.put(IGetFilterInfoModel.FILTER_DATA, filterData);
    filterInfo.put(IGetFilterInfoModel.TRANSLATABLE_ATTRIBUTES_IDS, translatableAttributeIds);
    
    mapToReturn.put(IConfigDetailsForGetPropertiesVariantInstancesInTableViewModel.FILTER_INFO,
        filterInfo);
  }
  
 private boolean elementMapExist(String attributeId,
      Map<String, Map<String, Object>> instanceIdVsReferencedElements)
  {
    return instanceIdVsReferencedElements.values()
        .stream()
        .anyMatch(map -> map.get(attributeId) != null);
  }

 private Map<String, Object> prepareMapByFieldsToPut(Map<String, Object> requestMap,
      List<String> fieldsToPut)
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    for (String field : fieldsToPut) {
      if (!requestMap.containsKey(field)) {
        continue;
      }
      responseMap.put(field, requestMap.get(field));
    }
    return responseMap;
  }
  
  protected Map<String, Object> getMapToReturn()
  {
    
    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS,
        new HashMap<>());
    referencedVariantContextsMap.put(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS,
        new HashMap<>());
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS,
        new HashMap<>());
    referencedVariantContextsMap.put(IReferencedContextModel.PROMOTIONAL_VERSION_CONTEXTS,
        new HashMap<>());
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS,
        new HashMap<>());
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedRolesMap = new HashMap<>();
    
    Map<String, Object> referencedPermissions = new HashMap<>();
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.EDITABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.EXPANDABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.VISIBLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.EDITABLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.VISIBLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.ENTITIES_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.KLASS_IDS_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(ITemplatePermissionForGetVariantInstancesModel.TAXONOMY_IDS_HAVING_RP,
        new HashSet<String>());
    referencedPermissions.put(
        ITemplatePermissionForGetVariantInstancesModel.ALL_TAXONOMY_IDS_HAVING_RP,
        new HashSet<String>());
    mapToReturn.put(
        IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_PROPERTY_COLLECTIONS,
        referencedPropertyCollectionMap);
    mapToReturn.put(
        IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_ATTRIBUTES,
        referencedAttributeMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_TAGS,
        referencedTagMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_ROLES,
        referencedRolesMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_ELEMENTS,
        new HashMap<>());
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_PERMISSIONS,
        referencedPermissions);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.INSTANCE_ID_VS_REFERENCED_ELEMENTS,
        new HashMap<>());
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_TAXONOMIES,
        new HashMap<>());
    
    return mapToReturn;
  }
  
  @Override
  protected void fillReferencedElements(Map<String, Object> mapToReturn, Vertex klassVertex,
      String vertexLabel, String contextId, IGetConfigDetailsHelperModel helperModel,
      List<String> propertiesToFetch) throws Exception
  {
    Map<String, Object> referencedElements = (Map<String, Object>) mapToReturn
        .get(TEMP_REFERENCED_ELEMENTS);
    Map<String, Set<String>> typeIdVsAssociatedPropertyIds = helperModel
        .getTypeIdVsAssociatedPropertyIds();
    Set<String> propertyIds = new HashSet<>();
    typeIdVsAssociatedPropertyIds.put(UtilClass.getCodeNew(klassVertex), propertyIds);

    Iterable<Vertex> kPNodesIterable = null;
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
    for (Vertex klassPropertyNode : kPNodesIterable) {
      String entityId = klassPropertyNode.getProperty(ISectionElement.PROPERTY_ID);
      
      Vertex entityNode = klassPropertyNode
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY)
          .iterator()
          .next();
      
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
          Map<String, Object> propertyContextMap = UtilClass
              .getMapFromVertex(PROPERTY_CONTEXT_FIELDS_TO_FETCH, attributeContextVertex);
          Set<String> entityIds = new HashSet<>();
          entityIds.add(entityId);
          propertyContextMap.put(IReferencedVariantContextModel.ENTITY_IDS, entityIds);
          embeddedContexts.put(klassPropertyContextId, propertyContextMap);
        }
      }
      
      String entityType = klassPropertyNode.getProperty(CommonConstants.TYPE);
      
      Map<String, Object> referencedElementMap = UtilClass.getMapFromNode(klassPropertyNode);
      referencedElementMap.put(CommonConstants.ID_PROPERTY, entityId);
      if (klassPropertyContextId != null) {
        referencedElementMap.put(ISectionAttribute.ATTRIBUTE_VARIANT_CONTEXT,
            klassPropertyContextId);
      }
      
      switch (entityType) {
        case SystemLevelIds.PROPERTY_TYPE_TAG:
          List<Map<String, Object>> defaultTagValues = KlassUtils
              .getDefaultTagValuesOfKlassPropertyNode(klassPropertyNode);
          referencedElementMap.put(ISectionTag.DEFAULT_VALUE, defaultTagValues);
          List<String> selectedTagValues = KlassUtils
              .getSelectedTagValuesListOfKlassPropertyNode(klassPropertyNode);
          referencedElementMap.put(CommonConstants.SELECTED_TAG_VALUES_LIST, selectedTagValues);
          helperModel.getTagIds()
              .add(entityId);
          break;
        
        case SystemLevelIds.PROPERTY_TYPE_ATTRIBUTE:
          helperModel.getAttributeIds()
              .add(entityId);
          break;
        
        case SystemLevelIds.PROPERTY_TYPE_ROLE:
          helperModel.getRoleIds()
              .add(entityId);
          break;
          
        case SystemLevelIds.PROPERTY_TYPE_TAXONOMY:
          fillMinorTaxonomyInReferencedTaxonomy(mapToReturn, entityId, referencedElementMap);
          
          // Above method adds entity in ReferencedElements too. Hence need to remove it.
          ((Map<String, Object>) mapToReturn
              .get(IGetConfigDetailsForCustomTabModel.REFERENCED_ELEMENTS)).remove(entityId);
          break;
        
        default:
          continue;
      }
      
      // This function fill conflicting source in referencedElementMap. It must
      // be outside if
      // because during merge it get both conflicting values
//      fillConflictionSourcesInReferencedElementMap(klassId , conflictingSourceType,
//          referencedElementMap);
      
      if (referencedElements.containsKey(entityId)) {
        Map<String, Object> existingReferencedElement = (Map<String, Object>) referencedElements
            .get(entityId);
        mergeReferencedElement(referencedElementMap, existingReferencedElement,
            UtilClass.getCodeNew(klassVertex), entityType, false);
      }
      else {
        referencedElements.put(entityId, referencedElementMap);
      }
    }
  }
  
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
    
    Vertex natureNode = helperModel.getNatureNode();
    String klassId = UtilClass.getCodeNew(natureNode);
    Boolean isNature = (Boolean) natureNode.getProperty(IKlass.IS_NATURE);
    if (isNature == null || !isNature) {
      return;
    }
    Map<String, Object> permissionMap = GlobalPermissionUtils.getKlassAndTaxonomyPermission(klassId,
        roleId);
    
    GlobalPermissionUtils.mergeGlobalPermissons(globalPermissionMap, permissionMap);
  }
}
