package com.cs.core.config.interactor.model.asset;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.runtime.interactor.model.assetinstance.AssetDownloadInformationModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetDownloadInformationModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

/**
 * Custom deserialiser for List<IAssetDownloadInformationModel>
 * 
 * @author mrunali.dhenge
 *
 */
public class BulkAssetDownloadWithTIVsCustomDeserializer extends StdDeserializer<List<IAssetDownloadInformationModel>> {
  
  private static final long serialVersionUID = 1L;
  
  protected BulkAssetDownloadWithTIVsCustomDeserializer(Class<?> vc)
  {
    super(vc);
  }
  
  public BulkAssetDownloadWithTIVsCustomDeserializer()
  {
    this(null);
  }
  
  @Override
  public List<IAssetDownloadInformationModel> deserialize(JsonParser arg0,
      DeserializationContext arg1) throws IOException, JsonProcessingException
  {
    List<IAssetDownloadInformationModel> returnList = ObjectMapperUtil.readValue(arg0,
        new TypeReference<ArrayList<AssetDownloadInformationModel>>()
        {
          
        });
    return returnList;
  }
}
