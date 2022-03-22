package com.cs.core.bgprocess.services.data;


import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.TransferPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class BaseEntityTransferTest extends AbstractBGProcessTests {
  
  private static final String SERVICE       = "TRANSFER";
  private static final int    NB_BATCHES   = 10;
  IBaseEntityDAO              baseEntityDao = null;
  
  @Before
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  @Test
  public void transferSubmitBGProcess() throws CSInitializationException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    TransferPlanDTO entryData = new TransferPlanDTO();
    List<Long> baseEntityIIDs = new ArrayList<>();
    // entities are chosen to have no "other classifiers" not known from ODB
    baseEntityIIDs.add(100014L);
    baseEntityIIDs.add(100009L);
    baseEntityIIDs.add(100038L);
    entryData.setAllProperties(true);
    entryData.setBaseEntityIIDs(baseEntityIIDs);
    entryData.setSourceCatalogCode("pim");
    entryData.setTargetCatalogCode("onboarding");
    
    BaseEntityIDDTO entity = (BaseEntityIDDTO) localeCatalogDao.getEntityByIID(100030L);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
        
    long jobIID = BGPDriverDAO.instance().submitBGPProcess(
            "Admin", SERVICE, getTestCallbackTemplateURL(), userPriority, new JSONContent(entryData.toJSON()));
    
    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent( jobIID);
  }
  
  @Test
  public void transferFromOrganzation() throws CSInitializationException, Exception
  {
    printTestTitle("transferFromOrganzation " + SERVICE);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    TransferPlanDTO entryData = new TransferPlanDTO();
    List<Long> baseEntityIIDs = new ArrayList<>();
    BaseEntityDTO baseEntity1 = createRandomBaseEntityWithName("TRN");
    BaseEntityDTO baseEntity2 = createRandomBaseEntityWithName("TRN");
    BaseEntityDTO baseEntity3 = createRandomBaseEntityWithName("TRN");
    baseEntityIIDs.add(baseEntity1.getBaseEntityIID());
    baseEntityIIDs.add(baseEntity2.getBaseEntityIID());
    baseEntityIIDs.add(baseEntity3.getBaseEntityIID());
    entryData.setAllProperties(true);
    entryData.setBaseEntityIIDs(baseEntityIIDs);
    entryData.setSourceCatalogCode("pim");
    entryData.setTargetCatalogCode("onboarding");
    entryData.setSourceOrganizationCode(IStandardConfig.STANDARD_ORGANIZATION_CODE);
    entryData.setTargetOrganizationCode("ORG_PXP");
        
    long jobIID = BGPDriverDAO.instance().submitBGPProcess(
            "Admin", SERVICE, getTestCallbackTemplateURL(), userPriority, new JSONContent(entryData.toJSON()));
    
    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent( jobIID);
  }
  
  public static BaseEntityDTO createRandomBaseEntityWithName(String idPrefix) throws RDBMSException
  {
    String baseEntityID = idPrefix + (new Random()).nextInt(10000);
    BaseEntityIDDTO entity = (BaseEntityIDDTO) AbstractBGProcessTests.localeCatalogDao
        .newBaseEntityDTOBuilder(baseEntityID, IBaseEntityIDDTO.BaseType.ARTICLE,
            AbstractBGProcessTests.localeCatalogDao.newClassifierDTO(4000, "Article",
                IClassifierDTO.ClassifierType.CLASS)).build();
    IBaseEntityDAO openBaseEntity = AbstractBGProcessTests.localeCatalogDao
        .openBaseEntity(entity);
    IBaseEntityDTO createPropertyRecords = createNamePropertyRecord(baseEntityID, openBaseEntity);
    return (BaseEntityDTO) createPropertyRecords;
  }
  
  private static IBaseEntityDTO createNamePropertyRecord(String baseEntityID,
      IBaseEntityDAO openBaseEntity) throws RDBMSException
  {
    IPropertyDTO childArticleNameProp = AbstractBGProcessTests.localeCatalogDao
        .newPropertyDTO(200, "nameattribute", IPropertyDTO.PropertyType.TEXT);
    IValueRecordDTO articleNameDto = openBaseEntity
        .newValueRecordDTOBuilder(childArticleNameProp, "")
        .localeID(AbstractBGProcessTests.localeCatalogDao.getLocaleCatalogDTO().getLocaleID())
        .build();
    
    IBaseEntityDTO createPropertyRecords = openBaseEntity.createPropertyRecords(articleNameDto);
    return createPropertyRecords;
  }
}
