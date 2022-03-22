package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.interactor.model.instancetree.INewInstanceTreeRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.NewApplicableFilterModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.util.getall.SearchUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForFilterAndSortDataStrategy;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;

public abstract class AbstractGetNewFilterAndSortData<P extends IGetNewFilterAndSortDataRequestModel, R extends IGetNewFilterAndSortDataResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ModuleMappingUtil                             moduleMappingUtil;
  
  @Autowired
  protected ISessionContext                               context;
  
  @Autowired
  protected GetAllUtils                                   getAllUtils;
  
  @Autowired
  private InstanceTreeUtils                               instanceTreeUtils;
  
  @Autowired
  protected IGetConfigDetailsForFilterAndSortDataStrategy getConfigDetailsForFilterAndSortDataStrategy;
  
  @Autowired
  protected SearchUtils                                   searchUtils;
  
  @Autowired
  protected SearchAssembler                               searchAssembler;
  
  @Autowired
  protected RDBMSComponentUtils                           rdbmsComponentUtils;
  
  protected abstract IConfigDetailsForFilterAndSortInfoRequestModel getConfigDetailsRequestModel();
  
  protected abstract IGetNewFilterAndSortDataResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForFilterAndSortInfoRequestModel configRequsetModel) throws Exception;
  
  protected abstract void additionalInformationForRelationshipFilter(P model, IGetNewFilterAndSortDataResponseModel responseModel);
  
  @Override
  @SuppressWarnings("unchecked")
  protected R executeInternal(P model) throws Exception
  {
    List<String> entities = getModuleEntities(model);
    
    // model.setModuleId(InstanceTreeUtils.getModuleIdByEntityId(entities.get(0)));
    
    IConfigDetailsForFilterAndSortInfoRequestModel configRequestModel = prepareConfigRequestModel(model, entities);
    IGetNewFilterAndSortDataResponseModel responseModel = executeConfigDetailsStrategy(configRequestModel);
    model.getSelectedTaxonomyIds().addAll(responseModel.getTaxonomyIdsHavingRP());
    model.getSelectedTypes().addAll(responseModel.getKlassIdsHavingRP());
    additionalInformationForRelationshipFilter(model, responseModel);
    
    responseModel.setCount(responseModel.getSortData().size());
    fillFilterData(model, responseModel);
    addRuleViolationFilter(responseModel);
    return (R) responseModel;
  }
  
  protected void fillFilterData(P model, IGetNewFilterAndSortDataResponseModel responseModel) throws Exception
  {
    if (!responseModel.getFilterData().isEmpty()) {
      if (model.getModuleId() == null || model.getModuleId().isEmpty())
        model.setModuleId(InstanceTreeUtils.getModuleIdByEntityId(responseModel.getAllowedEntities().get(0)));
      String searchExpression = generateSearchExpression(model);
      List<INewApplicableFilterModel> filterData = getAllUtils.getApplicableFilters(responseModel.getFilterData(), searchExpression, false);
      responseModel.setFilterData(filterData);
    }
  }
  
  protected void prepareRuntimeRequestModel(P model, IGetNewFilterAndSortDataResponseModel responseModel)
  {
    model.setKlassIdsHavingRP(responseModel.getKlassIdsHavingRP());
    model.setTaxonomyIdsHavingRP(responseModel.getTaxonomyIdsHavingRP());
    model.setModuleEntities(responseModel.getAllowedEntities());
    model.setFilterData(responseModel.getFilterData());
    model.setTranslatableAttributeIds(responseModel.getTranslatableAttributeIds());
    model.setSearchableAttributeIds(responseModel.getSearchableAttributeIds());
  }
  
  protected void addRuleViolationFilter(IGetNewFilterAndSortDataResponseModel responseModel)
  {
    INewApplicableFilterModel dataRuleVoilation = new NewApplicableFilterModel();
    dataRuleVoilation.setId(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setCode(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setType(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setPropertyType(CommonConstants.COLOR_VOILATION_FILTER);
    dataRuleVoilation.setLabel("Rule Violations");
    responseModel.getFilterData().add(dataRuleVoilation);
  }
  
  @SuppressWarnings("rawtypes")
  protected IConfigDetailsForFilterAndSortInfoRequestModel prepareConfigRequestModel(P model, List<String> entities)
  {
    List<String> attributeIds = new ArrayList<>();
    List<String> tagIds = new ArrayList<>();
    
    List<IPropertyInstanceFilterModel> attributes = model.getAttributes();
    for (IPropertyInstanceFilterModel attribute : attributes) {
      attributeIds.add(attribute.getId());
    }
    List<IPropertyInstanceFilterModel> tags = model.getTags();
    for (IPropertyInstanceFilterModel tag : tags) {
      tagIds.add(tag.getId());
    }
    
    IConfigDetailsForFilterAndSortInfoRequestModel configRequsetModel = getConfigDetailsRequestModel();
    configRequsetModel.setAllowedEntities(entities);
    configRequsetModel.setUserId(context.getUserId());
    configRequsetModel.setAttributeIds(attributeIds);
    configRequsetModel.setTagIds(tagIds);
    configRequsetModel.setPaginatedFilterInfo(model.getPaginatedFilterInfo());
    configRequsetModel.setPaginatedSortInfo(model.getPaginatedSortInfo());
    configRequsetModel.setTaxonomyIds(model.getSelectedTaxonomyIds());
    configRequsetModel.setKpiId(model.getKpiId());
    configRequsetModel.setClickedTaxonomyId(model.getClickedTaxonomyId());
    configRequsetModel.setSelectedTypes(((INewInstanceTreeRequestModel) model).getSelectedTypes());
    configRequsetModel.setIsBookmark(model.getIsBookmark());
    return configRequsetModel;
  }
  
  protected List<String> getModuleEntities(IGetNewFilterAndSortDataRequestModel model) throws Exception
  {
    String moduleId = model.getModuleId();
    List<String> moduleEntities = model.getModuleEntities();
    if (moduleId != null) {
      moduleEntities.addAll(moduleMappingUtil.getModule(moduleId).getEntities());
    }
    return moduleEntities;
  }
  
  public String generateSearchExpression(IGetNewFilterAndSortDataRequestModel dataModel)
  {
    return instanceTreeUtils.generateSearchExpression(dataModel);
  }
  
  protected void evaluateAndFillFilterData(
      P dataModel, IGetNewFilterAndSortDataResponseModel responseModel) throws Exception
  {
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    
    List<String> moduleIds = InstanceTreeUtils.getModuleIdByEntityId(responseModel.getAllowedEntities());
    
    ISearchDTOBuilder searchBuilder = localeCatalogDao
        .getSearchDTOBuilder(searchAssembler.getBaseTypeByModule(moduleIds), RootFilter.TRUE, dataModel.getIsArchivePortal());
    
    searchUtils.fillSearchDTO(dataModel, responseModel.getKlassIdsHavingRP(),
        responseModel.getTaxonomyIdsHavingRP(), responseModel.getTranslatableAttributeIds(),
        searchBuilder, responseModel.getSearchableAttributeIds(), responseModel.getMajorTaxonomyIds());
    
    fillUsecaseSpecificFilters(dataModel, responseModel, searchBuilder);
    
    ISearchDTO searchDTO = searchBuilder.build();
    
    responseModel.setFilterData(getAllUtils.getApplicableFilters(searchDTO, responseModel.getFilterData(),
        responseModel.getTranslatableAttributeIds()));
  }
  
  protected void fillUsecaseSpecificFilters(P dataModel,
      IGetNewFilterAndSortDataResponseModel configDetails, ISearchDTOBuilder searchBuilder) throws Exception
  {
    
  }
}
