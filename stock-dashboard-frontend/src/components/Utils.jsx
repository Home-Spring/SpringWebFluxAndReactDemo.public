import { createContext } from "react";

export const randomDelay = (max) => {
    return Math.floor(Math.random() * max)
};

export const ConfigContext = createContext();

ConfigContext.displayName = 'Config Context';