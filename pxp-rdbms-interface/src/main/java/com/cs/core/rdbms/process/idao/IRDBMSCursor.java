package com.cs.core.rdbms.process.idao;

import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

import java.util.List;

/**
 * The cursor represents a RDBMS cursor that fetches data with pagination
 *
 * @param <IDTO> refers to the DTO returned through the cursor
 * @author vallee
 */
public interface IRDBMSCursor<IDTO extends ISimpleDTO> {

  /**
   * This service involves a "select count(*)" query at first call
   *
   * @return the number of DTOs currently accessed through the cursor
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public long getCount() throws RDBMSException;

  ;
  
  /**
   * Fetch from the cursor in random order
   *
   * @param numberOfDTOs
   *          the max number of DTOs to fetch from the cursor
   * @return the list of fetched DTOs
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public List<IDTO> getNext(int numberOfDTOs) throws RDBMSException;

  /**
   * Fetch from the cursor in random order starting a defined position
   *
   * @param startPosition the starting position of DTO
   * @param numberOfDTOs the max number of DTOs to fetch from the cursor
   * @return the list of fetched DTOs
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   */
  public List<IDTO> getNext(long startPosition, int numberOfDTOs) throws RDBMSException;

  /**
   * @return the number of DTOs already fetched from the cursor
   */
  public long getPosition();

  /**
   * Specification of an order direction for getting the data
   */
  public enum OrderDirection {
    ASC, DESC
  }
}
