package com.cs.core.config.interactor.model.mapping;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public interface IConfigRuleAttributeOutBoundMappingModel extends IConfigRulePropertyMappingModel {
  
}
