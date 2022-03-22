package com.cs.core.rdbms.task.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.json.JSONBuilder;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.task.idto.TaskRecordDTO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO.RACIVS;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;

public class TaskRecordDAS extends RDBMSDataAccessService {

  private static final String ORDER_BY_TASK_NAME = " order by taskName asc";

  private static final String Q_GET_TASK_RECORD = "select * from pxp.taskfullrecord where taskIID = ? "
          + ORDER_BY_TASK_NAME;

  private static final String Q_GET_ALL_CHILDREN_TASK_RECORD = "select * from pxp.taskfullrecord where parentTaskIID = ? "
          + ORDER_BY_TASK_NAME;

  private static final String Q_GET_ALL_TASK = "select * from pxp.taskfullrecord where taskIID ";
  
  private static final String Q_GET_ALL_TASK_WITH_CATALOG = "select t.* from pxp.taskfullrecord t "
      + "join pxp.baseentity b on t.entityiid = b.baseentityiid where b.catalogcode = ? and t.taskIID ";

  private static final String Q_GET_ALL_PLANNED_TASK_COUNT = "select count(*) as taskCount from pxp.task t"
      + " join pxp.baseentity b on t.entityiid = b.baseentityiid where b.catalogcode = ? and b.organizationcode = ? and statuscode = 'taskplanned' ";
  
  private static final String Q_TASKIIDS_FOR_BASEENTITY = "select distinct t.taskIID as taskIID from pxp.task t where t.entityIID = ?";

  private static final String Q_TASKIIDS_FOR_BASEENTITY_WITH_ROLE = "select distinct t.taskIID as taskIID from pxp.task t join pxp.taskrolelink trl "
          + " on t.taskIID = trl.taskIID where t.entityIID = ? and t.parentTaskIID is null and trl.roleCode = ?";

  private static final String Q_TASKIIDS_FOR_BASEENTITY_WITH_USER = "select distinct t.taskIID as taskIID from pxp.task t join pxp.taskuserlink tul "
          + " on t.taskIID = tul.taskIID where t.entityIID = ? and t.parentTaskIID is null and tul.userIID = ?";

  private static final String Q_TASKIIDS_FOR_PROPERTY_FOR_BASEENTITY_WITH_ROLE = "select distinct t.taskIID as taskIID from pxp.task t join pxp.taskrolelink trl "
          + " on t.taskIID = trl.taskIID where t.entityIID = ? and t.parentTaskIID is null and t.propertyIID = ? and trl.roleCode = ?";

  private static final String Q_TASKIIDS_ON_PROPERTY_FOR_BASEENTITY_WITH_USER = "select t.taskIID as taskIID from pxp.task t join pxp.taskuserlink tul "
          + " on t.taskIID = tul.taskIID where t.entityIID = ? and t.parentTaskIID is null and t.propertyIID = ? and tul.userIID = ?";

  private static final String Q_GET_TASKIIDS_FOR_ROLECODE = "select distinct t.taskIID as taskIID from pxp.task t join pxp.taskrolelink trl "
          + " on t.taskIID = trl.taskIID where  t.parentTaskIID is null and trl.roleCode = ?";
  ;
  
  private static final String Q_GET_TASKIIDS_FOR_USERIID = "select distinct t.taskIID as taskIID from pxp.task t join pxp.taskuserlink tul "
          + " on t.taskIID = tul.taskIID where  t.parenttaskIID is null and tul.userIID = ?";

  private static final String Q_GET_ALL_ENTITY = "select * from pxp.taskentityrecord where taskIID ";

  public TaskRecordDAS(RDBMSConnection connection) {
    super(connection);
  }

  public void createTaskRecord(TaskRecordDTO taskRecordDTO)
          throws SQLException, RDBMSException, CSFormatException {
    String comments = "{"
            + JSONBuilder.newJSONArray(TaskRecordDTO.COMMENTS, taskRecordDTO.getComments()).toString()
            + "}";
    String formFields = "{"
        + JSONBuilder.newJSONArray(TaskRecordDTO.FORM_FIELDS, taskRecordDTO.getFormFields()).toString()
        + "}";
    
    IResultSetParser result = driver
            .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.IID,
                    "pxp.fn_createTaskRecord")
            .setInput(ParameterType.LONG, taskRecordDTO.getEntityIID())
            .setInput(ParameterType.STRING, taskRecordDTO.getTask().getCode())
            .setInput(ParameterType.LONG,
                    taskRecordDTO.getPropertyIID() != 0 ? taskRecordDTO.getPropertyIID() : null)
            .setInput(ParameterType.STRING, !taskRecordDTO.getStatusTag().getCode().isEmpty()
                    ? taskRecordDTO.getStatusTag().getCode() : null)
            .setInput(ParameterType.STRING, !taskRecordDTO.getPriorityTag().getCode()
                    .isEmpty() ? taskRecordDTO.getPriorityTag().getCode() : null)
            .setInput(ParameterType.LONG,
                    taskRecordDTO.getParentTaskIID() != 0 ? taskRecordDTO.getParentTaskIID() : null)
            .setInput(ParameterType.STRING, taskRecordDTO.getTaskName())
            .setInput(ParameterType.LONG, taskRecordDTO.getCreatedTime())
            .setInput(ParameterType.LONG,
                    taskRecordDTO.getStartDate() != 0 ? taskRecordDTO.getStartDate() : null)
            .setInput(ParameterType.LONG,
                    taskRecordDTO.getDueDate() != 0 ? taskRecordDTO.getDueDate() : null)
            .setInput(ParameterType.LONG,
                    taskRecordDTO.getOverdueDate() != 0 ? taskRecordDTO.getOverdueDate() : null)
            .setInput(ParameterType.BOOLEAN, taskRecordDTO.isWfCreated())
            .setInput(ParameterType.STRING, taskRecordDTO.getWfProcessID())
            .setInput(ParameterType.STRING, taskRecordDTO.getWfTaskInstanceID())
            .setInput(ParameterType.STRING, taskRecordDTO.getWfProcessInstanceID())
            .setInput(ParameterType.IID_ARRAY, taskRecordDTO.getAttachments())
            .setInput(ParameterType.STRING, taskRecordDTO.getDescription())
            .setInput(ParameterType.JSON, taskRecordDTO.getPosition().toString())
            .setInput(ParameterType.JSON, comments)
            .setInput(ParameterType.JSON,formFields )
            .execute();

    taskRecordDTO.setTaskIID(result.getIID());

    Map<RACIVS, Set<IUserDTO>> usersMap = taskRecordDTO.getUsersMap();
    Map<RACIVS, Set<String>> rolesMap = taskRecordDTO.getRolesMap();

    List<Integer> userRACIVSs = new ArrayList<>();
    List<Long> userIIDs = new ArrayList<>();
    fillUserRACIVSsAndUserCodes(usersMap, userRACIVSs, userIIDs);

    List<Integer> roleRCIs = new ArrayList<>();
    List<String> roleCodes = new ArrayList<>();
    fillRoleRCIsAndRoleCodes(rolesMap, roleRCIs, roleCodes);

    driver
            .getFunction(currentConnection, RDBMSAbstractFunction.ResultType.VOID,
                    "pxp.fn_createUsersAndRolesForTask")
            .setInput(ParameterType.IID, taskRecordDTO.getTaskIID())
            .setInput(ParameterType.INT_ARRAY, userRACIVSs)
            .setInput(ParameterType.IID_ARRAY, userIIDs)
            .setInput(ParameterType.INT_ARRAY, roleRCIs)
            .setInput(ParameterType.STRING_ARRAY, roleCodes)
            .execute();
  }

  private void fillUserRACIVSsAndUserCodes(Map<RACIVS, Set<IUserDTO>> usersMap,
          List<Integer> userRACIVSs, List<Long> userIIDs) {
    for (RACIVS racivs : usersMap.keySet()) {
      usersMap.get(racivs)
              .forEach(userIID -> {
                userRACIVSs.add(racivs.ordinal());
                userIIDs.add(userIID.getUserIID());
              });
    }
  }

  private void fillRoleRCIsAndRoleCodes(Map<RACIVS, Set<String>> rolesMap, List<Integer> roleRCIs,
          List<String> roleCodes) {
    for (RACIVS racivs : rolesMap.keySet()) {
      rolesMap.get(racivs)
              .forEach(roleCode -> {
                roleRCIs.add(racivs.ordinal());
                roleCodes.add(roleCode);
              });
    }
  }

  public void updateTaskRecord(TaskRecordDTO taskRecordDTO)
          throws SQLException, RDBMSException, CSFormatException {
    String comments = "{"
            + JSONBuilder.newJSONArray(TaskRecordDTO.COMMENTS, taskRecordDTO.getComments())
                    .toString()
            + "}";
    
    String formFields = "{"
        + JSONBuilder.newJSONArray(TaskRecordDTO.FORM_FIELDS, taskRecordDTO.getFormFields()).toString()
        + "}";

    RDBMSConnectionManager.instance()
            .runTransaction((RDBMSConnection currentConn) -> {
              driver.getFunction(currentConnection, RDBMSAbstractFunction.ResultType.VOID, "pxp.fn_updateTaskRecord")
                      .setInput(ParameterType.LONG, taskRecordDTO.getTaskIID())
                      .setInput(ParameterType.STRING, !taskRecordDTO.getStatusTag().getCode().isEmpty()
                              ? taskRecordDTO.getStatusTag().getCode() : null)
                      .setInput(ParameterType.STRING, !taskRecordDTO.getPriorityTag().getCode().isEmpty()
                              ? taskRecordDTO.getPriorityTag().getCode() : null)
                      .setInput(ParameterType.STRING, taskRecordDTO.getTaskName())
                      .setInput(ParameterType.LONG, taskRecordDTO.getStartDate() != 0 ? taskRecordDTO.getStartDate() : null)
                      .setInput(ParameterType.LONG, taskRecordDTO.getDueDate() != 0 ? taskRecordDTO.getDueDate() : null)
                      .setInput(ParameterType.LONG, taskRecordDTO.getOverdueDate() != 0 ? taskRecordDTO.getOverdueDate() : null)
                      .setInput(ParameterType.IID_ARRAY, taskRecordDTO.getAttachments())
                      .setInput(ParameterType.STRING, taskRecordDTO.getDescription())
                      .setInput(ParameterType.JSON, comments)
                      .setInput(ParameterType.JSON,formFields )
                      .execute();
              if (taskRecordDTO.isChanged()) {
                Map<RACIVS, Set<IUserDTO>> usersMap = taskRecordDTO.getUsersMap();
                Map<RACIVS, Set<String>> rolesMap = taskRecordDTO.getRolesMap();

                List<Integer> userRACIVSs = new ArrayList<>();
                List<Long> userIIDs = new ArrayList<>();
                fillUserRACIVSsAndUserCodes(usersMap, userRACIVSs, userIIDs);

                List<Integer> roleRCIs = new ArrayList<>();
                List<String> roleCodes = new ArrayList<>();
                fillRoleRCIsAndRoleCodes(rolesMap, roleRCIs, roleCodes);

                driver.getFunction(currentConnection, RDBMSAbstractFunction.ResultType.VOID, "pxp.fn_updateUsersAndRolesForTask")
                        .setInput(ParameterType.IID, taskRecordDTO.getTaskIID())
                        .setInput(ParameterType.INT_ARRAY, userRACIVSs)
                        .setInput(ParameterType.IID_ARRAY, userIIDs)
                        .setInput(ParameterType.INT_ARRAY, roleRCIs)
                        .setInput(ParameterType.STRING_ARRAY, roleCodes)
                        .execute();
              }
            });
  }

  public void deleteTaskRecord(long taskIID) throws SQLException, RDBMSException {
    driver.getFunction(currentConnection, RDBMSAbstractFunction.ResultType.VOID, "pxp.fn_deleteTaskRecord")
            .setInput(ParameterType.IID, taskIID)
            .execute();
  }

  public IResultSetParser getTaskRecordByTaskIID(long taskIID) throws SQLException, RDBMSException {
    PreparedStatement prepareStatement = currentConnection.prepareStatement(Q_GET_TASK_RECORD);
    prepareStatement.setLong(1, taskIID);
    return driver.getResultSetParser(prepareStatement.executeQuery());
  }

  public IResultSetParser getAllChildrenTasksByTaskIID(long taskIID)
          throws SQLException, RDBMSException {
    PreparedStatement prepareStatement = currentConnection.prepareStatement(Q_GET_ALL_CHILDREN_TASK_RECORD);
    prepareStatement.setLong(1, taskIID);
    return driver.getResultSetParser(prepareStatement.executeQuery());
  }

  public IResultSetParser getAllTaskByBaseEntityIID(long baseEntityIID, String roleCode,
          long userIID) throws RDBMSException, SQLException {
    Set<Long> taskIIDs = getTaskIIDsOnBaseEntityForRole(baseEntityIID, roleCode);
    taskIIDs.addAll(getTaskIIDsOnBaseEntityForUser(baseEntityIID, userIID));
    if (taskIIDs == null || taskIIDs.isEmpty()) {
      return null;
    }

    PreparedStatement prepareStatement = currentConnection.prepareStatement(
            Q_GET_ALL_TASK + prepareInQuery(taskIIDs) + ORDER_BY_TASK_NAME);
    return driver.getResultSetParser(prepareStatement.executeQuery());
  }

  private String prepareInQuery(Set<Long> iids) {
    StringBuilder query = new StringBuilder(" in (");
    int count = 0;
    for (long iid : iids) {
      if (count == 0) {
        query.append(iid);
      } else {
        query.append("," + iid);
      }
      count++;
    }
    query.append(")");
    return query.toString();
  }

  public int getTaskCountOnBaseEntity(long baseEntityIID, String roleCode, long userIID)
          throws RDBMSException, SQLException {

    Set<Long> entityIIDs = getTaskIIDsOnBaseEntityForRole(baseEntityIID, roleCode);
    entityIIDs.addAll(getTaskIIDsOnBaseEntityForUser(baseEntityIID, userIID));
    return entityIIDs.size();

  }

  private Set<Long> getTaskIIDsOnBaseEntityForUser(long baseEntityIID, long userIID)
          throws RDBMSException, SQLException {
    PreparedStatement statement = currentConnection
            .prepareStatement(Q_TASKIIDS_FOR_BASEENTITY_WITH_USER);
    statement.setLong(1, baseEntityIID);
    statement.setLong(2, userIID);
    ResultSet rs = statement.executeQuery();
    Set<Long> entityIIDs = new HashSet<Long>();
    while (rs.next()) {
      entityIIDs.add(rs.getLong("taskIID"));
    }
    return entityIIDs;
  }

  private Set<Long> getTaskIIDsOnBaseEntityForRole(long baseEntityIID, String roleCode)
          throws RDBMSException, SQLException {
    PreparedStatement statement = currentConnection
            .prepareStatement(Q_TASKIIDS_FOR_BASEENTITY_WITH_ROLE);
    statement.setLong(1, baseEntityIID);
    statement.setString(2, roleCode);
    ResultSet rs = statement.executeQuery();
    Set<Long> entityIIDs = new HashSet<Long>();
    while (rs.next()) {
      entityIIDs.add(rs.getLong("taskIID"));
    }
    return entityIIDs;
  }
  
  private Set<Long> getTaskIIDsOnBaseEntity(long baseEntityIID)
      throws RDBMSException, SQLException
  {
    PreparedStatement statement = currentConnection
        .prepareStatement(Q_TASKIIDS_FOR_BASEENTITY);
    statement.setLong(1, baseEntityIID);    
    ResultSet rs = statement.executeQuery();
    Set<Long> entityIIDs = new HashSet<Long>();
    while (rs.next()) {
      entityIIDs.add(rs.getLong("taskIID"));
    }
    return entityIIDs;
  }

  public int getTaskCountOnProperty(long baseEntityIID, long propertyIID, String roleCode,
          long userIID) throws RDBMSException, SQLException {
    PreparedStatement statement = currentConnection
            .prepareStatement(Q_TASKIIDS_FOR_PROPERTY_FOR_BASEENTITY_WITH_ROLE);
    statement.setLong(1, baseEntityIID);
    statement.setLong(2, propertyIID);
    statement.setString(3, roleCode);
    ResultSet rs = statement.executeQuery();
    Set<Long> entityIIDs = new HashSet<Long>();
    while (rs.next()) {
      entityIIDs.add(rs.getLong("taskIID"));
    }

    statement = currentConnection.prepareStatement(Q_TASKIIDS_ON_PROPERTY_FOR_BASEENTITY_WITH_USER);
    statement.setLong(1, baseEntityIID);
    statement.setLong(2, propertyIID);
    statement.setLong(3, userIID);
    rs = statement.executeQuery();
    while (rs.next()) {
      entityIIDs.add(rs.getLong("taskIID"));
    }
    return entityIIDs.size();
  }

  public IResultSetParser getAllTaskByBaseEntityIIDs(Set<Long> baseEntityIIDs, String roleCode,
          long userIID, long startTime, long endTime) throws RDBMSException, SQLException {
    String inQuery = prepareInQuery(baseEntityIIDs);
    String getTaskIIDsForRoleQuery = Q_GET_TASKIIDS_FOR_ROLECODE + " and t.entityIID " + inQuery;

    PreparedStatement statement = currentConnection.prepareStatement(getTaskIIDsForRoleQuery);
    statement.setString(1, roleCode);
    ResultSet rs = statement.executeQuery();
    Set<Long> taskIIDs = new HashSet<Long>();
    while (rs.next()) {
      taskIIDs.add(rs.getLong("taskIID"));
    }
    String getTaskIIDsForUserIID = Q_GET_TASKIIDS_FOR_USERIID + " and t.entityIID " + inQuery;
    statement = currentConnection.prepareStatement(getTaskIIDsForUserIID);
    statement.setLong(1, userIID);
    rs = statement.executeQuery();

    while (rs.next()) {
      taskIIDs.add(rs.getLong("taskIID"));
    }

    if (taskIIDs.isEmpty()) {
      return null;
    }

    PreparedStatement prepareStatement = null;
    if (startTime == 0 || endTime == 0) {
      prepareStatement = currentConnection.prepareStatement(
              Q_GET_ALL_TASK + prepareInQuery(taskIIDs) + ORDER_BY_TASK_NAME);
    } else {
      prepareStatement = currentConnection
              .prepareStatement(Q_GET_ALL_TASK + prepareInQuery(taskIIDs)
                      + " and (startDate >= " + startTime + " or dueDate <= " + endTime + ")" + ORDER_BY_TASK_NAME);
    }
    return driver.getResultSetParser(prepareStatement.executeQuery());
  }

  public long getAllPlannedTaskCount(long userIID, String roleCode, String physicalCatalogId, String organizationId) throws RDBMSException, SQLException {

    PreparedStatement preparedStatement = currentConnection.prepareStatement(Q_GET_TASKIIDS_FOR_ROLECODE);
    preparedStatement.setString(1, roleCode);
    ResultSet rs = preparedStatement.executeQuery();
    Set<Long> taskIIDs = new HashSet<Long>();
    while (rs.next()) {
      taskIIDs.add(rs.getLong("taskIID"));
    }
    preparedStatement = currentConnection.prepareStatement(Q_GET_TASKIIDS_FOR_USERIID);
    preparedStatement.setLong(1, userIID);
    rs = preparedStatement.executeQuery();
    while (rs.next()) {
      taskIIDs.add(rs.getLong("taskIID"));
    }

    if (taskIIDs.isEmpty()) {
      return 0;
    }

    preparedStatement = currentConnection
            .prepareStatement(Q_GET_ALL_PLANNED_TASK_COUNT + "and taskIID " + prepareInQuery(taskIIDs)
            );
    preparedStatement.setString(1, physicalCatalogId);
    preparedStatement.setString(2, organizationId);
    rs = preparedStatement.executeQuery();
    return (rs.next() ? rs.getLong("taskCount") : 0);
  }

  public IResultSetParser getAllTask(long userIID, String roleCode, RACIVS racivs, String physicalCatalogId) throws RDBMSException, SQLException {
    String getAllTaskIIDsByRole = Q_GET_TASKIIDS_FOR_ROLECODE;
    String getAllTaskIIDsByUser = Q_GET_TASKIIDS_FOR_USERIID;
    if (racivs != RACIVS.UNDEFINED) {
      getAllTaskIIDsByRole = getAllTaskIIDsByRole + " and rci = " + racivs.ordinal();
      getAllTaskIIDsByUser = getAllTaskIIDsByUser + " and racivs = " + racivs.ordinal();
    }
    Set<Long> taskIIDs = new HashSet<Long>();
    PreparedStatement preparedStatement = null;
    ResultSet rs = null;
    if (racivs == RACIVS.UNDEFINED || racivs == RACIVS.RESPONSIBLE || racivs == RACIVS.CONSULTED || racivs == RACIVS.INFORMED) {
      preparedStatement = currentConnection.prepareStatement(getAllTaskIIDsByRole);
      preparedStatement.setString(1, roleCode);
      rs = preparedStatement.executeQuery();

      while (rs.next()) {
        taskIIDs.add(rs.getLong("taskIID"));
      }
    }
    preparedStatement = currentConnection.prepareStatement(getAllTaskIIDsByUser);
    preparedStatement.setLong(1, userIID);
    rs = preparedStatement.executeQuery();
    while (rs.next()) {
      taskIIDs.add(rs.getLong("taskIID"));
    }

    if (taskIIDs.isEmpty()) {
      return null;
    }

    preparedStatement = currentConnection.prepareStatement(Q_GET_ALL_TASK_WITH_CATALOG + prepareInQuery(taskIIDs));
    preparedStatement.setString(1, physicalCatalogId);
    return driver.getResultSetParser(preparedStatement.executeQuery());
  }

  public IResultSetParser getAllEntity(long userIID, String roleCode, RACIVS racivs) throws RDBMSException, SQLException {
    String getAllTaskIIDsByRole = Q_GET_TASKIIDS_FOR_ROLECODE;
    String getAllTaskIIDsByUser = Q_GET_TASKIIDS_FOR_USERIID;
    if (racivs != RACIVS.UNDEFINED) {
      getAllTaskIIDsByRole = getAllTaskIIDsByRole + " and rci = " + racivs.ordinal();
      getAllTaskIIDsByUser = getAllTaskIIDsByUser + " and racivs = " + racivs.ordinal();
    }
    Set<Long> taskIIDs = new HashSet<>();
    PreparedStatement preparedStatement;
    ResultSet rs;
    if (racivs == RACIVS.UNDEFINED || racivs == RACIVS.RESPONSIBLE || racivs == RACIVS.CONSULTED || racivs == RACIVS.INFORMED) {
      preparedStatement = currentConnection.prepareStatement(getAllTaskIIDsByRole);
      preparedStatement.setString(1, roleCode);
      rs = preparedStatement.executeQuery();

      while (rs.next()) {
        taskIIDs.add(rs.getLong("taskIID"));
      }
    }
    preparedStatement = currentConnection.prepareStatement(getAllTaskIIDsByUser);
    preparedStatement.setLong(1, userIID);
    rs = preparedStatement.executeQuery();
    while (rs.next()) {
      taskIIDs.add(rs.getLong("taskIID"));
    }

    if (taskIIDs.isEmpty()) {
      return null;
    }

    preparedStatement = currentConnection
            .prepareStatement(Q_GET_ALL_ENTITY + prepareInQuery(taskIIDs)
            );
    return driver.getResultSetParser(preparedStatement.executeQuery());
  }

  /**
   * Delete all tasks which are linked to specified baseEntityIID
   * @param baseEntityIID
   * @throws SQLException
   * @throws RDBMSException
   */
  public void deleteTaskRecordsByBaseEntityIID(long baseEntityIID) throws SQLException, RDBMSException
  {
    Set<Long> taskIIDsOnBaseEntity = getTaskIIDsOnBaseEntity(baseEntityIID);
    deleteTaskRecordByTaskIIDs(taskIIDsOnBaseEntity);
  }
  
  public void deleteTaskRecordByTaskIIDs(Set<Long> taskIIDs) throws RDBMSException, SQLException
  {
    driver.getFunction(currentConnection, RDBMSAbstractFunction.ResultType.VOID, "pxp.fn_deleteTaskRecords")
    .setInput(ParameterType.IID_ARRAY, taskIIDs)
    .execute();
  }

}
