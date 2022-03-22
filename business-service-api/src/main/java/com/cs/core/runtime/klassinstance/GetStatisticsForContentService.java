package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.strategy.usecase.governancerule.IGetConfigDetailsForGetStatisticsForContentStrategy;
import com.cs.core.rdbms.process.idao.IRuleCatalogDAO;
import com.cs.core.rdbms.process.idto.IKPIResultDTO;
import com.cs.core.runtime.interactor.entity.propertyinstance.IStatistics;
import com.cs.core.runtime.interactor.entity.propertyinstance.Statistics;
import com.cs.core.runtime.interactor.model.configuration.*;
import com.cs.core.runtime.interactor.model.statistics.*;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetStatisticsForContentService
    extends AbstractRuntimeService<IIdAndTypeModel, IGetStatisticsForContentResponseModel>
    implements IGetStatisticsForContentService {

  @Autowired
  protected IGetConfigDetailsForGetStatisticsForContentStrategy getConfigDetailsForGetStatisticsForContentStrategy;

  @Autowired
  protected RDBMSComponentUtils                                 rdbmsComponentUtils;

  @Autowired
  protected ISessionContext                                     context;
  
  @Override
  public IGetStatisticsForContentResponseModel executeInternal(IIdAndTypeModel dataModel) throws Exception
  {
    IGetStatisticsForContentResponseModel returnModel = new GetStatisticsForContentResponseModel();
    IRuleCatalogDAO ruleCatalogDAO = rdbmsComponentUtils.openRuleDAO();
    long baseEntityIid = Long.parseLong(dataModel.getId());
    List<IKPIResultDTO> kpiResultDTOs = ruleCatalogDAO.loadKPI(baseEntityIid, rdbmsComponentUtils.getDataLanguage());
    
    List<String> kpiIds = new ArrayList<>();
    for(IKPIResultDTO kpiResultDto: kpiResultDTOs) {
     kpiIds.add(kpiResultDto.getRuleCode()); 
    }
    
    if(!kpiIds.isEmpty()) {
      IGetAllStatisticsWithIdsRequestModel model = new GetAllStatisticsWithIdsRequestModel();
      model.setKpiBlockIds(kpiIds);
      model.setUserId(context.getUserId());
      IGetStatisticsForKpiResponseModel configData = getConfigDetailsForGetStatisticsForContentStrategy.execute(model);
      
      Map<String, IIdLabelCodeMapModel> referencedKpis = configData.getReferencedKpi();
      Map<String, String> kpiBlocks = configData.getKpiBlocks();
      List<String> kpiRuleCodesForUniqueness = new ArrayList<>();
      Map<String, IStatistics> statisticsMap = new HashMap<>();
      for(IKPIResultDTO kpiResultDTO: kpiResultDTOs) {
        
        String KpiBlockRuleCode = kpiResultDTO.getRuleCode();
        if (!kpiBlocks.containsKey(KpiBlockRuleCode)) {
          continue;
        }
        String kpiCode = kpiBlocks.get(KpiBlockRuleCode);
        IIdLabelCodeMapModel referencedKpi = referencedKpis.get(kpiCode);
        
        Map<String, String> blockId = referencedKpi.getBlockId();
        String kpiBlockType = blockId.get(KpiBlockRuleCode);
        Double kpiResult = (kpiResultDTO.getKpiResult() * 100);
        fillStatiticsResponseInfo(statisticsMap, kpiCode, kpiBlockType, kpiResult);
        
        if(kpiBlockType.equals("uniquenessBlock")) {
          kpiRuleCodesForUniqueness.add(KpiBlockRuleCode);
        }
      }
      if(!kpiRuleCodesForUniqueness.isEmpty()) {
        
        List<IKPIResultDTO> uniquenessForKpi = ruleCatalogDAO.loadKPIForUniqunessBlock(kpiRuleCodesForUniqueness, 
            baseEntityIid);
        
        Map<String , Double> uniquness = new HashMap<>();
        for(IKPIResultDTO kpiResultDTO : uniquenessForKpi) {
          
          String ruleCode = kpiResultDTO.getRuleCode();
          String kpiCode = kpiBlocks.get(ruleCode);
          
          if(uniquness.containsKey(kpiCode)) {
            
            Double updateKpi = kpiResultDTO.getKpiResult();
            if(updateKpi == 0) {
              uniquness.put(kpiCode, updateKpi);
            }
            
          }else {
            uniquness.put(kpiCode, kpiResultDTO.getKpiResult() * 100);
          }
        }
        Set<String> keySet = uniquness.keySet();
        for(String kpiCode : keySet) {
          
          IStatistics statistics = statisticsMap.containsKey(kpiCode) ? statisticsMap.get(kpiCode) : new Statistics();
          statistics.setUniqueness(uniquness.get(kpiCode));
        }
      }
      
      returnModel.setStatistics(statisticsMap);
      returnModel.setLastModified(ruleCatalogDAO.getLastEvaluatedKpiForBaseEntity(baseEntityIid));
      
      
      Map<String, IIdLabelCodeModel> referencedKpiMap = new HashMap<>();
      for(Map.Entry<String, IIdLabelCodeMapModel> referencedkpi: referencedKpis.entrySet()) {
        
        IIdLabelCodeModel referencedKpiResponseModel =  new IdLabelCodeModel();
        IIdLabelCodeMapModel kpi = referencedkpi.getValue();
        String kpiId = kpi.getCode();
        referencedKpiResponseModel.setCode(kpiId);
        referencedKpiResponseModel.setId(kpiId);
        referencedKpiResponseModel.setLabel(referencedkpi.getValue().getLabel());
        referencedKpiMap.put(kpiId, referencedKpiResponseModel);
      }
      returnModel.setReferencedKpi(referencedKpiMap);
      
    }
    return returnModel;
  }

  private void fillStatiticsResponseInfo(Map<String, IStatistics> statisticsMap, String kpiCode, String kpiBlockType, Double kpiResult)
  {
    IStatistics statistics = statisticsMap.containsKey(kpiCode) ? statisticsMap.get(kpiCode) : new Statistics();
    if (kpiBlockType.equals("accuracyBlock")) {
      statistics.setAccuracy(kpiResult);
    }
    else if (kpiBlockType.equals("completenessBlock")) {
      statistics.setCompleteness(kpiResult);
    }
    else if (kpiBlockType.equals("conformityBlock")) {
      statistics.setConformity(kpiResult);
    }
    
    statistics.setKpiId(kpiCode);
    statisticsMap.put(kpiCode, statistics);
  }
}