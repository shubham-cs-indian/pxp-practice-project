package com.cs.core.rdbms.config.idto;

/**
 * DTO representation of PXP catalog or DI (synchronized from configuration DB)
 *
 * @author vallee
 */
public interface ICatalogDTO extends IRootConfigDTO {

  /**
   * @return the code of the catalog or DI
   */
  public default String getCatalogCode() {
    return getCode();
  }

  /**
   * @return the code of the organization
   */
  public  String getOrganizationCode();

}
