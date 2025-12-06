import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'
import Home from '@/views/Home.vue'
import BookList from '@/views/BookList.vue'
import BookDetail from '@/views/BookDetail.vue'
import Publish from '@/views/Publish.vue'
import Orders from '@/views/Orders.vue'
import UserCenter from '@/views/UserCenter.vue'
import Admin from '@/views/Admin.vue'
import { useUserStore } from '@/stores'
import { storeToRefs } from 'pinia'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/',
      component: Layout,
      children: [
        { path: 'books', name: 'BookList', component: BookList },
        { path: 'books/:id', name: 'BookDetail', component: BookDetail },
        { path: 'publish', name: 'Publish', component: Publish },
        { path: 'orders', name: 'Orders', component: Orders },
        { path: 'center', name: 'UserCenter', component: UserCenter },
        { path: 'admin', name: 'Admin', component: Admin },
      ],
    },
  ],
})

router.beforeEach((to, _from, next) => {
  const userStore = useUserStore()
  const { user } = storeToRefs(userStore)
  
  if (user.value && to.name === 'Home') {
    next({ name: 'BookList' })
  } else {
    next()
  }
})

export default router