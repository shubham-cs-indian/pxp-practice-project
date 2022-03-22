package com.cs.core.runtime.interactor.model.smartdocument;

import com.cs.core.runtime.interactor.model.typeswitch.MulticlassificationRequestModel;

public class MulticlassificationRequestModelForSmartDocument
    extends MulticlassificationRequestModel implements IMulticlassificationRequestModelForSmartDocument{

  private static final long serialVersionUID = 1L;
  protected String          selectedLanguage;

  @Override
  public String getSelectedLanguage()
  {
    return selectedLanguage;
  }

  @Override
  public void setSelectedLanguage(String selectedLanguage)
  {
    this.selectedLanguage = selectedLanguage;
  }
}
