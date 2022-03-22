package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

import java.util.List;

public interface IDeleteTranslationResponseModel extends IBulkResponseModel {
  
  String CONTENT_DIFF_FOR_LANGUAGE_INHERITANCE = "contentDiffForLanguageInheritance";
  String UPDATE_SEARCHABLE_DOCUMENT_DATA       = "updateSearchableDocumentData";
  
  public void setSuccess(List<String> success);
  
  public IContentsPropertyDiffModel getContentDiffForLanguageInheritance();
  
  public void setContentDiffForLanguageInheritance(
      IContentsPropertyDiffModel contentDiffForLanguageInheritance);
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData);
}
