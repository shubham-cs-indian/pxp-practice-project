package com.cs.core.rdbms.process.idao;

import java.util.Map;

import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;
/**
 * The ordered cursor represents a cursor that can be ordered/filtered by characteristics or properties Possible ordering of the returned
 * cursor are: - Base entity ID, use characteristic field: ORDER_BY_ENTITY_ID - Base locale ID, use characteristic field:
 * ORDER_BY_ENTITY_LOCALE - any property ID value (for attribute only) Possible filtering on the returned cursor are (filters can be
 * cumulated): - classifier - any property ID value (for attribute and tags)
 *
 * @param <IDTO> refers to the DTO returned through the cursor
 * @author vallee
 */
public interface IRDBMSOrderedCursor<IDTO extends ISimpleDTO> extends IRDBMSCursor<IDTO> {
	
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
	  
}
