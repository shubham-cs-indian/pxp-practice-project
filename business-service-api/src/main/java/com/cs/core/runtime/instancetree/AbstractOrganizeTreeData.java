package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.TaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.entity.dto.ClassifierAggregationDTO;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IAggregationResultDTO;
import com.cs.core.rdbms.entity.idto.IClassifierAggregationDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.runtime.interactor.model.configdetails.CategoryInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IOrganizeTreeDataRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.util.getall.SearchUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailForOrganizeTreeDataStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@SuppressWarnings("unchecked")
public abstract class AbstractOrganizeTreeData<P extends IOrganizeTreeDataRequestModel, R extends IGetOrganizeTreeDataResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext                                   context;
  
  @Autowired
  protected ModuleMappingUtil                                 moduleMappingUtil;
  
  @Autowired
  protected IGetConfigDetailForOrganizeTreeDataStrategy getConfigDetailForOrganizeScreenTreeDataStrategy;
  
  @Autowired
  protected GetAllUtils                                       getAllUtils;
  
  @Autowired
  protected RDBMSComponentUtils                               rdbmsComponentUtils;
  
  @Autowired
  private InstanceTreeUtils                                   instanceTreeUtils;
  
  @Autowired
  protected SearchAssembler                                   searchAssembler;
  
  @Autowired
  protected SearchUtils                                       searchUtils;
  
  protected abstract IConfigDetailsForGetKlassTaxonomyTreeRequestModel getConfigDetailsRequestModel();

  protected abstract IConfigDetailsOrganizeTreeDataResponseModel executeConfigDetailsStrategy(IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel) throws Exception;

  protected abstract IGetOrganizeTreeDataResponseModel executeRuntimeStrategy(P dataModel, IConfigDetailsOrganizeTreeDataResponseModel configData) throws Exception;
  
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    List<String> entities = getModuleEntities(dataModel);
    IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel = prepareConfigRequestModel(
        dataModel, entities);
    IConfigDetailsOrganizeTreeDataResponseModel configData = executeConfigDetailsStrategy(
        configRequsetModel);
    
    prepareRuntimeRequestModel(dataModel, configData);
    IGetOrganizeTreeDataResponseModel returnModel = executeRuntimeStrategy(dataModel, configData);
    return (R) returnModel;
  }
  
  protected List<String> getModuleEntities(P model) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(model.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }
  
  protected void prepareRuntimeRequestModel(P dataModel,
      IConfigDetailsOrganizeTreeDataResponseModel configData)
  {
    dataModel.setKlassIdsHavingRP(configData.getKlassIdsHavingRP());
    dataModel.setTaxonomyIdsHavingRP(configData.getTaxonomyIdsHavingRP());
    dataModel.setKlassTaxonomyInfo(configData.getKlassTaxonomyInfo());
    dataModel.setModuleEntities(configData.getAllowedEntities());
    dataModel.setSearchableAttributeIds(configData.getSearchableAttributeIds());
    dataModel.setTranslatableAttributeIds(configData.getTranslatableAttributeIds());
    dataModel.setMajorTaxonomyIds(configData.getMajorTaxonomyIds());
  }
  
  protected IConfigDetailsForGetKlassTaxonomyTreeRequestModel prepareConfigRequestModel(P dataModel,
      List<String> entities)
  {
    IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel = getConfigDetailsRequestModel();
    configRequsetModel.setKpiId(dataModel.getKpiId());
    configRequsetModel.setAllowedEntities(entities);
    configRequsetModel.setUserId(context.getUserId());
    configRequsetModel.setClickedTaxonomyId(dataModel.getClickedTaxonomyId());
    configRequsetModel.setParentTaxonomyId(dataModel.getParentTaxonomyId());
    configRequsetModel.setSelectedTaxonomyIds(dataModel.getSelectedTaxonomyIds());
    return configRequsetModel;
  }
  
  protected List<ICategoryInformationModel> getTaxonomyParentList(
      List<ICategoryInformationModel> taxonomiesInfo, String searchExpression) throws Exception
  {
    List<String> rootTaxonomyIds = taxonomiesInfo.stream()
        .filter(x -> x.getParent() == null)
        .map(x -> x.getId())
        .collect(Collectors.toList());
    
    if (rootTaxonomyIds.isEmpty()) {
      return new ArrayList<>();
    }
    return fillTaxonomyCountInfo(false, searchExpression, rootTaxonomyIds, taxonomiesInfo);
  }
  
  public List<ICategoryInformationModel> fillTaxonomyCountInfo(Boolean allowChildren,
      String searchExpression, List<String> classifierIds,
      List<ICategoryInformationModel> taxonomiesInfo) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    Map<String, Long> classifierCount = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getTaxonomyCount(allowChildren, searchExpression, classifierIds);
    
    for (IConfigEntityTreeInformationModel category : taxonomiesInfo) {
      if (!category.getId()
          .equals("-1")) {
        Long count = classifierCount.getOrDefault(category.getCode(), 0l);
        ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1,
            category);
        ITaxonomyInformationModel taxonomyInfo = new TaxonomyInformationModel(
            categoryInformationModel);
        classifierInfo.add(taxonomyInfo);
        
        List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
        taxonomyInfo.setChildren(new ArrayList<>());
        fillChildrenInfo(taxonomyInfo, classifierCount, children, count, false);
      }
    }
    return classifierInfo;
  }
  
  public Long fillChildrenInfo(ICategoryInformationModel categoryInfo,
      Map<String, Long> classifierCount, List<? extends ITreeEntity> children, Long count,
      boolean isRecursive) throws RDBMSException
  {
    ListIterator<? extends ITreeEntity> iterator = children.listIterator();
    while (iterator.hasNext()) {
      IConfigEntityTreeInformationModel treeModel = (IConfigEntityTreeInformationModel) iterator
          .next();
      String code = treeModel.getCode();
      Long currentCount = classifierCount.getOrDefault(code, 0l);
      IClassifierDTO childClassifier = ConfigurationDAO.instance()
          .getClassifierByCode(code);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(
          currentCount, childClassifier.getClassifierIID(), treeModel);
      List<? extends ITreeEntity> newChildren = new ArrayList<>(treeModel.getChildren());
      categoryInformationModel.setChildren(new ArrayList<>());
      Long childCount = fillChildrenInfo(categoryInformationModel, classifierCount, newChildren,
          currentCount, isRecursive);
      if (isRecursive) {
        count += childCount;
        categoryInfo.setCount(count);
      }
      else {
        categoryInfo.setCount(count);
      }
      List<ICategoryInformationModel> childsChildren = (List<ICategoryInformationModel>) categoryInfo
          .getChildren();
      childsChildren.add(categoryInformationModel);
    }
    return count;
  }
  
  protected ICategoryInformationModel getKlassContentCount(
      IOrganizeTreeDataRequestModel dataModel, IConfigEntityTreeInformationModel categoryInfo,
      boolean allowChildren) throws Exception
  {
    List<String> classifierIds = new ArrayList<>();
    getAllUtils.getFlatIdsListForKlass(categoryInfo, classifierIds);
    String generateSearchExpression = generateSearchExpressionForTaxonomy(dataModel);
    List<IConfigEntityTreeInformationModel> categoryTree = List.of(categoryInfo);
    List<ICategoryInformationModel> classifierInfo = fillClassifierCountInfo(allowChildren,
        generateSearchExpression, classifierIds, categoryTree);
    return classifierInfo.get(0);
  }
  
  public String generateSearchExpressionForTaxonomy(IOrganizeTreeDataRequestModel dataModel)
  {
    String moduleId = "";
    if (dataModel.getModuleEntities()
        .size() == 1) {
      moduleId = InstanceTreeUtils.getModuleIdByEntityId(dataModel.getModuleEntities()
          .get(0));
    }
    return instanceTreeUtils.generateSearchExpressionForTaxonomy(dataModel, moduleId);
  }
  
  protected String generateSearchExpression(IOrganizeTreeDataRequestModel dataModel)
  {
    
    return instanceTreeUtils.generateSearchExpression(dataModel);
  }
  
  public List<ICategoryInformationModel> fillClassifierCountInfo(Boolean allowChildren,
      String searchExpression, List<String> classifierIds,
      List<IConfigEntityTreeInformationModel> categoryInfo) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    Map<String, Long> classifierCount = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getClassCount(allowChildren, searchExpression, classifierIds);
    
    for (IConfigEntityTreeInformationModel category : categoryInfo) {
      Long count = classifierCount.getOrDefault(category.getCode(), 0l);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1,
          category);
      classifierInfo.add(categoryInformationModel);
      List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
      categoryInformationModel.setChildren(new ArrayList<>());
      fillChildrenInfo(categoryInformationModel, classifierCount, children, count, true);
    }
    return classifierInfo;
  }
  
  public List<ICategoryInformationModel> fillClassifierCountInfo(ISearchDTO searchDTO,
      List<String> classifierIds, List<IConfigEntityTreeInformationModel> categoryInfo)
      throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    
    ISearchDAO searchDAO = rdbmsComponentUtils.getLocaleCatlogDAO()
        .openSearchDAO(searchDTO);
    IClassifierAggregationDTO classifierAggregationDTO = new ClassifierAggregationDTO();
    classifierAggregationDTO.setAggregationType(IAggregationRequestDTO.AggregationType.byClass);
    classifierAggregationDTO.setClassifierIds(classifierIds);
    classifierAggregationDTO.setSize(classifierIds.size());
    IAggregationResultDTO aggregation = searchDAO.aggregation(classifierAggregationDTO);
    Map<String, Long> classifierCount = aggregation.getCount();
    
    for (IConfigEntityTreeInformationModel category : categoryInfo) {
      Long count = classifierCount.getOrDefault(category.getCode(), 0L);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1,
          category);
      classifierInfo.add(categoryInformationModel);
      List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
      categoryInformationModel.setChildren(new ArrayList<>());
      fillChildrenInfo(categoryInformationModel, classifierCount, children, count, true);
    }
    return classifierInfo;
  }
  
  public List<ICategoryInformationModel> fillTaxonomyCountInfo(ISearchDTO searchDTO,
      List<String> classifierIds, List<ICategoryInformationModel> taxonomiesInfo) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    
    ISearchDAO searchDAO = rdbmsComponentUtils.getLocaleCatlogDAO()
        .openSearchDAO(searchDTO);
    IClassifierAggregationDTO classifierAggregationDTO = new ClassifierAggregationDTO();
    classifierAggregationDTO.setAggregationType(IAggregationRequestDTO.AggregationType.byTaxonomy);
    classifierAggregationDTO.setClassifierIds(classifierIds);
    classifierAggregationDTO.setSize(classifierIds.size());
    IAggregationResultDTO aggregation = searchDAO.aggregation(classifierAggregationDTO);
    Map<String, Long> classifierCount = aggregation.getCount();
    
    for (IConfigEntityTreeInformationModel category : taxonomiesInfo) {
      if (!category.getId()
          .equals("-1")) {
        Long count = classifierCount.getOrDefault(category.getCode(), 0l);
        ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1,
            category);
        ITaxonomyInformationModel taxonomyInfo = new TaxonomyInformationModel(
            categoryInformationModel);
        classifierInfo.add(taxonomyInfo);
        
        List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
        taxonomyInfo.setChildren(new ArrayList<>());
        fillChildrenInfo(taxonomyInfo, classifierCount, children, count, false);
      }
    }
    return classifierInfo;
  }
  
  public List<ICategoryInformationModel> getTaxonomyParentList(
      List<ICategoryInformationModel> taxonomiesInfo, ISearchDTO searchDTO) throws Exception
  {
    List<String> rootTaxonomyIds = taxonomiesInfo.stream()
        .filter(x -> x.getParent() == null)
        .map(x -> x.getId())
        .collect(Collectors.toList());
    
    if (rootTaxonomyIds.isEmpty()) {
      return new ArrayList<>();
    }
    return fillTaxonomyCountInfo(searchDTO, rootTaxonomyIds, taxonomiesInfo);
  }
  
  protected ICategoryInformationModel getKlassContentCount(
      IOrganizeTreeDataRequestModel dataModel, IConfigEntityTreeInformationModel categoryInfo,
      ISearchDTO searchDTO) throws Exception
  {
    List<String> classifierIds = new ArrayList<>();
    getAllUtils.getFlatIdsListForKlass(categoryInfo, classifierIds);
    List<IConfigEntityTreeInformationModel> categoryTree = List.of(categoryInfo);
    List<ICategoryInformationModel> classifierInfo = fillClassifierCountInfo(searchDTO,
        classifierIds, categoryTree);
    return classifierInfo.get(0);
  }
  
  public IGetOrganizeTreeDataResponseModel getKlassTaxonomyTreeData(
      IOrganizeTreeDataRequestModel dataModel,
      IConfigDetailsOrganizeTreeDataResponseModel configData) throws Exception, RDBMSException
  {
    List<String> moduleIds = InstanceTreeUtils
        .getModuleIdByEntityId(configData.getAllowedEntities());
    ISearchDTOBuilder searchDTOBuilder = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getSearchDTOBuilder(searchAssembler.getBaseTypeByModule(moduleIds), RootFilter.TRUE,
            dataModel.getIsArchivePortal());
    
    fillUsecaseSpecificFilters(dataModel, configData, searchDTOBuilder);
    
    searchUtils.fillSearchDTO(dataModel, configData.getKlassIdsHavingRP(),
        configData.getTaxonomyIdsHavingRP(), configData.getTranslatableAttributeIds(),
        searchDTOBuilder, configData.getSearchableAttributeIds(), configData.getMajorTaxonomyIds());
    
    ISearchDTO searchDTOForParents = searchDTOBuilder.build();
    searchDTOForParents.getTaxonomyIds()
        .clear();
    searchDTOForParents.getClassIds()
        .clear();
    
    ISearchDTO searchDTO = searchDTOBuilder.build();
    
    IGetOrganizeTreeDataResponseModel returnModel = new GetOrganizeTreeDataResponseModel();
    if (dataModel.getClickedTaxonomyId() == null) {
      List<ICategoryInformationModel> klassTaxonomyInfo = configData.getKlassTaxonomyInfo();
      if (!klassTaxonomyInfo.isEmpty()) {
        returnModel.getKlassTaxonomyInfo()
            .add(getKlassContentCount(dataModel, klassTaxonomyInfo.get(0), searchDTO));
        if (klassTaxonomyInfo.size() > 1) {
          List<ICategoryInformationModel> subList = klassTaxonomyInfo.subList(1,
              klassTaxonomyInfo.size());
          returnModel.getKlassTaxonomyInfo()
              .addAll(getTaxonomyParentList(subList, searchDTOForParents));
        }
      }
    }
    else {
      List<String> rootTaxonomyIds = new ArrayList<>();
      getAllUtils.getFlatIdsList(dataModel.getKlassTaxonomyInfo(), rootTaxonomyIds);
      if (rootTaxonomyIds.isEmpty()) {
        return returnModel;
      }
      
      if (!dataModel.getKlassTaxonomyInfo()
          .isEmpty()) {
        
        List<IConfigEntityTreeInformationModel> categoryTree = List
            .of(dataModel.getKlassTaxonomyInfo()
                .get(0));
        returnModel.getKlassTaxonomyInfo()
            .addAll(fillClassifierCountInfo(searchDTOForParents, rootTaxonomyIds, categoryTree));
        
        // For taxonomy hierarchy usecase.
        if (!dataModel.getClickedTaxonomyId()
            .equals("-1")) {
          returnModel.getKlassTaxonomyInfo()
              .get(0)
              .setChildren(new ArrayList<>());
        }
      }
      
      returnModel.getKlassTaxonomyInfo()
          .addAll(
              fillTaxonomyCountInfo(searchDTO, rootTaxonomyIds, dataModel.getKlassTaxonomyInfo()));
    }
    return returnModel;
  }
  
  protected void fillUsecaseSpecificFilters(IOrganizeTreeDataRequestModel dataModel,
      IConfigDetailsOrganizeTreeDataResponseModel configData, ISearchDTOBuilder searchBuilder)
      throws Exception
  {
    
  }
  
}