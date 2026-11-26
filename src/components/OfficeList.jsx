import { useState, useEffect } from "react";
import axios from "axios";

function OfficeList() {
  const [offices, setOffices] = useState([]);
  const [keyword, setKeyword] = useState("");

  async function loadOffices(search) {
    try {
      const response = await axios.get("http://localhost:8080/api/offices/search", {
        params: { keyword: search },
      });
      setOffices(response.data);
    } catch (error) {
      console.error(error);
      alert("Could not load offices: " + error.message);
    }
  }

  useEffect(() => {
    loadOffices(keyword);
  }, [keyword]);

  async function deleteOffice(id) {
    if (!window.confirm("Are you sure you want to delete this office?")) return;
    try {
      const response = await axios.delete(`http://localhost:8080/api/offices/${id}`);
      alert(response.data);
      loadOffices(keyword);
    } catch (error) {
      console.error(error);
      alert("Could not delete office. It may be assigned to a doctor.");
    }
  }

  const cell = { border: "1px solid #ccc", padding: "8px 12px", textAlign: "left" };

  return (
    <div style={{ padding: "20px" }}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "15px" }}>
        <h1>OFFICES</h1>
        <input
          type="text"
          placeholder="Search by office name or number"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
          style={{ padding: "8px", width: "280px" }}
        />
      </div>

      <table style={{ borderCollapse: "collapse", width: "100%" }}>
        <thead>
          <tr style={{ backgroundColor: "#f0f0f0" }}>
            <th style={cell}>Id</th>
            <th style={cell}>Office Number</th>
            <th style={cell}>Office Name</th>
            <th style={cell}>Action</th>
          </tr>
        </thead>
        <tbody>
          {offices.length === 0 ? (
            <tr>
              <td style={cell} colSpan="4">No offices found.</td>
            </tr>
          ) : (
            offices.map((office) => (
              <tr key={office.id}>
                <td style={cell}>{office.id}</td>
                <td style={cell}>{office.roomNumber}</td>
                <td style={cell}>{office.name}</td>
                <td style={cell}>
                  <button onClick={() => deleteOffice(office.id)}>Delete</button>{" "}
                  <button>Update</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </div>
  );
}

export default OfficeList;