/*package com.cs.postgre.article.create;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.attribute.TextAttribute;
import com.cs.core.config.interactor.model.klass.IReferencedSectionAttributeModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.rdbms.entity.idto.IPropertyRecordDTO;
import com.cs.core.rdbms.entity.idto.IValueRecordDTO;
import com.cs.core.runtime.interactor.constants.application.CommonConstants;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.pim.runtime.interactor.usecase.articleinstance.ICreateArticleInstance;
import com.cs.pim.runtime.strategy.usecase.articleinstance.CreateArticleInstance;

public class CreateArticleInstanceTest {

  @Autowired
  ICreateArticleInstance createArticleInstance;

  @Mock
  IBaseEntityDAO baseEntityDAO;

  @Mock
  IGetConfigDetailsForCustomTabModel configDetails;

  Map<String, IAttribute> referencedAttributes = new HashMap<String, IAttribute>();

  Map<String, IReferencedSectionElementModel> referencedSectionElements = new HashMap<String, IReferencedSectionElementModel>();


  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void createPropertyRecordsTest() throws Exception{

    this.mockBadeEntityDAO();
    this.mockReferencedAttributes();
    this.mockTextReferencedAttribute();
    this.mockReferencedSectionElements();
    this.mockTextReferencedSectionElement();

    CreateArticleInstance mCreateArticleInstance= new CreateArticleInstance();

    IPropertyRecordDTO[] propertRecordArray = mCreateArticleInstance.createPropertyRecords(this.baseEntityDAO, this.configDetails, null);

    verify(propertRecordArray[0]).setDefaultValue("text_attribute");
    verify(propertRecordArray[0]).setIsDefaultValue(true);
    verify(propertRecordArray[0]).setPropertyType(PropertyType.TEXT);
    verify(propertRecordArray[0]).setPropertyID("84a34b55-b9fc-4ab7-853e-417281f15bc9");
    verify(propertRecordArray[0]).setVersion(0);
    verify((IValueRecordDTO)propertRecordArray[0]).setLocaleID("en_US");
  }

  //Add BaseEntityDao
  private void mockBadeEntityDAO() {
    when(baseEntityDAO.newValueRecordDTO()).thenReturn(mock(IValueRecordDTO.class));
  }

  //Add Reference Attributes
  private void mockReferencedAttributes() {
    when(configDetails.getReferencedAttributes()).thenReturn(referencedAttributes);
  }

  //Text Referenced Attribute
  private void mockTextReferencedAttribute() {
    IAttribute textAttribute = mock(IAttribute.class);
    when(textAttribute.getId()).thenReturn("84a34b55-b9fc-4ab7-853e-417281f15bc9");
    when(textAttribute.getType()).thenReturn(TextAttribute.class.getName());

    referencedAttributes.put(textAttribute.getId(), textAttribute);
  }

  //Add Referenced Section Elements
  private void mockReferencedSectionElements() {
    when(configDetails.getReferencedElements()).thenReturn(referencedSectionElements);
  }

  //Text Section Attribute
  private void mockTextReferencedSectionElement() {

    IReferencedSectionElementModel textReferencedSectionElementModel = mock(IReferencedSectionElementModel.class, withSettings().extraInterfaces(IReferencedSectionAttributeModel.class));
    when(textReferencedSectionElementModel.getId()).thenReturn("84a34b55-b9fc-4ab7-853e-417281f15bc9");
    when(textReferencedSectionElementModel.getType()).thenReturn(CommonConstants.ATTRIBUTE);
    when(textReferencedSectionElementModel.getPropertyId()).thenReturn("84a34b55-b9fc-4ab7-853e-417281f15bc9");
    when(((IReferencedSectionAttributeModel)textReferencedSectionElementModel).getDefaultValue()).thenReturn("text_attribute");

    referencedSectionElements.put(textReferencedSectionElementModel.getId(), textReferencedSectionElementModel);
  }



}
*/
