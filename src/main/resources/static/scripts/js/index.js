


let app = new Vue({
    el: "#app",
    data() {
        return {
            //问题列表数据
            questions: [],
            header: 1,
            offset: 0,
            limit: 10,
            orderBy: 'created_date',
            next: false,
            loading:false,
            bottomHight: 90,//滚动条到某个位置才触发时间
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
            if (getScrollBottomHeight() <= app.bottomHight &&
                app.next &&
                app.loading === false) {
                app.getMore();
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