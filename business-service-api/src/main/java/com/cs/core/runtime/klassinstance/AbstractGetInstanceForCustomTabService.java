package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.ITaxonomyInheritanceDTO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.rdbms.task.idto.ITaskRecordDTO;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflict;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyConflictingSource;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflict;
import com.cs.core.runtime.interactor.entity.datarule.TaxonomyConflictingSource;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.utils.klassinstance.*;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public abstract class AbstractGetInstanceForCustomTabService<P extends IGetInstanceRequestModel, R extends IGetKlassInstanceCustomTabModel>
    extends AbstractRuntimeService<P, R> {
  
  protected abstract IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception;
  
  @Autowired

  protected PermissionUtils               permissionUtils;
  
  @Autowired
  protected TransactionThreadData         controllerThread;
  
  @Autowired
  protected RDBMSComponentUtils           rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                    configUtil;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy postConfigDetailsForRelationshipsStrategy;
  
  @Autowired
  RelationshipInstanceUtil relationshipInstanceUtil;
  
  @Autowired
  protected VariantInstanceUtils          variantInstanceUtils;
  
  @Autowired
  protected KlassInstanceUtils             klassInstanceUtils;
  
  @Override
  protected R executeInternal(P instanceRequestModel) throws Exception
  {
    long starTime1 = System.currentTimeMillis();
    
    List<IBaseEntityDTO> baseEntitiesByIIDs = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getBaseEntitiesByIIDs(Arrays.asList(instanceRequestModel.getId()));
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openBaseEntity(baseEntitiesByIIDs.get(0));
    
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|executeInternal|openBaseEntity| %d ms",
            System.currentTimeMillis() - starTime1);
    IMulticlassificationRequestModel multiclassificationRequestModel = configUtil
        .getConfigRequestModelForCustomTab(instanceRequestModel, baseEntityDAO);
    
    long starTime2 = System.currentTimeMillis();
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(multiclassificationRequestModel);
    if (configDetails.getReferencedPermissions().getGlobalPermission().getCanRead() == false) {
      throw new UserNotHaveReadPermissionForArticle();
    }
    RDBMSLogger.instance()
        .debug("NA|OrientDB|" + this.getClass()
            .getSimpleName() + "|executeInternal|getConfigDetails| %d ms",
            System.currentTimeMillis() - starTime2);
    IGetKlassInstanceCustomTabModel returnModel = executeGetKlassInstance(instanceRequestModel,configDetails, baseEntityDAO);
    returnModel.setConfigDetails(configDetails);
    klassInstanceUtils.fillBranchOfCloneOfLabel(baseEntityDAO.getBaseEntityDTO(), returnModel, configDetails.getLinkedVariantCodes());
    relationshipInstanceUtil.executeGetRelationshipInstance(returnModel, configDetails, baseEntityDAO, rdbmsComponentUtils);
    returnModel = this.propertyLevelTaskinfo(returnModel, instanceRequestModel, configDetails, baseEntityDAO);
    
    fillTaxonomyConflicts(instanceRequestModel, configDetails, returnModel);
    
    permissionUtils.manageKlassInstancePermissions(returnModel);
    returnModel.setGlobalPermission(configDetails.getReferencedPermissions().getGlobalPermission());
    return (R) returnModel;
  }

  private void fillTaxonomyConflicts(P instanceRequestModel, IGetConfigDetailsForCustomTabModel configDetails,
      IGetKlassInstanceCustomTabModel returnModel) throws RDBMSException, Exception
  {
    List<IKlassInstanceRelationshipInstance> natureRelationships = returnModel.getNatureRelationships();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
      
    ITaxonomyInheritanceDTO taxonomyConflict = rdbmsComponentUtils.getTaxonomyInheritanceDTO(Long.parseLong(instanceRequestModel.getId()));
    if(taxonomyConflict != null) {
    ITaxonomyConflict taxonomyConflictingValue = new TaxonomyConflict();
    taxonomyConflictingValue.setIsResolved(taxonomyConflict.getIsResolved());
    List<ITaxonomyConflictingSource> css = new ArrayList<>();
    IPropertyDTO propertyByIID = ConfigurationDAO.instance().getPropertyByIID(taxonomyConflict.getPropertyIID());
    String propertyCode = propertyByIID.getPropertyCode();
    for (IKlassInstanceRelationshipInstance natureRelationship : natureRelationships) {
      String relationshipId = natureRelationship.getRelationshipId();
      if (relationshipId.equals(propertyCode)) {
        ITaxonomyConflictingSource cs = new TaxonomyConflictingSource();
        IGetReferencedNatureRelationshipModel referencedNatureRelationshipModel = referencedNatureRelationships.get(relationshipId);
        taxonomyConflictingValue.setTaxonomyInheritanceSetting(referencedNatureRelationshipModel.getTaxonomyInheritanceSetting());
        cs.setSourceContentId(Long.toString(taxonomyConflict.getSourceEntityIID()));
        cs.setRelationshipId(Long.toString(taxonomyConflict.getPropertyIID()));
        css.add(cs);
        taxonomyConflictingValue.setConflicts(css);
      }
    }
      
      List<ITaxonomyConflict> taxonomyConflictingValues = returnModel.getKlassInstance().getTaxonomyConflictingValues();
      taxonomyConflictingValues.add(taxonomyConflictingValue);
    }
  }

  
  protected IGetKlassInstanceCustomTabModel executeGetKlassInstance(
      IGetInstanceRequestModel instanceRequestModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IGetKlassInstanceCustomTabModel returnModel = new GetKlassInstanceForCustomTabModel();
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    returnModel.setKlassInstance((IContentInstance) klassInstance);
    klassInstanceUtils.getConflictAndCoupleType(baseEntityDAO, returnModel, configDetails);
    returnModel.setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
    
    returnModel.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    fillEntityInformation(baseEntityDAO, returnModel);
    return returnModel;
  }



  //method overriden in asset specific implementation
  protected void fillEntityInformation(IBaseEntityDAO baseEntityDAO, IGetKlassInstanceCustomTabModel returnModel) throws Exception
  {
   
  }

  protected IGetKlassInstanceCustomTabModel propertyLevelTaskinfo(
      IGetKlassInstanceCustomTabModel returnModel, IGetInstanceRequestModel instanceRequestModel,
      IGetConfigDetailsForCustomTabModel configDetails, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    ITaskRecordDAO taskRecordDAO = this.rdbmsComponentUtils.openTaskDAO();
    
    //property level task info
    Set<String> propertyIdsHavingTask = returnModel.getPropertyIdsHavingTask();
    String roleCode = configDetails.getRoleIdOfCurrentUser();
    List<ITaskRecordDTO> taskRecords = taskRecordDAO.getAllTaskByBaseEntityIID(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
        roleCode);
    for (ITaskRecordDTO taskRecord : taskRecords) {
      if(taskRecord.getPropertyIID() != 0) {
        propertyIdsHavingTask.add(RDBMSUtils.getPropertyByIID(taskRecord.getPropertyIID()).getPropertyCode());
      }
    }
    
    // Task Count
    int taskCountOnBaseEntity = taskRecordDAO
        .getTaskCountOnBaseEntity(baseEntityDAO.getBaseEntityDTO()
            .getBaseEntityIID(), roleCode);
    returnModel.setTasksCount(taskCountOnBaseEntity);
    
    return returnModel;
  }
}
