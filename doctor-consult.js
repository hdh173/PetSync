const { createApp, ref, computed, reactive, onMounted } = Vue;

const app = createApp({
  setup() {
    const userInfo = reactive({
        username: "王医生"
    });
    // 模拟数据
    const sessions = ref([
      {
        id: 'S001',
        userName: '张三',
        avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=1',
        status: 'ONGOING',
        lastMessage: '谢谢您的帮助',
        lastTime: '19:35',
        unread: 2,
        gender: '男',
        occupation: '学生',
        sessions: 3,
        lastSession: '2025-05-10',
        summary: '',
        suggestion: '',
        feedbackTime: null
      },
      {
        id: 'S002',
        userName: '李四',
        avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=2',
        status: 'ENDS',
        lastMessage: '下次还会来',
        lastTime: '18:20',
        unread: 0,
        gender: '女',
        occupation: '教师',
        sessions: 1,
        lastSession: '2025-05-12',
        summary: '',
        suggestion: '',
        feedbackTime: null
      },
      {
        id: 'S003',
        userName: '王五',
        avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=3',
        status: 'ONGOING',
        lastMessage: '好的，谢谢医生。',
        lastTime: '14:21',
        unread: 0,
        gender: '男',
        occupation: '设计师',
        sessions: 5,
        lastSession: '2025-05-11',
        summary: '',
        suggestion: '',
        feedbackTime: null
      },
      {
        id: 'S004',
        userName: '赵六',
        avatar: 'https://api.dicebear.com/7.x/miniavs/svg?seed=4',
        status: 'ENDED',
        lastMessage: '明白了！',
        lastTime: '15:13',
        unread: 0,
        gender: '女',
        occupation: '产品经理',
        sessions: 2,
        lastSession: '2025-05-09',
        summary: '初步判断为轻度炎症',
        suggestion: '使用宠物专用洗眼液清洗，观察一天。',
        feedbackTime: null
      }
    ]);
    const messagesData = ref({
      S001: [
        { type: 'received', text: '医生您好', time: '19:34' },
        { type: 'sent', text: '您好，有什么可以帮您？', time: '19:34' },
        { type: 'received', text: '我家猫猫不舒服', time: '19:35' },
        { type: 'sent', text: '请具体描述一下症状。', time: '19:35' },
        { type: 'received', text: '它不爱吃饭，没精神。', time: '19:36' },
        { type: 'sent', text: '这种情况持续多久了？', time: '19:36' },
        { type: 'received', text: '大概两天了。', time: '19:37' },
        { type: 'sent', text: '有呕吐或腹泻吗？', time: '19:37' },
        { type: 'received', text: '没有。', time: '19:38' },
        { type: 'sent', text: '体温正常吗？', time: '19:38' },
        { type: 'received', text: '我量了一下，有点低。', time: '19:39' },
        { type: 'sent', text: '建议尽快带到宠物医院检查。', time: '19:39' },
        { type: 'received', text: '好的，谢谢医生。', time: '19:40' },
        { type: 'sent', text: '不客气，希望它早日康复。', time: '19:40' },
        { type: 'received', text: '谢谢！', time: '19:41' }
      ],
      S002: [
        { type: 'received', text: '医生在吗？', time: '18:18' },
        { type: 'sent', text: '您好，请问有什么问题？', time: '18:19' },
        { type: 'received', text: '下次还会来', time: '18:20' }
      ],
      'S003': [
        { type: 'received', text: '你好，医生。我的狗狗最近好像没什么精神，也不怎么吃东西，有点担心。', time: '10:05' },
        { type: 'sent', text: '你好，请问狗狗多大了？这种情况持续多久了？除了精神不振和食欲下降，还有其他症状吗？比如呕吐、腹泻或者咳嗽？', time: '10:06' }
      ],
      'S004': [
        { type: 'received', text: '医生你好，我的猫咪是一只英短，最近发现它总是在舔毛，比平时频繁很多，正常吗？', time: '14:20' },
        { type: 'received', text: '需不需要带去医院看看？', time: '14:21' },
        { type: 'received', text: '我的兔子眼睛有点红，是生病了吗？', time: '15:02' },
        { type: 'sent', text: '兔子眼睛变红的原因有很多，比如环境刺激、感染或者异物。您可以先检查一下它的眼睛里有没有明显异物。', time: '15:03' },
        { type: 'received', text: '好的，我看看。', time: '15:04' },
        { type: 'sent', text: '如果方便的话，也可以拍张清晰的照片给我看看。', time: '15:05' },
        { type: 'received', text: '好的，稍等', time: '15:06' },
        { type: 'sent', text: '不客气', time: '15:07' },
        { type: 'received', text: '图片.jpg', time: '15:08' },
        { type: 'sent', text: '从照片上看，有一点炎症。建议先用宠物专用的洗眼液清洗一下，观察一天看看情况。', time: '15:10' },
        { type: 'received', text: '好的，谢谢医生！', time: '15:11' },
        { type: 'sent', text: '不客气，如果明天没有好转或者加重了，建议及时就医。', time: '15:12' },
        { type: 'received', text: '明白了！', time: '15:13' }
      ]
    });
    const currentSessionId = ref('S001');
    const search = ref('');
    const newMessage = ref('');
    const summary = ref('');
    const suggestion = ref('');
    const scrollbarRef = ref(null);
    const activeIndex = ref('1'); // '1' for "咨询服务"

    // 进入页面时提醒
    onMounted(() => {
      ElementPlus.ElMessageBox.alert('您已进入在线工作状态，请及时处理咨询。', '状态提醒', {
        confirmButtonText: '我已知晓',
        type: 'info',
      });
    });

    const handleSelect = (key) => {
      const linkMap = {
        '1': 'doctor-consult.html',
        '2': 'doctor-profile.html',
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

    const filteredSessions = computed(() => {
      return sessions.value.filter(s => s.userName.includes(search.value));
    });
    const currentSession = computed(() => sessions.value.find(s => s.id === currentSessionId.value));
    const currentMessages = computed(() => messagesData.value[currentSessionId.value] || []);

    function switchSession(id) {
      currentSessionId.value = id;
      summary.value = currentSession.value.summary || '';
      suggestion.value = currentSession.value.suggestion || '';
    }
    function sendMessage() {
      if (!newMessage.value.trim()) return;
      messagesData.value[currentSessionId.value] = messagesData.value[currentSessionId.value] || [];
      messagesData.value[currentSessionId.value].push({ type: 'sent', text: newMessage.value, time: new Date().toLocaleTimeString().slice(0,5) });
      newMessage.value = '';
      
      Vue.nextTick(() => {
        scrollbarRef.value?.setScrollTop(scrollbarRef.value.wrapRef.scrollHeight);
      });
    }
    function submitFeedback() {
       ElMessageBox.confirm('确认提交本次咨询的反馈吗？', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        currentSession.value.summary = summary.value;
        currentSession.value.suggestion = suggestion.value;
        currentSession.value.status = 'ENDED';
        currentSession.value.feedbackTime = new Date().toLocaleString();
        ElMessage.success('反馈已提交！');
      }).catch(() => {
        ElMessage.info('已取消提交');
      });
    }

    const getSessionEndText = (session) => {
      if (!session) return '';
      if (session.status === 'ENDS') return '用户已结束对话';
      if (session.status === 'TIMEOUT') return '对话已超时结束';
      if (session.status === 'ENDED' && session.feedbackTime) {
        return `已于 ${session.feedbackTime} 提交反馈`;
      }
      if (session.status === 'ENDED') {
        return '您已提交反馈';
      }
      return '';
    };

    const exitWorkStatus = () => {
      ElementPlus.ElMessageBox.confirm('退出前请确保所有咨询反馈均已提交。是否确认退出？', '退出确认', {
        confirmButtonText: '直接退出',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        ElementPlus.ElMessage.info('正在退出工作状态...');
        window.location.href = 'doctor-profile.html';
      }).catch(() => {
        ElementPlus.ElMessage.info('已取消退出操作。');
      });
    };

    return {
      userInfo,
      sessions,
      search,
      filteredSessions,
      currentSessionId,
      newMessage,
      summary,
      suggestion,
      currentSession,
      currentMessages,
      scrollbarRef,
      activeIndex,
      handleSelect,
      switchSession,
      sendMessage,
      submitFeedback,
      getSessionEndText,
      exitWorkStatus,
    };
  }
});
app.use(ElementPlus);
app.mount('#app'); 