package com.cs.core.rdbms.process.idto;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IRuleViolationDTO extends ISimpleDTO {

  /**
   * @return color of the violation
   */
  public String getColor();

  /**
   * @return Description provided in configuration for this particular Violation.
   */
  public String getDescription();

  /**
   * @return Property on which Violation happens to be triggered.
   */
  public IPropertyDTO getPropertyDTO();

  /**
   * @return Code of the violated Rule.
   */
  public String getRuleCode();

  /**
   * @return rule expressionIID of rule
   */
  public String getRuleExpressionIID();
}
