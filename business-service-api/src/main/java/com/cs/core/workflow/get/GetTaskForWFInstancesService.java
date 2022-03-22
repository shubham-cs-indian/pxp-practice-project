
package com.cs.core.workflow.get;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.articleimportcomponent.*;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByProcessDefinitionIdStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.runtime.entity.dao.IWorkflowStatusDAO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;
import com.cs.di.runtime.model.processinstance.IGetProcessInstanceModel;
import com.cs.di.workflow.constants.MessageCode;
import com.cs.di.workflow.constants.ObjectCode;
import com.cs.di.workflow.model.executionstatus.IOutputExecutionStatusModel;
import com.cs.di.workflow.model.executionstatus.OutputExecutionStatusModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Get the workflow instance on the basis of filter parameter.
 * Filtet paramter : Workflow defination id, User Id, Start time, End time.
 * 
 * @author subham S
 *
 */
@Component
public class GetTaskForWFInstancesService extends AbstractRuntimeService<IGetProcessInstanceModel, IProcessInstanceModel>
    implements IGetTaskForWFInstancesService {
  
  @Autowired
  protected RDBMSComponentUtils                   rdbmsComponentUtils;
  
  @Autowired
  protected IGetProcessDefinitionByProcessDefinitionIdStrategy  getProcessDefinitionStrategy;
  
  @Override
  public IProcessInstanceModel executeInternal(IGetProcessInstanceModel model) throws Exception
  {
    IProcessInstanceModel processInstance = new ProcessInstanceModel();
    IWorkflowStatusDAO openWorkflowStatusDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openWorkflowStatusDAO();
    List<IWorkflowStatusDTO> wfTaskList = openWorkflowStatusDAO.getTaskForWorkFlowInstance(model.getInstanceIIDs().get(0));
    Map<Long, List<IWorkflowStatusDTO>> workflowTaskGroup = wfTaskList.stream()
        .collect(Collectors.groupingBy(IWorkflowStatusDTO::getProcessInstanceID));
    List<IComponentInstanceModel> componentInstanceModelList = new ArrayList<IComponentInstanceModel>();
    // It will always have one entry
    for (Map.Entry<Long, List<IWorkflowStatusDTO>> entry : workflowTaskGroup.entrySet()) {
      // Details of selected workflow
      Optional<IWorkflowStatusDTO> wfDTO = entry.getValue().stream()
          .filter(w -> (w.getParentIID() == 0L || w.getParentIID()==null)).findFirst();
      if (!wfDTO.isEmpty()) {
        processInstance.setInstanceIID(wfDTO.get().getInstanceIID());
        processInstance.setId(String.valueOf(wfDTO.get().getInstanceIID()));
        processInstance.setProcessInstanceId(wfDTO.get().getProcessInstanceID());
        processInstance.setProcessEventId(wfDTO.get().getProcessId());
        processInstance.setLabel(wfDTO.get().getLabel());
        processInstance.setStartTime(wfDTO.get().getStartTime());
        processInstance.setEndTime(wfDTO.get().getEndTime());
        // TODO:PXPFDEV-15851 status will be done
        processInstance.setStatus(wfDTO.get().getStatus());
        processInstance.setUserId(wfDTO.get().getCreateUserID());
        processInstance.setProcessDefinition(model.getProcessDefinition());
        // List of all task in selected Workflow
        List<IWorkflowStatusDTO> taskList = entry.getValue().stream().filter(t -> (t.getParentIID() > 0L)).collect(Collectors.toList());
        for (IWorkflowStatusDTO task : taskList) {
          IComponentInstanceModel componentInstanceModel = new ComponentInstanceModel();
          // from Task_1d1z2z2:49383 get Task_1d1z2z2 as per UI demand
          String taskInstanceId = Arrays.asList(task.getTaskInstanceId().split(":")).stream().findFirst().orElse(null);
          componentInstanceModel.setComponentId(taskInstanceId);
          componentInstanceModel.setComponentLabel(task.getLabel());
          componentInstanceModel.setId(String.valueOf(task.getInstanceIID()));
          componentInstanceModel.setStartTime(task.getStartTime());
          componentInstanceModel.setEndTime(task.getEndTime());
          componentInstanceModel.setSystemComponentId(task.getLabel());
          componentInstanceModel.setJobID(task.getJobID());
          componentInstanceModel.setTaskStatus(task.getStatus());
          componentInstanceModel.setStatus(getStatusModel(task));
          // PXPFDEV-15368 : added for getting task details from workflow - ENDS
          componentInstanceModelList.add(componentInstanceModel);
        }
      }
      processInstance.setComponents(componentInstanceModelList);
    }
    return processInstance;
  }
  
  /**
   * This task prepare status count for UI response model
   * @param task
   * @return IStatusModel
   */
  @SuppressWarnings("unchecked")
  private IStatusModel getStatusModel(IWorkflowStatusDTO task) throws Exception
  {
    IStatusModel status = new StatusModel();
    // this is for non BGP Task
    if (task.getJobID()==null || task.getJobID() == 0L) {
      Integer successCount = 0;
      Integer totalCount = 0;
      if (!(task.getSummary() == null || task.getSummary().isBlank())) {
        List<IOutputExecutionStatusModel> summaryList = ObjectMapperUtil.readValue(task.getSummary(),
            new TypeReference<List<OutputExecutionStatusModel>>()
            {
            });
        for (IOutputExecutionStatusModel summary : summaryList) {
          if (MessageCode.GEN000.equals(summary.getMessageCode())) {
            
            List<ObjectCode> objectCodes = Arrays.asList(summary.getObjectCodes());
            int index = 0;
            for (ObjectCode objectCode : objectCodes) {
              if (ObjectCode.SUCCESS_COUNT.equals(objectCode)) {
                successCount = Integer.valueOf(Arrays.asList(summary.getObjectValues()).get(index++));
              }
              else if (ObjectCode.TOTAL_COUNT.equals(objectCode)) {
                totalCount = Integer.valueOf(Arrays.asList(summary.getObjectValues()).get(index++));
              }
            }
          }
        }
        status.setCompletedCount(successCount);
        status.setFailedCount(totalCount - successCount);
        status.setTotalCount(totalCount);
      }
    }
    // this is for BGP Task
    else {
      Map<String, Object> summaryMap  = ObjectMapperUtil.readValue(task.getSummary(), Map.class);
      Integer successCount = (Integer) summaryMap.get(SUCCESS_COUNT);
      Integer totalCount = (Integer) summaryMap.get(TOTAL_COUNT);
      status.setCompletedCount(successCount);
      status.setFailedCount(totalCount - successCount);
      status.setTotalCount(totalCount);
    }
    return status;
  }
}