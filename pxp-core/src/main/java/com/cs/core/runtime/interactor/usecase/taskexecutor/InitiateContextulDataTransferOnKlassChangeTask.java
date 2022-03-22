package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.contextdatatransfer.IContextInfoForContextualDataTransferModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IInitiateContextulDataTransferOnKlassChangeTask;

@SuppressWarnings("unchecked")
@Component
public class InitiateContextulDataTransferOnKlassChangeTask
    extends AbstractRuntimeInteractor<IContextInfoForContextualDataTransferModel, IModel>
    implements IInitiateContextulDataTransferOnKlassChangeTask {
  
  /*@Autowired
  protected IGetChildContentsByTypeInfoStrategy getChildContentsByTypeInfoStrategy;*/

  @Override
  protected IModel executeInternal(IContextInfoForContextualDataTransferModel model)
      throws Exception
  {
    /*
    IIdParentIdTypeModel idParentIdType = new IdParentIdTypeModel();
    
    idParentIdType.setId(model.getContextKlassId());
    idParentIdType.setParentId(model.getKlassId());
    idParentIdType.setKlassType(model.getKlassType());
    
    IListModel<IContentTransferHelperModel> contentTransferHelperListModel = getChildContentsByTypeInfoStrategy.execute(idParentIdType);
    
    List<IContentTransferHelperModel> list = (List<IContentTransferHelperModel>) contentTransferHelperListModel.getList();
    
    for (IContentTransferHelperModel contentTransferHelper : list) {
      IContetxtualDataTransferOnConfigChangeInputModel inputModel = new ContetxtualDataTransferOnConfigChangeInputModel();
      inputModel.setContentTransferMapping(contentTransferHelper);
      inputModel.setContextInfoForContextualDataTransfer(model);
      kafkaUtils.prepareMessageData(inputModel, PrepareDataForContextualDataTransferOnConfigChangeTask.class.getName());
    }
    */
    return null;
  }
}
