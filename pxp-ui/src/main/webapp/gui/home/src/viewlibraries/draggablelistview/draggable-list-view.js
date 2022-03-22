import CS from '../../libraries/cs';
import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ListNodeView } from './draggable-list-node-view';
import ListViewModel from './model/draggable-list-view-model';

const oPropTypes = {
  model: ReactPropTypes.arrayOf(ReactPropTypes.instanceOf(ListViewModel)).isRequired
};
/**
 * @class DraggableListView
 * @memberOf Views
 * @property {array} model - Contains items.
 */

// @CS.SafeComponent
class DraggableListView extends React.Component {

  constructor(props) {
    super(props);
  }

  getListNodeView =()=> {
    var aListNodeView = [];
    CS.forEach(this.props.model, function (oModel, iIndex) {
      aListNodeView.push(<ListNodeView key={oModel.label+"_"+iIndex} model={oModel}/>);
    });
    return aListNodeView;
  }

  render() {
    var aListNodeView = this.getListNodeView();

    return (
        <div className="listView">
          {aListNodeView}
        </div>
    );
  }
}

DraggableListView.propTypes = oPropTypes;

export const view = DraggableListView;
export const events = {};
