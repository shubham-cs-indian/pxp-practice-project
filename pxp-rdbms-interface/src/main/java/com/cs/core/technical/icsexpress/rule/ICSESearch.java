package com.cs.core.technical.icsexpress.rule;

import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import com.cs.core.technical.ijosn.IJSONContent;

/**
 * @author vallee
 */
public interface ICSESearch {

  /**
   * @return the scope that determines what entities are concerned by the search
   */
  public ICSEScope getScope();

  /**
   * @return the logical calculation that determines the conditions for being returned
   */
  public ICSECalculationNode getEvaluation();

  /**
   * @return the JSON structure of the search
   * @throws CSFormatException
   */
  public IJSONContent toJSON() throws CSFormatException;

}
