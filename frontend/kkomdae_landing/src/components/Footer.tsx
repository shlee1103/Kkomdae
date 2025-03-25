import "../styles/Footer.css";
import Modal from "./Modal"; // Modal 컴포넌트 import
import { useState } from "react";

const Footer: React.FC = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);

  const openModal = () => {
    console.log("openModal called");
    setIsModalOpen(true);
    console.log("isModalOpen:", isModalOpen);
  };

  const closeModal = () => {
    console.log("closeModal called");
    setIsModalOpen(false);
    console.log("isModalOpen:", isModalOpen);
  };

  return (
    <footer className="footer">
      <div className="container">
        <div className="footer-content">
          <div className="footer-logo">
            <span className="logo-text-footer">꼼대</span>
            <p className="footer-description">노트북 관리의 새로운 기준, 꼼대가 SSAFY 교육생 여러분의 노트북을 안전하게 지켜드립니다.</p>
          </div>

          <div className="footer-links">
            <div className="footer-links-column">
              <h4 className="footer-links-title">서비스</h4>
              <ul className="footer-links-list">
                <li>
                  <a href="#features">주요 기능</a>
                </li>
                <li>
                  <a href="#how-it-works">이용 방법</a>
                </li>
                <li>
                  <a href="#faq">자주 묻는 질문</a>
                </li>
              </ul>
            </div>

            <div className="footer-links-column">
              <h4 className="footer-links-title">다운로드</h4>
              <ul className="footer-links-list">
                <li>
                  <a href="#">iOS</a>
                </li>
                <li>
                  <a href="#">Android</a>
                </li>
                <li>
                  <button onClick={openModal} className="footer-modal-link">
                    자가진단 프로그램
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>

        <div className="footer-bottom">
          <p className="copyright">© 2025 꼼대(KKOMDAE). All rights reserved.</p>
        </div>
      </div>
      <Modal isOpen={isModalOpen} onClose={closeModal} /> {/* Modal 컴포넌트 사용 */}
    </footer>
  );
};

export default Footer;
