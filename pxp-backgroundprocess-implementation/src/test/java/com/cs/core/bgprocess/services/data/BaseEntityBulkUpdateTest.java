package com.cs.core.bgprocess.services.data;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BaseEntityBulkUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class BaseEntityBulkUpdateTest extends AbstractBGProcessTests {
  
  private static final String SERVICE       = "BULK_UPDATE";
  private static final int    NB_BATCHES   = 10;
  IBaseEntityDAO              baseEntityDao = null;
  
  @Before
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  @Test
  public void bulkUpdateSubmitBGProcess() throws CSInitializationException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    BaseEntityBulkUpdateDTO entryData = new BaseEntityBulkUpdateDTO();
    List<Long> baseEntityIIDs = new ArrayList<>();
    // entities are chosen to have no "other classifiers" not known from ODB
    baseEntityIIDs.add(100035L);
    baseEntityIIDs.add(100037L);
    baseEntityIIDs.add(100038L);
    entryData.setBaseEntityIIDs(baseEntityIIDs);
    
    entryData.setLocaleID("en_US");
    entryData.setCatalogCode("pim");
    
    BaseEntityIDDTO entity = (BaseEntityIDDTO) localeCatalogDao.getEntityByIID(100030L);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
    
    IPropertyDTO longDescription = localeCatalogDao.newPropertyDTO(0, "longdescriptionattribute", PropertyType.TEXT);
    IValueRecordDTO longDescriptionValueRecord = baseEntityDao
        .newValueRecordDTOBuilder(longDescription, "long description through bulk update")
        .localeID("en_US")
        .build();
    
    entryData.getPropertyRecords().add(longDescriptionValueRecord);
    IPropertyDTO nameAttribute = localeCatalogDao.newPropertyDTO( 
            IStandardConfig.StandardProperty.nameattribute.getIID(), 
            IStandardConfig.StandardProperty.nameattribute.name(), PropertyType.TEXT);
    IValueRecordDTO nameValueRecord = baseEntityDao
        .newValueRecordDTOBuilder(nameAttribute, "NEW bulk updated name XXX")
        .localeID("en_US")
        .build();
    entryData.getPropertyRecords().add(nameValueRecord);
    
    long jobIID = BGPDriverDAO.instance().submitBGPProcess(
            "Admin", SERVICE, getTestCallbackTemplateURL(), userPriority, new JSONContent(entryData.toJSON()));
    
    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent( jobIID);
  }
}
