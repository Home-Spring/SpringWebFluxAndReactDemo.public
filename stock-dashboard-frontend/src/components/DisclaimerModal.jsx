import React, { useState } from 'react'
import { Modal, Button } from 'react-bootstrap';
import '../index.css';

function DisclaimerModal(props) {
    const [show, setShow] = useState(props.show);
    const [ack, setAck] = useState(false);

    const clickUnderstand = (e) => {
        //e.preventDefault();
        //console.log(e.target.value);
        setAck(!ack);
    }

    const clickContinue = (e) => {
        //e.preventDefault();
        setShow(false);
    }

    return (
        <Modal
            show={show}
            dialogClassName="disclaimer-modal"
        >
            <Modal.Header>
                <Modal.Title>Disclaimer</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <h1>This page is for DEMO only! Stock prices in this page do not reflect the real market price!</h1>
                <input type="checkbox" 
                    className="mt-3 mb-3"
                    onChange={clickUnderstand}
                /> <span>I understand</span><br/>
                <Button id="btn-continue"
                    className="mt-3 mb-3"
                    onClick={clickContinue} 
                    disabled={!ack}
                >Continue to Demo</Button>
            </Modal.Body>
        </Modal>
    )
}

export default DisclaimerModal
