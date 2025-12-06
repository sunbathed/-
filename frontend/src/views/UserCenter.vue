<template>
  <div class="max-w-4xl mx-auto">
    <h2 class="text-2xl font-bold mb-6">个人中心</h2>
    
    <!-- Tabs -->
    <div class="flex border-b mb-6">
      <button 
        v-for="tab in tabs" 
        :key="tab.key"
        class="px-6 py-3 font-medium transition-colors relative"
        :class="currentTab === tab.key ? 'text-emerald-600' : 'text-zinc-500 hover:text-emerald-600'"
        @click="currentTab = tab.key"
      >
        {{ tab.name }}
        <span v-if="currentTab === tab.key" class="absolute bottom-0 left-0 w-full h-0.5 bg-emerald-600"></span>
      </button>
    </div>

    <!-- Tab Content -->
    
    <!-- 我发布的图书 -->
    <div v-if="currentTab === 'published'" class="animate-fade-in">
      <div v-if="myBooks.length === 0" class="text-zinc-500 bg-white p-8 rounded-lg shadow-sm text-center border border-dashed border-zinc-300">
        暂无发布记录
        <router-link to="/publish" class="block mt-2 text-emerald-600 hover:underline">去发布一本？</router-link>
      </div>
      <div v-else class="grid gap-4">
        <div v-for="b in myBooks" :key="b.id" class="bg-white p-4 rounded-lg shadow-sm border border-zinc-100 flex gap-4 hover:shadow-md transition-shadow">
          <img 
            :src="b.cover || '/picture/default-book.svg'" 
            class="w-24 h-32 object-cover rounded border border-zinc-200" 
            onerror="this.src='/picture/default-book.svg';this.onerror=null;"
          />
          <div class="flex-1 flex flex-col justify-between">
            <div>
              <div class="flex justify-between items-start">
                <h4 class="font-bold text-lg text-zinc-800">{{ b.title }}</h4>
                <span class="px-2 py-1 rounded text-xs font-medium" :class="{
                  'bg-orange-100 text-orange-600': b.status === 'pending',
                  'bg-emerald-100 text-emerald-600': b.status === 'onsale',
                  'bg-red-100 text-red-600': b.status === 'rejected',
                  'bg-zinc-100 text-zinc-500': b.status === 'sold'
                }">
                  {{ statusMap[b.status] || b.status }}
                </span>
              </div>
              <p class="text-emerald-600 font-bold mt-1">¥{{ b.price }}</p>
            </div>
            <div class="flex justify-between items-end">
              <p class="text-zinc-400 text-xs">发布于 {{ new Date(b.createdAt).toLocaleString() }}</p>
              <router-link :to="`/books/${b.id}`" class="text-sm text-emerald-600 hover:text-emerald-700">查看详情</router-link>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 我卖出的订单 -->
    <div v-if="currentTab === 'sold'" class="animate-fade-in">
      <div v-if="soldOrders.length === 0" class="text-zinc-500 bg-white p-8 rounded-lg shadow-sm text-center border border-dashed border-zinc-300">暂无销售记录</div>
      <div v-else class="space-y-4">
        <div v-for="o in soldOrders" :key="o.id" class="bg-white p-5 rounded-lg shadow-sm border border-zinc-100 flex justify-between items-center">
          <div class="flex gap-4 items-center">
            <div class="w-12 h-12 bg-emerald-50 rounded-full flex items-center justify-center text-emerald-600 font-bold text-xl">卖</div>
            <div>
              <h4 class="font-bold text-zinc-800">{{ o.book.title }}</h4>
              <p class="text-sm text-zinc-500 mt-1">买家：<span class="text-zinc-800 font-medium">{{ o.buyer.name }}</span></p>
              <p class="text-xs text-zinc-400 mt-1">{{ new Date(o.createdAt).toLocaleString() }}</p>
            </div>
          </div>
          <div class="text-right">
            <p class="text-emerald-600 font-bold text-lg">+ ¥{{ o.book.price }}</p>
            <span class="inline-block mt-1 px-2 py-0.5 rounded text-xs" :class="o.status === 'completed' ? 'bg-zinc-100 text-zinc-500' : 'bg-orange-100 text-orange-500'">
              {{ o.status === 'completed' ? '已完成' : '交易中' }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 我买到的订单 -->
    <div v-if="currentTab === 'bought'" class="animate-fade-in">
      <div v-if="boughtOrders.length === 0" class="text-zinc-500 bg-white p-8 rounded-lg shadow-sm text-center border border-dashed border-zinc-300">
        暂无购买记录
        <router-link to="/books" class="block mt-2 text-emerald-600 hover:underline">去逛逛？</router-link>
      </div>
      <div v-else class="space-y-4">
        <div v-for="o in boughtOrders" :key="o.id" class="bg-white p-5 rounded-lg shadow-sm border border-zinc-100 flex justify-between items-center">
          <div class="flex gap-4 items-center">
             <div class="w-12 h-12 bg-blue-50 rounded-full flex items-center justify-center text-blue-600 font-bold text-xl">买</div>
             <div>
              <h4 class="font-bold text-zinc-800">{{ o.book.title }}</h4>
              <p class="text-sm text-zinc-500 mt-1">卖家：<span class="text-zinc-800 font-medium">{{ o.book.seller.name }}</span></p>
              <p class="text-xs text-zinc-400 mt-1">{{ new Date(o.createdAt).toLocaleString() }}</p>
            </div>
          </div>
          <div class="text-right">
            <p class="text-zinc-800 font-bold text-lg">- ¥{{ o.book.price }}</p>
            <div class="flex flex-col items-end gap-2 mt-1">
                <span class="px-2 py-0.5 rounded text-xs" :class="o.status === 'completed' ? 'bg-zinc-100 text-zinc-500' : 'bg-blue-100 text-blue-500'">
                {{ o.status === 'completed' ? '已完成' : '待收货' }}
                </span>
                <button 
                    v-if="o.status !== 'completed'"
                    @click="completeOrder(o.id)"
                    class="text-xs px-3 py-1 rounded bg-emerald-600 text-white hover:bg-emerald-700 transition-colors"
                >确认收货</button>
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

const { user } = storeToRefs(useUserStore())
const myBooks = ref<any[]>([])
const soldOrders = ref<any[]>([])
const boughtOrders = ref<any[]>([])
const currentTab = ref('published')

const tabs = [
  { key: 'published', name: '我发布的' },
  { key: 'sold', name: '我卖出的' },
  { key: 'bought', name: '我买到的' }
]

const statusMap: Record<string, string> = {
  pending: '审核中',
  onsale: '在售',
  rejected: '已驳回',
  sold: '已售出',
  off_shelf: '已下架'
}

onMounted(() => {
  if (user.value) {
    loadMyBooks()
    loadSoldOrders()
    loadBoughtOrders()
  }
})

function loadMyBooks() {
  request.get('/books/my', { params: { sellerId: user.value!.id } })
    .then(res => myBooks.value = res.data)
}

function loadSoldOrders() {
  request.get(`/orders/seller/${user.value!.id}`)
    .then(res => soldOrders.value = res.data)
}

function loadBoughtOrders() {
  request.get(`/orders/${user.value!.id}`)
    .then(res => boughtOrders.value = res.data)
}

function completeOrder(orderId: number) {
    if(!confirm('确认收到书籍且无误吗？')) return
    request.post(`/orders/${orderId}/complete`, { user_id: user.value!.id })
        .then(() => {
            alert('订单已完成')
            loadBoughtOrders() // 刷新列表
        })
}
</script>

<style scoped>
.animate-fade-in {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(5px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>