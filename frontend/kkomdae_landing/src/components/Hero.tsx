import main_notebook_svg from "../assets/main_notebook.svg";
import { useEffect, useRef } from "react";
import { typeWriter } from "../utils/animations";
import "../styles/Hero.css";

const Hero: React.FC = () => {
  const titleRef = useRef<HTMLHeadingElement>(null);

  useEffect(() => {
    if (titleRef.current && titleRef.current.innerHTML === "") {
      // 타이핑 애니메이션 효과
      typeWriter(titleRef.current, "노트북 관리, 이제는 스마트하게", 100);
    }

    // 모바일 화면에서 노트북 이미지 애니메이션
    const notebookImg = document.querySelector(".notebook-image");
    if (notebookImg) {
      setTimeout(() => {
        notebookImg.classList.add("visible");
      }, 500);
    }
  }, []);

  return (
    <section className="hero">
      <div className="container hero-container">
        <div className="hero-content">
          <h1 ref={titleRef} className="hero-title"></h1>
          {/* <p className="hero-description fade-in">꼼대는 노트북의 상태를 AI로 체크하여 객관적인 기록을 남기고, 분실 및 파손을 방지할 수 있는 자동화된 시스템을 제공합니다.</p> */}
          <p className="hero-description fade-in">
            AI를 통한 노트북 외관 점검부터 자가진단프로그램까지 <br /> 꼼대만의 스마트한 노트북 관리를 경험해보세요.
          </p>
          <div className="hero-buttons fade-in">
            <a href="#download" className="btn-primary">
              앱 다운로드
            </a>
            <a href="#how-it-works" className="btn-secondary">
              이용 방법 알아보기
            </a>
          </div>

          <div className="hero-stats fade-in">
            <div className="stat-item">
              <span className="stat-number">98%</span>
              <span className="stat-text">정확도</span>
            </div>
            <div className="stat-item">
              <span className="stat-number">10,000+</span>
              <span className="stat-text">대여 관리</span>
            </div>
            <div className="stat-item">
              <span className="stat-number">100%</span>
              <span className="stat-text">보안 강화</span>
            </div>
          </div>
        </div>

        <div className="hero-image">
          <div className="notebook-image-container">
            <img src={main_notebook_svg} alt="노트북 메인 이미지" className="notebook-image" />
            <div className="notebook-scan-effect"></div>
          </div>
          <div className="hero-background"></div>
        </div>
      </div>

      <div className="hero-wave">
        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1440 320">
          <path
            fill="#ffffff"
            fillOpacity="1"
            d="M0,96L48,112C96,128,192,160,288,160C384,160,480,128,576,122.7C672,117,768,139,864,149.3C960,160,1056,160,1152,144C1248,128,1344,96,1392,80L1440,64L1440,320L1392,320C1344,320,1248,320,1152,320C1056,320,960,320,864,320C768,320,672,320,576,320C480,320,384,320,288,320C192,320,96,320,48,320L0,320Z"
          ></path>
        </svg>
      </div>
    </section>
  );
};

export default Hero;
