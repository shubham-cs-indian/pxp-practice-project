package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;

@Component
@SuppressWarnings("unchecked")
public class EvaluateGoldenRecordRuleBucketsTask
    extends AbstractRuntimeInteractor<IIdParameterModel, IIdParameterModel>
    implements IEvaluateGoldenRecordRuleBucketsTask {
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy getArticleInstanceTypeStrategy;
  
  /*@Autowired
  protected IGetGoldenRecordRulesAssociatedWithInstanceStrategy getGoldenRecordRulesAssociatedWithInstanceStrategy;
  
  @Autowired
  protected IEvaluateGoldenRecordRuleBucketStrategy             evaluateGoldenRecordRuleBucketStrategy;*/
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    /*
    try {
      IListModel<IGoldenRecordRuleModel> goldenRecords = getGoldenRecordRulesAssociatedWithInstanceStrategy
          .execute(getArticleInstanceTypeStrategy.execute(model));
    
      IEvaluateGoldenRecordRuleBucketRequestModel requestModel = new EvaluateGoldenRecordRuleBucketRequestModel();
      requestModel.setGoldenRecordRules((List<IGoldenRecordRuleModel>) goldenRecords.getList());
      requestModel.setKlassInstanceId(model.getId());
    
      return evaluateGoldenRecordRuleBucketStrategy.execute(requestModel);
    }
    catch(Throwable ex) {
      if(isKafkaLoggingEnabled) {
        KafkaUtils.log("EvaluateGoldenRecordRuleBucketsTask ", ex,bulkPropagationLogPath ,null);
      }
    }*/
    return null;
  }
}
