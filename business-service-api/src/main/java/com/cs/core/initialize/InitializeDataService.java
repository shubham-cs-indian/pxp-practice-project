package com.cs.core.initialize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.base.interactor.model.portal.IPortalModel;
import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.config.strategy.configuration.embeddedorientdb.IInitializeDataStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.intialize.elastic.ICreateIndices;

@Service
public class InitializeDataService extends AbstractService<IVoidModel, IVoidModel> implements IInitializeDataService {
  
  @Autowired
  protected IInitializeDataStrategy                     initializeConfigDataStrategy;
  
  @Autowired
  protected IInitializeDataBaseSchemaForLanguageService initializeDataBaseSchemaForLanguageService;
  
  @Autowired
  protected IInitializeDataStrategy                     initializeRuntimeDataStrategy;
  
  @Autowired
  protected IInitializeDataStrategy                     initializeRuntimeDataStrategyForLogging;
  
  @Autowired
  protected IInitializeDataStrategy                     initializeDataStrategyForArchive;
  
  @Autowired
  protected IInitializeDataStrategy                     initializePostgresDataStrategy;
  
  @Autowired
  protected IInitializeDataStrategy                     initializePostgresDataStrategyForPricing;
  
  @Autowired
  protected IInitializeUsersService                     initializeUsersService;
  
  @Autowired
  protected IInitializeAttributesService                initializeAttributesService;
  
  @Autowired
  protected IInitializeRolesService                     initializeRolesService;
  
  @Autowired
  protected IInitializeTagsService                      initalizeTagsService;
  
  @Autowired
  protected IInitializePIMKlassesService                initalizePIMKlassesService;
  
  @Autowired
  protected IInitializeMAMKlassesService                initializeMAMKlassesService;
  
  @Autowired
  protected IInitializeTargetKlassesService             initializeTargetKlassesService;
  
  @Autowired
  protected IInitializeTextAssetKlassesService          initializeTextAssetsService;
  
  @Autowired
  protected IInitializeSupplierKlassesService           initializeSupplierKlassesService;
  
  @Autowired
  protected IInitializeRelationshipsService             initializeRelationshipsService;
  
  @Autowired
  protected IInitializePropertyCollectionService        initializePropertyCollectionsService;
  
  @Autowired
  protected IPostInitializeService                      postInitializeService;
  
  @Autowired
  protected IInitializeOrganizationsService             initializeOrganizationsService;
  
  @Autowired
  protected IInitializeStaticLabelTranslationsService   initializeStaticLabelTranslationsService;
  
  @Autowired
  protected IInitializeTabsService                      initailizeTabsService;
  
  @Autowired
  protected IInitializeSystemsService                   initializeSystemsService;
  
  @Autowired
  protected IInitializeDashboardTabsService             initializeDashboardTabsService;
  
  @Autowired
  protected IInitializeLanguageService                  initializeLanguageService;
  
  @Autowired
  protected IInitializeSmartDocumentService             initializeSmartDocumentService;
  
  @Autowired
  protected IInitializeStandardWorkflowsService         initializeStandardWorkflowsService;
  
  @Autowired
  protected List<IPortalModel>                          portalModels;
  
  @Autowired
  protected IInitializeThemeConfigurationService        initializeThemeConfigurationService;
  
  @Autowired
  protected IInitializeViewConfigurationService         initializeViewConfigurationService;
  
  @Autowired
  protected IInitializeVariantConfigurationService      initializeVariantConfigurationService;
  
  @Autowired
  protected IInitializeIconForStandardEntitesService    initializeIconForStandardEntitesService;
  
  @Autowired
  protected ICreateIndices                              createIndices;
  
  @Override
  public IVoidModel executeInternal(IVoidModel dataModel) throws Exception
  {
    System.out.println("\n\nInitializing Default Data.........\n\n");
    initializeDatabaseConfiguration();
    // initializeConfigMigration.execute();
    // initializeRuntimeMigration.execute();

    initializeLanguageService.execute();
    initializeUsersService.execute();
    initializeIconForStandardEntitesService.execute();
    initializeRolesService.execute();
    initializeAttributesService.execute();
    initalizeTagsService.execute();
    // initializeContexts.execute();
    
    initailizeTabsService.execute();
    initializePropertyCollectionsService.execute();
    initializeOrganizationsService.execute();
    
    initializeKlasses();
   
    initializeSmartDocumentService.execute();
    
    initializeRelationshipsService.execute();
    initializeSystemsService.execute();
    initializeDashboardTabsService.execute();
    initializeStaticLabelTranslationsService.execute();
    initializeDataBaseSchemaForLanguageService.execute();
    initializeStandardWorkflowsService.execute();
    
    initializeThemeConfigurationService.execute();
    initializeViewConfigurationService.execute();
    initializeVariantConfigurationService.execute();
    
    // below code is to handle postInitialize
    postInitializeService.execute();
    System.out.println("\n\nInitialized Data Successfully.........\n\n");
    
    return null;
  }
  
  /**
   * Description : getting all entities from portal json if entity present in
   * json then only initializing class
   *
   * @author Mangesh.Darekar
   * @throws Exception
   */
  private void initializeKlasses() throws Exception
  {
    Set<String> allowedEntities = new HashSet<>();
    for (IPortalModel iPortalModel : portalModels) {
      allowedEntities.addAll(iPortalModel.getAllowedEntities());
    }
    
    for (String allowedEntity : allowedEntities) {
      switch (allowedEntity) {
        case "ArticleInstance":
          initalizePIMKlassesService.execute();
          break;
        case "AssetInstance":
          initializeMAMKlassesService.execute();
          break;
        case "TextAssetInstance":
          initializeTextAssetsService.execute();
          break;
        case "SupplierInstance":
          initializeSupplierKlassesService.execute();
          break;
      }
    }
    
    initializeTargetKlassesService.execute();
  }

  private void initializeDatabaseConfiguration() throws Exception
  {
    if (initializeRuntimeDataStrategyForLogging != null) {
      initializeRuntimeDataStrategyForLogging.execute(null);
    }

    // Initialize data base configuration for OrientDB.
    if (initializeConfigDataStrategy != null) {
      initializeConfigDataStrategy.execute(null);
    }

    // Initialize data base configuration for Elasticsearch.
    if (initializeRuntimeDataStrategy != null) {
      initializeRuntimeDataStrategy.execute(null);
    }
    if (initializeDataStrategyForArchive != null) {
      initializeDataStrategyForArchive.execute(null);
    }

    // for postgress initialization
    if (initializePostgresDataStrategy != null) {
      initializePostgresDataStrategy.execute(null);
    }

    if (initializePostgresDataStrategyForPricing != null) {
      initializePostgresDataStrategyForPricing.execute(null);
    }

    if (createIndices != null) {
      createIndices.execute();
    }
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
