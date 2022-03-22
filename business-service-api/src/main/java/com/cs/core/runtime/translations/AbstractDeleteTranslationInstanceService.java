package com.cs.core.runtime.translations;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.templating.IGetNumberOfVersionsToMaintainResponseModel;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.coupling.idao.ICouplingDAO;
import com.cs.core.rdbms.coupling.idto.ICouplingDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO.RecordStatus;
import com.cs.core.rdbms.process.dao.LocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.DeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetNumberOfVersionsToMaintainStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.TranslationInstanceUtils;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.coupling.ICSECoupling;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class AbstractDeleteTranslationInstanceService<P extends IDeleteTranslationRequestModel, R extends IDeleteTranslationResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  RDBMSComponentUtils                              rdbmsComponentUtils;


  @Autowired
  TranslationInstanceUtils translationInstanceUtils;

  @Autowired
  protected IGetNumberOfVersionsToMaintainStrategy getConfigDetailsForDeleteTranslationStrategy;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    String localeIdToDelete = model.getLanguageCodes().get(0);
    long baseEntityIID = model.getContentId();
    List<String> successIds = new ArrayList<>();
    IExceptionModel exception = new ExceptionModel();
    IDeleteTranslationResponseModel responseModel = new DeleteTranslationResponseModel();
    
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(baseEntityIID);
    translationInstanceUtils.handleTranslationDelete(localeIdToDelete, successIds, exception, responseModel, baseEntityDTO);
    
    IGetNumberOfVersionsToMaintainResponseModel configDetails = getConfigDetails(baseEntityDTO);
    rdbmsComponentUtils.createNewRevision(baseEntityDTO, configDetails.getNumberOfVersionsToMaintain());

    return (R) responseModel;
  }

  private IGetNumberOfVersionsToMaintainResponseModel getConfigDetails(IBaseEntityDTO baseEntityDTO) throws RDBMSException, Exception
  {
    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier().getCode());
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    multiclassificationRequestModel.setKlassIds(types);
    IGetNumberOfVersionsToMaintainResponseModel configDetails = getConfigDetailsForDeleteTranslationStrategy
        .execute(multiclassificationRequestModel);
    return configDetails;
  }
  
  private void deleteNotificationFromChildLanguage(String localeIdToDelete, long baseEntityIID, ICouplingDAO couplingDAO)
      throws RDBMSException, Exception, CSFormatException
  {
    ConfigurationDAO configurationDAO = ConfigurationDAO.instance();
    Long localeIID = configurationDAO.getLanguageConfig(localeIdToDelete).getLanguageIID();
    List<ICouplingDTO> allTargetCouplingDTOs = couplingDAO.getAllTargetLocaleIIDs(baseEntityIID, localeIID, RecordStatus.COUPLED);
    
    IBaseEntityDAO sourceBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, localeIdToDelete);
    for (ICouplingDTO targetCouplingDTO : allTargetCouplingDTOs) {
      
      IPropertyDTO propertyDTO = configurationDAO.getPropertyByIID(targetCouplingDTO.getPropertyIID());
      IBaseEntityDTO sourceBaseEntityDTO = sourceBaseEntityDAO.loadPropertyRecords(propertyDTO);
      IValueRecordDTO sourceValueRecord = (IValueRecordDTO) sourceBaseEntityDTO.getPropertyRecord(propertyDTO.getPropertyIID());
      String targetLocaleCode = configurationDAO.getLanguageConfigByLanguageIID(targetCouplingDTO.getLocaleIID()).getLanguageCode();
      
      IBaseEntityDAO targetBaseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, targetLocaleCode);
      IValueRecordDTO targetValueRecord = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, sourceValueRecord.getValue())
          .localeID(targetLocaleCode).build();
      targetBaseEntityDAO.createPropertyRecords(targetValueRecord);
      couplingDAO.deleteCoupledRecordByLocaleIIDAndPropertyIID(targetCouplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO());
    }

    ICouplingDTO couplingDTO = rdbmsComponentUtils.getLocaleCatlogDAO().newCouplingDTOBuilder().build();
    couplingDTO.setSourceEntityIID(baseEntityIID);
    couplingDTO.setTargetEntityIID(baseEntityIID);
    couplingDTO.setLocaleIID(localeIID);
    couplingDTO.setCouplingSourceIID(localeIID);
    couplingDTO.setCouplingSourceType(ICSECoupling.CouplingType.LANG_INHERITANCE);

    couplingDAO.deleteCoupledRecordByLocaleIID(couplingDTO, rdbmsComponentUtils.getLocaleCatlogDAO());
    couplingDAO.deleteConflictingValuesByLocaleIIds(couplingDTO);
  }
  
  public void createPropertyRecord(long sourceEntityIID, long targetEntityIID, long propertyIID, long localeIID) throws Exception
  {
    String languageCode = ConfigurationDAO.instance().getLanguageConfigByLanguageIID(localeIID).getLanguageCode();
    IBaseEntityDAO sourceEntityDAO = getBaseEntityDAO(sourceEntityIID, languageCode);
    LocaleCatalogDAO localeCatlogDAO = (LocaleCatalogDAO) rdbmsComponentUtils.getLocaleCatlogDAO();
    IPropertyDTO propertyDTO = localeCatlogDAO.getPropertyByIID(propertyIID);
    IBaseEntityDTO sourceBaseEntityDTO = sourceEntityDAO.loadPropertyRecords(propertyDTO);
    IPropertyRecordDTO propertyRecord = sourceBaseEntityDTO.getPropertyRecord(propertyIID);
    
    IBaseEntityDAO targetBaseEntityDAO = getBaseEntityDAO(targetEntityIID, languageCode);
    if (propertyRecord instanceof IValueRecordDTO) {
      IValueRecordDTO valueRecord = (IValueRecordDTO) propertyRecord;
      
      if(languageCode.equals(null)) {
        IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue()).build();
        targetBaseEntityDAO.createPropertyRecords(newValueRecordDTO);
      }else {
        IValueRecordDTO newValueRecordDTO = targetBaseEntityDAO.newValueRecordDTOBuilder(propertyDTO, valueRecord.getValue())
            .localeID(languageCode)
            .build();
        targetBaseEntityDAO.createPropertyRecords(newValueRecordDTO);
      }
    }
    else {
      ITagsRecordDTO tagRecord = (ITagsRecordDTO) propertyRecord;
      ITagsRecordDTO newTagRecordDTO = targetBaseEntityDAO.newTagsRecordDTOBuilder(propertyDTO).build();
      Set<ITagDTO> newTagsDTO = tagRecord.getTags();
      newTagRecordDTO.setTags(newTagsDTO.toArray(new ITagDTO[newTagsDTO.size()]));
      targetBaseEntityDAO.createPropertyRecords(newTagRecordDTO);
    }
  }
  
  public IBaseEntityDAO getBaseEntityDAO(long entityIID, String localeCode) throws Exception
  {
    
    if (localeCode.equals(null)) {
      return rdbmsComponentUtils.getBaseEntityDAO(entityIID);
    }
    return rdbmsComponentUtils.getBaseEntityDAO(entityIID, localeCode);
  }
  
}
