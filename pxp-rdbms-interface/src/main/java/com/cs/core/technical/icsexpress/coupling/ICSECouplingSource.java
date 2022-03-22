package com.cs.core.technical.icsexpress.coupling;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * Defines the source of coupling
 *
 * @author vallee
 */
public interface ICSECouplingSource {

  /**
   * @return true when the source is a property (expected to be a relationship)
   */
  public boolean isRelation();

  /**
   * @return true when the source is an entity (including $entity, $parent, $top, $origin)
   */
  public boolean isEntity();

  /**
   * @return true when the source is a classifier (including $nature)
   */
  public boolean isClassifier();

  /**
   * @return true if this source is a predefined keyword
   */
  public default boolean isPredefined() {
    return (toPredefined() != Predefined.NONE);
  }

  /**
   * @return the predefined keyword when applicable or NONE
   */
  public Predefined toPredefined();

  /**
   * @return the property definition of this coupling source
   * @throws CSFormatException when inappropriate
   */
  public ICSEProperty toRelation() throws CSFormatException;

  /**
   * @return the object definition of this coupling source or null if predefined
   * @throws CSFormatException when inappropriate
   */
  public ICSEObject toObject() throws CSFormatException;

  /**
   * @return the entity definition of this coupling source or null if predefined
   * @throws CSFormatException when inappropriate
   */
  public ICSEObject toEntity() throws CSFormatException;

  /**
   * @return the classifier definition of this coupling source or null if predefined
   * @throws CSFormatException when inappropriate
   */
  public ICSEObject toClassifier() throws CSFormatException;

  /**
   * @return true when the coupling source has been resolved
   */
  public boolean isResolved();

  public enum Predefined {
    NONE, $entity, $parent, $top, $origin, $source, $nature, $empty, $classification;
  }

}
