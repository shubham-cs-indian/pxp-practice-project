package com.cs.core.rdbms.taxonomyinheritance.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class TaxonomyInheritanceDAS extends RDBMSDataAccessService {

  public TaxonomyInheritanceDAS(RDBMSConnection connection)
  {
    super(connection);
  }
  
 /**
  * insert or update Taxonomy Inheritance Conflict
  * @param taxonomyInheritanceDTO
  * @throws SQLException
  * @throws RDBMSException
  */
  public void updateTaxonomyInheritanceConflict(ITaxonomyInheritanceDTO taxonomyInheritanceDTO)
      throws SQLException, RDBMSException
  {
    driver.getProcedure(currentConnection, "pxp.sp_upsertTaxonomyInheritanceConflict")
        .setInput(ParameterType.IID, taxonomyInheritanceDTO.getEntityIID())
        .setInput(ParameterType.IID, taxonomyInheritanceDTO.getSourceEntityIID())
        .setInput(ParameterType.IID, taxonomyInheritanceDTO.getPropertyIID())
        .setInput(ParameterType.BOOLEAN, taxonomyInheritanceDTO.getIsResolved() == null ? false : taxonomyInheritanceDTO.getIsResolved())
        .execute();
  }
  
  
  private static final String GET_TAXONOMY_CONFLICT = " select * from pxp.baseentitytaxonomyconflictlink where entityIID = ?";
  
  /**
   * 
   * @param entityIID
   * @return ResultSetParser that corresponds to Taxonomy Inheritance Conflict information 
   * @throws SQLException
   * @throws RDBMSException
   */
  public IResultSetParser getTaxonomyInheritanceConflict(Long entityIID)
      throws SQLException, RDBMSException
  {
    PreparedStatement statement = currentConnection.prepareStatement(GET_TAXONOMY_CONFLICT);
    statement.setLong(1, entityIID);
    return driver.getResultSetParser(statement.executeQuery());
  }
}
