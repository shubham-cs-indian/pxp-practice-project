package com.cs.core.rdbms.process.dao;

import com.cs.core.rdbms.process.idto.IKPIResultDTO;
import com.cs.core.rdbms.process.idto.IRuleViolationDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;


public class RuleCatalogDAOTests extends AbstractRDBMSDriverTests {
  
  private RuleCatalogDAO ruleDAO = null;

  @Before
  public void init() throws RDBMSException {
    super.init();
    ruleDAO = new RuleCatalogDAO(localeCatalogDao);
    refreshDataQualityResult();
  }
  
  public void refreshDataQualityResult() throws RDBMSException
  {
    ruleDAO.refreshDataQualityResult("rule_13", 100003, false);
    ruleDAO.refreshDataQualityResult("rule_12", 100003, false);
    ruleDAO.refreshDataQualityResult("rule_11", 100003, false);
    ruleDAO.evaluateProductIdentifier(2007, 100003, 4000);
    assert(true);
  }
  
  @Test
  public void loadKPI() throws RDBMSException, CSFormatException
  {
    printTestTitle("loadKPI");
    List<IKPIResultDTO> loadKPI = ruleDAO.loadKPI(100003l, "en_US");
    for( IKPIResultDTO kpi : loadKPI ) 
      printJSON("\t->kpi", kpi);
    assert(loadKPI.get(0).getKpiResult().equals(0d));
  }
  
  @Test
  public void loadViolations() throws RDBMSException, CSFormatException
  {
    printTestTitle("loadViolations");
    Set<IRuleViolationDTO> loadViolations = ruleDAO.loadViolations(100003l);
    for (IRuleViolationDTO violation : loadViolations)
      printJSON("\t->violation", violation);
    assert (loadViolations.stream().filter(x -> x.getColor().equals("yellow")).count() > 0);
  }
  
}
