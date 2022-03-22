package com.cs.core.workflow.get;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.articleimportcomponent.IProcessInstanceModel;
import com.cs.core.config.interactor.model.articleimportcomponent.ProcessInstanceModel;
import com.cs.core.config.interactor.model.component.IProcessInstanceEntity;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.strategy.usecase.task.GetProcessDefinitionByIdStrategy;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;
import com.cs.di.runtime.entity.idto.IFilterWorkFlowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;
import com.cs.workflow.get.IGetWorkflowInstancesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Get the workflow instance on the basis of filter parameter.
 * Filtet paramter : Workflow defination id, User Id, Start time, End time.
 * 
 * @author sopan.talekar
 *
 */
@Component
public class GetWorkflowInstancesService extends AbstractRuntimeService<IGetProcessInstanceModel, IListModel<IProcessInstanceModel>>
    implements IGetWorkflowInstancesService {
  
  @Autowired
  protected RDBMSComponentUtils                   rdbmsComponentUtils;
  
  @Autowired
  protected GetProcessDefinitionByIdStrategy  getProcessDefinitionByIdStrategy;
  
  @Override
  public IListModel<IProcessInstanceModel> executeInternal(IGetProcessInstanceModel model)
      throws Exception
  {
    IWorkflowStatusDAO openWorkflowStatusDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openWorkflowStatusDAO();
    IFilterWorkFlowStatusDTO filterWorkFlowStatusDTO = openWorkflowStatusDAO.newFilterWorkFlowStatusDTO();    
    filterWorkFlowStatusDTO.setDefinationIds(model.getProcessEventIds());
    filterWorkFlowStatusDTO.setUserIds(model.getUserIds());
    filterWorkFlowStatusDTO.setStartTime(model.getFrom());
    filterWorkFlowStatusDTO.setEndTime(model.getTo());
    filterWorkFlowStatusDTO.setEndpointIds(model.getEndpointIds());
    filterWorkFlowStatusDTO.setPhysicalCatalogIds(model.getPhysicalCatalogs());
    List<IWorkflowStatusDTO> workflowInstances = openWorkflowStatusDAO.getFilteredWorkFlowInstance(filterWorkFlowStatusDTO);
    List<IProcessInstanceModel> processInstances = new ArrayList<>();
    for (IWorkflowStatusDTO workflowInstance : workflowInstances) {
      IProcessInstanceModel processInstance = new ProcessInstanceModel();
      processInstance.setProcessInstanceId(workflowInstance.getProcessInstanceID());
      processInstance.setProcessEventId(workflowInstance.getProcessId());
      processInstance.setLabel(workflowInstance.getLabel());
      processInstance.setStartTime(workflowInstance.getStartTime()== 0 ? null:workflowInstance.getStartTime());
      processInstance.setEndTime(workflowInstance.getEndTime()== 0 ? null:workflowInstance.getEndTime());
      processInstance.setStatus(workflowInstance.getStatus());
      processInstance.setUserId(workflowInstance.getCreateUserID());
      processInstance.setInstanceIID(workflowInstance.getInstanceIID());
      // PXPFDEV-15368 : added for getting task details from workflow - STARTS
      processInstance.setProcessDefinition(getProcessDefinition(workflowInstance.getProcessId()));
      // PXPFDEV-15368 : added for getting task details from workflow - ENDS
      processInstances.add(processInstance);
    }
    IListModel<IProcessInstanceModel> processInstancesToReturn = new ListModel<IProcessInstanceModel>();
    processInstancesToReturn.setList(processInstances);
    return processInstancesToReturn;
  }
  
  /**Get processDefinition from OriantDB config details by 
   * using processdefinition code from PG
   * @param code from Process_Event.
   * @return processDefinition xml
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public String getProcessDefinition(String code) throws Exception
  {
    String processDefinition = null;
    IdsListParameterModel input = new IdsListParameterModel();
    input.setIds(Arrays.asList(code));
    IGetCamundaProcessDefinitionResponseModel response = getProcessDefinitionByIdStrategy.execute(input);
    Map<String, Object> processDefMap = (Map<String, Object>) response.getProcessDefinition().get(code);
    if (processDefMap != null && processDefMap.containsKey(IProcessInstanceEntity.PROCESSDEFINITION)) {
      Object xmlDefinition = processDefMap.get(IProcessInstanceEntity.PROCESSDEFINITION);
      processDefinition = xmlDefinition.toString();
    }
    return processDefinition;
  }
}
