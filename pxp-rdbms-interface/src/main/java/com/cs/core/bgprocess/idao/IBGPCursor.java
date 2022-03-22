package com.cs.core.bgprocess.idao;

import java.util.Set;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.rdbms.process.idao.IRDBMSCursor;

/**
 * Cursor to navigate through BGP process information
 *
 * @author vallee
 */
public interface IBGPCursor extends IRDBMSCursor<IBGProcessDTO> {

  /**
   * @param service defines the service name on which to filter process information
   */
  public void setServiceFilter(String service);

  /**
   * @param status defines the status on which to filter process information
   */
  public void setStatusFilter(IBGProcessDTO.BGPStatus status);

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

  /**
   * List of usable characteristics of ordering
   */
  public enum Characteristic {
    created, started, ended, userPriority, service;
  }
}
