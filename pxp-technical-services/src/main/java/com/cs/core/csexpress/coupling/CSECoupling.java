package com.cs.core.csexpress.coupling;

import com.cs.core.csexpress.definition.CSEProperty;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource.Predefined;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;
import java.util.Objects;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * @author vallee
 */
public class CSECoupling implements ICSECoupling {

  public static final String DYNAMIC_COUPLING = "&.";
  public static final String TIGHT_COUPLING = "|.";
  public static final String TRANSFER_COUPLING = "=.";
  private final CSECouplingSource source;
  private final CSEProperty property;
  private boolean isDynamicCoupling = false; // default
  private boolean isTightCoupling = false; // default

  /**
   * Build a coupling expression from a source keyword and a property
   *
   * @param sourceKey
   * @param cseProperty
   */
  public CSECoupling(ICSECouplingSource.Predefined sourceKey, CSEProperty cseProperty) {
    source = new CSECouplingSource(sourceKey);
    property = cseProperty;
  }

  /**
   * Build a coupling expression from a source property and a property
   *
   * @param sourceElement
   * @param property
   */
  public CSECoupling(ICSEElement sourceElement, CSEProperty property) {
    isDynamicCoupling = true; // default
    this.source = new CSECouplingSource(sourceElement);
    this.property = property;
  }

  @Override
  public ICSECouplingSource getSource() {
    return source;
  }

  @Override
  public boolean isDynamic() {
    return isDynamicCoupling;
  }

  @Override
  public void setDynamic(boolean couplingStatus) {
    isDynamicCoupling = couplingStatus;
    isTightCoupling = !couplingStatus;
  }

  @Override
  public boolean isTight() {
    return isTightCoupling;
  }

  @Override
  public ICSEProperty getProperty() {
    return property;
  }

  @Override
  public void setTransfer() {
    isDynamicCoupling = false;
    isTightCoupling = false;
  }

  @Override
  public int getPriority() {
    int priority = 0;
    if (isDynamicCoupling) // Dynamic coupling has the highest priority
    {
      priority += 100;
    }
    // Second it is regarded the type of coupling
    if (source.isEntity()) // Inheritance comes in first consideration
    {
      priority += 50;
    } else if (source.isRelation()) // Relationship comes in second
    {
      priority += 40;
    } else if (source.isClassifier()) { // Default value comes in third
      priority += 30; // Within default values, classifiers also come with
      // priority
      if (source.isPredefined()) // nature class has the highest priority
      {
        priority += 5;
      } else {
        ClassifierType sourceType = source.getSource()
                .getSpecification(ClassifierType.class, Keyword.$type);
        switch (sourceType) {
          case CLASS:
            priority += 4;
            break;
          case TAXONOMY:
            priority += 3;
            break;
        }
      }
    } else if (isTransfer()) // Transfer has the lowest priority with cloning over
    // catalog transfer
    {
      priority += source.toPredefined() == Predefined.$origin ? 20 : 10;
    }
    return priority;
  }

  @Override
  public CouplingType getCouplingType() {
    if (getSource().isClassifier()) {
      return isDynamic() ? CouplingType.DYN_CLASSIFICATION : CouplingType.TIGHT_CLASSIFICATION;
    } else if (getSource().isEntity() && !isTransfer()) {
      return isDynamic() ? CouplingType.DYN_INHERITANCE : CouplingType.TIGHT_INHERITANCE;
    } else if (getSource().isRelation()) {
      return isDynamic() ? CouplingType.DYN_RELATIONSHIP : CouplingType.TIGHT_RELATIONSHIP;
    } /*else if (isTransfer()) {
      return getSource().toPredefined() == ICSECouplingSource.Predefined.$origin
              ? CouplingType.CLONE
              : CouplingType.TRANSFER;
    }*/
    return CouplingType.UNDEFINED;
  }

  @Override
  public String toString() {
    StringBuffer cseStr = new StringBuffer(source.toString());
    if (isDynamicCoupling) {
      cseStr.append(DYNAMIC_COUPLING);
    } else if (isTightCoupling) {
      cseStr.append(TIGHT_COUPLING);
    } else {
      cseStr.append(TRANSFER_COUPLING);
    }
    cseStr.append(property.toString());
    return cseStr.toString();
  }

  @Override
  public int compareTo(Object that) {
    // classify by priority in reverse order
    return ((CSECoupling) that).getPriority() - this.getPriority();
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
    final CSECoupling other = (CSECoupling) obj;
    if (this.isDynamicCoupling != other.isDynamicCoupling) {
      return false;
    }
    if (this.isTightCoupling != other.isTightCoupling) {
      return false;
    }
    if (!Objects.equals(this.source, other.source)) {
      return false;
    }
    if (!Objects.equals(this.property, other.property)) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(7, 13).append(isDynamicCoupling)
            .append(isTightCoupling)
            .append(source.hashCode())
            .append(property.hashCode())
            .build();
  }
}
