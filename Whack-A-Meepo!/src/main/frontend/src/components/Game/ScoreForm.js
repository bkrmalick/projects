import React, { useEffect, useState,useCallback } from 'react'
import {useDropzone} from 'react-dropzone'
function ScoreForm(props)
{

    function Dropzone() {
        const onDrop = useCallback(acceptedFiles => {
          props.setFile(acceptedFiles[0]);
      
        }, [])
        const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

        return (
          <div {...getRootProps()}>
            <input {...getInputProps()} />
            {
              isDragActive ?
                <p>Drop it like it's hot 💃</p> :
                <p><span className="bold">Image:</span> Drag 'n' drop profile image here, or click to select</p>
            }
          </div>
        )
      }

    return (<form  onSubmit={props.saveScore}>
    <label className="bold">Name: </label>
    <input type="text" id="fname" name="fname" onChange={(e)=>{props.setName(e.target.value)}}></input><br></br>
    <Dropzone/>
    <button type="submit" >Save Score</button>
    <p className="error">{props.error}</p>
    </form> );
}

export default ScoreForm;