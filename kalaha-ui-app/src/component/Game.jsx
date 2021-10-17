import React from 'react';
import { Board } from '.';
import { ReactComponent as EagleSvg } from '../images/eagle.svg';
import { ReactComponent as HomeSvg } from '../images/home.svg';
import { ReactComponent as PeopleSvg } from '../images/people-circle.svg';

class Game extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      gameId: -1,
      pits: Array(14).fill(null),
      turn: '',
      message: 'Click New to begin game.',
      isAbout: false,
     };
    this.newGame = this.newGame.bind(this);
  }

  async newGame(e) {
    e.preventDefault();
    const game = await fetch('http://localhost:8000/games', {method: 'POST'})
                         .then(response => response.json());
    const newGameId = game.id;
    const initialStatus = await fetch('http://localhost:8000/games/'+newGameId, {method: 'GET'})
                                  .then(response => response.json());
    this.setState({
      gameId: newGameId,
      pits: initialStatus.status,
      message: '',
      turn: 'SOUTH'
    });
  }

  getAbout(e) {
    e.preventDefault();
    this.setState({isAbout: !this.state.isAbout});
  }

  async handleClick(i) {
    try {
        const move = await fetch('http://localhost:8000/games/'+this.state.gameId+'/pits/'+i, {method: 'PUT'})
                         .then(response => response.json())
                         .catch(error => error.json())
        if (move.error === undefined) {
            this.setState({ pits: move.status, turn: move.turn, message: '' });
        } else {
            this.setState({ message: move.message });
        }
    } catch(error) {
        console.log(error);
    }
  }

  render() {
    let status, turn;
    if (this.state.turn === '') {
        turn = 'Welcome !';
    } else {
        turn = 'Player '+ (this.state.turn) +' turn';
    }

    status = this.state.message;

    return (
        <div className="game">
            <div className="px-3 py-2 bg-indigo-800 text-white">
                <div className="container">
                    <div className="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
                    <span className="d-flex align-items-center my-2 my-lg-0 me-lg-auto text-white text-decoration-none">
                        <EagleSvg className="bi d-block mx-auto mb-1" fill="#FFFFFF" width="50" height="50" />
                        Kalaha Game
                    </span>
                    <ul className="nav col-12 col-lg-auto my-2 justify-content-center my-md-0 text-small">
                        <li>
                            <button className="bg-transparent border-0 text-white" onClick={(e) => this.newGame(e)}>
                                <HomeSvg className="bi d-block mx-auto mb-1" fill="currentColor" width="24" height="24"/>
                                New
                            </button>
                        </li>
                        <li>
                            <button className="bg-transparent border-0 text-white" onClick={(e) => this.getAbout(e)}>
                                <PeopleSvg className="bi d-block mx-auto mb-1" fill="currentColor" width="24" height="24" />
                                About
                            </button>
                        </li>
                    </ul>
                    </div>
                </div>
            </div>
            <div className="game-info">
              <div className="d-flex alert alert-primary justify-content-center" role="alert">{turn}</div>
            </div>
            <div className="game-board">
              <Board
                pits={this.state.pits}
                onClick={i => this.handleClick(i)}
              />
              <br />
              <div className="d-flex alert alert-info justify-content-center">{status}</div>
              <div className={ this.state.isAbout? 'd-flex d-block alert alert-primary justify-content-center': 'd-none' } >
              ***** by Jorge Ortiz 2021 *****
               &nbsp;<button className="d-flex btn btn-outline-info fs-8" onClick={(e)=> this.getAbout(e)}> x </button>
              </div>
            </div>
        </div>
    );
  }
}

export default Game;