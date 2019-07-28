
//验证码等待时间
let TIME_COUNT = 120;

let app = new Vue({
    el: "#app",
    data() {
        return {
            register: {
                username: '',
                username_error: '',
                email: '',
                email_error: '',
                password: '',
                password_error: '',
                repwd: '',
                repwd_error: '',
                code: '',
                code_error: '',
                code_message: '获取验证码',
                error: false,
                loading: false,
            },
            oldName: '',
            //发送验证码按钮
            show: true,  // 初始启用按钮
            count: '',   // 初始化次数
            timer: null,
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
        },
        'register.email': function () {
            this.register.email_error = '';
            this.register.error = false;
        },
        'register.code': function () {
            this.register.code_error = '';
            this.register.error = false;
        }
    },
    methods: {
        //访问登陆页面
        toLogin: function () {
            window.location.href = '/wenda/login';
        },
        //注册提交
        reg: function () {
            if (!this.register.error && this.check_register() && !this.register.loading) {
                this.register.error = true;
                let _t = this;
                //发送注册请求
                _t.register.loading = true;
                axios.post('/wenda/register',
                    {
                        name: _t.register.username,
                        password: _t.register.password,
                        email: _t.register.email,
                        code: _t.register.code
                    }
                ).then(function (value) {
                    _t.register.loading = false;
                    _t.register.error = false;
                    let code = value.data.status;
                    if (code === 200) {
                        window.location.href = '/wenda';
                    } else if (code === 3) {
                        _t.register.username_error = '用户名已存在';
                        _t.register.error = true;
                    } else if (code === 4) {
                        _t.register.email_error = '邮箱已被使用';
                        _t.register.error = true;
                    } else if (code === 5) {
                        _t.register.code_error = '验证码不正确';
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
            if (user.email === '') {
                user.email_error = "邮箱不能为空";
                user.error = true;
                return false;
            }
            let reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
            if (!reg.test(user.email)) {
                user.email_error = "请填写正确的邮箱";
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
            if (user.code === '') {
                user.code_error = "请填写验证码";
                user.error = true;
                return false;
            }

            return true;
        },

        //点击输入框
        clean: function () {

        },
        //查询用户名是否注册
        checkName: function () {
            if (this.register.username !== this.oldName) {
                //发送请求查询用户名是否被注册
            }
            this.oldName = this.register.username;
        },
        //获取验证码
        getCode: function () {
            if (this.register.error){
                return;
            }
            let user = this.register;
            let reg = /^([a-zA-Z]|[0-9])(\w|\-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
            if (!reg.test(user.email)) {
                user.email_error = "请填写正确的邮箱";
                user.error = true;
                return false;
            }
            let _t = this;
            let data = new FormData;
            data.append('email', this.register.email);

            axios.post('/wenda/code/email/', data)
                .then(function (value) {
                    let code = value.data.status;
                    if (code === 200) {
                        //获取成功
                        if (!_t.timer) {
                            _t.count = TIME_COUNT;
                            _t.show = false;
                            _t.timer = setInterval(() => {
                                if (_t.count > 0 && _t.count <= TIME_COUNT) {
                                    _t.count--;
                                } else {
                                    _t.show = true;
                                    clearInterval(_t.timer);  // 清除定时器
                                    _t.timer = null;
                                }
                            }, 1000)
                        }
                    } else if (code === 4) {
                        _t.register.email_error = '邮箱已被使用';
                        _t.register.error = true;
                    }
                });

        }

    },
    created: function () {

    }
});