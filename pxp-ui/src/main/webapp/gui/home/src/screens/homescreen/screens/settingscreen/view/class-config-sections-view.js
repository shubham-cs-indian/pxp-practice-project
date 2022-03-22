import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../../../../libraries/eventdispatcher/EventDispatcher';
import { getTranslations as getTranslation } from '../../../../../commonmodule/store/helper/translation-manager.js';
import TooltipView from '../../../../../viewlibraries/tooltipview/tooltip-view';
import { view as ClassConfigSectionTabularView } from './../../../../../viewlibraries/sectionTabularView/class-config-section-tabular-view';
import { view as LazyContextMenuView } from './../../../../../viewlibraries/lazycontextmenuview/lazy-context-menu-view';
import { view as PropertyCollectionGridElementView } from './property-collection-grid-element-view';
import ViewUtils from './utils/view-utils';
import MockDataForTagTypes from '../../../../../commonmodule/tack/mock-data-for-tag-types';
import { defaultCouplingTypes as MockDataForCouplingTypes } from '../../../../../commonmodule/tack/version-variant-coupling-types';
import MockAttributeTypes from './../../../../../commonmodule/tack/mock-data-for-attribute-types';
import MetricCheckConstants from '../../../../../commonmodule/tack/measurement-metrics-constants';
import MockDataForMandatoryFields from './../../../../../commonmodule/tack/mock-data-for-mandatory-fields';
import TagTypeConstants from '../../../../../commonmodule/tack/tag-type-constants';
import MockDataForTagTypesNew from '../../../../../commonmodule/tack/mock-data-for-tag-types-new';
import CouplingConstants from '../../../../../commonmodule/tack/coupling-constans';
import NonVersionableStandardAttributes from '../tack/mock/versionable-standard-attributes';
import { view as ClassConfigSectionExportSide2RelationshipView } from './../../../../../viewlibraries/sectionTabularView/class-config-section-export-side-2-relationship-view';
import ExportSide2RelationshipDictionary from '../../../../../commonmodule/tack/export-side2-relationship-dictionary';

const oEvents = {
  CLASS_CONFIG_SECTION_ADDED: "class_config_section_added",
  GRID_PROPERTY_COLLECTION_ICON_CLICKED: "grid_property_collection_icon_clicked",
  CLASS_CONFIG_SECTION_ELEMENT_CHECKBOX_TOGGLED: "class_config_section_element_checkbox_toggled",
  CLASS_CONFIG_SECTION_ELEMENT_INPUT_CHANGED: "class_config_section_element_input_changed",
  CLASS_CONFIG_SECTION_ELEMENT_MSS_CHANGED: "class_config_section_element_mss_changed",
  CLASS_CONFIG_SECTION_TOOLBAR_ICON_CLICKED: "class_config_section_toolbar_icon_clicked",
  CLASS_CONFIG_SECTION_TOGGLE_BUTTON_CLICKED: "class_config_section_toggle_button_clicked",
  VISUAL_ELEMENT_BLOCKER_CLICKED: "visual_element_blocker_clicked",
  HANDLE_TAG_DIALOG_CLOSE_CLICKED: "handle_tag_dialog_close_clicked",
};

const oPropTypes = {
  referencedContexts: ReactPropTypes.object,
  sections: ReactPropTypes.array,
  propertyCollectionModels: ReactPropTypes.array,
  useGrid: ReactPropTypes.bool,
  context: ReactPropTypes.string,
  isNature: ReactPropTypes.bool,
  side2CollapseEnabled: ReactPropTypes.object,
  showExportSide2RelationshipView: ReactPropTypes.bool,
};

// @CS.SafeComponent
class ClassConfigSectionsView extends React.Component {
  static propTypes = oPropTypes;

  addPropertyCollectionFromDropdown = (aSectionIds) => {
    var sContext = this.props.context;
    if(!CS.isEmpty(aSectionIds)) {
      EventBus.dispatch(oEvents.CLASS_CONFIG_SECTION_ADDED, aSectionIds, sContext);
    }
  };

  handleSectionElementInputCheckboxChanged = (sSectionId, sElementId, sProperty) => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_SECTION_ELEMENT_CHECKBOX_TOGGLED, sSectionId, sElementId, sProperty);
  };

  handleSectionElementInputChanged = (sSectionId, sElementId, sProperty, sNewValue) => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_SECTION_ELEMENT_INPUT_CHANGED, sSectionId, sElementId, sProperty, sNewValue);
  };

  handleSectionElementMSSValueChanged = (sSectionId, sElementId, sProperty, aNewValue) => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_SECTION_ELEMENT_MSS_CHANGED, sSectionId, sElementId, sProperty, aNewValue);
  };

  handleSectionToolbarIconChanged = (sContext, sSectionId, sIconType) => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_SECTION_TOOLBAR_ICON_CLICKED, sContext, sSectionId, sIconType);
  };

  handleSectionToggleButtonClicked = (sContext, sSectionId,) => {
    EventBus.dispatch(oEvents.CLASS_CONFIG_SECTION_TOGGLE_BUTTON_CLICKED, sContext, sSectionId);
  };

  handleVisualElementBlockedClicked = (oInfo, oEvent) => {
    EventBus.dispatch(oEvents.VISUAL_ELEMENT_BLOCKER_CLICKED, this, oEvent, oInfo);
  };

  handleTagDialogCloseClicked = (oTagGroupModel) => {
    EventBus.dispatch(oEvents.HANDLE_TAG_DIALOG_CLOSE_CLICKED, this, oTagGroupModel);
  };

  getAttributeTypeName = (sType) => {
    let oMockAttributeTypes = new MockAttributeTypes();
    var oAttribute = CS.find(oMockAttributeTypes, {value: sType}) || {};
    return oAttribute.name;
  };

  getGridElementHashMap = (oSection) => {
    var sSplitter = ViewUtils.getSplitter();
    var oRes = {};

    CS.forEach(oSection.elements, function (oEl) {
      var oPosition = oEl.startPosition || oEl.position;
      var iStartX = oPosition.x;
      var iStartY = oPosition.y;
      var sKey = iStartX + sSplitter + iStartY;
      oRes[sKey] = oEl;
      if (!oEl.data) {
        oEl.data = oEl[oEl.type];
      }
    });

    return oRes;
  };

  getSectionGridView = (oSection) => {
    var __props = this.props;
    var sSplitter = ViewUtils.getSplitter();
    var iRowCount = oSection.rows || 2;
    var iColCount = oSection.columns || 2;
    var aRowDOM = [];
    var iElWidth = 100 / iColCount;
    var oElMap = this.getGridElementHashMap(oSection);
    for (var i = 0; i < iRowCount; i++) {
      var aElementDom = [];
      for (var j = 0; j < iColCount; j++) {
        var style = {
          width: iElWidth + "%",
          position: "relative"
        };
        var sId = i + sSplitter + j;
        var oElDOM = (<span></span>);
        if (oElMap[sId]) {
          oElDOM = (<PropertyCollectionGridElementView
              element={oElMap[sId]}
              sectionId={oSection.id}
              context="propertycollection"
              notEditable={true}
          />)
        }

        var oElementDOM = (
            <div className="visualSectionElementDiv" style={style} key={j}>
              {oElDOM}
            </div>);
        aElementDom.push(oElementDOM);
      }
      aRowDOM.push(<div className="visualSectionElementsWrapper" key={i}>{aElementDom}</div>);
    }

    return (<div className="visualSectionRowsWrapper">{aRowDOM}</div>)
  };

  handlePropertyCollectionIconClicked = (sIconContext, sId) => {
    EventBus.dispatch(oEvents.GRID_PROPERTY_COLLECTION_ICON_CLICKED, this.props.context, sId, sIconContext);
  };

  getSectionsGridView = () => {
    var _this = this;
    var __props = _this.props;
    var sContext = __props.context;
    var aPropertyCollectionViews = [];

    CS.forEach(__props.sections, function (oSection, iIndex) {
      var aSectionMoveUpDownIconContainer = sContext == "relationships" ? null :
          [
            <div className="classPropertyCollectionIcon moveUp" key={"up" + oSection.id}
                 onClick={_this.handlePropertyCollectionIconClicked.bind(_this, "up", oSection.id)}></div>,
            <div className="classPropertyCollectionIcon moveDown" key={"down" + oSection.id}
                 onClick={_this.handlePropertyCollectionIconClicked.bind(_this, "down", oSection.id)}></div>
          ];
      var oPropertyCollectionGridView = (
        <div className="classConfigPropertyCollection" key={iIndex}>
          <div className="classPropertyCollectionHeader">
            <span className="classPropertyCollectionLabel">{oSection.label}</span>
            <div className="classPropertyCollectionIconContainer">
              {aSectionMoveUpDownIconContainer}
              <div className="classPropertyCollectionIcon remove" onClick={_this.handlePropertyCollectionIconClicked.bind(_this, "remove", oSection.id)}></div>
            </div>
          </div>
          <div className="visualSectionBody classSection">
            {_this.getSectionGridView(oSection)}
          </div>
        </div>
      );
      aPropertyCollectionViews.push(oPropertyCollectionGridView);
    });
    return aPropertyCollectionViews;
  };

  getMasterTagForSelectedTagValues = (oTag) => {
    let oClonedTag = CS.cloneDeep(oTag);
    oClonedTag.isMultiselect = true;
    oClonedTag.tagType = TagTypeConstants.YES_NEUTRAL_TAG_TYPE;
    return oClonedTag;
  };

  getFilteredRulerMasterTag = (oTag, aSelectedTagValues) => {
    if(!CS.isEmpty(aSelectedTagValues)) {
      let oClonedTag = CS.cloneDeep(oTag);
      let aTagValues = oClonedTag.children;

      CS.remove(aTagValues, function (oTagValue) {
        let oFoundAllowedTagValue = CS.find(aSelectedTagValues, {tagId: oTagValue.id});
        return CS.isEmpty(oFoundAllowedTagValue);
      });

      return oClonedTag;
    }

    return oTag;
  };

  getSectionsView = () => {
    var __props = this.props;
    var _this = this;
    var aSectionTabularViews = [];
    var aMasterTagList = [];
    CS.forEach(__props.sections, function (oSection) {
      var aFields = [];
      CS.forEach(oSection.elements, function (oElement) {
        var sType = oElement.type;
        var oProperty = oElement[sType] || {};
        oProperty.elementId = oElement.id;
        var oField = {
          id: oElement.id,
          label: oProperty.label,
          code: oProperty.code,
          isCutoffUI: oElement.isCutoffUI,
          isInheritedUI: oElement.isInheritedUI,
          type: oElement.type,
          masterEntity: oProperty
        };

        oField.isSkipped = oElement.isSkipped;
        if (oElement.type == "attribute") {
          let bIsTranslatable = oElement.attribute.isTranslatable;

          oField.isIdentifier = {
            value: oElement.isIdentifier,
            isDisabled: bIsTranslatable
          };
          var oAttributeContextModel = {};
          if (!(ViewUtils.isAttributeTypeCalculated(oProperty.type)
              || ViewUtils.isAttributeTypeConcatenated(oProperty.type)
              || ViewUtils.isAttributeTypeCoverflow(oProperty.type)
              || ViewUtils.isAttributeTypeName(oProperty.type)
              && !oElement.isIdentifier)) {
            let oReferencedContexts = __props.referencedContexts;
            oAttributeContextModel.selectedItems = oElement.attributeVariantContext && [oElement.attributeVariantContext] || [];
            oAttributeContextModel.referencedData = oReferencedContexts;
            oAttributeContextModel.searchText = __props.masterEntitiesForSection.entitySearchText || "";
            oAttributeContextModel.isLoadMoreEnabled = true;
            oAttributeContextModel.isMultiSelect = false;
            oAttributeContextModel.cannotRemove = false;
            oAttributeContextModel.requestResponseInfo = {requestType: "configData", entityName: "attributeVariantContexts"};

            oField.attributeVariantContext = oAttributeContextModel;
          }
          oField.isVariating = oElement.isVariating;
          oField.attributeId = oProperty.id;
          var oSelectValues = {
            id: oProperty.id,
            name: _this.getAttributeTypeName(oProperty.type)
          };
          if (!(ViewUtils.isAttributeTypeType(oProperty.type)
              || ViewUtils.isAttributeTypeTaxonomy(oProperty.type)
              || ViewUtils.isAttributeTypeSecondaryClasses(oProperty.type)
              || ViewUtils.isAttributeTypeConcatenated(oProperty.type)
              || ViewUtils.isAttributeTypeCoverflow(oProperty.type)
              || ViewUtils.isAttributeTypeCalculated(oProperty.type))) {
            oField.defaultValue = oElement.defaultValue;
            oField.defaultValueType = "text";

            if(!ViewUtils.isAttributeTypeCalculated(oProperty.type)){
              oField.isSortable = oElement.isSortable;
              oField.isFilterable = oElement.isFilterable;
              let aCouplingTypes = new MockDataForCouplingTypes();
              oField.couplingType = {
                selectedItems: [oElement.couplingType],
                items: bIsTranslatable ? CS.reject(aCouplingTypes, {id: CouplingConstants.DYNAMIC_COUPLED}) : aCouplingTypes,
                isDisabled: oElement.isIdentifier
              };

              var sValue = "can";
              if(oElement.isMandatory){
                sValue = "must";
              }
              else if(oElement.isShould){
                sValue = "should";
              }

              oField.mandatory = {
                selectedItems : [sValue],
                items : new MockDataForMandatoryFields(),
                isDisabled : oElement.isSkipped || oElement.isIdentifier
              };

              if (!CS.isEmpty(oField.attributeVariantContext) && !CS.isEmpty(oField.attributeVariantContext.selectedItems)) {
                oField.couplingType.isDisabled = true;
                oField.mandatory.isDisabled = true;
              }

            }
            else{
              oField.defaultUnit = oElement.defaultUnit || oElement.attribute.calculatedAttributeUnitAsHTML || oElement.attribute.calculatedAttributeUnit;
              if(oField.defaultUnit == null){
                delete oField.defaultValue;
              }
            }

          }

          if (ViewUtils.isAttributeTypeHtml(oProperty.type)) {
            oField.defaultValue = oElement.valueAsHtml;
          }

          if (ViewUtils.isAttributeTypeMeasurement(oProperty.type)) {
            oField.defaultUnit = oElement.defaultUnit;
          }

          if (ViewUtils.isAttributeTypeCoverflow(oProperty.type)) {
            delete oField.isIdentifier;
          }

          if (ViewUtils.isAttributeTypeNumber(oProperty.type) || ViewUtils.isAttributeTypeMeasurement(oProperty.type)
              || ViewUtils.isAttributeTypeCalculated(oProperty.type)) {
            var aPrecisionOptions = MetricCheckConstants.PRECISION_OPTIONS;
            var aPrecisionOptionObjects = [];
            CS.forEach(aPrecisionOptions, function (precision) {
              var oPrecision = {
                id: Number(precision),
                name: precision,
                label: precision
              };
              aPrecisionOptionObjects.push(oPrecision);
            });
            oField.precision = {
              selectedItems: [oElement.precision],
              items: aPrecisionOptionObjects
            };
          }

          if (ViewUtils.isAttributeTypeMeasurement(oProperty.type)) {
            oField.defaultUnit = oElement.defaultUnit;
          }

          if (ViewUtils.isAttributeTypeNumber(oProperty.type) || ViewUtils.isAttributeTypeMeasurement(oProperty.type)) {
            var aPrecisionOptions = MetricCheckConstants.PRECISION_OPTIONS;
            var aPrecisionOptionObjects = [];
            CS.forEach(aPrecisionOptions, function (precision) {
              var oPrecision = {
                id: Number(precision),
                name: precision,
                label: precision
              };
              aPrecisionOptionObjects.push(oPrecision);
            });
            oField.precision = {
              selectedItems: [oElement.precision],
              items: aPrecisionOptionObjects
            };
          }

          if(ViewUtils.isAttributeTypeCreatedOn(oProperty.type) ||
              ViewUtils.isAttributeTypeLastModified(oProperty.type) ||
              ViewUtils.isAttributeTypeUser(oProperty.type) || oElement.isIdentifier){
            delete oField.defaultValue;
          }
          oField.propertyType = {selectedItems: [oElement.attribute.id],
                                 items: [oSelectValues],
                                  isDisabled: true};

          if(!CS.isEmpty(oElement.context)){
            oField.context = oElement.context;
          }

          oField.isTranslatable = {
            value: bIsTranslatable,
            isDisabled: true
          };

          oField.isVersionable = {
            value: oElement.isVersionable,
            isDisabled: oElement.isIdentifier || CS.isNotEmpty(oElement.attributeVariantContext) || CS.includes(NonVersionableStandardAttributes, oElement.attribute.type)
          };
        }
        else if (oElement.type == "tag") {
          var oTag = oElement.tag;
          aMasterTagList.push(oTag);
          oField.tagId = oTag.id;
          oField.isFilterable = oElement.isFilterable;
          //oField.isMandatory = oElement.isMandatory;
          var sValue = "can";
          if(oElement.isMandatory){
            sValue = "must";
          }
          else if(oElement.isShould){
            sValue = "should";
          }
          oField.mandatory = {
            selectedItems : [sValue],
            items : new MockDataForMandatoryFields(),
            isDisabled : oElement.isSkipped
          };
          oField.propertyType = {selectedItems: [oElement.tagType],
            items: new MockDataForTagTypes()};

          var bIsBooleanTagType = (oElement.tagType === TagTypeConstants.TAG_TYPE_BOOLEAN);
          if (bIsBooleanTagType || oElement.tagType === TagTypeConstants.TAG_TYPE_MASTER) {
            oField.propertyType = {
              selectedItems: [oElement.tagType],
              items: new MockDataForTagTypes(),
              isDisabled: true
            };

            //boolean tag always has a value, 0 or 1. No need to make it mandatory
            oField.mandatory.isDisabled = bIsBooleanTagType;
          }
          else {
            let oTagTypes = new MockDataForTagTypesNew();
            let sTagType = oElement.tagType;
            let aTotalItems = [];

            let bIsLOVTypeTag = (sTagType === TagTypeConstants.YES_NEUTRAL_TAG_TYPE || sTagType === TagTypeConstants.YES_NEUTRAL_NO_TAG_TYPE ||
                sTagType === TagTypeConstants.RANGE_TAG_TYPE || sTagType === TagTypeConstants.RULER_TAG_TYPE);

            let bIsStatusTag = (sTagType === TagTypeConstants.LIFECYCLE_STATUS_TAG_TYPE || sTagType === TagTypeConstants.STATUS_TAG_TYPE || sTagType === TagTypeConstants.LISTING_STATUS_TAG_TYPE);

            if(bIsLOVTypeTag) {
              aTotalItems = oTagTypes.LOV;
            } else if (bIsStatusTag) {
              aTotalItems = oTagTypes.STATUS;
            }

            oField.propertyType = {
              selectedItems: [sTagType],
              items: aTotalItems
            };
          }

          oProperty.tagType = oElement.tagType; //in order to get relevant tag data according to type
          /** We passed isMultiselect with properties, because according to this default value of tag in tabular
           *  view get set.**/
          oProperty.isMultiselect = oElement.isMultiselect;

          let aSelectedTagValuesIds = [];
          CS.forEach(oElement.selectedTagValues, function (oSelectedTagValue) {
            aSelectedTagValuesIds.push(oSelectedTagValue.tagId);
          });

          var aTagValues = CS.clone(oElement.selectedTagValues);

          CS.map(aTagValues, function (oTagValue) {
            oTagValue.relevance = 100;
          });

          oField.selectedTagValuesType = "tagGroup";

          let oDummyEntityTag = {
            id: oProperty.id,
            tagId: oProperty.id,
            tagValues: aTagValues
          };
          let oTags = {
            [oDummyEntityTag['id']]: oDummyEntityTag
          };
          let oExtraData = {
            sectionId: oSection.id
          };
          oField.selectedTagValues = ViewUtils.getTagGroupModels(_this.getMasterTagForSelectedTagValues(oProperty),
              {tags: oTags}, oElement, "classConfigSelectedTagValues", {}, {}, oExtraData);

          /** To filter tag values according to allowed tag values only in case of ruler tag type because lazy loading is not implemented for ruler tag type **/
          let oMasterTag = (oElement.tagType === TagTypeConstants.RULER_TAG_TYPE) ? _this.getFilteredRulerMasterTag(oProperty, oElement.selectedTagValues): oProperty;
          if (oElement.tagType == TagTypeConstants.YES_NEUTRAL_TAG_TYPE) {
            /*******Default Value Will Only Be Visible in YES_NEUTRAL_TAG_TYPE********/
            oDummyEntityTag = {
              id: oProperty.id,
              baseType: "com.cs.runtime.interactor.entity.TagInstance",
              tagId: oProperty.id,
              tagValues:  CS.clone(oElement.defaultValue)
            };

            oTags = {
              [oDummyEntityTag['id']]: oDummyEntityTag
            };

            let oExtraData = {
              sectionId: oSection.id
            };
            oField.defaultValue = ViewUtils.getTagGroupModels(oMasterTag, {tags: oTags}, oElement,  "classConfigTagDefaultValue", {}, {}, oExtraData);
            oField.defaultValue.customRequestObject = {
              elementId: oElement.id
            };
            oField.defaultValueType = "tagGroup";
            oField.couplingType = {
              selectedItems: [oElement.couplingType],
              items: new MockDataForCouplingTypes()
            };
            oField.isMultiselect = oElement.isMultiselect;
          }
          oField.isVersionable = {
            value: oElement.isVersionable,
            isDisabled: oElement.isIdentifier || CS.isNotEmpty(oElement.attributeVariantContext)
          };
        }
        else if (oElement.type == "role") {
          //oField.isMandatory = oElement.isMandatory;  //mandatory field hide for roles
        }
        else if (oElement.type == "taxonomy") {
          oField.isMultiselect = oElement.isMultiselect;
        }
        else {

        }
        aFields.push(oField);
      });

      var fSectionElementCheckboxToggleHandler = _this.handleSectionElementInputCheckboxChanged;
      var fSectionElementInputChangeHandler = _this.handleSectionElementInputChanged;
      var fSectionToolbarIconChangeHandler = _this.handleSectionToolbarIconChanged;
      var fVisualElementBlockerClickHandler = _this.handleVisualElementBlockedClicked;
      var fTagDialogCloseClickHandler = _this.handleTagDialogCloseClicked;
      var fHandleSectionToggleButtonClicked = _this.handleSectionToggleButtonClicked;
      var fSectionElementMSSValueChanged = _this.handleSectionElementMSSValueChanged;
      var oMasterAttributeList = ViewUtils.getAttributeList();
      var bTagDialogVisible = ViewUtils.getTagDialogVisibility();
      aSectionTabularViews.push(
          <ClassConfigSectionTabularView
              fields={aFields}
              key={oSection.id}
              sectionId={oSection.id}
              sectionLabel={CS.getLabelOrCode(oSection)}
              context={_this.props.context}
              isInherited={oSection.isInherited}
              isSkipped={oSection.isSkipped}
              dragDetails={_this.props.dragDetails}
              isCollapsed={oSection.isCollapsedUI}
              sectionElementCheckboxToggleHandler={fSectionElementCheckboxToggleHandler}
              sectionElementMSSValueChanged={fSectionElementMSSValueChanged}
              sectionToolbarIconChangeHandler={fSectionToolbarIconChangeHandler}
              visualElementBlockerClickHandler={fVisualElementBlockerClickHandler}
              tagDialogCloseClickHandler={fTagDialogCloseClickHandler}
              sectionToggleButtonClicked={fHandleSectionToggleButtonClicked}
              masterAttributeList={oMasterAttributeList}
              isTagDialogVisible={bTagDialogVisible}
              masterTagList={aMasterTagList}
              sectionElementInputChangeHandler = {fSectionElementInputChangeHandler}
          />
      );
    });

    return aSectionTabularViews;
  };

  getRequestResponseInfo = () => {
    return {
      requestType: "configData",
      entityName: "propertyCollections"
    }
  }

  getSelectedItems = () => {
    return CS.map(this.props.sections, 'propertyCollectionId');
  }

  /* Export Side 2 Relationship Section View */
  getSide2RelationshipView = () => {
    let fHandleSectionToggleButtonClicked = this.handleSectionToggleButtonClicked;
    let oExportPropertiesSection = this.props.exportPropertiesSection;
    let oSide2CollapseEnabled = oExportPropertiesSection.side2CollapseEnabled;
    let sLabel = getTranslation().EXPORT_PROPERTIES_FOR_SIDE2_ENTITY;
    let oView =
        <div className="sectionsWrapper">
          <div className="classSectionHeader">{sLabel}</div>
          <ClassConfigSectionExportSide2RelationshipView
              isCollapsed={oSide2CollapseEnabled.relationshipList}
              isInherited={true}
              sectionLabel={getTranslation().RELATIONSHIP_FILTER}
              context={ExportSide2RelationshipDictionary.RELATIONSHIP_LIST}
              sectionToggleButtonClicked={fHandleSectionToggleButtonClicked}
              side2RelationshipData = {oExportPropertiesSection.side2RelationshipData.relationships}
          />
          <ClassConfigSectionExportSide2RelationshipView
              isCollapsed={oSide2CollapseEnabled.propertiesList}
              isInherited={true}
              sectionLabel={getTranslation().PROPERTIES}
              context={ExportSide2RelationshipDictionary.PROPERTIES_LIST}
              sectionToggleButtonClicked={fHandleSectionToggleButtonClicked}
              side2RelationshipData = {oExportPropertiesSection.side2RelationshipData.properties}
          />
        </div>
    return oView;
  };

  render() {
    var aSectionTabularViews = this.props.useGrid ? this.getSectionsGridView() : this.getSectionsView();
    var bIsMultiselect = true;
    var sClassName = "classConfigAddPropertyCollection ";
    var oAnchorOrigin = {horizontal: 'right', vertical: 'top'};
    var oTargetOrigin = {horizontal: 'right', vertical: 'bottom'};
    let oPopOverStyle = {
      "minWidth": "420px",
      "minHeight": "321px"
    };

    let oSide2Relationship = this.props.showExportSide2RelationshipView ? this.getSide2RelationshipView() : null;

    return (
        <div>
          <div className="sectionsWrapper">
            <div className="classSectionHeader">{getTranslation().PROPERTY_COLLECTION}</div>
            <LazyContextMenuView
                isMultiselect={bIsMultiselect}
                onApplyHandler={this.addPropertyCollectionFromDropdown}
                anchorOrigin={oAnchorOrigin}
                targetOrigin={oTargetOrigin}
                menuListHeight={"250px"}
                requestResponseInfo={this.getRequestResponseInfo()}
                selectedItems={this.getSelectedItems()}
                className={sClassName}
                popoverStyle={oPopOverStyle}
            >
              <TooltipView placement="bottom" label={"Add Property Collection"}>
                <div className="classAddPropertyCollection"></div>
              </TooltipView>
            </LazyContextMenuView>
            {aSectionTabularViews}
          </div>
          <div>
            {oSide2Relationship}
          </div>
        </div>
    );
  }
}

export const view = ClassConfigSectionsView;
export const events = oEvents;
