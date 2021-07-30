import './App.css';
import {Fragment, useContext, useEffect, useState} from "react";
import {Redirect, Route, Switch} from 'react-router-dom';
import Navigation from "./layout/Navigation";
import LiveGame from "./components/LiveGame";
import Layout from "./layout/Layout";
import Landing from "./components/Landing";
import Login from "./components/Login";
import Modal from "./UI/Modal";
import AuthContext from "./store/auth-context";

function App() {

    const context = useContext(AuthContext);
    const [currentGame, setCurrentGame] = useState(null);
    // Run once! Check if user has a valid token in browser
    useEffect(() => {
        const now = new Date().getTime();
        const expString = localStorage.getItem('expirationDate');
        const expirationDate = new Date(expString).getTime();
        //TODO remove logs
        if (expirationDate <= now) {
            console.log('expirationData < now');
            console.log(`expirationDate: ${expirationDate}, now: ${now}`);
            context.logout();
        } else {
            console.log('!expirationDate < now');
            const jwt = localStorage.getItem('jwt');
            const username = localStorage.getItem('username');
            context.login(jwt, expString, username);
        }
    }, []);

    return (
        <Fragment>
            <Navigation/>
            <Layout>
                <Switch>
                    <Route path='/chesslive' exact>
                        <Landing setGame={setCurrentGame}/>
                    </Route>
                    <Route path='/chesslive/login'>
                        <Modal><Login/></Modal>
                    </Route>
                    <Route path='/chesslive/:gameId'>
                        <LiveGame game={currentGame}/>
                    </Route>
                    <Route path='*'>
                        <Redirect to='/chesslive'/>
                    </Route>
                </Switch>
            </Layout>

        </Fragment>
    );
}

export default App;
