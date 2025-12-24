<template>
	<view class="container">
		<form @submit.prevent="addQuestion">
			<view class="form-group">
				<label for="qtype">题目类型</label>
				<select v-model="question.qtype" id="qtype" required>
					<option value="1">单选题</option>
					<option value="2">多选题</option>
					<option value="3">是非题</option>
				</select>
			</view>
			<view class="form-group">
				<label for="qmajor">专业</label>
				<input v-model="question.qmajor" id="qmajor" type="text" placeholder="专业" required />
			</view>
			<view class="form-group" v-if="question.qtype !== 3">
				<label for="qcontent">题目内容</label>
				<input v-model="question.qcontent" id="qcontent" type="text" placeholder="题目内容" required />
			</view>
			<view class="form-group" v-if="question.qtype !== 3">
				<label for="optionA">选项A</label>
				<input v-model="question.qchoice[0]" id="optionA" type="text" placeholder="选项A" required />
			</view>
			<view class="form-group" v-if="question.qtype !== 3">
				<label for="optionB">选项B</label>
				<input v-model="question.qchoice[1]" id="optionB" type="text" placeholder="选项B" required />
			</view>
			<view class="form-group" v-if="question.qtype !== 3">
				<label for="optionC">选项C</label>
				<input v-model="question.qchoice[2]" id="optionC" type="text" placeholder="选项C" required />
			</view>
			<view class="form-group" v-if="question.qtype !== 3">
				<label for="optionD">选项D</label>
				<input v-model="question.qchoice[3]" id="optionD" type="text" placeholder="选项D" required />
			</view>
			<view class="form-group">
				<label for="qanswer">答案</label>
				<input v-model="question.qanswer" id="qanswer" type="text"
					:placeholder="question.qtype === 3 ? '答案（true/false）' : '答案（A/B/C/D）'" required />
			</view>
			<view class="form-group">
				<label for="qimg">图片</label>
				<input type="file" id="qimg" @change="handleFileUpload" />
			</view>
			<view class="actions">
				<button type="submit">提交</button>
			</view>
		</form>
		<p v-if="message">{{ message }}</p>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				question: {
					qtype: 1,
					qmajor: '',
					qcontent: '',
					qchoice: ['', '', '', ''],
					qanswer: '',
					qimg: null
				},
				message: ''
			};
		},
		methods: {
			handleFileUpload(event) {
				this.question.qimg = event.target.files[0];
			},
			async addQuestion() {
				const token = localStorage.getItem('JWT');
				const formData = new FormData();
				formData.append('qtype', this.question.qtype);
				formData.append('qmajor', this.question.qmajor);
				formData.append('qcontent', this.question.qcontent);
				formData.append('qanswer', this.question.qanswer);
				formData.append('qimg', this.question.qimg);
				this.question.qchoice.forEach((choice, index) => {
					formData.append(`qchoice[${index}]`, choice);
				});

				if (token) {
					try {
						const res = await uni.request({
							method: 'POST',
							url: 'http://localhost:8080/Question/qInsert',
							header: {
								'Authorization': token,
								'Content-Type': 'multipart/form-data'
							},
							data: formData
						});

						if (res[1].data.success === 1) {
							this.message = '试题添加成功';
							this.clearForm();
						} else {
							this.message = '试题添加失败，请重试';
							console.error('试题添加失败:', res[1].data.data);
						}
					} catch (err) {
						this.message = '试题添加失败，请重试';
						console.error('请求失败:', err);
					}
				}
			},
			clearForm() {
				this.question = {
					qtype: 1,
					qmajor: '',
					qcontent: '',
					qchoice: ['', '', '', ''],
					qanswer: '',
					qimg: null
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
		flex-direction: column;
		justify-content: center;
		align-items: center;
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