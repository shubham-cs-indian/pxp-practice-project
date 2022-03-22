package com.cs.core.runtime.klassinstance;

import com.cs.config.standard.IConfigMap;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.language.IGetAllDataLanguagesInfoModel;
import com.cs.core.config.interactor.model.language.IGetLanguagesInfoModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.task.idao.ITaskRecordDAO;
import com.cs.core.runtime.interactor.entity.klassinstance.IAssetInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.instance.IGetInstanceRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.languageinstance.GetAllDataLanguagesModel;
import com.cs.core.runtime.interactor.model.languageinstance.IGetAllDataLanguagesModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.version.GetKlassInstanceVersionForTimeLineModel;
import com.cs.core.runtime.interactor.model.version.IGetKlassInstanceVersionsForTimeLineModel;
import com.cs.core.runtime.interactor.model.version.IKlassInstanceVersionModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetAllDataLanguageInfoStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.ijosn.IJSONContent;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.core.util.VersionUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@SuppressWarnings("unchecked")
public abstract class AbstractGetKlassInstanceForVersionTabService<P extends IGetInstanceRequestModel, R extends IGetKlassInstanceVersionsForTimeLineModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected PermissionUtils                 permissionUtils;
  
  @Autowired
  protected TransactionThreadData           controllerThread;
  
  @Autowired
  protected RDBMSComponentUtils             rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                      configUtil;
  
  @Autowired
  protected VersionUtils                    versionUtils;
  
  @Autowired
  protected IGetAllDataLanguageInfoStrategy getAllDataLanguageInfoStrategy;
  
  @Autowired
  protected VariantInstanceUtils                    variantInstanceUtils;
  
  @Autowired
  protected KlassInstanceUtils                      klassInstanceUtils;
  
  protected abstract IGetConfigDetailsForCustomTabModel getConfigDetails(
      IMulticlassificationRequestModel idParameterModel) throws Exception;
  
  protected abstract String getBaseType();
  
  protected abstract Boolean getIsArchive() throws Exception;
  
  @Override
  protected R executeInternal(P getInstanceRequestModel) throws Exception
  {
    ILocaleCatalogDAO localeCatlogDAO = this.rdbmsComponentUtils.getLocaleCatlogDAO();
    Long baseEntityIID = Long.parseLong(getInstanceRequestModel.getId());
    IBaseEntityDTO baseEntityDTO = localeCatlogDAO
        .getBaseEntitiesByIIDs(List.of(getInstanceRequestModel.getId()))
        .get(0);
    IBaseEntityDAO baseEntityDAO = localeCatlogDAO.openBaseEntity(baseEntityDTO);
    
    IMulticlassificationRequestModel multiclassificationRequestModel = configUtil.getConfigRequestModelForTimeline(getInstanceRequestModel, baseEntityDAO);
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetails(multiclassificationRequestModel);
    IGetKlassInstanceVersionsForTimeLineModel returnModel = new GetKlassInstanceVersionForTimeLineModel();
    
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails, rdbmsComponentUtils);
    IKlassInstance klassInstance = klassInstanceBuilder.getKlassInstance();
    // for asset instance we have to create asset coverflow attribute instance from entity extension
    if (baseEntityDAO.getBaseEntityDTO().getBaseType().equals(BaseType.ASSET)) {
      IJSONContent entityExtension = baseEntityDAO.getBaseEntityDTO()
          .getEntityExtension();
      String entityExtensionAsString = entityExtension.toString();
      IAssetInformationModel assetInformation = ObjectMapperUtil.readValue(entityExtensionAsString,
          AssetInformationModel.class);
      ((IAssetInstance) klassInstance).setAssetInformation(assetInformation);
    }
    klassInstance.setVersionId(-1l);
    returnModel.setKlassInstance((IContentInstance) klassInstance);
    returnModel.setConfigDetails(configDetails);
    int versionNumber = getIsArchive() ? CSProperties.instance().getInt("shouldMaintainArchive")
        : configDetails.getNumberOfVersionsToMaintain();
    List<IKlassInstanceVersionModel> objectRevisions = versionUtils.getObjectRevisions(baseEntityIID, versionNumber,
        getInstanceRequestModel.getFrom(), getIsArchive());      
    
    returnModel.setVersions(objectRevisions);
    fillTaskInfo(baseEntityIID, returnModel);
    permissionUtils.manageKlassInstancePermissions(returnModel);
    klassInstanceUtils.fillBranchOfCloneOfLabel(baseEntityDAO.getBaseEntityDTO(), returnModel, configDetails.getLinkedVariantCodes());

    Set<String> langCodes = new HashSet<String>();
    // get all langCodes available for timeline 
    for(IKlassInstanceVersionModel klassInstanceVersionModel : objectRevisions) {
      langCodes.addAll(klassInstanceVersionModel.getSummary().getDependentAttributeIdsCountMap().keySet());
    }
    
    IGetAllDataLanguagesModel dataLanguagesModel = new GetAllDataLanguagesModel();
    dataLanguagesModel.setLanguageCodes(Lists.newArrayList(langCodes));
    // fetch all language information from ODB for langCodes 
    IGetAllDataLanguagesInfoModel allLangaugesInfoModel = getAllDataLanguageInfoStrategy.execute(dataLanguagesModel);
    Map<String, IGetLanguagesInfoModel> referencedLanguages = returnModel.getConfigDetails().getReferencedLanguages();
    referencedLanguages.putAll(allLangaugesInfoModel.getReferencedLanguages());
    
    //Get Context Info on Timeline Tab
    returnModel.setReferencedInstances(variantInstanceUtils.fillContextualData(configDetails, baseEntityDAO, klassInstance));
    return (R) returnModel;
  }

  private void fillTaskInfo(long baseEntityIID,
      IGetKlassInstanceVersionsForTimeLineModel returnModel) throws Exception, RDBMSException
  {
    // Task Count
    ITaskRecordDAO taskRecordDAO = rdbmsComponentUtils.openTaskDAO();
    int taskCountOnBaseEntity = taskRecordDAO.getTaskCountOnBaseEntity(baseEntityIID, rdbmsComponentUtils.getUserID());
    returnModel.setTasksCount(taskCountOnBaseEntity);
  }

  protected IKlassInstanceTypeModel getKlassInstanceType(IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    KlassInstanceTypeModel klassInstanceTypeModel = new KlassInstanceTypeModel();
    
    List<String> types = new ArrayList<>();
    types.add(baseEntityDTO.getNatureClassifier()
        .getCode());
    klassInstanceTypeModel.setTypes(types);
    
    klassInstanceTypeModel.setName(baseEntityDTO.getBaseEntityName());
    klassInstanceTypeModel
        .setBaseType(IConfigMap.getBaseTypeClassPath(baseEntityDTO.getBaseType()));
    
    List<String> languageCodes = new ArrayList<>();
    languageCodes.add(baseEntityDTO.getBaseLocaleID());
    klassInstanceTypeModel.setLanguageCodes(languageCodes);
    
    return klassInstanceTypeModel;
  }
}
