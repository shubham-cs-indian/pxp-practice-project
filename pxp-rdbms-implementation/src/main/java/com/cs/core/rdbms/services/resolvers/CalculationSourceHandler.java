package com.cs.core.rdbms.services.resolvers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.eventqueue.dao.AbstractEventHandler;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * The handler for coupling source changes
 *
 * @author vallee
 */
public class CalculationSourceHandler extends AbstractEventHandler {
  
  private List<IPropertyRecordDTO> records         = new ArrayList<>();
  
  /**
   * Build a new coupling handler by assigning a number of identification
   *
   * @param driver
   *          the current application driver
   * @param handlerNo
   */
  public CalculationSourceHandler() throws Exception
  {
    super(RDBMSAppDriverManager.getDriver());
    RDBMSLogger.instance()
        .info(" initialized to manage calculation source events");
  }
  
  /**
   * The source of calculation changes corresponds to an array "dtos" in the
   * JSON part
   *
   * @param json
   *          the json extract from the event
   */
  private void fillSourcePropertyRecords(TimelineDTO timelineDTO)
      throws CSFormatException
  {
    TimelineDTO timelineData = currentEvent.getTimelineData();
    BaseEntityDTO baseEntity = new BaseEntityDTO();
    baseEntity.fromPXON(currentEvent.getJSONExtract().toString());
 
//    ICSEList calcSourceElements = timelineData.getElements(ChangeCategory.CalculationSource);
//    fillPropertyRecordsFromCSE(calcSourceElements, baseEntity);
    
    ICSEList coupSourceElements = timelineData.getElements(ChangeCategory.CouplingSource);
    fillPropertyRecordsFromCSE(coupSourceElements, baseEntity);
  }
  
  private void fillPropertyRecordsFromCSE(ICSEList recordsElements, BaseEntityDTO  baseEntityDTO) throws CSFormatException
  {
    if (recordsElements != null) {
      for (ICSEElement element : recordsElements.getSubElements()) {
        if (element.getSpecification(Keyword.$type).equals(SuperType.TAGS.name())) {
          TagsRecordDTO tagsRecord = new TagsRecordDTO();
          tagsRecord.fromCSExpressID(element);
       // fetch full information of tags record from base entity and add it into records
          records.add(baseEntityDTO.getPropertyRecord(tagsRecord.getProperty().getPropertyIID()));
        }
        else {
          ValueRecordDTO valueRecord = new ValueRecordDTO();
          valueRecord.fromCSExpressID(element);
          // fetch full information of value record from base entity and add it into records
          records.add(baseEntityDTO.getPropertyRecord(valueRecord.getProperty().getPropertyIID()));
        }
      }
    }
  }
  
  @Override
  public void run_New()
  {
    try {
      RDBMSLogger.instance()
          .info(" received Event: " + currentEvent);
      TimelineDTO sourceChange = currentEvent.getTimelineData();
      // => reopen the base entity DAO source of change with the correct locale
      // inheritance schema
      List<String> inheritanceSchema = sourceChange.getInheritanceSchema();
      String localeID = inheritanceSchema.isEmpty() ? IStandardConfig.GLOBAL_LOCALE
          : inheritanceSchema.get(inheritanceSchema.size() - 1);
      ILocaleCatalogDAO catalogDao = openCatalog(localeID);
      catalogDao.applyLocaleInheritanceSchema(inheritanceSchema);
      long baseEntityIID = currentEvent.getObjectIID();
      IBaseEntityDTO entity = catalogDao.getEntityByIID(baseEntityIID);
      BaseEntityDAO entityDao = (BaseEntityDAO) catalogDao.openBaseEntity(entity);
      // fill property records
      fillSourcePropertyRecords(sourceChange);
      RDBMSLogger.instance()
          .info("Update calculation targets from %d source(s)", records.size());
      entityDao.updateCalculationTargets(records);
      RDBMSLogger.instance()
          .info("End of calculation event handler ");
    }
    catch (Exception ex) {
      RDBMSLogger.instance().exception(ex);
    }
  }
}
