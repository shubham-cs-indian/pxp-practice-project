package com.cs.core.rdbms.common;

import static com.cs.core.printer.QuickPrinter.printTestTitle;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.postgresql.util.HStoreConverter;

/**
 * @author vallee
 */
public class RDBMSBatchExecuteTests extends AbstractRDBMSDriverTests {
  
  private static final String Q_INSERT_TAGS = "insert into pxp.tagsrecord values ( ?, ?, ?, ? :: hstore)";
  private static final String Q_UPDATE_TAGS = "update pxp.tagsrecord set usrTags = ? :: hstore where entityIID = ? and propertyIID = ?";
  private final long          entityIID     = 100020;
  private IPropertyDTO        tag1;
  private IPropertyDTO        tag2;
  private IPropertyDTO        tag3;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void testBatchWithArrays() throws RDBMSException
  {
    insertBatchWithArrays();
    updateBatchWithArrays();
  }
  
  public void insertBatchWithArrays() throws RDBMSException
  {
    printTestTitle("insertBatchWithArrays");
    tag1 = ConfigTestUtils.createRandomTagProperty();
    tag2 = ConfigTestUtils.createRandomTagProperty();
    tag3 = ConfigTestUtils.createRandomTagProperty();
    Map<String,Integer> tagValueMap1 =  Map.of("A", 10, "B", 20, "C", -30);
    String tagValueHStore1 = HStoreConverter.toString(tagValueMap1);
    
    Map<String,Integer> tagValueMap2 =  Map.of("D", 40, "E", 50, "F", -60);
    String tagValueHStore2 = HStoreConverter.toString(tagValueMap2);
    
    Map<String,Integer> tagValueMap3 =  Map.of("G", 70, "H", 80, "I", -90);
    String tagValueHStore3 = HStoreConverter.toString(tagValueMap3);

    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          PreparedStatement insert = connection.prepareStatement(Q_INSERT_TAGS);
          insert.setLong(1, tag1.getIID());
          insert.setLong(2, entityIID);
          insert.setInt(3, 1);
          insert.setString(4, tagValueHStore1);
          insert.addBatch();
          insert.setLong(1, tag2.getIID());
          insert.setLong(2, entityIID);
          insert.setInt(3, 1);
          insert.setString(4, tagValueHStore2);
          insert.addBatch();
          insert.setLong(1, tag3.getIID());
          insert.setLong(2, entityIID);
          insert.setInt(3, 1);
          insert.setString(4, tagValueHStore3);
          insert.addBatch();
          int[] results = insert.executeBatch();
          println("batch return: " + Arrays.toString(results));
        });
  }
  
  public void updateBatchWithArrays() throws RDBMSException
  {
    printTestTitle("updateBatchWithArrays");
    Map<String,Integer> tagValueMap1 =  Map.of("AA", 10, "BB", 20, "CC", -30);
    String tagValueHStore1 = HStoreConverter.toString(tagValueMap1);
    
    Map<String,Integer> tagValueMap2 =  Map.of("DD", 40, "EE", 50, "FF", -60);
    String tagValueHStore2 = HStoreConverter.toString(tagValueMap2);
    
    Map<String,Integer> tagValueMap3 =  Map.of("GG", 70, "HH", 80, "II", -90);
    String tagValueHStore3 = HStoreConverter.toString(tagValueMap3);

    RDBMSConnectionManager.instance()
        .runTransaction(( connection) -> {
          PreparedStatement update = connection.prepareStatement(Q_UPDATE_TAGS);
          update.setString(1, tagValueHStore1);
          update.setLong(2, entityIID);
          update.setLong(3, tag1.getIID());
          update.addBatch();
          update.setString(1, tagValueHStore2);
          update.setLong(2, entityIID);
          update.setLong(3, tag2.getIID());
          update.addBatch();
          update.setString(1, tagValueHStore3);
          update.setLong(2, entityIID);
          update.setLong(3, tag3.getIID());
          update.addBatch();
          int[] results = update.executeBatch();
          println("batch return: " + Arrays.toString(results));
        });
  }
  
}
