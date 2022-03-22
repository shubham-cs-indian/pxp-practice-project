package com.cs.core.runtime.interactor.model.context;

import com.cs.core.config.interactor.model.context.IContentInfoForContextualValueInheritanceModel;
import com.cs.core.config.interactor.model.context.IContextualValueInheritancePropagationModel;
import com.cs.core.runtime.interactor.model.instance.ContentInfoForContextualValueInheritanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class ContextualValueInheritancePropagationModel
    implements IContextualValueInheritancePropagationModel {
  
  private static final long                                      serialVersionUID = 1L;
  
  protected List<IContentInfoForContextualValueInheritanceModel> contentInfoForContextualValueInheritance;
  
  @Override
  public List<IContentInfoForContextualValueInheritanceModel> getContentInfoForContextualValueInheritance()
  {
    if (contentInfoForContextualValueInheritance == null) {
      contentInfoForContextualValueInheritance = new ArrayList<>();
    }
    return contentInfoForContextualValueInheritance;
  }
  
  @Override
  @JsonDeserialize(contentAs = ContentInfoForContextualValueInheritanceModel.class)
  public void setContentInfoForContextualValueInheritance(
      List<IContentInfoForContextualValueInheritanceModel> contentInfoForContextualValueInheritance)
  {
    this.contentInfoForContextualValueInheritance = contentInfoForContextualValueInheritance;
  }
}
