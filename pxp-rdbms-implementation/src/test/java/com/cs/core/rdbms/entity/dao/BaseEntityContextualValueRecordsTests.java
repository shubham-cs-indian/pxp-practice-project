package com.cs.core.rdbms.entity.dao;

import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.testutil.AbstractRDBMSDriverTests;
import com.cs.core.rdbms.testutil.ConfigTestUtils;
import com.cs.core.rdbms.testutil.DataTestUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.junit.Before;
import org.junit.Test;

/**
 * @author vallee
 */
public class BaseEntityContextualValueRecordsTests extends AbstractRDBMSDriverTests {
  
  private IBaseEntityDAO curEntityDao;
  
  @Before
  public void init() throws RDBMSException
  {
    super.init();
  }
  
  @Test
  public void loadContextualValue() throws RDBMSException, CSFormatException
  {
    printTestTitle("loadContextualValue");
    curEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    IBaseEntityDTO entity = curEntityDao.loadPropertyRecords(ConfigTestUtils.getTextProperty("LongDescription"));
    printJSON("Entity with contextual property", entity);
    assert (!((IValueRecordDTO) (entity.getPropertyRecords().iterator().next())).getContextualObject().isNull());
  }
  
  private IValueRecordDTO createNewContextualValue(IPropertyDTO property, String value,
      IContextDTO context, long startDate, long endDate, String tagValueID) throws RDBMSException
  {
    IValueRecordDTO vr = curEntityDao.newValueRecordDTOBuilder(property, value)
        .localeID("en_US")
        .contextDTO(context)
        .build();
    vr.getContextualObject().setContextStartTime(startDate);
    vr.getContextualObject().setContextEndTime(endDate);
    vr.getContextualObject().setAllowDuplicate(true);
    if (tagValueID != null) {
      ITagValueDTO tag = ConfigTestUtils.createTagValue(tagValueID);
      ITagDTO tagRec = curEntityDao.newTagDTO(-10, tag.getTagValueCode());
      vr.getContextualObject().getContextTagValues().add(tagRec);
    }
    return vr;
  }
  
  @Test
  public void createContextualValues() throws RDBMSException, CSFormatException
  {
    printTestTitle("createContextualValues");
    curEntityDao = DataTestUtils.openBaseEntityDAO(100020);
    IContextDTO context = ConfigTestUtils.createRandomValueContext();
    IPropertyDTO property = ConfigTestUtils.getTextProperty("ShortDescription");
    
    IValueRecordDTO[] values = {
        createNewContextualValue(property, "C1 data in time", context, 0L,
            System.currentTimeMillis(), null),
        createNewContextualValue(property, "C2 data in time", context, System.currentTimeMillis(),
            System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1), null),
        createNewContextualValue(property, "RED data with Tag", context, 0, 0,
            ConfigTestUtils.newRandomCode("RED")),
        createNewContextualValue(property, "BLUE data with Tag", context, 0, 0,
            ConfigTestUtils.newRandomCode("BLUE")) };
    
    curEntityDao.createPropertyRecords(values);
    printJSON("After property creation", curEntityDao.getBaseEntityDTO());
    curEntityDao.loadPropertyRecords(property);
    printJSON("After property reloading", curEntityDao.getBaseEntityDTO());
  }
  
  @Test
  public void updateContextualValues() throws RDBMSException, CSFormatException
  {
    printTestTitle("updateContextualValues");
    curEntityDao = DataTestUtils.openBaseEntityDAO(100005);
    IPropertyDTO longDesc = ConfigTestUtils.getTextProperty("LongDescription");
    IPropertyDTO shortDesc = ConfigTestUtils.getTextProperty("ShortDescription");
    IBaseEntityDTO entity = curEntityDao.loadPropertyRecords(longDesc, shortDesc);
    IValueRecordDTO longDescVal = (IValueRecordDTO) entity
        .getPropertyRecord(longDesc.getPropertyIID());
    IValueRecordDTO shortDescVal = (IValueRecordDTO) entity
        .getPropertyRecord(shortDesc.getPropertyIID());
    longDescVal.getContextualObject().setContextStartTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(100));
    longDescVal.getContextualObject().setContextEndTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(100));
    shortDescVal.getContextualObject().setContextStartTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(100));
    shortDescVal.getContextualObject().setContextEndTime(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(100));
    
    curEntityDao.updatePropertyRecords(longDescVal, shortDescVal);
    printJSON("Updated with context", curEntityDao.getBaseEntityDTO());
  }
  
  @Test
  public void removeContextValueRecords() throws RDBMSException, CSFormatException
  {
    printTestTitle("removeContextValueRecords");
    BaseEntityDTO entity = DataTestUtils
        .createRandomBaseEntityWithName("ContextValueRecordTestEntity");
    curEntityDao = DataTestUtils.openBaseEntityDAO(entity.getBaseEntityIID());
    IContextDTO context = ConfigTestUtils.createRandomValueContext();
    IPropertyDTO property = ConfigTestUtils.getTextProperty("ShortDescription");
    
    IValueRecordDTO[] values = {
        createNewContextualValue(property, "GREEN data with Tag", context, 0, 0,
            ConfigTestUtils.newRandomCode("GREEN")),
        createNewContextualValue(property, "PINK data with Tag", context, 0, 0,
            ConfigTestUtils.newRandomCode("PINK")) };
    
    curEntityDao.createPropertyRecords(values);
    printJSON("before removing value record", curEntityDao.getBaseEntityDTO());
    IBaseEntityDTO loadPropertyRecords = curEntityDao.loadPropertyRecords(property);
    Set<IPropertyRecordDTO> propertyRecords = loadPropertyRecords.getPropertyRecords();
    
    curEntityDao.removeValueRecords((IValueRecordDTO[]) propertyRecords.toArray(new IValueRecordDTO[0]));
    printJSON("After removing value record",
        DataTestUtils.openBaseEntityDAO(entity.getBaseEntityIID()).getBaseEntityDTO());
  }
}
