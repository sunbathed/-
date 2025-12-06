<template>
  <div class="min-h-screen flex items-center justify-center bg-no-repeat bg-center relative overflow-hidden"
       :style="{ backgroundImage: `url('${bgImage}')`, backgroundSize: 'cover' }">
    <!-- Overlay -->
    <div class="absolute inset-0 bg-black/20"></div>

    <div class="relative z-10 bg-white/90 backdrop-blur-md p-8 md:p-10 rounded-2xl shadow-2xl w-full max-w-md border border-white/20 transform transition-all duration-300">
      
      <!-- Logo Area -->
      <div class="text-center mb-8">
        <div class="inline-flex items-center justify-center p-4 bg-emerald-50 rounded-full mb-4 shadow-inner">
          <BookOpen class="w-10 h-10 text-emerald-600" />
        </div>
        <h1 class="text-3xl font-bold text-gray-800 tracking-tight mb-2">二手书交易系统</h1>
        <div class="flex items-center justify-center gap-2 text-sm text-gray-100 bg-emerald-600/90 py-1 px-4 rounded-full mx-auto w-fit shadow-sm">
            <span>安徽大学龙河校区</span>
        </div>
      </div>
      
      <!-- Toggle Switch -->
      <div class="flex p-1 bg-gray-100 rounded-lg mb-8 relative">
        <div class="absolute top-1 bottom-1 w-[calc(50%-4px)] bg-white rounded-md shadow-sm transition-all duration-300 ease-out"
             :class="isLogin ? 'left-1' : 'left-[calc(50%)]'"></div>
        <button 
          class="flex-1 py-2.5 text-sm font-semibold z-10 transition-colors duration-300 relative"
          :class="isLogin ? 'text-emerald-700' : 'text-gray-500 hover:text-gray-700'"
          @click="isLogin = true"
        >登录</button>
        <button 
          class="flex-1 py-2.5 text-sm font-semibold z-10 transition-colors duration-300 relative"
          :class="!isLogin ? 'text-emerald-700' : 'text-gray-500 hover:text-gray-700'"
          @click="isLogin = false"
        >注册</button>
      </div>

      <!-- Form -->
      <form @submit.prevent="submit" class="space-y-5">
        <div v-if="!isLogin" class="space-y-1 animate-fade-in-down">
            <label class="text-xs font-bold text-gray-600 ml-1 uppercase tracking-wider">昵称</label>
            <div class="relative group">
                <User class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-emerald-500 transition-colors" />
                <input required v-model="form.name" placeholder="请输入您的昵称" 
                    class="w-full pl-10 pr-4 py-3 rounded-lg border border-gray-200 bg-gray-50 focus:bg-white focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 outline-none transition-all placeholder:text-gray-400 text-gray-700" />
            </div>
        </div>

        <div class="space-y-1">
            <label class="text-xs font-bold text-gray-600 ml-1 uppercase tracking-wider">邮箱</label>
            <div class="relative group">
                <Mail class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-emerald-500 transition-colors" />
                <input required v-model="form.email" type="email" placeholder="name@example.com" 
                    class="w-full pl-10 pr-4 py-3 rounded-lg border border-gray-200 bg-gray-50 focus:bg-white focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 outline-none transition-all placeholder:text-gray-400 text-gray-700" />
            </div>
        </div>

        <div class="space-y-1">
            <label class="text-xs font-bold text-gray-600 ml-1 uppercase tracking-wider">密码</label>
            <div class="relative group">
                <Lock class="absolute left-3 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-400 group-focus-within:text-emerald-500 transition-colors" />
                <input required type="password" v-model="form.password" placeholder="••••••••" 
                    class="w-full pl-10 pr-4 py-3 rounded-lg border border-gray-200 bg-gray-50 focus:bg-white focus:ring-2 focus:ring-emerald-500/20 focus:border-emerald-500 outline-none transition-all placeholder:text-gray-400 text-gray-700" />
            </div>
        </div>
        
        <button type="submit" 
            class="w-full py-3.5 rounded-lg bg-emerald-600 hover:bg-emerald-700 text-white font-bold shadow-lg shadow-emerald-500/30 hover:shadow-emerald-500/40 active:scale-[0.98] transition-all duration-200 flex items-center justify-center gap-2 mt-2">
          <span>{{ isLogin ? '立即登录' : '创建账号' }}</span>
          <ArrowRight class="w-5 h-5" />
        </button>
      </form>
      
      <div class="mt-8 text-center border-t border-gray-100 pt-6">
          <p class="text-sm text-gray-500">
              {{ isLogin ? '还没有账号？' : '已有账号？' }}
              <button class="text-emerald-600 font-semibold hover:text-emerald-700 hover:underline transition-colors ml-1" @click="isLogin = !isLogin">
                  {{ isLogin ? '立即注册' : '去登录' }}
              </button>
          </p>
      </div>
    </div>
    
    <!-- Footer Credit -->
    <div class="absolute bottom-4 text-white/60 text-xs text-center w-full">
      &copy; 2024 二手书交易系统 · 安徽大学
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { BookOpen, User, Mail, Lock, ArrowRight } from 'lucide-vue-next'
import request from '@/utils/request'
import { useUserStore } from '@/stores'
import { useRouter } from 'vue-router'

// 背景图片 URL
const bgImage = '/picture/login.png'

const isLogin = ref(true)
const form = reactive({ name: '', email: '', password: '' })
const { setUser } = useUserStore()
const router = useRouter()

function submit() {
  const url = isLogin.value ? '/login' : '/register'
  request.post(url, form)
    .then(res => {
      setUser(res.data)
      router.push('/books')
    })
    .catch(err => {
      alert((isLogin.value ? '登录' : '注册') + '失败：' + (err.response?.data?.message || '未知错误'))
    })
}
</script>

<style scoped>
.animate-fade-in-down {
  animation: fadeInDown 0.3s ease-out;
}

@keyframes fadeInDown {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>