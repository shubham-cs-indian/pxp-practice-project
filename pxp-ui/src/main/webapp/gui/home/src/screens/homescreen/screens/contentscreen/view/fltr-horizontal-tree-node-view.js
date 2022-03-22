import CS from '../../../../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';

const oEvents = {
};

const oPropTypes = {
  nodeId: ReactPropTypes.string,
  headerView: ReactPropTypes.object,
  childrenViewArray: ReactPropTypes.array,
  affectedTreeNodeIds: ReactPropTypes.array,
};

// @CS.SafeComponent
class HorizontalTreeNodeView extends React.Component {
  static propTypes = oPropTypes;

  componentDidUpdate() {
  }

  shouldComponentUpdate(oNextProps, oNextState) {
    var sNodeId = oNextProps.nodeId;
    var aNewAffectedIds = oNextProps.affectedTreeNodeIds;

    if(CS.includes(aNewAffectedIds, sNodeId) || CS.isEmpty(aNewAffectedIds)) {
      return true;
    }

    return false;
  }

  render() {
    var __props = this.props;

    return (
        <div className="horizontalTreeGroupContainer expanded" key={__props.nodeId}>
          {__props.headerView}
          <div className="horizontalTreeGroupNodes">{__props.childrenViewArray}</div>
        </div>);
  }
}

export const view = HorizontalTreeNodeView;
export const events = oEvents;
