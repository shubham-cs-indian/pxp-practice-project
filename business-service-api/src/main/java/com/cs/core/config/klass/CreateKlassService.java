package com.cs.core.config.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.entity.klass.IKlassNatureRelationship;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.strategy.usecase.klass.ICreateKlassStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class CreateKlassService extends AbstractCreateKlassService<IKlassModel, IGetKlassEntityWithoutKPModel>
    implements ICreateKlassService {
  
  @Autowired
  protected ICreateKlassStrategy createKlassStrategy;
  
  @Override
  public IGetKlassEntityWithoutKPModel executeCreateKlass(IKlassModel klassModel) throws Exception
  {
    if (klassModel.getRelationships() != null && klassModel.getRelationships()
        .size() > 0) {
      IKlassNatureRelationship natureRelationship = klassModel.getRelationships()
          .get(0);
      String code = (natureRelationship.getCode() == null || natureRelationship.getCode()
          .trim()
          .equals("")) ? "code_" + natureRelationship.getId() : natureRelationship.getCode();
      // 'code_' suffix added because PXON expression break in RDBMS
      IPropertyDTO createProperty = RDBMSUtils.createProperty(natureRelationship.getId(), code,
          PropertyType.NATURE_RELATIONSHIP);
      natureRelationship.setCode(code);
      natureRelationship.setPropertyIID(createProperty.getPropertyIID());
    }
    return createKlassStrategy.execute(klassModel);
  }
}
