package com.cs.core.rdbms.benchmark;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Performance measurement tests
 * RUN with generic loaded volume data tests
 */
@Ignore("to be run with volume generic test data only")
public class BaseEntityDAOBenchmarkTests extends AbstractRDBMSDriverTests {
  private static final long BENCH_MARK_SIZE = 10;
  private static final int ATTRIBUTES_SIZE = 5;
  private static final int TAGS_SIZE = 5;
  private static final int COUPLED_SIZE = 2;
  
  // Obtained by select * from pxp.baseentity where baseentityid = 'ENT#1' and catalogcode = 'pim'
  private static final long BASEENTITY_IID = 1250644;
  private IBaseEntityDTO baseEntity;
  IBaseEntityDAO baseEntityDao = null;
  List<IPropertyDTO> properties = new ArrayList<>();
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  private void prepareProperties() throws RDBMSException {
    properties.clear();
    properties.add( ConfigTestUtils.getTagsProperty(IStandardConfig.StandardProperty.nameattribute.name()));
    for ( int i = 0; i < ATTRIBUTES_SIZE; i++ )
      properties.add( ConfigTestUtils.getTextProperty( "ATTR#" + (i+1)));
    for ( int i = 0; i < TAGS_SIZE; i++ )
      properties.add( ConfigTestUtils.getTagsProperty("TAG#" + (i+1)));
    for ( int i = 0; i < COUPLED_SIZE; i++ )
      properties.add( ConfigTestUtils.getTextProperty("COUP#" + (i+1)));
  }
  
  private void loadPropertyRecords() throws RDBMSException, CSFormatException
  {
    prepareProperties();
    long totalTime = 0;
    baseEntityDao = DataTestUtils.openBaseEntityDAO( BASEENTITY_IID);
    for ( int i = 0; i < BENCH_MARK_SIZE+1; i++ ) {
      long startTime = System.currentTimeMillis();
      baseEntity = baseEntityDao.loadPropertyRecords( properties.toArray( new IPropertyDTO[0]));
      if ( i == 0 ) {
        printJSON(  "Entity sampling: ", baseEntity);
      } else {
        long executionTime = System.currentTimeMillis() - startTime;
        totalTime += executionTime;
        println( String.format( "Load %d = %d ms", i, executionTime));
      }
    }
    println( String.format( "Average time = %d ms", totalTime / BENCH_MARK_SIZE));
  }
   
  @Test
  public void nominalLoadPropertyRecords() throws RDBMSException, CSFormatException {
    printTestTitle("BENCHMARK: loadPropertyRecords");
    localeCatalogDao.applyLocaleInheritanceSchema( Arrays.asList( new String[] { "en_US"}));
    loadPropertyRecords();
  }
  
  @Test
  public void oneLevelLoadPropertyRecords() throws RDBMSException, CSFormatException {
    printTestTitle("BENCHMARK: oneLevelLoadPropertyRecords with one degree inheritance");
    localeCatalogDao.applyLocaleInheritanceSchema( Arrays.asList( new String[] { "en_CA"}));
    loadPropertyRecords();
  }
  
  @Test
  public void twoLevelsLoadPropertyRecords() throws RDBMSException, CSFormatException {
    printTestTitle("BENCHMARK: twoLevelsLoadPropertyRecords with two degrees inheritance");
    localeCatalogDao.applyLocaleInheritanceSchema( Arrays.asList( new String[] { "fr_CA", "en_CA"}));
    loadPropertyRecords();
  }
  
  
  @Test
  public void createPropertyRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("BENCHMARK: createPropertyRecords");
    localeCatalogDao.applyLocaleInheritanceSchema( Arrays.asList( new String[] { "en_US"}));
    long totalTime = 0;
    baseEntityDao = DataTestUtils.openBaseEntityDAO( BASEENTITY_IID);
    for ( int i = 0; i < BENCH_MARK_SIZE+1; i++ ) {
      if ( i == 0 ) {
        prepareProperties();
        baseEntity = baseEntityDao.loadPropertyRecords( properties.toArray( new IPropertyDTO[0]));
        printJSON(  "Entity sampling: ", baseEntity);
      } else {
        ((BaseEntityDTO)baseEntity).setIID( 0L);
        ((BaseEntityDTO)baseEntity).setBaseEntityID("ENTNEW#" + (new Random()).nextInt(100000));
        ((ValueRecordDTO)baseEntity.getPropertyRecord( 
                IStandardConfig.StandardProperty.nameattribute.getIID())).setValue( baseEntity.getBaseEntityID());
        long startTime = System.currentTimeMillis();
        baseEntityDao.createPropertyRecords( 
                ((BaseEntityDTO)baseEntity).getPropertyRecords().toArray( new IPropertyRecordDTO[0]));
        long executionTime = System.currentTimeMillis() - startTime;
        totalTime += executionTime;
        println( String.format( "Create %d = %d ms", i, executionTime));
      }
    }
    println( String.format( "Average time = %d ms", totalTime / BENCH_MARK_SIZE));
    
   }
  
}
