package com.cs.core.config.interactor.model.klass;

import java.util.List;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.klass.ContentKlass;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ContentKlassModel extends AbstractKlassModel implements IContentKlassModel {
  
  private static final long serialVersionUID = 1L;
  
  protected ContentKlass    entity;
  
  public ContentKlassModel()
  {
    super(new ContentKlass());
  }
  
  public ContentKlassModel(ContentKlass klass)
  {
    super(klass);
  }
  
  @JsonDeserialize(as = ContentKlass.class)
  @Override
  public void setParent(ITreeEntity parent)
  {
    super.setParent(parent);
  }
  
  @JsonDeserialize(contentAs = ContentKlass.class)
  @Override
  public void setChildren(List<? extends ITreeEntity> children)
  {
    super.setChildren(children);
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
