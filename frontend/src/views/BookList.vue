<template>
  <div>
    <h2 class="text-2xl font-bold mb-4">逛书店</h2>
    <div class="mb-6 flex gap-2">
      <input
        v-model="keyword"
        placeholder="搜索书名..."
        class="flex-1 px-4 py-2 border rounded"
        @keyup.enter="search"
      />
      <button @click="search" class="px-6 py-2 bg-emerald-600 text-white rounded hover:bg-emerald-700">搜索</button>
    </div>
    <!-- 登录/注册提示 -->
    <div v-if="!user" class="mb-4 p-4 bg-emerald-50 rounded border text-emerald-700">
      请先登录后再浏览：
      <RouterLink to="/" class="underline font-bold">去登录</RouterLink>
    </div>

    <!-- 推荐算法区域 -->
    <div v-if="!keyword && recommendations.length > 0" class="mb-10 animate-fade-in">
      <div class="flex items-center gap-2 mb-4">
        <div class="bg-emerald-100 p-1.5 rounded-lg">
          <svg xmlns="http://www.w3.org/2001/svg" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="text-emerald-600"><path d="M2 12h10"/><path d="M9 4v16"/><path d="M3 9l9 6l9-6l-9-6l-9 6"/><path d="M14 9l7 6l-7 6l-7-6"/></svg>
        </div>
        <h3 class="text-xl font-bold text-zinc-800">猜你喜欢</h3>
        <span class="text-xs bg-orange-100 text-orange-600 px-2 py-0.5 rounded-full font-medium">个性化推荐</span>
      </div>
      <div class="grid gap-4 grid-cols-2 sm:grid-cols-3 lg:grid-cols-6">
        <RouterLink
          v-for="b in recommendations"
          :key="b.id"
          :to="`/books/${b.id}`"
          class="block bg-white rounded-lg shadow-sm hover:shadow-md transition-all duration-300 border border-zinc-100 group"
        >
          <div class="relative overflow-hidden rounded-t-lg h-40">
            <img
              :src="b.cover || '/picture/default-book.svg'"
              :alt="b.title"
              class="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
              onerror="this.src='/picture/default-book.svg';this.onerror=null;"
            />
            <div class="absolute inset-0 bg-gradient-to-t from-black/50 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-300 flex items-end p-2">
              <span class="text-white text-xs font-medium">查看详情</span>
            </div>
          </div>
          <div class="p-3">
            <h3 class="font-bold text-zinc-800 text-sm truncate mb-1" :title="b.title">{{ b.title }}</h3>
            <p class="text-xs text-zinc-500 mb-2 truncate">{{ b.author }}</p>
            <p class="text-emerald-600 font-bold text-sm">¥{{ b.price }}</p>
          </div>
        </RouterLink>
      </div>
    </div>

    <h3 class="text-xl font-bold mb-4 text-zinc-800" v-if="!keyword">全部图书</h3>
    <div class="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
      <RouterLink
        v-for="b in books"
        :key="b.id"
        :to="`/books/${b.id}`"
        class="block bg-white rounded shadow hover:shadow-md transition"
      >
        <img
          :src="b.cover || '/picture/default-book.svg'"
          :alt="b.title"
          class="w-full h-48 object-cover rounded-t"
          onerror="this.src='/picture/default-book.svg';this.onerror=null;"
        />
        <div class="p-4">
          <h3 class="font-semibold text-zinc-800 truncate">{{ b.title }}</h3>
          <p class="text-sm text-zinc-500">{{ b.author }}</p>
          <div class="mt-2 flex items-center justify-between">
            <span class="text-emerald-600 font-bold">¥{{ b.price }}</span>
            <span class="text-xs text-zinc-400">{{ b.seller.name }}</span>
          </div>
        </div>
      </RouterLink>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores'
import { storeToRefs } from 'pinia'

interface Book {
  id: number
  title: string
  author: string
  price: number
  cover: string
  seller: { name: string }
}

const userStore = useUserStore()
const { user } = storeToRefs(userStore)

const books = ref<Book[]>([])
const recommendations = ref<Book[]>([])
const keyword = ref('')

onMounted(() => {
  loadBooks()
  loadRecommendations()
})

function loadBooks() {
  request.get('/books', { params: { q: keyword.value } })
    .then((res) => {
      console.log('Books loaded:', res.data)
      books.value = res.data
    })
    .catch(err => console.error('Load books failed:', err))
}

function loadRecommendations() {
  const params = user.value ? { userId: user.value.id } : {}
  request.get('/books/recommend', { params })
    .then((res) => {
      recommendations.value = res.data
    })
}

function search() {
  loadBooks()
}
</script>