package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.goldenrecord.ITypesInfoWithRuleIdModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Component
@SuppressWarnings("unchecked")
public class EvaluateGoldenRecordRuleBucketsForConfigChangeTask
    extends AbstractRuntimeInteractor<ITypesInfoWithRuleIdModel, IIdParameterModel>
    implements IEvaluateGoldenRecordRuleBucketsForConfigChangeTask {
  
  /*@Autowired
  protected IGetKlassInstancesByKlassAndTaxonomyIdsAndDeleteBucketStrategy getAllInstancesByKlassAndTaxonomyIdsAndDeleteBucketStrategy;*/

  @Override
  protected IIdParameterModel executeInternal(ITypesInfoWithRuleIdModel model) throws Exception
  {
    /*
    IListModel<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomysModel = getAllInstancesByKlassAndTaxonomyIdsAndDeleteBucketStrategy
        .execute(model);
    List<IContentTypeIdsInfoModel> contentKlassIdsAndTaxonomyIdsList = (List<IContentTypeIdsInfoModel>) contentKlassIdsAndTaxonomysModel
        .getList();
    Thread.sleep(1000);
    if (!contentKlassIdsAndTaxonomyIdsList.isEmpty()) {
      for (IContentTypeIdsInfoModel typeIdModel : contentKlassIdsAndTaxonomyIdsList) {
        IIdParameterModel idParameterModel = new IdParameterModel();
        idParameterModel.setId(typeIdModel.getContentId());
        kafkaUtils.prepareMessageData(idParameterModel,
            EvaluateGoldenRecordRuleBucketsTask.class.getName(), idParameterModel.getId());
      }
    }
    */
    return null;
  }
}
