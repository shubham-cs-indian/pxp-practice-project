package com.cs.core.runtime.interactor.usecase.exporttask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.runtime.exporttask.IInitiateImportDiData;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.model.camunda.CamundaExecuteProcessModel;
import com.cs.core.runtime.interactor.model.camunda.ICamundaExecuteProcessModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.dataintegration.GetEndpointsAndOrganisationIdRequestModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdRequestModel;
import com.cs.core.runtime.interactor.model.dataintegration.IGetEndpointsAndOrganisationIdResponseModel;
import com.cs.core.runtime.interactor.model.dataintegration.IReceiveModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.cammunda.broadcast.IExecuteProcessStrategy;
import com.cs.core.runtime.strategy.usecase.dataintegration.IGetEndpointIdsOfCustomTypeProcessStrategy;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;

@Component
public class InitiateImportDiData extends AbstractRuntimeInteractor<IModel, IModel>
    implements IInitiateImportDiData {
  
  @Autowired
  protected TransactionThreadData   transactionThread;
  
  @Autowired
  protected ISessionContext         context;
  
  @Autowired
  protected IGetEndpointIdsOfCustomTypeProcessStrategy getEndpointIdsOfCustomTypeProcessStrategy;
  
  @Autowired
  protected IExecuteProcessStrategy executeProcessStrategy;
  
  @SuppressWarnings("unchecked")
  @Override
  public IModel executeInternal(IModel dataModel) throws Exception
  {
    IReceiveModel iReceiveModel = (IReceiveModel) dataModel;
    
    Map<String, Object> data = ObjectMapperUtil.readValue(iReceiveModel.getData(), Map.class);
    List<String> processids = (List<String>) data.get(ProcessConstants.PROCESS_IDS);
    
    IGetEndpointsAndOrganisationIdResponseModel responseModel = getEndpointIdsOfCustomTypeProcessStrategy(
        processids);
    Map<String, Object> processDefinitionInfo = responseModel.getProcessDefinitionInfo();
    String organisationId = responseModel.getOrganisationId();
    
    if (processDefinitionInfo.isEmpty()) {
      System.out.println("***** no endpoint found for : " + processids);
    }
    
    for (String processDefinitionId : processDefinitionInfo.keySet()) {
      setTransactionData(organisationId);
      Map<String, Object> eventParameters = prepareEventParameters(data, processDefinitionInfo,
          organisationId, processDefinitionId);
      
      ICamundaExecuteProcessModel camundaExecuteProcessModel = new CamundaExecuteProcessModel(
          processDefinitionId, null, eventParameters);
      executeProcessStrategy.execute(camundaExecuteProcessModel);
    }
    
    return null;
  }
  
  @SuppressWarnings("unchecked")
  private Map<String, Object> prepareEventParameters(Map<String, Object> data,
      Map<String, Object> processDefinitionInfo, String organisationId, String processDefinitionId)
  {
    Map<String, String> processDefinitionInfoMap = (Map<String, String>) processDefinitionInfo
        .get(processDefinitionId);
    Map<String, Object> infoMap = (Map<String, Object>) processDefinitionInfo
        .get(processDefinitionId);
    Map<String, Object> eventParameters = new HashMap<String, Object>();
    eventParameters.put(ProcessConstants.BOARDING_TYPE, ProcessConstants.DI_ONBOARDING_PROCESS);
    eventParameters.put(ProcessConstants.PROCESS_ID,
        processDefinitionInfoMap.get(ProcessConstants.PROCESS_ID));
    eventParameters.put(ProcessConstants.ENDPOINT_ID,
        processDefinitionInfoMap.get(ProcessConstants.ENDPOINT_ID));
    eventParameters.put(ProcessConstants.DATA, data);
    eventParameters.put(ProcessConstants.PHYSICAL_CATALOG_ID,
        ProcessConstants.DATA_INTEGRATION_CATALOG_IDS);
    eventParameters.put(ProcessConstants.PROCESS_TYPE, CommonConstants.JMS_ONBOARDING_PROCESS);
    eventParameters.put(ProcessConstants.ORGANIZATION_ID, organisationId);
    eventParameters.put(ProcessConstants.LOGICAL_CATALOG_ID, "-1");
    eventParameters.put(ProcessConstants.PROCESS_ENTITY_ID,
        infoMap.get(ProcessConstants.PROCESS_ID));
    
    return eventParameters;
  }
  
  private IGetEndpointsAndOrganisationIdResponseModel getEndpointIdsOfCustomTypeProcessStrategy(
      List<String> processids) throws Exception
  {
    IGetEndpointsAndOrganisationIdRequestModel getDataModel = new GetEndpointsAndOrganisationIdRequestModel();
    getDataModel.setProcessIds(processids);
    getDataModel.setUserId(context.getUserId());
    
    IGetEndpointsAndOrganisationIdResponseModel responseModel = getEndpointIdsOfCustomTypeProcessStrategy.execute(getDataModel);
    return responseModel;
  }
  
  private void setTransactionData(String organisationId)
  {
    TransactionData transactionData = transactionThread.getTransactionData();
    transactionData.setPhysicalCatalogId(ProcessConstants.DATA_INTEGRATION_CATALOG_IDS);
    transactionData.setOrganizationId(organisationId);
    transactionData.setLogicalCatalogId("-1");
    transactionThread.setTransactionData(transactionData);
  }
}
