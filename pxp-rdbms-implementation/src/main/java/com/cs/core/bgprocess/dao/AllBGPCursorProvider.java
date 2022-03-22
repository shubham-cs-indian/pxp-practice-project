package com.cs.core.bgprocess.dao;

import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.idao.IBGPCursor.Characteristic;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AllBGPCursorProvider implements IBGPCursorProvider<IBGProcessDTO> {
  
  private static final String Q_ROOT_TABLE = "select %s from pxp.backgroundprocess b join pxp.userconfig u on b.userIID = u.userIID";
  private static final String FILTER_BY_SERVICE = "b.service = '%s'";
  private static final String FILTER_BY_STATUS = "b.status = %d";
  private static final String FILTER_BY_USER_IIDS = "b.userIID IN (%s)";
  private static final String FILTER_BY_USER_NAMES = "u.userName IN (%s)";

  private Characteristic orderingCharacteristic = Characteristic.created;
  private OrderDirection direction = OrderDirection.DESC;
  private String service = "";
  private final Set<Long> userIIDs = new HashSet<>();
  private final Set<String> userNames = new HashSet<>();
  private BGPStatus status = BGPStatus.UNDEFINED;
  
  private String buildGenericQuery() {
    String query = Q_ROOT_TABLE;
    String whereOrAnd = " where ";
    if ( !service.isEmpty() ) {
      query += whereOrAnd + String.format( FILTER_BY_SERVICE, service);
      whereOrAnd = " and ";
    }
    if ( status != BGPStatus.UNDEFINED ) {
      query += whereOrAnd + String.format( FILTER_BY_STATUS, status.ordinal());
      whereOrAnd = " and ";
    }
    if ( userIIDs.size() > 0 ) {
      query += whereOrAnd + String.format( FILTER_BY_USER_IIDS, Text.join(",", userIIDs));
    } else if ( userNames.size() > 0 ) {
      query += whereOrAnd + String.format( FILTER_BY_USER_NAMES, Text.join(",", userNames));
    }
    return query;
  }

  
  @Override
  public long selectCount() throws RDBMSException
  {
    long[] count = { 0 };
    String query = String.format( buildGenericQuery(), "count(*) as nbgps");
    RDBMSConnectionManager.instance().runTransaction(( currConnection) -> {
          Statement countStmt = currConnection.getConnection().createStatement();
          ResultSet resultSet = countStmt.executeQuery(query);
          if (resultSet.next())
            count[0] = resultSet.getLong("nbgps");
        });
    return count[0];
  }
  
  @Override
  public List<IBGProcessDTO> selectFrom(String offsetExpression) throws RDBMSException
  {
    List<IBGProcessDTO> results = new ArrayList<>();
    String orderAndOffsetExpression = " order by b." + orderingCharacteristic + " " + direction
        + " " + offsetExpression;
    String query = String.format( buildGenericQuery(), "b.*, u.userName") + orderAndOffsetExpression;
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
          Statement selectStmt = connection.getConnection().createStatement();
          IResultSetParser parser = connection.getResultSetParser(selectStmt.executeQuery(query));
          while (parser.next()) {
            results.add(new BGProcessDTO(parser));
          }
        });
    
    return results;
  }
 
  @Override
  public void setServiceFilter(String service)
  {
    this.service = service;
  }
  
  @Override
  public void setStatusFilter(BGPStatus status)
  {
    this.status = status;
  }
  
  @Override
  public void setUserFilter(Set<Long> userIIDs)
  {
    this.userIIDs.addAll(userIIDs);
    this.userNames.clear();
  }

  @Override
  public void setUserNameFilter(Set<String> userNames) {
    this.userNames.addAll( userNames.stream()
        .map( Text::escapeStringWithQuotes )
        .collect( Collectors.toSet())
    );
    this.userIIDs.clear();
  }
  
  @Override
  public void setOrdering(OrderDirection direction, Characteristic orderingCharacteristic)
  {
    this.direction = direction;
    this.orderingCharacteristic = orderingCharacteristic;
  }

  @Override
  public void serOrderBy(IRDBMSOrderedCursor.OrderDirection direction, String... trackingFields)
      throws RDBMSException
  {
    // empty
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public void serOrderBy(IRDBMSOrderedCursor.OrderDirection direction, PropertyDTO property)
      throws RDBMSException
  {
    // empty
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public void setFilterBy(IClassifierDTO... classifiers)
  {
    // empty
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public void setFilterBy(IValueRecordDTO... records)
  {
    // empty
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public void setFilterBy(ITagsRecordDTO... records)
  {
    // empty
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public void allowChildren()
  {
    // empty
    throw new UnsupportedOperationException("Not supported yet.");
  }
  
  @Override
  public void addExclusion(Long... baseEntityIIDs)
  {
    // empty
    throw new UnsupportedOperationException("Not supported yet.");
  }


  @Override
  public void setOrderBy(Map orderBy) throws RDBMSException
  {
    throw new UnsupportedOperationException("Not supported yet.");
  }
 }

