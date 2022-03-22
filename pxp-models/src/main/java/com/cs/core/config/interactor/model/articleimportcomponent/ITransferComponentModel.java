package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.config.interactor.model.transfer.IGetVariantsForArticleResponseModel;

import java.util.List;

public interface ITransferComponentModel extends IComponentModel {
  
  public static final String ARTICLE_TRANSFER_COMPONENT_DATA      = "articleTransferComponentData";
  public static final String ASSET_TRANSFER_COMPONENT_DATA        = "assetTransferComponentData";
  public static final String RELATIONSHIP_TRANSFER_COMPONENT_DATA = "relationshipTransferComponentData";
  public static final String EMBEDDED_VARIANTS_COMPONENT_DATA     = "embeddedVariantsComponentData";
  public static final String ATTRIBUTE_VARIANTS_COMPONENT_DATA    = "attributeVariantsComponentData";
  
  public List<String> getArticleTransferComponentData();
  
  public void setArticleTransferComponentData(List<String> articleTransferComponentData);
  
  public List<String> getAssetTransferComponentData();
  
  public void setAssetTransferComponentData(List<String> assetTransferComponentData);
  
  public List<String> getRelationshipTransferComponentData();
  
  public void setRelationshipTransferComponentData(List<String> relationshipTransferComponentData);
  
  public List<IGetVariantsForArticleResponseModel> getEmbeddedVariantsComponentData();
  
  public void setEmbeddedVariantsComponentData(
      List<IGetVariantsForArticleResponseModel> embeddedVariantsComponentData);
  
  public List<IGetVariantsForArticleResponseModel> getAttributeVariantsComponentData();
  
  public void setAttributeVariantsComponentData(
      List<IGetVariantsForArticleResponseModel> attributeVariantsComponentData);
}
