package com.cs.di.runtime.entity.dao;

import java.util.List;

import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.idto.IFilterWorkFlowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;

public interface IWorkflowStatusDAO {
  
  /**
   * Insert entry into the table when Workflow instance get created.
   * 
   * @param workflowDTO
   * @throws RDBMSException
   */
  public void createWorkflowInstance(IWorkflowStatusDTO workflowDTO) throws RDBMSException;
  
  /**
   * Insert entry into the table when task instance get created.
   * 
   * @param workflowDTO
   * @return 
   * @throws RDBMSException
   */
  public void createTaskInstance(IWorkflowStatusDTO workflowDTO) throws RDBMSException;
 
  /**
   * Update entry in the table after Workflow or Task instance completion 
   * by using processInstanceIID.
   * @param workflowDTO
   * @throws RDBMSException
   */
  public void updateWFOrTaskStatus(IWorkflowStatusDTO workflowDTO) throws RDBMSException;
  
  public IWorkflowStatusDTO newWorkflowStatusDTO();
  
  public List<IWorkflowStatusDTO> getAllTaskInstanceByWF(Long workflowInstanceIID);
  
  public long getUserIID();
  
  /**
   * @param filterWorkFlowStatusDTO the filterWorkflowStatusDTO is used to filter the workflow instance. 
   * @return
   * @throws RDBMSException
   */
  public List<IWorkflowStatusDTO> getFilteredWorkFlowInstance(IFilterWorkFlowStatusDTO filterWorkFlowStatusDTO) throws RDBMSException;
  
  /**
   * @return this will return the new instance of FilterWorkFlowStatusDTO.
   */
  public IFilterWorkFlowStatusDTO newFilterWorkFlowStatusDTO();

  public List<IWorkflowStatusDTO> getTaskForWorkFlowInstance(String instanceIID) throws RDBMSException;

  /**
   * This method return the workflow instances on the basis of filter criteria given
   *
   * @param filterWorkflowDTO
   * @throws RDBMSException
   */
  public List<IWorkflowStatusDTO> getWorkFlowInstancesBulkDownload(IFilterWorkFlowStatusDTO filterWorkflowDTO) throws RDBMSException;

  void updateWFStatusForStuckInstances(List<String> processInstanceIds) throws RDBMSException;
  
  void updateWFStatusForFailedInstances(List<String> processInstanceIds) throws RDBMSException;
  
}
