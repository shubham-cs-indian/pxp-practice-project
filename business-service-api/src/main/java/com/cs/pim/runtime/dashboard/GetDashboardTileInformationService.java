package com.cs.pim.runtime.dashboard;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.runtime.dashboard.AbstractGetDashboardTileInformationService;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.model.filter.ISortModel;
import com.cs.core.runtime.interactor.model.instancetree.ISpecialUsecaseFiltersModel;
import com.cs.core.runtime.interactor.model.instancetree.SpecialUsecaseFiltersModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.strategy.dashboard.IGetConfigDetailsForDashboardTileInformationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GetDashboardTileInformationService extends AbstractGetDashboardTileInformationService<IDashboardInformationRequestModel, IDashboardInformationResponseModel>
    implements IGetDashboardTileInformationService {
  
  @Autowired
  protected IGetConfigDetailsForDashboardTileInformationStrategy getConfigDetailsForDashboardTileInformationStrategy;
  
  @Override
  protected IDashboardInformationResponseModel executeInternal(
      IDashboardInformationRequestModel dashboardInformationRequestModel) throws Exception
  {
    try {
      return super.executeInternal(dashboardInformationRequestModel);
    }
    catch (UserNotHaveReadPermission e) {
      throw new UserNotHaveReadPermissionForArticle();
    }
    catch (KlassNotFoundException e) {
      throw new ArticleKlassNotFoundException(e);
    }
  }
  
  @Override
  protected IConfigDetailsForInstanceTreeGetModel getConfigDetails(IIdParameterModel model)
      throws Exception
  {
    return getConfigDetailsForDashboardTileInformationStrategy.execute(model);
  }
  
  public String generateSearchExpression(IDashboardInformationRequestModel dataModel)
  {
    TransactionData transactionData = transactionThreadData.getTransactionData();
    BaseType baseTypeByModule = searchAssembler.getBaseTypeByModule(dataModel.getModuleId());
    StringBuilder searchExpression = searchAssembler.getBaseQuery(baseTypeByModule);

    StringBuilder entityFilter = searchAssembler.generateEntityFilterExpression(List.copyOf(dataModel.getKlassIdsHavingRP()), List.copyOf(dataModel.getTaxonomyIdsHavingRP()), true, dataModel.getMajorTaxonomyIds());
    if(!entityFilter.toString().isEmpty()){
      searchExpression.append(entityFilter.toString());
    }
    if(baseTypeByModule.equals(BaseType.ASSET)) {
      Optional<ISortModel> findAny = dataModel.getSortOptions().stream().filter(x-> x.getSortField().equals("expiredassets")).findAny();
      if(findAny.isPresent()) {
        ISpecialUsecaseFiltersModel specialUsecaseFilterModel = new SpecialUsecaseFiltersModel();
        specialUsecaseFilterModel.setAppliedValues(List.of(CommonConstants.EXPIRED_FILTER));
        String assetExpiryFilter = searchAssembler.getAssetExpiryFilter(specialUsecaseFilterModel);
        searchExpression.append(assetExpiryFilter);
      }
    }

    return searchExpression.toString();
  }
}
