package com.cs.core.config.interactor.usecase.migration;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.ListUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.config.interactor.model.datarule.DataRulesHelperModel;
import com.cs.core.config.interactor.model.datarule.IDataRulesHelperModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.entity.dao.BaseEntityDAO;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.rdbms.services.resolvers.RuleHandler;
import com.cs.core.rdbms.uniqueViolation.idao.IUniquenessViolationDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.core.services.CSConfigServer;

@Component
public class DQRulesMustShouldKPIGenerationMigration extends AbstractService<IIdsListParameterModel, IModel> implements IDQRulesMustShouldKPIGenerationMigration{

  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected ISessionContext       context;
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }

  @Override
  protected IModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    List<Long> ids = model.getIds().stream().map(e-> Long.parseLong(e)).collect(Collectors.toList());
    if(ids.isEmpty()) {
      RDBMSConnectionManager.instance()
      .runTransaction((RDBMSConnection currentConn) -> {
        String FETCH_BASE_ENTITY_QUERY = "Select baseentityiid from pxp.baseentity";
        PreparedStatement stmt = currentConn.prepareStatement(FETCH_BASE_ENTITY_QUERY);
        stmt.execute();
        IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
        while (result.next()) {
          ids.add(result.getLong("baseentityiid"));
        }
      });
    }
    
    RuleHandler ruleHandler = new RuleHandler();
    for(Long baseEntityIID :ids) {
      ILocaleCatalogDAO localeCatalogDAO = RDBMSUtils.newUserSessionDAO().openLocaleCatalog(context.getUserSessionDTO(), baseEntityIID);
      IBaseEntityDTO baseEntityDTO = localeCatalogDAO.getEntityByIID(baseEntityIID);
      IBaseEntityDAO baseEntityDAO = localeCatalogDAO.openBaseEntity(baseEntityDTO);
      List<String> baseEntityClassifiers = new ArrayList<String>();
      List<String> baseEntityTaxonomies = new ArrayList<String>();
    
      Map<String, Object> requestMap = new HashMap<>();
      ruleHandler.getBaseEntityClassifiersAndTaxonomies(localeCatalogDAO, baseEntityDAO, baseEntityClassifiers, baseEntityTaxonomies);
      requestMap.put("classifiers", ListUtils.sum(baseEntityClassifiers, baseEntityTaxonomies));
      JSONObject detailsFromODB = CSConfigServer.instance().request(requestMap, "GetProductIdentifiersForClassifiers",
          localeCatalogDAO.getLocaleCatalogDTO().getLocaleID());
      IUniquenessViolationDAO openUniquenessDAO = localeCatalogDAO.openUniquenessDAO();
      IDataRulesHelperModel dataRulesHelperModel = ObjectMapperUtil.readValue(detailsFromODB.toString(), DataRulesHelperModel.class); 
      
      ruleHandler.applyDQRules(localeCatalogDAO, baseEntityIID, baseEntityClassifiers, baseEntityTaxonomies, new ArrayList<>(), true,
          null, dataRulesHelperModel.getReferencedElements(), dataRulesHelperModel.getReferencedAttributes(), dataRulesHelperModel.getReferencedTags());
      ruleHandler.evaluateDQMustShould(baseEntityDTO.getBaseEntityIID(), dataRulesHelperModel, baseEntityDAO, localeCatalogDAO);
      ruleHandler.evaluateProductIdentifiers(baseEntityIID, detailsFromODB, (BaseEntityDAO)baseEntityDAO, openUniquenessDAO, localeCatalogDAO);
    }
    
    return null;
  }
  
}
