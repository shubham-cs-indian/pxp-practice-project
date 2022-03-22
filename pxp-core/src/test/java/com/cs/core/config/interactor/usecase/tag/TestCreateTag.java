/*package com.cs.core.config.interactor.usecase.tag;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.cs.core.config.interactor.entity.configuration.base.ITreeEntity;
import com.cs.core.config.interactor.model.tag.ITagModel;
import com.cs.core.config.strategy.usecase.tag.ICreateTagStrategy;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;

public class TestCreateTag {

  private static final String DUMMY_CODE = "dummyCode";

  private static final String DUMMY_PROPERTY_ID = "dummyPropertyId";

  @Rule
  public MockitoRule rule =  MockitoJUnit.rule();

  @Mock
  public ICreateTagStrategy         createTagStrategy;

  @Mock
  public IConfigurationDAO configurationDAO;

  @Mock
  public ITreeEntity parent;

  @Mock
  public ITagModel tagModel ;

  @Test
  public void testCreatePropertyRecordWithPropertyIDandCode() throws Exception
  {

    when(tagModel.getId()).thenReturn(DUMMY_PROPERTY_ID);
    when(tagModel.getCode()).thenReturn(DUMMY_CODE);
    when(configurationDAO.createProperty(DUMMY_PROPERTY_ID, DUMMY_CODE, PropertyType.TAG)).thenReturn(mock(PropertyDTO.class));
    CreateTag createTag = new CreateTag();
    //createTag.createRDBMSConfig(tagModel, configurationDAO);

    verify(configurationDAO, times(1)).createProperty(DUMMY_PROPERTY_ID, DUMMY_CODE,PropertyType.TAG );

  }

  @Test
  public void testCreatePropertyRecordWithPropertyIDandNullCode() throws Exception
  {

    when(tagModel.getId()).thenReturn(DUMMY_PROPERTY_ID);
    when(tagModel.getCode()).thenReturn(null);
    when(configurationDAO.createProperty(DUMMY_PROPERTY_ID, DUMMY_PROPERTY_ID, PropertyType.TAG)).thenReturn(mock(PropertyDTO.class));
    CreateTag createTag = new CreateTag();
    //createTag.createRDBMSConfig(tagModel, configurationDAO);
    verify(configurationDAO, times(1)).createProperty(DUMMY_PROPERTY_ID, DUMMY_PROPERTY_ID,PropertyType.TAG );
  }

  @Test
  public void testCreateTagValue() throws Exception
  {
    when(tagModel.getParent()).thenReturn(parent);
    when(parent.getId()).thenReturn("dummyParentId");
    when(tagModel.getId()).thenReturn(DUMMY_PROPERTY_ID);
    //when(configurationDAO.createTagValue(DUMMY_PROPERTY_ID, "dummyParentId")).thenReturn(mock(TagValueDTO.class));
    CreateTag createTag = new CreateTag();
    //createTag.createRDBMSConfig(tagModel, configurationDAO);
    //verify(configurationDAO, times(1)).createTagValue(DUMMY_PROPERTY_ID, "dummyParentId");
  }

}

*/
