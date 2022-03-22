package com.cs.dam.runtime.dashboard;

import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
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
import com.cs.core.runtime.interactor.model.filter.FilterValueRangeModel;
import com.cs.core.runtime.interactor.model.filter.IFilterValueRangeModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.filter.PropertyInstanceValueTypeFilterModer;
import com.cs.core.runtime.interactor.model.instancetree.ISpecialUsecaseFiltersModel;
import com.cs.core.runtime.interactor.model.instancetree.SpecialUsecaseFiltersModel;
import com.cs.core.runtime.strategy.dashboard.IGetConfigDetailsForDashboardTileInformationStrategy;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class GetDashboardTileInformationForAssetsAboutToExpireService extends AbstractGetDashboardTileInformationService<IDashboardInformationRequestModel, IDashboardInformationResponseModel>
    implements IGetDashboardTileInformationForAssetAboutToExpireService {
  
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
  
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public String generateSearchExpression(IDashboardInformationRequestModel dataModel) throws CSInitializationException
  {
    BaseType baseTypeByModule = searchAssembler.getBaseTypeByModule(dataModel.getModuleId());
    StringBuilder searchExpression = searchAssembler.getBaseQuery(baseTypeByModule);

    StringBuilder permissionExp = searchAssembler.generateEntityFilterExpression(List.copyOf(dataModel.getKlassIdsHavingRP()), List.copyOf(dataModel.getTaxonomyIdsHavingRP()), true, dataModel.getMajorTaxonomyIds());
    if(!permissionExp.toString().isEmpty()){
      searchExpression.append(permissionExp);
    }

    ISpecialUsecaseFiltersModel specialUsecaseFilter = new SpecialUsecaseFiltersModel();
    specialUsecaseFilter.setAppliedValues(List.of(CommonConstants.NON_EXPIRED_FILTER));
    String assetExpiryFilter = searchAssembler.getAssetExpiryFilter(specialUsecaseFilter);
    searchExpression.append(assetExpiryFilter);
    IPropertyInstanceFilterModel propertyInstanceFilterModel = new PropertyInstanceValueTypeFilterModer();
    propertyInstanceFilterModel.setId(SystemLevelIds.END_DATE_ATTRIBUTE);
    propertyInstanceFilterModel.setType(CommonConstants.DATE_ATTRIBUTE_TYPE);
    List<IFilterValueRangeModel> mandatory = propertyInstanceFilterModel.getMandatory();
    
    IFilterValueRangeModel model = new FilterValueRangeModel();
    long currentTimeMillis = System.currentTimeMillis();
    model.setType("range");
    model.setFrom(Double.valueOf(currentTimeMillis));
    try {
      model.setTo(getExpiryTime().doubleValue());
    }
    catch (CSInitializationException e) {
      throw new CSInitializationException(e.getMessage().toString());
    }
    model.setId(SystemLevelIds.END_DATE_ATTRIBUTE);
    model.setBaseType(model.getClass().getName());
    model.setAdvancedSearchFilter(true);
    mandatory.add(model);
    
    List<IPropertyInstanceFilterModel> attributes = new ArrayList<>();
    attributes.add(propertyInstanceFilterModel);
    String evaluationExpression = searchAssembler.getEvaluationExpression(attributes, new ArrayList<>(), "");
    if(!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ").append(evaluationExpression);
    }
    return searchExpression.toString();
  }
  
  protected Long getExpiryTime() throws CSInitializationException
  {
    Date value = new Date(System.currentTimeMillis());
    Calendar calender = Calendar.getInstance();
    calender.setTime(value);
    calender.set(Calendar.HOUR_OF_DAY, 0);
    calender.set(Calendar.MINUTE, 0);
    calender.set(Calendar.SECOND, 1);
    calender.set(Calendar.MILLISECOND, 0);
    
    Long currentTimeMillis = calender.getTimeInMillis();
    Long durationForAssetAboutToExpire = CSProperties.instance().getLong("asset.abouttoexpire.time");
    Long timePlusThirtyDays = currentTimeMillis + durationForAssetAboutToExpire;
    return timePlusThirtyDays;
  }
}
