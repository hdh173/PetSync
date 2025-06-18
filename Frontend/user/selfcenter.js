const { createApp, ref, reactive, onMounted } = Vue;

const app = createApp({
    setup() {
        window.auth.isLoggedIn();
        const activeIndex = ref('5');
        const linkList = ['index.html', 'selfcenter.html', 'index.html', 'event.html', 'consult.html', 'selfcenter.html'];
        const websites = ref(['主页', '个人中心', '主页', '事件簿', '咨询服务', '个人中心']);
        const defaultAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');
        const cities_sample = [
            '北京', '上海', '广州', '深圳', '杭州', '成都', '重庆', '武汉',
            '西安', '苏州', '天津', '南京', '长沙', '郑州', '东莞', '青岛',
            '沈阳', '宁波', '昆明', '哈尔滨', '长春', '厦门', '济南', '大连'
        ];
        const cities = cities_sample.map(city => ({
            value: city,
            label: city
        }));
        const location = ref('');

        const handleSelect = (key, keyPath) => {
            console.log(key, keyPath);
            window.location.href = linkList[key];
        };

        const userFileInput = ref(null);
        const newPetFileInput = ref(null);
        const petFileInputs = ref([]);

        function triggerFileInput(target) {
            if (target === 'user') {
                userFileInput.value?.click();
            } else if (target === 'new') {
                newPetFileInput.value?.click();
            } else if (typeof target === 'number') {
                petFileInputs.value[target]?.click();
            }
        }

        const currentUser = reactive({
            id: 0,
            name: '',
            avatar: '',
            followedCount: 0,
            followingCount: 0,
            postCount: 0,
            signature: "",
            gender: "",
            location: "",
            avatarFile: null
        });

        function handleFileUpload(event, target) {
            const file = event.target.files[0];
            if (!file) return;

            if (!file.type.startsWith('image/')) {
                alert('请选择图片文件');
                return;
            }
            if (file.size > 10 * 1024 * 1024) {
                alert('图片大小不能超过10MB');
                return;
            }

            const reader = new FileReader();
            reader.onload = () => {
                let targetObj = null;
                if (target === 'user') targetObj = currentUser;
                else if (target === 'new') targetObj = newPet;
                else if (typeof target === 'number') targetObj = petProfiles[target];

                if (targetObj) {
                    targetObj.avatar = reader.result;  // base64 資料
                    targetObj.avatarFile = file;
                }
            };
            reader.readAsDataURL(file);

            event.target.value = '';
        }

        const petProfiles = reactive([]);

        const createPetProfile = (data = {}) => ({
            name: data.name || "",
            type: data.type || "",
            gender: data.gender || "男",
            birthDay: data.birthDay || "",
            avatar: data.avatar || defaultAvatar.value,
            avatarFile: data.avatarFile || null,
            petId: data.petId || ""
        });

        const rawList = [
            { name: "豬咪", type: "貓", gender: "女", birthDay: "2023-01-01", petId: "A001", avatarFile: null, avatar: defaultAvatar.value },
            { name: "豆豆", type: "狗", gender: "男", birthDay: "2022-07-15", petId: "A002", avatarFile: null, avatar: defaultAvatar.value },
        ];

        rawList.forEach(data => {
            petProfiles.push(createPetProfile(data));
        });

        const shortcuts = [
            {
                text: 'Today',
                value: new Date(),
            },
            {
                text: 'Yesterday',
                value: () => {
                    const date = new Date()
                    date.setTime(date.getTime() - 3600 * 1000 * 24)
                    return date
                },
            },
            {
                text: 'A week ago',
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

        const petOptions = [
            {
                value: '狗狗',
                label: '狗狗',
            },
            {
                value: '猫咪',
                label: '猫咪',
            },
            {
                value: '鸟类',
                label: '鸟类',
            },
            {
                value: '其他',
                label: '其他',
            }
        ];

        const dialogVisible = ref(false);

        const newPet = reactive({ name: "", type: "", gender: "", birthDay: "", petId: "", avatar: defaultAvatar.value, avatarFile: null });

        const confirmAddPet = () => {
            petProfiles.push(createPetProfile(newPet));
            console.log(petProfiles);
            resetNewPet();
            dialogVisible.value = false;
        };

        function resetNewPet() {
            if (newPet.avatar && newPet.avatar.startsWith("blob:")) {
                URL.revokeObjectURL(newPet.avatar)
            }

            newPet.name = "";
            newPet.type = "";
            newPet.gender = "";
            newPet.birthDay = "";
            newPet.petId = "";
            newPet.avatar = defaultAvatar.value;
            newPet.avatarFile = null;
        }

        function handleLogout() {
            window.auth.logout();
        }

        const saveChanges = () => {
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            ElementPlus.ElMessage({
                message: '保存成功！',
                type: 'success',
            })
        };

        function fetchDataAndUpdateLocalStorage() {
            const savedUser = localStorage.getItem('currentUser');
            if (savedUser) {
                Object.assign(currentUser, JSON.parse(savedUser));
            }
        }

        onMounted(() => {
            fetchDataAndUpdateLocalStorage();
        })


        return {
            activeIndex,
            handleSelect,
            defaultAvatar,
            cities,
            location,
            websites,
            triggerFileInput,
            handleFileUpload,
            userFileInput,
            newPetFileInput,
            petFileInputs,
            petProfiles,
            shortcuts,
            disabledDate,
            petOptions,
            dialogVisible,
            newPet,
            confirmAddPet,
            handleLogout,
            currentUser,
            saveChanges
        };
    }
});

// 使用 ElementPlus
app.use(ElementPlus);

app.mount('#app');