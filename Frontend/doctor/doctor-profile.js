const { createApp, ref, reactive } = Vue;

const app = createApp({
    setup() {
        const defaultAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');
        const userFileInput = ref(null);
        const activeIndex = ref('2'); // '2' for "个人中心"

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

        function triggerFileInput() {
            userFileInput.value?.click();
        }

        const userInfo = reactive({
            username: "王医生",
            gender: "男",
            hospital: "PetSync 合作医院",
            avatar: 'https://images.unsplash.com/photo-1583337130587-d8a6a2e8a7e2?w=100',
            avatarFile: null,
            tags: ['猫科', '犬科', '营养学']
        });

        const qualifications = ref([
            { name: '执业兽医师资格证', number: '2021123456789012', status: '已认证' },
            { name: '高级宠物营养师', number: '2022ABCDEFGHIJKL', status: '已认证' },
            { name: '小动物内科专科认证', number: 'N/A', status: '审核中' },
            { name: '猫科动物行为学认证', number: '2023CATBEHAVIORCERT', status: '已认证' },
        ]);

        const inputVisible = ref(false);
        const inputValue = ref('');
        const inputRef = ref(null);

        const handleCloseTag = (tag) => {
            userInfo.tags.splice(userInfo.tags.indexOf(tag), 1);
        };

        const showInput = () => {
            inputVisible.value = true;
            Vue.nextTick(() => {
                inputRef.value?.input?.focus();
            });
        };

        const handleInputConfirm = () => {
            if (inputValue.value) {
                userInfo.tags.push(inputValue.value);
            }
            inputVisible.value = false;
            inputValue.value = '';
        };

        function handleFileUpload(event) {
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
                userInfo.avatar = reader.result;
                userInfo.avatarFile = file;
            };
            reader.readAsDataURL(file);
            event.target.value = '';
        }
        
        const saveChanges = () => {
            ElMessage.success('信息已保存！');
            console.log("Saving data:", userInfo);
        };

        return {
            defaultAvatar,
            userInfo,
            qualifications,
            userFileInput,
            activeIndex,
            handleSelect,
            triggerFileInput,
            handleFileUpload,
            saveChanges,
            inputVisible,
            inputValue,
            inputRef,
            handleCloseTag,
            showInput,
            handleInputConfirm
        };
    }
});

app.use(ElementPlus);
app.mount('#app'); 