package com.cs.core.rdbms.config.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.actions.CSEPropertyAssignment;
import com.cs.core.csexpress.actions.CSEPropertyQualityFlag;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.ConfigRuleDTO;
import com.cs.core.rdbms.config.dto.ContextDTO;
import com.cs.core.rdbms.config.dto.LanguageConfigDTO;
import com.cs.core.rdbms.config.dto.PropertyDTO;
import com.cs.core.rdbms.config.dto.RuleExpressionDTO;
import com.cs.core.rdbms.config.dto.TagValueDTO;
import com.cs.core.rdbms.config.dto.TaskDTO;
import com.cs.core.rdbms.config.idao.IConfigurationDAO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.config.idto.IConfigRuleDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.IContextDTO.ContextType;
import com.cs.core.rdbms.config.idto.ILanguageConfigDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.rdbms.config.idto.IPropertyDTO.PropertyType;
import com.cs.core.rdbms.config.idto.IRootConfigDTO;
import com.cs.core.rdbms.config.idto.IRuleExpressionDTO;
import com.cs.core.rdbms.config.idto.ITagValueDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO;
import com.cs.core.rdbms.config.idto.ITaskDTO.TaskType;
import com.cs.core.rdbms.driver.RDBMSConnection;
import com.cs.core.rdbms.driver.RDBMSConnectionManager;
import com.cs.core.rdbms.driver.RDBMSLogger;
import com.cs.core.rdbms.dto.UserDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO.BaseType;
import com.cs.core.rdbms.entity.idto.IEntityRelationDTO;
import com.cs.core.rdbms.entity.idto.ITagDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ParameterType;
import com.cs.core.rdbms.function.RDBMSAbstractFunction.ResultType;
import com.cs.core.rdbms.process.dao.RuleCatalogDAS;
import com.cs.core.rdbms.services.resolvers.DynamicViewDAS;
import com.cs.core.services.CSCache;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.actions.ICSEAction;
import com.cs.core.technical.icsexpress.actions.ICSEActionList;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyAssignment;
import com.cs.core.technical.icsexpress.actions.ICSEPropertyQualityFlag;
import com.cs.core.technical.icsexpress.calculation.ICSECalculationNode;
import com.cs.core.technical.icsexpress.calculation.ICSERecordOperand;
import com.cs.core.technical.icsexpress.rule.ICSERule;
import com.cs.core.technical.icsexpress.rule.ICSERule.RuleType;
import com.cs.core.technical.icsexpress.scope.ICSEScope;
import com.cs.core.technical.rdbms.exception.RDBMSException;
import com.cs.core.technical.rdbms.idto.IUserDTO;

/**
 * Provided Configuration Create Implementation for configuration entities like
 * Locale,Context,Property,TagValue etc
 *
 * @author PankajGajjar
 */
public class ConfigurationDAO implements IConfigurationDAO {
  
  private static final String     Q_SELECT_CODES     = "Select %s as code from pxp.%s where %s like ? order by code desc";
  private static final String     Q_PROPERTY         = "select propertyIID, propertyCode, propertyType "
      + "from pxp.propertyConfig ";
  private static final String     TAG_PROPERTY         = "select propertyIID, tagvaluecode "
      + "from pxp.tagValueConfig ";
  private static final String     W_IID              = "where propertyIID = ? ";
  private static final String     W_CODE             = "where propertyCode = ? ";
  private static final String     W_TAG              = "and superType = "
      + IPropertyDTO.SuperType.TAGS.ordinal();
  
  private static final String     Q_CLASSIFIER       = "select * from pxp.ClassifierConfig ";
  private static final String     Q_CLASSIFIER_W_CODES = Q_CLASSIFIER + " where classifierCode in ";

  private static final String     Q_USER       = "select * from pxp.userConfig ";
  private static final String     Q_PROPERTY_W_CODES = Q_PROPERTY + " where propertyCode in ";
  private static final String     Q_PROPERTY_W_IID = Q_PROPERTY + " where propertyIID in (%s)";
  private static final String     Q_RULE_CONFIG      = "select * from pxp.ruleconfig rc join pxp.ruleexpression re on rc.rulecode = re.rulecode";
  // Singleton implementation
  private static ConfigurationDAO INSTANCE           = null;
  
  private static final String     Q_NAURE_CLASS             = " select b.baseentityiid from pxp.baseentity b where b.classifieriid in( %s ) and b.ismerged != true and baseType != "+ BaseType.UNDEFINED.ordinal();
  private static final String     Q_NONNATURE_CLASS         = " select bc.baseentityiid from pxp.baseentityclassifierlink bc"
      + " join pxp.baseentity be on  bc.baseentityiid = be.baseentityiid and bc.otherclassifieriid in( %s ) and be.ismerged != true and be.baseType != " + BaseType.UNDEFINED.ordinal();
  private static final String     Q_TAXONOMY                = " select be.baseentityiid from pxp.baseentity be join"
      + " pxp.baseentityclassifierlink becl on be.baseentityiid = becl.baseentityiid and be.ismerged != true and be.baseType != "+ BaseType.UNDEFINED.ordinal()
      + " join pxp.classifierconfig cc on becl.otherclassifieriid = cc.classifieriid and  hierarchyiids && '{ %s }'";
  private static final String     Q_LANGUAGE     = " select be.baseentityiid from pxp.baseentity be join " + 
      "pxp.baseentitylocaleidlink becl on be.baseentityiid = becl.baseentityiid and be.ismerged != true and becl.localeid in ( %s )"; 
  
  private static final String     REMOVE_CONTEXTUAL_OBJECTS     = "Delete from pxp.contextualobject where contextcode in ( %s )";
  private static final String     Q_LANGUAGE_CONFIG         = "select * from pxp.languageConfig where languagecode = ?";
  private static final String     Q_LANGUAGE_CONFIG_BY_LANGUAGE_IID         = "select * from pxp.languageConfig where languageiid = ?";
  private static final String     Q_CHILD_LANGUAGES_BY_LANGUAGE_IID         = "select * from pxp.languageConfig where ? = ANY(parentIIds)";
  private static final String     Q_DELETE_LANGUAGE_CONFIG_BY_CODES  = "delete from pxp.languageConfig where languagecode in ( %s )";
  private static final String     Q_GET_CLASSIFIER_IIDS_FROM_CODES  = "SELECT classifieriid FROM pxp.classifierconfig WHERE classifiercode IN (%s)";
  private static final String     Q_ORGANIZATION            = "SELECT DISTINCT b.organizationcode FROM pxp.baseentity b where b.organizationcode in ( %s )";

  /**
   * Load in the cache default tracking attributes
   */
  private ConfigurationDAO()
  {
  }
  
  public static ConfigurationDAO instance()
  {
    
    if (INSTANCE == null) {
      synchronized (ConfigurationDAO.class) {
        if (INSTANCE == null) {
          INSTANCE = new ConfigurationDAO();
        }
      }
    }
    return INSTANCE;
  }
  
  @Override
  public <T extends IRootConfigDTO> String getCode(Class<T> configType, String name)
      throws RDBMSException
  {
    String camelCaseCode = Text.toCode(name);
    String tableName = configType.getSimpleName()
        .replaceAll("^I", "")
        .replaceAll("DTO$", "Config");
    String codeColumn = tableName.replaceAll("Config$", "Code");
    String[] similarCode = { "" };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement query = currentConn
              .prepareStatement(String.format(Q_SELECT_CODES, codeColumn, tableName, codeColumn));
          query.setString(1, camelCaseCode + "%");
          ResultSet rs = query.executeQuery();
          if (rs.next()) {
            similarCode[0] = rs.getString("code");
          }
        });
    if (similarCode[0].isEmpty()) {
      return camelCaseCode;
    }
    return Text.getNextCode(similarCode[0]);
  }
  
  @Override
  public IUserDTO createUser(String userName) throws RDBMSException
  {
    String ccode = UserDTO.getCacheCode(userName);
    /*Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (UserDTO) CSCache.instance().get(ccode);
    }*/
    Long[] userIID = new Long[1];
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_userConfig")
              .setInput(ParameterType.STRING, userName)
              .execute();
          userIID[0] = result.getIID();
        });
    UserDTO newUser = new UserDTO(userIID[0], userName);
    CSCache.instance().keep(ccode, newUser);
    return newUser;
  }
  
  @Override
  public IUserDTO createStandardUser(long userIID, String userName) throws RDBMSException
  {
    String ccode = UserDTO.getCacheCode(userName);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (UserDTO) CSCache.instance().get(ccode);
    }*/
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      currentConn.getFunction( ResultType.VOID, "pxp.sp_standardUserConfig")
          .setInput(ParameterType.IID, userIID)
          .setInput(ParameterType.STRING, userName)
          .execute();
    });
    UserDTO newUser = new UserDTO(userIID, userName);
    CSCache.instance().keep(ccode, newUser);
    return newUser;
  }
  
  @Override
  public IContextDTO createContext(String contextCode, ContextType contextType)
      throws RDBMSException
  {
    String ccode = ContextDTO.getCacheCode(contextCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (ContextDTO) CSCache.instance().get(ccode);
    }*/
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          currentConn.getProcedure( "pxp.sp_contextConfig")
              .setInput(ParameterType.STRING, contextCode)
              .setInput(ParameterType.INT, contextType.ordinal())
              .execute();
        });
    ContextDTO newContext = new ContextDTO(contextCode, contextType);
    CSCache.instance().keep(ccode, newContext);
    return newContext;
  }
  
  @Override
  public IContextDTO getContextByCode(String contextCode) throws RDBMSException
  {
    if (CSCache.instance()
        .isKept(ContextDTO.getCacheCode(contextCode))) {
      return (IContextDTO) CSCache.instance().get(ContextDTO.getCacheCode(contextCode));
    }
    ContextDTO[] context = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn
              .prepareStatement("select * from ContextConfig where contextCode = ?");
          stmt.setString(1, contextCode);
          IResultSetParser propertyResult = currentConn.getResultSetParser(stmt.executeQuery());
          if (!propertyResult.next()) {
            throw new RDBMSException(100, "Inconsistent Configuration",
                "Context Code " + contextCode + " not found");
          }
          context[0] = new ContextDTO(propertyResult);
        });
    String ccode = ContextDTO.getCacheCode(context[0].getContextCode());
    if (!CSCache.instance().isKept(ccode)) {
      CSCache.instance().keep(ccode, context[0]);
    }
    return context[0];
  }
  
  @Override
  public IPropertyDTO createProperty(String propertyCode, PropertyType propertyType)
      throws RDBMSException
  {
    String ccode = PropertyDTO.getCacheCode(propertyCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (PropertyDTO) CSCache.instance().get(ccode);
    }*/
    Long[] propertyIID = new Long[1];
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_propertyConfig")
              .setInput(ParameterType.STRING, propertyCode)
              .setInput(ParameterType.INT, propertyType.ordinal())
              .setInput(ParameterType.INT, propertyType.getSuperType().ordinal())
              .execute();
          propertyIID[0] = result.getIID();
        });
    PropertyDTO newProperty = new PropertyDTO(propertyIID[0], propertyCode, propertyType);
    CSCache.instance()
        .keep(ccode, newProperty);
    CSCache.instance()
        .keep(PropertyDTO.getCacheCode(propertyIID[0]), newProperty);
    return newProperty;
  }
  
  @Override
  public IPropertyDTO createStandardProperty(long propertyIID, String propertyCode, PropertyType propertyType) throws RDBMSException
  {
    String ccode = UserDTO.getCacheCode(propertyCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (PropertyDTO) CSCache.instance().get(ccode);
    }*/
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      currentConn.getFunction(ResultType.VOID, "pxp.sp_standardPropertyConfig").setInput(ParameterType.IID, propertyIID)
          .setInput(ParameterType.STRING, propertyCode).setInput(ParameterType.INT, propertyType.ordinal())
          .setInput(ParameterType.INT, propertyType.getSuperType().ordinal()).execute();
    });
    PropertyDTO newProperty = new PropertyDTO(propertyIID, propertyCode, propertyType);
    CSCache.instance().keep(ccode, newProperty);
    CSCache.instance().keep(PropertyDTO.getCacheCode(propertyIID), newProperty);
    
    return newProperty;
  }
  
  @Override
  public IPropertyDTO getPropertyByIID(long propertyIID) throws RDBMSException
  {
    if (CSCache.instance().isKept(PropertyDTO.getCacheCode(propertyIID))) {
      return (IPropertyDTO) CSCache.instance().get(PropertyDTO.getCacheCode(propertyIID));
    }
    PropertyDTO[] property = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(Q_PROPERTY + W_IID);
          stmt.setLong(1, propertyIID);
          IResultSetParser propertyResult = currentConn.getResultSetParser(stmt.executeQuery());
          if (!propertyResult.next()) {
            throw new RDBMSException(100, "Inconsistent Configuration", "propertyIID " + propertyIID + " not found");
          }
          property[0] = new PropertyDTO(propertyResult);
        });
    String ccode = PropertyDTO.getCacheCode(property[0].getPropertyCode());
    if (!CSCache.instance().isKept(ccode)) {
      CSCache.instance().keep(ccode, property[0]);
      CSCache.instance().keep(PropertyDTO.getCacheCode(propertyIID), property[0]);
    }
    return property[0];
  }
  
  /**
   * Retrieve a property by code from a current transaction
   * @param currentConn
   * @param propertyCode
   * @return
   * @throws RDBMSException 
   * @throws java.sql.SQLException 
   */
  public PropertyDTO getPropertyByCode(
          RDBMSConnection currentConn, String propertyCode) throws RDBMSException,SQLException
  {
    if (CSCache.instance().isKept(PropertyDTO.getCacheCode(propertyCode))) {
      return (PropertyDTO) CSCache.instance().get(PropertyDTO.getCacheCode(propertyCode));
    }
    PreparedStatement stmt = currentConn.prepareStatement(Q_PROPERTY + " where propertyCode = ?");
    stmt.setString(1, propertyCode);
    IResultSetParser propertyResult = currentConn.getResultSetParser(stmt.executeQuery());
    if (!propertyResult.next()) {
      throw new RDBMSException(100, "Inconsistent Configuration",
          "propertyCode " + propertyCode + " not found");
    }
    PropertyDTO property = new PropertyDTO(propertyResult);
    String ccode = PropertyDTO.getCacheCode(property.getPropertyCode());
    if (!CSCache.instance().isKept(ccode)) {
      CSCache.instance().keep(ccode, property);
      CSCache.instance().keep(PropertyDTO.getCacheCode(property.getPropertyIID()), property);
    }
    return property;
  }

  @Override
  public IPropertyDTO getPropertyByCode(String propertyCode) throws RDBMSException
  {
    if (CSCache.instance().isKept(PropertyDTO.getCacheCode(propertyCode))) {
      return (PropertyDTO) CSCache.instance().get(PropertyDTO.getCacheCode(propertyCode));
    }
    PropertyDTO[] property = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          property[0] = getPropertyByCode( currentConn, propertyCode);
        });
    return property[0];
  }
  
  @Override
  public ITagValueDTO createTagValue(String tagValueCode, long propertyIID) throws RDBMSException
  {
    String ccode = TagValueDTO.getCacheCode(tagValueCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (TagValueDTO) CSCache.instance().get(ccode);
    }*/
    Long[] refPropertyIID = { 0L };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          if (propertyIID >= 0) {
            PreparedStatement stmt = currentConn.prepareStatement(Q_PROPERTY + W_IID + W_TAG);
            stmt.setLong(1, propertyIID);
            stmt.execute();
            IResultSetParser propertyResult = currentConn.getResultSetParser(stmt.getResultSet());
            if (!propertyResult.next()) {
              throw new RDBMSException(100, "Inconsistent Configuration",
                  "Tag property with propertyIID " + propertyIID + " not found");
            }
          }
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_tagValueConfig")
              .setInput(ParameterType.STRING, tagValueCode)
              .setInput(ParameterType.LONG, propertyIID)
              .execute();
          refPropertyIID[0] = result.getIID();
        });
    TagValueDTO newTagValue = new TagValueDTO(tagValueCode, refPropertyIID[0]);
    CSCache.instance()
        .keep(ccode, newTagValue);
    return newTagValue;
  }
  
  @Override
  public ITagValueDTO  createTagValue(String tagValueCode, String propertyCode) throws RDBMSException
  {
    String ccode = TagValueDTO.getCacheCode(tagValueCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (TagValueDTO) CSCache.instance().get(ccode);
    }*/
    Long[] propertyIID = { 0L };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          if (propertyCode != null && !propertyCode.isEmpty()) {
            PreparedStatement stmt = currentConn.prepareStatement(Q_PROPERTY + W_CODE + W_TAG);
            stmt.setString(1, propertyCode);
            stmt.execute();
            IResultSetParser result = currentConn.getResultSetParser(stmt.getResultSet());
            if (!result.next()) {
              throw new RDBMSException(100, "Inconsistent Configuration",
                  "Tag property with propertyCode " + propertyCode + " not found");
            }
            propertyIID[0] = result.getLong("propertyIID");
          }
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_tagValueConfig")
              .setInput(ParameterType.STRING, tagValueCode)
              .setInput(ParameterType.LONG, propertyIID[0])
              .execute();
          propertyIID[0] = result.getIID();
        });
    TagValueDTO newTagValue = new TagValueDTO(tagValueCode, propertyIID[0]);
    CSCache.instance().keep(ccode, newTagValue);
    return newTagValue;
  }
  
  @Override
  public IClassifierDTO createClassifier(String classifierCode, ClassifierType classifierType)
      throws RDBMSException
  {
    String ccode = ClassifierDTO.getCacheCode(classifierCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (ClassifierDTO) CSCache.instance().get(ccode);
    }*/
    Long[] classifierIID = new Long[1];
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_classifierConfig")
              .setInput(ParameterType.STRING, classifierCode)
              .setInput(ParameterType.INT, classifierType.ordinal())
              .execute();
          classifierIID[0] = result.getLong();
        });
    ClassifierDTO newClassifier = new ClassifierDTO(classifierIID[0], classifierCode, classifierType);
//    CSCache.instance().keep(ccode, newClassifier);
//    CSCache.instance().keep(ClassifierDTO.getCacheCode(classifierIID[0]), newClassifier);
    return newClassifier;
  }
  
  @Override
  public IClassifierDTO createStandardClassifier(long classifierIID, String classifierCode, ClassifierType classifierType)
      throws RDBMSException
  {
    String ccode = ClassifierDTO.getCacheCode(classifierCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (ClassifierDTO) CSCache.instance().get(ccode);
    }*/
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      currentConn.getFunction(ResultType.VOID, "pxp.sp_standardClassifierConfig").setInput(ParameterType.IID, classifierIID)
          .setInput(ParameterType.STRING, classifierCode).setInput(ParameterType.INT, classifierType.ordinal()).execute();
    });
    ClassifierDTO newClassifier = new ClassifierDTO(classifierIID, classifierCode, classifierType);
    CSCache.instance().keep(ccode, newClassifier);
    CSCache.instance().keep(ClassifierDTO.getCacheCode(classifierIID), newClassifier);
    return newClassifier;
  }

	@Override
	public List<Long> getBaseentityIIDsForTypes(List<Long> klassIds)
			throws RDBMSException {
		List<Long> baseEntityIIDs = new ArrayList<Long>();

		RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
			StringBuffer query = new StringBuffer();
			query.append(String.format(Q_NAURE_CLASS, Text.join(",", klassIds, "'%s'")));
			query.append(" union");
			query.append(String.format(Q_NONNATURE_CLASS, Text.join(",", klassIds, "'%s'")));

			PreparedStatement stmt = currentConn.prepareStatement(query.toString());
			stmt.execute();
			IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
			while (resultSet.next()) {
				baseEntityIIDs.add(resultSet.getLong("baseentityiid"));
			}
		});
		return baseEntityIIDs;
	}

	@Override
	public List<Long> getBaseentityIIDsForTaxonomies(List<Long> classifierIIDs) throws RDBMSException {
		List<Long> baseEntityIIDs = new ArrayList<Long>();
		 
		RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
			StringBuilder query = new StringBuilder();
			query.append(String.format(Q_TAXONOMY, Text.join(",", classifierIIDs, "\" %s \"")));
			
			PreparedStatement stmt = currentConn.prepareStatement(query.toString());
			stmt.execute();
			IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
			while (resultSet.next()) {
				baseEntityIIDs.add(resultSet.getLong("baseentityiid"));
			}
		});
		return baseEntityIIDs;
	}
	
  @Override
  public List<Long> getBaseentityIIDsForLanguage(List<String> languageIds) throws RDBMSException
  {
    
    List<Long> baseEntityIIDs = new ArrayList<Long>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuffer query = new StringBuffer();
      query.append(String.format(Q_LANGUAGE, Text.join(",", languageIds, "'%s'")));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        baseEntityIIDs.add(resultSet.getLong("baseentityiid"));
      }
      
    });
    return baseEntityIIDs;
    
  }
	
  @Override
  public IClassifierDTO createTaxonomyClassifier(String classifierCode, ClassifierType classifierType, List<Long> parentIIDs) throws RDBMSException
  {
    String ccode = ClassifierDTO.getCacheCode(classifierCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (ClassifierDTO) CSCache.instance().get(ccode);
    }*/
    Long[] classifierIID = new Long[1];
    RDBMSConnectionManager.instance()
        .runTransaction(( RDBMSConnection currentConn) -> {
          IResultSetParser result = currentConn.getFunction( ResultType.IID, "pxp.fn_createTaxonomyClassifier")
              .setInput(ParameterType.STRING, classifierCode)
              .setInput(ParameterType.INT, classifierType.ordinal())
              .setInput(ParameterType.IID_ARRAY, parentIIDs)
              .execute();
          classifierIID[0] = result.getLong();
        });
    ClassifierDTO newClassifier = new ClassifierDTO(classifierIID[0], classifierCode, classifierType , parentIIDs);
    CSCache.instance().keep(ccode, newClassifier);
    CSCache.instance().keep(ClassifierDTO.getCacheCode(classifierIID[0]), newClassifier);
    return newClassifier;
  }

  @Override
  public IClassifierDTO getClassifierByIID(long classifierIID) throws RDBMSException
  {
    if (CSCache.instance().isKept(ClassifierDTO.getCacheCode(classifierIID))) {
      return (IClassifierDTO) CSCache.instance().get(ClassifierDTO.getCacheCode(classifierIID));
    }
    ClassifierDTO[] classifier = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn
              .prepareStatement(Q_CLASSIFIER + " where classifierIID = ?");
          stmt.setLong(1, classifierIID);
          stmt.execute();
          IResultSetParser classifierResult = currentConn.getResultSetParser(stmt.getResultSet());
          if (!classifierResult.next()) {
            throw new RDBMSException(100, "Inconsistent Configuration",
                "Classifier with classifierIID " + classifierIID + " not found");
          }
          classifier[0] = new ClassifierDTO(classifierResult);
        });
    String ccode = ClassifierDTO.getCacheCode(classifier[0].getClassifierCode());
    if (!CSCache.instance().isKept(ccode)) {
      CSCache.instance().keep(ccode, classifier[0]);
      CSCache.instance().keep(ClassifierDTO.getCacheCode(classifierIID), classifier[0]);
    }
    return classifier[0];
  }
  
  /**
   * Search for classifier within an existing connection
   * @param currentConn
   * @param classifierCode
   * @return the corresponding classifier DTO
   * @throws RDBMSException 
   * @throws java.sql.SQLException 
   */
  
  public ClassifierDTO getClassifierByCode(  RDBMSConnection currentConn,
          String classifierCode) throws RDBMSException, SQLException {
    /*if (CSCache.instance().isKept(ClassifierDTO.getCacheCode(classifierCode))) {
      return (ClassifierDTO) CSCache.instance().get(ClassifierDTO.getCacheCode(classifierCode));
    }*/
    PreparedStatement stmt = currentConn.prepareStatement(Q_CLASSIFIER + " where classifierCode = ?");
    stmt.setString(1, classifierCode);
    stmt.execute();
    IResultSetParser classifierResult = currentConn.getResultSetParser(stmt.getResultSet());
    if (!classifierResult.next()) {
      throw new RDBMSException(100, "Inconsistent Configuration",
          "Classifier with classifierCode " + classifierCode + " not found");
    }
    ClassifierDTO classifier = new ClassifierDTO(classifierResult);
    String ccode = ClassifierDTO.getCacheCode(classifier.getClassifierCode());
    /*if (!CSCache.instance().isKept(ccode)) {
      CSCache.instance().keep(ccode, classifier);
      CSCache.instance().keep(ClassifierDTO.getCacheCode(classifier.getClassifierIID()), classifier);
    }*/
    return classifier;
  }

  public ClassifierDTO doesClassifierExist(String classifierCode) throws RDBMSException
  {
    ClassifierDTO[] classifier = { null };
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(Q_CLASSIFIER + " where classifierCode = ?");
      stmt.setString(1, classifierCode);
      stmt.execute();
      IResultSetParser classifierResult = currentConn.getResultSetParser(stmt.getResultSet());
      if (classifierResult.next()) {
        classifier[0] = new ClassifierDTO(classifierResult);
      }
    });
    return classifier[0];
  }
          
  @Override
  public IClassifierDTO getClassifierByCode(String classifierCode) throws RDBMSException
  {

    /* if (CSCache.instance().isKept(ClassifierDTO.getCacheCode(classifierCode))) {
      return (ClassifierDTO) CSCache.instance().get(ClassifierDTO.getCacheCode(classifierCode));
    }*/
    ClassifierDTO[] classifier = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          classifier[0] = getClassifierByCode( currentConn, classifierCode);
        });
    return classifier[0];
  }
  
  @Override
  public IRuleExpressionDTO upsertRule(String code, long ruleExpressionIID, String ruleExpression, RuleType ruleType) throws RDBMSException, CSFormatException
  {
    IRuleExpressionDTO[] rule = { null };
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          
          if (StringUtils.isEmpty(ruleExpression)) {
            RuleCatalogDAS ruleDAS = new RuleCatalogDAS(currentConn);
            ruleDAS.deleteExpression(ruleExpressionIID, ruleType);
          }
          else {
            ICSERule parseRule = (new CSEParser()).parseRule(ruleExpression);
            ICSECalculationNode evaluation = parseRule.getEvaluation();
            Set<Long> whenPropertyIIds = getWhenPropertyIIds(evaluation, currentConn);
            
            ICSEActionList actions = parseRule.getActionList();

            Set<Long> forPropertyIIds = getForPropertyIIds(actions, currentConn);
            
            ICSEScope scope = parseRule.getScope();
            IResultSetParser resultParser = currentConn.getFunction(ResultType.IID, "pxp.sp_ruleConfig")
                .setInput(ParameterType.IID, ruleExpressionIID)
                .setInput(ParameterType.STRING, code)
                .setInput(ParameterType.INT, ruleType.ordinal())
                .setInput(ParameterType.STRING, ruleExpression)
                .setInput(ParameterType.STRING_ARRAY, scope.getLocaleIDs())
                .setInput(ParameterType.STRING_ARRAY, scope.getCatalogCodes())
                .setInput(ParameterType.STRING_ARRAY, scope.getOrganizationCodes())
                .setInput(ParameterType.IID_ARRAY, forPropertyIIds)
                .setInput(ParameterType.IID_ARRAY, whenPropertyIIds)
                .execute();
            
            rule[0] = new RuleExpressionDTO(resultParser.getIID(), forPropertyIIds, whenPropertyIIds, scope.getLocaleIDs(), scope.getCatalogCodes(),
                null, ruleExpression, code, scope.getOrganizationCodes());
          }
        });
    return rule[0];
  }
  
  private Set<Long> getWhenPropertyIIds(ICSECalculationNode evaluation, RDBMSConnection currentConn)
      throws RDBMSException, SQLException
  {
    Set<Long> whenPropertyIIDS = new HashSet<>();
    if(evaluation == null) {
      return whenPropertyIIDS;
    }
    Set<ICSERecordOperand> operands = evaluation.getRecords();
    Collection<String> whenCodes = new ArrayList<String>();
    
    for (ICSERecordOperand operand : operands) {
      String propertyCode = operand.getProperty().getCode();
      if (CSCache.instance().isKept(propertyCode)) {
        PropertyDTO propertyDTO = (PropertyDTO) CSCache.instance().get(propertyCode);
        whenPropertyIIDS.add(propertyDTO.getIID());
      }
      else {
        whenCodes.add(propertyCode);
      }
    }
    
    whenPropertyIIDS.addAll(getPropertyIIDsFromPropertyCodes(whenCodes));
    return whenPropertyIIDS;
  }
  
  private Set<Long> getForPropertyIIds(ICSEActionList actions, RDBMSConnection currentConn) throws RDBMSException, SQLException
  {
    
    Set<Long> forPropertyIIds = new HashSet<>();
    Collection<String> forPropertyCodes = new ArrayList<>();
    
    for (ICSEAction action : actions) {
      if (action instanceof CSEPropertyAssignment) {
        String propertyCode = ((ICSEPropertyAssignment) action).getProperty().getCode();
        getPropertyIIdFromCache(forPropertyIIds, forPropertyCodes, propertyCode);
      }
      else if (action instanceof CSEPropertyQualityFlag) {
        String propertyCode = ((ICSEPropertyQualityFlag) action).getProperty().getCode();
        getPropertyIIdFromCache(forPropertyIIds, forPropertyCodes, propertyCode);
      }
    }
    List<Long> propertyIIDs = getPropertyIIDsFromPropertyCodes(forPropertyCodes);
    forPropertyIIds.addAll(propertyIIDs);
    return forPropertyIIds;
  }
  
  public List<Long> getPropertyIIDsFromPropertyCodes(Collection<String> propertyCodes) throws RDBMSException, SQLException
  {
    List<Long> propertyIIDs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuffer query = new StringBuffer(Q_PROPERTY_W_CODES);
      if (!propertyCodes.isEmpty()) {
        String inPropertyCodesQuery = propertyCodes.stream().map(propertyCode -> "\'" + propertyCode + "\'")
            .collect(Collectors.joining(",", "(", ")"));
        query.append(inPropertyCodesQuery);
        
        PreparedStatement stmt = currentConn.prepareStatement(query.toString());
        stmt.execute();
        IResultSetParser resultParser = currentConn.getResultSetParser(stmt.getResultSet());
        while (resultParser.next()) {
          long propertyIId = resultParser.getLong("propertyiid");
          String propertyCode = resultParser.getString("propertycode");
          PropertyDTO propertyFromDB = new PropertyDTO(propertyIId, propertyCode,
              PropertyType.valueOf(resultParser.getInt("propertytype")));
          CSCache.instance().keep(PropertyDTO.getCacheCode(propertyCode), propertyFromDB);
          CSCache.instance().keep(PropertyDTO.getCacheCode(propertyIId), propertyFromDB);
          propertyIIDs.add(propertyIId);
        }
      }
    });
    return propertyIIDs;
  }

  public List<IPropertyDTO> getPropertyDTOs(Collection<String> propertyCodes) throws RDBMSException, SQLException
  {
    List<IPropertyDTO> propertyDTOs = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuffer query = new StringBuffer(Q_PROPERTY_W_CODES);
      if (!propertyCodes.isEmpty()) {
        String inPropertyCodesQuery = propertyCodes.stream().map(propertyCode -> "\'" + propertyCode + "\'")
            .collect(Collectors.joining(",", "(", ")"));
        query.append(inPropertyCodesQuery);

        PreparedStatement stmt = currentConn.prepareStatement(query.toString());
        stmt.execute();
        IResultSetParser resultParser = currentConn.getResultSetParser(stmt.getResultSet());
        while (resultParser.next()) {
          propertyDTOs.add(new PropertyDTO(resultParser));
        }
      }
    });
    return propertyDTOs;
  }
  
  private void getPropertyIIdFromCache(Set<Long> forPropertyIIds,
      Collection<String> forPropertyCodes, String propertyCode)
  {
    if (CSCache.instance().isKept(propertyCode)) {
      PropertyDTO propertyDTO = (PropertyDTO) CSCache.instance().get(propertyCode);
      forPropertyIIds.add(propertyDTO.getIID());
    }
    else {
      forPropertyCodes.add(propertyCode);
    }
  }

  public Set<IPropertyDTO> getPropertiesFromCache(Set<Long> propertyIIDs)
  {
    HashSet<IPropertyDTO> iPropertyDTOS = new HashSet<>();
    for (Long propertyIID : propertyIIDs) {
      if (CSCache.instance().isKept(PropertyDTO.getCacheCode(propertyIID))) {
        iPropertyDTOS.add((IPropertyDTO) CSCache.instance().get(PropertyDTO.getCacheCode(propertyIID)));
        propertyIIDs.remove(propertyIID);
      }
    }
    return iPropertyDTOS;
  }

  public Set<IPropertyDTO> getPropertiesFromIIDs(Set<Long> propertyIIDs) throws RDBMSException
  {
    Set<Long> propertyIIDsToMod = new HashSet<>(propertyIIDs);
    Set<IPropertyDTO> propertiesFromCache = getPropertiesFromCache(propertyIIDsToMod);
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      String finalQuery = String.format(Q_PROPERTY_W_IID, Text.join(",",propertyIIDsToMod));

      if (!propertyIIDsToMod.isEmpty()) {
        PreparedStatement stmt = currentConn.prepareStatement(finalQuery);
        stmt.execute();
        IResultSetParser resultParser = currentConn.getResultSetParser(stmt.getResultSet());
        while (resultParser.next()) {
          propertiesFromCache.add(new PropertyDTO(resultParser));
        }
      }
    });
    return propertiesFromCache;
  }
  
  @Override
  public IConfigRuleDTO getRuleByCode(String code) throws RDBMSException
  {
    IConfigRuleDTO[] rule = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn
              .prepareStatement(Q_RULE_CONFIG + " where rc.ruleCode = ?");
          stmt.setString(1, code);
          stmt.execute();
          
          IResultSetParser ruleResult = currentConn.getResultSetParser(stmt.getResultSet());
          IConfigRuleDTO ruleDTO = null;
          while (ruleResult.next()) {
            if (ruleDTO == null) {
              ruleDTO = new ConfigRuleDTO(ruleResult);
            }
            ruleDTO.getRuleExpressions().add(new RuleExpressionDTO(ruleResult));
          }
          rule[0] = ruleDTO;
        });

    return rule[0];
  }

  @Override
  public ITaskDTO createTask(String taskCode, TaskType taskType) throws RDBMSException
  {
    String ccode = ClassifierDTO.getCacheCode(taskCode);
    /*
     * Commenting cache check as it does not allowing to make entry in database while re-importing
     * if (CSCache.instance().isKept(ccode)) {
      return (TaskDTO) CSCache.instance().get(ccode);
    }*/
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          currentConn.getFunction( ResultType.VOID, "pxp.fn_taskConfig")
              .setInput(ParameterType.STRING, taskCode)
              .setInput(ParameterType.INT, taskType.ordinal())
              .execute();
        });
    TaskDTO taskDTO = new TaskDTO(taskCode, taskType);
    CSCache.instance()
        .keep(ccode, taskDTO);
    return taskDTO;
  }
  
  
  public Map<ClassifierType, List<String>> groupClassifiers(Collection<String> classifierCodes) throws RDBMSException
  {
    Map<ClassifierType, List<String>> classifiers = new HashMap<>();
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
          for (String classifierCode : classifierCodes) {
            ClassifierDTO classifier = getClassifierByCode( currentConn, classifierCode);
            List<String> list = classifiers.get(classifier.getClassifierType());
            if (list == null) {
              list = new ArrayList<>();
              classifiers.put(classifier.getClassifierType(), list);
            }
            list.add(classifier.getCode());
          }
        });
    return classifiers;
  }

  @Override
  public void deleteContextualObjects(IContextDTO... contexts) throws RDBMSException
  {
    long startClock = System.currentTimeMillis();
    List<String> removedAttributeContextIds = new ArrayList<>();
    for(IContextDTO context: contexts) {
      removedAttributeContextIds.add(context.getContextCode());
    }
      RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
        String query = String.format(REMOVE_CONTEXTUAL_OBJECTS, Text.join(",", removedAttributeContextIds, "'%s'"));

        PreparedStatement stmt = currentConn
            .prepareStatement(query);
        stmt.execute();
      });
      
      RDBMSLogger.instance().debug( 
          "NA|INTERNAL-RDBMS|ConfigurationDAO|deleteContextualObjects (%d properties)| %d ms",
          contexts.length, System.currentTimeMillis() - startClock);
    }
  
  @Override
  public void createLanguageConfig(String languageCode, String parentCode) throws RDBMSException 
  {
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      currentConn.getProcedure( "sp_languageConfig")
      .setInput(ParameterType.STRING, languageCode)
      .setInput(ParameterType.STRING, parentCode)
          .execute();
      
      //Create dynamic view
      List<String> localeInheritance = new ArrayList<>(Arrays.asList(languageCode));
      DynamicViewDAS propertyService = new DynamicViewDAS( currentConn,localeInheritance);
      String viewName = propertyService.createDynamicValueView();
      RDBMSLogger.instance().info("Created dynamic view "+viewName);
    });
  }
  
  @Override
  public ILanguageConfigDTO getLanguageConfig(String languageCode) throws RDBMSException
  {
    ILanguageConfigDTO languageDTO [] = {null};
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(Q_LANGUAGE_CONFIG);
          stmt.setString(1, languageCode);
          stmt.execute();
          
          IResultSetParser languageResult = currentConn.getResultSetParser(stmt.getResultSet());
          if (languageResult.next()) {
            long languageIID = languageResult.getLong("languageiid");
            String code = languageResult.getString("languagecode");
            Long[] longArray = languageResult.getLongArray("parentiids");
            List<Long> parentIIDs = new ArrayList<>();
            for (Long item : longArray) {
              parentIIDs.add(item);
            }
            languageDTO[0] = new LanguageConfigDTO(languageIID, code, parentIIDs);
          }
        });

    return languageDTO[0];

  }

  @Override
  public ILanguageConfigDTO getLanguageConfigByLanguageIID(Long languageiid) throws RDBMSException
  {
    ILanguageConfigDTO languageDTO [] = {null};
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(Q_LANGUAGE_CONFIG_BY_LANGUAGE_IID);
          stmt.setLong(1, languageiid);
          stmt.execute();
          
          IResultSetParser languageResult = currentConn.getResultSetParser(stmt.getResultSet());
          if (languageResult.next()) {
            long languageIID = languageResult.getLong("languageiid");
            String code = languageResult.getString("languagecode");
            Long[] longArray = languageResult.getLongArray("parentiids");
            List<Long> parentIIDs = new ArrayList<>();
            for (Long item : longArray) {
              parentIIDs.add(item);
            }
            languageDTO[0] = new LanguageConfigDTO(languageIID, code, parentIIDs);
          }
        });

    return languageDTO[0];

  }

  @Override
  public List<Long> getChildLanguageByLanguageIID(Long languageiid) throws RDBMSException
  {
    List<Long> childLanguageIIds = new ArrayList<>();
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          PreparedStatement stmt = currentConn.prepareStatement(Q_CHILD_LANGUAGES_BY_LANGUAGE_IID);
          stmt.setLong(1, languageiid);
          stmt.execute();
          
          IResultSetParser languageResult = currentConn.getResultSetParser(stmt.getResultSet());
          while (languageResult.next()) {
            long childLanguageIID = languageResult.getLong("languageiid");
            childLanguageIIds.add(childLanguageIID);
          }
        });

    return childLanguageIIds;

  }

  @Override
  public void deleteLanguageConfigByCodes(List<String> codes) throws RDBMSException
  {
    RDBMSConnectionManager.instance()
        .runTransaction((RDBMSConnection currentConn) -> {
          String query = String.format(Q_DELETE_LANGUAGE_CONFIG_BY_CODES, Text.join(",", codes, "'%s'"));
          PreparedStatement stmt = currentConn.prepareStatement(query);
          stmt.execute();
        });
  }

  public Map<String, Long> getTagPropertyIdByCode(IEntityRelationDTO relation) throws RDBMSException
  {
    Map<String, Long> result = new HashMap<>();
      RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
        for(ITagDTO tag: relation.getContextualObject().getContextTagValues()) {
        PreparedStatement stmt = currentConn.prepareStatement(TAG_PROPERTY + " where tagvaluecode = ?");
        stmt.setString(1, tag.getTagValueCode());
        IResultSetParser propertyResult = currentConn.getResultSetParser(stmt.executeQuery());
        while (propertyResult.next()) {
         result.put(tag.getTagValueCode(), propertyResult.getLong("propertyiid"));
         }
        }
      });
      return result;
  }
  
  @Override
  public Set<Long> getClassifierIIDsFromCodes(Set<String> classifierCodes)
      throws RDBMSException
  {
    Set<Long> classifierIIDs = new HashSet<>();

    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {

      String codes = Text.join(",", classifierCodes, "'%s'");
      String classifierQuery = String.format(Q_GET_CLASSIFIER_IIDS_FROM_CODES, codes);
      
      PreparedStatement classifierStmt = currentConn.prepareStatement(classifierQuery);
      IResultSetParser classifierResult= currentConn.getResultSetParser(classifierStmt.executeQuery());
      
      while(classifierResult.next()) {
        classifierIIDs.add(classifierResult.getLong("classifieriid"));
      }
    });
      
    return classifierIIDs;
  }
  
  @Override
  public List<String> getOrganizationIdHavingRuntimeInstance(List<String> partnerIdsToBeDeleted) throws RDBMSException
  {
    List<String> partnerhavingInstances = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuffer query = new StringBuffer();
      query.append(String.format(Q_ORGANIZATION, Text.join(",", partnerIdsToBeDeleted, "'%s'")));
      PreparedStatement stmt = currentConn.prepareStatement(query.toString());
      stmt.execute();
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        partnerhavingInstances.add(resultSet.getString("organizationcode"));
      }
      
    });
    return partnerhavingInstances;
  }

  private static final String INSERT_HIERARCHY = "update pxp.classifierConfig set hierarchyiids = '{%s}' where classifieriid = %s";

  public void updateHierarchyIIDs(Long classifierIID, List<Long> parentIIDs) throws RDBMSException
  {
    String query = String.format(INSERT_HIERARCHY, Text.join(",", parentIIDs), classifierIID.toString());
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement statement = currentConn.prepareStatement(query);
      statement.executeUpdate();
    });
  }
  
  @Override
  public List<String> getTagValueByIID(long propertyIID) throws RDBMSException{
    List<String> tagValues = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(TAG_PROPERTY + W_IID);
      stmt.setLong(1, propertyIID);
      stmt.execute();
      IResultSetParser resultSet = currentConn.getResultSetParser(stmt.getResultSet());
      while (resultSet.next()) {
        tagValues.add(resultSet.getString("tagvaluecode"));
      }
    });
    return tagValues;
  }

  public static final String CLASSIFIER_DELETE = "delete from pxp.classifierConfig where classifierCode = '%s'";
  public static final String BASE_ENTITY_DELETE = "delete from pxp.baseEntity where  baseEntity.baseEntityId = '%s'";

  public void deleteClassifier(String classifierCode) throws RDBMSException
  {
    String ccode = ClassifierDTO.getCacheCode(classifierCode);
    if (CSCache.instance().isKept(ccode)) {
      CSCache.instance().remove(ccode);
    }
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn.prepareStatement(String.format(BASE_ENTITY_DELETE, classifierCode));
      stmt.execute();

      stmt = currentConn.prepareStatement(String.format(CLASSIFIER_DELETE, classifierCode));
      stmt.execute();

    });
  }

  public UserDTO getUserByIID(long userIID) throws RDBMSException
  {
    UserDTO[] user = { null };
    RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
      PreparedStatement stmt = currentConn
          .prepareStatement(Q_USER + " where userIID = ?");
      stmt.setLong(1, userIID);
      stmt.execute();
      IResultSetParser userResult = currentConn.getResultSetParser(stmt.getResultSet());
      if (!userResult.next()) {
        throw new RDBMSException(100, "Inconsistent Configuration",
            "User with userIID " + userIID + " not found");
      }
      user[0] = new UserDTO(userResult);
    });
    return user[0];
  }
  
  public List<IClassifierDTO> getClassifierDtos(Collection<String> classifierCodes)
      throws RDBMSException, SQLException
  {
    List<IClassifierDTO> classifierDtos = new ArrayList<>();
    RDBMSConnectionManager.instance().runTransaction((RDBMSConnection currentConn) -> {
      StringBuffer query = new StringBuffer(Q_CLASSIFIER_W_CODES);
      if (!classifierCodes.isEmpty()) {
        String inClassifierCodesQuery = classifierCodes.stream().map(classfierCode -> "\'" + classfierCode + "\'")
                .collect(Collectors.joining(",", "(", ")"));
        query.append(inClassifierCodesQuery);
            
        PreparedStatement stmt = currentConn.prepareStatement(query.toString());
        stmt.execute();
        IResultSetParser resultParser = currentConn.getResultSetParser(stmt.getResultSet());
        while (resultParser.next()) {
          classifierDtos.add(new ClassifierDTO(resultParser));
        }
      }
    });
    return classifierDtos;
  }
  
  public List<IClassifierDTO> getClassifiersByIID(Collection<Long> classifierIIDs) throws RDBMSException
  {
    List<IClassifierDTO> classifierDtos = new ArrayList<>();
    if(!classifierIIDs.isEmpty())
    {
      RDBMSConnectionManager.instance().runTransaction(( RDBMSConnection currentConn) -> {
        String query = String.format(Q_CLASSIFIER + " where classifierIID in (%s)", Text.join(",", classifierIIDs));
        PreparedStatement stmt = currentConn.prepareStatement(query);
        stmt.execute();
        
        IResultSetParser resultParser = currentConn.getResultSetParser(stmt.getResultSet());
        while (resultParser.next()) 
        {
          classifierDtos.add(new ClassifierDTO(resultParser));
        }
      });
    }
    return classifierDtos;
  }
}