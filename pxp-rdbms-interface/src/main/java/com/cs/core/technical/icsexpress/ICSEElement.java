package com.cs.core.technical.icsexpress;

import com.cs.core.dataintegration.idto.IPXON;

/**
 * Generic expression of CSExpress language
 *
 * @author vallee
 */
public interface ICSEElement extends Comparable {

  /**
   * Definition for CS Expression definition export format SYSTEM: Only minimal identification with Code or IID e.g [e>8000001] USER: User
   * completed information e.g [e>PCSCREEN#45 $iid=8000001 $ctlg=pim $type=ARTICLE]
   */
  public enum ExportFormat {
    UNDEFINED, SYSTEM, USER; // Nota: USER format is not used in first version
  }

  // public list of keywords:
  public enum Keyword {
    // used for definition (order count in presentation)
    $iid, $name, $ctlg, $org, $prop, $side, 
    $locale, $cxt, $tag, $start, $end, $range, $rangeof, 
    $content, $search, $type, $date, $isver,
     // others
    $null, $any, $stdo, $baselocale, $withchildren,
    $entity, $nature, $parent, $top, $origin, $source,
    $green, $yellow, $orange, $red, $true, $false, $now, $target;
    ;
  }

  // Types of definition:
  public enum ElementType {
    META, LIST, PROPERTY, TAGVALUE, OBJECT, RANGE;
  }

  // Types of object:
  public enum CSEObjectType {
    Unknown('*'),
    // Runtime objects
    Entity('e'), ValueRecord('v'), TagsRecord('t'), RelationSideRecord('r'),
    ContextualObject('x'), Collection('b'), TaskRecord('k'), Tracking('!'),
    // Configuration objects
    Property('P'), Classifier('c'), TagValue('T'), Context('X'), Catalog('C'),
    Rule('R'), User('U'), PropertyCollection('G'), Tab('W'),
    Task('K'), Event('E'), Organization('O'), Endpoint('I'), Mapping('M'), BPMN('B'),
    UpdateAuthorization('A'), RoleAuthorization('Z'), LanguageConf('L'), SmartDoc('D'), Template('Y'), Golden_Rule('S'), Role('Q'),
    Translation('N'), CouplingSource(':'), PropertyPermission ('H'), RelationshipPermission('J'), Permission('V');
    

    private final char code;

    private CSEObjectType(char letter) {
      code = letter;
    }

    static public CSEObjectType valueOf(char letter) {
      for (CSEObjectType type : values()) {
        if (type.code == letter) {
          return type;
        }
      }
      return Unknown;
    }

    public char letter() {
      return code;
    }
  }

  /**
   * @param items the component of the list to be formated
   * @return a CSExpress string list expression from a series of elements
   */
  public static String toStringList(ICSEElement... items) {
    StringBuffer strList = new StringBuffer("{");
    // insert first metas and next other elements
    for (ICSEElement item : items) {
      if (item.getType() == ElementType.META) {
        strList.append(item.toString())
                .append(" ");
      }
    }
    for (ICSEElement item : items) {
      if (item.getType() != ElementType.META) {
        strList.append(item.toString())
                .append(",");
      }
    }
    strList.setCharAt(strList.length() - 1, '}');
    return strList.toString();
  }

  /**
   * @return the type of CSExpress element
   */
  public ElementType getType();

  /**
   * @param key the key of the meta (the first '@' can be optionally omitted)
   * @return the corresponding meta value or null if not existing
   */
  public ICSEMeta getMeta(String key);

  /**
   * @param cseMeta added meta information
   */
  public void addMeta(ICSEMeta cseMeta);

  /**
   * @param key a predefined key to be added as meta information
   * @param value the corresponding meta element or null if not required
   */
  public void addMeta(IPXON.PXONMeta key, String value);

  /**
   * @param key the key of the specification
   * @return the specification value or null if not required
   */
  public String getSpecification(Keyword key);

  /**
   * @param <E> enumeration class
   * @param enumType E.class
   * @param key the enumeration key of the specification
   * @return the specification value or empty if not defined
   */
  public <E extends Enum<E>> E getSpecification(Class<E> enumType, Keyword key);

  /**
   * @param key the key of the specification
   * @return true if that specification is defined
   */
  public boolean containsSpecification(Keyword key);

  /**
   * @return this identifier as String buffer
   */
  public StringBuffer toStringBuffer();

  @Override
  public String toString();

  /**
   * @return true when this is an empty identifier
   */
  public boolean isEmpty();
}
