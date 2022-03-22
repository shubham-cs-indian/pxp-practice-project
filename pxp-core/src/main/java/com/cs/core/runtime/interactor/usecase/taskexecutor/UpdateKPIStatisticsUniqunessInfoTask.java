package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.runtime.interactor.model.configuration.IIdAndTypeModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UpdateKPIStatisticsUniqunessInfoTask extends
    AbstractRuntimeInteractor<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel, IIdAndTypeModel>
    implements IUpdateKPIStatisticsUniqunessInfoTask {
  
  /*@Autowired
  private IUpdateKPIStatisticsUniqunessInfoStrategy updateKPIStatisticsUniqunessInfoStrategy;*/

  @Autowired
  protected Boolean isKafkaLoggingEnabled;
  
  @Override
  public IIdAndTypeModel executeInternal(
      IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel dataModel) throws Exception
  {
    /*
    try {
      return updateKPIStatisticsUniqunessInfoStrategy.execute(dataModel);
    }
    catch (Throwable ex) {
      if (isKafkaLoggingEnabled) {
        KafkaUtils.log("UpdateIdentifierAttributesTask ", ex, bulkPropagationLogPath, null);
      }
    }*/
    return null;
  }
}
