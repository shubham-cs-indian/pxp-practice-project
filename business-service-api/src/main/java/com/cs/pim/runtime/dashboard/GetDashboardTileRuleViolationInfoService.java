package com.cs.pim.runtime.dashboard;

import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.exception.klass.ArticleKlassNotFoundException;
import com.cs.core.config.interactor.model.klass.GetFilterInfoModel;
import com.cs.core.config.interactor.model.taxonomy.ApplicableFilterModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.runtime.dashboard.AbstractGetDashboardTileRuleViolationInfoService;
import com.cs.core.runtime.interactor.exception.articleinstance.UserNotHaveReadPermissionForArticle;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveReadPermission;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.dashboard.DashboardInformationResponseModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.searchable.IGetFilterInfoModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.dashboard.IGetConfigDetailsForDashboardTileInformationStrategy;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag.QualityFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetDashboardTileRuleViolationInfoService extends AbstractGetDashboardTileRuleViolationInfoService<IDashboardInformationRequestModel, IDashboardInformationResponseModel>
    implements IGetDashboardTileRuleViolationInfoService {
  
  @Autowired
  protected IGetConfigDetailsForDashboardTileInformationStrategy getConfigDetailsForDashboardTileInformationStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  protected SearchAssembler                                      searchAssembler;
  
  @Autowired
  protected TransactionThreadData                                transactionThreadData;
  
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
  protected IDashboardInformationResponseModel getDashboardTileRuleViolationInfo(
      IDashboardInformationRequestModel dashboardInformationRequestModel) throws Exception
  {	  IDashboardInformationResponseModel response = new DashboardInformationResponseModel();
	  IGetFilterInfoModel filterModel = new GetFilterInfoModel();
			  
			  List<IApplicableFilterModel> filterData = filterModel.getFilterData();
			    IApplicableFilterModel dataRulesMap = new ApplicableFilterModel();
			    filterData.add(dataRulesMap);

			    String searchExpression = generateSearchExpression(dashboardInformationRequestModel);
			    IRuleResultDTO ruleResult = rdbmsComponentUtils.getLocaleCatlogDAO().getDataQualityCount(false, searchExpression);
			    List<IApplicableFilterModel> dataRules = new ArrayList<>();
			    
			    for(QualityFlag value : QualityFlag.values()){
			      IApplicableFilterModel color = new ApplicableFilterModel();
			      long nbOfFlaggedEntities = ruleResult.getNbOfFlaggedEntities(value);
			      String label = value.name().substring(1);
			      String idAndType = label.substring(0,1).toUpperCase() + label.substring(1); // green -> Green
			      String name = label.toUpperCase();
			      String idAndTypeFormatted = String.format("is%s_aggs", idAndType);
			      
			      color.setCount(nbOfFlaggedEntities);
				  color.setId(label);
			      color.setType(label);
			      color.setLabel(name);
			      dataRules.add(color);
			    }
			    
			    dataRulesMap.setId(CommonConstants.COLOR_VOILATION_FILTER);
			    dataRulesMap.setType(CommonConstants.COLOR_VOILATION_FILTER);
			    dataRulesMap.setLabel("Rule Violations");
			    dataRulesMap.setChildren(dataRules);
			  
			 response.setFilterInfo(filterModel);
			 response.setTotalContents(ruleResult.getTotalNbOfEntities());
			 return response;
  }
  
  private String generateSearchExpression(IDashboardInformationRequestModel dataModel)
  {
    String moduleId = dataModel.getModuleId();
    BaseType baseType = searchAssembler.getBaseTypeByModule(moduleId);
      List<String> klassIdsHavingRP = List.copyOf(dataModel.getKlassIdsHavingRP());
      List<String> taxonomyIdsHavingRP = List.copyOf(dataModel.getTaxonomyIdsHavingRP());
      StringBuilder entityFilter = searchAssembler.generateEntityFilterExpression(klassIdsHavingRP, taxonomyIdsHavingRP, true, dataModel.getMajorTaxonomyIds());

      String baseTypeExp = searchAssembler.getBaseQuery(baseType).toString();
      if(!entityFilter.toString().isEmpty()){
          baseTypeExp += " " + entityFilter.toString();
      }
      return baseTypeExp;
  }
  
  @Override
  protected IConfigDetailsForInstanceTreeGetModel getConfigDetails(IIdParameterModel model)
      throws Exception
  {
    return getConfigDetailsForDashboardTileInformationStrategy.execute(model);
  }
}
