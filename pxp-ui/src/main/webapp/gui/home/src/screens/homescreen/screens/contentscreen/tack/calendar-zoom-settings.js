export default {
  minZoom: 1,
  maxZoom: 5,
  defaultZoom: 2,

  divisors: {
    1: 1, //1 day, required for auto adjustment of zoom level when we select date range
    2: 7,
    3: 31, //max 31, 30, 28
    4: 92, //max case : 31+30+31
    5: 183, //max case : 31+30+31+30+31
    6: 366 //max case : leap year
  },


  //to be used while using Moment's add/subtract functions
  manipulators: {
    1: {
      value: 1,
      unit: 'days'
    },
    2: {
      value: 1,
      unit: 'weeks'
    },
    3: {
      value: 1,
      unit: 'months'
    },
    4: {
      value: 1,
      unit: 'quarters'
    },
    5: {
      value: 2,
      unit: 'quarters'
    },
    6: {
      value: 1,
      unit: 'years'
    }
  }
};

/**
 1 - day
 2 - week
 3 - month
 4 - quarter year
 5 - half year
 6 - year
 */