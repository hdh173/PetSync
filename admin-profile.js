const { createApp, ref, reactive } = Vue;

const app = createApp({
  setup() {
    const activeIndex = ref('2'); // '2' for "个人中心"

    const userInfo = reactive({
      username: "Admin",
      role: "超级管理员",
      avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=admin'
    });

    const stats = ref([
      { title: "总用户数", value: "1,234" },
      { title: "总医生数", value: "56" },
      { title: "日活跃用户", value: "450" },
      { title: "总内容数", value: "8,765" }
    ]);

    const handleSelect = (key) => {
      const linkMap = {
        '1': 'admin-platform.html',
        '2': 'admin-profile.html',
      };
      if (linkMap[key]) {
        window.location.href = linkMap[key];
      } else if (key === '4') { // '4' for "退出"
        ElMessageBox.confirm('确认退出登录吗？', '提示', {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(() => {
          ElMessage.success('已退出登录');
          // In a real app, you would redirect to a login page
          // window.location.href = 'login.html';
        });
      }
    };

    return {
      activeIndex,
      userInfo,
      stats,
      handleSelect
    };
  }
});

app.use(ElementPlus);
app.mount('#app'); 