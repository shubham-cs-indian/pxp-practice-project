package com.cs.core.rdbms.config.dto;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * Configuration Rule Data Transfer Object
 *
 * @author Niraj.Dighe
 */
public class ConfigRuleDTO extends RootConfigDTO implements IConfigRuleDTO {
  
  private static final String     RULE_PREFIX     = "rule";
  
  private Set<IRuleExpressionDTO> ruleExpressions = new TreeSet<IRuleExpressionDTO>();
  
  private ICSERule.RuleType       type;
  private long                    lastRefreshed;
  
  /**
   * Value constructor
   *
   * @param catalogCode
   * @param type
   */
  public ConfigRuleDTO(String configRuleCode, ICSERule.RuleType type)
  {
    super(configRuleCode);
    this.type = type;
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public ConfigRuleDTO(IConfigRuleDTO source)
  {
    super(source);
    this.type = source.getType();
  }
  
  /**
   * Constructor from a result set
   *
   * @param parser
   * @throws java.sql.SQLException
   * @throws CSFormatException
   * @throws RDBMSException
   */
  public ConfigRuleDTO(IResultSetParser parser)
      throws SQLException, CSFormatException, RDBMSException
  {
    super(parser, RULE_PREFIX);
    this.type = ICSERule.RuleType.values()[parser.getInt("ruleType")];
    this.lastRefreshed = parser.getLong("lastevaluatedbybgp");
  }
  
  @Override
  public ICSEElement toCSExpressID()
  {
    CSEObject cse = new CSEObject(CSEObjectType.Rule);
    return initCSExpression(cse, type.toString());
  }
  
  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException
  {
    CSEObject gcse = (CSEObject) cse;
    fromCSExpression(gcse);
    this.type = gcse.getSpecification(ICSERule.RuleType.class, ICSEElement.Keyword.$type);
  }
  
  @Override
  public RuleType getType()
  {
    return type;
  }
  
  @Override
  public long getLastRefreshed()
  {
    return lastRefreshed;
  }
  
  @Override
  public Collection<IRuleExpressionDTO> getRuleExpressions()
  {
    return ruleExpressions;
  }
  
  @Override
  public void setRuleExpressions(Collection<IRuleExpressionDTO> ruleExpressions)
  {
    this.ruleExpressions.clear();
    this.ruleExpressions.addAll(ruleExpressions);
  }
}
