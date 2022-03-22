package com.cs.core.runtime.interactor.entity.textassetinstance;

import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NONE)
public class TextAssetInstance extends AbstractContentInstance implements ITextAssetInstance {
  
  private static final long serialVersionUID = 1L;
}
