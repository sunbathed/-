<template>
  <div>
    <h2 class="text-2xl font-bold mb-4">数据统计</h2>
    <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-10">
      <div class="bg-white p-6 rounded shadow">
        <h3 class="text-zinc-500 text-sm font-medium">总用户数</h3>
        <p class="text-3xl font-bold text-zinc-800 mt-2">{{ stats.userCount }}</p>
      </div>
      <div class="bg-white p-6 rounded shadow">
        <h3 class="text-zinc-500 text-sm font-medium">总图书数</h3>
        <p class="text-3xl font-bold text-zinc-800 mt-2">{{ stats.bookCount }}</p>
      </div>
      <div class="bg-white p-6 rounded shadow">
        <h3 class="text-zinc-500 text-sm font-medium">总订单数</h3>
        <p class="text-3xl font-bold text-zinc-800 mt-2">{{ stats.orderCount }}</p>
      </div>
      <div class="bg-white p-6 rounded shadow">
        <h3 class="text-zinc-500 text-sm font-medium">总交易额</h3>
        <p class="text-3xl font-bold text-emerald-600 mt-2">¥{{ stats.totalAmount }}</p>
      </div>
    </div>

    <h2 class="text-2xl font-bold mb-4">图书审核管理</h2>
    <div v-if="books.length === 0" class="text-zinc-500">暂无待审核图书</div>
    <div v-else class="space-y-4">
      <div
        v-for="b in books"
        :key="b.id"
        class="bg-white rounded shadow p-4 flex items-center justify-between"
      >
        <div class="flex gap-4">
          <img
            :src="b.cover && b.cover.startsWith('http') ? b.cover : 'https://placehold.co/100x100?text=Book'"
            class="w-24 h-32 object-cover rounded"
          />
          <div>
            <h3 class="font-semibold text-lg">{{ b.title }}</h3>
            <p class="text-sm text-zinc-500">作者：{{ b.author }}</p>
            <p class="text-sm text-zinc-500">卖家：{{ b.seller.name }}</p>
            <p class="text-emerald-600 font-bold">¥{{ b.price }}</p>
            <p class="text-zinc-600 mt-2 text-sm">{{ b.description }}</p>
          </div>
        </div>
        <div class="flex flex-col gap-2">
          <button @click="approve(b.id)" class="px-4 py-2 bg-emerald-600 text-white rounded hover:bg-emerald-700">通过</button>
          <button @click="reject(b.id)" class="px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700">驳回</button>
        </div>
      </div>
    </div>

    <div class="mt-10">
      <h2 class="text-2xl font-bold mb-4">举报处理</h2>
      <div v-if="reports.length === 0" class="text-zinc-500">暂无待处理举报</div>
      <div v-else class="space-y-4">
        <div v-for="r in reports" :key="r.id" class="bg-white rounded shadow p-4">
          <div class="flex justify-between items-start">
            <div class="flex-1">
              <!-- Book Report -->
              <div v-if="r.type === 'book' && r.book" class="flex gap-4">
                <img 
                  :src="r.book.cover || '/picture/default-book.svg'" 
                  class="w-20 h-28 object-cover rounded border border-zinc-200"
                  onerror="this.src='/picture/default-book.svg';this.onerror=null;"
                />
                <div>
                  <div class="flex items-center gap-2">
                    <span class="px-2 py-0.5 text-xs rounded bg-blue-100 text-blue-700 font-bold">图书举报</span>
                    <h3 class="font-bold text-zinc-800">{{ r.book.title }}</h3>
                  </div>
                  <p class="text-sm text-zinc-500 mt-1">作者：{{ r.book.author }}</p>
                  <p class="text-sm text-zinc-500">卖家：{{ r.book.seller ? r.book.seller.name : 'Unknown' }}</p>
                  <div class="mt-2 bg-red-50 p-2 rounded border border-red-100 inline-block">
                    <p class="text-sm text-red-600 font-medium">举报理由：{{ r.reason }}</p>
                  </div>
                </div>
              </div>
              
              <!-- User Report -->
              <div v-else-if="r.type === 'user' && r.targetUser" class="flex gap-4">
                <div class="w-20 h-20 rounded-full bg-zinc-100 flex items-center justify-center text-2xl font-bold text-zinc-400">
                  {{ r.targetUser.name.charAt(0).toUpperCase() }}
                </div>
                <div>
                  <div class="flex items-center gap-2">
                    <span class="px-2 py-0.5 text-xs rounded bg-orange-100 text-orange-700 font-bold">用户举报</span>
                    <h3 class="font-bold text-zinc-800">{{ r.targetUser.name }}</h3>
                  </div>
                  <p class="text-sm text-zinc-500 mt-1">ID: {{ r.targetUser.id }}</p>
                  <div class="mt-2 bg-red-50 p-2 rounded border border-red-100 inline-block">
                    <p class="text-sm text-red-600 font-medium">举报理由：{{ r.reason }}</p>
                  </div>
                </div>
              </div>

              <!-- Fallback -->
              <div v-else>
                <p class="font-semibold">类型：{{ r.type }}</p>
                <p class="text-sm text-zinc-500">理由：{{ r.reason }}</p>
              </div>
            </div>

            <div class="flex flex-col gap-2 ml-4">
              <button @click="resolve(r.id, 'valid')" class="px-4 py-2 rounded bg-red-600 text-white hover:bg-red-700 shadow-sm transition-colors">有效举报</button>
              <button @click="resolve(r.id, 'invalid')" class="px-4 py-2 rounded border border-zinc-300 text-zinc-600 hover:bg-zinc-50 transition-colors">无效举报</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores'
import { storeToRefs } from 'pinia'
import { useRouter } from 'vue-router'

interface Book {
  id: number
  title: string
  author: string
  price: number
  cover: string
  description: string
  seller: { name: string }
}

const books = ref<Book[]>([])
const reports = ref<any[]>([])
const stats = ref({
  userCount: 0,
  bookCount: 0,
  orderCount: 0,
  totalAmount: 0
})
const router = useRouter()
const userStore = useUserStore()
const { user } = storeToRefs(userStore)

onMounted(() => {
  if (!user.value || user.value.role !== 'admin') {
    alert('无权访问')
    router.push('/')
    return
  }
  loadBooks()
  loadReports()
  loadStats()
})

function loadStats() {
  request.get('/admin/stats').then((res) => (stats.value = res.data))
}

function loadBooks() {
  request.get('/books/pending').then((res) => (books.value = res.data))
}

function loadReports() {
  request.get('/reports/pending').then((res) => (reports.value = res.data))
}

function approve(id: number) {
  if (confirm('确认通过审核？')) {
    request.post(`/books/${id}/approve`)
      .then(() => {
        alert('已通过')
        loadBooks()
      })
      .catch((err) => {
        alert('通过失败：' + (err.response?.data?.detail || err.response?.data?.message || '未知错误'))
      })
  }
}

function resolve(id: number, action: 'valid' | 'invalid') {
  request.post(`/reports/${id}/resolve`, { action })
    .then(() => {
      alert('已处理')
      loadReports()
    })
    .catch((err) => {
      alert('处理失败：' + (err.response?.data?.detail || err.response?.data?.message || '未知错误'))
    })
}

function reject(id: number) {
  if (confirm('确认驳回？')) {
    request.post(`/books/${id}/reject`)
      .then(() => {
        alert('已驳回')
        loadBooks()
      })
      .catch((err) => {
        alert('驳回失败：' + (err.response?.data?.detail || err.response?.data?.message || '未知错误'))
      })
  }
}
</script>
