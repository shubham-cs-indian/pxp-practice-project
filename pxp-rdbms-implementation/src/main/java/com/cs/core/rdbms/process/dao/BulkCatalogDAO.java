package com.cs.core.rdbms.process.dao;

import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSDataAccessService;
import com.cs.core.rdbms.entity.dao.BulkBaseEntityDAS;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.ContextualDataDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IRelationsSetDTO;
import com.cs.core.rdbms.entity.idto.ITagsRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.process.dto.BulkReportDTO;
import com.cs.core.rdbms.process.idao.IBulkCatalogDAO;
import com.cs.core.rdbms.process.idto.IBulkReportDTO;
import com.cs.core.rdbms.tracking.dto.SimpleTrackingDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author vallee
 */
public class BulkCatalogDAO implements IBulkCatalogDAO {
  
  private final LocaleCatalogDTO localeCatalog;
  private final IUserSessionDTO  userSession;
  
  public BulkCatalogDAO(IUserSessionDTO userSession, ILocaleCatalogDTO catalog)
  {
    this.localeCatalog = (LocaleCatalogDTO) catalog;
    this.userSession = userSession;
  }
  
  @Override
  public IBulkReportDTO createBaseEntities(List<IBaseEntityDTO> entities)
      throws RDBMSException, CSFormatException
  {
    final List<Integer> bulkErrors = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction(( connection) -> {
          BulkBaseEntityDAS bulkDas = new BulkBaseEntityDAS( connection);
          Long[] entityIIDs = bulkDas.getRdbmsIdentifiers(
                  entities.size(), RDBMSDataAccessService.SequenceType.RuntimeSequence);
          int count = 0;
          for ( IBaseEntityDTO entity : entities ) {
            BaseEntityDTO entityDto = ((BaseEntityDTO) entity);
            if (entityDto.getBaseEntityIID() > 0)
              throw new RDBMSException(500, "Bulk", 
                      "Cannot create a base entity with an existing IID :" + entityDto.toCSExpressID());
            entityDto.setIID(entityIIDs[count++]);
            if ( !entityDto.getContextualObject().getContextCode().isEmpty() ) {
              ((ContextualDataDTO) entityDto.getContextualObject()).setIID(entityDto.getBaseEntityIID());
              bulkDas.updateBaseEntityContextualData( entityDto.getContextualObject(), UpdateLinkMode.overwrite);
            }
            entityDto.setLocaleCatalog(localeCatalog);
            entityDto.setCreatedTrack(new SimpleTrackingDTO(userSession.getUserName()));
            entityDto.setLastModifiedTrack(new SimpleTrackingDTO(userSession.getUserName()));
            if (entity.getNatureClassifier().getIID() == 0) {
              IClassifierDTO natureClass = ConfigurationDAO.instance()
                      .getClassifierByCode( connection, entity.getNatureClassifier().getCode());
              ((ClassifierDTO) entity.getNatureClassifier()).setIID(natureClass.getIID());
            }
            bulkDas.updateClassifiers(entityDto, UpdateLinkMode.add);
            bulkDas.updateChildren(entityDto, UpdateLinkMode.add);
            bulkDas.createEntity(entityDto, userSession.getUserIID());
          }
          bulkErrors.addAll(bulkDas.endCreateEntity());
          bulkDas.endUpdateBaseEntityContextualData();
          bulkDas.endUpdateClassifiers();
          bulkDas.endUpdateChildren();
        });
    return new BulkReportDTO(bulkErrors);
  }
  
  @Override
  public IBulkReportDTO updateBaseEntities(UpdateLinkMode classifiersMode,
      UpdateLinkMode childrenMode, UpdateLinkMode contextTagsMode,
      Collection<IBaseEntityDTO> entities) throws RDBMSException, CSFormatException
  {
    final List<Integer> bulkErrors = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction(( connection) -> {
          BulkBaseEntityDAS bulkDas = new BulkBaseEntityDAS( connection);
          for (IBaseEntityDTO entity : entities) {
            BaseEntityDTO entityDto = ((BaseEntityDTO) entity);
            if (entityDto.getBaseEntityIID() == 0)
              throw new RDBMSException(500, "Bulk",
                  "Cannot update a base entity without IID :" + entityDto.toCSExpressID());
            bulkDas.updateClassifiers(entityDto, classifiersMode);
            if (!entityDto.getContextualObject()
                .getContextCode()
                .isEmpty()) {
              ((ContextualDataDTO) entityDto.getContextualObject())
                  .setIID(entityDto.getBaseEntityIID());
              bulkDas.updateBaseEntityContextualData(entityDto.getContextualObject(),
                  contextTagsMode);
            }
            bulkDas.updateEntity(entityDto, userSession.getUserIID());
          }
          bulkErrors.addAll(bulkDas.endUpdateEntity());
          bulkDas.endUpdateClassifiers();
          bulkDas.endUpdateBaseEntityContextualData();
        });
    return new BulkReportDTO(bulkErrors);
  }
  
  @Override
  public IBulkReportDTO createValueRecords(Collection<IBaseEntityDTO> entities,
      Collection<IValueRecordDTO> records) throws RDBMSException, CSFormatException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public IBulkReportDTO updateValueRecords(Collection<IBaseEntityDTO> entities,
      UpdateLinkMode contextTagsMode, Collection<IValueRecordDTO> records)
      throws RDBMSException, CSFormatException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public IBulkReportDTO createTagsRecords(Collection<IBaseEntityDTO> entities,
      Collection<ITagsRecordDTO> records) throws RDBMSException, CSFormatException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public IBulkReportDTO updateTagsRecords(Collection<IBaseEntityDTO> entities,
      UpdateLinkMode tagsMode, Collection<ITagsRecordDTO> records)
      throws RDBMSException, CSFormatException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public IBulkReportDTO createRelationsSets(Collection<IBaseEntityDTO> entities,
      Collection<IRelationsSetDTO> records) throws RDBMSException, CSFormatException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
  @Override
  public IBulkReportDTO updateRelationsSets(Collection<IBaseEntityDTO> entities,
      UpdateLinkMode tagsMode, Collection<ITagsRecordDTO> records)
      throws RDBMSException, CSFormatException
  {
    throw new UnsupportedOperationException("Not supported yet."); 
  }
  
}
