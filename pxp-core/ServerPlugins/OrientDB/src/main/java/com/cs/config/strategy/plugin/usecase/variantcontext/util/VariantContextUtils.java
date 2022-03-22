package com.cs.config.strategy.plugin.usecase.variantcontext.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagAndTagValuesIds;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IProjectKlass;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.config.interactor.model.klass.IConflictingValueModel;
import com.cs.core.config.interactor.model.klass.IContextKlassModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.klass.IModifiedContextKlassModel;
import com.cs.core.config.interactor.model.klass.IProjectKlassSaveModel;
import com.cs.core.config.interactor.model.variantcontext.IConfigDetailsForGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IGetVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.config.interactor.model.variantcontext.ISaveVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IUniqueSelectorModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextModifiedTagsModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagModel;
import com.cs.core.config.interactor.model.variantcontext.IVariantContextTagValuesModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.MultipleLinkFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdAndCouplingTypeModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;
import com.cs.core.runtime.strategy.model.couplingtype.IIdCodeCouplingTypeModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class VariantContextUtils {

  public static Map<String, Object> getReferencedContexts(Vertex contextNode)
  {
    Map<String, Object> variantContextMap = new HashMap<>();
    List<Map<String, Object>> contextTagsList = getContextTags(contextNode);
    variantContextMap.put(IVariantContext.ID, UtilClass.getCodeNew(contextNode));
    variantContextMap.put(IVariantContext.CODE, contextNode.getProperty(IVariantContext.CODE));
    variantContextMap.put(IVariantContext.LABEL,
        UtilClass.getValueByLanguage(contextNode, IVariantContext.LABEL));
    variantContextMap.put(IVariantContext.TYPE, contextNode.getProperty(IVariantContext.TYPE));
    variantContextMap.put(IVariantContext.CONTEXT_IID,
        contextNode.getProperty(IVariantContext.CONTEXT_IID));
    variantContextMap.put(IVariantContext.IS_AUTO_CREATE,
        contextNode.getProperty(IVariantContext.IS_AUTO_CREATE));
    variantContextMap.put(IVariantContext.IS_TIME_ENABLED,
        contextNode.getProperty(IVariantContext.IS_TIME_ENABLED));
    variantContextMap.put(IReferencedVariantContextModel.TAGS, contextTagsList);
    variantContextMap.put(IReferencedVariantContextModel.ENTITIES,
        contextNode.getProperty(IVariantContext.ENTITIES));
    variantContextMap.put(IReferencedVariantContextModel.DEFAULT_VIEW,
        contextNode.getProperty(IVariantContext.DEFAULT_VIEW));
    variantContextMap.put(IReferencedVariantContextModel.IS_DUPLICATE_VARIANT_ALLOWED,
        contextNode.getProperty(IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED));
    variantContextMap.put(IReferencedVariantContextModel.DEFAULT_TIME_RANGE,
        contextNode.getProperty(IVariantContext.DEFAULT_TIME_RANGE));
    variantContextMap.put(IReferencedVariantContextModel.CODE,
        contextNode.getProperty(IVariantContext.CODE));
    UtilClass.fetchIconInfo(contextNode, variantContextMap);
    
    return variantContextMap;
  }

  /**
   * @author Lokesh
   * @param contextNode
   * @return
   */
  public static List<Map<String, Object>> getContextTags(Vertex contextNode)
  {
    List<Map<String, Object>> contextTagsList = new ArrayList<Map<String, Object>>();
    Iterable<Vertex> contextTags = contextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    for (Vertex contextTag : contextTags) {
      Vertex tagNode = null;
      Iterator<Vertex> tagNodes = contextTag
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY)
          .iterator();
      // FIXME : throw exception if contextTagNode Not found
      if (!tagNodes.hasNext()) {
        continue;
      }
      tagNode = tagNodes.next();
      Map<String, Object> contextTagMap = new HashMap<String, Object>();
      contextTagMap.put(IReferencedVariantContextTagsModel.TAG_ID, UtilClass.getCodeNew(tagNode));
      Iterable<Vertex> contextTagValueNodes = contextTag.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE);
      List<String> selectedTagValueIds = new ArrayList<>();
      for (Vertex contextTagValueNode : contextTagValueNodes) {
        selectedTagValueIds.add(UtilClass.getCodeNew(contextTagValueNode));
      }
      contextTagMap.put(IReferencedVariantContextTagsModel.TAG_VALUE_IDS, selectedTagValueIds);
      contextTagsList.add(contextTagMap);
    }

    return contextTagsList;
  }

  /**
   * @author Lokesh
   * @param klassADM
   * @param klassNode
   * @param contextualDataTransfer
   * @throws Exception
   */
  public static void manageLinkedEmbeddedContextKlasses(Map<String, Object> klassADM,
      Vertex klassNode, Map<String, Map<String, Object>> contextualDataTransfer,  List<Long> removedContextClassifierIIDs
  /*,Vertex templateNode*/ ) throws Exception
  {
    List<Map<String, Object>> addedContextKlasses = (List<Map<String, Object>>) klassADM
        .get(IProjectKlassSaveModel.ADDED_CONTEXT_KLASSES);
    List<Map<String, Object>> modifiedContextKlasses = (List<Map<String, Object>>) klassADM
        .get(IProjectKlassSaveModel.MODIFIED_CONTEXT_KLASSES);
    List<String> deletedContextKlasses = (List<String>) klassADM
        .get(IProjectKlassSaveModel.DELETED_CONTEXT_KLASSES);

    manageLinkedContextKlasses(klassADM, klassNode, addedContextKlasses, modifiedContextKlasses,
        deletedContextKlasses, contextualDataTransfer, removedContextClassifierIIDs);
  }

  public static void manageLinkedContextKlasses(Map<String, Object> klassADM, Vertex klassNode,
      List<Map<String, Object>> addedContextKlasses,
      List<Map<String, Object>> modifiedContextKlasses, List<String> deletedContextKlasses,
      Map<String, Map<String, Object>> contextualDataTransfer,  List<Long> removedContextClassifierIIDs) throws Exception
  {
    manageAddedContextKlasses(klassNode, addedContextKlasses, contextualDataTransfer);
    manageModifiedContextKlasses(klassNode, modifiedContextKlasses, contextualDataTransfer);
    manageDeletedContextKlasses(klassNode, deletedContextKlasses, contextualDataTransfer, removedContextClassifierIIDs);

    /* if (!addedContextKlasses.isEmpty() || !modifiedContextKlasses.isEmpty()) {
      fillContextKlassSavePropertiesToInherit(klassADM, klassNode, addedContextKlasses, modifiedContextKlasses);
    }*/
  }

  /*private static void fillContextKlassSavePropertiesToInherit(Map<String, Object> klassADM,
      Vertex klassNode, List<Map<String, Object>> addedContextKlasses,
      List<Map<String, Object>> modifiedContextKlasses)
  {
    Map<String, Object> newAddedContextKlassesAgainstContextId  = getListAsMap(addedContextKlasses, IModifiedContextKlassModel.CONTEXT_ID);
    Map<String, Object> newModifiedContextKlassesAgainstContextId  = getListAsMap(modifiedContextKlasses, IModifiedContextKlassModel.CONTEXT_ID);

    if (!newAddedContextKlassesAgainstContextId.isEmpty() || !newModifiedContextKlassesAgainstContextId.isEmpty()) {
      Map<String, Object> contextKlassSavePropertiesToInherit = new HashMap<>();
      if( klassADM.containsKey(IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_INHERIT)) {
        contextKlassSavePropertiesToInherit  = (Map<String, Object>) klassADM.get(IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_INHERIT);
        Map<String, Object> addedContextKlassesAgainstContextId = (Map<String, Object>) contextKlassSavePropertiesToInherit.get(IContextKlassSavePropertiesToInheritModel.ADDED_CONTEXTS);
        Map<String, Object> modifiedContextKlassesAgainstContextId = (Map<String, Object>) contextKlassSavePropertiesToInherit.get(IContextKlassSavePropertiesToInheritModel.MODIFIED_CONTEXTS);
        if(addedContextKlassesAgainstContextId == null) {
          addedContextKlassesAgainstContextId = new HashMap<>();
        }
        if(modifiedContextKlassesAgainstContextId == null) {
          modifiedContextKlassesAgainstContextId = new HashMap<>();
        }
        addedContextKlassesAgainstContextId.putAll(newAddedContextKlassesAgainstContextId);
        modifiedContextKlassesAgainstContextId.putAll(newModifiedContextKlassesAgainstContextId);
      }
      else {
        if(klassNode.getProperty(CommonConstants.ORIENTDB_CLASS_PROPERTY).equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
          contextKlassSavePropertiesToInherit.put(IContextKlassSavePropertiesToInheritModel.TAXONOMY_ID, UtilClass.getCode(klassNode));
        }
        else {
          contextKlassSavePropertiesToInherit.put(IContextKlassSavePropertiesToInheritModel.KLASS_ID, UtilClass.getCode(klassNode));
        }

        contextKlassSavePropertiesToInherit.put(IContextKlassSavePropertiesToInheritModel.ADDED_CONTEXTS, newAddedContextKlassesAgainstContextId);
        contextKlassSavePropertiesToInherit.put(IContextKlassSavePropertiesToInheritModel.MODIFIED_CONTEXTS, newModifiedContextKlassesAgainstContextId);
        klassADM.put(IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_INHERIT,
            contextKlassSavePropertiesToInherit);
      }
    }
  }*/

  public static void manageAddedContextKlasses(Vertex klassNode,
      List<Map<String, Object>> addedContextKlasses,
      Map<String, Map<String, Object>> contextualDataTransfer) throws Exception
  {
    if (!addedContextKlasses.isEmpty()) {
      for (Map<String, Object> addedContextKlassMap : addedContextKlasses) {
        String contextKlassId = (String) addedContextKlassMap
            .get(IContextKlassModel.CONTEXT_KLASS_ID);
        List<Map<String, Object>> tags = (List<Map<String, Object>>) addedContextKlassMap
            .get(IContextKlassModel.TAGS);
        List<Map<String, Object>> attributes = (List<Map<String, Object>>) addedContextKlassMap
            .get(IContextKlassModel.ATTRIBUTES);

        Vertex contextKlassNode = UtilClass.getVertexById(contextKlassId,
            VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
        if (UtilClass.fetchEdgeBetweenTwoNodes(klassNode, contextKlassNode, RelationshipLabelConstants.HAS_CONTEXT_KLASS)) {
          return;
        }
        klassNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_KLASS, contextKlassNode);
        Vertex contextNode = contextKlassNode
            .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
            .iterator()
            .next();
        String contextId = UtilClass.getCodeNew(contextNode);
        Map<String, Object> intermediateNodeMap = new HashMap<>();
        intermediateNodeMap.put(IContextKlassModel.CONTEXT_KLASS_ID, contextKlassId);
        intermediateNodeMap.put(CommonConstants.CONTEXT_ID, contextId);

        // for Bulk propagation
        addedContextKlassMap.put(IContextKlassModel.CONTEXT_ID, contextId);

        OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
            VertexLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES, new String());
        Vertex intermediateNode = UtilClass.createNode(intermediateNodeMap, vertexType, new ArrayList<>());
        klassNode.addEdge(RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK, intermediateNode);
        intermediateNode.addEdge(RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_KLASS_LINK, contextKlassNode);

        Map<String, Object> contextualDataTransferAsPerContextKlass = (Map<String, Object>) contextualDataTransfer
            .get(contextKlassId);
        if (contextualDataTransferAsPerContextKlass == null) {
          contextualDataTransferAsPerContextKlass = new HashMap<>();

          fillContextualDataTransferAsPerContextKlassMap(klassNode, contextKlassId,
              contextualDataTransferAsPerContextKlass);
          contextualDataTransfer.put(contextKlassId, contextualDataTransferAsPerContextKlass);
        }
        manageLinkAddedAttributeAndTag(attributes, tags, intermediateNode,
            contextualDataTransferAsPerContextKlass);
      }
    }
  }

  private static void manageLinkAddedProperties(List<Map<String, Object>> properties,
      Vertex intermediateNode, Map<String, Object> contextualDataTransferAsPerContextKlass,
      String entityLabel) throws Exception
  {
    if (!properties.isEmpty()) {
      for (Map<String, Object> property : properties) {
        String propertyId = (String) property.get(IIdAndCouplingTypeModel.ID);
        String couplingType = (String) property.get(IIdAndCouplingTypeModel.COUPLING_TYPE);
        Map<String, Object> propertyMap = new HashMap<>();
        propertyMap.put(IIdAndCouplingTypeModel.ID, propertyId);
        propertyMap.put(IIdAndCouplingTypeModel.COUPLING_TYPE, couplingType);
        Vertex propertyVertex = UtilClass.getVertexById(propertyId, entityLabel);
        fillAddedDataTransferProperty(entityLabel, contextualDataTransferAsPerContextKlass,
            propertyMap, propertyVertex);
        Edge edge = intermediateNode.addEdge(
            RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK,
            propertyVertex);
        edge.setProperty(IIdAndCouplingTypeModel.COUPLING_TYPE, couplingType);
      }
    }
  }

  private static void fillAddedDataTransferProperty(String entityLabel,
      Map<String, Object> contextualDataTransferAsPerContextKlass, Map<String, Object> propertyMap,
      Vertex propertyVertex)
  {
    List<Map<String, Object>> modifiedDepAtt = (List<Map<String, Object>>) contextualDataTransferAsPerContextKlass
        .get(ISideInfoForRelationshipDataTransferModel.MODIFIED_DEPENDENT_ATTRIBUTES);
    List<Map<String, Object>> modifiedIndepAtt = (List<Map<String, Object>>) contextualDataTransferAsPerContextKlass
        .get(ISideInfoForRelationshipDataTransferModel.MODIFIED_INDEPENDENT_ATTRIBUTES);
    List<Map<String, Object>> modifiedTag = (List<Map<String, Object>>) contextualDataTransferAsPerContextKlass
        .get(ISideInfoForRelationshipDataTransferModel.MODIFIED_TAGS);
    if (entityLabel.equals(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE)) {
      Boolean isTranslatable = propertyVertex.getProperty(IAttribute.IS_TRANSLATABLE);
      if (isTranslatable) {
        modifiedDepAtt.add(propertyMap);
      }
      else {
        modifiedIndepAtt.add(propertyMap);
      }
    }
    else {
      modifiedTag.add(propertyMap);
    }
  }

  public static void manageModifiedContextKlasses(Vertex klassNode,
      List<Map<String, Object>> modifiedContextKlasses,
      Map<String, Map<String, Object>> contextualDataTransfer) throws Exception
  {
    if (!modifiedContextKlasses.isEmpty()) {

      Map<String, Object> modifiedContextKlassesMap = getListAsMap(modifiedContextKlasses,
          IModifiedContextKlassModel.CONTEXT_KLASS_ID);
      Set<String> modifiedContextKlassesList = modifiedContextKlassesMap.keySet();

      Iterable<Vertex> vertices = klassNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK);
      for (Vertex intermediateNode : vertices) {
        String contextKlassId = intermediateNode
            .getProperty(IModifiedContextKlassModel.CONTEXT_KLASS_ID);
        if (modifiedContextKlassesList.contains(contextKlassId)) {
          Map<String, Object> contextualDataTransferAsPerContextKlass = (Map<String, Object>) contextualDataTransfer
              .get(contextKlassId);
          if (contextualDataTransferAsPerContextKlass == null) {
            contextualDataTransferAsPerContextKlass = new HashMap<>();
            fillContextualDataTransferAsPerContextKlassMap(klassNode, contextKlassId,
                contextualDataTransferAsPerContextKlass);

            contextualDataTransfer.put(contextKlassId, contextualDataTransferAsPerContextKlass);
          }
          manageLinkProperties(modifiedContextKlassesMap, intermediateNode, contextKlassId,
              contextualDataTransferAsPerContextKlass);
        }
      }
    }
  }

  private static void fillContextualDataTransferAsPerContextKlassMap(Vertex klassNode,
      String contextKlassId, Map<String, Object> contextualDataTransferAsPerContextKlass)
  {
    contextualDataTransferAsPerContextKlass
        .put(IContextInfoForContextualDataTransferModel.CONTEXT_KLASS_ID, contextKlassId);
    contextualDataTransferAsPerContextKlass.put(IContextInfoForContextualDataTransferModel.KLASS_ID,
        UtilClass.getCodeNew(klassNode));
    contextualDataTransferAsPerContextKlass.put(
        IContextInfoForContextualDataTransferModel.KLASS_TYPE,
        klassNode.getProperty(IKlassModel.TYPE));

    contextualDataTransferAsPerContextKlass.put(
        IContextInfoForContextualDataTransferModel.DELETED_DEPENDENT_ATTRIBUTES, new ArrayList<>());
    contextualDataTransferAsPerContextKlass.put(
        IContextInfoForContextualDataTransferModel.DELETED_INDEPENDENT_ATTRIBUTES,
        new ArrayList<>());
    contextualDataTransferAsPerContextKlass
        .put(IContextInfoForContextualDataTransferModel.DELETED_TAGS, new ArrayList<>());
    contextualDataTransferAsPerContextKlass.put(
        IContextInfoForContextualDataTransferModel.MODIFIED_DEPENDENT_ATTRIBUTES,
        new ArrayList<>());
    contextualDataTransferAsPerContextKlass.put(
        IContextInfoForContextualDataTransferModel.MODIFIED_INDEPENDENT_ATTRIBUTES,
        new ArrayList<>());
    contextualDataTransferAsPerContextKlass
        .put(IContextInfoForContextualDataTransferModel.MODIFIED_TAGS, new ArrayList<>());
  }

  private static void manageLinkProperties(Map<String, Object> modifiedContextKlassesMap,
      Vertex intermediateNode, String contextKlassId,
      Map<String, Object> contextualDataTransferAsPerContextKlass) throws Exception
  {
    Map<String, Object> modifiedContextKlassMap = (Map<String, Object>) modifiedContextKlassesMap
        .get(contextKlassId);
    List<Map<String, Object>> addedAttributes = (List<Map<String, Object>>) modifiedContextKlassMap
        .get(IModifiedContextKlassModel.ADDED_ATTRIBUTES);
    List<String> deletedAttributes = (List<String>) modifiedContextKlassMap
        .get(IModifiedContextKlassModel.DELETED_ATTRIBUTES);
    List<Map<String, Object>> modifiedAttributes = (List<Map<String, Object>>) modifiedContextKlassMap
        .get(IModifiedContextKlassModel.MODIFIED_ATTRIBUTES);
    List<Map<String, Object>> addedTags = (List<Map<String, Object>>) modifiedContextKlassMap
        .get(IModifiedContextKlassModel.ADDED_TAGS);
    List<String> deletedTags = (List<String>) modifiedContextKlassMap
        .get(IModifiedContextKlassModel.DELETED_TAGS);
    List<Map<String, Object>> modifiedTags = (List<Map<String, Object>>) modifiedContextKlassMap
        .get(IModifiedContextKlassModel.MODIFIED_TAGS);

    manageLinkAddedAttributeAndTag(addedAttributes, addedTags, intermediateNode,
        contextualDataTransferAsPerContextKlass);
    manageLinkDeletedAttributeAndTag(deletedAttributes, deletedTags, intermediateNode,
        contextualDataTransferAsPerContextKlass);
    manageLinkModifiedAttributeAndTag(modifiedAttributes, modifiedTags, intermediateNode,
        contextualDataTransferAsPerContextKlass);

    // for Bulk propagation
    modifiedContextKlassMap.put(IModifiedContextKlassModel.CONTEXT_ID,
        intermediateNode.getProperty(CommonConstants.CONTEXT_ID));
  }

  private static void manageLinkAddedAttributeAndTag(List<Map<String, Object>> addedAttributes,
      List<Map<String, Object>> addedTags, Vertex intermediateNode,
      Map<String, Object> contextualDataTransferAsPerContextKlass) throws Exception
  {

    manageLinkAddedProperties(addedAttributes, intermediateNode,
        contextualDataTransferAsPerContextKlass, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    manageLinkAddedProperties(addedTags, intermediateNode, contextualDataTransferAsPerContextKlass,
        VertexLabelConstants.ENTITY_TAG);
  }

  private static void manageLinkModifiedAttributeAndTag(
      List<Map<String, Object>> modifiedAttributesList, List<Map<String, Object>> modifiedTagsList,
      Vertex intermediateNode, Map<String, Object> contextualDataTransferAsPerContextKlass)
  {
    Map<String, Object> modifiedAttributesMap = getListAsMap(modifiedAttributesList,
        IIdAndCouplingTypeModel.ID);
    Set<String> modifiedAttributes = modifiedAttributesMap.keySet();

    Map<String, Object> modifiedTagMap = getListAsMap(modifiedTagsList, IIdAndCouplingTypeModel.ID);
    Set<String> modifiedTags = modifiedTagMap.keySet();

    if (!modifiedAttributes.isEmpty() || !modifiedTags.isEmpty()) {
      Iterable<Edge> edges = intermediateNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
      for (Edge edge : edges) {
        if (modifiedTags.isEmpty() && modifiedAttributes.isEmpty()) {
          break;
        }
        Vertex propertyVertex = edge.getVertex(Direction.IN);
        String propertyId = propertyVertex.getProperty(CommonConstants.CODE_PROPERTY);
        if (modifiedAttributes.contains(propertyId)) {
          Map<String, Object> propertyMap = changeCouplingType(modifiedAttributesMap, edge,
              propertyId);
          fillAddedDataTransferProperty(VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE,
              contextualDataTransferAsPerContextKlass, propertyMap, propertyVertex);
          modifiedAttributes.remove(propertyId);
        }
        else if (modifiedTags.contains(propertyId)) {
          Map<String, Object> propertyMap = changeCouplingType(modifiedTagMap, edge, propertyId);
          fillAddedDataTransferProperty(VertexLabelConstants.ENTITY_TAG,
              contextualDataTransferAsPerContextKlass, propertyMap, propertyVertex);
          modifiedTags.remove(propertyId);
        }
      }
    }
  }

  private static Map<String, Object> changeCouplingType(Map<String, Object> modifiedAttributesMap,
      Edge edge, String propertyId)
  {
    Map<String, Object> modifiedProperty = (Map<String, Object>) modifiedAttributesMap
        .get(propertyId);
    String couplingType = (String) modifiedProperty.get(IIdAndCouplingTypeModel.COUPLING_TYPE);
    edge.setProperty(IConflictingValueModel.COUPLING_TYPE, couplingType);

    Map<String, Object> propertyMap = new HashMap<>();
    propertyMap.put(IIdCodeCouplingTypeModel.ID, propertyId);
    propertyMap.put(IIdCodeCouplingTypeModel.COUPLING_TYPE, couplingType);

    return propertyMap;
  }

  private static void manageLinkDeletedAttributeAndTag(List<String> deletedAttributes,
      List<String> deletedTags, Vertex intermediateNode,
      Map<String, Object> contextualDataTransferAsPerContextKlass)
  {
    List<String> deletedContextDependentAttributes = (List<String>) contextualDataTransferAsPerContextKlass
        .get(IContextInfoForContextualDataTransferModel.DELETED_DEPENDENT_ATTRIBUTES);
    List<String> deletedContextIndependentAttributes = (List<String>) contextualDataTransferAsPerContextKlass
        .get(IContextInfoForContextualDataTransferModel.DELETED_INDEPENDENT_ATTRIBUTES);
    List<String> deletedContextTags = (List<String>) contextualDataTransferAsPerContextKlass
        .get(IContextInfoForContextualDataTransferModel.DELETED_TAGS);

    List<String> removeableDeletedAttributes = new ArrayList<>(deletedAttributes);
    List<String> removeableDeletedTags = new ArrayList<>(deletedTags);
    if (!removeableDeletedAttributes.isEmpty() || !deletedTags.isEmpty()) {
      Iterable<Edge> edges = intermediateNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.CONTEXTUAL_PROPAGABLE_PROPERTIES_PROPERTY_LINK);
      for (Edge edge : edges) {
        if (removeableDeletedAttributes.isEmpty() && deletedTags.isEmpty()) {
          break;
        }
        Vertex propertyVertex = edge.getVertex(Direction.IN);
        String propertyId = propertyVertex.getProperty(CommonConstants.CODE_PROPERTY);
        if (removeableDeletedAttributes.contains(propertyId)) {
          Boolean isTranslatable = (Boolean) propertyVertex.getProperty(IAttribute.IS_TRANSLATABLE);
          if (isTranslatable) {
            deletedContextDependentAttributes.add(propertyId);
          }
          else {
            deletedContextIndependentAttributes.add(propertyId);
          }
          edge.remove();
          removeableDeletedAttributes.remove(propertyId);
        }
        else if (removeableDeletedTags.contains(propertyId)) {
          deletedContextTags.add(propertyId);
          edge.remove();
          removeableDeletedTags.remove(propertyId);
        }
      }
    }
  }

  public static Map<String, Object> getListAsMap(
      List<Map<String, Object>> modifiedContextKlassesList, String key)
  {
    Map<String, Object> modifiedContextKlasses = new HashMap<>();
    modifiedContextKlassesList.forEach(modifiedContextKlass -> {
      String id = (String) modifiedContextKlass.get(key);
      if (id != null) {
        modifiedContextKlasses.put(id, modifiedContextKlass);
      }
    });
    return modifiedContextKlasses;
  }

  public static void manageDeletedContextKlasses(Vertex klassNode, List<String> deletedContextKlasses,
      Map<String, Map<String, Object>> contextualDataTransfer, List<Long> removedContextClassifierIIDs)
  {

    Iterable<Edge> edges = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Edge edge : edges) {
      Vertex contextKlass = edge.getVertex(Direction.IN);
      String contextKlassId = contextKlass.getProperty(CommonConstants.CODE_PROPERTY);
      if (deletedContextKlasses.contains(contextKlassId)) {
        long classifierIID = contextKlass.getProperty(ICategoryInformationModel.CLASSIFIER_IID);
        removedContextClassifierIIDs.add(classifierIID);
        edge.remove();
      }
    }

    Iterable<Edge> klassIntermediateLink = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.KLASS_CONTEXTUAL_PROPAGABLE_PROPERTIES_LINK);
    for (Edge edge : klassIntermediateLink) {
      Vertex intermediate = edge.getVertex(Direction.IN);
      String contextKlassId = intermediate.getProperty(IContextKlassModel.CONTEXT_KLASS_ID);
      if (deletedContextKlasses.contains(contextKlassId)) {
        intermediate.remove();
      }
    }
  }

  /**
   * @author Lokesh
   * @param klassADM
   * @param klassNode
   * @param taxonomyAndChildNodes
   * @throws Exception
   */
  public static void manageLinkedContextKlassesForAttributionTaxonomies(
      Map<String, Object> klassADM, Vertex klassNode, List<Vertex> taxonomyAndChildNodes)
      throws Exception
  {
    List<String> addedContextKlasses = (List<String>) klassADM
        .get(IProjectKlassSaveModel.ADDED_CONTEXT_KLASSES);
    List<String> deletedContextKlasses = (List<String>) klassADM
        .get(IProjectKlassSaveModel.DELETED_CONTEXT_KLASSES);

    if (addedContextKlasses.isEmpty() && deletedContextKlasses.isEmpty()) {
      return;
    }
    /*
    Vertex template = TemplateUtils.getTemplateFromKlassIfExist(klassNode);
    Vertex parentTemplate = null;
    if (template == null) {
      Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(new ArrayList<>(), klassNode);
      template = CreateKlassUtils.createDefaultTemplateNode(taxonomyMap, klassNode, "-1", TemplateUtils.getTemplateType(klassNode));
    }
    else {
      parentTemplate = template;
      Iterator<Edge> hasTemplateIterator = klassNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE).iterator();
      Edge hasTemplateEdge = hasTemplateIterator.next();
      Boolean isTemplateInherited = hasTemplateEdge.getProperty(CommonConstants.IS_INHERITED_PROPERTY);
      if (isTemplateInherited != null && isTemplateInherited) {
        hasTemplateEdge.remove();
        Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(new ArrayList<>(), klassNode);
        template = CreateKlassUtils.createDefaultTemplateNode(taxonomyMap, klassNode, "-1", CommonConstants.TAXONOMY_TEMPLATE, false);
        Edge newHasTemplateEdge = klassNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE, template);
        newHasTemplateEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);

        //KlassUtils.inheritParentPCInTemplate(template, parentTemplate);
        KlassUtils.inheritSequenceFromParent(template, parentTemplate, RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE);
        String type = ((OrientVertex) klassNode).getType().toString();
        if (type .equals(VertexLabelConstants.ATTRIBUTION_TAXONOMY)) {
          inheritParentRelationshipInTemplate(template, parentTemplate);
        }
        KlassUtils.inheritSequenceFromParent(template, parentTemplate, RelationshipLabelConstants.HAS_RELATIONSHIP_SEQUENCE);
        inheritParentContextInTemplate(template, parentTemplate);
      }
    }
    */

    // remove klass link with deletedContextKlasses and also remove already
    // linked klasses from addedContextKlasses
    Iterator<Edge> iterator = klassNode
        .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS)
        .iterator();
    while (iterator.hasNext()) {
      Edge hasContextKlass = iterator.next();
      Vertex contextKlass = hasContextKlass.getVertex(Direction.IN);
      String contextKlassId = contextKlass.getProperty(CommonConstants.CODE_PROPERTY);
      addedContextKlasses.remove(contextKlassId);
      if (deletedContextKlasses.contains(contextKlassId)) {
        hasContextKlass.remove();

        // Vertex templateNode =
        // TemplateUtils.getTemplateFromKlassIfExist(klassNode);
        // unlinkContextKlassFromTemplate(templateNode, contextKlassId);

        removeContextKlassFromInheritedTaxonomies(klassNode, deletedContextKlasses);
      }
    }

    for (String addedContextKlassId : addedContextKlasses) {
      Vertex unitKlassNode = UtilClass.getVertexById(addedContextKlassId,
          VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      Edge hasContextKlassEdge = klassNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_KLASS,
          unitKlassNode);
      hasContextKlassEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, false);

      // linkContextKlassWithKlassTemplate(unitKlassNode, template);
      inheritContextKlassInChildTaxonomies(klassNode, unitKlassNode);
    }
  }

  private static void removeContextKlassFromInheritedTaxonomies(Vertex taxonomyNode,
      List<String> deletedContextKlasses) throws Exception
  {
    Iterable<Vertex> childNodes = taxonomyNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childTaxonomyNode : childNodes) {
      Iterator<Edge> iterator = childTaxonomyNode
          .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_KLASS)
          .iterator();
      while (iterator.hasNext()) {
        Edge hasContextKlass = iterator.next();
        Vertex contextKlass = hasContextKlass.getVertex(Direction.IN);
        String contextKlassId = contextKlass.getProperty(CommonConstants.CODE_PROPERTY);
        if (!deletedContextKlasses.contains(contextKlassId)) {
          continue;
        }

        if ((Boolean) hasContextKlass.getProperty(CommonConstants.IS_INHERITED_PROPERTY)) {
          hasContextKlass.remove();
          /*
          Vertex templateNode = TemplateUtils.getTemplateFromKlassIfExist(taxonomyNode);
          unlinkContextKlassFromTemplate(templateNode, contextKlassId);
          */
          removeContextKlassFromInheritedTaxonomies(childTaxonomyNode, deletedContextKlasses);
        }
      }
    }
  }

  /**
   * inherit parent property collection in template
   *
   * @author Lokesh
   * @param childTemplateNode
   * @param parentTemplateNode
   */
  public static void inheritParentContextInTemplate(Vertex childTemplateNode,
      Vertex parentTemplateNode)
  {
    Iterable<Vertex> iterable = parentTemplateNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Vertex pCNode : iterable) {
      Edge hasTemplatePC = childTemplateNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_KLASS,
          pCNode);
      hasTemplatePC.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
    }
  }

  /**
   * @author Aayush
   * @param taxonomyNode
   * @param contextKlassNode
   * @throws Exception
   */
  @Deprecated
  private static void inheritContextKlassInChildTaxonomies(Vertex taxonomyNode,
      Vertex contextKlassNode, Vertex taxonomyTemplate, Vertex parentTemplate) throws Exception
  {
    String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
    Iterable<Vertex> childNodes = taxonomyNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childTaxonomyNode : childNodes) {

      // check if context already exist
      if (checkIfContextKlassNodeExist(UtilClass.getCodeNew(childTaxonomyNode), contextKlassId)) {
        continue;
      }

      /*Vertex template = TemplateUtils.getTemplateFromKlassIfExist(childTaxonomyNode);
            if (template == null) {
              Edge hasTemplateEdge = childTaxonomyNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE, taxonomyTemplate);
              hasTemplateEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
            } else {
              String parentTemplateId = null;
              if (parentTemplate != null) {
                parentTemplateId = UtilClass.getCode(parentTemplate);
              }

              if (taxonomyTemplate != parentTemplate && UtilClass.getCode(template).equals(parentTemplateId)) {
                Iterator<Edge> hasTemplateIterator = childTaxonomyNode
                    .getEdges(Direction.OUT, RelationshipLabelConstants.HAS_TEMPLATE).iterator();
                Edge hasTemplateEdgeInheritedFromParent = hasTemplateIterator.next();
                hasTemplateEdgeInheritedFromParent.remove();

                Edge hasTemplateEdge = childTaxonomyNode.addEdge(RelationshipLabelConstants.HAS_TEMPLATE, taxonomyTemplate);
                hasTemplateEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);
              }
            }
      */
      Edge hasContextKlassEdge = childTaxonomyNode
          .addEdge(RelationshipLabelConstants.HAS_CONTEXT_KLASS, contextKlassNode);
      hasContextKlassEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);

      inheritContextKlassInChildTaxonomies(childTaxonomyNode, contextKlassNode, taxonomyTemplate,
          parentTemplate);
    }
  }

  /**
   * @author Aayush
   * @param taxonomyNode
   * @param contextKlassNode
   * @throws Exception
   */
  private static void inheritContextKlassInChildTaxonomies(Vertex taxonomyNode,
      Vertex contextKlassNode) throws Exception
  {
    String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
    Iterable<Vertex> childNodes = taxonomyNode.getVertices(Direction.IN,
        RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex childTaxonomyNode : childNodes) {

      // check if context already exist
      if (checkIfContextKlassNodeExist(UtilClass.getCodeNew(childTaxonomyNode), contextKlassId)) {
        continue;
      }

      Edge hasContextKlassEdge = childTaxonomyNode
          .addEdge(RelationshipLabelConstants.HAS_CONTEXT_KLASS, contextKlassNode);
      hasContextKlassEdge.setProperty(CommonConstants.IS_INHERITED_PROPERTY, true);

      inheritContextKlassInChildTaxonomies(childTaxonomyNode, contextKlassNode);
    }
  }

  public static boolean checkIfContextKlassNodeExist(String taxonomyId, Object contextKlassId)
  {
    /*
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS + " where in('"
        + RelationshipLabelConstants.HAS_CONTEXT_KLASS + "') contains (code='" + taxonomyId + "') and out('"
        + RelationshipLabelConstants.HAS_CONTEXT_KLASS + "') contains (code='" + contextKlassId + "')";
    */
    String query = "select from " + RelationshipLabelConstants.HAS_CONTEXT_KLASS
        + " where out.code='" + taxonomyId + "' and in.code='" + contextKlassId + "'";

    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    Iterator<Vertex> iterator = vertices.iterator();
    if (iterator.hasNext()) {
      return true;
    }

    return false;
  }

  /*public static void unlinkContextKlassFromTemplate(Vertex templateNode, String contextKlassId)
  {
    // unlink from template..
    Set<Edge> edgesToRemove = new HashSet<Edge>();
    Iterable<Edge> hasTemplateContextKlassIterable = templateNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_KLASS);
    for (Edge hasTemplateContextKlass : hasTemplateContextKlassIterable) {
      Vertex contextKlassNode = hasTemplateContextKlass.getVertex(Direction.IN);
      if (UtilClass.getCode(contextKlassNode)
          .equals(contextKlassId)) {
        edgesToRemove.add(hasTemplateContextKlass);
      }
    }
    for (Edge edge : edgesToRemove) {
      edge.remove();
    }
  }*/

  public static Map<String, Object> getContext(Vertex contextNode,
      Map<String, Object> referencedTags) throws Exception
  {
    Map<String, Object> returnContextMap = UtilClass.getMapFromVertex(new ArrayList<>(),
        contextNode);
    List<Map<String, Object>> contextTagsList = new ArrayList<>();
    Map<String, Object> contextTagMapToSort = new HashMap<String, Object>();

    /*returnContextMap.put(IGetVariantContextModel.ID, UtilClass.getCode(contextNode));
    returnContextMap.put(IGetVariantContextModel.LABEL,
        contextNode.getProperty(IGetVariantContextModel.LABEL));*/

    List<String> tags = new ArrayList<>();

    Iterable<Vertex> i = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.ENTITY_TAG
            + " where " + ITag.IS_ROOT + " = true order by "
            + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc"))
        .execute();

    for (Vertex tagNode : i) {
      tags.add(tagNode.getProperty(CommonConstants.CODE_PROPERTY)
          .toString());
    }

    Iterable<Vertex> contextTags = contextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    for (Vertex contextTag : contextTags) {
      Vertex tagNode = null;
      Iterator<Vertex> tagNodes = contextTag
          .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY)
          .iterator();
      if (tagNodes.hasNext()) {
        tagNode = tagNodes.next();
      }
      else {
        continue;
      }
      String tagId = UtilClass.getCodeNew(tagNode);

      Map<String, Object> contextTagMap = prepareContextMapAndFillReferencedTags(tagNode,
          contextTag, referencedTags);

      contextTagMapToSort.put(tagId, contextTagMap);
      // contextTagsList.add(contextTagMap);
    }
    for (String tag : tags) {
      Map<String, Object> contextTagMap = (Map<String, Object>) contextTagMapToSort.get(tag);
      if (contextTagMap != null) {
        contextTagsList.add(contextTagMap);
      }
    }
    returnContextMap.put(IGetVariantContextModel.CONTEXT_TAGS, contextTagsList);

    // addEditablePropertiesToContextMap(returnContextMap, contextNode);
    KlassUtils.addSectionsToKlassEntityMap(contextNode, returnContextMap);

    List<String> subContexts = new ArrayList<String>();
    Iterable<Vertex> contextVertices = contextNode.getVertices(Direction.IN,
        RelationshipLabelConstants.SUB_CONTEXT_OF);
    for (Vertex subContext : contextVertices) {
      subContexts.add(subContext.getProperty(CommonConstants.CODE_PROPERTY));
    }
    returnContextMap.put(IGetVariantContextModel.SUB_CONTEXTS, subContexts);

    fillUniqueSelections(contextNode, returnContextMap);
    return returnContextMap;
  }

  public static Map<String, Object> prepareContextMapAndFillReferencedTags(Vertex tagNode,
      Vertex contextTag, Map<String, Object> referencedTags) throws Exception
  {
    Map<String, Object> contextTagMap = new HashMap<String, Object>();

    String tagId = UtilClass.getCodeNew(tagNode);
    contextTagMap.put(ITagAndTagValuesIds.ID, tagId);

    Iterable<Vertex> contextTagValueNodes = contextTag.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE);

    List<String> selectedTagValueIds = new ArrayList<>();
    for (Vertex contextTagValueNode : contextTagValueNodes) {
      selectedTagValueIds.add(UtilClass.getCodeNew(contextTagValueNode));
    }

    contextTagMap.put(ITagAndTagValuesIds.TAG_VALUE_IDS, selectedTagValueIds);
    referencedTags.put(tagId, TagUtils.getTagMap(tagNode, true));
    return contextTagMap;
  }

  /**
   * Description : fetched unique selector values saved for this context and
   * fills it inn return map..
   *
   * @author Ajit
   * @param contextNode
   * @param returnContextMap
   */
  public static void fillUniqueSelections(Vertex contextNode, Map<String, Object> returnContextMap)
  {
    Iterable<Vertex> uniqueSelectors = contextNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_UNIQUE_SELECTOR);
    List<Map<String, Object>> uniqueSelections = new ArrayList<>();
    returnContextMap.put(IGetVariantContextModel.UNIQUE_SELECTORS, uniqueSelections);

    for (Vertex uniqueSelectorNode : uniqueSelectors) {
      Iterable<Vertex> uniqueTagProperties = uniqueSelectorNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_UNIQUE_TAG_PROPERTY);
      Map<String, Object> uniqueSelectionMap = new HashMap<>();
      String uniqueSelectorId = UtilClass.getCodeNew(uniqueSelectorNode);
      List<Map<String, Object>> selectionValues = new ArrayList<>();
      uniqueSelectionMap.put(IUniqueSelectorModel.ID, uniqueSelectorId);
      uniqueSelectionMap.put(IUniqueSelectorModel.SELECTION_VALUES, selectionValues);
      uniqueSelections.add(uniqueSelectionMap);
      for (Vertex uniqueTagPropertyNode : uniqueTagProperties) {

        // populate tag
        Iterable<Vertex> uniqueTags = uniqueTagPropertyNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_UNIQUE_TAG);
        Map<String, Object> tagMap = new HashMap<>();
        selectionValues.add(tagMap);
        List<Map<String, Object>> tagValues = new ArrayList<>();
        tagMap.put(IVariantContextTagModel.TAG_VALUES, tagValues);
        for (Vertex tagNode : uniqueTags) {
          String label = (String) UtilClass.getValueByLanguage(tagNode,
              CommonConstants.LABEL_PROPERTY);
          String tagId = UtilClass.getCodeNew(tagNode);
          tagMap.put(IVariantContextTagModel.TAG_ID, tagId);
          tagMap.put(IVariantContextTagModel.LABEL, label);
        }

        // populate tag values
        Iterable<Vertex> uniqueTagValues = uniqueTagPropertyNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_UNIQUE_TAG_VALUE);
        for (Vertex tagValueNode : uniqueTagValues) {
          String tagValueLabel = (String) UtilClass.getValueByLanguage(tagValueNode,
              CommonConstants.LABEL_PROPERTY);
          String tagValueId = UtilClass.getCodeNew(tagValueNode);
          Map<String, Object> tagValueMap = new HashMap<>();
          tagValueMap.put(IVariantContextTagValuesModel.TAG_VALUE_ID, tagValueId);
          tagValueMap.put(IVariantContextTagValuesModel.LABEL, tagValueLabel);
          tagValues.add(tagValueMap);
        }
      }
    }
  }

  @Deprecated
  static class PropertiesSequenceComparator implements Comparator<Map<String, Object>> {

    @Override
    public int compare(Map<String, Object> property1, Map<String, Object> property2)
    {
      Integer property1Sequence = (Integer) property1.get(CommonConstants.SEQUENCE);
      Integer property2Sequence = (Integer) property2.get(CommonConstants.SEQUENCE);
      return property1Sequence - property2Sequence;
    }
  }

  @Deprecated
  /**
   * Deprecated because no one uses it
   *
   * @param contextId
   * @return
   * @throws Exception
   */
  public static Boolean getIsDuplicateAllowedForVariant(String contextId) throws Exception
  {
    Vertex contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    return contextNode.getProperty(IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED);
  }

  public static void manageModifiedContextTag(Vertex contextNode,
      List<Map<String, Object>> modifiedTags) throws Exception
  {
    Map<String, Object> addedTagValuesMap = new HashMap<>();
    Map<String, Object> deletedTagValuesMap = new HashMap<>();

    for (Map<String, Object> modifiedTag : modifiedTags) {
      String tagId = (String) modifiedTag.get(IVariantContextModifiedTagsModel.TAG_ID);
      List<String> addedTagValuesList = (List<String>) modifiedTag
          .get(IVariantContextModifiedTagsModel.ADDED_TAG_VALUE_IDS);
      if (addedTagValuesList.size() > 0) {
        addedTagValuesMap.put(tagId, addedTagValuesList);
      }
      List<String> deletedTagValuesList = (List<String>) modifiedTag
          .get(IVariantContextModifiedTagsModel.DELETED_TAG_VALUE_IDS);

      if (deletedTagValuesList.size() > 0) {
        deletedTagValuesMap.put(tagId, deletedTagValuesList);
      }
    }

    handleModifiedTags(contextNode, addedTagValuesMap, deletedTagValuesMap);
  }

  private static void handleModifiedTags(Vertex contextNode, Map<String, Object> addedTagValues,
      Map<String, Object> deletedTagValues) throws Exception
  {
    Iterable<Edge> contextNodeEdges = contextNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_CONTEXT_TAG);
    for (Edge contextNodeEdge : contextNodeEdges) {
      String tagId = contextNodeEdge.getProperty(IVariantContextTagModel.TAG_ID);
      List<String> addedTagValueIds = (List<String>) addedTagValues.get(tagId);
      if (addedTagValueIds != null) {
        Vertex contextTagNode = contextNodeEdge.getVertex(Direction.IN);
        for (String tagValueId : addedTagValueIds) {
          Iterator<Vertex> iterator = UtilClass.getGraph()
              .getVertices(VertexLabelConstants.ENTITY_TAG,
                  new String[] { CommonConstants.CODE_PROPERTY }, new Object[] { tagValueId })
              .iterator();
          try {
            Vertex tagValueNode = UtilClass.getVertexByIndexedId(tagValueId,
                VertexLabelConstants.ENTITY_TAG);
            Edge contextTagValueNodeEdge = contextTagNode
                .addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE, tagValueNode);
            contextTagValueNodeEdge.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID,
                UtilClass.getCodeNew(tagValueNode));
          }
          catch (NotFoundException e) {
            throw new TagNotFoundException();
          }
        }
      }

      List<String> deletedTagValueIds = (List<String>) deletedTagValues.get(tagId);
      if (deletedTagValueIds == null || deletedTagValueIds.isEmpty()) {
        continue;
      }

      Vertex variantContextTag = contextNodeEdge.getVertex(Direction.IN);
      String query = "select expand(outE('" + RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE
          + "')[" + IVariantContextTagValuesModel.TAG_VALUE_ID + " in "
          + EntityUtil.quoteIt(deletedTagValueIds) + "]) from " + variantContextTag.getId();

      Iterable<Edge> tagValueEdges = UtilClass.getGraph()
          .command(new OCommandSQL(query))
          .execute();
      for (Edge tagValueEdge : tagValueEdges) {
        tagValueEdge.remove();
      }
    }
  }

  public static Vertex createDefaultContextNode(String contextType) throws Exception
  {
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
    Map<String, Object> contextMap = new HashMap<String, Object>();
    contextMap.put(IVariantContext.ID, UtilClass.getUniqueSequenceId(vertexType));
    contextMap.put(IVariantContext.TYPE, contextType);
    contextMap.put(IVariantContext.IS_AUTO_CREATE, false);
    contextMap.put(IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED, true);
    contextMap.put(IVariantContext.IS_TIME_ENABLED, false);

    Vertex unitContextNode = UtilClass.createNode(contextMap, vertexType, new ArrayList<String>());
    return unitContextNode;
  }

  public static Vertex createDefaultContextNode(String contextType, String contextId) throws Exception
  {
    Map<String, Object> contextMap = new HashMap<String, Object>();
    contextMap.put(IVariantContext.ID, contextId);
    contextMap.put(IVariantContext.TYPE, contextType);
    contextMap.put(IVariantContext.IS_AUTO_CREATE, false);
    contextMap.put(IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED, true);
    contextMap.put(IVariantContext.IS_TIME_ENABLED, false);

    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
    Vertex unitContextNode = UtilClass.createNode(contextMap, vertexType, new ArrayList<String>());
    return unitContextNode;
  }

  public static Vertex createDefaultContextNode(String contextType, String contextId,
      String contextCode) throws Exception
  {
    Map<String, Object> contextMap = new HashMap<String, Object>();
    contextMap.put(IVariantContext.ID, contextId);
    contextMap.put(IVariantContext.TYPE, contextType);
    contextMap.put(IVariantContext.IS_AUTO_CREATE, false);
    contextMap.put(IVariantContext.IS_DUPLICATE_VARIANT_ALLOWED, true);
    contextMap.put(IVariantContext.IS_TIME_ENABLED, false);
    contextMap.put(IVariantContext.CODE, contextCode);

    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
        VertexLabelConstants.VARIANT_CONTEXT, CommonConstants.CODE_PROPERTY);
    Vertex unitContextNode = UtilClass.createNode(contextMap, vertexType, new ArrayList<String>());
    return unitContextNode;
  }

  /**
   * @param contextNode
   * @param contextMap
   */
  public static void manageEntities(Vertex contextNode, Map<String, Object> contextMap)
  {
    List<String> entities = contextNode.getProperty(IVariantContext.ENTITIES);
    if (entities == null) {
      entities = new ArrayList<String>();
    }
    entities.addAll((List<String>) contextMap.get(ISaveVariantContextModel.ADDED_ENTITIES));
    entities.removeAll((List<String>) contextMap.get(ISaveVariantContextModel.DELETED_ENTITIES));
    contextMap.put(IVariantContext.ENTITIES, entities);
  }

  public static void handleAddedTags(Vertex contextNode, Map<String, Object> addedTags)
      throws Exception
  {
    for (String tagId : addedTags.keySet()) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.VARIANT_CONTEXT_TAG, CommonConstants.CODE_PROPERTY);
      Vertex tagNode = null;
      try {
        tagNode = UtilClass.getVertexByIndexedId(tagId, VertexLabelConstants.ENTITY_TAG);
      }
      catch (NotFoundException e) {
        throw new TagNotFoundException();
      }
      Vertex contextTagNode = UtilClass.createNode(new HashMap<String, Object>(), vertexType,
          new ArrayList<>());
      Edge contextTagNodeEdge = contextNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG,
          contextTagNode);
      contextTagNodeEdge.setProperty(IVariantContextTagModel.TAG_ID, UtilClass.getCodeNew(tagNode));

      contextTagNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY, tagNode);
      List<String> tagValueIds = (List<String>) addedTags.get(tagId);
      if (tagValueIds == null) {
        continue;
      }
      for (String tagValueId : tagValueIds) {
        try {
          Vertex tagValueNode = UtilClass.getVertexByIndexedId(tagValueId,
              VertexLabelConstants.ENTITY_TAG);
          Edge contextTagValueNodeEdge = contextTagNode
              .addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE, tagValueNode);
          contextTagValueNodeEdge.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID,
              UtilClass.getCodeNew(tagValueNode));
        }
        catch (NotFoundException e) {
          throw new TagNotFoundException();
        }
      }
    }
  }

  public static void handleAddedTagsForPromotionContext(Vertex contextNode,
      List<String> addedTagIds) throws Exception
  {
    for (String tagId : addedTagIds) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(
          VertexLabelConstants.VARIANT_CONTEXT_TAG, CommonConstants.CODE_PROPERTY);

      Vertex tagNode = UtilClass.getVertexById(tagId, VertexLabelConstants.ENTITY_TAG);
      Vertex contextTagNode = UtilClass.createNode(new HashMap<String, Object>(), vertexType,
          new ArrayList<>());
      Edge contextTagNodeEdge = contextNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG,
          contextTagNode);
      contextTagNodeEdge.setProperty(IVariantContextTagModel.TAG_ID, UtilClass.getCodeNew(tagNode));
      contextTagNode.addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_PROPERTY, tagNode);

      Iterable<Vertex> tagValueNodes = tagNode.getVertices(Direction.IN,
          RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
      for (Vertex tagValueNode : tagValueNodes) {
        Edge contextTagValueNodeEdge = contextTagNode
            .addEdge(RelationshipLabelConstants.HAS_CONTEXT_TAG_VALUE, tagValueNode);
        contextTagValueNodeEdge.setProperty(IVariantContextTagValuesModel.TAG_VALUE_ID,
            UtilClass.getCodeNew(tagValueNode));
      }
    }
  }

  /**
   * create default context for contextual klass(embedded only)
   *
   * @author Lokesh
   * @param klassMap
   * @param klassNode
   * @throws Exception
   */
  public static void manageContextKlasses(Map<String, Object> klassMap, Vertex klassNode)
      throws Exception
  {
    String klassType = (String) klassMap.get(IProjectKlass.NATURE_TYPE);
    // for non-nature klasses
    if (klassType == null) {
      return;
    }
    if (klassType.equals(CommonConstants.EMBEDDED_KLASS_TYPE)) {
      KlassUtils.createContextNodeForKlass(klassNode, CommonConstants.CONTEXTUAL_VARIANT,
          (String) klassMap.get(IVariantContext.CODE));
    }
    else if (klassType.equals(CommonConstants.GTIN_KLASS_TYPE)) {
      KlassUtils.createContextNodeForKlass(klassNode, CommonConstants.GTIN_VARIANT,
          (String) klassMap.get(IVariantContext.CODE));
    }
    else if (klassType.equals(CommonConstants.LANGUAGE_KLASS_TYPE)) {
      KlassUtils.createContextNodeForKlass(klassNode,
          CommonConstants.LANGUAGE_VARIANT);
    }
  }

  /**
   * @author Niraj
   * @param klassADM
   * @param klassNode
   * @param contextualDataTransfer
   * @throws Exception
   */
  public static void manageLinkedLanguageKlass(Map<String, Object> klassADM, Vertex klassNode,
      Map<String, Map<String, Object>> contextualDataTransfer) throws Exception
  {
    Map<String, Object> addedLanguageKlass = (Map<String, Object>) klassADM
        .get(IProjectKlassSaveModel.ADDED_LANGUAGE_KLASS);
    String deletedLanguageKlass = (String) klassADM
        .get(IProjectKlassSaveModel.DELETED_LANGUAGE_KLASS);
    Map<String, Object> modifiedLanguageKlass = (Map<String, Object>) klassADM
        .get(IProjectKlassSaveModel.MODIFIED_LANGUAGE_KLASS);

    List<Map<String, Object>> addedLanguageKlassList = new ArrayList<>();
    if (addedLanguageKlass != null && !addedLanguageKlass.isEmpty()) {
      addedLanguageKlassList.add(addedLanguageKlass);
    }

    List<String> deletedLanguageKlassList = new ArrayList<>();
    if (deletedLanguageKlass != null && !deletedLanguageKlass.isEmpty()) {
      deletedLanguageKlassList.add(deletedLanguageKlass);
    }

    List<Map<String, Object>> modifiedLanguageKlassList = new ArrayList<>();
    if (modifiedLanguageKlass != null && !modifiedLanguageKlass.isEmpty()) {
      modifiedLanguageKlassList.add(modifiedLanguageKlass);
    }

    VariantContextUtils.manageLinkedContextKlasses(klassADM, klassNode, addedLanguageKlassList,
        modifiedLanguageKlassList, deletedLanguageKlassList, contextualDataTransfer, new ArrayList<>());
  }

  public static Vertex getContextNodeFromKlassNode(Vertex klassNode)
      throws MultipleLinkFoundException
  {
    Iterator<Vertex> contextNodeIterator = klassNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    if (!contextNodeIterator.hasNext()) {
      return null;
    }
    Vertex contextNode = contextNodeIterator.next();
    if (contextNodeIterator.hasNext()) {
      throw new MultipleLinkFoundException();
    }
    return contextNode;
  }

  public static Vertex getKlassNodeFromContextNode(String baseType,
      Iterator<Vertex> contextKlassIterator) throws KlassNotFoundException
  {
    String klassType = EntityUtil.getKlassType(baseType);
    Vertex klassNode = null;
    while (contextKlassIterator.hasNext()) {
      klassNode = contextKlassIterator.next();
      if (klassType.equals(klassNode.getProperty(IKlass.TYPE))) {
        return klassNode;
      }
    }
    throw new KlassNotFoundException();
  }

  public static Map<String, Object> getContextMapToReturn(Vertex contextNode) throws Exception
  {
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IConfigDetailsForGetVariantContextModel.REFERENCED_TAGS, referencedTags);
    Map<String, Object> returnMap = VariantContextUtils.getContext(contextNode, referencedTags);
    Map<String, Object> responseTabMap = TabUtils.getMapFromConnectedTabNode(contextNode,
        Arrays.asList(CommonConstants.CODE_PROPERTY, IIdLabelModel.LABEL,
            CommonConstants.CODE_PROPERTY));
    if (responseTabMap != null) {
      String tabId = (String) responseTabMap.get(IIdLabelModel.ID);
      returnMap.put(IGetVariantContextModel.TAB_ID, tabId);
      Map<String, Object> referencedTab = new HashMap<>();
      referencedTab.put(tabId, responseTabMap);
      configDetails.put(IConfigDetailsForGetVariantContextModel.REFERENCED_TABS, referencedTab);
    }
    returnMap.put(IGetVariantContextModel.CONFIG_DETAILS, configDetails);
    return returnMap;
  }

  public static void manageDeleteTagIds(Vertex contextNode, List<String> deletedTagIds)
  {
    if (deletedTagIds.size() > 0) {
      Iterable<Edge> contextNodeEdges = contextNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.HAS_CONTEXT_TAG);
      for (Edge contextNodeEdge : contextNodeEdges) {
        String tagId = contextNodeEdge.getProperty(IVariantContextTagModel.TAG_ID);
        if (deletedTagIds.contains(tagId)) {
          Vertex contextTagNode = contextNodeEdge.getVertex(Direction.IN);
          contextTagNode.remove();
        }
      }
    }
  }

  public static Map<String, Object> getValidEmbeddedClasses(Set<String> klassCodes) throws Exception
  {
    Map<String, Object> validEmbeddedClass = new HashMap<>();
    for(String klassCode : klassCodes) {
      Vertex vertex = UtilClass.getVertexByCode(klassCode, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
      String natureType = vertex.getProperty("natureType");
      if (natureType.equals(CommonConstants.EMBEDDED_KLASS_TYPE) || natureType.equals(
          CommonConstants.GTIN_KLASS_TYPE) || natureType.equals(CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE)) {
        validEmbeddedClass.put(vertex.getProperty(CommonConstants.CODE_PROPERTY), UtilClass.getMapFromNode(vertex));
      }
    }
    return validEmbeddedClass;
  }
}
