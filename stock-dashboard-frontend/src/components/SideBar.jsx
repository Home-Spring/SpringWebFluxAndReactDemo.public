import React, { useContext, useState } from 'react'
import '../sidebar.css'
import { Button, Form, InputGroup } from 'react-bootstrap';
import { ConfigContext } from './Utils';
import moment from 'moment';

function SideBar(props) {
    const [show,setShow] = useState(props.show);

    const config = useContext(ConfigContext);

    const updateConfig = (e) => {
        //console.log(e.target.checked);
        const newConfig = {};
        Object.assign(newConfig,config);
        if (e.target.checked) {
            newConfig.refreshInterval = 1;
        } else {
            newConfig.refreshInterval = 0;
        }
        props.setConfig(newConfig);
    };

    return (
        <>
            <div className="wrapper">
                <nav id="sidebar" className={show?"active":""}>
                    <div className="menu-btn">
                        <button type="button" className="btn btn-primary" onClick={() => setShow(!show)}>
                            M
                        </button>
                    </div>
                  
                    <div className="sidebar-header">
                        Stock Dashboard Demo Configuration
                    </div>

                    <Form className="config-form">
                        <Form.Group>
                            <Form.Label>Add New Stock Quote</Form.Label>
                            <InputGroup>
                                <Form.Control type="text" placeholder="googl, amzn, msft, ..."/>
                                <InputGroup.Append>
                                    <Button>Add</Button>
                                </InputGroup.Append>
                            </InputGroup>
                        </Form.Group>

                        <label htmlFor="refreshInterval">Auto Refresh Stock Quote</label>
                        <InputGroup>
                            <InputGroup.Prepend>
                                <InputGroup.Checkbox onChange={updateConfig} checked={config.refreshInterval>0}/>
                            </InputGroup.Prepend>
                            <Form.Control type="number" id="refreshInterval" onChange={updateConfig} value={config.refreshInterval} placeholder="interval in sec"/>
                            <InputGroup.Append>
                                <InputGroup.Text>
                                    sec
                                </InputGroup.Text>
                            </InputGroup.Append>
                        </InputGroup>

                        <label htmlFor="tradeDate">Sample Trade Date</label>
                        <Form.Control type="Date" id="tradeDate" onChange={updateConfig} value={moment(config.tradeDate).format("YYYY-MM-DD")}/>

                        <label htmlFor="apiHost">Backend API Host</label>
                        <InputGroup>
                            <InputGroup.Prepend>
                                <InputGroup.Text>
                                    http://
                                </InputGroup.Text>
                            </InputGroup.Prepend>
                            <Form.Control type="Text" id="apiHost" defaultValue={config.apiHost}/>
                        </InputGroup>
                    </Form>

                </nav> 
            </div>
            <div className={`overlay ${show?"active":""}`}></div>
        </>
    )
}

SideBar.defaultProps = {
    show: false
}

export default SideBar
