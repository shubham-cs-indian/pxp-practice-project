package com.cs.config.strategy.plugin.usecase.endpoint.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.StringUtils;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.exception.validationontype.InvalidMappingTypeException;
import com.cs.core.config.interactor.exception.validationontype.InvalidWorkflowTypeException;
import com.cs.core.config.interactor.model.endpoint.IConfigDetailsForGridEndpointsModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.ITagValueMappingModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.LabelMustNotBeEmptyException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.onboarding.notfound.FoundMultipleEntitiesException;
import com.cs.core.runtime.interactor.exception.onboarding.notfound.FoundPreExistingEntitiesException;
import com.cs.core.runtime.interactor.exception.variants.EmptyMandatoryFieldsException;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.plugin.exception.InvalidDataException;
import com.cs.workflow.base.WorkflowConstants;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings({ "unchecked" })
public class EndpointUtils {
  
  private static final String QUERY_LANGUAGE_SQL  = "sql";
  private static final String VALIDATION_INFO_0   = "PreExisting Entities %s not deleted , prepare request model with %s to delete ";
  private static final String VALIDATION_INFO_1   = "Requested entity %s already Exists with same value ";
  
  public static void getMapFromProfileNode(Vertex profileNode, Map<String, Object> returnMap,
      List<String> mappedColumns) throws Exception
  {
    Map<String, Object> endpointMap = (Map<String, Object>) returnMap
        .get(IGetEndpointForGridModel.ENDPOINT);
    endpointMap.put(IEndpointModel.PROCESSES, getProcesses(profileNode));
    endpointMap.put(IEndpointModel.JMS_PROCESSES, getJMSProcesses(profileNode));
    endpointMap.put(IEndpointModel.SYSTEM_ID, getSystemId(profileNode, returnMap));
    endpointMap.put(IEndpointModel.DASHBOARD_TAB_ID, TabUtils.getDashboardTabNode(profileNode));
    endpointMap.put(IEndpointModel.MAPPINGS, getPropertyMapping(profileNode));
    endpointMap.put(IEndpointModel.AUTHORIZATION_MAPPING, getAuthorizationMapping(profileNode)); 
  }
  
  private static String getSystemId(Vertex endPoint, Map<String, Object> returnMap)
  {
    Map<String, Map<String, String>> referencedSystems = new HashMap<>();
    Iterable<Vertex> systemVertices = endPoint.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM);
    String systemId = null;
    for (Vertex systemVertex : systemVertices) {
      systemId = systemVertex.getProperty(CommonConstants.CODE_PROPERTY);
      Map<String, String> referencedSystem = new HashMap<>();
      referencedSystem.put(IIdLabelModel.ID, systemId);
      referencedSystem.put(IIdLabelModel.LABEL,
          (String) UtilClass.getValueByLanguage(systemVertex, CommonConstants.LABEL_PROPERTY));
      referencedSystems.put(systemId, referencedSystem);
    }
    return systemId;
  }
  
  public static List<String> getProcesses(Vertex profileNode)
  {
    List<String> processIds = new ArrayList<>();
    Iterable<Vertex> processes = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.PROFILE_PROCESS_LINK);
    
    for (Vertex processNode : processes) {
      String processId = processNode.getProperty(CommonConstants.CODE_PROPERTY);
      processIds.add(processId);
    }
    
    return processIds;
  }
  
  public static List<String> getJMSProcesses(Vertex profileNode)
  {
    List<String> processIds = new ArrayList<>();
    Iterable<Vertex> processes = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.PROFILE_JMS_PROCESS_LINK);
    
    for (Vertex processNode : processes) {
      String processId = processNode.getProperty(CommonConstants.CODE_PROPERTY);
      processIds.add(processId);
    }
    
    return processIds;
  }
  
  public static List<String> getEntities(Vertex profileNode, String label)
  {
    List<String> entityIds = new ArrayList<>();
    Iterable<Vertex> entities = profileNode.getVertices(Direction.OUT, label);
    
    for (Vertex entityNode : entities) {
      String entityId = entityNode.getProperty(CommonConstants.CODE_PROPERTY);
      entityIds.add(entityId);
    }
    return entityIds;
  }
  
  public static List<String> getPropertyMapping(Vertex profileNode)
  {
    List<String> propertyMappingIds = new ArrayList<>();
    Iterable<Vertex> propertyMappings = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK);
    
    for (Vertex propertyMapping : propertyMappings) {
      String propertyMappingId = propertyMapping.getProperty(CommonConstants.CODE_PROPERTY);
      propertyMappingIds.add(propertyMappingId);
    }
    
    return propertyMappingIds;
  }
  
  public static Map<String, Map<String, Object>> getEntityMapping(Vertex mappingNode,
      String relationshipLabel, List<String> mappedColumns)
  {
    int count = 0;
    Map<String, Map<String, Object>> configRuleEntityMappings = new HashMap<>();
    
      Iterable<Vertex> configRuleNodes = mappingNode.getVertices(Direction.OUT,
          relationshipLabel);
      for (Vertex configRuleNode : configRuleNodes) {
        Map<String, Object> entityMapping = new HashMap<>();
        String columnName = "";
        entityMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
        List<String> columnNames = new ArrayList<>();
        entityMapping.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, columnNames);
        Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        for (Vertex columnMappingNode : columnMappingNodes) {
          // instead of getting Column name from label we are getting it from
          // "ColumnName"
          // property.
          columnName = (String) columnMappingNode
              .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
          columnNames.add(columnName);
          mappedColumns.add(columnName);
        }
        
        Iterable<Vertex> entityMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex entityMappingNode : entityMappingNodes) {
          entityMapping.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID,
              UtilClass.getCodeNew(entityMappingNode));
        }
        if (configRuleEntityMappings.get(columnName) != null) {
          columnName += count;
          count++;
        }
        configRuleEntityMappings.put(columnName, entityMapping);
      }
    
    return configRuleEntityMappings;
  }
  
  public static Map<String, Map<String, Object>> getTaxonomyMapping(Vertex profileNode,
      String relationshipLabel, List<String> mappedColumns)
  {
    int count = 0;
    Map<String, Map<String, Object>> configRuleEntityMappings = new HashMap<>();
    
    Iterable<Vertex> propertyMappingNodes = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK);
    for (Vertex propertyMappingNode : propertyMappingNodes) {
      Iterable<Vertex> configRuleNodes = propertyMappingNode.getVertices(Direction.OUT,
          relationshipLabel);
      for (Vertex configRuleNode : configRuleNodes) {
        Map<String, Object> entityMapping = new HashMap<>();
        String columnName = "";
        entityMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
        List<String> columnNames = new ArrayList<>();
        entityMapping.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, columnNames);
        Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        for (Vertex columnMappingNode : columnMappingNodes) {
          // instead of getting Column name from label we are getting it from
          // "ColumnName"
          // property.
          columnName = (String) columnMappingNode
              .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
          columnNames.add(columnName);
          mappedColumns.add(columnName);
        }
        
        Iterable<Vertex> entityMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex entityMappingNode : entityMappingNodes) {
          entityMapping.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID,
              UtilClass.getCodeNew(entityMappingNode));
        }
        if (configRuleEntityMappings.get(columnName) != null) {
          columnName += count;
          count++;
        }
        configRuleEntityMappings.put(columnName, entityMapping);
      }
    }
    
    return configRuleEntityMappings;
  }
  
  public static Map<String, Map<String, Object>> getClassMapping(Vertex profileNode,
      String relationshipLabel, List<String> mappedColumns)
  {
    int count = 0;
    Map<String, Map<String, Object>> configRuleEntityMappings = new HashMap<>();
    
    Iterable<Vertex> propertyMappingNodes = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK);
    for (Vertex propertyMappingNode : propertyMappingNodes) {
      Iterable<Vertex> configRuleNodes = propertyMappingNode.getVertices(Direction.OUT,
          relationshipLabel);
      for (Vertex configRuleNode : configRuleNodes) {
        Map<String, Object> entityMapping = new HashMap<>();
        String columnName = "";
        entityMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
        List<String> columnNames = new ArrayList<>();
        entityMapping.put(IConfigRuleAttributeMappingModel.COLUMN_NAMES, columnNames);
        Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        for (Vertex columnMappingNode : columnMappingNodes) {
          // instead of getting Column name from label we are getting it from
          // "ColumnName"
          // property.
          columnName = (String) columnMappingNode
              .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
          columnNames.add(columnName);
          mappedColumns.add(columnName);
        }
        
        Iterable<Vertex> entityMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex entityMappingNode : entityMappingNodes) {
          entityMapping.put(IConfigRuleAttributeMappingModel.MAPPED_ELEMENT_ID,
              UtilClass.getCodeNew(entityMappingNode));
        }
        if (configRuleEntityMappings.get(columnName) != null) {
          columnName += count;
          count++;
        }
        configRuleEntityMappings.put(columnName, entityMapping);
      }
    }
    
    return configRuleEntityMappings;
  }
  
  public static Map<String, Map<String, Object>> getTagMapping(Iterable<Vertex> propetiesConfigRuleNodes,
      List<String> mappedColumns)
  {
    Map<String, Map<String, Object>> configRuleTagMappings = new HashMap<>();
   
    for(Vertex propertyConfigRuleNode : propetiesConfigRuleNodes) {
   
      Iterable<Vertex> configRuleNodes = propertyConfigRuleNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
      for (Vertex configRuleNode : configRuleNodes) {
        List<Map<String, Object>> tagValuesMappingList = new ArrayList<>();
        Map<String, Object> tagMapping = new HashMap<>();
        String columnName = "";
        tagMapping.putAll(UtilClass.getMapFromVertex(new ArrayList<>(), configRuleNode));
        List<String> columnNames = new ArrayList<>();
        tagMapping.put(IConfigRuleTagMappingModel.COLUMN_NAMES, columnNames);
        Iterable<Vertex> columnMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_COLUMN_MAPPING);
        for (Vertex columnMappingNode : columnMappingNodes) {
          Map<String, Object> tagValuesMapping = new HashMap<>();
          // instead of getting Column name from label we are getting it from
          // "ColumnName"
          // property.
          columnName = (String) columnMappingNode
              .getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME);
          columnNames.add(columnName);
          mappedColumns.add(columnName);
          Iterable<Vertex> valueNodes = columnMappingNode.getVertices(Direction.OUT,
              RelationshipLabelConstants.HAS_VALUE_MAPPING);
          List<Map<String, String>> tagValueMappingList = new ArrayList<>();
          for (Vertex valueNode : valueNodes) {
            Map<String, String> tagValueMapping = new HashMap<>();
            Iterator<Vertex> tagValueNodes = valueNode
                .getVertices(Direction.OUT, RelationshipLabelConstants.MAPPED_TO_ENTITY)
                .iterator();
            while (tagValueNodes.hasNext()) {
              Vertex tagValueNode = tagValueNodes.next();
              tagValueMapping.put(ITagValueMappingModel.ID, UtilClass.getCodeNew(valueNode));
              // instead of getting Column name from label we are getting it
              // from "ColumnName"
              // property.
              tagValueMapping.put(ITagValueMappingModel.TAG_VALUE,
                  (String) valueNode.getProperty(IColumnValueTagValueMappingModel.COLUMN_NAME));
              tagValueMapping.put(ITagValueMappingModel.IS_IGNORE_CASE,
                  valueNode.getProperty(ITagValueMappingModel.IS_IGNORE_CASE));
              tagValueMapping.put(ITagValueMappingModel.MAPPED_TAG_VALUE_ID,
                  UtilClass.getCodeNew(tagValueNode));
              tagValueMappingList.add(tagValueMapping);
            }
          }
          if (tagValueMappingList.size() > 0) {
            tagValuesMapping.put(IColumnValueTagValueMappingModel.COLUMN_NAME, columnName);
            tagValuesMapping.put(IColumnValueTagValueMappingModel.MAPPINGS, tagValueMappingList);
            tagValuesMappingList.add(tagValuesMapping);
          }
        }
        
        Iterable<Vertex> tagGroupMappingNodes = configRuleNode.getVertices(Direction.OUT,
            RelationshipLabelConstants.MAPPED_TO_ENTITY);
        for (Vertex tagGroupMappingNode : tagGroupMappingNodes) {
          tagMapping.put(IConfigRuleTagMappingModel.MAPPED_ELEMENT_ID,
              UtilClass.getCodeNew(tagGroupMappingNode));
        }
        tagMapping.put(IConfigRuleTagMappingModel.TAG_VALUE_MAPPINGS, tagValuesMappingList);
        configRuleTagMappings.put(columnName, tagMapping);
      }
  }
    
    return configRuleTagMappings;
  }
  
  public static void deleteEndpointMappings(Vertex profileNode)
  {
    Iterable<Vertex> attributeConfigRules = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_ATTRIBUTE_CONFIG_RULE);
    for (Vertex attributeConfigRule : attributeConfigRules) {
      Iterable<Vertex> columnMappings = attributeConfigRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        columnMapping.remove();
      }
      attributeConfigRule.remove();
    }
    
    Iterable<Vertex> tagConfigRules = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_TAG_CONFIG_RULE);
    for (Vertex tagConfigRule : tagConfigRules) {
      Iterable<Vertex> columnMappings = tagConfigRule.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_COLUMN_MAPPING);
      for (Vertex columnMapping : columnMappings) {
        Iterable<Vertex> tagValues = columnMapping.getVertices(Direction.OUT,
            RelationshipLabelConstants.HAS_VALUE_MAPPING);
        for (Vertex tagValue : tagValues) {
          tagValue.remove();
        }
        columnMapping.remove();
      }
      tagConfigRule.remove();
    }
  }
  
  public static void saveProcesses(Vertex profileNode, Map<String, Object> profileMap, IExceptionModel failure)
      throws Exception
  {
    addProcesses(profileNode, profileMap, failure);
    deleteProcesses(profileNode, profileMap);
    
    addJMSProcesses(profileNode, profileMap, failure);
    deleteJMSProcesses(profileNode, profileMap);
    
    addMappings(profileNode, profileMap, failure);
    deleteMappings(profileNode, profileMap);
    
    addAuthorizationMappings(profileNode, profileMap);
    deleteAuthorizationMappings(profileNode, profileMap);
  }
  
  private static void addProcesses(Vertex profileNode, Map<String, Object> profileMap, IExceptionModel failure) throws Exception
  {
    List<String> addedProcesses = (List<String>) profileMap.get(ISaveEndpointModel.ADDED_PROCESSES);
    String endPointType = profileNode.getProperty(ISaveEndpointModel.ENDPOINT_TYPE);
    String eventType = endPointType.equals(CommonConstants.ONBOARDING_ENDPOINT) ? WorkflowConstants.IMPORT : WorkflowConstants.EXPORT;
    for (String processId : addedProcesses) {
      Vertex process = UtilClass.getVertexById(processId, VertexLabelConstants.PROCESS_EVENT);
      String processEvent = process.getProperty(IProcessEvent.TRIGGERING_TYPE);
      if (StringUtils.isNotEmpty(processEvent) && eventType.equals(processEvent)) {
        profileNode.addEdge(RelationshipLabelConstants.PROFILE_PROCESS_LINK, process);
      }else {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new InvalidWorkflowTypeException(),
            profileNode.getProperty(ISaveEndpointModel.CODE), processId);
      }
    }
  }
  
  private static void deleteProcesses(Vertex profileNode, Map<String, Object> profileMap)
      throws Exception
  {
    List<String> deletedProcessIds = (List<String>) profileMap
        .get(ISaveEndpointModel.DELETED_PROCESSES);
    if (deletedProcessIds.size() > 0) {
      Iterable<Edge> edges = profileNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.PROFILE_PROCESS_LINK);
      for (Edge edge : edges) {
        Vertex proccessNode = edge.getVertex(Direction.IN);
        if (deletedProcessIds.contains(UtilClass.getCodeNew(proccessNode))) {
          edge.remove();
        }
      }
    }
  }
  
  private static void addJMSProcesses(Vertex profileNode, Map<String, Object> profileMap, IExceptionModel failure)
      throws Exception
  {
    List<String> addedJmsProcesses = (List<String>) profileMap.get(ISaveEndpointModel.ADDED_JMS_PROCESSES);
    for (String processId : addedJmsProcesses) {
      Vertex process = UtilClass.getVertexById(processId, VertexLabelConstants.PROCESS_EVENT);
      String workflowType = process.getProperty(IProcessEvent.WORKFLOW_TYPE);
      if(WorkflowConstants.JMS_WORKFLOW.equals(workflowType)) {
        profileNode.addEdge(RelationshipLabelConstants.PROFILE_JMS_PROCESS_LINK, process);
      }else {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new InvalidWorkflowTypeException("Only JMS workflow is allowed"),
            profileNode.getProperty(ISaveEndpointModel.CODE), processId);
      }
    }
  }
  
  private static void deleteJMSProcesses(Vertex profileNode, Map<String, Object> profileMap)
      throws Exception
  {
    List<String> deletedJmsProcessIds = (List<String>) profileMap
        .get(ISaveEndpointModel.DELETED_JMS_PROCESSES);
    if (deletedJmsProcessIds.size() > 0) {
      Iterable<Edge> edges = profileNode.getEdges(Direction.OUT,
          RelationshipLabelConstants.PROFILE_JMS_PROCESS_LINK);
      for (Edge edge : edges) {
        Vertex proccessNode = edge.getVertex(Direction.IN);
        if (deletedJmsProcessIds.contains(UtilClass.getCodeNew(proccessNode))) {
          edge.remove();
        }
      }
    }
  }
  
  private static void addMappings(Vertex profileNode, Map<String, Object> profileMap, IExceptionModel failure) throws Exception
  {
    List<String> addedMappings = (List<String>) profileMap.get(ISaveEndpointModel.ADDED_MAPPINGS);
    String endpointType = profileNode.getProperty(ISaveEndpointModel.ENDPOINT_TYPE);
    String mappingType = endpointType.equals(CommonConstants.ONBOARDING_ENDPOINT) ? CommonConstants.INBOUND_MAPPING : CommonConstants.OUTBOUND_MAPPING;
    for (String mappingId : addedMappings) {
      Vertex mapping = UtilClass.getVertexById(mappingId, VertexLabelConstants.PROPERTY_MAPPING);
      String type = mapping.getProperty(IMapping.MAPPING_TYPE);
      if(type.equals(mappingType)) {
        profileNode.addEdge(RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK, mapping);
      }else {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, new InvalidMappingTypeException(),
            profileNode.getProperty(ISaveEndpointModel.CODE), mappingId);
      }
    }
  }
  
  private static void deleteMappings(Vertex profileNode, Map<String, Object> profileMap) throws Exception
  {
    List<String> deletedMappingIds = (List<String>) profileMap.get(ISaveEndpointModel.DELETED_MAPPINGS);
    if (deletedMappingIds.size() > 0) {
      Iterable<Edge> edges = profileNode.getEdges(Direction.OUT, RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK);
      for (Edge edge : edges) {
        Vertex propertyMappingNode = edge.getVertex(Direction.IN);
        if (deletedMappingIds.contains(UtilClass.getCId(propertyMappingNode))) {
          edge.remove();
        }
      }
    }
  }
  
  private static void addAuthorizationMappings(Vertex profileNode, Map<String, Object> profileMap) throws Exception
  {
    List<String> addedAuthorizatonMappings = (List<String>) profileMap.get(ISaveEndpointModel.ADDED_AUTHORIZATION_MAPPINGS);
    for (String mappingId : addedAuthorizatonMappings) {
      Vertex authorizationMapping = UtilClass.getVertexById(mappingId, VertexLabelConstants.AUTHORIZATION_MAPPING);
      profileNode.addEdge(RelationshipLabelConstants.HAS_AUTHORIZATION_MAPPING_LINK, authorizationMapping);
    }
  }
    
  private static void deleteAuthorizationMappings(Vertex profileNode, Map<String, Object> profileMap) throws Exception
  {
    List<String> deletedAuthorizationMappingIds = (List<String>) profileMap.get(ISaveEndpointModel.DELETED_AUTHORIZATION_MAPPINGS);
    if (deletedAuthorizationMappingIds.size() > 0) {
      Iterable<Edge> edges = profileNode.getEdges(Direction.OUT, RelationshipLabelConstants.HAS_AUTHORIZATION_MAPPING_LINK);
      for (Edge edge : edges) {
        Vertex authorizationMappingNode = edge.getVertex(Direction.IN);
        if (deletedAuthorizationMappingIds.contains(UtilClass.getCId(authorizationMappingNode))) {
          edge.remove();
        }
      }
    }
  }
  
  public static void manageAddedSystemId(Map<String, Object> endpointMap, Vertex profileNode)
      throws Exception
  {
    String addedSystemId = (String) endpointMap.remove(ISaveEndpointModel.ADDED_SYSTEM_ID);
    if (addedSystemId == null || addedSystemId.equals("")) {
      return;
    }
    Vertex systemVertex = UtilClass.getVertexById(addedSystemId, VertexLabelConstants.SYSTEM);
    Iterator<Vertex> existingSytem = profileNode
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_SYSTEM)
        .iterator();
    List<Vertex> listOfExistingSystem = IteratorUtils.toList(existingSytem);
    if (!listOfExistingSystem.contains(systemVertex)) {
      profileNode.addEdge(RelationshipLabelConstants.HAS_SYSTEM, systemVertex);
    }
  }
  
  public static void manageDeletedSystemId(Map<String, Object> endpointMap, Vertex profileNode)
      throws Exception
  {
    String deletedSystemId = (String) endpointMap.remove(ISaveEndpointModel.DELETED_SYSTEM_ID);
    if (deletedSystemId == null || deletedSystemId.equals("")) {
      return;
    }
    Set<Edge> edgesToRemove = new HashSet<>();
    Iterable<Edge> hasSystemEdges = profileNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.HAS_SYSTEM);
    for (Edge hasSystemEdge : hasSystemEdges) {
      Vertex systemVertex = hasSystemEdge.getVertex(Direction.IN);
      if (UtilClass.getCodeNew(systemVertex)
          .equals(deletedSystemId)) {
        edgesToRemove.add(hasSystemEdge);
      }
    }
    for (Edge edgeToRemove : edgesToRemove) {
      edgeToRemove.remove();
    }
  }
  
  public static void fillReferencedConfigDetails(List<Map<String, Object>> list,
      Map<String, Object> responseMap) throws Exception
  {
    List<String> processIds = new ArrayList<>();
    List<String> jmsProcessIds = new ArrayList<>();
    List<String> mappingIds = new ArrayList<>();
    List<String> systemIds = new ArrayList<>();
    List<String> dashboardTabIds = new ArrayList<>();
    List<String> authorizationMappingIds = new ArrayList<>();
    
    Map<String, Object> configDetails = new HashMap<>();
    responseMap.put(IGetGridEndpointsResponseModel.CONFIG_DETAILS, configDetails);
    
    for (Map<String, Object> endpoint : list) {
      Map<String, Object> endpointMap = (Map<String, Object>) endpoint
          .get(IGetEndpointForGridModel.ENDPOINT);
      
      List<String> processes = (List<String>) endpointMap.get(IEndpointModel.PROCESSES);
      processIds.addAll(processes);
      
      List<String> jmsProcesses = (List<String>) endpointMap.get(IEndpointModel.JMS_PROCESSES);
      jmsProcessIds.addAll(jmsProcesses);
      
      List<String> mappingId = (List<String>) endpointMap.get(IEndpointModel.MAPPINGS);
      mappingIds.addAll(mappingId);
      
      String systemId = (String) endpointMap.get(IEndpointModel.SYSTEM_ID);
      systemIds.add(systemId);
      
      String dashboardTabId = (String) endpointMap.get(IEndpointModel.DASHBOARD_TAB_ID);
      dashboardTabIds.add(dashboardTabId);
      
      List<String> authorizationMappings = (List<String>) endpointMap.get(IEndpointModel.AUTHORIZATION_MAPPING);
      authorizationMappingIds.addAll(authorizationMappings);
    }
    
    Map<String, Object> referencedProcesses = new HashMap<>();
    fillReferencedProcesses(processIds, referencedProcesses);
    
    Map<String, Object> referencedJMSProcesses = new HashMap<>();
    fillReferencedJMSProcesses(jmsProcessIds, referencedJMSProcesses);
    
    Map<String, Object> referencedMappings = new HashMap<>();
    fillReferencedMappings(mappingIds, referencedMappings);
    
    Map<String, Object> referencedSystems = new HashMap<>();
    fillReferencedSystems(systemIds, referencedSystems);

    Map<String, Object> referencedDashboardTabs = new HashMap<>();
    TabUtils.fillReferencedDashboardTabs(dashboardTabIds, referencedDashboardTabs);
    
    Map<String, Object> referencedAuthorizationMappings = new HashMap<>();
    fillReferencedAuthorizarionMappings(authorizationMappingIds, referencedAuthorizationMappings);
    
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_MAPPINGS, referencedMappings);
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_PROCESSES,
        referencedProcesses);
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_JMS_PROCESSES,
        referencedJMSProcesses);
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_SYSTEMS, referencedSystems);
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_DASHBOARD_TABS,
        referencedDashboardTabs);
    configDetails.put(IConfigDetailsForGridEndpointsModel.REFERENCED_AUTHORIZATION_MAPPINGS, referencedAuthorizationMappings);
  }
  
  private static void fillReferencedProcesses(List<String> dataruleIds,
      Map<String, Object> referencedProcesses) throws Exception
  {
    for (String processId : dataruleIds) {
      Map<String, Object> processMap = new HashMap<>();
      Vertex processNode = null;
      try {
        processNode = UtilClass.getVertexById(processId, VertexLabelConstants.PROCESS_EVENT);
      }
      catch (NotFoundException e) {
        continue;
      }
      processMap.put(IConfigEntityInformationModel.ID, processId);
      processMap.put(IConfigEntityInformationModel.LABEL,
          UtilClass.getValueByLanguage(processNode, IProcessEvent.LABEL));
      processMap.put(IConfigEntityInformationModel.ICON,
          processNode.getProperty(IProcessEvent.ICON));
      processMap.put(IConfigEntityInformationModel.TYPE,
          processNode.getProperty(IProcessEvent.PROCESS_TYPE));
      processMap.put(IConfigEntityInformationModel.CODE,
          processNode.getProperty(IProcessEvent.CODE));
      referencedProcesses.put(processId, processMap);
    }
  }
  
  private static void fillReferencedJMSProcesses(List<String> jmsProcessIds,
      Map<String, Object> referencedJMSProcesses) throws Exception
  {
    for (String processId : jmsProcessIds) {
      Map<String, Object> processMap = new HashMap<>();
      Vertex processNode = null;
      try {
        processNode = UtilClass.getVertexById(processId, VertexLabelConstants.PROCESS_EVENT);
      }
      catch (NotFoundException e) {
        continue;
      }
      processMap.put(IConfigEntityInformationModel.ID, processId);
      processMap.put(IConfigEntityInformationModel.LABEL,
          UtilClass.getValueByLanguage(processNode, IProcessEvent.LABEL));
      processMap.put(IConfigEntityInformationModel.ICON,
          processNode.getProperty(IProcessEvent.ICON));
      processMap.put(IConfigEntityInformationModel.TYPE,
          processNode.getProperty(IProcessEvent.PROCESS_TYPE));
      processMap.put(IConfigEntityInformationModel.CODE,
          processNode.getProperty(IProcessEvent.CODE));
      referencedJMSProcesses.put(processId, processMap);
    }
  }
  
  
  public static void fillReferencedMappings(List<String> mappingIds,
      Map<String, Object> referencedMappings) throws Exception
  {
    for (String mappingId : mappingIds) {
      Map<String, Object> mappingMap = new HashMap<>();
      Vertex mappingNode = null;
      try {
        mappingNode = UtilClass.getVertexById(mappingId, VertexLabelConstants.PROPERTY_MAPPING);
      }
      catch (NotFoundException e) {
        continue;
      }
      mappingMap.put(IConfigEntityInformationModel.ID, mappingId);
      mappingMap.put(IConfigEntityInformationModel.LABEL,
          UtilClass.getValueByLanguage(mappingNode, IMapping.LABEL));
      mappingMap.put(IConfigEntityInformationModel.ICON, null);
      mappingMap.put(IConfigEntityInformationModel.TYPE, mappingNode.getProperty(IMapping.TYPE));
      mappingMap.put(IConfigEntityInformationModel.CODE, mappingNode.getProperty(IMapping.CODE));
      referencedMappings.put(mappingId, mappingMap);
    }
  }
  
  public static void fillReferencedSystems(List<String> systemIds,
      Map<String, Object> referencedSystems) throws Exception
  {
    for (String systemId : systemIds) {
      Map<String, Object> systemMap = new HashMap<>();
      Vertex systemNode = null;
      try {
        systemNode = UtilClass.getVertexById(systemId, VertexLabelConstants.SYSTEM);
      }
      catch (NotFoundException e) {
        continue;
      }
      systemMap.put(IConfigEntityInformationModel.ID, systemId);
      systemMap.put(IConfigEntityInformationModel.LABEL,
          UtilClass.getValueByLanguage(systemNode, IMapping.LABEL));
      systemMap.put(IConfigEntityInformationModel.ICON, null);
      systemMap.put(IConfigEntityInformationModel.TYPE, systemNode.getProperty(IMapping.TYPE));
      systemMap.put(IConfigEntityInformationModel.CODE, systemNode.getProperty(IMapping.CODE));
      referencedSystems.put(systemId, systemMap);
    }
  }
  
  private static void fillReferencedAuthorizarionMappings(List<String> authorizationmappingIds,
      Map<String, Object> referencedAuthorizationMappings) throws Exception
  {
    for (String authorizationMappingId : authorizationmappingIds) {
      Map<String, Object> authorizationMappingMap = new HashMap<>();
      Vertex authorizationMappingNode = null;
      try {
        authorizationMappingNode = UtilClass.getVertexById(authorizationMappingId,
            VertexLabelConstants.AUTHORIZATION_MAPPING);
      }
      catch (NotFoundException e) {
        continue;
      }
      authorizationMappingMap.put(IConfigEntityInformationModel.ID, authorizationMappingId);
      authorizationMappingMap.put(IConfigEntityInformationModel.LABEL, UtilClass.getValueByLanguage(authorizationMappingNode, IMapping.LABEL));
      authorizationMappingMap.put(IConfigEntityInformationModel.ICON, null);
      authorizationMappingMap.put(IConfigEntityInformationModel.TYPE, authorizationMappingNode.getProperty(IMapping.TYPE));
      authorizationMappingMap.put(IConfigEntityInformationModel.CODE, authorizationMappingNode.getProperty(IMapping.CODE));
      referencedAuthorizationMappings.put(authorizationMappingId, authorizationMappingMap);
    }
  }
  
  public static List<String> getAuthorizationMapping(Vertex profileNode)
  {
    List<String> propertyMappingIds = new ArrayList<>();
    Iterable<Vertex> propertyMappings = profileNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_AUTHORIZATION_MAPPING_LINK);
    
    for (Vertex propertyMapping : propertyMappings) {
      String propertyMappingId = propertyMapping.getProperty(CommonConstants.CODE_PROPERTY);
      propertyMappingIds.add(propertyMappingId);
    }
    
    return propertyMappingIds;
  }
  
  /********************** Endpoint validation for Endpoint Save API STARTS ************************************/

  /**
   * This method validate the EP 
   * for requested EP model
   * throws Exception in case request 
   * has unexpected Input in it
   * @param endpointMap
   * @throws FoundMultipleEntitiesException
   * @throws FoundPreExistingEntitiesException 
   * @throws LabelMustNotBeEmptyException 
   * @throws InvalidDataException 
   * @throws EmptyMandatoryFieldsException 
   */
  public static void validateEndpointRequest(Map<String, Object> endpointMap) throws FoundMultipleEntitiesException,
      FoundPreExistingEntitiesException, LabelMustNotBeEmptyException, InvalidDataException, EmptyMandatoryFieldsException
  {
    validateLabel((String) endpointMap.get(IEndpointModel.LABEL));
    List<String> physicalCatalogs = validatePhysicalCatalog(endpointMap);
    List<String> addedProcesses = (List<String>) endpointMap.get(ISaveEndpointModel.ADDED_PROCESSES);
    List<String> addedMappings = (List<String>) endpointMap.get(ISaveEndpointModel.ADDED_MAPPINGS);
    List<String> addedAuthorizatonMappings = (List<String>) endpointMap.get(ISaveEndpointModel.ADDED_AUTHORIZATION_MAPPINGS);
    validateMultipleEntity(physicalCatalogs, addedProcesses, addedMappings, addedAuthorizatonMappings);
    String supplierId = (String) endpointMap.get(CommonConstants.ID_PROPERTY);
    List<Map<String, Map<String, Object>>> processesToReturn = new ArrayList<Map<String, Map<String, Object>>>();
    fetchPreExistinEntityDetailsForEP(supplierId, processesToReturn);
    validateAddedDeletedEntity(endpointMap, addedProcesses, addedMappings, addedAuthorizatonMappings, processesToReturn);
  }
  

  /**
   * Validate EP Label
   * @param label
   * @throws LabelMustNotBeEmptyException
   */
  private static void validateLabel(String label) throws LabelMustNotBeEmptyException
  {
    if (label == null || label.isEmpty() || label.isBlank()) {
      throw new LabelMustNotBeEmptyException(CommonConstants.LABEL_PROPERTY + " can't be empty");
    }
  }
  
  /**
   * if any of the specified entity 
   * has > 1 entry throw Exception
   * @param physicalCatalogs
   * @param addedProcesses
   * @param addedMappings
   * @param addedAuthorizatonMappings
   * @throws FoundMultipleEntitiesException
   */
  private static void validateMultipleEntity(List<String> physicalCatalogs, List<String> addedProcesses, List<String> addedMappings,
      List<String> addedAuthorizatonMappings) throws FoundMultipleEntitiesException
  {
    if (physicalCatalogs.size() > 1|| (addedProcesses != null && addedProcesses.size() > 1)
        || (addedMappings != null && addedMappings.size() > 1)
        || (addedAuthorizatonMappings != null && addedAuthorizatonMappings.size() > 1)) {
      throw new FoundMultipleEntitiesException(
          String.format("Multiple entities addition not allowed for entities %s, %s , %s , %s", ISaveEndpointModel.PHYSICAL_CATALOGS,
              ISaveEndpointModel.ADDED_PROCESSES, ISaveEndpointModel.ADDED_MAPPINGS, ISaveEndpointModel.ADDED_AUTHORIZATION_MAPPINGS));
    }
  }
  
  /**
   * physicalCatalog Validation
   * @param endpointMap
   * @return
   * @throws InvalidDataException
   * @throws EmptyMandatoryFieldsException
   */
  public static List<String> validatePhysicalCatalog(Map<String, Object> endpointMap) throws InvalidDataException, EmptyMandatoryFieldsException
  {
    List<String> physicalCatalogs = (List<String>) endpointMap.get(IEndpointModel.PHYSICAL_CATALOGS);
    if (physicalCatalogs != null) {
      Boolean changeDetected = physicalCatalogs.retainAll(Constants.PHYSICAL_CATALOG_IDS);
      if (changeDetected) {
        throw new InvalidDataException("Invalid physical catalog input detected");
      }
    }
    else {
      physicalCatalogs = new ArrayList<>();
    }
    if (physicalCatalogs.isEmpty()) {
      throw new EmptyMandatoryFieldsException("physical catalog input field empty");
    }
    return physicalCatalogs;
  }
  
  /**
   * using Request Model's 
   * Added and deleted entity compare
   * with Already added data if any
   * @param endpointMap
   * @param addedProcesses
   * @param addedMappings
   * @param addedAuthorizatonMappings
   * @param processesToReturn
   */
  private static void validateAddedDeletedEntity(Map<String, Object> endpointMap, List<String> addedProcesses, List<String> addedMappings,
      List<String> addedAuthorizatonMappings, List<Map<String, Map<String, Object>>> processesToReturn)
  {
    processesToReturn.forEach(p -> {
      p.forEach((k, v) -> {
        compareAddedWithPreexistingEntity(endpointMap, addedProcesses, addedMappings, addedAuthorizatonMappings, v);
      });
    });
  }
  
  /**
   * After Data comparison prepare validation 
   * response in case of invalid data received
   * @param endpointMap
   * @param addedProcesses
   * @param addedMappings
   * @param addedAuthorizatonMappings
   * @param v
   */
  private static void compareAddedWithPreexistingEntity(Map<String, Object> endpointMap, List<String> addedProcesses,
      List<String> addedMappings, List<String> addedAuthorizatonMappings, Map<String, Object> mappingDetailsList)
  {
    mappingDetailsList.forEach((k1, v1) -> {
      // ADDED_PROCESS and DELETED_PROCESS validation
      if (!(addedProcesses == null || addedProcesses.isEmpty()) && ISaveEndpointModel.DELETED_PROCESSES.equals(k1)) {
        prepareValidationInfoForProcess(endpointMap, addedProcesses, k1, v1);
      }
      // ADDED_MAPPINGS and DELETED_MAPPINGS validation
      else if (!(addedMappings == null || addedMappings.isEmpty()) && ISaveEndpointModel.DELETED_MAPPINGS.equals(k1)) {
        // added and deleted has same entry then ignore that entity
        // trying to add new entity without deleting old one throw exception
        prepareValidationInfoForMapping(endpointMap, addedMappings, k1, v1);
      }
      // ADDED_AUTHORIZATION_MAPPINGS and DELETED_AUTHORIZATION_MAPPINGS
      // validation
      else if (!(addedAuthorizatonMappings == null || addedAuthorizatonMappings.isEmpty())
          && ISaveEndpointModel.DELETED_AUTHORIZATION_MAPPINGS.equals(k1)) {
        // added and deleted has same entry then ignore that entity
        // trying to add new entity without deleting old one throw exception
        prepareValidationInfoForAuth(endpointMap, addedAuthorizatonMappings, k1, v1);
      }
    });
  }
  
  /**
   * 
   * @param endpointMap
   * @param addedAuthorizatonMappings
   * @param entityName
   * @param entityValueList
   */
  private static void prepareValidationInfoForAuth(Map<String, Object> endpointMap, List<String> addedAuthorizatonMappings,
      String entityName, Object entityValueList)
  {
    // Added entity List matches with DB entity List
    if (addedAuthorizatonMappings.equals(entityValueList)) {
      throw new FoundPreExistingEntitiesException(String.format(VALIDATION_INFO_1, ISaveEndpointModel.ADDED_AUTHORIZATION_MAPPINGS));
    }
    else if (!addedAuthorizatonMappings.equals(endpointMap.get(entityName))
        && !(endpointMap.get(entityName).equals(entityValueList))) {
      throw new FoundPreExistingEntitiesException(String.format(VALIDATION_INFO_0, ISaveEndpointModel.ADDED_AUTHORIZATION_MAPPINGS,
          ISaveEndpointModel.DELETED_AUTHORIZATION_MAPPINGS));
    }
  }
 
  /**
   * 
   * @param endpointMap
   * @param addedMappings
   * @param entityName
   * @param entityValueList
   */
  private static void prepareValidationInfoForMapping(Map<String, Object> endpointMap, List<String> addedMappings, String entityName,
      Object entityValueList)
  {
    // Added entity List matches with DB entity List
    if (addedMappings.equals(entityValueList)) {
      throw new FoundPreExistingEntitiesException(String.format(VALIDATION_INFO_1, ISaveEndpointModel.ADDED_MAPPINGS));
    }
    else if (!addedMappings.equals(endpointMap.get(entityName)) && !(endpointMap.get(entityName).equals(entityValueList))) {
      throw new FoundPreExistingEntitiesException(
          String.format(VALIDATION_INFO_0, ISaveEndpointModel.ADDED_MAPPINGS, ISaveEndpointModel.DELETED_MAPPINGS));
    }
  }
  
  /**
   * In Request Model added and deleted has same entry then ignore that entity
   * OR trying to add new entity without deleting old one throw exception 
   * @param endpointMap
   * @param addedProcesses
   * @param entityName
   * @param entityValueList
   */
  private static void prepareValidationInfoForProcess(Map<String, Object> endpointMap, List<String> addedProcesses, String entityName,
      Object entityValueList)
  {
    // Added entity List matches with DB entity List
    if (addedProcesses.equals(entityValueList)) {
      throw new FoundPreExistingEntitiesException(String.format(VALIDATION_INFO_1, ISaveEndpointModel.ADDED_PROCESSES));
    }
    else if (!addedProcesses.equals(endpointMap.get(entityName)) && !(endpointMap.get(entityName).equals(entityValueList))) {
      throw new FoundPreExistingEntitiesException(
          String.format(VALIDATION_INFO_0, ISaveEndpointModel.ADDED_PROCESSES, ISaveEndpointModel.DELETED_PROCESSES));
    }
  }
  
  /**
   * For the Given EP in Request model
   * Fetch Pre Existing data to perform
   * Added/Deleted validations
   * Request received 
   * @param supplierId
   * @param processesToReturn
   */
  private static void fetchPreExistinEntityDetailsForEP(String supplierId, List<Map<String, Map<String, Object>>> processesToReturn)
  {
    String query = "select out('Profile_Process_Link').code as Profile_Process_Link , "
        + "out('Profile_Property_mapping_Link').code as Profile_Property_mapping_Link , "
        + "out('Has_Authorization_Mapping_Link').code as Has_Linked_Authorization_Mapping from endpoint where code in"
        + EntityUtil.quoteIt(supplierId);
    try (OResultSet searchResults = UtilClass.getDatabase().execute(QUERY_LANGUAGE_SQL, query);) {
      while (searchResults.hasNext()) {
        Map<String, Object> endpointInfoMap = new HashMap<>();
        OResult row = searchResults.next();
        endpointInfoMap.put(ISaveEndpointModel.DELETED_PROCESSES,
            (List<String>) row.getProperty(RelationshipLabelConstants.PROFILE_PROCESS_LINK));
        endpointInfoMap.put(ISaveEndpointModel.DELETED_MAPPINGS,
            (List<String>) row.getProperty(RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK));
        endpointInfoMap.put(ISaveEndpointModel.DELETED_AUTHORIZATION_MAPPINGS,
            (List<String>) row.getProperty(RelationshipLabelConstants.HAS_LINKED_AUTHORIZATION_MAPPING));
        Map<String, Map<String, Object>> endpointResMap = new HashMap<>();
        endpointResMap.put(supplierId, endpointInfoMap);
        processesToReturn.add(endpointResMap);
      }
    }
  }
  
  /********************** Endpoint validation for Endpoint Save API ENDS ************************************/
}
