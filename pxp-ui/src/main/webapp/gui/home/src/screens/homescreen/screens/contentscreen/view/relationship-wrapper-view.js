import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as EntityDetailRelationshipSectionView } from './entity-detail-relationship-section-view';
import { view as NatureSectionView } from './nature-section-view';
import ContentScreenViewContextConstants from '../tack/content-screen-view-context-constants';

const oEvents = {};

const oPropTypes = {
  relationshipViewData: ReactPropTypes.object,
  viewContext: ReactPropTypes.string
};

// @CS.SafeComponent
class RelationShipWrapperView extends React.Component {
  static propTypes = oPropTypes;

  render () {
    let __props = this.props;
    let oRelationsipViewData = __props.relationshipViewData;
    let oRelationshipView = null;
    switch (__props.viewContext) {
      case ContentScreenViewContextConstants.RELATIONSHIP:
        oRelationshipView = (<EntityDetailRelationshipSectionView {...oRelationsipViewData}/>);
        break;
      case ContentScreenViewContextConstants.NATURE_RELATIONSHIP:
        oRelationshipView = (<NatureSectionView {...oRelationsipViewData}/>);
        break;
    }
    return (oRelationshipView);
  }
}


export const view = RelationShipWrapperView;
export const events = oEvents;
