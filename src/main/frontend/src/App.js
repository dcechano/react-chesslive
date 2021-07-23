import './App.css';
import {Fragment} from "react";
import {Redirect, Route, Switch} from 'react-router-dom';
import Navigation from "./layout/Navigation";
import classes from './index.module.css';
import LiveGame from "./components/LiveGame";
import Layout from "./layout/Layout";
import FindMatch from "./components/FindMatch";
import Card from "./UI/Card";
import Landing from "./components/Landing";
import Login from "./components/Login";

function App() {
    return (
        <Fragment>
            <Navigation/>
            <Layout>
                <Switch>
                    <Route path='/chesslive' exact>
                        <Landing/>
                    </Route>
                    <Route path='/chesslive/login'>
                        <Login/>
                    </Route>
                    <Route path='/chesslive/:gameId'>
                        <LiveGame/>
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
