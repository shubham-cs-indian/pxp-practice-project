package com.cs.core.config.interactor.model.textasset;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.textasset.ITextAsset;
import com.cs.core.config.interactor.entity.textasset.TextAsset;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class TextAssetSaveModel extends AbstractKlassSaveModel implements ITextAssetKlassSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetSaveModel()
  {
    super(new TextAsset());
  }
  
  public TextAssetSaveModel(ITextAsset klass)
  {
    super(klass);
  }
  
  @JsonDeserialize(as = TextAsset.class)
  @Override
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
  }
}
