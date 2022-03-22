package com.cs.core.bgprocess.services.elastic.update;

import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.dto.ElasticRelationshipUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IElasticRelationshipUpdateDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.Set;

public class RelationshipElasticUpdate extends AbstractBaseEntityProcessing {

  IElasticRelationshipUpdateDTO relationDTO      = new ElasticRelationshipUpdateDTO();
  ILocaleCatalogDAO             localCatalogDAO;
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
      throws CSInitializationException, CSFormatException, RDBMSException
  {
    BGProcessDTO preJobData = (BGProcessDTO) initialProcessData;
    relationDTO.fromJSON(preJobData.getEntryData().toString());
    IUserSessionDAO userSessionDAO = openUserSession();
    localCatalogDAO = userSessionDAO.openLocaleCatalog(userSession,
        new LocaleCatalogDTO(relationDTO.getLocaleID(), relationDTO.getCatalogCode(), relationDTO.getOrganizationCode()));
    preJobData.getEntryData().setLongArrayField(BaseEntityPlanDTO.BASE_ENTITY_IIDS, relationDTO.getEntitiesToUpdate());
    super.initBeforeStart(initialProcessData, userSession);
  }

  @Override
  protected void runBaseEntityBatch(Set<Long> batchIIDs) throws RDBMSException
  {
    for (Long batchIID : batchIIDs) {
      localCatalogDAO.postUsecaseUpdate(batchIID, EventType.ELASTIC_UPDATE);
    }
  }
}
