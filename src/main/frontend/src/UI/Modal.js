import classes from "./Modal.module.css";
import {Fragment} from "react";
import * as ReactDOM from "react-dom";

const Backdrop = props => {
    return (
        <div className={classes.backdrop}></div>
    );
};

const ModalOverlay = props => {
    return (
        <div className={classes.modal}>
            <div>{props.children}</div>
        </div>
    )
};

const Modal = props => {
    const portal = document.getElementById('portal');
    return (
        <Fragment>
            {ReactDOM.createPortal(<Backdrop/>, portal)}
            {ReactDOM.createPortal(<ModalOverlay>{props.children}</ModalOverlay>, portal)}
        </Fragment>
    );
}

export default Modal;