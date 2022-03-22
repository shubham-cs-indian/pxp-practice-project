package com.cs.core.csexpress.coupling;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEObject;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author vallee
 */
public class CSECouplingSource implements ICSECouplingSource {

  private ICSEElement source;
  private Predefined predefinedSource; // when the source is predefined keyword

  /**
   * build a new source from a predefined CSE keyword
   *
   * @param predefinedKeyWord
   */
  public CSECouplingSource(Predefined predefinedKeyWord) {
    source = null;
    predefinedSource = predefinedKeyWord;
  }

  /**
   * Build a new source from a property (implicitly a relationship), a classifier or an entity
   *
   * @param sourceElement
   */
  public CSECouplingSource(ICSEElement sourceElement) {
    source = sourceElement;
    predefinedSource = Predefined.NONE;
  }

  @Override
  public boolean isRelation() {
    return (source != null && source instanceof CSEProperty);
  }

  @Override
  public boolean isEntity() {
    return ((source != null && source instanceof CSEObject
            && ((CSEObject) source).getObjectType() == CSEObjectType.Entity)
            || predefinedSource == Predefined.$entity || predefinedSource == Predefined.$origin
            || predefinedSource == Predefined.$parent || predefinedSource == Predefined.$top);
  }

  @Override
  public boolean isClassifier() {
    return ((source != null && source instanceof CSEObject
            && ((CSEObject) source).getObjectType() == CSEObjectType.Classifier)
            || predefinedSource == Predefined.$nature);
  }

  @Override
  public ICSEProperty toRelation() throws CSFormatException {
    if (!isRelation()) {
      throw new CSFormatException("Not a Relationship Coupling");
    }
    return (ICSEProperty) source;
  }

  @Override
  public ICSEObject toObject() throws CSFormatException {
    if (!(source instanceof CSEObject)) {
      throw new CSFormatException("Not an Object Coupling");
    }
    return (CSEObject) source;
  }

  /**
   * @return the source without exception check
   */
  public ICSEObject getSource() {
    return (CSEObject) source;
  }

  /**
   * @param source overwritten source of coupling
   */
  public void setSource(ICSEObject source) {
    this.source = source;
    this.predefinedSource = Predefined.NONE;
  }

  @Override
  public ICSEObject toEntity() throws CSFormatException {
    if (!isEntity()) {
      throw new CSFormatException("Not an Entity Coupling");
    }
    return (CSEObject) source;
  }

  @Override
  public ICSEObject toClassifier() throws CSFormatException {
    if (!isClassifier()) {
      throw new CSFormatException("Not a Classifier Coupling");
    }
    return (CSEObject) source;
  }

  @Override
  public Predefined toPredefined() {
    return predefinedSource;
  }

  @Override
  public boolean isResolved() {
    return source != null && source.containsSpecification(Keyword.$iid);
  }

  @Override
  public String toString() {
    if (predefinedSource != Predefined.NONE) {
      return predefinedSource.toString();
    }
    return source.toString();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(7, 11).append(source != null ? source.hashCode() : -1)
            .append(predefinedSource)
            .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final CSECouplingSource other = (CSECouplingSource) obj;
    if (this.predefinedSource != other.predefinedSource) {
      return false;
    }
    if (source == null || other.source == null) {
      return false;
    }
    return Objects.equals(this.source, other.source);
  }
}
