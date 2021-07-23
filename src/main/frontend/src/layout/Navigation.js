import {NavLink} from "react-router-dom";
import classes from "./Navigation.module.css";

const Navigation = props => {
    return (
        <header className={classes.header}>
            <div className={classes.logo}>ChessLive</div>
            <nav className={classes.nav}>
                <ul>
                    <li>
                        {/*TODO this URI path is likely to change*/}
                        <NavLink to='/play'>Play</NavLink>
                    </li>
                    <li>

                        <NavLink to='/profile'>Profile</NavLink>
                    </li>
                    {/*    TODO Put in Log in link when authentication is ready*/}
                </ul>
            </nav>
        </header>
    );
};

export default Navigation;