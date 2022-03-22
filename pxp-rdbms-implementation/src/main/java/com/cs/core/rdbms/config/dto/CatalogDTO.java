package com.cs.core.rdbms.config.dto;

import java.io.Serializable;
import java.sql.SQLException;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;

/**
 * Catalog Data Transfer Object
 *
 * @author PankajGajjar
 */
public class CatalogDTO extends RootConfigDTO implements ICatalogDTO, Serializable {

  private static final long       serialVersionUID = 1L;
  private String                  organizationCode = IStandardConfig.STANDARD_ORGANIZATION_RCODE;
  
  /**
   * Enabled default constructor
   */
  public CatalogDTO()
  {
  }
  
  /**
   * Value constructor
   *
   * @param catalogCode
   */
  public CatalogDTO(String catalogCode, String organizationCode)
  {
    super(catalogCode);
    this.organizationCode = IStandardConfig.isStandardOrganization(organizationCode) ?
        IStandardConfig.STANDARD_ORGANIZATION_RCODE : organizationCode;
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public CatalogDTO(ICatalogDTO source)
  {
    super(source);
    String organizationCode = source.getOrganizationCode();
    this.organizationCode = IStandardConfig.isStandardOrganization(organizationCode) ?
        IStandardConfig.STANDARD_ORGANIZATION_RCODE : organizationCode;
  }
  
  /**
   * Constructor from a result set
   *
   * @param parser
   * @throws java.sql.SQLException
   */
  public CatalogDTO(IResultSetParser parser) throws SQLException
  {
    super(parser.getString("catalogCode"));
    this.organizationCode = parser.getString("organizationCode");
  }
  
  public static String getCacheCode(String code)
  {
    return String.format("%c:%s", CSEObject.CSEObjectType.Catalog.letter(), code);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(CSEObjectType.Catalog);
    cse.setSpecification(Keyword.$org, organizationCode);
    return initCSExpression(cse, "");
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject gcse = (CSEObject) cse;
    fromCSExpression(gcse);
    this.organizationCode = gcse.getSpecification(Keyword.$org);
  }
  
  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }
}
