package com.cs.core.rdbms.process.idto;

import com.cs.core.technical.rdbms.idto.ISimpleDTO;

public interface IKPIResultDTO extends ISimpleDTO {

  /**
   *
   * @return Result of the given KPI.
   */
  public Double getKpiResult();

  /**
   *
   * @return rule code of the given KPI.
   */
  public String getRuleCode();
}
