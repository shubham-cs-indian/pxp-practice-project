package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.stereotype.Component;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.languageinstance.IBulkPropogationForDeletedTranslationsRequestModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IInitiateBulkPropogationOnDeletedTranslationsTask;

@Component
public class InitiateBulkPropogationOnDeletedTranslationsTask
    extends AbstractRuntimeInteractor<IBulkPropogationForDeletedTranslationsRequestModel, IModel>
    implements IInitiateBulkPropogationOnDeletedTranslationsTask {
  
  /*@Autowired
  protected IGetAllRelatedInstancesOfKlassInstanceStrategy getAllRelatedInstancesOfKlassInstanceStrategy;*/
  
  
  @Override
  public IModel executeInternal(IBulkPropogationForDeletedTranslationsRequestModel dataModel)
      throws Exception
  {
    /*
    String baseType = dataModel.getBaseType();
    IListModel<IIdAndBaseType> allRelatedContentsModel = getAllRelatedInstancesOfKlassInstanceStrategy.execute(dataModel);
    List<IIdAndBaseType> relatedContentsInfo = new ArrayList<>(allRelatedContentsModel.getList());
    
    List<String> variantIds = dataModel.getVariantIds();
    variantIds.forEach(variantId -> relatedContentsInfo.add(new IdAndBaseType(variantId, baseType)));
    IContentDiffModelToPrepareDataForBulkPropagation propagationModel = new ContentDiffModelToPrepareDataForBulkPropagation();
    IDeletedTranslationsInfoModel deletedTranslationsInfoModel = new DeletedTranslationsInfoModel();
    deletedTranslationsInfoModel.setContentId(dataModel.getContentId());
    deletedTranslationsInfoModel.setLanguageCodes(dataModel.getLanguageCodes());
    deletedTranslationsInfoModel.setRelatedContentsInfo(relatedContentsInfo);
    propagationModel.setDeletedTranslationsInfoModel(deletedTranslationsInfoModel);
    kafkaUtils.prepareMessageData(propagationModel, PrepareDataForBulkPropagationTask.class.getName());*/
    return null;
  }
}
