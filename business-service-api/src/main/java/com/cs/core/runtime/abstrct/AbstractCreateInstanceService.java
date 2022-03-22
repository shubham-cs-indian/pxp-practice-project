
package com.cs.core.runtime.abstrct;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.ICreateInstanceModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.eventqueue.idto.IEventDTO;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.coupling.dto.ClassificationDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IClassificationDataTransferDTO;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.instancetree.GoldenRecordUtils;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveCreatePermission;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.*;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.runtime.utils.threadPoolExecutor.ThreadPoolExecutorUtil;
import com.cs.core.runtime.variant.articleinstance.ICreateArticleVariantInstanceService;
import com.cs.core.util.ConfigUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public abstract class AbstractCreateInstanceService<P extends ICreateInstanceModel, R extends IKlassInstanceInformationModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected PermissionUtils                             permissionUtils;
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;

  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected ConfigUtil                                  configUtil;
  
  @Autowired
  protected ThreadPoolTaskExecutor                      taskExecutor;
  
  @Autowired
  ICreateArticleVariantInstanceService                  createArticleVariantInstanceService;
  
  @Autowired
  ThreadPoolExecutorUtil                                threadPoolTaskExecutorUtil;
  
  @Autowired
  protected GoldenRecordUtils                           goldenRecordUtils;
  
  private static final String                           SERVICE                        = "CLASSIFICATION_DATA_TRANSFER";
  
  protected abstract Long getCounter();
  
  protected abstract String getModuleEntityType();
  
  protected abstract BaseType getBaseType();
  
  @Autowired
  TransactionThreadData transactionThreadData;
  
  @SuppressWarnings("unchecked")
  protected R executeInternal(P model) throws Exception
  {
    Boolean canCreate = model.getHasCreatePermission();
    if (canCreate == null) {
      long starTime = System.currentTimeMillis();
      canCreate = permissionUtils.isUserHasPermissionToCreate(model.getType(), "klass",
          getModuleEntityType());
      RDBMSLogger.instance()
          .debug("NA|OrientDB|" + this.getClass()
              .getSimpleName() + "|executeInternal|permissionUtils| %d ms",
              System.currentTimeMillis() - starTime);
    }
    if (!canCreate) {
      throw new UserNotHaveCreatePermission();
    }
    IMulticlassificationRequestModel multiclassificationRequestModel = this.configUtil
        .getConfigRequestModelForCreateInstance(model);
    
    long starTime = System.currentTimeMillis();
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);
    RDBMSLogger.instance()
        .debug("NA|OrientDB|" + this.getClass()
            .getSimpleName() + "|executeInternal|getConfigDetailsWithoutPermissionsStrategy| %d ms",
            System.currentTimeMillis() - starTime);
    
    String newInstanceName = model.getName();
    
    if (newInstanceName == null) {
      newInstanceName = klassInstanceUtils.getDefaultInstanceNameByConfigdetails(configDetails,
          model.getType());
      Long counter = getCounter();
      String languageCode = rdbmsComponentUtils.getDataLanguage();
      newInstanceName = newInstanceName + " " + counter+"_"+ languageCode;
      model.setName(newInstanceName);
    }
    IBaseEntityDTO baseEntity = createBaseEntity(configDetails, model);

    IKlassInstanceInformationModel klassInstanceInformationModel = fillklassInstanceInformationModel(baseEntity);
    
    ILocaleCatalogDAO localeCatlogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    localeCatlogDAO.loadLocaleIds(baseEntity);
   
    rdbmsComponentUtils.createNewRevision(baseEntity, configDetails.getNumberOfVersionsToMaintain());

    localeCatlogDAO.postUsecaseUpdate(baseEntity.getBaseEntityIID(), IEventDTO.EventType.ELASTIC_UPDATE);
    
    initiateClassificationDataTransfer(localeCatlogDAO, baseEntity.getBaseEntityIID(), baseEntity.getNatureClassifier());
    
    goldenRecordUtils.initiateEvaluateGoldenRecordBucket(baseEntity);
    
    List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableModel = configDetails
        .getTechnicalImageVariantContextWithAutoCreateEnable();
    String userName = rdbmsComponentUtils.getUserName();
    VariantInstanceUtils.createEmbeddedVariantsWithAutoCreateEnabled(newInstanceName, baseEntity,
        klassInstanceInformationModel, contextsWithAutoCreateEnableModel, transactionThreadData, userName);
    
    return (R) klassInstanceInformationModel;
  }
  
  protected IBaseEntityDTO createBaseEntity(IGetConfigDetailsForCustomTabModel configDetails,
      ICreateInstanceModel createInstanceModel) throws Exception
  {
    /*Product id*/
    String baseEntityID = createInstanceModel.getId();
    if ( baseEntityID == null ) {
      baseEntityID =  RDBMSUtils.newUniqueID( this.getBaseType().getPrefix());
    }
    
    IReferencedKlassDetailStrategyModel referencedNatureKlassDetail = configDetails
        .getReferencedKlasses()
        .get(createInstanceModel.getType());
    
/*    IClassifierDTO classifierDTO = new ClassifierDTO(referencedNatureKlassDetail.getClassifierIID(),
        referencedNatureKlassDetail.getCode(), ClassifierType.CLASS);*/
    IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByCode(createInstanceModel.getType());

    long starTime1 = System.currentTimeMillis();
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.newBaseEntityDTO(0, baseEntityID, this.getBaseType(),
        classifierDTO);
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|createBaseEntity|newBaseEntityDTO| %d ms",
            System.currentTimeMillis() - starTime1);
    
    long starTime2 = System.currentTimeMillis();
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|createBaseEntity|openBaseEntity| %d ms",
            System.currentTimeMillis() - starTime2);
    
    IPropertyRecordDTO[] propertyRecords = createPropertyRecordInstance(baseEntityDAO,
        configDetails, createInstanceModel);
    
    long starTime3 = System.currentTimeMillis();
    fillEntityExtensionForCreation(baseEntityDTO);
    IBaseEntityDTO baseEntityToReturn = baseEntityDAO.createPropertyRecords(propertyRecords);
    RDBMSLogger.instance()
        .debug("NA|RDBMS|" + this.getClass()
            .getSimpleName() + "|createBaseEntity|createPropertyRecords| %d ms",
            System.currentTimeMillis() - starTime3);
    
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(baseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO(), false,
        configDetails.getReferencedElements(), configDetails.getReferencedAttributes(), configDetails.getReferencedTags());
    
    return baseEntityToReturn;
  }

  //MEthod overrriden in Target implementation. Override if you want to fill extension data.
  protected void fillEntityExtensionForCreation(IBaseEntityDTO baseEntityDTO) throws Exception
  {
    
  }

  protected IPropertyRecordDTO[] createPropertyRecordInstance(IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails, ICreateInstanceModel createInstanceModel)
      throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT, rdbmsComponentUtils.getLocaleCatlogDAO());
    // Create Attribute
    List<IPropertyRecordDTO> propertyRecords = this.createAttributePropertyRecordInstance(
        propertyRecordBuilder, configDetails, createInstanceModel);
    // Create tag
    propertyRecords.addAll(this.createTagPropertyRecordInstance(propertyRecordBuilder,
        configDetails, createInstanceModel));
    return propertyRecords.toArray(new IPropertyRecordDTO[propertyRecords.size()]);
  }
  
  protected List<IPropertyRecordDTO> createAttributePropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails,
      ICreateInstanceModel createInstanceModel) throws Exception
  {
    List<IPropertyRecordDTO> attributePropertyRecords = new ArrayList<>();
    configDetails.getReferencedAttributes()
        .values()
        .forEach(referencedAttribute -> {
          try {
            IPropertyRecordDTO dto = propertyRecordBuilder.createValueRecord(referencedAttribute);
            if (dto != null) {
              attributePropertyRecords.add(dto);
            }
          }
          catch (Exception e) {
            new RuntimeException(e);
          }
        });
    addMandatoryAttribute(attributePropertyRecords, propertyRecordBuilder, configDetails,
        createInstanceModel);
    return attributePropertyRecords;
  }
  
  protected List<IPropertyRecordDTO> createTagPropertyRecordInstance(
      PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails,
      ICreateInstanceModel createInstanceModel) throws Exception
  {
    List<IPropertyRecordDTO> tagPropertyRecords = new ArrayList<>();
    configDetails.getReferencedTags()
        .values()
        .forEach(referencedTags -> {
          try {
            IPropertyRecordDTO dto = propertyRecordBuilder.createTagsRecord(referencedTags);
            if (dto != null) {
              tagPropertyRecords.add(dto);
            }
          }
          catch (Exception e) {
            new RuntimeException(e);
          }
        });
    return tagPropertyRecords;
  }
  
  public IKlassInstanceInformationModel fillklassInstanceInformationModel(
      IBaseEntityDTO baseEntityDTO) throws Exception
  {
    IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder.getKlassInstanceInformationModel(baseEntityDTO, rdbmsComponentUtils);
    return klassInstanceInformationModel;
  }
  
  protected void addMandatoryAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO,
      PropertyRecordBuilder propertyRecordBuilder, IGetConfigDetailsForCustomTabModel configDetails,
      ICreateInstanceModel createInstanceModel) throws Exception
  {
    addNameAttribute(listOfPropertyRecordsDTO, propertyRecordBuilder,
        createInstanceModel.getName());
  }
  
  protected void addNameAttribute(List<IPropertyRecordDTO> listOfPropertyRecordsDTO,
      PropertyRecordBuilder propertyRecordBuilder, String name) throws Exception
  {
    IPropertyRecordDTO nameAttribute = propertyRecordBuilder.createStandardNameAttribute(name);
    listOfPropertyRecordsDTO.add(nameAttribute);
  }

  private void initiateClassificationDataTransfer(ILocaleCatalogDAO localeCatlogDAO, Long baseEntityIID, IClassifierDTO classifierDTO) throws Exception
  {
    IClassificationDataTransferDTO classificationDataTransfer = new ClassificationDataTransferDTO();
    ILocaleCatalogDTO localeCatalogDTO = localeCatlogDAO.getLocaleCatalogDTO();
    classificationDataTransfer.setLocaleID(localeCatalogDTO.getLocaleID());
    classificationDataTransfer.setCatalogCode(localeCatalogDTO.getCatalogCode());
    classificationDataTransfer.setOrganizationCode(localeCatalogDTO.getOrganizationCode());
    classificationDataTransfer.setUserId(context.getUserId());
    classificationDataTransfer.setBaseEntityIID(baseEntityIID);
    classificationDataTransfer.getAddedOtherClassifiers().add(classifierDTO);

    BGPDriverDAO.instance().submitBGPProcess(context.getUserName(), SERVICE, "", BGPPriority.HIGH,
        new JSONContent(classificationDataTransfer.toJSON()));
  }

}
