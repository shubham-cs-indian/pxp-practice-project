package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.translations.utils.TranslationsUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.translations.IGetPropertyTranslationsModel;
import com.cs.core.config.interactor.model.translations.IGetTranslationsRequestModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportTranslationList extends AbstractOrientPlugin {
  
  private static final String LANGAUGE_TRANSLATION = "languageTranslation";
  
  public ExportTranslationList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String entityType = (String) requestMap.get(IGetTranslationsRequestModel.ENTITY_TYPE);
    String vertexLabel = EntityUtil.getVertexLabelByEntityType(entityType);
    List<String> languages = (List<String>) requestMap.get(IGetTranslationsRequestModel.LANGUAGES);
    if(languages.isEmpty()) {
      Iterable<Vertex> languageVertices = UtilClass.getGraph().command(new OCommandSQL("select from language")).execute();
      for (Vertex languageVertex : languageVertices) {
        languages.add(languageVertex.getProperty(ITag.CODE));
      }
    }
    languages.add(CommonConstants.LANGUAGE_CODE_FOR_DEFAULT_LABEL_PROPERTY);
    
    String sortLanguage = (String) requestMap.get(IGetTranslationsRequestModel.SORT_LANGUAGE);
    String sortBy = (String) requestMap.get(IGetTranslationsRequestModel.SORT_BY);
    StringBuilder query = new StringBuilder("select from ").append(vertexLabel);
    if (entityType.equals(CommonConstants.ATTRIBUTION_TAXONOMY)) {
      query = query.append(" where ").append(IMasterTaxonomy.IS_TAXONOMY).append(" = true ");
    }
    query = query.append(" order by ").append(TranslationsUtils.prepareSearchOrSortField(sortBy, sortLanguage)).append(" asc");
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query.toString(), languages, entityType);
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put("list", list);
    return responseMap;
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query, List<String> languages, String entityType) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query)).execute();
    List<Map<String, Object>> returnList = new ArrayList<>();
    for (Vertex node : vertices) {
      if(!CommonConstants.TAG_VALUES.equals(entityType)) {
        Map<String, Object> map = prepareTranslationsMap(languages, node, entityType);
        returnList.add(map);
      } else {
      prepareTagValuesTranslations(languages, node, returnList);
      }
        
      }
    return returnList;
  }
  
  public static Map<String, Object> prepareTranslationsMap(Collection<String> languages, Vertex node, String entityType)
  {
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, Object> translations = new HashMap<String, Object>();
    for (String lang : languages) {
      Map<String, Object> translationMap = new HashMap<>();
      if (CommonConstants.RELATIONSHIP.equals(entityType)) {
        translationMap = TranslationsUtils.getRelationshipTranslationMapFromVertex(lang, node);
      }
      else {
        translationMap = TranslationsUtils.getTranslationMapFromVertex(lang, node);
      }
      translations.put(lang, translationMap);
      if (lang.equals(UtilClass.getLanguage().getUiLanguage())) {
        map.putAll(translationMap);
      }
    }
    map.put(LANGAUGE_TRANSLATION, translations);
    map.put(IGetPropertyTranslationsModel.CODE, node.getProperty(ITag.CODE));
    map.put(IGetPropertyTranslationsModel.TYPE, entityType);
    
    return map;
  }
  
  public static void prepareTagValuesTranslations(Collection<String> languages, Vertex node, List<Map<String, Object>> list)
  {
    String query = "select expand(in ('Child_Of')) from " + node.getId().toString();
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query)).execute();
    for (Vertex valueNode : vertices) {
      Map<String, Object> map = prepareTranslationsMap(languages, valueNode, CommonConstants.TAG);
      list.add(map);
    }
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportTranslationList/*" };
  }
  
}
