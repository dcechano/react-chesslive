import React, {useContext, useRef, useState} from 'react';

import classes from './Login.module.css';
import generalClasses from '../index.module.css';
import {useHistory} from "react-router-dom";
import AuthContext from "../store/auth-context";

const Login = props => {

    const context = useContext(AuthContext);

    const usernameRef = useRef();
    const passwordRef = useRef();
    const [error, setError] = useState(false);
    const [loading, isLoading] = useState(false);
    const history = useHistory();

    const onSubmitHandler = event => {
        event.preventDefault();
        setError(false);
        login();
        history.push('/chesslive');
    };

    const cancelButtonHandler = event => {
        history.push('/chesslive');
    };

    const login = async () => {
        try {
            const username = usernameRef.current.value;
            const password = passwordRef.current.value;
            const response = await fetch("http://localhost:8080/api/authenticate", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    username: username,
                    password: password
                })
            });

            if (!response.ok) {
                if (response.status === 403) {
                    setError(true);
                }
                return;
            }
            const body = await response.json();
            context.login(body.jwt, body.expirationDate, username);
        } catch (e) {
            console.log(e);
        }
    };

    return (
        <form onSubmit={onSubmitHandler} className={generalClasses['margin-auto']}>
            <h2 className={generalClasses.center}>Login</h2>
            <div className={classes['form-group']}>
                <label htmlFor="username">Username</label>
                <input ref={usernameRef} id='username' type="text"/>
            </div>
            <div className={classes['form-group']}>
                <label htmlFor="password">Password</label>
                <input ref={passwordRef} id='password' type="password"/>
            </div>

            <div className={classes['button-group']}>
                <button type='submit' className={classes.btn}>Submit</button>
                <button className={classes.btn} onClick={cancelButtonHandler}>Cancel</button>
            </div>
        </form>
    );
};

export default Login;