package com.cs.core.config.goldenrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.DeleteGoldenRecordBucketDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.bgprocess.idto.IDeleteGoldenRecordBucketDTO;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleStrategyModel;
import com.cs.core.config.interactor.model.goldenrecord.IBulkDeleteGoldenRecordRuleSuccessStrategyModel;
import com.cs.core.config.strategy.usecase.goldenrecord.IDeleteGoldenRecordRulesStrategy;
import com.cs.core.json.JSONContent;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteGoldenRecordRulesService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteGoldenRecordRulesService {
  
  @Autowired
  protected IDeleteGoldenRecordRulesStrategy deleteGoldenRecordRuleStrategy;
  
  private static final String             DELETE_GOLDEN_RECORD_BUCKETS   = "DELETE_GOLDEN_RECORD_BUCKETS";
  
  /*@Autowired
  protected IGetKlassInstancesByKlassAndTaxonomyIdsStrategy getAllInstancesByKlassAndTaxonomyIdsStrategy;*/
  
  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    IBulkDeleteGoldenRecordRuleStrategyModel bulkDeleteGoldenRecordRuleStrategyModel = deleteGoldenRecordRuleStrategy.execute(dataModel);
    IBulkDeleteReturnModel returnModel = new BulkDeleteReturnModel();
    IBulkDeleteGoldenRecordRuleSuccessStrategyModel success = (IBulkDeleteGoldenRecordRuleSuccessStrategyModel) bulkDeleteGoldenRecordRuleStrategyModel
        .getSuccess();
    returnModel.setSuccess(success.getDeletedGoldenRecordRuleIds());
    returnModel.setFailure(bulkDeleteGoldenRecordRuleStrategyModel.getFailure());
    returnModel.setAuditLogInfo(bulkDeleteGoldenRecordRuleStrategyModel.getAuditLogInfo());
    evaluateGoldenRecordRuleBucket(success.getDeletedGoldenRecordRuleIds());
    
    return returnModel;
  }
  
  @SuppressWarnings("unchecked")
  private void evaluateGoldenRecordRuleBucket(List<String> ruleIds) throws Exception
  {
    for(String ruleId : ruleIds) {
      IBGProcessDTO.BGPPriority userPriority = IBGProcessDTO.BGPPriority.MEDIUM;
      IDeleteGoldenRecordBucketDTO deleteGoldenRecordbucketDTO = new DeleteGoldenRecordBucketDTO();
      deleteGoldenRecordbucketDTO.setRuleId(ruleId);
      BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), DELETE_GOLDEN_RECORD_BUCKETS, "", userPriority,
          new JSONContent(deleteGoldenRecordbucketDTO.toJSON()));
    }
  }
}
