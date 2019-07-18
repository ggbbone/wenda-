let questionId = document.getElementById("questionId").value;

let app = new Vue({
    el: "#app",
    data() {
        return {
            user: {},
            //回答内容
            answers_content: '',
            //回答列表
            answers: [],
            //回答分页参数
            page_answer: {
                //是否最后一页
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
                num: 0,
            },

            //排序条件
            orderBy: 1,
            //启用文本编辑器
            editor: 0,
            //评论内容
            comment_content: '',
        }
    },
    methods: {
        //获取回答列表并加载
        getAnswer: function (offset, limit) {
            let _t = this;
            axios.get(base + '/comments/1/' + questionId,
                {
                    params: {
                        offset: offset,
                        limit: limit
                    }
                })
                .then(function (value) {
                    let code = value.data.status;
                    if (code === 200) {
                        let comments = value.data.data.list;
                        _t.page_answer.total = value.data.data.total;
                        for (let i = 0; i < comments.length; i++) {
                            if (comments[i].content.length > 400) {
                                comments[i].flag = 0;
                            } else {
                                comments[i].flag = -1;
                            }
                            if (comments[i].userId == user.id) {
                                comments[i].delete = 1;
                            } else {
                                comments[i].delete = 0;
                            }
                            //设置评论展开状态
                            comments[i].openComment = 0;
                            //设置评论分页属性
                            comments[i].page = {
                                //是否最后一页
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
                                num: 0,
                            };
                            _t.answers.push(comments[i]);
                        }
                        _t.next = comments.length >= limit;
                    }
                })
        },
        //提交回答
        submit: function () {
            let _t = this;
            let data = new FormData;
            data.append('content', this.answers_content);
            axios.post(base + '/comments/1/' + questionId, data)
                .then(function (value) {
                    _t.editor = 0;
                    let code = value.data.status;
                    if (code === 200) {
                        _t.answers = [];
                        _t.getAnswer(0, 5);
                        _t.comment = '';
                    } else if (code === 403) {
                        header.toLogin();
                    }
                })
        },
        //查看更多回答
        getMoreAnswer: function () {
            this.page_answer.offset = (this.page_answer.offset + 1) * this.page_answer.limit;
            this.getAnswer(this.page_answer.offset, this.page_answer.limit, this.orderBy);
        },
        //写回答按钮
        writerAnswer: function () {
            if (user.id > 0) {
                this.editor = 1;
            } else {
                header.toLogin();
            }
        },

        //点击展开评论
        openComment: function (index) {
            console.log("comment");
            let answer = this.answers[index];
            if (this.answers[index].openComment === 1) {
                //收起
                Vue.set(answer, 'openComment', 0)
            }else {
                //展开
                //设置评论的初始值
                Vue.set(answer, 'comment', {list: [],editor: 0});
                Vue.set(answer, 'openComment', 1);
                //加载评论
                this.getComments(answer);
            }
        },

        //获取回答下的评论
        getComments:function(answer){
            let _t = this;
            axios.get(base + '/comments/2/' + answer.id,
                {
                    params: {
                        offset: answer.page.offset,
                        limit: answer.page.limit
                    }
                })
                .then(function (value) {
                    if (value.data.status === 200) {
                        //获取评论成功
                        Vue.set(answer, 'comments', value.data.data.list);
                        for(let i = 0; i<answer.comments.length;i++){
                            //增加评论的回复框
                            Vue.set(answer.comments[i], 'editor', {flag :0, content:''});
                            //增加评论的回复的分页属性
                            Vue.set(answer.comments[i],'page',{
                                next: false,//是否最后一页
                                offset: 0,  //开始数
                                limit: 2,//每页条数,初始化时加载2条回复
                                total: 0,//数据总数
                                pages: 0,//数据页数
                                num: 0,//当前页
                            });
                            //初始化评论的回复
                            _t.getReplies(answer.comments[i]);
                        }
                    }else {
                        //获取评论失败
                    }
                })
        },

        //发表回答下的评论
        postComment: function (answer) {
            let _t = this;
            let data = new FormData;
            data.append('content', this.comment_content);
            axios.post(base + '/comments/2/' + answer.id, data)
                .then(function (value) {
                    _t.editor = 0;
                    let code = value.data.status;
                    if (code === 200) {
                        //评论成功
                        _t.getComments(answer)
                    } else if (code === 403) {
                        header.toLogin();
                    }
                })
        },

        //展开评论回复框
        openCommentEditor:function (comment) {
            if (comment.editor.flag === 1){
                Vue.set(comment.editor, 'flag', 0);
            } else {
                Vue.set(comment.editor, 'flag', 1);
            }
        },

        //发表评论的回复
        submitComment:function (comment) {
            let _t = this;
            let data = new FormData;
            data.append('content', comment.editor.content);
            axios.post(base + '/comments/3/' + comment.id, data)
                .then(function (value) {
                    Vue.set(comment.editor, 'flag', 0);
                    let code = value.data.status;
                    if (code === 200) {
                        //回复成功

                    } else if (code === 403) {
                        header.toLogin();
                    }
                })
        },
        //获取评论下的回复
        getReplies:function(comment){
            let _t = this;
            axios.get(base + '/comments/3/' + comment.id,
                {
                    params: {
                        offset: comment.page.offset,
                        limit: comment.page.limit
                    }
                })
                .then(function (value) {
                    if (value.data.status === 200) {
                        //获取回复成功
                        //添加回复数据到Vue对象
                        let replies = value.data.data.list;
                        for (let i = 0; i<replies.length; i++){
                            replies[i].editor = 0;
                        }
                        Vue.set(comment, 'replies', replies);

                        Vue.set(comment.page, 'total', value.data.data.total);
                    }else{
                        //获取回复失败

                    }
                })
        }
    },
    created: function () {
        this.getAnswer(this.page_answer.offset, this.page_answer.limit);
    }
});