package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContetxtualDataTransferOnConfigChangeInputModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IPrepareDataForContextualDataTransferOnConfigChangeTask;

@Component
@SuppressWarnings("unchecked")
public class PrepareDataForContextualDataTransferOnConfigChangeTask
    extends AbstractRuntimeInteractor<IContetxtualDataTransferOnConfigChangeInputModel, IContentsPropertyDiffModel>
    implements IPrepareDataForContextualDataTransferOnConfigChangeTask {
  
  /*@Autowired
  protected IPrepareDataForContextualDataTransferOnConfigChangeStrategy prepareDataForContextualDataTransferOnConfigChangeStrategy;*/
  
  @Override
  public IContentsPropertyDiffModel executeInternal(
      IContetxtualDataTransferOnConfigChangeInputModel dataModel) throws Exception
  {
    /*
    IListModel<IContentsPropertyDiffModel> execute = prepareDataForContextualDataTransferOnConfigChangeStrategy.execute(dataModel);
    
    List<IContentsPropertyDiffModel> contentsPropertyDiffList = (List<IContentsPropertyDiffModel>) execute
        .getList();
    
    kafkaUtils.prepareMessageData(contentsPropertyDiffList, PropagateValuesHandlerTask.class.getName(),IContentsPropertyDiffModel.CONTENT_ID);
    */
    return null;
  }
}
