import React, {useContext} from 'react';
import classes from './FindMatch.module.css';
import AuthContext from "../store/auth-context";
import {useHistory} from "react-router-dom";

const TIME_CONTROLS = [
    {
        id: 'TWO_PLUS_1',
        text: '2 + 1',
    },
    {
        id: 'FIVE_PLUS_0',
        text: '5 + 0'
    },
    {
        id: 'FIVE_PLUS_5',
        text: '5 + 5'
    },
    {
        id: 'TEN_PLUS_10',
        text: '10 + 10'
    }
];

const FindMatch = props => {

        const context = useContext(AuthContext);
        const history = useHistory();

        const onClickHandler = async event => {

            try {
                const response = await fetch(`http://localhost:8080/api/find-game/?time_control=${event.target.id}`, {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json',
                            Authorization: `Bearer ${context.token}`
                        },
                        body: JSON.stringify({})
                    }
                );
                if (!response.ok) {
                    throw new Error("There was an error");
                }
                console.log(response);

                const body = await response.json();
                console.log(body);

                // const game = JSON.parse(body);
                props.setGame(body);
                history.push(`/chesslive/${body.gameId}`);
            } catch (e) {
                console.log(e.message);
            }
        }


        return (
            <div className={classes['find-match']}>
                {TIME_CONTROLS.map(timeControl => <button id={timeControl.id}
                                                          key={timeControl.id}
                                                          onClick={onClickHandler}
                                                          className={classes.btn}>{timeControl.text}</button>)
                }
            </div>
        );
    }
;

export default FindMatch;