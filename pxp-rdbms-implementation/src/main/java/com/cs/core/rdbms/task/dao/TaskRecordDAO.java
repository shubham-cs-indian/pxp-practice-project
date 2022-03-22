package com.cs.core.rdbms.task.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.data.Text;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTOBuilder;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.rdbms.task.idto.ITaskRecordDTOBuilder;
import com.cs.core.rdbms.task.idto.TaskCommentDTO.TaskCommentDTOBuilder;
import com.cs.core.rdbms.task.idto.TaskRecordDTO;
import com.cs.core.rdbms.task.idto.TaskRecordDTO.TaskRecordDTOBuilder;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class TaskRecordDAO implements ITaskRecordDAO {

  private long userIID;

  public TaskRecordDAO(long userIID) {
    this.userIID = userIID;
  }

  @Override
  public ITaskCommentDTOBuilder newTaskCommentDTOBuilder() {
    return new TaskCommentDTOBuilder();
  }

  @Override
  public ITaskRecordDTOBuilder newTaskRecordDTOBuilder(long baseEntityIID, ITaskDTO taskDTO) {
    return new TaskRecordDTOBuilder().entityIID(baseEntityIID).task(taskDTO);
  }

  @Override
  public ITaskRecordDTO createTaskRecord(ITaskRecordDTO taskRecordDTO) throws RDBMSException {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
      taskRecordDAS.createTaskRecord((TaskRecordDTO) taskRecordDTO);
    });
    return taskRecordDTO;
  }

  @Override
  public ITaskRecordDTO updateTaskRecord(ITaskRecordDTO taskRecordDTO) throws RDBMSException {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
      taskRecordDAS.updateTaskRecord(((TaskRecordDTO) taskRecordDTO));
    });
    return taskRecordDTO;
  }

  @Override
  public void deleteTaskRecord(long taskIID) throws RDBMSException {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
      taskRecordDAS.deleteTaskRecord(taskIID);
    });
  }

  @Override
  public ITaskRecordDTO getTaskByTaskIID(long taskIID) throws RDBMSException {
    TaskRecordDTO[] taskRecordDTO = {new TaskRecordDTO()};
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
      IResultSetParser parser = taskRecordDAS.getTaskRecordByTaskIID(taskIID);
      if (parser.next()) {
        taskRecordDTO[0] = new TaskRecordDTO(parser);
      }

      IResultSetParser childrenParser = taskRecordDAS.getAllChildrenTasksByTaskIID(taskIID);
      while (childrenParser.next()) {
        TaskRecordDTO recordDTO = new TaskRecordDTO(childrenParser);
        taskRecordDTO[0].addChildren(recordDTO);
      }
    });
    return taskRecordDTO[0].getTaskIID() != 0 ? taskRecordDTO[0] : null;
  }

  @Override
  public List<ITaskRecordDTO> getAllTaskByBaseEntityIID(long baseEntityIID, String roleCode)
          throws RDBMSException {
    List<ITaskRecordDTO> taskRecordDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
              TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
              IResultSetParser parser = taskRecordDAS.getAllTaskByBaseEntityIID(baseEntityIID, roleCode, userIID);
              while (parser != null && parser.next()) {
                TaskRecordDTO recordDTO = new TaskRecordDTO(parser);
                taskRecordDTOs.add(recordDTO);
              }
            });
    return taskRecordDTOs;
  }

  @Override
  public int getTaskCountOnBaseEntity(long baseEntityIID, String roleCode) throws RDBMSException {
    int[] count = new int[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
              TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
              count[0] = taskRecordDAS.getTaskCountOnBaseEntity(baseEntityIID, roleCode, userIID);
            });
    return count[0];
  }

  @Override
  public int getTaskCountOnProperty(long baseEntityIID, long propertyIID, String roleCode) throws RDBMSException {
    int[] count = new int[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
              TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
              count[0] = taskRecordDAS.getTaskCountOnProperty(baseEntityIID, propertyIID, roleCode, userIID);
            });
    return count[0];
  }

  @Override
  public Map<Long, List<ITaskRecordDTO>> getAllTaskByBaseEntityIIDs(Set<Long> baseEntityIIDs,
          String roleCode, long startDate, long endDate) throws RDBMSException {
    Map<Long, List<ITaskRecordDTO>> baseEntityTasks = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
              TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
              IResultSetParser parser = taskRecordDAS.getAllTaskByBaseEntityIIDs(baseEntityIIDs,
                      roleCode, userIID, startDate, endDate);
              while (parser != null && parser.next()) {
                long entityIID = parser.getLong("entityIID");
                List<ITaskRecordDTO> list = baseEntityTasks.get(entityIID);
                TaskRecordDTO taskRecordDTO = new TaskRecordDTO(parser);
                IResultSetParser childrenParser = taskRecordDAS.getAllChildrenTasksByTaskIID(taskRecordDTO.getTaskIID());
                while (childrenParser.next()) {
                  TaskRecordDTO recordDTO = new TaskRecordDTO(childrenParser);
                  taskRecordDTO.addChildren(recordDTO);
                }
                if (list == null) {
                  list = new ArrayList<>();
                  list.add(taskRecordDTO);
                  baseEntityTasks.put(entityIID, list);
                } else {
                  list.add(taskRecordDTO);
                }
              }
            });
    return baseEntityTasks;
  }

  @Override
  public int getAllPlannedTaskCount(String roleCode, String physicalCatalogId, String organizationId) throws RDBMSException {
    int[] count = new int[1];
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
              TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
              count[0] = (int) taskRecordDAS.getAllPlannedTaskCount(userIID, roleCode, physicalCatalogId, organizationId);
            });

    return count[0];
  }

  @Override
  public Set<ITaskRecordDTO> getAllTask(String roleCode, RACIVS racivs, String physicalCatalogId) throws RDBMSException {
    Set<ITaskRecordDTO> taskRecords = new LinkedHashSet<ITaskRecordDTO>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
              TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
              IResultSetParser parser = taskRecordDAS.getAllTask(userIID, roleCode, racivs, physicalCatalogId);
              while (parser != null && parser.next()) {
                TaskRecordDTO recordDTO = new TaskRecordDTO(parser);
                taskRecords.add(recordDTO);
              }
            });

    return taskRecords;
  }

  @Override
  public Set<IBaseEntityDTO> getAllEntity(String roleCode, RACIVS racivs) throws RDBMSException {
    Set<IBaseEntityDTO> entity = new LinkedHashSet<>();

    RDBMSConnectionManager.instance()
            .runTransaction((RDBMSConnection currentConn) -> {
              TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
              IResultSetParser parser = taskRecordDAS.getAllEntity(userIID, roleCode, racivs);
              while (parser != null && parser.next()) {
                BaseEntityIDDTO baseEntityIDDTO = new BaseEntityIDDTO(
                        "", BaseType.UNDEFINED, "", null, null);
                baseEntityIDDTO.setIID(parser.getLong("baseentityiid"));
                IBaseEntityDTO baseEntityDTO = new BaseEntityDTO((BaseEntityIDDTO) baseEntityIDDTO,
                        parser.getString("baseentityname"));
                entity.add(baseEntityDTO);
              }
            });

    return entity;
  }
  
  @Override
  public void deleteTasksByBaseEntityIID(long baseEntityIID) throws RDBMSException {
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
      taskRecordDAS.deleteTaskRecordsByBaseEntityIID(baseEntityIID);
    });
  }

  public static final String FETCH_TASK_BY_CODES_AND_ENTITYIID = "select * from pxp.task where entityiid = ? "
      + "and taskcode IN (%s)";
  
  @Override
  public void deleteTasksByTaskCodeAndEntityIID(List<String> taskCodes, Long entityiid) throws RDBMSException
  {
    Set<Long> taskIIDs = new HashSet<>();
    
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      String finalQuery = String.format(FETCH_TASK_BY_CODES_AND_ENTITYIID, Text.join(",", taskCodes, "'%s'"));
      PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
      stmt.setLong(1, entityiid);
      IResultSetParser result = currentConn.getResultSetParser(stmt.executeQuery());
      while (result.next()) {
        taskIIDs.add(result.getLong("taskiid"));
      }
      
      TaskRecordDAS taskRecordDAS = new TaskRecordDAS( currentConn);
      taskRecordDAS.deleteTaskRecordByTaskIIDs(taskIIDs);
    });
  }
}
