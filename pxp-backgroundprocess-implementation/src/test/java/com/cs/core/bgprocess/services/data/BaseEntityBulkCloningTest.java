package com.cs.core.bgprocess.services.data;


import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.testutil.AbstractBGProcessTests;
import com.cs.core.json.JSONContent;
import static com.cs.core.printer.QuickPrinter.printTestTitle;
import static com.cs.core.printer.QuickPrinter.println;
import com.cs.core.rdbms.entity.dto.BaseEntityIDDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class BaseEntityBulkCloningTest extends AbstractBGProcessTests {
  
  private static final String SERVICE       = "CLONING";
  private static final int    NB_BATCHES   = 10;
  IBaseEntityDAO              baseEntityDao = null;
  
  @Before
  public void init() throws CSInitializationException, RDBMSException, CSFormatException
  {
    super.init();
    AbstractBGProcessJob.disableCallback(); // disable callback for tests
  }
  
  @Test
  public void bulkCloningSubmitBGProcess() throws CSInitializationException, Exception
  {
    printTestTitle("runSamples " + SERVICE);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.LOW;
    BaseEntityPlanDTO entryData = new BaseEntityPlanDTO();
    List<Long> baseEntityIIDs = new ArrayList<>();
    // entities are chosen to have no "other classifiers" not known from ODB
    baseEntityIIDs.add(100001L);
    baseEntityIIDs.add(100002L);
    baseEntityIIDs.add(100014L);
    baseEntityIIDs.add(100009L);
    baseEntityIIDs.add(100038L);
    entryData.setBaseEntityIIDs(baseEntityIIDs);
    entryData.setAllProperties(true);
    
    BaseEntityIDDTO entity = (BaseEntityIDDTO) localeCatalogDao.getEntityByIID(100030L);
    baseEntityDao = localeCatalogDao.openBaseEntity(entity);
        
    long jobIID = BGPDriverDAO.instance().submitBGPProcess(
            "Admin", SERVICE, getTestCallbackTemplateURL(), userPriority, new JSONContent(entryData.toJSON()));
    
    this.runJobSample(NB_BATCHES);
    println("Executed samples of " + NB_BATCHES + " batches");
    displayLogContent( jobIID);
  }
}
