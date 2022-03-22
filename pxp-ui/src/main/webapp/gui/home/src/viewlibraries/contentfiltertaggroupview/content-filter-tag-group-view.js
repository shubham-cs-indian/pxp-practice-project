import CS from '../../libraries/cs/index';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as TagGroupView } from '../taggroupview/tag-group-view.js';
import ViewUtils from '../utils/view-library-utils';
import CommonUtils from "../../commonmodule/util/common-utils";

var oEvents = {};

const oPropTypes = {
  masterTag: ReactPropTypes.object,
  filterTags: ReactPropTypes.object,
  extraData: ReactPropTypes.object
};

// @CS.SafeComponent
class ContentTagItemView extends React.Component {
  static propTypes = oPropTypes;

/*
  createTagValues = (aTagValues) => {
    return CS.map(aTagValues, function (oTagValue) {
      return {
        id: oTagValue.relevance,
        label: CS.getLabelOrCode(oTagValue)
      }
    });
  };

*/


  getTagView = () => {
    // var oTagGroupModel = this.getTagGroupModel();
    let oMasterTag = this.props.masterTag;

    /**Here entityTag is not available so creating dummy entity tag */
    let oTags = {}
    oTags[oMasterTag["id"]] = CommonUtils.createDummyEntityTagForTagGroupModel(oMasterTag);
    let oExtraData = this.props.extraData;

    let oTagGroupModel = ViewUtils.getTagGroupModels(oMasterTag, {tags: oTags}, {}, "contentFilterTagGroupView", {}, oExtraData.filterContext, oExtraData, this.props.filterTags);
    let oProperties = oTagGroupModel.tagGroupModel.properties;
    return (
        <TagGroupView
            tagGroupModel={oTagGroupModel.tagGroupModel}
            tagRanges={oProperties.tagRanges}
            tagValues={oTagGroupModel.tagValues}
            disabled={oTagGroupModel.disabled}
            singleSelect={oProperties.singleSelect}
            isDoubleSlider={oTagGroupModel.isDoubleSlider}
            extraData={oTagGroupModel.extraData}
            masterTagList={[oMasterTag]}
            showDefaultIcon={this.props.showDefaultIcon}
        />
    );
  };

  render() {
    return (
        <div className="contentTagItemViewContainer">
          {this.getTagView()}
        </div>
    );
  }
}

export const view = ContentTagItemView;
export const events = oEvents;
