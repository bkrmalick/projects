import React, { useState } from 'react'
import incrementScore from './Game.js'

function Box(props)
{
    const[meep, setMeep]=useState(props.currentState);

    function toggleMeep()
    {
        if(props.currentState!==null)//has meepo img
        {
            props.setClicked(); //increment score in higher level compoenent 
        }
    }

    return <div  className="box" onMouseDown={toggleMeep}>
        {props.currentState}
        </div>;
}

export default Box;