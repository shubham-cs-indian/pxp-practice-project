package com.cs.core.bgprocess.services.workflowtrigger;

import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.dto.BaseEntityPlanDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.services.data.AbstractBaseEntityProcessing;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.SuperType;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.transactionend.handlers.dto.DependencyDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO;
import com.cs.core.transactionend.handlers.dto.IDependencyChangeDTO.Change;
import com.cs.core.transactionend.handlers.dto.IDependencyDTO;
import com.cs.di.workflow.trigger.standard.BusinessProcessTriggerModel;
import com.cs.di.workflow.trigger.standard.BusinessProcessTriggerService;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.ActionSubTypes;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerModel.BusinessProcessActionType;
import com.cs.di.workflow.trigger.standard.IBusinessProcessTriggerService;
import com.cs.utils.BaseEntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class PopulateWorkflowInfo extends AbstractBaseEntityProcessing  {

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
    for (Long batchIID : batchIIDs) {

      IBaseEntityDTO entityByIID = localeCatalogDAO.getEntityByIID(batchIID);
      IBusinessProcessTriggerModel businessProcessTriggerModel = fillBusinessProcessModel(batchIID, entityByIID);

      IBusinessProcessTriggerService businessProcessEvent = getBean(BusinessProcessTriggerService.class);
      businessProcessEvent.triggerQualifyingWorkflows(businessProcessTriggerModel);
    }
  }

  private IBusinessProcessTriggerModel fillBusinessProcessModel(Long batchIID, IBaseEntityDTO entityByIID) throws RDBMSException
  {
    IBusinessProcessTriggerModel businessProcessTriggerModel = new BusinessProcessTriggerModel();

    businessProcessTriggerModel.setBaseType(BaseEntityUtils.getBaseTypeString(entityByIID.getBaseType()));
    businessProcessTriggerModel.setActionSubType(ActionSubTypes.AFTER_PROPERTIES_SAVE);
    IDependencyChangeDTO changes = dependencyDTO.getDependencies().get(batchIID);
    if(changes.isCreated()){
      businessProcessTriggerModel.setBusinessProcessActionType(BusinessProcessActionType.AFTER_CREATE);
    } else {
      businessProcessTriggerModel.setBusinessProcessActionType(BusinessProcessActionType.AFTER_SAVE);
    }

    fillClassifiers(entityByIID, businessProcessTriggerModel);
    fillProperties(businessProcessTriggerModel, changes);
    return businessProcessTriggerModel;
  }

  private void fillClassifiers(IBaseEntityDTO entityByIID, IBusinessProcessTriggerModel businessProcessTriggerModel)
  {
    Map<Boolean, List<String>> classifiers = BaseEntityUtils.seggregateTaxonomyAndClasses(entityByIID.getOtherClassifiers());
    List<String> klassIds = classifiers.get(true);
    klassIds.add(entityByIID.getNatureClassifier().getClassifierCode());
    businessProcessTriggerModel.setKlassIds(klassIds);
    businessProcessTriggerModel.setTaxonomyIds(classifiers.get(false));
  }

  private void fillProperties(IBusinessProcessTriggerModel businessProcessTriggerModel, IDependencyChangeDTO changes) throws RDBMSException
  {

     List<String> attributes = new ArrayList<>();
     List<String> tags = new ArrayList<>();

    for (Entry<String, Change> change : changes.getPropertiesChange().entrySet()) {
      String key = change.getKey();
      Change value = change.getValue();
      IPropertyDTO properties = ConfigurationDAO.instance().getPropertyByCode(key);

      if (properties.getSuperType().equals(SuperType.TAGS)) {
        tags.add(key);
      }
      else {
        attributes.add(key);
      }
    }

    if (tags != null) {
      businessProcessTriggerModel.setTagIds(tags);
    }
    if (attributes != null) {
      businessProcessTriggerModel.setAttributeIds(attributes);
    }
  }
}
