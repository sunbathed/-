<template>
  <div v-if="book" class="grid md:grid-cols-3 gap-6">
    <img
      :src="book.cover || '/picture/default-book.svg'"
      :alt="book.title"
      class="rounded shadow w-full h-96 object-cover border border-zinc-100"
      onerror="this.src='/picture/default-book.svg';this.onerror=null;"
    />
    <div class="md:col-span-2 space-y-4">
      <h1 class="text-2xl font-bold">{{ book.title }}</h1>
      <p class="text-zinc-600">作者：{{ book.author }}</p>
      <p class="text-3xl text-emerald-600 font-bold">¥{{ book.price }}</p>
      <p class="text-zinc-600">卖家：{{ book.seller.name }}</p>
      <p class="text-zinc-700">{{ book.description }}</p>
      <button
        v-if="book.status === 'onsale'"
        @click="order"
        class="inline-flex items-center gap-2 px-4 py-2 rounded bg-emerald-600 text-white hover:bg-emerald-700"
      >
        <ShoppingCart class="w-5 h-5" /> 立即购买
      </button>
      <span v-else-if="book.status === 'sold'" class="text-zinc-500 font-bold bg-zinc-100 px-3 py-1 rounded">已售出</span>
      <span v-else-if="book.status === 'pending'" class="text-orange-600 font-bold bg-orange-50 px-3 py-1 rounded">审核中</span>
      <span v-else-if="book.status === 'rejected'" class="text-red-600 font-bold bg-red-50 px-3 py-1 rounded">已驳回</span>
      <span v-else-if="book.status === 'off_shelf'" class="text-zinc-500 font-bold bg-zinc-100 px-3 py-1 rounded">已下架</span>
      <span v-else class="text-zinc-500">暂不可售</span>

      <button
        v-if="user"
        @click="showReport = true"
        class="ml-3 px-4 py-2 rounded border border-red-600 text-red-600 hover:bg-red-50"
      >举报</button>

      <!-- 留言区 -->
      <div class="mt-8 pt-6 border-t">
        <h3 class="text-xl font-bold mb-4">留言区</h3>
        <div class="space-y-4 mb-6">
          <div v-for="c in comments" :key="c.id" class="bg-zinc-50 p-3 rounded">
            <div class="flex justify-between text-sm text-zinc-500 mb-1">
              <span>{{ c.user.name }}</span>
              <span>{{ new Date(c.createdAt).toLocaleString() }}</span>
            </div>
            <p>{{ c.content }}</p>
          </div>
          <p v-if="comments.length === 0" class="text-zinc-400">暂无留言</p>
        </div>
        <div v-if="user" class="flex gap-2">
          <input v-model="newComment" placeholder="输入留言..." class="flex-1 px-3 py-2 border rounded" />
          <button @click="postComment" class="px-4 py-2 bg-emerald-600 text-white rounded hover:bg-emerald-700">发送</button>
        </div>
        <p v-else class="text-zinc-500">登录后可留言</p>
      </div>
    </div>
  </div>
  <p v-else class="text-zinc-500">加载中…</p>

  <div v-if="showReport" class="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
    <div class="bg-white rounded p-6 w-96">
      <h3 class="font-bold mb-4">举报该书籍</h3>
      <textarea v-model="reportReason" placeholder="填写举报理由" rows="4" class="w-full px-3 py-2 border rounded"></textarea>
      <div class="mt-4 flex gap-2">
        <button @click="submitReport" class="flex-1 px-4 py-2 rounded bg-red-600 text-white">提交举报</button>
        <button @click="showReport=false" class="flex-1 px-4 py-2 rounded border">取消</button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ShoppingCart } from 'lucide-vue-next'
import request from '@/utils/request'
import { useUserStore } from '@/stores'
import { storeToRefs } from 'pinia'

interface Book {
  id: number
  title: string
  author: string
  price: number
  cover: string
  description: string
  status: string
  seller: { name: string }
}

interface Comment {
  id: number
  content: string
  user: { name: string }
  createdAt: string
}

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const { user } = storeToRefs(userStore)

const book = ref<Book | null>(null)
const comments = ref<Comment[]>([])
const newComment = ref('')
const showReport = ref(false)
const reportReason = ref('')

onMounted(() => {
  loadBook()
  loadComments()
})

function loadBook() {
  request.get(`/books/${route.params.id}`)
    .then((res) => {
        book.value = res.data
    })
    .catch(err => {
        console.error('Failed to fetch book:', err)
        alert('获取图书详情失败')
    })
}

function loadComments() {
  request.get(`/books/${route.params.id}/comments`).then(res => comments.value = res.data)
}

function postComment() {
  if (!newComment.value.trim()) return
  request.post('/comments', {
    book_id: book.value!.id,
    user_id: user.value!.id,
    content: newComment.value
  }).then(() => {
    newComment.value = ''
    loadComments()
  })
}

function order() {
  if (!user.value) return alert('请先登录')
  request.post('/orders', { book_id: book.value!.id, buyer_id: user.value.id }).then(() => {
    alert('下单成功')
    router.push('/orders')
  })
}

function submitReport() {
  if (!reportReason.value.trim()) return alert('请填写举报理由')
  request.post('/reports', {
    type: 'book',
    book_id: book.value!.id,
    reporter_id: user.value!.id,
    reason: reportReason.value
  }).then(() => {
    alert('举报已提交')
    showReport.value = false
    reportReason.value = ''
  })
}
</script>
