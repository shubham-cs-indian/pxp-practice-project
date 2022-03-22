package com.cs.core.config.relationship;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractCreateConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.relationship.ICreateRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetRelationshipModel;
import com.cs.core.config.strategy.usecase.relationship.ICreateRelationshipStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateRelationshipService extends AbstractCreateConfigService<ICreateRelationshipModel, IGetRelationshipModel>
    implements ICreateRelationshipService {
  
  @Autowired
  protected ICreateRelationshipStrategy createRelationshipStrategy;
  
  @Override
  public IGetRelationshipModel executeInternal(ICreateRelationshipModel dataModel) throws Exception
  {
    String code = dataModel.getCode();
    if (StringUtils.isEmpty(code)) {
      code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP.getPrefix());
    }
    
    dataModel.setCode(code);
    dataModel.setId(code);
    RelationshipValidations.validateCreate(dataModel);
    IRelationshipSide side1 = dataModel.getSide1();
    String side1Id = side1.getCode();
    if (isEmpty(side1Id)) {
      side1Id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP_SIDE.getPrefix());
      side1.setId(side1Id);
      side1.setCode(side1Id);
      dataModel.setSide1(side1);
    }
    
    IRelationshipSide side2 = dataModel.getSide2();
    String side2Id = side2.getCode();
    if (isEmpty(side2Id)) {
      side2Id = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.RELATIONSHIP_SIDE.getPrefix());
      side2.setId(side2Id);
      side2.setCode(side2Id);
      dataModel.setSide2(side2);
    }
    IPropertyDTO relationshipProperty = RDBMSUtils.createProperty(dataModel.getCode(),
        this.isEmpty(dataModel.getCode()) ? dataModel.getId() : dataModel.getCode(), PropertyType.RELATIONSHIP);
    dataModel.setPropertyIID(relationshipProperty.getIID());
    return createRelationshipStrategy.execute(dataModel);
  }
  
  private boolean isEmpty(String value)
  {
    return value == null || value.equals("");
  }
}
