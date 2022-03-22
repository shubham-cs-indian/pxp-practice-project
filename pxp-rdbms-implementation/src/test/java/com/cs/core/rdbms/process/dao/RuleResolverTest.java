package com.cs.core.rdbms.process.dao;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.services.resolvers.RuleResolver;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import com.cs.core.technical.icsexpress.calculation.ICSELiteralOperand;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author niraj
 */
public class RuleResolverTest extends AbstractRDBMSDriverTests {
  
  private final CSEParser parser;
  public RuleResolverTest()
  {
    parser = (new CSEParser());
  }
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void testDataQualityRule() throws RDBMSException, CSFormatException
  {
    printTestTitle("Test Evaluation For Rule: VIOLATION");

    IConfigRuleDTO ruleByCode = ConfigurationDAO.instance().getRuleByCode("rule_11");
    Collection<IRuleExpressionDTO> ruleExpressions = ruleByCode.getRuleExpressions();
    IRuleExpressionDTO ruleExpression = ruleExpressions.iterator().next();
    String csexpression = ruleExpression.getRuleExpression();
 
    QualityFlag[] violation = { QualityFlag.$green };
    ICSERule rule = parser.parseRule(csexpression);
    RDBMSConnectionManager.instance().runTransaction((connection) -> {
      RuleResolver resolver = new RuleResolver( connection, localeCatalogDao,
              ruleExpression.getCatalogCodes(), ruleExpression.getLocaleIDs(), ruleExpression.getOrganizationCodes(), new StringBuilder(), new ArrayList<>());
      RuleCatalogDAS ruleCatalogDAS = new RuleCatalogDAS( connection);

      ICSELiteralOperand result = resolver.getResult(100003, rule.getEvaluation());

      println("Test Result= " + result.asBoolean());
      resolver.evaluateRuleEffects(rule.getActionList(), ruleExpression.getRuleExpressionIId(), 100003);
      violation[0] = ruleCatalogDAS.getViolation(ruleExpression.getRuleExpressionIId(),
              localeCatalogDao.getLocaleCatalogDTO().getLocaleID(), 100003l, 2008l);
    });
      assertTrue(violation[0].equals(QualityFlag.$yellow));
  }
  
  @Test
  public void testNormalizationDataQualityRule() throws RDBMSException, CSFormatException
  {
    printTestTitle("Test Evaluation For Rule");

    IConfigRuleDTO ruleByCode = ConfigurationDAO.instance().getRuleByCode("rule_12");
    Collection<IRuleExpressionDTO> ruleExpressions = ruleByCode.getRuleExpressions();
    IRuleExpressionDTO ruleExpression = ruleExpressions.iterator().next();
    String csexpression = ruleExpression.getRuleExpression();
    ICSERule rule = parser.parseRule(csexpression);

    RDBMSConnectionManager.instance()
    .runTransaction((connection) -> {
      RuleResolver resolver = new RuleResolver(connection, localeCatalogDao, ruleExpression.getClassifierCodes(), ruleExpression.getLocaleIDs(), ruleExpression.getOrganizationCodes(), new StringBuilder(), new ArrayList<>());
      
      ICSELiteralOperand result = resolver.getResult( 100003, rule.getEvaluation());
      println("Test Result= " + result);
       Map<IRootConfigDTO, ICSELiteralOperand> applyRuleEffects = resolver.evaluateRuleEffects(rule.getActionList(), ruleExpression.getRuleExpressionIId(), 100003);
       
       IPropertyDTO marketPrice = ConfigurationDAO.instance().getPropertyByCode("ShortDescription");
       IPropertyDTO shortDescription = ConfigurationDAO.instance().getPropertyByCode("shortdescriptionattribute");
      
          assertTrue(applyRuleEffects.containsKey(marketPrice));
          assertTrue(applyRuleEffects.containsKey(shortDescription));
          
          List<String> collect = applyRuleEffects.values().stream().map(x -> x.asString()).collect(Collectors.toList());
          assertTrue(collect.contains("SHORT DESCRIPTION OF SCREEN DELL W530XP"));
          assertTrue(collect.contains("This is short description"));
        });
  }
  
  @Test
  public void testKPI() throws RDBMSException, CSFormatException
  {
    printTestTitle("Test Evaluation For KPI");

    IConfigRuleDTO ruleByCode = ConfigurationDAO.instance().getRuleByCode("rule_14");
    Collection<IRuleExpressionDTO> ruleExpressions = ruleByCode.getRuleExpressions();
    IRuleExpressionDTO ruleExpression = ruleExpressions.iterator().next();
    String csexpression = ruleExpression.getRuleExpression();
    ICSERule rule = parser.parseRule(csexpression);
    
    RDBMSConnectionManager.instance()
        .runTransaction((connection) -> {
          RuleResolver resolver = new RuleResolver(connection, localeCatalogDao,
              ruleExpression.getCatalogCodes(), ruleExpression.getLocaleIDs(), ruleExpression.getOrganizationCodes(), new StringBuilder(), new ArrayList<>());
          
          ICSELiteralOperand result = resolver.getResult( 100003, rule.getEvaluation());
          assert(result.asBoolean() == false);
          println("Test Result= " + result);
        });
  }
}