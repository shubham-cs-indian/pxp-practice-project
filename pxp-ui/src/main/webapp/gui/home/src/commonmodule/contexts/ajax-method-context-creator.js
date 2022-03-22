import React from 'react';

export const ContextList = React.createContext(
  {
    postMethodToCall: null,
    getMethodToCall: null,
    putMethodToCall: null,
    deleteMethodToCall: null
  }
);