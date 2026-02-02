import './LoadingSpinner.css';

const LoadingSpinner = () => {
  return (
    <div className="spinner-container">
      <div className="spinner-wrapper">
        <div className="spinner">
          <div className="spinner-ring"></div>
          <div className="spinner-ring"></div>
          <div className="spinner-ring"></div>
        </div>
        <div className="spinner-text">
          <span className="loading-dot">L</span>
          <span className="loading-dot">o</span>
          <span className="loading-dot">a</span>
          <span className="loading-dot">d</span>
          <span className="loading-dot">i</span>
          <span className="loading-dot">n</span>
          <span className="loading-dot">g</span>
          <span className="loading-dot">.</span>
          <span className="loading-dot">.</span>
          <span className="loading-dot">.</span>
        </div>
      </div>
    </div>
  );
};

export default LoadingSpinner;
