package com.cs.core.rdbms.config.dao;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.collections4.ListUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConfigurationDAOTests extends AbstractRDBMSDriverTests {
  
  static IConfigurationDAO configurationDAO;
  static long              propertyIID = 0;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
    configurationDAO = driver.newConfigurationDAO();
  }
  
  @Test
  public void createContext() throws RDBMSException, CSFormatException
  {
    printTestTitle("createContext");
    String contextCode = "ColourEV";
    IContextDTO result = configurationDAO.createContext(contextCode, ContextType.EMBEDDED_VARIANT);
    printJSON("create context", result);
    assert (result != null);
    result = configurationDAO.getContextByCode("Country");
    printJSON("get context by code", result);
    assert (result != null);
  }
  
  @Test
  public void createStandardProperty() throws RDBMSException, CSFormatException
  {
    printTestTitle("createStandardProperty");
    int random = (new Random()).nextInt(1100);
    String code = "Standard-property-"+ random;
    IPropertyDTO result = configurationDAO.createStandardProperty(random, code, PropertyType.TEXT);
    printJSON(result);
    assert (result != null);
    result = configurationDAO.getPropertyByCode(code);
    printJSON("[Standard-name] -> ", result);
    assert (result != null);
    result = configurationDAO.getPropertyByIID(random);
    printJSON("[1000l] -> ", result);
    assert (result != null);
  }
  
  @Test
  public void createProperty() throws RDBMSException, CSFormatException
  {
    printTestTitle("createProperty");
    IPropertyDTO result = configurationDAO.createProperty("India-State", PropertyType.TAG);
    propertyIID = result.getPropertyIID();
    printJSON(result);
    assert (result != null);
    result = configurationDAO.getPropertyByCode("ShortDescription");
    printJSON("[ShortDescription] -> ", result);
    assert (result != null);
    result = configurationDAO.getPropertyByIID(2011);
    printJSON("[2011] -> ", result);
    assert (result != null);
  }
  
 
  
  @Test
  public void createTagValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("createTagValue");
    String tagValueID = ConfigTestUtils.newRandomCode("RED");
    ITagValueDTO result = configurationDAO.createTagValue(tagValueID, propertyIID);
    printJSON(result);
    assert (result != null);
  }
  
  @Test
  public void createTagValueV2() throws RDBMSException, CSFormatException
  {
    printTestTitle("createTagValueV2");
    String tagValueID = ConfigTestUtils.newRandomCode("deep-purple");
    ITagValueDTO result = configurationDAO.createTagValue(tagValueID, "Colors");
    printJSON(result);
    assert (result != null);
    assert (result.getPropertyIID() != 0L);
  }
  
  @Test
  public void createClassifier() throws RDBMSException, CSFormatException
  {
    printTestTitle("createClassifier");
    String classifierID = ConfigTestUtils.newRandomCode("Electronics");
    IClassifierDTO result = configurationDAO.createClassifier(classifierID, ClassifierType.CLASS);
    printJSON(result);
    assert (result != null);
    result = configurationDAO.getClassifierByCode("Electronics");
    printJSON("[c>Electronics] -> ", result);
    assert (result != null);
    result = configurationDAO.getClassifierByIID(4001);
    printJSON("[c>4001] -> ", result);
    assert (result != null);
  }
  
  @Test
  public void createStandardClassifier() throws RDBMSException, CSFormatException
  {
    printTestTitle("createCLassifier");
    int random = (new Random()).nextInt(1000);
    String code = "Standard-classifier-"+ random;
    IClassifierDTO result = configurationDAO.createStandardClassifier(random, code, ClassifierType.CLASS);
    printJSON(result);
    assert (result != null);
    result = configurationDAO.getClassifierByCode(code);
    
    printJSON("[Standard-classifier] -> ", result);
    assert (result != null);
    result = configurationDAO.getClassifierByIID(random);
    printJSON("[1100l] -> ", result);
    assert (result != null);
  }
  
  @Test
  public void createRelationship() throws RDBMSException, CSFormatException
  {
    printTestTitle("createRelationship");
    IPropertyDTO result = configurationDAO.createProperty("Product-ImageTest",
        PropertyType.RELATIONSHIP);
    printJSON(result);
    assert (result != null);
  }
  
  @Test
  public void createUser() throws RDBMSException, CSFormatException
  {
    printTestTitle("createUser");
    int random = (new Random()).nextInt(1000);
    IUserDTO newUser = configurationDAO.createUser("Xavier Yantra-" + random);
    printJSON(newUser);
    assert (newUser != null);
  }
  
  @Test
  public void createStandardUser() throws RDBMSException, CSFormatException
  {
    printTestTitle("createUser");
    int random = (new Random()).nextInt(1000);
    IUserDTO newUser = configurationDAO.createStandardUser(random , "Xavier Yantra-" + random);
    printJSON(newUser);
    assert (newUser != null);
  }
  
  @Test
  public void createTask() throws RDBMSException, CSFormatException
  {
    printTestTitle("createUser");
    int random = (new Random()).nextInt(1000);
    ITaskDTO task = configurationDAO.createTask("Xavier Yantra-" + random, TaskType.SHARED);
    assert (task != null);
  }
  
  @Test
  public void upsertRule() throws RDBMSException, CSFormatException
  {
    
    printTestTitle("UPSERT RULE: INSERTION OF RULE AND EXPRESSION");
    String ruleCode = "1234";
    String ruleExpression = "for $basetype = $article $ctlg = pim $org = $stdo $entity is [c>single_article] and $entity under "
        + "[c>tax1] when not [nameattribute] = $null or [descriptionattribute].length < 30 then "
        + "[shortdescriptionattribute] >> $red";
    
    IRuleExpressionDTO upsertRule = configurationDAO.upsertRule(ruleCode, 123l, ruleExpression, RuleType.dataquality );
    
    assert (ListUtils.isEqualList(upsertRule.getCatalogCodes(), Arrays.asList("pim")));
    assert (ListUtils.isEqualList(upsertRule.getWhenPropertyIIDs(), Arrays.asList(200l, 207l)));
    assert (ListUtils.isEqualList(upsertRule.getForPropertyIIDs(), Arrays.asList(208l)));
    assert (upsertRule.getRuleCode().equals(ruleCode));
    
    printTestTitle("UPSERT RULE: INSERTION SUCCESS");
    
    printTestTitle("UPSERT RULE: UPDATE OF RULE EXPRESSION");
    
    String changedRuleExpression = "for $basetype = $article $ctlg = pim  $org = $stdo $entity is [c>single_article] and $entity under "
        + "[c>tax1] when not [nameattribute] = $null and [descriptionattribute].length < 30 and not [addressattribute] = $null "
        + "then [shortdescriptionattribute] >> $red, [discountattribute] >> $red";
    
    IRuleExpressionDTO changedRule = configurationDAO.upsertRule(ruleCode,
        upsertRule.getRuleExpressionIId(), changedRuleExpression, RuleType.dataquality);
    
    assert (ListUtils.isEqualList(changedRule.getCatalogCodes(), Arrays.asList("pim")));
    assert (ListUtils.isEqualList(changedRule.getWhenPropertyIIDs(),
        Arrays.asList(200l, 205l, 207l)));
    assert (ListUtils.isEqualList(changedRule.getForPropertyIIDs(), Arrays.asList(208l, 209l)));
    assert (changedRule.getRuleCode()
        .equals(ruleCode));
    
    printTestTitle("UPSERT RULE: INSERTION SUCCESS");
    
    printTestTitle("GET RULE BY CODE ");
    IConfigRuleDTO ruleByCode = configurationDAO.getRuleByCode(ruleCode);
    assert (ruleByCode.getCode()
        .equals(ruleCode));
    assert (ruleByCode.getType()
        .equals(RuleType.dataquality));
    assert (ruleByCode.getRuleExpressions()
        .contains(changedRule));
  }
  
  @Test
  public void makeCodes() throws RDBMSException
  {
    printTestTitle("makeCodes");
    String test = "\"Frédärik\": d'Alembert ";
    println(test + "-> " + configurationDAO.getCode(IContextDTO.class, test));
    println("Incremented code -> " + configurationDAO.getCode(IPropertyDTO.class, "Accessories"));
  }
}
