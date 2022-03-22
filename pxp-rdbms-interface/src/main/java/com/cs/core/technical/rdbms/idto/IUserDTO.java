package com.cs.core.technical.rdbms.idto;

/**
 * DTO representation of PXP users - id is the user ID (internal DB config) - name is the user name
 *
 * @author vallee
 */
public interface IUserDTO extends IRootDTO {

  /**
   * @return the user IID
   */
  public long getUserIID();

  /**
   * @return the name of the catalog or DI
   */
  public String getUserName();
}
