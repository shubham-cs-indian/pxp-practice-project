package com.cs.core.technical.icsexpress.scope;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.ijosn.IJSONContent;

import java.util.Collection;

/**
 * a filtering condition included into a scope expression
 *
 * @author vallee
 */
public interface ICSEEntityFilterNode {

  public enum FilterNodeType {
    compound, byClassifier, byProperty, byContext, byQuality, byRelationship, byCollection, byExpiry, byDuplicate, byTranslation;
  }

  /**
   * @return true if NOT is applied on the filtering notation
   */
  public boolean hasNot();

  /**
   * @param not overwritten not flag
   */
  void setNot(boolean not);

  /**
   * Fetch recursively all simple filters involved in this operation
   *
   * @return the record identities
   */
  public Collection<ICSEEntityFilterNode> getSimpleFilters();

  /**
   * @return the codes of all including classifiers
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Collection<String> getIncludingClassifiers() throws CSFormatException;

  /**
   * @return the codes of all excluded classifiers
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Collection<String> getExcludedClassifiers() throws CSFormatException;

  /**
   * @return the codes of all contained propertyIIDs
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public Collection<String> getContainedProperties() throws CSFormatException;

  /**
   * @return the type of filter
   */
  FilterNodeType getType();

  /**
   * @return true if this operand doesn't require more parsing
   */
  public default boolean isTerminal() {
    return (getType() == FilterNodeType.byClassifier || getType() == FilterNodeType.byProperty);
  }

  /**
   * @return the JSON compilation result of entity filter node
   * @throws com.cs.core.technical.exception.CSFormatException
   */
  public IJSONContent toJSON() throws CSFormatException;

}
