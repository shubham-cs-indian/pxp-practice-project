package com.cs.core.config.migration;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.model.migration.IMigrateDeprecateVirtualCatalogModel;
import com.cs.core.config.interactor.model.migration.MigrateDeprecateVirtualCatalogModel;
import com.cs.core.config.migration.IMigrateDeprecateVirtualCatalogService;
import com.cs.core.config.strategy.usecase.migration.IMigrateDeprecateVirtualCatalogStrategy;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * 
 * @author jamil.ahmad
 *
 */
@Service
public class MigrateDeprecateVirtualCatalogService extends AbstractDeleteConfigService<IVoidModel, IMigrateDeprecateVirtualCatalogModel>
    implements IMigrateDeprecateVirtualCatalogService {
  
  private static final String                       Q_NATURE_CLASS = "select b.baseentityiid from pxp.baseentity b where b.classifieriid in( %s )";
  
  @Autowired
  protected IMigrateDeprecateVirtualCatalogStrategy migratedeprecateVirtualCatalogStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                     rdbmsComponentUtils;
  
  @Override
  protected IMigrateDeprecateVirtualCatalogModel executeInternal(IVoidModel dataModel) throws Exception
  {
    IMigrateDeprecateVirtualCatalogModel configModel = new MigrateDeprecateVirtualCatalogModel();
    try {
      configModel = migratedeprecateVirtualCatalogStrategy.execute(new VoidModel());
    }
    catch (Exception e) {
      RDBMSLogger.instance().debug("Config Klass deletetion failed ");
    }
    
    if (configModel.getKlassIds().isEmpty()) {
      return configModel;
    }
    
    List<Long> baseEntityIIDs = getInstnaceIIdsOfKlasses(configModel.getKlassIIDs());
    
    baseEntityIIDs.forEach(iid -> {
      try {
        IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(iid);
        baseEntityDAO.delete(false);
      }
      catch (Exception e) {
        RDBMSLogger.instance().debug("Instance deletetion failed ");
      }
    });
    
    for (String code : configModel.getKlassIds()) {
      try {
        ConfigurationDAO.instance().deleteClassifier(code);
      }
      catch (Exception e) {
        RDBMSLogger.instance().debug("ClassifierConfig deletetion failed ");
      }
    }
    return configModel;
  }
  
  private List<Long> getInstnaceIIdsOfKlasses(List<Long> list)
  {
    List<Long> baseEntityIIDs = new ArrayList<Long>();
    
    try {
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        String query = String.format(Q_NATURE_CLASS, Text.join(",", list, "'%s'"));
        PreparedStatement stmt = currentConn.prepareStatement(query);
        stmt.execute();
        IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
        while (resultSet.next()) {
          baseEntityIIDs.add(resultSet.getLong("baseentityiid"));
        }
      });
    }
    catch (RDBMSException e) {
      RDBMSLogger.instance().debug("Get baseentity by klass code failed.");
    }
    return baseEntityIIDs;
  }
  
}
