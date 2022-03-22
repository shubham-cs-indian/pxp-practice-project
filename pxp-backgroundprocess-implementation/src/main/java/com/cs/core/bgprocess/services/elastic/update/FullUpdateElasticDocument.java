package com.cs.core.bgprocess.services.elastic.update;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.elastic.sync.FullElasticUpdate;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.handlers.dto.DependencyDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyDTO;

import java.util.Collection;
import java.util.Set;

public class FullUpdateElasticDocument extends AbstractBaseEntityProcessing  {

  ILocaleCatalogDAO localeCatalogDAO;
  IDependencyDTO    dependencyDTO = new DependencyDTO();

  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    BGProcessDTO preJobData = (BGProcessDTO) initialProcessData;
    dependencyDTO.fromJSON(preJobData.getEntryData().toString());
    IUserSessionDAO userSessionDAO = openUserSession();
    localeCatalogDAO = userSessionDAO.openLocaleCatalog(userSession,
        new LocaleCatalogDTO(dependencyDTO.getLocaleID(), dependencyDTO.getCatalogCode(), dependencyDTO.getOrganizationCode()));

    preJobData.getEntryData().setLongArrayField(BaseEntityPlanDTO.BASE_ENTITY_IIDS, dependencyDTO.getDependencies().keySet());
    super.initBeforeStart(initialProcessData, userSession);
  }

  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws Exception
  {
    for (Long baseEntityIID : batchIIDs) {
      IBaseEntityDTO entity = localeCatalogDAO.getEntityByIID(baseEntityIID);
      if (entity != null && !entity.getLocaleCatalog().getCatalogCode().equals("diff-"+ IStandardConfig.StandardCatalog.pim.toString())) {
        IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(entity);
        Collection<IPropertyDTO> allEntityProperties = localeCatalogDAO.getAllEntityProperties(baseEntityIID);
        baseEntityDAO.loadPropertyRecords(allEntityProperties.toArray(new IPropertyDTO[0]));
        FullElasticUpdate elasticUpdate = new FullElasticUpdate(baseEntityDAO);
        elasticUpdate.execute(localeCatalogDAO.getLocaleCatalogDTO().getLocaleID());
      }
    }
  }
}
