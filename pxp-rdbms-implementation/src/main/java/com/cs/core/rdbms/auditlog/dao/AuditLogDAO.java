package com.cs.core.rdbms.auditlog.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cs.core.rdbms.auditlog.dto.AuditLogDTO;
import com.cs.core.rdbms.auditlog.dto.AuditLogFilterDTO.AuditLogFilterDTOBuilder;
import com.cs.core.rdbms.auditlog.dto.AuditLogDTO.AuditLogDTOBuilder;
import com.cs.core.rdbms.auditlog.idao.IAuditLogDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.auditlog.idto.IAuditLogFilterDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class AuditLogDAO implements IAuditLogDAO {
  
  private static IAuditLogDAO auditLogDAOinstance;

  public static final String  INSERT_AUDIT_LOG        = "INSERT INTO auditlog (" + AuditLogDTO.ACTIVITY + ", " + AuditLogDTO.DATE + ", "
      + AuditLogDTO.ENTITY_TYPE + ", " + AuditLogDTO.ELEMENT + ", " + AuditLogDTO.ELEMENT_CODE + ", " + AuditLogDTO.ELEMENT_NAME + ", "
      + AuditLogDTO.ELEMENT_TYPE + ", " + AuditLogDTO.IP_ADDRESS + ", " + AuditLogDTO.USER_NAME + ") VALUES (?,?,?,?,?,?,?,?,?) ";
  
  public static final String  FETCH_AUDIT_LOG_RECORDS = " SELECT * FROM auditlog %s ORDER BY %s %s LIMIT %d OFFSET %d";

  public static final String  AUDIT_LOG_RECORDS_COUNT = "SELECT COUNT (" + AuditLogDTO.ACTIVITY + ") FROM auditlog %s";

  public static final String  FETCH_AUDIT_LOG_USERS   = " SELECT DISTINCT " + AuditLogDTO.USER_NAME + " FROM auditlog %s ORDER BY "
      + AuditLogDTO.USER_NAME + " %s LIMIT %d OFFSET %d";

  public static final String  AUDIT_LOG_USERS_COUNT   = "SELECT  COUNT ( DISTINCT " + AuditLogDTO.USER_NAME + " ) FROM auditlog %s";

  private AuditLogDAO() {
  }
  
  public static IAuditLogDAO getInstance()
  {
    if (auditLogDAOinstance == null) {
      synchronized (AuditLogDAO.class) {
        if (auditLogDAOinstance == null) {
          auditLogDAOinstance = new AuditLogDAO();
        }
      }
    }
    return auditLogDAOinstance;
  }

  @Override
  public void createAuditLogs(IAuditLogDTO... auditLogDTOs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement preparedStatement = currentConn.prepareStatement(INSERT_AUDIT_LOG);
      for (IAuditLogDTO auditLogInfo : auditLogDTOs) {
        preparedStatement.setShort(1, (short) auditLogInfo.getActivity().ordinal());
        preparedStatement.setLong(2, auditLogInfo.getDate());
        preparedStatement.setShort(3, (short) auditLogInfo.getEntityType().ordinal());
        preparedStatement.setShort(4, (short) auditLogInfo.getElement().ordinal());
        preparedStatement.setString(5, auditLogInfo.getElementCode());
        preparedStatement.setString(6, auditLogInfo.getElementName());
        preparedStatement.setString(7, auditLogInfo.getElementType());
        preparedStatement.setString(8, auditLogInfo.getIpAddress());
        preparedStatement.setString(9, auditLogInfo.getUserName());
        preparedStatement.executeUpdate();
      }
    });
  }

  @Override
  public List<IAuditLogDTO> fetchAuditLogs(StringBuilder filterQuery, String sortBy, String sortOrder, Long size, Long from)
      throws RDBMSException
  {
    StringBuilder finalFilterQuery = new StringBuilder();
    if (filterQuery.length() > 0) {
      finalFilterQuery.append("where " + filterQuery);
    }
    List<IAuditLogDTO> listOfAuditLog = new ArrayList<IAuditLogDTO>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(FETCH_AUDIT_LOG_RECORDS, finalFilterQuery.toString(), sortBy, sortOrder, size, from);
      PreparedStatement queryStatement = currentConn.prepareStatement(query);
      queryStatement.execute();
      IResultSetParser result = currentConn.getResultSetParser(queryStatement.getResultSet());
      while (result.next()) {
        listOfAuditLog.add(new AuditLogDTO(result));
      }
    });
    return listOfAuditLog;
  }

  @Override
  public IAuditLogDTO newAuditLogDTO(ServiceType activity, Long date, Elements element,
      String elementCode, String elementName, String elementType, Entities entityType,
      String ipAddress, String userName)
  {
    return new AuditLogDTOBuilder().activity(activity)
                                   .userName(userName)
                                   .date(date)
                                   .ipAddress(ipAddress)
                                   .entityType(entityType)
                                   .element(element)
                                   .elementType(elementType)
                                   .elementCode(elementCode)
                                   .elementName(elementName)
                                   .build();
  }

  @Override
  public long getAuditLogCount(StringBuilder filterQuery) throws RDBMSException
  {
    long[] recordsCount = { 0 };

    StringBuilder finalFilterQuery = new StringBuilder();
    if (filterQuery.length() > 0) {
      finalFilterQuery.append("where " + filterQuery);
    }

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(AUDIT_LOG_RECORDS_COUNT, finalFilterQuery.toString());
      PreparedStatement queryStatement = currentConn.prepareStatement(query);
      ResultSet result = queryStatement.executeQuery();
      if (result.next()) {
        recordsCount[0] = result.getLong("count");
      }

    });
    return recordsCount[0];
  }

  @Override
  public IAuditLogFilterDTO newAuditLogFilterDTO(String elementName, String elementCode, String ipAddress, String activityId, List<String> userNames,
      List<String> activities, List<String> entityTypes, List<String> elements, List<String> elementTypes, Long fromDate, Long toDate)
  {
    return new AuditLogFilterDTOBuilder().elementName(elementName)
                                         .elementCode(elementCode)
                                         .ipAddress(ipAddress)
                                         .activityIID(activityId)
                                         .userNames(userNames)
                                         .activities(activities)
                                         .entityTypes(entityTypes)
                                         .elements(elements)
                                         .elementTypes(elementTypes)
                                         .fromDate(fromDate)
                                         .toDate(toDate)
                                         .build();
  }

  @Override
  public List<IAuditLogDTO> fetchAllAuditLogUsers(String searchText, String sortOrder, Long size, Long from) throws RDBMSException
  {
    StringBuilder filterQuery = new StringBuilder();
    if(searchText != null && !searchText.isEmpty()) {
      filterQuery.append("where " + AuditLogDTO.USER_NAME + " ilike '%" + searchText + "%' ");
    }

    List<IAuditLogDTO> auditLogUserList = new ArrayList<IAuditLogDTO>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(FETCH_AUDIT_LOG_USERS, filterQuery.toString(), sortOrder, size, from);
      PreparedStatement queryStatement = currentConn.prepareStatement(query);
      ResultSet result = queryStatement.executeQuery();
      while (result.next()) {
        IAuditLogDTO logRecord = new AuditLogDTOBuilder().userName(result.getString(AuditLogDTO.USER_NAME)).build();
        auditLogUserList.add(logRecord);
      }
    });
    return auditLogUserList;
  }

  @Override
  public long getAuditLogUsersCount(String searchText) throws RDBMSException
  {
    long[] recordsCount = { 0 };

    StringBuilder filterQuery = new StringBuilder();
    if(searchText != null && !searchText.isEmpty()) {
      filterQuery.append("where " + AuditLogDTO.USER_NAME + " ilike '%" + searchText + "%' ");
    }

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String query = String.format(AUDIT_LOG_USERS_COUNT, filterQuery.toString());
      PreparedStatement queryStatement = currentConn.prepareStatement(query);
      ResultSet result = queryStatement.executeQuery();
      if (result.next()) {
        recordsCount[0] = result.getLong("count");
      }

    });
    return recordsCount[0];
  }

  private StringBuilder searchFilterQuery(StringBuilder filterQuery, String filterText, String filterColumn)
  {
    if (filterText != null && !filterText.isEmpty()) {
      String quotedText = filterText.replaceAll("[\\W|^_]", "\\\\$0");
      if (filterQuery.length() > 0) {
        filterQuery.append(" and ");
      }
      filterQuery.append(filterColumn + " ilike '%" + quotedText + "%' ");
    }
    return filterQuery;
  }

  private StringBuilder conditionFilterQuery(StringBuilder filterQuery, List<String> filterList, String filterColumn)
  {
    if(!filterList.isEmpty()) {
      if(filterQuery.length() > 0) {
        filterQuery.append(" and ");
      }
      filterQuery.append(filterColumn + " in (");
      int arraySize = filterList.size();
      for(String filter : filterList) {
        arraySize--;
        if(filterColumn == AuditLogDTO.ACTIVITY) {
          int ordinalValue =  ServiceType.valueOf(filter).ordinal();
          filterQuery.append("'" + ordinalValue + "'");
        }
        else if(filterColumn == AuditLogDTO.ENTITY_TYPE) {
          int ordinalValue = Entities.valueOf(filter).ordinal();
          filterQuery.append("'" + ordinalValue + "'");
        }
        else if(filterColumn == AuditLogDTO.ELEMENT) {
          int ordinalValue = Elements.valueOf(filter).ordinal();
          filterQuery.append("'" + ordinalValue + "'");
        }
        else {
          filterQuery.append("'" + filter + "'");
        }

        if(arraySize != 0) {
          filterQuery.append(", ");
        }
      }
      filterQuery.append(")");
    }
    return filterQuery;
  }

  @Override
  public StringBuilder getFilterQuery(IAuditLogFilterDTO auditLogDTO)
  {
    StringBuilder filterQuery = new StringBuilder();
    String activityId = auditLogDTO.getActivityIID();
    Long fromDate = auditLogDTO.getFromDate();
    Long toDate = auditLogDTO.getToDate();

    if (activityId != null && !activityId.isEmpty()) {
      filterQuery.append(AuditLogDTO.ACTIVITY_IID + " = " + activityId);
    }
    filterQuery = searchFilterQuery(filterQuery, auditLogDTO.getElementName(), AuditLogDTO.ELEMENT_NAME);
    filterQuery = searchFilterQuery(filterQuery, auditLogDTO.getElementCode(), AuditLogDTO.ELEMENT_CODE);
    filterQuery = searchFilterQuery(filterQuery, auditLogDTO.getIpAddress(), AuditLogDTO.IP_ADDRESS);
    filterQuery = conditionFilterQuery(filterQuery, auditLogDTO.getUserName(), AuditLogDTO.USER_NAME);
    filterQuery = conditionFilterQuery(filterQuery, auditLogDTO.getActivities(), AuditLogDTO.ACTIVITY);
    filterQuery = conditionFilterQuery(filterQuery, auditLogDTO.getEntityTypes(), AuditLogDTO.ENTITY_TYPE);
    filterQuery = conditionFilterQuery(filterQuery, auditLogDTO.getElements(), AuditLogDTO.ELEMENT);
    filterQuery = conditionFilterQuery(filterQuery, auditLogDTO.getElementTypes(), AuditLogDTO.ELEMENT_TYPE);

    if (fromDate != null && toDate != null) {
      if (filterQuery.length() > 0) {
        filterQuery.append(" and ");
      }
      filterQuery.append(AuditLogDTO.DATE + " between " + fromDate + " and " + toDate + " ");
    }

    return filterQuery;
  }

}
