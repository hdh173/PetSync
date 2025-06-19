const { createApp, ref, computed, onMounted, reactive } = Vue;

const app = createApp({
    setup() {
        window.auth.isLoggedIn();
        const activeIndex = ref('2');
        const linkList = ['index.html', 'selfcenter.html', 'index.html', 'event.html', 'consult.html', 'selfcenter.html'];
        const websites = ref(['主页', '个人中心', '主页', '事件簿', '咨询服务', '个人中心']);
        const defaultAvatar = ref('https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png');
        const loading = ref(false);
        const error = ref(false);
        const isScrollLeft = ref(true);
        const isScrollRight = ref(false);
        const visibleTags = ref([
            '萌宠日常', '养宠技巧', '宠物健康', '宠物饮食', '宠物美容'
        ]);

        const moreTags = ref([
            '猫咪行为', '狗狗训练', '宠物医疗', '异宠饲养', '宠物心理',
            '疫苗与驱虫', '宠物保险', '宠物领养', '走失协寻', '宠物配对',
            '宠物用品', '宠物摄影', '宠物旅游', '宠物趣事', '宠物老年照护'
        ]);

        const recommendedTags = ref([
            '萌宠日常', '养宠技巧', '宠物健康', '宠物饮食', '宠物美容',
            '猫咪行为', '狗狗训练', '宠物医疗', '异宠饲养', '宠物心理',
            '疫苗与驱虫', '宠物保险', '宠物领养', '走失协寻', '宠物配对',
            '宠物用品', '宠物摄影', '宠物旅游', '宠物趣事', '宠物老年照护'
        ]);
        const selectedTags = ref([]);
        const toggleTag = (tag) => {
            const index = selectedTags.value.indexOf(tag);
            if (index === -1) {
                selectedTags.value.push(tag);
            } else {
                selectedTags.value.splice(index, 1); // 再次點擊則取消
            }
        };

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
                tags: ['萌宠日常', '猫咪行为', '宠物美容', '宠物心理', '养宠技巧'],
                time: new Date(new Date().getTime() - 1000 * 60 * 60),
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
                            avatar: 'images/2.jpg'
                        }
                    },
                    {
                        id: 202,
                        text: '你是在哪里洗的呀？',
                        time: new Date(new Date().getTime() - 1000 * 60 * 20),
                        user: {
                            name: 'Carol',
                            avatar: 'images/3.jpg'
                        }
                    }
                ]
            },
            {
                id: 2,
                author: {
                    id: 102,
                    name: 'David',
                    avatar: 'images/4.jpg',
                    isFollowing: false
                },
                textContent: '刚买了新狗粮试试看，它居然全吃光了！😲🐶',
                image: '/images/dogFood.jpg',
                tags: ['狗狗训练', '宠物饮食', '宠物健康', '疫苗与驱虫', '宠物用品'],
                time: new Date(new Date().getTime() - 1000 * 60 * 90),
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
                    avatar: 'images/5.jpg',
                    isFollowing: true
                },
                textContent: '天气真好，来公园晒太阳☀️ #放松',
                tags: ['宠物趣事', '宠物旅游', '萌宠日常'],
                time: new Date(new Date().getTime() - 1000 * 60 * 10),
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
                            avatar: 'images/6.jpg'
                        }
                    }
                ]
            },
            {
                id: 4,
                author: {
                    id: 104,
                    name: 'Luna',
                    avatar: 'images/7.jpg',
                    isFollowing: false
                },
                textContent: '我家龙猫终于学会自己用厕所了！太感动了～',
                tags: ['异宠饲养', '宠物趣事', '养宠技巧', '宠物心理'],
                time: new Date(),
                likes: 8,
                commentCount: 1,
                coins: 1,
                isLiked: false,
                isCoined: false,
                showComments: false,
                comments: [
                    {
                        id: 204,
                        text: '也太聪明了吧！',
                        time: new Date(),
                        user: {
                            name: 'Amy',
                            avatar: 'images/8.jpg'
                        }
                    }
                ]
            },
            {
                id: 5,
                author: {
                    id: 105,
                    name: 'Tommy',
                    avatar: 'images/9.jpg',
                    isFollowing: true
                },
                textContent: '今天带狗狗去打疫苗，医生说它身体超棒！',
                tags: ['狗狗训练', '宠物医疗', '疫苗与驱虫', '宠物健康', '宠物保险'],
                time: new Date(),
                likes: 14,
                commentCount: 0,
                coins: 2,
                isLiked: true,
                isCoined: false,
                showComments: false,
                comments: []
            },
            {
                id: 6,
                author: {
                    id: 106,
                    name: 'Sophie',
                    avatar: 'images/10.jpg',
                    isFollowing: false
                },
                textContent: '参加了一次宠物摄影课程，拍出超多萌照📸📷',
                tags: ['宠物摄影', '萌宠日常', '宠物用品'],
                time: new Date(),
                likes: 18,
                commentCount: 2,
                coins: 1,
                isLiked: false,
                isCoined: true,
                showComments: false,
                comments: [
                    {
                        id: 205,
                        text: '照片太可爱了！求分享！',
                        time: new Date(),
                        user: {
                            name: 'Mia',
                            avatar: 'images/11.jpg'
                        }
                    },
                    {
                        id: 206,
                        text: '想看更多！',
                        time: new Date(),
                        user: {
                            name: 'Leo',
                            avatar: 'images/12.jpg'
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
            return postsData.value
                .filter(post => {
                    const matchUser = !selectedUser.value || post.author.id === selectedUser.value;
                    const matchTags = selectedTags.value.length === 0 ||
                        post.tags.some(tag => selectedTags.value.includes(tag));
                    return matchUser && matchTags;
                })
                .sort((a, b) => b.time - a.time);  // 按时间降序排列，最新的在前面
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
            showTagDropdown.value = !showTagDropdown.value
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

        const draftPost = reactive(
            {
                id: Date.now(),
                author: {
                    id: 0,
                    name: '',
                    avatar: '',
                    isFollowing: true
                },
                textContent: '',
                image: '',
                tags: [],
                time: new Date(),
                likes: 0,
                commentCount: 0,
                coins: 0,
                isLiked: false,
                isCoined: false,
                showComments: false,
                comments: []
            }
        );

        // 图片上传相关
        const previewImage = ref(null);
        const imageFile = ref(null);

        const handleImageUpload = (e) => {
            const file = e.target.files[0]
            if (file) {
                if (file.size > 10 * 1024 * 1024) {
                    ElementPlus.ElMessage.error('图片大小不能超过10MB');
                    return
                }
                imageFile.value = file
                previewImage.value = URL.createObjectURL(file)
            }
        };

        const removeImage = () => {
            previewImage.value = null
            imageFile.value = null
        };

        const addTag = (tag) => {
            if (draftPost.tags.length >= 5) {
                ElementPlus.ElMessage({
                    message: '最多只能添加5个标签',
                    type: 'warning',
                })
                return
            }
            if (!draftPost.tags.includes(tag)) {
                draftPost.tags.push(tag)
            }
        }

        const removeTag = (index) => {
            draftPost.tags.splice(index, 1)
        }

        const submitPost = () => {
            draftPost.author = {
                id: currentUser.id,
                name: currentUser.name,
                avatar: currentUser.avatar,
                isFollowing: true
            };
            draftPost.image = previewImage.value;
            postsData.value.unshift({ ...draftPost });
            draftPost.id = Date.now();
            draftPost.textContent = '';
            draftPost.tags = [];
            draftPost.time = new Date();
        };

        const removePost = (postID) => {
            ElementPlus.ElMessageBox.confirm(
                '是否要删除动态？',
                '警告',
                {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            )
                .then(() => {
                    postsData.value = postsData.value.filter(post => post.id !== postID);
                })
                .catch(() => {

                })
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
            submitPost,
            removePost,
            draftPost,
            addTag,
            removeTag,
            loading,
            error,
            isScrollLeft,
            isScrollRight,
            handleImageUpload,
            previewImage,
            removeImage,
            selectedTags,
            toggleTag
        };
    }
});

// 使用 ElementPlus
app.use(ElementPlus);

app.mount('#app');