const { createApp, ref, computed, onMounted, reactive } = Vue;

const app = createApp({
    setup() {
        window.auth.isLoggedIn();
        const activeIndex = ref('2');
        const linkList = ['index.html', 'selfcenter.html', 'index.html', 'event.html', 'consult.html', 'selfcenter.html'];
        const websites = ref(['主页', '个人中心', '主页', '事件簿', '咨询服务', '个人中心']);
        const defaultAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');
        const postContent = ref('');
        const selectedTags = ref([]);
        const visibleTags = ref([
            '萌寵日常', '養寵技巧', '寵物健康', '寵物飲食', '寵物美容'
        ]);

        const moreTags = ref([
            '貓咪行為', '狗狗訓練', '寵物醫療', '異寵飼養', '寵物心理',
            '疫苗與驅蟲', '寵物保險', '寵物領養', '走失協尋', '寵物配對',
            '寵物用品', '寵物攝影', '寵物旅遊', '寵物趣事', '寵物老年照護'
        ]);

        const recommendedTags = ref([
            '萌寵日常', '寵物健康', '貓咪行為', '狗狗訓練',
            '寵物醫療', '寵物美容', '走失協尋', '寵物領養'
        ]);
        const showTagDropdown = ref(false);
        const selectedUser = ref(null);
        function selectUser(userId) {
            selectedUser.value = (selectedUser.value === userId) ? null : userId;
        }

        // 当前用户数据
        const currentUser = reactive({});
        const postsData = ref([
            {
                id: 1,
                author: {
                    id: 101,
                    name: 'IvanPun',
                    avatar: 'images/user_icon.jpg',
                    isFollowing: true
                },
                textContent: '今天带猫咪去洗澡，它超乖的～ 🐱🛁',
                tags: ['宠物', '日常', '猫奴'],
                time: new Date(new Date().getTime() - 1000 * 60 * 60), // 1 小时前
                likes: 12,
                commentCount: 2,
                coins: 1,
                isLiked: false,
                isCoined: false,
                showComments: false,
                comments: [
                    {
                        id: 201,
                        text: '看起来好干净！',
                        time: new Date(new Date().getTime() - 1000 * 60 * 40),
                        user: {
                            name: 'Ben',
                            avatar: 'https://i.pravatar.cc/150?img=2'
                        }
                    },
                    {
                        id: 202,
                        text: '你是在哪里洗的呀？',
                        time: new Date(new Date().getTime() - 1000 * 60 * 20),
                        user: {
                            name: 'Carol',
                            avatar: 'https://i.pravatar.cc/150?img=3'
                        }
                    }
                ]
            },
            {
                id: 2,
                author: {
                    id: 102,
                    name: 'David',
                    avatar: 'https://i.pravatar.cc/150?img=4',
                    isFollowing: false
                },
                textContent: '刚买了新狗粮试试看，它居然全吃光了！😲🐶',
                image: '/images/dogFood.jpg',
                tags: ['狗狗', '生活分享'],
                time: new Date(new Date().getTime() - 1000 * 60 * 90), // 1.5 小时前
                likes: 20,
                commentCount: 0,
                coins: 3,
                isLiked: true,
                isCoined: true,
                showComments: false,
                comments: []
            },
            {
                id: 3,
                author: {
                    id: 103,
                    name: 'Emma',
                    avatar: 'https://i.pravatar.cc/150?img=5',
                    isFollowing: true
                },
                textContent: '天气真好，来公园晒太阳☀️ #放松',
                tags: ['日常', '阳光', '散步'],
                time: new Date(new Date().getTime() - 1000 * 60 * 10), // 10 分钟前
                likes: 5,
                commentCount: 1,
                coins: 0,
                isLiked: false,
                isCoined: false,
                showComments: false,
                comments: [
                    {
                        id: 203,
                        text: '真羡慕！我还在办公室 😭',
                        time: new Date(new Date().getTime() - 1000 * 60 * 5),
                        user: {
                            name: 'Frank',
                            avatar: 'https://i.pravatar.cc/150?img=6'
                        }
                    }
                ]
            }
        ]);

        const filterUsers = computed(() => {
            const seen = new Set();
            return postsData.value
                .map(post => post.author)
                .filter(author => {
                    if (seen.has(author.id)) return false;
                    seen.add(author.id);
                    return true;
                });
        });

        const filteredPosts = computed(() => {
            if (!selectedUser.value) {
                return postsData.value;
            }
            return postsData.value.filter(post => post.author.id === selectedUser.value);
        });


        // 格式化时间
        const formatTime = (date) => {
            return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        };

        const handleSelect = (key, keyPath) => {
            console.log(key, keyPath);
            window.location.href = linkList[key];
        };

        const toggleTagDropdown = () => {
        };

        const toggleLike = async (post) => {
            // await getPostDetail(post.id, currentUser.value.id)
            const wasLiked = post.isLiked
            post.isLiked = !wasLiked
            post.likes += post.isLiked ? 1 : -1


            // try {
            //     if (wasLiked) {
            //         await unlikePost(post.id, currentUser.value.id)
            //     } else {
            //         await likePost(post.id, currentUser.value.id)
            //     }
            // } catch (err) {
            //     post.isLiked = wasLiked
            //     post.likes = wasLiked ? post.likes : post.likes - 1
            //     console.error('操作点赞失败:', err)
            // }
        }

        const handleCommentClick = (post) => {
            if (post.comments.length > 10) {
                viewAllComments(post.id)
            } else {
                toggleComments(post)
            }
        };

        const viewAllComments = (postId) => {
            // router.push(`/post/${postId}/comments`)
        };

        const toggleComments = async (post) => {
            post.showComments = !post.showComments

            if (post.showComments && post.comments.length === 0) {
                try {
                    const response = await getPostDetail(post.id, currentUser.value.id)
                    post.comments = response.data.comments.map(comment => ({
                        id: comment.commentId,
                        user: {
                            id: comment.userId,
                            name: comment.userName,
                            avatar: comment.userAvatar
                        },
                        text: comment.content,
                        time: new Date(comment.createdAt)
                    }))
                } catch (err) {
                    console.error('加载评论出错:', err)
                }
            }
        };

        const addComment = async (post) => {
            if (!post.newComment.trim()) return

            try {
                // const response = await commentPost({
                //     userId: currentUser.value.id,
                //     postId: post.id,
                //     content: post.newComment
                // })

                // 模擬產生一個假 ID
                const fakeCommentId = Date.now();

                // post.comments.unshift({
                //     id: response.data.commentId,
                //     user: { ...currentUser.value },
                //     text: post.newComment,
                //     time: new Date()
                // })

                // 新增模擬留言
                post.comments.unshift({
                    id: fakeCommentId,
                    user: { ...currentUser.value },
                    text: post.newComment,
                    time: new Date()
                });

                post.commentCount += 1;
                post.newComment = ''
            } catch (err) {
                console.error('发表评论失败:', err)
            }
        }

        const toggleCoin = async (post) => {
            if (post.isCoined) {
                ElementPlus.ElMessage.primary('已投币');
                return
            }
            //await getPostDetail(post.id, currentUser.value.id)

            try {
                //await coinPost(post.id, currentUser.value.id)
                post.isCoined = true
                post.coins += 1
            } catch (err) {
                console.error('操作投币失败:', err)
            }
        };

        const submitPost = () => {

        };

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
            websites,
            activeIndex,
            handleSelect,
            defaultAvatar,
            postContent,
            selectedTags,
            visibleTags,
            showTagDropdown,
            moreTags,
            recommendedTags,
            toggleTagDropdown,
            filterUsers,
            selectedUser,
            selectUser,
            filteredPosts,
            formatTime,
            currentUser,
            toggleLike,
            handleCommentClick,
            toggleCoin,
            addComment,
            postsData,
            submitPost
        };
    }
});

// 使用 ElementPlus
app.use(ElementPlus);

app.mount('#app');