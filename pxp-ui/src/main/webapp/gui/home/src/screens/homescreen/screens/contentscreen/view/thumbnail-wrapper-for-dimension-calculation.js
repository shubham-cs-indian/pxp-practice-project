import React from 'react';

const oEvents = {
};

const oPropTypes = {};

// @CS.SafeComponent
class ContentSearchView extends React.Component {
  static propTypes = oPropTypes;

  componentDidMount() {
  }

  render() {

    return (
        <div className="thumbnailWrapperForDimension">
          {this.props.children}
        </div>
    );
  }
}

export const view = ContentSearchView;
export const events = oEvents;
