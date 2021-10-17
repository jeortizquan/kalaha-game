import React from 'react';
import './css/app.css';
import './css/custom.css';
import './css/bootstrap-icons/font/bootstrap-icons.css';
import { Game } from './component';

function App() {
  document.title= 'Kalaha | Game';
  return (
    <div className="App">
        <Game />
    </div>
  );
}

export default App;
