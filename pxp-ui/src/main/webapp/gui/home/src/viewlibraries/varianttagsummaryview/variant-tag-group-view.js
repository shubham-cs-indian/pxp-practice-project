import React from 'react';
import CS from '../../libraries/cs';
import ReactPropTypes from 'prop-types';
import { view as CircledTagGroupView } from './../circledtaggroupview/circled-tag-group-view';
import CircledTagNodeModel from './../circledtaggroupview/model/circled-tag-node-model';
import { view as CustomDatePickerView } from '../customdatepickerview/customdatepickerview.js';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher';
import { view as VariantLinkedEntitiesView } from '../variantlinkedentitiesview/variant-linked-entities-view';
import { getTranslations as getTranslation } from '../../commonmodule/store/helper/translation-manager.js';
import oFilterPropType from './../../screens/homescreen/screens/contentscreen/tack/filter-prop-type-constants';

const oEvents = {
  VARIANT_TAG_GROUP_VIEW_DATE_VALUE_CHANGED: "variant_tag_group_view_date_value_changed"
};

const oPropTypes = {
  variantSectionViewData: ReactPropTypes.object,
  canEdit: ReactPropTypes.bool,
  canDelete: ReactPropTypes.bool
};
/**
 * @class VariantTagGroupView - Use to Display Different variant tag group to relationship tab.
 * @memberOf Views
 * @property {object} [variantSectionViewData] -  object of variantSectionViewData.
 * @property {bool} [canEdit] - boolean value of canEdit the variant active or not.
 * @property {bool} [canDelete] - boolean value of canDelete the variant active or not.
 */

class VariantTagGroupView extends React.Component {

  constructor (props) {
    super(props);
  }

  handleVariantDateFieldChanged =(sKey, oNull, oDate)=> {
    var sValue = oDate ? oDate.getTime() : "";
    EventBus.dispatch(oEvents.VARIANT_TAG_GROUP_VIEW_DATE_VALUE_CHANGED, sKey, sValue);
  }


  getVariantToAndFromView =(oContext)=> {
    if(oContext.isTimeEnabled) {
      var oVariantSectionViewData = this.props.variantSectionViewData;
      var oDateRangeSelectorData = oVariantSectionViewData.dateRangeSelectorData || {};
      var oVariantObject = CS.isEmpty((oVariantSectionViewData.dummyVariant)) ? oVariantSectionViewData.activeVariant : oVariantSectionViewData.dummyVariant;
      if(oVariantSectionViewData.isVariantDialogOpen) {
        oVariantObject = oVariantSectionViewData.editableVariant;
      }
      var oStyle = {/*width: "40%", lineHeight: "4"*/};
      var oFromDate = undefined;
      var oToDate = undefined;
      var oMinToDate = undefined;
      var oMaxFromDate = undefined;
      var bDisablePast = oDateRangeSelectorData.disablePast;

      if(oVariantObject.timeRange){
        oFromDate = (oVariantObject.timeRange.from) ? new Date(oVariantObject.timeRange.from) : undefined;
        oToDate = (oVariantObject.timeRange.to) ? new Date(oVariantObject.timeRange.to) : undefined;
        oMaxFromDate = oToDate || undefined;
        oMinToDate = oFromDate || undefined;
      }
      var oTextFieldStyle = {
        "width": "100%",
        "height": "30px"
      };
      var oUnderlineStyle = {};
      return (<div className="variantSectionViewVariantLabelAndDateFieldsContainer">
            <div className="variantDateLabelContainer">
              <div className="variantSectionViewVariantLabelDateKey">
                {getTranslation().FROM}
              </div>
              <div className="variantDateFieldWrapper">
                <CustomDatePickerView
                    value={oFromDate}
                    maxDate={oMaxFromDate}
                    textFieldStyle={oTextFieldStyle}
                    underlineStyle={oUnderlineStyle}
                    className="datePickerCustom"
                    disabled={!this.props.canEdit}
                    disablePast={bDisablePast}
                    onChange={this.handleVariantDateFieldChanged.bind(this, "from")}
                    style={oStyle}
                />
              </div>
            </div>
            <div className="variantDateLabelContainer">
              <div className="variantSectionViewVariantLabelDateKey">
                {getTranslation().TO}
              </div>
              <div className="variantDateFieldWrapper">
                <CustomDatePickerView
                    value={oToDate}
                    minDate={oMinToDate}
                    textFieldStyle={oTextFieldStyle}
                    underlineStyle={oUnderlineStyle}
                    allowInfinity={true}
                    className="datePickerCustom"
                    onChange={this.handleVariantDateFieldChanged.bind(this, "to")}
                    style={oStyle}
                    endOfDay={true}
                    disabled={!this.props.canEdit}
                    disablePast={bDisablePast}
                />
              </div>
            </div>
          </div>
      );
    }
    return null;
  }

  getEntityTagsForDrawerView =()=> {
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var oDummyVariant = oVariantSectionViewData.dummyVariant; //For newlycreated
    var sVariantInstanceId = oVariantSectionViewData.variantInstanceId;
    var aVariantTag = CS.isEmpty(oDummyVariant) ? (sVariantInstanceId ? oVariantSectionViewData.variantTags : []) : oDummyVariant.tags;
    if(oVariantSectionViewData.isVariantDialogOpen) {
      aVariantTag = oVariantSectionViewData.editableVariant.tags;
    }
    return aVariantTag;
  }

  getChildrenModels =(oTagGroup)=> {
    var aChildren = oTagGroup.children;
    var aChildrenModel = [];

    var aAppliedTags = this.getEntityTagsForDrawerView();
    var oAppliedTagGroup = CS.find(aAppliedTags, {tagId: oTagGroup.id});

    CS.forEach(aChildren, function (oChild) {
      var iRelevance = 0;

      if(oAppliedTagGroup){
        var oTagValue = CS.find(oAppliedTagGroup.tagValues, {tagId: oChild.id});
        iRelevance = oTagValue ? oTagValue.relevance : 0;
      }

      var oModel = new CircledTagNodeModel(oChild.id, CS.getLabelOrCode(oChild), oChild.iconKey, oChild.color, oChild.type, iRelevance, 0, [], {});
      aChildrenModel.push(oModel);
    });

    return aChildrenModel;
  }

  getTagGroupViews =()=> {
    var _this = this;
    var aTagGroupViews = [];
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var oSelectedContext = oVariantSectionViewData.selectedContext;
    var oSelectedVisibleContext = oVariantSectionViewData.selectedVisibleContext;
    let sViewContext = oVariantSectionViewData.viewContext || "";

    var bIsVariantDialogOpened = oVariantSectionViewData.isVariantDialogOpen;
    var oDummyVariant = oVariantSectionViewData.dummyVariant;
    var bIsDummy = !CS.isEmpty(oDummyVariant);
    var aTags = oSelectedContext ? oSelectedContext.tags : [];
    if((bIsDummy && !oVariantSectionViewData.isProductVariant) || bIsVariantDialogOpened ||
        (oSelectedVisibleContext && (oSelectedVisibleContext.type == "promotionalVersion"))) {
      aTags = oSelectedVisibleContext.tags;
    }
    var bCanEdit = this.props.canEdit;
    //var bEditVariantTags = oVariantSectionViewData.editVariantTags;
    // var bEditMode = (bCanEdit || bIsDummy || bEditVariantTags || bIsVariantDialogOpened);

    let oCircleTagModelProperties = {
      selectedContext: oSelectedContext
    };
    CS.forEach(aTags, function (oTagGroup, iIndex) {
      var aChildrenModels = _this.getChildrenModels(oTagGroup);
      var oModel = new CircledTagNodeModel(oTagGroup.id, CS.getLabelOrCode(oTagGroup), oTagGroup.iconKey, oTagGroup.color, oTagGroup.type, 0, aChildrenModels, oCircleTagModelProperties);
      aTagGroupViews.push(<CircledTagGroupView circledTagGroupModel={oModel} key={iIndex} disabled={!bCanEdit}
                                               context={sViewContext}/>);
    });

    return !CS.isEmpty(aTagGroupViews) ? aTagGroupViews : null;
  }

  getVariantLinkedEntitiesView =()=> {
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var oSelectedContext = oVariantSectionViewData.selectedContext;
    var oSelectedVisibleContext = oVariantSectionViewData.selectedVisibleContext;
    var aEntities = oSelectedVisibleContext.entities;

    if(CS.isEmpty(oSelectedVisibleContext)) {
      aEntities = oSelectedContext.entities;
    }

    if(CS.isEmpty(aEntities)){
      return null;
    }

    let oFilterContext = {
      filterType: oFilterPropType.MODULE,
      screenContext: oSelectedContext.id || oSelectedVisibleContext.id
    }
    return (<VariantLinkedEntitiesView variantSectionViewData={oVariantSectionViewData}
                                       canEdit={this.props.canEdit}
                                       filterContext={oFilterContext}
                                       isInstanceRemovable={this.props.canDelete}/>);
  }

  render() {
    var aTagGroupViews = this.getTagGroupViews();
    var oVariantSectionViewData = this.props.variantSectionViewData;
    var oSelectedContext = oVariantSectionViewData.selectedContext;
    var oSelectedVisibleContext = oVariantSectionViewData.selectedVisibleContext;
    var bIsVariantDialogOpened = oVariantSectionViewData.isVariantDialogOpen;
    var oDummyVariant = oVariantSectionViewData.dummyVariant;
    var bIsDummy = !CS.isEmpty(oDummyVariant);
    var bIsProductVariant = oVariantSectionViewData.isProductVariant;
    var oContext = oSelectedContext;
    if((bIsDummy && !bIsProductVariant) || bIsVariantDialogOpened) {
      oContext = oSelectedVisibleContext;
    }
    var oVariantToAndFromView = this.getVariantToAndFromView(oContext);
    var oVariantLinkedEntitiesView = this.getVariantLinkedEntitiesView();

    if(CS.isEmpty(aTagGroupViews) && CS.isEmpty(oVariantToAndFromView) && CS.isEmpty(oVariantLinkedEntitiesView)){
      return null;
    }

    return (
        <div className="variantTagsEditContainer">
          {oVariantToAndFromView}
          {aTagGroupViews}
          {oVariantLinkedEntitiesView}
        </div>);
  }
}

VariantTagGroupView.propTypes = oPropTypes;

export const view = VariantTagGroupView;
export const events = oEvents;