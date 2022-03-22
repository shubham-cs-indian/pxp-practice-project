package com.cs.core.rdbms.iapp;

import com.cs.core.eventqueue.idao.IEventQueueDAO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.tracking.idao.IObjectTrackingDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.irdbms.IRDBMSDriver;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * The RDBMS driver is the factory entry point for DAOs From there, only 2 possibilities: - creating a new user session and starting
 * transactions from this DAO - creating a tracking interface for simple action logs
 *
 * @author vallee
 */
public interface IRDBMSAppDriver extends IRDBMSDriver {

  /**
   * @param prefix any text with alphabetic character at first position
   * @return a unique ID based on concatenation of prefix followed by a sequence number
   * @throws RDBMSException
   */
  public String newUniqueID(String prefix) throws RDBMSException;

  /**
   * @return a DAO from the configuration synchronization interface
   */
  public IConfigurationDAO newConfigurationDAO();

  /**
   * @return a IUserSessionDAO from the Tracking interface
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IUserSessionDAO newUserSessionDAO() throws RDBMSException;

  /**
   * @return an ObjectTrackingDAO to access Tracking services
   */
  public IObjectTrackingDAO newObjectTrackingDAO();

  /**
   * @return an EventQueueDAO to access event-queue services
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public IEventQueueDAO newEventQueueDAO() throws RDBMSException;


  /**
   * @param userSession the current user session
   * @return a revision interface to manage object revisions
   */
  public IRevisionDAO newRevisionDAO(IUserSessionDTO userSession);

}
