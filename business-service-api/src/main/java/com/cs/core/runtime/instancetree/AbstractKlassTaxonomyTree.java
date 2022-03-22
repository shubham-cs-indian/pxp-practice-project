package com.cs.core.runtime.instancetree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForTypeInfoModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.ITaxonomyHierarchyModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.config.strategy.usecase.taxonomy.IGetAllTaxonomiesForAllowedTypesStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetKlassesAndTaxonomyByIds;
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
import com.cs.core.runtime.interactor.model.configdetails.instancetree.GetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.instancetree.GetInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetSelectedKlassesAndTaxonomiesByIdsResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.INewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.AllowedTypesRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.util.getall.SearchUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailForKlassTaxonomyTreeStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@SuppressWarnings("unchecked")
public abstract class AbstractKlassTaxonomyTree<P extends IGetKlassTaxonomyTreeRequestModel,R extends IGetKlassTaxonomyTreeResponseModel>
extends AbstractRuntimeService<P, R>  {
  
  @Autowired
  protected ISessionContext                              context;
  
  @Autowired
  protected ModuleMappingUtil                            moduleMappingUtil;
  
  @Autowired
  protected IGetConfigDetailForKlassTaxonomyTreeStrategy getConfigDetailForKlassTaxonomyTreeStrategy;
  
  @Autowired
  protected GetAllUtils                                  getAllUtils;
  
  @Autowired
  protected RDBMSComponentUtils                          rdbmsComponentUtils;

  @Autowired
  protected SearchAssembler                              searchAssembler;
  
  @Autowired
  protected SearchUtils                                  searchUtils;
  
  @Autowired
  protected IGetAllTaxonomiesForAllowedTypesStrategy     getAllTaxonomiesForAllowedTypesStrategy;
  
  @Autowired
  protected IGetKlassesAndTaxonomyByIds                  getKlassesAndTaxonomyByIds;
  
  protected abstract IConfigDetailsForGetKlassTaxonomyTreeRequestModel getConfigDetailsRequestModel();

  protected abstract IConfigDetailsKlassTaxonomyTreeResponseModel executeConfigDetailsStrategy(IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel) throws Exception;

  protected abstract IGetKlassTaxonomyTreeResponseModel executeRuntimeStrategy(P dataModel, IConfigDetailsKlassTaxonomyTreeResponseModel configData) throws Exception;
  
  protected abstract void additionalInformationForRelationshipFilter(P model, IConfigDetailsKlassTaxonomyTreeResponseModel configData);
  
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {
    List<String> entities = getModuleEntities(dataModel);
    IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel = prepareConfigRequestModel(dataModel, entities);
    IConfigDetailsKlassTaxonomyTreeResponseModel configData = executeConfigDetailsStrategy(configRequsetModel);
    
    prepareRuntimeRequestModel(dataModel, configData);
    additionalInformationForRelationshipFilter(dataModel, configData);
    IGetKlassTaxonomyTreeResponseModel returnModel = executeRuntimeStrategy(dataModel, configData);
    return (R) returnModel;
  }
  
  protected List<String> getModuleEntities(P model) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(model.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }
  
  protected void prepareRuntimeRequestModel(P dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData)
  {
    String selectedType = dataModel.getSelectedCategory();
    if(selectedType.equals(IGetKlassTaxonomyTreeResponseModel.NATURE_KLASSES)) {
      dataModel.setKlassTaxonomyInfo(configData.getNatureClasses());
    }
    else if(selectedType.equals(IGetKlassTaxonomyTreeResponseModel.ATTRIBUTION_CLASSES)) {
      dataModel.setKlassTaxonomyInfo(configData.getAttributionClasses());
    }
    else if(selectedType.equals(IGetKlassTaxonomyTreeResponseModel.TAXONOMIES)){
      dataModel.setKlassTaxonomyInfo(configData.getTaxonomies());
    }
    dataModel.setModuleEntities(configData.getAllowedEntities());
    dataModel.setSearchableAttributeIds(configData.getSearchableAttributeIds());
  }

  protected IConfigDetailsForGetKlassTaxonomyTreeRequestModel prepareConfigRequestModel(P dataModel, List<String> entities)
  {
    IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequestModel = getConfigDetailsRequestModel();
    configRequestModel.setAllowedEntities(entities);
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setClickedTaxonomyId(dataModel.getClickedTaxonomyId());
    configRequestModel.setSelectedCategory(dataModel.getSelectedCategory());
    configRequestModel.setFrom(dataModel.getFrom());
    configRequestModel.setSize(dataModel.getSize());
    configRequestModel.setSearchText(dataModel.getSearchText());
    configRequestModel.setModuleId(dataModel.getModuleId());
    configRequestModel.setModuleEntities(dataModel.getModuleEntities());
    
    return configRequestModel;
  }
  
  protected List<ICategoryInformationModel> getTaxonomyParentList(List<ICategoryInformationModel> taxonomiesInfo, String searchExpression)
      throws Exception
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

  public List<ICategoryInformationModel> fillTaxonomyCountInfo(Boolean allowChildren, String searchExpression, List<String> classifierIds,
      List<ICategoryInformationModel> taxonomiesInfo) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    Map<String, Long> classifierCount = rdbmsComponentUtils.getLocaleCatlogDAO().getTaxonomyCount(allowChildren, searchExpression, classifierIds);
    
    for (IConfigEntityTreeInformationModel category : taxonomiesInfo) {
      if(!category.getId().equals("-1")) {
        Long count = classifierCount.getOrDefault(category.getCode(), 0l);
        ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1, category);
        ITaxonomyInformationModel taxonomyInfo = new TaxonomyInformationModel(categoryInformationModel);
        classifierInfo.add(taxonomyInfo);
        
        List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
        taxonomyInfo.setChildren(new ArrayList<>());
        fillChildrenInfo(taxonomyInfo, classifierCount, children, count, false);
      }
    }
    return classifierInfo;
  }

  public Long fillChildrenInfo(ICategoryInformationModel categoryInfo, Map<String, Long> classifierCount,
      List<? extends ITreeEntity> children, Long count, boolean isRecursive) throws RDBMSException
  {
    ListIterator<? extends ITreeEntity> iterator = children.listIterator();
    while (iterator.hasNext()) {
      IConfigEntityTreeInformationModel treeModel = (IConfigEntityTreeInformationModel) iterator.next();
      String code = treeModel.getCode();
      Long currentCount = classifierCount.getOrDefault(code, 0l);
      IClassifierDTO childClassifier = ConfigurationDAO.instance().getClassifierByCode(code);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(currentCount, childClassifier.getClassifierIID(),
          treeModel);
      ICategoryInformationModel categoryModel = (ICategoryInformationModel) treeModel;
      categoryInformationModel.setIsLastNode(categoryModel.getIsLastNode());
      List<? extends ITreeEntity> newChildren = new ArrayList<>(treeModel.getChildren());
      categoryInformationModel.setChildren(new ArrayList<>());
      Long childCount = fillChildrenInfo(categoryInformationModel, classifierCount, newChildren, currentCount, isRecursive);
      if (isRecursive) {
        count += childCount;
        categoryInfo.setCount(count);
      }
      else {
        categoryInfo.setCount(count);
      }
      List<ICategoryInformationModel> childsChildren = (List<ICategoryInformationModel>) categoryInfo.getChildren();
      childsChildren.add(categoryInformationModel);
    }
    return count;
  }

  public List<ICategoryInformationModel> fillClassifierCountInfo(Boolean allowChildren, String searchExpression, List<String> classifierIds,
      List<IConfigEntityTreeInformationModel> categoryInfo) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    Map<String, Long> classifierCount = rdbmsComponentUtils.getLocaleCatlogDAO().getClassCount(allowChildren, searchExpression, classifierIds);

    for (IConfigEntityTreeInformationModel category : categoryInfo) {
      Long count = classifierCount.getOrDefault(category.getCode(), 0l);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1, category);
      classifierInfo.add(categoryInformationModel);
      List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
      categoryInformationModel.setChildren(new ArrayList<>());
      fillChildrenInfo(categoryInformationModel, classifierCount, children, count, true);
    }
    return classifierInfo;
  }
  
  
  public List<ICategoryInformationModel> fillClassifierCountInfo(ISearchDTO searchDTO, List<String> classifierIds,
      List<IConfigEntityTreeInformationModel> categoryInfo) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    
    ISearchDAO searchDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openSearchDAO(searchDTO);
    IClassifierAggregationDTO classifierAggregationDTO = new ClassifierAggregationDTO();
    classifierAggregationDTO.setAggregationType(IAggregationRequestDTO.AggregationType.byClass);
    classifierAggregationDTO.setClassifierIds(classifierIds);
    classifierAggregationDTO.setSize(classifierIds.size());
    IAggregationResultDTO aggregation = searchDAO.aggregation(classifierAggregationDTO);
    Map<String, Long> classifierCount = aggregation.getCount();
    
    for (IConfigEntityTreeInformationModel category : categoryInfo) {
      Long count = classifierCount.getOrDefault(category.getCode(), 0L);
      ICategoryInformationModel categoryInformationModel = new CategoryInformationModel();
      classifierInfo.add(categoryInformationModel);
      List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
      fillChildrenInfo(categoryInformationModel, classifierCount, children, count, true);
    }
    return classifierInfo;
  }
  
  public List<ICategoryInformationModel> fillTaxonomyCountInfo(ISearchDTO searchDTO, List<String> classifierIds,
      List<ICategoryInformationModel> taxonomiesInfo, Long totalChildrenCount) throws Exception
  {
    List<ICategoryInformationModel> classifierInfo = new ArrayList<>();
    
    ISearchDAO searchDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openSearchDAO(searchDTO);
    IClassifierAggregationDTO classifierAggregationDTO = new ClassifierAggregationDTO();
    classifierAggregationDTO.setAggregationType(IAggregationRequestDTO.AggregationType.byTaxonomy);
    classifierAggregationDTO.setClassifierIds(classifierIds);
    classifierAggregationDTO.setSize(classifierIds.size());
    IAggregationResultDTO aggregation = searchDAO.aggregation(classifierAggregationDTO);
    Map<String, Long> classifierCount = aggregation.getCount();
    
    for (IConfigEntityTreeInformationModel category : taxonomiesInfo) {
      if (!category.getId().equals("-1")) {
        Long count = classifierCount.getOrDefault(category.getCode(), 0l);
        ICategoryInformationModel categoryInformationModel = new CategoryInformationModel(count, -1, category);
        ICategoryInformationModel categoryModel = (ICategoryInformationModel) category;
        categoryInformationModel.setIsLastNode(categoryModel.getIsLastNode());
        categoryInformationModel.setTotalChildrenCount(totalChildrenCount);
        ITaxonomyInformationModel taxonomyInfo = new TaxonomyInformationModel(categoryInformationModel);
        classifierInfo.add(taxonomyInfo);
        
        List<? extends ITreeEntity> children = new ArrayList<>(category.getChildren());
        taxonomyInfo.setChildren(new ArrayList<>());
        fillChildrenInfo(taxonomyInfo, classifierCount, children, count, false);
      }
    }
    return classifierInfo;
  }
  
  public List<ICategoryInformationModel> getTaxonomyList(List<ICategoryInformationModel> taxonomiesInfo, ISearchDTO searchDTO, Long totalChildrenCount)
      throws Exception
  {
    List<String> rootTaxonomyIds = taxonomiesInfo.stream().filter(x -> x.getParent() == null).map(x -> x.getId())
        .collect(Collectors.toList());
    
    if (rootTaxonomyIds.isEmpty()) {
      return new ArrayList<>();
    }
    return fillTaxonomyCountInfo(searchDTO, rootTaxonomyIds, taxonomiesInfo, totalChildrenCount);
  }
  
  protected ICategoryInformationModel getKlassContentCount(IGetKlassTaxonomyTreeRequestModel dataModel,
      IConfigEntityTreeInformationModel categoryInfo, ISearchDTO searchDTO) throws Exception
  {
    List<String> classifierIds = new ArrayList<>();
    getAllUtils.getFlatIdsListForKlass(categoryInfo, classifierIds);
    List<IConfigEntityTreeInformationModel> categoryTree = List.of(categoryInfo);
    List<ICategoryInformationModel> classifierInfo = fillClassifierCountInfo(searchDTO, classifierIds, categoryTree);
    return classifierInfo.get(0);
  }
  
  public IGetKlassTaxonomyTreeResponseModel getKlassTaxonomyTreeData(IGetKlassTaxonomyTreeRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData) throws Exception, RDBMSException
  {
    IGetKlassTaxonomyTreeResponseModel returnModel = new GetKlassTaxonomyTreeResponseModel();
    // To fetch configdetails for choosed taxonomy and klassIds
    if (!(dataModel.getSelectedTaxonomyIds()
        .isEmpty()
        && dataModel.getSelectedTypes()
            .isEmpty())) {
      returnModel.setConfigDetails(getKlassesAndTaxonomyByIds
          .execute(dataModel));
    }
    
    if (configData.getNatureClasses()
        .isEmpty()
        && configData.getAttributionClasses()
            .isEmpty()
        && (configData.getTaxonomies()
            .isEmpty()
            && dataModel.getSearchText()
                .isEmpty()))
      return returnModel;
    
    String selectedType = dataModel.getSelectedCategory();
    List<String> moduleIds = InstanceTreeUtils
        .getModuleIdByEntityId(configData.getAllowedEntities());
    ISearchDTOBuilder searchDTOBuilder = rdbmsComponentUtils.getLocaleCatlogDAO()
        .getSearchDTOBuilder(searchAssembler.getBaseTypeByModule(moduleIds), RootFilter.TRUE,
            dataModel.getIsArchivePortal());
    
    fillUsecaseSpecificFilters(dataModel, configData, searchDTOBuilder);
    INewInstanceTreeRequestModel instanceTreeRequestModel = new GetInstanceTreeRequestModel();
    instanceTreeRequestModel.setFrom(dataModel.getFrom());
    instanceTreeRequestModel.setSize(dataModel.getSize());
    instanceTreeRequestModel.setAllSearch(dataModel.getAllSearch());
    instanceTreeRequestModel.setSelectedTypes(Arrays.asList(dataModel.getSelectedCategory()));
    searchUtils.fillSearchDTO(instanceTreeRequestModel, configData.getKlassIdsHavingRP(),
        configData.getTaxonomyIdsHavingRP(), configData.getTranslatableAttributeIds(),
        searchDTOBuilder, dataModel.getSearchableAttributeIds(), configData.getMajorTaxonomyIds());
    
    ISearchDTO searchDTOForTaxonomy = searchDTOBuilder.build();
    searchDTOForTaxonomy.getTaxonomyIds()
        .clear();
    searchDTOForTaxonomy.getClassIds()
        .clear();
    
    ISearchDTO searchDTO = searchDTOBuilder.build();
    List<String> classifierIds = new ArrayList<>();
    
    if (selectedType.equals("all") || (!dataModel.getSearchText()
        .isEmpty() && selectedType.equals(IGetKlassTaxonomyTreeResponseModel.TAXONOMIES))) {
      // Nature Class
      if (!configData.getNatureClasses()
          .isEmpty()) {
        List<ICategoryInformationModel> natureClasses = (List<ICategoryInformationModel>) configData
            .getNatureClasses()
            .get(0)
            .getChildren();
        getAllUtils.getFlatIdsList(natureClasses, classifierIds);
        if (!classifierIds.isEmpty()) {
          Map<String, Long> natureClassifierCount = getClassifierCount(searchDTO, classifierIds);
          for (ICategoryInformationModel natureClass : natureClasses) {
            if (natureClassifierCount.containsKey(natureClass.getCode())) {
              natureClass.setCount(natureClassifierCount.get(natureClass.getCode()));
            }
          }
        }
      }
      returnModel.getNatureClasses()
          .addAll(configData.getNatureClasses());
      
      // Non-Nature Class
      if (!configData.getAttributionClasses()
          .isEmpty()) {
        List<ICategoryInformationModel> attributionClasses = (List<ICategoryInformationModel>) configData
            .getAttributionClasses()
            .get(0)
            .getChildren();
        getAllUtils.getFlatIdsList(attributionClasses, classifierIds);
        if (!classifierIds.isEmpty()) {
          Map<String, Long> attributionClassifierCount = getClassifierCount(searchDTO,
              classifierIds);
          for (ICategoryInformationModel attributionClass : attributionClasses) {
            if (attributionClassifierCount.containsKey(attributionClass.getCode())) {
              attributionClass.setCount(attributionClassifierCount.get(attributionClass.getCode()));
            }
          }
        }
      }
      returnModel.getAttributionClasses()
          .addAll(configData.getAttributionClasses());
      
      // Taxonomies
      ICategoryInformationModel taxonomy = new CategoryInformationModel();
      List<ICategoryInformationModel> childrenTaxonomy = (List<ICategoryInformationModel>) taxonomy
          .getChildren();
      IAllowedTypesRequestModel model = new AllowedTypesRequestModel();
      model.setSearchText(dataModel.getSearchText());
      Integer from = dataModel.getFrom();
      Integer size = dataModel.getSize();
      model.setFrom(from.longValue());
      model.setSize(size.longValue());
      model.setUserId(context.getUserId());
      model.setIdsToExclude(new ArrayList<String>());
      model.setSelectionType("");
      ITaxonomyHierarchyModel execute = getAllTaxonomiesForAllowedTypesStrategy.execute(model);
      List<IConfigTaxonomyHierarchyInformationModel> list = execute.getList();
      taxonomy.setTotalChildrenCount(execute.getCount());
      IGetSelectedKlassesAndTaxonomiesByIdsResponseModel configDetails = returnModel.getConfigDetails();
      Map<String, IConfigTaxonomyHierarchyInformationModel> referenceTaxonomyMap = (Map<String, IConfigTaxonomyHierarchyInformationModel>) execute
          .getConfigDetails();
      configDetails.getReferencedTaxonomies().putAll(referenceTaxonomyMap);
      returnModel.setConfigDetails(configDetails);
      for (IConfigTaxonomyHierarchyInformationModel iConfigTaxonomyHierarchyInformationModel : list) {
        ICategoryInformationModel child = new CategoryInformationModel();
        child.setCode(iConfigTaxonomyHierarchyInformationModel.getCode());
        child.setId(iConfigTaxonomyHierarchyInformationModel.getId());
        child.setLabel(iConfigTaxonomyHierarchyInformationModel.getLabel());
        child.setIconKey(iConfigTaxonomyHierarchyInformationModel.getIconKey());
        childrenTaxonomy.add(child);
      }
      List<String> taxonomyIds = childrenTaxonomy.stream()
          .map(x -> x.getId())
          .collect(Collectors.toList());
      if (!taxonomyIds.isEmpty()) {
        ISearchDAO searchDAO = rdbmsComponentUtils.getLocaleCatlogDAO()
            .openSearchDAO(searchDTO);
        IClassifierAggregationDTO classifierAggregationDTO = new ClassifierAggregationDTO();
        classifierAggregationDTO
            .setAggregationType(IAggregationRequestDTO.AggregationType.byTaxonomy);
        classifierAggregationDTO.setClassifierIds(taxonomyIds);
        classifierAggregationDTO.setSize(taxonomyIds.size());
        IAggregationResultDTO aggregation = searchDAO.aggregation(classifierAggregationDTO);
        Map<String, Long> taxonomyCount = aggregation.getCount();
        for (ICategoryInformationModel categoryModel : childrenTaxonomy) {
          if (taxonomyCount.containsKey(categoryModel.getCode())) {
            categoryModel.setCount(taxonomyCount.get(categoryModel.getCode()));
          }
        }
      }
      returnModel.getTaxonomies()
          .add(taxonomy);
      
      return returnModel;
    }
    else {
      List<ICategoryInformationModel> klassTaxonomyInfo = dataModel.getKlassTaxonomyInfo();
      getAllUtils.getFlatIdsList(klassTaxonomyInfo, classifierIds);
      Long totalChildrenCount = configData.getTotalChildrenCount();
      
      Map<String, Long> classifierCount = getClassifierCount(searchDTO, classifierIds);
      
      if (dataModel.getClickedTaxonomyId() == null) {
        List<ICategoryInformationModel> listCategoryInformationModel = configData
            .getListBySelectionType(selectedType);
        ICategoryInformationModel categoryInformationModel = new CategoryInformationModel();
        List<ICategoryInformationModel> children = (List<ICategoryInformationModel>) categoryInformationModel
            .getChildren();
        
        if (!selectedType.equals(IGetKlassTaxonomyTreeResponseModel.TAXONOMIES)) {
          for (ICategoryInformationModel iCategoryInformationModel : listCategoryInformationModel) {
            Long count = 0L;
            if (classifierCount.containsKey(iCategoryInformationModel.getCode())) {
              count = classifierCount.get(iCategoryInformationModel.getCode());
            }
            iCategoryInformationModel.setCount(count);
            children.add(iCategoryInformationModel);
          }
          categoryInformationModel.setTotalChildrenCount(totalChildrenCount);
          returnModel.getListBySelectionType(selectedType)
              .add(categoryInformationModel);
        }
        else {
          List<ICategoryInformationModel> taxonomyList = getTaxonomyList(
              listCategoryInformationModel, searchDTOForTaxonomy, totalChildrenCount);
          for (ICategoryInformationModel taxonomy : taxonomyList) {
            children.add(taxonomy);
          }
          categoryInformationModel.setTotalChildrenCount(totalChildrenCount);
          returnModel.getListBySelectionType(selectedType)
              .add(categoryInformationModel);
        }
      }
      else {
        if (classifierIds.isEmpty()) {
          return returnModel;
        }
        
        if (!klassTaxonomyInfo.isEmpty()) {
          if (selectedType.equals(IGetKlassTaxonomyTreeResponseModel.TAXONOMIES)) {
            
            // For taxonomy hierarchy usecase.
            if (!dataModel.getClickedTaxonomyId()
                .equals("-1")) {
              List<ICategoryInformationModel> iCategoryInformationModel = returnModel
                  .getListBySelectionType(selectedType);
              if (!iCategoryInformationModel.isEmpty())
                iCategoryInformationModel.get(0)
                    .setChildren(new ArrayList<>());
            }
            returnModel.getListBySelectionType(selectedType)
                .addAll(fillTaxonomyCountInfo(searchDTO, classifierIds, klassTaxonomyInfo,
                    totalChildrenCount));
          }
        }
      }
      return returnModel;
    }
  }

  private Map<String, Long> getClassifierCount(ISearchDTO searchDTO, List<String> classifierIds)
      throws RDBMSException, IOException
  {
    ISearchDAO searchDAO = rdbmsComponentUtils.getLocaleCatlogDAO().openSearchDAO(searchDTO);
    IClassifierAggregationDTO classifierAggregationDTO = new ClassifierAggregationDTO();
    classifierAggregationDTO.setAggregationType(IAggregationRequestDTO.AggregationType.byClass);
    classifierAggregationDTO.setClassifierIds(classifierIds);
    classifierAggregationDTO.setSize(classifierIds.size());
    IAggregationResultDTO aggregation = searchDAO.aggregation(classifierAggregationDTO);
    Map<String, Long> classifierCount = aggregation.getCount();
    return classifierCount;
  }
  
  protected void fillUsecaseSpecificFilters(IGetKlassTaxonomyTreeRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData, ISearchDTOBuilder searchBuilder) throws Exception
  {
    
  }

}