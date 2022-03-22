package com.cs.core.bgprocess.services.data;

import com.cs.bds.config.usecase.taxonomy.ITypeSwitchInstance;
import com.cs.core.bgprocess.BGProcessApplication;
import com.cs.core.bgprocess.dto.BaseEntityBulkUpdateDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.eventqueue.idto.IEventDTO.EventType;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeSwitchModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeSwitchModel;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BaseEntityTypeSwitch extends AbstractBaseEntityProcessing{

  private final BaseEntityBulkUpdateDTO bulkUpdateDTO = new BaseEntityBulkUpdateDTO();
  
  
  @Override
  protected void runBaseEntityBatch(Set<Long> baseEntityIIDs)
      throws RDBMSException, CSFormatException, CSInitializationException, PluginException
  {
    Set<IClassifierDTO> addedClassifiers = bulkUpdateDTO.getAddedClassifiers();
    Set<IClassifierDTO> removedClassifiers = bulkUpdateDTO.getRemovedClassifiers();
    
    if (!addedClassifiers.isEmpty() || !removedClassifiers.isEmpty()) {
      List<String> addedSecondaryTypes = new ArrayList<String>();
      List<String> deletedSecondaryTypes = new ArrayList<String>();
      List<String> addedTaxonomyIds = new ArrayList<String>();
      List<String> deletedTaxonomyIds = new ArrayList<String>();
      
      IKlassInstanceTypeSwitchModel typeSwitchModel = new KlassInstanceTypeSwitchModel();
      
      addedClassifiers.forEach(classifier -> {
        ClassifierType classifierType = classifier.getClassifierType();
        if (classifierType.equals(ClassifierType.CLASS)) {
          addedSecondaryTypes.add(classifier.getCode());
        }
        else {
          addedTaxonomyIds.add(classifier.getCode());
        }
      });
      
      removedClassifiers.forEach(classifier -> {
        ClassifierType classifierType = classifier.getClassifierType();
        if (classifierType.equals(ClassifierType.CLASS)) {
          deletedSecondaryTypes.add(classifier.getCode());
        }
        else {
          deletedTaxonomyIds.add(classifier.getCode());
        }
      });
      
      typeSwitchModel.setKlassInstanceId(String.valueOf(baseEntityIIDs.iterator().next()));
      typeSwitchModel.setAddedSecondaryTypes(addedSecondaryTypes);
      typeSwitchModel.setAddedTaxonomyIds(addedTaxonomyIds);
      typeSwitchModel.setDeletedSecondaryTypes(deletedSecondaryTypes);
      typeSwitchModel.setDeletedTaxonomyIds(deletedTaxonomyIds);
      
      try {
        ITypeSwitchInstance typeSwitchInstance = BGProcessApplication.getApplicationContext().getBean(ITypeSwitchInstance.class);
        typeSwitchInstance.execute(typeSwitchModel);
        
        ILocaleCatalogDAO localeCatalogDAO = openUserSession().openLocaleCatalog(userSession,
            new LocaleCatalogDTO(bulkUpdateDTO.getLocaleID(), bulkUpdateDTO.getCatalogCode(), ""));
        localeCatalogDAO.postUsecaseUpdate(Long.parseLong(typeSwitchModel.getKlassInstanceId()), EventType.ELASTIC_UPDATE);
      }
      catch (Exception e) {
        e.printStackTrace();
        RDBMSLogger.instance().exception(e);
      }
    }
  }
  
  @Override
  public void initBeforeStart(IBGProcessDTO initialProcessData, IUserSessionDTO userSession)
          throws CSInitializationException, CSFormatException, RDBMSException {
    super.initBeforeStart(initialProcessData, userSession);
    bulkUpdateDTO.fromJSON(jobData.getEntryData().toString());
  }
  
}