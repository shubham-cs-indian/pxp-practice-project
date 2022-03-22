package com.cs.core.config.interactor.usecase.klass;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;
import com.cs.utils.ContextUtil;

public abstract class AbstractCreateKlass<P extends IKlassModel, R extends IGetKlassEntityWithoutKPModel>
    extends AbstractCreateConfigInteractor<P, R> {
  
  public abstract R executeCreateKlass(P klassModel) throws Exception;
  
  public R executeInternal(P klassModel) throws Exception
  {
    
    String externalId = klassModel.getId();
    String code = klassModel.getCode();
    // Set id if not present
    if (StringUtils.isEmpty(code)) {
      code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.CLASS.getPrefix());
    }
    klassModel.setId(code);
    klassModel.setCode(code);
    
    klassModel.setSections(new ArrayList<>());
    
    IClassifierDTO createClassifier = RDBMSUtils.createClassifier(klassModel.getCode(),
        IClassifierDTO.ClassifierType.CLASS);
    klassModel.setClassifierIID(createClassifier.getClassifierIID());
    
    setContextIDAndContextIID(klassModel);
    setRelationshipAndSideId(klassModel, externalId);
    return executeCreateKlass(klassModel);
  }
  
  private void setContextIDAndContextIID(P klassModel) throws Exception
  {
    ContextType contextType = ContextUtil
        .getContextTypeByClassNatureType(klassModel.getNatureType());
    if (contextType != ContextType.UNDEFINED) {
      String id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.CONTEXT.getPrefix());
      RDBMSUtils.newConfigurationDAO().createContext(id, contextType);
      klassModel.setContextID(id);
      klassModel.setCode(id);
    }
  }
  
  private void setRelationshipAndSideId(P klassModel, String externalId) throws Exception
  {
    if (klassModel.getIsNature()) {
      List<IKlassNatureRelationship> relationships = klassModel.getRelationships();
      for (IKlassNatureRelationship klassNatureRelationship : relationships) {
        // Repair Relationship
        String rCode = klassNatureRelationship.getCode();
        if (StringUtils.isEmpty(rCode)) {
          rCode = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix());
        }
        
        klassNatureRelationship.setCode(rCode);
        klassNatureRelationship.setId(rCode);
        
        // Repair Side1
        IRelationshipSide side1 = klassNatureRelationship.getSide1();
        String side1Code = side1.getCode();
        if (StringUtils.isEmpty(rCode)) {
          side1Code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP_SIDE.getPrefix());
        }
        side1.setCode(side1Code);
        side1.setId(side1Code);
        
        if (externalId == null || externalId.equals(side1.getKlassId())) {
          side1.setKlassId(klassModel.getCode());
        }
        
        // Repair Side1
        IRelationshipSide side2 = klassNatureRelationship.getSide2();
        String side2Code = side2.getCode();
        if (StringUtils.isEmpty(rCode)) {
          side2Code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP_SIDE.getPrefix());
        }
        side2.setCode(side2Code);
        side2.setId(side2Code);
        
		/*
		 	* This needs to be done as in case of relationships like
		 	* fixedBundleRelationship, side1 klass and side2 klass is the same which is
			* currently being created
		*/
        
        if (externalId != null && externalId.equals(side2.getKlassId())) {
            side2.setKlassId(klassModel.getCode());
         }
      }
    }
  }
}
