package com.cs.core.bgprocess.dao;

import com.cs.core.bgprocess.idao.IBGPCursor.Characteristic;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.dao.ICursorProvider;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IRootDTO;
import java.util.Set;

public interface IBGPCursorProvider<IBGProcessDTO extends IRootDTO> extends ICursorProvider<IBGProcessDTO> {

  /**
   * @param service defines the service name on which to filter process information
   */
  public void setServiceFilter(String service);

  /**
   * @param status defines the status on which to filter process information
   */
  public void setStatusFilter(BGPStatus status);

  /**
   * @param userIIDs defines the submitting user on which to filter process information
   */
  public void setUserFilter(Set<Long> userIIDs);

  /**
   * @param userNames defines the submitting user on which to filter process information
   */
  public void setUserNameFilter(Set<String> userNames);

  /**
   * Define an ordering of the cursor according to available characteristics
   *
   * @param direction
   * @param orderingCharacteristic
   */
  public void setOrdering(OrderDirection direction, Characteristic orderingCharacteristic);
  
  public void serOrderBy(IRDBMSOrderedCursor.OrderDirection direction, String... trackingFields) throws RDBMSException;
  
  public void serOrderBy(IRDBMSOrderedCursor.OrderDirection direction, PropertyDTO property) throws RDBMSException;
  
  public void setFilterBy(IClassifierDTO... classifiers);
  
  public void setFilterBy(IValueRecordDTO... records);
  
  public void setFilterBy(ITagsRecordDTO... records);
  
  public void allowChildren();
  
  public void addExclusion(Long... baseEntityIIDs);
}
