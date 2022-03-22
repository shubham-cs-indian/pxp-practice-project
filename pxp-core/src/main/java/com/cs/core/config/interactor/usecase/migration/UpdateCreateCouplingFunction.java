package com.cs.core.config.interactor.usecase.migration;

import java.sql.PreparedStatement;
import org.springframework.stereotype.Service;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class UpdateCreateCouplingFunction extends AbstractRuntimeService<IVoidModel, IVoidModel> implements IUpdateCreateCouplingFunction {
  
  @Override
  protected IVoidModel executeInternal(IVoidModel model) throws Exception
  {
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      
      PreparedStatement functionUpdateQuery = currentConn.prepareStatement(getFunctionUpdateQuery());
      functionUpdateQuery.executeUpdate();
      
    });
    return null;
  }
  
  private String getFunctionUpdateQuery()
  {
    
    String query = "CREATE OR REPLACE FUNCTION pxp.fn_createcoupling(\r\n" + 
        "  p_entityiid bigint,\r\n" + 
        "  p_propertyiid bigint,\r\n" + 
        "  p_status integer,\r\n" + 
        "  p_couplingbehavior integer,\r\n" + 
        "  p_couplingtype integer,\r\n" + 
        "  p_masternodeid character varying,\r\n" + 
        "  p_coupling character varying,\r\n" + 
        "  p_masterentityiid bigint,\r\n" + 
        "  p_masterpropertyiid bigint,\r\n" + 
        "  p_couplingsourceiid bigint,\r\n" + 
        "  p_localeiid bigint)\r\n" + 
        "    RETURNS integer\r\n" + 
        "    LANGUAGE 'plpgsql'\r\n" + 
        "\r\n" + 
        "    COST 100\r\n" + 
        "    VOLATILE \r\n" + 
        "AS $BODY$ declare\r\n" + 
        " vMasterEntityiid bigint;\r\n" + 
        " vMasterValueRecord pxp.allvaluerecord%rowtype;\r\n" + 
        " vTargetValueRecord pxp.allvaluerecord%rowtype;\r\n" + 
        " vMasterTagRecord pxp.alltagsrecord%rowtype;\r\n" + 
        " vTargetTagRecord pxp.alltagsrecord%rowtype;\r\n" + 
        " vPropertyType integer;\r\n" + 
        " vValueiid integer;\r\n" + 
        " vLocaleid varchar;\r\n" + 
        " vParentLocaleid varchar;\r\n" + 
        " vIsCoupledCreated integer := 0;\r\n" + 
        "begin\r\n" + 
        " select propertytype INTO vPropertyType from pxp.propertyconfig where propertyiid = p_propertyiid;\r\n" + 
        "  if(vPropertyType = 11)THEN\r\n" + 
        "  select * INTO vTargetTagRecord from pxp.alltagsrecord where entityiid = p_entityiid and\r\n" + 
        "    propertyiid = p_propertyiid;\r\n" + 
        "    select * INTO vMasterTagRecord from pxp.alltagsrecord where entityiid = p_masterentityiid and\r\n" + 
        "    propertyiid = p_propertyiid;\r\n" + 
        "  ELSE\r\n" + 
        "    select languagecode into vLocaleid from pxp.languageconfig where languageiid = p_localeiid;\r\n" + 
        "    if(vLocaleid is null) then\r\n" + 
        "  select * INTO vTargetValueRecord from pxp.allvaluerecord where entityiid = p_entityiid and\r\n" + 
        "     propertyiid = p_propertyiid and localeid is null;\r\n" + 
        "     select * INTO vMasterValueRecord from pxp.allvaluerecord where entityiid = p_masterentityiid and\r\n" + 
        "      propertyiid = p_propertyiid and localeid is null;\r\n" + 
        " ELSE\r\n" + 
        "     select * INTO vTargetValueRecord from pxp.allvaluerecord where entityiid = p_entityiid and\r\n" + 
        "     propertyiid = p_propertyiid and localeid = vLocaleid;\r\n" + 
        "     if(p_couplingtype = 10) THEN\r\n" + 
        "   select languagecode into vParentLocaleid from pxp.languageconfig where languageiid = p_couplingsourceiid;\r\n" + 
        "    select * INTO vMasterValueRecord from pxp.allvaluerecord where entityiid = p_masterentityiid and\r\n" + 
        "       propertyiid = p_propertyiid and localeid = vParentLocaleid;\r\n" + 
        "   ELSE\r\n" + 
        "    select * INTO vMasterValueRecord from pxp.allvaluerecord where entityiid = p_masterentityiid and\r\n" + 
        "       propertyiid = p_propertyiid and localeid = vLocaleid;\r\n" + 
        "  END IF;\r\n" + 
        " END IF;\r\n" + 
        "  END IF;\r\n" + 
        " select masterentityiid into vMasterEntityiid from pxp.coupledrecord where propertyiid = p_propertyiid\r\n" + 
        " and entityiid = p_entityiid and localeiid = p_localeiid;\r\n" + 
        " if(vMasterEntityiid is not null)THEN\r\n" + 
        "  delete from pxp.coupledrecord where propertyiid = p_propertyiid and entityiid = p_entityiid and localeiid = p_localeiid;\r\n" + 
        "  update pxp.conflictingvalues set recordstatus = p_status where targetentityiid = p_entityiid and propertyiid = p_propertyiid\r\n" + 
        "  and sourceentityiid = vMasterEntityiid and localeiid = p_localeiid;\r\n" + 
        "  INSERT INTO pxp.conflictingvalues(\r\n" + 
        "  targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)\r\n" + 
        "  VALUES (p_entityiid, p_propertyiid, p_masterentityiid, p_couplingbehavior, p_couplingsourceiid, p_status, p_couplingtype, p_localeiid);\r\n" + 
        " ELSE\r\n" + 
        "  select sourceentityiid into vMasterEntityiid from pxp.conflictingvalues where propertyiid = p_propertyiid\r\n" + 
        "  and targetentityiid = p_entityiid and localeiid = p_localeiid;\r\n" + 
        "  if(vMasterEntityiid is null)THEN\r\n" + 
        "   if(vPropertyType = 11)THEN\r\n" + 
        "   if(vTargetTagRecord is null)THEN\r\n" + 
        "        p_status := 4;\r\n" + 
        "      ELSE\r\n" + 
        "        IF(vTargetTagRecord.usrtags = vMasterTagRecord.usrtags) THEN\r\n" + 
        "          p_status := 4;\r\n" + 
        "          delete from pxp.tagsrecord where entityiid = p_entityiid and\r\n" + 
        "           propertyiid = p_propertyiid;\r\n" + 
        "        END IF;\r\n" + 
        "      END IF;\r\n" + 
        "  ELSE\r\n" + 
        "   if(vTargetValueRecord is null)THEN\r\n" + 
        "       p_status := 4;\r\n" + 
        "       ELSE IF(vTargetValueRecord.value = vMasterValueRecord.value) THEN\r\n" + 
        "      p_status := 4;\r\n" + 
        "      if(vLocaleid is null)THEN\r\n" + 
        "        delete from pxp.valuerecord where entityiid = p_entityiid and\r\n" + 
        "           propertyiid = p_propertyiid and localeid is null;\r\n" + 
        "      ELSE\r\n" + 
        "        delete from pxp.valuerecord where entityiid = p_entityiid and\r\n" + 
        "           propertyiid = p_propertyiid and localeid = vLocaleid;\r\n" + 
        "      END IF;\r\n" + 
        "    END IF;\r\n" + 
        "     END IF;\r\n" + 
        " END IF;\r\n" + 
        "END IF;\r\n" + 
        " if(p_status = 4) THEN\r\n" + 
        "  INSERT INTO pxp.coupledrecord(\r\n" + 
        "   propertyiid, entityiid, recordstatus, couplingbehavior, couplingtype, masternodeid, coupling,\r\n" + 
        "    masterentityiid, masterpropertyiid, localeiid)\r\n" + 
        "   VALUES (p_propertyiid, p_entityiid, p_status, p_couplingbehavior, p_couplingType,p_masternodeid,\r\n" + 
        "     '', p_masterentityiid, p_propertyiid, p_localeiid);\r\n" + 
        "     vIsCoupledCreated := 1;\r\n" + 
        " END IF;\r\n" + 
        "  INSERT INTO pxp.conflictingvalues(\r\n" + 
        "  targetentityiid, propertyiid, sourceentityiid, couplingtype, couplingsourceiid, recordstatus, couplingsourcetype, localeiid)\r\n" + 
        "  VALUES (p_entityiid, p_propertyiid, p_masterentityiid, p_couplingbehavior, p_couplingsourceiid, p_status, p_couplingtype, p_localeiid);\r\n" + 
        " END IF;\r\n" + 
        " return vIsCoupledCreated;\r\n" + 
        "end\r\n" + 
        "$BODY$;\r\n" + 
        "\r\n" + 
        "ALTER FUNCTION pxp.fn_createcoupling(bigint, bigint, integer, integer, integer, character varying, character varying, bigint, bigint, bigint, bigint)\r\n" + 
        "    OWNER TO pxp;";
    
    return query;
  }
  
}
