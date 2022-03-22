package com.cs.core.bgprocess.services.asset;

import com.cs.constants.CommonConstants;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPStatus;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.Set;

/**
 * This is BGP task for Detecting duplicate assets 
 * 
 * @author jamil.ahmad
 *
 */
public class MarkDuplicateAssets extends AbstractBaseEntityProcessing {
  
  String organizationId;
  String userId;
  String localeId;
  String physicalCatalogId;
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    organizationId = initialProcessData.getEntryData().getInitField(CommonConstants.ORGANIZATION_ID, "-1");
    userId = initialProcessData.getEntryData().getInitField(CommonConstants.USER_ID, "");
    localeId = initialProcessData.getEntryData().getInitField(CommonConstants.LOCALE_ID, "");
    physicalCatalogId = initialProcessData.getEntryData().getInitField(CommonConstants.PHYSICAL_CATALOG_ID, "");
    super.initBeforeStart(initialProcessData, userSession);
  }
  
  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs)
      throws RDBMSException, CSFormatException, CSInitializationException, PluginException
  {
    try {
      IBaseEntityDAO baseEntityDAO = new BaseEntityDAO(new LocaleCatalogDAO(new UserSessionDTO(), new LocaleCatalogDTO()),
          new UserSessionDTO(), new BaseEntityDTO());
      jobData.getLog().info("Retriving all duplicate assets....");
      Set<Long> allDuplicateAssetsIIds = baseEntityDAO.detectAllDuplicateAssets(organizationId);
      
      if (!allDuplicateAssetsIIds.isEmpty()) {
        jobData.getLog().info("Marking all duplicate assets....");
        baseEntityDAO.markAssetsDuplicateByIIds(allDuplicateAssetsIIds, true);
        IUserSessionDAO userSessionDao = RDBMSAppDriverManager.getDriver().newUserSessionDAO();
        ILocaleCatalogDTO localeCatalogDto = userSessionDao.newLocaleCatalogDTO(localeId, physicalCatalogId, organizationId);
        ILocaleCatalogDAO localeCatalogDao =  userSessionDao.openLocaleCatalog(userSession, localeCatalogDto);
        for (Long duplicateIID : allDuplicateAssetsIIds) {
          localeCatalogDao.postUsecaseUpdate(duplicateIID, IEventDTO.EventType.ELASTIC_UPDATE);
        }
        jobData.getLog().info("Handling  already deleted  duplicate assets....");
        baseEntityDAO.handleDeletedDuplicateAssets(allDuplicateAssetsIIds);
      }
      else {
        jobData.getLog().info("No duplicate assets found.");
      }

    }
    catch (Exception e) {
      jobData.setStatus(BGPStatus.ENDED_EXCEPTION);
      throw new RDBMSException( 600, "Marking duplicate failed!", e);
    }
  }

}
