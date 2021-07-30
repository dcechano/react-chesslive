import Chessground from "react-chessground";
import "react-chessground/dist/styles/chessground.css";
import Chess from 'chess.js';
import {useState} from "react";
import Card from "../UI/Card";
import classes from './LiveGame.module.css';
import cardClasses from '../UI/Card.module.css';


const LiveGame = props => {

    const [chess, setChess] = useState(new Chess());
    const game = props.game;

    const move = (from, to) => {
        const moveObj = chess.move({from: from, to: to});
        setConfig(prevConfig => {
            prevConfig.turnColor = sideToMove(chess);
            prevConfig.movable = {
                ...prevConfig.movable,
                color: sideToMove(chess),
                dests: getValidMoves(chess),
            }

            // TODO Send the move!
            return {...prevConfig};
        });
    };

    const getValidMoves = chess => {
        let dests = new Map();
        chess.SQUARES.forEach(square => {
            let moves = chess.moves({square: square, verbose: true});
            if (moves.length)
                dests.set(square, moves.map(move => {
                    return move.to;
                }));
        });
        return dests;
    }

    const sideToMove = chess => {
        return (chess.turn() === 'w') ? 'white' : 'black';
    }

    const defaultConfig = {
        orientation: 'white',
        turnColor: 'white',
        autoCastle: true,
        animation: {
            enabled: true,
            duration: 500
        }, highlight: {
            lastMove: true,
            check: true
        },
        movable: {
            free: false,
            color: sideToMove(chess),
            rookCastle: true,
            dests: getValidMoves(chess),
            showDests: true,
            events: {}
        },
        events: {
            move: (orig, dest) => {
                let moveObj = chess.move({from: orig, to: dest});
                if (chess.game_over()) {
                    processGameOver();
                }


                let config = {
                    turnColor: sideToMove(chess),
                    movable: {
                        dests: getValidMoves(chess)
                    }
                }
                board.set(config);
                if (!(chess.turn() === color.charAt(0))) {
                    myClock.pause();
                    opponentClock.resume();
                    let gameUpdate = new GameUpdate(me.textContent,
                        opponent.textContent,
                        `${orig}-${dest}`,
                        chess.fen(),
                        myClock.seconds);

                    sendData(gameUpdate);
                }
                moveList.push(moveObj.san);
                updatePgnLog();
            }
        }
    };

    const [config, setConfig] = useState(defaultConfig);
    return (
        <section className={classes.wrapper}>
            <Card className={cardClasses.dark}>
                {game && <h2>{`Your game Id is: ${game.gameId}`}</h2>}
                <Chessground style={{margin: '2rem auto auto'}}
                             height='450px'
                             width='450px'
                             onMove={move}
                             {...config}/>
            </Card>
        </section>
    );
};


export default LiveGame;