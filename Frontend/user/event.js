const { createApp, ref, onMounted, reactive, computed } = Vue;

const app = createApp({
    setup() {
        window.auth.isLoggedIn();
        const activeIndex = ref('2');
        const linkList = ['index.html', 'index.html', 'event.html', 'consult.html', 'selfcenter.html'];
        const websites = ref(['主页', '主页', '事件簿', '咨询服务', '个人中心']);
        const newDialogVisible = ref(false);
        const petFilter = ref('全部');
        const inputWarning = ref(false);
        const detailDialogVisible = ref(false);
        const removeAlertVisible = ref(false);
        const repeatOptions = ref([
            {
                value: '每年',
                label: '每年',
            }, {
                value: '每月',
                label: '每月',
            }, {
                value: '每日',
                label: '每日',
            }, {
                value: '每小时',
                label: '每小时',
            }, {
                value: '一次性',
                label: '一次性',
            }
        ]);
        const sortOrder = ref('升序');
        const sortOptions = ref([
            {
                value: '升序',
                label: '升序',
            }, {
                value: '降序',
                label: '降序',
            }
        ]);

        const newEvent = reactive({
            name: "",
            pet: "",
            reminderMethod: "弹窗通知",
            repeat: "",
            remindTime: "",
            theme: "default",
        });

        const shortcuts = [
            {
                text: '今天',
                value: new Date(),
            },
            {
                text: '明天',
                value: () => {
                    const date = new Date()
                    date.setTime(date.getTime() + 3600 * 1000 * 24)
                    return date
                },
            },
            {
                text: '下周今天',
                value: () => {
                    const date = new Date()
                    date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
                    return date
                },
            },
        ];

        const colorOptions = ref([
            {
                value: 'default',
                label: '默认（白色）',
            },
            {
                value: 'sky-blue',
                label: '蓝色',
            }, {
                value: 'green-nature',
                label: '绿色',
            }, {
                value: 'healing-purple',
                label: '紫色',
            }, {
                value: 'soft-orange',
                label: '橙色',
            }
        ]);

        const eventList = ref([
            {
                name: '宠物疫苗提醒',
                pet: '小花',
                reminderMethod: '手机通知',
                repeat: '每年一次',
                remindTime: '2025-06-20 09:00',
                theme: 'sky-blue'
            },
            {
                name: '宠物洗澡',
                pet: '球球',
                reminderMethod: '短信提醒',
                repeat: '每月一次',
                remindTime: '2025-06-15 14:00',
                theme: 'green-nature'
            },
            {
                name: '定期驱虫',
                pet: '小花',
                reminderMethod: 'App弹窗',
                repeat: '每三个月一次',
                remindTime: '2025-07-01 10:30',
                theme: 'healing-purple'
            },
            {
                name: '宠物体检',
                pet: '球球',
                reminderMethod: '邮箱提醒',
                repeat: '每半年一次',
                remindTime: '2025-08-10 08:00',
                theme: 'soft-orange'
            },
            {
                name: '宠物绝育手术预约',
                pet: '小花',
                reminderMethod: '手机通知',
                repeat: '一次性',
                remindTime: '2025-09-05 13:00',
                theme: 'healing-purple'
            },
            {
                name: '宠物饮食调整',
                pet: '球球',
                reminderMethod: '短信提醒',
                repeat: '每周一次',
                remindTime: '2025-06-18 18:00',
                theme: 'soft-orange'
            },
            {
                name: '宠物训练课程',
                pet: '球球',
                reminderMethod: 'App弹窗',
                repeat: '每周三次',
                remindTime: '2025-06-14 17:00',
                theme: 'default'
            },
            {
                name: '宠物社交活动',
                pet: '小花',
                reminderMethod: '邮箱提醒',
                repeat: '每月一次',
                remindTime: '2025-07-20 15:30',
                theme: 'green-nature'
            }
        ]);

        const petOptions = computed(() => {
            const pets = [...new Set(eventList.value.map(event => event.pet))];
            return [
                { value: '全部', label: '全部' },
                ...pets.map(pet => ({ value: pet, label: pet }))
            ];
        });

        const filteredEventList = computed(() => {
            let filtered = eventList.value;

            // 依照寵物名稱過濾
            if (petFilter.value !== '全部') {
                filtered = filtered.filter(event => event.pet === petFilter.value);
            }

            // 依照時間排序（根據 remindTime）
            if (sortOrder.value === '升序') {
                filtered = filtered.slice().sort((a, b) => new Date(a.remindTime) - new Date(b.remindTime));
            } else if (sortOrder.value === '降序') {
                filtered = filtered.slice().sort((a, b) => new Date(b.remindTime) - new Date(a.remindTime));
            }

            return filtered;
        });



        const handleSelect = (key, keyPath) => {
            console.log(key, keyPath);
            window.location.href = linkList[key];
        };

        const requestNotificationPermission = () => {
            if (Notification.permission === "default") {
                Notification.requestPermission();
            }
        };

        const scheduleNotificationAt = (targetDateTime) => {
            const now = new Date();
            const timeUntilTarget = targetDateTime - now;

            if (timeUntilTarget <= 0) {
                console.log("指定時間已過，不觸發提醒");
                return;
            }

            setTimeout(() => {
                if (Notification.permission === "granted") {
                    new Notification("提醒時間到了！", {
                        body: "您預定的任務時間已到，請查看！",
                        icon: "https://cdn-icons-png.flaticon.com/512/1828/1828817.png"
                    });
                }
            }, timeUntilTarget);
        };

        const resetNewEvent = () => {
            newEvent.name = "";
            newEvent.pet = "";
            newEvent.reminderMethod = "弹窗通知";
            newEvent.repeat = "";
            newEvent.remindTime = "";
            newEvent.theme = "default";
        };

        function formatDateTime(date) {
            const y = date.getFullYear();
            const m = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
            const d = String(date.getDate()).padStart(2, '0');
            const hh = String(date.getHours()).padStart(2, '0');
            const mm = String(date.getMinutes()).padStart(2, '0');
            return `${y}-${m}-${d} ${hh}:${mm}`;
        }

        const addEvent = () => {
            console.log(newEvent);
            if (newEvent.name.trim() === '') {
                inputWarning.value = true;
            } else {
                newEvent.remindTime = formatDateTime(newEvent.remindTime);
                eventList.value.push({ ...newEvent });
                newDialogVisible.value = false;
                resetNewEvent();
            }
        };

        const detailEvent = reactive({
            name: "",
            pet: "",
            reminderMethod: "",
            repeat: "",
            remindTime: "",
            theme: "",
        });

        const showEventDetail = (event) => {
            Object.assign(detailEvent, event);
            detailDialogVisible.value = true;
        };

        const removeEvent = (event) => {
            Object.assign(detailEvent, event);
            removeAlertVisible.value = true;
        };

        const confirmRemoveEvent = () => {
            const index = eventList.value.findIndex(event => event.name === detailEvent.name);
            console.log(index);
            if (index !== -1) {
                eventList.value.splice(index, 1);
                removeAlertVisible.value = false;
            }
        };

        onMounted(() => {
            requestNotificationPermission();

            // 🕒 自定義目標時間：2025-06-13 10:30
            const targetTime = new Date("2025-06-13T00:05:00");
            scheduleNotificationAt(targetTime);
        });

        return {
            activeIndex,
            handleSelect,
            websites,
            newDialogVisible,
            repeatOptions,
            shortcuts,
            colorOptions,
            newEvent,
            petOptions,
            petFilter,
            filteredEventList,
            addEvent,
            inputWarning,
            showEventDetail,
            detailDialogVisible,
            detailEvent,
            removeEvent,
            confirmRemoveEvent,
            removeAlertVisible,
            sortOrder,
            sortOptions
        };
    }
});

app.use(ElementPlus);

app.mount('#app');