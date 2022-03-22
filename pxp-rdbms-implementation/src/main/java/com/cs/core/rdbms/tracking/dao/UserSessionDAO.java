package com.cs.core.rdbms.tracking.dao;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.PreparedStatement;

import org.apache.commons.dbutils.QueryRunner;

import com.cs.core.bgprocess.dao.BGPRelationshipDAO;
import com.cs.core.bgprocess.idao.IBGPRelationshipDAO;
import com.cs.core.rdbms.auditlog.dao.AuditLogDAO;
import com.cs.core.rdbms.auditlog.dao.AuditLogExportDAO;
import com.cs.core.rdbms.auditlog.dto.AuditLogDTO;
import com.cs.core.rdbms.auditlog.idao.IAuditLogDAO;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.process.dao.BulkCatalogDAO;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IBulkCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.ISessionWarningDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.rdbms.downloadtracker.dao.DownloadTrackerDAO;

/**
 * User Logging Data Access Object Layer Implementation. Provides all user
 * session management services
 *
 * @author PankajGajjar
 */
public class UserSessionDAO implements IUserSessionDAO {
  
  @Override
  public IUserSessionDTO openSession(String userName, String sessionID) throws RDBMSException
  {
    final Long loginTime = System.currentTimeMillis();
    UserDTO userDTO = new UserDTO(0L, userName);
    String[] serverHostName = { "Undefined host" };
    try {
      serverHostName[0] = InetAddress.getLocalHost()
          .getHostName();
    }
    catch (UnknownHostException exc) { // just log and ignore
      // ignore.
    }
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_openSession")
              .setInput(ParameterType.STRING, sessionID)
              .setInput(ParameterType.STRING, serverHostName[0])
              .setInput(ParameterType.STRING, userName)
              .execute();
          userDTO.setIID(result.getLong()); // setting userIID as IID
        });
    
    return new UserSessionDTO(new SessionDTO(sessionID, loginTime), userDTO, LogoutType.UNDEFINED,
        0, "");
  }
  
  @Override
  public IUserSessionDTO openSession(String sessionID) throws RDBMSException
  {
    UserSessionDTO[] userSession = { new UserSessionDTO() };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement cs = currentConn
              .prepareStatement("select * from pxp.userSessionWithUser where sessionID = ?");
          cs.setString(1, sessionID);
          IResultSetParser result = currentConn.getResultSetParser(cs.executeQuery());
          if (!result.next()) {
            throw new RDBMSException(0, "Session",
                "Inconsistent session, cannot reopen sessionID: " + sessionID);
          }
          userSession[0] = new UserSessionDTO(result);
        });
    return userSession[0];
  }
  
  @Override
  public void closeSession(String sessionID, LogoutType logoutType, String remarks)
      throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          QueryRunner query = new QueryRunner();
          query.update(currentConn.getConnection(), String.format(
              "UPDATE pxp.userSession SET logoutTime = %s, logoutType = ?, remarks = ? WHERE sessionId = ?",
              currentConn.getDriver().getSystemDateExpression()), logoutType.ordinal(), remarks, sessionID);
        });
  }
  
  @Override
  public void shutdownSessions() throws RDBMSException
  {
    String[] hostname = { "Undefined Host" };
    try {
      hostname[0] = InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException ex) { // just log and ignore...
      RDBMSLogger.instance().exception(ex);
    }
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          QueryRunner query = new QueryRunner();
          query.update(currentConn.getConnection(),
              String.format(
                  "UPDATE pxp.userSession SET logoutTime = %s, logoutType = ?, remarks = ? "
                      + "WHERE hostname = ? and logoutTime is null",
                  currentConn.getDriver().getSystemDateExpression()),
              LogoutType.SHUTDOWN.ordinal(), "Server Shutdown", hostname[0]);
        });
  }
  
  @Override
  public void clearWarnings(ISessionWarningDTO... warnings)
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public IRDBMSOrderedCursor<IUserSessionDTO> getCurrentUserSessions(String userID)
      throws RDBMSException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public IRDBMSOrderedCursor<ISessionWarningDTO> getCurrentUserWarnings(String userID)
      throws RDBMSException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public boolean isUserSessionActive(IUserSessionDTO userSessionDTO) throws RDBMSException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public ILocaleCatalogDAO openLocaleCatalog(IUserSessionDTO userSessionDTO,
      ILocaleCatalogDTO catalog) throws RDBMSException
  {
    if (catalog == null || catalog.getCatalogCode().isEmpty()) {
      throw new RDBMSException(1100, "RDBMSException:openLocaleCatalog",
          "catalog can't open with empty Catalog code");
    }
    return new LocaleCatalogDAO(userSessionDTO, catalog);
  }
  
  private static final String Q_GET_CATALOG = "select catalogcode, baselocaleid, organizationCode from pxp.baseentity where baseentityiid = ?";
  @Override
  public ILocaleCatalogDAO openLocaleCatalog(IUserSessionDTO userSessionDTO, long baseEntityIID) throws RDBMSException
  {
    String [] catalogCode = {""};
    String [] localeID = {""};
    String [] organizationCode = {""};
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
        PreparedStatement query = currentConn.prepareStatement(Q_GET_CATALOG);
        query.setLong(1, baseEntityIID);
        IResultSetParser result = currentConn.getResultSetParser(query.executeQuery());
        if (result.next()) {
          catalogCode[0] = result.getString("catalogcode");
          localeID[0] = result.getString("baselocaleid");
          organizationCode[0] = result.getString("organizationCode");
        }
    });
    return new LocaleCatalogDAO(userSessionDTO, new LocaleCatalogDTO( localeID[0], catalogCode[0], organizationCode[0]));
  }
  
  @Override
  public IBulkCatalogDAO openBulkCatalog(IUserSessionDTO userSessionDTO, ILocaleCatalogDTO catalog)
      throws RDBMSException
  {
    if (catalog == null || catalog.getCatalogCode().isEmpty()) {
      throw new RDBMSException(1100, "RDBMSException:openLocaleCatalog",
          "catalog can't open with empty Catalog code");
    }
    return new BulkCatalogDAO(userSessionDTO, catalog);
  }
  
  @Override
  public ILocaleCatalogDTO newLocaleCatalogDTO(String localeID, String catalogCode, String organizationCode) throws RDBMSException
  {
    return new LocaleCatalogDTO(localeID, catalogCode, organizationCode);
  }

  @Override
  public IAuditLogDAO newAuditLogDAO()
  {
    return AuditLogDAO.getInstance();
  }

  @Override
  public IDownloadTrackerDAO newDownloadTrackerDAO()
  {
    return DownloadTrackerDAO.getInstance();
  }

  @Override
  public IAuditLogExportDAO newAuditLogExportDAO()
  {
    return AuditLogExportDAO.getInstance();
  }

  @Override
  public IBGPRelationshipDAO newBGPRelationshipDAO()
  {
    return BGPRelationshipDAO.getInstance();
  }
}
