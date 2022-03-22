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
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Migration For Download Information to dump in download information pxp tables
 * 
 * @author mrunali.dhenge
 *
 */
@Service
public class MigrateDownloadInformation implements IMigrateDownloadInformation {
  
  public static final String ASSET_MISC_TABLE               = "assetmisc";
  public static final String DOWNLOAD_LOG_INFORMATION_TABLE = "downloadloginformation";
  public static int          BATCH_SIZE;             
  
  @Override
  public IVoidModel execute(IVoidModel dataModel) throws Exception
  {
    MigrationProperties migrationPropertiesInstance = MigrationProperties.instance();
    BATCH_SIZE = migrationPropertiesInstance.getInt("postgres.batchsize");
    
    Connection connection = connnectESPostgresDb(migrationPropertiesInstance);
    
    // Migrate download logs
    migrateDownloadInformation(connection);
    
    // Migrate asset misc information
    migrateAssetMiscInformation(connection); 
    
    
    return null;
  }

  /**
   * Connect with elastic search postgres database
   * @param migrationPropertiesInstance 
   * @return
   * @throws CSInitializationException
   * @throws SQLException
   */
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
  
  /**
   * Migrate download tracker information from ES To RDBMS
   * @param result
   * @param tableName
   * @param batchNumber 
   * @return 
   */
  private int migrateDownloadTrackerInformationFromESToRDBMS(ResultSet result, String tableName, int batchNumber)
  {
    List<Integer> totalCount = new ArrayList<Integer>();
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
            int count = 0;
            Statement statement = currentConn.getConnection().createStatement();
            while (result.next()) {
              if (DOWNLOAD_LOG_INFORMATION_TABLE.equals(tableName)) {
                fillValuesForDownloadLogs(result, statement, currentConn);
              }
              else {
                fillAssetMiscInformation(result, statement, currentConn);
              }
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
  
  /**
   * Restart Sequences pxp.assetmisctableprimarykey and
   * pxp.downloadloginformationtableprimarykey
   * 
   * @param tableName
   * @throws RDBMSException
   * @throws SQLException
   */
  private void restartSequenceAfterMigration(String tableName)
  {
    try {
      
      RDBMSConnectionManager.instance()
          .runTransaction((RDBMSConnection currentConn) -> {
            if (DOWNLOAD_LOG_INFORMATION_TABLE.equals(tableName)) {
              StringBuilder restartDownloadInformationSequence = new StringBuilder("Select ")
                  .append("setval('pxp.downloadloginformationtableprimarykey', ( ")
                  .append("Select max(primarykey)+1 from pxp.")
                  .append(DOWNLOAD_LOG_INFORMATION_TABLE)
                  .append("), false )");
              PreparedStatement stmt = currentConn
                  .prepareStatement(restartDownloadInformationSequence);
              stmt.executeQuery();
            }
            else {
              StringBuilder restartAssetMiscSequence = new StringBuilder("Select ")
                  .append("setval('pxp.assetmisctableprimarykey', ( ")
                  .append("Select max(primarykey)+1 from pxp.")
                  .append(ASSET_MISC_TABLE)
                  .append("), false )");
              PreparedStatement stmt = currentConn.prepareStatement(restartAssetMiscSequence);
              stmt.executeQuery();
            }
          });
    }
    catch (RDBMSException e) {
      e.printStackTrace();
    }
  }

  /**
   * Migrate asset misc information from ES To RDBMS System
   * 
   * @param connection
   * @throws SQLException
   * @throws RDBMSException
   */
  private void migrateAssetMiscInformation(Connection connection) throws SQLException
  {
    String getAssetMiscInformation = "SELECT \"primaryKey\", \"assetInstanceId\", \"renditionInstanceId\","
        + " \"downloadTimestamp\", \"downloadCount\", \"sharedObjectId\", \"sharedTimestamp\"\r\n" + 
      "  FROM public.asset_misc LIMIT 20 OFFSET ?";   
    PreparedStatement assetMiscQuery = connection.prepareStatement(getAssetMiscInformation);
    migrateRecordsByBatchSize(assetMiscQuery, ASSET_MISC_TABLE);
    
    // Restart Sequences pxp.assetmisctableprimarykey
    restartSequenceAfterMigration(ASSET_MISC_TABLE);
  }
 
  /**
   * Execute batch
   * @param currentConn
   * @param count
   * @param statement
   * @param tableName 
   * @throws SQLException
   */
  private void executeBatch(RDBMSConnection currentConn, int count, Statement statement, String tableName, int batchNo) throws SQLException
  {
    try {
      System.out.println( "Fetched " + count + " records of "+ tableName +" from elastic data.");
      System.out.println(count + " records have been added in batch "+ batchNo +" for insertion in the pxp."+ tableName);
      statement.executeBatch();
      System.out.println(count + " records have inserted successfully in the pxp."+ tableName);
      currentConn.commit();
    }
    catch (Exception e) {
      System.out.println("batch "+ batchNo + " of size " + count + " failed");
    }
    statement.clearBatch();
  }
  
  /**
   * Prepare batch to insert asset misc information
   * 
   * @param result
   * @param statement
   * @param insertQueryForAssetMisc
   * @param currentConn
   * @throws SQLException
   * @throws RDBMSException
   */
  private void fillAssetMiscInformation(ResultSet result, Statement statement, RDBMSConnection currentConn) throws SQLException, RDBMSException
  {
    StringBuilder insertAssetMiscInformation = new StringBuilder("INSERT INTO pxp.")
        .append(ASSET_MISC_TABLE)
        .append("( primarykey, assetinstanceiid, renditioninstanceiid, ")
        .append("downloadtimestamp, downloadcount, sharedobjectid, sharedtimestamp) ")
        .append("VALUES ( ")
        .append(result.getLong("primaryKey"))
        .append(", ")
        .append(getIID(result.getString("assetInstanceId"), currentConn))
        .append(", ")
        .append(getIID(result.getString("renditionInstanceId"), currentConn))
        .append(", ")
        .append(result.getLong("downloadTimestamp"))
        .append(", ")
        .append(result.getLong("downloadCount"))
        .append(", ");
    handleString(result.getString("sharedObjectId"), insertAssetMiscInformation);
    insertAssetMiscInformation.append(", ").append(result.getLong("sharedTimestamp")).append(") ");
    
    statement.addBatch(insertAssetMiscInformation.toString());
  }
  
  /**
   * Migrate all download logs from ES To RDBMS System
   * 
   * @param successIds
   * @param connection
   * @throws SQLException
   * @throws RDBMSException
   */
  private void migrateDownloadInformation(Connection connection) throws SQLException
  {
    String getAllDownloadLogsToMigrate = "SELECT \"primaryKey\", \"assetInstanceId\", \"assetInstanceName\", \"assetInstanceClassId\", \"assetInstanceClassCode\","
        + " \"renditionInstanceId\", \"renditionInstanceName\", \"renditionInstanceClassId\", \"renditionInstanceClassCode\","
        + " \"userId\", \"timestamp\", comment, \"downloadId\", \"assetFileName\", \"renditionFileName\", \"userName\", \"isReset\"\r\n"
        + "  FROM public.download_log_information LIMIT "+BATCH_SIZE+" OFFSET ?";
    PreparedStatement selectStmt = connection.prepareStatement(getAllDownloadLogsToMigrate);
    migrateRecordsByBatchSize(selectStmt, DOWNLOAD_LOG_INFORMATION_TABLE);  

    // Restart Sequence pxp.downloadloginformationtableprimarykey
    restartSequenceAfterMigration(DOWNLOAD_LOG_INFORMATION_TABLE);
  }

  /**
   * Migrate records by batch size
   * @param selectStmt
   * @param tableName 
   * @throws SQLException
   */
  private void migrateRecordsByBatchSize(PreparedStatement selectStmt, String tableName) throws SQLException
  {
    int offset = 0, batchNumber = 0, count = 0;
    do {
      selectStmt.setLong(1, offset);
      ResultSet result = selectStmt.executeQuery();
      batchNumber++;
      offset += BATCH_SIZE;
      count = migrateDownloadTrackerInformationFromESToRDBMS(result, tableName, batchNumber);
    }
    while (count == BATCH_SIZE);
  }

  /**
   * Prepare batch for download logs information
   * 
   * @param downloadLogs
   * @param statement
   * @param currentConn
   * @param insertQuery
   * @throws SQLException
   * @throws RDBMSException
   */
  private void fillValuesForDownloadLogs(ResultSet downloadLogs, Statement statement,
      RDBMSConnection currentConn) throws SQLException, RDBMSException
  {
    StringBuilder insertDownloadLogsInformation = new StringBuilder("INSERT INTO pxp.")
        .append(DOWNLOAD_LOG_INFORMATION_TABLE)
        .append("( primarykey, assetinstanceiid, assetinstancename, assetfilename, ")
        .append("assetinstanceclassid, assetinstanceclasscode, renditioninstanceiid, ")
        .append("renditioninstancename, renditionfilename, renditioninstanceclassid, ")
        .append("renditioninstanceclasscode, userid, \"timestamp\", ")
        .append("comment, downloadid, username, isreset ) ")
        .append("VALUES (")
        .append(downloadLogs.getLong("primaryKey"))
        .append(", ")
        .append(getIID(downloadLogs.getString("assetInstanceId"), currentConn))
        .append(", ");
    handleString(downloadLogs.getString("assetInstanceName"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("assetFileName"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("assetInstanceClassId"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("assetInstanceClassCode"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ").append(getIID(downloadLogs.getString("renditionInstanceId"), currentConn)).append(", ");
    handleString(downloadLogs.getString("renditionInstanceName"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("renditionFileName"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("renditionInstanceClassId"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("renditionInstanceClassCode"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("userId"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ").append(downloadLogs.getLong("timestamp")).append(", ");
    handleString(downloadLogs.getString("comment"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("downloadId"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ");
    handleString(downloadLogs.getString("userName"), insertDownloadLogsInformation);
    insertDownloadLogsInformation.append(", ").append(downloadLogs.getBoolean("isReset")).append(")");
    
    statement.addBatch(insertDownloadLogsInformation.toString());
  }
  
  /**
   * Handle string column value
   * @param stringFieldValue
   * @param INSERT_INTO_DOWNLOAD_LOGS
   */
  private void handleString(String stringFieldValue, StringBuilder INSERT_INTO_DOWNLOAD_LOGS)
  {
    if (stringFieldValue != null) {
      INSERT_INTO_DOWNLOAD_LOGS.append("'")
          .append(stringFieldValue)
          .append("'");
    }
    else {
      INSERT_INTO_DOWNLOAD_LOGS.append(stringFieldValue);
    }
  }
  
  /**
   * Get rdbms iid for es id from staging.baseentity table
   * 
   * @param id
   * @param currentConn
   * @return
   * @throws RDBMSException
   * @throws SQLException
   */
  private long getIID(String id, RDBMSConnection currentConn) throws RDBMSException, SQLException
  {
    List<Long> iidList = new ArrayList<Long>();
    
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currConn) -> {
          StringBuilder GET_ASSET_INSTANCE_IID = new StringBuilder(
              "Select baseentityiid from staging.baseentity ").append(" where id = '").append(id).append("' LIMIT 1");
          PreparedStatement stmt = currConn.prepareStatement(GET_ASSET_INSTANCE_IID);
          ResultSet iid = stmt.executeQuery();
          if (iid.next()) {
            iidList.add(iid.getLong("baseentityiid"));
          }
        });
    if (iidList.size() > 0) {
      return iidList.get(0);
    }
    return 0;
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
}
