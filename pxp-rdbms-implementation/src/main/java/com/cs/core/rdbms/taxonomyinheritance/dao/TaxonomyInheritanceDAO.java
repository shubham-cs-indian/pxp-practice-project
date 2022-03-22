package com.cs.core.rdbms.taxonomyinheritance.dao;

import java.sql.PreparedStatement;
import java.util.List;

import com.cs.core.data.Text;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dto.TaxonomyInheritanceDTO;
import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTO;
import com.cs.core.rdbms.taxonomyinheritance.idao.ITaxonomyInheritanceDAO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class TaxonomyInheritanceDAO implements ITaxonomyInheritanceDAO{

  
  private TaxonomyInheritanceDTO entity;
  
  public TaxonomyInheritanceDAO(ITaxonomyInheritanceDTO taxonomyInheritanceDTO)
  {
    entity = (TaxonomyInheritanceDTO) taxonomyInheritanceDTO;
  }
  
  @Override
  public void upsertTaxonmyConflict() throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      TaxonomyInheritanceDAS taxonomyInheritanceConflictDAS = new TaxonomyInheritanceDAS( connection);
      taxonomyInheritanceConflictDAS.updateTaxonomyInheritanceConflict(entity);
    });
  }


  @Override
  public ITaxonomyInheritanceDTO getTaxonmyConflict() throws RDBMSException
  {
    return entity;
  }

  private static final String DELETE_TAXONOMY_CONFLICTS  = "delete from pxp.baseentitytaxonomyconflictlink where entityiid in (%s)";

  @Override
  public void deleteTaxonmyConflict(List<Long> entityIIDs) throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      
      String format = String.format(DELETE_TAXONOMY_CONFLICTS, Text.join(",", entityIIDs));
      PreparedStatement stmt = connection.prepareStatement(format);
      stmt.execute();
    });
  }
  
  private static final String RESOLVE_TAXONOMY_CONFLICT = " update pxp.baseentitytaxonomyconflictlink set isresolved = true  where entityIID = ?";

  @Override
  public void resolveTaxonmyConflict() throws RDBMSException
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      
      PreparedStatement statement = connection.prepareStatement(RESOLVE_TAXONOMY_CONFLICT);
      statement.setLong(1, entity.getEntityIID());
      statement.execute();
    });
  }
}
