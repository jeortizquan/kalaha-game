import React from 'react';

function isKalah(value) {
    return value === 7 || value === 14;
}

function Pit(props) {
  console.log('pit['+props.index+ ']: '+props.value);
  var fontSize='bi bi-circle-fill ';
  if (props.value>=1 && props.value <=5) {
    fontSize += 'fs-3';
  } else if (props.value>=6 && props.value <=15) {
    fontSize += 'fs-4';
  } else if (props.value>=16 && props.value <=24) {
    fontSize += 'fs-5';
  } else if (props.value>=25 && props.value <=35) {
    fontSize += 'fs-6';
  } else if (props.value>=26 && props.value <=45) {
    fontSize += 'fs-7';
  } else if (props.value>=46 && props.value <=54) {
    fontSize += 'fs-8';
  } else {
    fontSize += 'fs-9';
  }
  var stones = [];
  for (var i=0; i < props.value; i++) {
      var stoneKey = 'st_'+props.index+'_'+i;
      stones.push(<i key={stoneKey} className={fontSize}></i>);
  }
  if (isKalah(props.index)) {
    return (
        <div className="d-flex flex-wrap flex-column bg-white kalahah justify-content-center align-items-center position-relative" onClick={props.onClick}>
            <span className="position-absolute top-100 start-50 translate-middle badge rounded-pill bg-yellow-200 text-yellow-800">{props.value}</span>
            {stones}
        </div>
    );
  } else {
    return (
        <div className="d-flex flex-wrap flex-column bg-white pit justify-content-center align-items-center position-relative" onClick={props.onClick}>
            <span className="position-absolute top-100 start-50 translate-middle badge rounded-pill bg-yellow-200 text-yellow-800">{props.value}</span>
            {stones}
        </div>
    );
  }
}

class Board extends React.Component {

  renderPit(i) {
    return (
      <Pit key={i}
        value={this.props.pits[i]}
        index={i}
        onClick={() => this.props.onClick(i)}
      />
    );
  }

  render() {
    return (
      <div id="game" className="container d-flex flex-row justify-content-center align-items-center">
          <div id="section1" className="container bg-orange-800">
            {this.renderPit(14)}
          </div>
          <div id="section2" className="container">
              <div id="north-container" className="container d-flex bg-orange-700">
                {this.renderPit(13)}
                {this.renderPit(12)}
                {this.renderPit(11)}
                {this.renderPit(10)}
                {this.renderPit(9)}
                {this.renderPit(8)}
              </div>
              <div id="south-container" className="container d-flex bg-orange-700">
                {this.renderPit(1)}
                {this.renderPit(2)}
                {this.renderPit(3)}
                {this.renderPit(4)}
                {this.renderPit(5)}
                {this.renderPit(6)}
              </div>
          </div>
          <div id="section3" className="container bg-orange-800">
            {this.renderPit(7)}
          </div>
      </div>
    );
  }
}

export default Board;