import CS from '../../../libraries/cs';
import MasterTagListContext from '../../../commonmodule/HOC/master-tag-list-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import TooltipView from './../../tooltipview/tooltip-view';
import ListItemModel from './../model/detailed-list-item-model';
import { view as ImageFitToContainerView } from '../../imagefittocontainerview/image-fit-to-container-view';
import ViewUtils from '../../utils/view-library-utils';

const oEvents = {};

const oPropTypes = {
  getLongDescription: ReactPropTypes.bool,
  getShortDescription: ReactPropTypes.bool,
  getTags: ReactPropTypes.bool,
  listItemModel: ReactPropTypes.instanceOf(ListItemModel).isRequired,
  masterTagListFromContext: ReactPropTypes.object
};
/**
 * @class ListAdditionalInformationView - use for Display list Additional Informationview.
 * @memberOf Views
 * @property {bool} [getLongDescription] -  boolean which is used for getLongDescription or not.
 * @property {bool} [getShortDescription] -  boolean which is used for getShortDescription or not.
 * @property {bool} [getTags] -  boolean which is used for getTags or not.
 * @property {custom} listItemModel - pass listModels.
 */

// @MasterTagListContext
// @CS.SafeComponent
class ListAdditionalInformationView extends React.Component {

  constructor(props) {
    super(props);
  }

  getTagListView =()=> {
    var aTagList = [];
    var _this = this;
    var aTagGroups = this.props.listItemModel.tags;

    CS.forEach(aTagGroups, function(oTagGroup){
      var aTagValues = oTagGroup.tagValues;
      var aMasterTagList = _this.props.masterTagListFromContext;
      var oTagGroupMaster = ViewUtils.getNodeFromTreeListById(aMasterTagList, oTagGroup.tagId);

      CS.forEach(aTagValues, function (oTagValue, iIndex) {
        var oTagMaster = ViewUtils.getNodeFromTreeListById(aMasterTagList, oTagValue.tagId);
        if (!CS.isEmpty(oTagMaster)) {
          if (oTagValue.relevance != 0) {
            var sColorClass = (oTagValue.relevance > 0) ? "greenBorder" : "redBorder";
            var sLabel = CS.getLabelOrCode(oTagGroupMaster) + ": " + CS.getLabelOrCode(oTagMaster);
            if (!CS.isEmpty(oTagMaster.klass)) {
              sLabel += " (" + oTagMaster.klass + ")";
            }
            var sTagName = CS.getLabelOrCode(oTagMaster);

            var aTagDetailView = [];
            var sRandom = Math.random()*1000;
            if (oTagMaster.icon) {
              var sIcon = ViewUtils.getIconUrl(oTagMaster.icon);
              aTagDetailView.push(<TooltipView placement="top" label={sLabel} key={oTagMaster.id + sRandom + "_overlay"}>
                <div className={"tagIcon " + sColorClass}>
                  <ImageFitToContainerView imageSrc={sIcon}/>
                </div></TooltipView>);
            } else if (oTagMaster.color) {
              var oStyle = {};
              oStyle.backgroundColor = oTagMaster.color;
              aTagDetailView.push(<div className={"tagColor " + sColorClass} style={oStyle} title={sLabel}
                                       key={oTagMaster.id + sRandom + "_overlay"}></div>);
            } else if (oTagMaster.klass) {
              aTagDetailView.push(<div className={"tagValue " + sColorClass} title={sTagName}
                                       key={oTagMaster.id + sRandom + "_overlay"}>{oTagMaster.klass}</div>);
            } else {
              aTagDetailView.push(<div className={"tagValue " + sColorClass} title={sTagName}
                                       key={oTagMaster.id + sRandom + "_overlay"}>{CS.getLabelOrCode(oTagMaster)}</div>);
            }

            aTagList.push(
                <div className="tagContainer">
                  <div className="coloredIndicator"></div>
                  {aTagDetailView}
                </div>
            );
          }
        }
      });
      /*CS.forEach(aTagValues, function (oTagValue, iIndex) {
        var oTagMaster = ViewLibraryUtils.getNodeFromTreeListById(aMasterTagList, oTagValue.tagId);
        if (oTagValue.relevance != 0) {
          var sColorClass = (oTagValue.relevance > 0) ? "greenBorder" : "redBorder";
          var sLabel = oTagGroupMaster.label + ": " + oTagMaster.label ;
          if(!CS.isEmpty(oTagMaster.klass)){
            sLabel += " (" + oTagMaster.klass + ")";
          }
          aTagList.push(
              <div className="tagContainer">
                <div className="coloredIndicator"></div>
                <TooltipView placement="bottom" label={sLabel} key={iIndex}>
                  <div className={"tagValue " + sColorClass}>{oTagMaster.label}</div>
                </TooltipView>
              </div>
          );
        }
      });*/
    });

    return aTagList;
  }

  render() {

    var oModel = this.props.listItemModel;

    var oEntity = oModel.properties['entity'];
    var aAttributes = oEntity.attributes;

    //long description
    var oLongDescriptionSection = null;
    if (this.props.getLongDescription) {
      var oLongDescriptionAttribute = CS.find(aAttributes, {attributeId: "longdescriptionattribute"});
      if (!CS.isEmpty(oLongDescriptionAttribute)) {
        var sLongDescription = oLongDescriptionAttribute.value;
        if (!CS.isEmpty(sLongDescription)) {
          oLongDescriptionSection = (
              <div className="longDescription">{sLongDescription}</div>
          );
        }
      }
    }

    //short description
    var oShortDescriptionSection = null;
    if (this.props.getShortDescription) {
      var oShortDescriptionAttribute = CS.find(aAttributes, {attributeId: "shortdescriptionattribute"});
      if (!CS.isEmpty(oShortDescriptionAttribute)) {
        var sShortDescription = oShortDescriptionAttribute.value;
        if (!CS.isEmpty(sShortDescription)) {
          oShortDescriptionSection = (
              <div className="shortDescription">{sShortDescription}</div>
          );
        }
      }
    }

    //tags
    var oTagsSection = null;
    if (this.props.getTags) {
      var oTagListViews = this.getTagListView();
      oTagsSection = <div className="tagList">{oTagListViews}</div>;
    }

    return (
        <div className="additionalInformationContainer">
          {oTagsSection}
          {oShortDescriptionSection}
          {oLongDescriptionSection}
        </div>
    );
  }
}

ListAdditionalInformationView.propTypes = oPropTypes;



export const view = MasterTagListContext(ListAdditionalInformationView);
export const events = oEvents;
