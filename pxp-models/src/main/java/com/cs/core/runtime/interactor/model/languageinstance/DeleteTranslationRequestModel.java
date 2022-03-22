package com.cs.core.runtime.interactor.model.languageinstance;

import java.util.ArrayList;
import java.util.List;

public class DeleteTranslationRequestModel implements IDeleteTranslationRequestModel {
  
  private static final long serialVersionUID = 1L;
  
  protected long            contentId;
  protected List<String>    languageCodes;
  
  @Override
  public long getContentId()
  {
    return contentId;
  }
  
  @Override
  public void setContentId(long contentId)
  {
    this.contentId = contentId;
  }
  
  @Override
  public List<String> getLanguageCodes()
  {
    if (languageCodes == null) {
      languageCodes = new ArrayList<>();
    }
    return languageCodes;
  }
  
  @Override
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
}
