package com.cs.core.runtime.klassinstance;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.strategy.usecase.governancerule.IGetConfigDetailsForGetStatisticsStrategy;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.model.statistics.*;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetStatisticsService
    extends AbstractRuntimeService<IGetStatisticsRequestModel, IGetStatisticsResponseModel>
    implements IGetStatisticsService {
  
  @Autowired
  protected IGetConfigDetailsForGetStatisticsStrategy    getConfigDetailsForGetStatisticsStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                          rdbmsComponentUtils;
  
  @Autowired
  protected ModuleMappingUtil                            moduleMappingUtil;
  
  @Override
  public IGetStatisticsResponseModel executeInternal(IGetStatisticsRequestModel dataModel) throws Exception
  {
	String kpiId = dataModel.getKpiId();
	IGetStatisticsResponseModel configDetailsForStatistics = getConfigDetailsForGetStatisticsStrategy.execute(dataModel);
    IRuleCatalogDAO ruleCatalogDAO = rdbmsComponentUtils.openRuleDAO();
    IKPIStatisticsModel kpiStatisticsModel = configDetailsForStatistics.getKpiStatistics();		
	
	if (kpiStatisticsModel.getKpiId().equals(kpiId)) {
		fillDataGovernanceResponseModel(kpiStatisticsModel, configDetailsForStatistics, ruleCatalogDAO);
	}
	
	IModule module = getModule(CommonConstants.DASHBOARD);
	IGetStatisticsResponseModel getStatisticsResponseModel = new GetStatisticsResponseModel();		
	getStatisticsResponseModel.setEntities(module.getEntities());
	getStatisticsResponseModel.setKpiStatistics(kpiStatisticsModel);
	getStatisticsResponseModel.setReferencedTaxonomies(configDetailsForStatistics.getReferencedTaxonomies());	
	getStatisticsResponseModel.setReferencedTags(configDetailsForStatistics.getReferencedTags());
	
	return getStatisticsResponseModel;
	}
  
  private void fillDataGovernanceResponseModel(IKPIStatisticsModel kpiStatistics, IGetStatisticsResponseModel configDetailsForStatistics, IRuleCatalogDAO ruleCatalogDAO) throws RDBMSException {
		
		List<ICategoryModel> categories = kpiStatistics.getCategories();
		
		List<String> classifierInfo = new ArrayList<>();
		List<String> tagInfo = new ArrayList<>();
		
		for(IPathModel pathModel : kpiStatistics.getPath()) {
		  if(pathModel.getType().equals("tag")) {
		    tagInfo.add(pathModel.getTypeId());
		  }else {
		    classifierInfo.add(pathModel.getTypeId());
		  }
		}
		
		for (ICategoryModel category : categories) {
			IRuleResultDTO ruleResult = null;
			Map<String, String> ruleBlockVsCode = configDetailsForStatistics.getRuleBlockVsCode();			
			
			List<IDimensionModel> dimensionModels = new ArrayList<>();

			ruleResult = ruleCatalogDAO.getKPIRuleResult(ruleBlockVsCode.get(CommonConstants.ACCURACY_BLOCK), 
			    category.getTypeId(),rdbmsComponentUtils.getDataLanguage(), category.getType(), classifierInfo, tagInfo);
			
			dimensionModels.add(new DimensionModel(IStatistics.ACCURACY, ruleResult.getKPIResult() * 100));

			ruleResult = ruleCatalogDAO.getKPIRuleResult(ruleBlockVsCode.get(CommonConstants.COMPLETENESS_BLOCK), 
			    category.getTypeId(), rdbmsComponentUtils.getDataLanguage(), category.getType(), classifierInfo, tagInfo);
			dimensionModels.add(new DimensionModel(IStatistics.COMPLETENESS, ruleResult.getKPIResult() * 100));

      ruleResult = ruleCatalogDAO.loadKpiForUniqueness(ruleBlockVsCode.get(CommonConstants.UNIQUENESS_BLOCK), 
          category.getTypeId(), category.getType(), classifierInfo, tagInfo);
      dimensionModels.add(new DimensionModel(IStatistics.UNIQUENESS, ruleResult.getKPIResult() * 100));

			ruleResult = ruleCatalogDAO.getKPIRuleResult(ruleBlockVsCode.get(CommonConstants.CONFORMITY_BLOCK), 
			    category.getTypeId(),rdbmsComponentUtils.getDataLanguage(), category.getType(), classifierInfo, tagInfo);
			dimensionModels.add(new DimensionModel(IStatistics.CONFORMITY, ruleResult.getKPIResult() * 100));
	  
			category.setDimensions(dimensionModels); 			
			
		} 
	}
  
  public IModule getModule(String moduleId) throws Exception
  {
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    for (IModule iModule : modules) {
      if (iModule.getId()
          .equals(moduleId)) {
        return iModule;
      }
    }
    return null;
  }
}
