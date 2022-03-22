package com.cs.core.rdbms.config.idto;

import java.util.List;

/**
 * DTO representation of PXP classifier = taxonomy or class (synchronized from configuration DB)
 *
 * @author vallee
 */
public interface IClassifierDTO extends IRootConfigDTO {

  /**
   * @return the property RDBMS IID
   */
  public default long getIID() {
    return getClassifierIID();
  }

  ;

  /**
   * @return the classifier RDBMS IID
   */
  public long getClassifierIID();

  /**
   * @return the Code of the classifier
   */
  public default String getClassifierCode() {
    return getCode();
  }

  /**
   * @return the classifier ClassifierType
   */
  public ClassifierType getClassifierType();

  /**
   *
   * @param hierarchyIIDs all the parentIIDs and selfIID List
   */
  public void setHierarchyIIDs(List<Long> hierarchyIIDs);

  /**
   *
   * @returnh all the parentIIDs and selfIID List
   */
  public List<Long> getHierarchyIIDs();

  public enum ClassifierType {

    UNDEFINED, CLASS, TAXONOMY, MINOR_TAXONOMY, HIERARCHY;

    private static final ClassifierType[] values = values();

    public static ClassifierType valueOf(int ordinal) {
      return values[ordinal];
    }
  }
}
