package com.cs.core.bgprocess.services.asset;

import com.cs.core.bgprocess.dto.AssetExpirationDTO;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.idto.IAssetExpirationDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.List;
import java.util.Set;

public class AssetExpiration extends AbstractBaseEntityProcessing {
  
  private ILocaleCatalogDAO localeCatalogDao;
  IAssetExpirationDTO       assetExpirationDto = new AssetExpirationDTO();
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    BGProcessDTO preJobData = (BGProcessDTO) initialProcessData;
    ((AssetExpirationDTO) assetExpirationDto).fromJSON(preJobData.getEntryData()
        .toString());
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs)
      throws RDBMSException, CSFormatException, CSInitializationException
  {
    IUserSessionDAO userSessionDao = RDBMSAppDriverManager.getDriver()
        .newUserSessionDAO();
    
    String localeID = assetExpirationDto.getDataLanguage();
    String physicalCatalogId = assetExpirationDto.getPhysicalCatalogId();
    String organizationId = assetExpirationDto.getOrganizationId();
    long currentTimeStamp = assetExpirationDto.getTimeStamp();
    
    ILocaleCatalogDTO localeCatalogDto = userSessionDao.newLocaleCatalogDTO(localeID, physicalCatalogId, organizationId);
    localeCatalogDao = userSessionDao.openLocaleCatalog(userSession, localeCatalogDto);
    List<Long> result = localeCatalogDao.updateAssetExpiryStatus(currentTimeStamp);
    
    for (long baseEntityIId : result) {
      localeCatalogDao.postUsecaseUpdate(baseEntityIId, IEventDTO.EventType.ELASTIC_UPDATE);
      try {
        jobData.getLog()
            .info("Asset Expired with success with IID : %s", baseEntityIId);
      }
      catch (Exception e) {
        jobData.getLog().info("Exception occered");
        jobData.getLog()
            .error(e.getMessage());
      }
    }
    jobData.getLog().info("Asset Expiration completed");
    
  }
  
}
