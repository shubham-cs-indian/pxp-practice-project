package com.cs.core.runtime.instancetree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.constants.CommonConstants;
import com.cs.core.elastic.das.ISearchDAO;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.entity.idto.ISearchDTO;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder;
import com.cs.core.rdbms.entity.idto.ISearchDTOBuilder.RootFilter;
import com.cs.core.rdbms.entity.idto.ISortDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.goldenrecord.bucket.IGetNewFilterAndSortDataForGoldenRecord;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForFilterAndSortInfoRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetNewFilterAndSortDataResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;
import com.cs.core.runtime.store.strategy.usecase.instancetree.IGetConfigDetailsForGoldenRecordFilterAndSortDataStrategy;

@Service
public class GetNewFilterAndSortDataForGoldenRecord extends AbstractGetNewFilterAndSortData<IGetNewFilterAndSortDataRequestModel, IGetNewFilterAndSortDataResponseModel>
    implements IGetNewFilterAndSortDataForGoldenRecord {
  
  @Autowired
  protected IGetConfigDetailsForGoldenRecordFilterAndSortDataStrategy getConfigDetailsForGoldenRecordFilterAndSortDataStrategy;
  
  @Override
  public ServiceType getServiceType()
  {
    return ServiceType.GET;
  }

  @Override
  protected ConfigDetailsForFilterAndSortInfoRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForFilterAndSortInfoRequestModel();
  }

  @Override
  protected IGetNewFilterAndSortDataResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForFilterAndSortInfoRequestModel configRequsetModel) throws Exception
  {
    return getConfigDetailsForGoldenRecordFilterAndSortDataStrategy.execute(configRequsetModel);
  }
  
  @Override
  protected List<String> getModuleEntities(IGetNewFilterAndSortDataRequestModel model) throws Exception
  {
    return Arrays.asList(CommonConstants.ARTICLE_INSTANCE_MODULE_ENTITY);
  }
  
  @Override
  protected void addRuleViolationFilter(IGetNewFilterAndSortDataResponseModel responseModel)
  {
    // Rule Violation not needed in the case of golden record.
  }

  @Override
  protected void fillFilterData(IGetNewFilterAndSortDataRequestModel model,
      IGetNewFilterAndSortDataResponseModel responseModel) throws Exception
  {
      ILocaleCatalogDAO localeCatalogDao = rdbmsComponentUtils.getLocaleCatlogDAO();
      List<ISortDTO> sortOptions = getAllUtils.getSortOptions(new ArrayList<>(), responseModel.getReferencedAttributes());
      
      List<String> moduleIds = InstanceTreeUtils.getModuleIdByEntityId(responseModel.getAllowedEntities());
      
      ISearchDTOBuilder searchBuilder = localeCatalogDao.getSearchDTOBuilder(searchAssembler.getBaseTypeByModule(moduleIds), RootFilter.TRUE, model.getIsArchivePortal());
      searchBuilder.addSort(sortOptions);
      
      searchUtils.fillSearchDTO(model, responseModel.getKlassIdsHavingRP(), responseModel.getTaxonomyIdsHavingRP(),
          responseModel.getTranslatableAttributeIds(), searchBuilder, responseModel.getSearchableAttributeIds(),
          responseModel.getMajorTaxonomyIds());
      ISearchDTO searchDTO = searchBuilder.build();
      //fillUsecaseSpecificFilters(model,configDetails,searchBuilder);
      ISearchDAO searchDAO = localeCatalogDao.openGoldenRecordBucketSearchDAO(searchDTO);
      List<INewApplicableFilterModel> filterData = getAllUtils.fillApplicableFilters(
          responseModel.getFilterData(), responseModel.getTranslatableAttributeIds(), searchDAO);
      responseModel.setFilterData(filterData);
  }
  
  @Override
  protected void additionalInformationForRelationshipFilter(
      IGetNewFilterAndSortDataRequestModel model,
      IGetNewFilterAndSortDataResponseModel responseModel)
  {
    
  }
  
}
