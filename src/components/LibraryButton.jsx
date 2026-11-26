import React from 'react'

function LibraryButton(props) {
  return (
    <div>
      <button onClick={props.buttonAction}>{props.buttonLabel}</button>
    </div>
  )
}

export default LibraryButton
