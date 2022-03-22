package com.cs.core.rdbms.config.idto;

/**
 * DTO representation of PXP tag value (synchronized from configuration DB)
 *
 * @author Farooq Kadri
 */
public interface ITagValueDTO extends IRootConfigDTO {

  /**
   * @return the tag value code
   */
  public default String getTagValueCode() {
    return getCode();
  }

  /**
   * @return the property IID attached to this tag value (i.e. the Tag List of Value)
   */
  public long getPropertyIID();
}
