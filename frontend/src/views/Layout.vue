<template>
  <div class="min-h-screen bg-zinc-50">
    <header class="bg-white shadow border-b">
      <div class="max-w-5xl mx-auto px-4 py-4 flex items-center justify-between">
        <RouterLink to="/" class="flex items-center gap-2 text-zinc-800 font-bold">
          <BookOpen class="w-6 h-6 text-emerald-600" />
          二手书交易
        </RouterLink>
        <nav class="flex items-center gap-4">
          <RouterLink to="/books" class="text-zinc-600 hover:text-emerald-600">逛书店</RouterLink>
          <template v-if="user">
            <span class="text-zinc-800 font-medium">欢迎，{{ user.name }}</span>
            <RouterLink v-if="user.role === 'admin'" to="/admin" class="text-emerald-600 font-bold hover:text-emerald-700">管理后台</RouterLink>
            <RouterLink to="/publish" class="text-zinc-600 hover:text-emerald-600">发布</RouterLink>
            <RouterLink to="/center" class="text-zinc-600 hover:text-emerald-600">个人中心</RouterLink>
            <button @click="logout" class="text-zinc-600 hover:text-emerald-700">
              <LogOut class="w-5 h-5" />
            </button>
          </template>
          <RouterLink v-else to="/" class="px-3 py-1 rounded bg-emerald-600 text-white hover:bg-emerald-700">登录</RouterLink>
        </nav>
      </div>
    </header>
    <main class="max-w-5xl mx-auto px-4 py-6">
      <RouterView />
    </main>
  </div>
</template>

<script setup lang="ts">
import { BookOpen, LogOut } from 'lucide-vue-next'
import { useUserStore } from '@/stores'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const { user } = storeToRefs(userStore)
const { setUser } = userStore
const router = useRouter()

function logout() {
  setUser(null)
  router.push('/')
}
</script>