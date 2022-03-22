package com.cs.core.rdbms.process.dao;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.services.resolvers.SearchRenderer;
import com.cs.core.rdbms.services.resolvers.SearchResolver;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSESearch;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.antlr.v4.misc.OrderedHashMap;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CursorProvider implements ICursorProvider<IBaseEntityDTO> {
  
  protected Map<String, OrderDirection> orderBy = new OrderedHashMap<>();
  protected final boolean               allowChildren;
  protected String                      searchExpression;
  
  /**
   * Cursor constructor
   * 
   */
  public CursorProvider(Boolean allowChildren, String searchExpression)
  {
    this.allowChildren = allowChildren;
    this.searchExpression = searchExpression;
  }
  
  @Override
  public void setOrderBy(Map<String, OrderDirection> orderBy) throws RDBMSException
  {
    this.orderBy = orderBy;
  }
  
  private static final String Q_COUNT = "select distinct(count(%s)) as count from %s";
  @Override
  public long selectCount() throws RDBMSException
  {
    long[] count = { 0 };
    RDBMSConnectionManager.instance().runTransaction(( currConnection) -> {
          CSEParser parser = new CSEParser();
          ICSESearch search = parser.parseSearch(searchExpression);
          SearchResolver searchResolver = new SearchResolver( currConnection);
          String queryTable = searchResolver.evaluateTotalResult(search.getScope(), search.getEvaluation());
          
          String countQuery = "";
          
          if (allowChildren) {
            countQuery = String.format(Q_COUNT, "baseentityiid", queryTable);
          }
          else {
            String querySource = String.format("%s a join pxp.baseentity be on a.baseentityiid = be.baseentityiid where childlevel = 1 and be.ismerged != true", queryTable);
            countQuery = String.format(Q_COUNT, "coalesce(be.topparentiid, be.baseentityiid)", querySource);
          }
          
          PreparedStatement statement = currConnection.prepareStatement(countQuery);
          ResultSet resultSet = statement.executeQuery();
          if (resultSet.next())
            count[0] = resultSet.getLong("count");
        });
    return count[0];
  }

  private List<BaseEntityDTO> render(String offsetExpression, SearchRenderer searchRenderer, String baseQuery)
      throws RDBMSException, SQLException, CSFormatException
  {
    String whereCondition = "";
    if (!allowChildren) {
      whereCondition = " where childlevel = 1 and ismerged != true ";
    }
    String queryWithSelectField = searchRenderer.getQueryWithSelectField(allowChildren, baseQuery);
    queryWithSelectField = searchRenderer.getOrderByExpression(queryWithSelectField, orderBy, !allowChildren, whereCondition);
    Set<Long> baseEntityIIDs = searchRenderer.getBaseEntities(queryWithSelectField, offsetExpression);
    return searchRenderer.renderSearchEntities(baseEntityIIDs);
  }
  
  private static final String Q_BASE_QUERY = "select ? from %s a";
  @Override
  public List<IBaseEntityDTO> selectFrom(String offsetExpression) throws RDBMSException
  {
    List<IBaseEntityDTO> baseEntities = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction(( currConnection) -> {
          CSEParser parser = new CSEParser();
          ICSESearch search = parser.parseSearch(searchExpression);
          SearchRenderer searchRenderer = new SearchRenderer( currConnection, search);
          SearchResolver searchResolver = new SearchResolver( currConnection);
          String queryTable = searchResolver.evaluateTotalResult(search.getScope(),
              search.getEvaluation());
          String baseQuery = String.format(Q_BASE_QUERY, queryTable);
          baseEntities.addAll(render(offsetExpression, searchRenderer, baseQuery));
        });
    
    return baseEntities;
  }
  
}
