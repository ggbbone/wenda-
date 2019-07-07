let headder = new Vue({
    el: "#header",
    data() {
        return {
            name:null
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
        }
    },
    created: function () {
    }
});