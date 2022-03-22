package com.cs.core.rdbms.task.idao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.task.idto.ITaskCommentDTOBuilder;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.rdbms.task.idto.ITaskRecordDTOBuilder;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * offers the data operation for task on base entity
 *
 * @author Janak.Gurme
 */
public interface ITaskRecordDAO {

  /**
   * factory method to get TaskCommentDTOBuilder
   *
   * @return TaskCommentDTOBuilder
   */
  public ITaskCommentDTOBuilder newTaskCommentDTOBuilder();

  /**
   * factory method to get TaskRecordDTOBuilder
   *
   * @param baseEntityIID
   * @param taskDTO the concerned task
   * @return TaskRecordDTOBuilder with some mandatory fields.
   */
  public ITaskRecordDTOBuilder newTaskRecordDTOBuilder(long baseEntityIID, ITaskDTO taskDTO);

  /**
   * create task or sub task in base entity
   *
   * @return taskRecordDTO
   * @throws RDBMSException
   */
  public ITaskRecordDTO createTaskRecord(ITaskRecordDTO taskRecordDTO) throws RDBMSException;

  /**
   * update task or sub task in base entity
   *
   * @return taskRecordDTO
   * @throws RDBMSException
   */
  public ITaskRecordDTO updateTaskRecord(ITaskRecordDTO taskRecordDTO) throws RDBMSException;

  /**
   * delete task or sub task from base entity
   *
   * @param taskIID
   * @throws RDBMSException
   */
  public void deleteTaskRecord(long taskIID) throws RDBMSException;

  /**
   * fetch task or sub task by taskiid
   *
   * @param taskRecordDTO
   * @return ITaskRecordDTO
   * @throws RDBMSException
   */
  public ITaskRecordDTO getTaskByTaskIID(long taskIID) throws RDBMSException;

  /**
   * get all task for base entity
   *
   * @param baseEntityIID
   * @param roleCode role assigned to user
   * @return List<ITaskRecordDTO>
   * @throws RDBMSException
   */
  public List<ITaskRecordDTO> getAllTaskByBaseEntityIID(long baseEntityIID, String roleCode)
          throws RDBMSException;

  /**
   * get all task for base entity iids between startdate and end date
   *
   * @param baseEntityIID
   * @param roleCode role assigned to user
   * @param startDate of task
   * @param endDate of task
   * @return List<ITaskRecordDTO>
   * @throws RDBMSException
   */
  public Map<Long, List<ITaskRecordDTO>> getAllTaskByBaseEntityIIDs(Set<Long> baseEntityIIDs,
          String roleCode, long startDate, long endDate) throws RDBMSException;

  /**
   * get count of all task on baseEntity except sub-task
   *
   * @param baseEntityIID
   * @param roleCode role assigned to user
   * @return count
   * @throws RDBMSException
   */
  public int getTaskCountOnBaseEntity(long baseEntityIID, String roleCode) throws RDBMSException;

  /**
   * get count of all task on property for baseEntity except sub-task
   *
   * @param baseEntityIID
   * @param propertyIID
   * @param roleCode role assigned to user
   * @return count
   * @throws RDBMSException
   */
  public int getTaskCountOnProperty(long baseEntityIID, long propertyIID, String roleCode)
          throws RDBMSException;
  
  /**
   * get count of all planned task for user or role
   * @param roleCode
   * @param physicalCatalogId
   * @param organizationId
   * @return
   * @throws RDBMSException
   */
  public int getAllPlannedTaskCount(String roleCode, String physicalCatalogId, String organizationId) throws RDBMSException;
  
  /**
   * get all task by specific RACIVS , if RACIVS is UNDEFINED then it will fetch for all RACIVS
   * @param roleCode
   * @param racivs
   * @return
   * @throws RDBMSException
   */
  public Set<ITaskRecordDTO> getAllTask(String roleCode, RACIVS racivs, String physicalCatalogId) throws RDBMSException;
  
  /**
   * Get all entity which have task with specific RACIVS, if RACIVS is UNDEFINED then it will fetch for all RACIVS
   * @param roleCode
   * @param racivs
   * @return
   * @throws RDBMSException
   */
  public Set<IBaseEntityDTO> getAllEntity(String roleCode, RACIVS racivs) throws RDBMSException;

  /**
   * Delete all tasks which are linked to specified baseEntityIID
   * @param baseEntityIID
   * @throws RDBMSException
   */
  public void deleteTasksByBaseEntityIID(long baseEntityIID) throws RDBMSException;
  
  /**
   * 
   * @param taskCodes
   * @param entityiid
   * @throws RDBMSException
   */
  public void deleteTasksByTaskCodeAndEntityIID(List<String> taskCodes, Long entityiid) throws RDBMSException;
}
