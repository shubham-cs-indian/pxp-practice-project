package com.cs.core.runtime.interactor.exception.configuration;

public class ErrorCodes {
  
  /**
   * ******************************** Asset
   * ********************************************************
   */
  public static final String ASSET_PARENT_KLASS_NOT_FOUND_ON_CREATE                              = "00001";
  
  public static final String ASSET_KLASS_NOT_FOUND_ON_GET                                        = "00002";
  public static final String ASSET_KLASS_NOT_FOUND_ON_GET_WITH_GLOBAL_PERMISSION                 = "000021";
  public static final String ASSET_KLASS_NOT_FOUND_ON_SAVE                                       = "00003";
  
  /**
   * ******************************** Attribute
   * ****************************************************
   */
  public static final String ATTRIBUTE_NOT_FOOUND_ON_GET                                         = "00012";
  
  public static final String ATTRIBUTE_NOT_FOUND_ON_SAVE                                         = "00013";
  
  /**
   * ******************************** Branch
   * *******************************************************
   */
  public static final String EMPTY_BRANCH_ID_UPON_DELETE_BRANCH                                  = "00024";
  
  /**
   * ******************************** CaseEffectRule
   * ***********************************************
   */
  public static final String CAUSE_EFFECT_RULE_NOT_FOUND_ON_GET                                  = "00032";
  
  public static final String CAUSE_EFFECT_RULE_NOT_FOUND_ON_SAVE                                 = "00033";
  
  /**
   * ******************************** Condition
   * ****************************************************
   */
  public static final String CONDITION_NOT_FOUND_ON_GET                                          = "00042";
  
  public static final String CONDITION_NOT_FOUND_ON_SAVE                                         = "00043";
  
  /**
   * ******************************** DataRule
   * *****************************************************
   */
  public static final String DATA_RULE_NOT_FOUND_ON_GET                                          = "00052";
  
  public static final String DATA_RULE_NOT_FOUND_ON_SAVE                                         = "00053";
  
  /**
   * ******************************** Util
   * *********************************************************
   */
  public static final String TREE_TYPE_VERTEX_NOT_FOUND                                          = "0007001";
  
  public static final String UNKNOWN_PROPERTY_TYPE                                               = "0007002";
  public static final String TREE_TYPE_OPTION_LINK_NOT_FOUND                                     = "0007003";
  public static final String PROPERTY_NOT_FOUND                                                  = "0007004";
  public static final String ALLOWED_TYPE_KLASS_NOT_FOUND_ON_SAVE                                = "0007305";
  public static final String DATA_RULES_NOT_FOUND_ON_SAVE                                        = "0007306";
  public static final String PARENT_NOT_FOUND_ON_CREATE                                          = "0007107";
  public static final String TAXONOMY_PARENT_NOT_FOUND                                           = "0007108";
  public static final String ROLE_NODE_NOT_FOUND                                                 = "0007009";
  public static final String MULTIPLE_LINK_FOUND_BETWEEN_ROLE_AND_GLOBAL_PERMISSION              = "0007010";
  public static final String ROLE_NODE_NOT_FOUND_ON_SAVE                                         = "0007311";
  public static final String MULTIPLE_LINK_FOUND_BETWEEN_ROLE_AND_GLOBAL_PERMISSION_ON_SAVE      = "0007312";
  public static final String STRUCTURE_TO_DELETE_NOT_FOUND_ON_SAVE                               = "0007313";
  public static final String TREE_TYPE_VERTEX_NOT_FOUND_ON_SAVE                                  = "0007314";
  public static final String TREE_TYPE_OPTION_LINK_NOT_FOUND_ON_SAVE                             = "0007315";
  public static final String VERTEX_NOT_FOUND                                                    = "0007016";
  public static final String MULTIPLE_VERTEX_FOUND_WITH_SAME_ID                                  = "0007017";
  public static final String MULTIPLE_VERTEX_FOUND_FOR_SECTION_PERMISSION_ON_SAVE                = "0007318";
  public static final String MULTIPLE_VERTEX_FOUND_FOR_SECTION_ELEMENT_PERMISSION_ON_SAVE        = "0007319";
  public static final String KLASS_NOT_FOUND_ON_GET_BRANCH_AND_VARIANTS_LIST_BY_ID_OR_SAVE_KLASS = "0007020";
  public static final String KLASS_CARDINALITY_MISMATCH                                          = "0007021";
  public static final String RELATIONSHIP_SECTION_NOT_FOUND                                      = "0007022";
  
  /**
   * ******************************** Klass
   * ********************************************************
   */
  public static final String KLASS_NOT_FOUND_ON_GET                                              = "00082";
  
  public static final String KLASS_NOT_FOUND_ON_GET_WITH_GLOBAL_PERMISSION                       = "000821";
  public static final String KLASS_NOT_FOUND_ON_GET_WITH_LINKED_KLASSES                          = "000822";
  public static final String KLASS_NOT_FOUND_ON_SAVE                                             = "00083";
  public static final String KLASS_NOT_FOUND_ON_GET_DEFAULT_KLASSES                              = "000823";
  
  /**
   * ******************************** Relationship
   * *************************************************
   */
  public static final String KLASS_NOT_FOUND_ON_RELATIONSHIP_CREATE                              = "000911";
  
  public static final String RELATIONSHIP_NOT_FOUND_ON_GET                                       = "00092";
  public static final String KLASS_NOT_FOUND_ON_GET_TARGET_KLASSES                               = "000921";
  public static final String RELATIONSHIP_SECTION_NOT_FOUND_ON_GET_TARGET_KLASSES                = "000922";
  public static final String RELATIONSHIP_NOT_FOUND_ON_SAVE                                      = "00093";
  
  /**
   * ******************************** Role
   * *********************************************************
   */
  public static final String ROLE_NOT_FOUND_ON_GET                                               = "00102";
  
  public static final String USER_NOT_FOUND_ON_GET_ROLE_IDS_FOR_CURRENT_USER                     = "001021";
  public static final String ROLE_NOT_FOUND_ON_SAVE                                              = "00103";
  
  /**
   * ******************************** RuleList
   * *****************************************************
   */
  public static final String RULE_LIST_NOT_FOUND_ON_GET_REFERENCED_DATA_RULES                    = "001121";
  
  public static final String KLASS_NOT_FOUND_ON_GET_RULE_LIST_WITH_REFERENCED_DATA_RULES         = "001122";
  public static final String RULE_LIST_NOT_FOUND_ON_GET                                          = "00112";
  public static final String RULE_LIST_NOT_FOUND_ON_SAVE                                         = "00113";
  
  /**
   * ******************************** State
   * ********************************************************
   */
  public static final String STATE_NOT_FOUND_ON_GET                                              = "00122";
  
  public static final String STATE_NOT_FOUND_ON_SAVE                                             = "00123";
  
  /**
   * ******************************** Tag
   * **********************************************************
   */
  public static final String TAG_PARENT_NOT_FOUND_ON_CREATE                                      = "00131";
  
  public static final String TAG_NOT_FOUND_ON_GET                                                = "00132";
  public static final String TAG_NOT_FOUND_ON_SAVE                                               = "00133";
  
  /**
   * ******************************** Target
   * *******************************************************
   */
  public static final String TARGET_PARENT_NOT_FOUND_ON_CREATE                                   = "00141";
  
  public static final String TARGET_NOT_FOUND_ON_GET                                             = "00142";
  public static final String TARGET_NOT_FOUND_ON_GET_WITH_GLOBAL_PERMISSION                      = "001421";
  public static final String TARGET_NOT_FOUND_ON_SAVE                                            = "00143";
  
  /**
   * ******************************** Task
   * *********************************************************
   */
  public static final String TASK_PARENT_NOT_FOUND_ON_CREATE                                     = "00151";
  
  public static final String TASK_NOT_FOUND_ON_GET                                               = "00152";
  public static final String TASK_NOT_FOUND_ON_GET_WITH_GLOBAL_PERMISSION                        = "001521";
  public static final String TASK_NOT_FOUND_ON_SAVE                                              = "00153";
  
  /**
   * ******************************** User
   * *********************************************************
   */
  public static final String USER_NOT_FOUND_ON_GET_CURRENT_USER                                  = "001621";
  
  public static final String USER_NOT_FOUND_ON_GET                                               = "00162";
  public static final String USER_NOT_FOUND_ON_RESET_PASSWORD                                    = "00160";
  public static final String USER_NOT_FOUND_ON_SAVE                                              = "00163";
  
  /**
   * ******************************** UserGroup
   * ****************************************************
   */
  public static final String USER_GROUP_NOT_FOUND_ON_GET                                         = "00172";
  
  public static final String USER_GROUP_NOT_FOUND_ON_SAVE                                        = "00173";
  
  /**
   * ******************************** ImportAttribute
   * **********************************************
   */
  public static final String IMPORT_ATTRIBUTE_NOT_FOUND_ON_SAVE                                  = "00183";
  
  public static final String IMPORT_ATTRIBUTE_MAPPING_ID_ALREADY_USED_ON_SAVE                    = "001831";
  
  /**
   * ******************************** ImportKlass
   * **************************************************
   */
  public static final String IMPORT_KLASS_PARENT_NOT_FOUND_ON_CREATE                             = "00191";
  
  public static final String IMPORT_KLASS_ROLE_NODE_NOT_FOUND_ON_SAVE                            = "001931";
  public static final String IMPORT_KLASS_MULTIPLE_LINK_FOUND_BETWEEN_ROLE_AND_GLOBAL_PERMISSION = "001932";
  public static final String IMPORT_KLASS_STRUCTURE_TO_DELETE_NOT_FOUND_ON_SAVE                  = "001933";
  public static final String IMPORT_KLASS_VALIDATION_FAILED_ON_CREATE                            = "001911";
  public static final String IMPORT_KLASS_NOT_FOUND_ON_GET                                       = "00192";
  public static final String IMPORT_KLASS_NOT_FOUND_ON_GET_WITH_GLOBAL_PERMISSION                = "001921";
  public static final String IMPORT_KLASS_NOT_FOUND_ON_GET_WITH_LINKED_KLASSES                   = "001922";
  public static final String IMPORT_KLASS_NOT_FOUND_ON_SAVE                                      = "00193";
  public static final String IMPORT_KLASS_NOT_FOUND_ON_GET_IMPORT_BRANCH_AND_VARIANTS_LIST_BY_ID = "001922";
  public static final String IMPORT_KLASS_CARDINALITY_MISMATCH_ON_GET                            = "001923";
  public static final String IMPORT_KLASS_NOT_FOUND_ON_RELATIONSHIP_CREATE                       = "001912";
  
  /**
   * ******************************** ImportRole
   * ***************************************************
   */
  public static final String IMPORT_ROLE_NOT_FOUND_ON_GET                                        = "00202";
  
  public static final String IMPORT_ROLE_NOT_FOUND_ON_SAVE                                       = "00203";
  
  /**
   * ******************************** ImportTag
   * ****************************************************
   */
  public static final String IMPORT_TAG_NOT_FOUND_ON_SAVE                                        = "00213";
  
  public static final String IMPORT_TAG_MAPPING_ID_ALREADY_USED_ON_SAVE                          = "002131";
  
  // ERROR CODES ::
  public static final String NOT_FOUND                                                           = "NOT_FOUND";
  public static final String VERSION_NOT_FOUND                                                   = "VERSION_NOT_FOUND";
  public static final String TYPE_CHANGED                                                        = "TYPE_CHANGED";
  public static final String USER_NOT_HAVE_EDIT_PERMISSION                                       = "USER_NOT_HAVE_EDIT_PERMISSION";
  public static final String PARENT_NOT_FOUND                                                    = "PARENT_NOT_FOUND";
}
