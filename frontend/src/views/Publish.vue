<template>
  <div class="max-w-2xl mx-auto">
    <h2 class="text-2xl font-bold mb-4">发布图书</h2>
    <form @submit.prevent="submit" class="space-y-4">
      <input
        required
        placeholder="书名"
        v-model="form.title"
        class="w-full px-3 py-2 border rounded"
      />
      <input
        required
        placeholder="作者"
        v-model="form.author"
        class="w-full px-3 py-2 border rounded"
      />
      <input
        required
        type="number"
        step="0.1"
        placeholder="价格"
        v-model.number="form.price"
        class="w-full px-3 py-2 border rounded"
      />
      <input
        placeholder="封面 URL（可选）"
        v-model="form.cover"
        class="w-full px-3 py-2 border rounded"
      />
      <textarea
        placeholder="图书描述"
        v-model="form.description"
        class="w-full px-3 py-2 border rounded"
        rows="4"
      />
      <button type="submit" class="px-4 py-2 rounded bg-emerald-600 text-white hover:bg-emerald-700">
        发布
      </button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '@/utils/request'
import { useUserStore } from '@/stores'

const { user } = useUserStore()
const router = useRouter()

const form = reactive({
  title: '',
  author: '',
  price: 0,
  cover: '',
  description: '',
})

function submit() {
  if (!user) return alert('请先登录')
  request.post('/books', { ...form, seller: { id: user.id } }).then(() => {
    alert('发布成功！请等待管理员审核通过后上架。')
    router.push('/books')
  })
}
</script>