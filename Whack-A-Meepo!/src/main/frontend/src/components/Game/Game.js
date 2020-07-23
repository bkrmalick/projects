import React, { useEffect, useState,useCallback } from 'react'
import './style.css'
import Box from './Box'
import { PropTypes } from 'react'
import {useDropzone} from 'react-dropzone'
import ScoreForm from './ScoreForm'

function Game(props)
{
    const SIZE=3*3;
    const ROW_LENGTH=3;
    const INTERVAL=1000;
    
    const [toggleStatus, setToggleStatus]= useState(true);

   const audio1= new Audio(require(`./sounds/attack1.mp3`));
   // const audio1= (<audio controls><source src={require(`./icons/meepo.png`)}  type="audio/mp3"/></audio>) ;

    audio1.volume=0.1;
    audio1.crossOrigin="anonymous";

    let startingArr=new Array(SIZE).fill(false);
    startingArr[0]=true;

    const [arr, setArr]= useState(startingArr);

    
    function sound1()
    {

        return <audio controls="controls" preload="auto" id="attack"
        crossOrigin="anonymous" src={require(`./icons/meepo.png`)}></audio>;
    }

    function incScore()
    {
        props.setScore(score=>score+1);
        playAttackSound();
    }
    
    function playAttackSound()
    {
        audio1.play().then(resp=>console.log(resp));
    }

    function setClicked()
    {
        incScore();
    }

    const meepImg=<img id ="meepImg" src={require(`./icons/meepo.png`)} alt="meepo" />;

    const [img,setImg]= useState(meepImg);

    function toggleImg()
    {
         const rand=Math.floor(Math.random() * (SIZE)) + 0; 
         let tempArr=new Array(SIZE).fill(false);
         tempArr[rand]=true;
         
         setArr(tempArr);
    }

    
    useEffect(() => 
    {
        if(toggleStatus)
        {
            const interval = setInterval(toggleImg, INTERVAL);
            return () => {
                            clearInterval(interval);
                        };
        }
      //  else
      //  {
            //setImg(null);    
       // }
    }, [toggleStatus]); //any time file is changed, rerun the inner callback



    function getTableBody()
    {
        let count=0;
        let body=<html></html>;

        /* for(let i=count;i<SIZE/ROW_LENGTH;i++)
        {
            body+=
            arr.map((value,index)=>
            {
                return <div className="row">
                <Box id={index} currentState={()=>
                {    
                    arr[index]=!value;
                    return value?null:meepImg;
                }} setClicked={setClicked}/>
                </div>;
            });
        } */
      
        return body;
    }




    let toRetun=<div id="game">
    <div className="border">
        <div className="row">
            <Box id={0} currentState={arr[0]?img:null} setClicked={setClicked}/>
            <Box id={1} currentState={arr[1]?img:null} setClicked={setClicked}/>
            <Box id={2} currentState={arr[2]?img:null} setClicked={setClicked}/>
        </div>

        <div className="row">
            <Box id={3} currentState={arr[3]?img:null} setClicked={setClicked}/>
            <Box id={4} currentState={arr[4]?img:null} setClicked={setClicked}/>
            <Box id={5} currentState={arr[5]?img:null} setClicked={setClicked}/>
        </div>

        <div className="row">
            <Box id={6} currentState={arr[6]?img:null} setClicked={setClicked}/>
            <Box id={7} currentState={arr[7]?img:null} setClicked={setClicked}/>
            <Box id={8} currentState={arr[8]?img:null} setClicked={setClicked}/>
        </div>
        
    </div>
</div>;

    return toRetun;
}

export default Game;