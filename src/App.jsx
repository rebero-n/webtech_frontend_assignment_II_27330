 import { useState } from 'react'
 import heroImg from './assets/hero.png'
 import reactLogo from './assets/react.svg'
 import viteLogo from './assets/vite.svg'
 import './App.css'
 import Office from './components/Office'
 import OfficeList from './components/OfficeList'
 import { Routes, Route } from 'react-router-dom'
 import LibraryMain from './components/LibraryMain'
import LibraryButton from './components/LibraryButton'
// // import LibraryHeader from './components/LibraryHeader'
// // import LibraryFooter from './components/LibraryFooter'
 import LibraryLogin from './components/LibraryLogin'


 function App() {
   const [count, setCount] = useState(0)

   return (
//     <div>
//        <LibraryMain/>
//        <LibraryHeader/>
//        <LibraryButton/>
//        <LibraryFooter/>
//     </div>
     <div>
       <Routes>
          <Route path="/" element={<LibraryLogin/>}/>
          <Route path="/offices" element={<OfficeList/>}/>
          <Route path="/main" element={<LibraryMain/>}/>
      </Routes>
     </div>
   )
 }

 export default App