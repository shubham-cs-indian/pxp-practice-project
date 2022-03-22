package com.cs.core.rdbms.process.dao;

import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IRootDTO;
import java.util.List;
import java.util.Map;

/**
 * A cursor requires two method implemented: count and selectFrom
 *
 * @param <IDTO>
 *          the type of DTO interface returned by the query
 * @author vallee
 */
public interface ICursorProvider<IDTO extends IRootDTO> {
  
  /**
   * @return the number of records involved in the query
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public long selectCount() throws RDBMSException;
  
  /**
   * Specify an order by clause to the cursor provider current requests /!\ This
   * ordering is exclusive with setOrder by tracking fields
   *
   * @param direction
   *          specifies Ascending or Descending
   * @param property
   *          the entity property against which to order the query result
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public void setOrderBy(Map<String, OrderDirection> orderBy) throws RDBMSException;
  
  /**
   * @param offsetExpression
   *          the query complement that contains offset/limit directive
   * @return the list of fetched records
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public List<IDTO> selectFrom(String offsetExpression) throws RDBMSException;
  
}
