package com.cs.core.config.interactor.model.supplier;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.entity.supplier.ISupplier;
import com.cs.core.config.interactor.entity.supplier.Supplier;
import com.cs.core.config.interactor.model.klass.AbstractKlassSaveModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class SupplierSaveModel extends AbstractKlassSaveModel implements ISupplierKlassSaveModel {
  
  private static final long serialVersionUID = 1L;
  
  public SupplierSaveModel()
  {
    super(new Supplier());
  }
  
  public SupplierSaveModel(ISupplier klass)
  {
    super(klass);
  }
  
  @JsonDeserialize(as = Supplier.class)
  @Override
  public ITreeEntity getParent()
  {
    return this.entity.getParent();
  }
}
