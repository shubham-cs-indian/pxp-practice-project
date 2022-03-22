package com.cs.core.config.interactor.model.context;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IContextualValueInheritancePropagationModel extends IModel {
  
  public static final String CONTENT_INFO_FOR_CONTEXTUAL_DATA_FLOW = "contentInfoForContextualValueInheritance";
  
  public List<IContentInfoForContextualValueInheritanceModel> getContentInfoForContextualValueInheritance();
  
  public void setContentInfoForContextualValueInheritance(
      List<IContentInfoForContextualValueInheritanceModel> contentInfoForContextualValueInheritance);
}
