import React from 'react';
import ReactPropTypes from 'prop-types';
import { view as ClassConfigRelationshipView } from './class-config-relationship-view';
import { view as LazyMSSView } from '../../../../../viewlibraries/lazy-mss-view/lazy-mss-view';

const oEvents = {
};

const oPropTypes = {
  relationshipModel: ReactPropTypes.object,
  contextMSSViewModel: ReactPropTypes.object,
  referencedData: ReactPropTypes.object,
  activeKlass: ReactPropTypes.object,
  isClassDirty: ReactPropTypes.bool
};

// @CS.SafeComponent
class ClassConfigContextView extends React.Component {
  static propTypes = oPropTypes;

  getMSSView = () => {
    let oData = this.props.contextMSSViewModel;

    return <LazyMSSView
        isMultiSelect={oData.isMultiSelect}
        disabled={oData.disabled}
        label={oData.label}
        selectedItems={oData.selectedItems}
        context={oData.context}
        cannotRemove={oData.cannotRemove}
        showColor={true}
        onApply={oData.onApply}
        showCreateButton={oData.showCreateButton}
        isLoadMoreEnabled={oData.isLoadMoreEnabled}
        onPopOverOpen={oData.onPopOverOpen}
        searchHandler={oData.searchHandler}
        searchText={oData.searchText}
        loadMoreHandler={oData.loadMoreHandler}
        referencedData={oData.referencedData}
        requestResponseInfo={oData.requestResponseInfo}
    />
  }

  getContextRelationshipView = () => {
    var oRelationshipView = null;
    var oRelationshipModel = this.props.relationshipModel;
    if (oRelationshipModel) {
      oRelationshipView = (<ClassConfigRelationshipView
          relationshipLayoutModel={oRelationshipModel}
          referencedData={this.props.referencedData}
          activeKlass={this.props.activeKlass}
          isEntityDirty={this.props.isClassDirty}
      />);
    }
    return oRelationshipView;
  }

  render() {
    var oRelationshipView = this.getContextRelationshipView();
    let oMSSViewDOm = this.getMSSView();
    return (<div className="classConfigContextView">
      {oMSSViewDOm}
      {oRelationshipView}
    </div>);
  }
}

export const view = ClassConfigContextView;
export const events = oEvents;
