package com.cs.core.runtime.instancetree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.constants.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenRequestModel;
import com.cs.core.config.interactor.model.instancetree.IConfigDetailsForGetFilterChildrenResponseModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.config.strategy.usecase.instancetree.IGetConfigDetailsForGetFilterChildrenStrategy;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.entity.dto.PropertyAggregationDTO;
import com.cs.core.rdbms.entity.dto.RangeAggregationDTO;
import com.cs.core.rdbms.entity.dto.RuleViolationAggregationDTO;
import com.cs.core.rdbms.entity.idto.IAggregationRequestDTO;
import com.cs.core.rdbms.entity.idto.IAggregationResultDTO;
import com.cs.core.rdbms.entity.idto.IRuleViolationAggregationDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.interactor.model.filter.FilterValueMatchModel;
import com.cs.core.runtime.interactor.model.filter.IFilterValueModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.cs.core.runtime.interactor.model.instancetree.GetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetFilterChildrenResponseModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.util.getall.SearchUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public abstract class AbstractGetFilterChildrenValues<P extends IGetFilterChildrenRequestModel, R extends IGetFilterChildrenResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected IGetConfigDetailsForGetFilterChildrenStrategy getConfigDetailsForGetFilterChildrenStrategy;
  
  @Autowired
  protected ModuleMappingUtil                             moduleMappingUtil;
  
  @Autowired
  protected GetAllUtils                                   getAllUtils;
  
  @Autowired
  protected InstanceTreeUtils                             instanceTreeUtils;
  
  @Autowired
  private SearchAssembler                                 searchAssembler;
  
  @Autowired
  private SearchUtils                                     searchUtils;
  
  @Autowired
  protected RDBMSComponentUtils                           rdbmsComponentUtils;
  
  protected abstract IConfigDetailsForGetFilterChildrenResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetFilterChildrenRequestModel configRequestModel) throws Exception;

  protected abstract IConfigDetailsForGetFilterChildrenRequestModel getConfigDetailsRequestModel();

  protected abstract List<IGetFilterChildrenModel> executeRuntimeStrategy(P model, IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception;

  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P model)
      throws Exception
  {
    List<String> entities = getModuleEntities(model);
    
    IConfigDetailsForGetFilterChildrenRequestModel configRequestModel = prepareConfigRequestModel(model, entities);
    IConfigDetailsForGetFilterChildrenResponseModel configDetails = executeConfigDetailsStrategy(configRequestModel);
    
    prepareRuntimeRequestModel(model, configDetails);
    List<IGetFilterChildrenModel> filterChildrens = executeRuntimeStrategy(model, configDetails);
    IGetFilterChildrenResponseModel responseModel = new GetFilterChildrenResponseModel();
    responseModel.setFilterChildren(filterChildrens);
    responseModel.setReferencedProperty(configDetails.getReferencedProperty());
    return (R) responseModel;
  }

  protected IConfigDetailsForGetFilterChildrenRequestModel prepareConfigRequestModel(P model,
      List<String> entities)
  {
    IConfigDetailsForGetFilterChildrenRequestModel configRequestModel = getConfigDetailsRequestModel();
    configRequestModel.setAllowedEntities(entities);
    configRequestModel.setId(model.getId());
    configRequestModel.setFilterType(model.getFilterType());
    configRequestModel.setUserId(context.getUserId());
    configRequestModel.setKpiId(model.getKpiId());
    configRequestModel.setSearchText(model.getSearchText());
    return configRequestModel;
  }

  protected void prepareRuntimeRequestModel(P model,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails)
  {
    model.setModuleEntities(configDetails.getAllowedEntities());
    model.setTranslatableAttributeIds(configDetails.getTranslatableAttributeIds());
    model.setKlassIdsHavingRP(configDetails.getKlassIdsHavingRP());
    model.setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    model.setReferencedProperty(configDetails.getReferencedProperty());
    model.setSearchableAttributeIds(configDetails.getSearchableAttributeIds());
    model.setMajorTaxonomyIds(configDetails.getMajorTaxonomyIds());
  }
  
  protected String generateSearchExpression(IGetFilterChildrenRequestModel dataModel)
  {
    fillDetailsForSearchText(dataModel);
    return instanceTreeUtils.generateSearchExpression(dataModel);
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected void fillDetailsForSearchText(IGetFilterChildrenRequestModel dataModel)
  {
    String searchText = dataModel.getSearchText();
    if (!searchText.isEmpty() && dataModel.getFilterType().equals(CommonConstants.ATTRIBUTE_PROPERTY)) {
      IPropertyInstanceFilterModel attributeModel = new PropertyInstanceValueTypeFilterModer();
      attributeModel.setAdvancedSearchFilter(false);
      attributeModel.setId(dataModel.getId());
      attributeModel.setType(dataModel.getFilterType());
      List<IFilterValueModel> mandatory = new ArrayList<IFilterValueModel>();
      
      FilterValueMatchModel filterValue = new FilterValueMatchModel();
      filterValue.setId(dataModel.getId());
      filterValue.setAdvancedSearchFilter(false);
      filterValue.setType(CommonConstants.CONTAINS);
      filterValue.setValue(searchText);
      mandatory.add(filterValue);
      
      attributeModel.setMandatory(mandatory);
      attributeModel.setShould(new ArrayList<IFilterValueModel>());
      
      List<IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
      attributes.add(attributeModel);
    }
  }
  
  protected List<String> getModuleEntities(IGetFilterChildrenRequestModel model) throws Exception
  {
    IModule module = moduleMappingUtil.getModule(model.getModuleId());
    List<String> entities = module.getEntities();
    return entities;
  }
  
  protected List<IGetFilterChildrenModel> getFilterChildrenValues(
      P dataModel, IConfigDetailsForGetFilterChildrenResponseModel configDetails) throws Exception
  {
    ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
    ISearchDTO searchDTO = fillSearchDTO(configDetails, dataModel, localeCatalogDao);
    ISearchDAO searchDAO = localeCatalogDao.openSearchDAO(searchDTO);
    
    return fillFilterChildren(dataModel, configDetails, localeCatalogDao, searchDAO);
  }

  protected ISearchDTO fillSearchDTO(IConfigDetailsForGetFilterChildrenResponseModel configDetails, P dataModel, ILocaleCatalogDAO localeCatalogDao) throws Exception
  {
    List<String> moduleIds = InstanceTreeUtils.getModuleIdByEntityId(configDetails.getAllowedEntities());
    
    ISearchDTOBuilder searchBuilder = localeCatalogDao
        .getSearchDTOBuilder(searchAssembler.getBaseTypeByModule(moduleIds), RootFilter.TRUE, dataModel.getIsArchivePortal());
    
    searchUtils.fillSearchDTO(dataModel, configDetails.getKlassIdsHavingRP(),
        configDetails.getTaxonomyIdsHavingRP(), configDetails.getTranslatableAttributeIds(),
        searchBuilder, configDetails.getSearchableAttributeIds(), configDetails.getMajorTaxonomyIds());
    
    fillUsecaseSpecificFilters(dataModel, configDetails, searchBuilder);
    
    return searchBuilder.build();
    
  }

  protected List<IGetFilterChildrenModel> fillFilterChildren(P dataModel,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails,
      ILocaleCatalogDAO localeCatalogDao, ISearchDAO searchDAO) throws IOException, RDBMSException
  {
    if (dataModel.getFilterType().equals(CommonConstants.COLOR_VOILATION_FILTER)) {
      IRuleViolationAggregationDTO request = new RuleViolationAggregationDTO(
          localeCatalogDao.getLocaleCatalogDTO().getLocaleID());
      IAggregationResultDTO aggregationResult = searchDAO.aggregation(request);
      
      return getAllUtils.fillRuleViolationsCount(aggregationResult, configDetails.getRuleViolationsLabels());
    }
    
    else {
      
      IPropertyDTO property = ConfigurationDAO.instance().getPropertyByCode(dataModel.getId());
      boolean isTranslatable = dataModel.getTranslatableAttributeIds().contains(dataModel.getId());
      IAggregationRequestDTO request;
      
      if(property.isNumeric()) {
        request = new RangeAggregationDTO(property, isTranslatable);
      }
      else {
      request = new PropertyAggregationDTO(property, isTranslatable);
      ((PropertyAggregationDTO)request).setBucketSearch(dataModel.getSearchText());
      }
      
      request.setSize(dataModel.getSize());
      IAggregationResultDTO aggregation = searchDAO.aggregation(request);
      
      return getAllUtils.getFilterChildren(configDetails.getReferencedProperty(),
          aggregation.getCount());
      
    }
  }
  
  protected void fillUsecaseSpecificFilters(P dataModel,
      IConfigDetailsForGetFilterChildrenResponseModel configDetails, ISearchDTOBuilder searchBuilder) throws Exception
  {
    
  }
  
}
