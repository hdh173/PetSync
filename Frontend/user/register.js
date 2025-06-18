const { createApp, ref, onMounted, reactive } = Vue;

const app = createApp({
    setup() {
        const websites = ref(['主页', '主页', '事件簿', '咨询服务', '个人中心']);
        const isEmailValid = ref(false);
        const isCountingDown = ref(false);
        const countdown = ref(0);
        const emailError = ref('');
        const confirmPasswordError = ref('');
        const showLogin = ref(true);
        //const defaultAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');
        const loginWarningVisible = ref(false);

        const registerForm = ref({
            username: '',
            email: '',
            verificationCode: '',
            password: '',
            confirmPassword: ''
        });

        const loginForm = ref({
            usernameOrEmail: '',
            password: ''
        });

        const isRegisterDisabled = ref(true);

        const validateEmail = () => {
            const emailRegex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
            isEmailValid.value = emailRegex.test(registerForm.value.email);
            emailError.value = isEmailValid.value ? '' : '请输入有效的邮箱地址';
            checkRegisterFormValidity();
        };

        const isPasswordValid = ref(false);
        const passwordWarning = ref(false);

        function validPassword(password) {
            const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,}$/;
            isPasswordValid.value = regex.test(password);
            passwordWarning.value = !isPasswordValid.value;
            checkRegisterFormValidity();
        };


        const validateConfirmPassword = () => {
            confirmPasswordError.value = registerForm.value.password !== registerForm.value.confirmPassword
                ? '密码不一致'
                : '';
            checkRegisterFormValidity();
        };

        const checkRegisterFormValidity = () => {
            isRegisterDisabled.value = !(
                registerForm.value.username &&
                isEmailValid.value &&
                registerForm.value.verificationCode &&
                isPasswordValid.value &&
                registerForm.value.confirmPassword &&
                confirmPasswordError.value === ''
            );
        };

        const sendVerificationCodeHandler = async () => {
            if (!isEmailValid.value || isCountingDown.value) return;

            isCountingDown.value = true;
            countdown.value = 60;

            try {
                await sendVerificationCode(registerForm.value.email);
                const timer = setInterval(() => {
                    countdown.value--;
                    if (countdown.value <= 0) {
                        clearInterval(timer);
                        isCountingDown.value = false;
                    }
                }, 1000);
            } catch (err) {
                console.error('验证码发送失败', err);
            }
        };

        const currentUser = reactive({
            id: 101,
            name: 'IvanPun',
            avatar: 'images/user_icon.jpg',
            followedCount: 826,
            followingCount: 1142,
            postCount: 1,
            signature: "",
            gender: "男",
            location: "",
            avatarFile: null
        });

        const handleRegister = async () => {
            const { username, email, verificationCode, password } = registerForm.value;

            /*try {
                await register({ username, email, verificationCode, password });
                alert('注册成功，请登录');
                switchToLogin();
            } catch (err) {
                console.error('注册失败', err);
                alert('注册失败，请重试');
            }*/
            ElementPlus.ElMessage({
                message: '注册成功',
                type: 'success',
                plain: true,
            });
            localStorage.setItem('loginState', true);
            localStorage.setItem('currentUser', JSON.stringify(currentUser));
            window.location.href = 'index.html';
        };

        const handleLogin = async () => {
            const { usernameOrEmail, password } = loginForm.value;

            if (usernameOrEmail == 'admin' && password == 'admin') {
                localStorage.setItem('loginState', true);
                console.log(currentUser);
                localStorage.setItem('currentUser', JSON.stringify(currentUser));
                window.location.href = 'index.html';
            } else {
                loginWarningVisible.value = true;
            }

            // try {
            //     const res = await login({ usernameOrEmail, password });
            //     userStore.setUserInfo(res.data);
            //     alert('登录成功！');
            //     router.push('/');
            // } catch (err) {
            //     console.error('登录失败', err);
            //     // 不用再 alert，因为 request.js 已弹出 Toast 或提示
            //     // 可选：再弹一个额外提示
            //     // alert(err.msg || '登录失败，请检查用户名/密码');
            // }
        };

        const switchToRegister = () => {
            showLogin.value = false;
        };

        const switchToLogin = () => {
            showLogin.value = true;
        };

        function fetchDataAndUpdateLocalStorage() {
            const savedUser = localStorage.getItem('currentUser');
            if (savedUser) {
                Object.assign(currentUser, JSON.parse(savedUser));
            }

        }

        onMounted(() => {
            fetchDataAndUpdateLocalStorage()
        })


        return {
            websites,
            showLogin,
            handleLogin,
            loginForm,
            registerForm,
            switchToRegister,
            switchToLogin,
            validateEmail,
            isEmailValid,
            isCountingDown,
            countdown,
            sendVerificationCodeHandler,
            emailError,
            validateConfirmPassword,
            isRegisterDisabled,
            confirmPasswordError,
            handleRegister,
            loginWarningVisible,
            validPassword,
            passwordWarning
        };
    }
});

// 使用 ElementPlus
app.use(ElementPlus);

app.mount('#app');