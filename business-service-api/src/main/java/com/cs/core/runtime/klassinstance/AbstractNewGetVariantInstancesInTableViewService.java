package com.cs.core.runtime.klassinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IReferencedPropertyCollectionModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.elastic.idto.ISearchResultDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.dto.SearchDTO.SearchDTOBuilder;
import com.cs.core.rdbms.entity.dto.VariantTableViewSearchDTO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.*;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IPropertyInstance;
import com.cs.core.runtime.interactor.model.attribute.IAttributeVariantsStatsModel;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplateConfigDetails;
import com.cs.core.runtime.interactor.model.configdetails.ITemplatePermissionForGetVariantInstancesModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForNewInstanceTreeModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IGetConfigDetailsForGetNewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.klassinstance.KlassInstanceTypeModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.variants.*;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForGetVariantInstancesInTableViewStrategy;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.util.getall.SearchUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.usecase.tableview.IGetConfigDetailsForTableViewStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.runtime.strategy.utils.PermissionUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public abstract class AbstractNewGetVariantInstancesInTableViewService<P extends IGetVariantInstanceInTableViewRequestModel, R extends IGetVariantInstancesInTableViewModel>
    extends AbstractRuntimeService<P, R> {

  @Autowired
  protected GetAllUtils getAllUtils;

  @Autowired
  protected SearchUtils searchUtils;

  @Autowired
  protected IGetConfigDetailsForTableViewStrategy getConfigDetailsForTableViewStrategy;

  @Autowired
  protected IGetConfigDetailsForGetVariantInstancesInTableViewStrategy getConfigDetailsForGetVariantInstancesInTableViewStrategy;

  @Autowired
  protected RDBMSComponentUtils rdbmsComponentUtils;

  @Autowired
  protected PermissionUtils permissionUtils;

  @Autowired
  protected VariantInstanceUtils variantInstanceUtils;

  @Autowired
  protected ModuleMappingUtil moduleMappingUtil;

  protected abstract String getBaseType();

  protected abstract String getModuleId();


  @Override protected R executeInternal(P requestModel) throws Exception
  {
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel = new GetConfigDetailsForGetNewInstanceTreeRequestModel();
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setAllowedEntities(new ArrayList<>());
    fillFilterAndSortInfo(requestModel, configRequestModel);
    IConfigDetailsForGetNewInstanceTreeModel configResponse = getConfigDetailsForTableViewStrategy.execute(configRequestModel);

    IVariantTableViewDTO searchDTO = prepareSearchDTOAndSearch(requestModel, configResponse, localeCatalogDAO);
    ISearchDAO searchDAO = localeCatalogDAO.openSearchDAO(searchDTO);
    ISearchResultDTO searchResult = searchDAO.tableView();

    List<String> baseEntityIIDs = searchResult.getBaseEntityIIDs();
    List<IBaseEntityDTO> variants =new ArrayList<>();
    if(!baseEntityIIDs.isEmpty()){
      variants.addAll(localeCatalogDAO.getBaseEntitiesByIIDs(baseEntityIIDs));
    }

    IConfigDetailsForGetVariantInstancesInTableViewModel postConfigDetails = getPostConfigDetails(requestModel, variants);
    Map<String, IReferencedSectionElementModel> referencedElements = getReferencedElements(postConfigDetails.getInstanceIdVsReferencedElements());
    postConfigDetails.setReferencedElements(referencedElements);

    IGetVariantInstancesInTableViewModel returnModel = getTableViewInfo(postConfigDetails, variants, requestModel);

    IGetFilterInfoModel filterInfo = postConfigDetails.getFilterInfo();
    getAllUtils.fillCountsForFilter(filterInfo, searchDAO, postConfigDetails.getFilterInfo().getTranslatableAttributesIds(), 20);

    returnModel.setFilterInfo(filterInfo);
    returnModel.setTotalContents(searchResult.getTotalCount());
    returnModel.setConfigDetails(postConfigDetails);
    permissionUtils.manageVariantInstancePermissions(postConfigDetails);
    return (R) returnModel;
  }

  private IConfigDetailsForGetVariantInstancesInTableViewModel getPostConfigDetails(P requestModel, List<IBaseEntityDTO> baseEntityDTOs) throws Exception
  {
    IConfigDetailsForGetVariantInstancesInTableViewRequestModel configRequestModel = new ConfigDetailsForGetVariantInstancesInTableViewRequestModel();

    String parentId = requestModel.getParentId();
    IKlassInstanceTypeModel klassInstanceTypeModel = getKlassInstanceType(parentId);
    String contextId = requestModel.getContextId();

    Map<Long, Map<String, List<String>>> childVSOtherClassifiers = getChildVSOtherClassifiers(baseEntityDTOs);

    configRequestModel.setInstanceIdVsOtherClassifiers(childVSOtherClassifiers);
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setContextId(contextId);
    List<String> columnIds = requestModel.getColumnIds();
    configRequestModel.setPropertyIds(columnIds);
    configRequestModel.setParentKlassIds(new ArrayList<>(klassInstanceTypeModel.getTypes()));
    configRequestModel.setParentTaxonomyIds(klassInstanceTypeModel.getTaxonomyIds());
    configRequestModel.setBaseType(getBaseType());
    configRequestModel.setModuleId(getModuleId());
    return getConfigDetailsForGetVariantInstancesInTableViewStrategy.execute(configRequestModel);
  }

  private Map<Long, Map<String, List<String>>> getChildVSOtherClassifiers(List<IBaseEntityDTO> baseEntityDTOs)
  {
    Map<Long, Map<String, List<String>>> childVSOtherClassifiers = new HashMap<>();
    for(IBaseEntityDTO baseEntityDTO : baseEntityDTOs){
      long baseEntityIID = baseEntityDTO.getBaseEntityIID();
      Map<String, List<String>> classifiers = new HashMap<>();
      List<String> classes = new ArrayList<>();
      List<String> taxonomies = new ArrayList<>();

      IClassifierDTO natureClassifier = baseEntityDTO.getNatureClassifier();
      classes.add(natureClassifier.getCode());

      for(IClassifierDTO classifierDTO:  baseEntityDTO.getOtherClassifiers()){
        if(classifierDTO.getClassifierType().equals(ClassifierType.CLASS)){
          classes.add(classifierDTO.getClassifierCode());
        }
        else{
          taxonomies.add(classifierDTO.getClassifierCode());
        }
      }

      classifiers.put("klasses",classes);
      classifiers.put("taxonomies",taxonomies);
      childVSOtherClassifiers.put(baseEntityIID, classifiers);
    }
    return childVSOtherClassifiers;
  }

  protected IVariantTableViewDTO prepareSearchDTOAndSearch(IGetVariantInstanceInTableViewRequestModel dataModel,
      IConfigDetailsForNewInstanceTreeModel configDetails, ILocaleCatalogDAO localeCatalogDao) throws Exception
  {
    List<ISortDTO> sortOptions = getAllUtils.getSortOptionsForTableView
        (dataModel.getSortOptions(), configDetails.getReferencedAttributes());

    SearchDTOBuilder searchBuilder = (SearchDTOBuilder) localeCatalogDao.getSearchDTOBuilder(List.of(getAllUtils.getBaseTypeByModule(getModuleId())),
        RootFilter.FALSE, false);
    searchBuilder.addSort(sortOptions);

    searchUtils.fillSearchDTOForTableView(dataModel, configDetails.getKlassIdsHavingRP(), configDetails.getTaxonomyIdsHavingRP(),
        configDetails.getTranslatableAttributeIds(), searchBuilder, configDetails.getSearchableAttributeIds(),
        configDetails.getMajorTaxonomyIds());

    return new VariantTableViewSearchDTO(searchBuilder, Long.parseLong(dataModel.getParentId()),
        dataModel.getContextId(), dataModel.getTimeRange().getFrom(), dataModel.getTimeRange().getTo());
  }

  private IKlassInstanceTypeModel getKlassInstanceType(String parentId)
      throws Exception
  {
    IKlassInstanceTypeModel typeModel = new KlassInstanceTypeModel();
    //remove get baseEntityIId
    List<IBaseEntityDTO> baseEntitiesByIIDs = rdbmsComponentUtils.getLocaleCatlogDAO().getBaseEntitiesByIIDs(List.of(parentId));

    IBaseEntityDAO baseEntityDAOByIID = rdbmsComponentUtils.getBaseEntityDAO(baseEntitiesByIIDs.get(0));
    IBaseEntityDTO baseEntityDTO = baseEntityDAOByIID.getBaseEntityDTO();
    typeModel.getParentKlassIds()
        .add(baseEntityDTO.getNatureClassifier()
            .getClassifierCode());

    for (IClassifierDTO classifierDTO : baseEntityDTO.getOtherClassifiers()) {
      if (classifierDTO.getClassifierType()
          .equals(ClassifierType.CLASS)) {
        typeModel.getParentKlassIds()
            .add(classifierDTO.getClassifierCode());
      }
      else {
        typeModel.getParentTaxonomyIds()
            .add(classifierDTO.getClassifierCode());
      }
    }
    return typeModel;
  }

  public IGetVariantInstancesInTableViewModel getTableViewInfo(IConfigDetailsForGetVariantInstancesInTableViewModel configDetails,
      List<IBaseEntityDTO> allChildrenForGivenContext, P requestModel) throws Exception
  {
    IGetVariantInstancesInTableViewModel variantModel = new GetVariantInstancesInTableViewModel();
    fillVariantInstancesForTableView(variantModel, allChildrenForGivenContext, configDetails, requestModel);
    variantModel.setFrom(requestModel.getFrom());

    return variantModel;
  }

  private Map<String, IReferencedSectionElementModel> getReferencedElements(
      Map<String, Map<String, IReferencedSectionElementModel>> idVsReferencedElements)
  {
    Map<String, IReferencedSectionElementModel> referencedElements = new HashMap<>();

    idVsReferencedElements.values().forEach(referencedElements::putAll);
    return referencedElements;
  }

  public void fillVariantInstancesForTableView(IGetVariantInstancesInTableViewModel variantModel,
      List<IBaseEntityDTO> allChildrenForGivenContext, IConfigDetailsForGetVariantInstancesInTableViewModel configDetails, P requestModel) throws Exception
  {
    Map<String, IReferencedSectionElementModel> referencedElements = configDetails.getReferencedElements();
    Map<String, ITag> referencedTags = configDetails.getReferencedTags();
    Map<String, IReferencedVariantContextModel> referencedContexts = configDetails.getReferencedVariantContexts().getEmbeddedVariantContexts();
    String contextId = requestModel.getContextId();
    IReferencedVariantContextModel referencedContext = referencedContexts.get(contextId);

    Map<String, Map<String, IReferencedSectionElementModel>> instanceIdVsReferencedElements = configDetails.getInstanceIdVsReferencedElements();
    Map<String, IReferencedPropertyCollectionModel> referencedPC = configDetails.getReferencedPropertyCollections();

    ITemplatePermissionForGetVariantInstancesModel referencedPermissions = (ITemplatePermissionForGetVariantInstancesModel) configDetails.getReferencedPermissions();
    List<String> allowedEntities = new ArrayList<>(referencedPermissions.getEntitiesHavingRP());

    List<String> columnIds =  requestModel.getColumnIds();
    Boolean isTimeEnabled = referencedContext.getIsTimeEnabled();

    Map<String, IVariantReferencedInstancesModel> referencedInstances = new HashMap<>();

    List<IIdParameterModel> columns = variantInstanceUtils.getColumns(columnIds, referencedElements, referencedTags,
        referencedPC, referencedContext, allowedEntities, instanceIdVsReferencedElements);
    variantModel.setColumns(columns);

    List<IRowIdParameterModel> rows = getRows(allChildrenForGivenContext, isTimeEnabled, columnIds,
        referencedInstances, allowedEntities, configDetails);
    variantModel.setRows(rows);
    variantModel.setReferencedInstances(referencedInstances);
  }

  public List<IRowIdParameterModel> getRows(List<IBaseEntityDTO> allChildrenForGivenContext, Boolean isTimeEnabled, List<String> columnIds,
      Map<String, IVariantReferencedInstancesModel> referencedInstances, List<String> allowedEntities,
      IConfigDetailsForGetVariantInstancesInTableViewModel configDetails) throws Exception
  {
    List<IRowIdParameterModel> rows = new ArrayList<>();
    Map<String, Map<String, IReferencedSectionElementModel>> refElements = configDetails.getInstanceIdVsReferencedElements();
    Set<String> contextualAttributeIds = variantInstanceUtils.getAttributeIdsHavingContext(configDetails, refElements);
    Map<Long, Set<IBaseEntityDTO>> variantIdVsLinkedInstances = new HashMap<>();
    if(!allChildrenForGivenContext.isEmpty()){
      rdbmsComponentUtils.getLocaleCatlogDAO().fillAllProperties(allChildrenForGivenContext);
      variantIdVsLinkedInstances.putAll(getContextualLinkedInstancesForVariants(allChildrenForGivenContext));
    }

    for (IBaseEntityDTO baseEntityDTO : allChildrenForGivenContext) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(baseEntityDTO);

      Map<String, IPropertyInstance> properties = new HashMap<>();
      IRowIdParameterModel rowMap = new RowIdParameterModel();
      rowMap.setId(String.valueOf(baseEntityDTO.getBaseEntityIID()));
      rowMap.setCreationLanguage(baseEntityDTO.getBaseLocaleID());
      rowMap.setProperties(properties);
      rowMap.getLanguageCodes().addAll(baseEntityDTO.getLocaleIds());
      rowMap.setOriginalInstanceId(baseEntityDTO.getBaseEntityID());
      rowMap.setAttributeVariantsStats(getAttributeVariantStats(baseEntityDAO, configDetails, contextualAttributeIds));
      if (baseEntityDTO.getBaseType().equals(BaseType.ASSET)) {
        rowMap.setAssetInformation(variantInstanceUtils.fillAssetInformation(baseEntityDTO));
      }
      rows.add(rowMap);

      Set<IPropertyRecordDTO> propertyRecords = baseEntityDAO.getBaseEntityDTO().getPropertyRecords();
      if (!columnIds.contains(IStandardConfig.StandardProperty.nameattribute.toString())) {
        columnIds.add(IStandardConfig.StandardProperty.nameattribute.toString());
      }

      for (IPropertyRecordDTO propertyRecord : propertyRecords) {
        String propertyID = propertyRecord.getProperty().getPropertyCode();
        if (!columnIds.contains(propertyID)) {
          continue;
        }

        IPropertyInstance propertyInstance = variantInstanceUtils.getPropertyInstance(propertyRecord, baseEntityDTO, configDetails);
        if (propertyInstance != null) {
          properties.put(propertyID, propertyInstance);
        }
      }
      IContextualDataDTO contextualObject = baseEntityDTO.getContextualObject();

      Set<IBaseEntityDTO> contextualLinkedEntities = variantIdVsLinkedInstances.computeIfAbsent(baseEntityDTO.getBaseEntityIID(), k -> new HashSet<>());
      variantInstanceUtils.fillContextualProperties(isTimeEnabled, contextualObject, properties, referencedInstances, contextualLinkedEntities, allowedEntities);
      variantInstanceUtils.fillContextTagValuesInTableView(properties, variantInstanceUtils.getTagValueMappingWithTag(configDetails.getReferencedTags()), baseEntityDTO);
      variantInstanceUtils.fillLinkedTaxonomies(baseEntityDTO, properties, configDetails);
    }
    return rows;
  }

  private Map<Long, Set<IBaseEntityDTO>> getContextualLinkedInstancesForVariants(List<IBaseEntityDTO> allChildrenForGivenContext)
      throws RDBMSException
  {
    Map<Long, Set<IBaseEntityDTO>> variantIDVsLinkedInstances = new HashMap<>();

    Map<Long,Long> contextIIDVsVariantIID = new HashMap<>();
    for (IBaseEntityDTO baseEntityDTO : allChildrenForGivenContext) {
      contextIIDVsVariantIID.put(baseEntityDTO.getContextualObject().getContextualObjectIID(), baseEntityDTO.getBaseEntityIID());
    }
    ILocaleCatalogDAO localeCatalogDAO = rdbmsComponentUtils.getLocaleCatlogDAO();
    Map<Long, Set<Long>> linkedIIDVsContext = localeCatalogDAO.getLinkedInstancesForContexts(contextIIDVsVariantIID.keySet());

    List<String> linkedInstanceIds = linkedIIDVsContext.keySet().stream().map(String::valueOf).collect(Collectors.toList());
    List<IBaseEntityDTO>  entities = localeCatalogDAO.getBaseEntitiesByIIDs(new ArrayList<>(linkedInstanceIds));

    for (IBaseEntityDTO entity : entities) {
      Set<Long> contexts = linkedIIDVsContext.get(entity.getBaseEntityIID());
      for (Long contextIID : contexts) {
        Long variantIID = contextIIDVsVariantIID.get(contextIID);
        Set<IBaseEntityDTO> baseEntities = variantIDVsLinkedInstances.computeIfAbsent(variantIID, k ->new HashSet<>());
        baseEntities.add(entity);
      }
    }
    return variantIDVsLinkedInstances;
  }

  public Map<String, IAttributeVariantsStatsModel> getAttributeVariantStats(IBaseEntityDAO baseEntityDAO,
      IBaseKlassTemplateConfigDetails configDetails, Set<String> attributeIdsHavingContext) throws Exception
  {
    Map<String, IAttributeVariantsStatsModel> attributeVariantStats = new HashMap<String, IAttributeVariantsStatsModel>();
    Map<String, IAttribute> referencedAttributes = configDetails.getReferencedAttributes();
    Map<String, Set<IPropertyRecordDTO>> propertyRecordsMapForAttributeVariants = new HashMap<>();
    
    for (IPropertyRecordDTO propertyRecordDto : baseEntityDAO.getBaseEntityDTO().getPropertyRecords()) {
      String propertyCode = propertyRecordDto.getProperty().getCode();
      if(attributeIdsHavingContext.contains(propertyCode) && !StringUtils.isEmpty(((IValueRecordDTO)propertyRecordDto).getValue())) {
        Set<IPropertyRecordDTO> propertyRecordsForAttribute = propertyRecordsMapForAttributeVariants.computeIfAbsent(propertyCode, k -> new HashSet<>()); 
        propertyRecordsForAttribute.add(propertyRecordDto);
      }
    }
    

    for (String attributeId : attributeIdsHavingContext) {
      IAttribute referencedAttribute = referencedAttributes.get(attributeId);
      IAttributeVariantsStatsModel attributeVariantStat = KlassInstanceBuilder
          .getAttributeVariantStatsForEntity(referencedAttribute, propertyRecordsMapForAttributeVariants.get(attributeId));
      if (attributeVariantStat != null) {
        attributeVariantStats.put(referencedAttribute.getId(), attributeVariantStat);
      }
    }
    return attributeVariantStats;
  }

  protected void fillFilterAndSortInfo(P model, IGetConfigDetailsForGetNewInstanceTreeRequestModel configRequestModel)
  {
    List<String> attributeIds = new ArrayList<>();
    List<String> tagIds = new ArrayList<>();
    List<? extends IPropertyInstanceFilterModel> attributes = model.getAttributes();
    for (IPropertyInstanceFilterModel attribute : attributes) {
      attributeIds.add(attribute.getId());
    }
    List<? extends IPropertyInstanceFilterModel> tags = model.getTags();
    for (IPropertyInstanceFilterModel tag : tags) {
      tagIds.add(tag.getId());
    }

    configRequestModel.setAttributeIds(attributeIds);
    configRequestModel.setTagIds(tagIds);
    configRequestModel.setIsFilterDataRequired(true);
    for (ISortModel sort : model.getSortOptions()) {
      configRequestModel.getAttributeIds().add(sort.getSortField());
    }
  }

}

