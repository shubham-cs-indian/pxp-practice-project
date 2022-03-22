package com.cs.core.runtime.interactor.usecase.taskexecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractConfigInteractor;
import com.cs.core.config.interactor.model.language.IUpdateSchemaOnLangaugeCreateModel;
import com.cs.core.config.strategy.usecase.language.IUpdateSchemaOnCreateLanguageStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.task.IUpdateSchemaOnLanguageCreateTaxonomyTask;

@Component
public class UpdateSchemaOnLanguageCreateTaxonomyTask
    extends AbstractConfigInteractor<IUpdateSchemaOnLangaugeCreateModel, IIdsListParameterModel>
    implements IUpdateSchemaOnLanguageCreateTaxonomyTask {
  
  @Autowired
  protected IUpdateSchemaOnCreateLanguageStrategy updateSchemaOnCreateLanguageStrategy;
  
  /*@Autowired
  protected IUpdateElasticMappingOnCreateLanguage      updateElasticMappingOnCreateLanguageTag;*/
  
  @Override
  protected IIdsListParameterModel executeInternal(IUpdateSchemaOnLangaugeCreateModel model)
      throws Exception
  {
    updateSchemaOnCreateLanguageStrategy.execute(model);
    
    // updateElasticMappingOnCreateLanguageTag.execute(model);
    
    return null;
  }

  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
}
