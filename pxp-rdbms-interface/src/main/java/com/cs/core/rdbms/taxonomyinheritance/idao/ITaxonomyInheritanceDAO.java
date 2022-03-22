package com.cs.core.rdbms.taxonomyinheritance.idao;

import java.util.List;

import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public interface ITaxonomyInheritanceDAO {
  
 
  /**
   * update/insert Taxonomy Conflict 
   * @throws RDBMSException
   */
  public void upsertTaxonmyConflict() throws RDBMSException;

  /**
   * @return an instance of TaxonomyInheritanceDTO present
   * @throws RDBMSException
   */
  public ITaxonomyInheritanceDTO getTaxonmyConflict() throws RDBMSException;

  /**
   * @param entityIID
   * @throws RDBMSException
   */
  public void deleteTaxonmyConflict(List<Long> entityIID) throws RDBMSException;
  
  /**
   * @param entityIID
   * @throws RDBMSException
   */
  public void resolveTaxonmyConflict() throws RDBMSException;

}
