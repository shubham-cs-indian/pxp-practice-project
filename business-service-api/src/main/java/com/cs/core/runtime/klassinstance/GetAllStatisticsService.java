package com.cs.core.runtime.klassinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.asset.services.CommonConstants;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.config.interactor.model.governancerule.IReferencedKPIModel;
import com.cs.core.config.strategy.usecase.governancerule.IGetConfigDetailsForGetAllStatisticsStrategy;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.process.dto.RuleResultDTO;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.process.idto.IRuleResultDTO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.model.statistics.DimensionModel;
import com.cs.core.runtime.interactor.model.statistics.GetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.ICategoryModel;
import com.cs.core.runtime.interactor.model.statistics.IDimensionModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IGetAllStatisticsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.rdbms.exception.RDBMSException; 

@Service
public class GetAllStatisticsService
    extends AbstractRuntimeService<IGetAllStatisticsRequestModel, IGetAllKPIStatisticsModel>
    implements IGetAllStatisticsService {
  
  @Autowired
  protected IGetConfigDetailsForGetAllStatisticsStrategy getConfigDetailsForGetAllStatisticsStrategy;

  @Autowired
  protected ISessionContext                              context;
  
  @Autowired
  protected TransactionThreadData                        transactionThread;
  
  @Autowired
  protected ModuleMappingUtil                            moduleMappingUtil;
  
  @Autowired
  protected RDBMSComponentUtils                          rdbmsComponentUtils;
  
  @Override
  public IGetAllKPIStatisticsModel executeInternal(IGetAllStatisticsRequestModel dataModel) throws Exception
  {
    dataModel.setUserId(context.getUserId());
    
    TransactionData transactionData = transactionThread.getTransactionData();
    dataModel.setOrganizationId(transactionData.getOrganizationId());
    dataModel.setPhysicalCatalogId(transactionData.getPhysicalCatalogId());
    dataModel.setPortalId(transactionData.getPortalId());
    dataModel.setEndpointId(transactionData.getEndpointId());
    
    IGetAllKPIStatisticsModel configDetailsForStatistics = getConfigDetailsForGetAllStatisticsStrategy.execute(dataModel);
    if (configDetailsForStatistics.getTotalCount() == 0) {
      return new GetAllKPIStatisticsModel();
    }
    
    Map<String, IReferencedKPIModel> referencedKpis = configDetailsForStatistics.getReferencedKpi();
    Set<String> allTaxonomyCodes = configDetailsForStatistics.getReferencedTaxonomies().keySet();
    Map<String,Long> taxonomyCodeVsIId = new HashMap<>();
    if(!allTaxonomyCodes.isEmpty()) {
      List<IClassifierDTO> classifierDtos = ConfigurationDAO.instance().getClassifierDtos(allTaxonomyCodes);
      for (IClassifierDTO iClassifierDTO : classifierDtos) {
        taxonomyCodeVsIId.put(iClassifierDTO.getClassifierCode(), iClassifierDTO.getClassifierIID());
      }
    }
    
    Set<String> allRuleCodes = new HashSet<>();
    Set<String> classifierCodes = new HashSet<>();
    Set<Long> heirarchyIids = new HashSet<>();
    StringBuilder cteStatement = getCteStatements(configDetailsForStatistics, taxonomyCodeVsIId, allRuleCodes,
        classifierCodes, heirarchyIids);
    String cteQuery = getCteQuery(allRuleCodes, classifierCodes, heirarchyIids, cteStatement);
    IRuleCatalogDAO ruleCatalogDAO = rdbmsComponentUtils.openRuleDAO();
    Map<String, IRuleResultDTO> result = ruleCatalogDAO.getKPIRuleResultsForGetAll(cteQuery);
    fillDataGovernanceResponseModel(configDetailsForStatistics, result, taxonomyCodeVsIId);
    
    IGetAllKPIStatisticsModel responseModel = new GetAllKPIStatisticsModel();
    IModule module = getModule(CommonConstants.DASHBOARD);
    configDetailsForStatistics.setEntities(module.getEntities());
    
    responseModel.setDataGovernance(configDetailsForStatistics.getDataGovernance());
    responseModel.setReferencedKpi(referencedKpis);
    responseModel.setReferencedKlasses(configDetailsForStatistics.getReferencedKlasses());
    responseModel.setReferencedTaxonomies(configDetailsForStatistics.getReferencedTaxonomies());
    responseModel.setTotalCount(configDetailsForStatistics.getTotalCount());
    
    return responseModel;
  }


  private StringBuilder getCteStatements(IGetAllKPIStatisticsModel configDetailsForStatistics,
      Map<String, Long> taxonomyCodeVsIId,Set<String> allRuleCodes, Set<String> classifierCodes, Set<Long> heirarchyIids)
  {
    List<IKPIStatisticsModel> dataGovernance = configDetailsForStatistics.getDataGovernance();
    StringBuilder cteStatement = new StringBuilder();
    for (IKPIStatisticsModel dataGoverenanceModel : dataGovernance) {
      List<ICategoryModel> categories = dataGoverenanceModel.getCategories();
      IReferencedKPIModel referencedKPI = configDetailsForStatistics.getReferencedKpi().get(dataGoverenanceModel.getKpiId());
      Set<String> ruleCodes = new HashSet<>();
      ruleCodes.add(referencedKPI.getAccuracyId());
      ruleCodes.add(referencedKPI.getCompletenessId());
      ruleCodes.add(referencedKPI.getUniquenessId());
      ruleCodes.add(referencedKPI.getConformityId());
      allRuleCodes.addAll(ruleCodes);
      for (ICategoryModel category : categories) {
        String categoryType = category.getType();
        String typeId = category.getTypeId();
        boolean isTaxonomy = categoryType.equals("taxonomy");
        String key = typeId;
        String groupBy = "";
        if(cteStatement.length() != 0) {
          cteStatement.append(" UNION ALL ");
        }
        if(isTaxonomy) {
          key = taxonomyCodeVsIId.get(typeId).toString();
          heirarchyIids.add(taxonomyCodeVsIId.get(typeId));
          cteStatement.append(" Select rulecode,avg(kpi) as kpi, rulecode || '--' ||  '"+ key +"' as key\r\n" + 
              "from cte_kpi");
          groupBy = " group by rulecode";
        }
        else {
          classifierCodes.add(typeId);
          cteStatement.append(" Select rulecode,kpi, rulecode || '--' ||  '"+ key +"' as key\r\n" + 
              "from cte_kpi");
        }
        String rulecodequery = " where rulecode in " + ruleCodes.stream().map(ruleCode -> "\'" + ruleCode + "\'")
            .collect(Collectors.joining(",", "(", ")"));
        String classifierQuery = isTaxonomy? "and hierarchyiids && '{"+ taxonomyCodeVsIId.get(typeId)+ "}'":"and classifiercode = '" + typeId + "'";
        cteStatement.append(rulecodequery);
        cteStatement.append(classifierQuery);
        cteStatement.append(groupBy);
      }
      
      //fillDataGovernanceResponseModel(dataGoverenanceModel, configDetailsForStatistics, ruleCatalogDAO);
    }
    return cteStatement;
  }


  private String getCteQuery(Set<String> allRuleCodes, Set<String> classifierCodes,
      Set<Long> heirarchyIids, StringBuilder cteStatement) throws RDBMSException
  {
    ILocaleCatalogDTO localCatDTO = rdbmsComponentUtils.getLocaleCatlogDAO().getLocaleCatalogDTO();
    StringBuilder cteKpi = new StringBuilder("with cte_kpi as ( ");
    String allRuleCodesQuery = "where rulecode in " + allRuleCodes.stream().map(ruleCode -> "\'" + ruleCode + "\'")
        .collect(Collectors.joining(",", "(", ")"));
    String catalogQuery = " and localeid = '%s' and catalogCode = '%s' \r\n" + 
        " and organizationCode = '%s'";
    StringBuilder classifierQuery = new StringBuilder();
    if(!classifierCodes.isEmpty() && !heirarchyIids.isEmpty()) {
      classifierQuery.append(" and (classifiercode in (" + Text.join(",", classifierCodes,"'%s'") + ") "
          + "or hierarchyiids && '{" + Text.join(",", heirarchyIids) + "}')");
    }
    else if(!classifierCodes.isEmpty()) {
      classifierQuery.append(" and classifiercode in (" + Text.join(",", classifierCodes,"'%s'") + ") ");
    }
    else if(!heirarchyIids.isEmpty()) {
      classifierQuery.append(" and  hierarchyiids && '{" + Text.join(",", heirarchyIids) + "}'");
    }
    cteKpi.append("select rulecode,hierarchyiids, classifiercode,avg(kpi) as kpi from pxp.kpigetall ");
    cteKpi.append(allRuleCodesQuery);
    cteKpi.append(String.format(catalogQuery, localCatDTO.getLocaleID(),
        localCatDTO.getCatalogCode(), localCatDTO.getOrganizationCode()));
    cteKpi.append(classifierQuery);
    cteKpi.append(" group by hierarchyiids, classifiercode, rulecode) ");
    cteKpi.append(cteStatement);
    
    return cteKpi.toString();
  }
  
  
  public void fillDataGovernanceResponseModel(IGetAllKPIStatisticsModel configDetailsForStatistics, Map<String, IRuleResultDTO> result,
      Map<String, Long> taxonomyCodeVsIId) throws RDBMSException
  {
    List<IKPIStatisticsModel> dataGovernance = configDetailsForStatistics.getDataGovernance();
    for (IKPIStatisticsModel kpiStatistics : dataGovernance) {
      List<ICategoryModel> categories = kpiStatistics.getCategories();
      IReferencedKPIModel referencedKPI = configDetailsForStatistics.getReferencedKpi().get(kpiStatistics.getKpiId());
      for (ICategoryModel category : categories) {
        IRuleResultDTO ruleResult = null;
        List<IDimensionModel> dimensionModels = new ArrayList<>(); 
        String typeId = category.getTypeId();
        Boolean isTaxonomy = category.getType().equals("taxonomy");
        Long taxonomyIId = taxonomyCodeVsIId.get(typeId);
        
        String key = isTaxonomy? referencedKPI.getAccuracyId() + "--" + taxonomyIId.toString():
          referencedKPI.getAccuracyId() + "--" + typeId;
        ruleResult = result.computeIfAbsent(key,k -> new RuleResultDTO(RuleType.kpi, referencedKPI.getAccuracyId()));
        dimensionModels.add(new DimensionModel(IStatistics.ACCURACY, ruleResult.getKPIResult() * 100));
        
        key = isTaxonomy? referencedKPI.getCompletenessId() + "--" + taxonomyIId.toString():
          referencedKPI.getCompletenessId() + "--" + typeId;
        ruleResult = result.computeIfAbsent(key,k -> new RuleResultDTO(RuleType.kpi, referencedKPI.getCompletenessId()));
        dimensionModels.add(new DimensionModel(IStatistics.COMPLETENESS, ruleResult.getKPIResult() * 100));
        
        key = isTaxonomy? referencedKPI.getUniquenessId() + "--" + taxonomyIId.toString():
          referencedKPI.getUniquenessId() + "--" + typeId;
        ruleResult = result.computeIfAbsent(key,k -> new RuleResultDTO(RuleType.kpi, referencedKPI.getUniquenessId()));
        dimensionModels.add(new DimensionModel(IStatistics.UNIQUENESS, ruleResult.getKPIResult() * 100));
        
        key = isTaxonomy? referencedKPI.getConformityId() + "--" + taxonomyIId.toString():
          referencedKPI.getConformityId() + "--" + typeId;
        ruleResult = result.computeIfAbsent(key,k -> new RuleResultDTO(RuleType.kpi, referencedKPI.getConformityId()));
        dimensionModels.add(new DimensionModel(IStatistics.CONFORMITY, ruleResult.getKPIResult() * 100));
        
        category.setDimensions(dimensionModels);
      }
    }
  }

  public IModule getModule(String moduleId)throws Exception 
  {
   
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId().equals(moduleId)) {
        // iModule.getEntities().retainAll(allowedEntities);
        return iModule;
      }
    }
    
    return null;
  }
}