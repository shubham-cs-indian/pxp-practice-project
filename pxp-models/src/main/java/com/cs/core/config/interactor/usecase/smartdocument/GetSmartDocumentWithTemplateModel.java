package com.cs.core.config.interactor.usecase.smartdocument;

import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelTypeModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelTypeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class GetSmartDocumentWithTemplateModel extends SmartDocumentModel
    implements IGetSmartDocumentWithTemplateModel {
  
  private static final long         serialVersionUID       = 1L;
  protected List<IIdLabelTypeModel> smartDocumentTemplates = new ArrayList<>();
  
  @Override
  public List<IIdLabelTypeModel> getSmartDocumentTemplates()
  {
    return smartDocumentTemplates;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelTypeModel.class)
  public void setSmartDocumentTemplates(List<IIdLabelTypeModel> smartDocumentTemplates)
  {
    this.smartDocumentTemplates = smartDocumentTemplates;
  }
}
