package com.cs.core.rdbms.config.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

import java.util.List;

/**
 * This represents a realm for data API which consists of a locale definition and a catalog
 *
 * @author vallee
 */
public interface ILocaleCatalogDTO extends ICatalogDTO, IRootDTO {

  /**
   * @return the locale inheritance schema attached to this catalog
   */
  public List<String> getLocaleInheritanceSchema();

  /**
   * Define a locale inheritance schema by a list of ordered locale IDs
   *
   * @param inheritanceSchema ordered left to right by order of application, from leaf to root
   */
  public void setLocaleInheritanceSchema(List<String> inheritanceSchema);

  /**
   * @return the locale ID
   */
  public String getLocaleID();

  /**
   * @param localeID overwritten locale ID
   */
  public void setLocaleID(String localeID);
}
