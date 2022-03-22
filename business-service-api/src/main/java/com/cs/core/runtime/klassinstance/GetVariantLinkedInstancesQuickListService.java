package com.cs.core.runtime.klassinstance;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.*;
import com.cs.core.config.interactor.model.klass.GetDefaultKlassesModel;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.klass.IReferencedKlassDetailStrategyModel;
import com.cs.core.config.interactor.model.taxonomy.ApplicableFilterModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.IGetTaxonomiesStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetFilterAndSortDataStrategy;
import com.cs.core.config.strategy.usecase.variantcontext.IGetConfigDetailsForGetLinkedInstancesQuicklistStrategy;
import com.cs.core.exception.ModuleEntityNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.process.idao.IRDBMSOrderedCursor;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.klassinstance.GetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstanceTreeModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInformationModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantLinkedInstancesQuicklistModel;
import com.cs.core.runtime.interactor.model.variants.IVariantLinkedInstancesQuickListModel;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceBuilder;
import com.cs.core.runtime.interactor.utils.klassinstance.KlassInstanceUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.VariantInstanceUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetPostConfigDetailsStrategy;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetVariantLinkedInstancesQuickListService extends AbstractRuntimeService<IVariantLinkedInstancesQuickListModel, IGetKlassInstanceTreeModel>
    implements IGetVariantLinkedInstancesQuickListService {
  
  @Autowired
  protected IGetConfigDetailsForGetLinkedInstancesQuicklistStrategy getConfigDetailsForGetLinkedInstancesQuicklistStrategy;
  
  @Autowired
  protected ISessionContext                                         context;
  
  @Autowired
  protected IGetFilterAndSortDataStrategy                           getFilterAndSortDataForKlassStrategy;
  
  @Autowired
  protected IGetTaxonomiesStrategy                                  getAllTypeTaxonomiesStrategy;

  @Autowired
  protected RDBMSComponentUtils                                     rdbmsComponentUtils;
  
  @Autowired
  protected IGetPostConfigDetailsStrategy                           postConfigDetailsForGetContextualTabStrategy;
  
  @Autowired
  protected VariantInstanceUtils                                    variantInstanceUtils;
  
  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  protected IGetKlassInstanceTreeModel executeInternal(IVariantLinkedInstancesQuickListModel model)
      throws Exception
  {
    String loginUserId = context.getUserId();
    model.setCurrentUserId(loginUserId);
    
    IIdParameterModel idModel = new IdParameterModel(model.getContextId());
    idModel.setCurrentUserId(loginUserId);
    IConfigDetailsForGetVariantLinkedInstancesQuicklistModel configDetails = getConfigDetailsForGetLinkedInstancesQuicklistStrategy
        .execute(idModel);
    
    // retain only entityId from entitiesList and
    // if entities became empty that means entityId is no longer present hence
    // rise an exception
    List<String> entitiesList = (List<String>) configDetails.getEntities();
    entitiesList.retainAll(Arrays.asList(model.getEntityId()));
    if (entitiesList.isEmpty()) {
      throw new ModuleEntityNotFoundException();
    }
    model.setEntities(entitiesList);
    
    String parentTaxonomyId = model.getParentTaxonomyId();
    parentTaxonomyId = parentTaxonomyId == null ? "" : parentTaxonomyId;
    
    IGetFilterAndSortDataRequestModel sortAndFilterDataRequestModel = KlassInstanceUtils
        .prepareSortAndFilterDataRequestModel(model);
    
    IGetFilterInfoModel filterInfoModel = null;
    if (!parentTaxonomyId.equals("")) {
      if (model.getSelectedTaxonomyIds()
          .size() == 0) {
        List<String> ids = new ArrayList<>();
        ids.add(parentTaxonomyId);
        sortAndFilterDataRequestModel.setTypeIds(ids);
      }
      else {
        sortAndFilterDataRequestModel.setTypeIds(model.getSelectedTaxonomyIds());
      }
      /*IdsListParameterModel idsListParameterModel = new IdsListParameterModel();
      idsListParameterModel.getIds().addAll(model.getEntities());*/
      sortAndFilterDataRequestModel.setTypeIds(model.getEntities());
      filterInfoModel = getTaxonomyFilterInfo(sortAndFilterDataRequestModel);
    }
    else {
      List<String> standardKlassIds = KlassInstanceUtils
          .getStandardKlassIds((List<String>) configDetails.getEntities());
      sortAndFilterDataRequestModel.setTypeIds(standardKlassIds);
      filterInfoModel = getFilterInfoModel(
          getFilterAndSortDataForKlassStrategy.execute(sortAndFilterDataRequestModel));
    }
    model.setSearchableAttributes(filterInfoModel.getSearchableAttributes());
    List<IApplicableFilterModel> filterDataModel = filterInfoModel.getFilterData();
    List<String> tagIdsToSet = new ArrayList<>();
    for (IApplicableFilterModel tag : filterDataModel) {
      if (IStandardConfig.TagType.AllTagTypes.contains(tag.getType())) {
        tagIdsToSet.add(tag.getId());
      }
    }
    List<IPropertyInstanceFilterModel> filterTagsToSet = new ArrayList<>();
    List<IPropertyInstanceFilterModel> filterTags = (List<IPropertyInstanceFilterModel>) model
        .getTags();
    for (IPropertyInstanceFilterModel filterTag : filterTags) {
      if (tagIdsToSet.contains(filterTag.getId())) {
        filterTagsToSet.add(filterTag);
      }
    }
    model.setTags(filterTagsToSet);
    model.setFilterInfo(filterInfoModel);
    model.setKlassIdsHavingRP(configDetails.getKlassIdsHavingRP());
    model.setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    model.setAllowedEntities(configDetails.getAllowedEntities());
    IGetKlassInstanceTreeModel returnModel = executeGetQuickListElements(model, configDetails);
    returnModel.setFilterInfo(filterInfoModel);
    getAndSetPostConfigData(returnModel);
    IGetDefaultKlassesModel defaultTypes = new GetDefaultKlassesModel();
    defaultTypes.setChildren(new ArrayList<>());
    returnModel.setDefaultTypes(defaultTypes);
    /*
    returnModel.setTaxonomies(getTaxonomyParentList(model.getModuleId(),
    configDetails.getAllowedEntities(), model.getAllSearch(),
    configDetails.getTaxonomyIdsHavingRP(), configDetails.getKlassIdsHavingRP()));*/
    return returnModel;
  }
  
  protected IGetFilterInfoModel getTaxonomyFilterInfo(IGetFilterAndSortDataRequestModel idsModel)
      throws Exception
  {
    IGetFilterInformationModel filterInformationModel = getFilterAndSortDataForKlassStrategy
        .execute(idsModel);
    return getFilterInfoModel(filterInformationModel);
  }
  
  @SuppressWarnings("unchecked")
  protected IGetFilterInfoModel getFilterInfoModel(
      IGetFilterInformationModel filterInformationModel)
  {
    IGetFilterInfoModel filterInfoModel = new GetFilterInfoModel();
    filterInfoModel.setDefaultFilterTags(filterInformationModel.getDefaultFilterTags());
    List<IConfigEntityTreeInformationModel> tags = filterInformationModel.getFilterData()
        .getTags();
    List<IConfigEntityTreeInformationModel> attributes = filterInformationModel.getFilterData()
        .getAttributes();
    List<IConfigEntityTreeInformationModel> filterInfos = new ArrayList<>();
    filterInfos.addAll(tags);
    filterInfos.addAll(attributes);
    List<IApplicableFilterModel> applicableFilters = new ArrayList<>();
    for (IConfigEntityTreeInformationModel filterInfo : filterInfos) {
      IApplicableFilterModel filterModel = new ApplicableFilterModel();
      filterModel.setId(filterInfo.getId());
      filterModel.setLabel(filterInfo.getLabel());
      filterModel.setType(filterInfo.getType());
      filterModel.setDefaultUnit(filterInfo.getDefaultUnit());
      filterModel.setPrecision(filterInfo.getPrecision());
      List<IApplicableFilterModel> childrensToSet = new ArrayList<>();
      List<IConfigEntityTreeInformationModel> childrens = (List<IConfigEntityTreeInformationModel>) filterInfo
          .getChildren();
      for (IConfigEntityTreeInformationModel children : childrens) {
        IApplicableFilterModel childrenFilterModel = new ApplicableFilterModel();
        childrenFilterModel.setId(children.getId());
        childrenFilterModel.setLabel(children.getLabel());
        childrenFilterModel.setType(children.getType());
        childrensToSet.add(childrenFilterModel);
      }
      filterModel.setChildren(childrensToSet);
      applicableFilters.add(filterModel);
    }
    filterInfoModel.setFilterData(applicableFilters);
    filterInfoModel.setSortData(filterInformationModel.getSortData());
    filterInfoModel.setSearchableAttributes(filterInformationModel.getSearchableAttributes());
    filterInfoModel.setConfigDetails(filterInformationModel.getConfigDetails());
    return filterInfoModel;
  }
  
  protected void getAndSetPostConfigData(IGetKlassInstanceTreeModel model) throws Exception
  {
    Set<String> klassIdsSet = new HashSet<>();
    List<IKlassInstanceInformationModel> children = model.getChildren();
    
    for (IKlassInstanceInformationModel child : children) {
      klassIdsSet.addAll(child.getTypes());
    }
    
    if (!klassIdsSet.isEmpty()) {
      List<String> klassIdsList = new ArrayList<>(klassIdsSet);
      IGetPostConfigDetailsRequestModel requestModel = new GetPostConfigDetailsRequestModel();
      requestModel.setKlassIds(klassIdsList);
      IGetPostConfigDetailsResponseModel responseModel = postConfigDetailsForGetContextualTabStrategy
          .execute(requestModel);
      
      Map<String, IReferencedKlassDetailStrategyModel> referencedKlasses = model
          .getReferencedKlasses();
      referencedKlasses.putAll(responseModel.getReferencedKlasses());
    }
  }
  
  protected IGetKlassInstanceTreeModel executeGetQuickListElements(
      IVariantLinkedInstancesQuickListModel klassInstanceQuickListModel,
      IConfigDetailsForGetVariantLinkedInstancesQuicklistModel configDetails) throws Exception
  {
    String klassByEntityType = variantInstanceUtils.getKlassByEntityType(klassInstanceQuickListModel.getEntityId());
    BaseType baseType = BaseEntityUtils.getBaseTypeByKlass(klassByEntityType);
    
    IGetKlassInstanceTreeModel response = new GetKlassInstanceTreeModel();
    List<IKlassInstanceInformationModel> childrens = new ArrayList<>();
    IRDBMSOrderedCursor<IBaseEntityDTO> cursor = rdbmsComponentUtils.getAllEntities(baseType);
    /*cursor.allowChildren();*/
    List<IBaseEntityDTO> entities = cursor.getNext(klassInstanceQuickListModel.getFrom(),
        klassInstanceQuickListModel.getSize());
    populateResponseList(childrens, entities);
    response.setChildren(childrens);
    response.setTotalContents(cursor.getCount());
    return response;
  }
  
  private void populateResponseList(List<IKlassInstanceInformationModel> responseList,
      List<IBaseEntityDTO> listOfDTO) throws Exception
  {
    for (IBaseEntityDTO baseEntityDTO : listOfDTO) {
      IKlassInstanceInformationModel klassInstanceInformationModel = KlassInstanceBuilder
          .getKlassInstanceInformationModel(baseEntityDTO, rdbmsComponentUtils);
      if (klassInstanceInformationModel != null) {
        responseList.add(klassInstanceInformationModel);
      }
    }
  }
  
  /*  @SuppressWarnings("unchecked")
  private List<ITaxonomyInformationModel> getTaxonomyParentList(String moduleId,
      Set<String> allowedEntities, String allSearch, Set<String> taxonomyIdsHavingRP,
      Set<String> klassIdsHavingRP) throws Exception
  {
    IGetTaxonomyTreeModel getTaxonomyTreeModel = new GetTaxonomyTreeModel();
    List<IConfigEntityTreeInformationModel> categoryInfo = new ArrayList<>();
    List<ITaxonomyInformationModel> taxonomyParentList = new ArrayList<>();
    IGetTaxonomyRequestModel getTaxonomyRequestModel = new GetTaxonomyRequestModel();
    getTaxonomyRequestModel.setId("-1");
    IGetAttributionTaxonomyModel taxonomyTree = getAllTypeTaxonomiesStrategy.execute(getTaxonomyRequestModel);
    List<ITaxonomy> rootLevelTaxonomies = (List<ITaxonomy>) taxonomyTree.getChildren();
    rootLevelTaxonomies.forEach(rootLevelTaxonomy -> {
      ITaxonomyInformationModel categoryInformationModel = new TaxonomyInformationModel();
      categoryInformationModel.setId(rootLevelTaxonomy.getId());
      categoryInformationModel.setLabel(rootLevelTaxonomy.getLabel());
      taxonomyParentList.add(categoryInformationModel);
    });
    ITaxonomyInformationModel klassTaxonomy = new TaxonomyInformationModel();
    klassTaxonomy.setId("-1");
    klassTaxonomy.setChildren(taxonomyParentList);
    categoryInfo.add(klassTaxonomy);
  
    getTaxonomyTreeModel.setIsKlassTaxonomy(false);
    getTaxonomyTreeModel.setCategoryInfo(categoryInfo);
    getTaxonomyTreeModel.setModuleEntities(getModule(allowedEntities, moduleId).getEntities());
    getTaxonomyTreeModel.setCurrentUserId(context.getUserId());
    getTaxonomyTreeModel.setAllSearch(allSearch);
    getTaxonomyTreeModel.setKlassIdsHavingRP(klassIdsHavingRP);
  
    ITaxonomyInformationModel infoModel = getTaxonomyContentCountStrategy.execute(getTaxonomyTreeModel);
    List<ITaxonomyInformationModel> childrens = (List<ITaxonomyInformationModel>) infoModel.getChildren();
    return childrens;
  }*/
  
}
