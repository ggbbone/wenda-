//时间过滤器
Vue.filter('time', function (data, fomrmat) {
    return moment(data).format(fomrmat || 'HH:mm');
});

let app = new Vue({
    el: "#app",
    data() {
        return {
            //问题列表数据
            questions: []
        }
    },
    methods: {
        //加载问题列表
        getQuestions: function (offset, limit) {
            let _t = this;
            axios.get('/questions', {
                params: {
                    offset: offset,
                    limit: limit
                }
            }).then(function (value) {
                //查询成功
                if (value.data.status === 200) {
                    //将查询到的问题数据添加到列表
                    let question = value.data.data;
                    for (let i = 0; i < question.length; i++) {
                        _t.questions.push(question[i]);
                    }
                }
            })
        }
    },
    created: function () {
        // 初始化列表加载10条数据
        this.getQuestions(0, 10);
    }
});