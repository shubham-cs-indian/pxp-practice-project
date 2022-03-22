package com.cs.di.runtime.entity.dao;

import java.util.List;

import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.idto.INotificationDTO;

public interface INotificationDAO {
  
  /**
   * create notification for user
   * @param workflowDTO
   * @throws RDBMSException
   */
  public void createNotificationInstance(INotificationDTO notificationDTO) throws RDBMSException;
  
  /**
   * get all notifications for user
   * @return
   * @throws RDBMSException
   */
  public List<INotificationDTO> getAllNotificationForUser() throws RDBMSException;

  /**
   * delete all notifications for user
   * @throws RDBMSException
   */
  public void deleteAllNotificationForUser() throws RDBMSException;
  
  public void deleteNotificationById(Long id) throws RDBMSException;
}