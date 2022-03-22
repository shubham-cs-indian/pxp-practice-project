package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;

import java.util.List;

public class DeletedTranslationsInfoModel implements IDeletedTranslationsInfoModel {
  
  private static final long      serialVersionUID = 1L;
  protected String               contentId;
  protected List<String>         languageCodes;
  protected List<IIdAndBaseType> relatedContentsInfo;
  
  public String getContentId()
  {
    return contentId;
  }
  
  public void setContentId(String contentId)
  {
    this.contentId = contentId;
  }
  
  public List<String> getLanguageCodes()
  {
    return languageCodes;
  }
  
  public void setLanguageCodes(List<String> languageCodes)
  {
    this.languageCodes = languageCodes;
  }
  
  public List<IIdAndBaseType> getRelatedContentsInfo()
  {
    return relatedContentsInfo;
  }
  
  public void setRelatedContentsInfo(List<IIdAndBaseType> relatedContentsInfo)
  {
    this.relatedContentsInfo = relatedContentsInfo;
  }
}
