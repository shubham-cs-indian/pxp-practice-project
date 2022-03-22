package com.cs.config.strategy.plugin.usecase.processevent.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.exception.NotFoundException;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.springframework.util.CollectionUtils;

import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.user.IUser;
import com.cs.core.config.interactor.exception.task.ProcessModifiedDeletedException;
import com.cs.core.config.interactor.model.processevent.IGetConfigDetailsModelForProcess;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.ISearchComponentAttributeInfoModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.di.workflow.tasks.SetAttributesAndTagsTask;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.WorkflowConstants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings({ "unchecked" })
public class ProcessEventUtils {
  
  public static final List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IConfigEntityInformationModel.TYPE, IConfigEntityInformationModel.LABEL,
      IConfigEntityInformationModel.CODE, IConfigEntityInformationModel.ICON);
  
  public static final List<String> ALLOWED_PROPETIES_FOR_WF_SAVE    = Arrays.asList(CommonConstants.NATURE_TYPE, CommonConstants.ATTRIBUTE,
      CommonConstants.TAG, CommonConstants.NON_NATURE_TYPE, CommonConstants.TAXONOMY);
  public static final List<String> ALLOWED_PROPETIES_FOR_WF_CREATE  = Arrays.asList(CommonConstants.NATURE_TYPE);
  
  public static Map<String, Object> getReferencedEntity(String entityId, String vertexLabel)
      throws Exception
  {
    Vertex entityNode;
    try {
      entityNode = UtilClass.getVertexById(entityId, vertexLabel);
    }
    catch (Exception e) {
      throw e;
    }
    return UtilClass.getMapFromVertex(fieldsToFetch, entityNode);
  }
  
  public static void saveProcessReferencedElements(Vertex processEventNode,
      Map<String, Object> processEventMap) throws Exception
  {
    List<String> allowedProperties = getAllowedProperties(processEventNode);
    
    List<String> addedKlasses = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_KLASS_IDS);
    List<String> deletedKlasses = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_KLASS_IDS);
    
    List<String> addedNonNatureKlasses = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_NON_NATURE_KLASS_IDS);
    List<String> deletedNonNatureKlasses = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_NON_NATURE_KLASS_IDS);
    
    List<String> addedAttributes = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_ATTRIBUTE_IDS);
    List<String> deletedAttributes = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_ATTRIBUTE_IDS);
    
    List<String> addedTags = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_TAG_IDS);
    List<String> deletedTags = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_TAG_IDS);
    
    List<String> addedTaxonomiesForEvent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_TAXONOMY_IDS);
    List<String> deletedTaxonomiesForEvent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_TAXONOMY_IDS);
    
    for (String property : allowedProperties) {
      switch (property) {
        case CommonConstants.NATURE_TYPE:
          // Nature klasses
          linkEntityToProcess(addedKlasses, processEventNode, RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_PROCESS,
              VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
          unlinkEntityToProcess(deletedKlasses, processEventNode, RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_PROCESS);
          break;
        
        case CommonConstants.NON_NATURE_TYPE:
          // Non nature klasses
          linkEntityToProcess(addedNonNatureKlasses, processEventNode, RelationshipLabelConstants.HAS_LINKED_NON_NATURE_KLASSES_FOR_PROCESS,
              VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
          unlinkEntityToProcess(deletedNonNatureKlasses, processEventNode,
              RelationshipLabelConstants.HAS_LINKED_NON_NATURE_KLASSES_FOR_PROCESS);
          break;
        
        case CommonConstants.ATTRIBUTE:
          linkEntityToProcess(addedAttributes, processEventNode, RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_PROCESS,
              VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
          unlinkEntityToProcess(deletedAttributes, processEventNode, RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_PROCESS);
          break;
        
        case CommonConstants.TAG:
          linkEntityToProcess(addedTags, processEventNode, RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_PROCESS,
              VertexLabelConstants.ENTITY_TAG);
          unlinkEntityToProcess(deletedTags, processEventNode, RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_PROCESS);
          break;
        
        case CommonConstants.TAXONOMY:
          linkEntityToProcess(addedTaxonomiesForEvent, processEventNode, RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_PROCESS,
              VertexLabelConstants.ROOT_KLASS_TAXONOMY);
          unlinkEntityToProcess(deletedTaxonomiesForEvent, processEventNode, RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_PROCESS);
          break;
        
        default:
          break;
      }
    }
    
    List<String> addedUsers = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_USERS);
    List<String> deletedUsers = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_USERS);
    
    linkEntityToProcess(addedUsers, processEventNode, RelationshipLabelConstants.HAS_LINKED_USERS,
        VertexLabelConstants.ENTITY_TYPE_USER);
    unlinkEntityToProcess(deletedUsers, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_USERS);
    
    List<String> addedOrganizations = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_ORGANIZATIONS);
    List<String> deletedOrganizations = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_ORGANIZATIONS);
    
    linkEntityToProcess(addedOrganizations, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ORGANIZATIONS, VertexLabelConstants.ORGANIZATION);
    unlinkEntityToProcess(deletedOrganizations, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ORGANIZATIONS);
    
    List<String> addedTasks = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_TASKS);
    List<String> deletedTasks = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_TASKS);
    
    linkEntityToProcess(addedTasks, processEventNode, RelationshipLabelConstants.HAS_LINKED_TASKS,
        VertexLabelConstants.ENTITY_TYPE_TASK);
    unlinkEntityToProcess(deletedTasks, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_TASKS);
    
    List<String> addedContexts = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_CONTEXTS);
    List<String> deletedContexts = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_CONTEXTS);
    
    linkEntityToProcess(addedContexts, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_CONTEXTS, VertexLabelConstants.VARIANT_CONTEXT);
    unlinkEntityToProcess(deletedContexts, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_CONTEXTS);
    
    List<String> addedRelationships = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_RELATIONSHIPS);
    List<String> deletedRelationships = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_RELATIONSHIPS);
    
    linkEntityToProcess(addedRelationships, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_RELATIONSHIPS,
        VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP);
    unlinkEntityToProcess(deletedRelationships, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_RELATIONSHIPS);
    
    List<String> addedKlassesForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_KLASS_IDS_FOR_COMPONENT);
    List<String> deletedKlassesForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_KLASS_IDS_FOR_COMPONENT);
    
    linkEntityToProcess(addedKlassesForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_COMPONENT,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    unlinkEntityToProcess(deletedKlassesForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_COMPONENT);
    
    List<String> addedTaxonomiesForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_TAXONOMY_IDS_FOR_COMPONENT);
    List<String> deletedTaxonomiesForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_TAXONOMY_IDS_FOR_COMPONENT);
    
    linkEntityToProcess(addedTaxonomiesForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_COMPONENT,
        VertexLabelConstants.ROOT_KLASS_TAXONOMY);
    unlinkEntityToProcess(deletedTaxonomiesForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_COMPONENT);
    
    List<String> addedAttributesForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_ATTRIBUTE_IDS_FOR_COMPONENT);
    List<String> deletedAttributesForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_ATTRIBUTE_IDS_FOR_COMPONENT);
    
    linkEntityToProcess(addedAttributesForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES,
        VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    unlinkEntityToProcess(deletedAttributesForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES);
    
    List<String> rawAddedTagsForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_TAG_IDS_FOR_COMPONENT);
    List<String> addedTagsForComponent = new ArrayList<String>();
    for (String tagId : rawAddedTagsForComponent) {
      if (tagId.contains(",")) {
        addedTagsForComponent.addAll(CollectionUtils.arrayToList(tagId.split(",")));
      } else {
        addedTagsForComponent.add(tagId);
      }
    }
    
    List<String> rawDeletedTagsForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_TAG_IDS_FOR_COMPONENT);
    List<String> deletedTagsForComponent = new ArrayList<String>();
    for (String tagId : rawDeletedTagsForComponent) {
      if (tagId.contains(",")) {
        deletedTagsForComponent.addAll(CollectionUtils.arrayToList(tagId.split(",")));
      } else {
        deletedTagsForComponent.add(tagId);
      }
    }
    
    linkEntityToProcess(addedTagsForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAGS, VertexLabelConstants.ENTITY_TAG);
    unlinkEntityToProcess(deletedTagsForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_TAGS);
    
    List<String> addedEndpointsForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_ENDPOINT_IDS_FOR_COMPONENT);
    List<String> deletedEndpointsForComponent = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_ENDPOINT_IDS_FOR_COMPONENT);
    
    linkEntityToProcess(addedEndpointsForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_COMPONENT,
        VertexLabelConstants.ENDPOINT);
    unlinkEntityToProcess(deletedEndpointsForComponent, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_COMPONENT);
    
    List<String> addedEndpointsForProcess = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_ENDPOINT_IDS_FOR_PROCESS);
    List<String> deletedEndpointsForProcess = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_ENDPOINT_IDS_FOR_PROCESS);
    
    linkEntityToProcess(addedEndpointsForProcess, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_PROCESS, VertexLabelConstants.ENDPOINT);
    unlinkEntityToProcess(deletedEndpointsForProcess, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_PROCESS);

    List<String> addedRoles = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_ROLES);
    List<String> deletedRoles = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_ROLES);
    
    linkEntityToProcess(addedRoles, processEventNode, RelationshipLabelConstants.HAS_LINKED_ROLES,
        VertexLabelConstants.ENTITY_TYPE_ROLE);
    unlinkEntityToProcess(deletedRoles, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_ROLES); 
    
    //Handle added and deleted destination organization for transfer call activity.
    List<String> addedDestinationOrganizations = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.ADDED_DESTINATION_ORGANIZATIONS);
    List<String> deletedDestinationOrganizations = (List<String>) processEventMap
        .remove(ISaveProcessEventModel.DELETED_DESTINATION_ORGANIZATIONS);
    
    linkEntityToProcess(addedDestinationOrganizations, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_DESTINATION_ORGANIZATIONS, VertexLabelConstants.ORGANIZATION);
    unlinkEntityToProcess(deletedDestinationOrganizations, processEventNode,
        RelationshipLabelConstants.HAS_LINKED_DESTINATION_ORGANIZATIONS);
  }

  private static List<String> getAllowedProperties(Vertex processEventNode)
  {
    String workflowType = processEventNode.getProperty(ISaveProcessEventModel.WORKFLOW_TYPE);
    String eventType =  processEventNode.getProperty(ISaveProcessEventModel.EVENT_TYPE);
    String triggeringType = processEventNode.getProperty(ISaveProcessEventModel.TRIGGERING_TYPE);
    switch (workflowType) {
      case WorkflowConstants.STANDARD_WORKFLOW:
      case WorkflowConstants.TASKS_WORKFLOW:
        if(eventType.equals(EventType.INTEGRATION.toString())) {
          return new ArrayList<>();
        }else if(triggeringType.equals(WorkflowConstants.AFTER_CREATE)) {
          return ALLOWED_PROPETIES_FOR_WF_CREATE;
        }else if(triggeringType.equals(WorkflowConstants.AFTER_SAVE)) {
          return ALLOWED_PROPETIES_FOR_WF_SAVE;
        }
        
      //WorkflowConstants.SCHEDULED_WORKFLOW and WorkflowConstants.JMS_WORKFLOW nothing is allowed
      default:
        return new ArrayList<>();
    }
  }

  private static void linkEntityToProcess(List<String> ids, Vertex processEventNode,
      String edgeName, String entityType) throws Exception
  {
    for (String id : ids) {
      Vertex vertex = UtilClass.getVertexById(id, entityType);
      processEventNode.addEdge(edgeName, vertex);
    }
  }
  
  private static void unlinkEntityToProcess(List<String> ids, Vertex processEventNode,
      String edgeName) throws Exception
  {
    if (ids.size() > 0) {
      Iterable<Edge> entityEdges = processEventNode.getEdges(Direction.OUT, edgeName);
      for (Edge entityEdge : entityEdges) {
        Vertex entityNode = entityEdge.getVertex(Direction.IN);
        if (ids.contains(UtilClass.getCodeNew(entityNode))) {
          entityEdge.remove();
        }
      }
    }
  }
  
  public static void getProcessEventNodeWithConfigInformation(Vertex processEventNode,
      Map<String, Object> returnMap, Map<String, Object> configMap) throws Exception
  {
    fillReferencedKlasses(processEventNode, configMap, returnMap);
    
    fillReferencedUsers(processEventNode, configMap);
    
    fillReferencedOrganizations(processEventNode, configMap, returnMap);
    
    fillReferencedTasks(processEventNode, configMap);
    
    fillReferencedAttributes(processEventNode, configMap, returnMap);
    
    fillReferencedTags(processEventNode, configMap, returnMap);
    
    fillReferencedContexts(processEventNode, configMap);
    
    fillReferencedRelationships(processEventNode, configMap);
    
    fillReferencedTaxonomies(processEventNode, configMap, returnMap);
    
    fillReferencedKlassesForComponent(processEventNode, configMap);
    
    fillReferencedTaxonomiesForComponent(processEventNode, configMap);
    
    fillReferencedAttributesForComponent(processEventNode, configMap);
    
    fillReferencedTagsForComponent(processEventNode, configMap);
    
    fillReferencedEndpointsForProcess(processEventNode, configMap, returnMap);
    
    fillReferencedEndpointsForComponent(processEventNode, configMap);
    
    fillReferencedRoles(processEventNode, configMap);

    fillReferencedDestinationOrganizations(processEventNode, configMap, returnMap);
  }
  
  public static void getConfigInformationForProcessInGrid(Vertex processEventNode,
      Map<String, Object> returnMap, Map<String, Object> configMap) throws Exception
  {
    fillReferencedKlasses(processEventNode, configMap, returnMap);
    
    fillReferencedTaxonomies(processEventNode, configMap, returnMap);
    
    fillReferencedAttributes(processEventNode, configMap, returnMap);
    
    fillReferencedTags(processEventNode, configMap, returnMap);
    
    fillReferencedEndpointsForProcess(processEventNode, configMap, returnMap);
    
    fillReferencedOrganizations(processEventNode, configMap, returnMap);
  }
  
  private static void fillReferencedTags(Vertex processEventNode, Map<String, Object> configMap,
      Map<String, Object> returnMap)
  {
    Map<String, Object> referencedTags = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_TAGS);
    if (referencedTags == null) {
      referencedTags = new HashMap<>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_TAGS, referencedTags);
    }
    Iterable<Vertex> tagIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_PROCESS);
    List<String> tagIds = new ArrayList<>();
    for (Vertex tag : tagIterator) {
      String tagId = UtilClass.getCodeNew(tag);
      tagIds.add(tagId);
      if (referencedTags.containsKey(tagId)) {
        continue;
      }
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, tag);
      referencedTags.put(tagId, klassMap);
    }
    getEntityIdsfromEndpoint(processEventNode, tagIds, referencedTags,
        RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_ENDPOINT);
    returnMap.put(IProcessEvent.TAG_IDS, tagIds);
  }
  
  private static void fillReferencedContexts(Vertex processEventNode, Map<String, Object> configMap)
  {
    Map<String, Object> referencedContexts = new HashMap<>();
    List<String> referencedContextsIds = new ArrayList<>();
    fillReferencedEntityAndIds(processEventNode, referencedContexts,
        RelationshipLabelConstants.HAS_LINKED_CONTEXTS, referencedContextsIds);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_CONTEXTS, referencedContexts);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_CONTEXTS_IDS, referencedContextsIds);
  }
  
  private static void fillReferencedRelationships(Vertex processEventNode,
      Map<String, Object> configMap)
  {
    Map<String, Object> referencedRelationships = new HashMap<>();
    List<String> referencedRelationshipsIds = new ArrayList<>();
    fillReferencedEntityAndIds(processEventNode, referencedRelationships,
        RelationshipLabelConstants.HAS_LINKED_RELATIONSHIPS, referencedRelationshipsIds);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_RELATIONSHIPS,
        referencedRelationships);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_RELATIONSHIPS_IDS,
        referencedRelationshipsIds);
  }

  private static void fillReferencedTaxonomies(Vertex processEventNode,
      Map<String, Object> configMap, Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> referencedTaxonomiesForEvent = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_TAXONOMIES);
    if (referencedTaxonomiesForEvent == null) {
      referencedTaxonomiesForEvent = new HashMap<>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_TAXONOMIES,
          referencedTaxonomiesForEvent);
    }
    List<String> taxonomiesIdsForEvent = new ArrayList<>();
    Iterable<Vertex> taxonomyIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_PROCESS);
    for (Vertex taxonomy : taxonomyIterator) {
      String taxonomyId = UtilClass.getCodeNew(taxonomy);
      taxonomiesIdsForEvent.add(taxonomyId);
      if (referencedTaxonomiesForEvent.containsKey(taxonomyId)) {
        continue;
      }
      Map<String, Object> taxonomyMap = new HashMap<>();
      TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomy);
      referencedTaxonomiesForEvent.put(taxonomyId, taxonomyMap);
    }
    getTaxonomyIdsfromEndpoint(processEventNode, taxonomiesIdsForEvent,
        referencedTaxonomiesForEvent,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_ENDPOINT);
    returnMap.put(IProcessEvent.TAXONOMY_IDS, taxonomiesIdsForEvent);
  }
  
  private static void fillReferencedAttributes(Vertex processEventNode,
      Map<String, Object> configMap, Map<String, Object> returnMap)
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_ATTRIBUTES);
    if (referencedAttributes == null) {
      referencedAttributes = new HashMap<>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ATTRIBUTES, referencedAttributes);
    }
    List<String> attributeIds = new ArrayList<>();
    Iterable<Vertex> attributeIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_PROCESS);
    for (Vertex attribute : attributeIterator) {
      String attributeId = UtilClass.getCodeNew(attribute);
      attributeIds.add(attributeId);
      if (referencedAttributes.containsKey(attributeId)) {
        continue;
      }
      Map<String, Object> endpointMap = UtilClass.getMapFromVertex(fieldsToFetch, attribute);
      referencedAttributes.put(attributeId, endpointMap);
    }
    getEntityIdsfromEndpoint(processEventNode, attributeIds, referencedAttributes,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_ENDPOINT);
    returnMap.put(IProcessEvent.ATTRIBUTE_IDS, attributeIds);
  }
  
  private static void fillReferencedTasks(Vertex processEventNode, Map<String, Object> configMap)
  {
    Map<String, Object> referencedTasks = new HashMap<>();
    List<String> referencedTasksIds = new ArrayList<>();
    fillReferencedEntityAndIds(processEventNode, referencedTasks,
        RelationshipLabelConstants.HAS_LINKED_TASKS, referencedTasksIds);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_TASKS, referencedTasks);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_TASKS_IDS, referencedTasksIds);
  }
  
  private static void fillReferencedOrganizations(Vertex processEventNode,
      Map<String, Object> configMap, Map<String, Object> returnMap)
  {
    Map<String, Object> referencedOrganization = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_ORGANIZATIONS);
    if (referencedOrganization == null) {
      referencedOrganization = new HashMap<String, Object>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ORGANIZATIONS,
          referencedOrganization);
    }
    List<String> organizationIds = new ArrayList<>();
    Iterable<Vertex> orgnizationIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_ORGANIZATIONS);
    for (Vertex orgnization : orgnizationIterator) {
      String organizationId = UtilClass.getCodeNew(orgnization);
      organizationIds.add(organizationId);
      if (referencedOrganization.containsKey(organizationId)) {
        continue;
      }
      Map<String, Object> organizationMap = UtilClass.getMapFromVertex(fieldsToFetch, orgnization);
      referencedOrganization.put(organizationId, organizationMap);
    }
    returnMap.put(IProcessEvent.ORGANIZATIONS_IDS, organizationIds);
  }
  
  private static void fillReferencedUsers(Vertex processEventNode, Map<String, Object> configMap)
  {
    Map<String, Object> referencedUsers = new HashMap<>();
    List<String> referencedUserIds = new ArrayList<>();
    fillReferencedUsers(processEventNode, referencedUsers,
        RelationshipLabelConstants.HAS_LINKED_USERS, referencedUserIds);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_USERS, referencedUsers);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_USERS_IDS, referencedUserIds);
  }
  
  private static void fillReferencedRoles(Vertex processEventNode, Map<String, Object> configMap)
  {
    Map<String, Object> referencedRoles = new HashMap<>();
    List<String> referencedRolessIds = new ArrayList<>();
    fillReferencedEntityAndIds(processEventNode, referencedRoles,
        RelationshipLabelConstants.HAS_LINKED_ROLES, referencedRolessIds);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ROLES, referencedRoles);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ROLES_IDS, referencedRolessIds);
  }
  
  private static void fillReferencedKlasses(Vertex processEventNode, Map<String, Object> configMap,
      Map<String, Object> returnMap)
  {
    Map<String, Object> referencedKlasses = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_KLASSES);
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_KLASSES, referencedKlasses);
    }
    List<String> klassIds = new ArrayList<>();
    Iterable<Vertex> klassIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_PROCESS);
    for (Vertex klass : klassIterator) {
      String klassId = UtilClass.getCodeNew(klass);
      klassIds.add(klassId);
      if (referencedKlasses.containsKey(klassId)) {
        continue;
      }
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klass);
      referencedKlasses.put(klassId, klassMap);
    }
    getEntityIdsfromEndpoint(processEventNode, klassIds, referencedKlasses,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_ENDPOINT);
    returnMap.put(IProcessEvent.KLASS_IDS, klassIds);
    
    //added for Non nature class selection
    Map<String, Object> referencedNonNatureKlasses = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_NON_NATURE_KLASSES);
    if (referencedNonNatureKlasses == null) {
      referencedNonNatureKlasses = new HashMap<>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_NON_NATURE_KLASSES, referencedNonNatureKlasses);
    }
    List<String> nonNatureklassIds = new ArrayList<>();
    Iterable<Vertex> nonNatureKlassIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_NON_NATURE_KLASSES_FOR_PROCESS);
    for (Vertex nonNatureklass : nonNatureKlassIterator) {
      String nonNatureKlassId = UtilClass.getCodeNew(nonNatureklass);
      nonNatureklassIds.add(nonNatureKlassId);
      if (referencedNonNatureKlasses.containsKey(nonNatureKlassId)) {
        continue;
      }
      Map<String, Object> nonNatureklassMap = UtilClass.getMapFromVertex(fieldsToFetch, nonNatureklass);
      referencedNonNatureKlasses.put(nonNatureKlassId, nonNatureklassMap);
    }
    getEntityIdsfromEndpoint(processEventNode, nonNatureklassIds, referencedNonNatureKlasses,
        RelationshipLabelConstants.HAS_LINKED_NON_NATURE_KLASSES_FOR_ENDPOINT);
    returnMap.put(IProcessEvent.NON_NATURE_KLASS_IDS, nonNatureklassIds);
  }

  private static void getEntityIdsfromEndpoint(Vertex processEventNode, List<String> entityIds,
      Map<String, Object> referencedEntites, String label)
  {
    Iterable<Vertex> endpointIterator = processEventNode.getVertices(Direction.IN,
        RelationshipLabelConstants.PROFILE_PROCESS_LINK);
    if (endpointIterator.iterator()
        .hasNext()) {
      Vertex endpointNode = endpointIterator.iterator()
          .next();
      Iterable<Vertex> entityIterator = endpointNode.getVertices(Direction.OUT, label);
      for (Vertex entity : entityIterator) {
        String entityId = UtilClass.getCodeNew(entity);
        if (!entityIds.contains(entityId)) {
          entityIds.add(entityId);
        }
        if (referencedEntites.containsKey(entityId)) {
          continue;
        }
        Map<String, Object> entityMap = UtilClass.getMapFromVertex(fieldsToFetch, entity);
        referencedEntites.put(entityId, entityMap);
      }
    }
  }
  
  private static void getTaxonomyIdsfromEndpoint(Vertex processEventNode,
      List<String> taxonomiesIdsForEvent, Map<String, Object> referencedTaxonomiesForEvent,
      String label) throws Exception
  {
    Iterable<Vertex> endpointIterator = processEventNode.getVertices(Direction.IN,
        RelationshipLabelConstants.PROFILE_PROCESS_LINK);
    if (endpointIterator.iterator()
        .hasNext()) {
      Vertex endpointNode = endpointIterator.iterator()
          .next();
      Iterable<Vertex> taxonomyIterator = endpointNode.getVertices(Direction.OUT, label);
      for (Vertex taxonomy : taxonomyIterator) {
        String taxonomyId = UtilClass.getCodeNew(taxonomy);
        taxonomiesIdsForEvent.add(taxonomyId);
        if (referencedTaxonomiesForEvent.containsKey(taxonomyId)) {
          continue;
        }
        Map<String, Object> taxonomyMap = new HashMap<>();
        TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomy);
        referencedTaxonomiesForEvent.put(taxonomyId, taxonomyMap);
      }
    }
  }
  
  private static void fillReferencedEndpointsForComponent(Vertex processEventNode,
      Map<String, Object> configMap) throws Exception
  {
    Map<String, Object> referencedEndpoints = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_ENDPOINTS);
    if (referencedEndpoints == null) {
      referencedEndpoints = new HashMap<String, Object>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ENDPOINTS, referencedEndpoints);
    }
    Iterable<Vertex> endpointIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_COMPONENT);
    for (Vertex endpoint : endpointIterator) {
      String endpointId = UtilClass.getCodeNew(endpoint);
      if (referencedEndpoints.containsKey(endpointId)) {
        continue;
      }
      Map<String, Object> endpointMap = UtilClass.getMapFromVertex(fieldsToFetch, endpoint);
      referencedEndpoints.put(endpointId, endpointMap);
    }
  }
  
  private static void fillReferencedEndpointsForProcess(Vertex processEventNode,
      Map<String, Object> configMap, Map<String, Object> returnMap) throws Exception
  {
    Map<String, Object> referencedEndpoints = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_ENDPOINTS);
    if (referencedEndpoints == null) {
      referencedEndpoints = new HashMap<String, Object>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ENDPOINTS, referencedEndpoints);
    }
    List<String> endpointIds = new ArrayList<>();
    Iterable<Vertex> endpointIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_ENDPOINTS_FOR_PROCESS);
    for (Vertex endpoint : endpointIterator) {
      String endpointId = UtilClass.getCodeNew(endpoint);
      endpointIds.add(endpointId);
      if (referencedEndpoints.containsKey(endpointId)) {
        continue;
      }
      Map<String, Object> endpointMap = UtilClass.getMapFromVertex(fieldsToFetch, endpoint);
      referencedEndpoints.put(endpointId, endpointMap);
    }
    returnMap.put(IProcessEvent.ENDPOINT_IDS, endpointIds);
  }
  
  private static void fillReferencedEntityAndIds(Vertex processEventNode,
      Map<String, Object> referencedEntityMap, String edgeLabel, List<String> referencedEntityIds)
  {
    fillReferencedEntityAndIds(processEventNode, referencedEntityMap, edgeLabel,
        referencedEntityIds, fieldsToFetch);
  }
  
  private static void fillReferencedEntityAndIds(Vertex processEventNode,
      Map<String, Object> referencedEntityMap, String edgeLabel, List<String> referencedEntityIds,
      List<String> fieldsToFetch)
  {
    Iterator<Edge> klassEdges = processEventNode.getEdges(Direction.OUT, edgeLabel)
        .iterator();
    
    while (klassEdges.hasNext()) {
      Edge edge = klassEdges.next();
      Vertex vertex = edge.getVertex(Direction.IN);
      Map<String, Object> vertexMap = UtilClass.getMapFromVertex(fieldsToFetch, vertex);
      String vertexId = UtilClass.getCodeNew(vertex);
      referencedEntityMap.put(vertexId, vertexMap);
      referencedEntityIds.add(vertexId);
    }
  }
  
  private static void fillReferencedUsers(Vertex processEventNode,
      Map<String, Object> referencedEntityMap, String edgeLabel, List<String> referencedEntityIds)
  {
    Iterator<Edge> klassEdges = processEventNode.getEdges(Direction.OUT, edgeLabel)
        .iterator();
    
    while (klassEdges.hasNext()) {
      Edge edge = klassEdges.next();
      Vertex vertex = edge.getVertex(Direction.IN);
      Map<String, Object> vertexMap = UtilClass
          .getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY, IUser.FIRST_NAME,
              IUser.LAST_NAME, IUser.ICON, IUser.CODE, IUser.USER_NAME), vertex);
      vertexMap.put(IUser.LABEL,
          vertexMap.remove(IUser.FIRST_NAME) + " " + vertexMap.remove(IUser.LAST_NAME));
      String vertexId = UtilClass.getCodeNew(vertex);
      referencedEntityMap.put(vertexId, vertexMap);
      referencedEntityIds.add(vertexId);
    }
  }
  
  private static void fillReferencedKlassesForComponent(Vertex processEventNode,
      Map<String, Object> configMap)
  {
    Map<String, Object> referencedKlasses = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_KLASSES);
    Iterable<Vertex> klassIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_COMPONENT);
    for (Vertex klass : klassIterator) {
      String klassId = UtilClass.getCodeNew(klass);
      if (referencedKlasses.get(klassId) == null) {
        Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klass);
        referencedKlasses.put(klassId, klassMap);
      }
    }
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_KLASSES, referencedKlasses);
  }
  
  private static void fillReferencedTaxonomiesForComponent(Vertex processEventNode,
      Map<String, Object> configMap) throws Exception
  {
    Map<String, Object> referencedTaxonomies = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_TAXONOMIES);
    Iterable<Vertex> taxonomyIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_COMPONENT);
    for (Vertex taxonomy : taxonomyIterator) {
      String taxonomyId = UtilClass.getCodeNew(taxonomy);
      if (referencedTaxonomies.get(taxonomyId) == null) {
        Map<String, Object> taxonomyMap = new HashMap<>();
        TaxonomyUtil.fillTaxonomiesChildrenAndParentData(taxonomyMap, taxonomy);
        referencedTaxonomies.put(taxonomyId, taxonomyMap);
      }
    }
  }
  
  private static void fillReferencedAttributesForComponent(Vertex processEventNode,
      Map<String, Object> configMap)
  {
    Map<String, Object> referencedAttributes = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_ATTRIBUTES);
    Iterable<Vertex> attributeIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES);
    List<String> attributeIds = new ArrayList<>();
    for (Vertex attribute : attributeIterator) {
      String attributeId = UtilClass.getCodeNew(attribute);
      if (referencedAttributes.get(attributeId) == null) {
        attributeIds.add(attributeId);
      }
    }
    fillReferencedEntityAndIds(processEventNode, referencedAttributes,
        RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES, attributeIds);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_ATTRIBUTES, referencedAttributes);
  }
  
  private static void fillReferencedTagsForComponent(Vertex processEventNode,
      Map<String, Object> configMap)
  {
    List<String> tagIds = new ArrayList<>();
    Map<String, Object> referencedTags = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_TAGS);
    Iterable<Vertex> atagterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_TAGS);
    for (Vertex tag : atagterator) {
      String attributeId = UtilClass.getCodeNew(tag);
      if (referencedTags.get(attributeId) == null) {
        tagIds.add(attributeId);
      }
    }
    
    List<String> filedsToFetchForTags = new ArrayList<String>();
    filedsToFetchForTags.addAll(fieldsToFetch);
    filedsToFetchForTags.add(ITag.IS_MULTI_SELECT);
    filedsToFetchForTags.add(ITag.TAG_TYPE);
    
    fillReferencedEntityAndIds(processEventNode, referencedTags,
        RelationshipLabelConstants.HAS_LINKED_TAGS, tagIds, filedsToFetchForTags);
    configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_TAGS, referencedTags);
  }
  
  public static Map<String, Object> createConfigDetails()
  {
    Map<String, Object> configDetails = new HashMap<>();
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_KLASSES, new HashMap<>());
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_NON_NATURE_KLASSES, new HashMap<>());
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_TAXONOMIES, new HashMap<>());
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_ATTRIBUTES, new HashMap<>());
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_TAGS, new HashMap<>());
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_ENDPOINTS, new HashMap<>());
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_MAPPINGS, new HashMap<>());
    configDetails.put(IGetConfigDetailsModelForProcess.REFERENCED_AUTHORIZATION_MAPPINGS, new HashMap<>());
    return configDetails;
  }
  
  public static Iterable<Vertex> getProcessDefinition(List<String> processIds, String queryField)
      throws ProcessModifiedDeletedException
  {
    String query = "select from " + VertexLabelConstants.PROCESS_EVENT + " where " + queryField
        + " in " + EntityUtil.quoteIt(processIds);
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> processNodes = graph.command(new OCommandSQL(query))
        .execute();
    //for custom WF we did't get process definition. 
    //To allow log window for BGP process without WF we avoid throwing exception
    /*if (!processNodes.iterator()
        .hasNext()) {
      throw new ProcessModifiedDeletedException();
    }*/
    return processNodes;
  }
  
  public static void getReferencedDetailsForSearchFilterComponent(HashMap<String, Object> returnMap,
      HashMap<String, Object> configMap)
      throws Exception, IOException, JsonParseException, JsonMappingException
  {
    // Namespace Uris in process xml
    String BPMN = "http://www.omg.org/spec/BPMN/20100524/MODEL";
    String CAMUNDA = "http://camunda.org/schema/1.0/bpmn";
    
    BpmnModelInstance bpmnModel = Bpmn.readModelFromStream(
        new ByteArrayInputStream(((String) returnMap.get("processDefinition")).getBytes()));
    
    DomDocument document = bpmnModel.getDocument();
    DomElement root = document.getRootElement();
    List<DomElement> elements = root.getChildElementsByNameNs(BPMN, "process");
    if (elements.isEmpty()) {
      throw new Exception(
          "No BPMN Process found in the Workflow : " + ((String) returnMap.get("label")));
    }
    if (elements.size() > 1) {
      throw new Exception("Multiple BPMN Processes/Pools found in the Workflow : "
          + ((String) returnMap.get("label")));
    }
    DomElement processElement = elements.get(0);
    Map<String, String> inputVariables;
    Set<String> selectedAttributeIdsInSearchComponent = new HashSet<String>();
    Set<String> selectedTagIdsInSearchComponent = new HashSet<String>();
    Set<String> selectedDataLanguageCodes = new HashSet<String>();
    for (DomElement serviceTask : processElement.getChildElementsByNameNs(BPMN, "serviceTask")) {
      inputVariables = new HashMap<String, String>();

        List<DomElement> extensionElements = serviceTask.getChildElementsByNameNs(BPMN,
            "extensionElements");
        for (DomElement extentionElement : extensionElements) {
          for (DomElement inputOutput : extentionElement.getChildElementsByNameNs(CAMUNDA,
              "inputOutput")) {
            for (DomElement inputParameter : inputOutput.getChildElementsByNameNs(CAMUNDA,
                "inputParameter")) {
              inputVariables.put(inputParameter.getAttribute("name"),
                  inputParameter.getTextContent());
            }
          }
        }
       /* String filterModel = inputVariables.get(ProcessConstants.FILTER_MODEL) != null ? inputVariables.get(ProcessConstants.FILTER_MODEL)
            : inputVariables.get(ProcessConstants.INPUT_FILTER_MODEL);
        if (filterModel != null && !filterModel.isEmpty()) {
          ObjectMapper objectMapper = new ObjectMapper();
          Map<String, Object> getKlassInstanceTreeStrategyModel = (Map<String, Object>) objectMapper.readValue(filterModel, HashMap.class);
          for (Map<String, Object> attributeModel : (List<Map<String, Object>>) getKlassInstanceTreeStrategyModel
              .get(IGetKlassInstanceTreeStrategyModel.ATTRIBUTES)) {
            selectedAttributeIdsInSearchComponent.add((String) attributeModel.get(IGetKlassInstanceTreeStrategyModel.ID));
          }
          
          for (Map<String, Object> tagModel : (List<Map<String, Object>>) getKlassInstanceTreeStrategyModel
              .get(IGetKlassInstanceTreeStrategyModel.TAGS)) {
            String tagId = (String) tagModel.get(IGetKlassInstanceTreeStrategyModel.ID);
            selectedTagIdsInSearchComponent.add(tagId);
            for (Map<String, Object> child : (List<Map<String, Object>>) tagModel.get(IPropertyInstanceFilterModel.MANDATORY)) {
              selectedTagIdsInSearchComponent.add((String) child.get(IGetKlassInstanceTreeStrategyModel.ID));
            }
          }
        }*/

       if ((inputVariables.get(WorkflowConstants.TASK_ID).equals("setAttributesAndTagsTask")
           && Boolean.parseBoolean(inputVariables.get(SetAttributesAndTagsTask.IS_TRIGGERED_THROUGH_SCHEDULER)))
           || inputVariables.get(WorkflowConstants.TASK_ID).equals("assetExpirationTask")) {
         String setDataLanguage = inputVariables.get(SetAttributesAndTagsTask.DATA_LANGUAGE);
         if (setDataLanguage != null && !setDataLanguage.isEmpty()) {
           selectedDataLanguageCodes.add(setDataLanguage);
         }
       }
    
    List<String> fieldsToFetchForAttributes = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IConfigEntityInformationModel.TYPE, IConfigEntityInformationModel.LABEL,
        IConfigEntityInformationModel.ICON, ISearchComponentAttributeInfoModel.DEFAULT_UNIT,
        ISearchComponentAttributeInfoModel.PRECISION);
    List<String> fieldsToFetchForTags = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IConfigEntityInformationModel.TYPE, IConfigEntityInformationModel.LABEL,
        IConfigEntityInformationModel.ICON, ITag.TAG_TYPE);
    List<String> fieldsToFetchForLanguages = Arrays.asList(CommonConstants.CODE_PROPERTY,
        IConfigEntityInformationModel.TYPE, IConfigEntityInformationModel.LABEL,
        IConfigEntityInformationModel.ICON, IConfigEntityInformationModel.CODE);
    
    Iterable<Vertex> attributeNodes = UtilClass.getVerticesByIds(
        selectedAttributeIdsInSearchComponent, VertexLabelConstants.ENTITY_TYPE_ATTRIBUTE);
    Map<String, Object> referencedAttributes = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_ATTRIBUTES);
    for (Vertex attributeNode : attributeNodes) {
      referencedAttributes.put(UtilClass.getCodeNew(attributeNode),
          UtilClass.getMapFromVertex(fieldsToFetchForAttributes, attributeNode));
    }
    
    Iterable<Vertex> tagNodes = UtilClass.getVerticesByIds(selectedTagIdsInSearchComponent,
        VertexLabelConstants.ENTITY_TAG);
    Map<String, Object> referencedTags = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_TAGS);
    for (Vertex tagNode : tagNodes) {
      referencedTags.put(UtilClass.getCodeNew(tagNode),
          UtilClass.getMapFromVertex(fieldsToFetchForTags, tagNode));
    }
    
    Map<String, Object> referencedLanguages = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_LANGUAGES);
    if (referencedLanguages == null) {
      referencedLanguages = new HashMap<String, Object>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_LANGUAGES, referencedLanguages);
    }
    
    if (!selectedDataLanguageCodes.isEmpty()) {
      List<Vertex> languageNodes = new ArrayList<Vertex>();
      for (String selectedDataLanguageCode : selectedDataLanguageCodes) {
        try{
          Vertex languageNode = UtilClass.getVertexByCode(selectedDataLanguageCode,
                  VertexLabelConstants.LANGUAGE);
          languageNodes.add(languageNode);
        }
        catch (NotFoundException e){ }
      }
      for (Vertex languageNode : languageNodes) {
        referencedLanguages.put(UtilClass.getCode(languageNode),
            UtilClass.getMapFromVertex(fieldsToFetchForLanguages, languageNode));
        }
      }
    }
  }
  
  /**
   * Fill reference data for destination organization for transfer call activity.
   * @param processEventNode
   * @param configMap
   * @param returnMap
   */
  public static void fillReferencedDestinationOrganizations(Vertex processEventNode,
      Map<String, Object> configMap, Map<String, Object> returnMap)
  {
    Map<String, Object> referencedDestinationOrganization = (Map<String, Object>) configMap
        .get(IGetConfigDetailsModelForProcess.REFERENCED_DESTINATION_ORGANIZATIONS);
    if (referencedDestinationOrganization == null) {
      referencedDestinationOrganization = new HashMap<String, Object>();
      configMap.put(IGetConfigDetailsModelForProcess.REFERENCED_DESTINATION_ORGANIZATIONS,
          referencedDestinationOrganization);
    }
    
    Iterable<Vertex> orgnizationIterator = processEventNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_LINKED_DESTINATION_ORGANIZATIONS);
    for (Vertex orgnization : orgnizationIterator) {
      String organizationId = UtilClass.getCodeNew(orgnization);
      if (referencedDestinationOrganization.containsKey(organizationId)) {
        continue;
      }
      Map<String, Object> organizationMap = UtilClass.getMapFromVertex(fieldsToFetch, orgnization);
      referencedDestinationOrganization.put(organizationId, organizationMap);
    }
  }
  
    public static List<String> validateMandataryFields(Vertex processEventNode, String workflowType, List<String> fieldsToExclude)
  {
    List<String> fieldToExcludes = new ArrayList<>(fieldsToExclude);
    if(!workflowType.equals(WorkflowConstants.JMS_WORKFLOW)) {
      fieldToExcludes.addAll(Arrays.asList(ISaveProcessEventModel.IP, ISaveProcessEventModel.PORT, ISaveProcessEventModel.QUEUE));
    }
    
    String triggeringType = processEventNode.getProperty(ISaveProcessEventModel.TRIGGERING_TYPE);
    if (workflowType.equals(WorkflowConstants.JMS_WORKFLOW)
        || triggeringType.equals(WorkflowConstants.IMPORT) || triggeringType.equals(WorkflowConstants.EXPORT)) {

      fieldToExcludes.add(ISaveProcessEventModel.PHYSICAL_CATALOG_IDS);
    }
    
    //Only allowed for after save triggering type of WF
    if(triggeringType.equals(WorkflowConstants.AFTER_SAVE)) {
      fieldToExcludes.remove(ISaveProcessEventModel.ACTION_SUB_TYPE);
    }
    
    //Only allowed for business_process event type.
    if (processEventNode.getProperty(ISaveProcessEventModel.EVENT_TYPE).equals(EventType.BUSINESS_PROCESS.name())) {
      fieldToExcludes.remove(ISaveProcessEventModel.USECASES);
    }
    return fieldToExcludes;
  }
}
