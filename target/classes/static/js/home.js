
let app = new Vue({
    el: '#app',
    methods: {
        submit() {
            this.addEntry(this.x, this.y, this.r)
        },
        shoot(x, y, r) {
            this.addEntry(x, y, r)
        },
        addEntry(x, y, r) {
            let that = this
            $.ajax({
                url:`/add_history_item?x=${x}&y=${y}&r=${r}`,
                type: "GET",
                contentType:"application/json; charset=utf-8",
                success: function (data) {
                    let entry = [data.x.toFixed(2), data.y.toFixed(2), data.y.toFixed(2), data.isHit]
                    that.history = [entry].concat(that.history)
                },
                error: function (error) {
                    alert(error.responseText)
                }
            })
        },
        loadHistory() {
            let that = this
            $.ajax({
                url:"/get_history",
                type: "GET",
                contentType:"application/json; charset=utf-8",
                success: function (data) {
                    for (let row of data) {
                        let entry = [row.x.toFixed(2), row.y.toFixed(2), row.y.toFixed(2), row.isHit]
                        that.history = [entry].concat(that.history)
                    }
                }
            })
        }
    },
    watch: {
        r: function (r, _) {
            canvas.setAreaRadius(r)
        },
        history: function (history, _) {
            canvas.setHistory(history)
        }
    },
    created() {
        this.loadHistory()
    },
    data: {
        x: 0,
        y: 0,
        r: 1,
        history: [],
    },
    components: {
        'multicheckbox': {
            props: {
                value: Number,
                options: Array,
            },
            template: `
                <div>
                    <label v-for="option in options">
                        <span>
                            <label>{{ option }}</label>
                            <input
                                type="checkbox"
                                @change="$emit('input', option == value ? null : option)"
                                v-bind:checked="option == value">
                        </span>
                    </label>
                </div>
            `,
        }
    },
})

let canvas = new CoordinatesCanvas('canvas',
    45,
    -2,
    2,
    -5,
    3,
    app.shoot
)
canvas.setAreaRadius(app.$data.r)