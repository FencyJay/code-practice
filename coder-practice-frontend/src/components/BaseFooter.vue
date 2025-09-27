<template>
  <footer class="battlefield-footer">
    <div class="footer-divider"></div>
    <div class="footer-content">
      <div class="footer-section">
        <h4 class="footer-title">⚔️ 技术练兵场</h4>
        <p class="footer-motto">代码如剑，调试如战</p>
      </div>

      <div class="footer-section">
        <p class="footer-copyright">
          Copyright © {{ currentYear }} CodeBear 版本所有
        </p>
        <p class="footer-beian">
          <a href="https://beian.miit.gov.cn/" target="_blank" rel="noopener noreferrer">
            📋 粤ICP备2025465914号
          </a>
        </p>
      </div>

      <div class="footer-section">
        <div class="battle-quotes">
          <p class="quote">"知己知彼，百战不殆"</p>
          <p class="quote-author">— 孙子兵法</p>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'

// 计算当前年份，确保自动更新
const currentYear = computed(() => new Date().getFullYear())
</script>

<style scoped>
/* 引入战场主题样式 */
@import '@/styles/battlefield-theme.css';

.battlefield-footer {
  background: linear-gradient(135deg, var(--deep-brown) 0%, var(--primary-brown) 100%);
  color: var(--warm-beige);
  font-family: 'Noto Serif SC', serif;
  margin-top: auto;
  position: relative;
  overflow: hidden;
}

.battlefield-footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, var(--battle-gold), var(--sword-silver), var(--battle-gold));
}

.footer-divider {
  width: 80%;
  height: 1px;
  background: linear-gradient(90deg, transparent, var(--battle-gold), transparent);
  margin: 0 auto 24px;
  opacity: 0.6;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 32px 20px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 32px;
  align-items: center;
}

.footer-section {
  text-align: center;
}

.footer-title {
  color: var(--battle-gold);
  font-size: 1.3rem;
  font-weight: bold;
  margin-bottom: 8px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
  letter-spacing: 1px;
}

.footer-motto {
  color: var(--parchment);
  font-style: italic;
  font-size: 1rem;
  margin: 0;
  opacity: 0.9;
}

.footer-copyright {
  color: var(--warm-beige);
  font-size: 0.9rem;
  margin: 0 0 8px 0;
  opacity: 0.8;
}

.footer-beian {
  margin: 0;
}

.footer-beian a {
  color: var(--parchment);
  text-decoration: none;
  font-size: 0.8rem;
  opacity: 0.7;
  transition: all 0.3s ease;
  border: 1px solid var(--battle-gold);
  padding: 4px 8px;
  border-radius: 4px;
  display: inline-block;
}

.footer-beian a:hover {
  opacity: 1;
  color: var(--battle-gold);
  background: rgba(218, 165, 32, 0.1);
  transform: translateY(-1px);
}

.battle-quotes {
  border-left: 3px solid var(--battle-gold);
  padding-left: 16px;
  text-align: left;
}

.quote {
  color: var(--battle-gold);
  font-style: italic;
  font-size: 0.9rem;
  margin: 0 0 4px 0;
  font-weight: 600;
}

.quote-author {
  color: var(--parchment);
  font-size: 0.8rem;
  margin: 0;
  opacity: 0.8;
}

/* 底部装饰性元素 */
.battlefield-footer::after {
  content: '⚔️ ⚔️ ⚔️';
  position: absolute;
  bottom: 8px;
  left: 50%;
  transform: translateX(-50%);
  color: var(--battle-gold);
  opacity: 0.3;
  font-size: 0.8rem;
  letter-spacing: 8px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .footer-content {
    grid-template-columns: 1fr;
    gap: 24px;
    padding: 24px 16px;
  }

  .footer-title {
    font-size: 1.1rem;
  }

  .footer-motto {
    font-size: 0.9rem;
  }

  .battle-quotes {
    text-align: center;
    border-left: none;
    border-top: 1px solid var(--battle-gold);
    border-bottom: 1px solid var(--battle-gold);
    padding: 16px 0;
    margin: 16px 0;
  }

  .footer-divider {
    width: 90%;
    margin-bottom: 16px;
  }

  .battlefield-footer::after {
    content: '⚔️';
    letter-spacing: 2px;
  }
}

/* 悬停效果 */
.footer-section:hover .footer-title {
  color: var(--warm-beige);
  text-shadow: 0 0 10px rgba(218, 165, 32, 0.5);
}

.footer-section:hover .footer-motto {
  opacity: 1;
}

/* 动画效果 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.footer-section {
  animation: fadeInUp 0.8s ease-out forwards;
}

.footer-section:nth-child(1) {
  animation-delay: 0.2s;
}

.footer-section:nth-child(2) {
  animation-delay: 0.4s;
}

.footer-section:nth-child(3) {
  animation-delay: 0.6s;
}
</style>