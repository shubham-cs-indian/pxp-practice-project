package com.cs.core.elastic.services;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import com.cs.core.elastic.Index;
import com.cs.core.elastic.das.ElasticServiceDAS;
import com.cs.core.elastic.iservices.IIndexServices;
import com.cs.core.services.CSProperties;
import com.cs.core.technical.exception.CSInitializationException;

public class IndexServices implements IIndexServices {

  private static final String NoOfShards   = "index.number_of_shards";
  private static final String NoOfReplicas = "index.number_of_replicas";

  private static final String replicaProperty = "elastic.server.number_of_replicas";
  private static final String shardsProperty = "elastic.server.number_of_shards";
  
  private final int numberOfReplicas;
  private final int numberOfShards;

  public IndexServices(int replica, int shard)
  {
    this.numberOfReplicas = replica;
    this.numberOfShards = shard;
  }

  public IndexServices() throws CSInitializationException
  {
    CSProperties.init("rdbms.properties");
    this.numberOfReplicas = CSProperties.instance().getInt(replicaProperty);
    this.numberOfShards = CSProperties.instance().getInt(shardsProperty);
  }

  @Override
  public void createIndices(Collection<Index> indices) throws IOException
  {
    for (Index index : indices) {
      Boolean doesIndexExist = ElasticServiceDAS.instance().doesIndexExist(index);
      if (doesIndexExist) {
        continue;
      }

      CreateIndexRequest createRequest = new CreateIndexRequest(index.name());
      Settings.Builder settingBuilder = Settings.builder().put(NoOfShards, numberOfShards).put(NoOfReplicas, numberOfReplicas);
      index.getAnalyzers().forEach(analyzer -> {
        analyzer.keySet().forEach(key ->{
          settingBuilder.put(key,analyzer.get(key));
        }
        );
      });
      createRequest.settings(settingBuilder);

      URL mappingFile = IndexServices.class.getClassLoader().getResource(index.getMappingFileName());
      URL url = Objects.requireNonNull(mappingFile, "Mapping File Url Found Null");

      String mapping = FileUtils.readFileToString(new File(url.getFile()), StandardCharsets.UTF_8.name());
      createRequest.mapping(mapping, XContentType.JSON);

      ElasticServiceDAS.instance().createIndex(createRequest);
    }
  }

}
