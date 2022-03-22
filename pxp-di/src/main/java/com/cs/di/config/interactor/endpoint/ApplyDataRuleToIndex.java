package com.cs.di.config.interactor.endpoint;
/*package com.cs.imprt.config.interactor.usecase.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cs.base.interactor.model.IIdsListParameterModel;
import com.cs.base.interactor.model.ISessionContext;
import com.cs.base.interactor.model.IdParameterModel;
import com.cs.base.interactor.model.IdsListParameterModel;
import com.cs.config.interactor.model.IDataRuleModel;
import com.cs.config.interactor.model.IListModel;
import com.cs.imprt.config.interactor.entity.endpoint.IEndpoint;
import com.cs.imprt.config.interactor.model.component.ApplyDataRulesRequestModel;
import com.cs.imprt.config.interactor.model.component.IApplyDataRulesRequestModel;
import com.cs.imprt.config.interactor.model.endpoint.IEndpointModel;
import com.cs.imprt.config.interactor.model.endpoint.IGetEndpointModel;
import com.cs.imprt.config.store.strategy.base.datarule.IGetAllDataQualityRulesStrategy;
import com.cs.imprt.config.store.strategy.base.endpoint.IGetEndpointStrategy;
import com.cs.imprt.runtime.interactor.usecase.base.strategy.IApplyDataRulesToIdsStrategy;
import com.cs.runtime.strategy.usecase.endpoint.IGetAllInstancesFromEndPointStrategy;

@Component
@Scope("prototype")
public class ApplyDataRuleToIndex implements Runnable {
  
  @Autowired
  IGetAllInstancesFromEndPointStrategy getAllInstancesFromIndex;
  
  @Autowired
  IGetEndpointStrategy                 getEndPointStrategy;
  
  @Autowired
  IApplyDataRulesToIdsStrategy         applyDataRulesToIdsStrategy;
  
  @Autowired
  IGetAllDataQualityRulesStrategy      getAllDataQualityRulesStrategy;
  
  @Autowired
  ISessionContext                      context;
  
  protected String                     indexName;
  protected String                     endPointId;
  protected List<String> removedDataRuleIds;
  
  public ApplyDataRuleToIndex(String indexName, String endPointId, List<String> removedDataRuleIds)
  {
    this.indexName = indexName;
    this.endPointId = endPointId;
    this.removedDataRuleIds = removedDataRuleIds;
  }
  
  @Override
  public void run()
  {
    
    try {
      IIdsListParameterModel idsToApplyDataRules = getAllInstancesFromIndex.execute(null);
      List<String> ids = idsToApplyDataRules.getIds();
      IGetEndpointModel getEndPoint = getEndPointStrategy.execute(new IdParameterModel(endPointId));
      IEndpointModel endPoint = getEndPoint.getEndpoint();
      IIdsListParameterModel dataRulesgetModel = new IdsListParameterModel();
      dataRulesgetModel.setIds(endPoint.getDataRules());
      IListModel<IDataRuleModel> dataRules = getAllDataQualityRulesStrategy
          .execute(dataRulesgetModel);
      IApplyDataRulesRequestModel applyDataRulesModel = new ApplyDataRulesRequestModel();
      applyDataRulesModel.setDataRules(dataRules);
      applyDataRulesModel.setDataRuleIdsToRemove(removedDataRuleIds);
      for (String id : ids) {
        List<String> newIds = new ArrayList<>();
        newIds.add(id);
        applyDataRulesModel.setIds(newIds);
        applyDataRulesToIdsStrategy.execute(applyDataRulesModel);
      }
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
  }
  
}
*/