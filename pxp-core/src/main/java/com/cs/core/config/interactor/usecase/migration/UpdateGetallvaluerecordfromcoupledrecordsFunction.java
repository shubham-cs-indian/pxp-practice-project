package com.cs.core.config.interactor.usecase.migration;

import java.sql.PreparedStatement;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

@Service
public class UpdateGetallvaluerecordfromcoupledrecordsFunction extends AbstractRuntimeService<IVoidModel, IVoidModel> implements IUpdateGetallvaluerecordfromcoupledrecordsFunction{

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
    
    String query = "CREATE OR REPLACE FUNCTION pxp.fn_getallvaluerecordfromcoupledrecords(\r\n" + 
        "  )\r\n" + 
        "    RETURNS SETOF record \r\n" + 
        "    LANGUAGE 'plpgsql'\r\n" + 
        "\r\n" + 
        "    COST 100\r\n" + 
        "    VOLATILE \r\n" + 
        "    ROWS 1000\r\n" + 
        "AS $BODY$ declare\r\n" + 
        "    vCoupledRecord pxp.coupledRecord%rowtype;\r\n" + 
        " vValueRecord pxp.valuerecord%rowtype;\r\n" + 
        " vRecord record;\r\n" + 
        " vPropertyType smallint;\r\n" + 
        " vLocaleid character varying;\r\n" + 
        "begin\r\n" + 
        " for vCoupledRecord in (select c.* from pxp.coupledrecord c\r\n" + 
        "  join pxp.propertyconfig pc on c.propertyiid = pc.propertyiid and pc.supertype = 1) loop\r\n" + 
        "   select * into vValueRecord from pxp.fn_getValueRecordFromCoupledRecord(vCoupledRecord);\r\n" + 
        "   \r\n" + 
        "   SELECT languagecode into vLocaleid\r\n" + 
        "        FROM pxp.languageconfig\r\n" + 
        "        WHERE languageiid = vCoupledRecord.localeiid;\r\n" + 
        "    \r\n" + 
        "    if(vLocaleid is null) then\r\n" + 
        "      vLocaleid = vValueRecord.localeid;\r\n" + 
        "    END IF;\r\n" + 
        "    \r\n" + 
        "         SELECT vCoupledRecord.propertyiid,\r\n" + 
        "      vCoupledRecord.entityiid,\r\n" + 
        "      vCoupledRecord.recordstatus,\r\n" + 
        "      vCoupledRecord.couplingtype,\r\n" + 
        "      vCoupledRecord.couplingbehavior,\r\n" + 
        "      vCoupledRecord.coupling,\r\n" + 
        "      vCoupledRecord.masterentityiid,\r\n" + 
        "      vCoupledRecord.masterpropertyiid,\r\n" + 
        "      vValueRecord.valueiid,\r\n" + 
        "      vLocaleid,\r\n" + 
        "      vValueRecord.contextualobjectiid,\r\n" + 
        "      vValueRecord.value,\r\n" + 
        "      vValueRecord.ashtml,\r\n" + 
        "      vValueRecord.asnumber,\r\n" + 
        "      vValueRecord.unitsymbol,\r\n" + 
        "      vValueRecord.calculation into vRecord;\r\n" + 
        "         return next vRecord;\r\n" + 
        " end loop;\r\n" + 
        "end\r\n" + 
        "$BODY$;\r\n" + 
        "\r\n" + 
        "ALTER FUNCTION pxp.fn_getallvaluerecordfromcoupledrecords()\r\n" + 
        "    OWNER TO pxp;\r\n" ;
    
    return query;
  }
  
}
