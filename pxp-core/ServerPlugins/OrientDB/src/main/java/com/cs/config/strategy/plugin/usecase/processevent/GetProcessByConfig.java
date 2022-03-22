package com.cs.config.strategy.plugin.usecase.processevent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.configuration.base.IConfigEntity;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdRequestModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.di.workflow.trigger.IWorkflowParameterModel;
import com.cs.di.workflow.trigger.IWorkflowTriggerModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.ActionSubTypes;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.BusinessProcessActionType;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.Usecase;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.EventType;
import com.cs.workflow.base.WorkflowType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.executor.OResult;
import com.orientechnologies.orient.core.sql.executor.OResultSet;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;

/**
 * Based on triggering criteria provided
 * select the  Workflow process Instance Id to execute
 * from Process_Event table
 *
 * @author mayuri.wankhade
 */
@SuppressWarnings("unchecked")
public class GetProcessByConfig extends AbstractOrientPlugin {

    private static final String WORKFLOW_TYPE_CONDITION = ISaveProcessEventModel.WORKFLOW_TYPE
            + " != \"" + WorkflowType.SCHEDULED_WORKFLOW + "\"";
    private static final String IS_EXECUTABLE_CONDITION = ISaveProcessEventModel.IS_EXECUTABLE
            + " = true";
    private static final String LINKED_KLASSES_FOR_PROCESS = "out('"
            + RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_PROCESS + "')."
            + CommonConstants.CODE_PROPERTY;
    private static final String LINKED_NON_NATURE_KLASSES_FOR_PROCESS = "out('"
            + RelationshipLabelConstants.HAS_LINKED_NON_NATURE_KLASSES_FOR_PROCESS + "')."
            + CommonConstants.CODE_PROPERTY;
    private static final String LINKED_TAXONOMIES_FOR_PROCESS = "out('"
            + RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_PROCESS + "')."
            + CommonConstants.CODE_PROPERTY;
    private static final String LINKED_ATTRIBUTES_FOR_PROCESS = "out('"
            + RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_PROCESS + "')."
            + CommonConstants.CODE_PROPERTY;
    private static final String LINKED_TAGS_FOR_PROCESS = "out('"
            + RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_PROCESS + "')."
            + CommonConstants.CODE_PROPERTY;
    private static final String LINKED_ENDPOINTS_FOR_PROCESS = "IN('"
            + RelationshipLabelConstants.PROFILE_PROCESS_LINK + "')."
            + CommonConstants.CODE_PROPERTY;

    private static final String PROCESSES_WITH_EMPTY_TAXONOMIES = LINKED_TAXONOMIES_FOR_PROCESS
            + " = []";
    private static final String PROCESSES_WITH_EMPTY_ATTRIBUTES = LINKED_ATTRIBUTES_FOR_PROCESS
            + " = []";
    private static final String PROCESSES_WITH_EMPTY_TAGS = LINKED_TAGS_FOR_PROCESS
            + " = []";
    private static final String PROCESSES_WITH_EMPTY_ENDPOINTS = LINKED_ENDPOINTS_FOR_PROCESS
            + " = []";
    private static final String PROCESSES_WITH_EMPTY_CATALOGS = ISaveProcessEventModel.PHYSICAL_CATALOG_IDS
            + " = []";
    private static final String PROCESSES_WITH_EMPTY_ACTION_SUBTYPE = ISaveProcessEventModel.ACTION_SUB_TYPE
            + " = []";
    private static final String PROCESSES_WITH_EMPTY_NON_NATURE_TYPE = LINKED_NON_NATURE_KLASSES_FOR_PROCESS
            + " = []";
    private static final String PROCESSES_WITH_EMPTY_ORGANIZATIONS = IProcessEvent.ORGANIZATIONS_IDS + " = []";
    private static final String PROCESSES_WITH_EMPTY_ATTRIBUTES_AND_TAGS = "("
            + PROCESSES_WITH_EMPTY_ATTRIBUTES + " and " + PROCESSES_WITH_EMPTY_TAGS + ")";
    private static final String PROCESSES_WITH_EMPTY_TAXONOMIES_AND_NON_NATURE_TYPE = "("
            + PROCESSES_WITH_EMPTY_TAXONOMIES + " and " + PROCESSES_WITH_EMPTY_NON_NATURE_TYPE + ")";
    private static final String PROCESSES_WITH_EMPTY_USECASE = ISaveProcessEventModel.USECASES
        + " = []";

    private static final String GET_KLASS_NATURE_FLAG = "select code , isNature from RootKlass where code ";

    private static final String QUERY_LANGUAGE_SQL = "sql";

    public GetProcessByConfig(OServerCommandConfiguration iConfiguration) {
        super(iConfiguration);
    }

    @Override
    public String[] getNames() {
        return new String[]{"POST|GetProcessByConfig/*"};
    }

  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<String, Object>();
    List<Map<String, Object>> processesToReturn = new ArrayList<>();
   
    String endpointId = (String) requestMap.get(ProcessConstants.ENDPOINT_ID);
    List<String> processIds = (List<String>) requestMap.get(IGetEndpointsAndOrganisationIdRequestModel.PROCESS_IDS);
    String eventType = (String) requestMap.get(ISaveProcessEventModel.EVENT_TYPE);
    if (((endpointId == null || endpointId.isEmpty())
        && (processIds == null || processIds.isEmpty()))
        || (eventType != null && eventType.equals(EventType.BUSINESS_PROCESS.name()))) {
      // When Workflow eventType is Business
      fillWorkflowConfiguration(requestMap, processesToReturn);
    }
    else if (processIds != null && !processIds.isEmpty()) {
      // When Workflow Type is JMS Workflow.
      fillJMSWorkflowInfo(processesToReturn, processIds);
    }
    else if (endpointId != null && !endpointId.isEmpty()) {
      // When Workflow eventType is Integration.
      fillWorkflowConfiguration(requestMap, processesToReturn);
      if(!processesToReturn.isEmpty()) {
        Map<String, Object> workflowInfoMap = (Map<String, Object>) processesToReturn.get(0).get(IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP);
        Vertex  endpointVertex = UtilClass.getVertexById(endpointId, VertexLabelConstants.ENDPOINT);
        fillEndpointConfiguration(workflowInfoMap, endpointVertex);
      }
    }
    
    responseMap.put(IListModel.LIST, processesToReturn);
    return responseMap;
  }

  /**
   * Read the workflow info from orient and fill it into the map.
   * 
   * @param requestMap
   * @param processesToReturn
   */
  private void fillWorkflowConfiguration(Map<String, Object> requestMap, List<Map<String, Object>> processesToReturn)
  {
    String query = getEventQuery(requestMap);
    if (query != null) {
      try (OResultSet searchResults = UtilClass.getDatabase().execute(QUERY_LANGUAGE_SQL, query, Arrays.asList(requestMap.keySet()));) {
        while (searchResults.hasNext()) {
          Map<String, String> workflowInfoMap = new HashMap<>();
          OResult row = searchResults.next();
          workflowInfoMap.put(IProcessEvent.PROCESS_DEFINITION_ID, (String) row.getProperty(ISaveProcessEventModel.PROCESS_DEFINITION_ID));
          Map<String, Object> WorkflowParameterMap = new HashMap<>();
          WorkflowParameterMap.put(IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP, workflowInfoMap);
          processesToReturn.add(WorkflowParameterMap);
        }
      }
    }
  }
  
  /**
   * Read the information from orient for JMS workflow type.
   * Get the endpoint information attached to given process. 
   *  
   * @param processesToReturn
   * @param processIds
   * @throws Exception
   */
  private void fillJMSWorkflowInfo(List<Map<String, Object>> processesToReturn, List<String> processIds) throws Exception
  {
    for (String processId : processIds) {
      Map<String, Object> workflowInfo = new HashMap<>();
      String query = "select from process_event where " + IProcessEvent.PROCESS_DEFINITION_ID + " ='" + processId + "'";
      Iterable<Vertex> processNodes = UtilClass.getGraph().command(new OCommandSQL(query)).execute();
      Iterator<Vertex> processNodeIterator = processNodes.iterator();
      if (processNodeIterator.hasNext()) {
        Vertex processNode = processNodeIterator.next();
        workflowInfo.put(IProcessEvent.PROCESS_DEFINITION_ID, (String) processNode.getProperty(ISaveProcessEventModel.PROCESS_DEFINITION_ID));
        workflowInfo.put(IProcessEvent.ORGANIZATIONS_IDS, processNode.getProperty(ISaveProcessEventModel.ORGANIZATIONS_IDS));
        Iterator<Vertex> endpointIterator = processNode.getVertices(Direction.IN, RelationshipLabelConstants.PROFILE_JMS_PROCESS_LINK)
            .iterator();
        if (endpointIterator.hasNext()) {
          Vertex endpoint = endpointIterator.next();
          fillEndpointConfiguration(workflowInfo, endpoint);
          Map<String, Object> WorkflowParameterMap = new HashMap<>();
          WorkflowParameterMap.put(IWorkflowParameterModel.WORKFLOW_PARAMETER_MAP, workflowInfo);
          processesToReturn.add(WorkflowParameterMap);
        }
      }
    }
  }
  
  /**
   * Read the endpoint info from orient and fill it into the map.
   * 
   * @param workflowInfo
   * @param endpointVertex
   * @throws Exception
   */
  private void fillEndpointConfiguration(Map<String, Object> workflowInfo, Vertex endpointVertex) throws Exception
  {
    workflowInfo.put(CommonConstants.ENDPOINT_ID, endpointVertex.getProperty(CommonConstants.CODE_PROPERTY));
    workflowInfo.put(CommonConstants.PHYSICAL_CATALOG_ID, ((List<String>)endpointVertex.getProperty(IEndpoint.PHYSICAL_CATALOGS)).get(0));
    Iterator<Vertex> mappingIterator = endpointVertex.getVertices(Direction.OUT, RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK)
        .iterator();
    if (mappingIterator.hasNext()) {
      workflowInfo.put(IEndpointModel.MAPPINGS, mappingIterator.next().getProperty(CommonConstants.CODE_PROPERTY));
    }
    
    Iterator<Vertex> authorizationIterator = endpointVertex
        .getVertices(Direction.OUT, RelationshipLabelConstants.HAS_AUTHORIZATION_MAPPING_LINK).iterator();
    
    if (authorizationIterator.hasNext()) {
      workflowInfo.put(IEndpointModel.AUTHORIZATION_MAPPING, authorizationIterator.next().getProperty(CommonConstants.CODE_PROPERTY));
    }
  }

    private String getEventQuery(Map<String, Object> requestMap) {
        String returnQuery = "select from " + VertexLabelConstants.PROCESS_EVENT + " where "
                + IS_EXECUTABLE_CONDITION + " and " + WORKFLOW_TYPE_CONDITION;

        switch(EventType.valueOf((String) requestMap.get(ISaveProcessEventModel.EVENT_TYPE))) {
            case BUSINESS_PROCESS:
                returnQuery = getBusinessProcessQuery(requestMap, returnQuery);
                break;

            case APPLICATION:
                returnQuery = getApplicationEventQuery(requestMap, returnQuery);
                break;

            case INTEGRATION:
                returnQuery = getIntegrationEventQuery(requestMap, returnQuery);
                break;

            default:
                return null;
        }
        return returnQuery;
    }

    /**
     *
     * @param requestMap
     * @param returnQuery
     * @return
     */
    private String getApplicationEventQuery(Map<String, Object> requestMap, String returnQuery) {
        String triggeringType = (String) requestMap.get(ISaveProcessEventModel.TRIGGERING_TYPE);
        returnQuery += " and " + ISaveProcessEventModel.EVENT_TYPE + " = \"" + EventType.APPLICATION + "\""
                + " and " + ISaveProcessEventModel.TRIGGERING_TYPE + " = '" + triggeringType + "'";
        return returnQuery;
    }

    /**
     *
     * @param requestMap
     * @param returnQuery
     * @return
     */
    private String getIntegrationEventQuery(Map<String, Object> requestMap, String returnQuery) {
        returnQuery = getEndpointsOrCatalogsCondition(requestMap, returnQuery);

        String triggeringType = (String) requestMap.get(ISaveProcessEventModel.TRIGGERING_TYPE);
        returnQuery += " and " + ISaveProcessEventModel.EVENT_TYPE + " = \"" + EventType.INTEGRATION + "\""
        + " and " + ISaveProcessEventModel.TRIGGERING_TYPE + " = '" + triggeringType + "' and ("
        + IProcessEvent.ORGANIZATIONS_IDS + " CONTAINS "
            + EntityUtil.quoteIt(requestMap.get(IWorkflowTriggerModel.ORGANIZATION_ID)) + " or "
            + PROCESSES_WITH_EMPTY_ORGANIZATIONS + ")";
        return returnQuery;
    }

    /**
     * @param requestMap
     * @return
     */
    private String getBusinessProcessQuery(Map<String, Object> requestMap, String returnQuery) {
        returnQuery = getEndpointsOrCatalogsCondition(requestMap, returnQuery);
        String organizationId= (String) requestMap.get(IWorkflowTriggerModel.ORGANIZATION_ID);
        String triggeringType = (String) requestMap.get(ISaveProcessEventModel.TRIGGERING_TYPE);
        returnQuery += " and " + ISaveProcessEventModel.EVENT_TYPE + " = \"" + EventType.BUSINESS_PROCESS + "\""
                +" and " + ISaveProcessEventModel.TRIGGERING_TYPE + " = '" + triggeringType + "' and (" + IProcessEvent.ORGANIZATIONS_IDS + " CONTAINS "
                         + EntityUtil.quoteIt("stdo".equalsIgnoreCase(organizationId) ? "-1" : organizationId) + " or " + PROCESSES_WITH_EMPTY_ORGANIZATIONS + ")";

        List<String> nonNatureklassIds = new ArrayList<String>();
        BusinessProcessActionType actionType = BusinessProcessActionType.valueOf((String)requestMap.get(IBusinessProcessTriggerModel.BUSINESS_PROCESS_ACTION_TYPE));
        List<String> klassIds = (List<String>) requestMap.get(ISaveProcessEventModel.KLASS_IDS);
        Usecase usecase = Usecase.valueOf((String) requestMap.get(IBusinessProcessTriggerModel.USECASE));
        returnQuery = getKlassCondition(actionType, klassIds, returnQuery,nonNatureklassIds);
        returnQuery = getActionSubTypeCondition(actionType, nonNatureklassIds, requestMap, returnQuery);
        returnQuery = getUsecaseCondition(usecase, returnQuery);
        return returnQuery;
    }

    /**
     *
     * @param requestMap
     * @param returnQuery
     * @return
     */
    private String getEndpointsOrCatalogsCondition(Map<String, Object> requestMap, String returnQuery) {
        String endpointId = (String) requestMap.get(ProcessConstants.ENDPOINT_ID);
        String eventType = (String) requestMap.get(ISaveProcessEventModel.EVENT_TYPE);
        if (endpointId == null || endpointId.equalsIgnoreCase("null") || endpointId.isEmpty()
            || endpointId.equals("-1")
            || (eventType != null && eventType.equals(EventType.BUSINESS_PROCESS.name()))) {
            String physicalCatalogId = (String) requestMap.get(ProcessConstants.PHYSICAL_CATALOG_ID);
            returnQuery += " and (" +
                    ISaveProcessEventModel.PHYSICAL_CATALOG_IDS + " CONTAINS " + EntityUtil.quoteIt(physicalCatalogId) +
                    " or " + PROCESSES_WITH_EMPTY_CATALOGS +
                    ")";
        } else {
            returnQuery += " and " + LINKED_ENDPOINTS_FOR_PROCESS + " CONTAINS " + EntityUtil.quoteIt(endpointId);
        }
        return returnQuery;
    }

    /**
     * This method provides estracts Nature types for klassIds and append query for natureTypes
     *
     * @param klassIds
     * @param returnQuery
     * @return
     */
    private String getKlassCondition(BusinessProcessActionType actionType, List<String> klassIds, String returnQuery , List<String> nonNatureklassIds) {
        switch (actionType) {
            case AFTER_CREATE:
            case AFTER_SAVE:
                if (klassIds != null && !klassIds.isEmpty()) {
                    Map<String, Boolean> natureTypeMap = new HashMap<String, Boolean>();
                    String natureTypeByCode = GET_KLASS_NATURE_FLAG + " in " + EntityUtil.quoteIt(klassIds);
                    try (OResultSet searchResults = UtilClass.getDatabase().execute(QUERY_LANGUAGE_SQL, natureTypeByCode);) {
                        while (searchResults.hasNext()) {
                            OResult row = searchResults.next();
                            natureTypeMap.put(row.getProperty("code"), row.getProperty("isNature"));
                        }
                    }
                    // Nature Class Query
                    klassIds =
                            Optional.ofNullable(natureTypeMap.entrySet().stream().filter(p -> p.getValue()).map(p -> p.getKey()).collect(Collectors.toList()))
                                    .orElse(Collections.EMPTY_LIST);
                    //Non Nature Class selected
                    nonNatureklassIds.addAll(Optional.ofNullable(natureTypeMap.entrySet().stream().filter(p -> !p.getValue()).map(p -> p.getKey()).collect(Collectors.toList()))
                        .orElse(Collections.EMPTY_LIST));
                    if (!klassIds.isEmpty()) {
                        returnQuery += " and " + LINKED_KLASSES_FOR_PROCESS + " CONTAINSANY " + EntityUtil.quoteIt(klassIds);
                    }
                }
                break;
        }
        return returnQuery;
    }

    /**
     * @param nonNatureklassIds
     * @param requestMap
     * @param returnQuery
     * @return
     */
    private String getActionSubTypeCondition(BusinessProcessActionType actionType, List<String> nonNatureklassIds, Map<String, Object> requestMap, String returnQuery) {
        switch (actionType) {
            case AFTER_SAVE:
                List<String> taxonomyIds = (List<String>) requestMap.get(ISaveProcessEventModel.TAXONOMY_IDS);
                ActionSubTypes actionSubType = ActionSubTypes.valueOf((String)requestMap.get(ISaveProcessEventModel.ACTION_SUB_TYPE));
                List<String> attributeIds = (List<String>) requestMap.get(ISaveProcessEventModel.ATTRIBUTE_IDS);
                List<String> tagIds = (List<String>) requestMap.get(ISaveProcessEventModel.TAG_IDS);
                returnQuery += " and ( " + ISaveProcessEventModel.ACTION_SUB_TYPE + " CONTAINSANY ['" + actionSubType + "'] or "
                        + PROCESSES_WITH_EMPTY_ACTION_SUBTYPE + " ) ";
                switch (actionSubType) {
                        // Properties considered only for after properties save
                    case AFTER_PROPERTIES_SAVE:
                        returnQuery = getPropertiesCondition(attributeIds, tagIds, returnQuery);
                        returnQuery = getClassificationsCondition(taxonomyIds, nonNatureklassIds, returnQuery);
                        break;
                    case AFTER_CLASSIFICATION_SAVE:
                        returnQuery = getClassificationsCondition(taxonomyIds, nonNatureklassIds, returnQuery);
                        break;                      
                    default :
                        //AFTER_RELATIONSHIP_SAVE and AFTER_CONTEXT_SAVE no filter needed in query
                        break;
                }
                break;
        }
        return returnQuery;
    }

    /**
     * @param attributeIds
     * @param tagIds
     * @param returnQuery
     * @return
     */
    private String getPropertiesCondition(List<String> attributeIds,
                                          List<String> tagIds, String returnQuery) {
        returnQuery += " and (" +
                LINKED_ATTRIBUTES_FOR_PROCESS + " CONTAINSANY " + EntityUtil.quoteIt(attributeIds) +
                " or " + LINKED_TAGS_FOR_PROCESS + " CONTAINSANY " + EntityUtil.quoteIt(tagIds) +
                " or " + PROCESSES_WITH_EMPTY_ATTRIBUTES_AND_TAGS +
                ")";
        return returnQuery;
    }

    /**
     * @param taxonomyIds
     * @param nonNatureklassIds
     * @param returnQuery
     * @return
     */
    private String getClassificationsCondition(List<String> taxonomyIds, List<String> nonNatureklassIds, String returnQuery) {
        ArrayList<String> taxCodes = new ArrayList<String>();
        if (!taxonomyIds.isEmpty()) {
            String attrTaxCodesQuery = "select code from(traverse out('Child_Of') from (select from Attribution_Taxonomy where code CONTAINSANY "
                    + EntityUtil.quoteIt(taxonomyIds) + ") strategy BREADTH_FIRST) ";
            try (OResultSet attrTaxCodesResults = UtilClass.getDatabase().execute(QUERY_LANGUAGE_SQL, attrTaxCodesQuery);) {
                while (attrTaxCodesResults.hasNext()) {
                    OResult row = attrTaxCodesResults.next();
                    taxCodes.add((String) row.getProperty(IConfigEntity.CODE));
                }
            }
        }
        returnQuery += " and (" +
                LINKED_TAXONOMIES_FOR_PROCESS + " CONTAINSANY " + EntityUtil.quoteIt(taxCodes) +
                " or " + LINKED_NON_NATURE_KLASSES_FOR_PROCESS + " CONTAINSANY " + EntityUtil.quoteIt(nonNatureklassIds) +
                " or " + PROCESSES_WITH_EMPTY_TAXONOMIES_AND_NON_NATURE_TYPE +
                ")";
        return returnQuery;
    }
    
    private String getUsecaseCondition(Usecase usecase, String returnQuery) {
      returnQuery += " and ( " + ISaveProcessEventModel.USECASES + " CONTAINSANY ['" + usecase.toString() + "'] or "
          + PROCESSES_WITH_EMPTY_USECASE + " ) ";
      return returnQuery;
    }
}
