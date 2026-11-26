import React, { useState } from 'react'
import LibraryButton from './LibraryButton';
import LibraryHeader from './LibraryHeader';
function LibraryMain() {

  const [count, setCount] = useState(0);
  
  function incrementNumber(){
    setCount(count + 1);
    console.log("This is the incremented value "+count);
  }
  return (
    <div>
      <main>
        <LibraryHeader/>
        <div><h2>WELCOME PAGE</h2>
        <LibraryButton buttonLabel ="Increment" 
        buttonAction={incrementNumber}/>
        <div>Increment value is {count}</div>
          
        
        </div>
      </main>
    </div>
  )
}

export default LibraryMain
