import classes from './FindMatch.module.css';

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
    const onClick = event => {
        console.log(event.target.id);
        //    TODO fill this in with useful code
    }

    return (
        <div className={classes['find-match']}>
            {TIME_CONTROLS.map(timeControl => <button id={timeControl.id}
                                                     key={timeControl.id}
                                                     onClick={onClick}
                                                     className={classes.btn}>{timeControl.text}</button>)
            }
        </div>
    );
};

export default FindMatch;