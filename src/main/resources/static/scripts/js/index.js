


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
            next: false
        }
    },
    methods: {
        //加载问题列表
        getQuestions: function (offset, limit, orderBy) {
            this.next = false;
            let _t = this;
            axios.get(base + '/questions', {
                params: {
                    offset: offset,
                    limit: limit,
                    orderBy: orderBy,
                    desc: 1
                }
            }).then(function (value) {
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
            this.offset = (this.offset + 1) * this.limit;
            this.getQuestions(this.offset, this.limit,this.orderBy);
        }
    },
    created: function () {
        // 初始化列表加载10条数据
        this.getQuestions(this.offset, this.limit, this.orderBy);
    }
});