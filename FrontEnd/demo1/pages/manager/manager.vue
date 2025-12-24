<template>
	<view class="container">
		<!-- 筛选属性栏 -->
		<view class="attributes">
			<view class="details">
				<text>题型：</text>
				<select v-model="selectedType" @change="filterQuestions">
					<!-- 题型筛选 -->
					<option value="">全部</option>
					<option value="1">单选题</option>
					<option value="2">多选题</option>
					<option value="3">是非题</option>
				</select>
				<text>科目：</text>
				<select v-model="selectedSubject" @change="filterQuestions">
					<!-- 科目筛选 -->
					<option value="">全部</option>
					<option value="计算机科学">计算机科学</option>
					<option value="数学">数学</option>
					<option value="语文">语文</option>
					<option value="英语">英语</option>
				</select>
			</view>
			<view class="add">
				<navigator url="/pages/add_problem/add_problem">
					<button>新增</button>
				</navigator>
			</view>
		</view>

		<!-- 题目列表 -->
		<view class="question-list">
			<view v-for="question in paginatedQuestions" :key="question.qid" class="question">
				<view class="question-details">
					<checkbox-group v-model="selectedQuestionIds">
						<checkbox :value="String(question.qid)"></checkbox>
					</checkbox-group>
					<view class="question-information">
						<text><strong>题目：</strong>{{ question.qcontent.split('/')[0] }}</text>
						<text><strong>题型：</strong>{{ question.qtype }}</text>
						<text><strong>科目：</strong>{{ question.qmajor }}</text>
					</view>
					<view class="actions">
						<button @tap="editQuestion(question)">编辑</button>
						<button @tap="deleteQuestion(question.qid)">删除</button>
					</view>
				</view>
			</view>
		</view>

		<view class="down">
			<view class="select-all">
				<checkbox @change="selectAll">全选</checkbox>
			</view>
			<view class="delete-selected">
				<button @tap="deleteSelected">删除选中</button>
			</view>
		</view>

		<!-- 分页控件 -->
		<view class="pagination">
			<button @tap="prevPage" :disabled="currentPage === 1">上一页</button>
			<text>{{ currentPage }} / {{ totalPages }}</text>
			<button @tap="nextPage" :disabled="currentPage === totalPages">下一页</button>
		</view>

		<!-- 编辑弹窗 -->
		<view v-if="showEditModal" class="edit-modal">
			<form @submit.prevent="updateQuestion">
				<view class="form-group">
					<label for="edit-title">题目</label>
					<input type="text" id="edit-title" v-model="editQuestionData.qcontent" required />
				</view>
				<view v-if="editQuestionData.qtype !== 3" class="form-group">
					<label for="edit-optionA">选项A</label>
					<input type="text" id="edit-optionA" v-model="editQuestionData.qchoice[0]" required />
				</view>
				<view v-if="editQuestionData.qtype !== 3" class="form-group">
					<label for="edit-optionB">选项B</label>
					<input type="text" id="edit-optionB" v-model="editQuestionData.qchoice[1]" required />
				</view>
				<view v-if="editQuestionData.qtype !== 3" class="form-group">
					<label for="edit-optionC">选项C</label>
					<input type="text" id="edit-optionC" v-model="editQuestionData.qchoice[2]" required />
				</view>
				<view v-if="editQuestionData.qtype !== 3" class="form-group">
					<label for="edit-optionD">选项D</label>
					<input type="text" id="edit-optionD" v-model="editQuestionData.qchoice[3]" required />
				</view>
				<view class="form-group">
					<label for="edit-answer">答案</label>
					<input type="text" id="edit-answer" v-model="editQuestionData.qanswer" required />
				</view>
				<view class="actions">
					<button type="button" @click="cancelEdit()">取消</button>
					<button type="submit">保存</button>
				</view>
			</form>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				questions: [],
				filteredQuestions: [],
				selectedType: '',
				selectedSubject: '',
				currentPage: 1,
				pageSize: 3,
				selectedQuestionIds: [],
				allSelected: false,
				showEditModal: false,
				editQuestionData: {}
			};
		},
		mounted() {
			this.fetchQuestions();
		},
		computed: {
			paginatedQuestions() {
				const startIndex = (this.currentPage - 1) * this.pageSize;
				return this.filteredQuestions.slice(startIndex, startIndex + this.pageSize);
			},
			totalPages() {
				return Math.ceil(this.filteredQuestions.length / this.pageSize);
			}
		},
		methods: {
			fetchQuestions() {
				const token = localStorage.getItem('JWT');
				if (token) {
					uni.request({
						method: 'GET',
						url: 'http://localhost:8080/Question/qSelect',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT":localStorage.getItem("JWT")
						},
						success: (res) => {
							if (res.data.success === 1) {
							    console.log("questions", res.data.data);
							    this.questions = res.data.data.map(question => {
							        // 如果 qcontent 为 null 或 undefined，设置默认值为空字符串
							        const qcontent = question.qcontent || '';
							        console.log("qcontent", qcontent);
							
							        // 如果 qcontent 为空，则 qchoices 为空数组
							        const [content, ...qchoices] = qcontent ? qcontent.split('/') : [qcontent];
							
							        return {
							            ...question,
							            qcontent: content,
							            qchoice: qchoices.length ? qchoices : question.qchoice // 保留原有的 qchoice，如果分割后为空
							        };
							    });
							    this.filteredQuestions = this.questions;
							}else {
								console.error('获取题目失败:', res.data.data);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			filterQuestions() {
				this.filteredQuestions = this.questions.filter(question => {
					const typeMatch = this.selectedType ? question.qtype === Number(this.selectedType) : true;
					const subjectMatch = this.selectedSubject ? question.qsubject === this.selectedSubject : true;
					return typeMatch && subjectMatch;
				});
			},
			selectAll(event) {
				if (event.detail.value.length) {
					this.selectedQuestionIds = this.filteredQuestions.map(question => String(question.qid));
				} else {
					this.selectedQuestionIds = [];
				}
			},
			deleteSelected() {
				const token = localStorage.getItem('JWT');
				if (token && this.selectedQuestionIds.length > 0) {
					uni.request({
						method: 'POST',
						url: 'http://localhost:8080/Question/qDelete',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT":localStorage.getItem("JWT")
						},
						data: JSON.stringify({
							qids: this.selectedQuestionIds
						}),
						success: (res) => {
							if (res.data.success === 1) {
								this.questions = this.questions.filter(
									question => !this.selectedQuestionIds.includes(String(question.qid))
								);
								this.filterQuestions();
								this.selectedQuestionIds = [];
							} else {
								console.error('删除题目失败:', res.data.data);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			editQuestion(question) {
				this.editQuestionData = {
					...question,
					qcontent: `${question.qcontent}/${question.qchoice.join('/')}`
				};
				this.showEditModal = true;
			},
			deleteQuestion(qid) {
				const token = localStorage.getItem('JWT');
				if (token) {
					uni.request({
						method: 'POST',
						url: 'http://localhost:8080/Question/qDelete',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT":localStorage.getItem("JWT")
						},
						data: JSON.stringify({
							qids: [String(qid)]
						}),
						success: (res) => {
							if (res.data.success === 1) {
								this.questions = this.questions.filter(q => q.qid !== qid);
								this.filterQuestions();
							} else {
								console.error('删除题目失败:', res.data.data);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			updateQuestion() {
				const token = localStorage.getItem('JWT');
				if (token) {
					const updateData = {
						...this.editQuestionData,
						qcontent: `${this.editQuestionData.qcontent}/${this.editQuestionData.qchoice.join('/')}`
					};
					uni.request({
						method: 'POST',
						url: 'http://localhost:8080/Question/qUpdate',
						header: {
							'Authorization': token,
							'Content-Type': 'application/json',
							"JWT":localStorage.getItem("JWT")
						},
						data: JSON.stringify(updateData),
						success: (res) => {
							if (res.data.success === 1) {
								const index = this.questions.findIndex(q => q.qid === this.editQuestionData
									.qid);
								if (index !== -1) {
									this.questions.splice(index, 1, {
										...this.editQuestionData,
										qcontent: this.editQuestionData.qcontent.split('/')[0],
										qchoice: this.editQuestionData.qcontent.split('/').slice(1)
									});
									this.filterQuestions();
									this.showEditModal = false;
								}
							} else {
								console.error('更新题目失败:', res.data.data);
							}
						},
						fail: (err) => {
							console.error('请求失败:', err);
						}
					});
				}
			},
			cancelEdit() {
				this.showEditModal = false;
			},
			nextPage() {
				if (this.currentPage < this.totalPages) {
					this.currentPage++;
				}
			},
			prevPage() {
				if (this.currentPage > 1) {
					this.currentPage--;
				}
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
		justify-content: space-between;
	}

	.attributes {
		display: flex;
		justify-content: space-between;
		margin-bottom: 20px;
	}

	.details {
		display: flex;
		align-items: center;
	}

	.details text,
	.details select {
		margin-right: 10px;
	}

	.add button {
		width: 100px;
		height: 30px;
		border: none;
		border-radius: 10px;
		background-color: #3aacf8;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
	}

	.add button:hover {
		background-color: #2a8cd6;
	}

	.attributes select {
		padding: 5px;
		border-radius: 5px;
	}

	.question-list {
		flex: 1;
		overflow-y: auto;
	}

	.question {
		border: 1px solid #ccc;
		padding: 10px;
		margin-bottom: 10px;
		background-color: rgba(255, 255, 255, 0.8);
		border-radius: 10px;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.question-details {
		display: flex;
		align-items: center;
	}

	.question-information {
		flex: 1;
		margin-left: 10px;
	}

	.actions {
		display: flex;
		align-items: center;
	}

	.actions button {
		width: 60px;
		height: 30px;
		border: none;
		border-radius: 10px;
		background-color: #3498db;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
		margin-left: 10px;
	}

	.actions button:hover {
		background-color: #2980b9;
	}

	.down {
		display: flex;
		justify-content: space-between;
		margin-bottom: 20px;
	}

	.select-all {
		display: flex;
		align-items: center;
	}

	.delete-selected button {
		width: 100px;
		height: 30px;
		border: none;
		border-radius: 10px;
		background-color: #e74c3c;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
	}

	.delete-selected button:hover {
		background-color: #c0392b;
	}

	.pagination {
		display: flex;
		justify-content: space-around;
	}

	.pagination button {
		width: 80px;
		height: 30px;
		border: none;
		border-radius: 10px;
		background-color: #3aacf8;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
	}

	.pagination button:hover {
		background-color: #2a8cd6;
	}

	/* 编辑弹窗样式 */
	.edit-modal {
		position: fixed;
		top: 50%;
		left: 50%;
		transform: translate(-50%, -50%);
		background: rgba(255, 255, 255, 0.95);
		border-radius: 10px;
		padding: 20px;
		box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
		max-width: 400px;
		width: 90%;
		z-index: 1000;
		/* 保证弹窗在最上层 */
	}

	.edit-modal form {
		display: flex;
		flex-direction: column;
	}

	.edit-modal .form-group {
		margin-bottom: 15px;
		display: flex;
		flex-direction: column;
	}

	.edit-modal label {
		font-weight: bold;
		margin-bottom: 5px;
	}

	.edit-modal input {
		width: 95%;
		padding: 8px;
		border: 1px solid #ccc;
		border-radius: 5px;
	}

	.edit-modal .actions {
		display: flex;
		justify-content: space-between;
		margin-top: 20px;
	}

	.edit-modal .actions button {
		width: 48%;
		height: 40px;
		border: none;
		border-radius: 10px;
		background-color: #3498db;
		color: #fff;
		font-size: 14px;
		cursor: pointer;
	}

	.edit-modal .actions button:hover {
		background-color: #2980b9;
	}
</style>