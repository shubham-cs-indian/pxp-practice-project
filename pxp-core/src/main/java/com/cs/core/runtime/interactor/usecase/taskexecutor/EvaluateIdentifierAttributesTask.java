package com.cs.core.runtime.interactor.usecase.taskexecutor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.propagation.EvaluateIdentifierAttributesStrategyModel;
import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributesInstanceModel;
import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributesStrategyModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
public class EvaluateIdentifierAttributesTask
    extends AbstractRuntimeInteractor<IEvaluateIdentifierAttributesInstanceModel, IVoidModel>
    implements IEvaluateIdentifierAttributeTask {
  
  /*@Autowired
  protected IEvaluateIdentifierAttributesStrategy                evaluateIdentifierAttributesStrategy;*/
  
  @Override
  protected IVoidModel executeInternal(IEvaluateIdentifierAttributesInstanceModel model)
      throws Exception
  {
    Thread.sleep(1000);
    
    List<String> changedAttributeIds = model.getChangedAttributeIds();
    Map<String, List<String>> typeIdIdentifierAttributeIds = model
        .getTypeIdIdentifierAttributeIds();
    
    Stream<String> flatMap = typeIdIdentifierAttributeIds.values()
        .stream()
        .flatMap(x -> x.stream());
    
    List<String> identifierAttributesChanged = flatMap
        .filter(changedAttributeId -> changedAttributeIds.contains(changedAttributeId))
        .collect(Collectors.toList());
    // identifierAttributesChanged.retainAll(changedAttributeIds);
    if (identifierAttributesChanged.isEmpty() && !model.getIsIdentifierAttributeChanged()
        && !model.getIsKlassInstanceTypeChanged()) {
      return null;
    }
    
    IEvaluateIdentifierAttributesStrategyModel strategyModel = new EvaluateIdentifierAttributesStrategyModel();
    strategyModel.setKlassInstanceId(model.getKlassInstanceId());
    strategyModel.setKlassIdIdentifierAttributeIds(typeIdIdentifierAttributeIds);
    strategyModel.setBaseType(model.getBaseType());
    /*IEvaluateIdentifierAttributeResponseModel response = evaluateIdentifierAttributesStrategy
        .execute(strategyModel);
    
    IUpdateSearchableInstanceModel updateSearchableInstanceModel = response
        .getUpdateSearchableInstanceModel();
    
    updateIdentifierStatusForSearchableInstance(updateSearchableInstanceModel);
    
    // Update other affected instances
    List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation = response
        .getIdentifierAttributeStatusForOtherInstances();
    kafkaUtils.prepareMessageData(dataForUniquenessStatEvaluation,
        UpdateIdentifierAttributesTask.class.getName(),
        IPropertyInstanceUniquenessEvaluationForPropagationModel.INSTANCE_ID);*/
    
    return new VoidModel();
  }
  
  private void updateIdentifierStatusForSearchableInstance(
      IUpdateSearchableInstanceModel updateSearchableInstanceModel) throws Exception
  {
    // update searchable instance
    
    if (updateSearchableInstanceModel == null) {
      return;
    }
    
    //TODO: BGP
  }
}
