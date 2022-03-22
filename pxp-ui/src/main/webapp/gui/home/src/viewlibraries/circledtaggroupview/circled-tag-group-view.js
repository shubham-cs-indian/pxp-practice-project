import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import EventBus from '../../libraries/eventdispatcher/EventDispatcher.js';
import { view as CircledTagNodeView } from './circled-tag-node-view';
import CircledTagNodeModel from './model/circled-tag-node-model';

const oEvents = {
  CIRCLED_TAG_NODE_CLICKED: "circled_tag_node_clicked",
  DISABLED_CIRCLED_TAG_NODE_CLICKED: "disabled_circled_tag_node_clicked"
};

const oPropTypes = {
  circledTagGroupModel: ReactPropTypes.instanceOf(CircledTagNodeModel).isRequired,
  disabled: ReactPropTypes.bool,
  context: ReactPropTypes.string
};
/**
 * @class CircledTagGroupView - Use for Circletags in the Application.
 * @memberOf Views
 * @property {custom} circledTagGroupModel -Pass data like id, label, icon, color, type, relevance, childrenModels,
 * properties
 * @property {bool} [disabled] - boolean for disabled class.
 * @property {string} [context] - context name entityDetailRelationSection.
 */

// @CS.SafeComponent
class CircledTagGroupView extends React.Component {

  constructor(props) {
    super(props);
  }
  // IMP: if any prop is added here, a check for it should also be added in shouldComponentUpdate

  handleClickOnTagNode=(sId)=> {
    var oModel = this.props.circledTagGroupModel;
    var sTagGroupId = oModel.id;
    var sContext = this.props.context;
    let oProperties = oModel.properties;
    let oExtraData = {selectedContext: oProperties.selectedContext};

    EventBus.dispatch(oEvents.CIRCLED_TAG_NODE_CLICKED, this, sTagGroupId, sId, sContext, oExtraData);
  }

  // Event not in use
  handleDisabledTagNodeClicked =()=> {
    EventBus.dispatch(oEvents.DISABLED_CIRCLED_TAG_NODE_CLICKED, this, this.props.context);
  }

  getTagNodeViews =()=> {
    var _this = this;
    var oModel = this.props.circledTagGroupModel;
    var sTagGroupId = oModel.id;
    var aChildrenModel = oModel.childrenModels;
    var aTagNodeView = [];
    var bIsDisabled = this.props.disabled;

    var fNodeClickHandler = bIsDisabled ? CS.noop : (bIsDisabled ? _this.handleDisabledTagNodeClicked : _this.handleClickOnTagNode);

    CS.forEach(aChildrenModel, function (oChildModel, sIndex) {
      aTagNodeView.push(<CircledTagNodeView
          key={sIndex}
          model={oChildModel}
          tagGroupId={sTagGroupId}
          disabled={bIsDisabled}
          handleNodeClicked={fNodeClickHandler}/>);
    });

    return aTagNodeView;
  }

  getView =()=> {
    var oModel = this.props.circledTagGroupModel;
    var sLabel = CS.getLabelOrCode(oModel);
    var aTagNodeViews = this.getTagNodeViews();

    return (<div className="circledTagGroupViewContainer">
      <div className="circledTagGroupViewLabel">{sLabel}</div>
      <div className="circledTagGroupViewItems">{aTagNodeViews}</div>
    </div>);
  }

  render() {
    return this.getView();
  }
}

CircledTagGroupView.propTypes = oPropTypes;

export const view = CircledTagGroupView;
export const events = oEvents;
