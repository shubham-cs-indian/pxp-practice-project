package com.cs.core.runtime.interactor.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentModel;
import com.cs.core.config.interactor.model.articleimportcomponent.IComponentParameterModel;
import com.cs.core.config.interactor.model.asset.IMultiPartFileInfoModel;
import com.cs.core.config.interactor.model.asset.MultiPartFileInfoModel;
import com.cs.core.config.interactor.model.component.IWriteInstancesToFileModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointModel;
import com.cs.core.config.interactor.model.klass.IGetKlassModel;
import com.cs.core.config.interactor.model.mapping.GetOutAndInboundMappingModel;
import com.cs.core.config.interactor.model.mapping.IColumnValueTagValueMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleAttributeMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleClassMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTagMappingModel;
import com.cs.core.config.interactor.model.mapping.IConfigRuleTaxonomyMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.ITagValueMappingModel;
import com.cs.core.config.interactor.usecase.endpoint.IGetEndpoint;
import com.cs.core.config.interactor.usecase.mapping.IGetMapping;
import com.cs.core.config.strategy.usecase.endpoint.IGetAllMappedEndpointsStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetKlassWithReferencedKlassesStrategy;
import com.cs.core.runtime.interactor.constants.OnboardingSystemLevelIds;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.OffboardingConstants;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.entity.fileinstance.OnboardingFileInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IOnboardingFileInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentTagInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.ITagInstance;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.fileinstance.ICreateOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceWithDataRuleModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToCSVFileModel;
import com.cs.core.runtime.interactor.model.klassinstance.IWriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceWithDataRuleModel;
import com.cs.core.runtime.interactor.model.klassinstanceexport.WriteInstancesToCSVFileModel;
import com.cs.core.runtime.interactor.model.klassinstanceexport.WriteInstancesToXLSXFileModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.offboarding.ICustomExportParameterModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsWithoutPermissionsStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.AttributeInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.offboarding.IOffboardingInstanceToCSVFileStrategy;
import com.cs.core.runtime.strategy.offboarding.IOffboardingInstancesToXLSXStrategy;
import com.cs.core.runtime.strategy.usecase.fileinstance.ICreateOnboardingFileInstanceStrategy;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.fasterxml.jackson.core.JsonProcessingException;

@Component
public class OffboardingUtils {
  
  @Autowired
  protected IGetAllMappedEndpointsStrategy              getAllMappedEndpointsStrategy;
  
  @Autowired
  protected IGetEndpoint                                getEndPoint;
  
  @Autowired
  protected IGetMapping                                 getMapping;
  
  @Autowired
  protected IOffboardingInstancesToXLSXStrategy         offboardingInstancesToXLSXStrategy;
  
  @Autowired
  protected IOffboardingInstanceToCSVFileStrategy       offboardingInstancesToCSVStrategy;
  
  @Autowired
  protected String                                      exportFolderPath;
  
  @Autowired
  protected ICreateOnboardingFileInstanceStrategy       createOnboardingFileInstanceStrategy;
  
  @Autowired
  protected IGetKlassWithReferencedKlassesStrategy      getKlassWithReferencedKlassesStrategy;
  
  @Autowired
  protected ISessionContext                             context;
  
  @Autowired
  protected KlassInstanceUtils                          klassInstanceUtils;
  
  @Autowired
  protected TransactionThreadData                       transactionThread;
  
  @Autowired
  protected IGetConfigDetailsWithoutPermissionsStrategy getConfigDetailsWithoutPermissionsStrategy;
  
  /*@Autowired
  IGetProcessEventsForUserStrategy getProcessEventsForUserStrategy;
  
  @Autowired
  ISessionContext                  context;
  
  public static void ManageAndFillTagInstances(IKlassInstance klassInstance,
      List<ITagInstance> addedTagInstances,
      List<IModifiedContentTagInstanceModel> modifiedTagInstances, Map<String, Object> itemMap,
      String idColumn, Map<String, String> TagsMapping,
      HashMap<String, HashMap<String, Object>> tagValuesMapping)
  {
    String name = null;
    Map<String, Object> TagMap = new HashMap<>();
  
    @SuppressWarnings("unchecked")
    List<IContentTagInstance> tags = (List<IContentTagInstance>) klassInstance.getTags();
    if (tags == null) {
      tags = new ArrayList<>();
    }
    for (IContentTagInstance tag : tags) {
      if (tag.getOwnerId() == null) {
        TagMap.put(tag.getTagId(), tag);
      }
    }
  
    for (String key : itemMap.keySet()) {
      if (key.equals(idColumn) || TagsMapping.get(key) == null || key.equals(Constants.FILEPATH))
        continue;
  
      String tagGroupId = TagsMapping.get(key);
      String tagValueId = itemMap.get(key)
          .toString();
      ITagInstance tag = (ITagInstance) TagMap.get(tagGroupId);
      List<ITagInstanceValue> tagValues = new LinkedList<>();
      List<ITagInstanceValue> modifiedTagValues = new LinkedList<>();
      List<ITagInstanceValue> adddedTagValues = new LinkedList<>();
  
      if (tag == null) {
        tag = new TagInstance();
  
        tag.setTagId(tagGroupId);
        tag.setId(UUID.randomUUID()
            .toString());
        tag.setBaseType(Constants.TAG_INSTANCE_PROPERTY_TYPE);
        tag.setIsValueChanged(true);
        setTagValues(tagValues, adddedTagValues, modifiedTagValues, tagValueId, tagValuesMapping,
            tagGroupId);
        tag.setTagValues(adddedTagValues);
        addedTagInstances.add(tag);
      }
      else {
  
        IModifiedContentTagInstanceModel modifiedTagInstance = new ModifiedTagInstanceModel(tag);
        tagValues = tag.getTagValues();
  
        setTagValues(tagValues, adddedTagValues, modifiedTagValues, tagValueId, tagValuesMapping,
            tagGroupId);
        modifiedTagInstance.setAddedTagValues(adddedTagValues);
        modifiedTagInstance.setModifiedTagValues(modifiedTagValues);
        if (!modifiedTagValues.isEmpty() || !adddedTagValues.isEmpty())
          modifiedTagInstances.add(modifiedTagInstance);
      }
    }
  }
  
  public static void setTagValues(List<ITagInstanceValue> tagValues,
      List<ITagInstanceValue> adddedTagValues, List<ITagInstanceValue> modifiedTagValues,
      String tagValueInString, HashMap<String, HashMap<String, Object>> tagValuesMapping,
      String tagGroupId)
  {
    List<String> valueList = OffboardingUtils.StringToList(tagValueInString);
    List<String> mappedTagValueList = new LinkedList<>();
  
    if (tagValuesMapping.containsKey(tagGroupId)) {
      for (String value : valueList) {
        if (tagValuesMapping.get(tagGroupId)
            .containsKey(value)) {
          mappedTagValueList.add((String) tagValuesMapping.get(tagGroupId)
              .get(value));
        }
        else {
          mappedTagValueList.add(value);
        }
        if (tagValuesMapping.get(tagGroupId)
            .get(value.toLowerCase() + "_isIgnoreCase") != null
            && (Boolean) tagValuesMapping.get(tagGroupId)
                .get(value.toLowerCase() + "_isIgnoreCase")) {
          // TODO FIX ME
          for (String key : tagValuesMapping.get(tagGroupId)
              .keySet()) {
            if (key.equalsIgnoreCase(value)) {
              mappedTagValueList.add((String) tagValuesMapping.get(tagGroupId)
                  .get(value.toLowerCase()));
            }
          }
        }
      }
    }
    else {
      mappedTagValueList.addAll(valueList);
    }
  
    for (ITagInstanceValue tagValue : tagValues) {
      Boolean isValueChanged = false;
      if (mappedTagValueList.contains(tagValue.getTagId())) {
        if (tagValue.getRelevance() != 100) {
          isValueChanged = true;
          tagValue.setRelevance(100);
        }
        mappedTagValueList.remove(tagValue.getTagId());
      }
      else {
        if (tagValue.getRelevance() != 0) {
          isValueChanged = true;
          tagValue.setRelevance(0);
        }
      }
      if (isValueChanged)
        modifiedTagValues.add(tagValue);
    }
    if (!mappedTagValueList.isEmpty()) {
      for (String tagValueId : mappedTagValueList) {
        ITagInstanceValue tagValue = new TagInstanceValue();
        tagValue.setId(UUID.randomUUID()
            .toString());
        tagValue.setTagId(tagValueId);
        tagValue.setRelevance(100);
        adddedTagValues.add(tagValue);
      }
    }
  }
  
  public static String ManageAndFillAttributeInstances(IKlassInstance klassInstance,
      List<IContentAttributeInstance> addedAttributeInstances,
      List<IModifiedContentAttributeInstanceModel> modifiedAttributeInstances,
      Map<String, Object> itemMap, String idColumn, Map<String, List<String>> attributesMapping)
  {
  
    String name = null;
    Map<String, Object> attributeMap = new HashMap<>();
    List<IAttributeInstance> attributes = (List<IAttributeInstance>) klassInstance.getAttributes();
    for (IContentAttributeInstance attribute : attributes) {
      if (attribute instanceof IImageAttributeInstance)
        continue;
      if (attribute.getOwnerId() == null) {
        attributeMap.put(attribute.getAttributeId(), attribute);
      }
    }
    for (String key : itemMap.keySet()) {
      // TODO remove second condition once tags are handled.
      if (key.equals(idColumn)  || attributesMapping.get(key) == null)
        continue;
      if (key.equals(CommonConstants.NAME_ATTRIBUTE) || attributesMapping.get(key)
          .contains(CommonConstants.NAME_ATTRIBUTE)) {
        name = (String) itemMap.get(key);
      }
      for (String attributeId : attributesMapping.get(key)) {
        IAttributeInstance attribute = (IAttributeInstance) attributeMap.get(attributeId);
        if (attribute == null) {
          attribute = new AttributeInstance();
          attribute.setAttributeId(attributeId);
          attribute.setValue((String) itemMap.get(key));
          attribute.setId(UUID.randomUUID()
              .toString());
          attribute.setBaseType(Constants.ATTRIBUTE_INSTANCE_PROPERTY_TYPE);
          attribute.setIsValueChanged(true);
          addedAttributeInstances.add(attribute);
        }
        else {
          Boolean isValueChanged = isAttributeValueChanged((String) itemMap.get(key),
              attribute.getValue());
          if (!isValueChanged) {
            attribute.setValue((String) itemMap.get(key));
            IModifiedContentAttributeInstanceModel modifiedAttributeInstance = new ModifiedAttributeInstanceModel(
                attribute);
            modifiedAttributeInstances.add(modifiedAttributeInstance);
          }
        }
      }
    }
    return name;
  }
  
  public static Boolean isAttributeValueChanged(String newAttributeValue, String OldAttributeValue)
  {
  
    return OldAttributeValue == null ? newAttributeValue == null
        : OldAttributeValue.equals(newAttributeValue);
  }
  
  public static List<String> StringToList(String str)
  {
    List<String> valuesList = new ArrayList<String>(Arrays.asList(str.split(",")));
    valuesList.replaceAll(String::trim);
  
    return valuesList;
  }
  
  public static void waitForThreadsExecution(List<Future<?>> threads)
  {
    while (true) {
      List<Future<?>> listToRemove = new ArrayList<>();
      for (Future<?> future : threads) {
        if (future.isDone()) {
          listToRemove.add(future);
        }
      }
      threads.removeAll(listToRemove);
      if (threads.size() == 0) {
        break;
      }
    }
  }
  
  @SuppressWarnings("unchecked")
  public Map<String, Map<String, Object>> getSheetsInfoAccordingToProcessFlow() throws Exception
  {
    Map<String, Map<String, Object>> mapToReturn = new HashMap<>();
    IGetProcessEventsForUserModel getModel = new GetProcessEventsForUserModel();
    getModel.setUserId(context.getUserId());
    getModel.setEventType(EventType.BUSINESS_PROCESS);
    IListModel<IGetProcessEventModel> responseList = getProcessEventsForUserStrategy
        .execute(getModel);
  
    Map<String, Object> flow = null;
    for (IGetProcessEventModel process : responseList.getList()) {
      flow = process.getProcessFlow();
    }
  
    if (flow != null) {
      for (String componentInstanceId : flow.keySet()) {
        Map<String, Object> componentConfig = (Map<String, Object>) flow.get(componentInstanceId);
        Map<String, Object> parameters = (Map<String, Object>) componentConfig.get("parameters");
        List<Map<String, Object>> dataSources = (List<Map<String, Object>>) parameters
            .get("dataSources");
        if (dataSources.size() > 0) {
          Map<String, Object> dataSource = dataSources.get(0);
          String sheet = (String) dataSource.get("sheet");
          Map<String, Object> rowInfo = new HashMap<>();
          rowInfo.put("headerRow", dataSource.get("headerRowNumber"));
          rowInfo.put("dataRow", dataSource.get("dataRowNumber"));
          mapToReturn.put(sheet, rowInfo);
        }
      }
    }
    return mapToReturn;
  }
  
  public static void getRuntimeMappings(IRuntimeEndpointModel endpoint, String hotFolderPath)
      throws HeaderNotFoundException, FileNotFoundException, IOException
  {
    Set<String> mappedColumnNames = new HashSet<>();
    Set<String> tagColumnNames = new HashSet<>();
    Map<String, IConfigRuleTagMappingModel> tagMappings = new HashMap<>();
    for (IConfigRuleTagMappingModel configRuleMapping : endpoint.getTagMappings()) {
      String columnName = configRuleMapping.getColumnNames()
          .get(0);
      mappedColumnNames.add(columnName);
      if (configRuleMapping.getTagValueMappings()
          .size() > 0) {
        tagColumnNames.add(columnName);
        tagMappings.put(columnName, configRuleMapping);
      }
    }
  
    String filePath = hotFolderPath + "//" + endpoint.getFileInstanceId();
    File file = new File(filePath);
    FileInputStream fileInputStream = new FileInputStream(file);
    XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
  
    List<String> headerList = OffboardingFileUtils.getRuntimeMappingsFromFile(workbook,
        tagColumnNames, tagMappings);
    headerList.removeAll(mappedColumnNames);
    endpoint.setUnmappedColumns(headerList);
  }*/
  
  @SuppressWarnings("unchecked")
  public static List<String[]> prepareInstancesDataToWrite(LinkedHashSet<String> header,
      Map<String, Object> klassInstanceDataMap) throws Exception
  {
    List<String[]> dataToWrite = new ArrayList<>();
    
    for (String instanceId : klassInstanceDataMap.keySet()) {
      
      LinkedList<String> instanceDataList = new LinkedList<>();
      Map<String, String> instanceDataMap = (Map<String, String>) klassInstanceDataMap
          .get(instanceId);
      
      for (String columnName : header) {
        instanceDataList.add(instanceDataMap.get(columnName));
      }
      
      String[] instanceDataToWrite = instanceDataList.toArray(new String[instanceDataList.size()]);
      dataToWrite.add(instanceDataToWrite);
    }
    
    return dataToWrite;
  }
  
  public static String getTimeStampToAppendFileName()
  {
    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd-HH_mm_ss");
    Date date = new Date();
    
    return dateFormat.format(date);
  }
  
  public IMappingModel getMappingForExport(String endpointId) throws Exception
  {
    /*IGetMappedEndpointRequestModel getMappedEndPointModel = new GetMappedEndpointsRequestModel();
    getMappedEndPointModel.setFileHeaders(new ArrayList<>());
    getMappedEndPointModel.setCurrentUserId(userId);
    getMappedEndPointModel.setBoardingType(CommonConstants.OFFBOARDING);
    
    IGetMappedEndpointResponseModel mappings = getAllMappedEndpointsStrategy
        .execute(getMappedEndPointModel);
    if (mappings.getEndpointId() == null) {
      throw new EndpointNotFoundException();
    }*/
    IIdParameterModel endpointModel = new IdParameterModel();
    endpointModel.setId(endpointId);
    IGetEndpointModel getEndPointModel = getEndPoint.execute(endpointModel);
    IEndpointModel endPoint = getEndPointModel.getEndpoint();
    /* List<String> mappingIds = endPoint.getMappings();
    if (mappingIds == null || mappingIds.size() == 0) {
      // throw new ProcessEndPointNotFoundException();
      return new MappingModel();
    } */
 // Now getMapping.execute() method does not return all  mapping at a time.For Runtime utilization this should be fixed  .
    GetOutAndInboundMappingModel mappingModel = new GetOutAndInboundMappingModel();
    //mappingModel.setId(mappingIds.get(0));
    mappingModel.setTabId(ProcessConstants.PROPERTYCOLLECTION_TAB_ID );
    IMappingModel propertyMapping = getMapping.execute(mappingModel);
    
    return propertyMapping;
  }
  
  public static void prepareMapForAttributeMappings(
      List<IConfigRuleAttributeMappingModel> attributeMappings,
      Map<String, String> attributeMapValues)
  {
    for (IConfigRuleAttributeMappingModel attributeMappingModel : attributeMappings) {
      attributeMapValues.put(attributeMappingModel.getMappedElementId(),
          attributeMappingModel.getColumnNames()
              .get(0));
    }
  }
  
  public static void prepareMapForKlassMappings(List<IConfigRuleClassMappingModel> classMappings,
      Map<String, String> klassMapValues)
  {
    for (IConfigRuleClassMappingModel klassMappingModel : classMappings) {
      klassMapValues.put(klassMappingModel.getMappedElementId(), klassMappingModel.getColumnNames()
          .get(0));
    }
  }
  
  public static void prepareMapForTaxonomyMappings(
      List<IConfigRuleTaxonomyMappingModel> taxonomyMappings, Map<String, String> taxonomyMapValues)
  {
    for (IConfigRuleTaxonomyMappingModel taxonomyMappingModel : taxonomyMappings) {
      taxonomyMapValues.put(taxonomyMappingModel.getMappedElementId(),
          taxonomyMappingModel.getColumnNames()
              .get(0));
    }
  }
  
  public static void prepareMapForTagMappings(List<IConfigRuleTagMappingModel> tagMappings,
      Map<String, String> tagMapValues)
  {
    for (IConfigRuleTagMappingModel tagMappingModel : tagMappings) {
      tagMapValues.put(tagMappingModel.getMappedElementId(), tagMappingModel.getColumnNames()
          .get(0));
      List<IColumnValueTagValueMappingModel> tagValueMappings = tagMappingModel
          .getTagValueMappings();
      for (IColumnValueTagValueMappingModel tagValueMappingModel : tagValueMappings) {
        List<ITagValueMappingModel> tagValuemappings = tagValueMappingModel.getMappings();
        for (ITagValueMappingModel valueMappingModel : tagValuemappings) {
          tagMapValues.put(valueMappingModel.getMappedTagValueId(),
              valueMappingModel.getTagValue());
        }
      }
    }
  }
  
  public static void prepareMappingValueWithIdAndColumn(IMappingModel propertyMapping,
      Map<String, String> attributeMapValues, Map<String, String> klassMapValues,
      Map<String, String> taxonomyMapValues, Map<String, String> tagMapValues)
  {
    prepareMapForAttributeMappings(propertyMapping.getAttributeMappings(), attributeMapValues);
    prepareMapForKlassMappings(propertyMapping.getClassMappings(), klassMapValues);
    prepareMapForTaxonomyMappings(propertyMapping.getTaxonomyMappings(), taxonomyMapValues);
    prepareMapForTagMappings(propertyMapping.getTagMappings(), tagMapValues);
  }
  
  public static List<String> StringToList(String str)
  {
    List<String> valuesList = new ArrayList<String>(Arrays.asList(str.split(",")));
    valuesList.replaceAll(String::trim);
    
    return valuesList;
  }
  
  public static void handlePrimaryKeyMultipleColumn(Map<String, String> dataMap, String instanceId,
      String primaryKeyColumn, LinkedHashSet<String> header)
  {
    String[] splitedPrimaryKey = primaryKeyColumn.split(OffboardingConstants.VALUE_SEPERATOR, -1);
    if (splitedPrimaryKey.length > 1) {
      String[] splitedInstanceId = instanceId.split(OffboardingConstants.ID_GENERATOR_AND_SPLITTER,
          -1);
      for (int i = 0; i < splitedPrimaryKey.length; i++) {
        header.add(splitedPrimaryKey[i]);
        dataMap.put(splitedPrimaryKey[i], splitedInstanceId[i]);
      }
    }
  }
  
  public void prepareDataModelAndExport(IComponentParameterModel componentModel,
      String[] headerToWrite, List<String[]> dataToWrite, String exportType, String fileName,
      String fileInstanceIdForExport) throws Exception
  {
    
    switch (exportType) {
      case OffboardingConstants.EXPORT_TYPE_XLSX:
        String xlsxfileName = fileName;
        IWriteInstancesToXLSXFileModel exportXLSXDataModel = new WriteInstancesToXLSXFileModel();
        setCommonModelProperties(exportXLSXDataModel, componentModel, headerToWrite, dataToWrite);
        exportXLSXDataModel.setSheetName(componentModel.getSheet());
        exportXLSXDataModel.setfileName(xlsxfileName);
        exportXLSXDataModel.setFileInstanceIdForExport(fileInstanceIdForExport);
        ICreateOnboardingFileInstanceModel fileInstanceModel = offboardingInstancesToXLSXStrategy
            .execute(exportXLSXDataModel);
        if (fileInstanceModel != null) {
          createFileInstance(fileInstanceModel);
        }
        break;
      case OffboardingConstants.EXPORT_TYPE_CSV:
        String csvFileName = fileName + OffboardingConstants.CSV_FILE_EXTENSION;
        IWriteInstancesToCSVFileModel exportCSVDataModel = new WriteInstancesToCSVFileModel();
        exportCSVDataModel.setfileName(csvFileName);
        setCommonModelProperties(exportCSVDataModel, componentModel, headerToWrite, dataToWrite);
        offboardingInstancesToCSVStrategy.execute(exportCSVDataModel);
        break;
    }
  }
  
  public void createFileInstance(ICreateOnboardingFileInstanceModel fileInstanceModel)
      throws Exception
  {
    // TODO Auto-generated method stub
    IGetKlassModel getKlassModel = getKlassWithReferencedKlassesStrategy
        .execute(new IdParameterModel(OnboardingSystemLevelIds.ONBOARDING_FILE_KLASS));
    IKlass typeKlass = getKlassModel.getKlass();
    
    IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
    List<String> types = new ArrayList<>();
    types.add(typeKlass.getId());
    multiclassificationRequestModel.setKlassIds(types);
    multiclassificationRequestModel.setEndpointId(fileInstanceModel.getEndpointId());
    multiclassificationRequestModel.setOrganizationId(fileInstanceModel.getOrganizationId());
    multiclassificationRequestModel.setPhysicalCatalogId(fileInstanceModel.getPhysicalCatalogId());
    // multiclassificationRequestModel.setLanguageCodes(Arrays.asList(fileInstanceModel.getDataLanguage()));
    multiclassificationRequestModel.setUserId(context.getUserId());
    
    IGetConfigDetailsForCustomTabModel configDetails = getConfigDetailsWithoutPermissionsStrategy
        .execute(multiclassificationRequestModel);
    
    IKlassInstance klassInstance = createNewKlassInstance(typeKlass, fileInstanceModel);
    IKlassInstanceWithDataRuleModel klassInstanceWithDataRuleModel = new KlassInstanceWithDataRuleModel();
    klassInstanceWithDataRuleModel.setKlassInstance(klassInstance);
    klassInstanceWithDataRuleModel.setDataRulesOfKlass(getKlassModel.getDataRulesOfKlass());
    klassInstanceWithDataRuleModel.setTypeKlass(getKlassModel.getKlass());
    klassInstanceWithDataRuleModel.setConfigDetails(configDetails);
    
    AttributeInstanceUtils.checkAndAddMissingMandatoryAttributes(klassInstance, new ArrayList<>(),
        (Map<String, IAttribute>) configDetails.getReferencedAttributes(), context.getUserId(),
        transactionThread.getTransactionData()
            .getDataLanguage());
    IOnboardingFileInstance fileInstance = createOnboardingFileInstanceStrategy
        .execute(klassInstanceWithDataRuleModel);
  }
  
  protected IKlassInstance createNewKlassInstance(IKlass klass,
      ICreateOnboardingFileInstanceModel createInstanceModel) throws Exception
  {
    IKlassInstance klassInstance = new OnboardingFileInstance();
    klassInstance.setName(createInstanceModel.getName());
    List<String> types = new ArrayList<>();
    types.add(klass.getId());
    klassInstance.setTypes(types);
    // TODO Conflicts
    // klassInstance.setParentId("-1");
    // klassInstance.setIsFolder(false);
    klassInstance.setVersionId(0l);
    // klassInstance.setOwner(context.getUserId());
    klassInstance.setId(createInstanceModel.getId());
    klassInstance.setEndpointId(createInstanceModel.getEndpointId());
    klassInstance.setOrganizationId(createInstanceModel.getOrganizationId());
    klassInstance.setPhysicalCatalogId(Constants.DATA_INTEGRATION_CATALOG_IDS);
    klassInstance.setLastModified(System.currentTimeMillis());
    return klassInstance;
  }
  
  protected void setCommonModelProperties(IWriteInstancesToFileModel exportDataModel,
      IComponentParameterModel componentParameterModel, String[] headerToWrite,
      List<String[]> dataToWrite)
  {
    
    exportDataModel.setfilePath(exportFolderPath + "\\");
    exportDataModel.setHeaderRowNumber(componentParameterModel.getHeaderRowNumber());
    exportDataModel.setDataRowNumber(componentParameterModel.getDataRowNumber());
    exportDataModel.setHeaderToWrite(headerToWrite);
    exportDataModel.setDataToWrite(dataToWrite);
  }
  
  public static String getDocTypeByBaseType(String BaseType)
  {
    switch (BaseType) {
      case Constants.ARTICLE_INSTANCE_BASE_TYPE:
        return Constants.ARTICLE_KLASS_INSTANCE_DOC_TYPE;
      case Constants.SET_INSTANCE_BASE_TYPE:
        return Constants.SET_KLASS_INSTANCE_DOC_TYPE;
      case Constants.ASSET_INSTANCE_BASE_TYPE:
        return Constants.ASSET_INSTANCE_DOC_TYPE;
      case Constants.MARKET_INSTANCE_BASE_TYPE:
        return Constants.MARKET_TARGET_INSTANCE_DOC_TYPE;
      case Constants.IMPORT_SYSTEM_INSTANCE_BASE_TYPE:
        return Constants.IMPORT_SYSTEM_ARTICLE_INSTANCE_DOC_TYPE;
      case Constants.MASTER_ARTICLE_INSTANCE_BASE_TYPE:
        return Constants.MASTER_ARTICLE_INSTANCE_DOC_TYPE;
      case Constants.TEXTASSET_INSTANCE_BASE_TYPE:
        return Constants.TEXTASSET_INSTANCE_DOC_TYPE;
      case Constants.SUPPLIER_INSTANCE_BASE_TYPE:
        return Constants.SUPPLIER_INSTANCE_DOC_TYPE;
    }
    return null;
  }
  
  public static String getBaseTypeByKlassType(String klassType)
  {
    switch (klassType) {
      case Constants.PROJECT_KLASS_TYPE:
        return Constants.ARTICLE_INSTANCE_BASE_TYPE;
      case Constants.ASSET_KLASS_TYPE:
        return Constants.ASSET_INSTANCE_BASE_TYPE;
      case Constants.MARKET_KLASS_TYPE:
        return Constants.MARKET_INSTANCE_BASE_TYPE;
      
      case Constants.MASTER_IMPORT_ARTICLE_KLASS_TYPE:
        return Constants.MASTER_ARTICLE_INSTANCE_BASE_TYPE;
      case Constants.IMPORT_SYSTEM_FILE_ARTICLE_KLASS_TYPE:
      case Constants.IMPORT_SYSTEM_INSTANCE_KLASS_TYPE:
      case Constants.IMPORT_SYSTEM_FILE_KLASS_TYPE:
      case Constants.IMPORT_ARTICLE_KLASS_TYPE:
        return Constants.IMPORT_SYSTEM_INSTANCE_BASE_TYPE;
      case Constants.SUPPLIER_KLASS_TYPE:
        return Constants.SUPPLIER_INSTANCE_BASE_TYPE;
      case Constants.TEXT_ASSET_KLASS_TYPE:
        return Constants.TEXTASSET_INSTANCE_BASE_TYPE;
    }
    return null;
  }
  
  public static void prepareMappingValueWithIdAndColumn(IMappingModel propertyMapping,
      ICustomExportParameterModel parameterModel)
  {
    prepareMapForAttributeMappings(propertyMapping.getAttributeMappings(),
        parameterModel.getAttributeMapValues());
    prepareMapForKlassMappings(propertyMapping.getClassMappings(),
        parameterModel.getKlassMapValues());
    prepareMapForTaxonomyMappings(propertyMapping.getTaxonomyMappings(),
        parameterModel.getTaxonomyMapValues());
    prepareMapForTagMappings(propertyMapping.getTagMappings(), parameterModel.getTagMapValues());
  }
  
  public static String checkNullValue(String stringToCheck, String otherwiseReturnString)
  {
    if (stringToCheck == null) {
      stringToCheck = otherwiseReturnString;
    }
    return stringToCheck;
  }
  
  public static String getDate(String value)
  {
    Date date = new Date(Long.parseLong(value));
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateText = dateFormat.format(date);
    return dateText;
  }
  
  public static String getDate(Long value)
  {
    Date date = new Date(value);
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    String dateText = dateFormat.format(date);
    return dateText;
  }
  
  public static void fillTagMapping(Map<String, String> contextTagMap, List<ITagInstance> tags,
      Map<String, String> tagMapping, LinkedHashSet<String> header)
  {
    for (IContentTagInstance tagInstance : tags) {
      String tagId = tagInstance.getTagId();
      String mappedTagId = OffboardingUtils.checkNullValue(tagMapping.get(tagId), tagId);
      String concatTagValues = null;
      for (ITagInstanceValue tagValue : tagInstance.getTagValues()) {
        if (tagValue.getRelevance() == 100) {
          if (concatTagValues == null) {
            concatTagValues = OffboardingUtils.checkNullValue(tagMapping.get(tagValue.getTagId()),
                tagValue.getTagId());
          }
          else {
            concatTagValues += "," + OffboardingUtils
                .checkNullValue(tagMapping.get(tagValue.getTagId()), tagValue.getTagId());
          }
        }
      }
      header.add(mappedTagId);
      contextTagMap.put(mappedTagId, concatTagValues);
    }
  }
  
  // TODO: 'parentChildMap' have randomId:taxonomyIDs of content , if we select
  // multiple columns
  // then
  // it has multiple entries of randomId:taxonomyIDs in whic taxonomyIDs are
  // same thats why
  // brak
  public static void setInstanceTaxonomyIds(Map<String, String> dataMap,
      Map<String, String> taxonomyMapValues, Map<String, List<String>> parentChildMap,
      LinkedHashSet<String> header) throws JsonProcessingException
  {
    int taxonomyCount = 1;
    List<String> taxonomyIdsToWrite = new ArrayList<>();
    for (Entry<String, List<String>> entry : parentChildMap.entrySet()) {
      for (String parentId : entry.getValue()) {
        taxonomyIdsToWrite.add(checkNullValue(taxonomyMapValues.get(parentId), parentId));
        parentId = "Taxonomy" + taxonomyCount;
        if (taxonomyIdsToWrite != null && !taxonomyIdsToWrite.isEmpty()) {
          dataMap.put(parentId, taxonomyIdsToWrite.get(taxonomyCount - 1));
          header.add(parentId);
          taxonomyCount++;
        }
      }
      break;
    }
  }
  
  public static IMultiPartFileInfoModel getMultipartDataFromFile(String filePath)
      throws IOException, RDBMSException
  {
    MultiPartFileInfoModel multiPartFileInfoModel = new MultiPartFileInfoModel();
    if (filePath != null) {
      File file = new File(filePath);
      String absoluteFilePath = file.getAbsolutePath();
      Path path = Paths.get(absoluteFilePath);
      byte[] bytes = Files.readAllBytes(path);
      multiPartFileInfoModel.setKey(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RANDOM.getPrefix()));
      multiPartFileInfoModel.setBytes(bytes);
      multiPartFileInfoModel.setOriginalFilename(file.getName());
    }
    
    return multiPartFileInfoModel;
  }
  
  public static void setTransactionData(IComponentModel model, ITransactionData transactionData)
  {
    transactionData.setEndpointId(model.getEndpointId());
    transactionData.setPhysicalCatalogId(model.getPhysicalCatalogId());
    transactionData.setPortalId(model.getPortalId());
    transactionData.setOrganizationId(model.getOrganizationId());
    transactionData.setSystemId(model.getSystemId());
    transactionData.setLogicalCatalogId(model.getLogicalCatalogId());
    transactionData.setDataLanguage(model.getDataLanguage());
    transactionData.setPortalId(model.getPortalId());
    transactionData.setUiLanguage(model.getUiLanguage());
  }
}
