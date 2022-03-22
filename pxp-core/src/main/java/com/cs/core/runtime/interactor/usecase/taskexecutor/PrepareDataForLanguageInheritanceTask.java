package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.language.IKlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceTypeModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.core.runtime.interactor.usecase.bulkpropagation.IPrepareDataForLanguageInheritanceTask;
import com.cs.core.runtime.strategy.usecase.klassinstance.IGetKlassInstanceTypeStrategy;

@Component
public class PrepareDataForLanguageInheritanceTask
    extends AbstractRuntimeInteractor<IKlassInstanceDiffForLanguageInheritanceModel, IModel>
    implements IPrepareDataForLanguageInheritanceTask {
  
  /*@Autowired
  protected IGetConfigDetailsToPrepareDataForLanguageInheritanceStrategy getConfigDetailsToPrepareDataForLanguageInheritanceStrategy;
  
  @Autowired
  protected IPrepareDataForLanguageInheritanceStrategy                   prepareDataForLanguageInheritanceStrategy;*/
  
  
  @Autowired
  protected IGetKlassInstanceTypeStrategy getInstanceTypeStrategy;
  
  @Override
  protected IModel executeInternal(IKlassInstanceDiffForLanguageInheritanceModel model)
      throws Exception
  {
    /*
    if(model.getAddedAttributes().isEmpty() && model.getModifiedAttributes().isEmpty()) {
      return null;
    }
    IKlassInstanceTypeModel klassInstanceTypeModel = getKlassInstanceType(model.getContentId());
    IIdsListParameterModel idsListParameterModel = new IdsListParameterModel();
    idsListParameterModel.setIds(klassInstanceTypeModel.getLanguageCodes());
    IPrepareDataForLanguageInheritanceModel requestModelForDataPreparation = new PrepareDataForLanguageInheritanceModel();
    requestModelForDataPreparation.setContentDiffForLanguageInheritance(model);
    Map<String, ILanguageHierarchyModel> referencedLanguages = getConfigDetailsToPrepareDataForLanguageInheritanceStrategy.execute(idsListParameterModel)
        .getReferencedLanguages();
    requestModelForDataPreparation.setReferencedLanguages(referencedLanguages);
    IContentsPropertyDiffModel dataForLanguageInheritance = prepareDataForLanguageInheritanceStrategy.execute(requestModelForDataPreparation);
    kafkaUtils.prepareMessageData(dataForLanguageInheritance, PropagateValuesHandlerTask.class.getName()
        , model.getContentId());*/
    return null;
  }
  
  private IKlassInstanceTypeModel getKlassInstanceType(String contentId) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(contentId);
    return getInstanceTypeStrategy.execute(idParameterModel);
  }
}
