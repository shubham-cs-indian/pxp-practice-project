grammar csexpress;
@header {
    package com.cs.core.parser.csexpress;
}
// Search condition (level 1 expression)
search_expression:
    SELECT WS scope WS WHERE WS evaluation_expression;

// Rule condition (level 1 expression)
// without 'then' = KPI rule, otherwise DQ rule
rule_expression:
    FOR WS scope (WS WHEN WS evaluation_expression)? (WS THEN WS action_list)?;
// Simple calculation (level 1 expression) 
calculation:
    EQUALS WS? evaluation_expression;

//--------------------------------------------------------------------------------
// Entity Scope Expressions
//--------------------------------------------------------------------------------
scope:
    (basetype_scope WS)? catalog_scope (WS organization_scope)? (WS locale_scope)? (WS endpoint_scope)? (WS iid_scope)? (WS entity_scope)?;
basetype_scope:
    SBASETYPE WS? EQUALS WS? entity_basetypes;
entity_basetypes:
    GROUP_IN WS? basetype (WS? OR WS? basetype)* WS? GROUP_OUT |
    basetype (WS? OR WS? basetype)*;
basetype:
    ( SARTICLE | SEXTENSION | SASSET | SPID | STEXTASSET | SVCATALOG | SSUPPLIER | STXAONOMY | STARGET);
catalog_scope:
    (SCATALOG WS? EQUALS WS? catalogs);
catalogs:
    GROUP_IN WS? catalog_code (WS? OR WS? catalog_code)* WS? GROUP_OUT |
    catalog_code (WS? OR WS? catalog_code)*;
organization_scope:
    (SORGANZIATION WS? EQUALS WS? organizations);
organizations:
    GROUP_IN WS? organization_code (WS? OR WS? organization_code)* WS? GROUP_OUT |
    organization_code (WS? OR WS? organization_code)*;
organization_code:
    ( primaryidentifier);
catalog_code:
    CODE;    
locale_scope:
    SLOCALE WS? EQUALS WS? locales;
locales:
    GROUP_IN WS? locale_code (WS? OR WS? locale_code)* GROUP_OUT |
    locale_code (WS? OR WS? locale_code)*;
locale_code:
    LOCALEID | KBASELOCALE;
iid_scope:
    SENTITYIID WS? EQUALS WS? entity_iids;
entity_iids:
    GROUP_IN WS? entity_iid (WS? OR WS? entity_iid)* WS? GROUP_OUT |
    entity_iid (WS? OR WS? entity_iid)*;
entity_iid:
    (INTEGER);
//===============endpoint=====================/
endpoint_scope:
    SENDPOINT WS? EQUALS WS? endpoints;
endpoints:
    GROUP_IN WS? endpoint_code (WS? OR WS? endpoint_code)* GROUP_OUT |
        endpoint_code (WS? OR WS? endpoint_code)*;
endpoint_code:
    primaryidentifier;

//=============================================//

entity_scope:
    (scope_not WS)? entity_filter (WS? scope_operator WS? (scope_not WS)? entity_filter)*;
entity_filter:
    GROUP_IN WS? evaluated_filter WS? GROUP_OUT |
    target_object WS (IN|UNDER|IS) WS classifier (WS? OR WS? classifier)* |
    target_object WS (IN|UNDER|IS) WS GROUP_IN WS? classifier (WS? OR WS? classifier)* WS? GROUP_OUT |
    target_object WS (BELONGSTO) WS list_through_property |
    target_object WS (BELONGSTO) WS collection |
    target_object WS (EXISTIN) WS translation (WS? OR WS? translation)* |
    target_object WS (EXISTIN) WS GROUP_IN WS? translation (WS? OR WS? translation)* WS? GROUP_OUT |
    target_object WS (HAS) WS context (WS? OR WS? context)* |
    target_object WS (HAS) WS GROUP_IN WS? context (WS? OR WS? context)* WS? GROUP_OUT |
    target_object WS (CONTAINS|INVOLVES) WS property (WS? OR WS? property)* |
    target_object WS (CONTAINS|INVOLVES) WS GROUP_IN WS? property (WS? OR WS? property)* WS? GROUP_OUT |
    target_object DOT FQUALITY WS? EQUALS WS? quality_level (WS? OR WS? quality_level)* |
    target_object DOT FQUALITY WS? EQUALS WS? GROUP_IN WS? quality_level (WS? OR WS? quality_level)* WS? GROUP_OUT |
    target_object DOT FEXPIRED WS? EQUALS WS? expiry |
    target_object DOT FDUPLICATE WS? EQUALS WS? duplicate;

scope_not:
    NOT; // not supported for "contains property" filter
scope_operator:
    (LAND|LOR|LXOR);
list_through_property:
    (( object | predefinedobject ) WS? DOT WS?)? property (DOT property_field)?;
evaluated_filter:
    entity_scope;
context:
    object;
collection:
    object;
expiry:
(TRUE | FALSE);
duplicate:
(TRUE | FALSE);
translation:
    object;

//--------------------------------------------------------------------------------
// Action expression 
//--------------------------------------------------------------------------------
action_list:
    WS? action (WS? SEP WS? action)*;
action:
    GROUP_IN WS? evaluated_action WS? GROUP_OUT |
    target_property WS? ASSIGNS WS? evaluation_expression |
    target_property WS? RAISETO WS? quality_level (WS? GROUP_IN STRING GROUP_OUT)? |
    target_object WS? MOVETO WS? classifier;
evaluated_action:
    action_list; // recursive case
target_property:
    (( object | predefinedobject) DOT)? property;
target_object:
    ( object | predefinedobject );
classifier:
    ( object | predefinedclassifier);
predefinedclassifier:
    KNATURE | KISEMPTY;
quality_level:
    ( SRED  | SORANGE | SYELLOW | SGREEN );


//--------------------------------------------------------------------------------
// Property Calculation Expressions
//--------------------------------------------------------------------------------
evaluation_expression:
    (unary_operator WS?)? operand ( WS? operator WS? (unary_operator WS?)? operand)*;

operand:
    GROUP_IN WS? evaluated_operand WS? GROUP_OUT |
    (function_operator WS?) GROUP_IN WS? (function_parameter ( WS? SEP WS? function_parameter)*)? WS? GROUP_OUT |
    property_operand (DOT property_field)? |
    literal_operand;

evaluated_operand:
    evaluation_expression; // recursive case

unary_operator:
    ( NOT );

function_operator:
    ( FUNIQUE | FUPPER | FLOWER | FREPLACE | FADD | FREMOVE);

function_parameter:
    ( property_operand (DOT property_field)? | literal_operand | evaluated_operand);

operator:
    math_operator |
    text_operator |
    conditional_operator;

math_operator:
    ( PLUS | MINUS | MULTIPLY | DIVIDE | MODULUS);
text_operator:
    ( CONCATENATE );
conditional_operator: 
      ( EQUALS | DIFF | LT | LTE | GT | GTE | LOR | LAND | LXOR | CONTAINS | LIKE | REGEX | RELATES | BETWEEN | NOTCONTAINS);

property_operand:
    property |
    ( object | predefinedobject | property ) WS? DOT WS? property;

property_field:
    ( FNUMBER | FHTML | FUNIT | FLENGTH | FLIST_COMPLEMENT | FRANGE);

literal_operand:
    (literal | tag_literal | range_literal_operand);
tag_literal:
    (tag_code (WS? AND WS? tag_code)*) | tag_value | list;
tag_code:
    CODE;
literal:
    ( STRING | DOUBLE | INTEGER | TRUE | FALSE | KNULL );
range_literal_operand:
    (OBJECT_IN WS? rangeLiteral WS? SEP WS? rangeLiteral WS? OBJECT_OUT);
rangeLiteral:
    ( DOUBLE | INTEGER | TIME | DATE );

//--------------------------------------------------------------------------------
// Coupling expression 
//--------------------------------------------------------------------------------
coupling:
    relationcoupling |
    objectcoupling ;

relationcoupling:
    property WS? ( DYNCOUPLING | TIGHCOUPLING ) WS? property;
objectcoupling:
    (object | predefinedobject) WS? ( DYNCOUPLING | TIGHCOUPLING ) WS? property;
predefinedobject:
    ( KORIGIN | KSOURCE | KNATURE | KENTITY | KPARENT | KTOP); 

//--------------------------------------------------------------------------------
// Definition element is a list or an object (formula not yet supported)
//--------------------------------------------------------------------------------
element:
    list |
    object |
    property |
    tag_value;

// A list is a series of other elements separated by , and inside curly brackets
list:
    LIST_IN WS? (meta WS)* element ( WS? SEP WS? element)* WS? LIST_OUT;

// A property is isolated by grammar since it plays a special role in coupling and calculation
property:
    OBJECT_IN (propertytype)? WS? (meta WS)* (primaryidentifier) (WS identifierelt)* WS? OBJECT_OUT;
propertytype:
    PROPERTY;

// Same isolation for tagvalue which plays a special role in calculation and actions
tag_value:
    OBJECT_IN (tagvaluetype)? WS? (meta WS)* (primaryidentifier) (WS identifierelt)* WS? OBJECT_OUT;
tagvaluetype:
    TAGVALUE;

// An object is any kind of definition between [ and ]
object: 
    OBJECT_IN objecttype WS? (meta WS)* (primaryidentifier) (WS identifierelt)* WS? OBJECT_OUT;
objecttype:
    (CXTUAL|RELR|TAGSR|VALUER|ENTITY|TAB|PROPERTYCOLL|CONTEXT|
    CATALOG|CLASSIFIER|RULE|USER|COLLECTION|TRACKING|ORGANIZATION|ENDPOINT|MAPPING|
    BPMNPROC|AUTHORIZATION|ROLEAUTH|TASK|EVENT|LANGCONF|SMARTDOC|TEMPLATE|GOLDEN_RULE|ROLE| TRANSLATION| COUPLING_SOURCE | PROPERTY_PERMISSION | RELATIONSHI_PPERMISSION | PERMISSION 
    ); // Excluding PROPERTY and USER

// Meta specification
meta:
    METACODE ( '=' STRING )?; 

identifierelt: 
    ( iidspec | catalogspec | organizationspec | localespec | typespec | sidespec | 
    contextspec | propertyspec | startspec | endspec | datespec | tagspec | 
    rangespec | rangeofspec | isversionablespec | targets_spec | endpointspec );

// all definition specifications
iidspec:
    SIID '=' INTEGER;
catalogspec:
    SCATALOG '=' primaryidentifier;
organizationspec:
    SORGANZIATION '=' ( primaryidentifier);
localespec:
    SLOCALE '=' ( LOCALEID | KBASELOCALE );
contextspec:
    SCONTEXT '=' primaryidentifier;
propertyspec:
    SPROPERTY '=' primaryidentifier;
typespec:
    STYPE '=' CODE;
sidespec:
    SSIDE '=' INTEGER;
startspec:
    SSTART '=' (INTEGER|DATETIME);
endspec:
    SEND '=' (INTEGER|DATETIME);
tagspec:
    STAG '=' ( tagcode )(WS? SEP WS? ( tagcode ))* ;
tagcode: ( CODE (WS? OBJECT_IN WS? INTEGER WS? OBJECT_OUT WS?)? );
rangespec:
    SRANGE '=' (MINUS WS*)?INTEGER;
rangeofspec:
    SRANGEOF '=' (MINUS WS*)?INTEGER WS? OR WS? (MINUS WS*)?INTEGER;
datespec:
    SDATE '=' ( KNOW | DATETIME | INTEGER );

isversionablespec:
    KISVER '=' ( TRUE | FALSE );
    
targets_spec:
KTARGET '=' ( TARGETS | INTEGER );
    
endpointspec:
    SENDPOINT '=' primaryidentifier;

// Primary identifier of object, can be a code, an integer code or $null
primaryidentifier: 
    ( CODE|USERCODE|KNULL|LOCALEID|KSTDO|INTEGER );

//--------------------------------------------------------------------------------
// lexer
//--------------------------------------------------------------------------------

// Literal constants
TRUE:           '$true';
FALSE:          '$false';

// Function keywords
FUNIQUE:        'unique';
FUPPER:         'upper';
FLOWER:         'lower';
FREPLACE:       'replace';
FADD:           'add';
FREMOVE:        'remove';
// Keywords which are not specification
KSOURCE:        '$source';
KENTITY:        '$entity';
KORIGIN:        '$origin';
KPARENT:        '$parent';
KTOP:           '$top';
KBASELOCALE:    '$baselocale';
KNATURE:        '$nature';
KISEMPTY:       '$empty';
KNOW:           '$now';
KANY:            '$any';
KWITHCHILDREN:   '$withchildren';
KNULL:           '$null';
KSTDO:           '$stdo';
KISVER:          '$isver';
KTARGET:    '$target';

// Identifier specifications
SIID:        '$iid';
SCATALOG:    '$ctlg';
SORGANZIATION: '$org';
SLOCALE:     '$locale';
SENDPOINT:   '$endpoint';
SPROPERTY:   '$prop';
SSIDE:       '$side';
SCONTEXT:    '$cxt';
// Selection specifications:
STAG:        '$tag';
SDATE:       '$date';
SSTART:      '$start';
SEND:        '$end';
SBASETYPE:   '$basetype';
SENTITYIID:   '$entityiid';
STYPE:       '$type';
SRANGE:      '$range';
SRANGEOF:    '$rangeof';

// Quality levels:
SGREEN:      '$green';
SYELLOW:     '$yellow';
SORANGE:     '$orange';
SRED:        '$red';
// Selection of base types:
SARTICLE:    '$article';
SEXTENSION:  '$extension';
SASSET:      '$asset';
SPID:        '$pid';
STEXTASSET:  '$textasset';
SVCATALOG:   '$vcatalog';
SSUPPLIER:   '$supplier';
STXAONOMY:   '$taxonomy';
STARGET:	 '$target';


// Entity fields (in entity filtering)
FQUALITY:       'quality';
FEXPIRED:       'expired';
FDUPLICATE:     'duplicate';

// Property fields (in calculation)
FNUMBER:         'number';
FHTML:           'html';
FUNIT:           'unit';
FLENGTH:         'length';
FLIST_COMPLEMENT:'complement'; 
FRANGE:		 'range';

// Runtime object types
CXTUAL: 'x>';
RELR:   'r>';
TAGSR: 't>';
VALUER: 'v>';
ENTITY: 'e>';
COLLECTION:'b>';
TRACKING: '!>';
// Configuration object types
TAB:     'W>';
PROPERTYCOLL: 'G>';
TAGVALUE:'T>';
USER:   'U>';
CONTEXT:'X>';
CATALOG:'C>';
CLASSIFIER:'c>';
PROPERTY:'P>';
RULE:'R>';
ORGANIZATION: 'O>';
ENDPOINT: 'I>';
MAPPING: 'M>';
BPMNPROC: 'B>';
AUTHORIZATION: 'A>';
ROLEAUTH: 'Z>';
TASK: 'K>';
EVENT: 'E>';
LANGCONF: 'L>';
SMARTDOC: 'D>';
TEMPLATE: 'Y>';
GOLDEN_RULE: 'S>';
TRANSLATION: 'N>';
ROLE:'Q>';
PROPERTY_PERMISSION : 'H>';
RELATIONSHI_PPERMISSION :'J>';
PERMISSION :'V>';
COUPLING_SOURCE : ':>';


// Rules and search operators
SELECT: 'select';
FOR:   'for';
WHEN:  'when';
WHERE: 'where';
IS:    'is';
IN:    'in';
UNDER: 'under';
HAS:   'has';
BELONGSTO:'belongsto';
BETWEEN: 'between';
THEN: 'then';
EXISTIN: 'existin';

// Action operators
ASSIGNS: ':=';
MOVETO: '=>';
RAISETO: '>>';
// Calculation operators
EQUALS: '=';
PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
MODULUS: '%';
CONCATENATE: '||';
LTE: '<=';
LT: '<';
GTE: '>=';
GT: '>';
DIFF: '<>';
LOR: 'or';
LAND: 'and';
LXOR: 'xor';
NOT: 'not';
LIKE: 'like'; // according to SQL syntax with % and _
REGEX: 'regex'; // according to java regex rules, $ and ^ are not supported
CONTAINS: 'contains'; // pure text search or entity filter by content for properties
NOTCONTAINS: 'notcontains'; // pure text search or entity filter by content for properties
INVOLVES: 'involves'; // entity filter by definition for properties 
RELATES: 'relates'; // search by lexeme

// Literals (order of token declaration counts as priority order)
// They necesseraly come after the definition of text operators -> so attribute code='relates' won't be recognized

LOCALEID: [a-z][a-z]'_'[A-Z][A-Z]'_'[A-Z][A-Z] | [a-z][a-z]'_'[A-Z][A-Z] | [a-z][a-z]'_'[0-9][0-9][0-9];

DATETIME: ( DATE (TIME)? | TIME );
DATE: [0-9][0-9][0-9][0-9]'-'[0-1][0-9]'-'[0-3][0-9] ;
TIME: 'T'[0-9][0-9]':'[0-9][0-9]':'[0-9][0-9] ;
DOUBLE : ([-])? ( [0-9]*'.'[0-9]+ | [0-9]+'.'[0-9]* ) ;
INTEGER: ([-])? [0-9]+ ;
METACODE: '@' CODE;
CODE   : [A-Za-z0-9:_#%$\-]+ ;
TARGETS  : [0-9>#]+ ;
USERCODE: '"'[A-Za-z0-9:_#%$\-][A-Za-z0-9:_#%$\-.&@ ]*'"' ;
STRING : '\'' ( '\'\'' | ~'\'' | [ \t\r\n] )* '\'' ;
// Property operators
OR: '|';
AND: '&';
SEP: ',';
DOT: '.';
COLUMN: ':';
DYNCOUPLING: '&.';
TIGHCOUPLING: '|.';

// Separator and key symbols
GROUP_IN: '(';
GROUP_OUT: ')';
LIST_IN: '{';
LIST_OUT: '}';
OBJECT_IN: '[';
OBJECT_OUT: ']';
WS: [ \t\r\n]+;
