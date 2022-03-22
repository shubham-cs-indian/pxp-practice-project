package com.cs.core.asset.services;

import java.util.List;
import java.util.Random;

import com.cs.core.asset.iservices.IBulkCreateAssetsLinks;
import com.cs.core.bgprocess.idto.IBulkAssetLinkCreationDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.iapp.RDBMSAppDriverManager;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.tracking.idao.IUserSessionDAO;
import com.cs.core.rdbms.tracking.idto.IUserSessionDTO;
import com.cs.core.technical.rdbms.exception.RDBMSException;

public class BulkCreateAssetsLinks implements IBulkCreateAssetsLinks {
  
  private ILocaleCatalogDAO localeCatalogDao;
  protected static String   sessionID = "session#" + new Random().nextInt();
  private String            localeID;
  private String            physicalCatalogId;
  private String            organizationId;
 
  
  public void initializeRDBMSComponents(IUserSessionDTO session) throws RDBMSException
  {
    IUserSessionDAO userSession = RDBMSAppDriverManager.getDriver()
        .newUserSessionDAO();
    ILocaleCatalogDTO localeCatalogDto = userSession.newLocaleCatalogDTO(localeID,
        physicalCatalogId, organizationId);
    // Get Locale CatelogDAO Interface
    localeCatalogDao = userSession.openLocaleCatalog(session, localeCatalogDto);
  }
  
  public BulkCreateAssetsLinks(String localeId, String physicalCatalogId, String organizationId)
  {
    this.physicalCatalogId = physicalCatalogId;
    this.localeID = localeId;
    this.organizationId = organizationId;
  }
  
  @Override
  public List<IBaseEntityDTO> getAssetEntities(IBulkAssetLinkCreationDTO assetLinkCreationDTO,
      IUserSessionDTO session) throws RDBMSException
  {
    initializeRDBMSComponents(session);
    String masterAssetIdList = "";
    String assetIdListTIV = "";
    masterAssetIdList = convertListToString(masterAssetIdList, assetLinkCreationDTO.getMasterAssetIds());
    int index = 0;
    for (String technicalVariantTypeId : assetLinkCreationDTO.getTechnicalVariantTypeIds()) {
      index++;
      if (assetLinkCreationDTO.getTechnicalVariantTypeIds().size() != index)
        assetIdListTIV += "'"+technicalVariantTypeId+ "',";
      else
        assetIdListTIV += "'"+technicalVariantTypeId+"'";
    }
    if(assetIdListTIV.length() == 0)
      assetIdListTIV += "''";
    
    return localeCatalogDao.getAllEntitiesByIIDList(masterAssetIdList, assetIdListTIV,
        assetLinkCreationDTO.getMasterAssetShare());
  }

  private String convertListToString(String assetIdList, List<String> assetIds)
  {
    int index = 0;
    for (String masterAssetId : assetIds) {
      index++;
      if (assetIds.size() != index)
        assetIdList += masterAssetId+ ",";
      else
        assetIdList += masterAssetId;
    }
    return assetIdList;
  }
  
}
