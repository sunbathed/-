<template>
  <div>
    <h2 class="text-2xl font-bold mb-4">我的订单</h2>
    <p v-if="!user" class="text-zinc-500">请先登录</p>
    <p v-else-if="orders.length === 0" class="text-zinc-500">暂无订单</p>
    <div v-else class="space-y-4">
      <div
        v-for="o in orders"
        :key="o.id"
        class="bg-white rounded shadow p-4 flex items-center justify-between"
      >
        <div>
          <h3 class="font-semibold">{{ o.book.title }}</h3>
          <p class="text-sm text-zinc-500">卖家：{{ o.book.seller.name }}</p>
          <p class="text-xs text-zinc-400">{{ new Date(o.createdAt).toLocaleString() }}</p>
        </div>
        <div class="text-right">
          <p class="text-emerald-600 font-bold">¥{{ o.book.price }}</p>
          <div v-if="o.status === 'pending'">
            <span class="text-xs text-orange-500 mr-2">待确认</span>
            <button @click="complete(o.id)" class="text-xs px-2 py-1 rounded border border-emerald-600 text-emerald-600 hover:bg-emerald-50">确认收货</button>
          </div>
          <span v-else class="text-xs text-zinc-500">已完成</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import request from '@/utils/request'
import { useUserStore } from '@/stores'

interface Order {
  id: number
  book: {
    title: string
    price: number
    seller: { name: string }
  }
  status: string
  createdAt: string
}

const { user } = useUserStore()
const orders = ref<Order[]>([])

onMounted(() => {
  loadOrders()
})

function loadOrders() {
  if (!user) return
  request.get(`/orders/${user.id}`).then((res) => (orders.value = res.data))
}

function complete(id: number) {
  if (!user) return
  if (confirm('确认已收到书本且无误？')) {
    request.post(`/orders/${id}/complete`, { user_id: user.id }).then(() => {
      alert('订单已完成')
      loadOrders()
    })
  }
}
</script>