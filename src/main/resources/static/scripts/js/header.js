
//公共js

//时间过滤器
Vue.filter('time', function (data, fomrmat) {
    return moment(data).format(fomrmat || 'HH:mm');
});
//防止xss
Vue.prototype.xss = function(msg){
    return filterXSS(msg);
    //return msg;
};
//数字过滤器
Vue.filter('likes',function (num,digits) {
    const si = [
        { value: 1, symbol: "" },
        { value: 1E3, symbol: "K" },
        { value: 1E6, symbol: "M" },
        { value: 1E9, symbol: "G" },
        { value: 1E12, symbol: "T" },
        { value: 1E15, symbol: "P" },
        { value: 1E18, symbol: "E" }
    ];
    const rx = /\.0+$|(\.[0-9]*[1-9])0+$/;
    let i;
    for (i = si.length - 1; i > 0; i--) {
        if (num >= si[i].value) {
            break;
        }
    }
    return (num / si[i].value).toFixed(digits).replace(rx, "$1") + si[i].symbol;
});



let base = document.getElementById("baseUrl").value;
let to_location = document.getElementById("location").value;
let user = {id:0,name:''};

function checkLogin() {
    if (user.id <=0){
        window.location.href='/wenda/login';
    }
}
let header = new Vue({
    el: "#header",
    data() {
        return {
            //当前位置
            location: to_location,
            //当前登陆用户名
            user: {id:0,name:''},
            //问题标题
            title: '',
            title_error: false,
            //问题详情
            content: '',
            content_error: false
        }
    },
    watch:{

    },
    methods: {
        getUser: function () {
            let _t = this;
            axios.get(base + '/users/id').then(function (value) {
                //已登陆
                if (value.data.status === 200) {
                    _t.user = value.data.data;
                    user = value.data.data;
                    console.log("header:userid=" + user.id);
                }else {
                    //未登录
                    _t.user = {id: -1};
                }

            })
        },
        //问题提交
        questionSubmit: function () {
            console.log("questionSubmit");
            let _t = this;

            axios.post(base + '/questions', {
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
                    window.location.href=base + "/login";
                }
            })

        },
        toLogin: function () {
            let url = document.location;
            window.location.href = (base + "/login?next="+url);
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
        this.getUser();
    }
});