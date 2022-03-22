import CS from '../../libraries/cs';
import MasterTagListContext from '../../commonmodule/HOC/master-tag-list-context';
import React from 'react';
import ReactPropTypes from 'prop-types';
import ViewUtils from '../utils/view-library-utils';
import { view as ImageFitToContainerView } from '../imagefittocontainerview/image-fit-to-container-view';
import TooltipView from './../tooltipview/tooltip-view';

const oEvents = {
};

const oPropTypes = {
  tags: ReactPropTypes.array,
  masterTagListFromContext: ReactPropTypes.object
};
/**
 * @class CircularTagListView - use for content image wrapper view.
 * @memberOf Views
 * @property {array} [tags] -  an array of tags.
 */

// @MasterTagListContext
// @CS.SafeComponent
class CircularTagListView extends React.Component {
  constructor(props) {
    super(props);
  }

  //TODO: Create mutable object to work with shouldComponentUpdate
  /*shouldComponentUpdate(oNextProps) {
    return !CS.isEqual(oNextProps, this.props)
  }*/

  getTagListView =()=> {
    var aTagList = [];
    var aTagLabelList = [];
    var aTagGroups = this.props.tags;
    var aMasterTagList = this.props.masterTagListFromContext;

    CS.forEach(aTagGroups, function (oTagGroup) {
      var aTagValues = oTagGroup.tagValues;
      var oMasterTagGroup = CS.find(aMasterTagList, {id: oTagGroup.tagId});

      if(!CS.isEmpty(oMasterTagGroup)){

        CS.forEach(aTagValues, function (oTagValue) {

          if (oTagValue.relevance != 0) {
            var oTagMaster = CS.find(oMasterTagGroup.children, {id: oTagValue.tagId});

            if (!CS.isEmpty(oTagMaster)) {

              var oTagView = null;
              var sTagName = CS.getLabelOrCode(oTagMaster);
              aTagLabelList.push(sTagName);
              if (oTagMaster.icon) {
                var sIcon = ViewUtils.getIconUrl(oTagMaster.icon);
                oTagView = (<div className="tagIcon" title={sTagName}>
                  <ImageFitToContainerView imageSrc={sIcon}/>
                </div>);
              } else {
                var sTagShort = sTagName.substring(0, 2);
                oTagView = (
                    <TooltipView label={sTagName}>
                      <div className="tagValue">
                        {sTagShort}
                      </div>
                    </TooltipView>
                );
              }

              aTagList.push(oTagView);
            }
          }
        });
      }
    });

    var aTagViews = [];

    switch (aTagList.length) {

      case 0:
        return null;

      case 1:
        aTagViews = [
          <div className="whole">
            {aTagList[0]}
          </div>
        ];
        break;

      case 2:
        aTagViews = [
          <div className="semi">
            {aTagList[0]}
          </div>,
          <div className="semi">
            {aTagList[1]}
          </div>
        ];
        break;

      case 3:
        aTagViews = [
          <div className="tern">
            {aTagList[0]}
          </div>,
          <div className="tern">
            {aTagList[1]}
          </div>,
          <div className="tern">
            {aTagList[2]}
          </div>
        ];
        break;

      case 4:
        aTagViews = [
          <div className="quarter">
            {aTagList[0]}
          </div>,
          <div className="quarter">
            {aTagList[1]}
          </div>,
          <div className="quarter">
            {aTagList[2]}
          </div>,
          <div className="quarter">
            {aTagList[3]}
          </div>
        ];
        break;

      default:
        for (var i = 0; i < 3; i++) {
          if (aTagList[i]) {
            aTagViews.push(
                <div className="quarter">
                  {aTagList[i]}
                </div>
            );
          }
        }
        var iRemainingTags = CS.size(aTagList) - 3;
        if (iRemainingTags > 0) {
          var aRemainingTagsLabelList = CS.drop(aTagLabelList, 3);
          var sRemainingTagsLabel = CS.join(aRemainingTagsLabelList, ', ');
          aTagViews.push(
              <div className="quarter">
                <TooltipView label={sRemainingTagsLabel}>
                  <div className={"tagValue "}>{"+" + iRemainingTags}</div>
                </TooltipView>
              </div>
          );
        }
    }

    return (<div className="additionalInformationContainer">{aTagViews}</div>);
  }

  getCircularTagsView =()=> {
    var oTagsView = this.getTagListView();

    return (<div className="miniThumbnailCircularTagsContainer">
      {oTagsView}
    </div>);
  }

  render() {
    return (this.getCircularTagsView());
  }
}

CircularTagListView.propTypes = oPropTypes;



export const view = MasterTagListContext(CircularTagListView);
export const events = oEvents;
