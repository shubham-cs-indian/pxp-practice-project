package com.cs.core.bgprocess.dao;

import com.cs.core.bgprocess.idao.IBGPCursor;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.List;
import java.util.Set;

/**
 * @author vallee
 */
public class AllBGPDataCursor implements IBGPCursor {
  
  private long   lastPosition = 0;
  private final IBGPCursorProvider<IBGProcessDTO> provider;

  /**
   * Initialization of the cursor (first time)
   *
   * @param provider
   *          the provide that executes queries
   * @throws RDBMSException
   */
  public AllBGPDataCursor(IBGPCursorProvider<IBGProcessDTO> provider) throws RDBMSException
  {
    this.provider = provider;
  }
  
  @Override
  public long getCount() throws RDBMSException
  {
    return provider.selectCount();
  }
  
  @Override
  public List<IBGProcessDTO> getNext(int numberOfDTOs) throws RDBMSException
  {
    List<IBGProcessDTO> selection = provider
        .selectFrom(((RDBMSAbstractDriver) RDBMSDriverManager.getDriver())
            .getOffsetExpression(numberOfDTOs, lastPosition));
    lastPosition += selection.size();
    return selection;
  }
  
  @Override
  public List<IBGProcessDTO> getNext(long startPosition, int numberOfDTOs) throws RDBMSException
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
  public void setServiceFilter(String service)
  {
    provider.setServiceFilter(service);
  }

  @Override
  public void setStatusFilter(BGPStatus status)
  {
     provider.setStatusFilter(status);
  }

  @Override
  public void setUserFilter(Set<Long> userIIDs)
  {
    provider.setUserFilter(userIIDs);
  }

  @Override
  public void setUserNameFilter(Set<String> userNames)
  {
    provider.setUserNameFilter(userNames);
  }

  @Override
  public void setOrdering(OrderDirection direction, Characteristic orderingCharacteristic)
  {
    provider.setOrdering(direction, orderingCharacteristic);
  }
}
