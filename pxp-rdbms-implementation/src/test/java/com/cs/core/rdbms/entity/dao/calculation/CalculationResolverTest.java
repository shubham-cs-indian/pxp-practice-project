package com.cs.core.rdbms.entity.dao.calculation;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.services.resolvers.CalculationResolver;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vallee
 */
public class CalculationResolverTest extends AbstractRDBMSDriverTests {
  
  private CSEParser parser;
    
  private static void printDependencies(ICSECalculationNode expression) throws CSFormatException
  {
    for (String signature : expression.getRecordNodeIDs()) {
      println("\tdependency-> " + signature);
    }
  }
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
    parser = (new CSEParser());
  }
  
  @Test
  public void resolveTextCalculation() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveTextCalculation");
    ICSECalculationNode expression = parser.parseCalculation(
        "= 'That''s it ' || $parent.[createdbyattribute] || ' / ' || [ShortDescription]");
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          CalculationResolver resolver = new CalculationResolver( connection,localeCatalogDao);
          println("TEXT Result= " + resolver.getResult(100010, expression));
        });
    printDependencies(expression);
  }
  
  @Test
  public void resolveCombinedCalculation() throws RDBMSException, CSFormatException
  {
    printTestTitle("resolveCombinedCalculation");
    ICSECalculationNode mathExpression = parser.parseCalculation(
        "= [Parcel-Length].number * [Parcel-width].number * [Parcel-Height].number / 1000");
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          CalculationResolver resolver = new CalculationResolver( connection, localeCatalogDao);
          println("Math Result= " + resolver.getResult( 100004, mathExpression));
        });
    ICSECalculationNode expression = parser.parseCalculation(
        "= 'Volume ' || [Parcel-Length].number * [Parcel-width].number * [Parcel-Height] / 1000 || ' ' || [Parcel-Length].unit || '3'");
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          CalculationResolver resolver = new CalculationResolver( connection, localeCatalogDao);
          println("COMBINED Result= " + resolver.getResult( 100004, expression));
        });
    printDependencies(expression);
  }
}
