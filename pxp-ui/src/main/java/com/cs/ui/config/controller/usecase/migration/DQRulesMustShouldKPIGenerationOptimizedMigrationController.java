package com.cs.ui.config.controller.usecase.migration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs.core.config.interactor.usecase.migration.IDQRulesMustShouldKPIGenerationOptimizedMigration;
import com.cs.core.config.interactor.usecase.migration.IDQRulesMustShouldKPIGenerationOptimizedMigration2;
import com.cs.core.runtime.interactor.model.configuration.KPIScriptRequestModel;
import com.cs.ui.runtime.controller.model.response.IRESTModel;
import com.cs.ui.runtime.controller.usecase.interceptor.BaseController;

@RestController
@RequestMapping
public class DQRulesMustShouldKPIGenerationOptimizedMigrationController extends BaseController {
  
  @Autowired
  protected IDQRulesMustShouldKPIGenerationOptimizedMigration dQRulesMustShouldKPIGenerationOptimizedMigration;
  
  @Autowired
  protected IDQRulesMustShouldKPIGenerationOptimizedMigration2 dQRulesMustShouldKPIGenerationOptimizedMigration2;
  
  @RequestMapping("/migratecontentkpioptimized/{id}")
  public IRESTModel execute(@PathVariable Integer id,@RequestBody KPIScriptRequestModel model) throws Exception
  {
    if(id == 1) {
      return createResponse(dQRulesMustShouldKPIGenerationOptimizedMigration.execute(model));
    }
    else if(id == 2){
      return createResponse(dQRulesMustShouldKPIGenerationOptimizedMigration2.execute(model));
    }
    else {
      throw new Exception("Invalid input for id in url");
    }
  }
  
}
