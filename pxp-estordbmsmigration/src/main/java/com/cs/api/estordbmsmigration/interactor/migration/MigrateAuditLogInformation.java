package com.cs.api.estordbmsmigration.interactor.migration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.api.estordbmsmigration.services.MigrationProperties;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class MigrateAuditLogInformation extends AbstractRuntimeService<IVoidModel, IVoidModel> implements IMigrateAuditLogInformation {

  public static int          BATCH_SIZE;
  public static final String AUDIT_LOG                = "auditlog";
  /*public static final String AUDIT_LOG_EXPORT_TRACKER = "audit_log_export_tracker";*/
  
  
  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    MigrationProperties migrationPropertiesInstance = MigrationProperties.instance();
    BATCH_SIZE = migrationPropertiesInstance.getInt("postgres.batchsize");
    Connection connection = connnectESPostgresDb(migrationPropertiesInstance);
    
    migrateAuditLogInformation(connection);
    /* migrateAuditLogExportTrackerInformation(connection);*/
    return null;
  }
  
  /* private void migrateAuditLogExportTrackerInformation(Connection connection) throws SQLException
  {
    String getAllAuditLogExportTrackerToMigrate = " SELECT \"id\", \"assetId\", \"fileName\", \"userName\", \"startTime\", \"endTime\" ,\"status\""
        + " FROM public.audit_log_export_tracker LIMIT  " +BATCH_SIZE+" OFFSET ? ";
    PreparedStatement selectStmt = connection.prepareStatement(getAllAuditLogExportTrackerToMigrate);
    migrateRecordsByBatchSize(selectStmt, AUDIT_LOG_EXPORT_TRACKER);
    
    // Restart Sequence pxp.downloadloginformationtableprimarykey
    restartSequenceAfterMigration(AUDIT_LOG_EXPORT_TRACKER);
  }
  */

  private void migrateAuditLogInformation(Connection connection) throws SQLException
  {
      String getAllAuditLogsToMigrate = "SELECT \"activityId\", \"activity\", \"date\", \"entityType\", \"element\", \"elementCode\", \"elementName\","
          + " \"elementType\", \"ipAddress\", \"userName\" FROM public.audit_log LIMIT "+BATCH_SIZE+" OFFSET ? ";
      PreparedStatement selectStmt = connection.prepareStatement(getAllAuditLogsToMigrate);
      migrateRecordsByBatchSize(selectStmt, AUDIT_LOG);  
      
      // Restart Sequence pxp.downloadloginformationtableprimarykey
      restartSequenceAfterMigration(AUDIT_LOG);
  }


  private void restartSequenceAfterMigration(String tableName)
  {
    try {
      
      RDBMSConnectionManager.instance()
          .runTransaction((RDBMSConnection currentConn) -> {
            if (AUDIT_LOG.equals(tableName)) {
              StringBuilder restartAuditLogInformationSequence = new StringBuilder("Select ")
                  .append("setval('pxp.auditLogUniqueIID', ( ")
                  .append("Select max(activityIID)+1 from pxp.")
                  .append(AUDIT_LOG)
                  .append("), false )");
              PreparedStatement stmt = currentConn
                  .prepareStatement(restartAuditLogInformationSequence);
              stmt.executeQuery();
            }
            /* else {
               StringBuilder restartAssetMiscSequence = new StringBuilder("Select ")
                   .append("setval('pxp.audit_log_export_trackerexportIID', ( ")
                   .append("Select max(exportIID)+1 from pxp.")
                   .append(AUDIT_LOG_EXPORT_TRACKER)
                   .append("), false )");
               PreparedStatement stmt = currentConn.prepareStatement(restartAssetMiscSequence);
               stmt.executeQuery();
             }*/
          });
    }
    catch (RDBMSException e) {
      e.printStackTrace();
    }
  }
  
  private void migrateRecordsByBatchSize(PreparedStatement selectStmt, String tableName) throws SQLException
  {
    int offset = 0, batchNumber = 0, count = 0;
    do {
      selectStmt.setLong(1, offset);
      ResultSet result = selectStmt.executeQuery();
      batchNumber++;
      offset += BATCH_SIZE;
      count = migrateAuditLogInformationFromESToRDBMS(result, tableName, batchNumber);
    }
    while (count == BATCH_SIZE);    
  }


  private int migrateAuditLogInformationFromESToRDBMS(ResultSet result, String tableName, int batchNumber)
  {
    List<Integer> totalCount = new ArrayList<Integer>();
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
            int count = 0;
            Statement statement = currentConn.getConnection().createStatement();
            while (result.next()) {
              if (AUDIT_LOG.equals(tableName)) {
                fillValuesForAuditLogs(result, statement, currentConn);
              }
          /*  else {
              fillAuditLogExportTrackerInformation(result, statement, currentConn);
            }*/
              count++;
            }
            executeBatch(currentConn, count, statement, tableName, batchNumber);
            totalCount.add(count);
          });
    }
    catch (RDBMSException e) {
      e.printStackTrace();
    }
    return totalCount.get(0);
  }


  private void executeBatch(RDBMSConnection currentConn, int count, Statement statement, String tableName, int batchNumber) throws SQLException
  {
    try {
      System.out.println( "Fetched " + count + " records of "+ tableName +" from elastic data.");
      System.out.println(count + " records have been added in batch "+ batchNumber +" for insertion in the pxp."+ tableName);
      statement.executeBatch();
      System.out.println(count + " records have inserted successfully in the pxp."+ tableName);
      currentConn.commit();
    }
    catch (Exception e) {
      System.out.println("batch "+ batchNumber + " of size " + count + " failed");
    }
    statement.clearBatch();
  }


  /*  private void fillAuditLogExportTrackerInformation(ResultSet auditLogExportTracker, Statement statement, RDBMSConnection currentConn) throws SQLException
  {
    StringBuilder insertAuditLogsExportInformation = new StringBuilder("INSERT INTO pxp.")
        .append(AUDIT_LOG_EXPORT_TRACKER)
        .append("(exportIID,assetId,fileName,userName,startTime,endTime,status)")
        .append("VALUES (")
        .append(auditLogExportTracker.getLong("id"));
    handleString(auditLogExportTracker.getString("assetId"),insertAuditLogsExportInformation);
    insertAuditLogsExportInformation.append(", ");
    handleString(auditLogExportTracker.getString("fileName"),insertAuditLogsExportInformation);
    insertAuditLogsExportInformation.append(", ");
    handleString(auditLogExportTracker.getString("userName"),insertAuditLogsExportInformation);
    insertAuditLogsExportInformation.append(", ");
    insertAuditLogsExportInformation.append(auditLogExportTracker.getLong("startTime")).append(", ");
    insertAuditLogsExportInformation.append(auditLogExportTracker.getLong("endTime")).append(", ");
    insertAuditLogsExportInformation.append(auditLogExportTracker.getInt("status")).append(" )");
    
    statement.addBatch(insertAuditLogsExportInformation.toString()); 
    
  }*/


  private void fillValuesForAuditLogs(ResultSet auditLogs, Statement statement, RDBMSConnection currentConn) throws SQLException
  {
    StringBuilder insertAuditLogsInformation = new StringBuilder("INSERT INTO pxp.")
        .append(AUDIT_LOG)
        .append("(activityIID,activity,date,entityType,element,elementCode,elementName,elementType,ipAddress,userName )")
        .append("VALUES (")
        .append(auditLogs.getLong("activityId"))
        .append(", ");
    handleServiceType(auditLogs.getString("activity"),insertAuditLogsInformation);
    insertAuditLogsInformation.append(", ");
    insertAuditLogsInformation.append(auditLogs.getLong("date")).append(", ");
    handleEntityType(getEnumCode(auditLogs, "entityType"),insertAuditLogsInformation );
    insertAuditLogsInformation.append(", ");
    handleElementType(getEnumCode(auditLogs, "element"),insertAuditLogsInformation );
    insertAuditLogsInformation.append(", ");
    handleString(getEnumCode(auditLogs, "elementCode"),insertAuditLogsInformation );
    insertAuditLogsInformation.append(", ");
    handleString(getEnumCode(auditLogs, "elementName"),insertAuditLogsInformation );
    insertAuditLogsInformation.append(", ");
    handleString(getEnumCode(auditLogs, "elementType"),insertAuditLogsInformation );
    insertAuditLogsInformation.append(", ");
    handleString(auditLogs.getString("ipAddress"),insertAuditLogsInformation );
    insertAuditLogsInformation.append(", ");
    handleString(auditLogs.getString("userName"),insertAuditLogsInformation );
    insertAuditLogsInformation.append(")");
    
    statement.addBatch(insertAuditLogsInformation.toString()); 
  }
  
  
  private String getEnumCode(ResultSet auditLogs, String columnLabel) throws SQLException
  {
    String element = auditLogs.getString(columnLabel);
    if (element == null || element.isEmpty()) {
      return "UNDEFINED";
    }
    
    return element;
  }
  
  private void handleServiceType(String string, StringBuilder insertAuditLogsInformation)
  {
    insertAuditLogsInformation.append(ServiceType.valueOf(string).ordinal());
  }

  private void handleElementType(String string, StringBuilder insertAuditLogsInformation)
  {
    insertAuditLogsInformation.append(Elements.valueOf(string).ordinal());    
  }

  private void handleEntityType(String string, StringBuilder insertAuditLogsInformation)
  {
    insertAuditLogsInformation.append(Entities.valueOf(string).ordinal());
  }

  private void handleString(String stringFieldValue, StringBuilder insertAuditLogsInformation)
  {
    if (stringFieldValue != null) {
      insertAuditLogsInformation.append("'")
          .append(stringFieldValue)
          .append("'");
    }
    else {
      insertAuditLogsInformation.append(stringFieldValue);
    }
  }

  private Connection connnectESPostgresDb(MigrationProperties migrationPropertiesInstance) throws CSInitializationException, SQLException
  {
    String esPostgresIp = migrationPropertiesInstance.getString("postgres.host");
    String esPostgresPort = migrationPropertiesInstance.getString("postgres.port");
    String esPostgresDbName = migrationPropertiesInstance.getString("postgres.dbname");
    String esPostgresUsername = migrationPropertiesInstance.getString("postgres.username");
    String esPostgresPassword = migrationPropertiesInstance.getString("postgres.password");
    
    String url = "jdbc:postgresql://" + esPostgresIp + ":" + esPostgresPort + "/" + esPostgresDbName;
    Connection connection = DriverManager.getConnection(url, esPostgresUsername, esPostgresPassword);
    
    System.out.println("Connected with database jdbc:postgresql://localhost:5432/camundadb");
    return connection;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }

}
