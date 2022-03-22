package com.cs.core.runtime.intialize.elastic;

import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.constants.Constants;
import com.cs.core.elastic.Index;
import com.cs.core.elastic.iservices.IIndexServices;
import com.cs.core.elastic.services.IndexServices;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.technical.exception.CSInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CreateIndices implements ICreateIndices {

  @Autowired
  protected List<IPortalModel> portalModels;

  public void execute() throws IOException
  {
    try {
      IIndexServices indexServices = new IndexServices();
      List<Index> indices = getIndicesForEntities();
      //Creation of golden record bucket index for match and merge tab view
      indices.add(Index.goldenrecordbucketcache);
      indexServices.createIndices(indices);
    }
    catch (CSInitializationException ex) {
      RDBMSLogger.instance().exception(ex);
    }
  }

  private List<Index> getIndicesForEntities()
  {
    List<BaseType> baseTypes = new ArrayList<>();
    for (IPortalModel portal : portalModels) {
      List<String> allowedEntities = portal.getAllowedEntities();
      baseTypes.addAll(getBaseTypes(allowedEntities));
    }
    return Index.getIndicesFromBaseTypes(baseTypes);
  }

  private List<BaseType> getBaseTypes(List<String> allowedEntities)
  {
    List<BaseType> baseTypes = new ArrayList<>();
    for (String allowedEntity : allowedEntities) {
      BaseType baseType = getBaseEntityTypeByModuleEntityType(allowedEntity);
      if (!baseType.equals(BaseType.UNDEFINED)) {
        baseTypes.add(baseType);
      }
    }
    return baseTypes;
  }

  private BaseType getBaseEntityTypeByModuleEntityType(String allowedEntity)
  {
    switch (allowedEntity) {
      case Constants.ARTICLE_INSTANCE_MODULE_ENTITY:
        return BaseType.ARTICLE;
      case Constants.ASSET_INSTANCE_MODULE_ENTITY:
        return BaseType.ASSET;
      case Constants.MARKET_INSTANCE_MODULE_ENTITY:
        return BaseType.TARGET;
      case Constants.TEXT_ASSET_INSTANCE_MODULE_ENTITY:
        return BaseType.TEXT_ASSET;
      case Constants.SUPPLIER_INSTANCE_MODULE_ENTITY:
        return BaseType.SUPPLIER;
      default:
        return BaseType.UNDEFINED;
    }
  }

}
