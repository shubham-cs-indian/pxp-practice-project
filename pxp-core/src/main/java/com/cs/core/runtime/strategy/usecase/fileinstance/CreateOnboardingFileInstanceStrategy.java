package com.cs.core.runtime.strategy.usecase.fileinstance;

import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.fileinstance.IOnboardingFileInstanceModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceWithDataRuleModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("createOnboardingFileInstanceStrategy")
public class CreateOnboardingFileInstanceStrategy /*extends BaseOnboardingElasticStrategy*/
    implements ICreateOnboardingFileInstanceStrategy {
  
  @Autowired
  ISessionContext context;
  
  @Override
  public IOnboardingFileInstanceModel execute(IKlassInstanceWithDataRuleModel model)
      throws Exception
  {
    /*return execute(FILE_INSTANCE_CREATE + "/"
    + model.getKlassInstance().getId() + "/"
    + model.getKlassInstance().getVersionId()
    + "?indexName=" + getElasticSearchIndex()
    + "&docType=" + FILE_INSTANCE_CACHE
    + "&versionDocType=" + FILE_INSTANCE_CACHE,
    model, OnboardingFileInstanceModel.class);*/
    return null;
  }
}
