package com.cs.di.config.businessapi.endpoint;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.CloneEndpointModel;
import com.cs.core.config.interactor.model.endpoint.CopyWorkflowModel;
import com.cs.core.config.interactor.model.endpoint.IBulkSaveEndpointsResponseModel;
import com.cs.core.config.interactor.model.endpoint.ICloneEndpointModel;
import com.cs.core.config.interactor.model.endpoint.ICopyWorkflowModel;
import com.cs.core.config.interactor.model.processevent.GetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IBulkSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.strategy.usecase.endpoint.ICloneEndpointsStrategy;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.IBulkUploadProcessToServerStrategy;
import com.cs.core.runtime.strategy.cammunda.broadcast.IDeleteProcessDefinationStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.ICloneProcessEventModel;
import com.cs.di.config.strategy.usecase.processevent.IGetProcessEventsByIdsStrategy;
import com.sun.net.httpserver.Authenticator.Failure;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Service
public class CloneEndpointsService extends
    AbstractCreateConfigService<IListModel<ICloneEndpointModel>, IBulkSaveEndpointsResponseModel>
    implements ICloneEndpointsService {
  
  private static final String PROCESS = "process";

  private static final String MODEL_NS = "http://www.omg.org/spec/BPMN/20100524/MODEL";

  @Autowired
  protected ICloneEndpointsStrategy            cloneEndpointStrategy;

  @Autowired
  protected IBulkUploadProcessToServerStrategy bulkUploadProcessToServerStrategy;

  @Autowired
  protected IDeleteProcessDefinationStrategy deleteProcessDefinationStrategy;

  @Autowired
  protected IGetProcessEventsByIdsStrategy getProcessEventsByIdsStrategy;

  @Override
  public IBulkSaveEndpointsResponseModel executeInternal(IListModel<ICloneEndpointModel> cloneEndpointsListModel) throws Exception
  {
    Map<String, Exception> processFailureMap = null;
    
    ICloneEndpointModel endpointToClone = cloneEndpointsListModel.getList().iterator().hasNext()
        ? cloneEndpointsListModel.getList().iterator().next()
        : new CloneEndpointModel();
    
    if (!endpointToClone.getWorkflowsToCopy().isEmpty()) {
      cloneWorkflowsLinkedWithEndpoints(cloneEndpointsListModel, endpointToClone, processFailureMap);
    }
    
    IBulkSaveEndpointsResponseModel cloneEndpointResponse = cloneEndpointStrategy.execute(cloneEndpointsListModel);
    IExceptionModel endpointFailure = cloneEndpointResponse.getFailure();
    
    fillProcessDeploymentFailure(processFailureMap, endpointFailure);
    return cloneEndpointResponse;
  }

  /**
   * Clone workflows linked with the endpoint
   * 
   * @param cloneEndpointsListModel
   * @param endpointToClone
   * @param processFailureMap
   * @throws Exception
   */
  private void cloneWorkflowsLinkedWithEndpoints(IListModel<ICloneEndpointModel> cloneEndpointsListModel,
      ICloneEndpointModel endpointToClone, Map<String, Exception> processFailureMap) throws Exception
  {
    Map<String, ICopyWorkflowModel> workflowMap = endpointToClone.getWorkflowsToCopy().stream()
        .collect(Collectors.toMap(x -> x.getFromWorkflowId(), x -> x));
    IIdsListParameterModel ids = new IdsListParameterModel();
    ids.setIds(new ArrayList<>(workflowMap.keySet()));
    
    Map<String, IProcessEventModel> processes = getProcessEventsByIdsStrategy.execute(ids).getProcessEvents();
    Map<String, ICopyWorkflowModel> newClonedProcessMap = new HashMap<String, ICopyWorkflowModel>();
    List<IGetProcessEventModel> processesToBeDeployed = new ArrayList<>();
    for (ICopyWorkflowModel copyWorkFlowModel : workflowMap.values()) {
      
      IProcessEventModel process = processes.get(copyWorkFlowModel.getFromWorkflowId());
      IGetProcessEventModel cloneProcess = new GetProcessEventModel();
      cloneProcess.setProcessDefinition(process.getProcessDefinition());
      cloneProcess.setCode(copyWorkFlowModel.getToWorkflowCode());
      cloneProcess.setId(UUID.randomUUID().toString());
      cloneProcess.setEventType(process.getEventType());
      cloneProcess.setLabel(copyWorkFlowModel.getToWorkflowName());
      cloneProcess.setType(process.getType());
      cloneProcess.setProcessType(process.getProcessType());
      cloneProcess.setTriggeringType(process.getTriggeringType());
      cloneProcess.setPhysicalCatalogIds(process.getPhysicalCatalogIds());
      cloneProcess.setIsExecutable(process.getIsExecutable());
      cloneProcess.setIsXMLModified(false);
      cloneProcess.setEntityType(process.getEntityType());
      cloneProcess.setWorkflowType(process.getWorkflowType());
      cloneProcess.setToSchedule(process.getToSchedule());
      cloneProcess.setFromSchedule(process.getFromSchedule());
      cloneProcess.setActionSubType(process.getActionSubType());
      cloneProcess.setOrganizationsIds(process.getOrganizationsIds());
      updateIsExecutableInProcessDefinition(cloneProcess);
      newClonedProcessMap.put(cloneProcess.getId(), copyWorkFlowModel);
      processesToBeDeployed.add(cloneProcess);
    }
    IListModel<IGetProcessEventModel> bulkUploadProcessesModel = new ListModel<>();
    bulkUploadProcessesModel.setList(processesToBeDeployed);
    bulkUploadProcessesModel.setAdditionalProperty(IS_CLONE, true);
    IBulkSaveProcessEventResponseModel uploadBulkResponse = bulkUploadProcessToServerStrategy.execute(bulkUploadProcessesModel);
    
    processFailureMap = uploadBulkResponse.getFailure();
    if (processFailureMap.isEmpty()) {
      for (IProcessEventModel successSaveProcessModel : uploadBulkResponse.getSuccess().getProcessEventsList()) {
        ICopyWorkflowModel workflowModel = newClonedProcessMap.get(successSaveProcessModel.getId());
        workflowModel.setCloneWorkflowSaveModel(successSaveProcessModel);
      }
    }
    else {
      cloneEndpointsListModel.getList().remove(endpointToClone);
    }
    
  }
  
  /**
   * Adding failure details of processes failed to deploy in Camunda
   * 
   * @param processFailureMap
   * @param endpointFailure
   */
  private void fillProcessDeploymentFailure(Map<String, Exception> processFailureMap,
      IExceptionModel endpointFailure)
  {
    if (processFailureMap != null) {
      for (Entry<String, Exception> processDeploymentFailure : processFailureMap.entrySet()) {
        ExceptionUtil.addFailureDetailsToFailureObject(endpointFailure,
            processDeploymentFailure.getValue(), processDeploymentFailure.getKey(), null);
      }
    }
  }
  
  /**
   * Modify isExecutable in ProcessDefinition while cloning
   * 
   * @param cloneProcess
   * @throws Exception
   */
  private void updateIsExecutableInProcessDefinition(IGetProcessEventModel cloneProcess) throws Exception
  {
    BpmnModelInstance bpmnModel = Bpmn.readModelFromStream(new ByteArrayInputStream(cloneProcess.getProcessDefinition().getBytes()));
    DomDocument document = bpmnModel.getDocument();
    DomElement root = document.getRootElement();
    DomElement processElement = parseProcessElement(root, PROCESS);
    processElement.setAttribute(IProcessEventModel.IS_EXECUTABLE, Boolean.toString(Boolean.FALSE));
    cloneProcess.setProcessDefinition(Bpmn.convertToString(bpmnModel));
  }

  /**
   * @param root - root Dom element of xml
   * @param elementName - Element label
   * @return DemElement with name as "process"
   * @throws Exception
   */
  protected DomElement parseProcessElement(DomElement root, String elementName) throws Exception
  {
    List<DomElement> elements = root.getChildElementsByNameNs(MODEL_NS, elementName);
    if (elements.isEmpty()) {
      throw new Exception("No BPMN Process found in the Workflow : " + elementName);
    }
    if (elements.size() > 1) {
      throw new Exception("Multiple BPMN Processes/Pools found in the Workflow : " + elementName);
    }
    return elements.get(0);
  }
  
}
