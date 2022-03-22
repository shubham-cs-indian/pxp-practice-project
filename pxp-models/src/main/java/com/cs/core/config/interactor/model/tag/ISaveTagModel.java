package com.cs.core.config.interactor.model.tag;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.template.IModifiedSequenceModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface ISaveTagModel extends ITag, IModel {
  
  public static final String MODIFIED_SEQUENCE = "modifiedSequence";
  
  public IModifiedSequenceModel getModifiedSequence();
  
  public void setModifiedSequence(IModifiedSequenceModel modifiedSequence);
}
