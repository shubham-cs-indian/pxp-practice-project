package com.cs.core.rdbms.entity.dao.calculation;

import java.util.Arrays;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author vallee
 */
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BaseEntityDAOCalculatedRecordsTest extends AbstractRDBMSDriverTests {
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  // @Ignore
  @Test
  public void createUndefinedCalculatedRecord()
      throws RDBMSException, CSFormatException, InterruptedException
  {
    printTestTitle("createUndefinedCalculatedRecord");
    IPropertyDTO sourceProp = ConfigTestUtils.createRandomTextProperty();
    IPropertyDTO calcProp = ConfigTestUtils.createRandomCalculatedProperty();
    String calcExpression = String.format("= 'New Content ' || [%s]", sourceProp.getCode());
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100004);
    IValueRecordDTO calcRecord = entityDAO.newValueRecordDTOBuilder(calcProp, "")
        .localeID(localeCatalogDto.getLocaleID())
        .build();
    
    calcRecord.addCalculation(calcExpression);
    entityDAO.createPropertyRecords(calcRecord);
    printJSON(entityDAO.getBaseEntityDTO());
    assert calcRecord.getValue().equals("New Content");
    
    IValueRecordDTO srcRecord = entityDAO.newValueRecordDTOBuilder(sourceProp, "JUST-CREATED")
        .localeID(localeCatalogDto.getLocaleID())
        .build();
    entityDAO.createPropertyRecords(srcRecord);
    printJSON("Source Record created:", entityDAO.getBaseEntityDTO());
    Thread.sleep(350); // Wait to allow the event handler running
    entityDAO.loadPropertyRecords(calcProp);
    printJSON("After propagation:", entityDAO.getBaseEntityDTO());
    String calcValue = ((IValueRecordDTO) entityDAO.getBaseEntityDTO().getPropertyRecord(calcProp.getIID())).getValue();
    assert calcValue.endsWith("JUST-CREATED");
  }
  
  @Test
  public void createMathCalculatedRecord() throws RDBMSException, CSFormatException
  {
    printTestTitle("createMathCalculatedRecord");
    String mathExpression = "= 'Volume ' || [Parcel-Length].number * [Parcel-width].number * [Parcel-Height] / 1000 "
        + "|| ' ' || [Parcel-Length].unit || '3'";
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100004);
    IPropertyDTO shortDesc = localeCatalogDao.newPropertyDTO(2008, "ShortDescription",
        PropertyType.CALCULATED);
    IValueRecordDTO calcRecord = entityDAO.newValueRecordDTOBuilder(shortDesc, "")
        .localeID(localeCatalogDto.getLocaleID())
        .build();
    calcRecord.addCalculation(mathExpression);
    entityDAO.createPropertyRecords(calcRecord);
    printJSON(entityDAO.getBaseEntityDTO());
    assert !calcRecord.getValue()
        .isEmpty();
  }
  
  private void testSourceOfCalculation(boolean useEventQueue, long sleepDelay)
      throws RDBMSException, InterruptedException, CSFormatException
  {
    println("\t-> testSourceOfCalculation");
    IPropertyDTO parcelLengthProp = localeCatalogDao.newPropertyDTO(2014, "Parcel-Length",
        PropertyType.MEASUREMENT);
    IPropertyDTO calculatedProp = localeCatalogDao.newPropertyDTO(2008, "ShortDescription",
        PropertyType.CALCULATED);
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100004);
    entityDAO.loadPropertyRecords(parcelLengthProp, calculatedProp);
    printJSON("Before Source Update:", entityDAO.getBaseEntityDTO());
    String initialCalcValue = ((IValueRecordDTO) entityDAO.getBaseEntityDTO().getPropertyRecord(2008)).getValue();
    printf("Before Source Update, calculation = %s\n", initialCalcValue);
    
    IValueRecordDTO record = (IValueRecordDTO) entityDAO.getBaseEntityDTO().getPropertyRecord(2014);
    double length = record.getAsNumber() + 10;
    record.setValue(String.format("%.2f %s", length, record.getUnitSymbol()));
    record.setAsNumber(length);
    entityDAO.updatePropertyRecords(record);
    if (useEventQueue) {
      printf("Source of calculation: record %s updated!\n", record.toCSExpressID().toString());
      Thread.sleep(sleepDelay); // Wait to allow the event handler running
    }
    else {
      printf("Source of calculation: record %s changed for direct calculation\n", record.toCSExpressID().toString());
      entityDAO.updateCalculationTargets(Arrays.asList(record));
    }
    entityDAO.loadPropertyRecords(parcelLengthProp, calculatedProp);
    IValueRecordDTO calcRecord = (IValueRecordDTO) entityDAO.getBaseEntityDTO().getPropertyRecord(2008);
    printf("Target of calculation: record %s reloaded!\n", calcRecord.toCSExpressID().toString());
    printJSON("After Source Update:", entityDAO.getBaseEntityDTO());
    String changedCalcValue = ((IValueRecordDTO) entityDAO.getBaseEntityDTO().getPropertyRecord(2008)).getValue();
    printf("After Source Update, calculation = %s\n", changedCalcValue);
    assert !initialCalcValue.equals(changedCalcValue);
  }
  
  @Test
  public void updateSourceOfCalculationWithoutEQ()
      throws RDBMSException, InterruptedException, CSFormatException
  {
    printTestTitle("updateSourceOfCalculationWithoutEQ (direct call)");
    testSourceOfCalculation(false, 0);
  }
  
  // @Ignore
  @Test
  public void updateSourceOfCalculationWithEQ()
      throws RDBMSException, InterruptedException, CSFormatException
  {
    printTestTitle("updateSourceOfCalculationWithEQ (with event queue)");
    testSourceOfCalculation(true, 500);
  }
  
  @Test
  public void updateCalculationFormula() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateCalculationFormula");
    localeCatalogDao.newPropertyDTO(2008, "ShortDescription",
        PropertyType.TEXT);
    IPropertyDTO calcProp = ConfigTestUtils.createRandomCalculatedProperty();
    String calcExpression = "= '1=> ' || [ShortDescription]";
    BaseEntityDAO entityDAO = DataTestUtils.openBaseEntityDAO(100005);
    IValueRecordDTO calcRecord = entityDAO.newValueRecordDTOBuilder(calcProp, "")
        .localeID(localeCatalogDto.getLocaleID())
        .build();
    calcRecord.addCalculation(calcExpression);
    entityDAO.createPropertyRecords(calcRecord);
    entityDAO.loadPropertyRecords(calcProp);
    calcRecord = (IValueRecordDTO) entityDAO.getBaseEntityDTO()
        .getPropertyRecord(calcProp.getIID());
    printJSON("Created calculated record", calcRecord);
    calcRecord.addCalculation(calcExpression.replace("1=>", "2=="));
    entityDAO.updatePropertyRecords(calcRecord);
    entityDAO.loadPropertyRecords(calcProp);
    calcRecord = (IValueRecordDTO) entityDAO.getBaseEntityDTO()
        .getPropertyRecord(calcProp.getIID());
    printJSON("After calculation update", calcRecord);
    assert (calcRecord.getValue()
        .startsWith("2=="));
  }
}
