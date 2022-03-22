package com.cs.core.runtime.abstrct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.strategy.usecase.task.IGetConfigDetailsForTasksTabStrategy;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.KlassInstanceRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.model.attribute.IModifiedContentAttributeInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IModifiedContentTagInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceSaveModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.taskinstance.GetTaskInstanceResponseModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForTasksTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceModel;
import com.cs.core.runtime.interactor.model.version.GetKlassInstanceVersionForTimeLineModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForCustomTabStrategy;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RelationshipInstanceUtil;
import com.cs.core.runtime.interactor.utils.klassinstance.TranslationInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.core.util.VersionUtils;

@Service
public abstract class AbstractCreateTranslatableInstanceService<P extends IKlassInstanceSaveModel, R extends IGetKlassInstanceModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForTasksTabStrategy        getConfigDetailsForTasksTabStrategy;
  
  @Autowired
  protected PermissionUtils                             permissionUtils;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  @Autowired
  protected IGetConfigDetailsForCustomTabStrategy       getConfigDetailsForCustomTabStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  RelationshipInstanceUtil                              relationshipInstanceUtil;
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  @Autowired
  protected TransactionThreadData                       transactionThread;
  
  @Autowired
  protected TranslationInstanceUtils                    translationInstanceUtils;
   
  @Autowired
  protected ConfigUtil                                  configUtil;

  @Autowired
  protected VariantInstanceUtils                        variantInstanceUtils;
  
  @Autowired
  protected VersionUtils                                versionUtils;
  
  protected abstract Long getCounter();
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P klassInstancesModel) throws Exception
  {
    Long counter = klassInstancesModel.getName() == null? getCounter() : null;
    IMulticlassificationRequestModel multiclassificationRequestModel = translationInstanceUtils.handleTranslationCreation(klassInstancesModel, counter);
    return (R) prepareResponseAndCreateRevision(klassInstancesModel, multiclassificationRequestModel);
  }

  private IGetKlassInstanceModel prepareResponseAndCreateRevision(P klassInstancesModel, IMulticlassificationRequestModel multiclassificationRequestModel)
      throws Exception, RDBMSException
  {

    long starTime2 = System.currentTimeMillis();
    IGetConfigDetailsModel configDetails = getConfigDetails(multiclassificationRequestModel, klassInstancesModel);
    RDBMSLogger.instance().debug("NA|OrientDB|" + this.getClass().getSimpleName() + "|executeInternal|Config Details after save| %d ms",
        System.currentTimeMillis() - starTime2);
    
    long baseEntityIID = Long.parseLong(klassInstancesModel.getId());
    List<String> languageInheritance = configUtil.getLanguageInheritance();
    IBaseEntityDAO baseEntityDAO = this.rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID, languageInheritance);

    IGetKlassInstanceModel returnModel = prepareDataForResponse(klassInstancesModel, configDetails,
        rdbmsComponentUtils.getBaseEntityDAO(baseEntityIID));
    
    rdbmsComponentUtils.createNewRevision(baseEntityDAO.getBaseEntityDTO(), configDetails.getNumberOfVersionsToMaintain());
    rdbmsComponentUtils.getLocaleCatlogDAO().postUsecaseUpdate(baseEntityIID, IEventDTO.EventType.ELASTIC_UPDATE);
    return returnModel;
  }

  protected IGetKlassInstanceModel prepareDataForResponse(IKlassInstanceSaveModel klassInstanceSaveModel,
      IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetKlassInstanceModel returnModel = executeGetKlassInstance(configDetails, klassInstanceSaveModel, baseEntityDAO);
    
    List<String> linkedVariantCodes = new ArrayList<String>();
    if (klassInstanceSaveModel.getTabType().equals(CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE)) {
      linkedVariantCodes = ((IGetConfigDetailsForTasksTabModel) configDetails).getLinkedVariantCodes();
    }
    else
      linkedVariantCodes = ((IGetConfigDetailsForCustomTabModel) configDetails).getLinkedVariantCodes();
    
    returnModel.setVariantOfLabel(klassInstanceUtils.getVariantOfLabel(baseEntityDAO, linkedVariantCodes));
    permissionUtils.manageKlassInstancePermissions(returnModel);
    return returnModel;
  }
  
  protected IGetKlassInstanceModel executeGetKlassInstance(IGetConfigDetailsModel configDetails, IKlassInstanceSaveModel saveInstanceModel,
      IBaseEntityDAO baseEntityDAO) throws Exception
  {
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, this.rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    IGetKlassInstanceModel model = null;
    switch (saveInstanceModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        model = new GetKlassInstanceForCustomTabModel();
        ((IGetKlassInstanceCustomTabModel) model).setAttributeVariantsStats(klassInstanceBuilder.getAttributeVariantsStats());
        if (!((IGetConfigDetailsForCustomTabModel) configDetails).getReferencedNatureRelationships().isEmpty()) {
          setNatureRelationship((IGetConfigDetailsForCustomTabModel) configDetails, (IGetKlassInstanceCustomTabModel) model);
        }
        relationshipInstanceUtil.executeGetRelationshipInstance((IGetKlassInstanceCustomTabModel) model,
            (IGetConfigDetailsForCustomTabModel) configDetails, baseEntityDAO, this.rdbmsComponentUtils);
        break;
      case CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE:
        model = new GetTaskInstanceResponseModel();
        break;
      case CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE:
        model = new GetKlassInstanceVersionForTimeLineModel();
        ((IGetKlassInstanceVersionsForTimeLineModel) model).getVersions()
            .addAll(versionUtils.getObjectRevisions(baseEntityDAO.getBaseEntityDTO().getBaseEntityIID(),
                ((IGetConfigDetailsForCustomTabModel) configDetails).getNumberOfVersionsToMaintain(), 0, false));
        break;
      default:
        break;
    }
    
    // on any tab we will get the context information
    model.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    
    model.setKlassInstance((IContentInstance) klassInstance);
    model.setConfigDetails(configDetails);
    return model;
  }
  
  public void setNatureRelationship(IGetConfigDetailsForCustomTabModel configDetails, IGetKlassInstanceCustomTabModel returnModel)
      throws RDBMSException, Exception
  {
    IKlassInstanceRelationshipInstance klassInstanceRelationshipInstance = new KlassInstanceRelationshipInstance();
    Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships = configDetails.getReferencedNatureRelationships();
    
    Set<Entry<String, IGetReferencedNatureRelationshipModel>> natureRelationshipsEntrySet = referencedNatureRelationships.entrySet();
    for (Entry<String, IGetReferencedNatureRelationshipModel> natureRelationshipEntrySet : natureRelationshipsEntrySet) {
      IGetReferencedNatureRelationshipModel natureRelationship = natureRelationshipEntrySet.getValue();
      klassInstanceRelationshipInstance.setRelationshipId(natureRelationship.getId());
      klassInstanceRelationshipInstance.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix()));
      klassInstanceRelationshipInstance.setSideId(natureRelationship.getSide1().getElementId());
      returnModel.getNatureRelationships().add(klassInstanceRelationshipInstance);
    }
  }
  
  //This method is overridden by CreateTranslatableAssetInstanceService.
  public IGetConfigDetailsModel getConfigDetails(IMulticlassificationRequestModel model, IKlassInstanceSaveModel saveModel)
      throws Exception
  {
    switch (saveModel.getTabType()) {
      case CommonConstants.CUSTOM_TEMPLATE_TAB_BASETYPE:
        return getConfigDetailsForCustomTabStrategy.execute(model);
      case CommonConstants.TEMPLATE_TASKS_TAB_BASETYPE:
        return getConfigDetailsForTasksTabStrategy.execute(model);
      case CommonConstants.TEMPLATE_TIME_LINE_TAB_BASETYPE:
        return getConfigDetailsForCustomTabStrategy.execute(model);
      default:
        return null;
    }
  }
  
  private boolean shouldCreateRevision(IKlassInstanceSaveModel klassInstancesModel, IGetConfigDetailsForCustomTabModel configDetails)
  {
    List<String> versionableAttributes = configDetails.getVersionableAttributes();
    List<IModifiedContentAttributeInstanceModel> modifiedAttributes = klassInstancesModel.getModifiedAttributes();
    for (IModifiedContentAttributeInstanceModel modifiedAttribute : modifiedAttributes) {
      if (versionableAttributes.contains(modifiedAttribute.getAttributeId())) {
        return true;
      }
    }
    
    List<String> versionableTags = configDetails.getVersionableTags();
    List<ITagInstance> addedTags = klassInstancesModel.getAddedTags();
    for (ITagInstance addedTag : addedTags) {
      if (versionableTags.contains(addedTag.getTagId())) {
        return true;
      }
    }
    
    List<IModifiedContentTagInstanceModel> modifiedTags = klassInstancesModel.getModifiedTags();
    for (IModifiedContentTagInstanceModel modifiedTag : modifiedTags) {
      if (versionableTags.contains(modifiedTag.getTagId())) {
        return true;
      }
    }
    return false;
  }
  
  protected IGetConfigDetailsForCustomTabModel getConfigDetailsForSave(IMulticlassificationRequestModel multiclassificationRequestModel)
      throws Exception
  {
    return getConfigDetailsWithoutPermissionsStrategy.execute(multiclassificationRequestModel);
  }
  
  public IKlassInstanceInformationModel fillklassInstanceInformationModel(IBaseEntityDTO baseEntityDTO) throws Exception
  {
    IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO,
        rdbmsComponentUtils);
    return klassInstanceInformationModel;
  }
  
}
