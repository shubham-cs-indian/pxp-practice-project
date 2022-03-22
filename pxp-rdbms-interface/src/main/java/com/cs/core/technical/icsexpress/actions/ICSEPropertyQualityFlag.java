package com.cs.core.technical.icsexpress.actions;

import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * An action that consists in raising a flag on a property
 *
 * @author vallee
 */
public interface ICSEPropertyQualityFlag extends ICSEAction {

  public enum QualityFlag {
    $red, $orange, $yellow, $green;
    private static final QualityFlag[] values = values();

    public static QualityFlag valueOf(int ordinal) {
      return values[ordinal];
    }
  }

  /**
   * @return the owner entity of the quality rule
   */
  ICSECouplingSource getOwner();

  /**
   * @return the source property to be flagged
   */
  ICSEProperty getProperty();

  /**
   * @return the quality flag to be assigned
   */
  QualityFlag getFlag();

  /**
   * @return the message attached to the rule infringement
   */
  String getMessage();
}
