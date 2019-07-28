
let username = document.getElementById("username").value;
let userId = document.getElementById("userId").value;
let headUrl = document.getElementById("headUrl").value;
let follower = document.getElementById("follower").value;
let app1 = new Vue({
    el: "#app",
    data() {
        return {
            //他的信息
            userInfo:{
                id:userId,
                name: username,
                headUrl: headUrl,
                //是否关注他
                follow: (follower === 'true'),
            },
            //它是否是当前登陆用户
            isMe: false,
            follow_tabs: ["他关注的人", "关注他的人", "他关注的问题"],
            //当前选择的关注标签
            follow_tab_flag: 0,
            //当前登陆用户
            loginUser: {id: 0},
            //关注他的人
            follower_users: {list: [], offset: 1, limit: 10, total: 0,loading:false},
            //他关注的人
            followee_users: {list: [], offset: 1, limit: 10, total: 0,loading:false},
            //他关注的问题
            follower_questions: {list: [], offset: 1, limit: 10, total: 0},
        }
    },
    watch: {
        'loginUser': function (newValue) {
            if (newValue.id === userId) {
                this.isMe = true;
                this.follow_tabs = ["我关注的人", "关注我的人", "我关注的问题"]
            }
        }
    },
    methods: {
        //获取当前登陆用户
        getUser: function () {
            let _t = this;
            axios.get(base + '/users/id').then(function (value) {
                //已登陆
                if (value.data.status === 200) {
                    _t.loginUser = value.data.data;
                    user = value.data.data;
                } else {
                    //未登录
                    _t.loginUser = {id: -1};
                }
            })
        },
        //关注用户
        followerUser: function (user) {
            let _t = this;
            axios.post(base + '/follow/user/' + user.id).then(function (value) {
                if (value.data.status === 200) {
                    user.follow = true;
                } else {

                }
            })
        },
        //取消关注用户
        unFollowerUser: function (user) {
            let _t = this;
            axios.post(base + '/unfollow/user/' + user.id).then(function (value) {
                if (value.data.status === 200) {
                    user.follow = false;
                } else {

                }
            })
        },
        //获取他关注的用户列表
        getFollowersByUser() {
            let _t = this;
            _t.followee_users.loading = true;
            axios.get(base + '/followees/user/' + userId, {
                params: {
                    offset: _t.follower_users.offset,
                    limit: _t.follower_users.limit
                }
            }).then(function (value) {
                _t.followee_users.loading = false;
                if (value.data.status === 200) {
                    _t.followee_users.list = value.data.data;

                } else {

                }
            })
        },
        //获取关注他的用户列表
        getFolloweesByUser() {
            let _t = this;
            _t.follower_users.loading = true;
            axios.get(base + '/followers/user/' + userId, {
                params: {
                    offset: _t.followee_users.offset,
                    limit: _t.followee_users.limit
                }
            }).then(function (value) {
                _t.follower_users.loading = false;
                if (value.data.status === 200) {
                    _t.follower_users.list = value.data.data;
                } else {

                }
            })
        }
    },
    created: function () {
        this.getUser();
        this.getFollowersByUser();
        this.getFolloweesByUser();
    }

});