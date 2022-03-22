package com.cs.core.config.goldenrecord;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cs.config.businessapi.base.Validations;
import com.cs.core.config.interactor.model.goldenrecord.IGoldenRecordRuleModel;
import com.cs.core.config.interactor.model.goldenrecord.ISaveGoldenRecordRuleModel;
import com.cs.core.runtime.interactor.exception.configuration.NatureKlassNotFoundException;

@Component
public class GoldenRecordRuleValidations extends Validations {
  
  public void validate(IGoldenRecordRuleModel createModel) throws Exception
  {
    validate(createModel.getCode(), createModel.getLabel());
    validateNatureKlass(createModel.getKlassIds());
  }
  
  public void validate(ISaveGoldenRecordRuleModel saveModel) throws Exception
  {
    validate(saveModel.getCode(), saveModel.getLabel());
  }
  
  private void validateNatureKlass(List<String> klassIds) throws NatureKlassNotFoundException
  {
    if (klassIds == null || klassIds.isEmpty()) {
      throw new NatureKlassNotFoundException();
    }
  }
}
