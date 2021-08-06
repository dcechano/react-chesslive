import React, {useCallback, useState} from "react";

const AuthContext = React.createContext({
    username: '',
    isLoggedIn: false,
    token: null,
    login: (token, expirationDate, username) =>{},
    logout: () => {}
});

const calculateRemainingTime = (expirationTime) => {
    const currentTime = new Date().getTime();
    const adjExpirationTime = new Date(expirationTime).getTime();

    return adjExpirationTime - currentTime;
};

export const AuthContextProvider = props => {

    const [jwt, setJwt] = useState(null);
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [username, setUsername] = useState(null);

    const loginHandler = useCallback((token, expirationDate, username) => {
        setIsLoggedIn(true);
        setUsername(username);
        setJwt(token);
        localStorage.setItem('jwt', token);
        localStorage.setItem('expirationDate', expirationDate);
        localStorage.setItem('username', username);
        const remainingTime = calculateRemainingTime(expirationDate);
        //TODO remove log
        console.log(`Remaining Time: ${remainingTime}`);
        setTimeout(logoutHandler, remainingTime);
    }, []);

    const logoutHandler = useCallback(() => {
        console.log('Logging user out');
        setJwt(null);
        setIsLoggedIn(false);
        localStorage.removeItem('jwt');
        localStorage.removeItem('expirationDate');
        localStorage.removeItem('username');
    }, []);

    const context = {
        username: username,
        token: jwt,
        isLoggedIn: isLoggedIn,
        login: loginHandler,
        logout: logoutHandler
    }

    return <AuthContext.Provider value={context}>{props.children}</AuthContext.Provider>
};

export default AuthContext;