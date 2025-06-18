const { createApp, ref, nextTick, computed, onMounted, reactive, watch } = Vue;

const app = createApp({
    setup() {
        window.auth.isLoggedIn();
        const activeIndex = ref('4');
        const linkList = ['index.html', 'selfcenter.html', 'index.html', 'event.html', 'consult.html', 'selfcenter.html'];
        const websites = ref(['主页', '个人中心', '主页', '事件簿', '咨询服务', '个人中心']);
        const currentUser = reactive({});
        const newMessage = ref('');
        const isWaitingResponse = ref(false);

        const messagesContainer = ref(null);
        const quickSuggestions = [
            "我的猫总是乱抓家具，该怎么办？",
            "狗狗总是叫个不停，有什么训练方法？",
            "宠物最近不爱吃东西，是生病了吗？",
            "第一次养宠物，有哪些注意事项？"
        ];

        const AIMessages = ref([]);

        const onlineMessages = ref([
            {
                text: '您好，请问有什么可以帮您？',
                type: 'received',
                timestamp: new Date()
            },
            {
                text: '医生，我家猫咪最近老是打喷嚏，是感冒了吗？',
                type: 'sent',
                timestamp: new Date()
            },
            {
                text: '有可能是上呼吸道感染，请问有没有流鼻涕或者眼睛分泌物？',
                type: 'received',
                timestamp: new Date()
            },
            {
                text: '鼻涕有点多，眼角也有些眼屎。',
                type: 'sent',
                timestamp: new Date()
            },
            {
                text: '建议先观察体温，如果有发热就需要及时就医。',
                type: 'received',
                timestamp: new Date()
            },
            {
                text: '它摸起来有点热，还不太爱吃东西。',
                type: 'sent',
                timestamp: new Date()
            },
            {
                text: '那很有可能是发烧，建议尽快带去宠物医院检查。',
                type: 'received',
                timestamp: new Date()
            },
            {
                text: '好的，我明天带它去看看。',
                type: 'sent',
                timestamp: new Date()
            },
            {
                text: '好的，外出记得保暖，注意不要让猫咪受风。',
                type: 'received',
                timestamp: new Date()
            },
            {
                text: '明白了，谢谢医生！',
                type: 'sent',
                timestamp: new Date()
            }
        ]);



        const handleSelect = (key, keyPath) => {
            console.log(key, keyPath);
            window.location.href = linkList[key];
        };

        const selectedServiceType = ref('线上咨询');
        const allServiceTypes = ref(['线上咨询', 'AI 咨询', '咨询历史']);

        function navigateToService(serviceType) {
            selectedServiceType.value = serviceType;
            scrollToBottom();
        }

        const sendAIMessage = async () => {
            const msg = newMessage.value.trim();
            if (!msg || isWaitingResponse.value) return;

            // 添加用户消息
            AIMessages.value.push({
                text: msg,
                type: 'sent',
                timestamp: new Date()
            });

            newMessage.value = '';
            isWaitingResponse.value = true; // 开始等待响应
            await scrollToBottom();

            try {
                // 调用API获取AI回复
                const response = await startAIChat(msg);

                AIMessages.value.push({
                    text: response,
                    type: 'received',
                    timestamp: new Date()
                });
            } catch (error) {
                console.error('获取AI回复失败:', error);
                AIMessages.value.push({
                    text: "抱歉，我暂时无法处理您的请求。您可以尝试重新表述您的问题。",
                    type: 'received',
                    timestamp: new Date()
                });
            } finally {
                isWaitingResponse.value = false; // 结束等待状态
                await scrollToBottom();
            }
        };

        // 发送快速问题
        const sendQuickQuestion = (question) => {
            newMessage.value = question;
            sendAIMessage();
        };

        // 滚动到底部
        function scrollToBottom() {
            nextTick(() => {
                if (messagesContainer.value) {
                    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
                }
            });
        }

        // 格式化时间
        const formatTime = (date) => {
            return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        };

        // API 数据
        const specialties = ref([
            { name: '狗狗' },
            { name: '猫咪' },
            { name: '仓鼠' },
            { name: '鹦鹉' },
            { name: '兔子' },
            { name: '爬行类' },
            { name: '水族动物' },
            { name: '乌龟' },
            { name: '龙猫' },
            { name: '蜜袋鼯' },
            { name: '异宠护理' },
            { name: '宠物行为训练' },
            { name: '宠物营养' },
            { name: '宠物心理' },
            { name: '宠物医疗' },
            { name: '老年宠物照护' },
            { name: '流浪动物救助' },
            { name: '宠物美容' },
            { name: '宠物繁育' },
            { name: '宠物安乐' }
        ]);

        const filteredExperts = computed(() => {
            if (selectedTags.value.length === 0) {
                return consultants.value
            }

            return consultants.value.filter(expert =>
                expert.specialties.some(spec => selectedTags.value.includes(spec))
            )
        })


        const detailExpert = reactive(
            {
                name: '',
                avatar: '',
                introduction: '',
                rating: 0,
                hospital: '',
                education: '',
                specialties: [''],
                consultations: 0,
                online: true
            }
        );

        const consultants = ref([
            {
                name: '李小猫',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=CatExpert1',
                introduction: '资深猫咪行为分析师，擅长处理猫咪焦虑与攻击行为。',
                rating: 4.8,
                hospital: '上海宠爱动物医院',
                education: '华东师范大学 动物心理学硕士',
                specialties: ['猫咪', '宠物心理'],
                consultations: 312
            },
            {
                name: '张旺旺',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=DogExpert1',
                introduction: '拥有十年犬类心理研究经验，熟悉各种犬种的行为管理。',
                rating: 4.6,
                hospital: '北京幸福犬宠物中心',
                education: '中国农业大学 动物行为学博士',
                specialties: ['狗狗', '宠物行为训练'],
                consultations: 415
            },
            {
                name: '陈羽毛',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=BirdExpert1',
                introduction: '擅长解决鸟类宠物的应激反应问题，耐心细致。',
                rating: 4.9,
                hospital: '广州小羽宠物诊所',
                education: '南京林业大学 野生动物与生态学本科',
                specialties: ['鹦鹉', '宠物心理'],
                consultations: 168
            },
            {
                name: '王喵喵',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=CatExpert2',
                introduction: '宠物心理咨询师，精通猫咪适应新环境的训练与陪伴。',
                rating: 4.7,
                hospital: '深圳安心宠物医院',
                education: '中山大学 心理学学士',
                specialties: ['猫咪', '宠物心理'],
                consultations: 289
            },
            {
                name: '赵哈士奇',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=DogExpert2',
                introduction: '专注犬只分离焦虑的干预方法，擅长建立主人与宠物之间的信任。',
                rating: 4.5,
                hospital: '成都友爱动物诊所',
                education: '四川农业大学 宠物行为硕士',
                specialties: ['狗狗', '宠物心理', '宠物行为训练'],
                consultations: 354
            },
            {
                name: '林仓仓',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=HamsterExpert1',
                introduction: '小型啮齿类护理专家，擅长仓鼠健康管理与环境适应。',
                rating: 4.4,
                hospital: '杭州萌宠天地',
                education: '浙江大学 动物营养与护理本科',
                specialties: ['仓鼠', '宠物营养'],
                consultations: 237
            },
            {
                name: '苏跳跳',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=RabbitExpert1',
                introduction: '专注兔子行为与健康，多年异宠护理经验。',
                rating: 4.7,
                hospital: '武汉可爱兔宠物医院',
                education: '华中农业大学 异宠护理硕士',
                specialties: ['兔子', '异宠护理'],
                consultations: 198
            },
            {
                name: '钱甲鱼',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=TurtleExpert1',
                introduction: '擅长水龟陆龟照护，提供专业的水族环境搭配建议。',
                rating: 4.6,
                hospital: '南京水宠中心',
                education: '南京农业大学 水族动物学硕士',
                specialties: ['乌龟', '水族动物'],
                consultations: 163
            },
            {
                name: '周小蛇',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=SnakeExpert1',
                introduction: '从事爬行类动物行为研究，尤擅蜥蜴与蛇类日常护理。',
                rating: 4.3,
                hospital: '重庆异宠研究所',
                education: '西南大学 爬行类生态硕士',
                specialties: ['爬行类', '异宠护理'],
                consultations: 152
            },
            {
                name: '艾心心',
                avatar: 'https://api.dicebear.com/8.x/pixel-art/svg?seed=PetPsychologist1',
                introduction: '专注宠物心理疏导与老年宠物照护，关注情绪与陪伴。',
                rating: 4.9,
                hospital: '上海安宁动物医院',
                education: '北京大学 心理学博士',
                specialties: ['宠物心理', '老年宠物照护'],
                consultations: 301
            }
        ]);


        // 搜索和筛选
        const selectedTags = ref([]);
        const searchQuery = ref('');
        const expertSearchQuery = ref('');

        // 过滤专家标签
        const filteredTags = computed(() => {
            return specialties.value.filter(tag =>
                tag.name.toLowerCase().includes(searchQuery.value.toLowerCase())
            );
        });

        // 切换标签
        function toggleTag(tag) {
            const index = selectedTags.value.indexOf(tag);
            if (index === -1) {
                selectedTags.value.push(tag);
            } else {
                selectedTags.value.splice(index, 1);
            }
            handleFilter();
        }

        // 清除筛选条件
        function clearFilter() {
            selectedTags.value = [];
            expertSearchQuery.value = '';
            filteredExperts.value = consultants.value;
        }

        // 筛选咨询师
        async function handleFilter() {
            console.log('handleFilter');
        }

        // 获取咨询师列表和标签
        const getConsultationList = async () => {
            try {
                const response = await request.get('/consultation');
                return response.data;
            } catch (error) {
                console.error('Error fetching consultation list:', error);
                throw error;
            }
        };

        // 获取咨询师列表
        async function fetchConsultants() {
            try {
                const data = await getConsultationList();
                specialties.value = data.Specialties;
                consultants.value = data.Consultants;
                filteredExperts.value = data.Consultants;
            } catch (error) {
                console.error('Error fetching consultants:', error);
            }
        }

        const detailExpertVisible = ref(false);

        const showExpertDetails = (index) => {
            Object.assign(detailExpert, filteredExperts.value[index]);
            detailExpertVisible.value = true;
        };

        // 截断描述文本
        const truncateDescription = (text, length = 40) => {
            if (!text) return '暂无简介';
            return text.length > length ? text.substring(0, length) + '...' : text;
        }

        // 获取评分星星
        const getRatingStars = (rating) => {
            const fullStars = Math.floor(rating || 0);
            const halfStar = (rating || 0) % 1 >= 0.5 ? 1 : 0;
            const emptyStars = 5 - fullStars - halfStar;
            return '★'.repeat(fullStars) + (halfStar ? '☆' : '') + '☆'.repeat(emptyStars);
        }

        const onlineChat = ref(false);

        const goConsulting = (index) => {
            Object.assign(detailExpert, filteredExperts.value[index]);
            onlineChat.value = true;
            scrollToBottom();
        };

        const sendOnlineMessage = () => {
            const msg = newMessage.value.trim();
            if (!msg || isWaitingResponse.value) return;

            // 添加用户消息
            onlineMessages.value.push({
                text: msg,
                type: 'sent',
                timestamp: new Date()
            });

            newMessage.value = '';
            scrollToBottom();
        };

        const openEndBox = () => {
            ElementPlus.ElMessageBox.confirm(
                '是否确认离开当前对话？',
                '结束提示',
                {
                    confirmButtonText: '确认',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            )
                .then(() => {
                    onlineChat.value = false;
                })
        };

        const ServiceFilters = ref(['线上咨询', 'AI 咨询']);
        const selectedFilter = ref('');

        const selectFilter = (filter) => {
            if (selectedFilter.value === filter) {
                selectedFilter.value = ''; // 如果点击已选中的项，则取消选择
            } else {
                selectedFilter.value = filter; // 否则选择新项
            }
        };

        const startDate = ref('');
        const endDate = ref('');
        const shortcuts = [
            {
                text: '今天',
                value: new Date(),
            },
            {
                text: '昨天',
                value: () => {
                    const date = new Date()
                    date.setTime(date.getTime() - 3600 * 1000 * 24)
                    return date
                },
            },
            {
                text: '上周今天',
                value: () => {
                    const date = new Date()
                    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
                    return date
                },
            },
        ];

        const disabledDate = (time) => {
            return time.getTime() > Date.now()
        }

        // 清除筛选方法
        const clearHistoryFilters = () => {
            selectedFilter.value = '';
            startDate.value = '';
            endDate.value = '';
        };

        const loading = ref(false);
        const historyData = ref([
            {
                type: 'AI 咨询',
                timestamp: '2025-06-12 10:30:00',
                title: '猫咪总是晚上喵喵叫怎么办？',
                content: '用户咨询关于猫咪晚上过度叫唤的原因与处理方法。'
            },
            {
                type: '线上咨询',
                timestamp: '2025-06-11 15:45:00',
                title: '教猫咪使用猫砂盆的技巧',
                content: '讨论猫咪排泄训练与习惯建立的有效方法。'
            },
            {
                type: '线上咨询',
                timestamp: '2025-06-10 09:10:00',
                title: '如何安抚紧张的猫咪？',
                content: '猫咪因新环境而焦虑，寻求安抚与适应方案。'
            },
            {
                type: 'AI 咨询',
                timestamp: '2025-06-09 14:20:00',
                title: '猫咪频繁打喷嚏要看医生吗？',
                content: '分析猫咪呼吸道异常是否属于轻微感冒或需就医。'
            },
            {
                type: 'AI 咨询',
                timestamp: '2025-06-08 11:30:00',
                title: '如何阻止猫咪乱抓沙发？',
                content: '提供猫抓板训练与正向引导行为的技巧。'
            },
            {
                type: '线上咨询',
                timestamp: '2025-06-07 16:00:00',
                title: '猫咪不亲人怎么办？',
                content: '探讨社交障碍与建立信任的方法。'
            },
            {
                type: 'AI 咨询',
                timestamp: '2025-06-06 13:00:00',
                title: '猫咪最近食欲下降',
                content: '询问食欲变化是否与天气或健康状况有关。'
            },
            {
                type: '线上咨询',
                timestamp: '2025-06-05 17:40:00',
                title: '猫咪讨厌洗澡，怎么办？',
                content: '咨询如何训练猫咪接受洗澡与减少抗拒反应。'
            }
        ]);



        // 过滤历史记录
        const filteredHistory = computed(() => {
            return historyData.value.filter(item => {
                // 服務類型匹配（單選）
                const typeMatch = !selectedFilter.value || item.type === selectedFilter.value;

                // 日期範圍匹配（含起止日）
                let dateMatch = true;
                if (startDate.value && endDate.value) {
                    const itemDate = new Date(item.timestamp).getTime();
                    const start = new Date(startDate.value).getTime();
                    const end = new Date(endDate.value).getTime();

                    dateMatch = itemDate >= start && itemDate <= end;
                }

                return typeMatch && dateMatch;
            });
        });

        const currentPage = ref(1);
        const pageSize = 5;

        // 计算分页后的历史记录
        const pagedHistory = computed(() => {
            const start = (currentPage.value - 1) * pageSize;
            const end = start + pageSize;
            return filteredHistory.value.slice(start, end);
        });


        watch(onlineMessages, () => {
            scrollToBottom();
        });

        function fetchDataAndUpdateLocalStorage() {
            const savedUser = localStorage.getItem('currentUser');
            if (savedUser) {
                Object.assign(currentUser, JSON.parse(savedUser));
            }

        };

        onMounted(() => {
            fetchDataAndUpdateLocalStorage();
        });

        return {
            activeIndex,
            handleSelect,
            websites,
            allServiceTypes,
            selectedServiceType,
            navigateToService,
            newMessage,
            isWaitingResponse,
            messagesContainer,
            quickSuggestions,
            sendQuickQuestion,
            onlineMessages,
            formatTime,
            filteredTags,
            selectedTags,
            toggleTag,
            clearFilter,
            expertSearchQuery,
            showExpertDetails,
            truncateDescription,
            getRatingStars,
            filteredExperts,
            handleFilter,
            detailExpertVisible,
            detailExpert,
            onlineChat,
            goConsulting,
            sendOnlineMessage,
            openEndBox,
            AIMessages,
            sendAIMessage,
            ServiceFilters,
            selectFilter,
            selectedFilter,
            clearHistoryFilters,
            startDate,
            endDate,
            shortcuts,
            disabledDate,
            loading,
            filteredHistory,
            pagedHistory,
            currentUser
        };
    }
});

// 使用 ElementPlus
app.use(ElementPlus);

app.mount('#app');