package com.cs.core.rdbms.config.idto;

/**
 * A representation of a context information (synchronized from configuration DB)
 *
 * @author vallee
 */
public interface IContextDTO extends IRootConfigDTO {

  public enum ContextType {

    UNDEFINED, ATTRIBUTE_CONTEXT, RELATIONSHIP_VARIANT, LINKED_VARIANT, EMBEDDED_VARIANT,
    GTIN_VARIANT, PID_VARIANT, IMAGE_VARIANT, PROMOTION_CONTEXT;

    private static final ContextType[] values = values();

    public static ContextType valueOf(int ordinal) {
      return values[ordinal];
    }
  };

  /**
   * @return the context code
   */
  public default String getContextCode() {
    return getCode();
  }

  /**
   * @return the type of attached context
   */
  public ContextType getContextType();
}
