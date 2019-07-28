
let app = new Vue({
    el: "#app",
    data() {
        return {
            //登陆信息
            login: {
                username: '',
                error: false,
                username_error: '',
                password: '',
                password_error: '',
                remember_me: true,
                loading:false,
            },
            //登陆成功跳转的url
            next:'/wenda',
        }
    },
    watch: {
        'login.username':function (newVal, oldVal) {
            this.login.username_error = '';
            this.login.error = false;
        },
        'login.password':function (newVal, oldVal) {
            this.login.password_error = '';
            this.login.error = false;
        }
    },
    methods: {
        //访问注册页面
        toRegister: function () {
            window.location.href = '/wenda/register';
        },
        //登陆提交
        log: function () {
            if (this.check_register() && !this.login.error && !this.login.loading) {
                let _t = this;
                //发送登陆请求
                this.login.loading = true;
                axios.post('/wenda/login',
                    "username=" + _t.login.username
                    + "&password=" + _t.login.password
                    + "&remember_me=" + _t.login.remember_me
                ).then(function (value) {
                    let code = value.data.status;
                    _t.login.loading = false;
                    if (code === 200) {
                        window.location.href = _t.next;
                    } else if (code === 1) {
                        _t.login.username_error = '用户名不存在';
                        _t.login.error = true;
                    } else if (code === 2) {
                        _t.login.password_error = '密码错误';
                        _t.login.error = true;
                    }
                });
            }
        },
        //校验注册信息
        check_register: function () {
            let user = this.login;
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
            return true;
        },

        //点击输入框
        clean: function () {

        },
        // 获取url参数
        getUrlKey(name) {
            return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.href) || [, ""])[1].replace(/\+/g, '%20')) || null
        }

    },
    created: function () {
        if (this.getUrlKey("next") !=null){
            this.next = this.getUrlKey("next")
        }
    },

    
});