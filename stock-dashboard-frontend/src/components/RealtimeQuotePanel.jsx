import { faArrowAltCircleDown, faArrowAltCircleUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import moment from 'moment';
import React from 'react'
import { Card } from 'react-bootstrap';

function RealtimeQuotePanel(props) {

    const { quote } = props;

    if ( quote === null || typeof(quote) === 'undefined') {
        return (
            <Card.Text>-</Card.Text>
        );
    } else {
        let icon, className;
        if (quote.latestPrice >= quote.previousClose) {
            icon = faArrowAltCircleUp;
            className = "price-up";
        } else {
            icon = faArrowAltCircleDown;
            className = "price-down";
        }
        return (
            <>
            <Card.Text className={`quote ${className}`}>
                <FontAwesomeIcon icon={icon}/>
                <span>{quote.latestPrice.toFixed(2)}</span>
                <span>{quote.change>0?"+":""}{quote.change.toFixed(2)}</span>
                <span>({quote.changePercent.toFixed(2)}%)</span>
            </Card.Text>
            <Card.Text>
                Last Updated at: {moment(quote.lastRefreshAt).format("D MMM h:m:s a")}
            </Card.Text>
            </>
        );
    }
}

export default RealtimeQuotePanel
