
package com.cs.core.rdbms.services.resolvers;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.cs.core.eventqueue.dao.AbstractEventHandler;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.coupling.dto.CouplingDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.dto.BaseEntityDTO;
import com.cs.core.rdbms.entity.dto.TagsRecordDTO;
import com.cs.core.rdbms.entity.dto.ValueRecordDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.dto.TimelineDTO;
import com.cs.core.rdbms.tracking.idto.ITimelineDTO.ChangeCategory;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.icsexpress.ICSEList;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingBehavior;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling.CouplingType;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class CouplingSourceHandler extends AbstractEventHandler{
  
  private List<IPropertyRecordDTO> records         = new ArrayList<>();
  ILocaleCatalogDAO catalogDao;
 
  public CouplingSourceHandler() throws RDBMSException
  {
    super(RDBMSAppDriverManager.getDriver());
    RDBMSLogger.instance().info(" initialized to manage rule events");
  }
 

  @Override
  public void run_New()  
  {
    try {
      @SuppressWarnings("unused")
      TimelineDTO sourceChange = currentEvent.getTimelineData();
      String localeID = currentEvent.getLocaleID();
      catalogDao = openCatalog(localeID);
      
      fillCreatedAndUpdatedPropertyRecords(currentEvent.getTimelineData(), catalogDao);
      long baseEntityIID = currentEvent.getObjectIID();

      ICouplingDAO couplingDAO = catalogDao.openCouplingDAO();
      
      for(IPropertyRecordDTO record : records) {
        updateSourceCoupling(baseEntityIID, record, couplingDAO);
        updateSourceCouplingForLanguageInheritance(baseEntityIID, record, couplingDAO);
      }
      
    }
    catch (CSFormatException | RDBMSException e) {
      e.printStackTrace();
      RDBMSLogger.instance().exception(e);
    }
    
  }

  private static final String GET_CONFLICTING_VALUES = "Select * from pxp.conflictingvalues where propertyiid = ? and "
      + "sourceentityiid = ? ";
  private static final String GET_COUPLING_TYPES = "Select * from pxp.conflictingvalues where propertyiid = ? "
      + "and targetentityiid = ?";
  
  private void updateSourceCoupling(long sourceEntityIID, IPropertyRecordDTO record, ICouplingDAO couplingDAO) throws RDBMSException
  {
    List<ICouplingDTO> dtos = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
      PreparedStatement prepareStatement = connection.prepareStatement(GET_CONFLICTING_VALUES);
      prepareStatement.setLong(1, record.getProperty().getPropertyIID());
      prepareStatement.setLong(2, sourceEntityIID);
      prepareStatement.execute();
      IResultSetParser result = connection.getResultSetParser(prepareStatement.getResultSet());
      while (result.next()) {
        dtos.add(new CouplingDTO(result));
      }
    });
    for(ICouplingDTO dto : dtos) {
      List<Integer>  coupling = new ArrayList<>();
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection connection) -> {
        PreparedStatement prepareStatement = connection.prepareStatement(GET_COUPLING_TYPES);
        prepareStatement.setLong(1, record.getProperty().getPropertyIID());
        prepareStatement.setLong(2, dto.getTargetEntityIID());
        prepareStatement.execute();
        IResultSetParser result = connection.getResultSetParser(prepareStatement.getResultSet());
        while (result.next()) {
          coupling.add(result.getInt("couplingtype"));
        }
      });
      Boolean isDynamic = false;
      if (coupling.contains(CouplingBehavior.DYNAMIC.ordinal())) {
        isDynamic = true;
      }
      if (dto.getCouplingType().equals(CouplingBehavior.TIGHTLY) && !isDynamic) {
        if(dto.getCouplingSourceType() == CouplingType.LANG_INHERITANCE) {
            continue;
        }
          
        long localeIID = dto.getLocaleIID();
        if(localeIID == 0l || ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID).getLanguageCode().
            equals(catalogDao.getLocaleCatalogDTO().getLocaleID())) {
          
          couplingDAO.updateSourceCoupledRecord(dto.getTargetEntityIID(), sourceEntityIID, record.getProperty().getPropertyIID(),
              dto.getCouplingSourceIID(), localeIID);
        }
      }
      if (dto.getCouplingType().equals(CouplingBehavior.DYNAMIC)) {
        
        long localeIID = dto.getLocaleIID();
        if (localeIID == 0l || ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID).getLanguageCode()
            .equals(catalogDao.getLocaleCatalogDTO().getLocaleID())) {
          
          List<ICouplingDTO> coupledRecordDTOs = couplingDAO.getCoupledRecordForDynamicCoupled(dto.getTargetEntityIID(), sourceEntityIID,
              record.getProperty().getPropertyIID(), dto.getCouplingSourceIID(), localeIID);
          
          if (coupledRecordDTOs.size() != 1) {
            couplingDAO.updateSourceCoupledRecordForDynamicCoupling(dto.getTargetEntityIID(), sourceEntityIID,
                record.getProperty().getPropertyIID(), dto.getCouplingSourceIID(), localeIID);
          }
          else {
            IBaseEntityDTO targetBaseEntity = catalogDao.getEntityByIID(dto.getTargetEntityIID());
            IBaseEntityDAO targetBaseEntityDAO = catalogDao.openBaseEntity(targetBaseEntity);
            targetBaseEntityDAO.registerForCoupledEvent(record);
          }
        }
      }
    }
  }
  
  private void updateSourceCouplingForLanguageInheritance(long sourceEntityIID, IPropertyRecordDTO record, 
      ICouplingDAO couplingDAO) throws RDBMSException {
    
    if(record instanceof IValueRecordDTO ) {
      IValueRecordDTO valueRecord = (IValueRecordDTO) record;
      
      if(valueRecord.getLocaleID() ==  null || valueRecord.getLocaleID().isEmpty()) {
        return;
      }
      
      ICouplingDTO couplingDTO = catalogDao.newCouplingDTOBuilder().build();
      couplingDTO.setSourceEntityIID(sourceEntityIID);
      couplingDTO.setPropertyIID(record.getProperty().getPropertyIID());
      couplingDTO.setCouplingSourceType(CouplingType.LANG_INHERITANCE);
      couplingDTO.setCouplingSourceIID(ConfigurationDAO.instance().getLanguageConfig(valueRecord.getLocaleID()).getLanguageIID());
      
      List<ICouplingDTO> conflictingValues = couplingDAO.getConflictingValuesByLocaleIID(couplingDTO);
      
      for(ICouplingDTO dto : conflictingValues) {
        couplingDAO.updateSourceCoupledRecord(dto.getTargetEntityIID(), sourceEntityIID, record.getProperty().getPropertyIID(),
            dto.getCouplingSourceIID(), dto.getLocaleIID());
      }
    }
  }
  
  private void fillCreatedAndUpdatedPropertyRecords(TimelineDTO timelineDTO, ILocaleCatalogDAO catalogDao)
      throws CSFormatException
  {
    TimelineDTO timelineData = currentEvent.getTimelineData();
    BaseEntityDTO baseEntity = new BaseEntityDTO();
    baseEntity.fromPXON(currentEvent.getJSONExtract()
        .toString());
    
    ICSEList createdRecordsElements = timelineData.getElements(ChangeCategory.CreatedRecord);
    fillPropertyRecordsFromCSE(createdRecordsElements, catalogDao);
    
    ICSEList updatedRecordsElements = timelineData.getElements(ChangeCategory.UpdatedRecord);
    fillPropertyRecordsFromCSE(updatedRecordsElements, catalogDao);
  }
  
  private void fillPropertyRecordsFromCSE(ICSEList recordsElements, ILocaleCatalogDAO catalogDao) throws CSFormatException
  {
    if (recordsElements != null) {
      for (ICSEElement element : recordsElements.getSubElements()) {
        if (element.getSpecification(Keyword.$type).equals(SuperType.TAGS.name())) {
          TagsRecordDTO tagsRecord = new TagsRecordDTO();
          tagsRecord.fromCSExpressID(element);
          records.add(tagsRecord);
        }
        else {
          ValueRecordDTO valueRecord = new ValueRecordDTO();
          valueRecord.fromCSExpressID(element);
          records.add(valueRecord);
          String localeID = valueRecord.getLocaleID();
          if(!StringUtils.isEmpty(localeID)) {
          catalogDao.getLocaleCatalogDTO().setLocaleID(localeID);
          catalogDao.getLocaleCatalogDTO().setLocaleInheritanceSchema(Arrays.asList(localeID));
          }
        }
      }
    }
  }
}
