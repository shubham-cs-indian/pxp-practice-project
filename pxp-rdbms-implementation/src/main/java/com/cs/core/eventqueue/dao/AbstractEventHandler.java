package com.cs.core.eventqueue.dao;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.cs.core.eventqueue.dto.EventDTO;
import com.cs.core.eventqueue.idao.IEventHandler;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.app.RDBMSAppDriver;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.iapp.IRDBMSAppDriver;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * Helper class to implement IEventHandler
 *
 * @author vallee
 */
public abstract class AbstractEventHandler implements IEventHandler {
  
  protected static IUserSessionDAO    userSessionDao = null;
  protected static IUserSessionDTO    userSessionDTO = null;
  protected final IConfigurationDAO configurationDao;
  protected EventDTO                currentEvent;
  
  protected abstract void run_New();
  
  public AbstractEventHandler(IRDBMSAppDriver driver) throws RDBMSException
  {
    this.configurationDao = driver.newConfigurationDAO();
    getUserSession(driver); // initialize user session DAO and DTO at initialize
  }
  
  /**
   * @param driver
   *          the application driver used for initialization
   * @return the generic session used by event handlers
   * @throws RDBMSException
   */
  private static UserSessionDTO getUserSession(IRDBMSAppDriver driver) throws RDBMSException
  {
    if (userSessionDTO == null) {
      userSessionDao = driver.newUserSessionDAO();
      userSessionDTO = userSessionDao.openSession(RDBMSAppDriver.ADMIN_USER_NAME,
          IUserSessionDTO.createUniqueSessionID("EV"));
    }
    return (UserSessionDTO) userSessionDTO;
  }
  
  /**
   * @return the generic session used by event handlers
   */
  protected static UserSessionDTO getUserSession()
  {
    return (UserSessionDTO) userSessionDTO;
  }
  
  @Override
  public void setEvent(IEventDTO event)
  {
    currentEvent = (EventDTO) event;
  }
  
  /**
   * @param localeID
   *          the locale ID in which to open the catalog
   * @return the opened Locale Catalog DAO from the catalog ID defined in the
   *         current event
   * @throws RDBMSException
   */
  protected ILocaleCatalogDAO openCatalog(String localeID) throws RDBMSException
  {
    ILocaleCatalogDTO catalogDTO = new LocaleCatalogDTO(localeID, currentEvent.getCatalog().getCatalogCode(),
        currentEvent.getCatalog().getOrganizationCode());
    return userSessionDao.openLocaleCatalog(userSessionDTO, catalogDTO);
  }
  
  /**
   * @param localeID
   *          the locale ID in which to open the base entity interface
   * @return the opened Base Entity DAO from the catalog ID and the Object ID
   *         defined in the current event
   * @throws RDBMSException
   */
  protected IBaseEntityDAO openBaseEntity(String localeID) throws RDBMSException, CSFormatException
  {
    ILocaleCatalogDAO localeDao = openCatalog(localeID);
    BaseEntityDTO baseEntity = new BaseEntityDTO();
    baseEntity.fromPXON(currentEvent.getJSONExtract().toString());
    return localeDao.openBaseEntity(baseEntity);
  }
  
  /**
   * The method runs in transaction boundary (Programmatic transaction)
   * Starting point of all event queue process 
   * If an exception is thrown while executing, it rollbacks all the SQL dataBase operation in a given process
   */
  @Override
  public void run()
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver)RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.execute(new TransactionCallbackWithoutResult()
    {
      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        run_New();
      }
    });
  }
}
