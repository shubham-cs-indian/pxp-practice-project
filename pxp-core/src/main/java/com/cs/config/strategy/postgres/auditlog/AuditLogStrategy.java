package com.cs.config.strategy.postgres.auditlog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.model.auditlog.IAuditLogModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.VoidModel;
import com.cs.core.runtime.strategy.utils.DbUtils;

@Component
public class AuditLogStrategy extends AbstractAuditLogStrategy<IListModel<IAuditLogModel>, IModel>
    implements IAuditLogStrategy {
  
  @Autowired
  DbUtils dbUtils;
  
  @Override
  public IModel executeInternal(Connection con, IListModel<IAuditLogModel> listmodel) throws Exception
  {
//    List<IAuditLogModel> auditLogInfoList = (List<IAuditLogModel>) listmodel.getList();
//    PreparedStatement getAuditLogStatements = con.prepareStatement(AUDIT_LOG);
//    for (IAuditLogModel auditLogInfo : auditLogInfoList) {
//      getAuditLogStatements.setString(1, auditLogInfo.getActivity());
//      getAuditLogStatements.setLong(2, auditLogInfo.getDate());
//      getAuditLogStatements.setString(3, auditLogInfo.getEntityType());
//      getAuditLogStatements.setString(4, auditLogInfo.getElement());
//      getAuditLogStatements.setString(5, auditLogInfo.getElementCode());
//      getAuditLogStatements.setString(6, auditLogInfo.getElementName());
//      getAuditLogStatements.setString(7, auditLogInfo.getElementType());
//      getAuditLogStatements.setString(8, auditLogInfo.getIpAddress());
//      getAuditLogStatements.setString(9, auditLogInfo.getUserName());
//      getAuditLogStatements.executeUpdate();
//    }
//    getAuditLogStatements.close();
    return new VoidModel();
  }
  
  
  
}
