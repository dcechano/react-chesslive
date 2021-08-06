import React, {Fragment, useContext, useEffect, useState} from 'react';
import './App.css';
import {Redirect, Route, Switch} from 'react-router-dom';
import Navigation from "./layout/Navigation";
import LiveGame from "./components/LiveGame";
import Layout from "./layout/Layout";
import Login from "./components/Login";
import Modal from "./UI/Modal";
import AuthContext from "./store/auth-context";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import Landing from "./components/Landing";

function App() {

    const context = useContext(AuthContext);
    const [currentGame, setCurrentGame] = useState(null);
    const [messages, setMessages] = useState([]);
    const [stompClient, setStompClient] = useState(null);

    // Run once! Check if user has a valid token in browser
    useEffect(() => {
        //TODO remove logs
        console.log('Inside useEffect');
        const now = new Date().getTime();
        const expString = localStorage.getItem('expirationDate');
        const expirationDate = new Date(expString).getTime();
        if (expirationDate <= now) {

            context.logout();
        } else {
            const jwt = localStorage.getItem('jwt');
            const username = localStorage.getItem('username');
            context.login(jwt, expString, username);
        }
    }, [context.logout, context.login]);


    useEffect(() => {
        setStompClient(() => {
            const stompClient = Stomp.over(SockJS("http://localhost:8080/chess-lite"));
            stompClient.debug = str => {
                console.log(str);
            }

            stompClient.connect({},  (frame) => {
                console.log(frame);

                stompClient.subscribe('/user/queue/update', (data) => {
                });

                stompClient.subscribe('/user/queue/message', (data) => {
                    let msg = JSON.parse(data.body).message;
                    console.log(`New message: ${msg}`);
                });

                stompClient.subscribe('/topic/hello', data => {
                    const msg = JSON.parse(data.body).message;
                    console.log(`New message from /topic/hello: ${msg}`);
                    setMessages(messageList => [...messageList, msg]);
                    messages.forEach(msg => console.log(msg));
                })
            });
            return stompClient;
        });

    }, []);

    return (
        <Fragment>
            <Navigation/>
            <Layout>
                <Switch>
                    <Route path='/chesslive' exact>
                        {messages.map((message, index) => <p>{`${index}. ${message}`}</p>)}
                        <Landing setGame={setCurrentGame}/>
                    </Route>
                    <Route path='/chesslive/login'>
                        <Modal><Login/></Modal>
                    </Route>
                    <Route path='/chesslive/:gameId'>
                        <LiveGame game={currentGame} stompClient={stompClient}/>
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
