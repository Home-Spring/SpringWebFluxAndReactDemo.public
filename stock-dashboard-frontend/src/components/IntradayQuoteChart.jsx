import React, { useEffect, useState } from 'react';
import { AreaChart, Area, XAxis, YAxis, ReferenceLine, Label, CartesianGrid, Tooltip, BarChart, Bar, Line, LineChart } from 'recharts';
import moment from 'moment';

function IntradayQuoteChart(props) {
    const { symbol, previous, intraday } = props;

    const chartWidth = 400;
    const xAxisHeight = 20;
    const yAxisWidth = 45;   

    const initData = {
        points: [],
        offset: 0.5,
        max: null,
        min: null
    };

    const [ data, setData ] = useState(initData);

    useEffect(() => {
        if (previous !== null && intraday !==null) {
            if (intraday.length > data.points.length) {
                const delta = [];
                let lastPrice = data.points.length>0?data.points[data.points.length-1].price:previous.close;
                for (let i=data.points.length;i<intraday.length;i++) {
                    const quote = intraday[i];
                    const point = {
                        date: new Date(quote.timestamp).getTime(),
                        price: (quote.close !== null)?quote.close:lastPrice,
                        volume: quote.volume,
                        volumeUp: 0,
                        volumeDown: 0
                    }
                    if ( quote.close !== null ) {
                        lastPrice = quote.close;
                    }
                    if ( quote.close >= previous.close ) {
                        point.volumeUp = quote.volume;
                    } else {
                        point.volumeDown = quote.volume;
                    }
                    delta.push(point);                    
                }
                const newpoints = [...data.points, ...delta];

                const max = newpoints.reduce((max,pt,i) => {
                    return ( i === 0 )?pt:(pt.price>max.price?pt:max);
                });
                const min = newpoints.reduce((min,pt,i) => {
                    return ( i === 0 )?pt:(pt.price<min.price?pt:min);
                });
                const offset = ( previous.close - min.price )/( max.price - min.price );
                //console.log(`${symbol}-max:${max.date},${max.price}-min:${min.date},${min.price}`);

                setData({
                    points: newpoints,
                    offset: offset,
                    max: max,
                    min: min
                });
            } else if (intraday.length === 0) {
                setData(initData);
            }
        }
    }, [previous,intraday]);

    const prev = previous!=null ? previous.close : 0;
    return (
        <>
            <AreaChart width={chartWidth} height={150} margin={{top:0,bottom:2,left:0,right:55}} 
                data={data.points} baseValue={prev} syncId={`${symbol}-chart`}>
                <XAxis dataKey="date"
                    type="number"
                    domain={['dataMin', 'dataMax']} 
                    scale="time"
                    tickFormatter={(timeValue) => {
                        return moment(timeValue).format('H:m');
                    }}
                    height={xAxisHeight} padding={{ left: 5, right: 5 }} />
                <YAxis
                    domain={[
                        dataMin => Math.min(dataMin,prev),
                        dataMax => Math.max(dataMax,prev)
                    ]}
                    tickFormatter={(value) => value.toFixed(2)}
                    width={yAxisWidth} padding={{top:5,bottom:5}}/>
                <CartesianGrid />
                <Tooltip content={<PriceToolTip prevClose={prev}/>} />
                <defs>
                    <linearGradient id={`splitColor-${symbol}`} x1="0" y1="1" x2="0" y2="0" >
                        <stop offset={data.offset} stopColor="red" stopOpacity={1} />
                        <stop offset={data.offset} stopColor="green" stopOpacity={1} />
                    </linearGradient>
                </defs>
                <ReferenceLine y={prev} stroke="red" strokeDasharray="3 3">
                    <Label position="right" className="prev-close-tag">
                        Prev-close
                    </Label>
                </ReferenceLine>
                <Area type="linear" dataKey="price" connectNulls={true}
                    activeDot={{ stroke: 'green', strokeWidth: 1, r: 2 }} 
                    isAnimationActive={false} stroke="#8884d8" strokeWidth={2} 
                    fill={`url(#splitColor-${symbol})`} />
            </AreaChart>
            
            {/*<BarChart width={chartWidth} height={100} margin={{ top: 0, bottom: 2, left: 0, right: 55 }}
                data={data.points} syncId={`${symbol}-chart`}>
                <XAxis dataKey="date"
                    type="number"
                    domain={['dataMin', 'dataMax']}
                    scale="time"
                    tickFormatter={(timeValue) => {
                        return moment(timeValue).format('H:m');
                    }}
                    height={xAxisHeight} padding={{ left: 5, right: 5 }}>
                </XAxis>
                <YAxis 
                    tickFormatter={(value) => (value / 1000).toFixed(0) + "k"} 
                    width={yAxisWidth} padding={{ top: 5, bottom: 5 }}/>
                <CartesianGrid />
                <Tooltip content={<VolumeToolTip prevClose={prev}/>} />
                <Bar id={`bar-vol-down-${symbol}`} dataKey="volumeDown" barSize="10" stackId="vol" fill="red" isAnimationActive={false} />
                <Bar id={`bar-vol-up-${symbol}`}   dataKey="volumeUp"   barSize="10" stackId="vol" fill="green" isAnimationActive={false} />
                </BarChart>*/}
        </>
    );

}

function PriceToolTip(props) {

    const { prevClose, label, payload, active } = props;

    if (active && payload != null && payload.length > 0) {
        const spot = payload[0].value;
        const fontColor = spot < prevClose ? "price-down" : "price-up";
        return (
            <div className="recharts-custom-tooltip">
                <span>{moment(label).format('D MMM h:m A')} &nbsp;
                    <span className={fontColor}>{spot}</span>
                </span>
            </div>
        );
    } else {
        return [];
    }
}

function VolumeToolTip(props) {

    const { prevClose, label, payload, active } = props;

    if (active && payload != null && payload.length > 0) {
        const spot = payload[0].payload['price'];
        const fontColor = spot < prevClose ? "price-down" : "price-up";
        return (
            <div className="recharts-custom-tooltip">
                <span>{moment(label).format('D MMM h:m A')} &nbsp;
                    <span className={fontColor}>{payload[0].payload['volume']}</span>
                </span>
            </div>
        );
    } else {
        return [];
    }
}
export default IntradayQuoteChart;
