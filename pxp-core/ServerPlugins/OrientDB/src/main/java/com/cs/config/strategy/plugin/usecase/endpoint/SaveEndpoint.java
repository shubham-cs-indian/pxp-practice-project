package com.cs.config.strategy.plugin.usecase.endpoint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.endpoint.util.EndpointUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.attribute.BulkSaveAttributeFailedException;
import com.cs.core.config.interactor.exception.profile.ProfileNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointForGridModel;
import com.cs.core.config.interactor.model.endpoint.IGetGridEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings({ "unchecked" })
public class SaveEndpoint extends AbstractOrientPlugin {
  
  public static List<String> propertiesToExclude = Arrays.asList(ISaveEndpointModel.ADDED_PROCESSES, ISaveEndpointModel.DELETED_PROCESSES,
      ISaveEndpointModel.ADDED_JMS_PROCESSES, ISaveEndpointModel.DELETED_JMS_PROCESSES, ISaveEndpointModel.ADDED_MAPPINGS,
      ISaveEndpointModel.DELETED_MAPPINGS, ISaveEndpointModel.ADDED_AUTHORIZATION_MAPPINGS,
      ISaveEndpointModel.DELETED_AUTHORIZATION_MAPPINGS, ISaveEndpointModel.DELETED_PROCESSES, ISaveEndpointModel.SYSTEM_ID,
      ISaveEndpointModel.ADDED_SYSTEM_ID, ISaveEndpointModel.DELETED_SYSTEM_ID, ISaveEndpointModel.ADDED_DASHBOARD_TAB_ID,
      ISaveEndpointModel.DELETED_DASHBOARD_TAB_ID, IEndpointModel.PROCESSES, IEndpointModel.JMS_PROCESSES, IEndpointModel.SYSTEM_ID,
      IEndpointModel.AUTHORIZATION_MAPPING, IEndpointModel.MAPPINGS,
      IEndpointModel.ENDPOINT_TYPE);
  
  public SaveEndpoint(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveEndpoint/*" };
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> listOfEndpoints = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> listOfSuccessSavedEndpoints = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    for (Map<String, Object> endpointMap : listOfEndpoints) {
      try {    
        //added for Validations to be performed on Request Model
        EndpointUtils.validateEndpointRequest(endpointMap);
        String supplierId = (String) endpointMap.get(CommonConstants.ID_PROPERTY);
        
        Vertex endpointNode = null;
        try {
          endpointNode = UtilClass.getVertexById(supplierId, VertexLabelConstants.ENDPOINT);
        }
        catch (NotFoundException e) {
          throw new ProfileNotFoundException();
        }
        
        UtilClass.saveNode(endpointMap, endpointNode, propertiesToExclude);
        EndpointUtils.saveProcesses(endpointNode, endpointMap, failure);
        EndpointUtils.manageAddedSystemId(endpointMap, endpointNode);
        EndpointUtils.manageDeletedSystemId(endpointMap, endpointNode);
        TabUtils.manageAddedDashboardTabId(endpointMap, endpointNode);
        TabUtils.manageDeletedDashboardTabId(endpointMap, endpointNode);
        
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Map<String, Object> profileMap = UtilClass.getMapFromVertex(new ArrayList<>(),
            endpointNode);
        returnMap.put(IGetEndpointForGridModel.ENDPOINT, profileMap);
        EndpointUtils.getMapFromProfileNode(endpointNode, returnMap, new ArrayList<>());
        listOfSuccessSavedEndpoints.add(returnMap);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    
    if (listOfSuccessSavedEndpoints.isEmpty()) {
      throw new BulkSaveAttributeFailedException(failure.getExceptionDetails(),
          failure.getDevExceptionDetails());
    }
    
    UtilClass.getGraph()
        .commit();
    Map<String, Object> successMap = new HashMap<>();
    successMap.put(IGetGridEndpointsResponseModel.ENDPOINTS_LIST, listOfSuccessSavedEndpoints);
    EndpointUtils.fillReferencedConfigDetails(listOfSuccessSavedEndpoints, successMap);
    
    Map<String, Object> bulkSaveEndpointsResponse = new HashMap<String, Object>();
    bulkSaveEndpointsResponse.put(IBulkSaveEndpointsResponseModel.SUCCESS, successMap);
    bulkSaveEndpointsResponse.put(IBulkSaveEndpointsResponseModel.FAILURE, failure);
    return bulkSaveEndpointsResponse;
  }

}

