package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;

public interface IMulticlassificationRequestModelForSmartDocument
    extends IMulticlassificationRequestModel {
  
  public static final String SELECTED_LANGUAGE = "selectedLanguage";
  
  public String getSelectedLanguage();
  public void setSelectedLanguage(String selectedLanguage);
}
