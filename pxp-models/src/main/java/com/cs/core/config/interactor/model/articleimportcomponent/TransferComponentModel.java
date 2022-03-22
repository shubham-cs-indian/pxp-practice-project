package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.config.interactor.model.transfer.GetVariantsForArticleResponseModel;
import com.cs.core.config.interactor.model.transfer.IGetVariantsForArticleResponseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class TransferComponentModel extends ComponentModel implements ITransferComponentModel {
  
  private static final long                         serialVersionUID = 1L;
  private List<String>                              articleTransferComponentData;
  private List<String>                              assetTransferComponentData;
  private List<String>                              relationshipTransferComponentData;
  private List<IGetVariantsForArticleResponseModel> embeddedVariantsComponentData;
  private List<IGetVariantsForArticleResponseModel> attributeVariantsComponentData;
  
  @Override
  public List<String> getArticleTransferComponentData()
  {
    return articleTransferComponentData;
  }
  
  @Override
  public void setArticleTransferComponentData(List<String> articleTransferComponentData)
  {
    this.articleTransferComponentData = articleTransferComponentData;
  }
  
  @Override
  public List<String> getAssetTransferComponentData()
  {
    return assetTransferComponentData;
  }
  
  @Override
  public void setAssetTransferComponentData(List<String> assetTransferComponentData)
  {
    this.assetTransferComponentData = assetTransferComponentData;
  }
  
  @Override
  public List<String> getRelationshipTransferComponentData()
  {
    return relationshipTransferComponentData;
  }
  
  @Override
  public void setRelationshipTransferComponentData(List<String> relationshipTransferComponentData)
  {
    this.relationshipTransferComponentData = relationshipTransferComponentData;
  }
  
  @Override
  public List<IGetVariantsForArticleResponseModel> getEmbeddedVariantsComponentData()
  {
    return embeddedVariantsComponentData;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetVariantsForArticleResponseModel.class)
  public void setEmbeddedVariantsComponentData(
      List<IGetVariantsForArticleResponseModel> embeddedVariantsComponentData)
  {
    this.embeddedVariantsComponentData = embeddedVariantsComponentData;
  }
  
  @Override
  public List<IGetVariantsForArticleResponseModel> getAttributeVariantsComponentData()
  {
    return attributeVariantsComponentData == null ? new ArrayList<>()
        : attributeVariantsComponentData;
  }
  
  @Override
  @JsonDeserialize(contentAs = GetVariantsForArticleResponseModel.class)
  public void setAttributeVariantsComponentData(
      List<IGetVariantsForArticleResponseModel> attributeVariantsComponentData)
  {
    this.attributeVariantsComponentData = attributeVariantsComponentData;
  }
}
