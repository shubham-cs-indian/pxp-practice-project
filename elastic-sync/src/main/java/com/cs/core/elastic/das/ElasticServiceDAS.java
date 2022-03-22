package com.cs.core.elastic.das;

import com.cs.core.elastic.Index;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class ElasticServiceDAS {

  // Singleton implementation
  private static ElasticServiceDAS   INSTANCE   = new ElasticServiceDAS();
  private RestHighLevelClient restClient;


  private ElasticServiceDAS()
  {
    initialize();
  }
  public static ElasticServiceDAS instance()
  {
    if(INSTANCE == null){
      INSTANCE = new ElasticServiceDAS();
    }
    return INSTANCE;
  }

  public RestHighLevelClient initialize()
  {
    try {
      if (restClient == null) {
        String connectionString = CSProperties.instance().getString("elastic.server.url");
        URL elasticURL = new URL(connectionString);
        restClient = new RestHighLevelClient(
            RestClient.builder(new HttpHost(elasticURL.getHost(), elasticURL.getPort(), elasticURL.getProtocol())));
      }
    }
    catch (MalformedURLException | CSInitializationException e) {
      RDBMSLogger.instance().exception(e);
      e.printStackTrace();
    }
    return restClient;
  }

  /**
   *
   * @param newInstance Source to be indexed.
   * @param index Elasticsearch index in which the source needs to be indexed.
   * @param id Unique id of the document to be indexed.
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public void indexDocument(Map<String, Object> newInstance, String index, String id) throws IOException
  {
    IndexRequest indexRequest = indexRequest(newInstance, index, id);
    restClient.index(indexRequest, RequestOptions.DEFAULT);
  }

  /**
   *
   * @param newInstance document to index
   * @param index index in which document is to be indexed.
   * @param id id of document.
   * @return index request
   */
  public IndexRequest indexRequest(Map<String, Object> newInstance, String index, String id)
  {
    IndexRequest indexRequest = new IndexRequest(index);
    indexRequest.source(newInstance).id(id);
    return indexRequest;
  }

  /**
   *
   * @param id Unique id of the document to be retrieved
   * @param index indexName from which the document needs to be retrieved.
   * @return Document belonging to the id provided in parameter,
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public Map<String, Object> getDocument(String id, String index) throws IOException
  {
    GetRequest getRequest = new GetRequest(index);
    getRequest.id(id);
    GetResponse getResponse = restClient.get(getRequest, RequestOptions.DEFAULT);
    return getResponse.getSource();
  }

  public void updateDocument(Map<String, Object> newInstance, String id, String index) throws IOException
  {
    UpdateRequest updateRequest = new UpdateRequest(index, id);
    updateRequest.doc(newInstance);
    restClient.update(updateRequest, RequestOptions.DEFAULT);
  }

  /**
   *
   * @param id id of the document to be deleted
   * @param index name of the index from which the document is to be deleted.
   * @throws IOException produced by failed or interrupted I/O operations.
   */
  public void deleteDocument(String id, String index) throws IOException
  {
    DeleteRequest deleteRequest = new DeleteRequest(index, id);
    restClient.delete(deleteRequest, RequestOptions.DEFAULT);
  }

  /**
   *
   * @param query search source with specifications for search.
   * @param indices indices on which this search source needs to be executed.
   * @return Array of SearchHit[A Object that represents the Document that qualified for search.]
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public SearchHit[] search(SearchSourceBuilder query, String... indices) throws IOException
  {
    SearchRequest sr = new SearchRequest(indices);
    sr.source(query);
    SearchResponse response = restClient.search(sr, RequestOptions.DEFAULT);
    return response.getHits().getHits();
  }

  /**
   *
   * @param query query with filtering for count.
   * @param indices indices on which the query should be executed.
   * @return Response that represents the total count of documents returned by search execution.
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public CountResponse count(QueryBuilder query, String... indices) throws IOException
  {
    CountRequest countRequest = new CountRequest(indices);
    countRequest.query(query);
    return restClient.count(countRequest, RequestOptions.DEFAULT);
  }

  /**
   *
   * @param query search source with specifications for search.
   * @param indices indices on which this search source needs to be executed.
   * @return Aggregations object containing result of aggregating data.
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public Aggregations aggregate(SearchSourceBuilder query, String... indices) throws IOException
  {
    SearchRequest sr = new SearchRequest(indices);
    sr.source(query);
    SearchResponse response = restClient.search(sr, RequestOptions.DEFAULT);
    return response.getAggregations();
  }

  /**
   *
   * @param requests Requests can be of type Bulk Index, Bulk Update, Bulk Delete
   * @return response of bulk request
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public BulkResponse bulkRequest(Collection<? extends DocWriteRequest> requests) throws IOException
  {
    BulkRequest bulkRequest = new BulkRequest();
    bulkRequest.add(requests.toArray(new DocWriteRequest[]{}));
    return restClient.bulk(bulkRequest, RequestOptions.DEFAULT);
  }

  /**
   *
   * @param request request to create mapping
   * @return response of index creation by elastic API.
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public CreateIndexResponse createIndex(CreateIndexRequest request) throws IOException
  {
     return restClient.indices().create(request, RequestOptions.DEFAULT);
  }

  /**
   *
   * @param index index to be checked.
   * @return whether the index exists or not
   * @throws IOException  produced by failed or interrupted I/O operations.
   */
  public Boolean doesIndexExist(Index index) throws IOException
  {
    GetIndexRequest request = new GetIndexRequest(index.name());
    return restClient.indices().exists(request,RequestOptions.DEFAULT);
  }

  /**
   * @param request request with multiple search
   * @return result of multiple searches
   * @throws IOException produced by failed or interrupted I/O operations.
   */
  public MultiSearchResponse multiSearch(MultiSearchRequest request) throws IOException
  {
    return restClient.msearch(request, RequestOptions.DEFAULT);
  }
  
  /**
   * 
   * @param ids list of document ids to delete
   * @param index index to be checked.
   * @throws IOException
   */
  public void bulkDelete(List<String> ids, String index) throws IOException
  {
    List<DeleteRequest> deleteRequest = new ArrayList<DeleteRequest>();
    for (String id : ids) {
      deleteRequest.add(new DeleteRequest(index, id));
    }
    if (!deleteRequest.isEmpty()) {
      bulkRequest(deleteRequest);
    }
  }
}
