let headder = new Vue({
    el: "#header",
    data() {
        return {
            //当前登陆用户名
            name: null,
            //问题标题
            title: '',
            title_error: false,
            //问题详情
            content: '',
            content_error: false
        }
    },
    methods: {
        getUser: function () {
            let _t = this;
            axios.get('/name').then(function (value) {
                //查询成功
                if (value.data.status === 200) {
                    _t.name = value.data.data;
                }
            })
        },
        //问题提交
        questionSubmit: function () {
            console.log("questionSubmit");
            let _t = this;

            axios.post('/questions', {
                title: _t.title,
                content: _t.content
            }).then(function (value) {
                let code = value.data.status;
                //成功
                if (code === 200) {
                    window.location.href = ("/");
                } else if (code === 1) {
                    alert("提交异常");
                }else if (code === 403) {
                    window.location.href="/login";
                }
            })

        },
        toLogin: function () {
            window.location.href = ("/login");
        },
        //问题输入校验
        checkQuestion: function () {
            let title = this.title;
            let content = this.content;
            if (title === '') {
                this.title_error = true;
                return false;
            }
        }
    },
    created: function () {
    }
});