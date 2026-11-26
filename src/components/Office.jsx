import { useState } from "react";
import axios from "axios";

function Office() {
  const [officeName, setOfficeName] = useState("");
  const [officeNumber, setOfficeNmbr] = useState("");
  const [deleteNmbr, setDeleteNmbr] = useState("");

  async function saveOffice() {
    try {
      const response = await axios.post("http://localhost:8080/api/offices/save", {
        name: officeName,
        officeNumber: officeNumber,
      });

      if (response.status === 201) {
        alert("Saved successfully");
        setOfficeName("");
        setOfficeNmbr("");
      } else {
        alert("Office is not saved");
      }
    } catch (error) {
      console.error(error);
      alert("This is the error: " + (error.response?.data?.message || error.message));
    }
  }

  async function deleteOffice() {
    try {
      const response = await axios.delete(`http://localhost:8080/api/offices/deleteByNumber/${deleteNmbr}`);
      alert(response.data);
      setDeleteNmbr("");
    } catch (error) {
      console.error(error);
      alert("This is the error: " + (error.response?.data?.message || error.message));
    }
  }

  return (
    <div>
      <h1>SAVE OFFICE</h1>

      <label>OFFICE NAME:</label>
      <input
        type="text"
        value={officeName}
        onChange={(e) => setOfficeName(e.target.value)}/><br />

      <label>OFFICE NUMBER:</label>
      <input
        type="text"
        value={officeNumber}
        onChange={(e) => setOfficeNmbr(e.target.value)}/><br />

      <button onClick={saveOffice}>SAVE</button>

      <h1>DELETE OFFICE</h1>

      <label>OFFICE NUMBER:</label>
      <input
        type="text"
        value={deleteNmbr}
        onChange={(e) => setDeleteNmbr(e.target.value)}/><br />

      <button onClick={deleteOffice}>DELETE</button>
    </div>
  );
}

export default Office;