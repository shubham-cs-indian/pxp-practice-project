package com.cs.core.runtime.imprt.revision;

import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.driver.RDBMSAbstractDriver;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.SessionDTO;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.entity.dto.LanguageTranslationDTO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.revision.idao.IRevisionDAO;
import com.cs.core.rdbms.tracking.dto.ObjectTrackingDTO;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.dto.TrackingDTO;
import com.cs.core.rdbms.tracking.dto.UserSessionDTO;
import com.cs.core.rdbms.tracking.idao.IObjectTrackingDAO;
import com.cs.core.rdbms.tracking.idto.IObjectTrackingDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.rdbms.tracking.idto.ITrackingDTO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.irdbms.RDBMSDriverManager;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Scope("prototype")
public class ZeroRevisionCreator implements Runnable {

  private       List<String>        baseEntityIIDs;
  private       ILocaleCatalogDAO localeCatalogDAO;
  private final Long              userIID  = 10L;
  private final String            userName = "admin";


  public void setData(List<String> baseEntityIIDs, ILocaleCatalogDAO localeCatalogDAO)
  {
    this.baseEntityIIDs = baseEntityIIDs;
    this.localeCatalogDAO = localeCatalogDAO;
  }

  @Override public void run()
  {
    RDBMSAbstractDriver driver = (RDBMSAbstractDriver) RDBMSDriverManager.getDriver();
    TransactionTemplate transactionTemplate = new TransactionTemplate(driver.getTransactionManager());
    transactionTemplate.execute(new TransactionCallbackWithoutResult() {

      protected void doInTransactionWithoutResult(TransactionStatus status)
      {
        execute();
      }
    });
  }

  private void execute()
  {
    try {
      long l = System.currentTimeMillis();

      List<IBaseEntityDTO> baseEntityDTOs = localeCatalogDAO.getBaseEntitiesByIIDs(baseEntityIIDs);

      createNewRevision(baseEntityDTOs);
      RDBMSLogger.instance().info("ZERO REVISION CREATION ||  Time: %d ms", l - System.currentTimeMillis());
    }
    catch (Exception e) {
      RDBMSLogger.instance().info("Revision creation failed baseentity iid:" + baseEntityIIDs.get(0));
      RDBMSLogger.instance().exception(e);
    }
  }

  private Map<Long, IObjectTrackingDTO> createObjectTracking(List<IBaseEntityDTO> baseEntities)
      throws RDBMSException, CSFormatException, SQLException
  {
    IObjectTrackingDAO objectTrackingDAO = RDBMSAppDriverManager.getDriver().newObjectTrackingDAO();
    Map<Long, ITimelineDTO> timeLineDTO = prepareTrackingData(baseEntities);
    UserDTO userDTO = new UserDTO(10, "admin");

    Map<Long, IObjectTrackingDTO> objectTracings = new HashMap<>(10000);

    for (IBaseEntityDTO baseEntity : baseEntities) {
      long baseEntityIID = baseEntity.getBaseEntityIID();
      String trackingData = timeLineDTO.get(baseEntityIID).toJSON();
      String pxonDelta = baseEntity.toPXON();
      long objectTrackingWithTrack = objectTrackingDAO.createObjectTrackingWithTrack(baseEntityIID,
          baseEntity.getNatureClassifier().getClassifierIID(), userIID, trackingData, ITrackingDTO.TrackingEvent.CREATE,
          pxonDelta.getBytes(), System.currentTimeMillis());

      ObjectTrackingDTO value = new ObjectTrackingDTO(new TrackingDTO(userDTO, ITrackingDTO.TrackingEvent.CREATE), baseEntity.getCatalog(),
          baseEntity.getBaseEntityID(), baseEntity.getBaseEntityIID());
      value.setTrackingData(trackingData);
      value.setPxonExtract(pxonDelta);
      value.setIID(objectTrackingWithTrack);

      objectTracings.put(baseEntityIID, value);
    }
    return objectTracings;
  }

  private Map<Long, ITimelineDTO> prepareTrackingData(List<IBaseEntityDTO> entities) throws CSFormatException, RDBMSException, SQLException
  {
    Map<Long, ITimelineDTO> timelines = new HashMap<>();
    prepareFlatProperties(timelines, entities);
    prepareRecordProperties(timelines, entities);
    prepareChildren(timelines, entities);
    return timelines;
  }

  private void prepareFlatProperties(Map<Long, ITimelineDTO> timeLine, List<IBaseEntityDTO> entities) throws CSFormatException, RDBMSException
  {
    for(IBaseEntityDTO entity : entities){
      ITimelineDTO timeline = timeLine.computeIfAbsent(entity.getBaseEntityIID(), k -> new TimelineDTO());

      Set<IClassifierDTO> otherClassifiers = entity.getOtherClassifiers();
      if (!otherClassifiers.isEmpty()) {
        timeline.register(ChangeCategory.AddedClassifier, otherClassifiers);
      }
      handleLocale(entity, timeline);
      handleDefaultImage(entity, timeline);
    }
  }

  private void handleLocale(IBaseEntityDTO entity, ITimelineDTO changedData)
      throws CSFormatException
  {
    List<String> localeIds = entity.getLocaleIds();
    if (localeIds.size() > 1) {
      for (String localeId : localeIds) {
        if(!localeId.equals(entity.getBaseLocaleID())){
          ILanguageTranslationDTO translationDTO = new LanguageTranslationDTO(localeId, entity.getBaseEntityIID());
          changedData.register(ChangeCategory.CreatedTranslation, translationDTO);
        }
      }
    }
  }

  private void handleDefaultImage(IBaseEntityDTO entity, ITimelineDTO changedData)
  {
    if (entity.getDefaultImageIID() != 0L) {
      CSEObject defaultImage = new CSEObject(ICSEElement.CSEObjectType.Entity);
      defaultImage.setCode(String.valueOf(entity.getDefaultImageIID()));
      defaultImage.setIID(entity.getDefaultImageIID());
      changedData.register(ChangeCategory.NewDefaultImageIID, defaultImage);
    }
  }

  private void prepareRecordProperties(Map<Long, ITimelineDTO> timeLine, List<IBaseEntityDTO> entities)
      throws RDBMSException, SQLException, CSFormatException
  {
    localeCatalogDAO.fillAllProperties(entities);

    for (IBaseEntityDTO entity : entities) {
      ITimelineDTO timeline = timeLine.computeIfAbsent(entity.getBaseEntityIID(), k -> new TimelineDTO());
      for (IPropertyRecordDTO propertyRecord : entity.getPropertyRecords()) {
        if (propertyRecord instanceof IRelationsSetDTO) {
          timeline.register(ChangeCategory.CreatedRelation, propertyRecord);
        }
        else {
          timeline.register(ChangeCategory.CreatedRecord, propertyRecord);
        }
      }
    }
  }

  private void prepareChildren(Map<Long, ITimelineDTO> timeLines, List<IBaseEntityDTO> entities) throws RDBMSException, CSFormatException
  {
    Map<Long, List<IBaseEntityDTO>> children = localeCatalogDAO.getAllChildren(entities);
    for(IBaseEntityDTO entity : entities){
      long baseEntityIID = entity.getBaseEntityIID();
      List<IBaseEntityDTO> dtos = children.get(baseEntityIID);
      if(dtos != null && !dtos.isEmpty()){
        ITimelineDTO timeLine = timeLines.get(baseEntityIID);
        timeLine.register(ChangeCategory.AddedChild, dtos);
        entity.setChildrenFromEntity(EmbeddedType.CONTEXTUAL_CLASS, dtos);
      }
    }
  }

  private void createNewRevision(List<IBaseEntityDTO> latestEntities) throws Exception
  {
    Map<Long, IObjectTrackingDTO> objectTracking = createObjectTracking(latestEntities);
    IRevisionDAO revisionDAO = RDBMSAppDriverManager.getDriver().newRevisionDAO(new UserSessionDTO(new SessionDTO(), new UserDTO(userIID, userName), IUserSessionDTO.LogoutType.NORMAL, 0L, ""));
    revisionDAO.bulkCreateZeroRevision( latestEntities,  objectTracking);
  }

}
