package com.cs.core.rdbms.entity.dto;

import java.sql.SQLException;

import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTO;
import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTOBuilder;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;

/**
 * DTO representation of a Taxonomy Inheritance
 *
 * @author Kushal
 */
public class TaxonomyInheritanceDTO extends RDBMSRootDTO implements ITaxonomyInheritanceDTO {

  private static final long serialVersionUID = 1L;
  
  private long              entityIID;
  private long              sourceEntityIID;
  private long              propertyIID;
  private boolean           isResolved       = false;
  
  /**
   * Enabled default constructor
   */
  public TaxonomyInheritanceDTO()
  {
  }
  
  /**
   * @param entityIID
   * @param sourceEntityIID
   * @param relationshipIID
   */
  public TaxonomyInheritanceDTO(long entityIID, long sourceEntityIID, long propertyIID)
  {
    super();
    this.entityIID = entityIID;
    this.sourceEntityIID = sourceEntityIID;
    this.propertyIID = propertyIID;
  }

  public TaxonomyInheritanceDTO(IResultSetParser parser) throws SQLException
  {
    this.entityIID = parser.getLong("entityIID");
    this.sourceEntityIID = parser.getLong("sourceEntityIID");
    this.propertyIID = parser.getLong("propertyIID");
    this.isResolved = parser.getBoolean("isResolved");
  }
  

  @Override
  public long getEntityIID()
  {
    return entityIID;
  }

  @Override
  public long getSourceEntityIID()
  {
    return sourceEntityIID;
  }

  @Override
  public long getPropertyIID()
  {
    return propertyIID;
  }

  @Override
  public Boolean getIsResolved()
  {
    return isResolved;
  }

  @Override
  public void setIsResolved(Boolean isResolved)
  {
    this.isResolved = isResolved;
  }

  @Override
  public ICSEElement toCSExpressID() throws CSFormatException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    // TODO Auto-generated method stub
    
  }
 
  /**
   * implementation of TaxonomyInheritanceDTOBuilder
   * @author Kushal
   *
   */
 public static class TaxonomyInheritanceDTOBuilder implements ITaxonomyInheritanceDTOBuilder {

  private final TaxonomyInheritanceDTO taxonomyInheritanceDTO;

  /**
   * 
   * @param entityIID
   * @param sourceEntityIID
   * @param relationshipIID
   */
  public TaxonomyInheritanceDTOBuilder(long entityIID, long sourceEntityIID, long propertyIID)
  {
    taxonomyInheritanceDTO = new TaxonomyInheritanceDTO(entityIID, sourceEntityIID, propertyIID);
  }

  
  public ITaxonomyInheritanceDTOBuilder isResolved(Boolean isResolved)
  {
    taxonomyInheritanceDTO.setIsResolved(isResolved);
    return this;
  }

  @Override
  public ITaxonomyInheritanceDTO build()
  {
    return taxonomyInheritanceDTO;
  }
 }    
}
