let questionId = document.getElementById("questionId").value;

//文本编辑器
let E = window.wangEditor;
let editor = new E('#editor');

// 或者 var editor = new E( document.getElementById('editor') )

let app = new Vue({
    el: "#app",
    data() {
        return {
            user: {},
            //回答内容
            answers_content: '',
            //回答列表
            answers: [],
            bottomHight: 80,//滚动条到某个位置才触发时间
            //回答分页参数
            page_answer: {
                //是否有下一页
                next: false,
                //开始数
                offset: 0,
                //每页条数
                limit: 5,
                //数据总数
                total: 0,
                //数据页数
                pages: 0,
                //当前页
                num: 1,
                loading: false,
            },
            //回答排序条件
            orderBy: 'likes',
            //回答排序是否倒序
            desc : 1,
            //启用文本编辑器
            editor_flag: 0,
            //评论内容
            comment_content: '',
            question_close: true,
        }
    },
    watch: {
        //监听回答输入框的内容变化
        answers_content: {
            handler(newValue, oldValue) {
                console.log("answers_content");
            }
        },
        answers: {
            handler(newValue, oldValue) {
                for (let i = 0; i < newValue.length; i++) {
                    if (oldValue[i].comment_content !== newValue[i].comment_content) {
                        console.log(i + oldValue[i].comment_content);
                        console.log(i + oldValue[i].comment_content);
                    }

                }
            },
            deep: true
        }
    },
    methods: {
        //回答点赞
        likeAnswer:function(answer){
            answer.isLike = 1;
            axios.post(base + '/like/answer/' + answer.id)
                .then(function (value) {
                    let code = value.data.status;
                    if (code === 200) {
                        let likes = value.data.data;
                        answer.likes = likes;
                    } else if (code === 403) {

                    }
                })
        },
        //取消点赞或点踩
        delLikeAnswer:function(answer){
            answer.isLike = 0;
            axios.post(base + '/delLike/answer/' + answer.id)
                .then(function (value) {
                    let code = value.data.status;
                    if (code === 200) {
                        let likes = value.data.data;
                        answer.likes = likes;
                    } else if (code === 403) {

                    }
                })
        },
        //点踩
        disLikeAnswer:function(answer){
            answer.isLike = -1;
            axios.post(base + '/dislike/answer/' + answer.id)
                .then(function (value) {
                    let code = value.data.status;
                    if (code === 200) {
                        let likes = value.data.data;
                        answer.likes = likes;
                    } else if (code === 403) {

                    }
                })
        },

        //切换回答列表排序方式
        changeOrderBy:function(orderBy){
            if (this.orderBy !== orderBy) {
                this.orderBy = orderBy;
                this.answers = [];
                this.getAnswer();
            }
        },
        //获取回答列表并加载
        getAnswer: function () {
            this.page_answer.loading = true;
            let _t = this;
            axios.get(base + '/comments/answer/' + questionId,
                {
                    params: {
                        offset: (this.page_answer.num-1) * this.page_answer.limit,
                        limit: this.page_answer.limit,
                        orderBy :this.orderBy,
                        desc: this.desc
                    }
                })
                .then(function (value) {
                    _t.page_answer.loading = false;
                    let code = value.data.status;
                    if (code === 200) {
                        let answers = value.data.data.list;
                        _t.page_answer.total = value.data.data.total;
                        _t.page_answer.next = value.data.data.hasNextPage;
                        _t.page_answer.pages = value.data.data.pages;
                        for (let i = 0; i < answers.length; i++) {
                            if (answers[i].content.length > 400) {
                                //回答是否要折叠 0折叠， 1展开， -1无需折叠
                                answers[i].flag = 0;
                            } else {
                                answers[i].flag = -1;
                            }
                            if (answers[i].userId == user.id) {
                                //回答是否可以删除
                                answers[i].delete = 1;
                            } else {
                                answers[i].delete = 0;
                            }
                            //设置评论回复输入框内容
                            answers[i].comment_content = '';
                            //设置评论加载状态
                            answers[i].loading = true;
                            //设置评论回复按钮
                            answers[i].disabled = true;
                            //设置评论展开状态
                            answers[i].openComment = 0;
                            //初始化评论分页属性
                            answers[i].page = {
                                //是否最后一页
                                next: false,
                                //开始数
                                offset: 0,
                                //每页条数
                                limit: 10,
                                //数据总数
                                total: 0,
                                //数据页数
                                pages: 0,
                                //当前页
                                num: 1,
                            };
                            _t.answers.push(answers[i]);
                        }
                    }
                })
        },
        //改变回答输入框内容
        changeCommentInput: function (answer) {
            console.log(answer.comment_content);
        },
        //提交回答
        submit: function () {
            let _t = this;
            let data = new FormData;
            data.append('content', this.answers_content);
            axios.post(base + '/comments/answer/' + questionId, data)
                .then(function (value) {
                    _t.editor_flag = 0;
                    let code = value.data.status;
                    if (code === 200) {
                        _t.answers = [];
                        _t.page_answer.num = 1;
                        _t.getAnswer();
                        _t.comment = '';

                    } else if (code === 403) {
                        header.toLogin();
                    }
                })
        },
        //查看更多回答
        getMoreAnswer: function () {
            this.page_answer.num ++;
            this.getAnswer();
        },
        //写回答按钮
        writerAnswer: function () {
            if (user.id > 0) {
                this.editor_flag = this.editor_flag === 1 ? 0 : 1;
            } else {
                header.toLogin();
            }
        },

        //点击展开评论
        openComment: function (index) {
            this.user = user;
            console.log("comment");
            let answer = this.answers[index];
            if (this.answers[index].openComment === 1) {
                //收起
                Vue.set(answer, 'openComment', 0)
            } else {
                //展开
                //设置评论的初始值
                Vue.set(answer, 'comment', {list: [], editor: 0});
                Vue.set(answer, 'openComment', 1);
                //加载评论
                this.getComments(answer);
            }
        },

        //获取回答下的评论
        getComments: function (answer) {
            answer.loading = true;
            let _t = this;
            //清空当前显示的评论
            Vue.set(answer, 'comments', {});
            axios.get(base + '/comments/comment/' + answer.id,
                {
                    params: {
                        offset: (answer.page.num - 1) * (answer.page.limit),
                        limit: answer.page.limit
                    }
                })
                .then(function (value) {
                    if (value.data.status === 200) {
                        //获取评论成功
                        answer.loading = false;
                        Vue.set(answer, 'comments', value.data.data.list);
                        //设置评论分页属性
                        answer.commentCount = value.data.data.total;
                        Vue.set(answer, 'page', {
                            next: value.data.data.isLastPage, //是否最后一页
                            offset: answer.page.offset,//开始数
                            limit: 10, //每页条数
                            total: value.data.data.total, //数据总数
                            pages: value.data.data.pages,//数据页数
                            num: answer.page.num,//当前页
                        });
                        for (let i = 0; i < answer.comments.length; i++) {
                            let comment = answer.comments[i];
                            //增加评论的回复框
                            Vue.set(comment, 'editor', {flag: 0, content: ''});
                            //增加回复的加载状态
                            Vue.set(comment, 'loading', false);
                            //初始化评论下回复的分页属性
                            Vue.set(comment, 'page', {
                                next: false,//是否最后一页
                                offset: 0,  //开始数
                                limit: 10,//每页条数
                                total: answer.comments[i].commentCount,//数据总数
                                pages: 0,//数据页数
                                num: 0,//当前页
                            });
                            //添加回复的属性
                            for (let j = 0; j < comment.replies.length; j++) {
                                //回复输入框
                                Vue.set(comment.replies[j], 'editor', {flag: 0, content: ''});
                            }
                        }
                    } else {
                        //获取评论失败
                    }
                })
        },
        //查看下一页的评论
        nextAnswerPage: function (answer) {
            //回到评论顶部
            document.getElementById("answer_" + answer.id).scrollIntoView();
            answer.page.num += 1;
            this.getComments(answer);
        },
        //查看上一页评论
        lastAnswerPage: function (answer) {
            //回到评论顶部
            document.getElementById("answer_" + answer.id).scrollIntoView();
            answer.page.num -= 1;
            this.getComments(answer);
        },
        //发表回答下的评论
        postComment: function (answer) {
            if (answer.comment_content === '') {
                return;
            }
            let _t = this;
            let data = new FormData;
            data.append('content', answer.comment_content);
            axios.post(base + '/comments/comment/' + answer.id, data)
                .then(function (value) {
                    _t.editor_flag = 0;
                    let code = value.data.status;
                    if (code === 200) {
                        //评论成功
                        document.getElementById("answer_" + answer.id).scrollIntoView();
                        //回到评论的第一页
                        answer.page.num = 1;
                        _t.getComments(answer);
                        answer.comment_content = '';
                    } else if (code === 403) {
                        header.toLogin();
                    }
                })
        },

        //展开评论回复框
        openCommentEditor: function (comment) {
            if (comment.editor.flag === 1) {
                Vue.set(comment.editor, 'flag', 0);
            } else {
                Vue.set(comment.editor, 'flag', 1);
            }
        },

        //发表评论的回复
        submitReply: function (comment) {
            if (comment.editor.content === '') {
                return;
            }
            let _t = this;
            let data = new FormData;
            data.append('content', comment.editor.content);
            axios.post(base + '/comments/' + comment.id + '/reply/' + comment.userId, data)
                .then(function (value) {
                    Vue.set(comment.editor, 'flag', 0);
                    let code = value.data.status;
                    if (code === 200) {
                        //回复成功
                        //重新加载评论下的回复
                        _t.getReplies(comment);
                        //清空回复输入框
                        comment.editor.content = '';
                    } else if (code === 403) {
                        header.toLogin();
                    }
                })
        },
        //发表回复的回复
        replyToReply: function (comment, reply) {
            if (reply.editor.content === '') {
                return;
            }
            let _t = this;
            let data = new FormData;
            data.append('content', reply.editor.content);
            axios.post(base + '/comments/' + reply.commentId + '/reply/' + reply.userId, data)
                .then(function (value) {
                    Vue.set(reply.editor, 'flag', 0);
                    let code = value.data.status;
                    if (code === 200) {
                        //回复成功
                        //重新加载评论下的回复
                        _t.getReplies(comment);
                        //清空回复输入框
                        reply.editor.content = '';
                    } else if (code === 403) {
                        header.toLogin();
                    }
                })
        },
        //获取评论下的回复
        getReplies: function (comment) {
            comment.loading = true;
            let _t = this;
            axios.get(base + '/comments/' + comment.id + '/reply',
                {
                    params: {
                        offset: comment.page.offset,
                        limit: comment.page.limit
                    }
                })
                .then(function (value) {
                    if (value.data.status === 200) {
                        //获取回复成功
                        comment.loading = false;
                        //添加回复数据到Vue对象
                        let replies = value.data.data;
                        for (let i = 0; i < replies.length; i++) {
                            replies[i].editor = {flag: 0, content: ''};
                        }
                        Vue.set(comment, 'replies', replies);

                    } else {
                        //获取回复失败

                    }
                })
        },
        handleScroll: function () {
            if (getScrollBottomHeight() <= app.bottomHight &&
                app.page_answer.num < app.page_answer.pages &&
                app.page_answer.loading === false) {
                app.getMoreAnswer();
            }
        }
    },
    created: function () {
        this.getAnswer();
    }
});

//文本编辑器设置
// 自定义菜单配置
editor.customConfig.menus = [
    'head',  // 标题
    'bold',  // 粗体
    'fontSize',  // 字号
    'link',  // 插入链接
    'list',  // 列表
    'justify',  // 对齐方式
    'emoticon',  // 表情
    'image',  // 插入图片
    'table',  // 表格
    'code',  // 插入代码
    'undo',  // 撤销
    'redo'  // 重复
];
//用户操作（鼠标点击、键盘打字等）导致的内容变化之后，会自动触发此函数并获取富文本中的所有内容
editor.customConfig.onchange = (html) => {
    app.answers_content = html;
};
editor.customConfig.zIndex = 1;    //防止富文本编辑器被别的内容所覆盖
editor.create();


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