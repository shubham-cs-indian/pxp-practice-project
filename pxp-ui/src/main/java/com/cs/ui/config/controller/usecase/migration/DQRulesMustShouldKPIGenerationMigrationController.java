package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IDQRulesMustShouldKPIGenerationMigration;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping
public class DQRulesMustShouldKPIGenerationMigrationController extends BaseController {
  
  @Autowired
  protected IDQRulesMustShouldKPIGenerationMigration dQRulesMustShouldKPIGenerationMigration;
  
  @RequestMapping("/migratecontentkpi")
  public IRESTModel execute(@RequestBody IdsListParameterModel model) throws Exception
  {
    return createResponse(dQRulesMustShouldKPIGenerationMigration.execute(model));
  }
  
}
