


let app = new Vue({
    el: "#app",
    data() {
        return {
            //选中标签 1推荐 2关注  3热榜
            header: 1,
            //推荐问题列表数据
            questions: [],
            offset: 0,
            limit: 10,
            orderBy: 'created_date',

            //关注列表
            follows:{list:[],page:{offset:1,limit:10}},


            next: false,
            loading:false,
            bottomHight: 90,//滚动条到某个位置才触发时间
        }
    },
    watch:{
        'header':{
            handler: function (newValue, oldValue) {
                if (newValue === 2) {
                    //查询关注
                    let _t = this;
                    axios.get(base + '/feed/follow', {
                        params: {
                            offset: _t.follows.page.offset,
                            limit: _t.follows.page.limit,
                        }
                    }).then(function (value) {
                        let code = value.data.status;
                        if (code === 200){

                            for (let i = 0; i < value.data.data.length; i++){
                                value.data.data[i].data = JSON.parse(value.data.data[i].data);
                                _t.follows.list.push(value.data.data[i]);
                            }
                            console.log(value.data.data[0]);
                        }
                    });
                }else if (newValue === 1) {
                    
                }
            }
        }
    },
    methods: {
        //加载问题列表
        getQuestions: function (offset, limit, orderBy) {
            this.loading = true;
            let _t = this;
            axios.get(base + '/questions', {
                params: {
                    offset: offset,
                    limit: limit,
                    orderBy: orderBy,
                    desc: 1
                }
            }).then(function (value) {
                _t.loading = false;
                //查询成功
                if (value.data.status === 200) {
                    //将查询到的问题数据添加到列表
                    let questions = value.data.data;
                    for (let i = 0; i < questions.length; i++) {
                        _t.questions.push(questions[i]);
                    }
                    _t.next = questions.length >= limit;
                }
            })
        },
        //加载更多
        getMore: function () {
            this.offset = this.offset + this.limit;
            this.getQuestions(this.offset, this.limit,this.orderBy);
        },
        //访问问题详情
        toQuestionInfo:function (question) {
            window.open(base + '/questions/'+ question.id);
        },
        handleScroll: function () {
            if (this.header === 1){
                if (getScrollBottomHeight() <= app.bottomHight &&
                    app.next &&
                    app.loading === false) {
                    app.getMore();
                }
            } else if (this.header === 2){
                
            }

        }
    },
    created: function () {
        // 初始化列表加载10条数据
        this.getQuestions(this.offset, this.limit, this.orderBy);
    }
});

//添加滚动事件
window.onload = function () {
    window.addEventListener('scroll', app.handleScroll)
};

//滚动条到底部的距离
function getScrollBottomHeight() {
    return getPageHeight() - getScrollTop() - getWindowHeight();

}
//页面高度
function getPageHeight() {
    return document.querySelector("html").scrollHeight
}
//滚动条顶 高度
function getScrollTop() {
    let scrollTop = 0, bodyScrollTop = 0, documentScrollTop = 0;
    if (document.body) {
        bodyScrollTop = document.body.scrollTop;
    }
    if (document.documentElement) {
        documentScrollTop = document.documentElement.scrollTop;
    }
    scrollTop = (bodyScrollTop - documentScrollTop > 0) ? bodyScrollTop : documentScrollTop;
    return scrollTop;
}
function getWindowHeight() {
    let windowHeight = 0;
    if (document.compatMode == "CSS1Compat") {
        windowHeight = document.documentElement.clientHeight;
    } else {
        windowHeight = document.body.clientHeight;
    }
    return windowHeight;
}