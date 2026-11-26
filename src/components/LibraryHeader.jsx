import React, { useReducer } from 'react'
import { Link } from 'react-router-dom';

function LibraryHeader() {
    const libraryTitle= "LIBRARY MANAGEMENT"
    const numbers = [1,2,3,4,5,6,7,8,9,10]

    const books = [
    
        {
            code: "SEN0001",
            name: "Software Engineering",
            price: 70
        },

        {
            code: "FIN0001",
            name: "Finance Book",
            price: 50
        },

        {
            code: "ACC0001",
            name: "Master Accounting",
            price: 60
        }
    ]
    let total = 0;
    total = books.reduce((total, book) => total + book.price, 0);

    const libraryHeader = "LIBRARY MANAGEMENT SYSTEM"
  return (
    <div> 
        {/* <div><h1>{libraryTitle}</h1></div> */}
    {/* //     <div><p>This is the library management for student books</p></div>
    //      <div>The total price for {books.length} books is {total} </div>          */}

    <nav>
        <Link to="/main" style={{ paddingRight: "10px" }}>DASHBOARD</Link>
        <Link to="/" style={{ paddingRight: "10px" }}>LOGIN</Link>
        <Link to="/offices" style={{ paddingRight: "10px" }}>OFFICES</Link>
    </nav>
    </div> 
  )
}

export default LibraryHeader
