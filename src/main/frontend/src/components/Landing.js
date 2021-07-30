import Chessground from "react-chessground/chessground";
import FindMatch from "./FindMatch";
import {useState} from "react";
import Card from "../UI/Card";
import classes from './Landing.module.css';
import generalClasses from '../index.module.css';

const Landing = props => {

    const [boardSize, setBoardSize] = useState(Math.min(window.innerWidth - 75, 400))


    window.onresize = () => {

        setBoardSize(Math.min(window.innerWidth - 75, 400));
    };

    return (
        <section className={classes.landing}>
            <Card className={generalClasses.width50}>
                <h4 className={generalClasses.center}>Carlsen v. Caruana 2020</h4>
                <Chessground style={{margin: 'auto'}}
                             height={boardSize}
                             width={boardSize}
                />
            </Card>
            {/*<Card className={generalClasses.width25}>*/}
            <Card>
                <FindMatch setGame={props.setGame}/>
            </Card>
        </section>
    );

};

export default Landing;