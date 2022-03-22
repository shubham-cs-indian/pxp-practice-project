package com.cs.core.config.interactor.usecase.migration;

import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.migration.IMigrateConfigIIDsModel;
import com.cs.core.config.interactor.model.migration.MigrateConfigIIDsModel;
import com.cs.core.config.strategy.usecase.migration.IMigrateConfigIIDsStrategy;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.dto.TagValueDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;

/**
 * @author tauseef
 */
@Service
public class MigrateConfigIIDs implements IMigrateConfigIIDs {

  @Autowired
  IMigrateConfigIIDsStrategy migrateConfigIIDsStrategy;

  @Override
  public IVoidModel execute(IVoidModel dataModel) throws Exception {

    IMigrateConfigIIDsModel migrateConfigIIDsModel = new MigrateConfigIIDsModel();
    fillProperties(migrateConfigIIDsModel);
    fillClassifiers(migrateConfigIIDsModel);
    fillUsers(migrateConfigIIDsModel);

    migrateConfigIIDsStrategy.execute(migrateConfigIIDsModel);

    RDBMSLogger.instance().info("\n\nMigrated IIDs Successfully.........\n\n");
    return null;
  }

  private void fillUsers(IMigrateConfigIIDsModel migrateConfigIIDsModel) throws RDBMSException {
    List<IUserDTO> userDTOS = new ArrayList<>();
    migrateConfigIIDsModel.setUsers(userDTOS);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(Q_USER);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        userDTOS.add(new UserDTO(result));
      }
    });
  }

  public void fillProperties(IMigrateConfigIIDsModel migrateConfigIIDsModel) throws RDBMSException {
    List<IPropertyDTO> attributeDTOS = new ArrayList<>();
    List<IPropertyDTO> tagDTOS = new ArrayList<>();
    List<IPropertyDTO> relationshipDTOS = new ArrayList<>();
    List<ITagValueDTO> tagValueDTOS = new ArrayList<>();

    migrateConfigIIDsModel.setAttributes(attributeDTOS);
    migrateConfigIIDsModel.setTags(tagDTOS);
    migrateConfigIIDsModel.setRelationships(relationshipDTOS);
    migrateConfigIIDsModel.setTagValues(tagValueDTOS);

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(Q_PROPERTY);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        IPropertyDTO propertyDTO = new PropertyDTO(result);
        switch (propertyDTO.getSuperType()) {
          case ATTRIBUTE:
            attributeDTOS.add(propertyDTO);
            break;
          case TAGS:
            tagDTOS.add(propertyDTO);
            break;
          case RELATION_SIDE:
            relationshipDTOS.add(propertyDTO);
            break;
        }
      }
    });

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(Q_TAG_VALUE);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        ITagValueDTO tagValueDTO = new TagValueDTO(result);
        tagValueDTOS.add(tagValueDTO);
      }
    });
  }

  public void fillClassifiers(IMigrateConfigIIDsModel migrateConfigIIDsModel) throws RDBMSException {
    List<IClassifierDTO> classDTOS = new ArrayList<>();
    List<IClassifierDTO> taxonomyDTOS = new ArrayList<>();

    migrateConfigIIDsModel.setClassifiers(classDTOS);
    migrateConfigIIDsModel.setTaxonomies(taxonomyDTOS);

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(Q_CLASSIFIER);
      stmt.execute();
      IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
      while (result.next()) {
        IClassifierDTO classifierDTO = new ClassifierDTO(result);
        switch (classifierDTO.getClassifierType()) {
          case CLASS:
            classDTOS.add(classifierDTO);
            break;

          case TAXONOMY:
          case MINOR_TAXONOMY:
            taxonomyDTOS.add(classifierDTO);
            break;
        }
      }
    });
  }

  @Override
  public ServiceType getServiceType()
  {
    // TODO Auto-generated method stub
    return null;
  }
}
