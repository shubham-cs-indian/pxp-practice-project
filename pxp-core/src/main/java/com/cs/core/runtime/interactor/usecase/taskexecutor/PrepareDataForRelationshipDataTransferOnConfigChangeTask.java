package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IRelationshipDataTransferOnConfigChangeInputModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IPrepareDataForRelationshipDataTransferOnConfigChangeTask;

@Component
@SuppressWarnings("unchecked")
public class PrepareDataForRelationshipDataTransferOnConfigChangeTask
    extends AbstractRuntimeInteractor<IRelationshipDataTransferOnConfigChangeInputModel, IContentsPropertyDiffModel>
    implements IPrepareDataForRelationshipDataTransferOnConfigChangeTask {
  
  /*@Autowired
  protected IPrepareDataForRelationshipDataTransferOnConfigChangeStrategy prepareDataForRelationshipDataTransferOnConfigChange;*/

  
  @Override
  public IContentsPropertyDiffModel executeInternal(
      IRelationshipDataTransferOnConfigChangeInputModel dataModel) throws Exception
  {
    /*IListModel<IContentsPropertyDiffModel> execute = prepareDataForRelationshipDataTransferOnConfigChange.execute(dataModel);
    
       List<IContentsPropertyDiffModel> contentsPropertyDiffList = (List<IContentsPropertyDiffModel>) execute.getList();
    
    kafkaUtils.prepareMessageData(contentsPropertyDiffList, PropagateValuesHandlerTask.class.getName(), IContentsPropertyDiffModel.CONTENT_ID);
       */
    return null;
  }
}
