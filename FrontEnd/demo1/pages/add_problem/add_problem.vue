<template>
	<view class="container">
		<view class="form-group">
			<label for="question-type">题目类型</label>
			<select v-model="question.type" id="question-type" required>
				<option value="0">单选题</option>
				<option value="1">多选题</option>
				<option value="2">是非题</option>
			</select>
		</view>
		<view class="form-group" v-if="question.type !== 'truefalse'">
			<label for="title">题目内容</label>
			<input v-model="question.title" id="title" type="text" placeholder="题目内容" required />
		</view>
		<view class="form-group" v-if="question.type !== 'truefalse'">
			<label for="optionA">选项A</label>
			<input v-model="question.optionA" id="optionA" type="text" placeholder="选项A" required />
		</view>
		<view class="form-group" v-if="question.type !== 'truefalse'">
			<label for="optionB">选项B</label>
			<input v-model="question.optionB" id="optionB" type="text" placeholder="选项B" required />
		</view>
		<view class="form-group" v-if="question.type !== 'truefalse'">
			<label for="optionC">选项C</label>
			<input v-model="question.optionC" id="optionC" type="text" placeholder="选项C" required />
		</view>
		<view class="form-group" v-if="question.type !== 'truefalse'">
			<label for="optionD">选项D</label>
			<input v-model="question.optionD" id="optionD" type="text" placeholder="选项D" required />
		</view>
		<view class="form-group" v-if="question.type === 'truefalse'">
			<label for="title">题目内容</label>
			<input v-model="question.title" id="title" type="text" placeholder="是非题仅输入题目内容" required />
		</view>
		<view class="form-group">
			<label for="answer">答案</label>
			<input v-model="question.answer" id="answer" type="text"
				:placeholder="question.type === 'truefalse' ? '答案（true/false）' : '答案（A/B/C/D）'" required />
		</view>
		<view class="form-group">
			<label for="explanation">答案解析</label>
			<textarea v-model="question.explanation" id="explanation" cols="30" rows="4"
				placeholder="输入答案解析"></textarea>
		</view>
		<view class="form-group actions">
			<button @click="addQuestion()">提交</button>
		</view>
		<p v-if="message">{{ message }}</p>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				question: {
					title: '',
					optionA: '',
					optionB: '',
					optionC: '',
					optionD: '',
					answer: '',
					type: 'single', // 默认题型为单选题
					explanation: ''
				},
				message: ''
			};
		},
		methods: {
			addQuestion() {
				// 语法审核
				if (this.question.type !== 'truefalse' && !['A', 'B', 'C', 'D'].includes(this.question.answer)) {
					this.message = '答案格式错误，请输入A/B/C/D中的一个';
					return;
				} else if (this.question.type === 'truefalse' && !['true', 'false'].includes(this.question.answer
						.toLowerCase())) {
					this.message = '答案格式错误，请输入true或false';
					return;
				}

				const token = localStorage.getItem('JWT');
				if (token) {
					uni.request({
						url: 'http://localhost:8080/Question/qInsert',
						method: 'POST',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT": localStorage.getItem("JWT")
						},
						data: this.question,
						success: (res) => {
							if (res.data.success === 1) {
								this.message = '试题添加成功';
								this.clearForm();
							} else {
								this.message = '试题添加失败，请重试';
								console.error('试题添加失败:', res.data.data);
							}
						},
						fail: (err) => {
							this.message = '试题添加失败，请重试';
							console.error('请求失败:', err);
						}
					});
				}
			},
			clearForm() {
				this.question = {
					title: '',
					optionA: '',
					optionB: '',
					optionC: '',
					optionD: '',
					answer: '',
					type: 'single',
					explanation: ''
				};
			}
		}
	};
</script>

<style scoped>
	.container {
		padding: 20px;
		background-image: url(../../static/images/restaurant.webp);
		background-size: cover;
		width: 100vw;
		height: 100vh;
		display: flex;
		justify-content: center;
		align-items: center;
		flex-direction: column;
	}

	form {
		background-color: rgba(255, 255, 255, 0.9);
		padding: 20px;
		border-radius: 10px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
		width: 100%;
		max-width: 500px;
		display: flex;
		flex-direction: column;
	}

	.form-group {
		margin-bottom: 15px;
		display: flex;
		flex-direction: column;
	}

	label {
		font-weight: bold;
		margin-bottom: 5px;
	}

	input,
	select,
	textarea {
		padding: 10px;
		border: 1px solid #ccc;
		border-radius: 5px;
		width: 100%;
	}

	.actions {
		display: flex;
		justify-content: center;
	}

	.actions button {
		width: 100%;
		padding: 10px;
		border: none;
		border-radius: 10px;
		background-color: #3498db;
		color: #fff;
		font-size: 18px;
		cursor: pointer;
	}

	.actions button:hover {
		background-color: #2980b9;
	}

	p {
		margin-top: 20px;
		color: green;
		text-align: center;
	}
</style>