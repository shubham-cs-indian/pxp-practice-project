package com.cs.core.config.interactor.usecase.migration;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.businessapi.base.AbstractService;
import com.cs.core.json.JSONContent;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.coupling.dto.RuleHandlerForMigrationDTO;
import com.cs.core.rdbms.coupling.idto.IRuleHandlerForMigrationDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.interactor.model.configuration.IKPIScriptRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.google.common.collect.Lists;

@Service
public class DQRulesMustShouldKPIGenerationOptimizedMigration2 extends AbstractService<IKPIScriptRequestModel, IModel> implements IDQRulesMustShouldKPIGenerationOptimizedMigration2 { 
  
  @Autowired
  protected RDBMSComponentUtils                         rdbmsComponentUtils;
  
  @Autowired
  protected ISessionContext       context;
  
  protected Integer batchSize =    100;
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }

  @Override
  protected IModel executeInternal(IKPIScriptRequestModel model) throws Exception
  {
    RDBMSLogger.instance().info("DQ-MustShould API initiated Mode 2");
      RDBMSConnectionManager.instance()
      .runTransaction((RDBMSConnection currentConn) -> {
        
        String FETCH_BUCKET_QUERY = "select array_agg(be.baseentityiid) as baseentityiids , be.classifieriid \r\n" + 
            "from pxp.baseentity be LEFT JOIN pxp.baseentityclassifierlink becl ON be.baseentityiid = becl.baseentityiid \r\n" + 
            "where catalogcode != 'diff-pim' and becl.otherclassifieriid is null group by be.classifieriid";
        PreparedStatement stmt = currentConn.prepareStatement(FETCH_BUCKET_QUERY);
        stmt.execute();
        IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
        while (result.next()) {
          Long classifieriid = result.getLong("classifieriid");
          Long[] baseentityiids = result.getLongArray("baseentityiids");
          List<Long> classifiers = new ArrayList<>();
          classifiers.add(classifieriid);
          List<Long> baseentityiidsAsList = Arrays.asList(baseentityiids);
          if(baseentityiidsAsList.size() > batchSize) {
            List<List<Long>> partitions = Lists.partition(baseentityiidsAsList, batchSize);
            for (List<Long> iids:partitions) {
              IRuleHandlerForMigrationDTO dto = new RuleHandlerForMigrationDTO();
              dto.setBaseEntityIids(iids);
              dto.setClassifierIids(classifiers);
              dto.setShouldUseCSCache(true);
              dto.setShouldEvaluateIdentifier(model.getShouldEvaluateIdentifier());
              
              BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), "RULE_HANDLER_FOR_MIGRATION_SERVICE", ""
                  , IBGProcessDTO.BGPPriority.HIGH, new JSONContent(dto.toJSON()));
            }
          }
          else {
            IRuleHandlerForMigrationDTO dto = new RuleHandlerForMigrationDTO();
            dto.setBaseEntityIids(baseentityiidsAsList);
            dto.setClassifierIids(classifiers);
            dto.setShouldUseCSCache(true);
            dto.setShouldEvaluateIdentifier(model.getShouldEvaluateIdentifier());
            
            BGPDriverDAO.instance().submitBGPProcess(context.getUserSessionDTO().getUserName(), "RULE_HANDLER_FOR_MIGRATION_SERVICE", ""
                , IBGProcessDTO.BGPPriority.HIGH, new JSONContent(dto.toJSON()));
          }
        }
      });
    
      RDBMSLogger.instance().info("DQ-MustShould API ended Mode 2");
    return null;
  }
  
}
