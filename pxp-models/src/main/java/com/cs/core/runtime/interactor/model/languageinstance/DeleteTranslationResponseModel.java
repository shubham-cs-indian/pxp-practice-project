package com.cs.core.runtime.interactor.model.languageinstance;

import com.cs.core.config.interactor.model.klass.ContentsPropertyDiffModel;
import com.cs.core.config.interactor.model.klass.IContentsPropertyDiffModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class DeleteTranslationResponseModel implements IDeleteTranslationResponseModel {
  
  private static final long                serialVersionUID = 1L;
  protected List<String>                   success;
  protected IExceptionModel                failure;
  protected IContentsPropertyDiffModel     contentPropertyDiff;
  protected IUpdateSearchableInstanceModel updateSearchableDocumentData;
  
  @Override
  public List<String> getSuccess()
  {
    if (success == null) {
      success = new ArrayList<>();
    }
    return success;
  }
  
  @Override
  public void setSuccess(List<String> success)
  {
    this.success = success;
  }
  
  @Override
  public IExceptionModel getFailure()
  {
    if (failure == null) {
      failure = new ExceptionModel();
    }
    return failure;
  }
  
  @JsonDeserialize(as = ExceptionModel.class)
  @Override
  public void setFailure(IExceptionModel failure)
  {
    this.failure = failure;
  }
  
  @Override
  public IContentsPropertyDiffModel getContentDiffForLanguageInheritance()
  {
    return contentPropertyDiff;
  }
  
  @JsonDeserialize(as = ContentsPropertyDiffModel.class)
  @Override
  public void setContentDiffForLanguageInheritance(
      IContentsPropertyDiffModel contentDiffForLanguageInheritance)
  {
    this.contentPropertyDiff = contentDiffForLanguageInheritance;
  }
  
  @Override
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData()
  {
    return updateSearchableDocumentData;
  }
  
  @Override
  @JsonDeserialize(as = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData)
  {
    this.updateSearchableDocumentData = updateSearchableDocumentData;
  }
}
