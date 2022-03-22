package com.cs.core.runtime.interactor.usecase.klassinstance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.governancerule.IGetKeyPerformanceIndexModel;
import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.strategy.usecase.governancerule.IGetGovernanceRulesByKlassAndTaxonomyIdsStrategy;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.datarule.EvaluateGovernanceRulesRequestModel;
import com.cs.core.runtime.interactor.model.datarule.IEvaluateGovernanceRulesRequestModel;
import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.taskexecutor.IEvaluateGovernanceRulesTask;

@Component
public class EvaluateGovernanceRulesTask extends AbstractRuntimeInteractor<ITypeInfoWithContentIdentifiersModel, IIdParameterModel>
    implements IEvaluateGovernanceRulesTask {
  
  @Autowired
  protected IGetGovernanceRulesByKlassAndTaxonomyIdsStrategy getGovernanceRulesByKlassAndTaxonomyIdsStrategy;
  
  /*@Autowired
  protected ICreateOrSaveStatisticsInstanceStrategy          createOrSaveStatisticsInstanceStrategy;*/

  @SuppressWarnings("unchecked")
  public IIdParameterModel executeInternal(ITypeInfoWithContentIdentifiersModel contentTypeIdsInfoModel) throws Exception
  {
    try {
      IMulticlassificationRequestModel multiclassificationRequestModel = new MulticlassificationRequestModel();
      multiclassificationRequestModel.setKlassIds(contentTypeIdsInfoModel.getKlassIds());
      multiclassificationRequestModel.setSelectedTaxonomyIds(contentTypeIdsInfoModel.getTaxonomyIds());
      multiclassificationRequestModel.setOrganizationId(contentTypeIdsInfoModel.getOrganizationId());
      multiclassificationRequestModel.setEndpointId(contentTypeIdsInfoModel.getEndpointId());
      multiclassificationRequestModel.setPhysicalCatalogId(contentTypeIdsInfoModel.getPhysicalCatalogId());
      IListModel<IGetKeyPerformanceIndexModel> kpiList = getGovernanceRulesByKlassAndTaxonomyIdsStrategy
          .execute(multiclassificationRequestModel);
      
      IEvaluateGovernanceRulesRequestModel evaluateGovernanceRulesRequestModel = new EvaluateGovernanceRulesRequestModel();
      evaluateGovernanceRulesRequestModel.setKpiList((List<IGetKeyPerformanceIndexModel>) kpiList.getList());
      evaluateGovernanceRulesRequestModel.setContentId(contentTypeIdsInfoModel.getContentId());
      evaluateGovernanceRulesRequestModel.setBaseType(contentTypeIdsInfoModel.getBaseType());
      /*IListModel<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> response = createOrSaveStatisticsInstanceStrategy.execute(evaluateGovernanceRulesRequestModel);
      List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel> contentInfoToEvaluate = (List<IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel>) response.getList();
      if(!contentInfoToEvaluate.isEmpty()) {
        kafkaUtils.prepareMessageData(contentInfoToEvaluate, UpdateKPIStatisticsUniqunessInfoTask.class.getName(), IKPIStatisticsUniqunessnessRuleEvaluationPropagationModel.CONTENT_ID);
      }*/
      return new IdParameterModel(contentTypeIdsInfoModel.getContentId());
    }
    catch (Throwable ex) {
      //TODO: BGP
    }
    
    return null;
  }
}
