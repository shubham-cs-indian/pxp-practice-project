package com.cs.core.config.interactor.model.language;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetChildLanguageCodeAgainstLanguageIdReturnModel implements IGetChildLanguageCodeAgainstLanguageIdReturnModel {
  
  protected Map<String, List<String>> childLanguageCodeAgainstLanguageId;
  
  @Override
  public Map<String, List<String>> getChildLanguageCodeAgainstLanguageId()
  {
    if (childLanguageCodeAgainstLanguageId == null) {
      return new HashMap<>();
    }
    return childLanguageCodeAgainstLanguageId;
  }
  
  @Override
  public void setChildLanguageCodeAgainstLanguageId(Map<String, List<String>> childLanguageCodeAgainstLanguageId)
  {
    this.childLanguageCodeAgainstLanguageId = childLanguageCodeAgainstLanguageId;
  }
}
