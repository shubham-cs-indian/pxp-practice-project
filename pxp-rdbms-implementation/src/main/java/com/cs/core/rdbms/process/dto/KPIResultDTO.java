package com.cs.core.rdbms.process.dto;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idto.IKPIResultDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;

public class KPIResultDTO extends SimpleDTO implements IKPIResultDTO {
  
  private static final String KPI_RESULT = "kpi";
  private static final String RULE_CODE = "ruleCode";
  private Double       kpiResult = .0;
  private String       ruleCode;
  
  public KPIResultDTO()
  {
    super();
  }
  
  public KPIResultDTO(IResultSetParser resultSet) throws SQLException, RDBMSException
  {
    this.ruleCode = resultSet.getString("ruleCode");
    this.kpiResult = resultSet.getDouble("kpi");
  }
  
  @Override
  public Double getKpiResult()
  {
    return kpiResult;
  }


  @Override
  public String getRuleCode()
  {
    return ruleCode;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    return JSONBuilder.assembleJSONBuffer(
            JSONBuilder.newJSONField( RULE_CODE, ruleCode, true),
            JSONBuilder.newJSONField( KPI_RESULT, kpiResult)
    );
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException {
    kpiResult = json.getDouble(KPI_RESULT);
    ruleCode = json.getString(RULE_CODE);
  }
}
