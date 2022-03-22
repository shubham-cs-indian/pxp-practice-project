package com.cs.dam.runtime.assetinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IImageAttributeInstance;
import com.cs.core.runtime.interactor.model.assetinstance.BulkAssetInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IBulkAssetInstanceInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.ICreateAssetInstanceModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkCreateAssetInstanceModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.*;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.strategy.usecase.asset.IFetchAssetConfigurationDetails;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.dam.runtime.util.AssetInstanceUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

@SuppressWarnings("unchecked")
@Component
public abstract class AbstractCreateBulkAssetInstances <P extends IBulkCreateAssetInstanceModel, R extends IBulkAssetInstanceInformationModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  @Autowired
  protected ISessionContext                             context;
  @Autowired
  protected TransactionThreadData                       transactionThread;
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  @Autowired
  protected IFetchAssetConfigurationDetails             fetchAssetConfigurationDetails;
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  @Autowired
  TransactionThreadData                                 controllerThread;
  
  protected abstract IKlassInstance getKlassInstanceObjectToCreate() throws Exception;
  
  protected abstract Long getCounter();
  
  @Override
  protected R executeInternal(P bulklistModel) throws Exception
  {
    IExceptionModel failure = new ExceptionModel();
    Map<String, IGetConfigDetailsForCustomTabModel> typeIdConfigDetailsMapping = new HashMap<>();
    
    for (ICreateAssetInstanceModel assetInstance : bulklistModel.getAssetInstances()) {
      if (typeIdConfigDetailsMapping.get(assetInstance.getType()) == null) {
        IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
        TransactionData transactionData = transactionThread.getTransactionData();
        multiclassificationRequestModel.setEndpointId(transactionData.getEndpointId());
        multiclassificationRequestModel
            .setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
        multiclassificationRequestModel.setOrganizationId(transactionData.getOrganizationId());
        multiclassificationRequestModel.setKlassIds(Arrays.asList(assetInstance.getType()));
        multiclassificationRequestModel.setUserId(transactionData.getUserId());
        IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
            .execute(multiclassificationRequestModel);
        typeIdConfigDetailsMapping.put(assetInstance.getType(), configDetails);
      }
    }
    
    List<IKlassInstanceInformationModel> success = new ArrayList<>();
    for (ICreateAssetInstanceModel model : bulklistModel.getAssetInstances()) {
      try {
        String klassType = model.getType();
        String newInstanceName = model.getName();
        String id = model.getId();
        BaseType baseType = this.getBaseType();
        if ( id == null ) {
          id = RDBMSUtils.newUniqueID( baseType.getPrefix());
        }
              
        IGetConfigDetailsForCustomTabModel configDetails = typeIdConfigDetailsMapping
            .get(klassType);
        if (newInstanceName == null) {
          newInstanceName = klassInstanceUtils.getDefaultInstanceNameByConfigdetails(configDetails,
              klassType);
          Long counter = getCounter();
          newInstanceName = newInstanceName + " " + counter;
          model.setName(newInstanceName);
        }
        
        IBaseEntityDTO createAssetInstance = createAssetInstance(configDetails, model, id);
        IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils
            .getBaseEntityDAO(createAssetInstance);
            IAssetInformationModel assetInfo = model.getAssetInformation();
            // for attachments upload
            if (assetInfo.getType()
                .equalsIgnoreCase(CommonConstants.SWIFT_CONTAINER_ATTACHMENT)) {
              
              Map<String, Object> imageAttr = new HashMap<>();
              imageAttr.put(IImageAttributeInstance.ASSET_OBJECT_KEY,
                  assetInfo.getAssetObjectKey());
              imageAttr.put(IImageAttributeInstance.FILENAME, assetInfo.getFileName());
              imageAttr.put(IImageAttributeInstance.PROPERTIES, assetInfo.getProperties());
              imageAttr.put(IImageAttributeInstance.THUMB_KEY, assetInfo.getThumbKey());
              imageAttr.put(IImageAttributeInstance.TYPE, assetInfo.getType());
              imageAttr.put(IImageAttributeInstance.PREVIEW_IMAGE_KEY,
                  assetInfo.getPreviewImageKey());
              IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
              try {
                baseEntityDTO.setHashCode(assetInfo.getHash());
                baseEntityDTO.setEntityExtension(ObjectMapperUtil.writeValueAsString(imageAttr));
                baseEntityDAO.updatePropertyRecords(new IPropertyRecordDTO[] {});
              }
              catch (CSFormatException e) {
                RDBMSLogger.instance()
                    .exception(e);
              }
            }
            
            else {
              // image asset attribute
        IImageAttributeInstance imageAttributeInstance = (IImageAttributeInstance) model.getAttributes().get(0);
        AssetInstanceUtils.fillEntityExtension(baseEntityDAO, imageAttributeInstance);
        }
        IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder
            .getKlassInstanceInformationModel(baseEntityDAO.getBaseEntityDTO(), rdbmsComponentUtils);
        success.add(klassInstanceInformationModel);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, model.getId(), null);
      }
    }
    
    IBulkAssetInstanceInformationModel klassInstanceInformationModel = new BulkAssetInstanceInformationModel();
    klassInstanceInformationModel.setSuccess(success);
    klassInstanceInformationModel.setFailure(failure);
    return (R) klassInstanceInformationModel;
  }
  
  private IBaseEntityDTO createAssetInstance(IGetConfigDetailsForCustomTabModel configDetails,
      ICreateAssetInstanceModel createInstanceModel, String baseEntityID)
      throws Exception, RDBMSException
  {
    IReferencedKlassDetailStrategyModel referencedNatureKlassDetail = configDetails
        .getReferencedKlasses()
        .get(createInstanceModel.getType());
    
    IClassifierDTO classifierDTO = new ClassifierDTO(referencedNatureKlassDetail.getClassifierIID(),
        referencedNatureKlassDetail.getCode(), ClassifierType.CLASS);
    
    BaseType baseType = this.getBaseType();
    
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.newBaseEntityDTO(0, baseEntityID, baseType,
        classifierDTO);
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
    
    IPropertyRecordDTO[] propertyRecords = CreateInstanceUtils
        .createPropertyRecordInstance(baseEntityDAO, configDetails, createInstanceModel, rdbmsComponentUtils.getLocaleCatlogDAO());
    
    List<IPropertyRecordDTO> propertyRecordsList = Arrays.asList(propertyRecords);
    List<IPropertyRecordDTO> metaDataAttributes = addMetadataAttributesToAssetInstanceAttributes(
        createInstanceModel.getMetadata(), baseEntityDAO, configDetails);
    
    metaDataAttributes.addAll(propertyRecordsList);
    baseEntityDAO.createPropertyRecords(metaDataAttributes.toArray(new IPropertyRecordDTO[] {}));
    return baseEntityDTO;
  }
  
  protected List<IPropertyRecordDTO> addMetadataAttributesToAssetInstanceAttributes(
      Map<String, Object> metadataMap, IBaseEntityDAO baseEntityDAO,
      IGetConfigDetailsForCustomTabModel configDetails) throws Exception
  {
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.DEFAULT, rdbmsComponentUtils.getLocaleCatlogDAO());
    List<String> metadataAttributeIds = IStandardConfig.StandardProperty.AssetMetaAttributeCodes;
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Set<String> referencedAttributeIds = referencedAttributes.keySet();
    
    if (metadataMap == null) {
      return new ArrayList<IPropertyRecordDTO>();
    }
    
    InputStream stream = CreateBulkAssetInstancesService.class.getClassLoader()
        .getResourceAsStream("metadataPropertyMapping.json");
    Map<String, Object> metadataPropertyMapping = ObjectMapperUtil.readValue(stream,
        new TypeReference<Map<String, Object>>()
        {
          
        });
    
    Map<String, Object> propertyMap = (Map<String, Object>) metadataPropertyMapping
        .get(CommonConstants.PROPERTY_MAP);
    List<String> priorityList = (List<String>) metadataPropertyMapping
        .get(CommonConstants.PRIORITY);
    String localeID = baseEntityDAO.getBaseEntityDTO()
        .getBaseLocaleID();
    
    Map<String, Object> convertedMap = MetadataUtils.convertMetadataIntoMap(metadataMap,
        priorityList);
    
    List<IPropertyRecordDTO> metaDataAttributes = new ArrayList<IPropertyRecordDTO>();
    for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
      String attributeId = entry.getKey();
      Map<String, Object> mapping = (Map<String, Object>) entry.getValue();
      String finalValue = null;
      Double finalValueNumber = null;
      boolean valueFoundInPriority = false;
      for (String metadataKey : priorityList) {
        List<String> keyList = (List<String>) mapping.get(metadataKey);
        Map<String, Object> metadataKeyMap = (Map<String, Object>) convertedMap.get(metadataKey);
        boolean valueFoundInKeyList = false;
        if (metadataKeyMap != null) {
          if (keyList != null) {
            for (String key : keyList) {
              finalValue = (String) metadataKeyMap.get(key);
              if (finalValue != null && !finalValue.equals("")) {
                valueFoundInKeyList = true;
                try {
                  finalValueNumber = Double.parseDouble(finalValue);
                }
                catch (Exception e) {
                  finalValueNumber = null;
                }
                break;
              }
            }
          }
        }
        if (valueFoundInKeyList) {
          valueFoundInPriority = true;
          break;
        }
      }
      
      if (valueFoundInPriority) {
        if (metadataAttributeIds.contains(attributeId)
            && referencedAttributeIds.contains(attributeId)) {
          IAttribute attributeConfig = referencedAttributes.get(attributeId);
          IValueRecordDTO attributeInstance = propertyRecordBuilder.buildValueRecord(0l, 0l,
              finalValue, "", null, null, attributeConfig, PropertyType.ASSET_ATTRIBUTE);
          attributeInstance.setValue(finalValue);
          if (finalValueNumber != null) {
            attributeInstance.setAsNumber(finalValueNumber);
          }
          metaDataAttributes.add(attributeInstance);
        }
      }
    }
    return metaDataAttributes;
  }
  
  protected BaseType getBaseType()
  {
    return BaseType.ASSET;
  }
}
