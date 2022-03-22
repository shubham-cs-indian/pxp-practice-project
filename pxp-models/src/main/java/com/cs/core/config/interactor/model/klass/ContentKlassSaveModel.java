package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.ContentKlass;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ContentKlassSaveModel extends AbstractKlassSaveModel
    implements IContentKlassSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public ContentKlassSaveModel()
  {
    super(new ContentKlass());
  }
  
  public ContentKlassSaveModel(ContentKlass content)
  {
    super(content);
  }
  
  @JsonDeserialize(as = ContentKlass.class)
  @Override
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
  }
  
  @Override
  public String toString()
  {
    try {
      return ObjectMapperUtil.writeValueAsString(this);
    }
    catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
