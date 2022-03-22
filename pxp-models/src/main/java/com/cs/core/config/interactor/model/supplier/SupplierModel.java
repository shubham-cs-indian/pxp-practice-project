package com.cs.core.config.interactor.model.supplier;

import com.cs.core.config.interactor.entity.supplier.ISupplier;
import com.cs.core.config.interactor.entity.supplier.Supplier;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.AbstractKlassModel;
import com.cs.core.config.interactor.model.variantcontext.ITechnicalImageVariantWithAutoCreateEnableModel;
import com.cs.core.config.interactor.model.variantcontext.TechnicalImageVariantWithAutoCreateEnableModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

@JsonTypeInfo(use = Id.NONE)
public class SupplierModel extends AbstractKlassModel implements ISupplierModel {
  
  private static final long                                       serialVersionUID                                 = 1L;
  protected List<? extends ITag>                                  referencedTags                                   = new ArrayList<>();
  protected List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable = new ArrayList<>();
  
  public SupplierModel()
  {
    super(new Supplier());
  }
  
  public SupplierModel(ISupplier klass)
  {
    super(klass);
  }
  
  @Override
  public List<? extends ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @JsonDeserialize(contentAs = Tag.class)
  @Override
  public void setReferencedTags(List<? extends ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public List<ITechnicalImageVariantWithAutoCreateEnableModel> getTechnicalImageVariantContextWithAutoCreateEnable()
  {
    return technicalImageVariantContextWithAutoCreateEnable;
  }
  
  @JsonDeserialize(contentAs = TechnicalImageVariantWithAutoCreateEnableModel.class)
  @Override
  public void setTechnicalImageVariantContextWithAutoCreateEnable(
      List<ITechnicalImageVariantWithAutoCreateEnableModel> technicalImageVariantContextWithAutoCreateEnable)
  {
    this.technicalImageVariantContextWithAutoCreateEnable = technicalImageVariantContextWithAutoCreateEnable;
  }
}
