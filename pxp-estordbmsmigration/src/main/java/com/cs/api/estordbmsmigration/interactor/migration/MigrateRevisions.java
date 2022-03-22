package com.cs.api.estordbmsmigration.interactor.migration;

import com.cs.api.estordbmsmigration.services.RequestHandler;
import com.cs.api.estordbmsmigration.services.RevisionMigration;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.elastic.services.ScrollUtils;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.IdsQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Migration For Download Information to dump in download information pxp tables
 * 
 * @author mrunali.dhenge
 *
 */
@SuppressWarnings("unchecked")
@Component
public class MigrateRevisions extends AbstractRuntimeService<IVoidModel, IVoidModel> implements IMigrateRevisions {
  
  @Autowired
  RevisionMigration revisionMigration;

  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    List<String> indices = Arrays.asList("cs","csarchive");

    List<String> docTypes = Arrays.asList("klassinstancecache","assetinstancecache", "targetinstancemarketcache","supplierinstancecache", "virtualcataloginstancecache");
    ScrollUtils.scrollThrough(this::executeInScroll, RequestHandler.getRestClient() ,indices, QueryBuilders.matchAllQuery(), docTypes);
    return new VoidModel();
  }

  private String executeInScroll(SearchHit hit)
  {
    Map<String, Object> document = hit.getSourceAsMap();
    String id = (String) document.get(VersionDocFields.id.name());
    try {
      SearchHit[] searchHits = getRevisions(hit, id);

      IBaseEntityDTO oldEntity = null;

      //iterate over versions
      for (SearchHit searchHit : searchHits) {
        Map<String, Object> versionDocument = searchHit.getSourceAsMap();
        Map<String, Map<String, Object>> languageVersionDoc = getLanguageDocs(searchHit);
        oldEntity = revisionMigration.prepareTimelineData(versionDocument, languageVersionDoc, oldEntity);
      }

      Map<String, Map<String, Object>> languageVersionDoc = getLanguageDocs(hit);
      revisionMigration.prepareTimelineData(document, languageVersionDoc, oldEntity);
    }
    catch (Exception e) {
      RDBMSLogger.instance().exception(e);
    }
    return "success";
  }

  private Map<String, Map<String, Object>> getLanguageDocs(SearchHit hit) throws Exception
  {
    Map<String, Map<String, Object>> languages = new HashMap<>();
    Map<String, Object> document = hit.getSourceAsMap();
    String id = (String) document.get(VersionDocFields.id.name());
    List<Map<String, Object>> languageInstances = (List<Map<String, Object>>) document.get(VersionDocFields.languageInstances.name());
    List<String> ids = new ArrayList<>();
    List<String> docTypes = new ArrayList<>();

    for (Map<String, Object> languageInstance : languageInstances) {
      Integer versionId = (Integer) languageInstance.get(VersionDocFields.versionId.name());
      String languageCode = (String) languageInstance.get(VersionDocFields.languageCode.name());
      ids.add(id + "__" + languageCode + "_" + versionId);
      docTypes.add(hit.getType() + "__" + languageCode);
    }

    IdsQueryBuilder getLanguageDocQuery = QueryBuilders.idsQuery().addIds(ids.toArray(String[]::new));
    SearchSourceBuilder source = new SearchSourceBuilder();
    source.query(getLanguageDocQuery);
    source.size(1000);
    List<String> indices = new ArrayList<>();
    //added due to inconsistent behaviour in language version docs
    if(hit.getIndex().equals("csarchive_version") || hit.getIndex().equals("cs_version")){
      indices.add("csarchive_version");
      indices.add("cs_version");
    }
    else{
      indices.add(hit.getIndex());
    }
    SearchRequest sr = new SearchRequest(indices.toArray(new String[0]));
    sr.source(source);
    sr.types(docTypes.toArray(new String[0]));
    SearchResponse response = RequestHandler.getRestClient().search(sr, RequestOptions.DEFAULT);

    for (SearchHit searchHit : response.getHits().getHits()) {
      Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
      languages.put((String) sourceAsMap.get(VersionDocFields.language.name()), sourceAsMap);
    }

    return languages;
  }

  private SearchHit[] getRevisions(SearchHit hit, String id) throws Exception
  {
    TermQueryBuilder getRevisionsQuery = QueryBuilders.termQuery(VersionDocFields.id.name(), id);
    SearchSourceBuilder source = new SearchSourceBuilder();
    source.sort(SortBuilders.fieldSort(VersionDocFields.versionId.name()).order(SortOrder.ASC));
    source.query(getRevisionsQuery);
    source.size(1000);
    SearchRequest sr = new SearchRequest("cs_version","csarchive_version");
    sr.source(source);
    sr.types(hit.getType());
    SearchResponse response = RequestHandler.getRestClient().search(sr, RequestOptions.DEFAULT);
    return response.getHits().getHits();
  }
}
