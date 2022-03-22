package com.cs.core.bgprocess.services.productDeleteHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.bgprocess.utils.BgprocessUtils;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.ProductDeleteDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class ProductDeleteHandler extends AbstractBGProcessJob implements IBGProcessJob {
  
  ProductDeleteDTO    productDeleteDTO     = new ProductDeleteDTO();
  
  private int         batchSize;
  private int         currentBatchNo       = 0;
  protected int       nbBatches            = 0;
  
  protected Set<Long> passedBaseEntityIIDs = new HashSet<>();
  
  protected KlassInstanceUtils klassInstanceUtils;
  protected ILocaleCatalogDAO  localeCatalogDAO;
  
  @Override
  public BGPStatus runBatch() throws Exception
  {
    
    currentBatchNo = currentBatchNo + 1;
    
    Set<Long> currentBaseEntities = new HashSet<>();
    currentBaseEntities.addAll(productDeleteDTO.getBaseEntityIIDs());
    currentBaseEntities.removeAll(passedBaseEntityIIDs);
    
    Set<Long> batchEntityIIDs = new HashSet<>();
    Iterator<Long> remEntityIID = currentBaseEntities.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      Long sourceEntityIID = remEntityIID.next();
      batchEntityIIDs.add(sourceEntityIID);

      // Deleting the source article instances
      if (productDeleteDTO.getSourceEntityIIDs().contains(sourceEntityIID)) {
        klassInstanceUtils.deleteCoupledRecord(sourceEntityIID);
        rdbmsComponentUtils.getBaseEntityDAO(sourceEntityIID).delete(true);
        localeCatalogDAO.openTaskDAO().deleteTasksByBaseEntityIID(sourceEntityIID);
      }
    }
    
    BgprocessUtils.deleteChildrens(new ArrayList<Long>(batchEntityIIDs),
        this.rdbmsComponentUtils.getUserID(), localeCatalogDAO);
    
    for (Long baseEntityIID : batchEntityIIDs) {
      BgprocessUtils.updateBaseEntityKpi(baseEntityIID, localeCatalogDAO);
    }
    
    passedBaseEntityIIDs.addAll(batchEntityIIDs);
    
    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);
    // Keep continuing as soon as the final number of batches has not been reached or the last batch was not empty
    if (currentBatchNo < nbBatches && !batchEntityIIDs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
    
  }

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(initialProcessData, userSession);
    productDeleteDTO.fromJSON(jobData.getEntryData().toString());
    
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    nbBatches = productDeleteDTO.getBaseEntityIIDs().size() / batchSize;
    
    localeCatalogDAO = openUserSession().openLocaleCatalog(userSession,
        new LocaleCatalogDTO(productDeleteDTO.getLocaleID(), productDeleteDTO.getCatalogCode(), ""));
    
    klassInstanceUtils = BGProcessApplication.getApplicationContext().getBean(KlassInstanceUtils.class);
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
  }
  
}
