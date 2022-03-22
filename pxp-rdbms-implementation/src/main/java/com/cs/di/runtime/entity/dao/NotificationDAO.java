package com.cs.di.runtime.entity.dao;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.di.runtime.entity.dto.NotificationDTO;
import com.cs.di.runtime.entity.idto.INotificationDTO;

public class NotificationDAO  implements INotificationDAO{

  RDBMSAbstractDriver driver;
  private long userIID;
  
  static final String Q_CREATE_NOTIFICATION = "INSERT INTO pxp.notification (actedFor, actedBy, status, action, "
      + "description, createdOn) VALUES (?,?,?,?,?,?) RETURNING instanceIID";
  
  private String Q_GET_NOTIFICATIONS    = "select * from pxp.notification where actedFor = ";

  private String Q_Delete_NOTIFICATIONS    = "delete from pxp.notification where actedFor = ";
  
  private String Q_Delete_NOTIFICATION_BY_ID    = "delete from pxp.notification where instanceIID = ";
  
  private static final String INSTANCE_IID = "instanceIID";
  
  public NotificationDAO(long userIID) {
    this.userIID = userIID;
  }
  
  @Override
  public void createNotificationInstance(INotificationDTO notificationDTO) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(Q_CREATE_NOTIFICATION);
      statement.setLong(1, notificationDTO.getActedFor());
      statement.setLong(2, notificationDTO.getActedBy());
      statement.setString(3, notificationDTO.getStatus());
      statement.setString(4, notificationDTO.getAction());
      statement.setString(5, notificationDTO.getDescription());
      statement.setLong(6, notificationDTO.getCreatedOn());
      IResultSetParser resultParser = currentConn.getDriver().getResultSetParser(statement.executeQuery());
      while (resultParser != null && resultParser.next()) {
        notificationDTO.setInstanceIID(resultParser.getLong(INSTANCE_IID));
      }
    });
  }

  @Override
  public List<INotificationDTO> getAllNotificationForUser() throws RDBMSException
  {
    List<INotificationDTO> notificationDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(Q_GET_NOTIFICATIONS + userIID);
      IResultSetParser resultParser = currentConn.getDriver().getResultSetParser(statement.executeQuery());
      while (resultParser != null && resultParser.next()) {
        INotificationDTO notificationDTO = new NotificationDTO(resultParser);
        notificationDTOs.add(notificationDTO);
      }
    });
    return notificationDTOs;
    
  }
  
  @Override
  public void deleteAllNotificationForUser() throws RDBMSException
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          currentConn.prepareStatement(Q_Delete_NOTIFICATIONS + userIID)
              .executeUpdate();
        });
  }

  @Override
  public void deleteNotificationById(Long id) throws RDBMSException
  {
    RDBMSConnectionManager.instance()
    .runTransaction((RDBMSConnection currentConn) -> {
      currentConn.prepareStatement(Q_Delete_NOTIFICATION_BY_ID + id)
          .executeUpdate();
    });
  }
  
}
