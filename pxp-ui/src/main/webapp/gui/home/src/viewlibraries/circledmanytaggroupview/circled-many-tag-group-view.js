import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as CircledTagGroupView } from '../circledtaggroupview/circled-tag-group-view';
import CircledTagNodeModel from '../circledtaggroupview/model/circled-tag-node-model';

const oEvents = {
};

const oPropTypes = {
  masterTagGroups: ReactPropTypes.array,
  disabled: ReactPropTypes.bool,
  tagInstances: ReactPropTypes.array,
  context: ReactPropTypes.string
};
/**
 * @class CircledManyTagGroupView - Use for Circletags in the Application.
 * @memberOf Views
 * @property {array} [masterTagGroups] - this prop is  array which contain masterTag group value.
 * @property {bool} [disabled] boolean value for disabled class.
 * @property {array} [tagInstances] -  an array of applied tag values.
 * @property {string} [context] context name.
 */

// @CS.SafeComponent
class CircledManyTagGroupView extends React.Component{
  constructor(props) {
    super(props);
  }

  getChildrenModels =(oTagGroup)=> {
    var aChildren = oTagGroup.children;
    var aChildrenModel = [];

    var aAppliedTags = this.props.tagInstances;
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
    var aTags = this.props.masterTagGroups;
    var bDisabled = this.props.disabled;
    var sContext = this.props.context;

    CS.forEach(aTags, function (oTagGroup, iIndex) {
      var aChildrenModels = _this.getChildrenModels(oTagGroup);
      var oModel = new CircledTagNodeModel(oTagGroup.id, CS.getLabelOrCode(oTagGroup), oTagGroup.iconKey, oTagGroup.color, oTagGroup.type, 0, aChildrenModels, {});
      aTagGroupViews.push(<CircledTagGroupView context={sContext} circledTagGroupModel={oModel} key={iIndex} disabled={bDisabled}/>);
    });

    return aTagGroupViews;
  }

  render() {
    var aTagGroupViews = this.getTagGroupViews();

    return (
        <div className="variantTagsEditContainer">
          {aTagGroupViews || null}
        </div>);
  }
}

CircledManyTagGroupView.propTypes = oPropTypes;

export const view = CircledManyTagGroupView;
export const events = oEvents;
