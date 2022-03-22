package com.cs.di.runtime.entity.dao;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.junit.Test;

import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.common.test.DiIntegrationTestConfig;
import com.cs.di.runtime.entity.dto.FilterWorkFlowStatusDTO;
import com.cs.di.runtime.entity.dto.WorkflowStatusDTO;
import com.cs.di.runtime.entity.idto.IWorkflowStatusDTO;

/**
 * This class test WorkflowStatusDAO DAO operations with valid invalid test data
 *
 * @author mayuri.wankhade
 *
 */
public class WorkflowStatusDaoTest extends DiIntegrationTestConfig {
  
  WorkflowStatusDAO workflowStatusDao = new WorkflowStatusDAO();
  
  //@Test
  public void validWFInstaceIID()
  {
    Long workflowInstanceIID = 0L;
    workflowStatusDao.getAllTaskInstanceByWF(workflowInstanceIID);
  }
  
  //@Test
  public void invalidWFInstaceIID()
  {
    Long workflowInstanceIID = 0L;
    workflowStatusDao.getAllTaskInstanceByWF(workflowInstanceIID);
  }
  
  
//  @Test
  public void validUpdTaskInstance() throws RDBMSException
  {
    IWorkflowStatusDTO workflowDTO = new WorkflowStatusDTO();
    long longData = (long) 9999;
    String stringData = null;
//    workflowDTO.setSuccess(stringData);
//    workflowDTO.setError(stringData);
//    workflowDTO.setInformation(stringData);
//    workflowDTO.setSummary(stringData);
//    workflowDTO.setWarning(stringData);
//    workflowDTO.setEndTime(longData);
    workflowDTO.setStatus(0);
    workflowDTO.setEndTime(longData);
    workflowDTO.setInstanceIID((long) 1002);
    workflowStatusDao.updateWFOrTaskStatus(workflowDTO);
  }
  
  
  //@Test
  public void invalidUpdTaskInstance()
  {
  }
  
  @Test
  public void validCreateTaskInstance() throws RDBMSException
  {
    IWorkflowStatusDTO workflowDTO = new WorkflowStatusDTO();
    long longData = (long) 1002;
    String stringData = "taskInsert";
    workflowDTO.setProcessInstanceID(longData);
    workflowDTO.setTaskInstanceId(stringData);
    workflowDTO.setParentIID((long) 1003);
    workflowDTO.setJobID(longData);
    workflowDTO.setStatus(1);
    workflowDTO.setLabel(stringData);
    workflowDTO.setCreateUserID(longData);
    workflowDTO.setStartTime(longData);
    workflowDTO.setEndTime(longData);    
    workflowStatusDao.createTaskInstance(workflowDTO);
  }
  
  @Test
  public void validCreateWFInstance() throws RDBMSException
  {
    IWorkflowStatusDTO workflowDTO = new WorkflowStatusDTO();
    long longData = (long) 1003;
    String stringData = "workflowInsert";
    workflowDTO.setProcessInstanceID(longData);
    //workflowDTO.setDefinitionID(stringData);
    workflowDTO.setLabel(stringData);
    workflowDTO.setCreateUserID(longData);
    workflowDTO.setStartTime(longData);
    workflowDTO.setEndTime(longData);
    workflowStatusDao.createWorkflowInstance(workflowDTO);
  }
  
 
  
  public void invalidCreateWFInstance() throws RDBMSException
  {
  }
  
  @Test
  public void validUpdateWFInstance() throws RDBMSException
  {
    IWorkflowStatusDTO workflowDTO = new WorkflowStatusDTO();
    long longData = 9999l;
    String stringData = "task UPD";
    workflowDTO.setInstanceIID(14L);
    workflowDTO.setStatus(0);
    String InputJson = "{\r\n \"errors\":0,\"warnings\":0,\"logsize\":8}";
    workflowDTO.setSuccess(InputJson);
    workflowDTO.setError("{\"ERROR\":[{\"objectCodes\":[\"INPUT_PARAM\"],\"objectValues\":[\"FOLDER_PATH\"],\"messageCode\":\"GEN004\",\"messageType\":\"E\"}]}");
    workflowDTO.setEndTime(longData);
    workflowStatusDao.updateWFOrTaskStatus(workflowDTO);
    
  }
  
  //@Test
  public void invalidUpdateWFInstance() throws RDBMSException
  {
  }
  
  //workflow test cases
  @Test
  public void validFilterWorkflowInstancesForDefinationIds() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setDefinationIds(Arrays.asList("Pro100102"));
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getFilteredWorkFlowInstance(filterWorkflowDTO);
    assertTrue(!result.isEmpty());
  }
  
  //@Test
  public void inValidFilterWorkflowInstancesForDefinationIds() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setDefinationIds(Arrays.asList("putWrongId"));
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getFilteredWorkFlowInstance(filterWorkflowDTO);
    assertTrue(result.isEmpty());
  }
  
  //@Test
  public void validFilterWorkflowInstancesForUserIds() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setUserIds(Arrays.asList(10l));
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getFilteredWorkFlowInstance(filterWorkflowDTO);
    assertTrue(!result.isEmpty());
  }
  
  //@Test
  public void inValidFilterWorkflowInstancesForUserIds() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setUserIds(Arrays.asList(111111l));
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getFilteredWorkFlowInstance(filterWorkflowDTO);
    assertTrue(result.isEmpty());
  }
  
  //@Test
  public void validFilterWorkflowInstancesForTimestamp() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setStartTime(1583519400000l);
    filterWorkflowDTO.setEndTime(1584124199999l);
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getFilteredWorkFlowInstance(filterWorkflowDTO);
    assertTrue(!result.isEmpty());
  }  

  //@Test
  public void inValidFilterWorkflowInstancesForTimestamp() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setStartTime(100l);
    filterWorkflowDTO.setEndTime(109l);
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getFilteredWorkFlowInstance(filterWorkflowDTO);
    assertTrue(result.isEmpty());
  }
  
  //@Test
  public void validFilterWorkflowInstances() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setDefinationIds(Arrays.asList("Pro100112"));
    filterWorkflowDTO.setUserIds(Arrays.asList(10l));
    filterWorkflowDTO.setStartTime(1583519400000l);
    filterWorkflowDTO.setEndTime(1584124199999l);
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getFilteredWorkFlowInstance(filterWorkflowDTO);
    assertTrue(!result.isEmpty());
  }
  
  //@Test
  public void validTaskListForWFInstance() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setParentIID(5L);
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getTaskForWorkFlowInstance("");
    result.forEach(p->System.out.println(p.getError()));
    assertTrue(!result.isEmpty());  
  }
  
  /**
   * tested for JobID
   * @throws RDBMSException
   */
  //@Test
  public void checkJobIIDForBgp() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setParentIID(5L);
    List<IWorkflowStatusDTO> result = workflowStatusDao.getTaskForWorkFlowInstance("");
    List<IWorkflowStatusDTO> taskStatusFiltered = result.stream().filter(p -> (p.getJobID()>0L))
        .collect(Collectors.toList());
    assertTrue(!taskStatusFiltered.isEmpty());
    result.forEach(p -> System.out.println(" JOB ID "+p.getJobID()));
  }
  
  /**
   * tested for JobID and TaskStatus
   * @throws RDBMSException
   */
  //@Test
  public void checkTaskStatusForBgp() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setParentIID(5L);
    List<IWorkflowStatusDTO> result = workflowStatusDao.getTaskForWorkFlowInstance("");
    List<IWorkflowStatusDTO> taskStatusFiltered = result.stream().filter(p -> !ObjectUtils.NULL.equals(p.getStatus()))
        .collect(Collectors.toList());
    assertTrue(!taskStatusFiltered.isEmpty());
    result.forEach(p -> System.out.println(" Task Status for BGP "+p.getStatus()));
  }
  
  /**
   * tested for task with no JobID linked in table 
   * TaskStatus for Non BGP
   * @throws RDBMSException
   */
  //@Test
  public void checkTaskStatusForNonBgp() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setParentIID(1003L);
    List<IWorkflowStatusDTO> result = workflowStatusDao.getTaskForWorkFlowInstance("");
    List<IWorkflowStatusDTO> taskStatusFiltered = result.stream().filter(p -> !(p.getJobID()>0L))
        .collect(Collectors.toList());
    assertTrue(!taskStatusFiltered.isEmpty());
    result.forEach(p -> System.out.println(" Expected JobID is 0 and received jobID "+p.getJobID()));
  }
  
  /**
   * tested for invalid test Data
   * to confirm no result received without Exception
   * @throws RDBMSException
   */
  //@Test
  public void emptyTaskListForWFInstance() throws RDBMSException
  {
    FilterWorkFlowStatusDTO filterWorkflowDTO = new FilterWorkFlowStatusDTO();
    filterWorkflowDTO.setParentIID(1010L);
    List<IWorkflowStatusDTO> result =  workflowStatusDao.getTaskForWorkFlowInstance("");
    result.forEach(p->System.out.println(p.getInstanceIID()));
    assertTrue(!result.isEmpty());  
  }
  
}
