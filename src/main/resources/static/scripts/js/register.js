
let app = new Vue({
    el: "#app",
    data() {
        return {
            register: {
                username: '',
                username_error: '',
                password: '',
                password_error: '',
                repwd: '',
                repwd_error: '',
                error: false
            },
        }
    },
    watch: {
        'register.username': function () {
            this.register.username_error = '';
            this.register.error = false;
        },
        'register.password': function () {
            this.register.password_error = '';
            this.register.error = false;
        },
        'register.repwd': function () {
            this.register.repwd_error = '';
            this.register.error = false;
        }
    },
    methods: {
        //访问登陆页面
        toLogin: function () {
            window.location.href = '/login';
        },
        //注册提交
        reg: function () {
            if (!this.register.error && this.check_register()) {
                this.register.error = true;
                let _t = this;
                //发送注册请求
                axios.post('/register',
                    {
                        name: _t.register.username,
                        password: _t.register.password
                    }
                ).then(function (value) {
                    _t.register.error = false;
                    let code = value.data.status;
                    if (code === 200) {
                        window.location.href = '/';
                    } else if (code === 3) {
                        _t.register.username_error = '用户名已存在';
                        _t.register.error = true;
                    } else {
                        alert("注册失败，请重试");
                    }
                }).catch(function (error) {

                })
            }
        },
        //校验注册信息
        check_register: function () {
            let user = this.register;
            if (user.username === '') {
                user.username_error = "用户名不能为空";
                user.error = true;
                return false;
            }
            if (user.password === '') {
                user.password_error = "密码不能为空";
                user.error = true;
                return false;
            }
            if (user.repwd === '') {
                user.repwd_error = "确认密码不能为空";
                user.error = true;
                return false;
            } else if (user.password !== user.repwd) {
                user.repwd_error = "两次密码不一致";
                user.error = true;
                return false;
            }
            return true;
        },

        //点击输入框
        clean: function () {

        },


    },
    created: function () {

    }
});