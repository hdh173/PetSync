const { createApp, ref, computed, reactive, onMounted, nextTick } = Vue;

const app = createApp({
  setup() {
    const activeTab = ref('doctor-audit');
    const activeIndex = ref('1'); // '1' for "审核中心"
    
    const userInfo = reactive({
        username: "Admin"
    });

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
          // window.location.href = 'login.html';
        });
      }
    };

    // --- 医生审核 ---
    const doctorAuditStatusFilter = ref('全部');
    const doctorStatusOptions = ref(['全部', '待审核', '已通过', '未通过']);
    const doctorAuditList = ref([
      { id: 201, name: '李医生', hospital: '中心宠物医院', qualification: 'https://images.unsplash.com/photo-1559827291-72ee739d0d95?w=100', qualificationId: '110101202301011234', status: '待审核' },
      { id: 202, name: '张兽医', hospital: '爱心动物诊所', qualification: 'https://images.unsplash.com/photo-1589922582153-f763205a5a6a?w=100', qualificationId: '120102202302022345', status: '已通过' },
      { id: 203, name: '王医生', hospital: '爱宠医院', qualification: 'https://images.unsplash.com/photo-1583337130587-d8a6a2e8a7e2?w=100', qualificationId: '130103202303033456', status: '未通过' }
    ]);
    const filteredDoctorAuditList = computed(() => {
      if (doctorAuditStatusFilter.value === '全部') {
        return doctorAuditList.value;
      }
      return doctorAuditList.value.filter(d => d.status === doctorAuditStatusFilter.value);
    });
    const approveDoctor = (doctor) => {
      ElMessageBox.confirm(`确认通过 ${doctor.name} 的资质审核吗？`, '审核确认', {
        confirmButtonText: '确认通过',
        cancelButtonText: '取消',
        type: 'success',
      }).then(() => {
        doctor.status = '已通过';
        ElMessage.success('审核已通过');
      });
    };
    const rejectDoctor = (doctor) => {
      ElMessageBox.prompt('请输入驳回理由', '审核驳回', {
        confirmButtonText: '确认驳回',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputValidator: (value) => {
          if (!value || value.trim().length === 0) {
            return '驳回理由不能为空';
          }
          return true;
        },
      }).then(({ value }) => {
        doctor.status = '未通过';
        ElMessage.warning(`审核已驳回，理由：${value}`);
      });
    };

    // --- 举报处理 ---
    const reportStatusFilter = ref('全部');
    const reportStatusOptions = ref(['全部', '待处理', '已处理', '已取消']);
    const reportTypeSearch = ref('');
    const reportList = ref([
      { id: 301, reporter: '热心市民张女士', target: '李医生', type: '医生服务', status: '待处理', handleMsg: '', contentType: 'text', content: '该医生服务态度恶劣，回答问题非常不耐烦，完全没有解决我的问题。' },
      { id: 302, reporter: '正义伙伴', target: '评论ID:201', type: '内容举报', status: '已处理', handleMsg: '经核实，该评论存在引战行为，已删除。', contentType: 'text', content: '你懂什么？不懂就别瞎说。' },
      { id: 303, reporter: '用户C', target: '文章ID:45', type: '内容举报', status: '待处理', handleMsg: '', contentType: 'image', content: 'https://images.unsplash.com/photo-1592194991136-44165a6a69a7?w=100' },
      { id: 304, reporter: '用户D', target: '张医生', type: '医生服务', status: '已取消', handleMsg: '用户表示是自己误操作。', contentType: 'text', content: '医生未按时上线。' }
    ]);
    const filteredReportList = computed(() => {
      return reportList.value.filter(r => {
        const statusMatch = reportStatusFilter.value === '全部' || r.status === reportStatusFilter.value;
        const typeMatch = reportTypeSearch.value === '' || r.type.includes(reportTypeSearch.value);
        return statusMatch && typeMatch;
      });
    });
    const handleReport = (report, isApproved) => {
       if (!isApproved) {
        report.status = '已取消';
        report.handleMsg = '管理员取消了处理';
        ElMessage.info('举报已取消');
        return;
      }
      if (!report.handleMsg || report.handleMsg.trim() === '') {
        ElMessage.warning('请填写处理说明');
        return;
      }
      report.status = '已处理';
      ElMessage.success('举报已处理');
    };
    const viewHandleNote = (note) => {
      ElMessageBox.alert(note, '处理说明详情', {
        confirmButtonText: '关闭',
      });
    };

    // --- 数据统计 ---
    const dauChart = ref(null);
    const consultationChart = ref(null);
    const initCharts = () => {
      // 日活用户图
      const dauChartInstance = echarts.init(dauChart.value);
      dauChartInstance.setOption({
        title: { text: '最近7日活跃用户' },
        tooltip: { trigger: 'axis' },
        xAxis: { data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] },
        yAxis: {},
        series: [{ name: 'DAU', type: 'line', data: [320, 332, 301, 334, 390, 330, 450] }]
      });
      // 咨询量图
      const consultationChartInstance = echarts.init(consultationChart.value);
      consultationChartInstance.setOption({
        title: { text: '最近7日咨询量' },
        tooltip: { trigger: 'axis' },
        xAxis: { data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'] },
        yAxis: {},
        series: [{ name: '咨询量', type: 'bar', data: [50, 62, 71, 74, 80, 85, 89] }]
      });
    };

    onMounted(() => {
      nextTick(() => {
          if(activeTab.value === 'data-statistics') {
              initCharts();
          }
      });
    });
    
    const onTabChange = (tabName) => {
        if (tabName === 'data-statistics') {
            nextTick(() => {
                initCharts();
            });
        }
    };

    return {
      activeTab,
      activeIndex,
      userInfo,
      handleSelect,
      // 医生审核
      doctorAuditList,
      filteredDoctorAuditList,
      doctorAuditStatusFilter,
      doctorStatusOptions,
      approveDoctor,
      rejectDoctor,
      // 举报处理
      reportList,
      filteredReportList,
      reportStatusFilter,
      reportStatusOptions,
      reportTypeSearch,
      handleReport,
      viewHandleNote,
      // 数据统计
      dauChart,
      consultationChart,
      onTabChange,
    };
  }
});

app.use(ElementPlus);
app.mount('#app'); 