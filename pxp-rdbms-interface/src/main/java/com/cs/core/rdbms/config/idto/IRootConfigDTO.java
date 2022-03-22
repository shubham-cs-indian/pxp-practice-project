package com.cs.core.rdbms.config.idto;

import com.cs.core.technical.rdbms.idto.IRootDTO;

/**
 * DTO representation common to all configuration DTO all share the same generic structure - IID (RDBMS internal identified) - Code (name
 * defined by the user for readability) - ID (ODB identifier) (synchronized from configuration DB)
 *
 * @author vallee
 */
public interface IRootConfigDTO extends IRootDTO, Comparable {

  /**
   * @return the readable code of the configuration object
   */
  public String getCode();

  @Override
  public default boolean isNull() {
    return getCode().isEmpty();
  }

  /**
   * Type of configuration items
   */
  public enum ItemType {

    UNDEFINED, LOCALE, USER, PARTNER, CONTEXT, CLASSIFIER, HIERARCHY_TAXONOMY, MASTER_TAXONOMY,
    PROPERTY, PROPERTY_COLLECTION, TASK, EVENT, RULE, GOLDEN_RECORD_RULE, TRANSLATION, ORGANIZATION,
    LANGUAGE, TAB, PERMISSION;
	
    private static final ItemType[] values = values();

    public static ItemType valueOf(int ordinal) {
      return values[ordinal];
    }
  }
}
