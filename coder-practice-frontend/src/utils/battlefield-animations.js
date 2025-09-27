// 战场动画和微交互效果
export class BattlefieldAnimations {
  constructor() {
    this.animations = new Map()
    this.observer = null
  }

  // 初始化动画
  init() {
    this.setupScrollAnimations()
    this.setupParallaxEffect()
    this.setupBattlefieldSounds()
    this.setupLoadingAnimations()
  }

  // 滚动动画
  setupScrollAnimations() {
    this.observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          if (entry.isIntersecting) {
            entry.target.classList.add('battlefield-visible')
            this.triggerElementAnimation(entry.target)
          }
        })
      },
      {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
      }
    )

    // 观察所有需要动画的元素
    document.querySelectorAll('.soldier-enter, .battle-card').forEach((el) => {
      this.observer.observe(el)
    })
  }

  // 视差滚动效果
  setupParallaxEffect() {
    let ticking = false

    const updateParallax = () => {
      const scrolled = window.pageYOffset
      const parallaxElements = document.querySelectorAll('.parallax-element')

      parallaxElements.forEach((element) => {
        const speed = element.dataset.speed || 0.5
        const yPos = -(scrolled * speed)
        element.style.transform = `translateY(${yPos}px)`
      })

      ticking = false
    }

    const requestTick = () => {
      if (!ticking) {
        window.requestAnimationFrame(updateParallax)
        ticking = true
      }
    }

    window.addEventListener('scroll', requestTick)
  }

  // 战场音效（模拟）
  setupBattlefieldSounds() {
    // 为按钮添加音效反馈
    document.querySelectorAll('.battle-button').forEach((button) => {
      button.addEventListener('click', () => {
        this.playSound('drum')
        this.createRippleEffect(button)
      })
    })

    // 为卡片添加悬停音效
    document.querySelectorAll('.battle-card').forEach((card) => {
      card.addEventListener('mouseenter', () => {
        this.playSound('sword')
      })
    })
  }

  // 创建涟漪效果
  createRippleEffect(element) {
    const ripple = document.createElement('span')
    ripple.classList.add('ripple')

    const rect = element.getBoundingClientRect()
    const size = Math.max(rect.width, rect.height)
    const x = event.clientX - rect.left - size / 2
    const y = event.clientY - rect.top - size / 2

    ripple.style.width = ripple.style.height = size + 'px'
    ripple.style.left = x + 'px'
    ripple.style.top = y + 'px'

    element.appendChild(ripple)

    setTimeout(() => {
      ripple.remove()
    }, 600)
  }

  // 播放音效（模拟）
  playSound(type) {
    // 这里可以添加实际的音效播放逻辑
    console.log(`Playing ${type} sound`)
  }

  // 加载动画
  setupLoadingAnimations() {
    // 为加载状态添加特殊动画
    const observer = new MutationObserver((mutations) => {
      mutations.forEach((mutation) => {
        if (mutation.type === 'attributes' && mutation.attributeName === 'disabled') {
          const element = mutation.target
          if (element.disabled) {
            element.classList.add('battle-loading')
            this.startLoadingAnimation(element)
          } else {
            element.classList.remove('battle-loading')
            this.stopLoadingAnimation(element)
          }
        }
      })
    })

    document.querySelectorAll('.battle-button').forEach((button) => {
      observer.observe(button, { attributes: true })
    })
  }

  // 开始加载动画
  startLoadingAnimation(element) {
    const dots = document.createElement('div')
    dots.classList.add('loading-dots')
    dots.innerHTML = '<span>.</span><span>.</span><span>.</span>'
    element.appendChild(dots)

    let dotIndex = 0
    const animateDots = () => {
      if (!element.disabled) return

      const spans = dots.querySelectorAll('span')
      spans.forEach((span, index) => {
        span.style.opacity = index === dotIndex ? '1' : '0.3'
      })

      dotIndex = (dotIndex + 1) % 3
      setTimeout(animateDots, 300)
    }

    animateDots()
  }

  // 停止加载动画
  stopLoadingAnimation(element) {
    const dots = element.querySelector('.loading-dots')
    if (dots) {
      dots.remove()
    }
  }

  // 触发元素动画
  triggerElementAnimation(element) {
    const animationType = element.dataset.animation || 'fadeInUp'

    switch (animationType) {
      case 'fadeInUp':
        element.style.animation = 'fadeInUp 0.8s ease-out forwards'
        break
      case 'slideInLeft':
        element.style.animation = 'slideInLeft 0.8s ease-out forwards'
        break
      case 'slideInRight':
        element.style.animation = 'slideInRight 0.8s ease-out forwards'
        break
      case 'scaleIn':
        element.style.animation = 'scaleIn 0.6s ease-out forwards'
        break
    }
  }

  // 添加战场特效
  addBattlefieldEffect(element, effectType) {
    switch (effectType) {
      case 'sword':
        this.createSwordEffect(element)
        break
      case 'shield':
        this.createShieldEffect(element)
        break
      case 'fire':
        this.createFireEffect(element)
        break
    }
  }

  // 创建剑光效果
  createSwordEffect(element) {
    const sword = document.createElement('div')
    sword.classList.add('sword-effect')
    sword.style.cssText = `
      position: absolute;
      top: 50%;
      left: -100%;
      width: 100px;
      height: 2px;
      background: linear-gradient(90deg, transparent, var(--battle-gold), transparent);
      transform: translateY(-50%);
      animation: swordSlash 0.6s ease-out forwards;
    `

    element.appendChild(sword)

    setTimeout(() => {
      sword.remove()
    }, 600)
  }

  // 创建护盾效果
  createShieldEffect(element) {
    const shield = document.createElement('div')
    shield.classList.add('shield-effect')
    shield.style.cssText = `
      position: absolute;
      top: 50%;
      left: 50%;
      width: 100%;
      height: 100%;
      border: 2px solid var(--battle-gold);
      border-radius: 4px;
      transform: translate(-50%, -50%) scale(0);
      animation: shieldExpand 0.8s ease-out forwards;
    `

    element.appendChild(shield)

    setTimeout(() => {
      shield.remove()
    }, 800)
  }

  // 创建火焰效果
  createFireEffect(element) {
    const fire = document.createElement('div')
    fire.classList.add('fire-effect')
    fire.style.cssText = `
      position: absolute;
      top: 50%;
      left: 50%;
      width: 20px;
      height: 20px;
      background: radial-gradient(circle, var(--battle-gold), transparent);
      border-radius: 50%;
      transform: translate(-50%, -50%);
      animation: fireFlicker 1s ease-in-out infinite;
    `

    element.appendChild(fire)

    setTimeout(() => {
      fire.remove()
    }, 1000)
  }

  // 清理动画
  destroy() {
    if (this.observer) {
      this.observer.disconnect()
    }

    // 清理所有动画元素
    document.querySelectorAll('.ripple, .loading-dots, .sword-effect, .shield-effect, .fire-effect').forEach(el => {
      el.remove()
    })
  }
}

// 创建全局动画实例
let battlefieldAnimations = null

// 初始化动画
export function initBattlefieldAnimations() {
  if (!battlefieldAnimations) {
    battlefieldAnimations = new BattlefieldAnimations()
    battlefieldAnimations.init()
  }
  return battlefieldAnimations
}

// 获取动画实例
export function getBattlefieldAnimations() {
  return battlefieldAnimations
}

// 添加动画CSS到head
export function injectAnimationStyles() {
  const style = document.createElement('style')
  style.textContent = `
    @keyframes fadeInUp {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }

    @keyframes slideInLeft {
      from {
        opacity: 0;
        transform: translateX(-50px);
      }
      to {
        opacity: 1;
        transform: translateX(0);
      }
    }

    @keyframes slideInRight {
      from {
        opacity: 0;
        transform: translateX(50px);
      }
      to {
        opacity: 1;
        transform: translateX(0);
      }
    }

    @keyframes scaleIn {
      from {
        opacity: 0;
        transform: scale(0.8);
      }
      to {
        opacity: 1;
        transform: scale(1);
      }
    }

    @keyframes swordSlash {
      0% {
        left: -100px;
        opacity: 0;
      }
      50% {
        opacity: 1;
      }
      100% {
        left: 100%;
        opacity: 0;
      }
    }

    @keyframes shieldExpand {
      0% {
        transform: translate(-50%, -50%) scale(0);
        opacity: 1;
      }
      100% {
        transform: translate(-50%, -50%) scale(1);
        opacity: 0;
      }
    }

    @keyframes fireFlicker {
      0%, 100% {
        transform: translate(-50%, -50%) scale(1);
        opacity: 1;
      }
      50% {
        transform: translate(-50%, -50%) scale(1.2);
        opacity: 0.8;
      }
    }

    .ripple {
      position: absolute;
      border-radius: 50%;
      background: rgba(218, 165, 32, 0.3);
      transform: scale(0);
      animation: rippleEffect 0.6s ease-out;
    }

    @keyframes rippleEffect {
      to {
        transform: scale(4);
        opacity: 0;
      }
    }

    .loading-dots {
      display: inline-block;
      margin-left: 8px;
    }

    .loading-dots span {
      display: inline-block;
      width: 4px;
      height: 4px;
      border-radius: 50%;
      background: var(--battle-gold);
      margin: 0 2px;
      transition: opacity 0.3s ease;
    }

    .battlefield-visible {
      opacity: 1;
      transform: translateY(0);
    }
  `

  document.head.appendChild(style)
}