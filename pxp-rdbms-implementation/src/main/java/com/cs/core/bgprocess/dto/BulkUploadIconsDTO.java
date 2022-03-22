package com.cs.core.bgprocess.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;

import com.cs.core.bgprocess.idto.IAssetFileDTO;
import com.cs.core.bgprocess.idto.IBulkUploadIconsDTO;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.dto.SimpleDTO;
import com.cs.core.technical.exception.CSFormatException;

public class BulkUploadIconsDTO extends SimpleDTO implements IBulkUploadIconsDTO{
  
  private final String        ASSETS            = "assets";
  private final String        LOCALE_ID         = "localeId";
  private final String        USER_ID           = "userId";
  private static final String ORGANIZATION_CODE = "organizationCode";
  
  List<IAssetFileDTO>         assets     = new ArrayList<>();
  private String              localeId;
  private String              userId;
  private String              organizationCode;
  
  @Override
  public List<IAssetFileDTO> getAssets()
  {
    return assets;
  }

  @Override
  public void setAssets(List<IAssetFileDTO> assets)
  {
    this.assets = assets;
  }

  @Override
  public String getLocaleId()
  {
    return localeId;
  }

  @Override
  public void setLocaleId(String localeId)
  {
    this.localeId = localeId;
  }

  @Override
  public String getUserId()
  {
    return userId;
  }

  @Override
  public void setUserId(String userId)
  {
    this.userId = userId;
  }

  @Override
  public String getOrganizationCode()
  {
    return organizationCode;
  }

  @Override
  public void setOrganizationCode(String organizationCode)
  {
    this.organizationCode = organizationCode;
  }

  @Override
  public StringBuffer toJSONBuffer() throws CSFormatException
  {
    return JSONBuilder.assembleJSONBuffer(JSONBuilder.newJSONArray(ASSETS, assets),
        JSONBuilder.newJSONField(LOCALE_ID, localeId),
        JSONBuilder.newJSONField(USER_ID, userId),
        JSONBuilder.newJSONField(ORGANIZATION_CODE, organizationCode)
        );
  }
  
  @Override
  public void fromJSON(JSONContentParser json) throws CSFormatException
  {
    assets.clear();
    JSONArray jsonArray = json.getJSONArray(ASSETS);
    for(Object jsonV : jsonArray) {
      AssetFileDTO assetFileDTO = new AssetFileDTO();
      assetFileDTO.fromJSON(jsonV.toString());
      assets.add(assetFileDTO);
    }
    localeId = json.getString(LOCALE_ID);
    userId = json.getString(USER_ID);
    organizationCode = json.getString(ORGANIZATION_CODE);
    
  }
  
}
