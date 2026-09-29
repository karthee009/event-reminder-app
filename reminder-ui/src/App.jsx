import { useState, useEffect, useRef } from "react";
import "./App.css";

function playBeep() {
  const ctx = new (window.AudioContext || window.webkitAudioContext)();
  let count = 0;
  const interval = setInterval(() => {
    const osc = ctx.createOscillator();
    osc.type = "square";
    osc.frequency.value = 880;
    osc.connect(ctx.destination);
    osc.start();
    osc.stop(ctx.currentTime + 0.3);
    count++;
    if (count >= 5) clearInterval(interval);
  }, 500);
}

const API = import.meta.env.VITE_API_URL || "http://localhost:8097/api/reminders";

export default function App() {
  const [list, setList] = useState([]);
  const [title, setTitle] = useState("");
  const [purpose, setPurpose] = useState("");
  const [dateTime, setDateTime] = useState("");
  const [email, setEmail] = useState("");
  const [remindBefore, setRemindBefore] = useState(0);
  const alerted = useRef(new Set());
  const [ringing, setRinging] = useState(null);

  const load = () => fetch(API).then((r) => r.json()).then(setList);

  useEffect(() => { load(); }, []);

  useEffect(() => {
    const timer = setInterval(async () => {
      const res = await fetch(API);
      const data = await res.json();
      setList(data);
      const now = new Date();
      data.forEach((r) => {
        const alarmTime =
          new Date(r.dateTime).getTime() - (r.remindBefore || 0) * 60000;
        if (!alerted.current.has(r.id) && alarmTime <= now.getTime()) {
          alerted.current.add(r.id);
          playBeep();
          setRinging(r);
        }
      });
    }, 10000);
    return () => clearInterval(timer);
  }, []);

  const add = async () => {
    if (!title || !dateTime || !email.includes("@")) {
      alert("Please enter a valid email");
      return;
    }
    await fetch(API, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ title, purpose, dateTime, email, remindBefore }),
    });
    setTitle("");
    setPurpose("");
    setDateTime("");
    setEmail("");
    setRemindBefore(0);
    load();
  };

  const remove = async (id) => {
    await fetch(`${API}/${id}`, { method: "DELETE" });
    load();
  };

  return (
    <div className="container">
      <h2>⏰ Event Reminder</h2>

      {ringing && (
        <div className="alarm">
          <b>⏰ {ringing.title}</b>
          <p style={{ margin: "6px 0" }}>{ringing.purpose}</p>
          <button className="btn" onClick={() => setRinging(null)}>
            Stop Alarm
          </button>
        </div>
      )}

      <input placeholder="Event title" value={title}
        onChange={(e) => setTitle(e.target.value)} />
      <input placeholder="Purpose" value={purpose}
        onChange={(e) => setPurpose(e.target.value)} />
      <input type="datetime-local" value={dateTime}
        onChange={(e) => setDateTime(e.target.value)} />
      <input placeholder="Email to remind" value={email}
        onChange={(e) => setEmail(e.target.value)} />

      <select
        value={remindBefore}
        onChange={(e) => setRemindBefore(Number(e.target.value))}
        style={{
          width: "100%",
          padding: 10,
          marginBottom: 10,
          borderRadius: 8,
          border: "1px solid #ccc",
          fontSize: 15,
        }}
      >
        <option value={0}>Remind at event time</option>
        <option value={5}>5 minutes before</option>
        <option value={10}>10 minutes before</option>
        <option value={30}>30 minutes before</option>
        <option value={60}>1 hour before</option>
      </select>

      <button className="btn" onClick={add}>Set Reminder</button>
      <button className="btn gray" onClick={playBeep}>🔊 Test Sound</button>

      <ul className="list">
        {list.map((r) => (
          <li key={r.id} className={"item" + (r.sent ? " sent" : "")}>
            <div>
              <b>{r.title}</b> - {r.purpose}
              <div className="time">
                {new Date(r.dateTime).toLocaleString()}
                {r.remindBefore > 0 && ` (🔔 ${r.remindBefore} min before)`}{" "}
                {r.sent && "✅ Sent"}
              </div>
            </div>
            <button className="del" onClick={() => remove(r.id)}>❌</button>
          </li>
        ))}
      </ul>
    </div>
  );
}