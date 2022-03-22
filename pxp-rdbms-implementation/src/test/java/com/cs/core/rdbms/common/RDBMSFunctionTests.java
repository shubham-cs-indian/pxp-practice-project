package com.cs.core.rdbms.common;

import java.sql.PreparedStatement;

import org.junit.Before;
import org.junit.Test;

import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * RDBMSFunctionTests
 *
 * @author vallee
 */
// @Ignore
public class RDBMSFunctionTests extends AbstractRDBMSDriverTests {
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void createClassifier() throws RDBMSException
  {
    printTestTitle("createClassifier");
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          IResultSetParser result = connection.getFunction( ResultType.IID, "pxp.fn_classifierConfig")
              .setInput(ParameterType.STRING, "PlasticFurnitutes")
              .setInput(ParameterType.INT, IClassifierDTO.ClassifierType.TAXONOMY.ordinal())
              .execute();
          println("New classifier: " + result.getIID());
        });
  }
  
  @Test
  public void readHSTore() throws RDBMSException
  {
    printTestTitle("readHstore format");
    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          PreparedStatement s = connection.prepareStatement("select * from pxp.contextualObject");
          IResultSetParser p = connection.getResultSetParser(s.executeQuery());
          while (p.next()) {
            long iid = p.getLong("contextualObjectIID");
            String cxtTags = p.getString("cxtTags");
            printf("contextualObjectIID = %d, cxtTags =  %s)\n", iid,
                cxtTags);
          }
        });
  }
}  
