package com.cs.core.technical.icsexpress.actions;

import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.coupling.ICSECouplingSource;
import com.cs.core.technical.icsexpress.definition.ICSEProperty;

/**
 * An action that consists in assigning a value or the result of a calculation to a property
 *
 * @author vallee
 */
public interface ICSEPropertyAssignment extends ICSEAction {

  /**
   * @return the owner entity of the assignment
   */
  ICSECouplingSource getOwner();

  /**
   * @return the source property to be assigned
   */
  ICSEProperty getProperty();

  /**
   * @return the value of assignment as the parsed result of a calculation expression
   */
  ICSECalculationNode getValue();
}
