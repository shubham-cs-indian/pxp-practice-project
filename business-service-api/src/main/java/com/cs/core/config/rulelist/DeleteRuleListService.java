package com.cs.core.config.rulelist;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.rulelist.IBulkDeleteRuleListReturnModel;
import com.cs.core.config.interactor.model.rulelist.IBulkDeleteSuccessRuleListModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.rulelist.IDeleteRuleListStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DeleteRuleListService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteRuleListService {
  
  @Autowired
  protected IDeleteRuleListStrategy deleteRuleListStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy                 getRuleListEntityConfigurationStrategy;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getRuleListEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(dataModel.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();

    if (!referenceData.keySet().isEmpty()) {
      throw new EntityConfigurationDependencyException();
    }

    IBulkDeleteRuleListReturnModel strategyModel = deleteRuleListStrategy.execute(dataModel);
    IBulkDeleteReturnModel returnModel = new BulkDeleteReturnModel();
    returnModel.setFailure(strategyModel.getFailure());
    IBulkDeleteSuccessRuleListModel successModel = (IBulkDeleteSuccessRuleListModel) strategyModel
        .getSuccess();
    List<String> deletedRuleListIds = successModel.getIds();
    returnModel.setSuccess(deletedRuleListIds);
    returnModel.setAuditLogInfo(strategyModel.getAuditLogInfo());
    return returnModel;
  }
}
