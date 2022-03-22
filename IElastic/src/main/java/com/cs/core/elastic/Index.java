package com.cs.core.elastic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;

@SuppressWarnings("unchecked")
public enum Index {

  //if mapping file needs to be changed for entity, change it here.
  undefined(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), articleinstancecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearchWithFieldLimitSetting), 
  assetinstancecache(IndexSettings.ASSET_INSTANCE_MAPPING,IndexSettings.customAnalyzerForSearh), supplierinstancecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), 
  textassetinstancecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), targetinstancecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), 
  virtualcataloginstancecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), 
  //Archive indices
  articleinstancearchivecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), 
  assetinstancearchivecache(IndexSettings.ASSET_INSTANCE_MAPPING,IndexSettings.customAnalyzerForSearh), supplierinstancearchivecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), 
  textassetinstancearchivecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), targetinstancearchivecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh), 
  virtualcataloginstancearchivecache(IndexSettings.MAPPING,IndexSettings.customAnalyzerForSearh),
  //Golden record bucket index    
  goldenrecordbucketcache(IndexSettings.GOLDEN_RECORD_BUCKET_MAPPING,IndexSettings.customAnalyzerForSearh);

  String mappingFileName;
  List<Map<String,String>> analyzers;
  
  public static final String number_mapping_prefix = "i_";
  public static final String text_mapping_prefix   = "t_";

  Index(String mappingFileName, Map<String,String> ...analyzers)
  {
    this.mappingFileName = mappingFileName;
    this.analyzers = Arrays.asList(analyzers);
  }
  public String getMappingFileName()
  {
    return mappingFileName;
  }

  public List<Map<String,String>> getAnalyzers()
  {
    return this.analyzers;
  }
  public static Index getIndexByBaseType(BaseType baseType)
  {
    switch (baseType) {
      case ARTICLE:
        return articleinstancecache;
      case ASSET:
        return assetinstancecache;
      case TEXT_ASSET:
        return textassetinstancecache;
      case SUPPLIER:
        return supplierinstancecache;
      case TARGET:
        return targetinstancecache;
      default:
        return undefined;
    }
  }
  
  public static Index getArchiveIndexByBaseType(BaseType baseType)
  {
    switch (baseType) {
      case ARTICLE:
        return articleinstancearchivecache;
      case ASSET:
        return assetinstancearchivecache;
      case TEXT_ASSET:
        return textassetinstancearchivecache;
      case SUPPLIER:
        return supplierinstancearchivecache;
      case TARGET:
        return targetinstancearchivecache;
      default:
        return undefined;
    }
  }

  public static List<Index> getIndicesFromBaseTypes(List<BaseType> baseTypes)
  {
    List<Index> indicesToCreate = new ArrayList<>();
    for(BaseType baseType : baseTypes){
      indicesToCreate.add(Index.getIndexByBaseType(baseType));
      indicesToCreate.add(Index.getArchiveIndexByBaseType(baseType));
    }
    return indicesToCreate;
  }

  public static List<String> getIndices(List<BaseType> baseTypes)
  {
    List<String> indicesToCreate = new ArrayList<>();
    for(BaseType baseType : baseTypes){
      indicesToCreate.add(Index.getIndexByBaseType(baseType).name());
    }
    return indicesToCreate;
  }
  
  public static List<String> getArchiveIndices(List<BaseType> baseTypes)
  {
    List<String> indicesToCreate = new ArrayList<>();
    for(BaseType baseType : baseTypes){
      indicesToCreate.add(Index.getArchiveIndexByBaseType(baseType).name());
    }
    return indicesToCreate;
  }
  
  public static class IndexSettings {
    
    public static final String              MAPPING                      = "klass_instance.json";
    public static final String              ASSET_INSTANCE_MAPPING       = "asset_instance.json";
    public static final String              GOLDEN_RECORD_BUCKET_MAPPING = "goldenrecordbucket_instance.json";
    
    private static final String             customAnalyzerFilter         = "index.analysis.analyzer.searchable.filter.0";
    private static final String             customAnalyzerTokenizer      = "index.analysis.analyzer.searchable.tokenizer";
    private static final String             customFieldLimitSetting      = "index.mapping.total_fields.limit";
    public static final Map<String, String> customAnalyzerForSearh       = Map
        .of(customAnalyzerFilter, "lowercase", customAnalyzerTokenizer, "whitespace");
    public static final Map<String, String> customAnalyzerForSearchWithFieldLimitSetting = Map
        .of(customAnalyzerFilter, "lowercase", customAnalyzerTokenizer, "whitespace", customFieldLimitSetting, "10000");
  }
}