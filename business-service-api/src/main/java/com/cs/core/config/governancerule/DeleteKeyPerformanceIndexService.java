package com.cs.core.config.governancerule;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.UpdateInstancesOnKPIDeleteDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IUpdateInstancesOnKPIDeleteDTO;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.strategy.model.governancerule.IBulkDeleteKeyPerformanceIndexStrategyModel;
import com.cs.core.config.strategy.model.governancerule.IBulkDeleteKeyPerformanceIndexSuccessStrategyModel;
import com.cs.core.config.strategy.usecase.governancerule.IDeleteKeyPerformanceIndexStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;

@Service
public class DeleteKeyPerformanceIndexService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteKeyPerformanceIndexService {
  
  private static final String SERVICE = "UPDATE_INSTANCES_ON_KPI_DELETE";
  
  @Autowired
  protected IDeleteKeyPerformanceIndexStrategy deleteKPI;
  
  /*@Autowired
  protected IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getAllInstancesByKlassAndTaxonomyIdsStrategy;*/
  
  @Autowired
  protected TransactionThreadData              controllerThread;
 
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    IBulkDeleteKeyPerformanceIndexStrategyModel bulkDeleteKeyPerformanceInexStrategyModel = deleteKPI
        .execute(dataModel);
    IBulkDeleteReturnModel returnModel = new BulkDeleteReturnModel();
    IBulkDeleteKeyPerformanceIndexSuccessStrategyModel success = (IBulkDeleteKeyPerformanceIndexSuccessStrategyModel) bulkDeleteKeyPerformanceInexStrategyModel
        .getSuccess();
    returnModel.setSuccess(success.getDeletedKeyPerformanceIndexIds());
    returnModel.setFailure(bulkDeleteKeyPerformanceInexStrategyModel.getFailure());
    returnModel.setAuditLogInfo(bulkDeleteKeyPerformanceInexStrategyModel.getAuditLogInfo());
    
    List<String> deletedRuleCodes = success.getDeletedRuleCodes();
    IUpdateInstancesOnKPIDeleteDTO entryData = new UpdateInstancesOnKPIDeleteDTO();
    entryData.setRuleCodes(deletedRuleCodes);
    
    IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
    BGPDriverDAO.instance()
        .submitBGPProcess(context.getUserSessionDTO()
            .getUserName(), SERVICE, "", userPriority, new JSONContent(entryData.toJSON()));
    return returnModel;
  }
  
}
