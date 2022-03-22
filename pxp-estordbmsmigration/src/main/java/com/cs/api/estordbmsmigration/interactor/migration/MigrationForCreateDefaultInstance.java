package com.cs.api.estordbmsmigration.interactor.migration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSRequestModel;
import com.cs.api.estordbmsmigration.model.migration.ISyncCongifToRDBMSResponseModel;
import com.cs.api.estordbmsmigration.model.migration.SyncCongifToRDBMSRequestModel;
import com.cs.api.estordbmsmigration.strategy.migration.ISyncCongifToRDBMSStrategy;
import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.defaultklassinstance.ICreateDefaultInstanceService;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

@Service
public class MigrationForCreateDefaultInstance extends AbstractService<IVoidModel, IVoidModel> implements IMigrationForCreateDefaultInstance {
  
  @Autowired
  ISyncCongifToRDBMSStrategy    syncCongifToRDBMSStrategy;
  
  @Autowired
  ICreateDefaultInstanceService createDefaultInstanceService;
  
  
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    int size = 500;
    int from = 0;
    
    ILocaleCatalogDAO defaultLocaleCatalogDAO = RDBMSUtils.getDefaultLocaleCatalogDAO();
    Set<String> standardClassifierCode = new HashSet<>();
    standardClassifierCode.add("article");
    standardClassifierCode.add("product_types");
    standardClassifierCode.add("attribute_classes");
    standardClassifierCode.add("single_article");
    standardClassifierCode.add("fileklass");
    standardClassifierCode.add("golden_article_klass");
    standardClassifierCode.add("attachment_asset");
    standardClassifierCode.add("asset_asset");
    standardClassifierCode.add("image_asset");
    standardClassifierCode.add("video_asset");
    standardClassifierCode.add("document_asset");
    standardClassifierCode.add("smartdocument_asset");
    standardClassifierCode.add("market");
    standardClassifierCode.add("supplier");
    standardClassifierCode.add("suppliers");
    standardClassifierCode.add("marketplaces");
    standardClassifierCode.add("distributors");
    standardClassifierCode.add("wholesalers");
    standardClassifierCode.add("translation_agency");
    standardClassifierCode.add("content_enrichment_agency");
    standardClassifierCode.add("digital_asset_agency");
    standardClassifierCode.add("text_asset");
    standardClassifierCode.add("virtual_catalog");
    createDefaultInstanceForKLasses(defaultLocaleCatalogDAO, standardClassifierCode, size, from);
    
    createDefaultInstanceForTaxonomies(defaultLocaleCatalogDAO, size, from);
    
    return new VoidModel();
  }
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  private void createDefaultInstanceForKLasses(ILocaleCatalogDAO defaultLocaleCatalogDAO, Set<String> standardClassifierCode, int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<String> types = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      if (!standardClassifierCode.contains(model.getCode())) {
        types.add(model.getCode());
      }
      
    }
    
    if (types.size() > 0) {
      ITypesTaxonomiesModel taxonomiesModel = new TypesTaxonomiesModel();
      taxonomiesModel.setTypes(types);
      createDefaultInstanceService.execute(taxonomiesModel);
    }
    if (response.getCount() > (from + size)) {
      createDefaultInstanceForKLasses(defaultLocaleCatalogDAO, standardClassifierCode, 500, from + 500);
    }
  }
  
  private void createDefaultInstanceForTaxonomies(ILocaleCatalogDAO defaultLocaleCatalogDAO,int size, int from) throws Exception, RDBMSException
  {
    ISyncCongifToRDBMSRequestModel requestModel = new SyncCongifToRDBMSRequestModel();
    requestModel.setFrom(from);
    requestModel.setSize(size);
    requestModel.setVertexType(VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    
    ISyncCongifToRDBMSResponseModel response = syncCongifToRDBMSStrategy.execute(requestModel);
    List<String> taxonomies = new ArrayList<>();
    for (ISyncCongifToRDBMSModel model : response.getList()) {
      taxonomies.add(model.getCode());
      addTaxonomyChildren(defaultLocaleCatalogDAO, taxonomies, model.getChildrens());
      
    }
    if (taxonomies.size() > 0) {
      ITypesTaxonomiesModel taxonomiesModel = new TypesTaxonomiesModel();
      taxonomiesModel.setTaxonomyIds(taxonomies);
      createDefaultInstanceService.execute(taxonomiesModel);
    }
    if (response.getCount() > (from + size)) {
      createDefaultInstanceForTaxonomies(defaultLocaleCatalogDAO,500, from + 500);
    }
  }
  
  private void addTaxonomyChildren(ILocaleCatalogDAO defaultLocaleCatalogDAO, List<String> taxonomies,
      List<ISyncCongifToRDBMSModel> childrens) throws RDBMSException
  {
    
    for (ISyncCongifToRDBMSModel children : childrens) {
        taxonomies.add(children.getCode());
      addTaxonomyChildren(defaultLocaleCatalogDAO, taxonomies, children.getChildrens());
    }
  }
  
}
