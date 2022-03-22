package com.cs.core.bgprocess.services.productDeleteHandler;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.AbstractBGProcessJob;
import com.cs.core.bgprocess.services.IBGProcessJob;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.ProductDeleteDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IProductDeleteDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class AssignDefaultImageOnAssetDeletion extends AbstractBGProcessJob implements IBGProcessJob{
  
  private static final String PROCESSED_IIDS      = "processedIIDs";
  private int                 batchSize;
  protected int               nbBatches           = 0;
  int                         currentBatchNo      = 0;
  IProductDeleteDTO           assetDeleteDTO      = new ProductDeleteDTO();
  protected Set<Long>         processedEntityIIDs = new HashSet<>();
  protected List<Long>        entityIIDs          = new ArrayList<>();
  protected ILocaleCatalogDAO localeCatalogDAO    = null;
  
  @Override
  public void initBeforeStart(IBGProcessDTO taskData, IUserSessionDTO defaultUserSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    super.initBeforeStart(taskData, defaultUserSession);
    assetDeleteDTO.fromJSON(jobData.getEntryData().toString());
    entityIIDs.addAll(assetDeleteDTO.getBaseEntityIIDs());
    batchSize = CSProperties.instance().getInt(propName("batchSize"));
    nbBatches = entityIIDs.size() / batchSize;
    localeCatalogDAO = openUserSession().openLocaleCatalog(userSession,
        new LocaleCatalogDTO(assetDeleteDTO.getLocaleID(), assetDeleteDTO.getCatalogCode(), ""));
    
    RDBMSLogger.instance().debug("End of initialization BGP %s / iid=%d", getService(), getIID());
    
  }
  
  @Override
  public BGPStatus runBatch() throws RDBMSException, CSFormatException, CSInitializationException,
      PluginException, Exception
  {
    currentBatchNo = currentBatchNo + 1;
    entityIIDs.removeAll(processedEntityIIDs);
    Set<Long> batchEntityIIDs = new HashSet<>();
    Iterator<Long> remEntityIID = entityIIDs.iterator();
    for (int i = 0; i < batchSize; i++) {
      if (!remEntityIID.hasNext())
        break;
      batchEntityIIDs.add(remEntityIID.next());
    }
    
    handleDefaultImage(batchEntityIIDs);
    processedEntityIIDs.addAll(batchEntityIIDs);
    
    jobData.getRuntimeData().setLongArrayField(PROCESSED_IIDS, processedEntityIIDs);
    RDBMSLogger.instance().info("Batch %d: processed base entity IIDs %s", getCurrentBatchNo(), batchEntityIIDs.toString());
    jobData.getProgress().setPercentageCompletion( currentBatchNo * 100 / nbBatches);
    
    // Keep continuing as soon as the final number of batches has not been reached or the last batch was not empty
    if (currentBatchNo < nbBatches && !batchEntityIIDs.isEmpty())
      return IBGProcessDTO.BGPStatus.RUNNING;
    return (jobData.getSummary().getNBOfErrors() == 0 ? IBGProcessDTO.BGPStatus.ENDED_SUCCESS
            : IBGProcessDTO.BGPStatus.ENDED_ERRORS);
  }

  private void handleDefaultImage(Set<Long> batchEntityIIDs) throws RDBMSException
  {
    for(Long iid : batchEntityIIDs) {
      IBaseEntityDTO entityDTO = localeCatalogDAO.getEntityByIID(iid);
      KlassInstanceUtils.handleDefaultImage(localeCatalogDAO.openBaseEntity(entityDTO));
    }
  }
}
