package com.cs.core.rdbms.config.dto;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.LocaleID;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;

/**
 * @author vallee
 */
public class LocaleCatalogDTO extends CatalogDTO implements ILocaleCatalogDTO {
  
  private List<String> localeInheritances = new ArrayList<>();
  private LocaleID     localeID           = new LocaleID();
  
  /**
   * Enabled default constructor
   */
  public LocaleCatalogDTO()
  {
  }
  
  /**
   * Value constructor (using embedded objects)
   *
   * @param localeID
   * @param catalogCode
   * @param organizationCode
   */
  public LocaleCatalogDTO(String localeID, String catalogCode, String organizationCode)
  {
    super(catalogCode, organizationCode);
    this.localeID = new LocaleID(localeID);
    if (!localeID.isEmpty())
      this.localeInheritances.add(localeID);
  }
  
  /**
   * Value constructor (using embedded objects)
   *
   * @param localeID
   * @param catalog
   */
  public LocaleCatalogDTO(String localeID, CatalogDTO catalog)
  {
    super(catalog);
    this.localeID = new LocaleID(localeID);
    if ( !localeID.isEmpty() )
      this.localeInheritances.add(localeID);
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public LocaleCatalogDTO(LocaleCatalogDTO source)
  {
    super(source);
    localeID = new LocaleID(source.localeID.toString());
    this.localeInheritances.addAll(source.getLocaleInheritanceSchema());
  }
  
  /**
   * Copy constructor with different inheritance schema
   *
   * @param source
   * @param localeInheritanceSchema
   */
  public LocaleCatalogDTO(LocaleCatalogDTO source, List<String> localeInheritanceSchema)
  {
    super(source);
    localeID = new LocaleID(source.localeID.toString());
    setLocaleInheritanceSchema(localeInheritanceSchema);
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = (CSEObject) super.toCSExpressID();
    cse.setSpecification(Keyword.$locale, localeID.toString());
    return cse;
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject gcse = (CSEObject) cse;
    super.fromCSExpressID(gcse);
    localeID = new LocaleID(gcse.getSpecification(Keyword.$locale));
  }
  
  @Override
  public String getLocaleID()
  {
    return localeID.toString();
  }
  
  @Override
  public void setLocaleID(String localeID)
  {
    this.localeID = new LocaleID(localeID);
  }
  
  @Override
  public List<String> getLocaleInheritanceSchema()
  {
    return localeInheritances;
  }
  
  @Override
  public void setLocaleInheritanceSchema(List<String> inheritanceSchema)
  {
    localeInheritances.clear();
    localeInheritances.addAll(inheritanceSchema);
    if (localeID.isEmpty() && inheritanceSchema.size() > 0) {
      localeID = new LocaleID(inheritanceSchema.get(0));
    }
    else if ( !localeID.isEmpty() ) {
      int index = localeInheritances.indexOf(localeID.toString());
      if (index < 0) { // if the catalog locale ID is not found then add it at last position
        localeInheritances.add( localeID.toString());
      }
      else if (index > 0) { // then truncate the schema until the current locale id is found
        localeInheritances = localeInheritances.subList( 0, index);
      }
    }
  }
  
  @Override
  public boolean equals(Object other)
  {
    if (!super.equals(other)) {
      return false;
    }
    LocaleCatalogDTO otherCatalog = (LocaleCatalogDTO) other;
    return new EqualsBuilder().append(this.localeID, otherCatalog.localeID).isEquals();
  }
  
  @Override
  public int compareTo(Object other)
  {
    int catalogComparison = super.compareTo(other);
    if (catalogComparison != 0) {
      return catalogComparison;
    }
    LocaleCatalogDTO otherCatalog = (LocaleCatalogDTO) other;
    return new CompareToBuilder().append(this.localeID, otherCatalog.localeID).toComparison();
  }
}
