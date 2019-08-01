//公共js

//时间过滤器
Vue.filter('time', function (data, fomrmat) {
    return moment(data).format(fomrmat || 'HH:mm');
});
//防止xss
Vue.prototype.xss = function (msg) {
    return filterXSS(msg);
    //return msg;
};
//数字过滤器
Vue.filter('likes', function (num, digits) {
    const si = [
        {value: 1, symbol: ""},
        {value: 1E3, symbol: "K"},
        {value: 1E6, symbol: "M"},
        {value: 1E9, symbol: "G"},
        {value: 1E12, symbol: "T"},
        {value: 1E15, symbol: "P"},
        {value: 1E18, symbol: "E"}
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
let user = {id: 0, name: ''};

function checkLogin() {
    if (user.id <= 0) {
        window.location.href = '/wenda/login';
    }
}

let header = new Vue({
    el: "#header",
    data() {
        return {
            //当前位置
            location: to_location,
            //当前登陆用户名
            user: {id: 0, name: ''},
            //问题标题
            title: '',
            title_error: false,
            //问题详情
            content: '',
            content_error: false,
            //提问搜索框
            questionInput: '',
        }
    },
    watch: {
        'questionInput': function (newValue, oldValue) {
            if (newValue !== oldValue && newValue !== '') {
                console.log(newValue);
            }
        }
    },
    methods: {
        getUser: function () {
            let _t = this;
            axios.get(base + '/users/id').then(function (value) {
                //请求成功
                if (value.data.status === 200) {
                    //已登陆
                    _t.user = value.data.data;
                    user = value.data.data;
                    console.log("header:userid=" + user.id);
                } else {
                    //未登录
                    _t.user = {id: -1};
                }
            }).catch(function (thrown) {
                //请求失败

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
                } else if (code === 403) {
                    window.location.href = base + "/login";
                }
            })

        },
        toLogin: function () {
            let url = document.location;
            window.location.href = (base + "/login?next=" + url);
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

let myModal = new Vue({
    el: "#myModal",
    data() {
        return {
            //提问搜索框
            questionInput: '',
            inputTimeStamp: 0,
            questions:[],
        }
    },
    watch: {
        /*'questionInput': function (newValue, oldValue) {
            if (newValue !== oldValue && newValue !== '') {
                setTimeout(console.log(newValue), 1000);
            }
        }*/
    },
    methods: {
        /**
         * des:用户在输入状态下不发送请求，等停止输入后发送请求
         */
        search(event) {
            if (event.keyCode !== 13) {//除回车键外
                //标记当前事件函数的时间戳
                this.inputTimeStamp = event.timeStamp;
                if (this.questionInput !== '') {
                    setTimeout(() => {
                        //1s后比较二者是否还相同（因为只要还有事件触发，inputTimeStamp就会被改写，不再是当前事件函数的时间戳）
                        if (this.inputTimeStamp == event.timeStamp) {
                            console.log('发送请求'+this.inputTimeStamp);
                            this.request(this.questionInput);
                        }
                    }, 500);
                }
            } else {
                console.log('按下回车键');
            }

        },
        request(keyword) {
            let CancelToken = axios.CancelToken;
            let source = CancelToken.source();
            // 取消上一次请求
            this.cancelRequest();
            axios.get("/wenda/questions/search", {
                params:{
                    kw:keyword
                }
                }, {
                cancelToken: new axios.CancelToken(function executor(c) {
                    this.source = c;
                })
            }).then((res) => {
                // 在这里处理得到的数据
                let code = res.data.status;
                if (code == 200) {
                    this.questions = res.data.data;
                }
            }).catch((err) => {
                if (axios.isCancel(err)) {
                    console.log('Rquest canceled', err.message); //请求如果被取消，这里是返回取消的message
                } else {
                    //handle error
                    console.log(err);
                }
            })
        },
        cancelRequest(){
            if(typeof this.source ==='function'){
                this.source('终止请求')
            }
        },
    },
    created: function () {

    }
});