package com.cs.core.rdbms.tracking.idao;

import com.cs.core.bgprocess.idao.IBGPRelationshipDAO;
import com.cs.core.rdbms.auditlog.idao.IAuditLogDAO;
import com.cs.core.rdbms.auditlog.idao.IAuditLogExportDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.rdbms.process.idao.IBulkCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.tracking.idto.ISessionWarningDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO.LogoutType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * DAO access for session management and user logging tracking
 *
 * @author vallee
 */
/**
 * @author DP212
 *
 */
public interface IUserSessionDAO {

  /**
   * Open a logical RDBMS session and create a tracking information in the trail/audit table at first time
   *
   * @param userIID the user identifier
   * @param userName the user name
   * @param sessionID the current application session ID (as provided by application server environment)
   * @return the new opened session information
   * @throws RDBMSException
   */
  public IUserSessionDTO openSession(String userName, String sessionID) throws RDBMSException;

  /**
   * Reopen an already declared RDBMS session from an existing session ID
   *
   * @param sessionID
   * @return the current session information
   * @throws RDBMSException
   */
  public IUserSessionDTO openSession(String sessionID) throws RDBMSException;

  /**
   * Close the current RDBMS session and update tracking information
   *
   * @param sessionID the session that must be closed
   * @param logoutType the type of session closed (user logout, timeout, killed), please refer constants
   * @param remarks any comment to add to the logout track
   * @throws RDBMSException
   */
  public void closeSession(String sessionID, LogoutType logoutType, String remarks)
          throws RDBMSException;

  /**
   * Close all remaining open sessions due to server shutdown
   *
   * @throws RDBMSException
   */
  public void shutdownSessions() throws RDBMSException;

  /**
   * @param userSessionDTO user session information
   * @return true or false depending on if the User Session is active
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public boolean isUserSessionActive(IUserSessionDTO userSessionDTO) throws RDBMSException;

  /**
   * For audit/trail purpose retrieve sessions attached to the current user
   *
   * @param userName the ODB user identification by name
   * @return the list of sessions order by logging time descending
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<IUserSessionDTO> getCurrentUserSessions(String userName)
          throws RDBMSException;

  /**
   * For audit/trail purpose retrieve all session warnings attached to the current user
   *
   * @param userID identifier of the related user
   * @return the list of session warnings order by logging time descending
   * @throws RDBMSException
   */
  public IRDBMSOrderedCursor<ISessionWarningDTO> getCurrentUserWarnings(String userID)
          throws RDBMSException;

  /**
   * @param warnings the warnings to be cleaned out (with user aknowledgement)
   */
  public void clearWarnings(ISessionWarningDTO... warnings);

  /**
   * Assume a catalog and a local is already existing in database This method is
   * recommended before calling openLocaleCatalog
   *
   * @param localeID the locale ID of the catalog to open
   * @param catalogCode the catalog user Code
   * @param organizationCode , code of the organization
   * @return a locale catalog DTO + locale information
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public ILocaleCatalogDTO newLocaleCatalogDTO(String localeID, String catalogCode, String organizationCode) throws RDBMSException;
  
  /**
   * The entry point to access single DAO interfaces within a catalog and a current locale. If the catalogIID is not correctly initialized,
   * an exception is returned
   *
   * @param userSessionDTO user session information
   * @param catalog the locale catalog information corresponding to the catalog to open
   * @return a transaction interface that holds a pointer to the current user session
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public ILocaleCatalogDAO openLocaleCatalog(IUserSessionDTO userSessionDTO,
          ILocaleCatalogDTO catalog) throws RDBMSException;

  /**
   * The entry point to access single DAO interfaces within a catalog and a current locale.In that case, the catalog corresponding to the 
   * base entity is automatically selected
   *
   * @param userSessionDTO user session information
   * @param baseEntityIID the identifier of the entity that defines where to open the catalog
   * @return a transaction interface that holds a pointer to the current user session
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public ILocaleCatalogDAO openLocaleCatalog(IUserSessionDTO userSessionDTO, long baseEntityIID) throws RDBMSException;

  /**
   * The entry point to access DAO interface for bulk operations within a catalog and a current locale.
   *
   * @param userSessionDTO
   * @param catalog
   * @return
   * @throws RDBMSException
   */
  public IBulkCatalogDAO openBulkCatalog(IUserSessionDTO userSessionDTO, ILocaleCatalogDTO catalog)
          throws RDBMSException;
  
  /**
   * This method returns a new AuditLogDAO
   * 
   * @return new AuditLogDAO
   */
  public IAuditLogDAO newAuditLogDAO();

  public IDownloadTrackerDAO newDownloadTrackerDAO();

  /**
   * @return This method returns a new AuditLogExportDAO.
   */
  public IAuditLogExportDAO newAuditLogExportDAO();
  
  /**
   *  this method returns BGPRelationshipDAO
   * @return new BGPRelationshipDAO 
   */
  public IBGPRelationshipDAO newBGPRelationshipDAO();
  
}
