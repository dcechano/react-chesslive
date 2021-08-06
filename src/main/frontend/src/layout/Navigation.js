import React, {useContext} from 'react';
import {NavLink, useLocation} from "react-router-dom";
import classes from "./Navigation.module.css";
import AuthContext from "../store/auth-context";

const Navigation = props => {
    const location = useLocation();
    const context = useContext(AuthContext);

    const profileLink = <li><NavLink activeClassName={classes.active} to='/chesslive/profile'>{context.username}</NavLink></li>;
    const loginLink = <li><NavLink activeClassName={classes.active} to='/chesslive/login'>Login</NavLink></li>;

    return (
        <header className={classes.header}>
            <div className={classes.logo}><NavLink activeClassName={classes.active} to='/chesslive'>ChessLive</NavLink></div>
            <nav className={classes.nav}>
                <ul>
                    <li>
                        {/*TODO this URI path is likely to change*/}
                        <NavLink activeClassName={classes.active} to='/chesslive/play'>Play</NavLink>
                    </li>
                    {context.isLoggedIn ? profileLink : loginLink}
                    {context.isLoggedIn && <li><button className={classes.btn} onClick={context.logout}>Logout</button></li>}
                </ul>
            </nav>
        </header>
    );
};

export default Navigation;