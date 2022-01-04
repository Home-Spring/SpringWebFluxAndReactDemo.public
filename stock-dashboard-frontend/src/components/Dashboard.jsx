import React, { createContext, useState } from 'react'
import SideBar from './SideBar';
import StockCard from './StockCard'
import { ConfigContext } from './Utils';
import moment from 'moment';

function Dashboard() {

    //const [refreshInterval,setRefreshInterval] = useState(2);

    const [ config, setConfig ] = useState({
        tradeDate: moment("2020-09-24","YYYY-MM-DD"),
        apiHost: "127.0.0.1:8081",
        refreshInterval: 1
    });

    return (
        <ConfigContext.Provider value={config}>
            <SideBar setConfig={setConfig}/>
            <section className="title">
                <h2>Stock Quote Dashboard</h2>
                <span><a href="https://iexcloud.io">Data provided by IEX Cloud</a></span>
            </section>
            <section className="card-list">
                <StockCard symbol="aapl"/>
                <StockCard symbol="msft"/>
                <StockCard symbol="amzn"/>
                <StockCard symbol="googl"/>
                <StockCard symbol="fb"/>
                <StockCard symbol="baba"/>
                <StockCard symbol="v"/>
                <StockCard symbol="jnj"/>
                <StockCard symbol="wmt"/>
                <StockCard symbol="tsm"/>
                <StockCard symbol="pg"/>
                <StockCard symbol="ma"/>
                <StockCard symbol="jpm"/>
                <StockCard symbol="unh"/>
                <StockCard symbol="hd"/>
                <StockCard symbol="tsla"/>
                <StockCard symbol="intc"/>
                <StockCard symbol="nvda"/>
                <StockCard symbol="vz"/>
                <StockCard symbol="t"/>
                <StockCard symbol="dis"/>
                <StockCard symbol="adbe"/>
                <StockCard symbol="pypl"/>
                <StockCard symbol="mrk"/>
                <StockCard symbol="pfe"/>
                <StockCard symbol="bac"/>
                <StockCard symbol="ko"/>
                <StockCard symbol="csco"/>
                <StockCard symbol="cmcsa"/>
                <StockCard symbol="pep"/>
            </section>
        </ConfigContext.Provider>
    )
}

export default Dashboard
