package com.cs.config.strategy.plugin.usecase.endpoint;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.processevent.CloneProcessEvents;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.exception.endpoint.BulkCloneEndpointFailedException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ICloneEndpointModel;
import com.cs.core.config.interactor.model.endpoint.ICopyWorkflowModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.di.config.interactor.model.initializeworflowevent.ICloneProcessEventModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.cs.workflow.base.WorkflowType;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CloneEndpoints extends AbstractOrientPlugin {
  
  public CloneEndpoints(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CloneEndpoints/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> endpointCloneModelsList = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSavedEndpoints = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ENDPOINT,
        CommonConstants.CODE_PROPERTY);
    
    for (Map<String, Object> endpointCloneModel : endpointCloneModelsList) {
      String fromEndPointId = (String) endpointCloneModel.get(ICloneEndpointModel.FROM_ENDPOINT_ID);
      
      try {
        Vertex srcEndpointNode = UtilClass.getVertexById(fromEndPointId,
            VertexLabelConstants.ENDPOINT);
        Map<String, Object> srcEndpointMap = UtilClass.getMapFromVertex(new ArrayList<>(),
            srcEndpointNode);
        
        srcEndpointMap.put(CommonConstants.CODE_PROPERTY,
            (String) endpointCloneModel.get(ICloneEndpointModel.TO_ENDPOINT_CODE));
        srcEndpointMap.put(IEndpointModel.LABEL,
            (String) endpointCloneModel.get(ICloneEndpointModel.TO_ENDPOINT_NAME));
        srcEndpointMap.put(CommonConstants.ID_PROPERTY, UtilClass.getUniqueSequenceId(vertexType));
        
        // creating a clone of source endpoint node
        Vertex clonedEndpointNode = UtilClass.createNode(srcEndpointMap, vertexType, CreateEndpoint.propertiesToExclude);
        // Process and link cloned workflows to cloned endpoint.
        List<Map<String, Object>> workflows = (List<Map<String, Object>>) endpointCloneModel.get(ICloneEndpointModel.WORKFLOWS_TO_COPY);
        for (Map<String, Object> workflow : workflows) {
          Map<String, Object> processEventMap = (Map<String, Object>) workflow.get(ICopyWorkflowModel.CLONE_WORKFLOW_SAVE_MODEL);
          if (processEventMap != null) {
            processEventMap.put(ICloneProcessEventModel.ORIGINAL_ENTITY_ID, workflow.get(ICopyWorkflowModel.FROM_WORKFLOW_ID));
            Vertex clonedProcessNode = CloneProcessEvents.cloneProcessEvent(processEventMap);
            // link cloned workflow to cloned endpoint
            if (clonedProcessNode.getProperty(IProcessEvent.WORKFLOW_TYPE).equals(WorkflowType.JMS_WORKFLOW.toString())) {
              clonedEndpointNode.addEdge(RelationshipLabelConstants.PROFILE_JMS_PROCESS_LINK, clonedProcessNode);
            }
            else {
              clonedEndpointNode.addEdge(RelationshipLabelConstants.PROFILE_PROCESS_LINK, clonedProcessNode);
            }
          }
        }
        
        // duplicated remaining source links in cloned endpoint
        cloneEdges(srcEndpointNode, clonedEndpointNode);
        
        UtilClass.getGraph()
            .commit();
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> profileMap = UtilClass.getMapFromVertex(new ArrayList<>(),
            clonedEndpointNode);
        returnMap.put(IGetEndpointForGridModel.ENDPOINT, profileMap);
        EndpointUtils.getMapFromProfileNode(clonedEndpointNode, returnMap, new ArrayList<>());
        listOfSuccessSavedEndpoints.add(returnMap);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, fromEndPointId, null);
      }
      
      if (listOfSuccessSavedEndpoints.isEmpty()) {
        throw new BulkCloneEndpointFailedException(failure.getExceptionDetails(),
            failure.getDevExceptionDetails());
      }
    }
    
    return prepareResponse(listOfSuccessSavedEndpoints, failure);
  }
  
  /**
   * @param sourceEndpointNode
   * @param clonedEndpointNode
   * @throws Exception
   */
  private void cloneEdges(Vertex sourceEndpointNode, Vertex clonedEndpointNode) throws Exception
  {
    UtilClass.copyEdgeWithoutProperties(sourceEndpointNode, clonedEndpointNode,
        RelationshipLabelConstants.PROFILE_PROPERTY_MAPPING_LINK, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(sourceEndpointNode, clonedEndpointNode,
        RelationshipLabelConstants.HAS_AUTHORIZATION_MAPPING_LINK, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(sourceEndpointNode, clonedEndpointNode,
        RelationshipLabelConstants.HAS_SYSTEM, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(sourceEndpointNode, clonedEndpointNode,
        RelationshipLabelConstants.HAS_DASHBOARD_TAB, Direction.OUT);
    UtilClass.copyEdgeWithoutProperties(sourceEndpointNode, clonedEndpointNode,
        RelationshipLabelConstants.HAS_ICON, Direction.OUT);
  }
  
  /**
   * @param listOfSuccessSavedEndpoints
   * @param failure
   * @return
   * @throws Exception
   */
  private Map<String, Object> prepareResponse(List<Map<String, Object>> listOfSuccessSavedEndpoints,
      IExceptionModel failure) throws Exception
  {
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetGridEndpointsResponseModel.ENDPOINTS_LIST, listOfSuccessSavedEndpoints);
    EndpointUtils.fillReferencedConfigDetails(listOfSuccessSavedEndpoints, successMap);
    
    Map<String, Object> bulkSaveEndpointsResponse = new HashMap<String, Object>();
    bulkSaveEndpointsResponse.put(IBulkSaveEndpointsResponseModel.SUCCESS, successMap);
    bulkSaveEndpointsResponse.put(IBulkSaveEndpointsResponseModel.FAILURE, failure);
    return bulkSaveEndpointsResponse;
  }
}
