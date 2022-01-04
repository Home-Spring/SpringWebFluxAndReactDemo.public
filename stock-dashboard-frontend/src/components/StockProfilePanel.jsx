import React from 'react'

function StockProfilePanel(props) {
    const { symbol, profile } = props;

    if (profile===null) {
        return (
        <span>[{symbol}] loading profile...</span>
        )
    } else {
        return (
            <>
                <h4>{profile.companyName}</h4>
                <h6>{profile.exchange}:{profile.symbol}</h6>
            </>
        )
    }

}

export default StockProfilePanel
