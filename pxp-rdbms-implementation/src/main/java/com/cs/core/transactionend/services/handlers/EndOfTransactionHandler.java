package com.cs.core.transactionend.services.handlers;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.elastic.sync.DeleteElasticDocument;
import com.cs.core.elastic.sync.FullElasticUpdate;
import com.cs.core.eventqueue.dao.AbstractEventHandler;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.handlers.dto.IDeltaInfoDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyDTO;
import com.cs.core.transactionend.handlers.dto.IPostProcessingOnCouplingUpdateDTO;
import com.cs.core.transactionend.handlers.dto.PostProcessingOnCouplingUpdateDTO;

public class EndOfTransactionHandler extends AbstractEventHandler {

  public EndOfTransactionHandler() throws RDBMSException
  {
    super(RDBMSAppDriverManager.getDriver());
  }

  @Override
  public void run_New()
  {
    try {
      RDBMSLogger.instance().info("Started Extraction of Delta");
      execute();
      RDBMSLogger.instance().info("Ended Extraction of Delta");
    }
    catch (Exception e) {
      RDBMSLogger.instance().info("Following Exception occured while extracting delta");
      RDBMSLogger.instance().exception(e);
    }
  }

  protected void execute() throws Exception
  {
    IBaseEntityDTO baseEntity = new BaseEntityDTO();
    baseEntity.fromPXON(currentEvent.getJSONExtract().toString());

    selfElasticUpdate(baseEntity);

    IDependencyDTO dependencies = getDependencies(baseEntity);
    triggerBackgroundProcesses(dependencies);
  }

  private IDependencyDTO getDependencies(IBaseEntityDTO baseEntity) throws RDBMSException, SQLException
  {
    Map<Long, IDeltaInfoDTO> deltaInfo = DeltaExtractor.getDelta(currentEvent.getTransactionId());
    IDependencyDTO dependencyDTO = DependencyTracer.traceDependents(deltaInfo, currentEvent.getLocaleID());
    return dependencyDTO;
  }

  private void triggerBackgroundProcesses(IDependencyDTO dependencies) throws RDBMSException, CSFormatException
  {
    if(!dependencies.getDependencies().isEmpty()) {

      dependencies.setCatalogCode(currentEvent.getCatalog().getCatalogCode());
      dependencies.setOrganizationCode(currentEvent.getCatalog().getOrganizationCode());
      dependencies.setLocaleID(currentEvent.getLocaleID());
      dependencies.setUserId(String.valueOf(userSessionDTO.getUserName()));
      dependencies.setUserName(userSessionDTO.getUserName());
      
      BGPDriverDAO.instance()
          .submitBGPProcess(userSessionDTO.getUserName(), "COUPLING_UPDATE_WORKFLOW_TRIGGER", "", IBGProcessDTO.BGPPriority.LOW,
              new JSONContent(dependencies.toJSON()));

      BGPDriverDAO.instance()
          .submitBGPProcess(userSessionDTO.getUserName(), "UPDATE_ELASTIC_COUPLING", "", IBGProcessDTO.BGPPriority.HIGH,
              new JSONContent(dependencies.toJSON()));
      
      for(IDependencyChangeDTO dependencyChangeDTO : dependencies.getDependencies().values()) {
        IPostProcessingOnCouplingUpdateDTO dto = new PostProcessingOnCouplingUpdateDTO();
        dto.setChanges(dependencyChangeDTO);
        dto.setCatalogCode(currentEvent.getCatalog().getCatalogCode());
        dto.setOrganizationCode(currentEvent.getCatalog().getOrganizationCode());
        dto.setLocaleID(currentEvent.getLocaleID());
        dto.setUserId(String.valueOf(userSessionDTO.getUserName()));
        dto.setUserName(userSessionDTO.getUserName());
        
        BGPDriverDAO.instance()
        .submitBGPProcess(userSessionDTO.getUserName(), "POST_PROCESSING_ON_COUPLING_UPDATE", "", IBGProcessDTO.BGPPriority.HIGH,
            new JSONContent(dto.toJSON()));
      }
    }
  }

  private void selfElasticUpdate(IBaseEntityDTO baseEntity) throws Exception
  {
    ILocaleCatalogDTO catalogDTO = new LocaleCatalogDTO(currentEvent.getLocaleID(), baseEntity.getCatalog().getCatalogCode(),
        baseEntity.getCatalog().getOrganizationCode());

    ILocaleCatalogDAO localeDAO = userSessionDao.openLocaleCatalog(userSessionDTO, catalogDTO);
    long baseEntityIID = baseEntity.getBaseEntityIID();

    IBaseEntityDTO entity = localeDAO.getEntityByIID(baseEntityIID);
    if (entity != null && !entity.isMerged()) {
      IBaseEntityDAO baseEntityDAO = localeDAO.openBaseEntity(entity);
      Collection<IPropertyDTO> allEntityProperties = localeDAO.getAllEntityProperties(baseEntityIID);
      baseEntityDAO.loadPropertyRecords(allEntityProperties.toArray(new IPropertyDTO[0]));
      FullElasticUpdate elasticUpdate = new FullElasticUpdate(baseEntityDAO);
      elasticUpdate.execute(localeDAO.getLocaleCatalogDTO().getLocaleID());
    }
    else {
      DeleteElasticDocument.execute(baseEntity);
    }
  }

  protected ILocaleCatalogDAO openCatalog(String localeID) throws RDBMSException
  {
    ILocaleCatalogDTO catalogDTO = new LocaleCatalogDTO(localeID, currentEvent.getCatalog().getCatalogCode(),
        currentEvent.getCatalog().getOrganizationCode());
    return userSessionDao.openLocaleCatalog(userSessionDTO, catalogDTO);
  }
}
