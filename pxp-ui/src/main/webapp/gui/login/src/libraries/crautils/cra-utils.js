export const DEV = 'development';
export const PROD = 'production';

export const isDevMode = () => {
  return process.env.NODE_ENV && process.env.NODE_ENV === DEV;
};

export const isProdMode = () => {
  return process.env.NODE_ENV && process.env.NODE_ENV === PROD;
};
