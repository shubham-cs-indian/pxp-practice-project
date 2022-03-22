package com.cs.export.config.strategy.plugin.usecase.user;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.base.organization.OrganizationUtil;
import com.cs.config.strategy.plugin.usecase.endpoint.AbstractGetEndpointsForDashboard;
import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.endpoint.IEndpoint;
import com.cs.core.config.interactor.exception.endpoint.EndpointNotFoundException;
import com.cs.core.config.interactor.exception.processevent.ProcessEventNotFoundException;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndointsInfoModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessExportEndpointModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.OnboardingConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dataintegration.IDataIntegrationRequestModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointIdsRequestModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.workflow.base.EventType;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetEndpointIdsOfCustomTypeProcessForUser extends AbstractGetEndpointsForDashboard {
  
  public GetEndpointIdsOfCustomTypeProcessForUser(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> endPointIds = new ArrayList<String>();
    Map<String, Object> returnMap = new HashMap<String, Object>();
    
    try {
      
      String physicalCatalogId = requestMap.get(IGetEndpointIdsRequestModel.PHYSICAL_CATALOG_ID)
          .toString();
      String triggeringType = requestMap.get(IGetEndpointIdsRequestModel.TRIGGERING_TYPE)
          .toString();
      Map<String, Object> requestMapForDashbaord = new HashMap<String, Object>();
      requestMapForDashbaord.put(IDataIntegrationRequestModel.USER_ID,
          requestMap.get(IGetEndpointIdsRequestModel.USER_ID));
      requestMapForDashbaord.put(IDataIntegrationRequestModel.DASHBOARD_TAB_ID,
          SystemLevelIds.DEFAULT_DATA_INTEGRATION_TAB_ID);
      requestMapForDashbaord.put(IDataIntegrationRequestModel.FROM, 0);
      requestMapForDashbaord.put(IDataIntegrationRequestModel.SIZE, 100);
      
      List<String> modifiedPropertyIds = (List<String>) requestMap
          .get(IGetEndpointIdsRequestModel.ATTRIBUTE_IDS);
      modifiedPropertyIds
          .addAll((List<String>) requestMap.get(IGetEndpointIdsRequestModel.TAG_IDS));
      List<String> modifiedTaxonomyIds = (List<String>) requestMap
          .get(IGetEndpointIdsRequestModel.TAXONOMY_IDS);
      List<String> modifiedKlassIds = (List<String>) requestMap
          .get(IGetEndpointIdsRequestModel.KLASS_IDS);
      
      Vertex roleNode = RoleUtils
          .getRoleFromUser((String) requestMap.get(IGetEndpointIdsRequestModel.USER_ID));
      Vertex organizationNode = OrganizationUtil.getOrganizationNodeForRole(roleNode);
      String organizationRid = organizationNode.getProperty(CommonConstants.CODE_PROPERTY)
          .toString();
      Iterable<Vertex> systems = organizationNode.getVertices(Direction.OUT,
          RelationshipLabelConstants.HAS_SYSTEM_ORAGNIZATION);
      String systemID = null;
      if (systems.iterator()
          .hasNext()) {
        systemID = systems.iterator()
            .next()
            .getProperty(CommonConstants.CODE_PROPERTY);
      }
      
      Map<String, Object> endpointDataMap = (Map<String, Object>) getEndpointsByDashboardIdOrSystemId(
          requestMapForDashbaord);
      List<Map<String, Object>> endpointsList = (List<Map<String, Object>>) endpointDataMap
          .get(IGetEndointsInfoModel.ENDPOINTS);
      
      for (Map<String, Object> endpoint : endpointsList) {
        String endpointId = endpoint.get(IIdLabelTypeModel.ID)
            .toString();
        
        if (!(endpoint.get(IIdLabelTypeModel.TYPE)
            .equals(OnboardingConstants.ONBOARDING_ENDPOINT_TYPE))) {
          HashMap<String, Object> endpointMap = (HashMap<String, Object>) getEndpoint(endpointId)
              .get(IGetEndpointForGridModel.ENDPOINT);
          // endpointMap.get(IGetEndpointModel.ENDPOINT);
          List<String> physicalCatalogs = (List<String>) endpointMap
              .get(IEndpointModel.PHYSICAL_CATALOGS);
          if (physicalCatalogs.contains(physicalCatalogId)) {
            endPointIds.add(endpointId);
          }
        }
      }
      
      returnMap.put(IGetProcessExportEndpointModel.ENDPOINTS,
          getEndpointIdsWithCustomTypeProcessEvent(endPointIds, physicalCatalogId, triggeringType,
              modifiedPropertyIds, modifiedTaxonomyIds, modifiedKlassIds));
      returnMap.put(IGetProcessExportEndpointModel.ORGANIZATION, organizationRid);
      returnMap.put(IGetProcessExportEndpointModel.SYSTEM, systemID);
      returnMap.put(IGetProcessExportEndpointModel.ENDPOINT_DETAILS,
          getEndpointsDetials(endpointsList, endPointIds));
    }
    catch (Exception e) {
      throw new EndpointNotFoundException();
    }
    
    return returnMap;
  }
  
  public HashMap<String, Object> getEndpoint(String profileId) throws Exception
  {
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    OrientGraph graph = UtilClass.getGraph();
    try {
      Vertex profileNode = UtilClass.getVertexByIndexedId(profileId, VertexLabelConstants.ENDPOINT);
      Map<String, Object> profileMap = UtilClass.getMapFromVertex(new ArrayList<>(), profileNode);
      returnMap.put(IGetEndpointForGridModel.ENDPOINT, profileMap);
      // EndpointUtils.getMapFromProfileNode(profileNode, returnMap, new
      // ArrayList<>());
    }
    catch (NotFoundException e) {
      throw new ProfileNotFoundException();
    }
    return returnMap;
  }
  
  private List<String> getEndpointIdsWithCustomTypeProcessEvent(List<String> endpointsIds,
      String physicalCatalogId, String triggeringType, List<String> modifiedPropertyIds,
      List<String> modifiedTaxonomyIds, List<String> modifiedKlassIds) throws Exception
  {
    List<String> endpointIdsToRemove = new ArrayList<String>();
    for (String endpointsId : endpointsIds) {
      OrientGraph graph = UtilClass.getGraph();
      try {
        Vertex profileNode = UtilClass.getVertexByIndexedId(endpointsId,
            VertexLabelConstants.ENDPOINT);
        List<String> processIds = EndpointUtils.getProcesses(profileNode);
        getCustomTypeProcessesEventsForEndpoint(processIds, endpointIdsToRemove, endpointsId,
            physicalCatalogId, triggeringType);
        if (!endpointIdsToRemove.contains(endpointsId)) {
          List<String> propertyIdsToFilter = EndpointUtils.getEntities(profileNode,
              RelationshipLabelConstants.HAS_LINKED_ATTRIBUTES_FOR_ENDPOINT);
          propertyIdsToFilter.addAll(EndpointUtils.getEntities(profileNode,
              RelationshipLabelConstants.HAS_LINKED_TAGS_FOR_ENDPOINT));
          List<String> taxonomyIdsToFilter = EndpointUtils.getEntities(profileNode,
              RelationshipLabelConstants.HAS_LINKED_TAXONOMIES_FOR_ENDPOINT);
          List<String> klassIdsToFilter = EndpointUtils.getEntities(profileNode,
              RelationshipLabelConstants.HAS_LINKED_KLASSES_FOR_ENDPOINT);
          if (!(validateCondition(modifiedPropertyIds, propertyIdsToFilter)
              && validateCondition(modifiedKlassIds, klassIdsToFilter)
              && validateCondition(modifiedTaxonomyIds, taxonomyIdsToFilter))) {
            endpointIdsToRemove.add(endpointsId);
          }
        }
      }
      catch (NotFoundException e) {
        throw new ProfileNotFoundException();
      }
    }
    endpointsIds.removeAll(endpointIdsToRemove);
    return endpointsIds;
  }
  
  private Boolean validateCondition(List<String> requestIds, List<String> selectedIds)
  {
    if (selectedIds.isEmpty()) {
      return true;
    }
    else if (!Collections.disjoint(selectedIds, requestIds)) {
      return true;
    }
    return false;
  }
  
  private List<String> getCustomTypeProcessesEventsForEndpoint(List<String> processIds,
      List<String> endpointIdsToRemove, String endpointId, String physicalCatalogId,
      String triggeringType) throws Exception
  {
    Boolean customTypeEventFound = false;
    for (String processEventId : processIds) {
      try {
        Vertex processEventNode = UtilClass.getVertexByIndexedId(processEventId,
            VertexLabelConstants.PROCESS_EVENT);
        Map<String, Object> processEventMap = UtilClass.getMapFromVertex(new ArrayList<>(),
            processEventNode);
        String triggeringEventType = (String) processEventMap
            .get(IGetProcessEventModel.TRIGGERING_TYPE);
        if (processEventMap.get(IGetProcessEventModel.EVENT_TYPE)
            .equals(EventType.INTEGRATION) && triggeringEventType != null
            && triggeringEventType.equals(triggeringType)
            && (Boolean) processEventMap.get(IGetProcessEventModel.IS_EXECUTABLE)) {
          customTypeEventFound = true;
        }
      }
      catch (NotFoundException e) {
        throw new ProcessEventNotFoundException();
      }
    }
    
    if (!customTypeEventFound) {
      endpointIdsToRemove.add(endpointId);
    }
    
    return endpointIdsToRemove;
  }
  
  private Map<String, Object> getEndpointsDetials(List<Map<String, Object>> endpointsList,
      List<String> endPointIds) throws Exception
  {
    Map<String, Object> endpointIdVsDetailsMap = new HashMap<>();
    Iterable<Vertex> vertices = UtilClass.getVerticesByIds(endPointIds,
        VertexLabelConstants.ENDPOINT);
    for (Vertex endpoint : vertices) {
      Map<String, Object> endpointMap = new HashMap<>();
      endpointIdVsDetailsMap.put(endpoint.getProperty(CommonConstants.CODE_PROPERTY),
          endpointMap);
    }
    return endpointIdVsDetailsMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetEndpointIdsOfCustomTypeProcessForUser/*" };
  }
}
