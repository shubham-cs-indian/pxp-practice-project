package com.cs.core.runtime.interactor.constants.klassinstance;

public class ArticleInstanceQueryConstants extends KlassInstanceQueryConstants {
  
  public static final String ARTICLE_CREATE_QUERY_KEY            = "article.create";
  public static final String ARTICLE_SAVE_QUERY_KEY              = "article.save";
  public static final String ARTICLE_GET_QUERY_KEY               = "article.get";
  public static final String ARTICLE_SEARCH_QUERY_KEY            = "article.search";
  public static final String ARTICLE_ATTRIBUTE_CREATE_QUERY_KEY  = "article.create.attributes";
  public static final String ARTICLE_TAG_CREATE_QUERY_KEY        = "article.create.tags";
  public static final String ARTICLE_LANG_CREATE_QUERY_KEY       = "article.create.lang";
  public static final String ARTICLE_GET_TYPES_QUERY_KEY         = "article.get.types";
  
  public static final String SAVE_ATTRIBUTE_VALUE_QUERY          = "attributes.save.value";
  public static final String SAVE_TAG_TAGVALUES_QUERY            = "tags.save.tagValues";
  public static final String SAVE_ARTICLE_NAME_QUERY             = "article.save.name";
  public static final String SAVE_ARTICLE_LASTMODIFIED_QUERY     = "article.save.lastModified";
  
  public static final String GET_TAGS_BY_TAGIDS_QUERY            = "tags.get.tagsIds";
  public static final String GET_ATTRIBUTES_BY_ITEM_ID_QUERY     = "attributes.get";
  public static final String GET_TAGS_BY_ITEM_ID_QUERY           = "tags.get";
  public static final String GET_TAGS_BY_ID_QUERY                = "tags.get.tagBy.id";
  public static final String GET_TAG_TAGVALUES_QUERY             = "tags.get.tagValues";
  
  public static final String GET_ALL_ARTICLE_QUERY               = "article.get.all";
  public static final String GET_ALL_ATTRIBUTES_BY_ITEM_ID_QUERY = "attributes.get.all";
  public static final String GET_ALL_TAGS_BY_ITEM_ID_QUERY       = "tags.get.all";
  public static final String GET_TOTAL_COUNT_QUERY               = "article.get.total.count";
  public static final String GET_ITEM_BY_NAME_QUERY              = "article.get.itemBy.name";
  public static final String ARTICLE_DELETE_BY_IDS               = "article.delete.byids";
  public static final String ASSET_DELETE_BY_IDS                 = "asset.delete.byids";
}
