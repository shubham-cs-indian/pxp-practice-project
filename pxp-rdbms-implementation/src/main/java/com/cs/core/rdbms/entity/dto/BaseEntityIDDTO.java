package com.cs.core.rdbms.entity.dto;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.cs.config.standard.IStandardConfig;
import com.cs.core.csexpress.CSEList;
import com.cs.core.csexpress.CSEParser;
import com.cs.core.csexpress.definition.CSEObject;
import com.cs.core.data.LocaleID;
import com.cs.core.dataintegration.idto.IPXON.PXONTag;
import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.rdbms.config.dao.ConfigurationDAO;
import com.cs.core.rdbms.config.dto.CatalogDTO;
import com.cs.core.rdbms.config.dto.ClassifierDTO;
import com.cs.core.rdbms.config.dto.LocaleCatalogDTO;
import com.cs.core.rdbms.config.idto.ICatalogDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IContextDTO;
import com.cs.core.rdbms.config.idto.ILocaleCatalogDTO;
import com.cs.core.rdbms.dto.RDBMSRootDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTO;
import com.cs.core.rdbms.entity.idto.IBaseEntityIDDTOBuilder;
import com.cs.core.rdbms.entity.idto.IContextualDataDTO;
import com.cs.core.rdbms.function.IResultSetParser;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.icsexpress.ICSEElement;
import com.cs.core.technical.icsexpress.ICSEElement.CSEObjectType;
import com.cs.core.technical.icsexpress.ICSEElement.Keyword;
import com.cs.core.technical.rdbms.exception.RDBMSException;

/**
 * @author farooq.kadri
 */
public class BaseEntityIDDTO extends RDBMSRootDTO implements IBaseEntityIDDTO {

  private static final String       BASE_LOCALE              = PXONTag.baselocale.toReadOnlyTag();
  private static final String       OTHER_CLASSIFIERS        = PXONTag.classifier.toCSEListTag();
  private static final String       CHILD_LEVEL              = PXONTag.level.toPrivateTag();
  private static final String       CONTEXTUAL_OBJECT        = PXONTag.cxtual.toJSONContentTag();
  private static final String       DEFAULT_IMAGE            = PXONTag.img.toCSETag();
  private static final String       HASH_CODE                = PXONTag.hash.toCSETag();
  private static final String       NATURE                   = PXONTag.nature.toReadOnlyCSETag();
  private static final String       SOURCE_ORGANIZATION_CODE = PXONTag.sourceorganizationcode.toCSETag();
  
  private final Set<IClassifierDTO> otherClassifiers       = new TreeSet<>();
  protected LocaleID                baseLocaleID           = new LocaleID();
  protected ICatalogDTO             catalog                = new CatalogDTO();
  protected String                  baseEntityID           = "";
  protected BaseType                baseType               = BaseType.UNDEFINED;
  protected IClassifierDTO          natureClass            = new ClassifierDTO();
  protected long                    defaultImageIID        = 0L;
  protected int                     childLevel             = 1;
  protected String                  sourceOrganizationCode = IStandardConfig.STANDARD_ORGANIZATION_RCODE;
  // by
  // default
  protected ContextualDataDTO       contextualObject  = new ContextualDataDTO();
  protected String                  hashCode          = "";
  protected String                  defaultImageID    = "";
  
  /**
   * Enabled default constructor
   */
  public BaseEntityIDDTO() {
  }

  /**
   * Value constructor
   *
   * @param baseEntityID
   * @param baseType
   * @param baseLocaleID
   * @param catalog
   * @param natureClass
   */
  public BaseEntityIDDTO(String baseEntityID, BaseType baseType, String baseLocaleID,
      ICatalogDTO catalog, ClassifierDTO natureClass)
  {
    this.baseEntityID = baseEntityID;
    this.baseType = baseType;
    this.baseLocaleID = new LocaleID(baseLocaleID);
    this.catalog = catalog;
    this.natureClass = natureClass;
    this.sourceOrganizationCode = catalog.getOrganizationCode();
  }
  
  /**
   * Copy constructor
   *
   * @param source
   */
  public BaseEntityIDDTO(BaseEntityIDDTO source) {
    super(source.getBaseEntityIID());
    this.baseEntityID = source.getBaseEntityID();
    this.baseType = source.getBaseType();
    this.baseLocaleID = new LocaleID(source.getBaseLocaleID());
    this.catalog = source.getCatalog();
    this.natureClass = (ClassifierDTO) source.getNatureClassifier();
    this.defaultImageIID = source.getDefaultImageIID();
    this.childLevel = source.getChildLevel();
    this.contextualObject = (ContextualDataDTO) source.getContextualObject();
    this.otherClassifiers.clear();
    this.otherClassifiers.addAll(source.getOtherClassifiers());
  }

  /**
   * Complete the DTO from the informations returned in view BaseEntityTrackingWithName
   *
   * @param parser a result set of type BaseEntityTracking
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws CSFormatException
   */
  public void mapFromBaseEntityTrackingWithName(IResultSetParser parser)
          throws SQLException, RDBMSException, CSFormatException {
    // TODO: to be reviewed
    setIID(parser.getLong("baseEntityIID"));
    this.baseEntityID = parser.getString("baseentityID");
    this.baseType = BaseType.valueOf(parser.getInt("basetype"));
    this.baseLocaleID = new LocaleID(parser.getString("baselocaleid"));
    this.childLevel = parser.getInt("childlevel");
    this.defaultImageIID = parser.getLong("defaultImageIID");
    this.defaultImageID = parser.getString("defaultImageIID");
    this.hashCode = parser.getString("hashCode");
    this.contextualObject = new ContextualDataDTO(parser);
    IClassifierDTO classifierDTO = ConfigurationDAO.instance()
            .getClassifierByIID(parser.getLong("classifieriid"));
    this.catalog = new CatalogDTO(parser.getString("catalogcode"), parser.getString("organizationCode"));
    this.sourceOrganizationCode = parser.getString("sourceOrganizationCode");
    this.natureClass = (ClassifierDTO) classifierDTO;
    otherClassifiers.clear();
    otherClassifiers.addAll(ClassifierDTO.newClassifierDTOs(parser.getString("classifieriids")));
  }

  public void mapFromBaseEntityWithContextualData(IResultSetParser parser)
          throws SQLException, RDBMSException, CSFormatException {
    setIID(parser.getLong("baseEntityIID"));
    this.baseEntityID = parser.getString("baseentityID");
    this.baseType = BaseType.valueOf(parser.getInt("basetype"));
    String localeID = parser.getString("baselocaleid");
    this.baseLocaleID = new LocaleID(localeID);
    this.catalog = new CatalogDTO(parser.getString("catalogcode"), parser.getString("organizationCode"));
    this.childLevel = parser.getInt("childlevel");
    this.defaultImageIID = parser.getLong("defaultImageIID");
    this.contextualObject = new ContextualDataDTO();
    this.contextualObject.mapFromContextualObjectWithLinkedEntities(parser);
  }

  /**
   * Complete DTO from the informations returned from table baseentity
   *
   * @param parser a result set of type BaseEntityTracking
   * @throws java.sql.SQLException
   * @throws com.cs.core.technical.rdbms.exception.RDBMSException
   * @throws CSFormatException
   */
  public void mapFromBaseEntityTrackingWithoutName(IResultSetParser parser) throws SQLException, RDBMSException, CSFormatException
  {
    // TODO: to be reviewed
    setIID(parser.getLong("baseEntityIID"));
    this.baseEntityID = parser.getString("baseentityID");
    this.baseType = BaseType.valueOf(parser.getInt("basetype"));
    this.baseLocaleID = new LocaleID(parser.getString("baselocaleid"));
    this.childLevel = parser.getInt("childlevel");
    this.defaultImageIID = parser.getLong("defaultImageIID");
    this.hashCode = parser.getString("hashCode");
    IClassifierDTO classifierDTO = ConfigurationDAO.instance().getClassifierByIID(parser.getLong("classifieriid"));
    this.catalog = new CatalogDTO(parser.getString("catalogcode"), parser.getString("organizationCode"));
    this.natureClass = (ClassifierDTO) classifierDTO;
  }
  
  @Override
  public ICSEElement toCSExpressID() {
    CSEObject ecse = new CSEObject(CSEObjectType.Entity);
    ecse.setIID(getIID());
    ecse.setCode(baseEntityID);
    if ( exportIID )
      ecse.setIID(getIID());
    ecse.setSpecification(Keyword.$type, baseType);
    ecse.setSpecification(Keyword.$ctlg, catalog.getCatalogCode());
    ecse.setSpecification(Keyword.$org, catalog.getOrganizationCode());
    return ecse;
  }

  @Override
  public void fromCSExpressID(ICSEElement cse) throws CSFormatException {
    CSEObject ecse = (CSEObject) cse;
    setIID(ecse.getIID());
    baseEntityID = ecse.getCode();
    baseType = ecse.getSpecification(BaseType.class, Keyword.$type);
    catalog = new CatalogDTO(ecse.getSpecification(Keyword.$ctlg), ecse.getSpecification(Keyword.$org));
  }

  @Override
  public void fromPXON(JSONContentParser parser) throws CSFormatException {
    super.fromPXON(parser);
    if ( parser.toJSONObject().containsKey(CONTEXTUAL_OBJECT) ) {
      CSEObject cxtual = (CSEObject)parser.getCSEElement(CONTEXTUAL_OBJECT);
      contextualObject.fromCSExpressID(cxtual);
    }
    else {
      contextualObject = new ContextualDataDTO();
    }
    
    String natureClassifierCSE = parser.getString(NATURE);
    ICSEElement natureClassCSE = (new CSEParser()).parseDefinition(natureClassifierCSE);
    this.natureClass.fromCSExpressID(natureClassCSE);
    
    // load other classifiers list
    otherClassifiers.clear();
    String classCse = parser.getString(OTHER_CLASSIFIERS);
    if (!classCse.isEmpty()) {
      CSEList classList = (CSEList) parser.getCSEElement(OTHER_CLASSIFIERS);
      for (ICSEElement CSEObject : classList.getSubElements()) {
        ClassifierDTO classifier = new ClassifierDTO();
        classifier.fromCSExpressID(CSEObject);
        otherClassifiers.add(classifier);
      }
    }
    childLevel = parser.getInt(CHILD_LEVEL);
    sourceOrganizationCode = parser.getString(SOURCE_ORGANIZATION_CODE);
    baseLocaleID = new LocaleID(parser.getString(BASE_LOCALE));
    String defImageCse = parser.getString(DEFAULT_IMAGE);
    if (!defImageCse.isEmpty()) {
      CSEObject defImgCseObj =  (CSEObject) parser.getCSEElement(DEFAULT_IMAGE);
      defaultImageID = defImgCseObj.getCode();
      defaultImageIID = defImgCseObj.getIID();
    } else {
      defaultImageIID = 0L;
    }
    hashCode = parser.getString(HASH_CODE);
  }

  @Override
  public StringBuffer toPXONBuffer() throws CSFormatException {
    CSEList classifierList = new CSEList();
    for (IClassifierDTO classifier : otherClassifiers) {
      classifierList.getSubElements().add(classifier.toCSExpressID());
    }
    CSEObject defaultImage = new CSEObject(CSEObjectType.Entity);
    defaultImage.setCode(defaultImageID);
    defaultImage.setIID(defaultImageIID);
    return JSONBuilder.assembleJSONBuffer(super.toPXONBuffer(),
            JSONBuilder.newJSONField(CHILD_LEVEL, childLevel),
            JSONBuilder.newJSONField(BASE_LOCALE, baseLocaleID.toString()),
            JSONBuilder.newJSONField(NATURE, natureClass.toCSExpressID()),
            JSONBuilder.newJSONField(SOURCE_ORGANIZATION_CODE, sourceOrganizationCode),
            defaultImageIID > 0
              ? JSONBuilder.newJSONField(DEFAULT_IMAGE, defaultImage) : JSONBuilder.VOID_FIELD,
            !hashCode.isEmpty()
              ? JSONBuilder.newJSONField(HASH_CODE, hashCode) : JSONBuilder.VOID_FIELD,
            !contextualObject.isNull()
              ? JSONBuilder.newJSONField(CONTEXTUAL_OBJECT, contextualObject.toCSExpressID()) : JSONBuilder.VOID_FIELD,
            !otherClassifiers.isEmpty()
              ? JSONBuilder.newJSONField(OTHER_CLASSIFIERS, classifierList) : JSONBuilder.VOID_FIELD);
  }

  @Override
  public ILocaleCatalogDTO getLocaleCatalog()
  {
    return new LocaleCatalogDTO(baseLocaleID.toString(), catalog.getCatalogCode(), catalog.getOrganizationCode());
  }

  /**
   * Initialize the locale catalog of this base entity
   *
   * @param catalog the locale catalog defined from the parent catalog in which this DTO is created
   */
  public void setLocaleCatalog(LocaleCatalogDTO catalog) {
    this.catalog = new CatalogDTO(catalog.getCatalogCode(), catalog.getOrganizationCode());
    if (this.baseLocaleID == null || this.baseLocaleID.isEmpty()) {
      this.baseLocaleID = new LocaleID(catalog.getLocaleID());
    }
  }

  @Override
  public ICatalogDTO getCatalog() {
    return catalog;
  }

  @Override
  public IBaseEntityIDDTO.BaseType getBaseType() {
    return baseType;
  }

  @Override
  public String getBaseLocaleID() {
    return baseLocaleID.toString();
  }

  @Override
  public void setBaseLocaleID(String baselocalID)
  {
    this.baseLocaleID = new LocaleID(baselocalID);
  }
  
  @Override
  public IClassifierDTO getNatureClassifier() {
    return natureClass;
  }
  
  @Override
  public void setNatureClassifier(IClassifierDTO natureClass) {
    this.natureClass = natureClass;
  }

  @Override
  public long getDefaultImageIID() {
    return defaultImageIID;
  }

  @Override
  public void setDefaultImageIID(long defaultImageIID) {
    setChanged(this.defaultImageIID != defaultImageIID ? true : isChanged());
    this.defaultImageIID = defaultImageIID;
  }

  @Override
  public Set<IClassifierDTO> getOtherClassifiers() {
    return otherClassifiers;
  }

  public Set<Long> getOtherClassifierIIDs() {
    Set<Long> classifIIDs = new HashSet<>();
    otherClassifiers.forEach(classifier -> classifIIDs.add(classifier.getIID()));
    return classifIIDs;
  }

  @Override
  public void setOtherClassifierIIDs(IClassifierDTO... classifiers) {
    otherClassifiers.clear();
    setChanged(true);
    otherClassifiers.addAll(Arrays.asList(classifiers));
  }

  @Override
  public long getBaseEntityIID() {
    return getIID();
  }

  /**
   * @param id overwritten ID
   */
  public void setBaseEntityID( String id) {
    baseEntityID = id;
  }
  
  @Override
  public String getBaseEntityID() {
    return baseEntityID;
  }

  @Override
  public int getChildLevel() {
    return childLevel;
  }

  /**
   * @param level overwritten child level
   */
  public void setChildLevel(int level) {
    this.childLevel = level;
  }

  @Override
  public IContextualDataDTO getContextualObject() {
    return contextualObject;
  }
  
  @Override
  public void setContextualObject(IContextualDataDTO contextualObject) {
    this.contextualObject = (ContextualDataDTO) contextualObject;
  }

  @Override
  public String getSourceOrganizationCode() 
  {
    return sourceOrganizationCode;
  }
  
  @Override
  public String getHashCode() {
    return hashCode;
  }

  @Override
  public void setHashCode(String hashCode) {
    setChanged(this.hashCode != hashCode ? true : isChanged());
    this.hashCode = hashCode;
  }
  
  /**
   * implementation of IBaseEntityIDDTOBuilder
   * @author Janak.Gurme
   *
   */
  
  @SuppressWarnings("rawtypes")
  public static class BaseEntityIDDTOBuilder implements IBaseEntityIDDTOBuilder<IBaseEntityIDDTOBuilder> {
    
    private final BaseEntityIDDTO baseEntityIDDTO;
    
    /**
     * mininal fields to prepare BaseEntityIDDTO
     * @param baseEntityID
     * @param baseType
     * @param baseLocaleID
     * @param natureClass
     */
    public BaseEntityIDDTOBuilder(String baseEntityID, BaseType baseType, String baseLocaleID,
        ICatalogDTO catalog, ClassifierDTO natureClass)
    {
      baseEntityIDDTO = new BaseEntityIDDTO(baseEntityID, baseType, baseLocaleID, catalog,
          natureClass);
    }
   
    @Override
    public IBaseEntityIDDTOBuilder baseEntityIID(long baseEntityIID)
    {
      baseEntityIDDTO.setIID(baseEntityIID);
      return this;
    }
    
    @Override
    public IBaseEntityIDDTOBuilder contextDTO(IContextDTO contextDTO)
    {
      baseEntityIDDTO.contextualObject = new ContextualDataDTO(contextDTO.getCode());
      return this;
    }
    
    @Override
    public IBaseEntityIDDTOBuilder defaultImageIID(long imageIID)
    {
      baseEntityIDDTO.setDefaultImageIID(imageIID);;
      return this;
    }
    
    @Override
    public IBaseEntityIDDTOBuilder hashCode(String hashCode)
    {
      baseEntityIDDTO.setHashCode(hashCode);
      return this;
    }
    
    @Override
    public IBaseEntityIDDTO build()
    {
      return baseEntityIDDTO;
    }
  }
}
