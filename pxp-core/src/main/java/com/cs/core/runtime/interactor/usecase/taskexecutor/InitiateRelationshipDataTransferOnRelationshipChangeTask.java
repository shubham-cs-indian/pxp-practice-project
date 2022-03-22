package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.relationship.ISideInfoForRelationshipDataTransferModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IInitiateRelationshipDataTransferOnRelationshipChangeTask;

@SuppressWarnings("unchecked")
@Component
public class InitiateRelationshipDataTransferOnRelationshipChangeTask
    extends AbstractRuntimeInteractor<ISideInfoForRelationshipDataTransferModel, IModel>
    implements IInitiateRelationshipDataTransferOnRelationshipChangeTask {
  
  /*@Autowired
  protected IGetAllSideLinkedContentByRelationshipIdStrategy getAllSideLinkedContentByRelationshipId;*/
  
  @Override
  protected IModel executeInternal(ISideInfoForRelationshipDataTransferModel model) throws Exception
  {
    /*
    IRelationshipIdSideIdModel relationshipIdSideIdModel = new RelationshipIdSideIdModel();
    relationshipIdSideIdModel.setRelationshipId(model.getRelationshipId());
    relationshipIdSideIdModel.setSideId(model.getSideId());
    IListModel<IContentTransferHelperModel> contentTransferHelperListModel = getAllSideLinkedContentByRelationshipId.execute(relationshipIdSideIdModel);
    List<IContentTransferHelperModel> list = (List<IContentTransferHelperModel>) contentTransferHelperListModel.getList();
    
    for (IContentTransferHelperModel contentTransferHelper : list) {
      IRelationshipDataTransferOnConfigChangeInputModel inputModel = new RelationshipDataTransferOnConfigChangeInputModel();
      inputModel.setContentTransferMapping(contentTransferHelper);
      inputModel.setSideInfoForRelationshipDataTransfer(model);
      kafkaUtils.prepareMessageData(inputModel, PrepareDataForRelationshipDataTransferOnConfigChangeTask.class.getName());
    }
    */
    return null;
  }
}
