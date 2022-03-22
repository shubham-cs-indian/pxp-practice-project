package com.cs.core.rdbms.process.dto;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idto.IRuleViolationDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;

public class RuleViolationDTO extends SimpleDTO implements IRuleViolationDTO {
  private static final String COLOR = "color";
  private static final String DESCRIPTION = "description";
  private static final String PROPERTY = "property";
  private static final String RULE_CODE = "ruleCode";
  private static final String EXPR_IID = "expressionIID";
  private String       color;
  private String       description;
  private IPropertyDTO property;
  private String       ruleCode;
  private String       ruleExpressionIID;
  
  public RuleViolationDTO()
  {
    super();
  }
  
  public RuleViolationDTO(IResultSetParser resultSet) throws SQLException, RDBMSException
  {
    this.color = QualityFlag.valueOf(resultSet.getInt("qualityflag")).toString().replace("$", "");
    this.description = resultSet.getString("message");
    long propertyIId = resultSet.getLong("propertyiid");
    this.property = ConfigurationDAO.instance().getPropertyByIID(propertyIId);
    this.ruleCode = resultSet.getString("ruleCode");
    this.ruleExpressionIID = resultSet.getString("ruleexpressioniid");
  }
  
  @Override
  public String getColor()
  {
    return color;
  }
  
  @Override
  public String getDescription()
  {
    return description;
  }
  
  @Override
  public IPropertyDTO getPropertyDTO()
  {
    return property;
  }
  
  @Override
  public String getRuleCode()
  {
    return ruleCode;
  }
  
  @Override
  public String getRuleExpressionIID()
  {
    return ruleExpressionIID;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException {
    return JSONBuilder.assembleJSONBuffer(
            JSONBuilder.newJSONField( COLOR, color),
            JSONBuilder.newJSONField( DESCRIPTION, description),
            JSONBuilder.newJSONField( PROPERTY, property.toPXONBuffer()),
            JSONBuilder.newJSONField( RULE_CODE, ruleCode),
            JSONBuilder.newJSONField( EXPR_IID, ruleExpressionIID)
    );
  }

  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException {
    color = json.getString(COLOR);
    description = json.getString(DESCRIPTION);
    property = new PropertyDTO();
    property.fromJSON( json.toString());
    ruleCode = json.getString( RULE_CODE);
    ruleExpressionIID = json.getString( EXPR_IID);
  }
}
