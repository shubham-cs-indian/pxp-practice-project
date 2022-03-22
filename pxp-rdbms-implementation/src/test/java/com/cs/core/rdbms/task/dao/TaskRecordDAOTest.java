package com.cs.core.rdbms.task.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.cs.core.rdbms.config.dto.TaskDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.rdbms.task.idto.ITaskRecordDTOBuilder;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;

public class TaskRecordDAOTest extends AbstractRDBMSDriverTests {
 
  ITaskRecordDAO taskRecordDao = null;

  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void createTaskRecord() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    ITaskDTO taskDTO = localeCatalogDao.newTaskDTO("Motor-Class", TaskType.SHARED);
    ITaskCommentDTO commentDTO = taskRecordDao.newTaskCommentDTOBuilder()
        .text(" motor class")
        .time(System.currentTimeMillis())
        .build();
    Set<IUserDTO> userIIDs = new HashSet<>();
    userIIDs.add(new UserDTO(6001l, "admin"));
    Set<String> roleCodes = new HashSet<String>();
    roleCodes.add("admin");
    
    ITaskRecordDTOBuilder taskRecordDTOBuilder = taskRecordDao.newTaskRecordDTOBuilder(100004, taskDTO);
    ITaskRecordDTO taskRecord = taskRecordDTOBuilder.startDate(System.currentTimeMillis())
        .taskName("motorclass1")
        .dueDate(System.currentTimeMillis())
        .overdueDate(System.currentTimeMillis())
        .createdTime(System.currentTimeMillis())
        .wfCreated(false)
        .comment(commentDTO)
        .userMap(RACIVS.ACCOUNTABLE, userIIDs).roleMap(RACIVS.RESPONSIBLE, roleCodes)
        .build();
    taskRecord = taskRecordDao.createTaskRecord(taskRecord);
    assertTrue(taskRecord.getTaskIID() != 0);
  }
  
  @Test
  public void updateTaskRecord() throws RDBMSException
  {
    long time = System.currentTimeMillis();
    taskRecordDao = localeCatalogDao.openTaskDAO();
    taskRecordDao = localeCatalogDao.openTaskDAO();
    ITaskCommentDTO commentDTO = taskRecordDao.newTaskCommentDTOBuilder()
        .text(" motor class")
        .time(System.currentTimeMillis())
        .build();
    ITaskRecordDTO taskRecord = taskRecordDao.getTaskByTaskIID(900000);
        taskRecord.setOverdueDate(time);
        taskRecord.setStartDate(time);
        taskRecord.addComment(commentDTO);
        
    taskRecordDao.updateTaskRecord(taskRecord);
    taskRecord = taskRecordDao.getTaskByTaskIID(900000);
    assertTrue(taskRecord.getStartDate() == time && taskRecord.getOverdueDate() == time);
  }
  
  @Test
  public void getTaskRecord() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    ITaskRecordDTO taskRecord = taskRecordDao.getTaskByTaskIID(900000);
    assertTrue(taskRecord != null);
  }
  
  @Test
  public void deleteTaskRecord() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    taskRecordDao.deleteTaskRecord(900001);
    ITaskRecordDTO task = taskRecordDao.getTaskByTaskIID(900001);
    assertNull(task);
  }
  
  @Test
  public void getAllTaskRecordOnBaseEntityIID() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    List<ITaskRecordDTO> taskRecords = taskRecordDao.getAllTaskByBaseEntityIID(100003, "admin");
    assertTrue(taskRecords.size() == 2);
  }
  
  @Test
  public void getAllTaskRecordOnBaseEntityIIDS() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    Set<Long> baseEntityIIDs = new HashSet<Long>();
    baseEntityIIDs.add(100003l);
    baseEntityIIDs.add(100005l);
    baseEntityIIDs.add(100002l);
    Map<Long, List<ITaskRecordDTO>> taskRecords = taskRecordDao
        .getAllTaskByBaseEntityIIDs(baseEntityIIDs, "admin", 0, 0);
    taskRecords.keySet().forEach(baseEntityIID -> {
          List<ITaskRecordDTO> list = taskRecords.get(baseEntityIID);
          System.out.println(list.size() + " baseentityId " + baseEntityIID);
        });
    assertTrue(taskRecords.keySet().size() == 3);
    taskRecords.keySet()
        .forEach(baseEntityIID -> {
          List<ITaskRecordDTO> list = taskRecords.get(baseEntityIID);
          assertTrue(list.size() > 0);
        });
  }
  
  @Test
  public void getTaskCountOnBaseEntity() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    int taskCountOnBaseEntity = taskRecordDao.getTaskCountOnBaseEntity(100003, "admin");
    assertTrue(taskCountOnBaseEntity > 1);
  }
  
  @Test
  public void getTaskCountOnPropertyForBaseEntity() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    int taskCountOnBaseEntity = taskRecordDao.getTaskCountOnProperty(100005, 200, "admin");
    System.out.println(taskCountOnBaseEntity);
    assertTrue(taskCountOnBaseEntity == 1);
  }
  
  @Test
  public void getALLPlannedTaskCount() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    int taskCountOnBaseEntity = taskRecordDao.getAllPlannedTaskCount("admin");
    assertTrue(taskCountOnBaseEntity >= 1);
  }

  @Test
  public void getALLTask() throws RDBMSException
  {
    taskRecordDao = localeCatalogDao.openTaskDAO();
    Set<ITaskRecordDTO> allTask = taskRecordDao.getAllTask("admin", RACIVS.UNDEFINED);
    System.out.println(allTask.size());
    assertTrue(allTask.size() >= 1);
    
    Set<ITaskRecordDTO> allAccountableTask = taskRecordDao.getAllTask("manager", RACIVS.ACCOUNTABLE);
    System.out.println(allAccountableTask.size());
    assertTrue(allAccountableTask.size() >= 1);
    
    Set<ITaskRecordDTO> allResponsibleTask = taskRecordDao.getAllTask("manager", RACIVS.RESPONSIBLE);
    System.out.println(allResponsibleTask.size());
    assertTrue(allResponsibleTask.size() >= 1);
  }
  
  @Test
  public void taskDTOTest() throws CSFormatException {
    ITaskDTO task = new TaskDTO("R_Task", TaskType.SHARED);
    String pxon = task.toPXON();
    printf(pxon);
    
    ITaskDTO taskDTO = new TaskDTO();
    taskDTO.fromPXON(pxon);
    printf(taskDTO.toPXON());
    assertEquals(pxon, taskDTO.toPXON());
  }

  
}
