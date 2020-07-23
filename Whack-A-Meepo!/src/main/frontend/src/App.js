import React, {useState,useEffect, useCallback} from 'react';
import logo from './logo.svg';
import './App.css';
import axios from "axios"
import {useDropzone} from 'react-dropzone'
import Game from './components/Game/Game'
import ScoreForm from './components/Game/ScoreForm.js'


/*
The port 5000 is set in application.properties in spring boot
and also in packages.json as proxy (this setting only works in dev)
*/
const API_URL = "/";//"http://whack-a-meepo.eu-west-2.elasticbeanstalk.com/";

//functional component 
const UserProfiles = () => 
{
  const [profiles, setUserProfiles]= useState([]);

    const fetchUserProfiles=() =>
    {
        axios.get(API_URL +"api/v1/user-profile").then(res=>
        {
          const compare=(p1,p2)=>{
            if(p1.score>p2.score)
              return -1;
            else if(p1.score==p2.score)
              return 0;
            else
              return 1;
          };

          console.log(res.data.sort(compare)); 
          setUserProfiles(res.data.sort(compare).slice(0,10));
        });
    }

    useEffect(()=>
    {
        fetchUserProfiles();
    }, []);

    return profiles.map((UserProfile,index)=>{

      return (
        <div key = {index} className="ProfileBox">
          {UserProfile.userProfileID ? <img src={API_URL +`api/v1/user-profile/${UserProfile.userProfileID}/image/download`} />:null}
          <br/><br/>
      <h3>{UserProfile.username}{index==0?"🔥":"👑"}</h3>
          <p>Score: {UserProfile.score}</p>
          <br/>
        </div>
      ); // <Dropzone userProfileID={UserProfile.userProfileID}/>

    })
}

function Dropzone({userProfileID}) {
  const onDrop = useCallback(acceptedFiles => {
    const file = acceptedFiles[0];
    
    console.log(file);
    
    let formData = new FormData();
    formData.append("file",file);

    axios.post(
      API_URL +`api/v1/user-profile/${userProfileID}/image/upload`, formData, {headers: {'Content-Type': 'multipart/form-data'}      }
    ).then(resp=>{console.log(resp); window.location.reload(true);}).catch(err=>console.log(err));

  }, [])
  const {getRootProps, getInputProps, isDragActive} = useDropzone({onDrop})

  return (
    <div {...getRootProps()}>
      <input {...getInputProps()} />
      {
        isDragActive ?
          <p>Drop the image here ...</p> :
          <p>Drag 'n' drop profile image, or click to select</p>
      }
    </div>
  )
}

function saveScore(score, name, file)
{

  let formData = new FormData();

  console.log(formData);
  formData.append("file",file);
  formData.append("name",name);  
  formData.append("score",score);
  console.log("FILE: "+file);
  let returnValue="";

  return axios.get(API_URL +"api/v1/user-profile").then(res=>
  {
    let profiles=[];
    profiles=res.data;
    console.log(profiles);

    let found=false;

      profiles.map((UserProfile,index)=>{

      if(UserProfile.username === name)
      {
        found=true;
      }

    });

    if(!found)
    {
      
    axios.post(
      API_URL +`api/v1/user-profile/score/upload`, formData, {headers: {'Content-Type': 'multipart/form-data'} }
          ).then(resp=>{window.location.reload(true);}).catch(err=>console.log(err));

          returnValue= "";
    }
    else
    {
      returnValue= "User already exists, try another one";
    }

    return returnValue;
  }).catch(err=>console.log(err));
}



function App() {

  const [file, setFile]=useState(null);
  const [score, setScore]= useState(0);
  const [name, setName] =useState("");
  const [error,setError] = useState("");

  function saveScoreClickHandler(e)
    {
        e.preventDefault();

        if(name.trim().length<2)
        {
            setError("Name cannot be less than 2 characters");
            return;
        }
        else if(score==0)
        {
            setError("Try scoring something first...");
            return;
        }
        else if(file==null)
        {
            setError("Add a profile image.");
            return;
        }
        else
        {
            //console.log("INNER FILE: "+file);
            saveScore(score,name,file).then(error=>setError(error));
        }
    }


  return (
    <div className="App">
      <h1 className="title">Whack-a-Meepo!</h1>
    
      <Game setScore={setScore}/>
      <h2>Score: {score}</h2><br/>
      <ScoreForm saveScore={saveScoreClickHandler} score={score} error={error} setName={setName} setFile={setFile}/>
      <div className="wrapper">
	<div className="divider div-transparent div-arrow-down"></div>
      </div>
      
      <h1>Leaderboards</h1><br/><br/>      
      <div className="profiles">
        <UserProfiles/>
      </div>

    </div>
    
  );
}

export default App;
