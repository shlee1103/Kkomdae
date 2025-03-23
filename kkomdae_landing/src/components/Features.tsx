import { useEffect } from "react";
import { initScrollAnimations } from "../utils/animations";
import "../styles/Features.css";

const Features: React.FC = () => {
  useEffect(() => {
    initScrollAnimations();
  }, []);

  return (
    <section id="features" className="features section">
      <div className="container">
        <div className="section-header-feature fade-in">
          <h2 className="section-title-feature">주요 기능</h2>
          <p className="section-subtitle-feature">꼼대의 핵심 기능들을 통해 노트북 관리를 더 스마트하게</p>
        </div>

        <div className="features-grid">
          <div className="feature-card fade-in">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="2" y="3" width="20" height="14" rx="2" ry="2"></rect>
                <line x1="8" y1="21" x2="16" y2="21"></line>
                <line x1="12" y1="17" x2="12" y2="21"></line>
              </svg>
            </div>
            <h3 className="feature-title">AI 기반 노트북 상태 분석</h3>
            <p className="feature-description">AI가 온전한 새 노트북의 상태와 비교하여 스크래치, 흠집, 파손이 있는 부분을 자동으로 찾아내고 리스트화합니다.</p>
          </div>

          <div className="feature-card fade-in">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>
                <polyline points="14 2 14 8 20 8"></polyline>
                <line x1="16" y1="13" x2="8" y2="13"></line>
                <line x1="16" y1="17" x2="8" y2="17"></line>
                <polyline points="10 9 9 9 8 9"></polyline>
              </svg>
            </div>
            <h3 className="feature-title">자동화된 문서화 시스템</h3>
            <p className="feature-description">분실 및 파손을 방지하기 위해 PDF 형태의 자동화된 문서화 시스템을 제공하여 기기 상태를 쉽게 기록하고 관리합니다.</p>
          </div>

          <div className="feature-card fade-in">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <rect x="4" y="4" width="16" height="16"></rect>
                <rect x="7" y="7" width="4" height="4"></rect>
                <rect x="13" y="7" width="4" height="4"></rect>
                <rect x="7" y="13" width="4" height="4"></rect>
                <rect x="14" y="14" width="3" height="3"></rect>
              </svg>
            </div>
            <h3 className="feature-title">QR 코드 연동 자가진단</h3>
            <p className="feature-description">꼼대 자체의 자가진단 프로그램을 노트북에서 실행하고 QR 코드를 통해 결과를 모바일로 간편하게 전송합니다.</p>
          </div>

          <div className="feature-card fade-in">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="3" width="18" height="18" rx="2" ry="2"></rect>
                <circle cx="8.5" cy="8.5" r="1.5"></circle>
                <polyline points="21 15 16 10 5 21"></polyline>
              </svg>
            </div>
            <h3 className="feature-title">상세한 사진 촬영 가이드</h3>
            <p className="feature-description">상판, 하판, 측면, 화면, 키보드 등 노트북의 모든 부분을 정확하게 촬영할 수 있도록 상세한 가이드를 제공합니다.</p>
          </div>

          <div className="feature-card fade-in">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="3" width="7" height="7"></rect>
                <rect x="14" y="3" width="7" height="7"></rect>
                <rect x="14" y="14" width="7" height="7"></rect>
                <rect x="3" y="14" width="7" height="7"></rect>
              </svg>
            </div>
            <h3 className="feature-title">관리자 웹 대시보드</h3>
            <p className="feature-description">대여와 반납 시 생성된 노트북 기기 상태 문서를 관리자 웹에서 편리하게 확인하고 관리할 수 있습니다.</p>
          </div>

          <div className="feature-card fade-in">
            <div className="feature-icon">
              <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                <path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"></path>
              </svg>
            </div>
            <h3 className="feature-title">보안 강화 시스템</h3>
            <p className="feature-description">노트북 정보와 상태 기록에 대한 철저한 보안 관리로 데이터 유출 위험을 최소화하고 안전하게 보호합니다.</p>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Features;
