package com.cs.core.runtime.interactor.usecase.taskexecutor;

import com.cs.core.config.interactor.model.relationship.IRestoreRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForRelationshipRestoreStrategy;
import com.cs.core.runtime.interactor.usecase.task.IRestoreRelationshipInstancesTask;
import com.cs.core.runtime.strategy.utils.BulkPropagationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestoreRelationshipInstancesTask
    extends AbstractRuntimeInteractor<IRestoreRelationshipInstancesRequestModel, IModel>
    implements IRestoreRelationshipInstancesTask {
  
  @Autowired
  protected IGetConfigDetailsForRelationshipRestoreStrategy getConfigDetailsForRelationshipRestoreStrategy;
  
  @Autowired
  protected BulkPropagationUtil                             bulkPropagationUtil;
  
  /*@Autowired
  protected IRestoreRelationshipInstancesStrategy           restoreRelationshipInstancesStrategy;
  
  @Autowired
  protected IGetInstanceVersionTypeStrategy                 getInstanceVersionTypeStrategy;*/
  
  @Override
  protected IModel executeInternal(
      IRestoreRelationshipInstancesRequestModel restoreRelationshipInstancesModel) throws Exception
  {
    /*
      IInstanceTypeGetRequestModel typeRequestModel = new InstanceTypeGetRequestModel();
      typeRequestModel.setId(restoreRelationshipInstancesModel.getKlassInstanceId());
      typeRequestModel.setVersionId(restoreRelationshipInstancesModel.getVersionId());
      typeRequestModel.setBaseType(restoreRelationshipInstancesModel.getBaseType());
    
      IKlassInstanceTypeModel typesModel = getInstanceVersionTypeStrategy.execute(typeRequestModel);
    
      IGetConfigDetailsForSaveRelationshipInstancesRequestModel configDetailsRequestModel = new GetConfigDetailsForSaveRelationshipInstancesRequestModel();
      configDetailsRequestModel.setKlassIds((List<String>) typesModel.getTypes());
      configDetailsRequestModel.setTaxonomyIds(typesModel.getSelectedTaxonomyIds());
    
      IGetConfigDetailsForSaveRelationshipInstancesResponseModel configDetails = getConfigDetailsForRelationshipRestoreStrategy.execute(configDetailsRequestModel);
      restoreRelationshipInstancesModel.setConfigDetails(configDetails);
    
      ISaveRelationshipInstanceResponseModel relationshipsSaveResponse = restoreRelationshipInstancesStrategy.execute(restoreRelationshipInstancesModel);
    
      bulkPropagationUtil.initiateVirtualReferencesUpdateOnAdditionOrRemovalOfRelationshipElements(relationshipsSaveResponse.getDataTransferInfo());
    
      bulkPropagationUtil.initiateRelationshipDataTransfer(restoreRelationshipInstancesModel.getKlassInstanceId(), relationshipsSaveResponse.getDataTransferInfo(), typesModel.getLanguageCodes());
    */
    return null;
  }
}
