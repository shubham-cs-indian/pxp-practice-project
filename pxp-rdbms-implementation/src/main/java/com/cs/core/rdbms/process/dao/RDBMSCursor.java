package com.cs.core.rdbms.process.dao;

import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IRootDTO;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The cursor implementation aims at hiding the chosen design for its
 * implementation That's why the CursorProvider interface is key to separate the
 * execution of the cursor from the way of querying behind.
 *
 * @param <IDTO>
 *          is the type of DTO returned by the cursor
 * @author vallee
 */
public class RDBMSCursor<IDTO extends IRootDTO> implements IRDBMSOrderedCursor<IDTO>, Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private final ICursorProvider<IDTO> provider;
  private long                        count            = -1;
  private long                        lastPosition     =  0;
  
  /**
   * Initialization of the cursor (first time)
   *
   * @param provider
   *          the provide that executes queries
   * @throws RDBMSException
   */
  public RDBMSCursor(ICursorProvider<IDTO> provider) throws RDBMSException
  {
    this.provider = provider;
    RDBMSLogger.instance()
        .debug("Cursor end of initialization, count %d", this.count);
  }
  
  @Override
  public long getCount() throws RDBMSException
  {
    return provider.selectCount();
  }
  
  @Override
  public List<IDTO> getNext(int numberOfDTOs) throws RDBMSException
  {
    List<IDTO> selection = provider
        .selectFrom(((RDBMSAbstractDriver) RDBMSDriverManager.getDriver())
            .getOffsetExpression(numberOfDTOs, lastPosition));
    lastPosition += selection.size();
    RDBMSLogger.instance()
        .debug("Cursor end of next %d, position %d", numberOfDTOs, lastPosition);
    return selection;
  }
  
  @Override
  public List<IDTO> getNext(long startPosition, int numberOfDTOs) throws RDBMSException
  {
    lastPosition = startPosition;
    return getNext(numberOfDTOs);
  }
  
  @Override
  public long getPosition()
  {
    return lastPosition;
  }

	@Override
   public void setOrderBy(Map<String, OrderDirection> orderBy) throws RDBMSException {
		provider.setOrderBy(orderBy);
   }
}
