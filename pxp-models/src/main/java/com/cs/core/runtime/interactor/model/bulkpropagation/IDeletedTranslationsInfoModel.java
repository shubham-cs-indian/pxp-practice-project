package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IDeletedTranslationsInfoModel extends IModel {
  
  public static final String CONTENT_ID            = "contentId";
  public static final String LANGUAGE_CODES        = "languageCodes";
  public static final String RELATED_CONTENTS_INFO = "relatedContentsInfo";
  
  public String getContentId();
  
  public void setContentId(String contentId);
  
  public List<String> getLanguageCodes();
  
  public void setLanguageCodes(List<String> languageCodes);
  
  public List<IIdAndBaseType> getRelatedContentsInfo();
  
  public void setRelatedContentsInfo(List<IIdAndBaseType> relatedContentsInfo);
}
