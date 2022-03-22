package com.cs.core.runtime.interactor.utils.klassinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.asset.services.VariantInstanceCreation;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.AutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IAutoCreateVariantDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.entity.attribute.*;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.tag.IIdRelevance;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.ITagValue;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.model.asset.LinkedInstancesPropertyModel;
import com.cs.core.config.interactor.model.attribute.DateInstanceModel;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyChildrenModel;
import com.cs.core.config.interactor.model.variantcontext.*;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.dto.ContextualDataTransferGranularDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferDTO;
import com.cs.core.rdbms.coupling.idto.IContextualDataTransferGranularDTO;
import com.cs.core.rdbms.entity.dto.PropertyRecordDTO;
import com.cs.core.rdbms.entity.dto.SortDTO;
import com.cs.core.rdbms.entity.dto.TagDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.EmbeddedType;
import com.cs.core.rdbms.process.idao.IRDBMSCursor.OrderDirection;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.Seperators;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.*;
import com.cs.core.runtime.interactor.entity.tag.ITagInstanceValue;
import com.cs.core.runtime.interactor.entity.tag.TagInstance;
import com.cs.core.runtime.interactor.entity.tag.TagInstanceValue;
import com.cs.core.runtime.interactor.entity.timerange.IInstanceTimeRange;
import com.cs.core.runtime.interactor.entity.timerange.InstanceTimeRange;
import com.cs.core.runtime.interactor.entity.variants.ContextInstance;
import com.cs.core.runtime.interactor.entity.variants.IContextInstance;
import com.cs.core.runtime.interactor.exception.variants.DuplicateVariantExistsException;
import com.cs.core.runtime.interactor.exception.variants.EmptyMandatoryFieldsException;
import com.cs.core.runtime.interactor.exception.variants.InvalidTimeRangeException;
import com.cs.core.runtime.interactor.model.assetinstance.AssetInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInformationModel;
import com.cs.core.runtime.interactor.model.attribute.IAttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplateConfigDetails;
import com.cs.core.runtime.interactor.model.configuration.*;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDateInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.logger.ITransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.pluginexception.*;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.templating.GetKlassInstanceForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.core.runtime.interactor.model.templating.IGetKlassInstanceCustomTabModel;
import com.cs.core.runtime.interactor.model.variants.*;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.PropertyRecordBuilder.PropertyRecordType;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.util.ConfigUtil;
import com.cs.utils.BaseEntityUtils;
import com.cs.utils.ContextUtil;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Component
public class VariantInstanceUtils {
  
  @Autowired
  RDBMSComponentUtils      rdbmsComponentUtils;
  
  @Autowired
  SearchAssembler          searchAssembler;
  
  @Autowired
  GetAllUtils              getAllUtils;
  
  @Autowired
  protected ConfigUtil     configUtil;
  
  @Autowired
  RelationshipInstanceUtil relationshipInstanceUtil;
  
  @Autowired
  protected TransactionThreadData transactionThread;
  
  private static final String SERVICE_FOR_CDT = "CONTEXTUAL_DATA_TRANSFER_TASK";

  String[] subBaseTypes = {Constants.MARKET_INSTANCE_BASE_TYPE};
  
  public Map<String, IVariantReferencedInstancesModel> getReferencedInstances(
      IBaseEntityDAO baseEntityDAO, IContextInstance contextInstance) throws Exception
  {
    // FIX ME: Change to get linked entities.
    Set<IBaseEntityDTO> contextualLinkedEntities = baseEntityDAO.getContextualLinkedEntities();
    List<IIdAndBaseType> linkedInstances = contextInstance.getLinkedInstances();
    Map<String, IVariantReferencedInstancesModel> referencedInstances = new HashMap<>();
    
    if (contextualLinkedEntities != null) {
      
      for (IBaseEntityDTO contextualLinkedEntity : contextualLinkedEntities) {
        VariantReferencedInstancesModel variantReferencedInstancesModel = new VariantReferencedInstancesModel();
        String id = String.valueOf(contextualLinkedEntity.getBaseEntityIID());
        referencedInstances.put(id, variantReferencedInstancesModel);
        
        BaseType baseType = contextualLinkedEntity.getBaseType();
        
        String baseTypeString = BaseEntityUtils.getBaseTypeString(baseType);
        variantReferencedInstancesModel.setBaseType(baseTypeString);
        variantReferencedInstancesModel.setId(id);
        variantReferencedInstancesModel.setTypes(getTypes(contextualLinkedEntity));
        variantReferencedInstancesModel.setName(contextualLinkedEntity.getBaseEntityName());
        IIdAndBaseType idModel = new IdAndBaseType(id, baseTypeString);
        linkedInstances.add(idModel);
      }
    }
    return referencedInstances;
  }
  
  private List<String> getTypes(IBaseEntityDTO contextualLinkedEntity)
  {
    List<String> types = contextualLinkedEntity.getOtherClassifiers()
        .stream()
        .filter(x -> x.getClassifierType()
            .equals(ClassifierType.CLASS))
        .map(x -> x.getCode())
        .collect(Collectors.toList());
    
    types.add(contextualLinkedEntity.getNatureClassifier()
        .getCode());
    return types;
  }
  
  public IContextInstance getContextInstance(List<IContentTagInstance> tagInstances,
      IBaseEntityDAO baseEntityDAO, Map<String, ITag> referencedTags)
  {
    IContextualDataDTO contextualObject = baseEntityDAO.getBaseEntityDTO()
        .getContextualObject();
    
    IContextInstance contextInstance = new ContextInstance();
    contextInstance.setContextId(contextualObject.getContextCode());
    contextInstance.setId(String.valueOf(contextualObject.getContextualObjectIID()));
    
    IInstanceTimeRange timeRange = new InstanceTimeRange();
    timeRange.setFrom(contextualObject.getContextStartTime());
    timeRange.setTo(contextualObject.getContextEndTime());
    contextInstance.setTimeRange(timeRange);
    Set<ITagDTO> contextTagValues = contextualObject.getContextTagValues();
    
    Map<String, String> tagValueMappingWithTag = getTagValueMappingWithTag(referencedTags);
    fillContextTagValues(tagInstances, contextTagValues, tagValueMappingWithTag, baseEntityDAO,
        contextInstance);
    return contextInstance;
  }
  
  public Map<String, String> getTagValueMappingWithTag(Map<String, ITag> referencedTags)
  {
    Map<String, String> tagValueIdTagIdMapping = new HashMap<>();
    for (ITag referencedTag : referencedTags.values()) {
      List<? extends ITreeEntity> children = referencedTag.getChildren();
      for (ITreeEntity child : children) {
        tagValueIdTagIdMapping.put(child.getId(), referencedTag.getId());
      }
    }
    return tagValueIdTagIdMapping;
  }
  
  private void fillContextTagValues(List<IContentTagInstance> tagInstances,
      Set<ITagDTO> contextTagValues, Map<String, String> tagValueMappingWithTag,
      IBaseEntityDAO baseEntityDAO, IContextInstance contextInstance)
  {
    Map<String, ITagInstance> tagInstanceMap = new HashMap<>();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    
    for (ITagDTO contextTagValue : contextTagValues) {
      String tagValueCode = contextTagValue.getTagValueCode();
      String tagCode = tagValueMappingWithTag.get(tagValueCode);
      ITagInstance tagInstance = tagInstanceMap.get(tagCode);
      if (tagInstance == null) {
        tagInstance = new TagInstance();
        String tagInstanceID = tagCode + Seperators.UNDERSCORE_SEPERATOR
            + contextualObject.getContextualObjectIID();
        contextInstance.getTagInstanceIds()
            .add(tagInstanceID);
        tagInstance.setId(tagInstanceID);
        tagInstance.setKlassInstanceId(baseEntityDTO.getNatureClassifier()
            .getClassifierCode());
        tagInstance.setBaseType("com.cs.core.runtime.interactor.entity.tag.TagInstance");
        tagInstance.setTagId(tagValueMappingWithTag.get(contextTagValue.getTagValueCode()));
        tagInstance.setContextInstanceId(contextInstance.getId());
        tagInstanceMap.put(tagCode, tagInstance);
      }
      // Tag values
      List<ITagInstanceValue> tagValues = tagInstance.getTagValues();
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setTagId(contextTagValue.getTagValueCode());
      tagInstanceValue.setId(contextTagValue.getTagValueCode());
      tagInstanceValue.setCode(contextTagValue.getTagValueCode());
      tagInstanceValue.setRelevance(contextTagValue.getRange());
      tagValues.add(tagInstanceValue);
    }
    tagInstances.addAll(new ArrayList<>(tagInstanceMap.values()));
  }
  
  public IGetKlassInstanceCustomTabModel executeGetKlassInstanceForOverviewTab(
      IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO) throws Exception
  {
    IGetKlassInstanceCustomTabModel returnModel = new GetKlassInstanceForCustomTabModel();
    KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO,
        configDetails, rdbmsComponentUtils);
    returnModel
        .setKlassInstance((IContentInstance) klassInstanceBuilder.getKlassInstanceForOverviewTab());
    IContentInstance klassInstance = returnModel.getKlassInstance();
    IContextualDataDTO contextualObject = baseEntityDAO.getBaseEntityDTO()
        .getContextualObject();
    // set Context and fill context tag values
    IContextInstance contextInstance = null;
    if (contextualObject != null && contextualObject.getContextCode() != null
        && !contextualObject.getContextCode()
            .equals("")) {
      contextInstance = getContextInstance((List<IContentTagInstance>) klassInstance.getTags(),
          baseEntityDAO, configDetails.getReferencedTags());
      ((IContentInstance) klassInstance).setContext(contextInstance);
      returnModel.setReferencedInstances(getReferencedInstances(baseEntityDAO, contextInstance));
    }
    
    returnModel.setConfigDetails(configDetails);
    relationshipInstanceUtil.executeGetRelationshipInstance(returnModel, (IGetConfigDetailsForCustomTabModel)configDetails, baseEntityDAO, rdbmsComponentUtils);
    
    return returnModel;
  }
  
  public Boolean fillContextualColumns(List<IIdParameterModel> columns, String columnId,
      IReferencedVariantContextModel referencedContext)
  {
    if (columnId.equals(IInstanceTimeRange.FROM) || columnId.equals(IInstanceTimeRange.TO)) {
      putTimeColumnEntry(columns, columnId);
      return true;
    }
    
    List<String> entities = (List<String>) referencedContext.getEntities();
    for (String entity : entities) {
      String klass = getKlassByEntityType(entity);
      if (klass.equals(columnId) && CommonConstants.KLASS_TYPES.contains(columnId)) {
        IIdParameterModel map = new IdParameterModel();
        map.setId(columnId);
        map.setType(IContextInstance.LINKED_INSTANCES);
        columns.add(map);
        return true;
      }
    }
    return false;
  }
  
  public void putTimeColumnEntry(List<IIdParameterModel> columns, String id)
  {
    IIdParameterModel map = new IdParameterModel();
    map.setId(id);
    map.setType(CommonConstants.DATE_TYPE);
    columns.add(map);
  }
  
  public void putLinkedInstancesColumnEntry(List<IIdParameterModel> columns, List<String> entities)
  {
    
    for (String entity : entities) {
      
      IIdParameterModel map = new IdParameterModel();
      map.setId(getKlassByEntityType(entity));
      map.setType(IContextInstance.LINKED_INSTANCES);
      columns.add(map);
    }
  }
  
  public String getKlassByEntityType(String entity)
  {
    switch (entity) {
      case Constants.ARTICLE_INSTANCE_MODULE_ENTITY:
        return CommonConstants.ARTICLE;
      case Constants.ASSET_INSTANCE_MODULE_ENTITY:
        return CommonConstants.ASSET;
      case Constants.MARKET_INSTANCE_MODULE_ENTITY:
        return CommonConstants.MARKET;
      case Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY:
        return CommonConstants.TEXT_ASSET;
      case Constants.SUPPLIER_INSTANCE_MODULE_ENTITY:
        return CommonConstants.SUPPLIER;
      case Constants.FILE_INSTANCE_MODULE_ENTITY:
        return CommonConstants.SUPPLIER;
    }
    return null;
  }
  
  public void fillContextualProperties(Boolean isTimeEnabled, IContextualDataDTO contextualObject,
      Map<String, IPropertyInstance> properties,
      Map<String, IVariantReferencedInstancesModel> referencedInstances,
      Set<IBaseEntityDTO> contextualLinkedEntities, List<String> allowedEntities) throws Exception
  {
    if (isTimeEnabled) {
      long contextEndTime = contextualObject.getContextEndTime();
      long contextStartTime = contextualObject.getContextStartTime();
      
      IDateInstanceModel from = new DateInstanceModel();
      from.setValue(String.valueOf(contextStartTime));
      from.setBaseType(CommonConstants.DATE_INSTANCE_BASE_TYPE);
      from.setId(IInstanceTimeRange.FROM);
      properties.put(IInstanceTimeRange.FROM, from);
      
      IDateInstanceModel to = new DateInstanceModel();
      to.setValue(String.valueOf(contextEndTime));
      to.setBaseType(CommonConstants.DATE_INSTANCE_BASE_TYPE);
      to.setId(IInstanceTimeRange.TO);
      properties.put(IInstanceTimeRange.TO, to);
    }
    
    for (String entity : allowedEntities) {
      ILinkedInstancesPropertyModel propertyInstance = new LinkedInstancesPropertyModel();
      propertyInstance.setId(IContextInstance.LINKED_INSTANCES);
      propertyInstance.setBaseType(CommonConstants.LINKED_INSTANCES_BASE_TYPE);
      propertyInstance.setValues(new ArrayList<>());
      properties.put(getKlassByEntityType(entity), propertyInstance);
    }
    
    for (IBaseEntityDTO contextualLinkedEntity : contextualLinkedEntities) {
      VariantReferencedInstancesModel variantReferencedInstancesModel = new VariantReferencedInstancesModel();
      String id = String.valueOf(contextualLinkedEntity.getBaseEntityIID());
      referencedInstances.put(id, variantReferencedInstancesModel);
      variantReferencedInstancesModel
          .setBaseType(BaseEntityUtils.getBaseTypeString(contextualLinkedEntity.getBaseType()));
      variantReferencedInstancesModel.setId(id);
      variantReferencedInstancesModel.setTypes(getTypes(contextualLinkedEntity));
      variantReferencedInstancesModel.setName(contextualLinkedEntity.getBaseEntityName());
      
      String klassByBaseType = BaseEntityUtils
          .getKlassByBaseType(contextualLinkedEntity.getBaseType());
      ILinkedInstancesPropertyModel propertyInstance = (ILinkedInstancesPropertyModel) properties
          .get(klassByBaseType);
      List<String> values = ((LinkedInstancesPropertyModel) propertyInstance).getValues();
      if (values == null) {
        values = new ArrayList<String>();
        ((LinkedInstancesPropertyModel) propertyInstance).setValues(values);
      }
      values.add(id);
    }
  }
  
  public IGetVariantInstancesInTableViewModel executeGetVariantInstances(IGetVariantInstancesInTableViewRequestStrategyModel requestModel,
      IBaseKlassTemplateConfigDetails configDetails) throws Exception
  {
    IGetVariantInstancesInTableViewModel variantModel = new GetVariantInstancesInTableViewModel();
    String searchExpression = generateSearchExpression(requestModel.getContextId(), requestModel);
    List<IBaseEntityDTO> allChildrenForGivenContext = getAllChildrenForGivenContext(requestModel, variantModel, searchExpression);
    fillVariantInstancesForTableView(requestModel, variantModel, allChildrenForGivenContext, configDetails);
    variantModel.setFrom(requestModel.getFrom());


    IGetFilterInfoModel filterInfo = requestModel.getFilterInfo();
    getAllUtils.fillCountsForFilter(filterInfo, searchExpression, true);

    variantModel.setFilterInfo(filterInfo);

    return variantModel;
  }
  
  private List<IBaseEntityDTO> getAllChildrenForGivenContext(IGetVariantInstancesInTableViewRequestStrategyModel requestModel,
      IGetVariantInstancesInTableViewModel variantModel, String searchExpression) throws Exception
  {
    List<ISortModel> sortOptionsModel = requestModel.getSortOptions();

    List<ISortDTO> sortOptions = sortOptionsModel.stream().map(x -> new SortDTO(x.getSortField(),
        ISortDTO.SortOrder.valueOf(x.getSortOrder().toLowerCase()),x.getIsNumeric())).collect(Collectors.toList());

    Map<String, OrderDirection> sort = RDBMSUtils.getSortMap(sortOptions);
    IRDBMSOrderedCursor<IBaseEntityDTO> contextualChildren = rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntitiesBySearchExpression(searchExpression, true);
    contextualChildren.setOrderBy(sort);
    variantModel.setTotalContents(contextualChildren.getCount());
    int size = requestModel.getSize() == null ? Math.toIntExact(contextualChildren.getCount()) : requestModel.getSize();
    return contextualChildren.getNext(requestModel.getFrom(), size);
  }

  public void fillVariantInstancesForTableView(
      IGetVariantInstancesInTableViewRequestStrategyModel requestModel,
      IGetVariantInstancesInTableViewModel variantModel,
      List<IBaseEntityDTO> allChildrenForGivenContext, IBaseKlassTemplateConfigDetails configDetails) throws Exception
  {
    Map<String, IReferencedSectionElementModel> referencedElements = requestModel
        .getReferencedElements();
    Map<String, ITag> referencedTags = requestModel.getReferencedTags();
    Map<String, IReferencedVariantContextModel> referencedContexts = requestModel
        .getReferencedVariantContexts();
    String contextId = requestModel.getContextId();
    IReferencedVariantContextModel referencedContext = referencedContexts.get(contextId);
    
    Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements = ((IConfigDetailsForGetVariantInstancesInTableViewModel) configDetails)
        .getInstanceIdVsReferencedElements();
    
    Map<String, IReferencedPropertyCollectionModel> referenecdPC = requestModel
        .getReferencedPropertyCollections();
    
    List<String> allowedEntities = requestModel.getEntities();
    
    List<String> columnIds =  requestModel.getColumnIds();
    Boolean isTimeEnabled = referencedContext.getIsTimeEnabled();
    
    Map<String, IVariantReferencedInstancesModel> referencedInstances = new HashMap<>();
    
    List<IIdParameterModel> columns = getColumns(columnIds, referencedElements, referencedTags,
        referenecdPC, referencedContext, allowedEntities, instanceIdVsReferencedElements);
    variantModel.setColumns(columns);
    
    List<IRowIdParameterModel> rows = getRows(allChildrenForGivenContext, isTimeEnabled, columnIds,
        referencedInstances, allowedEntities, referencedElements, requestModel.getReferencedTags(), configDetails);
    variantModel.setRows(rows);
    variantModel.setReferencedInstances(referencedInstances);
  }
  
  public List<IRowIdParameterModel> getRows(List<IBaseEntityDTO> allChildrenForGivenContext,
      Boolean isTimeEnabled, List<String> columnIds,
      Map<String, IVariantReferencedInstancesModel> referencedInstances, List<String> entities,
      Map<String, IReferencedSectionElementModel> referenecdElements,
      Map<String, ITag> referencedTags, IBaseKlassTemplateConfigDetails configDetails) throws Exception
  {
    List<IRowIdParameterModel> rows = new ArrayList<>();
    Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements = ((IConfigDetailsForGetVariantInstancesInTableViewModel) configDetails)
        .getInstanceIdVsReferencedElements();
    
    Set<String> attributeIdsHavingContext = getAttributeIdsHavingContext(configDetails, instanceIdVsReferencedElements);

    for (IBaseEntityDTO childDTO : allChildrenForGivenContext) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(childDTO);
      KlassInstanceBuilder klassInstanceBuilder = new KlassInstanceBuilder(baseEntityDAO, configDetails.getReferencedAttributes(),
          configDetails.getReferencedTags(), configDetails.getReferencedElements(), rdbmsComponentUtils);
      IBaseEntityDTO baseEntityDTO = klassInstanceBuilder.getBaseEntityDTO();
      baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);

      Map<String, IPropertyInstance> properties = new HashMap<>();
      IRowIdParameterModel rowMap = new RowIdParameterModel();
      rowMap.setId(String.valueOf(childDTO.getBaseEntityIID()));
      rowMap.setCreationLanguage(childDTO.getBaseLocaleID());
      rowMap.setProperties(properties);
      rowMap.getLanguageCodes().addAll(childDTO.getLocaleIds());
      rowMap.setOriginalInstanceId(childDTO.getBaseEntityID());
      rowMap.setAttributeVariantsStats(getAttributeVariantStats(childDTO, configDetails,attributeIdsHavingContext, klassInstanceBuilder));
      if (childDTO.getBaseType().equals(BaseType.ASSET)) {
        rowMap.setAssetInformation(fillAssetInformation(childDTO));
      }
      rows.add(rowMap);
      Set<IPropertyRecordDTO> propertyRecords =  klassInstanceBuilder.getBaseEntityDTO().getPropertyRecords();
      
      if (!columnIds.contains(IStandardConfig.StandardProperty.nameattribute.toString())) {
        columnIds.add(IStandardConfig.StandardProperty.nameattribute.toString());
      }
      
      for (IPropertyRecordDTO propertyRecord : propertyRecords) {
        
        String propertyID = propertyRecord.getProperty()
            .getPropertyCode();
        
        if (!columnIds.contains(propertyID)) {
          continue;
        }
        
        IPropertyInstance propertyInstance = getPropertyInstance(propertyRecord, childDTO, configDetails);
        if (propertyInstance != null) {
          properties.put(propertyID, propertyInstance);
        }
      }
      IContextualDataDTO contextualObject = childDTO.getContextualObject();
      
      // FIX ME: USE DAO AFTER CONTEXTUAL LINKED ENTITIES API IS MODIFIED
      Set<IBaseEntityDTO> contextualLinkedEntities = baseEntityDAO
          .getContextualLinkedEntities();
      fillContextualProperties(isTimeEnabled, contextualObject, properties, referencedInstances,
          contextualLinkedEntities, entities);
      fillContextTagValuesInTableView(properties, getTagValueMappingWithTag(referencedTags),
          childDTO);
      fillLinkedTaxonomies(baseEntityDTO, properties, configDetails);
    }
    return rows;
  }
  
  public void fillLinkedTaxonomies(IBaseEntityDTO childEntityDTO, Map<String, IPropertyInstance> properties,
      IBaseKlassTemplateConfigDetails configDetails)
  {
    Set<IClassifierDTO> otherClassifiers = childEntityDTO.getOtherClassifiers();
    Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies = ((IConfigDetailsForGetVariantInstancesInTableViewModel) configDetails)
        .getReferencedTaxonomies();
    
    for(IClassifierDTO otherClassifier : otherClassifiers) {
      ClassifierType classifierType = otherClassifier.getClassifierType();
      if (isMinorTaxonomy(classifierType, referencedTaxonomies, otherClassifier.getClassifierCode())) {
       String childCode = otherClassifier.getCode();
       String parentCode = getRootTaxonomy(referencedTaxonomies, childCode);
       if(parentCode != null) {
         
         ILinkedTaxonomiesPropertyModel taxonomyModel = (ILinkedTaxonomiesPropertyModel) properties.get(parentCode);
        
         if(taxonomyModel == null) {
           taxonomyModel =  new LinkedTaxonomiesPropertyModel();
           taxonomyModel.setBaseType(Constants.MINOR_TAXONOMY_BASE_TYPE);
           taxonomyModel.setId(parentCode);
           properties.put(parentCode, taxonomyModel);
         }
         
         taxonomyModel.getSelectedTaxonomies().add(childCode);
        }
      }
    }
  }

  private String getRootTaxonomy(Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies,
      String childCode)
  {
    String parent = null;
    for(IReferencedArticleTaxonomyModel referencedTaxonomy : referencedTaxonomies.values()) {
       for(IReferencedTaxonomyChildrenModel children : referencedTaxonomy.getChildren()) {
         if(children.getCode().equals(childCode)) {
           parent = referencedTaxonomy.getCode();
           return parent;
         }
       }
     }
    return parent;
  }
  
  private boolean isMinorTaxonomy(ClassifierType classifierType,
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies, String code)
  {
    return  (classifierType == ClassifierType.MINOR_TAXONOMY
                  || classifierType == ClassifierType.HIERARCHY
                  || classifierType == ClassifierType.TAXONOMY)
        &&
        referencedTaxonomies.get(code).getTaxonomyType().equals(CommonConstants.MINOR_TAXONOMY);
  }

  public IAssetInformationModel fillAssetInformation(IBaseEntityDTO baseEntityDTO) throws Exception
  {
    String entityExtension = baseEntityDTO.getEntityExtension().toString();
    IAssetInformationModel assetInformation = (IAssetInformationModel) ObjectMapperUtil.readValue(entityExtension,
        AssetInformationModel.class);
    if(assetInformation == null)
    {
      return null;
    }
    assetInformation.setHash(baseEntityDTO.getHashCode());
    return assetInformation;
  }

  public Map<String, IAttributeVariantsStatsModel> getAttributeVariantStats(IBaseEntityDTO childDTO,
      IBaseKlassTemplateConfigDetails configDetails, Set<String> attributeIdsHavingContext, KlassInstanceBuilder klassInstanceBuilder) throws Exception
  {
    Map<String, IAttributeVariantsStatsModel> attributeVariantStats = new HashMap<String, IAttributeVariantsStatsModel>();
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();


    for(String attributeId : attributeIdsHavingContext) {
      IAttribute referencedAttribute = referencedAttributes.get(attributeId);
      IAttributeVariantsStatsModel attributeVariantStat = klassInstanceBuilder.getAttributeVariantStats(referencedAttribute);
      if (attributeVariantStat != null) {
        attributeVariantStats.put(referencedAttribute.getId(), attributeVariantStat);
      }
    }
    return attributeVariantStats;
  }
  
  public Set<String> getAttributeIdsHavingContext(IBaseKlassTemplateConfigDetails configDetails,
      Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements) throws Exception
  {
    Set<String> attributeIdsHavingContext = new HashSet<>();
    for(Entry<String, IAttribute> entry : configDetails.getReferencedAttributes().entrySet()) {
      IReferencedSectionElementModel referenceElement = configDetails.getReferencedElements().get(entry.getKey());
      if(referenceElement == null) {
        continue;
      }
      
      for(Map<String, IReferencedSectionElementModel> referencedMap : instanceIdVsReferencedElements.values()) {
      IReferencedSectionElementModel referencedSectionModel = referencedMap.get(entry.getKey());
     
      if(referencedSectionModel != null) {
      String attributeVariantContext = referencedSectionModel.getAttributeVariantContext();
      if(attributeVariantContext != null && !attributeVariantContext.trim().equals("")) {
        attributeIdsHavingContext.add(referenceElement.getId()); 
         }
       }
     }
    }
    
    return attributeIdsHavingContext;
  }

  public void fillContextTagValuesInTableView(Map<String, IPropertyInstance> properties,
      Map<String, String> tagValueMappingWithTag, IBaseEntityDTO childDTO)
  {
    IContextualDataDTO contextualObject = childDTO.getContextualObject();
    Set<ITagDTO> contextTagValues = contextualObject.getContextTagValues();
    
    for (ITagDTO contextTagValue : contextTagValues) {
      String tagValueID = contextTagValue.getTagValueCode();
      String tagId = tagValueMappingWithTag.get(tagValueID);
      ITagInstance propertyInstance = (ITagInstance) properties.get(tagId);
      if (propertyInstance == null) {
        propertyInstance = new TagInstance();
        propertyInstance.setId(contextTagValue.getTagValueCode() + Seperators.UNDERSCORE_SEPERATOR
            + contextualObject.getContextualObjectIID());
        propertyInstance.setTagId(tagId);
        propertyInstance.setContextInstanceId(String.valueOf(childDTO.getBaseEntityIID()));
        propertyInstance.setVariantInstanceId(String.valueOf(childDTO.getBaseEntityIID()));
        propertyInstance.setKlassInstanceId(String.valueOf(childDTO.getParentIID()));
        properties.put(tagId, propertyInstance);
      }
      List<ITagInstanceValue> tagValues = ((ITagInstance) propertyInstance).getTagValues();
      
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setTagId(contextTagValue.getTagValueCode());
      tagInstanceValue.setId(contextTagValue.getTagValueCode());
      tagInstanceValue.setCode(contextTagValue.getTagValueCode());
      tagInstanceValue.setRelevance(contextTagValue.getRange());
      tagValues.add(tagInstanceValue);
    }
  }
  
  public IPropertyInstance getPropertyInstance(IPropertyRecordDTO propertyRecord, IBaseEntityDTO childDTO,
      IBaseKlassTemplateConfigDetails configDetails)
  {
    PropertyType propertyType = propertyRecord.getProperty()
        .getPropertyType();
    IPropertyInstance propertyInstance = null;
    
    if(propertyType.equals(PropertyType.RELATIONSHIP)
        || propertyType.equals(PropertyType.NATURE_RELATIONSHIP)
        || propertyType.equals(PropertyType.UNDEFINED)) {
      return propertyInstance;
    }
    if (propertyRecord instanceof IValueRecordDTO) {
      propertyInstance = fillAttributeInstance(propertyRecord, childDTO, configDetails);
      propertyInstance.setId(KlassInstanceBuilder.getAttributeID((IValueRecordDTO)propertyRecord));
    }
    else if (propertyRecord instanceof ITagsRecordDTO) {
      propertyInstance = fillTagInstance(propertyRecord);
      propertyInstance.setId(((ITagsRecordDTO)propertyRecord).getNodeID());
    }
    
    propertyInstance.setKlassInstanceId(childDTO.getBaseEntityID());
		/*
		 * propertyInstance.setId(String.valueOf(propertyRecord.getProperty()
		 * .getPropertyIID()));
		 */
    propertyInstance.setIid(propertyRecord.getProperty()
        .getIID());
    return propertyInstance;
  }
  
  private IPropertyInstance fillAttributeInstance(IPropertyRecordDTO propertyRecord,
      IBaseEntityDTO childDTO, IBaseKlassTemplateConfigDetails configDetails)
  {
    IAttributeInstance propertyInstance = new AttributeInstance();
    IValueRecordDTO valueRecordDTO = (IValueRecordDTO) propertyRecord;
    propertyInstance.setId(KlassInstanceBuilder.getAttributeID(valueRecordDTO));
    
    switch (propertyRecord.getProperty()
        .getPropertyType()) {
      case BOOLEAN:
        return fillTagInstance(propertyRecord);
      case MEASUREMENT:
        ((IAttributeInstance) propertyInstance).setValueAsNumber(valueRecordDTO.getAsNumber());
        break;
      case HTML:
        ((IAttributeInstance) propertyInstance).setValueAsHtml(valueRecordDTO.getAsHTML());
        ((IAttributeInstance) propertyInstance).setLanguage(valueRecordDTO.getLocaleID());
        break;
      case NUMBER:
        ((IAttributeInstance) propertyInstance).setValueAsNumber(valueRecordDTO.getAsNumber());
        ((IAttributeInstance) propertyInstance).setValue(valueRecordDTO.getValue());
        break;
      case DATE:
        ((IAttributeInstance) propertyInstance).setValueAsNumber(valueRecordDTO.getAsNumber());
        break;
      case PRICE:
        ((IAttributeInstance) propertyInstance).setValueAsNumber(valueRecordDTO.getAsNumber());
        break;
      case TEXT:
        ((IAttributeInstance) propertyInstance).setLanguage(valueRecordDTO.getLocaleID());
        break;
      case ASSET_ATTRIBUTE:
        break;
        
      case CONCATENATED:
    	  fillValueAsExpression(propertyRecord, childDTO, configDetails, propertyInstance, valueRecordDTO);
      break;
    }
    ((IAttributeInstance) propertyInstance).setValue(valueRecordDTO.getValue());
    ((IAttributeInstance) propertyInstance).setAttributeId(valueRecordDTO.getProperty()
        .getPropertyCode());
    ((IAttributeInstance) propertyInstance).setOriginalInstanceId(childDTO.getBaseEntityID());
    return propertyInstance;
  }

private void fillValueAsExpression(IPropertyRecordDTO propertyRecord, IBaseEntityDTO childDTO,
		IBaseKlassTemplateConfigDetails configDetails, IAttributeInstance propertyInstance,
		IValueRecordDTO valueRecordDTO) {
	Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
	  Map<String, ITag> referencedTags = configDetails.getReferencedTags();
	  String expression = valueRecordDTO.getCalculation();
	  IAttribute referencedAttribute = referencedAttributes.get(propertyRecord.getProperty().getPropertyCode());
	  List<IConcatenatedOperator> attributeConcatenatedList = ((IConcatenatedAttribute) referencedAttribute).getAttributeConcatenatedList();
	    
	    Map<Integer, String> userTextMap = new HashMap<>();
	    for (IConcatenatedOperator attributeConcatenated : attributeConcatenatedList) {
	      if (attributeConcatenated.getType()
	          .equals("html")) {
	        userTextMap.put(attributeConcatenated.getOrder(), attributeConcatenated.getCode());
	      }
	    }
	    
	    String expressionIfExist = expression.replace("{", "")
	        .replace("}", "");
	    
	    if (!expressionIfExist.isEmpty()) {
	      String[] expressionSplit = expressionIfExist.split("=",2);
	      String expressionAfterEqual = expressionSplit[1];
	      String expressionRemovalOfSpecialChar = null;
	      int order = 0;
	      
	      if (expressionAfterEqual.contains("[") || expressionAfterEqual.contains("'") || expressionAfterEqual.contains("}")) {
	        expressionRemovalOfSpecialChar = expressionAfterEqual.replace("[", "")
	            .replace("]", "")
	            .replace("'", "")
	            .replace("\"", "")
	            .replace("}", "");
	      }
	      expressionRemovalOfSpecialChar = StringEscapeUtils.unescapeHtml4(expressionRemovalOfSpecialChar);
	      String[] expressionSubStrings = expressionRemovalOfSpecialChar.split("\\|\\|");
	      List<IConcatenatedOperator> valueAsExpression = new ArrayList<>();
	      for (String expressionSubString : expressionSubStrings) {
	        long propertyIID = 0;
	        IAttribute attributeValue = null;
	        ITag iTag = null;
	        
	        if (referencedAttributes.containsKey(expressionSubString)) {
	          attributeValue = referencedAttributes.get(expressionSubString);
	          propertyIID = attributeValue.getPropertyIID();
	        }
	        else if (referencedTags.containsKey(expressionSubString)) {
	          iTag = referencedTags.get(expressionSubString);
	          propertyIID = iTag.getPropertyIID();
	        }
	        
	        if (propertyIID != 0) {
	          ConcatenatedAttributeOperator concatenatedOperator = new ConcatenatedAttributeOperator();
	          IPropertyRecordDTO valueRecord = childDTO.getPropertyRecord(propertyIID);
	          
	          if (valueRecord != null && valueRecord instanceof IValueRecordDTO) {
	            IValueRecordDTO ValueRecordDto = (IValueRecordDTO) valueRecord;
	            concatenatedOperator.setCode(expressionSubString);
	            concatenatedOperator.setId(Long.toString(ValueRecordDto.getProperty()
	                .getPropertyIID()));
	            concatenatedOperator.setAttributeId(attributeValue.getId());
	            concatenatedOperator.setType("attribute");
	            concatenatedOperator.setOrder(order);
	            valueAsExpression.add(concatenatedOperator);
	          }
	          else if (valueRecord != null && valueRecord instanceof ITagsRecordDTO) {
	            ConcatenatedTagOperator concatenatedTagOperator = new ConcatenatedTagOperator();
	            ITagsRecordDTO tagRecordDto = (ITagsRecordDTO) valueRecord;
	            concatenatedTagOperator.setCode(expressionSubString);
	            concatenatedTagOperator.setId(tagRecordDto.getNodeID());
	            concatenatedTagOperator.setTagId(iTag.getId());
	            concatenatedTagOperator.setType("tag");
	            concatenatedTagOperator.setOrder(order);
	            valueAsExpression.add(concatenatedTagOperator);
	          }
	        }
	        else {
	          IConcatenatedHtmlOperator concatenatedOperator = new ConcatenatedHtmlOperator();
	          concatenatedOperator.setValue(expressionSubString);//TODO: Temporary fix, need proper solution 
	          concatenatedOperator.setType("html");
	          concatenatedOperator.setValueAsHtml(expressionSubString);
	          concatenatedOperator.setOrder(order);
	          String id = userTextMap.get(order);
	          concatenatedOperator.setId(id);
	          concatenatedOperator.setCode(id);
	          
	          valueAsExpression.add(concatenatedOperator);
	        }
	        order++;
	      }
	      
	      propertyInstance.setValueAsExpression(valueAsExpression);
	    }
}
  
  private ITagInstance fillTagInstance(IPropertyRecordDTO propertyRecord)
  {
    ITagInstance tagInstance = new TagInstance();
    tagInstance.setId(Long.toString(propertyRecord.getProperty()
        .getIID()));
    tagInstance.setTagId(propertyRecord.getProperty()
        .getCode());
    
    List<ITagInstanceValue> tagInstanceValues = new ArrayList<ITagInstanceValue>();
    
    PropertyType propertyType = propertyRecord.getProperty()
        .getPropertyType();
    
    /* if (propertyType.equals(PropertyType.BOOLEAN)) {
      IValueRecordDTO valueRecordDto = (IValueRecordDTO) propertyRecord;
      ITagInstanceValue tagInstanceValue = new TagInstanceValue();
      tagInstanceValue.setId(Long.toString(valueRecordDto.getValueIID()));
      tagInstanceValue.setCode(Long.toString(valueRecordDto.getValueIID()));
      int relevance = 0;
      if (valueRecordDto.getValue()
          .equals(IStandardConfig.TRUE) && valueRecordDto.getAsNumber() == 1) {
        relevance = 100;
      }
      tagInstanceValue.setRelevance(relevance);
      tagInstanceValues.add(tagInstanceValue);
      tagInstance.setTagValues(tagInstanceValues);
    }
    else {*/
      ITagsRecordDTO tagsRecordDTO = (ITagsRecordDTO) propertyRecord;
      
      tagsRecordDTO.getTags()
          .forEach(tagRecordDTO -> {
            ITagInstanceValue tagInstanceValue = new TagInstanceValue();
            tagInstanceValue.setTagId(tagRecordDTO.getTagValueCode());
            tagInstanceValue.setId(tagRecordDTO.getTagValueCode());
            tagInstanceValue.setCode(tagRecordDTO.getTagValueCode());
            tagInstanceValue.setRelevance(tagRecordDTO.getRange());
            tagInstanceValues.add(tagInstanceValue);
          });
      tagInstance.setTagValues(tagInstanceValues);
    //}
    return tagInstance;
  }
  
  public List<IIdParameterModel> getColumns(List<String> columnIds,
      Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, ITag> referencedTags,
      Map<String, IReferencedPropertyCollectionModel> referenecdPC,
      IReferencedVariantContextModel referencedContext, List<String> allowedEntities, Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements)
  {
    List<IReferencedVariantContextTagsModel> contextTags = referencedContext.getTags();
    List<IIdParameterModel> columns = new ArrayList<>();
    
    if (columnIds.isEmpty()) {
      fillColumnsIfRequestColumnsIsEmpty(referencedElements, referencedContext, referenecdPC,
          contextTags, columns, columnIds, allowedEntities, instanceIdVsReferencedElements);
    }
    else {
      for (String columnId : columnIds) {
        Boolean isContextualColumn = fillContextualColumns(columns, columnId, referencedContext);
        if (isContextualColumn) {
          continue;
        }
        String type = getTypeForColumn(referencedElements, referencedTags, columnId, instanceIdVsReferencedElements);
        if (type != null) {
          IIdParameterModel map = new IdParameterModel();
          map.setId(columnId);
          map.setType(type);
          columns.add(map);
        }
      }
    }
    return columns;
  }
  
  private String getTypeForColumn(Map<String, IReferencedSectionElementModel> referencedElements,
      Map<String, ITag> referencedTags, String columnId, Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements)
  {
    String type = null;
    IReferencedSectionElementModel elementMap = referencedElements.get(columnId);
    if (elementMap == null) {
      ITag refTag = referencedTags.get(columnId);
      if (refTag != null)
        type = CommonConstants.TAG;
    }
    else {
      Boolean isAnyUnskipped = getIsAnyUnskipped(instanceIdVsReferencedElements, columnId);
      if (isAnyUnskipped) {
        type = elementMap.getType();
      }
    }
    return type;
  }
  
  public void fillColumnsIfRequestColumnsIsEmpty(
      Map<String, IReferencedSectionElementModel> referencedElements, IReferencedVariantContextModel referencedContext,
      Map<String, IReferencedPropertyCollectionModel> referenecdPC,
      List<IReferencedVariantContextTagsModel> contextTags, List<IIdParameterModel> columns,
      List<String> columnIds, List<String> entitiesHavingRP, Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements)
  {
    Boolean isTimeEnabled = referencedContext.getIsTimeEnabled();
    
    if (isTimeEnabled) {
      putTimeColumnEntry(columns, IInstanceTimeRange.FROM);
      putTimeColumnEntry(columns, IInstanceTimeRange.TO);
    }
    
    for (IReferencedVariantContextTagsModel tagMap : contextTags) {
      IIdParameterModel contextTagMap = new IdParameterModel();
      String tagId = (String) tagMap.getTagId();
      contextTagMap.setId(tagId);
      contextTagMap.setType(CommonConstants.TAG);
      columns.add(contextTagMap);
      columnIds.add(tagId);
    }
    
    List<String> entities = (List<String>) referencedContext.getEntities();
    if (entities != null && !entities.isEmpty()) {
      entities.retainAll(entitiesHavingRP);
      putLinkedInstancesColumnEntry(columns, entities);
    }
    
    for (IReferencedPropertyCollectionModel propertyCollection : referenecdPC.values()) {
      List<IReferencedPropertyCollectionElementModel> elements = propertyCollection
          .getElements();
      for (IReferencedPropertyCollectionElementModel elementMap : elements) {
        String elementId = (String) elementMap.getId();

        
        Boolean isAnyUnskipped = getIsAnyUnskipped(instanceIdVsReferencedElements, elementId);
        
        if (isAnyUnskipped && !columnIds.contains(elementId)) {
          IIdParameterModel map = new IdParameterModel();
          map.setId(elementId);
          map.setType(referencedElements.get(elementId).getType());
          columns.add(map);
          columnIds.add(elementId);
        }
      }
    }
  }

  private boolean getIsAnyUnskipped(
      Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements,
      String elementId)
  {
    
    for(Map<String, IReferencedSectionElementModel> referencedElements : instanceIdVsReferencedElements.values()) {
      IReferencedSectionElementModel element = referencedElements.get(elementId);
      if(element!= null && !element.getIsSkipped()) {
          return true;
      }
    }
    return false;
  }
  
  public void skipAttributesHavingAttributeContexts(List<IIdParameterModel> columns,
      Map<String, IReferencedSectionElementModel> referencedElements)
  {
    List<String> attributesToSkip = new ArrayList<>();
    for (Entry<String, IReferencedSectionElementModel> entry : referencedElements.entrySet()) {
      String contextId = entry.getValue()
          .getAttributeVariantContext();
      if (contextId != null) {
        attributesToSkip.add(entry.getKey());
      }
    }
    List<IIdParameterModel> columnsToSkip = new ArrayList<>();
    for (IIdParameterModel column : columns) {
      if (attributesToSkip.contains(column.getId())) {
        columnsToSkip.add(column);
      }
    }
    columns.removeAll(columnsToSkip);
  }
  
  public String getNewVariantNameForCreate(Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses, String typeId,
      Long counter, String language)
  {
    IReferencedKlassDetailStrategyModel iReferencedKlassDetailStrategyModel = referencedKlasses
        .get(typeId);
    String languageCode = language;
    return iReferencedKlassDetailStrategyModel.getLabel() + " " + counter+"_context_"+ languageCode;
  }
  
  public IBaseEntityDTO createVariantBaseEntity(IGetConfigDetailsForCustomTabModel configDetails,
      ICreateVariantModel createInstanceModel, BaseType baseType) throws Exception
  {
    if (Constants.ASSET_INSTANCE_BASE_TYPE.equals(createInstanceModel.getBaseType())) {
      TransactionData transactionData = transactionThread.getTransactionData();
      Map<String, Object> transactionDataMap = new HashMap<>();
      transactionDataMap.put("physicalCatalogId", transactionData.getPhysicalCatalogId());
      transactionDataMap.put("dataLanguage", transactionData.getDataLanguage());
      transactionDataMap.put(ITransactionData.ORGANIZATION_ID, transactionData.getOrganizationId());
      transactionDataMap.put("userId", transactionData.getUserId());
      transactionDataMap.put("localeId", transactionData.getDataLanguage());
      transactionDataMap.put(ITransactionData.ENDPOINT_ID, transactionData.getEndpointId());
      VariantInstanceCreation variantInstanceCreationInstance = VariantInstanceCreation.getInstance();
      variantInstanceCreationInstance.initializeRDBMSComponents(transactionDataMap);
      IBaseEntityDTO baseEntityDTO = variantInstanceCreationInstance.createVariantBaseEntity(configDetails, createInstanceModel, baseType);
      
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);
      Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
      Map<String, ITag> referencedTags = configDetails.getReferencedTags();
      IReferencedVariantContextModel variantContext = configDetails.getReferencedVariantContexts()
          .getEmbeddedVariantContexts().get(createInstanceModel.getContextId());
      fillContextualData(baseEntityDTO, variantContext, referencedTags, referencedElements, baseEntityDAO);
      RuleHandler ruleHandler = new RuleHandler();
      ruleHandler.initiateRuleHandling(baseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO(), false, referencedElements,
          configDetails.getReferencedAttributes(), referencedTags);
      return baseEntityDTO; 
    }
    
    String baseEntityID = createInstanceModel.getVariantInstanceId() != null
        ? createInstanceModel.getVariantInstanceId()
        : RDBMSUtils.newUniqueID( baseType.getPrefix());
    
    IReferencedKlassDetailStrategyModel natureKlass = configDetails.getReferencedKlasses().get(createInstanceModel.getType());
    
    IReferencedVariantContextModel variantContext = configDetails
        .getReferencedVariantContexts()
        .getEmbeddedVariantContexts()
        .get(createInstanceModel.getContextId());
    
    ContextType contextType = ContextUtil.getContextTypeByType(variantContext.getType());
    IContextDTO contextDto = RDBMSUtils.getContextDTO(variantContext.getCode(), contextType);
    IClassifierDTO classifierDTO = new ClassifierDTO(natureKlass.getClassifierIID(), natureKlass.getCode(), ClassifierType.CLASS);

    IBaseEntityDTO parentDTO = rdbmsComponentUtils.getBaseEntityDTO(Long.parseLong(createInstanceModel.getParentId()));
    IBaseEntityDAO parentDAO = rdbmsComponentUtils.getBaseEntityDAO(parentDTO);
    
    
    IBaseEntityDTO newBaseEntityDTO = rdbmsComponentUtils.getLocaleCatlogDAO()
        .newBaseEntityDTOBuilder(baseEntityID, baseType, classifierDTO)
        .endpointCode(transactionThread.getTransactionData().getEndpointId())
        .contextDTO(contextDto)
        .build();
    createInstanceModel.setVariantInstanceId(newBaseEntityDTO.getBaseEntityID());
    String baseTypeString = createInstanceModel.getBaseType();
    
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(newBaseEntityDTO);
    if (Arrays.asList(subBaseTypes).contains(baseTypeString)) {
      Map<String, Object> subType = new HashMap<>();
      subType.put("subType", baseTypeString);
      newBaseEntityDTO.setEntityExtension(ObjectMapperUtil.writeValueAsString(subType));
    }
    
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    IBaseEntityDTO baseEntityDTO = baseEntityDAO.getBaseEntityDTO();
    
    fillContextualData(baseEntityDTO, variantContext, referencedTags, referencedElements,
        baseEntityDAO);
    
    List<Long> collect = createInstanceModel.getLinkedInstances()
        .stream()
        .map(x -> Long.parseLong(x.getId()))
        .collect(Collectors.toList());
    
    PropertyRecordBuilder propertyRecordBuilder = new PropertyRecordBuilder(baseEntityDAO,
        configDetails, PropertyRecordType.CREATE, rdbmsComponentUtils.getLocaleCatlogDAO());
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    
    Boolean shouldUpdateEntity = false;
    contextualObject.getLinkedBaseEntityIIDs()
        .addAll(collect);
    List<IContentTagInstance> contextTags = createInstanceModel.getContextTags();
    shouldUpdateEntity = propertyRecordBuilder.setContextTimeRange(createInstanceModel.getTimeRange(), contextualObject);
    
    if (!contextTags.isEmpty()) {
      propertyRecordBuilder.createContextTags(contextTags, contextualObject);
      shouldUpdateEntity = true;
    }
    if (shouldUpdateEntity) {
      String contextCode = contextualObject.getContext().getContextCode();
      IReferencedVariantContextModel referencedContext = configDetails.getReferencedVariantContexts()
          .getEmbeddedVariantContexts().get(contextCode);
      checkDuplicate(baseEntityDAO,parentDTO.getBaseEntityIID(), referencedContext, true, Long.parseLong(createInstanceModel.getId()));
    }
    IPropertyRecordDTO[] propertyRecords = CreateInstanceUtils
        .createPropertyRecordInstance(baseEntityDAO, configDetails, createInstanceModel, rdbmsComponentUtils.getLocaleCatlogDAO());
    baseEntityDAO.createPropertyRecords(propertyRecords);
    
    baseEntityDTO = rdbmsComponentUtils.getLocaleCatlogDAO().loadLocaleIds(baseEntityDTO);
    
    rdbmsComponentUtils.createNewRevision(baseEntityDTO, configDetails.getNumberOfVersionsToMaintain());
	  parentDAO.addChildren(EmbeddedType.CONTEXTUAL_CLASS, newBaseEntityDTO);
    baseEntityDAO.updatePropertyRecords(new PropertyRecordDTO[] {});
    RuleHandler ruleHandler = new RuleHandler();
    ruleHandler.initiateRuleHandling(baseEntityDAO, rdbmsComponentUtils.getLocaleCatlogDAO(), false, referencedElements,
        configDetails.getReferencedAttributes(), referencedTags);
    return rdbmsComponentUtils.getBaseEntityDTO(baseEntityDTO.getBaseEntityIID());
  }
 
  private void fillContextualData(IBaseEntityDTO baseEntityDTO,
      IReferencedVariantContextModel variantContextModel, Map<String, ITag> referencedTags,
      Map<String, IReferencedSectionElementModel> referencedElements, IBaseEntityDAO baseEntityDAO)
      throws Exception
  {
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    IDefaultTimeRange defaultTimeRange = variantContextModel.getDefaultTimeRange();
    if (defaultTimeRange != null) {
      contextualObject
          .setContextEndTime(defaultTimeRange.getTo() != null ? defaultTimeRange.getTo() : 0);
      contextualObject
          .setContextStartTime(defaultTimeRange.getFrom() != null ? defaultTimeRange.getFrom() : 0);
    }
    fillContextTagValues(variantContextModel, referencedTags, referencedElements, baseEntityDAO,
        contextualObject);
  }
  
  private void fillContextTagValues(IReferencedVariantContextModel variantContextModel,
      Map<String, ITag> referencedTags,
      Map<String, IReferencedSectionElementModel> referencedElements, IBaseEntityDAO baseEntityDAO,
      IContextualDataDTO contextualObject) throws RDBMSException
  {
    List<IReferencedVariantContextTagsModel> tags = variantContextModel.getTags();
    Set<ITagDTO> contextTagValues = new HashSet<>();
    for (IReferencedVariantContextTagsModel tag : tags) {
      String tagId = tag.getTagId();
      
      IReferencedSectionElementModel referencedElement = referencedElements.get(tagId);
      // if default value is not there no need to create contextual tag
      if (referencedElement == null) {
        continue;
      }
      List<IIdRelevance> defaultValue = ((ReferencedSectionTagModel) referencedElement)
          .getDefaultValue();
      
      if (defaultValue.isEmpty()) {
        continue;
      }
      
      List<String> defaultValueIds = defaultValue.stream()
          .map(defaultVal -> defaultVal.getId())
          .collect(Collectors.toList());
      
      ITag referencedTag = referencedTags.get(tagId);
      List<ITagValue> tagValues = referencedTag.getTagValues();
      
      for (ITagValue tagValue : tagValues) {
        long propertyIID = referencedTag.getPropertyIID();
        int relevance = defaultValueIds.contains(tagValue.getId()) ? 100 : 0;
        contextTagValues.add(baseEntityDAO.newTagDTO(relevance, tagValue.getCode()));
      }
    }
    contextualObject.setContextTagValues(contextTagValues.toArray(new TagDTO[contextTagValues.size()]));
  }
  
  public void checkDuplicate(IBaseEntityDAO workingObject, long parentIID,
      IReferencedVariantContextModel referencedContext, Boolean isContextModified, long contextParentId) throws Exception
  {
    if (referencedContext == null)
      return;
    
    Boolean isDuplicateVariantAllowed = referencedContext.getIsDuplicateVariantAllowed();
    
    IBaseEntityDTO baseEntityDTO = workingObject.getBaseEntityDTO();
    
    IContextualDataDTO context = baseEntityDTO.getContextualObject();
    String contextId = (String) context.getContext()
        .getContextCode();
    
    // context field check
    checkContextFieldsExists(context.getContextTagValues(), context, referencedContext);
    
    if ((isDuplicateVariantAllowed != null && isDuplicateVariantAllowed) || !isContextModified) {
      return;
    }
    if(workingObject.isEmbeddedEntityDuplicate(parentIID)){
      throw new DuplicateVariantExistsException();
    }
  }
  
  public void checkContextFieldsExists(Set<ITagDTO> set, IContextualDataDTO context,
      IReferencedVariantContextModel referencedContext) throws Exception
  {
    Boolean isTimeEnabled = referencedContext.getIsTimeEnabled();
    List<IReferencedVariantContextTagsModel> tags = referencedContext.getTags();
    
    // is contextTags is empty then isContextTagSelecteds = true as no need to
    // check it
    Boolean isContextTagSelecteds = tags.isEmpty();
    
    List<String> relevance100TagValue = set.stream()
        .filter(x -> x.getRange() == 100)
        .map(x -> x.getTagValueCode())
        .collect(Collectors.toList());
    
    for (IReferencedVariantContextTagsModel tag : tags) {
      isContextTagSelecteds = !ListUtils.intersection(relevance100TagValue, tag.getTagValueIds())
          .isEmpty();
      if(isContextTagSelecteds) break;
    }
    
    // If from date and to date column are empty then default values are set.
    IDefaultTimeRange defaultTimeRange = referencedContext.getDefaultTimeRange();
    if (defaultTimeRange != null) {
      Boolean isCurrentTime = defaultTimeRange.getIsCurrentTime();
      
      if (context.getContextStartTime() == 0) {
        if (isCurrentTime != null && isCurrentTime) {
          context.setContextStartTime(new Date().getTime());
        }
        else if (defaultTimeRange.getFrom() != null) {
          context.setContextEndTime(defaultTimeRange.getFrom());
        }
      }
      
      if (context.getContextEndTime() == 0 && defaultTimeRange.getTo() != null) {
        context.setContextEndTime(defaultTimeRange.getTo());
      }
    }
    // if context is not time enabled then no need to check context
    Boolean isContextTimeRangeEntered = !isTimeEnabled;
    if (isTimeEnabled && context.getContextStartTime() > 0 && context.getContextEndTime() > 0) {
      validateTimeRange(context);
      isContextTimeRangeEntered = true;
    }
    
    if (!isContextTagSelecteds || !isContextTimeRangeEntered) {
      throw new EmptyMandatoryFieldsException();
    }
  }
  
  public void checkDuplicateVariant(IBaseEntityDTO baseEntityDTO, String contextId,
      IReferencedVariantContextModel context, long contextParentId) throws Exception
  {
    IContextualDataDTO currentContextualVariantObject = baseEntityDTO.getContextualObject();
    validateTimeRange(currentContextualVariantObject.getContextStartTime(),
        currentContextualVariantObject.getContextEndTime());

    long currentVariantID = baseEntityDTO.getBaseEntityIID();
    List<IBaseEntityDTO> similarVariantInstances = getSimilarVariantInstances(baseEntityDTO, context, contextParentId);


    for (IBaseEntityDTO similarVariantInstance : similarVariantInstances) {
      long similarVariantInstanceId = similarVariantInstance.getBaseEntityIID();
      // if during save same tag is selected again
      if (currentVariantID != 0 && similarVariantInstanceId == currentVariantID) {
        continue;
      }
      IContextualDataDTO contextualObject = similarVariantInstance.getContextualObject();

      if (checkIfRangeIsSame(currentContextualVariantObject, contextualObject)) {
        throw new DuplicateVariantExistsException();
      }
      else if (checkIfSupersetRangeExist(currentContextualVariantObject, contextualObject)) {
        // If variant being created/updated is external and one which is being
        // found as superset is non external,
        // then do nothing..
        throw new DuplicateVariantExistsException();
      }
      else if (checkIfSubsetRangeExist(currentContextualVariantObject, contextualObject)) {
        // If variant being created/updated is external and one which is being
        // found as subset is non external
        // then add to subsets array to adapt properties values, else throw
        // exception..
        throw new DuplicateVariantExistsException();
      }
      else if (checkIfOverlapRangeExist(currentContextualVariantObject, contextualObject)) {
        // If variant being created/updated is external and one which is being
        // found as overlapping is non external,
        // then ignore overlapping variants as we cannot adapt the properties
        // values.
        throw new DuplicateVariantExistsException();
      }
      else {
        // Means totally different timeRange.. Do nothing..
        continue;
      }
    }
  }
  
  public void validateTimeRange(IContextualDataDTO context) throws InvalidTimeRangeException
  {
    if (context == null) {
      return;
    }
    Long from = (Long) context.getContextStartTime();
    Long to = (Long) context.getContextEndTime();
    
    if ((from != null && to != null && from > to) || (from == null && to != null)
        || (from != null && to == null)) {
      throw new InvalidTimeRangeException();
    }
  }
  
  public void validateTimeRange(Long from, Long to) throws InvalidTimeRangeException
  {
    if ((from != null && to != null && from > to) || (from == null && to != null)
        || (from != null && to == null)) {
      throw new InvalidTimeRangeException();
    }
  }
  
  public Boolean checkIfRangeIsSame(IContextualDataDTO timeRange1, IContextualDataDTO timeRange2)
      throws Exception
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    // if all became null then time range is equal
    if (from1 == 0 && from2 == 0 && to1 == 0 && to2 == 0) {
      return true;
    }
    
    // if all are not null and any became null then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    
    return (checkIfDateIsSame(from1, from2) && checkIfDateIsSame(to1, to2));
  }
  
  public Boolean checkIfSupersetRangeExist(IContextualDataDTO timeRange1,
      IContextualDataDTO timeRange2) throws Exception
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    
    validateTimeRange(from1, to1);
    validateTimeRange(from2, to2);
    // if all are not 0 and any became 0 then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    
    return ((checkIfDateIsSame(from1, from2) || checkIfDateIsInBetween(from1, from2, to1))
        && (checkIfDateIsSame(to1, to2) || checkIfDateIsInBetween(from1, to2, to1)));
  }
  
  public Boolean checkIfDateIsSame(Long date1, Long date2)
  {
    Date d1 = new Date(date1);
    Calendar calender1 = Calendar.getInstance();
    calender1.setTime(d1);
    int day1 = calender1.get(Calendar.DAY_OF_MONTH);
    int month1 = calender1.get(Calendar.MONTH);
    int year1 = calender1.get(Calendar.YEAR);
    
    Date d2 = new Date(date2);
    Calendar calender2 = Calendar.getInstance();
    calender2.setTime(d2);
    int day2 = calender2.get(Calendar.DAY_OF_MONTH);
    int month2 = calender2.get(Calendar.MONTH);
    int year2 = calender2.get(Calendar.YEAR);
    
    if (day1 == day2 && month1 == month2 && year1 == year2) {
      return true;
    }
    return false;
  }
  
  public Boolean checkIfDateIsInBetween(Long date1, Long checkDate, Long date2)
  {
    Date d1 = new Date(date1);
    Calendar calender1 = Calendar.getInstance();
    calender1.setTime(d1);
    int day1 = calender1.get(Calendar.DAY_OF_MONTH);
    int month1 = calender1.get(Calendar.MONTH);
    int year1 = calender1.get(Calendar.YEAR);
    
    Date d2 = new Date(date2);
    Calendar calender2 = Calendar.getInstance();
    calender2.setTime(d2);
    int day2 = calender2.get(Calendar.DAY_OF_MONTH);
    int month2 = calender2.get(Calendar.MONTH);
    int year2 = calender2.get(Calendar.YEAR);
    
    Date d3 = new Date(checkDate);
    Calendar calender3 = Calendar.getInstance();
    calender3.setTime(d3);
    int checkDay = calender3.get(Calendar.DAY_OF_MONTH);
    int checkMonth = calender3.get(Calendar.MONTH);
    int checkYear = calender3.get(Calendar.YEAR);
    
    if ((day1 == checkDay && month1 == checkMonth && year1 == checkYear)
        || (day2 == checkDay && month2 == checkMonth && year2 == checkYear)) {
      return false;
    }
    if (calender3.after(calender1) && calender3.before(calender2)) {
      return true;
    }
    return false;
  }
  
  public List<IBaseEntityDTO> getSimilarVariantInstances(IBaseEntityDTO baseEntityDTO,
      IReferencedVariantContextModel context, long contextParentId) throws Exception
  {
    IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();
    Set<ITagDTO> contextTagValues = contextualObject.getContextTagValues();
    
    Set<String> relevantTagValueIds = get100RelevanceContextTagValueIDs(contextTagValues);
    
    long parentIID = baseEntityDTO.getParentIID() == 0 ? contextParentId : baseEntityDTO.getParentIID();
    String searchExpression = generateSearchExpressionForDuplicate(context.getCode(), parentIID);
    IRDBMSOrderedCursor<IBaseEntityDTO> contextualChildren = rdbmsComponentUtils.getLocaleCatlogDAO().getAllEntitiesBySearchExpression(searchExpression, true);
    
    Long count = contextualChildren.getCount();
    
    List<IBaseEntityDTO> similiarVariants = new ArrayList<>();
    List<IBaseEntityDTO> variants = contextualChildren.getNext(0, count.intValue());
    if (contextTagValues.isEmpty() && context.getTags()
        .isEmpty()) {
      return variants;
    }
    for (IBaseEntityDTO variant : variants) {
      Set<String> contextTagValueIDs = get100RelevanceContextTagValueIDs(
          variant.getContextualObject()
              .getContextTagValues());
      boolean isRelevanceSame = SetUtils.isEqualSet(relevantTagValueIds, contextTagValueIDs);
      if (isRelevanceSame) {
        similiarVariants.add(variant);
      }
    }
    return similiarVariants;
  }
  
  private Set<String> get100RelevanceContextTagValueIDs(Set<ITagDTO> contextTagValues)
  {
    return contextTagValues.stream()
        .filter(x -> x.getRange() == 100)
        .map(x -> x.getTagValueCode())
        .collect(Collectors.toSet());
  }
  
  public Boolean checkIfSubsetRangeExist(IContextualDataDTO timeRange1,
      IContextualDataDTO timeRange2) throws Exception
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    
    validateTimeRange(from1, to1);
    validateTimeRange(from2, to2);
    // if all are not null and any became null then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    
    return ((checkIfDateIsSame(from1, from2) || checkIfDateIsInBetween(from2, from1, to2))
        && (checkIfDateIsSame(to1, to2) || checkIfDateIsInBetween(from2, to1, to2)));
  }
  
  public Boolean checkIfOverlapRangeExist(IContextualDataDTO timeRange1,
      IContextualDataDTO timeRange2) throws Exception
  {
    Long from1 = (Long) timeRange1.getContextStartTime();
    Long to1 = (Long) timeRange1.getContextEndTime();
    Long from2 = (Long) timeRange2.getContextStartTime();
    Long to2 = (Long) timeRange2.getContextEndTime();
    
    // if all are not null and any became null then they are not same
    if (from1 == 0 || from2 == 0 || to1 == 0 || to2 == 0) {
      return false;
    }
    validateTimeRange(from1, to1);
    validateTimeRange(from2, to2);
    
    return ((checkIfDateIsSame(from1, to2) || checkIfDateIsInBetween(from1, from2, to1))
        || (checkIfDateIsSame(from2, to1) || checkIfDateIsInBetween(from1, to2, to1)));
  }
  
  public String generateSearchExpression(String contextId, IGetVariantInstancesInTableViewRequestStrategyModel requestModel)
  {
    
    StringBuilder baseQuery = searchAssembler.getBaseQuery(BaseType.UNDEFINED);
    IInstanceTimeRange timeRange = requestModel.getTimeRange();
    Long from = timeRange.getFrom();
    Long to = timeRange.getTo();
    if(from != null && to != null)
      baseQuery.append(String.format(" $entity has [X> %s $iid=%d $start=%d $end=%d] ", contextId,
          Long.parseLong(requestModel.getParentId()), from, to));
    else
      baseQuery.append(String.format(" $entity has [X> %s $iid=%d] ", contextId, Long.parseLong(requestModel.getParentId())));
    
    String evaluationExpression = searchAssembler.getEvaluationExpression(requestModel.getAttributes(), requestModel.getTags(), "");
    if (!evaluationExpression.isEmpty()) {
      baseQuery.append(" where ").append(evaluationExpression);
    }
    return baseQuery.toString();
  }
  
  public String generateSearchExpressionForDuplicate(String contextId, Long parentIID)
  {
    StringBuilder baseQuery = searchAssembler.getBaseQuery(BaseType.UNDEFINED);
    baseQuery.append(String.format(" $entity has [X> %s $iid=%d] ", contextId, parentIID));
    return baseQuery.toString();
  }

  public Map<String, IVariantReferencedInstancesModel> fillContextualData(
      IGetConfigDetailsModel configDetails, IBaseEntityDAO baseEntityDAO, IKlassInstance klassInstance) throws Exception
  {
    IContextualDataDTO contextualObject = baseEntityDAO.getBaseEntityDTO().getContextualObject();
    // set Context and fill context tag values
    IContextInstance contextInstance = null;
    Map<String, IVariantReferencedInstancesModel> referencedInstances = new HashMap<>();
    if (contextualObject != null && contextualObject.getContextCode() != null
        && !contextualObject.getContextCode()
            .equals("")) {
      contextInstance = getContextInstance((List<IContentTagInstance>) klassInstance.getTags(),
          baseEntityDAO, configDetails.getReferencedTags());
      ((IContentInstance) klassInstance).setContext(contextInstance);

      IReferencedVariantContextModel referencedVariantContextModel = configDetails.getReferencedVariantContexts()
          .getEmbeddedVariantContexts().get(contextualObject.getContextCode());

      if(referencedVariantContextModel != null && !referencedVariantContextModel.getEntities().isEmpty()){
        referencedInstances = getReferencedInstances(baseEntityDAO, contextInstance);
      }
    }
    return referencedInstances;
  }
  
  public void deleteVariants(List<Long> ids, List<String> successfulIds, IExceptionModel failure, IProductDeleteDTO entryData) throws RDBMSException, CSFormatException, Exception
  {
    for (Long id : ids) {
      try {
        
        IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(id);

        IBaseEntityDTO parentDTO = baseEntityDAO.getParent();
        IBaseEntityDAO parentDAO = rdbmsComponentUtils.getBaseEntityDAO(parentDTO);
        parentDAO.removeChildren(EmbeddedType.CONTEXTUAL_CLASS, baseEntityDAO.getBaseEntityDTO());

        Boolean isDeleteFromDICatalog = transactionThread.getTransactionData().getPhysicalCatalogId().equals(Constants.DATA_INTEGRATION_CATALOG_IDS); 
        baseEntityDAO.delete(isDeleteFromDICatalog);

        successfulIds.add(Long.toString(id));
        entryData.getBaseEntityIIDs().add(id);

        rdbmsComponentUtils.getLocaleCatlogDAO().postDeleteUpdate(baseEntityDAO.getBaseEntityDTO());
      }
      catch (Exception e) {
        addFailureDetailsToFailureObject(failure, e, Long.toString(id), null);
      }
    }
    
    backgroundProcessForDeleteCoupleRecord(successfulIds);
    
    entryData.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    entryData.setLocaleID(rdbmsComponentUtils.getDataLanguage());
    entryData.setOrganizationCode(transactionThread.getTransactionData().getOrganizationId());

    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
    
    BGPDriverDAO.instance().submitBGPProcess(rdbmsComponentUtils.getUserName(), "PRODUCT_DELETE_HANDLER", "", userPriority,
        new JSONContent(entryData.toJSON()));
   
  }
  
  public void backgroundProcessForDeleteCoupleRecord(List<String> ids) throws Exception, RDBMSException, CSFormatException
  {
    IContextualDataTransferDTO dataTransferDTO = new ContextualDataTransferDTO();
    List<IContextualDataTransferGranularDTO> bgpCouplingDTOs = new ArrayList<>();
    ids.forEach(id-> {
      IContextualDataTransferGranularDTO granularDTO =new ContextualDataTransferGranularDTO();
      granularDTO.setDeletedVariantEntityIID(Long.parseLong(id));
      bgpCouplingDTOs.add(granularDTO);
      });

    dataTransferDTO.setBGPCouplingDTOs(bgpCouplingDTOs);
    
    if(dataTransferDTO.getBGPCouplingDTOs().isEmpty()) {
      return;
    }
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.HIGH;
    dataTransferDTO.setLocaleID(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getLocaleID());
    dataTransferDTO.setCatalogCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getCatalogCode());
    dataTransferDTO.setOrganizationCode(rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO().getOrganizationCode());
    dataTransferDTO.setUserId(transactionThread.getTransactionData().getUserId());

    BGPDriverDAO.instance().submitBGPProcess(transactionThread.getTransactionData().getUserName(), SERVICE_FOR_CDT, "", userPriority,
        new JSONContent(dataTransferDTO.toJSON()));
    
  }
  
  public static void addFailureDetailsToFailureObject(IExceptionModel failure, Exception e,
      String itemId, String itemName)
  {
    IExceptionDetailModel exceptionDetail = new ExceptionDetailModel();
    exceptionDetail.setKey(e.getClass()
        .getSimpleName());
    exceptionDetail.setItemId(itemId);
    exceptionDetail.setItemName(itemName);
    
    IDevExceptionDetailModel devExceptionDetail = new DevExceptionDetailModel();
    devExceptionDetail.setDetailMessage(e.getMessage());
    devExceptionDetail.setKey(e.getClass()
        .getSimpleName());
    devExceptionDetail.setStack(e.getStackTrace());
    devExceptionDetail.setExceptionClass(e.getClass()
        .getName());
    devExceptionDetail.setItemId(itemId);
    devExceptionDetail.setItemName(itemName);
    
    failure.getExceptionDetails()
        .add(exceptionDetail);
    failure.getDevExceptionDetails()
        .add(devExceptionDetail);
  }
  
  /**
   * @param newInstanceName
   * @param baseEntity
   * @param klassInstanceInformationModel
   * @param contextsWithAutoCreateEnableModel
   * @param transactionThreadData 
   * @param userName 
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public static void createEmbeddedVariantsWithAutoCreateEnabled(String newInstanceName,
      IBaseEntityDTO baseEntity, IKlassInstanceInformationModel klassInstanceInformationModel,
      List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableModel,
      TransactionThreadData transactionThreadData, String userName)
      throws IllegalAccessException, InvocationTargetException, RDBMSException, CSFormatException
  {
    for (ITechnicalImageVariantWithAutoCreateEnableModel contextWithAutoCreateEnableModel : contextsWithAutoCreateEnableModel) {
      if (!contextWithAutoCreateEnableModel.getType().equals(CommonConstants.IMAGE_VARIANT)) {
        ITechnicalImageVariantWrapperModel wrappedModel = new TechnicalImageVariantWrapperModel();
        wrappedModel.setVariantModel(contextWithAutoCreateEnableModel);
        wrappedModel.setKlassInstanceId(klassInstanceInformationModel.getId());
        wrappedModel.setParentId(klassInstanceInformationModel.getId());
        wrappedModel.setInstanceName(newInstanceName);
        wrappedModel.setBaseType(klassInstanceInformationModel.getBaseType());
        submitBGPAutoCreateVariantProcess(userName, baseEntity, wrappedModel, transactionThreadData);
      }
    }
  }
  /**
   * 
   * @param newInstanceName
   * @param baseEntity
   * @param klassInstanceInformationModel
   * @param contextsWithAutoCreateEnableModel
   * @param transactionThreadData
   * @param userName
   * @param targetEntityDAO
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  public static void createEmbeddedVariantsWithAutoCreateEnabledTransfer(String newInstanceName,
      IBaseEntityDTO baseEntity, IKlassInstanceInformationModel klassInstanceInformationModel,
      List<ITechnicalImageVariantWithAutoCreateEnableModel> contextsWithAutoCreateEnableModel,
      TransactionThreadData transactionThreadData, String userName, IBaseEntityDAO targetEntityDAO)
      throws IllegalAccessException, InvocationTargetException, RDBMSException, CSFormatException
  {
    for (ITechnicalImageVariantWithAutoCreateEnableModel contextWithAutoCreateEnableModel : contextsWithAutoCreateEnableModel) {
      if (!contextWithAutoCreateEnableModel.getType().equals(CommonConstants.IMAGE_VARIANT)) {
        ITechnicalImageVariantWrapperModel wrappedModel = new TechnicalImageVariantWrapperModel();
        wrappedModel.setVariantModel(contextWithAutoCreateEnableModel);
        wrappedModel.setKlassInstanceId(klassInstanceInformationModel.getId());
        wrappedModel.setParentId(klassInstanceInformationModel.getId());
        wrappedModel.setInstanceName(newInstanceName);
        wrappedModel.setBaseType(klassInstanceInformationModel.getBaseType());
        submitBGPAutoCreateVariantProcessTransfer(userName, baseEntity, wrappedModel, transactionThreadData,  targetEntityDAO);
      }
    }
  }
  
  /**
   * Submit BGP Auto create Variant process according to it class type.
   * Added this task as BGP task because thread pool start creating variants before committing current transaction 
   * @param userName 
   * @param baseEntity
   * @param wrappedModel
   * @param transactionThreadData 
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  @SuppressWarnings("unchecked")
  public static void submitBGPAutoCreateVariantProcess(String userName, IBaseEntityDTO baseEntity, ITechnicalImageVariantWrapperModel wrappedModel, TransactionThreadData transactionThreadData)
      throws IllegalAccessException, InvocationTargetException, RDBMSException, CSFormatException
  {
    String sourceEndPointId = transactionThreadData.getTransactionData().getEndpointId();
    AutoCreateVariantDTO autoCreateVariantDTO = new AutoCreateVariantDTO();
    autoCreateVariantDTO.setBaseEntity(baseEntity);
    autoCreateVariantDTO.setConfigData(ObjectMapperUtil.convertValue(wrappedModel, JSONObject.class));
    transactionThreadData.getTransactionData().setEndpointId(baseEntity.getEndpointCode());
    autoCreateVariantDTO.setTransaction(transactionThreadData.getTransactionData());
    BGPDriverDAO.instance().submitBGPProcess(userName, IAutoCreateVariantDTO.AUTO_CREATE_VARIANT_SERVICE, "",
        IBGProcessDTO.BGPPriority.HIGH, autoCreateVariantDTO.toJSONContent());
    transactionThreadData.getTransactionData().setEndpointId(sourceEndPointId);
  }
  /**
   * Submit BGP Auto create Variant process in Transfer .
   * @param userName
   * @param baseEntity
   * @param wrappedModel
   * @param transactionThreadData
   * @param targetEntityDAO
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   * @throws RDBMSException
   * @throws CSFormatException
   */
  @SuppressWarnings("unchecked")
  public static void submitBGPAutoCreateVariantProcessTransfer(String userName,
      IBaseEntityDTO baseEntity, ITechnicalImageVariantWrapperModel wrappedModel,
      TransactionThreadData transactionThreadData, IBaseEntityDAO targetEntityDAO)
      throws IllegalAccessException, InvocationTargetException, RDBMSException, CSFormatException
  {
    String sourceEndPointId = transactionThreadData.getTransactionData().getEndpointId();
    String sourceCatalogId = transactionThreadData.getTransactionData().getPhysicalCatalogId();
    String sourceOrgId = transactionThreadData.getTransactionData().getOrganizationId();
    AutoCreateVariantDTO autoCreateVariantDTO = new AutoCreateVariantDTO();
    autoCreateVariantDTO.setBaseEntity(baseEntity);
    autoCreateVariantDTO.setConfigData(ObjectMapperUtil.convertValue(wrappedModel, JSONObject.class));
    transactionThreadData.getTransactionData().setPhysicalCatalogId(targetEntityDAO.getLocaleCatalog()
            .getLocaleCatalogDTO().getCatalogCode());
    transactionThreadData.getTransactionData().setEndpointId(targetEntityDAO.getBaseEntityDTO().getEndpointCode());
    transactionThreadData.getTransactionData().setOrganizationId(targetEntityDAO.getBaseEntityDTO().getCatalog().getOrganizationCode());
    autoCreateVariantDTO.setTransaction(transactionThreadData.getTransactionData());
           BGPDriverDAO.instance().submitBGPProcess(userName, IAutoCreateVariantDTO.AUTO_CREATE_VARIANT_SERVICE, "",
            IBGProcessDTO.BGPPriority.HIGH, autoCreateVariantDTO.toJSONContent());
    transactionThreadData.getTransactionData().setPhysicalCatalogId(sourceCatalogId);
    transactionThreadData.getTransactionData().setEndpointId(sourceEndPointId);
    transactionThreadData.getTransactionData().setOrganizationId(sourceOrgId);
    
  }
}
