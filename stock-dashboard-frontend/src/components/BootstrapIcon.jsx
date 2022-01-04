import React from 'react'

function BootstrapIcon({filename}) {
    return (
        <svg className="bi" width="32" height="32" fill="currentColor">
            <use xlinkHref={`bootstrap-icons.svg#${filename}`}/>
        </svg>
    )
}

export default BootstrapIcon
