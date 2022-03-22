package com.cs.config.strategy.postgres.auditlog;

import java.sql.Connection;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.base.strategy.postgres.constants.AuditLogConstant;
import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;
import com.cs.core.runtime.strategy.db.BasePostgresStrategy;

public abstract class AbstractAuditLogStrategy<P extends IModel, R extends IModel> extends BasePostgresStrategy
    implements IRuntimeStrategy<P, R> {
  
  @Autowired
  protected BasicDataSource postgresDataSource;
  
  public abstract R executeInternal(Connection con, P model) throws Exception;
  
  // Queries
  public static final String AUDIT_LOG = "INSERT INTO " + AuditLogConstant.AUDIT_LOG + " ( "
      + "\"" +IAuditLogModel.ACTIVITY  +"\"," 
      + "\"" +IAuditLogModel.DATE  +"\"," 
      + "\"" +IAuditLogModel.ENTITY_TYPE+"\"," 
      + "\"" +IAuditLogModel.ELEMENT  +"\"," 
      + "\"" +IAuditLogModel.ELEMENT_CODE  +"\"," 
      + "\"" +IAuditLogModel.ELEMENT_NAME  +"\"," 
      + "\"" +IAuditLogModel.ELEMENT_TYPE  +"\"," 
      + "\"" +IAuditLogModel.IP_ADDRESS  +"\"," 
      + "\"" +IAuditLogModel.USER_NAME  +"\" ) " 
      + " VALUES(?,?,?,?,?,?,?,?,?)";
  
	/*
	 * public static final String AUDIT_LOG_EXPORT = "INSERT INTO " +
	 * AuditLogConstant.AUDIT_LOG_EXPORT_TRACKER_TABLE+ " ( " + "\""
	 * +IAuditLogExportEntryModel.ASSET_ID +"\"," + "\""
	 * +IAuditLogExportEntryModel.FILE_NAME +"\"," + "\""
	 * +IAuditLogExportEntryModel.USERNAME +"\"," + "\""
	 * +IAuditLogExportEntryModel.START_TIME +"\"," + "\""
	 * +IAuditLogExportEntryModel.STATUS +"\" ) " + " VALUES(?,?,?,?,?)";
	 * 
	 * public static final String DELETE_AUDIT_LOG_EXPORT_ENTRY = "DELETE FROM " +
	 * AuditLogConstant.AUDIT_LOG_EXPORT_TRACKER_TABLE + " WHERE \"" +
	 * IAuditLogExportEntryModel.ASSET_ID + "\" IN  ";
	 * 
	 * public static final String UPDATE_AUDIT_LOG_EXPORT_ENTRY = "UPDATE " +
	 * AuditLogConstant.AUDIT_LOG_EXPORT_TRACKER_TABLE+ " SET " + "\""
	 * +IAuditLogExportEntryModel.STATUS +"\" = ? ," + "\""
	 * +IAuditLogExportEntryModel.END_TIME +"\" = ? " + " WHERE \"" +
	 * IAuditLogExportEntryModel.ASSET_ID + "\" = ? ";
	 * 
	 * public static final String GET_AUDIT_LOG_EXPORT_ENTRY = "SELECT \""+
	 * IAuditLogExportEntryModel.ASSET_ID +"\"" + " FROM " +
	 * AuditLogConstant.AUDIT_LOG_EXPORT_TRACKER_TABLE + " WHERE \"" +
	 * IAuditLogExportEntryModel.END_TIME + "\" <= ? ";
	 * 
	 * public static final String DELETE_AUDIT_LOG_INFO_ENTRY = "DELETE FROM " +
	 * AuditLogConstant.AUDIT_LOG + " WHERE \"" + IAuditLogModel.DATE + "\" <= ? ";
	 * 
	 * public static final String GET_FAILED_AUDIT_LOG_EXPORT_ENTRY = "SELECT \""+
	 * IAuditLogExportEntryModel.ASSET_ID +"\"" + " FROM " +
	 * AuditLogConstant.AUDIT_LOG_EXPORT_TRACKER_TABLE + " WHERE \"" +
	 * IAuditLogExportEntryModel.STATUS + "\" =  " +
	 * AuditLogExportStatus.FAILED.getCode() + " AND \"" +
	 * IAuditLogExportEntryModel.ASSET_ID + "\" IN  ";
	 */
   
  public R execute(P model) throws Exception
  {
    R returnModel = null;
    Connection postgresConnection = postgresDataSource.getConnection();
    try {
      returnModel = executeInternal(postgresConnection, model);
      postgresConnection.commit();
    }
    catch (Exception e) {
      postgresConnection.rollback();
      throw e;
    }
    finally {
      postgresConnection.close();
    }
    return returnModel;
  };
  
}
